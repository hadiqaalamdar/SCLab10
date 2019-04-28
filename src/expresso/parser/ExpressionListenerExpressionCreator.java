package expresso.parser;

import java.util.Stack;

import expresso.*;
import expresso.parser.ExpressionParser.*;

/**
 * This class represents a listener that extends {@link ExpressionBaseListener}
 * An instance of ExpressionListenerExpressionCreator is passed into a parse
 * tree's walker method, and the listener methods are fired every time the walk
 * visits a node of a specific type. Only a subset of nodes have listeners
 * attached.
 *
 * Upon concluding the walk, a tree of Expression nodes is created, and can be
 * accessed by calling getExpression() on the instance
 */
public class ExpressionListenerExpressionCreator extends ExpressionBaseListener {

    private final Stack<Expression> stack = new Stack<Expression>();

    /**
     * Tries to assert that the Expression tree is complete
     * This is not a bijective condition; it only ensures that there are no dangling nodes, 
     * i.e. that there is only one node left that has no parent, namely, the root node.
     *
     * This method is fired whenever the walker exits the root node
     *
     * @param ctx root context
     * @return none
     * @throws RuntimeException if the Expression tree is incomplete
     */
    public void exitRoot(RootContext ctx) {
        if (stack.size() != 1) throw new RuntimeException("Parse tree is awkward!");
    }

    /**
     * If the node is a literal, push it into the stack; this passes the literal
     * into its ancestor Expression's constructor. By design, the ancestor
     * Expression is either a MultiplicationExpression or an AdditionExpression.
     *
     * This method is fired whenever the walker exits a literal node
     *
     * @param ctx literal context
     */
    public void exitLiteral(LiteralContext ctx) {
        String token = ctx.getText();
        if (token.matches("[a-zA-Z]+")) {
            Expression variable = new Variable(token);
            stack.push(variable);
        } else {
            Expression constant = new Constant(Double.valueOf(token));
            stack.push(constant);
        }
    }

    /**
     * If the node is a mult_expression, push it into the stack; this passes it
     * into its ancestor Expression's constructor. By design, the ancestor
     * Expression is either a MultiplicationExpression or an AdditionExpression.
     *
     * Also creates a MultiplicationExpression using the top two elements in the
     * stack; this assigns the Expression's closest two descendants as its
     * children The descendant expressions can be MultiplicationExpressions,
     * Variables, or Constants.
     * 
     * This method is fired whenever the walker exits a multExpression node
     *
     * @param ctx multExpression context
     */
    public void exitMultExpression(MultExpressionContext ctx) {
        Expression rightExpression = stack.pop();
        Expression leftExpression = stack.pop();
        Expression multiplicationExpression = new MultiplicationExpression(leftExpression, rightExpression);
        stack.push(multiplicationExpression);
    }

    /**
     * If the node is a add_expresison, push it into the stack; this passes it
     * into its ancestor Expression's constructor. By design, the ancestor
     * Expression is either a MultiplicationExpression or an AdditionExpression.
     *
     * Also creates an AdditionExpression using the top two elements in the
     * stack; this assigns the Expression's closest two descendants as its
     * children. The descendants can be AdditionExpressions,
     * MultiplicationExpressions, Variables, or Constants.
     * 
     * This method is fired whenever the walker exits an addExpression node
     *
     * @param ctx addExpression context
     */
    public void exitAddExpression(AddExpressionContext ctx) {
        Expression rightExpression = stack.pop();
        Expression leftExpression = stack.pop();
        Expression additionExpression = new AdditionExpression(leftExpression, rightExpression);
        stack.push(additionExpression);
    }

    /**
     * Returns Expression type represented by given ParseTree
     * 
     * @return expression expression represented by ParseTree
     */
    public Expression getExpression() {
        return stack.get(0);
    }
}
