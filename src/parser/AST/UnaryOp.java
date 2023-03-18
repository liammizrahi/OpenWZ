package parser.AST;

import interpreter.Environment;
import lexer.TokenType;

public class UnaryOp extends AST {
    private final TokenType op;
    private final AST expr;

    public UnaryOp(TokenType op, AST expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public Object execute(Environment env) {
        Object value = expr.execute(env);
        switch (op) {
            case MINUS:
                if (value instanceof Integer) {
                    return -(int) value;
                } else {
                    throw new RuntimeException("Cannot negate non-integer value");
                }
            case NOT:
                if (value instanceof Boolean) {
                    return !(boolean) value;
                } else {
                    throw new RuntimeException("Cannot negate non-boolean value");
                }
            default:
                throw new RuntimeException("Invalid unary operator: " + op);
        }
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", op, expr);
    }
}
