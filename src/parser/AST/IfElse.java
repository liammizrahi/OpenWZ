package parser.AST;

import interpreter.Environment;
import interpreter.Value;

public class IfElse extends AST {
    private final AST condition;
    private final AST ifBody;
    private final AST elseBody;

    public IfElse(AST condition, AST ifBody, AST elseBody) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }

    @Override
    public Value execute(Environment env) {
        Value conditionValue = condition.execute(env);
        if (conditionValue.asBoolean()) {
            return ifBody.execute(env);
        } else {
            return elseBody.execute(env);
        }
    }

    @Override
    public String toString() {
        return String.format("if %s %s else %s", condition, ifBody, elseBody);
    }
}
