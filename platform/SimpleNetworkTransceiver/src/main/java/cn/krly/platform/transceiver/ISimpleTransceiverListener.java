package cn.krly.platform.transceiver;

import io.netty.channel.Channel;

public interface ISimpleTransceiverListener {
    void onChannelConnected(Channel channel, String host, int port);
    void onChannelClosed(Channel channel, String host, int port);
    void onChannelRead(Channel channel, byte[] message, String host, int port);
}
