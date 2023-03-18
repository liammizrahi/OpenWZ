package lexer;

public class Token {
    private final TokenType type;
    private final Object value;

    public Token(TokenType type) {
        this.type = type;
        this.value = null;
    }

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value != null) {
            return String.format("(%s, %s)", type, value);
        } else {
            return String.format("(%s)", type);
        }
    }
}
