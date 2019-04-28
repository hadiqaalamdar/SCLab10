package expresso;

/**
 * Constant is an immutable type implementing expression representing a constant.
 * 
 * Constant supports the following methods in addition to those of Expression
 * getValue()
 */
public class Constant implements Expression {

    private final double value;

    /*
     * Abstraction Function:
     * Value represents the constant value
     * 
     * Rep Invariant: 
     * value is a nonnegative double.
     * 
     * Safety From Rep Exposure:
     * name is immutable and final, 
     * so there is no risk of rep exposure.
     */

    /**
     * Creates a constant with given value
     * 
     * @param value value of constant
     */
    public Constant(double value) {
        this.value = value;
        checkRep();
    }

    @Override
    public Expression expand() {
        return this;
    }
    
    @Override
    public Expression getLeft() {
        return this;
    }

    @Override
    public Expression getRight() {
        return this;
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
        return true;
    }
    
    /**
     * We ensure structural equality in Expression (meaning order is considered)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Constant) {
            return ((Constant) obj).value == value;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Double.toString(value).hashCode();
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }   

    private void checkRep() {
        assert value >= 0;
    }
}
