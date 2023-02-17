package Grammar;

import FiniteAutomaton.FiniteAutomaton;
import java.util.*;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private List<ProductionRule> productionRules;

    public Grammar(Set<String> nonTerminals, Set<String> terminals, List<ProductionRule> productionRules) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productionRules = productionRules;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

    public List<String> generateStrings(int count) {
        List<String> strings = new ArrayList<>();
        Random random = new Random();
        while (strings.size() < count) {
            StringBuilder stringBuilder = new StringBuilder();
            String startSymbol = productionRules.get(0).getLeftHandSide();
            generateString(startSymbol, stringBuilder, random);
            if (stringBuilder.length() > 0) {
                strings.add(stringBuilder.toString());
            }
        }
        return strings;
    }

    private void generateString(String symbol, StringBuilder sb, Random random) {
        if (terminals.contains(symbol)) {
            sb.append(symbol);
            return;
        }
        List<ProductionRule> rules = new ArrayList<>();
        for (ProductionRule rule : productionRules) {
            if (rule.getLeftHandSide().equals(symbol)) {
                rules.add(rule);
            }
        }
        int index = random.nextInt(rules.size());
        ProductionRule chosenRule = rules.get(index);
        List<String> rightHandSide = chosenRule.getRightHandSide();
        for (String rhsSymbol : rightHandSide) {
            generateString(rhsSymbol, sb, random);
        }
    }

    public FiniteAutomaton convertToFiniteAutomaton() {
        Map<String, Map<String, Set<String>>> transitionFunction = new HashMap<>();
        Set<String> states = new HashSet<>();
        Set<String> symbols = new HashSet<>();
        Set<String> finalStates = new HashSet<>();

        String initialState = productionRules.get(0).getLeftHandSide();
        states.add(initialState);

        for (ProductionRule rule : productionRules) {
            String leftHandSide = rule.getLeftHandSide();
            states.add(leftHandSide);
            List<String> rightHandSide = rule.getRightHandSide();
            for (int i = 0; i < rightHandSide.size(); i++) {
                String symbol = rightHandSide.get(i);
                if (terminals.contains(symbol)) {
                    symbols.add(symbol);
                    Set<String> nextStates = transitionFunction.getOrDefault(leftHandSide, new HashMap<>()).getOrDefault(symbol, new HashSet<>());
                    if (i < rightHandSide.size() - 1) {
                        String nextState = rightHandSide.get(i + 1);
                        nextStates.add(nextState);
                        states.add(nextState);
                    } else {
                        String nextState = rightHandSide.get(i);
                        nextStates.add(nextState);
                        finalStates.add(symbol);
                    }
                    transitionFunction.computeIfAbsent(leftHandSide, k -> new HashMap<>()).put(symbol, nextStates);
                }
            }
        }

        return new FiniteAutomaton(states, symbols, transitionFunction, finalStates, initialState);
    }
}