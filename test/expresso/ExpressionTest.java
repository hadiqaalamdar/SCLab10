package expresso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.Future;

import javax.swing.JDialog;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import expresso.parser.ExpressionLexer;
import expresso.parser.ExpressionParser;

/**
 * This class contains tests for the language of balanced parentheses.
 * 
 * Expression supports the following methods...
 *  parse(String)
 *  expand()
 *  getType()
 *  getLeft()
 *  getRight()
 *  equals()
 *  hashCode()
 *  toString()
 */
public class ExpressionTest {
    
    /*
     * Test strategy for expression parser:
     * 
     * Partitions:
     * - valid, invalid input
     * - 0, 1, 2+ addition operations
     * - 0, 1, 2+ multiplication operations
     * - 0, 1, 2+ variables (length 1, 1+)
     * - 0, 1, 2+ constants (integer, float)
     * - sequence of parentheses
     * - nested parentheses
     * - unbalanced parentheses
     * - missing operation
     * - missing variable/constant
     * - no whitespaces
     * - begin, middle, end whitespaces
     * 
     * Test cases:
     * - 3 + 2.4
     * - 3 * x + 2.4
     * - 3 * (x + 2.4)
     * - ((3 + 4) * x * x)
     * - foo + bar+baz
     * - (2*x    )+    (    y*x    )
     * - 4 + 3 * x + 2 * x * x + 1 * x * x * (((x)))
     * - 3 *
     * - ( 3
     * - 3 x
     */
    
    /*
     * Test strategy for structural equality:
     * 
     * Partitions:
     * - same order variables, values
     * - different order variables, values
     * 
     * - same operations
     * - different operations
     * 
     * - parentheses
     * - no parentheses
     * 
     * - different groupings, mathematically equal
     * 
     * - integer or decimal (i.e. 1 vs. 1.000)
     * 
     * - whitespaces
     * 
     * Test cases:
     * - "x + y + z" and "x+y+z" (equal)
     * - "4.0*2.0 + 3.4" and "3.4 + 4.0*   2.0" (not equal, different order of values)
     * - "x + y + z" and "x*y+z" (not equal, different operations)
     * - "(x + y + z)" and "x+y+z" (equal)
     * - "(x) + (y) + (z)" and "x+y+z" (equal)
     * - "(x*y)*z" and "x*(y*z)" (not equal, different groupings)
     * - "x+1" and "x+1.00000" (equal)
     */
    private static final boolean DISPLAY_GRAPHICS = true;
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Tests for Expression parse
    
    @Test
    public void testExpressionAddConstants() {
        Expression.parse("3 + 2.4");
    }
    
    @Test
    public void testExpressionAddMultiplyConstantVariable() {
        Expression.parse("3 * x + 2.4");
    }
    
    @Test
    public void testExpressionParentheses() {
        Expression.parse("3 * (x + 2.4030)");
    }
    
    @Test
    public void testExpressionNestedParentheses() {
        Expression.parse("((3 + 4) * x * x)");
    }
    
    @Test
    public void testExpressionVariablesOnly() {
        Expression.parse("foo + bar+baz");
    }
    
    @Test
    public void testExpressionMultiply() {
        Expression.parse("(3+5*6)*4*3");
    }
    
    @Test
    public void testExpressionMultiplyPlus() {
        Expression.parse("(3+5*6)*4*3+3");
    }
    
    @Test
    public void testVariableExpression() {
        Expression.parse("abc");
    }

        
    public void testExpressionSequenceParentheses() {
        Expression.parse("(2*x    )+    (    y*x    )");
    }
    
    @Test
    public void testComplicatedExpression() {
        Expression.parse("4 + 3 * x + 2 * x * x + 1 * x * x * (((x)))");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testExpressionMissingConstant() {
        Expression.parse("3 *");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testExpressionUnbalancedParentheses() {
        Expression.parse("( 3");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testExpressionMissingOperation() {
        Expression.parse("3 x");
    }
    
    // Tests for structural equality
    
    @Test
    public void testEqualityWhiteSpace() {
        assertEquals(Expression.parse("x + y + z"), Expression.parse("x+y+z"));
    }
    
    @Test
    public void testEqualityOperationOrder() {
        assertFalse(Expression.parse("4.0*2.0 + 3.4").equals(Expression.parse("3.4 + 4.0*   2.0")));
    }
    
    @Test
    public void testEqualityDifferentOperations() {
        assertFalse(Expression.parse("x + y + z").equals(Expression.parse("x*y+z")));
    }
    
    @Test
    public void testEqualityEqualGrouping() {
        assertEquals(Expression.parse("(x + y + z)"), Expression.parse("x + y + z"));
    }
    
    @Test
    public void testEqualityEqualGrouping2() {
        assertEquals(Expression.parse("(x) + (y) + (z)"), Expression.parse("x + y + z"));
    }
    
    @Test
    public void testEqualityNotEqualGrouping() {
        assertFalse(Expression.parse("(x*y)*z").equals(Expression.parse("x*(y*z)")));
    }
    
    @Test
    public void testEqualityIntegerDouble() {
        assertEquals(Expression.parse("x+1"), Expression.parse("x + 1.00000"));
    }

    //Shows visual tree
    private void parseToDebug(String string) {
        CharStream stream = new ANTLRInputStream(string);

        // Instantiate lexer
        ExpressionLexer lexer = new ExpressionLexer(stream);
        lexer.reportErrorsAsExceptions();

        TokenStream tokens = new CommonTokenStream(lexer);

        // Instantiate parser
        ExpressionParser parser = new ExpressionParser(tokens);
        parser.reportErrorsAsExceptions();

        ParseTree tree = parser.root();

        if (DISPLAY_GRAPHICS) {
          try {
            System.out.println(tree.toStringTree(parser));
            Future<JDialog> future = ((RuleContext)tree).inspect(parser);
            Utils.waitForClose(future.get());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
    }
}
