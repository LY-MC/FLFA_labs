//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import FiniteAutomaton.*;
import Grammar.*;
import java.util.*;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {

        Set<String> nonTerminals = new HashSet(Arrays.asList("S", "B", "D"));
        Set<String> terminals = new HashSet(Arrays.asList("a", "b", "c"));
        List<ProductionRule> productionRules = new ArrayList();
        productionRules.add(new ProductionRule("S", Arrays.asList("a", "B")));
        productionRules.add(new ProductionRule("B", Arrays.asList("a", "D")));
        productionRules.add(new ProductionRule("B", Arrays.asList("b", "B")));
        productionRules.add(new ProductionRule("D", Arrays.asList("a", "D")));
        productionRules.add(new ProductionRule("D", Arrays.asList("b", "S")));
        productionRules.add(new ProductionRule("B", Arrays.asList("c", "S")));
        productionRules.add(new ProductionRule("D", List.of("c")));
        Grammar grammar = new Grammar(nonTerminals, terminals, productionRules);
        System.out.print("Grammar type: ");
        System.out.println(grammar.classifyGrammar());
        System.out.println();

        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");
        states.add("q2");
        states.add("q3");

        Set<String> symbols = new HashSet<>();
        symbols.add("a");
        symbols.add("b");

        Map<String, Map<String, Set<String>>> transitionFunction = new HashMap<>();

        Map<String, Set<String>> q0Transitions = new HashMap<>();
        Set<String> q0_a = new HashSet<>();
        q0_a.add("q0");
        q0Transitions.put("a", q0_a);
        Set<String> q0_b = new HashSet<>();
        q0_b.add("q1");
        q0Transitions.put("b", q0_b);
        transitionFunction.put("q0", q0Transitions);

        Map<String, Set<String>> q1Transitions = new HashMap<>();
        Set<String> q1_a = new HashSet<>();
        q1_a.add("q1");
        q1Transitions.put("a", q1_a);
        Set<String> q1_b = new HashSet<>();
        q1_b.add("q2");
        q1Transitions.put("b", q1_b);
        transitionFunction.put("q1", q1Transitions);

        Map<String, Set<String>> q2Transitions = new HashMap<>();
        Set<String> q2_a = new HashSet<>();
        q2_a.add("q2");
        q2Transitions.put("a", q2_a);
        Set<String> q2_b = new HashSet<>();
        q2_b.add("q3");
        q2Transitions.put("b", q2_b);
        transitionFunction.put("q2", q2Transitions);

        Map<String, Set<String>> q3Transitions = new HashMap<>();
        transitionFunction.put("q3", q3Transitions);

        Set<String> finalStates = new HashSet<>();
        finalStates.add("q3");

        String initialState = "q0";


        FiniteAutomaton nfa = new FiniteAutomaton(states, symbols, transitionFunction, finalStates, initialState);
        System.out.println(nfa.toGrammar());
        if (nfa.isDeterministic()) {
            System.out.println("this Finite Automaton is deterministic");
        } else {
            System.out.println("this Finite Automaton is non-deterministic");
        }

        System.out.println();

        FiniteAutomaton dfa = NDFAToDFAConverter.convertToDFA(nfa);
        System.out.println(dfa);
    }
}
