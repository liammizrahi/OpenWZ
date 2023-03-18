package WZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Interpreter implements ASTNode.Visitor<Object> {
    private final Map<String, Object> environment = new HashMap<>();

    public void interpret(ASTNode node) {
        try {
            node.accept(this);
        } catch (RuntimeException e) {
            // Handle runtime errors here.
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeException(operator.getLexeme() + " must be followed by a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeException(operator.getLexeme() + " must have number operands.");
    }

    @Override
    public Object visitBinaryExpr(ASTNode.Binary expr) {
        Object left = expr.left.accept(this);
        Object right = expr.right.accept(this);

        switch (expr.operator.getType()) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String || right instanceof String) {
                    return left.toString() + right.toString();
                }
                throw new RuntimeException("Operands must be two numbers or two strings.");
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double) right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double) left * (double) right;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double) left / (double) right;
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case EQUAL_EQUAL:
                return left.equals(right);
            case BANG_EQUAL:
                return !left.equals(right);
        }

        // Unreachable
        return null;
    }

    @Override
    public Object visitLiteralExpr(ASTNode.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(ASTNode.Unary expr) {
        Object right = expr.right.accept(this);

        switch (expr.operator.getType()) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double) right;
            case BANG:
                return !isTruthy(right);
        }

        // Unreachable
        return null;
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    @Override
    public Object visitVariableExpr(ASTNode.Variable expr) {
        return environment.get(expr.name.getLexeme());
    }

    @Override
    public Object visitAssignExpr(ASTNode.Assign expr) {
        Object value = expr.value.accept(this);
        environment.put(expr.name.getLexeme        (), value);
        return value;
    }

    @Override
    public Object visitPrintStmt(ASTNode.Print stmt) {
        Object value = stmt.expression.accept(this);
        System.out.println(value);
        return null;
    }

    @Override
    public Object visitExpressionStmt(ASTNode.Expression stmt) {
        stmt.expression.accept(this);
        return null;
    }

    @Override
    public Object visitVarStmt(ASTNode.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = stmt.initializer.accept(this);
        }
        environment.put(stmt.name.getLexeme(), value);
        return null;
    }

    @Override
    public Object visitBlockStmt(ASTNode.Block stmt) {
        for (ASTNode statement : stmt.statements) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Object visitIfStmt(ASTNode.If stmt) {
        if (isTruthy(stmt.condition.accept(this))) {
            stmt.thenBranch.accept(this);
        } else if (stmt.elseBranch != null) {
            stmt.elseBranch.accept(this);
        }
        return null;
    }

    @Override
    public Object visitArrayLiteralExpr(WZ.ArrayLiteral expr) {
        List<Object> values = new ArrayList<>();
        for (ASTNode element : expr.elements) {
            //values.add(evaluate(element));
        }
        return new ArrayList<>(values);
    }
}
