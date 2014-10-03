package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.denkovi.Pin;
import ch.adrianelsener.train.denkovi.PinState;
import com.google.common.collect.ImmutableList;

public enum SwitchWithState {
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

    private SwitchWithState(final State p1, final State p2, final State p3, final State p4) {
        states = ImmutableList.of(p1, p2, p3, p4);
    }

    public ImmutableList<State> getStates() {
        return states;
    }

    public enum State {
        On {
            @Override
            public PinState forPin(final Pin pin) {
                return new PinState(pin, ch.adrianelsener.train.denkovi.State.On);
            }
        }//
        ,
        Off {
            @Override
            public PinState forPin(final Pin pin) {
                return new PinState(pin, ch.adrianelsener.train.denkovi.State.Off);
            }

        };

        public abstract PinState forPin(Pin pin);
    }

}
