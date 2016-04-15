package org.lolhens.commons.network.disconnect;

import org.lolhens.commons.network.ProtocolProvider;

import java.io.IOException;

public class Quit extends DisconnectReason {
    public Quit(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, false, "Quit");
    }
}
