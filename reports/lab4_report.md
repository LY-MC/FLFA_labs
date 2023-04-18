# Topic: Chomsky Normal Form
### Course: Formal Languages & Finite Automata
### Author: Maria Lesenco

----

## Theory

The Chomsky Normal Form (CNF) is a widely used form for context-free grammars in theoretical computer science and linguistics. In CNF, all production rules are of the form A → BC or A → a, where A, B, and C are nonterminal symbols and a is a terminal symbol. CNF simplifies parsing algorithms and allows for efficient analysis of the structure of the language. Additionally, every context-free language can be represented by a grammar in CNF.

## Objectives:

1. Learn about Chomsky Normal Form (CNF)

2. Get familiar with the approaches of normalizing a grammar.

3. Implement a method for normalizing an input grammar by the rules of CNF.
    i.The implementation needs to be encapsulated in a method with an appropriate signature (also ideally in an appropriate class/type).
    ii.The implemented functionality needs executed and tested.
    iii.A BONUS point will be given for the student who will have unit tests that validate the functionality of the project.
    iv.Also, another BONUS point would be given if the student will make the aforementioned function to accept any grammar, not only the one from the student's variant.


## Implementation description

### ChromskyNormalForm

This ChomskyNormalForm class is a collection of static methods for transforming a context-free grammar into Chomsky Normal Form.

```
public class ChomskyNormalForm {

    public static Grammar eliminateEpsilonProductions(Grammar grammar) {
    
    }

    public static Grammar eliminateRenaming(Grammar grammar) {

    }

    public static Grammar eliminateInaccessibleSymbols(Grammar grammar) {
 
    }

    public static Grammar eliminateNonProductiveSymbols(Grammar grammar) {

    }

    public static Grammar convertToChomskyNormalForm(Grammar grammar) {

    }
}

```

### eliminateEpsilonProductions method

The method first initializes a set of nullable non-terminals and sets a flag for changes made to the grammar.

It then enters a loop that iterates until no more changes can be made to the nullable non-terminals.

Within the loop, it iterates through each production rule in the grammar and checks if its right-hand side is empty or contains only nullable non-terminals. If it does, it adds the left-hand side non-terminal to the nullable non-terminals set.

After all nullable non-terminals have been determined, it creates a new list of production rules with all possible combinations of non-empty right-hand sides with nullable non-terminals removed.

Finally, it creates and returns a new Grammar object with the updated production rules, non-terminals, and terminals.

```
//Creating the nullableNonTerminals set and initializing the changed variable to true

Set<String> nullableNonTerminals = new HashSet<>();
boolean changed = true;

// Iterating over the production rules and checking if they have an empty right-hand side

for (ProductionRule productionRule : grammar.getProductionRules()) {
    if (productionRule.getRightHandSide().isEmpty()) {
        nullableNonTerminals.add(productionRule.getLeftHandSide());
        changed = nullableNonTerminals.add(productionRule.getLeftHandSide()) || changed;
    } else {
        // ...
    }
}

```

### eliminateRenaming method

This method takes a Grammar object and returns a new Grammar object that eliminates renaming productions. It iteratively checks for production rules where the right-hand side contains only one non-terminal symbol, removes them and replaces them with new production rules that do not have the non-terminal symbol. The process continues until no more changes can be made.

```
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

```

### eliminateInaccessibleSymbolsmethod

This method takes a grammar and returns a new grammar with all inaccessible symbols removed.

The method uses a BFS algorithm to determine which symbols are accessible from the start symbol, and then removes all other symbols.

The resulting grammar has a new set of non-terminals, terminals, and production rules.

For each production rule in the original grammar, the method only keeps the rule if its left-hand side is accessible, and replaces its right-hand side with a new right-hand side containing only accessible symbols.

The method returns the resulting grammar.

```
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


```

### eliminateNonProductiveSymbols method

This method removes non-productive symbols from a given grammar. It first identifies the productive non-terminals by traversing through all the production rules. Then, it removes any rule with a non-productive left-hand side, and removes any non-productive symbols on the right-hand side of a rule. The resulting grammar will only contain symbols that can be derived from the start symbol.

```
// Checking if a production rule is productive or not
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

// Creating new production rules with only productive symbols
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

```

### convertToChomskyNormalForm method

This method converts a context-free grammar into Chomsky normal form. It eliminates epsilon productions, renaming, and inaccessible symbols. It creates new non-terminals and production rules to replace longer production rules, ensuring that all right-hand sides have either two non-terminals or a single terminal. Finally, it adds new productions for each terminal in the original grammar. The result is a new grammar object in Chomsky normal form.

```
grammar = eliminateEpsilonProductions(grammar);
grammar = eliminateRenaming(grammar);
grammar = eliminateInaccessibleSymbols(grammar);

// ...

for (ProductionRule productionRule : grammar.getProductionRules()) {
    List<String> rightHandSide = productionRule.getRightHandSide();
    if (rightHandSide.size() == 1) {
        productionRules.add(productionRule);
    } else if (rightHandSide.size() == 2) {
        productionRules.add(productionRule);
    } else {
        // ...
    }
}

// ...

for (String terminal : terminals) {
    String newVariable = "V" + (nonTerminals.size() + 1);
    nonTerminals.add(newVariable);
    productionRules.add(new ProductionRule(newVariable, Arrays.asList(terminal)));
}

return new Grammar(nonTerminals, terminals, productionRules);

```

## Input and output

This is how I initialize grammar from my Varient 13:

![Screenshot 2023-04-19 004240](https://user-images.githubusercontent.com/88684853/232912158-c87a9a2a-12a6-47a0-bdef-3e5c2153bc88.png)

```
    Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "A", "B", "C", "D"));
    Set<String> terminals = new HashSet<>(Arrays.asList("a", "b"));
    List<ProductionRule> productionRules = new ArrayList();
    productionRules.add(new ProductionRule("S", Arrays.asList("a", "B")));
    productionRules.add(new ProductionRule("S", Arrays.asList("D", "A")));
    productionRules.add(new ProductionRule("A", List.of("a")));
    productionRules.add(new ProductionRule("A", Arrays.asList("B", "D")));
    productionRules.add(new ProductionRule("A", Arrays.asList("b", "D", "A", "B")));
    productionRules.add(new ProductionRule("B", List.of("b")));
    productionRules.add(new ProductionRule("B", Arrays.asList("B", "A")));
    productionRules.add(new ProductionRule("D", List.of()));
    productionRules.add(new ProductionRule("D", Arrays.asList("B", "A")));
    productionRules.add(new ProductionRule("C", Arrays.asList("B", "A")));
    Grammar grammar = new Grammar(nonTerminals, terminals, productionRules);
```

This is the output:

```
Initial grammar:
Non-terminals: A, B, S, C, D
Terminals: a, b
Production rules:
A -> a | BD | bDAB
B -> b | BA
S -> aB | DA
C -> BA
D ->  | BA

Grammar after eliminating epsilon productions:
Non-terminals: A, B, S, C, D
Terminals: a, b
Production rules:
A -> aa | BDB | BDBD | BDB | bDABbAB | bDABbDAB | bDABbAB
B -> bb | BABA
S -> aBaB | DAA | DADA | DAA
C -> BABA
D -> BABA

Grammar after eliminating renaming:
Non-terminals: A, B, S, C, D
Terminals: a, b
Production rules:
A -> aa | BDB | BDBD | BDB | bDABbAB | bDABbDAB | bDABbAB
B -> bb | BABA
S -> aBaB | DAA | DADA | DAA
C -> BABA
D -> BABA

Grammar after eliminating inaccessible symbols:
Non-terminals: A, B, D
Terminals: a, b
Production rules:
A -> aa | BDB | BDBD | BDB | bDABbAB | bDABbDAB | bDABbAB
B -> bb | BABA
D -> BABA

Grammar after eliminating non-productive symbols:
Non-terminals: A, B, D
Terminals: a, b
Production rules:
A -> aa | BDB | BDBD | BDB | bDABbAB | bDABbDAB | bDABbAB
B -> bb | BABA
D -> BABA

Grammar after conversation to Chomsky Normal Form:
Non-terminals: A, B, D, V21, V20, V23, V22, V25, V24, V27, V26, V29, V28, V4, V5, V6, V7, V8, V9, V10, V12, V11, V14, V13, V16, V15, V18, V17, V19
Terminals: a, b
Production rules:
A -> aa | BV4 | BV5 | BV7 | bV8 | bV13 | bV19
B -> bb | BV24
D -> BV26
V21 -> BV22
V20 -> AV21
V23 -> AB
V22 -> bV23
V25 -> BA
V24 -> AV25
V27 -> BA
V26 -> AV27
V29 -> b
V28 -> a
V4 -> DB
V5 -> DV6
V6 -> BD
V7 -> DB
V8 -> DV9
V9 -> AV10
V10 -> BV11
V12 -> AB
V11 -> bV12
V14 -> AV15
V13 -> DV14
V16 -> bV17
V15 -> BV16
V18 -> AB
V17 -> DV18
V19 -> DV20
```