package parser.AST;

import interpreter.Environment;

public abstract class AST {
    public abstract Object execute(Environment env);
}
