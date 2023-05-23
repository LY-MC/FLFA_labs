package AST;

public class IfStatementNode implements ASTNode {
    private final ASTNode condition;
    private final ASTNode ifBody;
    private final ASTNode elseBody;

    public IfStatementNode(ASTNode condition, ASTNode ifBody, ASTNode elseBody) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getIfBody() {
        return ifBody;
    }

    public ASTNode getElseBody() {
        return elseBody;
    }

    @Override
    public String toString() {
        return "IfStatementNode{\n" +
                "  condition=" + condition.toString().replaceAll("\n", "\n  ") + ",\n" +
                "  ifBody=" + ifBody.toString().replaceAll("\n", "\n  ") + ",\n" +
                "  elseBody=" + elseBody.toString().replaceAll("\n", "\n  ") +
                "\n}";
    }
}