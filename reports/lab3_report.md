# Topic: Lexer & Scanner
### Course: Formal Languages & Finite Automata
### Author: Maria Lesenco

----

## Theory

A lexer, short for lexical analyzer, is a component of a compiler or interpreter that reads a sequence of characters (typically from a source code file) and converts them into a sequence of tokens, which are meaningful units of the programming language syntax.

## Objectives:

1. Understand what lexical analysis is.

2. Get familiar with the inner workings of a lexer/scanner/tokenizer.

3. Implement a sample lexer and show how it works.


## Implementation description

### Lexer

This is a the most important class named Lexer that takes a string input and converts it into a list of tokens, which are lexical units of the input language. The Lexer uses a simple while loop to iterate through the input string character by character, and uses conditional statements to identify and classify each character as a specific token type. If a character does not match any token type, an exception is thrown. The Lexer uses a Token class to create a new token object for each identified token type and adds it to a list of tokens. The resulting list of tokens can be used by a parser to build a parse tree and evaluate the input language.

```
public class Lexer {

    private final String input;
    private int pos;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
    }

    public List<Token> tokenize() throws Exception {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char currentChar = input.charAt(pos);
            if (Character.isDigit(currentChar)) {
                int initialPos = pos;
                while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                    pos++;
                }
                tokens.add(new Token(TokenType.INTEGER, input.substring(initialPos, pos)));
            } else if (Character.isLetter(currentChar)) {
                int startPos = pos;
                while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
                    pos++;
                }
                String identifier = input.substring(startPos, pos);
                switch (identifier) {
                    case "if" -> tokens.add(new Token(TokenType.IF, "if"));
                    case "else" -> tokens.add(new Token(TokenType.ELSE, "else"));
                    case "while" -> tokens.add(new Token(TokenType.WHILE, "while"));
                    case "do" -> tokens.add(new Token(TokenType.DO, "do"));
                    case "int" -> tokens.add(new Token(TokenType.INT, "int"));
                    case "for" -> tokens.add(new Token(TokenType.FOR, "int"));
                    default -> tokens.add(new Token(TokenType.IDENTIFIER, identifier));
                }
            } else if (currentChar == '+') {
                tokens.add(new Token(TokenType.PLUS, "+"));
                pos++;
            } else if (currentChar == '-') {
                tokens.add(new Token(TokenType.MINUS, "-"));
                pos++;
            } else if (currentChar == '*') {
                tokens.add(new Token(TokenType.MULTIPLY, "*"));
                pos++;
            } else if (currentChar == '/') {
                tokens.add(new Token(TokenType.DIVIDE, "/"));
                pos++;
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "("));
                pos++;
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")"));
                pos++;
            } else if (currentChar == '=') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                    tokens.add(new Token(TokenType.EQUALS, "=="));
                    pos += 2;
                } else {
                    tokens.add(new Token(TokenType.ASSIGNMENT, "="));
                    pos++;
                }
            } else if (currentChar == '>') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                    tokens.add(new Token(TokenType.GREATER_THAN_OR_EQUAL, ">="));
                    pos += 2;
                } else {
                    tokens.add(new Token(TokenType.GREATER_THAN, ">"));
                    pos++;
                }
            } else if (currentChar == '<') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                    tokens.add(new Token(TokenType.LESS_THAN_OR_EQUAL, "<="));
                    pos += 2;
                } else {
                    tokens.add(new Token(TokenType.LESS_THAN, "<"));
                    pos++;
                }
            } else if (currentChar == '{') {
                tokens.add(new Token(TokenType.LEFT_BRACE, "{"));
                pos++;
            } else if (currentChar == '}') {
                tokens.add(new Token(TokenType.RIGHT_BRACE, "}"));
                pos++;
            } else if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                pos++;
            } else if (currentChar == ',') {
                tokens.add(new Token(TokenType.COMMA, ","));
                pos++;
            } else if (currentChar == '&') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '&') {
                    tokens.add(new Token(TokenType.AND, "&&"));
                    pos += 2;
                } else {
                    throw new Exception("Invalid token: " + currentChar);
                }
            } else if (currentChar == '|') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '|') {
                    tokens.add(new Token(TokenType.OR, "||"));
                    pos += 2;
                } else {
                    throw new Exception("Invalid token: " + currentChar);
                }
            } else if (currentChar == '!') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                    tokens.add(new Token(TokenType.NOT_EQUALS, "!="));
                    pos += 2;
                } else {
                    tokens.add(new Token(TokenType.NOT, "!"));
                    pos++;
                }
            } else if (currentChar == ' ') {
                pos++;
            } else if (currentChar == '\t') {
                pos++;
            } else if (currentChar == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\n"));
                pos++;
            } else {
                throw new Exception("Invalid token: " + currentChar);
            }
        }
        return tokens;
    }
}

```

### Token class

Here is the token class used by Lexer class. It is a record class that has two fields: an instance of TokenType and a String value. It also overrides the toString() method to print out a formatted string representation of the Token object. The record class is immutable and automatically generates a constructor, getters, equals(), and hashCode() methods.

```
public record Token(TokenType identifier, String value) {

    @Override
    public String toString() {
        return String.format("Token(%s, %s)", identifier, value);
    }
}

```

### Enum TokenType

This is a Java enumeration TokenType that represents the types of tokens in a programming language that are recongnisible by my lexer. Each token type is represented as a named constant, such as INTEGER, IDENTIFIER, IF, ELSE, and so on. 

```
public enum TokenType {
    INTEGER,
    IDENTIFIER,
    IF,
    ELSE,
    WHILE,
    FOR,
    DO,
    INT,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    LEFT_PAREN,
    RIGHT_PAREN,
    EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    NOT_EQUALS,
    ASSIGNMENT,
    LEFT_BRACE,
    RIGHT_BRACE,
    SEMICOLON,
    COMMA,
    AND,
    OR,
    NOT,
    NEWLINE,
    END_OF_FILE
}

```

## Input and output

```
Enter input: 3 + 4 * 2 / ( 1 - 5 )
Token(INTEGER, 3)
Token(PLUS, +)
Token(INTEGER, 4)
Token(MULTIPLY, *)
Token(INTEGER, 2)
Token(DIVIDE, /)
Token(LEFT_PAREN, ()
Token(INTEGER, 1)
Token(MINUS, -)
Token(INTEGER, 5)
Token(RIGHT_PAREN, ))

Enter input: if (x > y) { x = 10; } else { y = 20; }
Token(IF, if)
Token(LEFT_PAREN, ()
Token(IDENTIFIER, x)
Token(GREATER_THAN, >)
Token(IDENTIFIER, y)
Token(RIGHT_PAREN, ))
Token(LEFT_BRACE, {)
Token(IDENTIFIER, x)
Token(ASSIGNMENT, =)
Token(INTEGER, 10)
Token(SEMICOLON, ;)
Token(RIGHT_BRACE, })
Token(ELSE, else)
Token(LEFT_BRACE, {)
Token(IDENTIFIER, y)
Token(ASSIGNMENT, =)
Token(INTEGER, 20)
Token(SEMICOLON, ;)
Token(RIGHT_BRACE, })

Enter input: a = b == c || d != e && f >= g;
Token(IDENTIFIER, a)
Token(ASSIGNMENT, =)
Token(IDENTIFIER, b)
Token(EQUALS, ==)
Token(IDENTIFIER, c)
Token(OR, ||)
Token(IDENTIFIER, d)
Token(NOT_EQUALS, !=)
Token(IDENTIFIER, e)
Token(AND, &&)
Token(IDENTIFIER, f)
Token(GREATER_THAN_OR_EQUAL, >=)
Token(IDENTIFIER, g)
Token(SEMICOLON, ;)
```
