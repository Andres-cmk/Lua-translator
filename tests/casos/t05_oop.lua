local Animal = {}
Animal.__index = Animal
function Animal.new(name, sound) local self = setmetatable({}, Animal) self.name = name self.sound = sound return self end
function Animal:speak() return self.name .. " dice " .. self.sound end
local Dog = setmetatable({}, {__index = Animal})
Dog.__index = Dog
function Dog.new(name) local self = Animal.new(name, "guau") return setmetatable(self, Dog) end
function Dog:fetch() return self.name .. " trae la pelota" end
local d = Dog.new("Rex")
print(d:speak(), d:fetch(), getmetatable(d) == Dog)
local V = {}
V.__index = V
V.__add = function(a, b) return V.new(a.x + b.x, a.y + b.y) end
V.__sub = function(a, b) return V.new(a.x - b.x, a.y - b.y) end
V.__mul = function(a, k) if type(a) == "number" then a, k = k, a end return V.new(a.x * k, a.y * k) end
V.__eq = function(a, b) return a.x == b.x and a.y == b.y end
V.__lt = function(a, b) return a.x < b.x end
V.__le = function(a, b) return a.x <= b.x end
V.__tostring = function(v) return "(" .. v.x .. ", " .. v.y .. ")" end
V.__len = function(v) return math.sqrt(v.x ^ 2 + v.y ^ 2) end
V.__unm = function(v) return V.new(-v.x, -v.y) end
V.__concat = function(a, b) return tostring(a) .. tostring(b) end
V.__call = function(self, k) return self.x * k end
function V.new(x, y) return setmetatable({x = x, y = y}, V) end
local a, b = V.new(1, 2), V.new(3, 4)
print(tostring(a + b), tostring(a - b), tostring(a * 2), tostring(3 * a), a == V.new(1, 2), a < b, a <= b, #b, tostring(-a))
print(a .. b, a(10))
local defaults = setmetatable({}, {__index = function(t, k) return "default_" .. k end})
print(defaults.foo, defaults.bar)
local log = {}
local proxy = setmetatable({}, {__newindex = function(t, k, v) rawset(t, k, v * 2) log[#log + 1] = k end})
proxy.a = 5 proxy.b = 10 print(proxy.a, proxy.b, #log)
local ro = setmetatable({}, {__index = {x = 1}, __newindex = function() error("tabla de solo lectura", 2) end})
print(pcall(function() ro.y = 2 end))
local Stack = {} Stack.__index = Stack
function Stack.new() return setmetatable({items = {}, size = 0}, Stack) end
function Stack:push(v) self.size = self.size + 1 self.items[self.size] = v end
function Stack:pop() local v = self.items[self.size] self.items[self.size] = nil self.size = self.size - 1 return v end
local st = Stack.new() st:push(1) st:push(2) st:push(3) print(st:pop(), st:pop(), st.size)
print(getmetatable("abc").__index == string, ("x"):rep(3))
local Account = {balance = 0}
function Account:new(o) o = o or {} setmetatable(o, self) self.__index = self return o end
function Account:deposit(v) self.balance = self.balance + v end
local acc = Account:new() acc:deposit(100) print(acc.balance, Account.balance)
local mt = {__index = function(t, k) return k * 2 end}
local dbl = setmetatable({}, mt) print(dbl[21], rawget(dbl, 21))
print(rawequal(a, V.new(1, 2)), a ~= b)
local prot = setmetatable({}, {__metatable = "protegido"})
print(getmetatable(prot), pcall(setmetatable, prot, {}))
