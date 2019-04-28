package expresso;

/**
 * Variable is an immutable type representing a variable.
 * 
 * Variable supports the following methods in addition to those of Expression
 * getName()
 */
public class Variable implements Expression {

    private final String name;

    /*
     * Abstraction Function:
     * name represents the name of the variable
     * 
     * Rep Invariant:
     * name is a string of lowercase and/or uppercase alphabet letters. 
     * length of name is at least 1.
     * 
     * Safety from Rep Exposure:
     * name is immutable, so there is no risk of rep exposure.
     */

    /**
     * Creates a variable with given name.
     * 
     * @param name name of variable
     */
    public Variable(String name) {
        this.name = name;
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
        return true;
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
        if (obj instanceof Variable) {
            return ((Variable) obj).name.equals(name);
        } else {
            return false;
        }    
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return name;
    }

    /**
     * We ensure the rep invariant is maintained
     */
    private void checkRep() {
        name.matches("[a-zA-Z]+");
    }
}
