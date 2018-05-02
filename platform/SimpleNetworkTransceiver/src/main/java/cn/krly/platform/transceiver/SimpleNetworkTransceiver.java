package cn.krly.platform.transceiver;

import cn.krly.platform.transceiver.api.ISimpleNetworkEventListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;


public class SimpleNetworkTransceiver implements ISimpleTransceiverListener {
    private ISimpleNetworkEventListener listener;
    private String host;
    private int port;

    public SimpleNetworkTransceiver(String host, int port, ISimpleNetworkEventListener listener) {
        this.host = host;
        this.port = port;
        this.listener = listener;
    }

    public void start() {
        TCPTransceiver tcpTransceiver = new TCPTransceiver(host, port, this);
        UDPTransceiver udpTransceiver = new UDPTransceiver(host, port, this);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        udpTransceiver.start(workerGroup);
        tcpTransceiver.start(bossGroup, workerGroup);
    }

    //===================================================================================
    @Override
    public void onChannelConnected(Channel channel, String host, int port) {
        //if (listener == null)
        //    return;
        //listener.onChannelConnected(channel, host, port);
    }

    @Override
    public void onChannelClosed(Channel channel, String host, int port) {
        if (listener == null)
            return;

        Integer token = ChannelHolder.getInstance().getToken(channel);
        if (token == null)
            return;

        listener.onDisconnected(token, host, port);
    }

    @Override
    public void onChannelRead(Channel channel, byte[] data, String host, int port) {
        if (data == null || data.length < 4)
            return;

        int token = (data[0] & 0xFF) << 24;
        token += (data[1] & 0xFF) << 16;
        token += (data[2] & 0xFF) << 8;
        token += data[3] & 0xFF;

        // FIXME
        /*
        * A workaround here to simplify the server implementation.
        * For a distributed TCP connection management, it always works behind a load balancer,
        * we have no idea to assume which connection goes on a specified server, it determined by
        * the balancer (e.g. SLB). So a monitor is needed to keep the mapping between the server and token which
        * stands for an individual device. But we also can't assume the monitor always be fine, so here to use
        * ZooKeeper as a register.
        * A monitor watches the status of all connection management servers.
        *
        * */

        // Command message from AbstractDevice
        if (token == 0) {
            // TODO
            if (data.length <= 8) // token + token + data
                return;

            token = token = (data[4] & 0xFF) << 24;
            token += (data[5] & 0xFF) << 16;
            token += (data[6] & 0xFF) << 8;
            token += data[7] & 0xFF;

            byte[] msg = new byte[data.length - 8];
            for (int pos = 8; pos < data.length; pos++)
                msg[pos - 8] = data[pos];

            write(token, msg);

            return;
        }

        //
        ChannelHolder.getInstance().addChannel(token, channel);

        if (listener == null)
            return;
        listener.onRead(token, data, host, port);
    }

    //===================================================================================
    private void write(int token, byte[] message) {
        Channel channel = ChannelHolder.getInstance().getChannel(token);
        if (channel == null)
            return;

        //
        ByteBuf buf = Unpooled.buffer(message.length);
        buf.writeBytes(message);

        channel.writeAndFlush(buf);
    }
}
