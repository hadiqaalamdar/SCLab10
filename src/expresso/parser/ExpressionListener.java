// Generated from Expression.g4 by ANTLR 4.5

package expresso.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExpressionParser}.
 */
public interface ExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ExpressionParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ExpressionParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ExpressionParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ExpressionParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#rootExpression}.
	 * @param ctx the parse tree
	 */
	void enterRootExpression(ExpressionParser.RootExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#rootExpression}.
	 * @param ctx the parse tree
	 */
	void exitRootExpression(ExpressionParser.RootExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(ExpressionParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(ExpressionParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#parenExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(ExpressionParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#parenExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(ExpressionParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#multExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultExpression(ExpressionParser.MultExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#multExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultExpression(ExpressionParser.MultExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#addExpression}.
	 * @param ctx the parse tree
	 */
	void enterAddExpression(ExpressionParser.AddExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#addExpression}.
	 * @param ctx the parse tree
	 */
	void exitAddExpression(ExpressionParser.AddExpressionContext ctx);
}