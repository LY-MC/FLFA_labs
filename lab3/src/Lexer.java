import java.util.ArrayList;
import java.util.List;

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