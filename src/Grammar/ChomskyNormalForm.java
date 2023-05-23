package Grammar;

import java.util.*;
import java.util.stream.Collectors;

public class ChomskyNormalForm {


    public static Grammar eliminateEpsilonProductions(Grammar grammar) {
        Set<String> nullableNonTerminals = new HashSet<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (ProductionRule productionRule : grammar.getProductionRules()) {
                if (productionRule.getRightHandSide().isEmpty()) {
                    nullableNonTerminals.add(productionRule.getLeftHandSide());
                    changed = nullableNonTerminals.add(productionRule.getLeftHandSide()) || changed;
                } else {
                    boolean allNullable = true;
                    for (String symbol : productionRule.getRightHandSide()) {
                        if (!nullableNonTerminals.contains(symbol)) {
                            allNullable = false;
                            break;
                        }
                    }
                    if (allNullable) {
                        nullableNonTerminals.add(productionRule.getLeftHandSide());
                        changed = nullableNonTerminals.add(productionRule.getLeftHandSide()) || changed;
                    }
                }
            }
        }

        List<ProductionRule> newProductionRules = new ArrayList<>();
        for (ProductionRule productionRule : grammar.getProductionRules()) {
            List<String> rightHandSide = productionRule.getRightHandSide();
            if (rightHandSide.isEmpty()) {
                continue;
            }
            List<List<String>> expandedRightHandSides = new ArrayList<>();
            if (nullableNonTerminals.containsAll(rightHandSide)) {
                expandedRightHandSides.add(new ArrayList<>());
            } else {
                expandedRightHandSides.add(new ArrayList<>(rightHandSide));
                for (String symbol : rightHandSide) {
                    int size = expandedRightHandSides.size();
                    for (int i = 0; i < size; i++) {
                        List<String> currentRightHandSide = expandedRightHandSides.get(i);
                        if (nullableNonTerminals.contains(symbol)) {
                            List<String> withSymbol = new ArrayList<>(currentRightHandSide);
                            withSymbol.add(symbol);
                            expandedRightHandSides.add(withSymbol);
                            expandedRightHandSides.add(new ArrayList<>(currentRightHandSide));
                        } else {
                            currentRightHandSide.add(symbol);
                        }
                    }
                }
            }
            for (List<String> newRightHandSide : expandedRightHandSides) {
                if (!newRightHandSide.isEmpty()) {
                    newProductionRules.add(new ProductionRule(productionRule.getLeftHandSide(), newRightHandSide));
                }
            }
        }
        // Construct and return the new grammar
        Set<String> nonTerminals = grammar.getNonTerminals();
        Set<String> terminals = grammar.getTerminals();
        return new Grammar(nonTerminals, terminals, newProductionRules);
    }


        public static Grammar eliminateRenaming(Grammar grammar) {
        Set<String> nonTerminals = new HashSet<>(grammar.getNonTerminals());
        Set<String> terminals = new HashSet<>(grammar.getTerminals());
        List<ProductionRule> productionRules = new ArrayList<>(grammar.getProductionRules());

        boolean changed = true;
        while (changed) {
            changed = false;
            for (ProductionRule rule : grammar.getProductionRules()) {
                if (rule.getRightHandSide().size() == 1 && nonTerminals.contains(rule.getRightHandSide().get(0))) {
                    nonTerminals.add(rule.getRightHandSide().get(0));
                    productionRules.remove(rule);
                    for (ProductionRule newRule : grammar.getProductionRules()) {
                        if (newRule.getLeftHandSide().equals(rule.getLeftHandSide())) {
                            List<String> newRightHandSide = new ArrayList<>(newRule.getRightHandSide());
                            newRightHandSide.set(newRightHandSide.indexOf(rule.getLeftHandSide()), rule.getRightHandSide().get(0));
                            productionRules.add(new ProductionRule(newRule.getLeftHandSide(), newRightHandSide));
                            changed = true;
                        }
                    }
                }
            }
        }

        return new Grammar(nonTerminals, terminals, productionRules);
    }

    public static Grammar eliminateInaccessibleSymbols(Grammar grammar) {
        Set<String> newNonTerminals = new HashSet<>();
        Set<String> newTerminals = new HashSet<>();
        List<ProductionRule> newProductionRules = new ArrayList<>();

        Set<String> accessibleSymbols = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        String startSymbol = grammar.getNonTerminals().iterator().next();

        queue.offer(startSymbol);
        visited.add(startSymbol);
        while (!queue.isEmpty()) {
            String symbol = queue.poll();
            accessibleSymbols.add(symbol);
            for (ProductionRule rule : grammar.getProductionRules()) {
                if (rule.getLeftHandSide().equals(symbol)) {
                    for (String rhsSymbol : rule.getRightHandSide()) {
                        if (!visited.contains(rhsSymbol)) {
                            queue.offer(rhsSymbol);
                            visited.add(rhsSymbol);
                        }
                    }
                }
            }
        }

        for (ProductionRule rule : grammar.getProductionRules()) {
            String lhs = rule.getLeftHandSide();
            List<String> rhs = rule.getRightHandSide();
            if (accessibleSymbols.contains(lhs)) {
                List<String> newRhs = new ArrayList<>();
                for (String symbol : rhs) {
                    if (accessibleSymbols.contains(symbol)) {
                        newRhs.add(symbol);
                        if (!grammar.getNonTerminals().contains(symbol)) {
                            newTerminals.add(symbol);
                        }
                    }
                }
                newProductionRules.add(new ProductionRule(lhs, newRhs));
                newNonTerminals.add(lhs);
            }
        }

        return new Grammar(newNonTerminals, newTerminals, newProductionRules);
    }


    public static Grammar eliminateNonProductiveSymbols(Grammar grammar) {
        Set<String> productiveNonTerminals = new HashSet<>();
        Set<String> newNonTerminals = new HashSet<>();
        Set<String> newTerminals = new HashSet<>();
        List<ProductionRule> newProductionRules = new ArrayList<>();

        Set<String> nonTerminals = grammar.getNonTerminals();
        for (ProductionRule rule : grammar.getProductionRules()) {
            boolean isProductive = true;
            for (String symbol : rule.getRightHandSide()) {
                if (!productiveNonTerminals.contains(symbol) && nonTerminals.contains(symbol)) {
                    isProductive = false;
                    break;
                }
            }
            if (isProductive) {
                productiveNonTerminals.add(rule.getLeftHandSide());
            }
        }

        for (ProductionRule rule : grammar.getProductionRules()) {
            if (productiveNonTerminals.contains(rule.getLeftHandSide())) {
                List<String> newRightHandSide = new ArrayList<>();
                boolean isProductive = true;
                for (String symbol : rule.getRightHandSide()) {
                    if (productiveNonTerminals.contains(symbol) || !nonTerminals.contains(symbol)) {
                        newRightHandSide.add(symbol);
                    } else {
                        isProductive = false;
                    }
                }
                if (isProductive) {
                    newProductionRules.add(new ProductionRule(rule.getLeftHandSide(), newRightHandSide));
                    newNonTerminals.add(rule.getLeftHandSide());
                    newTerminals.addAll(newRightHandSide.stream().filter(grammar.getTerminals()::contains).collect(Collectors.toList()));
                }
            }
        }

        return new Grammar(newNonTerminals, newTerminals, newProductionRules);
    }




    public static Grammar convertToChomskyNormalForm(Grammar grammar) {
        grammar = eliminateEpsilonProductions(grammar);
        grammar = eliminateRenaming(grammar);
        grammar = eliminateInaccessibleSymbols(grammar);

        Set<String> nonTerminals = grammar.getNonTerminals();
        Set<String> terminals = grammar.getTerminals();
        List<ProductionRule> productionRules = new ArrayList<>();

        for (ProductionRule productionRule : grammar.getProductionRules()) {
            List<String> rightHandSide = productionRule.getRightHandSide();
            if (rightHandSide.size() == 1) {
                productionRules.add(productionRule);
            } else if (rightHandSide.size() == 2) {
                productionRules.add(productionRule);
            } else {
                String leftHandSide = productionRule.getLeftHandSide();
                String newVariable = "V" + (nonTerminals.size() + 1);
                nonTerminals.add(newVariable);
                productionRules.add(new ProductionRule(leftHandSide, Arrays.asList(rightHandSide.get(0), newVariable)));
                for (int i = 1; i < rightHandSide.size() - 2; i++) {
                    String intermediateVariable = "V" + (nonTerminals.size() + 1);
                    nonTerminals.add(intermediateVariable);
                    productionRules.add(new ProductionRule(newVariable, Arrays.asList(rightHandSide.get(i), intermediateVariable)));
                    newVariable = intermediateVariable;
                }
                productionRules.add(new ProductionRule(newVariable, Arrays.asList(rightHandSide.get(rightHandSide.size() - 2), rightHandSide.get(rightHandSide.size() - 1))));
            }
        }

        for (String terminal : terminals) {
            String newVariable = "V" + (nonTerminals.size() + 1);
            nonTerminals.add(newVariable);
            productionRules.add(new ProductionRule(newVariable, Arrays.asList(terminal)));
        }

        return new Grammar(nonTerminals, terminals, productionRules);
    }
}
