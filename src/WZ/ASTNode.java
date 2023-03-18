package WZ;

import java.util.List;

public abstract class ASTNode {

    public abstract <R> R accept(Visitor<R> visitor);

    public interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitLiteralExpr(Literal expr);
        R visitUnaryExpr(Unary expr);
        R visitVariableExpr(Variable expr);
        R visitAssignExpr(Assign expr);
        R visitPrintStmt(Print stmt);
        R visitExpressionStmt(Expression stmt);
        R visitVarStmt(Var stmt);
        R visitBlockStmt(Block stmt);
        R visitIfStmt(If stmt);
        R visitArrayLiteralExpr(WZ.ArrayLiteral expr);
    }

    public static class Binary extends ASTNode {
        public final ASTNode left;
        public final Token operator;
        public final ASTNode right;

        public Binary(ASTNode left, Token operator, ASTNode right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Literal extends ASTNode {
        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    public static class Unary extends ASTNode {
        public final Token operator;
        public final ASTNode right;

        public Unary(Token operator, ASTNode right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public static class Variable extends ASTNode {
        public final Token name;

        public Variable(Token name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }

    public static class Assign extends ASTNode {
        public final Token name;
        public final ASTNode value;

        public Assign(Token name, ASTNode value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }

    public static class Print extends ASTNode {
        public final ASTNode expression;

        public Print(ASTNode expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    public static class Expression extends ASTNode {
        public final ASTNode expression;

        public Expression(ASTNode expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    public static class Var extends ASTNode {
        public final Token name;
        public final ASTNode initializer;

        public Var(Token name, ASTNode initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    public static class Block extends ASTNode {
        public final List<ASTNode> statements;

        public Block(List<ASTNode> statements) {
            this.statements = statements;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

    public static class If extends ASTNode {
        public final ASTNode condition;
        public final ASTNode thenBranch;
        public final ASTNode elseBranch;

        public If(ASTNode condition, ASTNode thenBranch, ASTNode elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }
    }
}