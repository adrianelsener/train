package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.common.net.NetAddress;
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
        final NetAddress address = mock(NetAddress.class);
        PiTwiSpeedBoardV1 testee = new PiTwiSpeedBoardV1(address);
        testee.setSpeed(14);

    }
}
