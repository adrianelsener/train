package ch.adrianelsener.train.denkovi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.adrianelsener.train.denkovi.Pin;

public class DenkoviTimesUpDownConsole {

    public static void main(String[] args) {
        new DenkoviTimesUpDownConsole().run();
    }

    private final Board board = new DenkoviWrapper(IpAddress.fromValue("172.16.100.2"));

    public void run() {
        Pin releay = Pin._02;
        while (true) {
            final int times = getTimesFromConsole();
            if (times == 0) {
                System.out.println("Beendet");
                return;
            }
            if (times > 0) {
                set(Pin._01.on());
            } else {
                set(Pin._01.off());
            }
            toggle(releay, Math.abs(times));
        }
    }

    private void set(PinState relay) {
        board.set(relay);
    }

    private void toggle(Pin relay, int times) {
        for (int i = 0; i < times; i++) {
            board.set(relay.on());
            sleep(50);
            board.set(relay.off());
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

    private int getTimesFromConsole() {
        System.out.print("Anzahl toggles: ");
        InputStreamReader streamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String times;
        try {
            times = bufferedReader.readLine();
            return Integer.parseInt(times);
        } catch (IOException e) {
            return 0;
        }
    }

}
