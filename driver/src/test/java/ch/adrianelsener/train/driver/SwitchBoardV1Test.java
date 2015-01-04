package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.Pin;
import ch.adrianelsener.train.denkovi.PinState;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mockito.InOrder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class SwitchBoardV1Test {

    @Test
    public void isReady_returnsTrue_ifConnectionIsOk() {
        final Board board = mock(Board.class);
        when(board.isReady()).thenReturn(true);
        final Config config = createSampleConfig();
        final SwitchBoardV1 testee = new SwitchBoardV1(board, config, 0);
        // act
        boolean result = testee.isRead();
        // assert
        assertThat(result, is(true));
    }

    private final Config config = createSampleConfig();

    @DataProvider(name = "leftAndRight", parallel = true)
    public Object[][] createLeftRightData() {
        return new Object[][]{//
                {new Testdata(SwitchWithState._01L, Pin._10.on(), Pin._11.on(), Pin._12.on(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._01R, Pin._10.off(), Pin._11.on(), Pin._12.on(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._02L, Pin._10.on(), Pin._11.off(), Pin._12.on(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._02R, Pin._10.off(), Pin._11.off(), Pin._12.on(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._03L, Pin._10.on(), Pin._11.on(), Pin._12.off(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._03R, Pin._10.off(), Pin._11.on(), Pin._12.off(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._04L, Pin._10.on(), Pin._11.off(), Pin._12.off(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._04R, Pin._10.off(), Pin._11.off(), Pin._12.off(), Pin._13.on(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._05L, Pin._10.on(), Pin._11.on(), Pin._12.on(), Pin._13.off(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._05R, Pin._10.off(), Pin._11.on(), Pin._12.on(), Pin._13.off(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._06L, Pin._10.off(), Pin._11.off(), Pin._12.on(), Pin._13.off(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._06R, Pin._10.off(), Pin._11.on(), Pin._12.off(), Pin._13.off(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._07L, Pin._10.on(), Pin._11.off(), Pin._12.off(), Pin._13.off(), Pin._14, config)} //
                , {new Testdata(SwitchWithState._07R, Pin._10.off(), Pin._11.off(), Pin._12.off(), Pin._13.off(), Pin._14, config)} //
        };
    }

    @Test(dataProvider = "leftAndRight")
    public void callWeiche1(final Testdata testData) {
        final Board board = mock(Board.class);
        final Config config = testData.config;

        final SwitchBoardV1 testee = new SwitchBoardV1(board, config, 0);
        // Act
        testee.turn(testData.weicheSeite);
        // Assert
        final InOrder order = inOrder(board);
        order.verify(board).set(testData.p1);
        order.verify(board).set(testData.p2);
        order.verify(board).set(testData.p3);
        order.verify(board).set(testData.p4);
        order.verify(board).set(PinState.Pon(testData.ptoggle));
        order.verify(board).set(PinState.Poff(testData.ptoggle));
    }

    @Test
    public void getBoardCfg() {
        final Board board = mock(Board.class);

        final SwitchBoardV1 testee = new SwitchBoardV1(board, config, 0);
        // Act
        final Config result = testee.getBoardCfg();
        // assert
        final ConfKey boardKey = ConfKey.createForBoard("RB.00");
        Config expectedConfig = config.getAll(boardKey);
        assertThat(result, equalTo(expectedConfig));
    }

    private Config createSampleConfig() {
        final Config config = new Config();
        config.put(ConfKey.create("RB.00.01"), "P.10");
        config.put(ConfKey.create("RB.00.02"), "P.11");
        config.put(ConfKey.create("RB.00.03"), "P.12");
        config.put(ConfKey.create("RB.00.04"), "P.13");
        config.put(ConfKey.create("RB.00.T"), "P.14");
        return config;
    }

    private static class Testdata {

        private final SwitchWithState weicheSeite;
        private final PinState p1;
        private final PinState p2;
        private final PinState p3;
        private final PinState p4;
        private final Pin ptoggle;
        private final Config config;

        public Testdata(final SwitchWithState weicheSeite, final PinState p1, final PinState p2, final PinState p3, final PinState p4,
                        final Pin ptoggle, final Config config) {
            this.weicheSeite = weicheSeite;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.p4 = p4;
            this.ptoggle = ptoggle;
            this.config = config;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
        }
    }
}
