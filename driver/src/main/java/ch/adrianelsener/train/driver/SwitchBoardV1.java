package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.DenkoviWrapper.Pin;
import ch.adrianelsener.train.denkovi.PinState;

import com.google.common.collect.ImmutableList;

public class SwitchBoardV1 extends AbstractBoard implements SwitchBoardDriver {
    private final Config boardProps;

    private enum State {
        On {
            @Override
            public PinState forPin(final Pin pin) {
                return new PinState(pin, ch.adrianelsener.train.denkovi.DenkoviWrapper.State.On);
            }
        }//
        ,
        Off {
            @Override
            public PinState forPin(final Pin pin) {
                return new PinState(pin, ch.adrianelsener.train.denkovi.DenkoviWrapper.State.Off);
            }

        };
        public abstract PinState forPin(Pin pin);
    }

//    public enum Weiche {
//
//        _01(WeicheMitState._01L, WeicheMitState._01R)//
//        , _02(WeicheMitState._02L, WeicheMitState._02R)//
//        , _03(WeicheMitState._03L, WeicheMitState._03R)//
//        , _04(WeicheMitState._04L, WeicheMitState._04R)//
//        , _05(WeicheMitState._05L, WeicheMitState._05R)//
//        , _06(WeicheMitState._06L, WeicheMitState._06R)//
//        , _07(WeicheMitState._07L, WeicheMitState._07R)//
//        ;
//
//        private final WeicheMitState links;
//        private final WeicheMitState rechts;
//
//        private Weiche(final WeicheMitState links, final WeicheMitState rechts) {
//            this.links = links;
//            this.rechts = rechts;
//
//        }
//
//        public WeicheMitState left() {
//            return links;
//        }
//
//        public WeicheMitState right() {
//            return rechts;
//        }
//    }

    public enum WeicheMitState {
        _01L(State.On, State.On, State.On, State.On)//
        , _01R(State.Off, State.On, State.On, State.On)//
        , _02L(State.On, State.Off, State.On, State.On) //
        , _02R(State.Off, State.Off, State.On, State.On)//
        , _03L(State.On, State.On, State.Off, State.On) //
        , _03R(State.Off, State.On, State.Off, State.On)//
        , _04L(State.On, State.Off, State.Off, State.On)//
        , _04R(State.Off, State.Off, State.Off, State.On)//
        , _05L(State.On, State.On, State.On, State.Off)//
        , _05R(State.Off, State.On, State.On, State.Off)//
        , _06L(State.Off, State.Off, State.On, State.Off)//
        , _06R(State.Off, State.On, State.Off, State.Off)//
        , _07L(State.On, State.Off, State.Off, State.Off)//
        , _07R(State.Off, State.Off, State.Off, State.Off)//
        ;
        private final ImmutableList<State> states;

        private WeicheMitState(final State p1, final State p2, final State p3, final State p4) {
            states = ImmutableList.of(p1, p2, p3, p4);
        }
    }

    public SwitchBoardV1(final Board board, final Config config, final int boardNummer) {
        super(board);
        String rbPrefix = "RB.";
        if (boardNummer < 10) {
            rbPrefix += "0";
        }
        final ConfKey boardKey = ConfKey.createForBoard(rbPrefix + boardNummer);
        boardProps = config.getAll(boardKey);
    }

    @Override
    public void turn(final WeicheMitState statedRelay) {
        setStatePins(statedRelay);
        final String toggleNumber = boardProps.getChild("T");
        final Pin togglePin = Pin.forPDotNotation(toggleNumber);
        togglePin(togglePin);
    }

    private void setStatePins(final WeicheMitState statedRelay) {
        for (int i = 0; i < 4; i++) {
            setStatePin(statedRelay, i);
        }
    }

    private void setStatePin(final WeicheMitState statedRelay, final int i) {
        final PinState pinState = getPinStateForId(statedRelay, i);
        set(pinState);
    }

    private PinState getPinStateForId(final WeicheMitState statedRelay, final int i) {
        final String mappedPin = boardProps.getChild("0" + (i + 1)).substring(2);
        final Pin pin1 = Pin.forNumber(mappedPin);
        return statedRelay.states.get(i).forPin(pin1);
    }

    @Override
    protected Config getBoardCfg() {
        return boardProps;
    }
}
