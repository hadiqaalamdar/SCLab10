package expresso;

import java.util.ArrayList;
import java.util.List;

/**
 * An immutable string-based class of the expression system.
 * 
 * Expressions support the following methods...
 *  differentiate(String, String)
 *  simplify(String)
 * 
 */
public class Expressions {
    private static String ADDITIVE_IDENTITY = "0.0";

    /**
     * Differentiate an expression with respect to a given variable. 
     * All constants are represented in form of [0-9]+\.[0-9]+
     * All expressions are represented in lexical order (largest power first)
     * All unnecessary spaces are removed from the returned expression
     * All identities are removed (such as unnecessary +0.0 or 1.0*) 
     * 
     * @param expression the expression to differentiate
     * @param variable the variable to differentiate by
     * @return expression's derivative with respect to variable; will be a valid simplified expression
     * @throws IllegalArgumentException if the expression or variable is invalid
     */
    public static String differentiate(String expression, String variable) {

        Expression parsedExpression = Expression.parse(expression);

        // We could also return a list of not simplified differentiate here.
        List<PolynomialTerm> listOfPolynomials = toPolynomial(parsedExpression);
        List<PolynomialTerm> simplifiedPolynomialList = PolynomialTerm.simplify(listOfPolynomials);

        // Push differentiated terms into new list
        // We need this because two different PolynomialTerm's when
        // differentiated might return the same term, e.g. d/dx(y+1)
        List<PolynomialTerm> differentiatedPolynomialList = new ArrayList<PolynomialTerm>();

        for (PolynomialTerm polynomial : simplifiedPolynomialList) {
            differentiatedPolynomialList
                    .add(polynomial.differentiate(variable));
        }

        return simplifyWithList(differentiatedPolynomialList);
    }

    /**
     * Simplify an expression. This method wraps {@link simplifyWithList()}
     * 
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static String simplify(String expression) {
        Expression parsedExpression = Expression.parse(expression);
        List<PolynomialTerm> listOfPolynomials = toPolynomial(parsedExpression);

        return simplifyWithList(listOfPolynomials);
    }

    /**
     * Simplify an expression.
     * All constants are represented in form of [0-9]+\.[0-9]+
     * All expressions are represented in lexical order (largest power first)
     * All unnecessary spaces are removed from the returned expression
     * All identities are removed (such as unnecessary +0.0 or 1.0*) 
     * Multiplied variables which are mathematically equivalent, but not structurally equivalent
     * are combined and represented as the first term (e.g. x*y + y*x = 2*x*y)
     * 
     * @param listOfPolynomials a non-empty list of PolynomialTerm's to simplify
     * @return An expression equal to the input that is a sum of terms without
     *         parentheses, where for all variables var_i in the expression, for
     *         all exponents e_i, the term (var_1^e_1 x var_2^e_2 x ... x
     *         var_n^e_n) appears at most once; each term may be multiplied by a
     *         non-zero, non-identity constant factor; and read left-to-right,
     *         the largest exponent in each term is non-increasing
     *         
     * @throws IllegalArgumentException if the expression is invalid
     */
    private static String simplifyWithList(List<PolynomialTerm> listOfPolynomials) {
        List<PolynomialTerm> simplifiedPolynomialList = PolynomialTerm.simplify(listOfPolynomials);
        StringBuffer simplifiedString = new StringBuffer();
        simplifiedString.append(simplifiedPolynomialList.get(0).toString());

        for (int i = 1; i < simplifiedPolynomialList.size(); i++) {
            String stringPoly = simplifiedPolynomialList.get(i).toString();

            // Skip loop iteration if term is the additive identity
            if (stringPoly == ADDITIVE_IDENTITY) continue;

            simplifiedString.append("+" + stringPoly);
        }

        return simplifiedString.toString();
    }

    /**
     * Returns a list of PolynomialTerms that is 
     * algebraically equivalent to those in the given expression.  
     * This method wraps {@link extractPolynomialTerms()}
     * 
     * @return a list of PolynomialTerms that is algebraically equivalent to those in expression
     */
    private static List<PolynomialTerm> toPolynomial(Expression expression) {
        Expression expansion = expression.expand();
        return extractPolynomialTerms(expansion);
    }

    /**
     * Walks through each node of the expression 
     * and extracts polynomial terms into an array.
     * 
     * @param expansion fully-expanded expression whose polynomial terms are to be extracted
     * @return a list of polynomial terms contained in the expression
     */
    private static List<PolynomialTerm> extractPolynomialTerms(Expression expansion) {
        List<PolynomialTerm> listOfPolyTerms = new ArrayList<PolynomialTerm>();

        // if expansion has properties of an addition expression
        if (!(expansion.isDistributable()) && !(expansion.isLiteral()) && !(expansion.isParameterizable())) {

            // Adds terms from both left and right children
            listOfPolyTerms.addAll(extractPolynomialTerms(expansion.getLeft()));
            listOfPolyTerms.addAll(extractPolynomialTerms(expansion.getRight()));

        } else {
            listOfPolyTerms.add(new PolynomialTerm(expansion));
        }
        return listOfPolyTerms;
    }

}
