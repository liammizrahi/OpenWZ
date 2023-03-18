package WZ;

import java.util.List;

public class ArrayLiteral extends ASTNode {
    public final List<ASTNode> elements;

    public ArrayLiteral(List<ASTNode> elements) {
        this.elements = elements;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitArrayLiteralExpr(this);
    }
}
