package Parser;

import Lexer.Token;
import Lexer.TokenType;
import AST.*;

import java.util.List;
import java.util.ArrayList;

public class Parser {

    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    public ASTNode parse() throws Exception {
        return parseStatement();
    }

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



    private ASTNode parseStatementOrBlock() throws Exception {
        Token currentToken = getCurrentToken();
        if (currentToken.getType() == TokenType.LEFT_BRACE) {
            return parseBlock();
        } else if (currentToken.getType() == TokenType.IF) {
            return parseIfStatement();
        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
            return parseAssignment();
        }
        throw new Exception("Invalid statement or block at position: " + currentToken.getValue());
    }


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



    private ASTNode parseAssignment() throws Exception {
        Token identifierToken = expectToken(TokenType.IDENTIFIER);
        expectToken(TokenType.ASSIGNMENT);
        ASTNode expression = parseExpression();
        return new AssignmentNode(identifierToken.getValue(), expression);
    }

    private ASTNode parseExpression() throws Exception {
        ASTNode left = parseLogicalOrExpression();
        while (matchToken(TokenType.EQUALS, TokenType.NOT_EQUAL, TokenType.GREATER_THAN, TokenType.GREATER_OR_EQUAL, TokenType.LESS_THAN, TokenType.LESS_OR_EQUAL, TokenType.PLUS, TokenType.MINUS)) {
            Token operatorToken = getPreviousToken();
            ASTNode right = parseLogicalOrExpression();
            left = new BinaryExpressionNode(left, right, operatorToken.getValue());
        }
        return left;
    }

    private ASTNode parseLogicalOrExpression() throws Exception {
        ASTNode left = parseLogicalAndExpression();
        while (matchToken(TokenType.OR)) {
            Token operatorToken = getPreviousToken();
            ASTNode right = parseLogicalAndExpression();
            left = new BinaryExpressionNode(left, right, operatorToken.getValue());
        }
        return left;
    }

    private ASTNode parseLogicalAndExpression() throws Exception {
        ASTNode left = parseTerm();
        while (matchToken(TokenType.AND)) {
            Token operatorToken = getPreviousToken();
            ASTNode right = parseTerm();
            left = new BinaryExpressionNode(left, right, operatorToken.getValue());
        }
        return left;
    }



    private ASTNode parseTerm() throws Exception {
        ASTNode left = parseFactor();
        while (matchToken(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token operatorToken = getPreviousToken();
            ASTNode right = parseFactor();
            left = new BinaryExpressionNode(left, right, operatorToken.getValue());
        }
        return left;
    }

    private ASTNode parseBlock() throws Exception {
        expectToken(TokenType.LEFT_BRACE);
        List<ASTNode> statements = new ArrayList<>();
        while (!checkToken(TokenType.RIGHT_BRACE) && !checkToken(TokenType.END_OF_FILE)) {
            statements.add(parseStatement());
        }
        expectToken(TokenType.RIGHT_BRACE);
        return new BlockNode(statements);
    }

    private ASTNode parseFactor() throws Exception {
        Token currentToken = getCurrentToken();
        if (currentToken.getType() == TokenType.INTEGER) {
            consumeToken();
            int value = Integer.parseInt(currentToken.getValue());
            return new IntegerNode(value);
        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
            consumeToken();
            return new IdentifierNode(currentToken.getValue());
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            expectToken(TokenType.LEFT_PAREN);
            ASTNode expression = parseExpression();
            expectToken(TokenType.RIGHT_PAREN);
            return expression;
        }
        throw new Exception("Invalid factor at position: " + currentToken.getValue());
    }

    private Token expectToken(TokenType expectedType) throws Exception {
        Token currentToken = getCurrentToken();
        if (currentToken.getType() == expectedType) {
            consumeToken();
            return currentToken;
        }
        throw new Exception("Expected token type " + expectedType + " but found " + currentToken.getType());
    }

    private boolean matchToken(TokenType... expectedTypes) {
        for (TokenType expectedType : expectedTypes) {
            if (checkToken(expectedType)) {
                consumeToken();
                return true;
            }
        }
        return false;
    }

    private boolean checkToken(TokenType expectedType) {
        if (isEndOfFile()) {
            return false;
        }
        Token currentToken = getCurrentToken();
        return currentToken.getType() == expectedType;
    }

    private Token consumeToken() {
        if (!isEndOfFile()) {
            pos++;
        }
        return getPreviousToken();
    }

    private Token getCurrentToken() {
        if (isEndOfFile()) {
            return new Token(TokenType.END_OF_FILE, "");
        }
        return tokens.get(pos);
    }

    private Token getPreviousToken() {
        if (pos > 0) {
            return tokens.get(pos - 1);
        }
        return null;
    }

    private boolean isEndOfFile() {
        return pos >= tokens.size();
    }
}
