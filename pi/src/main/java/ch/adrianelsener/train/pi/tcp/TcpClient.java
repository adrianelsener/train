package ch.adrianelsener.train.pi.tcp;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;

public interface TcpClient {
    Result sendCommand(final Command cmd);
}
