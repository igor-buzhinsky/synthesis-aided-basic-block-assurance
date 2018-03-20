// Generated from C:/Users/Lenovo/repos/synthesis-aided-basic-block-assurance\ltl.g4 by ANTLR 4.7
package basic_block_generator.generated;

import java.util.*;
import basic_block_generator.formula.*;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ltlParser}.
 */
public interface ltlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ltlParser#unary_operator_sign}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator_sign(ltlParser.Unary_operator_signContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#unary_operator_sign}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator_sign(ltlParser.Unary_operator_signContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#binary_function_sign}.
	 * @param ctx the parse tree
	 */
	void enterBinary_function_sign(ltlParser.Binary_function_signContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#binary_function_sign}.
	 * @param ctx the parse tree
	 */
	void exitBinary_function_sign(ltlParser.Binary_function_signContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(ltlParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(ltlParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#proposition}.
	 * @param ctx the parse tree
	 */
	void enterProposition(ltlParser.PropositionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#proposition}.
	 * @param ctx the parse tree
	 */
	void exitProposition(ltlParser.PropositionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#binary_function}.
	 * @param ctx the parse tree
	 */
	void enterBinary_function(ltlParser.Binary_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#binary_function}.
	 * @param ctx the parse tree
	 */
	void exitBinary_function(ltlParser.Binary_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(ltlParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(ltlParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator(ltlParser.Unary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator(ltlParser.Unary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#priority1_binary_operator}.
	 * @param ctx the parse tree
	 */
	void enterPriority1_binary_operator(ltlParser.Priority1_binary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#priority1_binary_operator}.
	 * @param ctx the parse tree
	 */
	void exitPriority1_binary_operator(ltlParser.Priority1_binary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#priority2_binary_operator}.
	 * @param ctx the parse tree
	 */
	void enterPriority2_binary_operator(ltlParser.Priority2_binary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#priority2_binary_operator}.
	 * @param ctx the parse tree
	 */
	void exitPriority2_binary_operator(ltlParser.Priority2_binary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#priority3_binary_operator}.
	 * @param ctx the parse tree
	 */
	void enterPriority3_binary_operator(ltlParser.Priority3_binary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#priority3_binary_operator}.
	 * @param ctx the parse tree
	 */
	void exitPriority3_binary_operator(ltlParser.Priority3_binary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#priority4_binary_operator}.
	 * @param ctx the parse tree
	 */
	void enterPriority4_binary_operator(ltlParser.Priority4_binary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#priority4_binary_operator}.
	 * @param ctx the parse tree
	 */
	void exitPriority4_binary_operator(ltlParser.Priority4_binary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ltlParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFormula(ltlParser.FormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ltlParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFormula(ltlParser.FormulaContext ctx);
}