package com.krly.project.batterymanagement.batteryserver;

import cn.krly.project.batterymanagement.protocol.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 充电台工作协议处理服务器
 * 负责所有充电台上报信息的解析
 */
public class BatteryServer {
    public interface BatteryServerResponseListener {
        void onChannelRegistered(ChannelHandlerContext ctx, String id);
        void onChannelUnregistered(ChannelHandlerContext ctx);

        void onBatteryContainerCheckSecretResponse(String id, ClientCheckSecretResponse response);
        void onBatteryContainerRentResponse(String id, ClientBatteryRentResponse response);
        void onBatteryContainerGetRandomResponse(String id, int transactionId, byte[] random);
    }

    private List<BatteryServerResponseListener> listeners = new ArrayList<>();
    private IBatteryService batteryService;

    public synchronized void addListener(BatteryServerResponseListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(BatteryServerResponseListener listener) {
        listeners.remove(listener);
    }

    public void setBatteryService(IBatteryService batteryService) {
        this.batteryService = batteryService;
    }

    //===================================================================================
    private String host;
    private int port;

    public BatteryServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    protected void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline().addLast(new ProtocolDecoder());

                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                onConnected(ctx);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                onClosed(ctx);
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                onRead(ctx, msg);
                            }
                        });
                    }
                });

        ChannelFuture future = bootstrap.bind(host, port).sync();
        future.channel().closeFuture().sync();
    }

    //===================================================================================
    private void writeMessage(ChannelHandlerContext ctx, Message message) {
        byte[] data = message.getData();

        ByteBuf msg = Unpooled.buffer(data.length);
        msg.writeBytes(data);

        ctx.writeAndFlush(msg);
    }

    private long getBatteryId(byte[] data) {
        return ((data[0] & 0xFF) << 24) & 0xFFFFFFFF
                + (data[1] & 0xFF) << 16
                + (data[2] & 0xFF) << 8
                + (data[3] & 0xFF);
    }

    //===================================================================================
    private void onConnected(ChannelHandlerContext ctx) {
        // Request for device information when it connected

        // 当充电台连接时，立即发送设备信息获取请求，完成注册
        ServerQueryDeviceVersionRequest request = MessageFactory.newQueryDeviceVersionRequest();
        writeMessage(ctx, request);
    }

    private void onClosed(ChannelHandlerContext ctx) {
        String id = ChannelContextHolder.getInstance().getIdByContext(ctx);

        for (BatteryServerResponseListener listener : listeners)
            listener.onChannelUnregistered(ctx);

        if (batteryService != null)
            batteryService.onBatteryContainerUnregistered(id);
    }

    private void onRead(ChannelHandlerContext ctx, ByteBuf msg) {
        int readableBytes = msg.readableBytes();
        byte[] data = new byte[readableBytes];

        msg.readBytes(data);

        //
        Message message = MessageFactory.newMessage(data);
        if (message == null)
            return;

        handleMessage(ctx, message);
    }

    //===================================================================================
    // TODO to be implemented according to OCP
    private void handleMessage(ChannelHandlerContext ctx, Message message) {
        String deviceId = ChannelContextHolder.getInstance().getIdByContext(ctx);

        switch (message.getId()) {
            case Message.MSG_ID_CLIENT_HEARTBEAT: { // 心跳请求
                onHeartBeat(ctx);
                break;
            }
            case Message.MSG_ID_CLIENT_SYNC_TIME_REQUEST: { // 时钟同步请求
                onSyncTimeRequest(ctx);
                break;
            }
            case Message.MSG_ID_CLIENT_QUERY_VERSION_RESPONSE: { // 获取充电台信息应答
                ClientQueryDeviceVersionResponse response = (ClientQueryDeviceVersionResponse) message;
                onQueryVersionResponse(ctx, response);
                break;
            }
            case Message.MSG_ID_CLIENT_QUERY_STATUS_RESPONSE: { // 获取充电台状态应答
                ClientQueryDeviceStatusResponse response = (ClientQueryDeviceStatusResponse) message;
                onQueryStatusResponse(response);
                break;
            }
            case Message.MSG_ID_CLIENT_GET_RANDOM_KEY_RESPONSE: { // 获取随机码应答
                ClientGetRandomKeyResponse response = (ClientGetRandomKeyResponse) message;
                onBatteryContainerUpdateRandom(deviceId, response.getTransactionId(), response.getRandomKey());
                break;
            }
            case Message.MSG_ID_CLIENT_UPDATE_RANDOM_REQUEST: { // 充电台随机码提交请求
                ClientUpdateRandomRequest request = (ClientUpdateRandomRequest) message;
                onBatteryContainerUpdateRandom(deviceId, request.getTransactionId(), request.getRandomKey());

                // workaround: response here directly
                ServerUpdateRandomResponse response =
                        MessageFactory.newServerUpdateRandomResponse(request.getTransactionId());
                writeMessage(ctx, response);

                break;
            }
            case Message.MSG_ID_CLIENT_RENT_RESPONSE: { // 电池借出应答
                ClientBatteryRentResponse response = (ClientBatteryRentResponse) message;
                onBatteryContainerRentResponse(ctx, deviceId, response);
                break;
            }
            case Message.MSG_ID_CLIENT_RETURN_REQUEST: { // 电池归还请求
                ClientBatteryReturnRequest request = (ClientBatteryReturnRequest) message;
                onBatteryContainerReturnRequest(ctx, deviceId, request);
                break;
            }
            case Message.MSG_ID_CLIENT_CHECK_SECRET_REQUEST: { // 充电台主动验证秘钥请求
                ClientCheckSecretRequest request = (ClientCheckSecretRequest) message;
                onBatteryCheckSecretRequest(ctx, deviceId, request);
                break;
            }
            case Message.MSG_ID_CLIENT_CHECK_SECRET_RESPONSE: { // 充电台验证秘钥应答
                ClientCheckSecretResponse response = (ClientCheckSecretResponse) message;
                onBatteryCheckSecretResponse(deviceId, response);
                break;
            }
        }
    }

    private void onHeartBeat(ChannelHandlerContext ctx) {
        ServerHeartBeatResponse response = MessageFactory.newHeartBeatResponse();
        writeMessage(ctx, response);

        //
        if (batteryService == null)
            return;

        String id = ChannelContextHolder.getInstance().getIdByContext(ctx);
        if (id == null)
            return;

        batteryService.onBatteryContainerHeartBeat(id);
    }

    private void onSyncTimeRequest(ChannelHandlerContext ctx) {
        ServerSyncTimeResponse response = MessageFactory.newSyncTimeResponse(System.currentTimeMillis());
        writeMessage(ctx, response);
    }

    private void onQueryVersionResponse(ChannelHandlerContext ctx,
                                        ClientQueryDeviceVersionResponse response) {
        ServerQueryDeviceStatusRequest request = MessageFactory.newQueryDeviceInfoRequest();
        writeMessage(ctx, request);

        //
        for (BatteryServerResponseListener listener : listeners)
            listener.onChannelRegistered(ctx, response.getIMEI());

        //
        if (batteryService == null)
            return;

        BatteryContainerInfo info = new BatteryContainerInfo();

        //
        info.setLocalHost(host + ":" + port);

        //
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        info.setRemoteHost(address.getHostName() + ":" + address.getPort());

        //
        info.setFirmwareVersion(response.getFirmwareVersion());
        info.setHardwareVersion(response.getHardwareVersion());
        info.setIMEI(response.getIMEI());
        info.setICCID(response.getICCID());
        info.setMac(response.getMAC());
        info.setStatus(0x01);

        //
        batteryService.onBatteryContainerInfoUpdate(info);
    }

    private void onQueryStatusResponse(ClientQueryDeviceStatusResponse response) {
        if (batteryService == null)
            return;

        ClientQueryDeviceStatusResponse.Battery[] batteries = response.getBatteries();
        BatteryInfo[] batteryInfoArray = new BatteryInfo[batteries.length];

        String containerId = response.getDeviceId();

        //
        for (int n = 0; n < batteries.length; n++) {
            BatteryInfo info = new BatteryInfo();
            info.setContainerId(containerId);

            batteryInfoArray[n] = info;

            //
            ClientQueryDeviceStatusResponse.Battery battery = batteries[0];
            if (battery == null)
                continue;

            //
            info.setId(battery.id);
            info.setStatus(battery.status);
            info.setStatus(battery.slot);
        }

        //
        batteryService.onBatteryContainerStatusUpdate(containerId, batteryInfoArray);
    }

    private void onBatteryContainerUpdateRandom(String id, int transactionId, byte[] random) {
        for (BatteryServerResponseListener listener : listeners)
            listener.onBatteryContainerGetRandomResponse(id, transactionId, random);

        //
        if (batteryService == null)
            return;

        batteryService.onBatteryContainerUpdateRandom(id, transactionId, random);
    }

    private void onBatteryContainerRentResponse(ChannelHandlerContext ctx,
                                                String id,
                                                ClientBatteryRentResponse response) {
        ServerBatteryRentConfirmResponse confirmResponse =
                MessageFactory.newServerBatteryRentConfirmResponse(response.getTransactionId(), response.getBatteryId());
        writeMessage(ctx, confirmResponse);

        //
        for (BatteryServerResponseListener listener : listeners)
            listener.onBatteryContainerRentResponse(id, response);

        //
        if (batteryService == null)
            return;

        batteryService.onBatteryContainerRent(id,
                response.getResult(),
                getBatteryId(response.getBatteryId()),
                response.getSlot(),
                response.getTimestamp());
    }

    private void onBatteryContainerReturnRequest(ChannelHandlerContext ctx,
                                                 String id,
                                                 ClientBatteryReturnRequest request) {
        ServerBatteryReturnResponse response =
                MessageFactory.newServerBatteryReturnResponse(request.getTransactionId(),
                        request.getBatteryId(),
                        (byte) 0x01);
        writeMessage(ctx, response);

        //
        if (batteryService == null)
            return;

        batteryService.onBatteryContainerReturned(id,
                request.getResult(),
                getBatteryId(request.getBatteryId()),
                request.getSlot(),
                request.getTimestamp());
    }

    private void onBatteryCheckSecretRequest(ChannelHandlerContext ctx,
                                             String id,
                                             ClientCheckSecretRequest request) {
        if (batteryService == null)
            return;

        byte[] secret = batteryService.getBatterContainerSecret(id);
        byte[] data = request.getSecret();

        boolean isPassed = true;
        try {
            for (int n = 0; n < 8; n++) {
                if (secret[n] != data[n]) {
                    isPassed = false;
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        byte status = isPassed ? (byte) 0x01 : 0;
        ServerCheckSecretResponse response =
                MessageFactory.newServerCheckSecretResponse(request.getTransactionId(), status);
        writeMessage(ctx, response);
    }

    private void onBatteryCheckSecretResponse(String id, ClientCheckSecretResponse response) {
        for (BatteryServerResponseListener listener : listeners)
            listener.onBatteryContainerCheckSecretResponse(id, response);

        //
        if (batteryService == null)
            return;

        batteryService.onBatteryContainerCheckSecret(id, response.getTransactionId(), response.getIsPassed());
    }
}
