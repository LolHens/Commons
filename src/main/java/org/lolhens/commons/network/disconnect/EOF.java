package org.lolhens.commons.network.disconnect;

import org.lolhens.commons.network.ProtocolProvider;

import java.io.IOException;

public class EOF extends DisconnectReason {
    public EOF(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, true, "End of Stream");
    }
}
