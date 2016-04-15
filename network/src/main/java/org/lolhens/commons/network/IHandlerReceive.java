package org.lolhens.commons.network;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IHandlerReceive<P> {
    void onReceive(AbstractClient<P> client, P packet);
}
