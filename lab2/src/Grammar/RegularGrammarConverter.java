package Grammar;

import FiniteAutomaton.FiniteAutomaton;
import java.util.*;

public class RegularGrammarConverter {
    public static Grammar convert(FiniteAutomaton automaton) {
        Set<String> nonTerminals = new HashSet<>();
        Set<String> terminals = automaton.getSymbols();
        List<ProductionRule> productionRules = new ArrayList<>();

        // Step 1: Add the new start symbol S -> q0
        String startSymbol = "S";
        nonTerminals.add(startSymbol);
        productionRules.add(new ProductionRule(startSymbol, Arrays.asList(automaton.getInitialState())));

        // Step 2: Add the transition rules A -> aB
        for (String state : automaton.getStates()) {
            for (String symbol : terminals) {
                Set<String> nextStates = automaton.getTransitionFunction().getOrDefault(state, new HashMap<>()).getOrDefault(symbol, new HashSet<>());
                for (String nextState : nextStates) {
                    String leftHandSide = state;
                    String rightHandSide = symbol + nextState;
                    if (nextStates.contains(automaton.getInitialState())) {
                        // Add the start symbol to the right-hand side of the rule if the next state is the initial state
                        rightHandSide += startSymbol;
                    }
                    nonTerminals.add(leftHandSide);
                    nonTerminals.add(nextState);
                    productionRules.add(new ProductionRule(leftHandSide, Arrays.asList(rightHandSide)));
                }
            }
        }

        // Step 3: Add the final state rules B -> ε
        for (String finalState : automaton.getFinalStates()) {
            nonTerminals.add(finalState);
            productionRules.add(new ProductionRule(finalState, Arrays.asList("")));
        }

        return new Grammar(nonTerminals, terminals, productionRules);
    }
}