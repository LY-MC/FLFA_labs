package AST;

public class BinaryExpressionNode implements ASTNode {
    private final ASTNode left;
    private final ASTNode right;
    private final String operator;

    public BinaryExpressionNode(ASTNode left, ASTNode right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "BinaryExpressionNode{\n" +
                "    left=" + left + ",\n" +
                "    right=" + right + ",\n" +
                "    operator='" + operator + "'" +
                "\n  }";
    }
}