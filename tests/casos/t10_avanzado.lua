-- estructuras de datos y algoritmos tipicos
local function hanoi(n, a, b, c, moves) if n == 0 then return moves end hanoi(n-1, a, c, b, moves) moves[#moves+1] = a .. "->" .. c return hanoi(n-1, b, a, c, moves) end
print(table.concat(hanoi(3, "A", "B", "C", {}), " "))
local List = {} List.__index = List
function List.new() return setmetatable({head = nil, n = 0}, List) end
function List:push(v) self.head = {v = v, next = self.head} self.n = self.n + 1 end
function List:iter() local node = self.head return function() if node then local v = node.v node = node.next return v end end end
local l = List.new() for i = 1, 5 do l:push(i * i) end
for v in l:iter() do io.write(v, " ") end print(l.n)
local graph = {a = {"b", "c"}, b = {"d"}, c = {"d", "e"}, d = {"f"}, e = {"f"}, f = {}}
local function bfs(start) local visited, queue, order = {[start] = true}, {start}, {}
  while #queue > 0 do local node = table.remove(queue, 1) order[#order + 1] = node
    for _, nb in ipairs(graph[node]) do if not visited[nb] then visited[nb] = true queue[#queue + 1] = nb end end end
  return order end
print(table.concat(bfs("a"), ","))
local function qsort(t, lo, hi) lo = lo or 1 hi = hi or #t
  if lo < hi then local p, i = t[hi], lo - 1
    for j = lo, hi - 1 do if t[j] <= p then i = i + 1 t[i], t[j] = t[j], t[i] end end
    t[i + 1], t[hi] = t[hi], t[i + 1] qsort(t, lo, i) qsort(t, i + 2, hi) end return t end
print(table.concat(qsort({9, 4, 7, 1, 8, 2, 6, 3, 5}), " "))
local memo = {} local function mfib(n) if n <= 2 then return 1 end if memo[n] then return memo[n] end local r = mfib(n-1) + mfib(n-2) memo[n] = r return r end
print(mfib(80), mfib(90))
local function isPrime(n) if n < 2 then return false end for i = 2, math.floor(math.sqrt(n)) do if n % i == 0 then return false end end return true end
local primes = {} for i = 1, 50 do if isPrime(i) then primes[#primes+1] = i end end print(table.concat(primes, " "))
local function gcd(a, b) while b ~= 0 do a, b = b, a % b end return a end print(gcd(48, 18), gcd(17, 5))
local words = {} for w in ("el perro y el gato y el raton"):gmatch("%a+") do words[w] = (words[w] or 0) + 1 end
print(words.el, words.y, words.perro)
local function serialize(v, indent) indent = indent or ""
  if type(v) == "table" then local keys = {} for k in pairs(v) do keys[#keys+1] = k end
    table.sort(keys, function(a, b) return tostring(a) < tostring(b) end)
    local out = {} for _, k in ipairs(keys) do out[#out+1] = indent .. "  " .. tostring(k) .. " = " .. serialize(v[k], indent .. "  ") end
    return "{\n" .. table.concat(out, ",\n") .. "\n" .. indent .. "}" end
  return type(v) == "string" and string.format("%q", v) or tostring(v) end
print(serialize({nombre = "Ana", edad = 30, notas = {4.5, 3.8}, activo = true}))
-- upvalues compartidos
local function makePair() local n = 0 return function() n = n + 1 return n end, function() return n end end
local inc, get = makePair() inc() inc() print(get())
local x = 1 do local x = 2 print(x) end print(x)
local t = {1, 2, 3} local i = 1 i, t[i] = i + 1, 20 print(i, t[1], t[2])
print(("x"):rep(3), ("%d-%s"):format(1, "a"), #("abc"):rep(10))
print(("a.b.c"):gsub("%.", "/"), ("hello world"):gsub("o", {o = "0"}), ("abc"):gsub("", "-"))
print(("THE (quick) fox"):gsub("%f[%a]%a+", function(w) return w:lower() end))
print(string.format("%d", 2^31), 2^63 .. "", 1e15 .. "", -0.0 .. "", 7 // 2 .. "")
print(select("#", nil, nil), select("#"), table.unpack({1, nil, 3}, 1, 3))
local nested = {a = {b = {c = {d = "profundo"}}}} print(nested.a.b.c.d, nested["a"]["b"].c["d"])
for i = math.maxinteger - 2, math.maxinteger do io.write(i, " ") end print()
for i = 1, 3 do for j = 1, 3 do if j == 2 then goto next_i end io.write(i, j, " ") end ::next_i:: end print()
local s = "" for i = 1, 10 do s = s .. i .. (i < 10 and "," or "") end print(s)
print(math.max(1, 2.5), math.min(-1, -1.5), math.abs(math.mininteger), -math.mininteger)
print(string.format("%.0f %.1f %5.3s %-3d| %3d", 2.5, 0.05, "abcdef", 7, -7))
print(tostring(print):match("^builtin") ~= nil or tostring(print):match("^function") ~= nil)
print(10 // 3, 10.0 // 3, -10 // 3, 10 % -3, -10 % 3, 2^2, 10 / 2)
print(("%5.1f|%-8.3e|%G"):format(3.14159, 12345.6789, 0.000012345))
