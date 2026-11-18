for i = 1, 3 do io.write(i, " ") end print()
for i = 10, 1, -3 do io.write(i, " ") end print()
for i = 1, 2, 0.5 do io.write(i, " ") end print()
for i = 1.0, 3 do io.write(i, " ") end print()
local i = 0 while true do i = i + 1 if i > 5 then break end end print(i)
local n = 0 repeat local x = n n = n + 1 until x >= 3 print(n)
for i = 1, 10 do
  if i % 2 == 0 then goto continue end
  if i > 7 then break end
  io.write(i, ",")
  ::continue::
end
print()
local x = 15
if x > 20 then print("grande") elseif x > 10 then print("mediano") else print("pequeno") end
local r = x > 10 and "si" or "no" print(r)
print(nil and 1, false or nil, 1 and 2, nil or "def", not nil, not 0)
do local y = 5 print(y) end print(y)
local k = 1
::top:: if k <= 3 then io.write("k", k, " ") k = k + 1 goto top end print()
for i = 3, 1 do print("nunca") end
for _, v in ipairs({"a", "b", nil, "d"}) do io.write(v) end print()
local t = {} for i = 1, 5 do t[#t+1] = function() return i end end print(t[3]())
