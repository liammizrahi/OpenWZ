package WZ;

public class LexingError extends RuntimeException {
    private final int line;

    public LexingError(int line, String message) {
        super(message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
