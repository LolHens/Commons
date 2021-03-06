package org.lolhens.commons.network;

import org.lolhens.commons.network.disconnect.DisconnectReason;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by LolHens on 15.11.2014.
 */
public abstract class AbstractExceptionHandler implements Closeable {
    protected ProtocolProvider<?> protocolProvider;

    protected abstract void handle(IOException exception);

    protected final void onDisconnect(DisconnectReason reason) {
        protocolProvider.onDisconnect(reason);
    }

    @Override
    public final void close() {
        try {
            protocolProvider.setClosed();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
