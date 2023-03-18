package parser;

import lexer.*;
import interpreter.Environment;
import lexer.Token;
import parser.AST.*;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
    }

    private void eat(TokenType type) {
        if (currentToken.getType() == type) {
            currentToken = lexer.getNextToken();
        } else {
            throw new ParserException("Expected token " + type + " but got " + currentToken.getType());
        }
    }

    private AST factor() {
        Token token = currentToken;
        if (token.getType() == TokenType.NUMBER) {
            eat(TokenType.NUMBER);
            return new Integer((int) token.getValue());
        } else if (token.getType() == TokenType.STRING) {
            eat(TokenType.STRING);
            return new StringNode((String) token.getValue());
        } else if (token.getType() == TokenType.BOOLEAN || token.getType() == TokenType.LPAREN) {
            return parseBoolean();
        } else if (token.getType() == TokenType.LBRACKET) {
            return parseArray();
        } else if (token.getType() == TokenType.IDENTIFIER) {
            return parseVar();
        } else if (token.getType() == TokenType.MINUS) {
            eat(TokenType.MINUS);
            return new UnaryOp(TokenType.MINUS, factor());
        } else if (token.getType() == TokenType.PLUS) {
            eat(TokenType.PLUS);
            return factor();
        } else if (token.getType() == TokenType.NOT) {
            eat(TokenType.NOT);
            return new UnaryOp(TokenType.NOT, factor());
        } else if (token.getType() == TokenType.LPAREN) {
            eat(TokenType.LPAREN);
            AST node = expr();
            eat(TokenType.RPAREN);
            return node;
        } else {
            throw new ParserException("Unexpected token: " + token);
        }
    }

    private AST term() {
        AST left = factor();
        while (currentToken.getType() == TokenType.MULT || currentToken.getType() == TokenType.DIV) {
            Token token = currentToken;
            if (token.getType() == TokenType.MULT) {
                eat(TokenType.MULT);
                left = new BinOp(TokenType.MULT, left, factor());
            } else if (token.getType() == TokenType.DIV) {
                eat(TokenType.DIV);
                left = new BinOp(TokenType.DIV, left, factor());
            }
        }
        return left;
    }

    private AST expr() {
        AST left = term();
        while (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
            Token token = currentToken;
            if (token.getType() == TokenType.PLUS) {
                eat(TokenType.PLUS);
                left = new BinOp(TokenType.PLUS, left, term());
            } else if (token.getType() == TokenType.MINUS) {
                eat(TokenType.MINUS);
                left = new BinOp(TokenType.MINUS, left, term());
            }
        }
        return left;
    }

    private AST parseLet() {
        eat(TokenType.LET);
        Token token = currentToken;
        eat(TokenType.IDENTIFIER);
        String varName = (String) token.getValue();
        eat(TokenType.EQUALS);
        AST expr = expr();
        return new Assign(varName, expr);
    }

    private AST parsePrint() {
        eat(TokenType.PRINT);
        AST expr = expr();
        return new Print(expr);
    }

    private AST parseVar() {
        Token token = currentToken;
        eat(TokenType.IDENTIFIER);
        if (currentToken.getType() == TokenType.LBRACKET) {
            eat(TokenType.LBRACKET);
            AST index = expr();
            eat(TokenType.RBRACKET);
            return new ArrayVar((String) token.getValue(), index);
        } else {
            return new Var((String) token.getValue());
        }
    }

    private AST parseBoolean() {
        Token token = currentToken;
        if (token.getType() == TokenType.BOOLEAN) {
            eat(TokenType.BOOLEAN);
            return new BooleanNode((Boolean) token.getValue());
        } else {
            eat(TokenType.LPAREN);
            AST expr = expr();
            eat(TokenType.RPAREN);
            return new BooleanNode(expr.execute(new Environment()));
        }
    }

    private AST parseArray() {
        eat(TokenType.LBRACKET);
        AST firstElem = expr();
        eat(TokenType.COMMA);
        AST secondElem = expr();
        eat(TokenType.RBRACKET);
        return new Array(firstElem, secondElem);
    }

    private AST parseIfElse() {
        eat(TokenType.IF);
        AST condition = expr();
        eat(TokenType.THEN);
        AST ifBody = expr();
        eat(TokenType.ELSE);
        AST elseBody = expr();
        return new IfElse(condition, ifBody, elseBody);
    }

    private AST parse() {
        AST node = null;
        switch (currentToken.getType()) {
            case LET:
                node = parseLet();
                break;
            case PRINT:
                node = parsePrint();
                break;
            case IF:
                node = parseIfElse();
                break;
            default:
                node = expr();
                break;
        }
        return node;
    }

    public AST parseProgram() {
        AST node = parse();
        if (currentToken.getType() != TokenType.EOF) {
            throw new ParserException("Unexpected token: " + currentToken);
        }
        return node;
    }
}