package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.pi.tcp.TcpClient;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class PiTwiSpeedBoardV1Test {
    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setSpeed() {
        final TcpClient tcpClient = mock(TcpClient.class);
        PiTwiSpeedBoardV1 testee = new PiTwiSpeedBoardV1(tcpClient);
        testee.setSpeed(14);

    }
}
