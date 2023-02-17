import Grammar.*;
import FiniteAutomaton.*;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "B", "D"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "c"));

        List<ProductionRule> productionRules = new ArrayList<>();
        productionRules.add(new ProductionRule("S", Arrays.asList("a", "B")));
        productionRules.add(new ProductionRule("B", Arrays.asList("a", "D")));
        productionRules.add(new ProductionRule("B", Arrays.asList("b", "B")));
        productionRules.add(new ProductionRule("D", Arrays.asList("a", "D")));
        productionRules.add(new ProductionRule("D", Arrays.asList("b", "S")));
        productionRules.add(new ProductionRule("B", Arrays.asList("c", "S")));
        productionRules.add(new ProductionRule("D", List.of("c")));

        Grammar grammar = new Grammar(nonTerminals, terminals, productionRules);

        System.out.println("5 valid strings:");
        List<String> validStrings = grammar.generateStrings(5);
        for (String validString : validStrings) {
            System.out.println(validString);
        }

        System.out.println();

        FiniteAutomaton fa = grammar.convertToFiniteAutomaton();
        System.out.println(fa);

        String input = "acabcacacabcacaaacb";

        if (fa.accepts(input)) {
            System.out.println("The input string " + input + " can be obtained via the state transition from the finite automaton.");
        } else {
            System.out.println("The input string " + input + " cannot be obtained via the state transition from the finite automaton.");
        }
    }
}

