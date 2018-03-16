// Generated from C:/Users/Lenovo/repos/synthesis-aided-basic-block-assurance\ltl.g4 by ANTLR 4.7
package basic_block_generator.generated;

package basic_block_generator.generated;

import java.util.*;
import basic_block_generator.formula.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ltlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, WS=4, GLOBALLY=5, FUTURE=6, UNTIL=7, NEXT=8, RELEASE=9, 
		WEAK_UNTIL=10, AND=11, OR=12, IMPLIES=13, NOT=14, EQUIVALENT=15, EQUALS=16, 
		NOT_EQUALS=17, TRUE=18, FALSE=19, INT_CONST=20, ID=21;
	public static final int
		RULE_unary_operator_sign = 0, RULE_binary_function_sign = 1, RULE_constant = 2, 
		RULE_proposition = 3, RULE_binary_function = 4, RULE_atom = 5, RULE_unary_operator = 6, 
		RULE_priority1_binary_operator = 7, RULE_priority2_binary_operator = 8, 
		RULE_priority3_binary_operator = 9, RULE_priority4_binary_operator = 10, 
		RULE_formula = 11;
	public static final String[] ruleNames = {
		"unary_operator_sign", "binary_function_sign", "constant", "proposition", 
		"binary_function", "atom", "unary_operator", "priority1_binary_operator", 
		"priority2_binary_operator", "priority3_binary_operator", "priority4_binary_operator", 
		"formula"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "','", "')'", null, "'G'", "'F'", "'U'", "'X'", "'R'", "'W'", 
		null, null, null, null, "'<->'", null, "'!='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, "WS", "GLOBALLY", "FUTURE", "UNTIL", "NEXT", "RELEASE", 
		"WEAK_UNTIL", "AND", "OR", "IMPLIES", "NOT", "EQUIVALENT", "EQUALS", "NOT_EQUALS", 
		"TRUE", "FALSE", "INT_CONST", "ID"
	};
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
	public String getGrammarFileName() { return "ltl.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }



	public ltlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class Unary_operator_signContext extends ParserRuleContext {
		public String value;
		public TerminalNode NOT() { return getToken(ltlParser.NOT, 0); }
		public TerminalNode NEXT() { return getToken(ltlParser.NEXT, 0); }
		public TerminalNode GLOBALLY() { return getToken(ltlParser.GLOBALLY, 0); }
		public TerminalNode FUTURE() { return getToken(ltlParser.FUTURE, 0); }
		public Unary_operator_signContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_operator_sign; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterUnary_operator_sign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitUnary_operator_sign(this);
		}
	}

	public final Unary_operator_signContext unary_operator_sign() throws RecognitionException {
		Unary_operator_signContext _localctx = new Unary_operator_signContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_unary_operator_sign);
		try {
			setState(32);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(24);
				match(NOT);
				 ((Unary_operator_signContext)_localctx).value =  "!"; 
				}
				break;
			case NEXT:
				enterOuterAlt(_localctx, 2);
				{
				setState(26);
				match(NEXT);
				 ((Unary_operator_signContext)_localctx).value =  "X"; 
				}
				break;
			case GLOBALLY:
				enterOuterAlt(_localctx, 3);
				{
				setState(28);
				match(GLOBALLY);
				 ((Unary_operator_signContext)_localctx).value =  "G"; 
				}
				break;
			case FUTURE:
				enterOuterAlt(_localctx, 4);
				{
				setState(30);
				match(FUTURE);
				 ((Unary_operator_signContext)_localctx).value =  "F"; 
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

	public static class Binary_function_signContext extends ParserRuleContext {
		public String value;
		public TerminalNode UNTIL() { return getToken(ltlParser.UNTIL, 0); }
		public TerminalNode RELEASE() { return getToken(ltlParser.RELEASE, 0); }
		public TerminalNode WEAK_UNTIL() { return getToken(ltlParser.WEAK_UNTIL, 0); }
		public Binary_function_signContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binary_function_sign; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterBinary_function_sign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitBinary_function_sign(this);
		}
	}

	public final Binary_function_signContext binary_function_sign() throws RecognitionException {
		Binary_function_signContext _localctx = new Binary_function_signContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_binary_function_sign);
		try {
			setState(40);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case UNTIL:
				enterOuterAlt(_localctx, 1);
				{
				setState(34);
				match(UNTIL);
				 ((Binary_function_signContext)_localctx).value =  "U"; 
				}
				break;
			case RELEASE:
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
				match(RELEASE);
				 ((Binary_function_signContext)_localctx).value =  "R"; 
				}
				break;
			case WEAK_UNTIL:
				enterOuterAlt(_localctx, 3);
				{
				setState(38);
				match(WEAK_UNTIL);
				 ((Binary_function_signContext)_localctx).value =  "W"; 
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

	public static class ConstantContext extends ParserRuleContext {
		public String value;
		public Token INT_CONST;
		public TerminalNode INT_CONST() { return getToken(ltlParser.INT_CONST, 0); }
		public TerminalNode TRUE() { return getToken(ltlParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(ltlParser.FALSE, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitConstant(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_constant);
		try {
			setState(48);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT_CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(42);
				((ConstantContext)_localctx).INT_CONST = match(INT_CONST);
				 ((ConstantContext)_localctx).value =  (((ConstantContext)_localctx).INT_CONST!=null?((ConstantContext)_localctx).INT_CONST.getText():null); 
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 2);
				{
				setState(44);
				match(TRUE);
				 ((ConstantContext)_localctx).value =  "TRUE"; 
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 3);
				{
				setState(46);
				match(FALSE);
				 ((ConstantContext)_localctx).value =  "FALSE"; 
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

	public static class PropositionContext extends ParserRuleContext {
		public LTLFormula f;
		public Token ID;
		public ConstantContext constant;
		public TerminalNode ID() { return getToken(ltlParser.ID, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(ltlParser.EQUALS, 0); }
		public TerminalNode NOT_EQUALS() { return getToken(ltlParser.NOT_EQUALS, 0); }
		public PropositionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_proposition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterProposition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitProposition(this);
		}
	}

	public final PropositionContext proposition() throws RecognitionException {
		PropositionContext _localctx = new PropositionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_proposition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 String c = "TRUE"; boolean notEquals = false; 
			setState(51);
			((PropositionContext)_localctx).ID = match(ID);
			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EQUALS || _la==NOT_EQUALS) {
				{
				setState(55);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case EQUALS:
					{
					setState(52);
					match(EQUALS);
					}
					break;
				case NOT_EQUALS:
					{
					{
					setState(53);
					match(NOT_EQUALS);
					 notEquals = true; 
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(57);
				((PropositionContext)_localctx).constant = constant();
				 c = ((PropositionContext)_localctx).constant.value; 
				}
			}

			 LTLFormula tmp = new Proposition((((PropositionContext)_localctx).ID!=null?((PropositionContext)_localctx).ID.getText():null), c); ((PropositionContext)_localctx).f =  notEquals ? new UnaryOperator("!", tmp) : tmp; 
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

	public static class Binary_functionContext extends ParserRuleContext {
		public LTLFormula f;
		public Binary_function_signContext binary_function_sign;
		public FormulaContext f1;
		public FormulaContext f2;
		public Binary_function_signContext binary_function_sign() {
			return getRuleContext(Binary_function_signContext.class,0);
		}
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public Binary_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binary_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterBinary_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitBinary_function(this);
		}
	}

	public final Binary_functionContext binary_function() throws RecognitionException {
		Binary_functionContext _localctx = new Binary_functionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_binary_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			((Binary_functionContext)_localctx).binary_function_sign = binary_function_sign();
			setState(65);
			match(T__0);
			setState(66);
			((Binary_functionContext)_localctx).f1 = formula();
			setState(67);
			match(T__1);
			setState(68);
			((Binary_functionContext)_localctx).f2 = formula();
			setState(69);
			match(T__2);
			 ((Binary_functionContext)_localctx).f =  new BinaryFunction(((Binary_functionContext)_localctx).binary_function_sign.value, ((Binary_functionContext)_localctx).f1.f, ((Binary_functionContext)_localctx).f2.f); 
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

	public static class AtomContext extends ParserRuleContext {
		public LTLFormula f;
		public PropositionContext proposition;
		public Binary_functionContext binary_function;
		public FormulaContext formula;
		public PropositionContext proposition() {
			return getRuleContext(PropositionContext.class,0);
		}
		public Binary_functionContext binary_function() {
			return getRuleContext(Binary_functionContext.class,0);
		}
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitAtom(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_atom);
		try {
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(72);
				((AtomContext)_localctx).proposition = proposition();
				 ((AtomContext)_localctx).f =  ((AtomContext)_localctx).proposition.f; 
				}
				break;
			case UNTIL:
			case RELEASE:
			case WEAK_UNTIL:
				enterOuterAlt(_localctx, 2);
				{
				setState(75);
				((AtomContext)_localctx).binary_function = binary_function();
				 ((AtomContext)_localctx).f =  ((AtomContext)_localctx).binary_function.f; 
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 3);
				{
				setState(78);
				match(T__0);
				setState(79);
				((AtomContext)_localctx).formula = formula();
				setState(80);
				match(T__2);
				 ((AtomContext)_localctx).f =  ((AtomContext)_localctx).formula.f; 
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

	public static class Unary_operatorContext extends ParserRuleContext {
		public LTLFormula f;
		public Unary_operator_signContext unary_operator_sign;
		public Unary_operatorContext unary_operator;
		public AtomContext atom;
		public Unary_operator_signContext unary_operator_sign() {
			return getRuleContext(Unary_operator_signContext.class,0);
		}
		public Unary_operatorContext unary_operator() {
			return getRuleContext(Unary_operatorContext.class,0);
		}
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public Unary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterUnary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitUnary_operator(this);
		}
	}

	public final Unary_operatorContext unary_operator() throws RecognitionException {
		Unary_operatorContext _localctx = new Unary_operatorContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_unary_operator);
		try {
			setState(92);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GLOBALLY:
			case FUTURE:
			case NEXT:
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(85);
				((Unary_operatorContext)_localctx).unary_operator_sign = unary_operator_sign();
				setState(86);
				((Unary_operatorContext)_localctx).unary_operator = unary_operator();
				 ((Unary_operatorContext)_localctx).f =  new UnaryOperator(((Unary_operatorContext)_localctx).unary_operator_sign.value, ((Unary_operatorContext)_localctx).unary_operator.f); 
				}
				break;
			case T__0:
			case UNTIL:
			case RELEASE:
			case WEAK_UNTIL:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(89);
				((Unary_operatorContext)_localctx).atom = atom();
				 ((Unary_operatorContext)_localctx).f =  ((Unary_operatorContext)_localctx).atom.f; 
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

	public static class Priority1_binary_operatorContext extends ParserRuleContext {
		public LTLFormula f;
		public Unary_operatorContext f1;
		public Unary_operatorContext f2;
		public List<Unary_operatorContext> unary_operator() {
			return getRuleContexts(Unary_operatorContext.class);
		}
		public Unary_operatorContext unary_operator(int i) {
			return getRuleContext(Unary_operatorContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(ltlParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(ltlParser.AND, i);
		}
		public Priority1_binary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_priority1_binary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterPriority1_binary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitPriority1_binary_operator(this);
		}
	}

	public final Priority1_binary_operatorContext priority1_binary_operator() throws RecognitionException {
		Priority1_binary_operatorContext _localctx = new Priority1_binary_operatorContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_priority1_binary_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			((Priority1_binary_operatorContext)_localctx).f1 = unary_operator();
			 ((Priority1_binary_operatorContext)_localctx).f =  ((Priority1_binary_operatorContext)_localctx).f1.f; 
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(96);
				match(AND);
				setState(97);
				((Priority1_binary_operatorContext)_localctx).f2 = unary_operator();
				 ((Priority1_binary_operatorContext)_localctx).f =  new BinaryOperator("&", _localctx.f, ((Priority1_binary_operatorContext)_localctx).f2.f); 
				}
				}
				setState(104);
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

	public static class Priority2_binary_operatorContext extends ParserRuleContext {
		public LTLFormula f;
		public Priority1_binary_operatorContext f1;
		public Priority1_binary_operatorContext f2;
		public List<Priority1_binary_operatorContext> priority1_binary_operator() {
			return getRuleContexts(Priority1_binary_operatorContext.class);
		}
		public Priority1_binary_operatorContext priority1_binary_operator(int i) {
			return getRuleContext(Priority1_binary_operatorContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(ltlParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(ltlParser.OR, i);
		}
		public Priority2_binary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_priority2_binary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterPriority2_binary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitPriority2_binary_operator(this);
		}
	}

	public final Priority2_binary_operatorContext priority2_binary_operator() throws RecognitionException {
		Priority2_binary_operatorContext _localctx = new Priority2_binary_operatorContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_priority2_binary_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			((Priority2_binary_operatorContext)_localctx).f1 = priority1_binary_operator();
			 ((Priority2_binary_operatorContext)_localctx).f =  ((Priority2_binary_operatorContext)_localctx).f1.f; 
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(107);
				match(OR);
				setState(108);
				((Priority2_binary_operatorContext)_localctx).f2 = priority1_binary_operator();
				 ((Priority2_binary_operatorContext)_localctx).f =  new BinaryOperator("|", _localctx.f, ((Priority2_binary_operatorContext)_localctx).f2.f); 
				}
				}
				setState(115);
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

	public static class Priority3_binary_operatorContext extends ParserRuleContext {
		public LTLFormula f;
		public Priority2_binary_operatorContext f1;
		public Priority2_binary_operatorContext f2;
		public List<Priority2_binary_operatorContext> priority2_binary_operator() {
			return getRuleContexts(Priority2_binary_operatorContext.class);
		}
		public Priority2_binary_operatorContext priority2_binary_operator(int i) {
			return getRuleContext(Priority2_binary_operatorContext.class,i);
		}
		public List<TerminalNode> IMPLIES() { return getTokens(ltlParser.IMPLIES); }
		public TerminalNode IMPLIES(int i) {
			return getToken(ltlParser.IMPLIES, i);
		}
		public Priority3_binary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_priority3_binary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterPriority3_binary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitPriority3_binary_operator(this);
		}
	}

	public final Priority3_binary_operatorContext priority3_binary_operator() throws RecognitionException {
		Priority3_binary_operatorContext _localctx = new Priority3_binary_operatorContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_priority3_binary_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			((Priority3_binary_operatorContext)_localctx).f1 = priority2_binary_operator();
			 List<LTLFormula> formulas = new ArrayList<>(); formulas.add(((Priority3_binary_operatorContext)_localctx).f1.f); 
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPLIES) {
				{
				{
				setState(118);
				match(IMPLIES);
				setState(119);
				((Priority3_binary_operatorContext)_localctx).f2 = priority2_binary_operator();
				 formulas.add(((Priority3_binary_operatorContext)_localctx).f2.f); 
				}
				}
				setState(126);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}

			        ((Priority3_binary_operatorContext)_localctx).f =  formulas.get(formulas.size() - 1);
			        for (int i = formulas.size() - 2; i >= 0; i--) {
			            ((Priority3_binary_operatorContext)_localctx).f =  new BinaryOperator("->", formulas.get(i), _localctx.f);
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

	public static class Priority4_binary_operatorContext extends ParserRuleContext {
		public LTLFormula f;
		public Priority3_binary_operatorContext f1;
		public Priority3_binary_operatorContext f2;
		public List<Priority3_binary_operatorContext> priority3_binary_operator() {
			return getRuleContexts(Priority3_binary_operatorContext.class);
		}
		public Priority3_binary_operatorContext priority3_binary_operator(int i) {
			return getRuleContext(Priority3_binary_operatorContext.class,i);
		}
		public List<TerminalNode> EQUIVALENT() { return getTokens(ltlParser.EQUIVALENT); }
		public TerminalNode EQUIVALENT(int i) {
			return getToken(ltlParser.EQUIVALENT, i);
		}
		public Priority4_binary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_priority4_binary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterPriority4_binary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitPriority4_binary_operator(this);
		}
	}

	public final Priority4_binary_operatorContext priority4_binary_operator() throws RecognitionException {
		Priority4_binary_operatorContext _localctx = new Priority4_binary_operatorContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_priority4_binary_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			((Priority4_binary_operatorContext)_localctx).f1 = priority3_binary_operator();
			 ((Priority4_binary_operatorContext)_localctx).f =  ((Priority4_binary_operatorContext)_localctx).f1.f; 
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EQUIVALENT) {
				{
				{
				setState(131);
				match(EQUIVALENT);
				setState(132);
				((Priority4_binary_operatorContext)_localctx).f2 = priority3_binary_operator();
				 ((Priority4_binary_operatorContext)_localctx).f =  new BinaryOperator("<->", _localctx.f, ((Priority4_binary_operatorContext)_localctx).f2.f); 
				}
				}
				setState(139);
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

	public static class FormulaContext extends ParserRuleContext {
		public LTLFormula f;
		public Priority4_binary_operatorContext priority4_binary_operator;
		public Priority4_binary_operatorContext priority4_binary_operator() {
			return getRuleContext(Priority4_binary_operatorContext.class,0);
		}
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).enterFormula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ltlListener ) ((ltlListener)listener).exitFormula(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_formula);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			((FormulaContext)_localctx).priority4_binary_operator = priority4_binary_operator();
			 ((FormulaContext)_localctx).f =  ((FormulaContext)_localctx).priority4_binary_operator.f; 
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\27\u0092\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2#\n\2\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\5\3+\n\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4\63\n\4\3\5\3\5\3"+
		"\5\3\5\3\5\5\5:\n\5\3\5\3\5\3\5\5\5?\n\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7V\n\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\5\b_\n\b\3\t\3\t\3\t\3\t\3\t\3\t\7\tg\n\t\f\t\16"+
		"\tj\13\t\3\n\3\n\3\n\3\n\3\n\3\n\7\nr\n\n\f\n\16\nu\13\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\7\13}\n\13\f\13\16\13\u0080\13\13\3\13\3\13\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\7\f\u008a\n\f\f\f\16\f\u008d\13\f\3\r\3\r\3\r\3\r\2\2\16"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\2\2\2\u0095\2\"\3\2\2\2\4*\3\2\2\2\6\62"+
		"\3\2\2\2\b\64\3\2\2\2\nB\3\2\2\2\fU\3\2\2\2\16^\3\2\2\2\20`\3\2\2\2\22"+
		"k\3\2\2\2\24v\3\2\2\2\26\u0083\3\2\2\2\30\u008e\3\2\2\2\32\33\7\20\2\2"+
		"\33#\b\2\1\2\34\35\7\n\2\2\35#\b\2\1\2\36\37\7\7\2\2\37#\b\2\1\2 !\7\b"+
		"\2\2!#\b\2\1\2\"\32\3\2\2\2\"\34\3\2\2\2\"\36\3\2\2\2\" \3\2\2\2#\3\3"+
		"\2\2\2$%\7\t\2\2%+\b\3\1\2&\'\7\13\2\2\'+\b\3\1\2()\7\f\2\2)+\b\3\1\2"+
		"*$\3\2\2\2*&\3\2\2\2*(\3\2\2\2+\5\3\2\2\2,-\7\26\2\2-\63\b\4\1\2./\7\24"+
		"\2\2/\63\b\4\1\2\60\61\7\25\2\2\61\63\b\4\1\2\62,\3\2\2\2\62.\3\2\2\2"+
		"\62\60\3\2\2\2\63\7\3\2\2\2\64\65\b\5\1\2\65>\7\27\2\2\66:\7\22\2\2\67"+
		"8\7\23\2\28:\b\5\1\29\66\3\2\2\29\67\3\2\2\2:;\3\2\2\2;<\5\6\4\2<=\b\5"+
		"\1\2=?\3\2\2\2>9\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\b\5\1\2A\t\3\2\2\2BC\5"+
		"\4\3\2CD\7\3\2\2DE\5\30\r\2EF\7\4\2\2FG\5\30\r\2GH\7\5\2\2HI\b\6\1\2I"+
		"\13\3\2\2\2JK\5\b\5\2KL\b\7\1\2LV\3\2\2\2MN\5\n\6\2NO\b\7\1\2OV\3\2\2"+
		"\2PQ\7\3\2\2QR\5\30\r\2RS\7\5\2\2ST\b\7\1\2TV\3\2\2\2UJ\3\2\2\2UM\3\2"+
		"\2\2UP\3\2\2\2V\r\3\2\2\2WX\5\2\2\2XY\5\16\b\2YZ\b\b\1\2Z_\3\2\2\2[\\"+
		"\5\f\7\2\\]\b\b\1\2]_\3\2\2\2^W\3\2\2\2^[\3\2\2\2_\17\3\2\2\2`a\5\16\b"+
		"\2ah\b\t\1\2bc\7\r\2\2cd\5\16\b\2de\b\t\1\2eg\3\2\2\2fb\3\2\2\2gj\3\2"+
		"\2\2hf\3\2\2\2hi\3\2\2\2i\21\3\2\2\2jh\3\2\2\2kl\5\20\t\2ls\b\n\1\2mn"+
		"\7\16\2\2no\5\20\t\2op\b\n\1\2pr\3\2\2\2qm\3\2\2\2ru\3\2\2\2sq\3\2\2\2"+
		"st\3\2\2\2t\23\3\2\2\2us\3\2\2\2vw\5\22\n\2w~\b\13\1\2xy\7\17\2\2yz\5"+
		"\22\n\2z{\b\13\1\2{}\3\2\2\2|x\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2~\177\3"+
		"\2\2\2\177\u0081\3\2\2\2\u0080~\3\2\2\2\u0081\u0082\b\13\1\2\u0082\25"+
		"\3\2\2\2\u0083\u0084\5\24\13\2\u0084\u008b\b\f\1\2\u0085\u0086\7\21\2"+
		"\2\u0086\u0087\5\24\13\2\u0087\u0088\b\f\1\2\u0088\u008a\3\2\2\2\u0089"+
		"\u0085\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2"+
		"\2\2\u008c\27\3\2\2\2\u008d\u008b\3\2\2\2\u008e\u008f\5\26\f\2\u008f\u0090"+
		"\b\r\1\2\u0090\31\3\2\2\2\r\"*\629>U^hs~\u008b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}