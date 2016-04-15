package org.lolhens.commons.network;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IHandlerConnect<P> {
    void onConnect(AbstractClient<P> client);
}
