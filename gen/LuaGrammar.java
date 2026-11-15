// Generated from C:/Users/ramir/Desktop/SEMESTRE-9/lenguajes/traductor/grammar/LuaGrammar.g4 by ANTLR 4.13.2

    package org.example;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class LuaGrammar extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SEMI=1, EQ=2, BREAK=3, GOTO=4, DO=5, END=6, WHILE=7, REPEAT=8, UNTIL=9, 
		IF=10, THEN=11, ELSEIF=12, ELSE=13, FOR=14, COMMA=15, IN=16, FUNCTION=17, 
		LOCAL=18, LT=19, GT=20, RETURN=21, CC=22, NIL=23, FALSE=24, TRUE=25, DOT=26, 
		SQUIG=27, MINUS=28, POUND=29, OP=30, CP=31, NOT=32, LL=33, GG=34, AMP=35, 
		SS=36, PER=37, COL=38, LE=39, GE=40, AND=41, OR=42, PLUS=43, STAR=44, 
		OCU=45, CCU=46, OB=47, CB=48, EE=49, DD=50, PIPE=51, CARET=52, SLASH=53, 
		DDD=54, SQEQ=55, NAME=56, NUMBER=57, STRING=58, COMMENT=59, WS=60;
	public static final int
		RULE_chunk = 0, RULE_block = 1, RULE_stat = 2, RULE_attnamelist = 3, RULE_attrib = 4, 
		RULE_retstat = 5, RULE_label = 6, RULE_funcname = 7, RULE_varlist = 8, 
		RULE_var = 9, RULE_namelist = 10, RULE_explist = 11, RULE_exp = 12, RULE_prefixexp = 13, 
		RULE_functioncall = 14, RULE_args = 15, RULE_functiondef = 16, RULE_funcbody = 17, 
		RULE_parlist = 18, RULE_tableconstructor = 19, RULE_fieldlist = 20, RULE_field = 21, 
		RULE_fieldsep = 22, RULE_binop = 23, RULE_unop = 24;
	private static String[] makeRuleNames() {
		return new String[] {
			"chunk", "block", "stat", "attnamelist", "attrib", "retstat", "label", 
			"funcname", "varlist", "var", "namelist", "explist", "exp", "prefixexp", 
			"functioncall", "args", "functiondef", "funcbody", "parlist", "tableconstructor", 
			"fieldlist", "field", "fieldsep", "binop", "unop"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'='", "'break'", "'goto'", "'do'", "'end'", "'while'", 
			"'repeat'", "'until'", "'if'", "'then'", "'elseif'", "'else'", "'for'", 
			"','", "'in'", "'function'", "'local'", "'<'", "'>'", "'return'", "'::'", 
			"'nil'", "'false'", "'true'", "'.'", "'~'", "'-'", "'#'", "'('", "')'", 
			"'not'", "'<<'", "'>>'", "'&'", "'//'", "'%'", "':'", "'<='", "'>='", 
			"'and'", "'or'", "'+'", "'*'", "'{'", "'}'", "'['", "']'", "'=='", "'..'", 
			"'|'", "'^'", "'/'", "'...'", "'~='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SEMI", "EQ", "BREAK", "GOTO", "DO", "END", "WHILE", "REPEAT", 
			"UNTIL", "IF", "THEN", "ELSEIF", "ELSE", "FOR", "COMMA", "IN", "FUNCTION", 
			"LOCAL", "LT", "GT", "RETURN", "CC", "NIL", "FALSE", "TRUE", "DOT", "SQUIG", 
			"MINUS", "POUND", "OP", "CP", "NOT", "LL", "GG", "AMP", "SS", "PER", 
			"COL", "LE", "GE", "AND", "OR", "PLUS", "STAR", "OCU", "CCU", "OB", "CB", 
			"EE", "DD", "PIPE", "CARET", "SLASH", "DDD", "SQEQ", "NAME", "NUMBER", 
			"STRING", "COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "LuaGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LuaGrammar(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ChunkContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ChunkContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chunk; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterChunk(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitChunk(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitChunk(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChunkContext chunk() throws RecognitionException {
		ChunkContext _localctx = new ChunkContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_chunk);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public RetstatContext retstat() {
			return getRuleContext(RetstatContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 72057595116275130L) != 0)) {
				{
				{
				setState(52);
				stat();
				}
				}
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RETURN) {
				{
				setState(58);
				retstat();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(LuaGrammar.SEMI, 0); }
		public VarlistContext varlist() {
			return getRuleContext(VarlistContext.class,0);
		}
		public TerminalNode EQ() { return getToken(LuaGrammar.EQ, 0); }
		public ExplistContext explist() {
			return getRuleContext(ExplistContext.class,0);
		}
		public FunctioncallContext functioncall() {
			return getRuleContext(FunctioncallContext.class,0);
		}
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public TerminalNode BREAK() { return getToken(LuaGrammar.BREAK, 0); }
		public TerminalNode GOTO() { return getToken(LuaGrammar.GOTO, 0); }
		public TerminalNode NAME() { return getToken(LuaGrammar.NAME, 0); }
		public TerminalNode DO() { return getToken(LuaGrammar.DO, 0); }
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public TerminalNode END() { return getToken(LuaGrammar.END, 0); }
		public TerminalNode WHILE() { return getToken(LuaGrammar.WHILE, 0); }
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode REPEAT() { return getToken(LuaGrammar.REPEAT, 0); }
		public TerminalNode UNTIL() { return getToken(LuaGrammar.UNTIL, 0); }
		public TerminalNode IF() { return getToken(LuaGrammar.IF, 0); }
		public List<TerminalNode> THEN() { return getTokens(LuaGrammar.THEN); }
		public TerminalNode THEN(int i) {
			return getToken(LuaGrammar.THEN, i);
		}
		public List<TerminalNode> ELSEIF() { return getTokens(LuaGrammar.ELSEIF); }
		public TerminalNode ELSEIF(int i) {
			return getToken(LuaGrammar.ELSEIF, i);
		}
		public TerminalNode ELSE() { return getToken(LuaGrammar.ELSE, 0); }
		public TerminalNode FOR() { return getToken(LuaGrammar.FOR, 0); }
		public List<TerminalNode> COMMA() { return getTokens(LuaGrammar.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LuaGrammar.COMMA, i);
		}
		public NamelistContext namelist() {
			return getRuleContext(NamelistContext.class,0);
		}
		public TerminalNode IN() { return getToken(LuaGrammar.IN, 0); }
		public TerminalNode FUNCTION() { return getToken(LuaGrammar.FUNCTION, 0); }
		public FuncnameContext funcname() {
			return getRuleContext(FuncnameContext.class,0);
		}
		public FuncbodyContext funcbody() {
			return getRuleContext(FuncbodyContext.class,0);
		}
		public TerminalNode LOCAL() { return getToken(LuaGrammar.LOCAL, 0); }
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitStat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitStat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		StatContext _localctx = new StatContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_stat);
		int _la;
		try {
			setState(142);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(62);
				varlist();
				setState(63);
				match(EQ);
				setState(64);
				explist();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(66);
				functioncall();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(67);
				label();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(68);
				match(BREAK);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(69);
				match(GOTO);
				setState(70);
				match(NAME);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(71);
				match(DO);
				setState(72);
				block();
				setState(73);
				match(END);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(75);
				match(WHILE);
				setState(76);
				exp(0);
				setState(77);
				match(DO);
				setState(78);
				block();
				setState(79);
				match(END);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(81);
				match(REPEAT);
				setState(82);
				block();
				setState(83);
				match(UNTIL);
				setState(84);
				exp(0);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(86);
				match(IF);
				setState(87);
				exp(0);
				setState(88);
				match(THEN);
				setState(89);
				block();
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELSEIF) {
					{
					{
					setState(90);
					match(ELSEIF);
					setState(91);
					exp(0);
					setState(92);
					match(THEN);
					setState(93);
					block();
					}
					}
					setState(99);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(100);
					match(ELSE);
					setState(101);
					block();
					}
				}

				setState(104);
				match(END);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(106);
				match(FOR);
				setState(107);
				match(NAME);
				setState(108);
				match(EQ);
				setState(109);
				exp(0);
				setState(110);
				match(COMMA);
				setState(111);
				exp(0);
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(112);
					match(COMMA);
					setState(113);
					exp(0);
					}
				}

				setState(116);
				match(DO);
				setState(117);
				block();
				setState(118);
				match(END);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(120);
				match(FOR);
				setState(121);
				namelist();
				setState(122);
				match(IN);
				setState(123);
				explist();
				setState(124);
				match(DO);
				setState(125);
				block();
				setState(126);
				match(END);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(128);
				match(FUNCTION);
				setState(129);
				funcname();
				setState(130);
				funcbody();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(132);
				match(LOCAL);
				setState(133);
				match(FUNCTION);
				setState(134);
				match(NAME);
				setState(135);
				funcbody();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(136);
				match(LOCAL);
				setState(137);
				namelist();
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EQ) {
					{
					setState(138);
					match(EQ);
					setState(139);
					explist();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttnamelistContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(LuaGrammar.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(LuaGrammar.NAME, i);
		}
		public List<AttribContext> attrib() {
			return getRuleContexts(AttribContext.class);
		}
		public AttribContext attrib(int i) {
			return getRuleContext(AttribContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(LuaGrammar.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LuaGrammar.COMMA, i);
		}
		public AttnamelistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attnamelist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterAttnamelist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitAttnamelist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitAttnamelist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttnamelistContext attnamelist() throws RecognitionException {
		AttnamelistContext _localctx = new AttnamelistContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_attnamelist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(NAME);
			setState(145);
			attrib();
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(146);
				match(COMMA);
				setState(147);
				match(NAME);
				setState(148);
				attrib();
				}
				}
				setState(153);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttribContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(LuaGrammar.LT, 0); }
		public TerminalNode NAME() { return getToken(LuaGrammar.NAME, 0); }
		public TerminalNode GT() { return getToken(LuaGrammar.GT, 0); }
		public AttribContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attrib; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterAttrib(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitAttrib(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitAttrib(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttribContext attrib() throws RecognitionException {
		AttribContext _localctx = new AttribContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_attrib);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(154);
				match(LT);
				setState(155);
				match(NAME);
				setState(156);
				match(GT);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RetstatContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(LuaGrammar.RETURN, 0); }
		public ExplistContext explist() {
			return getRuleContext(ExplistContext.class,0);
		}
		public RetstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_retstat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterRetstat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitRetstat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitRetstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RetstatContext retstat() throws RecognitionException {
		RetstatContext _localctx = new RetstatContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_retstat);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(RETURN);
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 522452747514150912L) != 0)) {
				{
				setState(160);
				explist();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelContext extends ParserRuleContext {
		public List<TerminalNode> CC() { return getTokens(LuaGrammar.CC); }
		public TerminalNode CC(int i) {
			return getToken(LuaGrammar.CC, i);
		}
		public TerminalNode NAME() { return getToken(LuaGrammar.NAME, 0); }
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(CC);
			setState(164);
			match(NAME);
			setState(165);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncnameContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(LuaGrammar.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(LuaGrammar.NAME, i);
		}
		public List<TerminalNode> DOT() { return getTokens(LuaGrammar.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(LuaGrammar.DOT, i);
		}
		public TerminalNode COL() { return getToken(LuaGrammar.COL, 0); }
		public FuncnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterFuncname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitFuncname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitFuncname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncnameContext funcname() throws RecognitionException {
		FuncnameContext _localctx = new FuncnameContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_funcname);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(NAME);
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(168);
				match(DOT);
				setState(169);
				match(NAME);
				}
				}
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COL) {
				{
				setState(175);
				match(COL);
				setState(176);
				match(NAME);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarlistContext extends ParserRuleContext {
		public List<VarContext> var() {
			return getRuleContexts(VarContext.class);
		}
		public VarContext var(int i) {
			return getRuleContext(VarContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(LuaGrammar.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LuaGrammar.COMMA, i);
		}
		public VarlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterVarlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitVarlist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitVarlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarlistContext varlist() throws RecognitionException {
		VarlistContext _localctx = new VarlistContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_varlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			var();
			setState(184);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(180);
				match(COMMA);
				setState(181);
				var();
				}
				}
				setState(186);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(LuaGrammar.NAME, 0); }
		public PrefixexpContext prefixexp() {
			return getRuleContext(PrefixexpContext.class,0);
		}
		public TerminalNode OB() { return getToken(LuaGrammar.OB, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode CB() { return getToken(LuaGrammar.CB, 0); }
		public TerminalNode DOT() { return getToken(LuaGrammar.DOT, 0); }
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_var);
		try {
			setState(197);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(187);
				match(NAME);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(188);
				prefixexp(0);
				setState(189);
				match(OB);
				setState(190);
				exp(0);
				setState(191);
				match(CB);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(193);
				prefixexp(0);
				setState(194);
				match(DOT);
				setState(195);
				match(NAME);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamelistContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(LuaGrammar.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(LuaGrammar.NAME, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(LuaGrammar.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LuaGrammar.COMMA, i);
		}
		public NamelistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namelist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterNamelist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitNamelist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitNamelist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamelistContext namelist() throws RecognitionException {
		NamelistContext _localctx = new NamelistContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_namelist);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(NAME);
			setState(204);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(200);
					match(COMMA);
					setState(201);
					match(NAME);
					}
					} 
				}
				setState(206);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExplistContext extends ParserRuleContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(LuaGrammar.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LuaGrammar.COMMA, i);
		}
		public ExplistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterExplist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitExplist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitExplist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExplistContext explist() throws RecognitionException {
		ExplistContext _localctx = new ExplistContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_explist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			exp(0);
			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(208);
				match(COMMA);
				setState(209);
				exp(0);
				}
				}
				setState(214);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpContext extends ParserRuleContext {
		public TerminalNode NIL() { return getToken(LuaGrammar.NIL, 0); }
		public TerminalNode FALSE() { return getToken(LuaGrammar.FALSE, 0); }
		public TerminalNode TRUE() { return getToken(LuaGrammar.TRUE, 0); }
		public TerminalNode NUMBER() { return getToken(LuaGrammar.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(LuaGrammar.STRING, 0); }
		public TerminalNode DDD() { return getToken(LuaGrammar.DDD, 0); }
		public FunctiondefContext functiondef() {
			return getRuleContext(FunctiondefContext.class,0);
		}
		public PrefixexpContext prefixexp() {
			return getRuleContext(PrefixexpContext.class,0);
		}
		public TableconstructorContext tableconstructor() {
			return getRuleContext(TableconstructorContext.class,0);
		}
		public UnopContext unop() {
			return getRuleContext(UnopContext.class,0);
		}
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public BinopContext binop() {
			return getRuleContext(BinopContext.class,0);
		}
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		return exp(0);
	}

	private ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState);
		ExpContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_exp, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NIL:
				{
				setState(216);
				match(NIL);
				}
				break;
			case FALSE:
				{
				setState(217);
				match(FALSE);
				}
				break;
			case TRUE:
				{
				setState(218);
				match(TRUE);
				}
				break;
			case NUMBER:
				{
				setState(219);
				match(NUMBER);
				}
				break;
			case STRING:
				{
				setState(220);
				match(STRING);
				}
				break;
			case DDD:
				{
				setState(221);
				match(DDD);
				}
				break;
			case FUNCTION:
				{
				setState(222);
				functiondef();
				}
				break;
			case OP:
			case NAME:
				{
				setState(223);
				prefixexp(0);
				}
				break;
			case OCU:
				{
				setState(224);
				tableconstructor();
				}
				break;
			case SQUIG:
			case MINUS:
			case POUND:
			case NOT:
				{
				setState(225);
				unop();
				setState(226);
				exp(1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(236);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp);
					setState(230);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(231);
					binop();
					setState(232);
					exp(3);
					}
					} 
				}
				setState(238);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixexpContext extends ParserRuleContext {
		public TerminalNode OP() { return getToken(LuaGrammar.OP, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode CP() { return getToken(LuaGrammar.CP, 0); }
		public TerminalNode NAME() { return getToken(LuaGrammar.NAME, 0); }
		public PrefixexpContext prefixexp() {
			return getRuleContext(PrefixexpContext.class,0);
		}
		public TerminalNode OB() { return getToken(LuaGrammar.OB, 0); }
		public TerminalNode CB() { return getToken(LuaGrammar.CB, 0); }
		public TerminalNode DOT() { return getToken(LuaGrammar.DOT, 0); }
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public TerminalNode COL() { return getToken(LuaGrammar.COL, 0); }
		public PrefixexpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixexp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterPrefixexp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitPrefixexp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitPrefixexp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixexpContext prefixexp() throws RecognitionException {
		return prefixexp(0);
	}

	private PrefixexpContext prefixexp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PrefixexpContext _localctx = new PrefixexpContext(_ctx, _parentState);
		PrefixexpContext _prevctx = _localctx;
		int _startState = 26;
		enterRecursionRule(_localctx, 26, RULE_prefixexp, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OP:
				{
				setState(240);
				match(OP);
				setState(241);
				exp(0);
				setState(242);
				match(CP);
				}
				break;
			case NAME:
				{
				setState(244);
				match(NAME);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(263);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(261);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
					case 1:
						{
						_localctx = new PrefixexpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_prefixexp);
						setState(247);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(248);
						match(OB);
						setState(249);
						exp(0);
						setState(250);
						match(CB);
						}
						break;
					case 2:
						{
						_localctx = new PrefixexpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_prefixexp);
						setState(252);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(253);
						match(DOT);
						setState(254);
						match(NAME);
						}
						break;
					case 3:
						{
						_localctx = new PrefixexpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_prefixexp);
						setState(255);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(256);
						args();
						}
						break;
					case 4:
						{
						_localctx = new PrefixexpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_prefixexp);
						setState(257);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(258);
						match(COL);
						setState(259);
						match(NAME);
						setState(260);
						args();
						}
						break;
					}
					} 
				}
				setState(265);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctioncallContext extends ParserRuleContext {
		public PrefixexpContext prefixexp() {
			return getRuleContext(PrefixexpContext.class,0);
		}
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public TerminalNode COL() { return getToken(LuaGrammar.COL, 0); }
		public TerminalNode NAME() { return getToken(LuaGrammar.NAME, 0); }
		public FunctioncallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functioncall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterFunctioncall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitFunctioncall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitFunctioncall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctioncallContext functioncall() throws RecognitionException {
		FunctioncallContext _localctx = new FunctioncallContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_functioncall);
		try {
			setState(274);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(266);
				prefixexp(0);
				setState(267);
				args();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(269);
				prefixexp(0);
				setState(270);
				match(COL);
				setState(271);
				match(NAME);
				setState(272);
				args();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgsContext extends ParserRuleContext {
		public TerminalNode OP() { return getToken(LuaGrammar.OP, 0); }
		public TerminalNode CP() { return getToken(LuaGrammar.CP, 0); }
		public ExplistContext explist() {
			return getRuleContext(ExplistContext.class,0);
		}
		public TableconstructorContext tableconstructor() {
			return getRuleContext(TableconstructorContext.class,0);
		}
		public TerminalNode STRING() { return getToken(LuaGrammar.STRING, 0); }
		public ArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_args; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgsContext args() throws RecognitionException {
		ArgsContext _localctx = new ArgsContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_args);
		int _la;
		try {
			setState(283);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OP:
				enterOuterAlt(_localctx, 1);
				{
				setState(276);
				match(OP);
				setState(278);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 522452747514150912L) != 0)) {
					{
					setState(277);
					explist();
					}
				}

				setState(280);
				match(CP);
				}
				break;
			case OCU:
				enterOuterAlt(_localctx, 2);
				{
				setState(281);
				tableconstructor();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(282);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctiondefContext extends ParserRuleContext {
		public TerminalNode FUNCTION() { return getToken(LuaGrammar.FUNCTION, 0); }
		public FuncbodyContext funcbody() {
			return getRuleContext(FuncbodyContext.class,0);
		}
		public FunctiondefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functiondef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterFunctiondef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitFunctiondef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitFunctiondef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctiondefContext functiondef() throws RecognitionException {
		FunctiondefContext _localctx = new FunctiondefContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_functiondef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			match(FUNCTION);
			setState(286);
			funcbody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncbodyContext extends ParserRuleContext {
		public TerminalNode OP() { return getToken(LuaGrammar.OP, 0); }
		public TerminalNode CP() { return getToken(LuaGrammar.CP, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode END() { return getToken(LuaGrammar.END, 0); }
		public ParlistContext parlist() {
			return getRuleContext(ParlistContext.class,0);
		}
		public FuncbodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcbody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterFuncbody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitFuncbody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitFuncbody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncbodyContext funcbody() throws RecognitionException {
		FuncbodyContext _localctx = new FuncbodyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_funcbody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(OP);
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DDD || _la==NAME) {
				{
				setState(289);
				parlist();
				}
			}

			setState(292);
			match(CP);
			setState(293);
			block();
			setState(294);
			match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParlistContext extends ParserRuleContext {
		public NamelistContext namelist() {
			return getRuleContext(NamelistContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(LuaGrammar.COMMA, 0); }
		public TerminalNode DDD() { return getToken(LuaGrammar.DDD, 0); }
		public ParlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterParlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitParlist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitParlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParlistContext parlist() throws RecognitionException {
		ParlistContext _localctx = new ParlistContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_parlist);
		int _la;
		try {
			setState(302);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				namelist();
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(297);
					match(COMMA);
					setState(298);
					match(DDD);
					}
				}

				}
				break;
			case DDD:
				enterOuterAlt(_localctx, 2);
				{
				setState(301);
				match(DDD);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableconstructorContext extends ParserRuleContext {
		public TerminalNode OCU() { return getToken(LuaGrammar.OCU, 0); }
		public TerminalNode CCU() { return getToken(LuaGrammar.CCU, 0); }
		public FieldlistContext fieldlist() {
			return getRuleContext(FieldlistContext.class,0);
		}
		public TableconstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableconstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterTableconstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitTableconstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitTableconstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableconstructorContext tableconstructor() throws RecognitionException {
		TableconstructorContext _localctx = new TableconstructorContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_tableconstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			match(OCU);
			setState(306);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 522593485002506240L) != 0)) {
				{
				setState(305);
				fieldlist();
				}
			}

			setState(308);
			match(CCU);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldlistContext extends ParserRuleContext {
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public List<FieldsepContext> fieldsep() {
			return getRuleContexts(FieldsepContext.class);
		}
		public FieldsepContext fieldsep(int i) {
			return getRuleContext(FieldsepContext.class,i);
		}
		public FieldlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterFieldlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitFieldlist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitFieldlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldlistContext fieldlist() throws RecognitionException {
		FieldlistContext _localctx = new FieldlistContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_fieldlist);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			field();
			setState(316);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(311);
					fieldsep();
					setState(312);
					field();
					}
					} 
				}
				setState(318);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			setState(320);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI || _la==COMMA) {
				{
				setState(319);
				fieldsep();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(LuaGrammar.OB, 0); }
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode CB() { return getToken(LuaGrammar.CB, 0); }
		public TerminalNode EQ() { return getToken(LuaGrammar.EQ, 0); }
		public TerminalNode NAME() { return getToken(LuaGrammar.NAME, 0); }
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_field);
		try {
			setState(332);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(322);
				match(OB);
				setState(323);
				exp(0);
				setState(324);
				match(CB);
				setState(325);
				match(EQ);
				setState(326);
				exp(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				match(NAME);
				setState(329);
				match(EQ);
				setState(330);
				exp(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(331);
				exp(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldsepContext extends ParserRuleContext {
		public TerminalNode COMMA() { return getToken(LuaGrammar.COMMA, 0); }
		public TerminalNode SEMI() { return getToken(LuaGrammar.SEMI, 0); }
		public FieldsepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldsep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterFieldsep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitFieldsep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitFieldsep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldsepContext fieldsep() throws RecognitionException {
		FieldsepContext _localctx = new FieldsepContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_fieldsep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			_la = _input.LA(1);
			if ( !(_la==SEMI || _la==COMMA) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BinopContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(LuaGrammar.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(LuaGrammar.MINUS, 0); }
		public TerminalNode STAR() { return getToken(LuaGrammar.STAR, 0); }
		public TerminalNode SLASH() { return getToken(LuaGrammar.SLASH, 0); }
		public TerminalNode CARET() { return getToken(LuaGrammar.CARET, 0); }
		public TerminalNode PER() { return getToken(LuaGrammar.PER, 0); }
		public TerminalNode DD() { return getToken(LuaGrammar.DD, 0); }
		public TerminalNode LT() { return getToken(LuaGrammar.LT, 0); }
		public TerminalNode LE() { return getToken(LuaGrammar.LE, 0); }
		public TerminalNode GT() { return getToken(LuaGrammar.GT, 0); }
		public TerminalNode GE() { return getToken(LuaGrammar.GE, 0); }
		public TerminalNode EE() { return getToken(LuaGrammar.EE, 0); }
		public TerminalNode SQEQ() { return getToken(LuaGrammar.SQEQ, 0); }
		public TerminalNode AND() { return getToken(LuaGrammar.AND, 0); }
		public TerminalNode OR() { return getToken(LuaGrammar.OR, 0); }
		public BinopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterBinop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitBinop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitBinop(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinopContext binop() throws RecognitionException {
		BinopContext _localctx = new BinopContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_binop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 51263218086576128L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnopContext extends ParserRuleContext {
		public TerminalNode MINUS() { return getToken(LuaGrammar.MINUS, 0); }
		public TerminalNode NOT() { return getToken(LuaGrammar.NOT, 0); }
		public TerminalNode POUND() { return getToken(LuaGrammar.POUND, 0); }
		public TerminalNode SQUIG() { return getToken(LuaGrammar.SQUIG, 0); }
		public UnopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).enterUnop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LuaGrammarListener ) ((LuaGrammarListener)listener).exitUnop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LuaGrammarVisitor ) return ((LuaGrammarVisitor<? extends T>)visitor).visitUnop(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnopContext unop() throws RecognitionException {
		UnopContext _localctx = new UnopContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_unop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 5234491392L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return exp_sempred((ExpContext)_localctx, predIndex);
		case 13:
			return prefixexp_sempred((PrefixexpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean prefixexp_sempred(PrefixexpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 2);
		case 4:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001<\u0155\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0005\u00016\b\u0001\n\u0001\f\u0001"+
		"9\t\u0001\u0001\u0001\u0003\u0001<\b\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002`\b\u0002\n\u0002\f\u0002c\t\u0002\u0001"+
		"\u0002\u0001\u0002\u0003\u0002g\b\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0003\u0002s\b\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u008d\b\u0002\u0003"+
		"\u0002\u008f\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0005\u0003\u0096\b\u0003\n\u0003\f\u0003\u0099\t\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0003\u0004\u009e\b\u0004\u0001\u0005\u0001\u0005"+
		"\u0003\u0005\u00a2\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00ab\b\u0007\n\u0007"+
		"\f\u0007\u00ae\t\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u00b2\b\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0005\b\u00b7\b\b\n\b\f\b\u00ba\t\b\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003"+
		"\t\u00c6\b\t\u0001\n\u0001\n\u0001\n\u0005\n\u00cb\b\n\n\n\f\n\u00ce\t"+
		"\n\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u00d3\b\u000b\n\u000b"+
		"\f\u000b\u00d6\t\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00e5"+
		"\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00eb\b\f\n\f\f\f\u00ee\t"+
		"\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u00f6\b\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u0106\b\r\n\r\f\r\u0109\t\r"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0003\u000e\u0113\b\u000e\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u0117\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f"+
		"\u011c\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0003\u0011\u0123\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u012c\b\u0012\u0001\u0012"+
		"\u0003\u0012\u012f\b\u0012\u0001\u0013\u0001\u0013\u0003\u0013\u0133\b"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0005\u0014\u013b\b\u0014\n\u0014\f\u0014\u013e\t\u0014\u0001\u0014"+
		"\u0003\u0014\u0141\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0003\u0015\u014d\b\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0000\u0002\u0018\u001a\u0019\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.0\u0000\u0003\u0002\u0000\u0001\u0001\u000f\u000f\u0007"+
		"\u0000\u0013\u0014\u001c\u001c%%\',124577\u0002\u0000\u001b\u001d  \u0174"+
		"\u00002\u0001\u0000\u0000\u0000\u00027\u0001\u0000\u0000\u0000\u0004\u008e"+
		"\u0001\u0000\u0000\u0000\u0006\u0090\u0001\u0000\u0000\u0000\b\u009d\u0001"+
		"\u0000\u0000\u0000\n\u009f\u0001\u0000\u0000\u0000\f\u00a3\u0001\u0000"+
		"\u0000\u0000\u000e\u00a7\u0001\u0000\u0000\u0000\u0010\u00b3\u0001\u0000"+
		"\u0000\u0000\u0012\u00c5\u0001\u0000\u0000\u0000\u0014\u00c7\u0001\u0000"+
		"\u0000\u0000\u0016\u00cf\u0001\u0000\u0000\u0000\u0018\u00e4\u0001\u0000"+
		"\u0000\u0000\u001a\u00f5\u0001\u0000\u0000\u0000\u001c\u0112\u0001\u0000"+
		"\u0000\u0000\u001e\u011b\u0001\u0000\u0000\u0000 \u011d\u0001\u0000\u0000"+
		"\u0000\"\u0120\u0001\u0000\u0000\u0000$\u012e\u0001\u0000\u0000\u0000"+
		"&\u0130\u0001\u0000\u0000\u0000(\u0136\u0001\u0000\u0000\u0000*\u014c"+
		"\u0001\u0000\u0000\u0000,\u014e\u0001\u0000\u0000\u0000.\u0150\u0001\u0000"+
		"\u0000\u00000\u0152\u0001\u0000\u0000\u000023\u0003\u0002\u0001\u0000"+
		"3\u0001\u0001\u0000\u0000\u000046\u0003\u0004\u0002\u000054\u0001\u0000"+
		"\u0000\u000069\u0001\u0000\u0000\u000075\u0001\u0000\u0000\u000078\u0001"+
		"\u0000\u0000\u00008;\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u0000"+
		":<\u0003\n\u0005\u0000;:\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000"+
		"<\u0003\u0001\u0000\u0000\u0000=\u008f\u0005\u0001\u0000\u0000>?\u0003"+
		"\u0010\b\u0000?@\u0005\u0002\u0000\u0000@A\u0003\u0016\u000b\u0000A\u008f"+
		"\u0001\u0000\u0000\u0000B\u008f\u0003\u001c\u000e\u0000C\u008f\u0003\f"+
		"\u0006\u0000D\u008f\u0005\u0003\u0000\u0000EF\u0005\u0004\u0000\u0000"+
		"F\u008f\u00058\u0000\u0000GH\u0005\u0005\u0000\u0000HI\u0003\u0002\u0001"+
		"\u0000IJ\u0005\u0006\u0000\u0000J\u008f\u0001\u0000\u0000\u0000KL\u0005"+
		"\u0007\u0000\u0000LM\u0003\u0018\f\u0000MN\u0005\u0005\u0000\u0000NO\u0003"+
		"\u0002\u0001\u0000OP\u0005\u0006\u0000\u0000P\u008f\u0001\u0000\u0000"+
		"\u0000QR\u0005\b\u0000\u0000RS\u0003\u0002\u0001\u0000ST\u0005\t\u0000"+
		"\u0000TU\u0003\u0018\f\u0000U\u008f\u0001\u0000\u0000\u0000VW\u0005\n"+
		"\u0000\u0000WX\u0003\u0018\f\u0000XY\u0005\u000b\u0000\u0000Ya\u0003\u0002"+
		"\u0001\u0000Z[\u0005\f\u0000\u0000[\\\u0003\u0018\f\u0000\\]\u0005\u000b"+
		"\u0000\u0000]^\u0003\u0002\u0001\u0000^`\u0001\u0000\u0000\u0000_Z\u0001"+
		"\u0000\u0000\u0000`c\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000"+
		"ab\u0001\u0000\u0000\u0000bf\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000"+
		"\u0000de\u0005\r\u0000\u0000eg\u0003\u0002\u0001\u0000fd\u0001\u0000\u0000"+
		"\u0000fg\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hi\u0005\u0006"+
		"\u0000\u0000i\u008f\u0001\u0000\u0000\u0000jk\u0005\u000e\u0000\u0000"+
		"kl\u00058\u0000\u0000lm\u0005\u0002\u0000\u0000mn\u0003\u0018\f\u0000"+
		"no\u0005\u000f\u0000\u0000or\u0003\u0018\f\u0000pq\u0005\u000f\u0000\u0000"+
		"qs\u0003\u0018\f\u0000rp\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000"+
		"st\u0001\u0000\u0000\u0000tu\u0005\u0005\u0000\u0000uv\u0003\u0002\u0001"+
		"\u0000vw\u0005\u0006\u0000\u0000w\u008f\u0001\u0000\u0000\u0000xy\u0005"+
		"\u000e\u0000\u0000yz\u0003\u0014\n\u0000z{\u0005\u0010\u0000\u0000{|\u0003"+
		"\u0016\u000b\u0000|}\u0005\u0005\u0000\u0000}~\u0003\u0002\u0001\u0000"+
		"~\u007f\u0005\u0006\u0000\u0000\u007f\u008f\u0001\u0000\u0000\u0000\u0080"+
		"\u0081\u0005\u0011\u0000\u0000\u0081\u0082\u0003\u000e\u0007\u0000\u0082"+
		"\u0083\u0003\"\u0011\u0000\u0083\u008f\u0001\u0000\u0000\u0000\u0084\u0085"+
		"\u0005\u0012\u0000\u0000\u0085\u0086\u0005\u0011\u0000\u0000\u0086\u0087"+
		"\u00058\u0000\u0000\u0087\u008f\u0003\"\u0011\u0000\u0088\u0089\u0005"+
		"\u0012\u0000\u0000\u0089\u008c\u0003\u0014\n\u0000\u008a\u008b\u0005\u0002"+
		"\u0000\u0000\u008b\u008d\u0003\u0016\u000b\u0000\u008c\u008a\u0001\u0000"+
		"\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d\u008f\u0001\u0000"+
		"\u0000\u0000\u008e=\u0001\u0000\u0000\u0000\u008e>\u0001\u0000\u0000\u0000"+
		"\u008eB\u0001\u0000\u0000\u0000\u008eC\u0001\u0000\u0000\u0000\u008eD"+
		"\u0001\u0000\u0000\u0000\u008eE\u0001\u0000\u0000\u0000\u008eG\u0001\u0000"+
		"\u0000\u0000\u008eK\u0001\u0000\u0000\u0000\u008eQ\u0001\u0000\u0000\u0000"+
		"\u008eV\u0001\u0000\u0000\u0000\u008ej\u0001\u0000\u0000\u0000\u008ex"+
		"\u0001\u0000\u0000\u0000\u008e\u0080\u0001\u0000\u0000\u0000\u008e\u0084"+
		"\u0001\u0000\u0000\u0000\u008e\u0088\u0001\u0000\u0000\u0000\u008f\u0005"+
		"\u0001\u0000\u0000\u0000\u0090\u0091\u00058\u0000\u0000\u0091\u0097\u0003"+
		"\b\u0004\u0000\u0092\u0093\u0005\u000f\u0000\u0000\u0093\u0094\u00058"+
		"\u0000\u0000\u0094\u0096\u0003\b\u0004\u0000\u0095\u0092\u0001\u0000\u0000"+
		"\u0000\u0096\u0099\u0001\u0000\u0000\u0000\u0097\u0095\u0001\u0000\u0000"+
		"\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0007\u0001\u0000\u0000"+
		"\u0000\u0099\u0097\u0001\u0000\u0000\u0000\u009a\u009b\u0005\u0013\u0000"+
		"\u0000\u009b\u009c\u00058\u0000\u0000\u009c\u009e\u0005\u0014\u0000\u0000"+
		"\u009d\u009a\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000"+
		"\u009e\t\u0001\u0000\u0000\u0000\u009f\u00a1\u0005\u0015\u0000\u0000\u00a0"+
		"\u00a2\u0003\u0016\u000b\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a1"+
		"\u00a2\u0001\u0000\u0000\u0000\u00a2\u000b\u0001\u0000\u0000\u0000\u00a3"+
		"\u00a4\u0005\u0016\u0000\u0000\u00a4\u00a5\u00058\u0000\u0000\u00a5\u00a6"+
		"\u0005\u0016\u0000\u0000\u00a6\r\u0001\u0000\u0000\u0000\u00a7\u00ac\u0005"+
		"8\u0000\u0000\u00a8\u00a9\u0005\u001a\u0000\u0000\u00a9\u00ab\u00058\u0000"+
		"\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000\u00ab\u00ae\u0001\u0000\u0000"+
		"\u0000\u00ac\u00aa\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001\u0000\u0000"+
		"\u0000\u00ad\u00b1\u0001\u0000\u0000\u0000\u00ae\u00ac\u0001\u0000\u0000"+
		"\u0000\u00af\u00b0\u0005&\u0000\u0000\u00b0\u00b2\u00058\u0000\u0000\u00b1"+
		"\u00af\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2"+
		"\u000f\u0001\u0000\u0000\u0000\u00b3\u00b8\u0003\u0012\t\u0000\u00b4\u00b5"+
		"\u0005\u000f\u0000\u0000\u00b5\u00b7\u0003\u0012\t\u0000\u00b6\u00b4\u0001"+
		"\u0000\u0000\u0000\u00b7\u00ba\u0001\u0000\u0000\u0000\u00b8\u00b6\u0001"+
		"\u0000\u0000\u0000\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9\u0011\u0001"+
		"\u0000\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u00c6\u0005"+
		"8\u0000\u0000\u00bc\u00bd\u0003\u001a\r\u0000\u00bd\u00be\u0005/\u0000"+
		"\u0000\u00be\u00bf\u0003\u0018\f\u0000\u00bf\u00c0\u00050\u0000\u0000"+
		"\u00c0\u00c6\u0001\u0000\u0000\u0000\u00c1\u00c2\u0003\u001a\r\u0000\u00c2"+
		"\u00c3\u0005\u001a\u0000\u0000\u00c3\u00c4\u00058\u0000\u0000\u00c4\u00c6"+
		"\u0001\u0000\u0000\u0000\u00c5\u00bb\u0001\u0000\u0000\u0000\u00c5\u00bc"+
		"\u0001\u0000\u0000\u0000\u00c5\u00c1\u0001\u0000\u0000\u0000\u00c6\u0013"+
		"\u0001\u0000\u0000\u0000\u00c7\u00cc\u00058\u0000\u0000\u00c8\u00c9\u0005"+
		"\u000f\u0000\u0000\u00c9\u00cb\u00058\u0000\u0000\u00ca\u00c8\u0001\u0000"+
		"\u0000\u0000\u00cb\u00ce\u0001\u0000\u0000\u0000\u00cc\u00ca\u0001\u0000"+
		"\u0000\u0000\u00cc\u00cd\u0001\u0000\u0000\u0000\u00cd\u0015\u0001\u0000"+
		"\u0000\u0000\u00ce\u00cc\u0001\u0000\u0000\u0000\u00cf\u00d4\u0003\u0018"+
		"\f\u0000\u00d0\u00d1\u0005\u000f\u0000\u0000\u00d1\u00d3\u0003\u0018\f"+
		"\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d3\u00d6\u0001\u0000\u0000"+
		"\u0000\u00d4\u00d2\u0001\u0000\u0000\u0000\u00d4\u00d5\u0001\u0000\u0000"+
		"\u0000\u00d5\u0017\u0001\u0000\u0000\u0000\u00d6\u00d4\u0001\u0000\u0000"+
		"\u0000\u00d7\u00d8\u0006\f\uffff\uffff\u0000\u00d8\u00e5\u0005\u0017\u0000"+
		"\u0000\u00d9\u00e5\u0005\u0018\u0000\u0000\u00da\u00e5\u0005\u0019\u0000"+
		"\u0000\u00db\u00e5\u00059\u0000\u0000\u00dc\u00e5\u0005:\u0000\u0000\u00dd"+
		"\u00e5\u00056\u0000\u0000\u00de\u00e5\u0003 \u0010\u0000\u00df\u00e5\u0003"+
		"\u001a\r\u0000\u00e0\u00e5\u0003&\u0013\u0000\u00e1\u00e2\u00030\u0018"+
		"\u0000\u00e2\u00e3\u0003\u0018\f\u0001\u00e3\u00e5\u0001\u0000\u0000\u0000"+
		"\u00e4\u00d7\u0001\u0000\u0000\u0000\u00e4\u00d9\u0001\u0000\u0000\u0000"+
		"\u00e4\u00da\u0001\u0000\u0000\u0000\u00e4\u00db\u0001\u0000\u0000\u0000"+
		"\u00e4\u00dc\u0001\u0000\u0000\u0000\u00e4\u00dd\u0001\u0000\u0000\u0000"+
		"\u00e4\u00de\u0001\u0000\u0000\u0000\u00e4\u00df\u0001\u0000\u0000\u0000"+
		"\u00e4\u00e0\u0001\u0000\u0000\u0000\u00e4\u00e1\u0001\u0000\u0000\u0000"+
		"\u00e5\u00ec\u0001\u0000\u0000\u0000\u00e6\u00e7\n\u0002\u0000\u0000\u00e7"+
		"\u00e8\u0003.\u0017\u0000\u00e8\u00e9\u0003\u0018\f\u0003\u00e9\u00eb"+
		"\u0001\u0000\u0000\u0000\u00ea\u00e6\u0001\u0000\u0000\u0000\u00eb\u00ee"+
		"\u0001\u0000\u0000\u0000\u00ec\u00ea\u0001\u0000\u0000\u0000\u00ec\u00ed"+
		"\u0001\u0000\u0000\u0000\u00ed\u0019\u0001\u0000\u0000\u0000\u00ee\u00ec"+
		"\u0001\u0000\u0000\u0000\u00ef\u00f0\u0006\r\uffff\uffff\u0000\u00f0\u00f1"+
		"\u0005\u001e\u0000\u0000\u00f1\u00f2\u0003\u0018\f\u0000\u00f2\u00f3\u0005"+
		"\u001f\u0000\u0000\u00f3\u00f6\u0001\u0000\u0000\u0000\u00f4\u00f6\u0005"+
		"8\u0000\u0000\u00f5\u00ef\u0001\u0000\u0000\u0000\u00f5\u00f4\u0001\u0000"+
		"\u0000\u0000\u00f6\u0107\u0001\u0000\u0000\u0000\u00f7\u00f8\n\u0004\u0000"+
		"\u0000\u00f8\u00f9\u0005/\u0000\u0000\u00f9\u00fa\u0003\u0018\f\u0000"+
		"\u00fa\u00fb\u00050\u0000\u0000\u00fb\u0106\u0001\u0000\u0000\u0000\u00fc"+
		"\u00fd\n\u0003\u0000\u0000\u00fd\u00fe\u0005\u001a\u0000\u0000\u00fe\u0106"+
		"\u00058\u0000\u0000\u00ff\u0100\n\u0002\u0000\u0000\u0100\u0106\u0003"+
		"\u001e\u000f\u0000\u0101\u0102\n\u0001\u0000\u0000\u0102\u0103\u0005&"+
		"\u0000\u0000\u0103\u0104\u00058\u0000\u0000\u0104\u0106\u0003\u001e\u000f"+
		"\u0000\u0105\u00f7\u0001\u0000\u0000\u0000\u0105\u00fc\u0001\u0000\u0000"+
		"\u0000\u0105\u00ff\u0001\u0000\u0000\u0000\u0105\u0101\u0001\u0000\u0000"+
		"\u0000\u0106\u0109\u0001\u0000\u0000\u0000\u0107\u0105\u0001\u0000\u0000"+
		"\u0000\u0107\u0108\u0001\u0000\u0000\u0000\u0108\u001b\u0001\u0000\u0000"+
		"\u0000\u0109\u0107\u0001\u0000\u0000\u0000\u010a\u010b\u0003\u001a\r\u0000"+
		"\u010b\u010c\u0003\u001e\u000f\u0000\u010c\u0113\u0001\u0000\u0000\u0000"+
		"\u010d\u010e\u0003\u001a\r\u0000\u010e\u010f\u0005&\u0000\u0000\u010f"+
		"\u0110\u00058\u0000\u0000\u0110\u0111\u0003\u001e\u000f\u0000\u0111\u0113"+
		"\u0001\u0000\u0000\u0000\u0112\u010a\u0001\u0000\u0000\u0000\u0112\u010d"+
		"\u0001\u0000\u0000\u0000\u0113\u001d\u0001\u0000\u0000\u0000\u0114\u0116"+
		"\u0005\u001e\u0000\u0000\u0115\u0117\u0003\u0016\u000b\u0000\u0116\u0115"+
		"\u0001\u0000\u0000\u0000\u0116\u0117\u0001\u0000\u0000\u0000\u0117\u0118"+
		"\u0001\u0000\u0000\u0000\u0118\u011c\u0005\u001f\u0000\u0000\u0119\u011c"+
		"\u0003&\u0013\u0000\u011a\u011c\u0005:\u0000\u0000\u011b\u0114\u0001\u0000"+
		"\u0000\u0000\u011b\u0119\u0001\u0000\u0000\u0000\u011b\u011a\u0001\u0000"+
		"\u0000\u0000\u011c\u001f\u0001\u0000\u0000\u0000\u011d\u011e\u0005\u0011"+
		"\u0000\u0000\u011e\u011f\u0003\"\u0011\u0000\u011f!\u0001\u0000\u0000"+
		"\u0000\u0120\u0122\u0005\u001e\u0000\u0000\u0121\u0123\u0003$\u0012\u0000"+
		"\u0122\u0121\u0001\u0000\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000"+
		"\u0123\u0124\u0001\u0000\u0000\u0000\u0124\u0125\u0005\u001f\u0000\u0000"+
		"\u0125\u0126\u0003\u0002\u0001\u0000\u0126\u0127\u0005\u0006\u0000\u0000"+
		"\u0127#\u0001\u0000\u0000\u0000\u0128\u012b\u0003\u0014\n\u0000\u0129"+
		"\u012a\u0005\u000f\u0000\u0000\u012a\u012c\u00056\u0000\u0000\u012b\u0129"+
		"\u0001\u0000\u0000\u0000\u012b\u012c\u0001\u0000\u0000\u0000\u012c\u012f"+
		"\u0001\u0000\u0000\u0000\u012d\u012f\u00056\u0000\u0000\u012e\u0128\u0001"+
		"\u0000\u0000\u0000\u012e\u012d\u0001\u0000\u0000\u0000\u012f%\u0001\u0000"+
		"\u0000\u0000\u0130\u0132\u0005-\u0000\u0000\u0131\u0133\u0003(\u0014\u0000"+
		"\u0132\u0131\u0001\u0000\u0000\u0000\u0132\u0133\u0001\u0000\u0000\u0000"+
		"\u0133\u0134\u0001\u0000\u0000\u0000\u0134\u0135\u0005.\u0000\u0000\u0135"+
		"\'\u0001\u0000\u0000\u0000\u0136\u013c\u0003*\u0015\u0000\u0137\u0138"+
		"\u0003,\u0016\u0000\u0138\u0139\u0003*\u0015\u0000\u0139\u013b\u0001\u0000"+
		"\u0000\u0000\u013a\u0137\u0001\u0000\u0000\u0000\u013b\u013e\u0001\u0000"+
		"\u0000\u0000\u013c\u013a\u0001\u0000\u0000\u0000\u013c\u013d\u0001\u0000"+
		"\u0000\u0000\u013d\u0140\u0001\u0000\u0000\u0000\u013e\u013c\u0001\u0000"+
		"\u0000\u0000\u013f\u0141\u0003,\u0016\u0000\u0140\u013f\u0001\u0000\u0000"+
		"\u0000\u0140\u0141\u0001\u0000\u0000\u0000\u0141)\u0001\u0000\u0000\u0000"+
		"\u0142\u0143\u0005/\u0000\u0000\u0143\u0144\u0003\u0018\f\u0000\u0144"+
		"\u0145\u00050\u0000\u0000\u0145\u0146\u0005\u0002\u0000\u0000\u0146\u0147"+
		"\u0003\u0018\f\u0000\u0147\u014d\u0001\u0000\u0000\u0000\u0148\u0149\u0005"+
		"8\u0000\u0000\u0149\u014a\u0005\u0002\u0000\u0000\u014a\u014d\u0003\u0018"+
		"\f\u0000\u014b\u014d\u0003\u0018\f\u0000\u014c\u0142\u0001\u0000\u0000"+
		"\u0000\u014c\u0148\u0001\u0000\u0000\u0000\u014c\u014b\u0001\u0000\u0000"+
		"\u0000\u014d+\u0001\u0000\u0000\u0000\u014e\u014f\u0007\u0000\u0000\u0000"+
		"\u014f-\u0001\u0000\u0000\u0000\u0150\u0151\u0007\u0001\u0000\u0000\u0151"+
		"/\u0001\u0000\u0000\u0000\u0152\u0153\u0007\u0002\u0000\u0000\u01531\u0001"+
		"\u0000\u0000\u0000\u001f7;afr\u008c\u008e\u0097\u009d\u00a1\u00ac\u00b1"+
		"\u00b8\u00c5\u00cc\u00d4\u00e4\u00ec\u00f5\u0105\u0107\u0112\u0116\u011b"+
		"\u0122\u012b\u012e\u0132\u013c\u0140\u014c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}