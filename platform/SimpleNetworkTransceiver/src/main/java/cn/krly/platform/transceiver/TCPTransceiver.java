package cn.krly.platform.transceiver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Administrator on 2018/4/26.
 */
public class TCPTransceiver {
    private final String host;
    private final int port;
    private final ISimpleTransceiverListener listener;

    public TCPTransceiver(String host, int port, ISimpleTransceiverListener listener) {
        this.host = host;
        this.port = port;
        this.listener = listener;
    }

    public void start(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("channelActive: " + ctx);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("channelInactive: " + ctx);

                                if (listener == null)
                                    return;
                                listener.onChannelClosed(ctx.channel(), host, port);
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                if (listener == null)
                                    return;

                                int bytes = msg.readableBytes();
                                if (bytes < 4) // the token takes 4 bytes
                                    return;

                                byte[] data = new byte[bytes];
                                msg.readBytes(data);

                                //
                                listener.onChannelRead(ctx.channel(), data, host, port);
                            }
                        });

                    }
                });

        ChannelFuture future = bootstrap.bind(port);
    }
}
