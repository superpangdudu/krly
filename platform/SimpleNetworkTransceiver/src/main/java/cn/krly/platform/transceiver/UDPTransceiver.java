package cn.krly.platform.transceiver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Created by Administrator on 2018/4/26.
 */
public class UDPTransceiver {
    private final String host;
    private final int port;
    private final ISimpleTransceiverListener listener;

    public UDPTransceiver(String host, int port, ISimpleTransceiverListener listener) {
        this.host = host;
        this.port = port;
        this.listener = listener;
    }

    public void start(EventLoopGroup workerGroup) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(workerGroup)
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                        byte[] data = new byte[msg.content().readableBytes()];
                        msg.content().readBytes(data);

                        if (listener == null)
                            return;
                        listener.onChannelRead(ctx.channel(), data, host, port);
                    }
                })
                .bind(host, port);
    }
}
