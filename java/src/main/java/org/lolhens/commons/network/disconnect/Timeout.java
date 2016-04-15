package org.lolhens.commons.network.disconnect;

import org.lolhens.commons.network.ProtocolProvider;

import java.io.IOException;

public class Timeout extends DisconnectReason {
    public Timeout(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, true, "Timeout");
    }
}
