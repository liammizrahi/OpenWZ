package parser.AST;

import interpreter.Environment;

public interface Expr {
    Object execute(Environment env);
}
