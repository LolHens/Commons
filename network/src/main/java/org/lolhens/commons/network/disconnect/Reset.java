package org.lolhens.commons.network.disconnect;

import org.lolhens.commons.network.ProtocolProvider;

import java.io.IOException;

public class Reset extends DisconnectReason {
    public Reset(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, true, "Connection Reset");
    }
}
