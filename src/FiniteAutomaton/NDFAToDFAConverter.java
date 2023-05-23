package FiniteAutomaton;

import java.util.*;
import java.util.stream.Collectors;

public class NDFAToDFAConverter {

    public static FiniteAutomaton convertToDFA(FiniteAutomaton nfa) {
        Set<String> newStates = new HashSet<>();
        Set<String> newSymbols = new HashSet<>(nfa.getSymbols());
        Map<String, Map<String, Set<String>>> newTransitions = new HashMap<>();
        Set<String> newFinalStates = new HashSet<>();
        String newInitialState = nfa.getInitialState();

        Map<String, Set<String>> epsilonClosures = new HashMap<>();
        for (String state : nfa.getStates()) {
            Set<String> closure = new HashSet<>();
            computeEpsilonClosure(nfa, state, closure);
            epsilonClosures.put(state, closure);
        }

        Set<String> initialStateSet = epsilonClosures.get(nfa.getInitialState());
        String initialState = getStateName(initialStateSet);
        newStates.add(initialState);

        if (initialStateSet.stream().anyMatch(nfa::isFinalState)) {
            newFinalStates.add(initialState);
        }

        LinkedList<String> unprocessedStates = new LinkedList<>();
        unprocessedStates.add(initialState);

        while (!unprocessedStates.isEmpty()) {
            String state = unprocessedStates.removeFirst();

            for (String symbol : nfa.getSymbols()) {
                Set<String> nextStates = new HashSet<>();

                for (String nfaState : getStateElements(state)) {
                    Set<String> transitions = nfa.getTransitionFunction().get(nfaState).get(symbol);
                    if (transitions != null) {
                        nextStates.addAll(transitions);
                    }
                }

                Set<String> nextStateSet = new HashSet<>();
                for (String nextState : nextStates) {
                    nextStateSet.addAll(epsilonClosures.get(nextState));
                }

                if (nextStateSet.isEmpty()) {
                    continue;
                }

                String nextStateName = getStateName(nextStateSet);

                if (!newStates.contains(nextStateName)) {
                    newStates.add(nextStateName);

                    if (nextStateSet.stream().anyMatch(nfa::isFinalState)) {
                        newFinalStates.add(nextStateName);
                    }

                    unprocessedStates.add(nextStateName);
                }

                Map<String, Set<String>> stateTransitions = newTransitions.computeIfAbsent(state, k -> new HashMap<>());
                stateTransitions.put(symbol, nextStateSet);
            }
        }

        return new FiniteAutomaton(newStates, newSymbols, newTransitions, newFinalStates, newInitialState);
    }

    private static void computeEpsilonClosure(FiniteAutomaton nfa, String state, Set<String> closure) {
        closure.add(state);
        Set<String> transitions = nfa.getTransitionFunction().get(state).get("");

        if (transitions != null) {
            for (String nextState : transitions) {
                if (!closure.contains(nextState)) {
                    computeEpsilonClosure(nfa, nextState, closure);
                }
            }
        }
    }

    private static String getStateName(Set<String> stateSet) {
        return stateSet.stream().sorted().collect(Collectors.joining(","));
    }

    private static Set<String> getStateElements(String stateName) {
        return new HashSet<>(Arrays.asList(stateName.split(",")));
    }

}
