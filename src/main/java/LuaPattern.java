/**
 * Patrones de Lua (port del matcher de lstrlib.c de Lua 5.4).
 */
final class LuaPattern {
    static final int MAXCAPTURES = 32;
    static final int CAP_UNFINISHED = -1;
    static final int CAP_POSITION = -2;
    static final char L_ESC = '%';
    static final String SPECIALS = "^$*+?.([%-";
    private static final int MAXCCALLS = 200;

    final String src;
    final String pat;
    int level;
    final int[] capStart = new int[MAXCAPTURES];
    final int[] capLen = new int[MAXCAPTURES];
    int matchdepth;

    LuaPattern(String src, String pat) {
        this.src = src;
        this.pat = pat;
    }

    static LuaError err(String msg) {
        return new LuaError(msg, true);
    }

    void reprep() {
        level = 0;
        matchdepth = MAXCCALLS;
    }

    private int classEnd(int p) {
        if (p >= pat.length()) throw err("malformed pattern (ends with '%')");
        char c = pat.charAt(p++);
        if (c == L_ESC) {
            if (p >= pat.length()) throw err("malformed pattern (ends with '%')");
            return p + 1;
        }
        if (c == '[') {
            if (p < pat.length() && pat.charAt(p) == '^') p++;
            do {
                if (p >= pat.length()) throw err("malformed pattern (missing ']')");
                char cc = pat.charAt(p++);
                if (cc == L_ESC && p < pat.length()) p++;
            } while (p >= pat.length() || pat.charAt(p) != ']');
            return p + 1;
        }
        return p;
    }

    static boolean isalpha(int c) { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'); }
    static boolean isdigit(int c) { return c >= '0' && c <= '9'; }
    static boolean islower(int c) { return c >= 'a' && c <= 'z'; }
    static boolean isupper(int c) { return c >= 'A' && c <= 'Z'; }
    static boolean isspace(int c) { return c == ' ' || (c >= '\t' && c <= '\r'); }
    static boolean iscntrl(int c) { return c < 32 || c == 127; }
    static boolean ispunct(int c) { return c > 32 && c < 127 && !isalpha(c) && !isdigit(c); }
    static boolean isxdigit(int c) { return isdigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'); }
    static boolean isgraph(int c) { return c > 32 && c < 127; }

    static boolean matchClass(int c, int cl) {
        boolean res;
        switch (Character.toLowerCase(cl)) {
            case 'a': res = isalpha(c); break;
            case 'c': res = iscntrl(c); break;
            case 'd': res = isdigit(c); break;
            case 'g': res = isgraph(c); break;
            case 'l': res = islower(c); break;
            case 'p': res = ispunct(c); break;
            case 's': res = isspace(c); break;
            case 'u': res = isupper(c); break;
            case 'w': res = isalpha(c) || isdigit(c); break;
            case 'x': res = isxdigit(c); break;
            default: return cl == c;
        }
        if (isupper(cl)) res = !res;
        return res;
    }

    private boolean matchBracketClass(int c, int p, int ec) {
        boolean sig = true;
        if (pat.charAt(p + 1) == '^') {
            sig = false;
            p++;
        }
        while (++p < ec) {
            char pc = pat.charAt(p);
            if (pc == L_ESC) {
                p++;
                if (matchClass(c, pat.charAt(p))) return sig;
            } else if (p + 2 < ec && pat.charAt(p + 1) == '-') {
                if (pat.charAt(p) <= c && c <= pat.charAt(p + 2)) return sig;
                p += 2;
            } else if (pc == c) {
                return sig;
            }
        }
        return !sig;
    }

    private boolean singleMatch(int s, int p, int ep) {
        if (s >= src.length()) return false;
        int c = src.charAt(s);
        char pc = pat.charAt(p);
        switch (pc) {
            case '.': return true;
            case L_ESC: return matchClass(c, pat.charAt(p + 1));
            case '[': return matchBracketClass(c, p, ep - 1);
            default: return pc == c;
        }
    }

    /** Devuelve el indice final del match o -1. */
    int match(int s, int p) {
        if (matchdepth-- == 0) throw err("pattern too complex");
        try {
            while (true) {
                if (p >= pat.length()) return s;
                char pc = pat.charAt(p);
                switch (pc) {
                    case '(':
                        if (p + 1 < pat.length() && pat.charAt(p + 1) == ')') return startCapture(s, p + 2, CAP_POSITION);
                        return startCapture(s, p + 1, CAP_UNFINISHED);
                    case ')':
                        return endCapture(s, p + 1);
                    case '$':
                        if (p + 1 == pat.length()) return (s == src.length()) ? s : -1;
                        break;
                    case L_ESC: {
                        if (p + 1 < pat.length()) {
                            char nc = pat.charAt(p + 1);
                            if (nc == 'b') {
                                s = matchBalance(s, p + 2);
                                if (s != -1) {
                                    p += 4;
                                    continue;
                                }
                                return -1;
                            } else if (nc == 'f') {
                                p += 2;
                                if (p >= pat.length() || pat.charAt(p) != '[') {
                                    throw err("missing '[' after '%f' in pattern");
                                }
                                int ep = classEnd(p);
                                int prev = (s == 0) ? 0 : src.charAt(s - 1);
                                int cur = (s < src.length()) ? src.charAt(s) : 0;
                                if (!matchBracketClass(prev, p, ep - 1) && matchBracketClass(cur, p, ep - 1)) {
                                    p = ep;
                                    continue;
                                }
                                return -1;
                            } else if (isdigit(nc)) {
                                s = matchCapture(s, nc);
                                if (s != -1) {
                                    p += 2;
                                    continue;
                                }
                                return -1;
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
                int ep = classEnd(p);
                char epc = ep < pat.length() ? pat.charAt(ep) : '\0';
                if (!singleMatch(s, p, ep)) {
                    if (epc == '*' || epc == '?' || epc == '-') {
                        p = ep + 1;
                        continue;
                    }
                    return -1;
                }
                switch (epc) {
                    case '?': {
                        int res = match(s + 1, ep + 1);
                        if (res != -1) return res;
                        p = ep + 1;
                        continue;
                    }
                    case '+':
                        return maxExpand(s + 1, p, ep);
                    case '*':
                        return maxExpand(s, p, ep);
                    case '-':
                        return minExpand(s, p, ep);
                    default:
                        s++;
                        p = ep;
                        continue;
                }
            }
        } finally {
            matchdepth++;
        }
    }

    private int maxExpand(int s, int p, int ep) {
        int i = 0;
        while (singleMatch(s + i, p, ep)) i++;
        while (i >= 0) {
            int res = match(s + i, ep + 1);
            if (res != -1) return res;
            i--;
        }
        return -1;
    }

    private int minExpand(int s, int p, int ep) {
        while (true) {
            int res = match(s, ep + 1);
            if (res != -1) return res;
            if (singleMatch(s, p, ep)) s++;
            else return -1;
        }
    }

    private int startCapture(int s, int p, int what) {
        if (level >= MAXCAPTURES) throw err("too many captures");
        capStart[level] = s;
        capLen[level] = what;
        level++;
        int res = match(s, p);
        if (res == -1) level--;
        return res;
    }

    private int endCapture(int s, int p) {
        int l = captureToClose();
        capLen[l] = s - capStart[l];
        int res = match(s, p);
        if (res == -1) capLen[l] = CAP_UNFINISHED;
        return res;
    }

    private int captureToClose() {
        int lv = level;
        for (lv--; lv >= 0; lv--) {
            if (capLen[lv] == CAP_UNFINISHED) return lv;
        }
        throw err("invalid pattern capture");
    }

    private int matchBalance(int s, int p) {
        if (p + 1 >= pat.length()) throw err("malformed pattern (missing arguments to '%b')");
        if (s >= src.length() || src.charAt(s) != pat.charAt(p)) return -1;
        char b = pat.charAt(p), e = pat.charAt(p + 1);
        int cont = 1;
        while (++s < src.length()) {
            char c = src.charAt(s);
            if (c == e) {
                if (--cont == 0) return s + 1;
            } else if (c == b) {
                cont++;
            }
        }
        return -1;
    }

    private int matchCapture(int s, char l) {
        int idx = checkCapture(l);
        int len = capLen[idx];
        if (src.length() - s >= len && src.regionMatches(capStart[idx], src, s, len)) return s + len;
        return -1;
    }

    private int checkCapture(char l) {
        int idx = l - '1';
        if (idx < 0 || idx >= level || capLen[idx] == CAP_UNFINISHED) {
            throw err("invalid capture index %" + (idx + 1));
        }
        return idx;
    }

    /** Valor de la captura i (o el match completo si no hay capturas e i == 0). */
    Object getCapture(int i, int s, int e) {
        if (i >= level) {
            if (i != 0) throw err("invalid capture index %" + (i + 1));
            return src.substring(s, e);
        }
        int l = capLen[i];
        if (l == CAP_UNFINISHED) throw err("unfinished capture");
        if (l == CAP_POSITION) return (long) (capStart[i] + 1);
        return src.substring(capStart[i], capStart[i] + l);
    }

    Object[] getCaptures(int s, int e, boolean wholeIfNone) {
        int n = (level == 0 && wholeIfNone) ? 1 : level;
        Object[] r = new Object[n];
        for (int i = 0; i < n; i++) r[i] = getCapture(i, s, e);
        return r;
    }

    static boolean noSpecials(String p) {
        for (int i = 0; i < p.length(); i++) {
            if (SPECIALS.indexOf(p.charAt(i)) >= 0) return false;
        }
        return true;
    }
}
