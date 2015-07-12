package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.pi.dto.Command;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TcpClientTest {

    @Test
    public void sendCommand_overClient() {
        final OutputStream outputStream = mock(OutputStream.class);
        final AutoClosableSocket socket = mock(AutoClosableSocket.class);
        when(socket.getOutputStream()).thenReturn(outputStream);
        final InputStream inputStream = mock(InputStream.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        final SocketFactory socketFacktry = when(mock(SocketFactory.class).create(anyString(), anyInt())).thenReturn(socket).getMock();
        final TcpClient testee = new TcpClient(socketFacktry, mock(GsonWrapper.class));
        // act
        Command cmd = Command.builder().build();
        testee.sendCommand(cmd);
    }
}