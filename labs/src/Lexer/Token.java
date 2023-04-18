package Lexer;
public record Token(TokenType identifier, String value) {

    @Override
    public String toString() {
        return String.format("Token(%s, %s)", identifier, value);
    }
}
