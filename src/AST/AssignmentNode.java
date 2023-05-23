package AST;

public class AssignmentNode implements ASTNode {
    private final String identifier;
    private final ASTNode expression;

    public AssignmentNode(String identifier, ASTNode expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ASTNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "AssignmentNode{\n" +
                "  identifier='" + identifier + "',\n" +
                "  expression=" + expression +
                "\n}";
    }
}