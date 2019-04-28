package expresso;

import expresso.parser.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public interface Expression {

    /**
     * @param String input Expression to parse
     * @return Expression expression AST for the input
     * @throws IllegalArgumentException If the expression is invalid
     */
    public static Expression parse(String input) {
        CharStream stream = new ANTLRInputStream(input);
        
        ExpressionLexer lexer = new ExpressionLexer(stream);
        lexer.reportErrorsAsExceptions();

        TokenStream tokens = new CommonTokenStream(lexer);

       
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

    /**
    
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
     
     * @return true if the Expression is Distributable, false otherwise
     */
    public boolean isDistributable();
    
    /**
   
     * @return true if the Expression is Parameterizable, false otherwise
     */
    public boolean isParameterizable();
    
    /**
    
     * @return true if the Expression is a literal, false otherwise
     */
    public boolean isLiteral();
    
    /**
    
     * @param Object obj  The object to test equality with
     * @return boolean True if obj and the current instance are equal
     */
    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();
}
