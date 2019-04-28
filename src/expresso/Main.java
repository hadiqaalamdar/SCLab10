package expresso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Console interface to the expression system.
 */
public class Main {

    private static final String COMMAND_PREFIX = "!";
    private static final String DERIVATIVE_PREFIX = "d/d";
    private static final String SIMPLIFY = "simplify";
    
    private static String currentExpression = "";
    
    /**
     * Read expression and command inputs from the console and output results.
     * 
     * Acceptable commands include...
     *  
     *  An empty return, which terminates the program.
     *  
     *  Each new valid input expression updates the current expression.
     *  "!simplify", which simplifies the current expression.  There must be a current stored expression.  
     *  "!d/d[variable]", which differentiates the current expression with respect to [variable].  Variable cannot be a constant.  
     *  "!", which throws a "ParseError: unknown command """, but then prints the current stored expression.
     *  
     *  With no current stored expression, the above 3 inputs return "ParseError: no stored current expression"
     *  Each valid call to differentiate (not simplify) also updates the current expression to be the differentiated expression.
     *  Input "!d/d" alerts the user they are missing a variable to differentiate by.
     *  
     * @param args unused
     * @throws IOException if there is an error reading the input
     */
    public static void main(String[] args) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("> ");
            final String input = in.readLine();

            if (input.isEmpty()) {
                return;
            }
            
            try {
                final String trimmedInput = input.trim();
                final String output;
                
                if (trimmedInput.startsWith(COMMAND_PREFIX)) {
                    output = handleCommand(trimmedInput.substring(COMMAND_PREFIX.length()));
                } else {
                    String expression = handleExpression(trimmedInput);
                    // updates current expression if input is valid
                    currentExpression = expression;
                    output = trimmedInput.replaceAll("\\s+", "");
                }
                System.out.println(output);
            } catch (RuntimeException re) {
                System.out.println("ParseError: Invalid expression");
            }
        }
    }

    /**
     * If input is parsed as a valid expression, 
     * we return the parsed expression as inputed, but with all whitespaces removed.  
     * In cases where the input cannot be parsed as a valid expression, 
     * we return an RuntimeException error.  
     * 
     * For example,
     * 1) x*y*       x+y    becomes     x*y*x+y
     * 2) 4*35+90           becomes     4*35+90
     * 3) 4.0+5*9.          becomes     4.0+5*9.
     * 4) 4((               becomes     RuntimeException
     *     
     * @param input expression
     * @return parsed expression
     */
    private static String handleExpression(String input) {
        return Expression.parse(input).toString();
    }

    /**
     * Returns output after executing command on current expression
     * If substring is "d/d[variable]" the output is the result of differentiating the
     * expression with respect to [variable]. If substring is "simplify" the output is
     * the simplification of the current expression.
     * 
     * @param substring command to be executed
     * @return result of executing command
     */
    private static String handleCommand(String substring) {
        if (currentExpression.equals("")) {
            return "ParseError: no stored current expression";
        }
        if (substring.contains(DERIVATIVE_PREFIX)) {
            String variable = substring.substring(DERIVATIVE_PREFIX.length());
            if (variable.length() == 0) {
                return "ParseError: missing variable in derivative command";
            } else if (!(variable.matches("[a-zA-Z]+"))) {
                return "ParseError: must differentiate with respect to a valid variable";
            } else {
                currentExpression = Expressions.differentiate(currentExpression, variable);
                return currentExpression;
            }
        } else if (substring.equals(SIMPLIFY)) {
            return Expressions.simplify(currentExpression);
        } else {
            return "ParseError: unknown command \"" + substring +"\"" + "\nCurrentExpression: " + currentExpression;
            //modify response to invalid if this is not allowed once clarified by TAs
        }
    }
}
