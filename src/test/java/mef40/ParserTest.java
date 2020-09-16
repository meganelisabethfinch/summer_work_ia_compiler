package mef40;

import mef40.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.google.common.truth.Truth.assertThat;

public class ParserTest {
    @Test
    public void parser_parsesNumber() {
        // ARRANGE
        Queue<Token> tokens = new LinkedList<>(List.of(new UFloat(6)));
        var expected = List.of(
                Parser.grammar.get(14), // FLOAT -> UFLOAT
                Parser.grammar.get(12), // STATEMENT -> FLOAT
                Parser.grammar.get(10), // OPTFACT -> STATEMENT
                Parser.grammar.get(8), // OPTCOS -> OPTFACT
                Parser.grammar.get(6), // PRODUCT -> OPTCOS
                Parser.grammar.get(4), // DIFFERENCE -> PRODUCT
                Parser.grammar.get(2) // EXPRESSION -> DIFFERENCE
        );
        // Does not include START -> EXPRESSION

        // ACT
        var actual = Parser.parse(tokens);

        // ASSERT
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void parser_acceptsSimpleExpression() {
        // ARRANGE
        var tokens = new LinkedList<>(List.of(
                new UFloat((float)2.3),
                new Token(Terminal.PLUS),
                new UFloat(4)
        ));

        // ACT
        Parser.parse(tokens);

        // ASSERT
        // Passes as long as Parser.parse() throws no exceptions
    }

    @Test
    public void parser_acceptsNestedFactorial() {
        // ARRANGE

        // ACT

        // ASSERT
        assertThat(true).isFalse();
    }

    @Test
    public void parser_acceptsNestedCos() {
        // ARRANGE

        // ACT

        // ASSERT
        assertThat(true).isFalse();
    }

    @Test
    public void parser_rejectsInvalidExpression() {
        // ARRANGE

        // ACT

        // ASSERT
        assertThat(true).isFalse();
    }
}
