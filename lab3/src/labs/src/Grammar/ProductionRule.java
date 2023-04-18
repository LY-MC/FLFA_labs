
package Grammar;

import java.util.List;

public class ProductionRule {
    private String leftHandSide;
    private List<String> rightHandSide;

    public ProductionRule(String leftHandSide, List<String> rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public String getLeftHandSide() {
        return this.leftHandSide;
    }

    public List<String> getRightHandSide() {
        return this.rightHandSide;
    }

    @Override
    public String toString() {
        return "ProductionRule(" + leftHandSide + ", " + rightHandSide + ")";
    }
}
