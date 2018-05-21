package com.krly.project.batterymanagement.batteryserver;

import cn.krly.project.batterymanagement.protocol.ClientBatteryRentResponse;
import cn.krly.project.batterymanagement.protocol.ClientCheckSecretResponse;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ChannelContextHolder implements BatteryServer.BatteryServerResponseListener {

    @Override
    public void onChannelRegistered(ChannelHandlerContext ctx, String id) {
        contextIdMap.put(ctx, id);
        idContextMap.put(id, ctx);
    }

    @Override
    public void onChannelUnregistered(ChannelHandlerContext ctx) {
        String id = getIdByContext(ctx);

        contextIdMap.remove(ctx);
        idContextMap.remove(id);
    }

    @Override
    public void onBatteryContainerCheckSecretResponse(String id, ClientCheckSecretResponse response) {

    }

    @Override
    public void onBatteryContainerRentResponse(String id, ClientBatteryRentResponse response) {

    }

    @Override
    public void onBatteryContainerGetRandomResponse(String id, int transactionId, byte[] random) {

    }

    //===================================================================================
    private static volatile ChannelContextHolder INSTANCE;
    private ChannelContextHolder() {

    }

    public static ChannelContextHolder getInstance() {
        if (INSTANCE == null) {
            synchronized (ChannelContextHolder.class) {
                INSTANCE = new ChannelContextHolder();
            }
        }
        return INSTANCE;
    }

    //===================================================================================
    private Map<ChannelHandlerContext, String> contextIdMap = new ConcurrentHashMap<>();
    private Map<String, ChannelHandlerContext> idContextMap = new ConcurrentHashMap<>();

    public ChannelHandlerContext getContextById(String id) {
        return idContextMap.get(id);
    }

    public String getIdByContext(ChannelHandlerContext context) {
        return contextIdMap.get(context);
    }
}
