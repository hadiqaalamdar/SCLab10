package expresso;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class contains tests for the simplify static method in the Expressions class.
 */
public class SimplifyTest {
    
    /*
     * Test strategy for simplify:
     * 
     * Partitions:
     *  - constant only
     *  - variable only
     *  - addition expression only
     *  - multiplication expression only
     *  
     *  - addition and multiplication expressions (not simplified)
     *  - addition and multiplication expressions (simplified)
     *  
     *  - not structurally equal, but algebraically equal polynomial terms
     *  - structurally equal polynomial terms
     *  
     *  - different terms with different largest degree
     *  - different terms with same largest degree
     *  
     *  - additive identity (i.e. x+0 -> x)
     *  - multiplicative identity (i.e. 1.0*x -> x)
     *  - coefficient is 0 (i.e. 0*x -> 0.0)
     *  
     *  - simplify to 0.0 (i.e. 0+0 -> 0.0)
     *  
     *  - extra white spaces
     *  - nested expressions
     *  
     *  - left-to-right distribution
     *  - right-to-left distribution
     *  
     *  - distribute constant
     *  - distribute variable
     * 
     * Test cases:
     *  - 5.3
     *  - 0.0*x+1.0
     *  - 0+0
     *  - 4+9
     *  - 1*foo
     *  - 4.2 + foo
     *  - 0 + foo
     *  - foo*foo
     *  - 4*(x*y + y*x)
     *  - 4 + (x*x*x)
     *  - 4*(x*y + y*x + x*x*x)
     *  - 4*(x*y + y*x + x*x*x + y*y*y)
     *  - (x*y + y*x + x*x*x)*4.0
     *  - x*(x*y + y*x + x*x*x)
     *  - 2.0*x*(x*y + y*x + x*x*x)
     */
    
    @Test
    public void testSimplifyConstant() {
        assertEquals(Expressions.simplify("   5.3"), "5.3");
    }
    
    @Test
    public void testSimplifyAdditiveIdentity() {
        assertEquals(Expressions.simplify("0.0*x+1.0"), "1.0");
    }

    @Test
    public void testSimplifyAdditiveIdentityDouble() {
        assertEquals(Expressions.simplify("0+0"), "0.0");
    }
    
    @Test
    public void testSimplifyAddConstants() {
        assertEquals(Expressions.simplify("4+9"), "13.0");
    }

    @Test
    public void testSimplifyVariable() {
        assertEquals(Expressions.simplify("1*foo  "), "foo");
    }
    
    @Test
    public void testSimplifyAddition() {
        assertEquals(Expressions.simplify("4.2 + foo"), "foo+4.2");
    }
    
    @Test
    public void testSimplifyIdentity() {
        assertEquals(Expressions.simplify("0 + foo"), "foo");
    }
    
    
    @Test
    public void testSimplifyMultiplication() {
        assertEquals(Expressions.simplify("foo* foo"), "foo*foo");
    }
    
    @Test
    public void testSimplifyAdditionMultiplicationUnsimplified() {
        assertEquals(Expressions.simplify("4*(x*y + y*x)"), "8.0*x*y");
    }
    
    @Test
    public void testSimplifyAdditionMultiplication() {
        assertEquals(Expressions.simplify("4 + (x*x*x)"), "x*x*x+4.0");
    }
    
    @Test
    public void testSimplifyComplicated() {
        assertEquals(Expressions.simplify("4*(x*y + y*x + x*x*x)"), "4.0*x*x*x+8.0*x*y");
    }
    
    @Test
    public void testSimplifySameLargestDegree() {
        assertEquals(Expressions.simplify("4*(x*y + y*x + x*x*x + y*y*y)"), "4.0*x*x*x+4.0*y*y*y+8.0*x*y");
    }
    
    @Test
    public void testSimplifyRightToLeftDistribution() {
        assertEquals(Expressions.simplify("(x*y + y*x + x*x*x)*4.0"), "4.0*x*x*x+8.0*x*y");
    }
    
    @Test
    public void testSimplifyDistributeVariable() {
        assertEquals(Expressions.simplify("x*(x*y + y*x + x*x*x)"), "x*x*x*x+2.0*x*x*y");
    }
    
    @Test // Can't cast to PolynomialTerm
    public void testSimplifyDistributeTwoVariables() {
        assertEquals(Expressions.simplify("x*x*(x*y + y*x + x*x*x)"), "x*x*x*x*x+2.0*x*x*x*y");
    }
    
    @Test // Can't cast to PolynomialTerm
    public void testSimplifyDistributeVariableAndConstant() {
        assertEquals(Expressions.simplify("2.0*x*(x*y + y*x + x*x*x)"), "2.0*x*x*x*x+4.0*x*x*y");
    }
    
    @Test // Can't cast to PolynomialTerm
    public void testSimplifyNested() {
        assertEquals(Expressions.simplify("2.0*(x*y + x*(y*x + x*x*x))"), "2.0*x*x*x*x+2.0*x*x*y+2.0*x*y");
    }
}
