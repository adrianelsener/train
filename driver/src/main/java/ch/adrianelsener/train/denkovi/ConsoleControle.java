package ch.adrianelsener.train.denkovi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.adrianelsener.train.denkovi.DenkoviWrapper.IpAddress;
import ch.adrianelsener.train.denkovi.DenkoviWrapper.Pin;

public class ConsoleControle implements Runnable {

    public ConsoleControle(Board denkoviWrapper) {
        board = denkoviWrapper;
    }

    public static void main(String[] args) {
        for (String string : args) {
            System.out.println(string);
        }

        new ConsoleControle(new DenkoviWrapper(IpAddress.fromValue("172.16.100.2"))).run();
    }

    private final Board board;

    @Override
    public void run() {
        while (true) {
            final ConsoleResult action = getTimesFromConsole();
            int amount = action.amount;
            board.set(Pin._15.off());
            switch (action.type) {
            case FAHRT:
                toggle(Pin._02, Math.abs(amount));
                break;
            case WEICHE:
                Weiche2RelaysMapping relaysMapping = Weiche2RelaysMapping.fromInput(amount);
                for (PinState relayState : relaysMapping.relayState) {
                    board.set(relayState);
                    sleep(50);
                }
                toggle(Pin._15, 1);
                break;
            case NOOP:
                System.out.println("NOOP");
                break;
            case EXIT:
                System.out.println("Beendet");
                System.exit(0);
            }
        }
    }

    private enum Weiche2RelaysMapping {
        W01_L(1, Pin._11.on(), Pin._12.on(), Pin._13.on(), Pin._14.on()), //
        W01_R(2, Pin._11.off(), Pin._12.on(), Pin._13.on(), Pin._14.on()), //
        W02_L(3, Pin._11.on(), Pin._12.off(), Pin._13.on(), Pin._14.on()), //
        W02_R(4, Pin._11.off(), Pin._12.off(), Pin._13.on(), Pin._14.on()), //
        W03_L(5, Pin._11.on(), Pin._12.on(), Pin._13.off(), Pin._14.on()), //
        W03_R(6, Pin._11.off(), Pin._12.on(), Pin._13.off(), Pin._14.on()), //
        W04_L(7, Pin._11.on(), Pin._12.off(), Pin._13.off(), Pin._14.on()), //
        W04_R(8, Pin._11.off(), Pin._12.off(), Pin._13.off(), Pin._14.on()), //

        NOOP(0)
        //
        ;
        private final int input;
        private final PinState[] relayState;

        private Weiche2RelaysMapping(int input, PinState... relayState) {
            this.input = input;
            this.relayState = relayState;
        }

        public static Weiche2RelaysMapping fromInput(int input) {
            for (Weiche2RelaysMapping w2rm : Weiche2RelaysMapping.values()) {
                if (w2rm.input == input) {
                    return w2rm;
                }
            }
            return NOOP;
        }
    }

    private void toggle(Pin pin, int times) {
        for (int i = 0; i < times; i++) {
            board.set(pin.on());
            sleep(50);
            board.set(pin.off());
            sleep(50);
        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            System.err.println("Kein korrekter Sleep");
            e.printStackTrace(System.err);
        }

    }

    private static class ConsoleResult {
        final int amount;
        final Type type;

        public ConsoleResult(Type type, int amount) {
            this.amount = amount;
            this.type = type;
        }
    }

    private ConsoleResult getTimesFromConsole() {
        System.out.print("[W]eiche, [S]peed, [E]xit: ");
        InputStreamReader streamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String input;
        try {
            input = bufferedReader.readLine();
            String charAt = String.valueOf(input.charAt(0));
            final Type typ = Type.fromSteuerzeichen(charAt);
            final int amount;
            if (input.length() > 1) {
                amount = Integer.parseInt(input.substring(1));
            } else {
                amount = 0;
            }
            return new ConsoleResult(typ, amount);
        } catch (IOException e) {
            return new ConsoleResult(Type.NOOP, 0);
        }
    }

    private enum Type {
        WEICHE("W"), //
        FAHRT("S"), //
        EXIT("E"), //
        NOOP("N");
        private final String steuerzeichen;

        private Type(String steuerzeichen) {
            this.steuerzeichen = steuerzeichen;
        }

        public static Type fromSteuerzeichen(String steuerzeichen) {
            if (null != steuerzeichen) {
                for (Type typ : Type.values()) {
                    if (typ.steuerzeichen.equals(steuerzeichen.toUpperCase())) {
                        return typ;
                    }
                }
            }
            return NOOP;
        }
    }
}
