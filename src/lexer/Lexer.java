package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        position = 0;
        currentChar = input.charAt(position);
    }

    private void advance() {
        position++;
        if (position < input.length()) {
            currentChar = input.charAt(position);
        } else {
            currentChar = '\0';
        }
    }

    private void skipWhiteSpace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private int integer() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return Integer.parseInt(result.toString());
    }

    private String string() {
        StringBuilder result = new StringBuilder();
        advance();
        while (currentChar != '\0' && currentChar != '"') {
            result.append(currentChar);
            advance();
        }
        advance();
        return result.toString();
    }

    private Token identifier() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        String identifier = result.toString();
        return new Token(TokenType.IDENTIFIER, identifier);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                skipWhiteSpace();
            } else if (Character.isDigit(currentChar)) {
                int value = integer();
                tokens.add(new Token(TokenType.INTEGER, value));
            } else if (currentChar == '+') {
                tokens.add(new Token(TokenType.PLUS));
                advance();
            } else if (currentChar == '-') {
                tokens.add(new Token(TokenType.MINUS));
                advance();
            } else if (currentChar == '*') {
                tokens.add(new Token(TokenType.MULT));
                advance();
            } else if (currentChar == '/') {
                tokens.add(new Token(TokenType.DIV));
                advance();
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.LPAREN));
                advance();
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RPAREN));
                advance();
            } else if (currentChar == '=') {
                if (input.charAt(position + 1) == '=') {
                    tokens.add(new Token(TokenType.EQ));
                    advance();
                    advance();
                } else {
                    tokens.add(new Token(TokenType.ASSIGN));
                    advance();
                }
            } else if (currentChar == '!') {
                if (input.charAt(position + 1) == '=') {
                    tokens.add(new Token(TokenType.NEQ));
                    advance();
                    advance();
                } else {
                    throw new RuntimeException("Invalid syntax: expected !=, but found !");
                }
            } else if (currentChar == '<') {
                if (input.charAt(position + 1) == '=') {
                    tokens.add(new Token(TokenType.LTE));
                    advance();
                    advance();
                } else {
                    tokens.add(new Token(TokenType.LT));
                    advance();
                }
            } else if (currentChar == '>') {
                if (input.charAt(position + 1) == '=') {
                    tokens.add(new Token(TokenType.GTE));
                    advance();
                    advance();
                } else {
                    tokens.add(new Token(TokenType.GT));
                    advance();
                }
            } else if (currentChar == '&') {
                if (input.charAt(position + 1) == '&') {
                    tokens.add(new Token(TokenType.AND));
                    advance();
                    advance();
                } else {
                    throw new RuntimeException("Invalid syntax: expected &&, but found &");
                }
            } else if (currentChar == '|') {
                if (input.charAt(position + 1) == '|') {
                    tokens.add(new Token(TokenType.OR));
                    advance();
                    advance();
                } else {
                    throw new RuntimeException("Invalid syntax: expected ||, but found |");
                }
            } else if (currentChar == '!') {
                if (input.charAt(position + 1) == '=') {
                    tokens.add(new Token(TokenType.NEQ));
                    advance();
                    advance();
                } else {
                    throw new RuntimeException("Invalid syntax: expected !=, but found !");
                }
            } else if (currentChar == '=') {
                if (input.charAt(position + 1) == '=') {
                    tokens.add(new Token(TokenType.EQ));
                    advance();
                    advance();
                } else {
                    tokens.add(new Token(TokenType.ASSIGN));
                    advance();
                }
            } else {
                throw new RuntimeException("Invalid syntax: " + currentChar);
            }
        }
        tokens.add(new Token(TokenType.EOF));
        return tokens;
    }
}
