package com.krly.project.batterymanagement.dispatcher;

import cn.krly.project.batterymanagement.protocol.ClientGetServerRequest;
import cn.krly.project.batterymanagement.protocol.ServerGetServerResponse;
import cn.krly.project.batterymanagement.protocol.Message;
import cn.krly.project.batterymanagement.protocol.MessageFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 分派服务器
 */
class DispatcherServer {

    private String host = "";
    private int port = 0;

    DispatcherServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {

                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                int readableBytes = msg.readableBytes();
                                byte[] data = new byte[readableBytes];

                                msg.readBytes(data);

                                // 仅处理服务器获取请求
                                Message message = MessageFactory.newMessage(data);
                                if (message == null
                                        || message.getId() != Message.MSG_ID_GET_SERVER_REQUEST) {
                                    ctx.close();
                                    return;
                                }

                                ClientGetServerRequest request = (ClientGetServerRequest) message;

                                OnlineServerInfoManagement.OnlineServerInfo serverInfo =
                                        OnlineServerInfoManagement.getInstance().getOnlineServerInfo();

                                //
                                ServerGetServerResponse response =
                                        MessageFactory.newGetServerResponse(serverInfo.getHost(), serverInfo.getPort());
                                data = response.getData();

                                ByteBuf buf = Unpooled.buffer(data.length);
                                buf.writeBytes(data);

                                ctx.writeAndFlush(buf);
                            }
                        });
                    }
                });

        ChannelFuture future = bootstrap.bind(host, port).sync();
        future.channel().closeFuture().sync();
    }
}
