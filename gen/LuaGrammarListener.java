// Generated from C:/Users/ramir/Desktop/SEMESTRE-9/lenguajes/traductor/grammar/LuaGrammar.g4 by ANTLR 4.13.2

    package org.example;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LuaGrammar}.
 */
public interface LuaGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#chunk}.
	 * @param ctx the parse tree
	 */
	void enterChunk(LuaGrammar.ChunkContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#chunk}.
	 * @param ctx the parse tree
	 */
	void exitChunk(LuaGrammar.ChunkContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(LuaGrammar.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(LuaGrammar.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(LuaGrammar.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(LuaGrammar.StatContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#attnamelist}.
	 * @param ctx the parse tree
	 */
	void enterAttnamelist(LuaGrammar.AttnamelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#attnamelist}.
	 * @param ctx the parse tree
	 */
	void exitAttnamelist(LuaGrammar.AttnamelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#attrib}.
	 * @param ctx the parse tree
	 */
	void enterAttrib(LuaGrammar.AttribContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#attrib}.
	 * @param ctx the parse tree
	 */
	void exitAttrib(LuaGrammar.AttribContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#retstat}.
	 * @param ctx the parse tree
	 */
	void enterRetstat(LuaGrammar.RetstatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#retstat}.
	 * @param ctx the parse tree
	 */
	void exitRetstat(LuaGrammar.RetstatContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(LuaGrammar.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(LuaGrammar.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#funcname}.
	 * @param ctx the parse tree
	 */
	void enterFuncname(LuaGrammar.FuncnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#funcname}.
	 * @param ctx the parse tree
	 */
	void exitFuncname(LuaGrammar.FuncnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#varlist}.
	 * @param ctx the parse tree
	 */
	void enterVarlist(LuaGrammar.VarlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#varlist}.
	 * @param ctx the parse tree
	 */
	void exitVarlist(LuaGrammar.VarlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(LuaGrammar.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(LuaGrammar.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#namelist}.
	 * @param ctx the parse tree
	 */
	void enterNamelist(LuaGrammar.NamelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#namelist}.
	 * @param ctx the parse tree
	 */
	void exitNamelist(LuaGrammar.NamelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#explist}.
	 * @param ctx the parse tree
	 */
	void enterExplist(LuaGrammar.ExplistContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#explist}.
	 * @param ctx the parse tree
	 */
	void exitExplist(LuaGrammar.ExplistContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(LuaGrammar.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(LuaGrammar.ExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#prefixexp}.
	 * @param ctx the parse tree
	 */
	void enterPrefixexp(LuaGrammar.PrefixexpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#prefixexp}.
	 * @param ctx the parse tree
	 */
	void exitPrefixexp(LuaGrammar.PrefixexpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#functioncall}.
	 * @param ctx the parse tree
	 */
	void enterFunctioncall(LuaGrammar.FunctioncallContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#functioncall}.
	 * @param ctx the parse tree
	 */
	void exitFunctioncall(LuaGrammar.FunctioncallContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(LuaGrammar.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(LuaGrammar.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#functiondef}.
	 * @param ctx the parse tree
	 */
	void enterFunctiondef(LuaGrammar.FunctiondefContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#functiondef}.
	 * @param ctx the parse tree
	 */
	void exitFunctiondef(LuaGrammar.FunctiondefContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#funcbody}.
	 * @param ctx the parse tree
	 */
	void enterFuncbody(LuaGrammar.FuncbodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#funcbody}.
	 * @param ctx the parse tree
	 */
	void exitFuncbody(LuaGrammar.FuncbodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#parlist}.
	 * @param ctx the parse tree
	 */
	void enterParlist(LuaGrammar.ParlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#parlist}.
	 * @param ctx the parse tree
	 */
	void exitParlist(LuaGrammar.ParlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#tableconstructor}.
	 * @param ctx the parse tree
	 */
	void enterTableconstructor(LuaGrammar.TableconstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#tableconstructor}.
	 * @param ctx the parse tree
	 */
	void exitTableconstructor(LuaGrammar.TableconstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#fieldlist}.
	 * @param ctx the parse tree
	 */
	void enterFieldlist(LuaGrammar.FieldlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#fieldlist}.
	 * @param ctx the parse tree
	 */
	void exitFieldlist(LuaGrammar.FieldlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#field}.
	 * @param ctx the parse tree
	 */
	void enterField(LuaGrammar.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#field}.
	 * @param ctx the parse tree
	 */
	void exitField(LuaGrammar.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#fieldsep}.
	 * @param ctx the parse tree
	 */
	void enterFieldsep(LuaGrammar.FieldsepContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#fieldsep}.
	 * @param ctx the parse tree
	 */
	void exitFieldsep(LuaGrammar.FieldsepContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#binop}.
	 * @param ctx the parse tree
	 */
	void enterBinop(LuaGrammar.BinopContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#binop}.
	 * @param ctx the parse tree
	 */
	void exitBinop(LuaGrammar.BinopContext ctx);
	/**
	 * Enter a parse tree produced by {@link LuaGrammar#unop}.
	 * @param ctx the parse tree
	 */
	void enterUnop(LuaGrammar.UnopContext ctx);
	/**
	 * Exit a parse tree produced by {@link LuaGrammar#unop}.
	 * @param ctx the parse tree
	 */
	void exitUnop(LuaGrammar.UnopContext ctx);
}