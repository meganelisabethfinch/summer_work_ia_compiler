package mef40;

import static com.google.common.truth.Truth.assertThat;

import mef40.grammar.NonTerminal;
import mef40.grammar.Terminal;
import mef40.parser.Item;
import mef40.parser.Production;
import org.junit.Test;

public class ItemTest {
    @Test
    public void itemEquals_returnsTrue_whenEqual() {
        // ARRANGE
        var i1 = new Item(new Production(NonTerminal.EXPR, NonTerminal.EXPR, Terminal.PLUS, NonTerminal.PROD), 0);
        var i2 = new Item(new Production(NonTerminal.EXPR, NonTerminal.EXPR, Terminal.PLUS, NonTerminal.PROD), 0);

        // ACT

        // ASSERT
        var result = i1.equals(i2);
        assertThat(result).isTrue();
    }
}
