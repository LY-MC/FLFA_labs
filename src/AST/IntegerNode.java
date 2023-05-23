package AST;

public class IntegerNode implements ASTNode {
    private final int value;

    public IntegerNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerNode{value=" + value + "}";
    }
}