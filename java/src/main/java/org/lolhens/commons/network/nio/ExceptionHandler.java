package org.lolhens.commons.network.nio;

import org.lolhens.commons.network.AbstractExceptionHandler;
import org.lolhens.commons.network.disconnect.EOF;
import org.lolhens.commons.network.disconnect.Refused;
import org.lolhens.commons.network.disconnect.Reset;
import org.lolhens.commons.network.disconnect.Timeout;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by LolHens on 15.11.2014.
 */
public class ExceptionHandler extends AbstractExceptionHandler {
    @Override
    public void handle(IOException exception) {
        close();
        if (exception instanceof EOFException) {
            onDisconnect(new EOF(protocolProvider, exception));
        } else if (exception instanceof SocketTimeoutException) {
            onDisconnect(new Timeout(protocolProvider, exception));
        } else if (exception instanceof ConnectException) {
            onDisconnect(new Refused(protocolProvider, exception));
        } else {
            onDisconnect(new Reset(protocolProvider, exception));
        }
    }
}
