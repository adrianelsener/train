package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.Pin;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OccupiedBoardBetaTest {
    private final Config cfg = createSampleConfig();
    @Mock
    private Board board;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void readReturnsValueFromBoard() {
        final OccupiedBoardBeta testee = new OccupiedBoardBeta(cfg, board);
        when(board.read(any(Pin.class))).thenReturn(42);
        // Act
        int result = testee.read(Occupied._01);
        // Assert
        assertThat(result, is(equalTo(42)));
    }

    @Test
    public void readCallsBoardCorrect() {
        final OccupiedBoardBeta testee = new OccupiedBoardBeta(cfg, board);
        when(board.read(any(Pin.class))).thenReturn(42);
        // Act
        int result = testee.read(Occupied._01);
        // Assert
        verify(board).read(Pin._17);
    }

    private Config createSampleConfig() {
        final Config config = new Config();
        config.put(ConfKey.create("OB.00.01"), "P.17");
        config.put(ConfKey.create("OB.00.02"), "P.18");
        config.put(ConfKey.create("OB.00.03"), "P.19");
        config.put(ConfKey.create("OB.00.04"), "P.20");
        config.put(ConfKey.create("OB.00.05"), "P.21");
        config.put(ConfKey.create("OB.00.06"), "P.22");
        config.put(ConfKey.create("OB.00.07"), "P.23");
        config.put(ConfKey.create("OB.00.08"), "P.24");
        return config;
    }

}
