package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.gui.swing.events.*;
import com.google.common.eventbus.EventBus;

import java.awt.event.MouseEvent;

enum DrawMode {
    Switch() {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createSwitch(e.getPoint()));
        }

        @Override
        public void doWhileMouseIsDragged(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createSwitch(e.getPoint()));
        }

        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(PartCreationAction.createSwitch(e.getPoint()));
        }
    }, //
    Track() {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(UpdatePoint.createCreationStartPoint(e.getPoint()));
        }

        @Override
        public void doWhileMouseIsDragged(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createTrack(e.getPoint()));
        }

        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(PartCreationAction.createTrack(e.getPoint()));
        }
    }, //
    NoOp {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(UpdatePart.createToggle(e.getPoint()));
        }
    }, //
    Rotate {
        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(UpdatePart.createMirror(e.getPoint()));
        }
    }, //
    Toggle {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(UpdatePart.createToggle(e.getPoint()));
        }
    }, //
    Move() {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(UpdatePoint.createCreationStartPoint(e.getPoint()));
        }

        @Override
        public void doWhileMouseIsDragged(EventBus bus, MouseEvent e) {
            bus.post(UpdateMoveDraftPart.create(e.getPoint()));
        }

        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(UpdatePart.movePart(e.getPoint()));
        }
    }, //
    Delete {
        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(UpdatePart.deletePart(e.getPoint()));
        }
    }, //
    Detail {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(UpdatePoint.createDetailCoordinates(e.getPoint()));
        }
    }, //
    DummySwitch() {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createDummySwitch(e.getPoint()));
        }

        @Override
        public void doWhileMouseIsDragged(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createDummySwitch(e.getPoint()));
        }

        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(PartCreationAction.createDummySwitch(e.getPoint()));
        }
    },//
    SwitchTrack() {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(UpdatePoint.createCreationStartPoint(e.getPoint()));
        }

        @Override
        public void doWhileMouseIsDragged(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createSwitchTrack(e.getPoint()));
        }

        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(PartCreationAction.createSwitchTrack(e.getPoint()));
        }
    },//
    PowerTrack() {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(UpdatePoint.createCreationStartPoint(e.getPoint()));
        }

        @Override
        public void doWhileMouseIsDragged(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createPowerTrack(e.getPoint()));
        }

        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(PartCreationAction.createPowerTrack(e.getPoint()));
        }
    },//
    TripleSwitch() {
        @Override
        public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createTripleSwitch(e.getPoint()));
        }

        @Override
        public void doWhileMouseIsDragged(EventBus bus, MouseEvent e) {
            bus.post(DraftPartCreationAction.createTripleSwitch(e.getPoint()));
        }

        @Override
        public void doWhileMouseReleased(EventBus bus, MouseEvent e) {
            bus.post(PartCreationAction.createTripleSwitch(e.getPoint()));
        }
    };

    DrawMode() {

    }

    public void doWhileInitialButtonIsPressed(EventBus bus, MouseEvent e){
        // noop
    }

    public void doWhileMouseIsDragged(EventBus bus, MouseEvent e){
        // noop
    }

    public void doWhileMouseReleased(EventBus bus, MouseEvent e){
        // noop
    }

    @Override
    public String toString() {
        return name();
    }

}