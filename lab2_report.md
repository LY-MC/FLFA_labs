# Topic: Types of Finite Automata
### Course: Formal Languages & Finite Automata
### Author: Maria Lesenco

----

## Theory

In my Variant 13:

```
Q = {q0,q1,q2,q3},
∑ = {a,b},
F = {q3},
δ(q0,a) = q0,
δ(q0,b) = q1,
δ(q1,a) = q1,
δ(q1,a) = q2,
δ(q1,b) = q3,
δ(q2,a) = q2,
δ(q2,b) = q3.
```

## Objectives:

1. Understand what an automaton is and what it can be used for.

2. Continuing the work in the same repository and the same project, the following need to be added:

    a. Provide a function in your grammar type/class that could classify the grammar based on Chomsky hierarchy.

    b. For this you can use the variant from the previous lab

3. According to your variant number (by universal convention it is register ID), get the finite automaton definition and do the following tasks:

    a. Implement conversion of a finite automaton to a regular grammar.

    b. Determine whether your FA is deterministic or non-deterministic.

    c. Implement some functionality that would convert an NFA to a DFA.
    
    d. Represent the finite automaton graphically (Optional, and can be considered as a bonus point):


## Implementation description

### Grammar type

This Java code defines a method called classifyGrammar() that attempts to classify the type of a grammar based on its production rules. The method examines each production rule's right-hand side symbols and determines if the grammar is regular, context-free, context-sensitive, or unrestricted. If the grammar cannot be classified, the method returns "Unknown Grammar".

```
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
}

```

### String generator

The method generateStrings takes a count of how many strings to generate and repeatedly calls the generateString method to generate each string.

The generateString method recursively generates a string by randomly choosing a production rule for the given symbol and then replacing the symbol with the right-hand side of the chosen rule. If the symbol is a terminal, it is simply appended to the StringBuilder.

```
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
```

### Finite automata to Grammar

This code defines a method called "toGrammar" which takes no arguments and returns a Grammar object. The method constructs a regular grammar from a finite automaton by iterating over its states and transitions. For each transition, if the next state is a final state, a new non-terminal symbol is added to handle epsilon transitions. The method uses a helper method called "getNextNonTerminal" to get the next available non-terminal symbol. Finally, the method returns a Grammar object containing the non-terminals, terminals, and production rules.

```
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
```

### Finite automata type

This code checks whether a given finite state machine is deterministic or not. It iterates over each state and each symbol in the machine, and checks whether the transition function has exactly one next state for each symbol. If any state-symbol combination does not satisfy this condition, the machine is considered non-deterministic and the method returns false. If all state-symbol combinations satisfy this condition, the machine is considered deterministic and the method returns true.

```
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
```

### NFA to DFA

This code defines a static method convertToDFA in the class NDFAToDFAConverter that takes an instance of the FiniteAutomaton class as input and returns a new deterministic finite automaton (DFA) that is equivalent to the input non-deterministic finite automaton (NFA). The method uses the subset construction algorithm to convert the NFA to a DFA. The resulting DFA has the same set of symbols and final states as the input NFA, but its set of states and transition function are different.


```
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

```

## Output

```
Grammar type: Regular Grammar

Non-terminals: [q1, q2, A, q3, q0]
Terminals: [a, b]
Production rules:
	ProductionRule(q1, [a, q1])
	ProductionRule(q1, [b, q2])
	ProductionRule(q2, [a, q2])
	ProductionRule(q2, [b, A])
	ProductionRule(A, [ε])
	ProductionRule(q0, [a, q0])
	ProductionRule(q0, [b, q1])

this Finite Automaton is non-deterministic

Initial State: q0
States: [q1, q2, q3, q0]
Transition Functions:
q1-a->q1
q1-b->q2
q2-a->q2
q2-b->q3 (final)
q0-a->q0
q0-b->q1
Final States: [q3]
```
