import java.util.concurrent.Semaphore;

/**
 * Corrutina de Lua implementada con un hilo de Java. Solo un hilo corre a la vez:
 * resume() despierta a la corrutina y espera hasta que esta haga yield o termine.
 */
public final class Coroutine {
    private static final long STACK_SIZE = 16L * 1024 * 1024;

    final Interpreter interp;
    final Object fn;
    String status = "suspended"; // suspended | running | normal | dead

    private Thread thread;
    private final Semaphore toCoroutine = new Semaphore(0);
    private final Semaphore toResumer = new Semaphore(0);
    private Object[] transfer = Lua.NONE;
    private LuaError error;
    private boolean finished;

    // estado del interprete propio de esta corrutina
    private Interpreter.Scope savedScope;
    private Interpreter.CallState callState = new Interpreter.CallState();
    private Coroutine previous;
    // estado de quien hizo resume mientras esta corrutina corre (para el recolector)
    Interpreter.Scope resumerScope;
    Interpreter.CallState resumerState;

    Interpreter.Scope savedScope() {
        return savedScope;
    }

    Interpreter.CallState callState() {
        return callState;
    }

    Coroutine previous() {
        return previous;
    }

    Coroutine(Interpreter interp, Object fn) {
        this.interp = interp;
        this.fn = fn;
    }

    /** Devuelve {true, valores...} o {false, error}. */
    Object[] resume(Object[] args) {
        if ("dead".equals(status)) return new Object[] {Boolean.FALSE, "cannot resume dead coroutine"};
        if (!"suspended".equals(status)) {
            return new Object[] {Boolean.FALSE, "cannot resume non-suspended coroutine"};
        }
        Coroutine prev = interp.currentCoroutine;
        if (prev != null) prev.status = "normal";
        previous = prev;
        Interpreter.Scope resumerScope = interp.scope;
        Interpreter.CallState resumerState = interp.state;
        this.resumerScope = resumerScope;
        this.resumerState = resumerState;

        status = "running";
        interp.currentCoroutine = this;
        interp.state = callState;
        interp.scope = savedScope;
        transfer = args;
        if (thread == null) {
            thread = new Thread(null, this::body, "coroutine", STACK_SIZE);
            thread.setDaemon(true);
            thread.start();
        } else {
            toCoroutine.release();
        }
        toResumer.acquireUninterruptibly();

        // de vuelta en quien llamo a resume
        interp.currentCoroutine = prev;
        interp.state = resumerState;
        interp.scope = resumerScope;
        if (prev != null) prev.status = "running";

        if (error != null) {
            LuaError e = error;
            error = null;
            return new Object[] {Boolean.FALSE, e.value};
        }
        Object[] vals = transfer;
        Object[] r = new Object[vals.length + 1];
        r[0] = Boolean.TRUE;
        System.arraycopy(vals, 0, r, 1, vals.length);
        return r;
    }

    private void body() {
        try {
            transfer = Lua.call(fn, transfer);
        } catch (LuaError e) {
            error = e;
        } catch (StackOverflowError e) {
            error = new LuaError("stack overflow");
        } catch (RuntimeException e) {
            error = new LuaError(String.valueOf(e));
        }
        finished = true;
        status = "dead";
        toResumer.release();
    }

    /** Llamado desde dentro de la corrutina. */
    Object[] yield(Object[] values) {
        savedScope = interp.scope;
        callState = interp.state;
        transfer = values;
        status = "suspended";
        toResumer.release();
        toCoroutine.acquireUninterruptibly();
        // reanudada: resume() ya restauro scope/state de esta corrutina
        return transfer;
    }

    boolean isFinished() {
        return finished;
    }
}
