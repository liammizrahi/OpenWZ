package lexer;

public enum TokenType {
    // Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER, STRING, NUMBER, BOOLEAN,

    // Keywords
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR, NOT,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE, LET,

    // Additional tokens
    DIV, // '/'
    LPAREN, // '('
    RPAREN, // ')'
    SEMI, // ';'
    MULT, // '*',
    EQ,         // ==
    NEQ,        // !=
    LT,         // <
    GT,         // >
    LTE,        // <=
    GTE,        // >=

    // End of file
    EOF
}
