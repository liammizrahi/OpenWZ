package WZ;

import java.util.ArrayList;
import java.util.List;

import static WZ.TokenType.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<ASTNode> parse() {
        List<ASTNode> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private ASTNode declaration() {
        try {
            if (match(LET)) return varDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private ASTNode varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");

        ASTNode initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new ASTNode.Var(name, initializer);
    }

    private ASTNode statement() {
        if (match(PRINT)) return printStatement();
        if (match(LEFT_BRACE)) return new ASTNode.Block(block());
        if (match(IF)) return ifStatement();
        return expressionStatement();
    }

    private ASTNode printStatement() {
        ASTNode value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new ASTNode.Print(value);
    }

    private ASTNode ifStatement() {
        consume(LEFT_PAREN, "Expect '(' after 'if'.");
        ASTNode condition = expression();
        consume(RIGHT_PAREN, "Expect ')' after if condition.");

        ASTNode thenBranch = statement();
        ASTNode elseBranch = null;
        if (match(ELSE)) {
            elseBranch = statement();
        }

        return new ASTNode.If(condition, thenBranch, elseBranch);
    }

    private ASTNode expressionStatement() {
        ASTNode expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new ASTNode.Expression(expr);
    }

    private List<ASTNode> block() {
        List<ASTNode> statements = new ArrayList<>();

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    private ASTNode expression() {
        return assignment();
    }

    private ASTNode assignment() {
        ASTNode expr = equality();

        if (match(EQUAL)) {
            Token equals = previous();
            ASTNode value = assignment();

            if (expr instanceof ASTNode.Variable) {
                Token name = ((ASTNode.Variable) expr).name;
                return new ASTNode.Assign(name, value);
            }

            error(equals, "Invalid assignment target.");
        }

        return expr;
    }

    private ASTNode equality() {
        ASTNode expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            ASTNode right = comparison();
            expr = new ASTNode.Binary(expr, operator, right);
        }

        if (match(LEFT_BRACKET)) {
            List<ASTNode> elements = new ArrayList<>();
            if (!check(RIGHT_BRACKET)) {
                do {
                    elements.add(expression());
                } while (match(COMMA));
            }
            consume(RIGHT_BRACKET, "Expect ']' after array literal.");
            return new ArrayLiteral(elements);
        }

        return expr;
    }

    private ASTNode comparison() {
        ASTNode expr = addition();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            ASTNode right = addition();
            expr = new ASTNode.Binary(expr, operator, right);
        }

        return expr;
    }

    private ASTNode addition() {
        ASTNode expr = multiplication();

        while (match        (MINUS, PLUS)) {
            Token operator = previous();
            ASTNode right = multiplication();
            expr = new ASTNode.Binary(expr, operator, right);
        }

        return expr;
    }

    private ASTNode multiplication() {
        ASTNode expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            ASTNode right = unary();
            expr = new ASTNode.Binary(expr, operator, right);
        }

        return expr;
    }

    private ASTNode array() {
        List<ASTNode> elements = new ArrayList<>();
        if (!check(RIGHT_BRACKET)) {
            do {
                elements.add(expression());
            } while (match(COMMA));
        }
        consume(RIGHT_BRACKET, "Expect ']' after array literal.");
        return new ArrayLiteral(elements);
    }

    private ASTNode unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            ASTNode right = unary();
            return new ASTNode.Unary(operator, right);
        }

        return primary();
    }

    private ASTNode primary() {
        if (match(FALSE)) return new ASTNode.Literal(false);
        if (match(TRUE)) return new ASTNode.Literal(true);
        if (match(NULL)) return new ASTNode.Literal(null);

        if (match(NUMBER, STRING)) {
            return new ASTNode.Literal(previous().getLiteral());
        }

        if (match(IDENTIFIER)) {
            return new ASTNode.Variable(previous());
        }

        if (match(LEFT_PAREN)) {
            ASTNode expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return expr;
        }

        if (match(LEFT_BRACKET)) {
            List<ASTNode> elements = new ArrayList<>();
            if (!check(RIGHT_BRACKET)) {
                do {
                    elements.add(expression());
                } while (match(COMMA));
            }
            consume(RIGHT_BRACKET, "Expect ']' after array literal.");
            return new ArrayLiteral(elements);
        }

        throw error(peek(), "Expect expression.");
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        System.err.println("Error at line " + token.getLine() + ": " + message);
        return new ParseError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().getType() == SEMICOLON) return;

            switch (peek().getType()) {
                case LET:
                case PRINT:
                case IF:
                case LEFT_BRACE:
                    return;
            }

            advance();
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private static class ParseError extends RuntimeException {}
}
