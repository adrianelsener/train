package ch.adrianelsener.train.pi;

/**
 * Created by els on 08.04.15.
 */
public class Foo {
    public static void main(String[] args) throws Exception {
        boolean huh = Integer.valueOf("15").byteValue() == (byte) 15;
        System.out.printf("%s", huh);
    }
}
