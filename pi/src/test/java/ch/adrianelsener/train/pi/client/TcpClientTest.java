package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.pi.dto.Command;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class TcpClientTest {

    @Test
    public void sendCommand_overClient() {
        final SocketFactory socketFacktry = mock(SocketFactory.class);
        final TcpClient testee = new TcpClient(socketFacktry);
        // act
        Command cmd = mock(Command.class);
        testee.sendCommand(cmd);
    }
}