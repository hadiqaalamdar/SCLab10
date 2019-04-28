package expresso;

/**
 * AdditionExpression is an immutable type implementing expression representing an addition expression.
 * 
 * AdditionExpression supports the methods of Expression
 */
public class AdditionExpression implements Expression {

    private final Expression left;
    private final Expression right;
    
    private static final int FIRST_PRIME_NUMBER = 5;
    private static final int SECOND_PRIME_NUMBER = 37;

    /*
     * Abstraction Function: 
     * left -> the augend (expression prior to '+') of a mathematical expression 
     * right -> the addend (expression post '+') of a mathematical expression
     * 
     * Representation Invariant:
     * left and right not null
     * 
     * Safety from Rep Exposure:
     * left and right are both immutable data types.  
     * They are final and thus don't risk rep exposure. 
     */

    /**
     * Creates an addition expression with given left and right expressions.
     * 
     * @param left the left expression of the addExpression
     * @param right the right expression of the addExpression
     */
    public AdditionExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }

    @Override
    public Expression expand() {
        //An expanded expression is the addition of expressions is an AdditionExpression
        return new AdditionExpression(left.expand(), right.expand());
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
    public boolean isDistributable() {
        return false;
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
     * We ensure structural equality in Expression (meaning order is considered_
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AdditionExpression) {
            AdditionExpression expression = (AdditionExpression) obj;
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

    /**
     * We ensure the rep invariant is maintained
     */
    private void checkRep() {
        assert left != null;
        assert right != null;
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
        output.append(" + ");
        if (!(right.isLiteral())) {
            output.append("(");
            output.append(right.toString());
            output.append(")");
        } else {
            output.append(right.toString());
        }
        return output.toString();
    }
}
