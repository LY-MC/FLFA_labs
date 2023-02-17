package FiniteAutomaton;
import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<String> symbols;
    private Map<String, Map<String, Set<String>>> transitionFunction;
    private Set<String> finalStates;
    private String initialState;
    public FiniteAutomaton(Set<String> states, Set<String> symbols,
                           Map<String, Map<String, Set<String>>> transitionFunction,
                           Set<String> finalStates, String initialState) {
        this.states = states;
        this.symbols = symbols;
        this.transitionFunction = transitionFunction;
        this.finalStates = finalStates;
        this.initialState = initialState;
    }
    public Set<String> getStates() {
        return states;
    }
    public Set<String> getSymbols() {
        return symbols;
    }
    public Map<String, Map<String, Set<String>>> getTransitionFunction() {
        return transitionFunction;
    }
    public Set<String> getFinalStates() {
        return finalStates;
    }
    public String getInitialState() {
        return initialState;
    }
    public boolean accepts(String input) {
        String currentState = initialState;
        for (int i = 0; i < input.length(); i++) {
            String symbol = input.substring(i, i + 1);
            if (!symbols.contains(symbol)) {
                return false;
            }
            Map<String, Set<String>> stateTransitions = transitionFunction.get(currentState);
            if (stateTransitions == null || stateTransitions.get(symbol) == null) {
                return false;
            } else {
                currentState = stateTransitions.get(symbol).iterator().next();
            }

        }
        return finalStates.contains(currentState);
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Initial State: ").append(initialState).append("\n");
        builder.append("States: ").append(states).append("\n");
        builder.append("Transition Functions:\n");
        for (String state : transitionFunction.keySet()) {
            Map<String, Set<String>> stateTransitions = transitionFunction.get(state);
            for (String symbol : stateTransitions.keySet()) {
                Set<String> nextStates = stateTransitions.get(symbol);
                for (String nextState : nextStates) {
                    builder.append(state).append("-").append(symbol).append("->").append(nextState);
                    if (finalStates.contains(nextState)) {
                        builder.append(" (final)");
                    }
                    builder.append("\n");
                }
            }
        }
        builder.append("Final States: ").append(finalStates).append("\n");
        return builder.toString();
    }
}