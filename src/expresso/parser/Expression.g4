/**
 * This file is the grammar file used by ANTLR.
 *
 * In order to compile this file, navigate to this directory
 * (src/expresso/parser) and run the following command:
 * 
 * java -jar ../../../antlr.jar Expression.g4
 */

grammar Expression;

/*
 * This puts "package differentiator.grammar;" at the top of the output Java
 * files. Do not change these lines.
 */
@header {
package expresso.parser;
}

/*
 * This adds code to the generated lexer and parser. Do not change these lines. 
 */
@members {
    // This method makes the lexer or parser stop running if it encounters
    // invalid input and throw a RuntimeException.
    public void reportErrorsAsExceptions() {
        removeErrorListeners();
        addErrorListener(new ExceptionThrowingErrorListener());
    }
    
    private static class ExceptionThrowingErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                Object offendingSymbol, int line, int charPositionInLine,
                String msg, RecognitionException e) {
            throw new RuntimeException(msg);
        }
    }
}

/*
 * Nonterminal rules (a.k.a. parser rules) must be lowercase, e.g. "line" below.
 *
 * Terminal rules (a.k.a. lexical rules) must be UPPERCASE. Terminals can be
 * defined with quoted strings (e.g. '(' below) or regular expressions.
 *
 * You should make sure you have one rule that describes the entire input.
 * This is the "start rule". The start rule should end with the special token
 * EOF so that it describes the entire input. Below, "line" is the start rule.
 *
 * For more information, see
 * http://www.antlr.org/wiki/display/ANTLR4/Parser+Rules#ParserRules-StartRulesandEOF
 */

root                  : expression? EOF;

expression            : rootExpression | multExpression | addExpression; 
rootExpression        : literal | parenExpression;
literal               : VARIABLE | CONSTANT;

parenExpression       : LEFT_PAREN expression RIGHT_PAREN;
multExpression        : rootExpression MULTIPLY (rootExpression | multExpression);
addExpression         : (rootExpression | multExpression) PLUS (rootExpression | multExpression | addExpression);

PLUS                  : '+';
MULTIPLY              : '*';
VARIABLE              : [a-zA-Z]+;
CONSTANT              : ([0-9]*[.][0-9]+ | [0-9]+([.][0-9]*)?);  //counts for doubles
LEFT_PAREN            : '(';
RIGHT_PAREN           : ')';

WHITESPACE            : [ ]+ -> skip;
