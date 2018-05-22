package com.krly.project.batterymanagement.batteryserver;

import cn.krly.project.batterymanagement.protocol.*;
import cn.krly.utility.common.SerializeUtils;
import com.krly.project.batterymanagement.command.RentCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/5/17.
 */
public class LocalCommandServer implements BatteryServer.BatteryServerResponseListener {
    private String host;
    private int port;

    public LocalCommandServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(workerGroup)
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                        byte[] data = new byte[msg.content().readableBytes()];
                        msg.content().readBytes(data);
                        //
                        try {
                            onCommand(data);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .bind(host, port);
    }

    //===================================================================================
    private void onCommand(byte[] data) throws Exception {
        RentCommand cmd = SerializeUtils.deserialize(data);
        if (cmd == null)
            return;

        int ret = doRent(cmd);
        // TODO handle the command result
        cmd.setResult(ret);

        byte[] cmdData = SerializeUtils.serialize(cmd);
        if (cmdData == null)
            return;


    }

    private int doRent(RentCommand cmd) throws Exception {
        // TODO do the following in thread pool
        ChannelHandlerContext ctx = ChannelContextHolder.getInstance().getContextById(cmd.getDeviceId());
        if (ctx == null)
            return -1;

        String objectKey = cmd.getDeviceId() + ":" + cmd.getTransactionId();

        //
        ServerGetRandomKeyRequest getRandomKeyRequest = MessageFactory.newGetRandomKeyRequest(cmd.getTransactionId());
        GetRandomResult randomResult = new GetRandomResult();

        randomResult = getResult(ctx, getRandomKeyRequest, objectKey, randomResult);
        if (randomResult.isDone == false)
            return -2;

        //
        ServerCheckSecretRequest checkSecretRequest = MessageFactory.newCheckSecretRequest(cmd.getTransactionId(),
                randomResult.random,
                cmd.getMac());
        CheckSecretResult checkSecretResult = new CheckSecretResult();

        checkSecretResult = getResult(ctx, checkSecretRequest, objectKey, checkSecretResult);
        if (checkSecretResult.isDone == false)
            return -3;
        if (checkSecretResult.isPassed == false)
            return -4;

        //
        ServerBatteryRentRequest rentRequest = MessageFactory.newServerBatteryRentRequest(cmd.getTransactionId());
        RentResult rentResult = new RentResult();

        rentResult = getResult(ctx, rentRequest, objectKey, rentResult);
        if (rentResult.isDone == false)
            return -5;

        return rentResult.status;
    }

    private void writeMessage(ChannelHandlerContext ctx, Message message) {
        byte[] data = message.getData();

        ByteBuf msg = Unpooled.buffer(data.length);
        msg.writeBytes(data);

        ctx.writeAndFlush(msg);
    }

    private <T> T getResult(ChannelHandlerContext ctx, Message message, String key, T result) throws Exception {
        synchronized (result) {
            syncObjectMap.put(key, result);
            writeMessage(ctx, message);

            result.wait(2000);
            syncObjectMap.remove(key);

            return result;
        }
    }

    //===================================================================================
    private static class GetRandomResult {
        boolean isDone;
        byte[] random;
    }

    private static class CheckSecretResult {
        boolean isDone;
        boolean isPassed;
    }

    private static class RentResult {
        boolean isDone;
        int status;
    }

    private Map<String, Object> syncObjectMap = new ConcurrentHashMap<>();

    //===================================================================================
    @Override
    public void onChannelRegistered(ChannelHandlerContext ctx, String id) {
    }

    @Override
    public void onChannelUnregistered(ChannelHandlerContext ctx) {
    }

    @Override
    public void onBatteryContainerCheckSecretResponse(String id, ClientCheckSecretResponse response) {
        String objectKey = id + ":" + response.getTransactionId();
        Object object = syncObjectMap.remove(objectKey);
        if (object == null)
            return;

        if (object instanceof CheckSecretResult == false)
            return;

        CheckSecretResult result = (CheckSecretResult) object;
        synchronized (result) {
            result.isDone = true;
            result.isPassed = response.getIsPassed();

            result.notify();
        }
    }

    @Override
    public void onBatteryContainerRentResponse(String id, ClientBatteryRentResponse response) {
        String objectKey = id + ":" + response.getTransactionId();
        Object object = syncObjectMap.remove(objectKey);
        if (object == null)
            return;

        if (object instanceof RentResult == false)
            return;

        RentResult result = (RentResult) object;
        synchronized (result) {
            result.isDone = true;
            result.status = response.getResult();

            result.notify();
        }
    }

    @Override
    public void onBatteryContainerGetRandomResponse(String id, int transactionId, byte[] random) {
        String objectKey = id + ":" + transactionId;
        Object object = syncObjectMap.remove(objectKey);
        if (object == null)
            return;

        if (object instanceof GetRandomResult == false)
            return;

        GetRandomResult result = (GetRandomResult) object;
        synchronized (result) {
            result.isDone = true;
            result.random = random;

            result.notify();
        }
    }
}
