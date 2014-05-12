package ch.adrianelsener.train.driver;

import static org.mockito.Mockito.verify;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.DenkoviWrapper.Pin;
import ch.adrianelsener.train.denkovi.PinState;

public class SpeedBoardV1Test {
    private final Config cfg = createSampleConfig();
    @Mock
    private Board board;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fasterTogglesOnce() {
        final SpeedBoardV1 testee = new SpeedBoardV1(cfg, board);
        // Act
        testee.faster();
        // Assert
        verify(board).set(PinState.Pon(Pin._10));
        verify(board).set(PinState.Poff(Pin._11));
        verify(board).set(PinState.Pon(Pin._12));
        verify(board).set(PinState.Poff(Pin._12));
    }

    @Test
    public void slowerTogglesOnce() {
        final SpeedBoardV1 testee = new SpeedBoardV1(cfg, board);
        // Act
        testee.slower();
        // Assert
        verify(board).set(PinState.Poff(Pin._10));
        verify(board).set(PinState.Pon(Pin._11));
        verify(board).set(PinState.Pon(Pin._12));
        verify(board).set(PinState.Poff(Pin._12));
    }

    private Config createSampleConfig() {
        final Config config = new Config();
        config.put(ConfKey.create("SB.00.Up"), "P.10");
        config.put(ConfKey.create("SB.00.Down"), "P.11");
        config.put(ConfKey.create("SB.00.SpeedStep"), "P.12");
        config.put(ConfKey.create("SB.00.CS"), "P.13");
        config.put(ConfKey.create("SB.00.URef"), "P.20");
        return config;
    }

}
