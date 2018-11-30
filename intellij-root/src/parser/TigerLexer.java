// $ANTLR 3.5.2 src/Tiger.g 2018-11-30 09:11:48

package parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class TigerLexer extends Lexer {
	public static final int EOF=-1;
	public static final int T__19=19;
	public static final int T__20=20;
	public static final int T__21=21;
	public static final int T__22=22;
	public static final int T__23=23;
	public static final int T__24=24;
	public static final int T__25=25;
	public static final int T__26=26;
	public static final int T__27=27;
	public static final int T__28=28;
	public static final int T__29=29;
	public static final int T__30=30;
	public static final int T__31=31;
	public static final int T__32=32;
	public static final int T__33=33;
	public static final int T__34=34;
	public static final int T__35=35;
	public static final int T__36=36;
	public static final int T__37=37;
	public static final int T__38=38;
	public static final int T__39=39;
	public static final int T__40=40;
	public static final int T__41=41;
	public static final int T__42=42;
	public static final int T__43=43;
	public static final int T__44=44;
	public static final int T__45=45;
	public static final int T__46=46;
	public static final int T__47=47;
	public static final int T__48=48;
	public static final int T__49=49;
	public static final int T__50=50;
	public static final int T__51=51;
	public static final int T__52=52;
	public static final int T__53=53;
	public static final int T__54=54;
	public static final int T__55=55;
	public static final int T__56=56;
	public static final int T__57=57;
	public static final int T__58=58;
	public static final int ARR=4;
	public static final int ARRTYPE=5;
	public static final int CALL=6;
	public static final int CALLTYPE=7;
	public static final int COMMENT=8;
	public static final int DEC=9;
	public static final int FIELD=10;
	public static final int ID=11;
	public static final int INT=12;
	public static final int ITEM=13;
	public static final int REC=14;
	public static final int RECTYPE=15;
	public static final int SEQ=16;
	public static final int STR=17;
	public static final int WS=18;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public TigerLexer() {} 
	public TigerLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public TigerLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "src/Tiger.g"; }

	// $ANTLR start "T__19"
	public final void mT__19() throws RecognitionException {
		try {
			int _type = T__19;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:7:7: ( '&' )
			// src/Tiger.g:7:9: '&'
			{
			match('&'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__19"

	// $ANTLR start "T__20"
	public final void mT__20() throws RecognitionException {
		try {
			int _type = T__20;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:8:7: ( '(' )
			// src/Tiger.g:8:9: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__20"

	// $ANTLR start "T__21"
	public final void mT__21() throws RecognitionException {
		try {
			int _type = T__21;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:9:7: ( ')' )
			// src/Tiger.g:9:9: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__21"

	// $ANTLR start "T__22"
	public final void mT__22() throws RecognitionException {
		try {
			int _type = T__22;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:10:7: ( '*' )
			// src/Tiger.g:10:9: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__22"

	// $ANTLR start "T__23"
	public final void mT__23() throws RecognitionException {
		try {
			int _type = T__23;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:11:7: ( '+' )
			// src/Tiger.g:11:9: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__23"

	// $ANTLR start "T__24"
	public final void mT__24() throws RecognitionException {
		try {
			int _type = T__24;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:12:7: ( ',' )
			// src/Tiger.g:12:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__24"

	// $ANTLR start "T__25"
	public final void mT__25() throws RecognitionException {
		try {
			int _type = T__25;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:13:7: ( '-' )
			// src/Tiger.g:13:9: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__25"

	// $ANTLR start "T__26"
	public final void mT__26() throws RecognitionException {
		try {
			int _type = T__26;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:14:7: ( '.' )
			// src/Tiger.g:14:9: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__26"

	// $ANTLR start "T__27"
	public final void mT__27() throws RecognitionException {
		try {
			int _type = T__27;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:15:7: ( '/' )
			// src/Tiger.g:15:9: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__27"

	// $ANTLR start "T__28"
	public final void mT__28() throws RecognitionException {
		try {
			int _type = T__28;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:16:7: ( ':' )
			// src/Tiger.g:16:9: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__28"

	// $ANTLR start "T__29"
	public final void mT__29() throws RecognitionException {
		try {
			int _type = T__29;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:17:7: ( ':=' )
			// src/Tiger.g:17:9: ':='
			{
			match(":="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__29"

	// $ANTLR start "T__30"
	public final void mT__30() throws RecognitionException {
		try {
			int _type = T__30;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:18:7: ( ';' )
			// src/Tiger.g:18:9: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__30"

	// $ANTLR start "T__31"
	public final void mT__31() throws RecognitionException {
		try {
			int _type = T__31;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:19:7: ( '<' )
			// src/Tiger.g:19:9: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__31"

	// $ANTLR start "T__32"
	public final void mT__32() throws RecognitionException {
		try {
			int _type = T__32;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:20:7: ( '<=' )
			// src/Tiger.g:20:9: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__32"

	// $ANTLR start "T__33"
	public final void mT__33() throws RecognitionException {
		try {
			int _type = T__33;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:21:7: ( '<>' )
			// src/Tiger.g:21:9: '<>'
			{
			match("<>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__33"

	// $ANTLR start "T__34"
	public final void mT__34() throws RecognitionException {
		try {
			int _type = T__34;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:22:7: ( '=' )
			// src/Tiger.g:22:9: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__34"

	// $ANTLR start "T__35"
	public final void mT__35() throws RecognitionException {
		try {
			int _type = T__35;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:23:7: ( '>' )
			// src/Tiger.g:23:9: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__35"

	// $ANTLR start "T__36"
	public final void mT__36() throws RecognitionException {
		try {
			int _type = T__36;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:24:7: ( '>=' )
			// src/Tiger.g:24:9: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__36"

	// $ANTLR start "T__37"
	public final void mT__37() throws RecognitionException {
		try {
			int _type = T__37;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:25:7: ( '[' )
			// src/Tiger.g:25:9: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__37"

	// $ANTLR start "T__38"
	public final void mT__38() throws RecognitionException {
		try {
			int _type = T__38;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:26:7: ( ']' )
			// src/Tiger.g:26:9: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__38"

	// $ANTLR start "T__39"
	public final void mT__39() throws RecognitionException {
		try {
			int _type = T__39;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:27:7: ( 'array' )
			// src/Tiger.g:27:9: 'array'
			{
			match("array"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__39"

	// $ANTLR start "T__40"
	public final void mT__40() throws RecognitionException {
		try {
			int _type = T__40;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:28:7: ( 'break' )
			// src/Tiger.g:28:9: 'break'
			{
			match("break"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__40"

	// $ANTLR start "T__41"
	public final void mT__41() throws RecognitionException {
		try {
			int _type = T__41;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:29:7: ( 'do' )
			// src/Tiger.g:29:9: 'do'
			{
			match("do"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__41"

	// $ANTLR start "T__42"
	public final void mT__42() throws RecognitionException {
		try {
			int _type = T__42;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:30:7: ( 'else' )
			// src/Tiger.g:30:9: 'else'
			{
			match("else"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__42"

	// $ANTLR start "T__43"
	public final void mT__43() throws RecognitionException {
		try {
			int _type = T__43;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:31:7: ( 'end' )
			// src/Tiger.g:31:9: 'end'
			{
			match("end"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__43"

	// $ANTLR start "T__44"
	public final void mT__44() throws RecognitionException {
		try {
			int _type = T__44;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:32:7: ( 'for' )
			// src/Tiger.g:32:9: 'for'
			{
			match("for"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__44"

	// $ANTLR start "T__45"
	public final void mT__45() throws RecognitionException {
		try {
			int _type = T__45;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:33:7: ( 'function' )
			// src/Tiger.g:33:9: 'function'
			{
			match("function"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__45"

	// $ANTLR start "T__46"
	public final void mT__46() throws RecognitionException {
		try {
			int _type = T__46;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:34:7: ( 'if' )
			// src/Tiger.g:34:9: 'if'
			{
			match("if"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__46"

	// $ANTLR start "T__47"
	public final void mT__47() throws RecognitionException {
		try {
			int _type = T__47;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:35:7: ( 'in' )
			// src/Tiger.g:35:9: 'in'
			{
			match("in"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__47"

	// $ANTLR start "T__48"
	public final void mT__48() throws RecognitionException {
		try {
			int _type = T__48;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:36:7: ( 'let' )
			// src/Tiger.g:36:9: 'let'
			{
			match("let"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__48"

	// $ANTLR start "T__49"
	public final void mT__49() throws RecognitionException {
		try {
			int _type = T__49;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:37:7: ( 'nil' )
			// src/Tiger.g:37:9: 'nil'
			{
			match("nil"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__49"

	// $ANTLR start "T__50"
	public final void mT__50() throws RecognitionException {
		try {
			int _type = T__50;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:38:7: ( 'of' )
			// src/Tiger.g:38:9: 'of'
			{
			match("of"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__50"

	// $ANTLR start "T__51"
	public final void mT__51() throws RecognitionException {
		try {
			int _type = T__51;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:39:7: ( 'then' )
			// src/Tiger.g:39:9: 'then'
			{
			match("then"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__51"

	// $ANTLR start "T__52"
	public final void mT__52() throws RecognitionException {
		try {
			int _type = T__52;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:40:7: ( 'to' )
			// src/Tiger.g:40:9: 'to'
			{
			match("to"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__52"

	// $ANTLR start "T__53"
	public final void mT__53() throws RecognitionException {
		try {
			int _type = T__53;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:41:7: ( 'type' )
			// src/Tiger.g:41:9: 'type'
			{
			match("type"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__53"

	// $ANTLR start "T__54"
	public final void mT__54() throws RecognitionException {
		try {
			int _type = T__54;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:42:7: ( 'var' )
			// src/Tiger.g:42:9: 'var'
			{
			match("var"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__54"

	// $ANTLR start "T__55"
	public final void mT__55() throws RecognitionException {
		try {
			int _type = T__55;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:43:7: ( 'while' )
			// src/Tiger.g:43:9: 'while'
			{
			match("while"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__55"

	// $ANTLR start "T__56"
	public final void mT__56() throws RecognitionException {
		try {
			int _type = T__56;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:44:7: ( '{' )
			// src/Tiger.g:44:9: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__56"

	// $ANTLR start "T__57"
	public final void mT__57() throws RecognitionException {
		try {
			int _type = T__57;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:45:7: ( '|' )
			// src/Tiger.g:45:9: '|'
			{
			match('|'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__57"

	// $ANTLR start "T__58"
	public final void mT__58() throws RecognitionException {
		try {
			int _type = T__58;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:46:7: ( '}' )
			// src/Tiger.g:46:9: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__58"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:296:5: ( ( 'A' .. 'Z' | 'a' .. 'z' ) ( '0' .. '9' | 'A' .. 'Z' | '_' | 'a' .. 'z' )* )
			// src/Tiger.g:296:5: ( 'A' .. 'Z' | 'a' .. 'z' ) ( '0' .. '9' | 'A' .. 'Z' | '_' | 'a' .. 'z' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// src/Tiger.g:299:5: ( '0' .. '9' | 'A' .. 'Z' | '_' | 'a' .. 'z' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// src/Tiger.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop1;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "STR"
	public final void mSTR() throws RecognitionException {
		try {
			int _type = STR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:307:5: ( '\"' ( ' ' .. '!' | '#' .. '[' | ']' .. '~' | '\\\\' ( 'n' | 't' | '\"' | '\\\\' | '^' '@' .. '_' | '0' '0' .. '9' '0' .. '9' | '1' ( '0' .. '1' '0' .. '9' | '2' '0' .. '7' ) | ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ '\\\\' ) )* '\"' )
			// src/Tiger.g:307:5: '\"' ( ' ' .. '!' | '#' .. '[' | ']' .. '~' | '\\\\' ( 'n' | 't' | '\"' | '\\\\' | '^' '@' .. '_' | '0' '0' .. '9' '0' .. '9' | '1' ( '0' .. '1' '0' .. '9' | '2' '0' .. '7' ) | ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ '\\\\' ) )* '\"'
			{
			match('\"'); 
			// src/Tiger.g:308:5: ( ' ' .. '!' | '#' .. '[' | ']' .. '~' | '\\\\' ( 'n' | 't' | '\"' | '\\\\' | '^' '@' .. '_' | '0' '0' .. '9' '0' .. '9' | '1' ( '0' .. '1' '0' .. '9' | '2' '0' .. '7' ) | ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ '\\\\' ) )*
			loop5:
			while (true) {
				int alt5=5;
				switch ( input.LA(1) ) {
				case ' ':
				case '!':
					{
					alt5=1;
					}
					break;
				case '#':
				case '$':
				case '%':
				case '&':
				case '\'':
				case '(':
				case ')':
				case '*':
				case '+':
				case ',':
				case '-':
				case '.':
				case '/':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case ':':
				case ';':
				case '<':
				case '=':
				case '>':
				case '?':
				case '@':
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
				case 'G':
				case 'H':
				case 'I':
				case 'J':
				case 'K':
				case 'L':
				case 'M':
				case 'N':
				case 'O':
				case 'P':
				case 'Q':
				case 'R':
				case 'S':
				case 'T':
				case 'U':
				case 'V':
				case 'W':
				case 'X':
				case 'Y':
				case 'Z':
				case '[':
					{
					alt5=2;
					}
					break;
				case ']':
				case '^':
				case '_':
				case '`':
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
				case 'g':
				case 'h':
				case 'i':
				case 'j':
				case 'k':
				case 'l':
				case 'm':
				case 'n':
				case 'o':
				case 'p':
				case 'q':
				case 'r':
				case 's':
				case 't':
				case 'u':
				case 'v':
				case 'w':
				case 'x':
				case 'y':
				case 'z':
				case '{':
				case '|':
				case '}':
				case '~':
					{
					alt5=3;
					}
					break;
				case '\\':
					{
					alt5=4;
					}
					break;
				}
				switch (alt5) {
				case 1 :
					// src/Tiger.g:308:9: ' ' .. '!'
					{
					matchRange(' ','!'); 
					}
					break;
				case 2 :
					// src/Tiger.g:309:9: '#' .. '['
					{
					matchRange('#','['); 
					}
					break;
				case 3 :
					// src/Tiger.g:310:9: ']' .. '~'
					{
					matchRange(']','~'); 
					}
					break;
				case 4 :
					// src/Tiger.g:311:9: '\\\\' ( 'n' | 't' | '\"' | '\\\\' | '^' '@' .. '_' | '0' '0' .. '9' '0' .. '9' | '1' ( '0' .. '1' '0' .. '9' | '2' '0' .. '7' ) | ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ '\\\\' )
					{
					match('\\'); 
					// src/Tiger.g:312:9: ( 'n' | 't' | '\"' | '\\\\' | '^' '@' .. '_' | '0' '0' .. '9' '0' .. '9' | '1' ( '0' .. '1' '0' .. '9' | '2' '0' .. '7' ) | ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ '\\\\' )
					int alt4=8;
					switch ( input.LA(1) ) {
					case 'n':
						{
						alt4=1;
						}
						break;
					case 't':
						{
						alt4=2;
						}
						break;
					case '\"':
						{
						alt4=3;
						}
						break;
					case '\\':
						{
						alt4=4;
						}
						break;
					case '^':
						{
						alt4=5;
						}
						break;
					case '0':
						{
						alt4=6;
						}
						break;
					case '1':
						{
						alt4=7;
						}
						break;
					case '\t':
					case '\n':
					case '\f':
					case '\r':
					case ' ':
						{
						alt4=8;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 4, 0, input);
						throw nvae;
					}
					switch (alt4) {
						case 1 :
							// src/Tiger.g:312:13: 'n'
							{
							match('n'); 
							}
							break;
						case 2 :
							// src/Tiger.g:313:13: 't'
							{
							match('t'); 
							}
							break;
						case 3 :
							// src/Tiger.g:314:13: '\"'
							{
							match('\"'); 
							}
							break;
						case 4 :
							// src/Tiger.g:315:13: '\\\\'
							{
							match('\\'); 
							}
							break;
						case 5 :
							// src/Tiger.g:316:13: '^' '@' .. '_'
							{
							match('^'); 
							matchRange('@','_'); 
							}
							break;
						case 6 :
							// src/Tiger.g:318:13: '0' '0' .. '9' '0' .. '9'
							{
							match('0'); 
							matchRange('0','9'); 
							matchRange('0','9'); 
							}
							break;
						case 7 :
							// src/Tiger.g:321:13: '1' ( '0' .. '1' '0' .. '9' | '2' '0' .. '7' )
							{
							match('1'); 
							// src/Tiger.g:322:13: ( '0' .. '1' '0' .. '9' | '2' '0' .. '7' )
							int alt2=2;
							int LA2_0 = input.LA(1);
							if ( ((LA2_0 >= '0' && LA2_0 <= '1')) ) {
								alt2=1;
							}
							else if ( (LA2_0=='2') ) {
								alt2=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 2, 0, input);
								throw nvae;
							}

							switch (alt2) {
								case 1 :
									// src/Tiger.g:322:17: '0' .. '1' '0' .. '9'
									{
									matchRange('0','1'); 
									matchRange('0','9'); 
									}
									break;
								case 2 :
									// src/Tiger.g:324:17: '2' '0' .. '7'
									{
									match('2'); 
									matchRange('0','7'); 
									}
									break;

							}

							}
							break;
						case 8 :
							// src/Tiger.g:327:13: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ '\\\\'
							{
							// src/Tiger.g:327:13: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
							int cnt3=0;
							loop3:
							while (true) {
								int alt3=2;
								int LA3_0 = input.LA(1);
								if ( ((LA3_0 >= '\t' && LA3_0 <= '\n')||(LA3_0 >= '\f' && LA3_0 <= '\r')||LA3_0==' ') ) {
									alt3=1;
								}

								switch (alt3) {
								case 1 :
									// src/Tiger.g:
									{
									if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
										input.consume();
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										recover(mse);
										throw mse;
									}
									}
									break;

								default :
									if ( cnt3 >= 1 ) break loop3;
									EarlyExitException eee = new EarlyExitException(3, input);
									throw eee;
								}
								cnt3++;
							}

							match('\\'); 
							}
							break;

					}

					}
					break;

				default :
					break loop5;
				}
			}

			match('\"'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STR"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:340:5: ( ( '0' .. '9' )+ )
			// src/Tiger.g:340:5: ( '0' .. '9' )+
			{
			// src/Tiger.g:340:5: ( '0' .. '9' )+
			int cnt6=0;
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// src/Tiger.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt6 >= 1 ) break loop6;
					EarlyExitException eee = new EarlyExitException(6, input);
					throw eee;
				}
				cnt6++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:344:5: ( ( '/*' ( . )* ( COMMENT ( . )* )* '*/' ) )
			// src/Tiger.g:344:5: ( '/*' ( . )* ( COMMENT ( . )* )* '*/' )
			{
			// src/Tiger.g:344:5: ( '/*' ( . )* ( COMMENT ( . )* )* '*/' )
			// src/Tiger.g:345:9: '/*' ( . )* ( COMMENT ( . )* )* '*/'
			{
			match("/*"); 

			// src/Tiger.g:346:9: ( . )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0=='/') ) {
					int LA7_1 = input.LA(2);
					if ( (LA7_1=='*') ) {
						alt7=2;
					}
					else if ( ((LA7_1 >= '\u0000' && LA7_1 <= ')')||(LA7_1 >= '+' && LA7_1 <= '\uFFFF')) ) {
						alt7=1;
					}

				}
				else if ( (LA7_0=='*') ) {
					int LA7_2 = input.LA(2);
					if ( (LA7_2=='/') ) {
						alt7=2;
					}
					else if ( ((LA7_2 >= '\u0000' && LA7_2 <= '.')||(LA7_2 >= '0' && LA7_2 <= '\uFFFF')) ) {
						alt7=1;
					}

				}
				else if ( ((LA7_0 >= '\u0000' && LA7_0 <= ')')||(LA7_0 >= '+' && LA7_0 <= '.')||(LA7_0 >= '0' && LA7_0 <= '\uFFFF')) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// src/Tiger.g:346:9: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop7;
				}
			}

			// src/Tiger.g:347:9: ( COMMENT ( . )* )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0=='/') ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// src/Tiger.g:347:13: COMMENT ( . )*
					{
					mCOMMENT(); 

					// src/Tiger.g:348:13: ( . )*
					loop8:
					while (true) {
						int alt8=2;
						int LA8_0 = input.LA(1);
						if ( (LA8_0=='*') ) {
							int LA8_1 = input.LA(2);
							if ( (LA8_1=='/') ) {
								alt8=2;
							}
							else if ( ((LA8_1 >= '\u0000' && LA8_1 <= '.')||(LA8_1 >= '0' && LA8_1 <= '\uFFFF')) ) {
								alt8=1;
							}

						}
						else if ( (LA8_0=='/') ) {
							int LA8_2 = input.LA(2);
							if ( (LA8_2=='*') ) {
								alt8=2;
							}
							else if ( ((LA8_2 >= '\u0000' && LA8_2 <= ')')||(LA8_2 >= '+' && LA8_2 <= '\uFFFF')) ) {
								alt8=1;
							}

						}
						else if ( ((LA8_0 >= '\u0000' && LA8_0 <= ')')||(LA8_0 >= '+' && LA8_0 <= '.')||(LA8_0 >= '0' && LA8_0 <= '\uFFFF')) ) {
							alt8=1;
						}

						switch (alt8) {
						case 1 :
							// src/Tiger.g:348:13: .
							{
							matchAny(); 
							}
							break;

						default :
							break loop8;
						}
					}

					}
					break;

				default :
					break loop9;
				}
			}

			match("*/"); 

			}

			_channel = HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/Tiger.g:356:5: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ )
			// src/Tiger.g:356:5: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
			{
			// src/Tiger.g:356:5: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
			int cnt10=0;
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( ((LA10_0 >= '\t' && LA10_0 <= '\n')||(LA10_0 >= '\f' && LA10_0 <= '\r')||LA10_0==' ') ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// src/Tiger.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt10 >= 1 ) break loop10;
					EarlyExitException eee = new EarlyExitException(10, input);
					throw eee;
				}
				cnt10++;
			}

			_channel = HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// src/Tiger.g:1:8: ( T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | ID | STR | INT | COMMENT | WS )
		int alt11=45;
		alt11 = dfa11.predict(input);
		switch (alt11) {
			case 1 :
				// src/Tiger.g:1:10: T__19
				{
				mT__19(); 

				}
				break;
			case 2 :
				// src/Tiger.g:1:16: T__20
				{
				mT__20(); 

				}
				break;
			case 3 :
				// src/Tiger.g:1:22: T__21
				{
				mT__21(); 

				}
				break;
			case 4 :
				// src/Tiger.g:1:28: T__22
				{
				mT__22(); 

				}
				break;
			case 5 :
				// src/Tiger.g:1:34: T__23
				{
				mT__23(); 

				}
				break;
			case 6 :
				// src/Tiger.g:1:40: T__24
				{
				mT__24(); 

				}
				break;
			case 7 :
				// src/Tiger.g:1:46: T__25
				{
				mT__25(); 

				}
				break;
			case 8 :
				// src/Tiger.g:1:52: T__26
				{
				mT__26(); 

				}
				break;
			case 9 :
				// src/Tiger.g:1:58: T__27
				{
				mT__27(); 

				}
				break;
			case 10 :
				// src/Tiger.g:1:64: T__28
				{
				mT__28(); 

				}
				break;
			case 11 :
				// src/Tiger.g:1:70: T__29
				{
				mT__29(); 

				}
				break;
			case 12 :
				// src/Tiger.g:1:76: T__30
				{
				mT__30(); 

				}
				break;
			case 13 :
				// src/Tiger.g:1:82: T__31
				{
				mT__31(); 

				}
				break;
			case 14 :
				// src/Tiger.g:1:88: T__32
				{
				mT__32(); 

				}
				break;
			case 15 :
				// src/Tiger.g:1:94: T__33
				{
				mT__33(); 

				}
				break;
			case 16 :
				// src/Tiger.g:1:100: T__34
				{
				mT__34(); 

				}
				break;
			case 17 :
				// src/Tiger.g:1:106: T__35
				{
				mT__35(); 

				}
				break;
			case 18 :
				// src/Tiger.g:1:112: T__36
				{
				mT__36(); 

				}
				break;
			case 19 :
				// src/Tiger.g:1:118: T__37
				{
				mT__37(); 

				}
				break;
			case 20 :
				// src/Tiger.g:1:124: T__38
				{
				mT__38(); 

				}
				break;
			case 21 :
				// src/Tiger.g:1:130: T__39
				{
				mT__39(); 

				}
				break;
			case 22 :
				// src/Tiger.g:1:136: T__40
				{
				mT__40(); 

				}
				break;
			case 23 :
				// src/Tiger.g:1:142: T__41
				{
				mT__41(); 

				}
				break;
			case 24 :
				// src/Tiger.g:1:148: T__42
				{
				mT__42(); 

				}
				break;
			case 25 :
				// src/Tiger.g:1:154: T__43
				{
				mT__43(); 

				}
				break;
			case 26 :
				// src/Tiger.g:1:160: T__44
				{
				mT__44(); 

				}
				break;
			case 27 :
				// src/Tiger.g:1:166: T__45
				{
				mT__45(); 

				}
				break;
			case 28 :
				// src/Tiger.g:1:172: T__46
				{
				mT__46(); 

				}
				break;
			case 29 :
				// src/Tiger.g:1:178: T__47
				{
				mT__47(); 

				}
				break;
			case 30 :
				// src/Tiger.g:1:184: T__48
				{
				mT__48(); 

				}
				break;
			case 31 :
				// src/Tiger.g:1:190: T__49
				{
				mT__49(); 

				}
				break;
			case 32 :
				// src/Tiger.g:1:196: T__50
				{
				mT__50(); 

				}
				break;
			case 33 :
				// src/Tiger.g:1:202: T__51
				{
				mT__51(); 

				}
				break;
			case 34 :
				// src/Tiger.g:1:208: T__52
				{
				mT__52(); 

				}
				break;
			case 35 :
				// src/Tiger.g:1:214: T__53
				{
				mT__53(); 

				}
				break;
			case 36 :
				// src/Tiger.g:1:220: T__54
				{
				mT__54(); 

				}
				break;
			case 37 :
				// src/Tiger.g:1:226: T__55
				{
				mT__55(); 

				}
				break;
			case 38 :
				// src/Tiger.g:1:232: T__56
				{
				mT__56(); 

				}
				break;
			case 39 :
				// src/Tiger.g:1:238: T__57
				{
				mT__57(); 

				}
				break;
			case 40 :
				// src/Tiger.g:1:244: T__58
				{
				mT__58(); 

				}
				break;
			case 41 :
				// src/Tiger.g:1:250: ID
				{
				mID(); 

				}
				break;
			case 42 :
				// src/Tiger.g:1:253: STR
				{
				mSTR(); 

				}
				break;
			case 43 :
				// src/Tiger.g:1:257: INT
				{
				mINT(); 

				}
				break;
			case 44 :
				// src/Tiger.g:1:261: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 45 :
				// src/Tiger.g:1:269: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA11 dfa11 = new DFA11(this);
	static final String DFA11_eotS =
		"\11\uffff\1\45\1\47\1\uffff\1\52\1\uffff\1\54\2\uffff\14\40\20\uffff\2"+
		"\40\1\100\4\40\1\105\1\106\2\40\1\111\1\40\1\113\5\40\1\uffff\1\40\1\122"+
		"\1\123\1\40\2\uffff\1\125\1\126\1\uffff\1\40\1\uffff\1\40\1\131\3\40\1"+
		"\135\2\uffff\1\40\2\uffff\1\137\1\140\1\uffff\1\40\1\142\1\143\1\uffff"+
		"\1\40\2\uffff\1\145\2\uffff\1\40\1\uffff\1\40\1\150\1\uffff";
	static final String DFA11_eofS =
		"\151\uffff";
	static final String DFA11_minS =
		"\1\11\10\uffff\1\52\1\75\1\uffff\1\75\1\uffff\1\75\2\uffff\2\162\1\157"+
		"\1\154\1\157\1\146\1\145\1\151\1\146\1\150\1\141\1\150\20\uffff\1\162"+
		"\1\145\1\60\1\163\1\144\1\162\1\156\2\60\1\164\1\154\1\60\1\145\1\60\1"+
		"\160\1\162\1\151\2\141\1\uffff\1\145\2\60\1\143\2\uffff\2\60\1\uffff\1"+
		"\156\1\uffff\1\145\1\60\1\154\1\171\1\153\1\60\2\uffff\1\164\2\uffff\2"+
		"\60\1\uffff\1\145\2\60\1\uffff\1\151\2\uffff\1\60\2\uffff\1\157\1\uffff"+
		"\1\156\1\60\1\uffff";
	static final String DFA11_maxS =
		"\1\175\10\uffff\1\52\1\75\1\uffff\1\76\1\uffff\1\75\2\uffff\2\162\1\157"+
		"\1\156\1\165\1\156\1\145\1\151\1\146\1\171\1\141\1\150\20\uffff\1\162"+
		"\1\145\1\172\1\163\1\144\1\162\1\156\2\172\1\164\1\154\1\172\1\145\1\172"+
		"\1\160\1\162\1\151\2\141\1\uffff\1\145\2\172\1\143\2\uffff\2\172\1\uffff"+
		"\1\156\1\uffff\1\145\1\172\1\154\1\171\1\153\1\172\2\uffff\1\164\2\uffff"+
		"\2\172\1\uffff\1\145\2\172\1\uffff\1\151\2\uffff\1\172\2\uffff\1\157\1"+
		"\uffff\1\156\1\172\1\uffff";
	static final String DFA11_acceptS =
		"\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\14\1\uffff\1\20\1"+
		"\uffff\1\23\1\24\14\uffff\1\46\1\47\1\50\1\51\1\52\1\53\1\55\1\54\1\11"+
		"\1\13\1\12\1\16\1\17\1\15\1\22\1\21\23\uffff\1\27\4\uffff\1\34\1\35\2"+
		"\uffff\1\40\1\uffff\1\42\6\uffff\1\31\1\32\1\uffff\1\36\1\37\2\uffff\1"+
		"\44\3\uffff\1\30\1\uffff\1\41\1\43\1\uffff\1\25\1\26\1\uffff\1\45\2\uffff"+
		"\1\33";
	static final String DFA11_specialS =
		"\151\uffff}>";
	static final String[] DFA11_transitionS = {
			"\2\43\1\uffff\2\43\22\uffff\1\43\1\uffff\1\41\3\uffff\1\1\1\uffff\1\2"+
			"\1\3\1\4\1\5\1\6\1\7\1\10\1\11\12\42\1\12\1\13\1\14\1\15\1\16\2\uffff"+
			"\32\40\1\17\1\uffff\1\20\3\uffff\1\21\1\22\1\40\1\23\1\24\1\25\2\40\1"+
			"\26\2\40\1\27\1\40\1\30\1\31\4\40\1\32\1\40\1\33\1\34\3\40\1\35\1\36"+
			"\1\37",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\44",
			"\1\46",
			"",
			"\1\50\1\51",
			"",
			"\1\53",
			"",
			"",
			"\1\55",
			"\1\56",
			"\1\57",
			"\1\60\1\uffff\1\61",
			"\1\62\5\uffff\1\63",
			"\1\64\7\uffff\1\65",
			"\1\66",
			"\1\67",
			"\1\70",
			"\1\71\6\uffff\1\72\11\uffff\1\73",
			"\1\74",
			"\1\75",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\76",
			"\1\77",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\1\101",
			"\1\102",
			"\1\103",
			"\1\104",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\1\107",
			"\1\110",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\1\112",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\1\114",
			"\1\115",
			"\1\116",
			"\1\117",
			"\1\120",
			"",
			"\1\121",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\1\124",
			"",
			"",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"",
			"\1\127",
			"",
			"\1\130",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\1\132",
			"\1\133",
			"\1\134",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"",
			"",
			"\1\136",
			"",
			"",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"",
			"\1\141",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"",
			"\1\144",
			"",
			"",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			"",
			"",
			"\1\146",
			"",
			"\1\147",
			"\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
			""
	};

	static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
	static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
	static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
	static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
	static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
	static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
	static final short[][] DFA11_transition;

	static {
		int numStates = DFA11_transitionS.length;
		DFA11_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
		}
	}

	protected class DFA11 extends DFA {

		public DFA11(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 11;
			this.eot = DFA11_eot;
			this.eof = DFA11_eof;
			this.min = DFA11_min;
			this.max = DFA11_max;
			this.accept = DFA11_accept;
			this.special = DFA11_special;
			this.transition = DFA11_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | ID | STR | INT | COMMENT | WS );";
		}
	}

}
