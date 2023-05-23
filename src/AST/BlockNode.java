package AST;

import java.util.List;

public class BlockNode implements ASTNode {
    private final List<ASTNode> statements;

    public BlockNode(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BlockNode{\n");
        for (ASTNode statement : statements) {
            sb.append("  ").append(statement.toString().replaceAll("\n", "\n  ")).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}