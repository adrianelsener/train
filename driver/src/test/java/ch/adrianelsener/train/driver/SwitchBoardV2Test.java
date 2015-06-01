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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
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

    @Test
    public void getBoardCfg() {
        final Board board = mock(Board.class);
        final Config config = createSampleConfig();
        final SwitchBoardV2 testee = new SwitchBoardV2(board, config, 0);
        // Act
        final Config result = testee.getBoardCfg();
        // assert
        final ConfKey boardKey = ConfKey.createForBoard("RB.00");
        Config expectedConfig = config.getAll(boardKey);
        assertThat(result, equalTo(expectedConfig));
    }

    @Test(dataProvider = "leftAndRight")
    public void turn(final Testdata testData) {
        final Board board = mock(Board.class);
        final Config config = testData.config;

        final SwitchBoardV2 testee = new SwitchBoardV2(board, config, 0);
        // Act
        testee.turn(testData.weicheSeite);
        // Assert
        testData.verify(board);
    }

    @DataProvider(name = "leftAndRight", parallel = true)
    public Object[][] createLeftRightData() {
        final Config config = createSampleConfig();
        config.put(ConfKey.create("RB.00.B"), "01");
        PinState p09 = PinState.Pon(Pin._09);
        PinState p10 = PinState.Pon(Pin._10);
        PinState p11 = PinState.Pon(Pin._11);
        PinState p12 = PinState.Pon(Pin._12);
        PinState p13 = PinState.Pon(Pin._13);
        PinState p14 = PinState.Pon(Pin._14);
        PinState p15 = PinState.Pon(Pin._15);
        PinState p16 = PinState.Pon(Pin._16);
        Pin p17 = Pin._17;
        return new Object[][]{//
                {new Testdata(SwitchWithState._01L, p09, p10, p11, p12,p13, p14, p15, p16, p17, config)}//
        };
    }


    private Config createSampleConfig() {
        final Config config = new Config();
        config.put(ConfKey.create("RB.00.S1"), "P.09");
        config.put(ConfKey.create("RB.00.S2"), "P.10");
        config.put(ConfKey.create("RB.00.S3"), "P.11");
        config.put(ConfKey.create("RB.00.S4"), "P.12");
        config.put(ConfKey.create("RB.00.B1"), "P.13");
        config.put(ConfKey.create("RB.00.B2"), "P.14");
        config.put(ConfKey.create("RB.00.B3"), "P.15");
        config.put(ConfKey.create("RB.00.B4"), "P.16");
        config.put(ConfKey.create("RB.00.T"), "P.17");
        return config;
    }

    private static class Testdata {

        private final SwitchWithState weicheSeite;
        private final PinState s1;
        private final PinState s2;
        private final PinState s3;
        private final PinState s4;
        private final PinState b1;
        private final PinState b2;
        private final PinState b3;
        private final PinState b4;
        private final Pin ptoggle;
        private final Config config;

        public Testdata(final SwitchWithState weicheSeite, final PinState s1, final PinState s2, final PinState s3, final PinState s4,final PinState b1, final PinState b2, final PinState b3, final PinState b4,
                        final Pin ptoggle, final Config config) {
            this.weicheSeite = weicheSeite;
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
            this.s4 = s4;
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
            this.ptoggle = ptoggle;
            this.config = config;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }

        public void verify(final Board board) {
            final InOrder order = inOrder(board);
            order.verify(board).set(eq(b1));
            order.verify(board).set(eq(b2));
            order.verify(board).set(eq(b3));
            order.verify(board).set(eq(b4));
            order.verify(board).set(eq(s1));
            order.verify(board).set(eq(s2));
            order.verify(board).set(eq(s3));
            order.verify(board).set(eq(s4));
            order.verify(board).set(PinState.Pon(ptoggle));
            order.verify(board).set(PinState.Poff(ptoggle));
        }
    }


}