// $ANTLR 3.5.2 src/Tiger.g 2018-11-30 09:11:48

package parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class TigerParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ARR", "ARRTYPE", "CALL", "CALLTYPE", 
		"COMMENT", "DEC", "FIELD", "ID", "INT", "ITEM", "REC", "RECTYPE", "SEQ", 
		"STR", "WS", "'&'", "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", 
		"':'", "':='", "';'", "'<'", "'<='", "'<>'", "'='", "'>'", "'>='", "'['", 
		"']'", "'array'", "'break'", "'do'", "'else'", "'end'", "'for'", "'function'", 
		"'if'", "'in'", "'let'", "'nil'", "'of'", "'then'", "'to'", "'type'", 
		"'var'", "'while'", "'{'", "'|'", "'}'"
	};
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
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public TigerParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public TigerParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return TigerParser.tokenNames; }
	@Override public String getGrammarFileName() { return "src/Tiger.g"; }


	public static class program_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "program"
	// src/Tiger.g:23:1: program : exp EOF !;
	public final TigerParser.program_return program() throws RecognitionException {
		TigerParser.program_return retval = new TigerParser.program_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EOF2=null;
		ParserRuleReturnScope exp1 =null;

		Object EOF2_tree=null;

		try {
			// src/Tiger.g:24:5: ( exp EOF !)
			// src/Tiger.g:24:5: exp EOF !
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_exp_in_program137);
			exp1=exp();
			state._fsp--;

			adaptor.addChild(root_0, exp1.getTree());

			EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_program139); 
			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "program"


	public static class exp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "exp"
	// src/Tiger.g:27:1: exp : orExp ( ':=' ^ exp )? ;
	public final TigerParser.exp_return exp() throws RecognitionException {
		TigerParser.exp_return retval = new TigerParser.exp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal4=null;
		ParserRuleReturnScope orExp3 =null;
		ParserRuleReturnScope exp5 =null;

		Object string_literal4_tree=null;

		try {
			// src/Tiger.g:28:5: ( orExp ( ':=' ^ exp )? )
			// src/Tiger.g:28:5: orExp ( ':=' ^ exp )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_orExp_in_exp151);
			orExp3=orExp();
			state._fsp--;

			adaptor.addChild(root_0, orExp3.getTree());

			// src/Tiger.g:29:5: ( ':=' ^ exp )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==29) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// src/Tiger.g:29:9: ':=' ^ exp
					{
					string_literal4=(Token)match(input,29,FOLLOW_29_in_exp161); 
					string_literal4_tree = (Object)adaptor.create(string_literal4);
					root_0 = (Object)adaptor.becomeRoot(string_literal4_tree, root_0);

					pushFollow(FOLLOW_exp_in_exp172);
					exp5=exp();
					state._fsp--;

					adaptor.addChild(root_0, exp5.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "exp"


	public static class orExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "orExp"
	// src/Tiger.g:34:1: orExp : andExp ( '|' ^ andExp ( '|' ! andExp )* )? ;
	public final TigerParser.orExp_return orExp() throws RecognitionException {
		TigerParser.orExp_return retval = new TigerParser.orExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal7=null;
		Token char_literal9=null;
		ParserRuleReturnScope andExp6 =null;
		ParserRuleReturnScope andExp8 =null;
		ParserRuleReturnScope andExp10 =null;

		Object char_literal7_tree=null;
		Object char_literal9_tree=null;

		try {
			// src/Tiger.g:35:5: ( andExp ( '|' ^ andExp ( '|' ! andExp )* )? )
			// src/Tiger.g:35:5: andExp ( '|' ^ andExp ( '|' ! andExp )* )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_andExp_in_orExp190);
			andExp6=andExp();
			state._fsp--;

			adaptor.addChild(root_0, andExp6.getTree());

			// src/Tiger.g:36:5: ( '|' ^ andExp ( '|' ! andExp )* )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==57) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// src/Tiger.g:36:9: '|' ^ andExp ( '|' ! andExp )*
					{
					char_literal7=(Token)match(input,57,FOLLOW_57_in_orExp200); 
					char_literal7_tree = (Object)adaptor.create(char_literal7);
					root_0 = (Object)adaptor.becomeRoot(char_literal7_tree, root_0);

					pushFollow(FOLLOW_andExp_in_orExp211);
					andExp8=andExp();
					state._fsp--;

					adaptor.addChild(root_0, andExp8.getTree());

					// src/Tiger.g:38:9: ( '|' ! andExp )*
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( (LA2_0==57) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// src/Tiger.g:38:13: '|' ! andExp
							{
							char_literal9=(Token)match(input,57,FOLLOW_57_in_orExp225); 
							pushFollow(FOLLOW_andExp_in_orExp240);
							andExp10=andExp();
							state._fsp--;

							adaptor.addChild(root_0, andExp10.getTree());

							}
							break;

						default :
							break loop2;
						}
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "orExp"


	public static class andExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "andExp"
	// src/Tiger.g:44:1: andExp : compExp ( '&' ^ compExp ( '&' ! compExp )* )? ;
	public final TigerParser.andExp_return andExp() throws RecognitionException {
		TigerParser.andExp_return retval = new TigerParser.andExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal12=null;
		Token char_literal14=null;
		ParserRuleReturnScope compExp11 =null;
		ParserRuleReturnScope compExp13 =null;
		ParserRuleReturnScope compExp15 =null;

		Object char_literal12_tree=null;
		Object char_literal14_tree=null;

		try {
			// src/Tiger.g:45:5: ( compExp ( '&' ^ compExp ( '&' ! compExp )* )? )
			// src/Tiger.g:45:5: compExp ( '&' ^ compExp ( '&' ! compExp )* )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_compExp_in_andExp269);
			compExp11=compExp();
			state._fsp--;

			adaptor.addChild(root_0, compExp11.getTree());

			// src/Tiger.g:46:5: ( '&' ^ compExp ( '&' ! compExp )* )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==19) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// src/Tiger.g:46:9: '&' ^ compExp ( '&' ! compExp )*
					{
					char_literal12=(Token)match(input,19,FOLLOW_19_in_andExp279); 
					char_literal12_tree = (Object)adaptor.create(char_literal12);
					root_0 = (Object)adaptor.becomeRoot(char_literal12_tree, root_0);

					pushFollow(FOLLOW_compExp_in_andExp290);
					compExp13=compExp();
					state._fsp--;

					adaptor.addChild(root_0, compExp13.getTree());

					// src/Tiger.g:48:9: ( '&' ! compExp )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( (LA4_0==19) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// src/Tiger.g:48:13: '&' ! compExp
							{
							char_literal14=(Token)match(input,19,FOLLOW_19_in_andExp304); 
							pushFollow(FOLLOW_compExp_in_andExp319);
							compExp15=compExp();
							state._fsp--;

							adaptor.addChild(root_0, compExp15.getTree());

							}
							break;

						default :
							break loop4;
						}
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "andExp"


	public static class compExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "compExp"
	// src/Tiger.g:54:1: compExp : addExp ( ( '=' | '<>' | '>' | '<' | '>=' | '<=' ) ^ addExp )? ;
	public final TigerParser.compExp_return compExp() throws RecognitionException {
		TigerParser.compExp_return retval = new TigerParser.compExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set17=null;
		ParserRuleReturnScope addExp16 =null;
		ParserRuleReturnScope addExp18 =null;

		Object set17_tree=null;

		try {
			// src/Tiger.g:55:5: ( addExp ( ( '=' | '<>' | '>' | '<' | '>=' | '<=' ) ^ addExp )? )
			// src/Tiger.g:55:5: addExp ( ( '=' | '<>' | '>' | '<' | '>=' | '<=' ) ^ addExp )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_addExp_in_compExp348);
			addExp16=addExp();
			state._fsp--;

			adaptor.addChild(root_0, addExp16.getTree());

			// src/Tiger.g:56:5: ( ( '=' | '<>' | '>' | '<' | '>=' | '<=' ) ^ addExp )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( ((LA6_0 >= 31 && LA6_0 <= 36)) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// src/Tiger.g:56:9: ( '=' | '<>' | '>' | '<' | '>=' | '<=' ) ^ addExp
					{
					set17=input.LT(1);
					set17=input.LT(1);
					if ( (input.LA(1) >= 31 && input.LA(1) <= 36) ) {
						input.consume();
						root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set17), root_0);
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_addExp_in_compExp453);
					addExp18=addExp();
					state._fsp--;

					adaptor.addChild(root_0, addExp18.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "compExp"


	public static class addExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "addExp"
	// src/Tiger.g:67:1: addExp : mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )? ( '-' ^ mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )? )* ;
	public final TigerParser.addExp_return addExp() throws RecognitionException {
		TigerParser.addExp_return retval = new TigerParser.addExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal20=null;
		Token char_literal22=null;
		Token char_literal24=null;
		Token char_literal26=null;
		Token char_literal28=null;
		ParserRuleReturnScope mulExp19 =null;
		ParserRuleReturnScope mulExp21 =null;
		ParserRuleReturnScope mulExp23 =null;
		ParserRuleReturnScope mulExp25 =null;
		ParserRuleReturnScope mulExp27 =null;
		ParserRuleReturnScope mulExp29 =null;

		Object char_literal20_tree=null;
		Object char_literal22_tree=null;
		Object char_literal24_tree=null;
		Object char_literal26_tree=null;
		Object char_literal28_tree=null;

		try {
			// src/Tiger.g:68:5: ( mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )? ( '-' ^ mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )? )* )
			// src/Tiger.g:68:5: mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )? ( '-' ^ mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )? )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_mulExp_in_addExp471);
			mulExp19=mulExp();
			state._fsp--;

			adaptor.addChild(root_0, mulExp19.getTree());

			// src/Tiger.g:69:5: ( '+' ^ mulExp ( '+' ! mulExp )* )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==23) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// src/Tiger.g:69:9: '+' ^ mulExp ( '+' ! mulExp )*
					{
					char_literal20=(Token)match(input,23,FOLLOW_23_in_addExp481); 
					char_literal20_tree = (Object)adaptor.create(char_literal20);
					root_0 = (Object)adaptor.becomeRoot(char_literal20_tree, root_0);

					pushFollow(FOLLOW_mulExp_in_addExp492);
					mulExp21=mulExp();
					state._fsp--;

					adaptor.addChild(root_0, mulExp21.getTree());

					// src/Tiger.g:71:9: ( '+' ! mulExp )*
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( (LA7_0==23) ) {
							alt7=1;
						}

						switch (alt7) {
						case 1 :
							// src/Tiger.g:71:13: '+' ! mulExp
							{
							char_literal22=(Token)match(input,23,FOLLOW_23_in_addExp506); 
							pushFollow(FOLLOW_mulExp_in_addExp521);
							mulExp23=mulExp();
							state._fsp--;

							adaptor.addChild(root_0, mulExp23.getTree());

							}
							break;

						default :
							break loop7;
						}
					}

					}
					break;

			}

			// src/Tiger.g:75:5: ( '-' ^ mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )? )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==25) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// src/Tiger.g:75:9: '-' ^ mulExp ( '+' ^ mulExp ( '+' ! mulExp )* )?
					{
					char_literal24=(Token)match(input,25,FOLLOW_25_in_addExp549); 
					char_literal24_tree = (Object)adaptor.create(char_literal24);
					root_0 = (Object)adaptor.becomeRoot(char_literal24_tree, root_0);

					pushFollow(FOLLOW_mulExp_in_addExp560);
					mulExp25=mulExp();
					state._fsp--;

					adaptor.addChild(root_0, mulExp25.getTree());

					// src/Tiger.g:77:9: ( '+' ^ mulExp ( '+' ! mulExp )* )?
					int alt10=2;
					int LA10_0 = input.LA(1);
					if ( (LA10_0==23) ) {
						alt10=1;
					}
					switch (alt10) {
						case 1 :
							// src/Tiger.g:77:13: '+' ^ mulExp ( '+' ! mulExp )*
							{
							char_literal26=(Token)match(input,23,FOLLOW_23_in_addExp574); 
							char_literal26_tree = (Object)adaptor.create(char_literal26);
							root_0 = (Object)adaptor.becomeRoot(char_literal26_tree, root_0);

							pushFollow(FOLLOW_mulExp_in_addExp589);
							mulExp27=mulExp();
							state._fsp--;

							adaptor.addChild(root_0, mulExp27.getTree());

							// src/Tiger.g:79:13: ( '+' ! mulExp )*
							loop9:
							while (true) {
								int alt9=2;
								int LA9_0 = input.LA(1);
								if ( (LA9_0==23) ) {
									alt9=1;
								}

								switch (alt9) {
								case 1 :
									// src/Tiger.g:79:17: '+' ! mulExp
									{
									char_literal28=(Token)match(input,23,FOLLOW_23_in_addExp607); 
									pushFollow(FOLLOW_mulExp_in_addExp626);
									mulExp29=mulExp();
									state._fsp--;

									adaptor.addChild(root_0, mulExp29.getTree());

									}
									break;

								default :
									break loop9;
								}
							}

							}
							break;

					}

					}
					break;

				default :
					break loop11;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "addExp"


	public static class mulExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mulExp"
	// src/Tiger.g:86:1: mulExp : unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )? ( '/' ^ unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )? )* ;
	public final TigerParser.mulExp_return mulExp() throws RecognitionException {
		TigerParser.mulExp_return retval = new TigerParser.mulExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal31=null;
		Token char_literal33=null;
		Token char_literal35=null;
		Token char_literal37=null;
		Token char_literal39=null;
		ParserRuleReturnScope unaryExp30 =null;
		ParserRuleReturnScope unaryExp32 =null;
		ParserRuleReturnScope unaryExp34 =null;
		ParserRuleReturnScope unaryExp36 =null;
		ParserRuleReturnScope unaryExp38 =null;
		ParserRuleReturnScope unaryExp40 =null;

		Object char_literal31_tree=null;
		Object char_literal33_tree=null;
		Object char_literal35_tree=null;
		Object char_literal37_tree=null;
		Object char_literal39_tree=null;

		try {
			// src/Tiger.g:87:5: ( unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )? ( '/' ^ unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )? )* )
			// src/Tiger.g:87:5: unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )? ( '/' ^ unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )? )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_unaryExp_in_mulExp670);
			unaryExp30=unaryExp();
			state._fsp--;

			adaptor.addChild(root_0, unaryExp30.getTree());

			// src/Tiger.g:88:5: ( '*' ^ unaryExp ( '*' ! unaryExp )* )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==22) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// src/Tiger.g:88:9: '*' ^ unaryExp ( '*' ! unaryExp )*
					{
					char_literal31=(Token)match(input,22,FOLLOW_22_in_mulExp680); 
					char_literal31_tree = (Object)adaptor.create(char_literal31);
					root_0 = (Object)adaptor.becomeRoot(char_literal31_tree, root_0);

					pushFollow(FOLLOW_unaryExp_in_mulExp691);
					unaryExp32=unaryExp();
					state._fsp--;

					adaptor.addChild(root_0, unaryExp32.getTree());

					// src/Tiger.g:90:9: ( '*' ! unaryExp )*
					loop12:
					while (true) {
						int alt12=2;
						int LA12_0 = input.LA(1);
						if ( (LA12_0==22) ) {
							alt12=1;
						}

						switch (alt12) {
						case 1 :
							// src/Tiger.g:90:13: '*' ! unaryExp
							{
							char_literal33=(Token)match(input,22,FOLLOW_22_in_mulExp705); 
							pushFollow(FOLLOW_unaryExp_in_mulExp720);
							unaryExp34=unaryExp();
							state._fsp--;

							adaptor.addChild(root_0, unaryExp34.getTree());

							}
							break;

						default :
							break loop12;
						}
					}

					}
					break;

			}

			// src/Tiger.g:94:5: ( '/' ^ unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )? )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( (LA16_0==27) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// src/Tiger.g:94:9: '/' ^ unaryExp ( '*' ^ unaryExp ( '*' ! unaryExp )* )?
					{
					char_literal35=(Token)match(input,27,FOLLOW_27_in_mulExp748); 
					char_literal35_tree = (Object)adaptor.create(char_literal35);
					root_0 = (Object)adaptor.becomeRoot(char_literal35_tree, root_0);

					pushFollow(FOLLOW_unaryExp_in_mulExp759);
					unaryExp36=unaryExp();
					state._fsp--;

					adaptor.addChild(root_0, unaryExp36.getTree());

					// src/Tiger.g:96:9: ( '*' ^ unaryExp ( '*' ! unaryExp )* )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0==22) ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// src/Tiger.g:96:13: '*' ^ unaryExp ( '*' ! unaryExp )*
							{
							char_literal37=(Token)match(input,22,FOLLOW_22_in_mulExp773); 
							char_literal37_tree = (Object)adaptor.create(char_literal37);
							root_0 = (Object)adaptor.becomeRoot(char_literal37_tree, root_0);

							pushFollow(FOLLOW_unaryExp_in_mulExp788);
							unaryExp38=unaryExp();
							state._fsp--;

							adaptor.addChild(root_0, unaryExp38.getTree());

							// src/Tiger.g:98:13: ( '*' ! unaryExp )*
							loop14:
							while (true) {
								int alt14=2;
								int LA14_0 = input.LA(1);
								if ( (LA14_0==22) ) {
									alt14=1;
								}

								switch (alt14) {
								case 1 :
									// src/Tiger.g:98:17: '*' ! unaryExp
									{
									char_literal39=(Token)match(input,22,FOLLOW_22_in_mulExp806); 
									pushFollow(FOLLOW_unaryExp_in_mulExp825);
									unaryExp40=unaryExp();
									state._fsp--;

									adaptor.addChild(root_0, unaryExp40.getTree());

									}
									break;

								default :
									break loop14;
								}
							}

							}
							break;

					}

					}
					break;

				default :
					break loop16;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "mulExp"


	public static class unaryExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "unaryExp"
	// src/Tiger.g:105:1: unaryExp : ( seqExp | negExp | valueExp | ifExp | whileExp | forExp | letExp | STR | INT | 'nil' | 'break' );
	public final TigerParser.unaryExp_return unaryExp() throws RecognitionException {
		TigerParser.unaryExp_return retval = new TigerParser.unaryExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STR48=null;
		Token INT49=null;
		Token string_literal50=null;
		Token string_literal51=null;
		ParserRuleReturnScope seqExp41 =null;
		ParserRuleReturnScope negExp42 =null;
		ParserRuleReturnScope valueExp43 =null;
		ParserRuleReturnScope ifExp44 =null;
		ParserRuleReturnScope whileExp45 =null;
		ParserRuleReturnScope forExp46 =null;
		ParserRuleReturnScope letExp47 =null;

		Object STR48_tree=null;
		Object INT49_tree=null;
		Object string_literal50_tree=null;
		Object string_literal51_tree=null;

		try {
			// src/Tiger.g:106:5: ( seqExp | negExp | valueExp | ifExp | whileExp | forExp | letExp | STR | INT | 'nil' | 'break' )
			int alt17=11;
			switch ( input.LA(1) ) {
			case 20:
				{
				alt17=1;
				}
				break;
			case 25:
				{
				alt17=2;
				}
				break;
			case ID:
				{
				alt17=3;
				}
				break;
			case 46:
				{
				alt17=4;
				}
				break;
			case 55:
				{
				alt17=5;
				}
				break;
			case 44:
				{
				alt17=6;
				}
				break;
			case 48:
				{
				alt17=7;
				}
				break;
			case STR:
				{
				alt17=8;
				}
				break;
			case INT:
				{
				alt17=9;
				}
				break;
			case 49:
				{
				alt17=10;
				}
				break;
			case 40:
				{
				alt17=11;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 17, 0, input);
				throw nvae;
			}
			switch (alt17) {
				case 1 :
					// src/Tiger.g:106:5: seqExp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_seqExp_in_unaryExp869);
					seqExp41=seqExp();
					state._fsp--;

					adaptor.addChild(root_0, seqExp41.getTree());

					}
					break;
				case 2 :
					// src/Tiger.g:107:5: negExp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_negExp_in_unaryExp875);
					negExp42=negExp();
					state._fsp--;

					adaptor.addChild(root_0, negExp42.getTree());

					}
					break;
				case 3 :
					// src/Tiger.g:108:5: valueExp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_valueExp_in_unaryExp881);
					valueExp43=valueExp();
					state._fsp--;

					adaptor.addChild(root_0, valueExp43.getTree());

					}
					break;
				case 4 :
					// src/Tiger.g:109:5: ifExp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_ifExp_in_unaryExp887);
					ifExp44=ifExp();
					state._fsp--;

					adaptor.addChild(root_0, ifExp44.getTree());

					}
					break;
				case 5 :
					// src/Tiger.g:110:5: whileExp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_whileExp_in_unaryExp893);
					whileExp45=whileExp();
					state._fsp--;

					adaptor.addChild(root_0, whileExp45.getTree());

					}
					break;
				case 6 :
					// src/Tiger.g:111:5: forExp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_forExp_in_unaryExp899);
					forExp46=forExp();
					state._fsp--;

					adaptor.addChild(root_0, forExp46.getTree());

					}
					break;
				case 7 :
					// src/Tiger.g:112:5: letExp
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_letExp_in_unaryExp905);
					letExp47=letExp();
					state._fsp--;

					adaptor.addChild(root_0, letExp47.getTree());

					}
					break;
				case 8 :
					// src/Tiger.g:113:5: STR
					{
					root_0 = (Object)adaptor.nil();


					STR48=(Token)match(input,STR,FOLLOW_STR_in_unaryExp911); 
					STR48_tree = (Object)adaptor.create(STR48);
					adaptor.addChild(root_0, STR48_tree);

					}
					break;
				case 9 :
					// src/Tiger.g:114:5: INT
					{
					root_0 = (Object)adaptor.nil();


					INT49=(Token)match(input,INT,FOLLOW_INT_in_unaryExp917); 
					INT49_tree = (Object)adaptor.create(INT49);
					adaptor.addChild(root_0, INT49_tree);

					}
					break;
				case 10 :
					// src/Tiger.g:115:5: 'nil'
					{
					root_0 = (Object)adaptor.nil();


					string_literal50=(Token)match(input,49,FOLLOW_49_in_unaryExp923); 
					string_literal50_tree = (Object)adaptor.create(string_literal50);
					adaptor.addChild(root_0, string_literal50_tree);

					}
					break;
				case 11 :
					// src/Tiger.g:116:5: 'break'
					{
					root_0 = (Object)adaptor.nil();


					string_literal51=(Token)match(input,40,FOLLOW_40_in_unaryExp929); 
					string_literal51_tree = (Object)adaptor.create(string_literal51);
					adaptor.addChild(root_0, string_literal51_tree);

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "unaryExp"


	public static class seqExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "seqExp"
	// src/Tiger.g:119:1: seqExp : '(' ( exp ( ';' exp )* )? ')' ( -> ^( SEQ ( exp )* ) ) ;
	public final TigerParser.seqExp_return seqExp() throws RecognitionException {
		TigerParser.seqExp_return retval = new TigerParser.seqExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal52=null;
		Token char_literal54=null;
		Token char_literal56=null;
		ParserRuleReturnScope exp53 =null;
		ParserRuleReturnScope exp55 =null;

		Object char_literal52_tree=null;
		Object char_literal54_tree=null;
		Object char_literal56_tree=null;
		RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleTokenStream stream_21=new RewriteRuleTokenStream(adaptor,"token 21");
		RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");

		try {
			// src/Tiger.g:120:5: ( '(' ( exp ( ';' exp )* )? ')' ( -> ^( SEQ ( exp )* ) ) )
			// src/Tiger.g:120:5: '(' ( exp ( ';' exp )* )? ')' ( -> ^( SEQ ( exp )* ) )
			{
			char_literal52=(Token)match(input,20,FOLLOW_20_in_seqExp940);  
			stream_20.add(char_literal52);

			// src/Tiger.g:121:5: ( exp ( ';' exp )* )?
			int alt19=2;
			int LA19_0 = input.LA(1);
			if ( ((LA19_0 >= ID && LA19_0 <= INT)||LA19_0==STR||LA19_0==20||LA19_0==25||LA19_0==40||LA19_0==44||LA19_0==46||(LA19_0 >= 48 && LA19_0 <= 49)||LA19_0==55) ) {
				alt19=1;
			}
			switch (alt19) {
				case 1 :
					// src/Tiger.g:121:9: exp ( ';' exp )*
					{
					pushFollow(FOLLOW_exp_in_seqExp950);
					exp53=exp();
					state._fsp--;

					stream_exp.add(exp53.getTree());
					// src/Tiger.g:122:9: ( ';' exp )*
					loop18:
					while (true) {
						int alt18=2;
						int LA18_0 = input.LA(1);
						if ( (LA18_0==30) ) {
							alt18=1;
						}

						switch (alt18) {
						case 1 :
							// src/Tiger.g:122:13: ';' exp
							{
							char_literal54=(Token)match(input,30,FOLLOW_30_in_seqExp964);  
							stream_30.add(char_literal54);

							pushFollow(FOLLOW_exp_in_seqExp978);
							exp55=exp();
							state._fsp--;

							stream_exp.add(exp55.getTree());
							}
							break;

						default :
							break loop18;
						}
					}

					}
					break;

			}

			char_literal56=(Token)match(input,21,FOLLOW_21_in_seqExp1002);  
			stream_21.add(char_literal56);

			// src/Tiger.g:127:5: ( -> ^( SEQ ( exp )* ) )
			// src/Tiger.g:127:6: 
			{
			// AST REWRITE
			// elements: exp
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 127:6: -> ^( SEQ ( exp )* )
			{
				// src/Tiger.g:127:9: ^( SEQ ( exp )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SEQ, "SEQ"), root_1);
				// src/Tiger.g:127:15: ( exp )*
				while ( stream_exp.hasNext() ) {
					adaptor.addChild(root_1, stream_exp.nextTree());
				}
				stream_exp.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "seqExp"


	public static class negExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "negExp"
	// src/Tiger.g:130:1: negExp : '-' ^ unaryExp ;
	public final TigerParser.negExp_return negExp() throws RecognitionException {
		TigerParser.negExp_return retval = new TigerParser.negExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal57=null;
		ParserRuleReturnScope unaryExp58 =null;

		Object char_literal57_tree=null;

		try {
			// src/Tiger.g:131:5: ( '-' ^ unaryExp )
			// src/Tiger.g:131:5: '-' ^ unaryExp
			{
			root_0 = (Object)adaptor.nil();


			char_literal57=(Token)match(input,25,FOLLOW_25_in_negExp1028); 
			char_literal57_tree = (Object)adaptor.create(char_literal57);
			root_0 = (Object)adaptor.becomeRoot(char_literal57_tree, root_0);

			pushFollow(FOLLOW_unaryExp_in_negExp1035);
			unaryExp58=unaryExp();
			state._fsp--;

			adaptor.addChild(root_0, unaryExp58.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "negExp"


	public static class valueExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "valueExp"
	// src/Tiger.g:135:1: valueExp : ID ( -> ID ) ( '(' ( exp ( ',' exp )* )? ')' ( -> ^( CALL $valueExp ( exp )* ) ) | '[' exp ']' ( ( -> ^( ITEM $valueExp exp ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | 'of' unaryExp ( -> ^( ARR $valueExp exp unaryExp ) ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | '{' ( ID '=' exp ( ',' ID '=' exp )* )? '}' ( -> ^( REC $valueExp ( ID exp )* ) ) )? ;
	public final TigerParser.valueExp_return valueExp() throws RecognitionException {
		TigerParser.valueExp_return retval = new TigerParser.valueExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ID59=null;
		Token char_literal60=null;
		Token char_literal62=null;
		Token char_literal64=null;
		Token char_literal65=null;
		Token char_literal67=null;
		Token char_literal68=null;
		Token char_literal70=null;
		Token char_literal71=null;
		Token ID72=null;
		Token string_literal73=null;
		Token char_literal75=null;
		Token ID76=null;
		Token char_literal77=null;
		Token char_literal79=null;
		Token char_literal80=null;
		Token ID81=null;
		Token char_literal82=null;
		Token ID83=null;
		Token char_literal84=null;
		Token char_literal86=null;
		Token ID87=null;
		Token char_literal88=null;
		Token char_literal90=null;
		ParserRuleReturnScope exp61 =null;
		ParserRuleReturnScope exp63 =null;
		ParserRuleReturnScope exp66 =null;
		ParserRuleReturnScope exp69 =null;
		ParserRuleReturnScope unaryExp74 =null;
		ParserRuleReturnScope exp78 =null;
		ParserRuleReturnScope exp85 =null;
		ParserRuleReturnScope exp89 =null;

		Object ID59_tree=null;
		Object char_literal60_tree=null;
		Object char_literal62_tree=null;
		Object char_literal64_tree=null;
		Object char_literal65_tree=null;
		Object char_literal67_tree=null;
		Object char_literal68_tree=null;
		Object char_literal70_tree=null;
		Object char_literal71_tree=null;
		Object ID72_tree=null;
		Object string_literal73_tree=null;
		Object char_literal75_tree=null;
		Object ID76_tree=null;
		Object char_literal77_tree=null;
		Object char_literal79_tree=null;
		Object char_literal80_tree=null;
		Object ID81_tree=null;
		Object char_literal82_tree=null;
		Object ID83_tree=null;
		Object char_literal84_tree=null;
		Object char_literal86_tree=null;
		Object ID87_tree=null;
		Object char_literal88_tree=null;
		Object char_literal90_tree=null;
		RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_26=new RewriteRuleTokenStream(adaptor,"token 26");
		RewriteRuleTokenStream stream_37=new RewriteRuleTokenStream(adaptor,"token 37");
		RewriteRuleTokenStream stream_38=new RewriteRuleTokenStream(adaptor,"token 38");
		RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleTokenStream stream_21=new RewriteRuleTokenStream(adaptor,"token 21");
		RewriteRuleSubtreeStream stream_unaryExp=new RewriteRuleSubtreeStream(adaptor,"rule unaryExp");
		RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");

		try {
			// src/Tiger.g:136:5: ( ID ( -> ID ) ( '(' ( exp ( ',' exp )* )? ')' ( -> ^( CALL $valueExp ( exp )* ) ) | '[' exp ']' ( ( -> ^( ITEM $valueExp exp ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | 'of' unaryExp ( -> ^( ARR $valueExp exp unaryExp ) ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | '{' ( ID '=' exp ( ',' ID '=' exp )* )? '}' ( -> ^( REC $valueExp ( ID exp )* ) ) )? )
			// src/Tiger.g:136:5: ID ( -> ID ) ( '(' ( exp ( ',' exp )* )? ')' ( -> ^( CALL $valueExp ( exp )* ) ) | '[' exp ']' ( ( -> ^( ITEM $valueExp exp ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | 'of' unaryExp ( -> ^( ARR $valueExp exp unaryExp ) ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | '{' ( ID '=' exp ( ',' ID '=' exp )* )? '}' ( -> ^( REC $valueExp ( ID exp )* ) ) )?
			{
			ID59=(Token)match(input,ID,FOLLOW_ID_in_valueExp1047);  
			stream_ID.add(ID59);

			// src/Tiger.g:137:5: ( -> ID )
			// src/Tiger.g:137:6: 
			{
			// AST REWRITE
			// elements: ID
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 137:6: -> ID
			{
				adaptor.addChild(root_0, stream_ID.nextNode());
			}


			retval.tree = root_0;

			}

			// src/Tiger.g:138:5: ( '(' ( exp ( ',' exp )* )? ')' ( -> ^( CALL $valueExp ( exp )* ) ) | '[' exp ']' ( ( -> ^( ITEM $valueExp exp ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | 'of' unaryExp ( -> ^( ARR $valueExp exp unaryExp ) ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | '{' ( ID '=' exp ( ',' ID '=' exp )* )? '}' ( -> ^( REC $valueExp ( ID exp )* ) ) )?
			int alt27=5;
			switch ( input.LA(1) ) {
				case 20:
					{
					alt27=1;
					}
					break;
				case 37:
					{
					alt27=2;
					}
					break;
				case 26:
					{
					alt27=3;
					}
					break;
				case 56:
					{
					alt27=4;
					}
					break;
			}
			switch (alt27) {
				case 1 :
					// src/Tiger.g:138:9: '(' ( exp ( ',' exp )* )? ')' ( -> ^( CALL $valueExp ( exp )* ) )
					{
					char_literal60=(Token)match(input,20,FOLLOW_20_in_valueExp1067);  
					stream_20.add(char_literal60);

					// src/Tiger.g:139:9: ( exp ( ',' exp )* )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( ((LA21_0 >= ID && LA21_0 <= INT)||LA21_0==STR||LA21_0==20||LA21_0==25||LA21_0==40||LA21_0==44||LA21_0==46||(LA21_0 >= 48 && LA21_0 <= 49)||LA21_0==55) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// src/Tiger.g:139:13: exp ( ',' exp )*
							{
							pushFollow(FOLLOW_exp_in_valueExp1081);
							exp61=exp();
							state._fsp--;

							stream_exp.add(exp61.getTree());
							// src/Tiger.g:140:13: ( ',' exp )*
							loop20:
							while (true) {
								int alt20=2;
								int LA20_0 = input.LA(1);
								if ( (LA20_0==24) ) {
									alt20=1;
								}

								switch (alt20) {
								case 1 :
									// src/Tiger.g:140:17: ',' exp
									{
									char_literal62=(Token)match(input,24,FOLLOW_24_in_valueExp1099);  
									stream_24.add(char_literal62);

									pushFollow(FOLLOW_exp_in_valueExp1117);
									exp63=exp();
									state._fsp--;

									stream_exp.add(exp63.getTree());
									}
									break;

								default :
									break loop20;
								}
							}

							}
							break;

					}

					char_literal64=(Token)match(input,21,FOLLOW_21_in_valueExp1153);  
					stream_21.add(char_literal64);

					// src/Tiger.g:145:9: ( -> ^( CALL $valueExp ( exp )* ) )
					// src/Tiger.g:145:10: 
					{
					// AST REWRITE
					// elements: valueExp, exp
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 145:10: -> ^( CALL $valueExp ( exp )* )
					{
						// src/Tiger.g:145:13: ^( CALL $valueExp ( exp )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CALL, "CALL"), root_1);
						adaptor.addChild(root_1, stream_retval.nextTree());
						// src/Tiger.g:145:30: ( exp )*
						while ( stream_exp.hasNext() ) {
							adaptor.addChild(root_1, stream_exp.nextTree());
						}
						stream_exp.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}

					}
					break;
				case 2 :
					// src/Tiger.g:146:9: '[' exp ']' ( ( -> ^( ITEM $valueExp exp ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | 'of' unaryExp ( -> ^( ARR $valueExp exp unaryExp ) ) )
					{
					char_literal65=(Token)match(input,37,FOLLOW_37_in_valueExp1186);  
					stream_37.add(char_literal65);

					pushFollow(FOLLOW_exp_in_valueExp1196);
					exp66=exp();
					state._fsp--;

					stream_exp.add(exp66.getTree());
					char_literal67=(Token)match(input,38,FOLLOW_38_in_valueExp1206);  
					stream_38.add(char_literal67);

					// src/Tiger.g:149:9: ( ( -> ^( ITEM $valueExp exp ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )* | 'of' unaryExp ( -> ^( ARR $valueExp exp unaryExp ) ) )
					int alt23=2;
					int LA23_0 = input.LA(1);
					if ( (LA23_0==EOF||LA23_0==19||(LA23_0 >= 21 && LA23_0 <= 27)||(LA23_0 >= 29 && LA23_0 <= 38)||(LA23_0 >= 41 && LA23_0 <= 43)||LA23_0==45||LA23_0==47||(LA23_0 >= 51 && LA23_0 <= 54)||(LA23_0 >= 57 && LA23_0 <= 58)) ) {
						alt23=1;
					}
					else if ( (LA23_0==50) ) {
						alt23=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 23, 0, input);
						throw nvae;
					}

					switch (alt23) {
						case 1 :
							// src/Tiger.g:149:13: ( -> ^( ITEM $valueExp exp ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )*
							{
							// src/Tiger.g:149:13: ( -> ^( ITEM $valueExp exp ) )
							// src/Tiger.g:149:14: 
							{
							// AST REWRITE
							// elements: valueExp, exp
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (Object)adaptor.nil();
							// 149:14: -> ^( ITEM $valueExp exp )
							{
								// src/Tiger.g:149:17: ^( ITEM $valueExp exp )
								{
								Object root_1 = (Object)adaptor.nil();
								root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ITEM, "ITEM"), root_1);
								adaptor.addChild(root_1, stream_retval.nextTree());
								adaptor.addChild(root_1, stream_exp.nextTree());
								adaptor.addChild(root_0, root_1);
								}

							}


							retval.tree = root_0;

							}

							// src/Tiger.g:150:13: ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )*
							loop22:
							while (true) {
								int alt22=3;
								int LA22_0 = input.LA(1);
								if ( (LA22_0==37) ) {
									alt22=1;
								}
								else if ( (LA22_0==26) ) {
									alt22=2;
								}

								switch (alt22) {
								case 1 :
									// src/Tiger.g:150:17: '[' exp ']' ( -> ^( ITEM $valueExp exp ) )
									{
									char_literal68=(Token)match(input,37,FOLLOW_37_in_valueExp1250);  
									stream_37.add(char_literal68);

									pushFollow(FOLLOW_exp_in_valueExp1268);
									exp69=exp();
									state._fsp--;

									stream_exp.add(exp69.getTree());
									char_literal70=(Token)match(input,38,FOLLOW_38_in_valueExp1286);  
									stream_38.add(char_literal70);

									// src/Tiger.g:153:17: ( -> ^( ITEM $valueExp exp ) )
									// src/Tiger.g:153:18: 
									{
									// AST REWRITE
									// elements: exp, valueExp
									// token labels: 
									// rule labels: retval
									// token list labels: 
									// rule list labels: 
									// wildcard labels: 
									retval.tree = root_0;
									RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

									root_0 = (Object)adaptor.nil();
									// 153:18: -> ^( ITEM $valueExp exp )
									{
										// src/Tiger.g:153:21: ^( ITEM $valueExp exp )
										{
										Object root_1 = (Object)adaptor.nil();
										root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ITEM, "ITEM"), root_1);
										adaptor.addChild(root_1, stream_retval.nextTree());
										adaptor.addChild(root_1, stream_exp.nextTree());
										adaptor.addChild(root_0, root_1);
										}

									}


									retval.tree = root_0;

									}

									}
									break;
								case 2 :
									// src/Tiger.g:154:17: '.' ID ( -> ^( FIELD $valueExp ID ) )
									{
									char_literal71=(Token)match(input,26,FOLLOW_26_in_valueExp1334);  
									stream_26.add(char_literal71);

									ID72=(Token)match(input,ID,FOLLOW_ID_in_valueExp1352);  
									stream_ID.add(ID72);

									// src/Tiger.g:156:17: ( -> ^( FIELD $valueExp ID ) )
									// src/Tiger.g:156:18: 
									{
									// AST REWRITE
									// elements: valueExp, ID
									// token labels: 
									// rule labels: retval
									// token list labels: 
									// rule list labels: 
									// wildcard labels: 
									retval.tree = root_0;
									RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

									root_0 = (Object)adaptor.nil();
									// 156:18: -> ^( FIELD $valueExp ID )
									{
										// src/Tiger.g:156:21: ^( FIELD $valueExp ID )
										{
										Object root_1 = (Object)adaptor.nil();
										root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FIELD, "FIELD"), root_1);
										adaptor.addChild(root_1, stream_retval.nextTree());
										adaptor.addChild(root_1, stream_ID.nextNode());
										adaptor.addChild(root_0, root_1);
										}

									}


									retval.tree = root_0;

									}

									}
									break;

								default :
									break loop22;
								}
							}

							}
							break;
						case 2 :
							// src/Tiger.g:158:13: 'of' unaryExp ( -> ^( ARR $valueExp exp unaryExp ) )
							{
							string_literal73=(Token)match(input,50,FOLLOW_50_in_valueExp1411);  
							stream_50.add(string_literal73);

							pushFollow(FOLLOW_unaryExp_in_valueExp1425);
							unaryExp74=unaryExp();
							state._fsp--;

							stream_unaryExp.add(unaryExp74.getTree());
							// src/Tiger.g:160:13: ( -> ^( ARR $valueExp exp unaryExp ) )
							// src/Tiger.g:160:14: 
							{
							// AST REWRITE
							// elements: unaryExp, valueExp, exp
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (Object)adaptor.nil();
							// 160:14: -> ^( ARR $valueExp exp unaryExp )
							{
								// src/Tiger.g:160:17: ^( ARR $valueExp exp unaryExp )
								{
								Object root_1 = (Object)adaptor.nil();
								root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ARR, "ARR"), root_1);
								adaptor.addChild(root_1, stream_retval.nextTree());
								adaptor.addChild(root_1, stream_exp.nextTree());
								adaptor.addChild(root_1, stream_unaryExp.nextTree());
								adaptor.addChild(root_0, root_1);
								}

							}


							retval.tree = root_0;

							}

							}
							break;

					}

					}
					break;
				case 3 :
					// src/Tiger.g:162:9: '.' ID ( -> ^( FIELD $valueExp ID ) ) ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )*
					{
					char_literal75=(Token)match(input,26,FOLLOW_26_in_valueExp1473);  
					stream_26.add(char_literal75);

					ID76=(Token)match(input,ID,FOLLOW_ID_in_valueExp1483);  
					stream_ID.add(ID76);

					// src/Tiger.g:164:9: ( -> ^( FIELD $valueExp ID ) )
					// src/Tiger.g:164:10: 
					{
					// AST REWRITE
					// elements: ID, valueExp
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 164:10: -> ^( FIELD $valueExp ID )
					{
						// src/Tiger.g:164:13: ^( FIELD $valueExp ID )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FIELD, "FIELD"), root_1);
						adaptor.addChild(root_1, stream_retval.nextTree());
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}

					// src/Tiger.g:165:9: ( '[' exp ']' ( -> ^( ITEM $valueExp exp ) ) | '.' ID ( -> ^( FIELD $valueExp ID ) ) )*
					loop24:
					while (true) {
						int alt24=3;
						int LA24_0 = input.LA(1);
						if ( (LA24_0==37) ) {
							alt24=1;
						}
						else if ( (LA24_0==26) ) {
							alt24=2;
						}

						switch (alt24) {
						case 1 :
							// src/Tiger.g:165:13: '[' exp ']' ( -> ^( ITEM $valueExp exp ) )
							{
							char_literal77=(Token)match(input,37,FOLLOW_37_in_valueExp1519);  
							stream_37.add(char_literal77);

							pushFollow(FOLLOW_exp_in_valueExp1533);
							exp78=exp();
							state._fsp--;

							stream_exp.add(exp78.getTree());
							char_literal79=(Token)match(input,38,FOLLOW_38_in_valueExp1547);  
							stream_38.add(char_literal79);

							// src/Tiger.g:168:13: ( -> ^( ITEM $valueExp exp ) )
							// src/Tiger.g:168:14: 
							{
							// AST REWRITE
							// elements: valueExp, exp
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (Object)adaptor.nil();
							// 168:14: -> ^( ITEM $valueExp exp )
							{
								// src/Tiger.g:168:17: ^( ITEM $valueExp exp )
								{
								Object root_1 = (Object)adaptor.nil();
								root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ITEM, "ITEM"), root_1);
								adaptor.addChild(root_1, stream_retval.nextTree());
								adaptor.addChild(root_1, stream_exp.nextTree());
								adaptor.addChild(root_0, root_1);
								}

							}


							retval.tree = root_0;

							}

							}
							break;
						case 2 :
							// src/Tiger.g:169:13: '.' ID ( -> ^( FIELD $valueExp ID ) )
							{
							char_literal80=(Token)match(input,26,FOLLOW_26_in_valueExp1587);  
							stream_26.add(char_literal80);

							ID81=(Token)match(input,ID,FOLLOW_ID_in_valueExp1601);  
							stream_ID.add(ID81);

							// src/Tiger.g:171:13: ( -> ^( FIELD $valueExp ID ) )
							// src/Tiger.g:171:14: 
							{
							// AST REWRITE
							// elements: ID, valueExp
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (Object)adaptor.nil();
							// 171:14: -> ^( FIELD $valueExp ID )
							{
								// src/Tiger.g:171:17: ^( FIELD $valueExp ID )
								{
								Object root_1 = (Object)adaptor.nil();
								root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FIELD, "FIELD"), root_1);
								adaptor.addChild(root_1, stream_retval.nextTree());
								adaptor.addChild(root_1, stream_ID.nextNode());
								adaptor.addChild(root_0, root_1);
								}

							}


							retval.tree = root_0;

							}

							}
							break;

						default :
							break loop24;
						}
					}

					}
					break;
				case 4 :
					// src/Tiger.g:173:9: '{' ( ID '=' exp ( ',' ID '=' exp )* )? '}' ( -> ^( REC $valueExp ( ID exp )* ) )
					{
					char_literal82=(Token)match(input,56,FOLLOW_56_in_valueExp1648);  
					stream_56.add(char_literal82);

					// src/Tiger.g:174:9: ( ID '=' exp ( ',' ID '=' exp )* )?
					int alt26=2;
					int LA26_0 = input.LA(1);
					if ( (LA26_0==ID) ) {
						alt26=1;
					}
					switch (alt26) {
						case 1 :
							// src/Tiger.g:174:13: ID '=' exp ( ',' ID '=' exp )*
							{
							ID83=(Token)match(input,ID,FOLLOW_ID_in_valueExp1662);  
							stream_ID.add(ID83);

							char_literal84=(Token)match(input,34,FOLLOW_34_in_valueExp1676);  
							stream_34.add(char_literal84);

							pushFollow(FOLLOW_exp_in_valueExp1690);
							exp85=exp();
							state._fsp--;

							stream_exp.add(exp85.getTree());
							// src/Tiger.g:177:13: ( ',' ID '=' exp )*
							loop25:
							while (true) {
								int alt25=2;
								int LA25_0 = input.LA(1);
								if ( (LA25_0==24) ) {
									alt25=1;
								}

								switch (alt25) {
								case 1 :
									// src/Tiger.g:177:17: ',' ID '=' exp
									{
									char_literal86=(Token)match(input,24,FOLLOW_24_in_valueExp1708);  
									stream_24.add(char_literal86);

									ID87=(Token)match(input,ID,FOLLOW_ID_in_valueExp1726);  
									stream_ID.add(ID87);

									char_literal88=(Token)match(input,34,FOLLOW_34_in_valueExp1744);  
									stream_34.add(char_literal88);

									pushFollow(FOLLOW_exp_in_valueExp1762);
									exp89=exp();
									state._fsp--;

									stream_exp.add(exp89.getTree());
									}
									break;

								default :
									break loop25;
								}
							}

							}
							break;

					}

					char_literal90=(Token)match(input,58,FOLLOW_58_in_valueExp1798);  
					stream_58.add(char_literal90);

					// src/Tiger.g:184:9: ( -> ^( REC $valueExp ( ID exp )* ) )
					// src/Tiger.g:184:10: 
					{
					// AST REWRITE
					// elements: exp, valueExp, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 184:10: -> ^( REC $valueExp ( ID exp )* )
					{
						// src/Tiger.g:184:13: ^( REC $valueExp ( ID exp )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(REC, "REC"), root_1);
						adaptor.addChild(root_1, stream_retval.nextTree());
						// src/Tiger.g:184:29: ( ID exp )*
						while ( stream_exp.hasNext()||stream_ID.hasNext() ) {
							adaptor.addChild(root_1, stream_ID.nextNode());
							adaptor.addChild(root_1, stream_exp.nextTree());
						}
						stream_exp.reset();
						stream_ID.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "valueExp"


	public static class ifExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ifExp"
	// src/Tiger.g:188:1: ifExp : 'if' ^ exp 'then' ! unaryExp ( options {greedy=true; } : 'else' ! unaryExp )? ;
	public final TigerParser.ifExp_return ifExp() throws RecognitionException {
		TigerParser.ifExp_return retval = new TigerParser.ifExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal91=null;
		Token string_literal93=null;
		Token string_literal95=null;
		ParserRuleReturnScope exp92 =null;
		ParserRuleReturnScope unaryExp94 =null;
		ParserRuleReturnScope unaryExp96 =null;

		Object string_literal91_tree=null;
		Object string_literal93_tree=null;
		Object string_literal95_tree=null;

		try {
			// src/Tiger.g:189:5: ( 'if' ^ exp 'then' ! unaryExp ( options {greedy=true; } : 'else' ! unaryExp )? )
			// src/Tiger.g:189:5: 'if' ^ exp 'then' ! unaryExp ( options {greedy=true; } : 'else' ! unaryExp )?
			{
			root_0 = (Object)adaptor.nil();


			string_literal91=(Token)match(input,46,FOLLOW_46_in_ifExp1843); 
			string_literal91_tree = (Object)adaptor.create(string_literal91);
			root_0 = (Object)adaptor.becomeRoot(string_literal91_tree, root_0);

			pushFollow(FOLLOW_exp_in_ifExp1850);
			exp92=exp();
			state._fsp--;

			adaptor.addChild(root_0, exp92.getTree());

			string_literal93=(Token)match(input,51,FOLLOW_51_in_ifExp1856); 
			pushFollow(FOLLOW_unaryExp_in_ifExp1863);
			unaryExp94=unaryExp();
			state._fsp--;

			adaptor.addChild(root_0, unaryExp94.getTree());

			// src/Tiger.g:193:5: ( options {greedy=true; } : 'else' ! unaryExp )?
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==42) ) {
				alt28=1;
			}
			switch (alt28) {
				case 1 :
					// src/Tiger.g:195:9: 'else' ! unaryExp
					{
					string_literal95=(Token)match(input,42,FOLLOW_42_in_ifExp1895); 
					pushFollow(FOLLOW_unaryExp_in_ifExp1906);
					unaryExp96=unaryExp();
					state._fsp--;

					adaptor.addChild(root_0, unaryExp96.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "ifExp"


	public static class whileExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "whileExp"
	// src/Tiger.g:200:1: whileExp : 'while' ^ exp 'do' ! unaryExp ;
	public final TigerParser.whileExp_return whileExp() throws RecognitionException {
		TigerParser.whileExp_return retval = new TigerParser.whileExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal97=null;
		Token string_literal99=null;
		ParserRuleReturnScope exp98 =null;
		ParserRuleReturnScope unaryExp100 =null;

		Object string_literal97_tree=null;
		Object string_literal99_tree=null;

		try {
			// src/Tiger.g:201:5: ( 'while' ^ exp 'do' ! unaryExp )
			// src/Tiger.g:201:5: 'while' ^ exp 'do' ! unaryExp
			{
			root_0 = (Object)adaptor.nil();


			string_literal97=(Token)match(input,55,FOLLOW_55_in_whileExp1924); 
			string_literal97_tree = (Object)adaptor.create(string_literal97);
			root_0 = (Object)adaptor.becomeRoot(string_literal97_tree, root_0);

			pushFollow(FOLLOW_exp_in_whileExp1931);
			exp98=exp();
			state._fsp--;

			adaptor.addChild(root_0, exp98.getTree());

			string_literal99=(Token)match(input,41,FOLLOW_41_in_whileExp1937); 
			pushFollow(FOLLOW_unaryExp_in_whileExp1944);
			unaryExp100=unaryExp();
			state._fsp--;

			adaptor.addChild(root_0, unaryExp100.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "whileExp"


	public static class forExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "forExp"
	// src/Tiger.g:207:1: forExp : 'for' ^ ID ':=' ! exp 'to' ! exp 'do' ! unaryExp ;
	public final TigerParser.forExp_return forExp() throws RecognitionException {
		TigerParser.forExp_return retval = new TigerParser.forExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal101=null;
		Token ID102=null;
		Token string_literal103=null;
		Token string_literal105=null;
		Token string_literal107=null;
		ParserRuleReturnScope exp104 =null;
		ParserRuleReturnScope exp106 =null;
		ParserRuleReturnScope unaryExp108 =null;

		Object string_literal101_tree=null;
		Object ID102_tree=null;
		Object string_literal103_tree=null;
		Object string_literal105_tree=null;
		Object string_literal107_tree=null;

		try {
			// src/Tiger.g:208:5: ( 'for' ^ ID ':=' ! exp 'to' ! exp 'do' ! unaryExp )
			// src/Tiger.g:208:5: 'for' ^ ID ':=' ! exp 'to' ! exp 'do' ! unaryExp
			{
			root_0 = (Object)adaptor.nil();


			string_literal101=(Token)match(input,44,FOLLOW_44_in_forExp1955); 
			string_literal101_tree = (Object)adaptor.create(string_literal101);
			root_0 = (Object)adaptor.becomeRoot(string_literal101_tree, root_0);

			ID102=(Token)match(input,ID,FOLLOW_ID_in_forExp1962); 
			ID102_tree = (Object)adaptor.create(ID102);
			adaptor.addChild(root_0, ID102_tree);

			string_literal103=(Token)match(input,29,FOLLOW_29_in_forExp1968); 
			pushFollow(FOLLOW_exp_in_forExp1975);
			exp104=exp();
			state._fsp--;

			adaptor.addChild(root_0, exp104.getTree());

			string_literal105=(Token)match(input,52,FOLLOW_52_in_forExp1981); 
			pushFollow(FOLLOW_exp_in_forExp1988);
			exp106=exp();
			state._fsp--;

			adaptor.addChild(root_0, exp106.getTree());

			string_literal107=(Token)match(input,41,FOLLOW_41_in_forExp1994); 
			pushFollow(FOLLOW_unaryExp_in_forExp2001);
			unaryExp108=unaryExp();
			state._fsp--;

			adaptor.addChild(root_0, unaryExp108.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "forExp"


	public static class letExp_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "letExp"
	// src/Tiger.g:218:1: letExp : 'let' ( dec )+ 'in' ( exp ( ';' exp )* )? 'end' ( -> ^( 'let' ^( DEC ( dec )+ ) ^( SEQ ( exp )* ) ) ) ;
	public final TigerParser.letExp_return letExp() throws RecognitionException {
		TigerParser.letExp_return retval = new TigerParser.letExp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal109=null;
		Token string_literal111=null;
		Token char_literal113=null;
		Token string_literal115=null;
		ParserRuleReturnScope dec110 =null;
		ParserRuleReturnScope exp112 =null;
		ParserRuleReturnScope exp114 =null;

		Object string_literal109_tree=null;
		Object string_literal111_tree=null;
		Object char_literal113_tree=null;
		Object string_literal115_tree=null;
		RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
		RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
		RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
		RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
		RewriteRuleSubtreeStream stream_dec=new RewriteRuleSubtreeStream(adaptor,"rule dec");
		RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");

		try {
			// src/Tiger.g:219:5: ( 'let' ( dec )+ 'in' ( exp ( ';' exp )* )? 'end' ( -> ^( 'let' ^( DEC ( dec )+ ) ^( SEQ ( exp )* ) ) ) )
			// src/Tiger.g:219:5: 'let' ( dec )+ 'in' ( exp ( ';' exp )* )? 'end' ( -> ^( 'let' ^( DEC ( dec )+ ) ^( SEQ ( exp )* ) ) )
			{
			string_literal109=(Token)match(input,48,FOLLOW_48_in_letExp2012);  
			stream_48.add(string_literal109);

			// src/Tiger.g:220:5: ( dec )+
			int cnt29=0;
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==45||(LA29_0 >= 53 && LA29_0 <= 54)) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// src/Tiger.g:220:5: dec
					{
					pushFollow(FOLLOW_dec_in_letExp2018);
					dec110=dec();
					state._fsp--;

					stream_dec.add(dec110.getTree());
					}
					break;

				default :
					if ( cnt29 >= 1 ) break loop29;
					EarlyExitException eee = new EarlyExitException(29, input);
					throw eee;
				}
				cnt29++;
			}

			string_literal111=(Token)match(input,47,FOLLOW_47_in_letExp2025);  
			stream_47.add(string_literal111);

			// src/Tiger.g:222:5: ( exp ( ';' exp )* )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( ((LA31_0 >= ID && LA31_0 <= INT)||LA31_0==STR||LA31_0==20||LA31_0==25||LA31_0==40||LA31_0==44||LA31_0==46||(LA31_0 >= 48 && LA31_0 <= 49)||LA31_0==55) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// src/Tiger.g:222:9: exp ( ';' exp )*
					{
					pushFollow(FOLLOW_exp_in_letExp2035);
					exp112=exp();
					state._fsp--;

					stream_exp.add(exp112.getTree());
					// src/Tiger.g:223:9: ( ';' exp )*
					loop30:
					while (true) {
						int alt30=2;
						int LA30_0 = input.LA(1);
						if ( (LA30_0==30) ) {
							alt30=1;
						}

						switch (alt30) {
						case 1 :
							// src/Tiger.g:223:13: ';' exp
							{
							char_literal113=(Token)match(input,30,FOLLOW_30_in_letExp2049);  
							stream_30.add(char_literal113);

							pushFollow(FOLLOW_exp_in_letExp2063);
							exp114=exp();
							state._fsp--;

							stream_exp.add(exp114.getTree());
							}
							break;

						default :
							break loop30;
						}
					}

					}
					break;

			}

			string_literal115=(Token)match(input,43,FOLLOW_43_in_letExp2087);  
			stream_43.add(string_literal115);

			// src/Tiger.g:228:5: ( -> ^( 'let' ^( DEC ( dec )+ ) ^( SEQ ( exp )* ) ) )
			// src/Tiger.g:228:6: 
			{
			// AST REWRITE
			// elements: dec, exp, 48
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 228:6: -> ^( 'let' ^( DEC ( dec )+ ) ^( SEQ ( exp )* ) )
			{
				// src/Tiger.g:228:9: ^( 'let' ^( DEC ( dec )+ ) ^( SEQ ( exp )* ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(stream_48.nextNode(), root_1);
				// src/Tiger.g:228:17: ^( DEC ( dec )+ )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(DEC, "DEC"), root_2);
				if ( !(stream_dec.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_dec.hasNext() ) {
					adaptor.addChild(root_2, stream_dec.nextTree());
				}
				stream_dec.reset();

				adaptor.addChild(root_1, root_2);
				}

				// src/Tiger.g:228:29: ^( SEQ ( exp )* )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(SEQ, "SEQ"), root_2);
				// src/Tiger.g:228:35: ( exp )*
				while ( stream_exp.hasNext() ) {
					adaptor.addChild(root_2, stream_exp.nextTree());
				}
				stream_exp.reset();

				adaptor.addChild(root_1, root_2);
				}

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "letExp"


	public static class dec_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "dec"
	// src/Tiger.g:231:1: dec : ( tyDec | funDec | varDec );
	public final TigerParser.dec_return dec() throws RecognitionException {
		TigerParser.dec_return retval = new TigerParser.dec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope tyDec116 =null;
		ParserRuleReturnScope funDec117 =null;
		ParserRuleReturnScope varDec118 =null;


		try {
			// src/Tiger.g:232:5: ( tyDec | funDec | varDec )
			int alt32=3;
			switch ( input.LA(1) ) {
			case 53:
				{
				alt32=1;
				}
				break;
			case 45:
				{
				alt32=2;
				}
				break;
			case 54:
				{
				alt32=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 32, 0, input);
				throw nvae;
			}
			switch (alt32) {
				case 1 :
					// src/Tiger.g:232:5: tyDec
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_tyDec_in_dec2124);
					tyDec116=tyDec();
					state._fsp--;

					adaptor.addChild(root_0, tyDec116.getTree());

					}
					break;
				case 2 :
					// src/Tiger.g:233:5: funDec
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_funDec_in_dec2130);
					funDec117=funDec();
					state._fsp--;

					adaptor.addChild(root_0, funDec117.getTree());

					}
					break;
				case 3 :
					// src/Tiger.g:234:5: varDec
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_varDec_in_dec2136);
					varDec118=varDec();
					state._fsp--;

					adaptor.addChild(root_0, varDec118.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dec"


	public static class tyDec_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "tyDec"
	// src/Tiger.g:237:1: tyDec : 'type' ID '=' ( ID ( -> ^( 'type' ID ID ) ) | 'array' 'of' ID ( -> ^( 'type' ID ^( ARRTYPE ID ) ) ) | '{' ( ID ':' ID ( ',' ID ':' ID )* )? '}' ( -> ^( 'type' ID ^( RECTYPE ( ID ID )* ) ) ) ) ;
	public final TigerParser.tyDec_return tyDec() throws RecognitionException {
		TigerParser.tyDec_return retval = new TigerParser.tyDec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal119=null;
		Token ID120=null;
		Token char_literal121=null;
		Token ID122=null;
		Token string_literal123=null;
		Token string_literal124=null;
		Token ID125=null;
		Token char_literal126=null;
		Token ID127=null;
		Token char_literal128=null;
		Token ID129=null;
		Token char_literal130=null;
		Token ID131=null;
		Token char_literal132=null;
		Token ID133=null;
		Token char_literal134=null;

		Object string_literal119_tree=null;
		Object ID120_tree=null;
		Object char_literal121_tree=null;
		Object ID122_tree=null;
		Object string_literal123_tree=null;
		Object string_literal124_tree=null;
		Object ID125_tree=null;
		Object char_literal126_tree=null;
		Object ID127_tree=null;
		Object char_literal128_tree=null;
		Object ID129_tree=null;
		Object char_literal130_tree=null;
		Object ID131_tree=null;
		Object char_literal132_tree=null;
		Object ID133_tree=null;
		Object char_literal134_tree=null;
		RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_39=new RewriteRuleTokenStream(adaptor,"token 39");
		RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
		RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");

		try {
			// src/Tiger.g:238:5: ( 'type' ID '=' ( ID ( -> ^( 'type' ID ID ) ) | 'array' 'of' ID ( -> ^( 'type' ID ^( ARRTYPE ID ) ) ) | '{' ( ID ':' ID ( ',' ID ':' ID )* )? '}' ( -> ^( 'type' ID ^( RECTYPE ( ID ID )* ) ) ) ) )
			// src/Tiger.g:238:5: 'type' ID '=' ( ID ( -> ^( 'type' ID ID ) ) | 'array' 'of' ID ( -> ^( 'type' ID ^( ARRTYPE ID ) ) ) | '{' ( ID ':' ID ( ',' ID ':' ID )* )? '}' ( -> ^( 'type' ID ^( RECTYPE ( ID ID )* ) ) ) )
			{
			string_literal119=(Token)match(input,53,FOLLOW_53_in_tyDec2147);  
			stream_53.add(string_literal119);

			ID120=(Token)match(input,ID,FOLLOW_ID_in_tyDec2153);  
			stream_ID.add(ID120);

			char_literal121=(Token)match(input,34,FOLLOW_34_in_tyDec2159);  
			stream_34.add(char_literal121);

			// src/Tiger.g:241:5: ( ID ( -> ^( 'type' ID ID ) ) | 'array' 'of' ID ( -> ^( 'type' ID ^( ARRTYPE ID ) ) ) | '{' ( ID ':' ID ( ',' ID ':' ID )* )? '}' ( -> ^( 'type' ID ^( RECTYPE ( ID ID )* ) ) ) )
			int alt35=3;
			switch ( input.LA(1) ) {
			case ID:
				{
				alt35=1;
				}
				break;
			case 39:
				{
				alt35=2;
				}
				break;
			case 56:
				{
				alt35=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 35, 0, input);
				throw nvae;
			}
			switch (alt35) {
				case 1 :
					// src/Tiger.g:241:9: ID ( -> ^( 'type' ID ID ) )
					{
					ID122=(Token)match(input,ID,FOLLOW_ID_in_tyDec2169);  
					stream_ID.add(ID122);

					// src/Tiger.g:242:9: ( -> ^( 'type' ID ID ) )
					// src/Tiger.g:242:10: 
					{
					// AST REWRITE
					// elements: ID, ID, 53
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 242:10: -> ^( 'type' ID ID )
					{
						// src/Tiger.g:242:13: ^( 'type' ID ID )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_53.nextNode(), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}

					}
					break;
				case 2 :
					// src/Tiger.g:243:9: 'array' 'of' ID ( -> ^( 'type' ID ^( ARRTYPE ID ) ) )
					{
					string_literal123=(Token)match(input,39,FOLLOW_39_in_tyDec2200);  
					stream_39.add(string_literal123);

					string_literal124=(Token)match(input,50,FOLLOW_50_in_tyDec2210);  
					stream_50.add(string_literal124);

					ID125=(Token)match(input,ID,FOLLOW_ID_in_tyDec2220);  
					stream_ID.add(ID125);

					// src/Tiger.g:246:9: ( -> ^( 'type' ID ^( ARRTYPE ID ) ) )
					// src/Tiger.g:246:10: 
					{
					// AST REWRITE
					// elements: 53, ID, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 246:10: -> ^( 'type' ID ^( ARRTYPE ID ) )
					{
						// src/Tiger.g:246:13: ^( 'type' ID ^( ARRTYPE ID ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_53.nextNode(), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						// src/Tiger.g:246:25: ^( ARRTYPE ID )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(ARRTYPE, "ARRTYPE"), root_2);
						adaptor.addChild(root_2, stream_ID.nextNode());
						adaptor.addChild(root_1, root_2);
						}

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}

					}
					break;
				case 3 :
					// src/Tiger.g:247:9: '{' ( ID ':' ID ( ',' ID ':' ID )* )? '}' ( -> ^( 'type' ID ^( RECTYPE ( ID ID )* ) ) )
					{
					char_literal126=(Token)match(input,56,FOLLOW_56_in_tyDec2255);  
					stream_56.add(char_literal126);

					// src/Tiger.g:248:9: ( ID ':' ID ( ',' ID ':' ID )* )?
					int alt34=2;
					int LA34_0 = input.LA(1);
					if ( (LA34_0==ID) ) {
						alt34=1;
					}
					switch (alt34) {
						case 1 :
							// src/Tiger.g:248:13: ID ':' ID ( ',' ID ':' ID )*
							{
							ID127=(Token)match(input,ID,FOLLOW_ID_in_tyDec2269);  
							stream_ID.add(ID127);

							char_literal128=(Token)match(input,28,FOLLOW_28_in_tyDec2283);  
							stream_28.add(char_literal128);

							ID129=(Token)match(input,ID,FOLLOW_ID_in_tyDec2297);  
							stream_ID.add(ID129);

							// src/Tiger.g:251:13: ( ',' ID ':' ID )*
							loop33:
							while (true) {
								int alt33=2;
								int LA33_0 = input.LA(1);
								if ( (LA33_0==24) ) {
									alt33=1;
								}

								switch (alt33) {
								case 1 :
									// src/Tiger.g:251:17: ',' ID ':' ID
									{
									char_literal130=(Token)match(input,24,FOLLOW_24_in_tyDec2315);  
									stream_24.add(char_literal130);

									ID131=(Token)match(input,ID,FOLLOW_ID_in_tyDec2333);  
									stream_ID.add(ID131);

									char_literal132=(Token)match(input,28,FOLLOW_28_in_tyDec2351);  
									stream_28.add(char_literal132);

									ID133=(Token)match(input,ID,FOLLOW_ID_in_tyDec2369);  
									stream_ID.add(ID133);

									}
									break;

								default :
									break loop33;
								}
							}

							}
							break;

					}

					char_literal134=(Token)match(input,58,FOLLOW_58_in_tyDec2405);  
					stream_58.add(char_literal134);

					// src/Tiger.g:258:9: ( -> ^( 'type' ID ^( RECTYPE ( ID ID )* ) ) )
					// src/Tiger.g:258:10: 
					{
					// AST REWRITE
					// elements: 53, ID, ID, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 258:10: -> ^( 'type' ID ^( RECTYPE ( ID ID )* ) )
					{
						// src/Tiger.g:258:13: ^( 'type' ID ^( RECTYPE ( ID ID )* ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_53.nextNode(), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						// src/Tiger.g:258:25: ^( RECTYPE ( ID ID )* )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(RECTYPE, "RECTYPE"), root_2);
						// src/Tiger.g:258:35: ( ID ID )*
						while ( stream_ID.hasNext()||stream_ID.hasNext() ) {
							adaptor.addChild(root_2, stream_ID.nextNode());
							adaptor.addChild(root_2, stream_ID.nextNode());
						}
						stream_ID.reset();
						stream_ID.reset();

						adaptor.addChild(root_1, root_2);
						}

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "tyDec"


	public static class funDec_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "funDec"
	// src/Tiger.g:262:1: funDec : 'function' ID '(' (i+= ID ':' j+= ID ( ',' i+= ID ':' j+= ID )* )? ')' ( ':' k= ID )? '=' exp ( -> ^( 'function' ID ^( CALLTYPE ( $i $j)* ) ( $k)? exp ) ) ;
	public final TigerParser.funDec_return funDec() throws RecognitionException {
		TigerParser.funDec_return retval = new TigerParser.funDec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token k=null;
		Token string_literal135=null;
		Token ID136=null;
		Token char_literal137=null;
		Token char_literal138=null;
		Token char_literal139=null;
		Token char_literal140=null;
		Token char_literal141=null;
		Token char_literal142=null;
		Token char_literal143=null;
		Token i=null;
		Token j=null;
		List<Object> list_i=null;
		List<Object> list_j=null;
		ParserRuleReturnScope exp144 =null;

		Object k_tree=null;
		Object string_literal135_tree=null;
		Object ID136_tree=null;
		Object char_literal137_tree=null;
		Object char_literal138_tree=null;
		Object char_literal139_tree=null;
		Object char_literal140_tree=null;
		Object char_literal141_tree=null;
		Object char_literal142_tree=null;
		Object char_literal143_tree=null;
		Object i_tree=null;
		Object j_tree=null;
		RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
		RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
		RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
		RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_20=new RewriteRuleTokenStream(adaptor,"token 20");
		RewriteRuleTokenStream stream_21=new RewriteRuleTokenStream(adaptor,"token 21");
		RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");

		try {
			// src/Tiger.g:263:5: ( 'function' ID '(' (i+= ID ':' j+= ID ( ',' i+= ID ':' j+= ID )* )? ')' ( ':' k= ID )? '=' exp ( -> ^( 'function' ID ^( CALLTYPE ( $i $j)* ) ( $k)? exp ) ) )
			// src/Tiger.g:263:5: 'function' ID '(' (i+= ID ':' j+= ID ( ',' i+= ID ':' j+= ID )* )? ')' ( ':' k= ID )? '=' exp ( -> ^( 'function' ID ^( CALLTYPE ( $i $j)* ) ( $k)? exp ) )
			{
			string_literal135=(Token)match(input,45,FOLLOW_45_in_funDec2452);  
			stream_45.add(string_literal135);

			ID136=(Token)match(input,ID,FOLLOW_ID_in_funDec2458);  
			stream_ID.add(ID136);

			char_literal137=(Token)match(input,20,FOLLOW_20_in_funDec2464);  
			stream_20.add(char_literal137);

			// src/Tiger.g:266:5: (i+= ID ':' j+= ID ( ',' i+= ID ':' j+= ID )* )?
			int alt37=2;
			int LA37_0 = input.LA(1);
			if ( (LA37_0==ID) ) {
				alt37=1;
			}
			switch (alt37) {
				case 1 :
					// src/Tiger.g:266:9: i+= ID ':' j+= ID ( ',' i+= ID ':' j+= ID )*
					{
					i=(Token)match(input,ID,FOLLOW_ID_in_funDec2478);  
					stream_ID.add(i);

					if (list_i==null) list_i=new ArrayList<Object>();
					list_i.add(i);
					char_literal138=(Token)match(input,28,FOLLOW_28_in_funDec2488);  
					stream_28.add(char_literal138);

					j=(Token)match(input,ID,FOLLOW_ID_in_funDec2502);  
					stream_ID.add(j);

					if (list_j==null) list_j=new ArrayList<Object>();
					list_j.add(j);
					// src/Tiger.g:269:9: ( ',' i+= ID ':' j+= ID )*
					loop36:
					while (true) {
						int alt36=2;
						int LA36_0 = input.LA(1);
						if ( (LA36_0==24) ) {
							alt36=1;
						}

						switch (alt36) {
						case 1 :
							// src/Tiger.g:269:13: ',' i+= ID ':' j+= ID
							{
							char_literal139=(Token)match(input,24,FOLLOW_24_in_funDec2516);  
							stream_24.add(char_literal139);

							i=(Token)match(input,ID,FOLLOW_ID_in_funDec2534);  
							stream_ID.add(i);

							if (list_i==null) list_i=new ArrayList<Object>();
							list_i.add(i);
							char_literal140=(Token)match(input,28,FOLLOW_28_in_funDec2548);  
							stream_28.add(char_literal140);

							j=(Token)match(input,ID,FOLLOW_ID_in_funDec2566);  
							stream_ID.add(j);

							if (list_j==null) list_j=new ArrayList<Object>();
							list_j.add(j);
							}
							break;

						default :
							break loop36;
						}
					}

					}
					break;

			}

			char_literal141=(Token)match(input,21,FOLLOW_21_in_funDec2590);  
			stream_21.add(char_literal141);

			// src/Tiger.g:276:5: ( ':' k= ID )?
			int alt38=2;
			int LA38_0 = input.LA(1);
			if ( (LA38_0==28) ) {
				alt38=1;
			}
			switch (alt38) {
				case 1 :
					// src/Tiger.g:276:9: ':' k= ID
					{
					char_literal142=(Token)match(input,28,FOLLOW_28_in_funDec2600);  
					stream_28.add(char_literal142);

					k=(Token)match(input,ID,FOLLOW_ID_in_funDec2614);  
					stream_ID.add(k);

					}
					break;

			}

			char_literal143=(Token)match(input,34,FOLLOW_34_in_funDec2627);  
			stream_34.add(char_literal143);

			pushFollow(FOLLOW_exp_in_funDec2633);
			exp144=exp();
			state._fsp--;

			stream_exp.add(exp144.getTree());
			// src/Tiger.g:281:5: ( -> ^( 'function' ID ^( CALLTYPE ( $i $j)* ) ( $k)? exp ) )
			// src/Tiger.g:281:6: 
			{
			// AST REWRITE
			// elements: k, i, j, 45, ID, exp
			// token labels: k
			// rule labels: retval
			// token list labels: i, j
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleTokenStream stream_k=new RewriteRuleTokenStream(adaptor,"token k",k);
			RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i", list_i);
			RewriteRuleTokenStream stream_j=new RewriteRuleTokenStream(adaptor,"token j", list_j);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 281:6: -> ^( 'function' ID ^( CALLTYPE ( $i $j)* ) ( $k)? exp )
			{
				// src/Tiger.g:281:9: ^( 'function' ID ^( CALLTYPE ( $i $j)* ) ( $k)? exp )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(stream_45.nextNode(), root_1);
				adaptor.addChild(root_1, stream_ID.nextNode());
				// src/Tiger.g:281:25: ^( CALLTYPE ( $i $j)* )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(CALLTYPE, "CALLTYPE"), root_2);
				// src/Tiger.g:281:36: ( $i $j)*
				while ( stream_i.hasNext()||stream_j.hasNext() ) {
					adaptor.addChild(root_2, stream_i.nextNode());
					adaptor.addChild(root_2, stream_j.nextNode());
				}
				stream_i.reset();
				stream_j.reset();

				adaptor.addChild(root_1, root_2);
				}

				// src/Tiger.g:281:47: ( $k)?
				if ( stream_k.hasNext() ) {
					adaptor.addChild(root_1, stream_k.nextNode());
				}
				stream_k.reset();

				adaptor.addChild(root_1, stream_exp.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "funDec"


	public static class varDec_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "varDec"
	// src/Tiger.g:284:1: varDec : 'var' ID ( ':' ID )? ':=' exp ( -> ^( 'var' ID ( ID )? exp ) ) ;
	public final TigerParser.varDec_return varDec() throws RecognitionException {
		TigerParser.varDec_return retval = new TigerParser.varDec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal145=null;
		Token ID146=null;
		Token char_literal147=null;
		Token ID148=null;
		Token string_literal149=null;
		ParserRuleReturnScope exp150 =null;

		Object string_literal145_tree=null;
		Object ID146_tree=null;
		Object char_literal147_tree=null;
		Object ID148_tree=null;
		Object string_literal149_tree=null;
		RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
		RewriteRuleTokenStream stream_29=new RewriteRuleTokenStream(adaptor,"token 29");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
		RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");

		try {
			// src/Tiger.g:285:5: ( 'var' ID ( ':' ID )? ':=' exp ( -> ^( 'var' ID ( ID )? exp ) ) )
			// src/Tiger.g:285:5: 'var' ID ( ':' ID )? ':=' exp ( -> ^( 'var' ID ( ID )? exp ) )
			{
			string_literal145=(Token)match(input,54,FOLLOW_54_in_varDec2677);  
			stream_54.add(string_literal145);

			ID146=(Token)match(input,ID,FOLLOW_ID_in_varDec2683);  
			stream_ID.add(ID146);

			// src/Tiger.g:287:5: ( ':' ID )?
			int alt39=2;
			int LA39_0 = input.LA(1);
			if ( (LA39_0==28) ) {
				alt39=1;
			}
			switch (alt39) {
				case 1 :
					// src/Tiger.g:287:9: ':' ID
					{
					char_literal147=(Token)match(input,28,FOLLOW_28_in_varDec2693);  
					stream_28.add(char_literal147);

					ID148=(Token)match(input,ID,FOLLOW_ID_in_varDec2703);  
					stream_ID.add(ID148);

					}
					break;

			}

			string_literal149=(Token)match(input,29,FOLLOW_29_in_varDec2716);  
			stream_29.add(string_literal149);

			pushFollow(FOLLOW_exp_in_varDec2722);
			exp150=exp();
			state._fsp--;

			stream_exp.add(exp150.getTree());
			// src/Tiger.g:292:5: ( -> ^( 'var' ID ( ID )? exp ) )
			// src/Tiger.g:292:6: 
			{
			// AST REWRITE
			// elements: ID, exp, ID, 54
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 292:6: -> ^( 'var' ID ( ID )? exp )
			{
				// src/Tiger.g:292:9: ^( 'var' ID ( ID )? exp )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(stream_54.nextNode(), root_1);
				adaptor.addChild(root_1, stream_ID.nextNode());
				// src/Tiger.g:292:20: ( ID )?
				if ( stream_ID.hasNext() ) {
					adaptor.addChild(root_1, stream_ID.nextNode());
				}
				stream_ID.reset();

				adaptor.addChild(root_1, stream_exp.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "varDec"

	// Delegated rules



	public static final BitSet FOLLOW_exp_in_program137 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_program139 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_orExp_in_exp151 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_29_in_exp161 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_exp172 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_andExp_in_orExp190 = new BitSet(new long[]{0x0200000000000002L});
	public static final BitSet FOLLOW_57_in_orExp200 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_andExp_in_orExp211 = new BitSet(new long[]{0x0200000000000002L});
	public static final BitSet FOLLOW_57_in_orExp225 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_andExp_in_orExp240 = new BitSet(new long[]{0x0200000000000002L});
	public static final BitSet FOLLOW_compExp_in_andExp269 = new BitSet(new long[]{0x0000000000080002L});
	public static final BitSet FOLLOW_19_in_andExp279 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_compExp_in_andExp290 = new BitSet(new long[]{0x0000000000080002L});
	public static final BitSet FOLLOW_19_in_andExp304 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_compExp_in_andExp319 = new BitSet(new long[]{0x0000000000080002L});
	public static final BitSet FOLLOW_addExp_in_compExp348 = new BitSet(new long[]{0x0000001F80000002L});
	public static final BitSet FOLLOW_set_in_compExp358 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_addExp_in_compExp453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_mulExp_in_addExp471 = new BitSet(new long[]{0x0000000002800002L});
	public static final BitSet FOLLOW_23_in_addExp481 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_mulExp_in_addExp492 = new BitSet(new long[]{0x0000000002800002L});
	public static final BitSet FOLLOW_23_in_addExp506 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_mulExp_in_addExp521 = new BitSet(new long[]{0x0000000002800002L});
	public static final BitSet FOLLOW_25_in_addExp549 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_mulExp_in_addExp560 = new BitSet(new long[]{0x0000000002800002L});
	public static final BitSet FOLLOW_23_in_addExp574 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_mulExp_in_addExp589 = new BitSet(new long[]{0x0000000002800002L});
	public static final BitSet FOLLOW_23_in_addExp607 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_mulExp_in_addExp626 = new BitSet(new long[]{0x0000000002800002L});
	public static final BitSet FOLLOW_unaryExp_in_mulExp670 = new BitSet(new long[]{0x0000000008400002L});
	public static final BitSet FOLLOW_22_in_mulExp680 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_mulExp691 = new BitSet(new long[]{0x0000000008400002L});
	public static final BitSet FOLLOW_22_in_mulExp705 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_mulExp720 = new BitSet(new long[]{0x0000000008400002L});
	public static final BitSet FOLLOW_27_in_mulExp748 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_mulExp759 = new BitSet(new long[]{0x0000000008400002L});
	public static final BitSet FOLLOW_22_in_mulExp773 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_mulExp788 = new BitSet(new long[]{0x0000000008400002L});
	public static final BitSet FOLLOW_22_in_mulExp806 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_mulExp825 = new BitSet(new long[]{0x0000000008400002L});
	public static final BitSet FOLLOW_seqExp_in_unaryExp869 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_negExp_in_unaryExp875 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_valueExp_in_unaryExp881 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ifExp_in_unaryExp887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_whileExp_in_unaryExp893 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_forExp_in_unaryExp899 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_letExp_in_unaryExp905 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STR_in_unaryExp911 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_unaryExp917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_49_in_unaryExp923 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_40_in_unaryExp929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_seqExp940 = new BitSet(new long[]{0x0083510002321800L});
	public static final BitSet FOLLOW_exp_in_seqExp950 = new BitSet(new long[]{0x0000000040200000L});
	public static final BitSet FOLLOW_30_in_seqExp964 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_seqExp978 = new BitSet(new long[]{0x0000000040200000L});
	public static final BitSet FOLLOW_21_in_seqExp1002 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_25_in_negExp1028 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_negExp1035 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_valueExp1047 = new BitSet(new long[]{0x0100002004100002L});
	public static final BitSet FOLLOW_20_in_valueExp1067 = new BitSet(new long[]{0x0083510002321800L});
	public static final BitSet FOLLOW_exp_in_valueExp1081 = new BitSet(new long[]{0x0000000001200000L});
	public static final BitSet FOLLOW_24_in_valueExp1099 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_valueExp1117 = new BitSet(new long[]{0x0000000001200000L});
	public static final BitSet FOLLOW_21_in_valueExp1153 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_37_in_valueExp1186 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_valueExp1196 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_38_in_valueExp1206 = new BitSet(new long[]{0x0004002004000002L});
	public static final BitSet FOLLOW_37_in_valueExp1250 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_valueExp1268 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_38_in_valueExp1286 = new BitSet(new long[]{0x0000002004000002L});
	public static final BitSet FOLLOW_26_in_valueExp1334 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_valueExp1352 = new BitSet(new long[]{0x0000002004000002L});
	public static final BitSet FOLLOW_50_in_valueExp1411 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_valueExp1425 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_26_in_valueExp1473 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_valueExp1483 = new BitSet(new long[]{0x0000002004000002L});
	public static final BitSet FOLLOW_37_in_valueExp1519 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_valueExp1533 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_38_in_valueExp1547 = new BitSet(new long[]{0x0000002004000002L});
	public static final BitSet FOLLOW_26_in_valueExp1587 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_valueExp1601 = new BitSet(new long[]{0x0000002004000002L});
	public static final BitSet FOLLOW_56_in_valueExp1648 = new BitSet(new long[]{0x0400000000000800L});
	public static final BitSet FOLLOW_ID_in_valueExp1662 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_34_in_valueExp1676 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_valueExp1690 = new BitSet(new long[]{0x0400000001000000L});
	public static final BitSet FOLLOW_24_in_valueExp1708 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_valueExp1726 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_34_in_valueExp1744 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_valueExp1762 = new BitSet(new long[]{0x0400000001000000L});
	public static final BitSet FOLLOW_58_in_valueExp1798 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_46_in_ifExp1843 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_ifExp1850 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_51_in_ifExp1856 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_ifExp1863 = new BitSet(new long[]{0x0000040000000002L});
	public static final BitSet FOLLOW_42_in_ifExp1895 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_ifExp1906 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_55_in_whileExp1924 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_whileExp1931 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_41_in_whileExp1937 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_whileExp1944 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_44_in_forExp1955 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_forExp1962 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_29_in_forExp1968 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_forExp1975 = new BitSet(new long[]{0x0010000000000000L});
	public static final BitSet FOLLOW_52_in_forExp1981 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_forExp1988 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_41_in_forExp1994 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_unaryExp_in_forExp2001 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_48_in_letExp2012 = new BitSet(new long[]{0x0060200000000000L});
	public static final BitSet FOLLOW_dec_in_letExp2018 = new BitSet(new long[]{0x0060A00000000000L});
	public static final BitSet FOLLOW_47_in_letExp2025 = new BitSet(new long[]{0x0083590002121800L});
	public static final BitSet FOLLOW_exp_in_letExp2035 = new BitSet(new long[]{0x0000080040000000L});
	public static final BitSet FOLLOW_30_in_letExp2049 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_letExp2063 = new BitSet(new long[]{0x0000080040000000L});
	public static final BitSet FOLLOW_43_in_letExp2087 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_tyDec_in_dec2124 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_funDec_in_dec2130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_varDec_in_dec2136 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_53_in_tyDec2147 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_tyDec2153 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_34_in_tyDec2159 = new BitSet(new long[]{0x0100008000000800L});
	public static final BitSet FOLLOW_ID_in_tyDec2169 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_39_in_tyDec2200 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_50_in_tyDec2210 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_tyDec2220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_56_in_tyDec2255 = new BitSet(new long[]{0x0400000000000800L});
	public static final BitSet FOLLOW_ID_in_tyDec2269 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_tyDec2283 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_tyDec2297 = new BitSet(new long[]{0x0400000001000000L});
	public static final BitSet FOLLOW_24_in_tyDec2315 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_tyDec2333 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_tyDec2351 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_tyDec2369 = new BitSet(new long[]{0x0400000001000000L});
	public static final BitSet FOLLOW_58_in_tyDec2405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_45_in_funDec2452 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_funDec2458 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_20_in_funDec2464 = new BitSet(new long[]{0x0000000000200800L});
	public static final BitSet FOLLOW_ID_in_funDec2478 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_funDec2488 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_funDec2502 = new BitSet(new long[]{0x0000000001200000L});
	public static final BitSet FOLLOW_24_in_funDec2516 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_funDec2534 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_funDec2548 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_funDec2566 = new BitSet(new long[]{0x0000000001200000L});
	public static final BitSet FOLLOW_21_in_funDec2590 = new BitSet(new long[]{0x0000000410000000L});
	public static final BitSet FOLLOW_28_in_funDec2600 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_funDec2614 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_34_in_funDec2627 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_funDec2633 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_54_in_varDec2677 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_varDec2683 = new BitSet(new long[]{0x0000000030000000L});
	public static final BitSet FOLLOW_28_in_varDec2693 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_varDec2703 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_29_in_varDec2716 = new BitSet(new long[]{0x0083510002121800L});
	public static final BitSet FOLLOW_exp_in_varDec2722 = new BitSet(new long[]{0x0000000000000002L});
}
