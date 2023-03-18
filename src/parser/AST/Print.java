package parser.AST;

import interpreter.Environment;
import interpreter.Value;

public class Print extends AST {
    private final AST expr;

    public Print(AST expr) {
        this.expr = expr;
    }

    @Override
    public Value execute(Environment env) {
        System.out.println(expr.execute(env));
        return null;
    }

    @Override
    public String toString() {
        return String.format("print %s;", expr);
    }
}
