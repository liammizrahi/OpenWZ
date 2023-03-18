package parser.AST;

import interpreter.Environment;
import interpreter.Value;

import java.util.ArrayList;
import java.util.List;

public class Compound extends AST {
    private final List<AST> children;

    public Compound() {
        this.children = new ArrayList<>();
    }

    public void addChild(AST node) {
        children.add(node);
    }

    @Override
    public Value execute(Environment env) {
        for (AST child : children) {
            child.execute(env);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (AST child : children) {
            sb.append(child.toString());
        }
        return sb.toString();
    }
}
