package AST;

public class IdentifierNode implements ASTNode {
    private final String identifier;

    public IdentifierNode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "IdentifierNode{identifier='" + identifier + "'}";
    }
}