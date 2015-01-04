package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwitchBoardV2Test {
    @Test
    public void isReady_returnsTrue_ifConnectionIsOk() {
        final Board board = mock(Board.class);
        when(board.isReady()).thenReturn(true);
        final Config config = createSampleConfig();
        final SwitchBoardV2 testee = new SwitchBoardV2(board, config, 0);
        // act
        boolean result = testee.isRead();
        // assert
        assertThat(result, is(true));
    }

    private Config createSampleConfig() {
        final Config config = new Config();
        config.put(ConfKey.create("RB.00.S1"), "P.10");
        config.put(ConfKey.create("RB.00.S2"), "P.11");
        config.put(ConfKey.create("RB.00.S3"), "P.12");
        config.put(ConfKey.create("RB.00.S4"), "P.13");
        config.put(ConfKey.create("RB.00.B1"), "P.14");
        config.put(ConfKey.create("RB.00.B2"), "P.15");
        config.put(ConfKey.create("RB.00.B3"), "P.16");
        config.put(ConfKey.create("RB.00.B4"), "P.17");
        config.put(ConfKey.create("RB.00.T"), "P.18");
        return config;
    }

}