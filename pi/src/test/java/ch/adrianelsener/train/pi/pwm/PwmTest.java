package ch.adrianelsener.train.pi.pwm;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import java.util.Scanner;

/**
 * Created by els on 08.04.15.
 */
public class PwmTest {
    private static int PIN_NUMBER = 7;

    public static void main(String[] args) throws Exception {
        // initialize wiringPi library, this is needed for PWM
        Gpio.wiringPiSetup();
        // softPwmCreate(int pin, int value, int range)
        // the range is set like (min=0 ; max=100)
        SoftPwm.softPwmCreate(PIN_NUMBER, 0, 100);
        int value = 50;
        String line = "";
        Scanner scan = new Scanner(System.in);
        while(!line.contains("e")) {
            System.out.println("Current is "+value);
            SoftPwm.softPwmWrite(PIN_NUMBER, value);
            line = scan.nextLine();
            if (line.contains("+")) {
                value += 5;
            } else if (line.contains("-")) {
                value -= 5;
            }
        }
//        int counter = 0;
//        while (counter < 3000) {
//
//            // fade LED to fully ON
//            for (int i = 0; i <= 100; i++) {
//                // softPwmWrite(int pin, int value)
//                // This updates the PWM value on the given pin. The value is
//                // checked to be in-range and pins
//                // that haven't previously been initialized via softPwmCreate
//                // will be silently ignored.
//                SoftPwm.softPwmWrite(PIN_NUMBER, i);
//                Thread.sleep(25);
//            }
//
//            // fade LED to fully OFF
//            for (int i = 100; i >= 0; i--) {
//                SoftPwm.softPwmWrite(PIN_NUMBER, i);
//                Thread.sleep(25);
//            }
//
//            counter++;
//        }
    }
}
