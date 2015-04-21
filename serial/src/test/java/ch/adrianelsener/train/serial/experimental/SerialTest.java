package ch.adrianelsener.train.serial.experimental;

import org.testng.annotations.Test;

public class SerialTest {
    @Test
    public void communicate() {
        final Baud baud = Baud.create(2000);
        final SerialSender sender = new SerialSender();
        final SerialReceiver receiver = new SerialReceiver();
        final Medium connection = new DummyConnection();
        sender.connect(connection, baud);
        receiver.connect(connection, baud);
        byte data = 122;
        sender.send(data);
        byte result = receiver.getData();
//        assertThat(result, is(equalTo(data)));
    }

    @Test
    public void foo() throws InterruptedException {
        int b = 0b00000001;
        Integer x = b;
        int i = x << 1;
        System.out.printf("%o\n", b);
        System.out.printf("foooo\n");
        System.out.printf("%s", i);
        Thread.sleep(200);
    }
}