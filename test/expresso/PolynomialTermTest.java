package expresso;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

/**
 * This class contains tests for the PolynomialTerm class
 * 
 * PolynomialTerm supports the following methods...
 *  differentiate()
 *  simplify()
 *  equals()
 *  hashCode()
 *  toString()
 */
public class PolynomialTermTest {
    
    /*
     * Testing strategy for PolynomialTerm:
     *  To test PolynomialTerm, we will ensure that equality is correctly implemented,
     *  meaning we will be checking for mathematical, not structural equality.  
     *  We will ensure all supported functions work as specified.    
     *      
     * Testing strategy for equals method:
     * 
     * Partitions:
     * - same variable, different exponent
     * - different variable, same exponent
     * - same variable, same exponent
     * 
     * - non-zero coefficients
     * - zero coefficients
     * 
     * Test cases:
     * - x and x*x
     * - x and y
     * - 1*x and 2*x
     * - 0*x*x*x and 0
     *
     * Testing strategy for toString method:
     * 
     * Partitions:
     * - identity cases
     * - coefficient = 0
     * - coefficient = 1
     * 
     * - single variable
     * - multiple variables (repeated)
     * 
     * Test cases:
     * - additive identity (0.0)
     * - multiplicative identity (1.0)
     * - 1.000*x returns x
     * - 0.00*x returns 0
     * - 4.25*foo
     * - 5.82*foo*foodly*food*food
     */
    
    private static PolynomialTerm AdditiveIdentity;
    private static PolynomialTerm MultiplicativeIdentity;
    private static Map<String, Integer> variables = new HashMap<String, Integer>();

    /**
     * Creates the identities for comparison in future tests
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        AdditiveIdentity = new PolynomialTerm(0, variables);
        MultiplicativeIdentity = new PolynomialTerm(1, variables);
    }

    /**
     * Refreshes vars in variables
     */
    @Before
    public void setUpBeforeEach() {
        variables = new HashMap<String, Integer>();
    }

    // Tests for equality
    
    /**
     * Test that two polynomials with different vars are different
     */
    @Test
    public void testEqualityVarsNegative() {
        variables.put("x", 1);
        PolynomialTerm firstTerm = new PolynomialTerm(1, variables);
        variables.put("x", 2);
        PolynomialTerm secondTerm = new PolynomialTerm(1, variables);
        assertFalse(firstTerm.equals(secondTerm));
    }
    
    /**
     * Test that two polynomials with different vars are different
     */
    @Test
    public void testEqualityVarsNegativeAlso() {
        variables.put("x", 1);
        PolynomialTerm firstTerm = new PolynomialTerm(1, variables);
        variables.put("y", 1);
        PolynomialTerm secondTerm = new PolynomialTerm(1, variables);
        assertFalse(firstTerm.equals(secondTerm));
    }

    /**
     * Test that two polynomials with different non-zero coefficients are equal 
     */
    @Test
    public void testEqualityVarsCoefficientsPositive() {
        variables.put("x", 1);
        PolynomialTerm firstTerm = new PolynomialTerm(1, variables);
        PolynomialTerm secondTerm = new PolynomialTerm(2, variables);
        assertEquals(firstTerm, secondTerm);
    }

    /**
     * Test that 0*x^3 is equal to 0
     */
    @Test
    public void testAdditiveIdentityEquality() {
        variables.put("x", 3);
        PolynomialTerm newPolynomialTerm = new PolynomialTerm(0, variables);
        assertEquals(AdditiveIdentity, newPolynomialTerm);
    }

    // Tests for toString method
    
    /**
     * Test that additive identity's string rep is 0.0
     */
    @Test
    public void testAdditiveIdentityStringRep() {
        assertEquals(AdditiveIdentity.toString(), "0.0");
    }

    /**
     * Test that multiplicative identity's string rep is 1.0
     */
    @Test
    public void testMultiplicativeIdentityStringRep() {
        assertEquals(MultiplicativeIdentity.toString(), "1.0");
    }

    /**
     * Test the string rep of polynomial term with coefficient 1
     */
    @Test
    public void testCoefficientOneStringRep() {
        variables.put("x", 1);
        PolynomialTerm newPolynomialTerm = new PolynomialTerm(1.000, variables);
        assertEquals(newPolynomialTerm.toString(), "x");
    }
    
    /**
     * Test the string rep of polynomial term with coefficient 0
     */
    @Test
    public void testCoefficientZeroStringRep() {
        variables.put("x", 1);
        PolynomialTerm newPolynomialTerm = new PolynomialTerm(0.00, variables);
        assertEquals(newPolynomialTerm.toString(), "0.0");
    }
    
    /**
     * Test the string rep of polynomial term with coefficient != 0 or 1
     */
    @Test
    public void testSingleVariableString() {
        variables.put("foo", 1);
        PolynomialTerm newPolynomialTerm = new PolynomialTerm(4.25, variables);
        assertEquals(newPolynomialTerm.toString(), "4.25*foo");
    }
    
    /**
     * Test the string rep of polynomial term 5.82*foo*foodly*food*food
     * Check for lexical order of variables
     */
    @Test
    public void testMultipleVariableString() {
        variables.put("foo", 1);
        variables.put("food", 2);
        variables.put("foodly", 1);
        PolynomialTerm newPolynomialTerm = new PolynomialTerm(5.82, variables);
        assertEquals(newPolynomialTerm.toString(), "5.82*foodly*food*food*foo");
    }
}
