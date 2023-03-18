package parser.AST;

import interpreter.Environment;
import lexer.TokenType;

public class BinOp extends AST {
    private final TokenType op;
    private final AST left;
    private final AST right;

    public BinOp(TokenType op, AST left, AST right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public Object execute(Environment env) {
        Object leftVal = left.execute(env);
        Object rightVal = right.execute(env);
        switch (op) {
            case PLUS:
                if (leftVal instanceof String || rightVal instanceof String) {
                    return leftVal.toString() + rightVal.toString();
                } else if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal + (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '+' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case MINUS:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal - (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '-' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case MULT:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal * (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '*' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case DIV:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal / (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '/' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            caseEQ:
                return leftVal.equals(rightVal);
            case NEQ:
                return !leftVal.equals(rightVal);
            case LT:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal < (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '<' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case GT:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal > (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '>' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case LTE:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal <= (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '<=' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case GTE:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (int) leftVal >= (int) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '>=' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case AND:
                if (leftVal instanceof Boolean && rightVal instanceof Boolean) {
                    return (boolean) leftVal && (boolean) rightVal;
                }
            case AND:
                if (leftVal instanceof Boolean && rightVal instanceof Boolean) {
                    return (boolean) leftVal && (boolean) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '&&' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            case OR:
                if (leftVal instanceof Boolean && rightVal instanceof Boolean) {
                    return (boolean) leftVal || (boolean) rightVal;
                } else {
                    throw new RuntimeException(String.format("Invalid types for '||' operator: %s, %s", leftVal.getClass().getSimpleName(), rightVal.getClass().getSimpleName()));
                }
            default:
                throw new RuntimeException(String.format("Unknown operator: %s", op));
        }
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, op, right);
    }
}
