package mef40;

import static com.google.common.truth.Truth.assertThat;

import mef40.grammar.NonTerminal;
import mef40.grammar.Terminal;
import mef40.parser.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParsingTableTest {
    // Simple grammar for testing ParsingTable helper functions
    private static final List<Production> simpleGrammar = List.of(
        new Production(NonTerminal.START, NonTerminal.EXPR),
        new Production(NonTerminal.EXPR, NonTerminal.EXPR, Terminal.PLUS, NonTerminal.PROD),
        new Production(NonTerminal.EXPR, NonTerminal.PROD),
        new Production(NonTerminal.PROD, NonTerminal.PROD, Terminal.MULT, NonTerminal.STATEMENT),
        new Production(NonTerminal.PROD, NonTerminal.STATEMENT),
        new Production(NonTerminal.STATEMENT, Terminal.OPEN, NonTerminal.EXPR, Terminal.CLOSE),
        new Production(NonTerminal.STATEMENT, Terminal.UFLOAT)
    );

    @Test
    public void generateClosure_forSimpleGrammar() {
        // ARRANGE
        // E' -> E, E -> E + P | P, P -> P * S | S, S -> (E) | num
        var items = Set.of(new Item(simpleGrammar.get(0), 0));

        // E' -> . E, E -> . E + P, E -> . P, P -> . P * S, P -> . S, S -> . ( E ), S -> . num
        var expected = Set.of(
          new Item(simpleGrammar.get(0), 0),
          new Item(simpleGrammar.get(1), 0),
          new Item(simpleGrammar.get(2), 0),
          new Item(simpleGrammar.get(3), 0),
          new Item(simpleGrammar.get(4), 0),
          new Item(simpleGrammar.get(5), 0),
          new Item(simpleGrammar.get(6), 0)
        );

        // ACT
        var actual = ParsingTable.generateClosure(items, simpleGrammar);

        // ASSERT
        assertThat(actual).containsExactlyElementsIn(expected);
    }

    @Test
    public void testGoto_forSimpleGrammar() {
        // ARRANGE
        var I = Set.of(new Item(simpleGrammar.get(0), 1), new Item(simpleGrammar.get(1), 1));

        var expected = Set.of(
                new Item(simpleGrammar.get(1), 2),
                new Item(simpleGrammar.get(3), 0),
                new Item(simpleGrammar.get(4), 0),
                new Item(simpleGrammar.get(5), 0),
                new Item(simpleGrammar.get(6), 0)
        );

        // ACT
        var actual = ParsingTable.generateGoto(I, Terminal.PLUS, simpleGrammar);

        // ASSERT
        assertThat(actual).containsExactlyElementsIn(expected);
    }

    @Test
    public void testItems_forSimpleGrammar() {
        // ARRANGE
        var I1 = Set.of(new Item(simpleGrammar.get(0), 1), new Item(simpleGrammar.get(1), 1));
        var I5 = Set.of(new Item(simpleGrammar.get(6), 1));
        var I11 = Set.of(new Item(simpleGrammar.get(5), 3));

        // ACT
        var actual = ParsingTable.generateItems(simpleGrammar);

        // ASSERT
        assertThat(actual.size()).isEqualTo(12);
        assertThat(actual).contains(I1);
        assertThat(actual).contains(I5);
        assertThat(actual).contains(I11);
    }

    @Test
    public void generateFirsts_forSimpleGrammar() {
        // ARRANGE
        var nt_first = Set.of(Terminal.OPEN, Terminal.UFLOAT);
        var expected = Map.ofEntries(
                Map.entry(NonTerminal.START, nt_first),
                Map.entry(NonTerminal.EXPR, nt_first),
                Map.entry(NonTerminal.PROD, nt_first),
                Map.entry(NonTerminal.STATEMENT, nt_first),
                Map.entry(Terminal.PLUS, Set.of(Terminal.PLUS)),
                Map.entry(Terminal.MULT, Set.of(Terminal.MULT)),
                Map.entry(Terminal.OPEN, Set.of(Terminal.OPEN)),
                Map.entry(Terminal.CLOSE, Set.of(Terminal.CLOSE)),
                Map.entry(Terminal.UFLOAT, Set.of(Terminal.UFLOAT))
        );

        // ACT
        var actual = ParsingTable.generateFirsts(simpleGrammar);

        // ASSERT
        assertThat(actual).containsExactlyEntriesIn(expected);
    }

    @Test
    public void generateFollows_forSimpleGrammar() {
        // ARRANGE
        var firsts = ParsingTable.generateFirsts(simpleGrammar);

        var expected = Map.of(
                NonTerminal.START, Set.of(Terminal.$),
                NonTerminal.EXPR, Set.of(Terminal.$, Terminal.PLUS, Terminal.CLOSE),
                NonTerminal.PROD, Set.of(Terminal.$, Terminal.MULT, Terminal.PLUS, Terminal.CLOSE),
                NonTerminal.STATEMENT, Set.of(Terminal.$, Terminal.MULT, Terminal.PLUS, Terminal.CLOSE)
        );

        // ACT
        var actual = ParsingTable.generateFollows(simpleGrammar, firsts);

        // ASSERT
        assertThat(actual).containsExactlyEntriesIn(expected);
    }

    @Test
    public void constructSLRParsingTable_forSimpleGrammar() {
        // ARRANGE
        // Generate some states from their kernels
        var I0 = ParsingTable.generateClosure(Set.of(new Item(simpleGrammar.get(0), 0)), simpleGrammar);
        var I2 = Set.of(new Item(simpleGrammar.get(2), 1), new Item(simpleGrammar.get(3), 1));
        var I5 = Set.of(new Item(simpleGrammar.get(6), 1));

        // ACT
        var actual = new ParsingTable(simpleGrammar);

        // ASSERT
        // State 2 should Reduce E -> P on $, +, and )
        for (Terminal t : Set.of(Terminal.$, Terminal.PLUS, Terminal.CLOSE)) {
            Action action = actual.ACTION(I2, t);
            assertThat(action.getClass()).isEqualTo(Reduce.class);
            Reduce reduce = (Reduce)action;
            assertThat(reduce.production).isEqualTo(simpleGrammar.get(2));
        }

        Action action = actual.ACTION(I0, Terminal.UFLOAT);
        assertThat(action.getClass()).isEqualTo(Shift.class);
        Shift shift = (Shift)action;
        assertThat(shift.state).containsExactlyElementsIn(I5);
    }
}
