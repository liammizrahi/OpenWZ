package parser.AST;

import interpreter.Environment;

public class Assignment extends AST {
    private final String varName;
    private final AST expr;

    public Assignment(String varName, AST expr) {
        this.varName = varName;
        this.expr = expr;
    }

    @Override
    public Object execute(Environment env) {
        Object value = expr.execute(env);
        env.setVariable(varName, value);
        return value;
    }

    @Override
    public String toString() {
        return String.format("let %s = %s;", varName, expr);
    }
}
