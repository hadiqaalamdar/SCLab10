package expresso;

import expresso.parser.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Expression represents a mathematical expression. 
 * This is a immutable recursive abstract datatype.
 * 
 * Expression supports the following methods...
 * parse(String)
 * expand()
 * getType()
 * getLeft()
 * getRight()
 */
public interface Expression {

    // Datatype definition
    // Expression = Constant(value:double)
    //  + Variable(value:String)
    //  + MultiplicationExpression(left:Expression, right:Expression)
    //  + AdditionExpression(left:Expression, right:Expression)

    /**
     * Parse an expression. 
     *
     * @param String input Expression to parse
     * @return Expression expression AST for the input
     * @throws IllegalArgumentException If the expression is invalid
     */
    public static Expression parse(String input) {
        CharStream stream = new ANTLRInputStream(input);

        // Instantiate lexer
        ExpressionLexer lexer = new ExpressionLexer(stream);
        lexer.reportErrorsAsExceptions();

        TokenStream tokens = new CommonTokenStream(lexer);

        // Instantiate parser
        ExpressionParser parser = new ExpressionParser(tokens);
        parser.reportErrorsAsExceptions();

        try {
          ParseTree expressionTree = parser.root();
          ParseTreeWalker walker = new ParseTreeWalker();
          ExpressionListenerExpressionCreator listener = new ExpressionListenerExpressionCreator();
          walker.walk(listener, expressionTree);

          return listener.getExpression();

        } catch (RuntimeException e) {
          throw new IllegalArgumentException("Input is invalid");
        }
    }

    // Instance methods

    /**
     * Returns an algebraically equivalent expression that consists of a sum of
     * products. Expands by distributing terms first from left to right, then from right to left.
     * 
     * @return an algebraically equivalent expression that consists of a sum of products
     */
    public Expression expand();
    
    /**
     * Returns the left expression
     * 
     * @return left expression
     */
    public Expression getLeft();
    
    /**
     * Returns the right expression
     * 
     * @return right expression
     */
    public Expression getRight();
    
    /**
     * An Expression is distributable iff either child of the Expression can be distributed
     * over its operation into the children of the other child, while maintaining the algebraic 
     * integrity of the Expression.
     *
     * For example, an Expression E = (3)*(4+5) with operation (*) and children (3) and (4+5)
     * can distribute (3) over (*) into the children of (4+5) such that E = (3*4)+(3*5)
     *
     * @return true if the Expression is Distributable, false otherwise
     */
    public boolean isDistributable();
    
    /**
     * An Expression is parameterizable iff its abstraction is a differentiatable parameter
     * according to the Expresso specification. In other words, it must be a valid variable in
     * the symbolic differentiation clause of the spec
     * 
     * @return true if the Expression is Parameterizable, false otherwise
     */
    public boolean isParameterizable();
    
    /**
     * An Expression is a literal iff it is an atomic expression, that is, if it is an algebraic
     * expression with no deeper formulaic structure. Concretely, an Expression is a literal if 
     * it is a base case to the recursive ADT. 
     *
     * @return true if the Expression is a literal, false otherwise
     */
    public boolean isLiteral();
    
    /**
     * Defines structural equality for two Expression objects.
     * Expression A and Expression B are equal if and only if their parse trees are equal.
     * If the order of operations is ambiguous, the parser automatically treats the 
     * leftmost literal as its left child, i.e.
     *
     *  1) x*y*z is equal to x*(y*z)
     *  2) x*y*z is not equal to (x*y)*z
     *  3) x*y+z is equal to (x*y)+z
     *  4) x*y+z is not equal to x*(y+z)
     * 
     * @param Object obj  The object to test equality with
     * @return boolean True if obj and the current instance are equal
     */
    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();
}
