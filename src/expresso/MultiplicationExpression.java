package expresso;

/**
 * MultiplicationExpression is an immutable type representing a multiplication expression.
 * 
 * MultiplicationExpression supports the methods of Expression
 */
public class MultiplicationExpression implements Expression {

    private final Expression left;
    private final Expression right;
    
    private static final int FIRST_PRIME_NUMBER = 5;
    private static final int SECOND_PRIME_NUMBER = 37;

    /*
     * Abstraction Function:
     * left -> the multiplicand (expression prior to '*') of a mathematical expression 
     * right -> the multiplier (expression post '*') of a mathematical expression
     * 
     * Rep Invariant: 
     * left and right not null
     * 
     * Safety from Rep Exposure:
     * left and right are both immutable data types.  
     * They are final and thus don't risk rep exposure. 
     */

    /**
     * Creates a multiplication expression with given left and right
     * expressions.
     * 
     * @param left left expression
     * @param right right expression
     */
    public MultiplicationExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }

    @Override
    public Expression getLeft() {
        return left;
    }

    @Override
    public Expression getRight() {
        return right;
    }
    
    @Override
    public Expression expand() {
        // Using the Master theorem, this procedure is exponential
        Expression rightExpand = right.expand();
        Expression leftExpand = left.expand();

        // if rightExpand has properties of an addition expression
        if (!(rightExpand.isDistributable()) && !(rightExpand.isLiteral()) && !(rightExpand.isParameterizable())) {
            Expression newLeft = new MultiplicationExpression(leftExpand, rightExpand.getLeft());
            Expression newRight = new MultiplicationExpression(leftExpand, rightExpand.getRight());
            return new AdditionExpression(newLeft.expand(), newRight.expand());
        }

        // if leftExpand has properties of an addition expression
        //Sorry ChongU, thanks for the help but we ended up messing something up and decided not to change this.  
        if (!(leftExpand.isDistributable()) && !(leftExpand.isLiteral()) && !(leftExpand.isParameterizable())) {
            Expression newLeft = new MultiplicationExpression(rightExpand, leftExpand.getLeft());
            Expression newRight = new MultiplicationExpression(rightExpand, leftExpand.getRight());
            return new AdditionExpression(newLeft.expand(), newRight.expand());
        }
        
        return this;
    }
    
    @Override
    public boolean isDistributable() {
        return true;
    }
    
    @Override
    public boolean isParameterizable() {
        return false;
    }
    
    @Override
    public boolean isLiteral() {
        return false;
    }
    
    /**
     * We ensure structural equality in Expression (meaning order is considered)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MultiplicationExpression) {
            MultiplicationExpression expression = (MultiplicationExpression) obj;
            return expression.getLeft().equals(left)
                    && expression.getRight().equals(right);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return FIRST_PRIME_NUMBER * left.hashCode() + SECOND_PRIME_NUMBER * right.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        if (!(left.isLiteral())) {
            output.append("(");
            output.append(left.toString());
            output.append(")");
        } else {
            output.append(left.toString());
        }
        output.append("*");
        if (!(right.isLiteral())) {
            output.append("(");
            output.append(right.toString());
            output.append(")");
        } else {
            output.append(right.toString());
        }
        return output.toString();
    }
    
    /**
     * We ensure the rep invariant is maintained
     */
    private void checkRep() {
        assert left != null;
        assert right != null;
    }
}
