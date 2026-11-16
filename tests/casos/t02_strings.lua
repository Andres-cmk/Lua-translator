local s = "Hola Mundo desde Lua"
print(s:upper(), s:lower(), s:len(), s:sub(1, 4), s:sub(-3), s:sub(6, -6), s:rep(2, "|"))
print(s:find("Mundo"), s:find("o", 3), s:find("xyz"), s:find("l+"), s:find(".", 1, true))
print(s:match("(%a+) (%a+)"), s:match("%d+"), ("key=value"):match("(%w+)=(%w+)"))
for w in s:gmatch("%a+") do io.write(w, ";") end print()
print(s:gsub("o", "0"), s:gsub("%a+", string.upper, 2), ("hello world"):gsub("(%w+)", "<%1>"))
print(("abc"):reverse(), ("abc"):byte(1, -1), string.char(72, 105), ("%d items"):format(3))
print(string.format("%5d|%-5d|%05d|%x|%X|%o|%e|%.3f|%10.2f|%g|%g|%g", 42, 42, 42, 255, 255, 8, 12345.678, 3.14159, 2.5, 0.0001, 1e20, 100))
print(string.format("%s %q %5.1s| %c %%", "hi", 'a "quoted"\n line', "abc", 65))
print(string.format("%-10s|%10s|", "izq", "der"), string.format("%+d %+.2f", 5, 3.14159))
print(("  trim me  "):match("^%s*(.-)%s*$") .. "!")
print(("a,b,,c"):gsub(",", ";"), ("x"):rep(0), ("abc"):sub(5), ("abc"):sub(0))
local t = {}
for k, v in string.gmatch("a=1, b=2, c=3", "(%w+)=(%w+)") do t[#t+1] = k .. ":" .. v end
print(table.concat(t, " "))
print(("hello"):find("l"), ("hello"):match(".-l"), ("[test]"):match("%[(.-)%]"), ("f(a(b)c)"):match("%b()"))
print(("THE (quick) fox"):find("%((%a+)%)"), ("hello123"):match("^%a+"), ("x = 10"):match("^(%w+)%s*=%s*(%d+)$"))
print(#"áéí", ("ñandú"):upper(), "tab\there", "nl\\n", 'single \'q\'', "\65\066\x43\u{48}")
print([[long
string]], [==[with ]] inside]==])
print(("CamelCaseString"):gsub("(%u)", " %1"), ("%d"):rep(3, ","))
print(tostring(12):len(), ("abc" .. 1 .. 2.0))
