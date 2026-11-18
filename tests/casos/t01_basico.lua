-- aritmetica, enteros vs floats, coercion
print(1, 1.0, -0.0, 1/0, -1/0, 3/2, 7//2, 7.0//2, -7//2, 7%3, -7%3, 7%-3, 5.5%2)
print(2^10, 2^0.5, 10/2, 3 | 5, 3 & 5, 3 ~ 5, ~0, 1 << 63, 1 << 64, -1 >> 1)
print("10" + 5, "3.5" * 2, 10 .. 20, "0x10" + 0, math.maxinteger + 1 == math.mininteger)
print(100000000000000, 1e15, 1e100, 123.456, 0.1 + 0.2, 1/3, -1.5e-7, 2^53, 2^63)
print(math.type(1), math.type(1.0), math.type("1"), 3 == 3.0, math.huge, -math.huge)
print(tostring(nil), tostring(true), tonumber("  0x1F  "), tonumber("1e2"), tonumber("abc"), tonumber("z", 36), tonumber("777", 8))
print(#"hola", #{1,2,3}, #"", "a" < "b", "Z" < "a", 1 < 1.5, -2 < -1)
print(10 == "10", nil == false, 0 == -0)
print(math.floor(3.7), math.ceil(3.2), math.floor(-3.5), math.abs(-4), math.max(1, 5, 3), math.min(2.5, 1))
print(math.fmod(7, 3), math.fmod(-7, 3), math.modf(3.7), math.sqrt(16), math.tointeger(5.0), math.pi)
print(8 // 0.0, -8 // 0.0, 0/0 ~= 0/0, math.ult(1, -1))
local a, b, c = 1, 2
print(a, b, c)
a, b = b, a
print(a, b)
