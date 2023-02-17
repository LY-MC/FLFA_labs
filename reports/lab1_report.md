# Topic: Introduction to formal languages. Regular grammars. Finite Automata.
### Course: Formal Languages & Finite Automata
### Author: Maria Lesenco

----

## Theory

A formal language is a language that is defined by a set of rules or formal grammar. 
A formal grammar has a form like G = {Vt, Vn, P, S}, where:

* Vt is a set of terminal symbols;
* Vn is a set of non-terminal symbols;
* P is a set of all possible productions;
* S is a starting symbol.

In my Variant 13:

```
VN={S, B, D}, 
VT={a, b, c}, 
P={ 
    S → aB
    B → aD
    B → bB
    D → aD
    D → bS
    B → cS
    D → c
}
```

A finite automaton is a mathematical model of a computing machine that has a finite number of states and transitions between those states based on input symbols. 

## Objectives:

1. Understand what a language is and what it needs to have in order to be considered a formal one.

2. Provide the initial setup for the evolving project that I will work on during this semester.

    a. Create a local && remote repository of a VCS hosting service;

    b. Choose a programming language;

    c. Create a separate folder where you will be keeping the report.

3. According to my variant number, get the grammar definition and do the following tasks:

    a. Implement a type/class for my grammar;

    b. Add one function that would generate 5 valid strings from the language expressed by your given grammar;

    c. Implement some functionality that would convert and object of type Grammar to one of type Finite Automaton;
    
    d. For the Finite Automaton add a method that checks if an input string can be obtained via the state transition from it;


## Implementation description

### Grammar Class

A class that represents a grammar, which consists of a set of non-terminals, a set of terminals, and a list of production rules that specify how to derive strings from the non-terminals. It provides methods to retrieve these components and generate a specified number of strings from the grammar, chosen at random.


```
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

### Grammar to Finite Automaton converter

This method converts a given context-free grammar to a finite automaton. It iterates over the production rules of the grammar, extracts states and symbols, and creates a transition function that maps a state-symbol pair to a set of next states. It then returns the resulting finite automaton with initial and final states.

```
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
```

### Input checker

This method determines whether a given input string is accepted by the finite state machine. It iterates through the input string, checking that each symbol is valid and that there is a valid transition from the current state to the next state. If the final state is reached after processing the entire input string, the method returns true.

```
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
```

## Output

```
5 valid strings:
acaaac
acacacacaabacaac
abcaaac
acacacacaac
acacaabacabcacacabac

Initial State: S
States: [B, S, D]
Transition Functions:
B-a->D
B-b->B
B-c->S
S-a->B
D-a->D
D-b->S
D-c->c (final)
Final States: [c]

The input string acabcacacabcacaaac can be obtained via the state transition from the finite automaton.

or 

The input string acabcacacabcacaaacb cannot be obtained via the state transition from the finite automaton.

```
