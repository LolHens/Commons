package org.lolhens.commons.network;

/**
 * Created by LolHens on 28.12.2014.
 */
public interface IHandlerAccept<P> {
    void onAccept(AbstractClient<P> client);
}
