package Lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final String input;
    private int pos;

    private static final Pattern INTEGER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern KEYWORD_PATTERN = Pattern.compile("IF|ELSE|if|else");

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
    }

    public List<Token> tokenize() throws Exception {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char currentChar = input.charAt(pos);
            if (Character.isWhitespace(currentChar)) {
                pos++;
                continue;
            }
            String remainingInput = input.substring(pos);
            Matcher integerMatcher = INTEGER_PATTERN.matcher(remainingInput);
            Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher(remainingInput);
            Matcher keywordMatcher = KEYWORD_PATTERN.matcher(remainingInput);

            if (currentChar == '|') {
                if (peekNextChar() == '|') {
                    consumeChar();
                    consumeChar();
                    tokens.add(new Token(TokenType.OR, "||"));
                    continue;
                }
                throw new Exception("Invalid token: " + currentChar);
            } else if (currentChar == '&') {
                if (peekNextChar() == '&') {
                    consumeChar();
                    consumeChar();
                    tokens.add(new Token(TokenType.AND, "&&"));
                    continue;
                }
                throw new Exception("Invalid token: " + currentChar);
            } else if (currentChar == '!') {
                if (peekNextChar() == '=') {
                    consumeChar();
                    consumeChar();
                    tokens.add(new Token(TokenType.NOT_EQUAL, "!="));
                    continue;
                }
                throw new Exception("Invalid token: " + currentChar);
            } else if (currentChar == '>') {
                if (peekNextChar() == '=') {
                    consumeChar();
                    consumeChar();
                    tokens.add(new Token(TokenType.GREATER_OR_EQUAL, ">="));
                    continue;
                }
                tokens.add(new Token(TokenType.GREATER_THAN, ">"));
                pos++;
                continue;
            } else if (currentChar == '<') {
                if (peekNextChar() == '=') {
                    consumeChar();
                    consumeChar();
                    tokens.add(new Token(TokenType.LESS_OR_EQUAL, "<="));
                    continue;
                }
                tokens.add(new Token(TokenType.LESS_THAN, "<"));
                pos++;
                continue;
            } else if (currentChar == '=') {
                if (peekNextChar() == '=') {
                    consumeChar();
                    consumeChar();
                    tokens.add(new Token(TokenType.EQUALS, "=="));
                    continue;
                }
                tokens.add(new Token(TokenType.ASSIGNMENT, "="));
                pos++;
                continue;
            }

            if (integerMatcher.lookingAt()) {
                String integerMatch = integerMatcher.group();
                tokens.add(new Token(TokenType.INTEGER, integerMatch));
                pos += integerMatch.length();
            } else if (identifierMatcher.lookingAt()) {
                String identifierMatch = identifierMatcher.group();
                TokenType tokenType;
                if (keywordMatcher.lookingAt() && keywordMatcher.start() == 0) {
                    tokenType = TokenType.valueOf(identifierMatch.toUpperCase());
                } else {
                    tokenType = TokenType.IDENTIFIER;
                }
                tokens.add(new Token(tokenType, identifierMatch));
                pos += identifierMatch.length();
            } else {
                TokenType tokenType = getTokenTypeForCharacter(currentChar);
                if (tokenType == null) {
                    throw new Exception("Invalid token: " + currentChar);
                }
                String value = Character.toString(currentChar);
                tokens.add(new Token(tokenType, value));
                pos++;
            }
        }
        tokens.add(new Token(TokenType.END_OF_FILE, ""));
        return tokens;
    }

    private TokenType getTokenTypeForCharacter(char currentChar) {
        switch (currentChar) {
            case '+' -> {
                return TokenType.PLUS;
            }
            case '-' -> {
                return TokenType.MINUS;
            }
            case '*' -> {
                return TokenType.MULTIPLY;
            }
            case '/' -> {
                return TokenType.DIVIDE;
            }
            case '(' -> {
                return TokenType.LEFT_PAREN;
            }
            case ')' -> {
                return TokenType.RIGHT_PAREN;
            }
            case ';' -> {
                return TokenType.SEMICOLON;
            }
            case '{' -> {
                return TokenType.LEFT_BRACE;
            }
            case '}' -> {
                return TokenType.RIGHT_BRACE;
            }
        }
        return null;
    }


    private char peekNextChar() {
        if (pos + 1 < input.length()) {
            return input.charAt(pos + 1);
        }
        return '\0';
    }

    private void consumeChar() {
        pos++;
    }
}
