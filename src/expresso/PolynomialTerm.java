package expresso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * PolynomialTerm is an immutable type representing 
 * a term in a polynomial expression.
 * PolynomialTerms are non-negative.
 *
 * Two instances of PolynomialTerm are equal iff they contain the same variables
 * that are raised to the same power.  This is mathematical, not structural equality. 
 * 
 * For example:
 * 1) x*y = y*x 
 * 2) 2*x*y = y*x 
 * 3) x*y*x != y*x
 * 
 * PolynomialTerm supports the following methods...
 * differentiate(Expression)
 * simplify(Expression)
 */
public class PolynomialTerm {
    
    /*
     * Abstraction Function: 
     * coefficient -> the coefficient for the polynomial term as a double
     * variables -> a hashMap representing the variables [a-zA-z]+ multiplied within the polynomialTerm
     * variables.key -> The string representation of a variable (e.g., x, foo...)
     * variables.value -> the exponent of the variable represented by the string in the key
     * 
     * Representation Invariant:
     * coefficient >= 0 (We cannot have negative numbers)
     * variables != null (We can have an empty map when we have a constant expression)
     * variables.key == [a-zA-z]+
     * 
     * Safety from Rep Exposure:
     * We never return coefficient and variables, 
     * they are touched only within PolynomialTerm 
     * and thus are safe from rep exposure.
     */

    private double coefficient = 1.;
    private Map<String, Integer> variables = new HashMap<String, Integer>();

    /**
     * An instance of PolynomialTerm is constructed with an instance of Expression
     * The given expression must be of only nested MEs.  
     * 
     * @param expression Expression must only contain ME descendants
     * @return an instance of a PolynomialTerm representing the imputed expression
     */
    public PolynomialTerm(Expression expression) {
        walkTree(expression);
        MultipliedByZero();
        checkRep();
    }

    /**
     * PolynomialTerm can also be constructed explicitly with the Map and Constant term
     * 
     * @param coefficient a non-negative double constant multiplying the given variables
     * @param variables a map of variables in string (must be [a-zA-z]+) and the power they are raised to
     * @return a polynomialTerm representing the inputs
     */
    public PolynomialTerm(double coefficient, Map<String, Integer> variables) {
        this.coefficient = coefficient;
        this.variables = new HashMap<String, Integer>(variables);
        MultipliedByZero();
        checkRep();
    }

    /**
     * Turns a PolynomialTerm multiplied by zero into zero. This flushes the
     * variables, ensuring the hashcode of all polynomials multiplied by 0, 
     * equaling 0 are the same.
     */
    private void MultipliedByZero() {
        if (this.coefficient == 0) {
            this.variables.clear();
        }
    }

    /**
     * Returns a PolynomialTerm that is a differentiated form of the current term
     *
     * @param String variable The partial derivative
     * @return A new PolynomialTerm that is differentiated with respect to variable
     */
    public PolynomialTerm differentiate(String variable) {
        double newCoefficient = coefficient;
        Map<String, Integer> newVariables = new HashMap<String, Integer>(variables);
        if (newVariables.containsKey(variable)) {
            int power = newVariables.get(variable);
            if (power > 1) {
                newVariables.put(variable, power - 1);
                newCoefficient = newCoefficient * power;
            } else {
                newVariables.remove(variable);
            }
        } else {
            newCoefficient = 0;
        }
        return new PolynomialTerm(newCoefficient, newVariables);
    }

    /**
     * This method is called in the PolynomialTerm constructor taking in an expression. 
     * It is a recursive procedure. The method starts by walking the tree rooted at node, 
     * and if any of the descendant nodes rooted are literals, 
     * records the literal in our internal representation
     *
     * Expression must only contain ME descendants
     *
     * @param Expression node The root node of the subtree, containing only ME descendants
     */ 
    private void walkTree(Expression node) {

        // If node has properties of a multiplication expression
        if (node.isDistributable() && !(node.isLiteral()) && !(node.isParameterizable())) {
            walkTree(node.getLeft());
            walkTree(node.getRight());
            return;
        }

        // If node has properties of a variable
        if (node.isLiteral() && node.isParameterizable()) {
            String variable = node.toString();
            int power = variables.containsKey(variable) ? variables
                    .get(variable) + 1 : 1;
            variables.put(variable, power);
            return;
        }

        // If node has properties of a constant
        if (node.isLiteral() && !(node.isParameterizable())) {
            coefficient = coefficient * Double.parseDouble(node.toString());
            return;
        }

        // Default
        throw new RuntimeException("Can't cast to PolynomialTerm");
    }


    /**
     * This method simplifies a non-empty list of PolynomialTerms into a
     * simplified list of PolynomialTerms by adding their coefficients.
     * For example, [2*x, 3*x, 4*x*x, 5, 2] will be merged into [4*x*x, 5*x, 7]
     * 
     * The list is returned in lexical order.  When two variables have the same 
     * exponential value, the one with the earliest appearance in the list is chosen first.
     * For example, [2*x, 2*y, 3*z, 4*y] will be merged into [2*x, 6*y, 3*z]
     *
     * @param listOfPolynomials is a list of PolynomialTerms we want simplified
     * @return a new list of polynomials where some have been combined, then sorted in lexical order.
     */
    public static List<PolynomialTerm> simplify(List<PolynomialTerm> listOfPolynomials){

        Map<Integer, PolynomialTerm> newPolynomialMap = new HashMap<Integer, PolynomialTerm>();

        for (PolynomialTerm polynomial: listOfPolynomials){

            int key = polynomial.hashCode();

            if (!newPolynomialMap.containsKey(key)){
                newPolynomialMap.put(key, polynomial);
            } else {
                PolynomialTerm containedPolynomial = newPolynomialMap.get(key);
                PolynomialTerm combinedPolynomial = new PolynomialTerm(containedPolynomial.coefficient + polynomial.coefficient, polynomial.variables);
                newPolynomialMap.put(key, combinedPolynomial);
            }
        }

        List<PolynomialTerm> simplifiedPolynomialList = new ArrayList<PolynomialTerm>();
        simplifiedPolynomialList.addAll(newPolynomialMap.values());

        Collections.sort(simplifiedPolynomialList,
                new Comparator<PolynomialTerm>() {
                    public int compare(PolynomialTerm firstPolynomial,
                            PolynomialTerm secondPolynomial) {
                        if (secondPolynomial.variables.values().isEmpty()) {
                            return -1;
                        }

                        if (firstPolynomial.variables.values().isEmpty()) {
                            return 1;
                        }

                        // This is to allow us to return the largest first
                        if (Collections.max(firstPolynomial.variables.values()) >= (Collections
                                .max(secondPolynomial.variables.values()))) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

        return simplifiedPolynomialList;
    }

    /**
     * This method returns a String representation of our polynomial. 
     * If the coefficient of a non-trivial polynomial term is 1, then the representation
     * does not include the coefficient, i.e. 1*x*y will be represented as x*y
     * Coefficients are represented as doubles (i.e. 5.0*x)
     *
     * @return the String representation of the PolynomialTerm
     */ 
    
    @Override
    public String toString() {
        String returnString = (coefficient == 1) ? "" : String.valueOf(coefficient);
        String operation = (coefficient == 1) ? "" : "*";
        String multiplyByZero = "0.0";

        //Takes care of * 0
        if (coefficient == 0) return multiplyByZero;

        //Takes care of identity
        if (variables.isEmpty()) return String.valueOf(coefficient);

        //Adds variables into the string
        Iterator<Entry<String, Integer>> it = variables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)it.next();
            for (int i = 0; i < pair.getValue(); i++) {
                returnString += operation + pair.getKey();
                operation = "*";
            }
        } 
        return returnString;
    }

    @Override
    public int hashCode() {
        return variables.hashCode();
    }

    @Override
    public boolean equals(Object otherTerm) {
        if (otherTerm instanceof PolynomialTerm) {
            return variables.equals(((PolynomialTerm) otherTerm).variables);
        } else {
            return false;
        }
    }

    private void checkRep() {
        assert coefficient >= 0;
        for (String key: variables.keySet()){
            assert key.matches("[a-zA-z]+");
        }                
        if (coefficient == 0) {
            assert variables.isEmpty();
        }
    }
}
