package org.lolhens.commons.network.nio;

import org.lolhens.commons.network.AbstractClient;
import org.lolhens.commons.network.AbstractProtocol;
import org.lolhens.commons.network.SelectionKeyContainer;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Client<P> extends AbstractClient<P> {
    private volatile SelectionKeyContainer selectionKeyContainer;

    public Client(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
        setExceptionHandler(new ExceptionHandler());
    }

    private final int finishConnect() {
        try {
            getSocketChannel().finishConnect();
            getSocketChannel().setOption(StandardSocketOptions.TCP_NODELAY, true);
        } catch (IOException e) {
            onException(e);
        }
        return SelectionKey.OP_CONNECT | SelectionKey.OP_READ;
    }

    @Override
    public void connect(SocketAddress socketAddress) throws IOException {
        setSocketChannel(SocketChannel.open());

        try {
            getSocketChannel().connect(socketAddress);
        } catch (IOException e) {
            onException(e);
        }
    }

    // Setters

    public void setSocketChannel(SocketChannel socketChannel) throws IOException {
        super.setSocketChannel(socketChannel);

        int ops = SelectionKey.OP_CONNECT;
        if (socketChannel.isConnected()) ops ^= finishConnect();

        if (selectionKeyContainer != null) selectionKeyContainer.cancel();

        selectionKeyContainer = getSelectorThread().register(socketChannel, ops, (selectionKeyContainer, readyOps) -> {
            if (!isAlive()) return;

            if ((readyOps & SelectionKey.OP_READ) != 0) {
                read(socketChannel);
            }
            if ((readyOps & SelectionKey.OP_WRITE) != 0) {
                write(socketChannel);
            }
            if ((readyOps & SelectionKey.OP_CONNECT) != 0) {
                selectionKeyContainer.toggleInterestOps(finishConnect());
                onConnect();
            }
        });

        if ((ops & SelectionKey.OP_CONNECT) == 0) onConnect();
    }

    @Override
    protected void setWriting(boolean writing) {
        synchronized (this) {
            if (!selectionKeyContainer.isValid()) return;

            if (isWriting() != writing) {
                super.setWriting(writing);
                selectionKeyContainer.setInterestOps(writing ? SelectionKey.OP_WRITE : 0, SelectionKey.OP_WRITE);
            }
        }
    }

    @Override
    protected void setClosed() throws IOException {
        super.setClosed();
        while (isWriting()) {
            try {
                synchronized (this) {
                    this.wait(100);
                }
            } catch (InterruptedException e) {
            }
        }
        selectionKeyContainer.cancel();
    }

    // Getters
}
