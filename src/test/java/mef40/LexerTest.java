package mef40;

import static com.google.common.truth.Truth.assertThat;

import mef40.grammar.Terminal;
import mef40.lexer.Lexer;
import org.junit.Test;

import java.io.EOFException;
import java.util.Arrays;
import java.util.LinkedList;

public class LexerTest {
    @Test
    public void lex_returnsNumber_forScientificNotation()
            throws IllegalArgumentException, EOFException {
        // ARRANGE
        var input = "5.05e-1";
        var expected = new LinkedList<Token>(Arrays.asList(
                new UFloat(Float.parseFloat(input))
        ));

        // ACT
        var actual = Lexer.lex(input);

        // ASSERT
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual.peek()).isEqualTo(expected.peek());
    }

    @Test
    public void lex_returnsTokens_forExpression1()
            throws IllegalArgumentException, EOFException {
        // ARRANGE
        var input = "12.34+56.78";
        var expected = new LinkedList<>(Arrays.asList(
                new UFloat((float)12.34),
                new Token(Terminal.PLUS),
                new UFloat((float)56.78)
        ));

        // ACT
        var actual = Lexer.lex(input);

        // ASSERT
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < 3; i++) {
            assertThat(actual.poll()).isEqualTo(expected.poll());
        }
    }

    @Test
    public void lex_returnsTokens_forExpression2()
            throws IllegalArgumentException, EOFException {
        // ARRANGE
        var input = "cos cos 5!";
        var expected = new LinkedList<>(Arrays.asList(
                new Token(Terminal.COS),
                new Token(Terminal.COS),
                new UFloat(5),
                new Token(Terminal.FACTORIAL)
        ));

        // ACT
        var actual = Lexer.lex(input);

        // ASSERT
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < 4; i++) {
            assertThat(actual.poll()).isEqualTo(expected.poll());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void lex_throwsException_whenInvalidSymbol()
            throws IllegalArgumentException, EOFException {
        // ARRANGE
        var input = "5.0x24.3co";

        // ACT
        Lexer.lex(input);
    }

    @Test(expected =  EOFException.class)
    public void lex_throwsException_whenStringEndsMidLexeme()
            throws IllegalArgumentException, EOFException {
        // ARRANGE
        var input = "5.0co";

        // ACT
        Lexer.lex(input);
    }
}
