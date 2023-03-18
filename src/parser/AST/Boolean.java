package parser.AST;

import interpreter.Environment;

public class Boolean extends AST {
    private final boolean value;

    public Boolean(boolean value) {
        this.value = value;
    }

    @Override
    public Object execute(Environment env) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}