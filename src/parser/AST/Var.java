package parser.AST;

import interpreter.Environment;
import interpreter.Value;

public class Var extends Expr {
    private final String name;

    public Var(String name) {
        this.name = name;
    }

    @Override
    public Value execute(Environment env) {
        return env.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
