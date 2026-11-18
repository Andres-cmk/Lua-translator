local function fact(n) if n <= 1 then return 1 end return n * fact(n - 1) end
print(fact(5), fact(20), fact(25))
local function fib(n) if n < 2 then return n end return fib(n-1) + fib(n-2) end
print(fib(20))
local function counter()
  local c = 0
  return function() c = c + 1 return c end
end
local c1, c2 = counter(), counter()
print(c1(), c1(), c1(), c2())
local function sum(...)
  local s = 0
  for i, v in ipairs({...}) do s = s + v end
  return s, select("#", ...)
end
print(sum(1, 2, 3, 4))
local function multi() return 1, 2, 3 end
print(multi(), "x") print("x", multi()) print((multi()))
local t = {multi(), multi()} print(#t)
local function apply(f, ...) return f(...) end
print(apply(math.max, 3, 9, 2))
local fns = {}
for i = 1, 3 do fns[i] = function() return i end end
print(fns[1](), fns[2](), fns[3]())
local function loop(n, acc) if n == 0 then return acc end return loop(n - 1, acc + 1) end
print(loop(100000, 0))
local function deep(n) if n == 0 then return 0 end return 1 + deep(n - 1) end
print(deep(10000))
local compose = function(f, g) return function(...) return f(g(...)) end end
print(compose(function(x) return x * 2 end, function(x) return x + 1 end)(5))
local function map(t, f) local r = {} for i, v in ipairs(t) do r[i] = f(v) end return r end
print(table.concat(map({1, 2, 3}, function(x) return x ^ 2 end), " "))
function globalFn(a, b) return a + b end
print(globalFn(2, 3), type(globalFn))
local obj = {val = 10}
function obj.get(self) return self.val end
function obj:inc(n) self.val = self.val + (n or 1) return self end
print(obj:inc():inc(5):get())
local function varargs(...) local a, b = ... return a, b, ... end
print(varargs(1, 2, 3))
print(string.format("%d", 3.0), math.floor(-0.0))
