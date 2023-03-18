package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> variables;

    public Environment() {
        this.variables = new HashMap<>();
    }

    public Object getVariable(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return variables.get(name);
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }
}
