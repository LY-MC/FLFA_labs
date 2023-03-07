//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
        return this.nonTerminals;
    }

    public Set<String> getTerminals() {
        return this.terminals;
    }

    public List<ProductionRule> getProductionRules() {
        return this.productionRules;
    }

    public List<String> generateStrings(int count) {
        List<String> strings = new ArrayList();
        Random random = new Random();

        while(strings.size() < count) {
            StringBuilder stringBuilder = new StringBuilder();
            String startSymbol = ((ProductionRule)this.productionRules.get(0)).getLeftHandSide();
            this.generateString(startSymbol, stringBuilder, random);
            if (stringBuilder.length() > 0) {
                strings.add(stringBuilder.toString());
            }
        }

        return strings;
    }

    private void generateString(String symbol, StringBuilder sb, Random random) {
        if (this.terminals.contains(symbol)) {
            sb.append(symbol);
        } else {
            List<ProductionRule> rules = new ArrayList();
            Iterator var5 = this.productionRules.iterator();

            ProductionRule rule;
            while(var5.hasNext()) {
                rule = (ProductionRule)var5.next();
                if (rule.getLeftHandSide().equals(symbol)) {
                    rules.add(rule);
                }
            }

            int index = random.nextInt(rules.size());
            rule = (ProductionRule)rules.get(index);
            List<String> rightHandSide = rule.getRightHandSide();
            Iterator var8 = rightHandSide.iterator();

            while(var8.hasNext()) {
                String rhsSymbol = (String)var8.next();
                this.generateString(rhsSymbol, sb, random);
            }

        }
    }

    public FiniteAutomaton convertToFiniteAutomaton() {
        Map<String, Map<String, Set<String>>> transitionFunction = new HashMap();
        Set<String> states = new HashSet();
        Set<String> symbols = new HashSet();
        Set<String> finalStates = new HashSet();
        String initialState = ((ProductionRule)this.productionRules.get(0)).getLeftHandSide();
        states.add(initialState);
        Iterator var6 = this.productionRules.iterator();

        while(var6.hasNext()) {
            ProductionRule rule = (ProductionRule)var6.next();
            String leftHandSide = rule.getLeftHandSide();
            states.add(leftHandSide);
            List<String> rightHandSide = rule.getRightHandSide();

            for(int i = 0; i < rightHandSide.size(); ++i) {
                String symbol = (String)rightHandSide.get(i);
                if (this.terminals.contains(symbol)) {
                    symbols.add(symbol);
                    Set<String> nextStates = (Set)((Map)transitionFunction.getOrDefault(leftHandSide, new HashMap())).getOrDefault(symbol, new HashSet());
                    String nextState;
                    if (i < rightHandSide.size() - 1) {
                        nextState = (String)rightHandSide.get(i + 1);
                        nextStates.add(nextState);
                        states.add(nextState);
                    } else {
                        nextState = (String)rightHandSide.get(i);
                        nextStates.add(nextState);
                        finalStates.add(symbol);
                    }

                    ((Map)transitionFunction.computeIfAbsent(leftHandSide, (k) -> {
                        return new HashMap();
                    })).put(symbol, nextStates);
                }
            }
        }

        return new FiniteAutomaton(states, symbols, transitionFunction, finalStates, initialState);
    }

    public String classifyGrammar() {
        boolean isRegular = true;
        boolean isContextFree = true;
        boolean isContextSensitive = true;
        boolean isUnrestricted = true;

        for (ProductionRule rule : productionRules) {
            List<String> rhs = rule.getRightHandSide();

            if (rhs.size() > 2) {
                isRegular = false;
                isContextFree = false;
                isContextSensitive = false;
            }

            if (rhs.size() > 1 && !nonTerminals.contains(rhs.get(0))) {
                isContextFree = false;
                isContextSensitive = false;
            }

            if (rhs.size() > 0 && nonTerminals.contains(rhs.get(0))) {
                isRegular = false;

                if (rhs.size() == 1) {
                    isContextFree = false;
                }

                if (rhs.size() > 1 && !nonTerminals.contains(rhs.get(1))) {
                    isContextFree = false;
                    isContextSensitive = false;
                }
            }

            if (rhs.size() == 0) {
                isRegular = false;

                if (!rule.getLeftHandSide().equals("S")) {
                    isContextFree = false;
                    isContextSensitive = false;
                }
            }
        }

        if (isRegular) {
            return "Regular Grammar";
        }

        if (isContextFree) {
            return "Context-Free Grammar";
        }

        if (isContextSensitive) {
            return "Context-Sensitive Grammar";
        }

        if (isUnrestricted) {
            return "Unrestricted Grammar";
        }

        return "Unknown Grammar";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Non-terminals: ").append(this.nonTerminals).append("\n");
        sb.append("Terminals: ").append(this.terminals).append("\n");
        sb.append("Production rules:\n");
        for (ProductionRule rule : this.productionRules) {
            sb.append("\t").append(rule.toString()).append("\n");
        }
        return sb.toString();
    }
}
