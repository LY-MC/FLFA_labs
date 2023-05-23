# Topic: Parser & Building an Abstract Syntax Tree
### Course: Formal Languages & Finite Automata
### Author: Maria Lesenco

----

## Theory

An Abstract Syntax Tree (AST) is a hierarchical representation of the structure and semantics of a program written in a programming language. It captures the syntactic and semantic relationships between the different elements of the program, such as statements, expressions, and declarations. The AST is commonly used in compilers and interpreters to analyze and transform the program, as it provides a more structured and easily manipulable representation compared to the raw source code. By traversing and analyzing the AST, various optimizations and code transformations can be applied to improve the performance and correctness of the program.

## Objectives:

1. Get familiar with parsing, what it is and how it can be programmed 

2. Get familiar with the concept of AST

3. In addition to what has been done in the 3rd lab work do the following:
    i.In case you didn't have a type that denotes the possible types of tokens you need to:
        a. Have a type TokenType (like an enum) that can be used in the lexical analysis to categorize the tokens.
        b. Please use regular expressions to identify the type of the token.
    ii.Implement the necessary data structures for an AST that could be used for the text you have processed in the 3rd lab work.
    iii.Implement a simple parser program that could extract the syntactic information from the input text.


## Implementation description

### TokenType and regular expressions

I have already had the implementation of enum TokenType, but I didn't use regular expressions to identify the type of the token, so regular expressions and matchers were introduced to handle tokenization. Three patterns were defined: INTEGER_PATTERN to match integers, IDENTIFIER_PATTERN to match identifiers, and KEYWORD_PATTERN to match keywords. This allows for a more flexible and maintainable approach to token recognition.

```
    private static final Pattern INTEGER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern KEYWORD_PATTERN = Pattern.compile("IF|ELSE|if|else");
```

### AST

I implemented classes which are part of the Abstract Syntax Tree (AST) structure for representing the parsed program. They provide a hierarchical representation of the program's syntax and can be used for various analysis and transformations of the code.

ASTNode: An interface representing an abstract syntax tree (AST) node. It defines a toString() method that must be implemented by classes implementing this interface.

```
public interface ASTNode {
    @Override
    String toString();
}
```

AssignmentNode: A class representing an assignment statement in the AST. It has an identifier and an expression as its children.

```
public class AssignmentNode implements ASTNode {
    private final String identifier;
    private final ASTNode expression;
    
    // Constructor and methods omitted for brevity
    
    @Override
    public String toString() {
        return "AssignmentNode{\n" +
                "  identifier='" + identifier + "',\n" +
                "  expression=" + expression +
                "\n}";
    }
}
```

BinaryExpressionNode: A class representing a binary expression in the AST. It has a left and a right child, along with an operator.

```
public class BinaryExpressionNode implements ASTNode {
    private final ASTNode left;
    private final ASTNode right;
    private final String operator;
    
    // Constructor and methods omitted for brevity
    
    @Override
    public String toString() {
        return "BinaryExpressionNode{\n" +
                "    left=" + left + ",\n" +
                "    right=" + right + ",\n" +
                "    operator='" + operator + "'" +
                "\n  }";
    }
}
```

BlockNode: A class representing a block of statements in the AST. It contains a list of statements.

```
public class BlockNode implements ASTNode {
    private final List<ASTNode> statements;
    
    // Constructor and methods omitted for brevity
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BlockNode{\n");
        for (ASTNode statement : statements) {
            sb.append("  ").append(statement.toString().replaceAll("\n", "\n  ")).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
```
EmptyStatementNode: A class representing an empty statement in the AST. It does not have any children.

```
public class EmptyStatementNode implements ASTNode {
    @Override
    public String toString() {
        return "EmptyStatementNode";
    }
}
```

IdentifierNode: A class representing an identifier in the AST. It has a single attribute for the identifier name.

```
public class IdentifierNode implements ASTNode {
    private final String identifier;
    
    // Constructor and methods omitted for brevity
    
    @Override
    public String toString() {
        return "IdentifierNode{identifier='" + identifier + "'}";
    }
}
```

IfStatementNode: A class representing an if statement in the AST. It has a condition, an if body, and an else body as its children.

```
public class IfStatementNode implements ASTNode {
    private final ASTNode condition;
    private final ASTNode ifBody;
    private final ASTNode elseBody;
    
    // Constructor and methods omitted for brevity
    
    @Override
    public String toString() {
        return "IfStatementNode{\n" +
                "  condition=" + condition.toString().replaceAll("\n", "\n  ") + ",\n" +
                "  ifBody=" + ifBody.toString().replaceAll("\n", "\n  ") + ",\n" +
                "  elseBody=" + elseBody.toString().replaceAll("\n", "\n  ") +
                "\n}";
    }
}
```

IntegerNode: A class representing an integer literal in the AST. It has a single attribute for the integer value.

```
public class IntegerNode implements ASTNode {
    private final int value;
    
    // Constructor and methods omitted for brevity
    
    @Override
    public String toString() {
        return "IntegerNode{value=" + value + "}";
    }
}
```

### Parser

The Parser class is responsible for converting a sequence of tokens into an abstract syntax tree (AST) representation. It contains methods for parsing different types of statements and expressions based on the token types. The parse() method initiates the parsing process and returns the root node of the generated AST.

Code snippets that demonstrate the process of parsing different types of statements and expressions, creating the corresponding AST nodes, and handling the syntax rules defined by the token types:

1. Parsing a statement:

```
private ASTNode parseStatement() throws Exception {
    Token currentToken = getCurrentToken();
    if (currentToken.getType() == TokenType.IF) {
        return parseIfStatement();
    } else if (currentToken.getType() == TokenType.IDENTIFIER) {
        return parseAssignment();
    } else if (currentToken.getType() == TokenType.LEFT_BRACE) {
        return parseBlock();
    } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
        consumeToken();
        ASTNode expression = parseExpression();
        expectToken(TokenType.RIGHT_PAREN);
        return expression;
    } else if (currentToken.getType() == TokenType.SEMICOLON) {
        consumeToken();
        return new EmptyStatementNode(); 
    } else if (currentToken.getType() == TokenType.END_OF_FILE) {
        return null; 
    }
    throw new Exception("Invalid statement at position: " + currentToken.getValue());
}
```

2. Parsing an if statement:

```
private ASTNode parseIfStatement() throws Exception {
    expectToken(TokenType.IF);
    expectToken(TokenType.LEFT_PAREN);
    ASTNode condition = parseExpression();
    expectToken(TokenType.RIGHT_PAREN);
    ASTNode ifBody = parseStatementOrBlock();
    ASTNode elseBody = null;

    if (matchToken(TokenType.ELSE)) {
        elseBody = parseStatementOrBlock();
    }

    return new IfStatementNode(condition, ifBody, elseBody);
}
```

3. Parsing an assignment statement:

```
private ASTNode parseAssignment() throws Exception {
    Token identifierToken = expectToken(TokenType.IDENTIFIER);
    expectToken(TokenType.ASSIGNMENT);
    ASTNode expression = parseExpression();
    return new AssignmentNode(identifierToken.getValue(), expression);
}
```

## Input and output

```
Enter input: (3 + 4 * 2 / ( 1 - 5 ))

Tokens: [
Token{type=LEFT_PAREN, value='('}, 
Token{type=INTEGER, value='3'}, 
Token{type=PLUS, value='+'}, 
Token{type=INTEGER, value='4'}, 
Token{type=MULTIPLY, value='*'}, 
Token{type=INTEGER, value='2'}, 
Token{type=DIVIDE, value='/'}, 
Token{type=LEFT_PAREN, value='('}, 
Token{type=INTEGER, value='1'}, 
Token{type=MINUS, value='-'}, 
Token{type=INTEGER, value='5'}, 
Token{type=RIGHT_PAREN, value=')'}, 
Token{type=RIGHT_PAREN, value=')'}, 
Token{type=END_OF_FILE, value=''}]

AST: BinaryExpressionNode{
    left=IntegerNode{value=3},
    right=BinaryExpressionNode{
    left=BinaryExpressionNode{
    left=IntegerNode{value=4},
    right=IntegerNode{value=2},
    operator='*'
  },
    right=BinaryExpressionNode{
    left=IntegerNode{value=1},
    right=IntegerNode{value=5},
    operator='-'
  },
    operator='/'
  },
    operator='+'
  }


Enter input: if (x > y) { x = 10; } else { y = 20; }

Tokens: [
Token{type=IF, value='if'}, 
Token{type=LEFT_PAREN, value='('}, 
Token{type=IDENTIFIER, value='x'}, 
Token{type=GREATER_THAN, value='>'}, 
Token{type=IDENTIFIER, value='y'}, 
Token{type=RIGHT_PAREN, value=')'}, 
Token{type=LEFT_BRACE, value='{'}, 
Token{type=IDENTIFIER, value='x'}, 
Token{type=ASSIGNMENT, value='='}, 
Token{type=INTEGER, value='10'}, 
Token{type=SEMICOLON, value=';'}, 
Token{type=RIGHT_BRACE, value='}'}, 
Token{type=ELSE, value='else'}, 
Token{type=LEFT_BRACE, value='{'}, 
Token{type=IDENTIFIER, value='y'}, 
Token{type=ASSIGNMENT, value='='}, 
Token{type=INTEGER, value='20'}, 
Token{type=SEMICOLON, value=';'}, 
Token{type=RIGHT_BRACE, value='}'}, 
Token{type=END_OF_FILE, value=''}]

AST: IfStatementNode{
  condition=BinaryExpressionNode{
      left=IdentifierNode{identifier='x'},
      right=IdentifierNode{identifier='y'},
      operator='>'
    },
  ifBody=BlockNode{
    AssignmentNode{
      identifier='x',
      expression=IntegerNode{value=10}
    }
    EmptyStatementNode
  },
  elseBody=BlockNode{
    AssignmentNode{
      identifier='y',
      expression=IntegerNode{value=20}
    }
    EmptyStatementNode
  }
}


Enter input: a = b == c || d != e && f >= g;

Tokens: [
Token{type=IDENTIFIER, value='a'}, 
Token{type=ASSIGNMENT, value='='}, 
Token{type=IDENTIFIER, value='b'}, 
Token{type=EQUALS, value='=='}, 
Token{type=IDENTIFIER, value='c'}, 
Token{type=OR, value='||'}, 
Token{type=IDENTIFIER, value='d'}, 
Token{type=NOT_EQUAL, value='!='}, 
Token{type=IDENTIFIER, value='e'}, 
Token{type=AND, value='&&'}, 
Token{type=IDENTIFIER, value='f'}, 
Token{type=GREATER_OR_EQUAL, value='>='}, 
Token{type=IDENTIFIER, value='g'}, 
Token{type=SEMICOLON, value=';'}, 
Token{type=END_OF_FILE, value=''}]

AST: AssignmentNode{
  identifier='a',
  expression=BinaryExpressionNode{
    left=BinaryExpressionNode{
    left=BinaryExpressionNode{
    left=IdentifierNode{identifier='b'},
    right=BinaryExpressionNode{
    left=IdentifierNode{identifier='c'},
    right=IdentifierNode{identifier='d'},
    operator='||'
  },
    operator='=='
  },
    right=BinaryExpressionNode{
    left=IdentifierNode{identifier='e'},
    right=IdentifierNode{identifier='f'},
    operator='&&'
  },
    operator='!='
  },
    right=IdentifierNode{identifier='g'},
    operator='>='
  }
}
```