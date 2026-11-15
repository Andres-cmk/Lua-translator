// Generated from C:/Users/ramir/Desktop/SEMESTRE-9/lenguajes/traductor/grammar/LuaGrammar.g4 by ANTLR 4.13.2

    package org.example;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LuaGrammar}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LuaGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#chunk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChunk(LuaGrammar.ChunkContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(LuaGrammar.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(LuaGrammar.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#attnamelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttnamelist(LuaGrammar.AttnamelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#attrib}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrib(LuaGrammar.AttribContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#retstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetstat(LuaGrammar.RetstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(LuaGrammar.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#funcname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncname(LuaGrammar.FuncnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#varlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarlist(LuaGrammar.VarlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(LuaGrammar.VarContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#namelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamelist(LuaGrammar.NamelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#explist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplist(LuaGrammar.ExplistContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(LuaGrammar.ExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#prefixexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixexp(LuaGrammar.PrefixexpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#functioncall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctioncall(LuaGrammar.FunctioncallContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(LuaGrammar.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#functiondef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctiondef(LuaGrammar.FunctiondefContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#funcbody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncbody(LuaGrammar.FuncbodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#parlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParlist(LuaGrammar.ParlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#tableconstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableconstructor(LuaGrammar.TableconstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#fieldlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldlist(LuaGrammar.FieldlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(LuaGrammar.FieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#fieldsep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldsep(LuaGrammar.FieldsepContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#binop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinop(LuaGrammar.BinopContext ctx);
	/**
	 * Visit a parse tree produced by {@link LuaGrammar#unop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnop(LuaGrammar.UnopContext ctx);
}