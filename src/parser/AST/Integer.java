package parser.AST;

import interpreter.Environment;

public class Integer implements Expr {
    private final int value;

    public Integer(int value) {
        this.value = value;
    }

    @Override
    public Object execute(Environment env) {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}