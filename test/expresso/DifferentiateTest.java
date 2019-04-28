package expresso;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class contains tests for the differentiate static method in the Expressions class.
 */
public class DifferentiateTest {
    
    /*
     * Test strategy for differentiate:
     * 
     * Partitions:
     * - constant, variable
     * - variable of degree 1, 2+
     * 
     * - add constants and/or variables
     * - add equal polynomial terms
     * - polynomial term with more than one variable
     * 
     * - simplified expression
     * - unsimplified expression
     * 
     * - differentiate with respect to valid variable not in the expression
     * 
     * - whitespaces
     * 
     * Test cases:
     * - 5.3, with respect to x
     * - foo, with respect to foo
     * - 4.2 + foo, with respect to foo
     * - x + foo, with respect to foo
     * - foo* foo, with respect to foo
     * - 4*(x*y + y*x), with respect to y
     * - 4 + (x*x*x), with respect to x
     * - 4*(x*y + y*x + x*x*x), with respect to x
     * - 4*(x*x*y + y), with respect to y
     * - food + food + 2.45*food, with respect to food
     * - foo* foo, with respect to food
     */
    
    @Test
    public void testDifferentiateConstant() {
        assertEquals(Expressions.differentiate("   5.3", "x"), "0.0");
    }
    
    @Test
    public void testDifferentiateVariable() {
        assertEquals(Expressions.differentiate("foo  ","foo"), "1.0");
    }
    
    @Test
    public void testDifferentiateAddition() {
        assertEquals(Expressions.differentiate("4.2 + foo", "foo"), "1.0");
    }
    
    @Test
    public void testDifferentiateIdentity() {
        assertEquals(Expressions.differentiate("x + foo", "foo"), "1.0");
    }
    
    
    @Test
    public void testDifferentiateMultiplication() {
        assertEquals(Expressions.differentiate("foo* foo","foo"), "2.0*foo");
    }
    
    @Test
    public void testDifferentiateAdditionMultiplicationUnsimplified() {
        assertEquals(Expressions.differentiate("4*(x*y + y*x)", "y"), "8.0*x");
    }
    
    @Test
    public void testDifferentiateAdditionMultiplication() {
        assertEquals(Expressions.differentiate("4 + (x*x*x)", "x"), "3.0*x*x");
    }
    
    @Test
    public void testDifferentiateComplicated() {
        assertEquals(Expressions.differentiate("4*(x*y + y*x + x*x*x)", "x"), "12.0*x*x+8.0*y");
    }
    
    @Test
    public void testDifferentiateMultipleVariablePolyTerm() {
        assertEquals(Expressions.differentiate("4*(x*x*y + y)", "y"), "4.0*x*x+4.0");
    }
    
    @Test
    public void testDifferentiateSumOfEqualPolyTerms() {
        assertEquals(Expressions.differentiate("food*food + food*food + 2.45*(food*food)", "food"), "8.9*food");
    }
    
    @Test
    public void testDifferentiateAbsentVariable() {
        assertEquals(Expressions.differentiate("foo* foo", "food"), "0.0");
    }
}
