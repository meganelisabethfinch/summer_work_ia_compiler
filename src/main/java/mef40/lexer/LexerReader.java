package mef40.lexer;

public class LexerReader {
    private final String string;
    private int lexemeBegin;
    private int forward;

    public LexerReader(String string) {
        this.string = string + "$";
        lexemeBegin = 0;
        forward = 0;
    }

    public int getLexemeBegin() {
        return lexemeBegin;
    }

    public int getForward() {
        return forward;
    }

    public char read() {
        if (lexemeBegin + forward < string.length()) {
            var c = string.charAt(lexemeBegin + forward);
            forward++;
            return c;
        }

        return '$';
    }

    public char peek() {
        return string.charAt(lexemeBegin + forward);
    }

    public void retract() {
        if (forward > 0) {
            forward--;
        }
    }

    public String consumeLexeme() {
        var lexeme = string.substring(lexemeBegin, lexemeBegin + forward);
        lexemeBegin = lexemeBegin + forward;
        forward = 0;
        return lexeme;
    }
}
