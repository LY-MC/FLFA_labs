//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package FiniteAutomaton;

import java.util.*;
import Grammar.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<String> symbols;
    private Map<String, Map<String, Set<String>>> transitionFunction;
    private Set<String> finalStates;
    private String initialState;

    public FiniteAutomaton(Set<String> states, Set<String> symbols, Map<String, Map<String, Set<String>>> transitionFunction, Set<String> finalStates, String initialState) {
        this.states = states;
        this.symbols = symbols;
        this.transitionFunction = transitionFunction;
        this.finalStates = finalStates;
        this.initialState = initialState;
    }

    public Set<String> getStates() {
        return this.states;
    }

    public Set<String> getSymbols() {
        return this.symbols;
    }

    public Map<String, Map<String, Set<String>>> getTransitionFunction() {
        return this.transitionFunction;
    }

    public Set<String> getFinalStates() {
        return this.finalStates;
    }

    public String getInitialState() {
        return this.initialState;
    }

    public boolean accepts(String input) {
        String currentState = this.initialState;

        for(int i = 0; i < input.length(); ++i) {
            String symbol = input.substring(i, i + 1);
            if (!this.symbols.contains(symbol)) {
                return false;
            }

            Map<String, Set<String>> stateTransitions = (Map)this.transitionFunction.get(currentState);
            if (stateTransitions == null || stateTransitions.get(symbol) == null) {
                return false;
            }

            currentState = (String)((Set)stateTransitions.get(symbol)).iterator().next();
        }

        return this.finalStates.contains(currentState);
    }

    public boolean isDeterministic() {
        for (String state : states) {
            Map<String, Set<String>> transitions = transitionFunction.get(state);
            for (String symbol : symbols) {
                Set<String> nextStates = transitions.get(symbol);
                if (nextStates == null || nextStates.size() != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isFinalState(String state) {
        return finalStates.contains(state);
    }

    public Grammar toGrammar() {
        Set<String> nonTerminals = new HashSet<>(this.states);
        Set<String> terminals = new HashSet<>(this.symbols);
        List<ProductionRule> productionRules = new ArrayList<>();

        for (String state : this.states) {
            Map<String, Set<String>> transitions = this.transitionFunction.get(state);
            if (transitions != null) {
                for (Map.Entry<String, Set<String>> entry : transitions.entrySet()) {
                    String symbol = entry.getKey();
                    Set<String> nextStates = entry.getValue();
                    for (String nextState : nextStates) {
                        if (this.finalStates.contains(nextState)) {
                            String newNonTerminal = getNextNonTerminal(nonTerminals);
                            nonTerminals.add(newNonTerminal);
                            productionRules.add(new ProductionRule(state, Arrays.asList(symbol, newNonTerminal)));
                            productionRules.add(new ProductionRule(newNonTerminal, Arrays.asList("ε")));
                        } else {
                            productionRules.add(new ProductionRule(state, Arrays.asList(symbol, nextState)));
                        }
                    }
                }
            }
        }

        return new Grammar(nonTerminals, terminals, productionRules);
    }

    private String getNextNonTerminal(Set<String> nonTerminals) {
        char nextSymbol = 'A';
        while (nonTerminals.contains(Character.toString(nextSymbol))) {
            nextSymbol++;
        }
        return Character.toString(nextSymbol);
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Initial State: ").append(this.initialState).append("\n");
        builder.append("States: ").append(this.states).append("\n");
        builder.append("Transition Functions:\n");
        Iterator var2 = this.transitionFunction.keySet().iterator();

        while(var2.hasNext()) {
            String state = (String)var2.next();
            Map<String, Set<String>> stateTransitions = (Map)this.transitionFunction.get(state);
            Iterator var5 = stateTransitions.keySet().iterator();

            while(var5.hasNext()) {
                String symbol = (String)var5.next();
                Set<String> nextStates = (Set)stateTransitions.get(symbol);

                for(Iterator var8 = nextStates.iterator(); var8.hasNext(); builder.append("\n")) {
                    String nextState = (String)var8.next();
                    builder.append(state).append("-").append(symbol).append("->").append(nextState);
                    if (this.finalStates.contains(nextState)) {
                        builder.append(" (final)");
                    }
                }
            }
        }

        builder.append("Final States: ").append(this.finalStates).append("\n");
        return builder.toString();
    }
}
