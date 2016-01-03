package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.common.net.AutoClosableSocket;
import ch.adrianelsener.train.common.net.NetAddress;
import ch.adrianelsener.train.common.net.SocketFactory;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import org.hamcrest.Matcher;
import org.hamcrest.object.IsCompatibleType;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class TcpGsonClientTest {
    @Mock
    private GsonWrapper gson;
    @Mock
    private SocketFactory socketFactory;
    @Mock
    private NetAddress netAddress;
    @Mock
    private Result resultDto;

    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        initSocketFactory(socketFactory);
        initGson(gson);
    }

    private void initGson(GsonWrapper gson) {
        final Matcher<Class<Object>> isOfTypeResult = new IsCompatibleType(Result.class);
        when(gson.fromJson(any(InputStream.class),
                argThat(isOfTypeResult))).thenReturn(resultDto);
    }

    private void initSocketFactory(SocketFactory socketFactory) {
        final OutputStream outputStream = mock(OutputStream.class);
        final AutoClosableSocket socket = mock(AutoClosableSocket.class);
        when(socket.getOutputStream()).thenReturn(outputStream);
        final InputStream inputStream = mock(InputStream.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socketFactory.create(any(NetAddress.class))).thenReturn(socket);
    }

    @Test
    public void sendCommand_overClient() {
        final TcpGsonClient testee = new TcpGsonClient(netAddress, socketFactory, gson);
        final Command cmd = Command.builder().build();
        // act
        final Result result = testee.sendCommand(cmd);
        // assert
        assertThat(result, sameInstance(resultDto));
    }
}