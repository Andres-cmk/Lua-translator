local t = {10, 20, 30, nombre = "lua", [5] = 50}
print(#t, t[1], t.nombre, t[5], t[4])
table.insert(t, 40)
table.insert(t, 1, 5)
print(table.concat(t, ","), #t)
print(table.remove(t), table.remove(t, 1), table.concat(t, ","))
local nums = {5, 2, 8, 1, 9, 3}
table.sort(nums)
print(table.concat(nums, " "))
table.sort(nums, function(a, b) return a > b end)
print(table.concat(nums, " "))
local people = {{n="Ana", e=30}, {n="Luis", e=25}, {n="Eva", e=35}, {n="Bob", e=25}}
table.sort(people, function(a, b) if a.e ~= b.e then return a.e < b.e end return a.n < b.n end)
for i, p in ipairs(people) do io.write(p.n, "(", p.e, ") ") end print()
local keys = {}
for k, v in pairs({a=1, b=2, c=3}) do keys[#keys+1] = k .. "=" .. v end
table.sort(keys) print(table.concat(keys, ","))
print(select("#", 1, nil, 3), select(2, "a", "b", "c"), select(-1, "a", "b", "c"))
print(table.unpack({1, 2, 3})) print(table.unpack({1, 2, 3}, 2))
local p = table.pack(1, nil, 3) print(p.n, p[1], p[2], p[3])
local m = {} for i = 1, 3 do m[i] = {} for j = 1, 3 do m[i][j] = i * j end end
for i = 1, 3 do print(table.concat(m[i], "\t")) end
local cnt = 0 for _ in pairs({1, 2, 3, x = 1, y = 2}) do cnt = cnt + 1 end print(cnt)
print(next({}), type(next), rawlen({1, 2}), rawequal(t, t), rawget({x = 1}, "x"))
local words = {"banana", "apple", "cherry"} table.sort(words) print(table.concat(words, ", "))
local s = {} s[1.0] = "uno" print(s[1], #s)
local arr = {1, 2, 3, 4, 5}
for i = #arr, 1, -1 do if arr[i] % 2 == 0 then table.remove(arr, i) end end
print(table.concat(arr, ","))
print(table.concat({}, ","), table.concat({1, 2.5, "x"}, "-"))
local q = {} for i = 1, 10 do q[#q + 1] = i * i end print(#q, q[10])
