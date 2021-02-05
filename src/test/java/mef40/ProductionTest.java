package mef40;


import static com.google.common.truth.Truth.assertThat;

import mef40.grammar.NonTerminal;
import mef40.grammar.Terminal;
import mef40.parser.Production;
import org.junit.Test;

public class ProductionTest {
    @Test
    public void productionEquals_returnsTrue_whenEqual() {
        // ARRANGE
        var p1 = new Production(NonTerminal.EXPR, NonTerminal.EXPR, Terminal.PLUS, NonTerminal.PROD);
        var p2 = new Production(NonTerminal.EXPR, NonTerminal.EXPR, Terminal.PLUS, NonTerminal.PROD);

        // ACT
        boolean result = p1.equals(p2);

        // ASSERT
        assertThat(result).isTrue();
    }
}
