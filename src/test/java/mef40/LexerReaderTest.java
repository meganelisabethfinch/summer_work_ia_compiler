package mef40;

import static com.google.common.truth.Truth.assertThat;

import mef40.lexer.LexerReader;
import org.junit.Test;

public class LexerReaderTest {
    @Test
    public void lexerReader_setsPointers_whenConsumingLexeme() {
        // ARRANGE
        var input = "(9)";
        var reader = new LexerReader(input);

        // ACT
        reader.read();
        reader.consumeLexeme();

        // ASSERT
        assertThat(reader.getLexemeBegin()).isEqualTo(1);
        assertThat(reader.getForward()).isEqualTo(0);
    }
}
