package org.lolhens.commons.network;

import org.lolhens.commons.network.disconnect.DisconnectReason;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IHandlerDisconnect<P> {
    void onDisconnect(ProtocolProvider<P> protocolProvider, DisconnectReason disconnectReason);
}
