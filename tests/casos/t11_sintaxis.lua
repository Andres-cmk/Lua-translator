print(2^-3, -2^2, not 1 == 2, 1 .. 2 .. 3, 1 < 2 == true, 2^3^2, -3 % 5, 1 + 2 * 3 - 4 / 2)
print(5 & 3 | 8, 1 << 2 + 1, 6 ~ 3 & 1, "a" .. "b" == "ab", #"abc" + 1, - - 2, not not nil)
print(1 + 2 .. "", 2 * 3 .. 4 * 5, 3 > 2 and 2 > 1, nil == false or 0)
local t = {[1]=1, [2]=2; 3, x = "y";} print(#t, t[1], t.x)
local f = function(...) return ... end print(f(1, nil, 3))
print(type(function() end), (function(...) return select('#', ...) end)(nil, nil))
local s = [==[
a]]b]==] print(s, #s)
print(#[[
x]], "\z
      continua", '\104\101\108\108\111', "\u{1F600}" == "\xF0\x9F\x98\x80")
--[[ comentario
largo ]] print("tras comentario") --[==[ otro ]==] print("ok")
local a <const> = 10 print(a)
print(0x10, 0xff, 1e2, .5, 3., 0x.8p1, 1E-2, 0xA)
local obj = {f = function(self, x) return x * 2 end} print(obj:f(4), obj.f(obj, 5))
print(("x"):upper(), #{n = 1}, ({1, 2, 3})[2], ({...})[1])
local x, y = 1 print(x, y)
local p = print p("alias")
;;; print("puntos y comas")
print(math.maxinteger // -1, math.mininteger // -1, math.mininteger % -1)
print(string.format("%q", 1/3), string.format("%q", 10), tostring(2^63) , math.tointeger(2^53))
print(8 // 3 * 3 + 8 % 3, 2^0.5 * 2^0.5 == 2, 0.1 * 3, 100 / 3)
