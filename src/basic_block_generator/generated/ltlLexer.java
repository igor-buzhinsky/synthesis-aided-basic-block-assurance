// Generated from C:/Users/Lenovo/repos/synthesis-aided-basic-block-assurance\ltl.g4 by ANTLR 4.7
package basic_block_generator.generated;

package basic_block_generator.generated;

import java.util.*;
import basic_block_generator.formula.*;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ltlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, WS=4, GLOBALLY=5, FUTURE=6, UNTIL=7, NEXT=8, RELEASE=9, 
		WEAK_UNTIL=10, AND=11, OR=12, IMPLIES=13, NOT=14, EQUIVALENT=15, EQUALS=16, 
		NOT_EQUALS=17, TRUE=18, FALSE=19, INT_CONST=20, ID=21;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "WS", "GLOBALLY", "FUTURE", "UNTIL", "NEXT", "RELEASE", 
		"WEAK_UNTIL", "AND", "OR", "IMPLIES", "NOT", "EQUIVALENT", "EQUALS", "NOT_EQUALS", 
		"TRUE", "FALSE", "INT_CONST", "ID"
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


	public ltlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ltl.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u00b9\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\5\5\66\n\5\3\5\6\59\n\5\r\5\16\5:\3\5\3\5\3\6\3\6\3\7\3\7\3\b"+
		"\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f"+
		"T\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r]\n\r\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16y\n\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\5\17\u0082\n\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\5\21\u008b"+
		"\n\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u0098"+
		"\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00a4\n\24"+
		"\3\25\5\25\u00a7\n\25\3\25\3\25\3\25\7\25\u00ac\n\25\f\25\16\25\u00af"+
		"\13\25\5\25\u00b1\n\25\3\26\3\26\7\26\u00b5\n\26\f\26\16\26\u00b8\13\26"+
		"\2\2\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\'\25)\26+\27\3\2\6\4\2\13\13\"\"\4\2##\u0080"+
		"\u0080\5\2C\\aac|\6\2\62;C\\aac|\2\u00ce\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\3-\3\2\2\2\5/\3\2\2\2\7\61\3\2\2\2\t8\3\2\2\2\13>"+
		"\3\2\2\2\r@\3\2\2\2\17B\3\2\2\2\21D\3\2\2\2\23F\3\2\2\2\25H\3\2\2\2\27"+
		"S\3\2\2\2\31\\\3\2\2\2\33x\3\2\2\2\35\u0081\3\2\2\2\37\u0083\3\2\2\2!"+
		"\u008a\3\2\2\2#\u008c\3\2\2\2%\u0097\3\2\2\2\'\u00a3\3\2\2\2)\u00a6\3"+
		"\2\2\2+\u00b2\3\2\2\2-.\7*\2\2.\4\3\2\2\2/\60\7.\2\2\60\6\3\2\2\2\61\62"+
		"\7+\2\2\62\b\3\2\2\2\639\t\2\2\2\64\66\7\17\2\2\65\64\3\2\2\2\65\66\3"+
		"\2\2\2\66\67\3\2\2\2\679\7\f\2\28\63\3\2\2\28\65\3\2\2\29:\3\2\2\2:8\3"+
		"\2\2\2:;\3\2\2\2;<\3\2\2\2<=\b\5\2\2=\n\3\2\2\2>?\7I\2\2?\f\3\2\2\2@A"+
		"\7H\2\2A\16\3\2\2\2BC\7W\2\2C\20\3\2\2\2DE\7Z\2\2E\22\3\2\2\2FG\7T\2\2"+
		"G\24\3\2\2\2HI\7Y\2\2I\26\3\2\2\2JK\7(\2\2KT\7(\2\2LT\7(\2\2MN\7c\2\2"+
		"NO\7p\2\2OT\7f\2\2PQ\7C\2\2QR\7P\2\2RT\7F\2\2SJ\3\2\2\2SL\3\2\2\2SM\3"+
		"\2\2\2SP\3\2\2\2T\30\3\2\2\2UV\7~\2\2V]\7~\2\2W]\7~\2\2XY\7q\2\2Y]\7t"+
		"\2\2Z[\7Q\2\2[]\7T\2\2\\U\3\2\2\2\\W\3\2\2\2\\X\3\2\2\2\\Z\3\2\2\2]\32"+
		"\3\2\2\2^_\7/\2\2_y\7@\2\2`a\7k\2\2ab\7o\2\2bc\7r\2\2cd\7n\2\2dy\7{\2"+
		"\2ef\7k\2\2fg\7o\2\2gh\7r\2\2hi\7n\2\2ij\7k\2\2jk\7g\2\2ky\7u\2\2lm\7"+
		"K\2\2mn\7O\2\2no\7R\2\2op\7N\2\2py\7[\2\2qr\7K\2\2rs\7O\2\2st\7R\2\2t"+
		"u\7N\2\2uv\7K\2\2vw\7G\2\2wy\7U\2\2x^\3\2\2\2x`\3\2\2\2xe\3\2\2\2xl\3"+
		"\2\2\2xq\3\2\2\2y\34\3\2\2\2z\u0082\t\3\2\2{|\7p\2\2|}\7q\2\2}\u0082\7"+
		"v\2\2~\177\7P\2\2\177\u0080\7Q\2\2\u0080\u0082\7V\2\2\u0081z\3\2\2\2\u0081"+
		"{\3\2\2\2\u0081~\3\2\2\2\u0082\36\3\2\2\2\u0083\u0084\7>\2\2\u0084\u0085"+
		"\7/\2\2\u0085\u0086\7@\2\2\u0086 \3\2\2\2\u0087\u008b\7?\2\2\u0088\u0089"+
		"\7?\2\2\u0089\u008b\7?\2\2\u008a\u0087\3\2\2\2\u008a\u0088\3\2\2\2\u008b"+
		"\"\3\2\2\2\u008c\u008d\7#\2\2\u008d\u008e\7?\2\2\u008e$\3\2\2\2\u008f"+
		"\u0090\7V\2\2\u0090\u0091\7T\2\2\u0091\u0092\7W\2\2\u0092\u0098\7G\2\2"+
		"\u0093\u0094\7v\2\2\u0094\u0095\7t\2\2\u0095\u0096\7w\2\2\u0096\u0098"+
		"\7g\2\2\u0097\u008f\3\2\2\2\u0097\u0093\3\2\2\2\u0098&\3\2\2\2\u0099\u009a"+
		"\7H\2\2\u009a\u009b\7C\2\2\u009b\u009c\7N\2\2\u009c\u009d\7U\2\2\u009d"+
		"\u00a4\7G\2\2\u009e\u009f\7h\2\2\u009f\u00a0\7c\2\2\u00a0\u00a1\7n\2\2"+
		"\u00a1\u00a2\7u\2\2\u00a2\u00a4\7g\2\2\u00a3\u0099\3\2\2\2\u00a3\u009e"+
		"\3\2\2\2\u00a4(\3\2\2\2\u00a5\u00a7\7/\2\2\u00a6\u00a5\3\2\2\2\u00a6\u00a7"+
		"\3\2\2\2\u00a7\u00b0\3\2\2\2\u00a8\u00b1\7\62\2\2\u00a9\u00ad\4\63;\2"+
		"\u00aa\u00ac\4\62;\2\u00ab\u00aa\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab"+
		"\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0"+
		"\u00a8\3\2\2\2\u00b0\u00a9\3\2\2\2\u00b1*\3\2\2\2\u00b2\u00b6\t\4\2\2"+
		"\u00b3\u00b5\t\5\2\2\u00b4\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4"+
		"\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7,\3\2\2\2\u00b8\u00b6\3\2\2\2\21\2\65"+
		"8:S\\x\u0081\u008a\u0097\u00a3\u00a6\u00ad\u00b0\u00b6\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}