package cn.krly.platform.transceiver;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ChannelHolder {
    private static volatile ChannelHolder INSTANCE;

    private ChannelHolder() {

    }

    public static ChannelHolder getInstance() {
        if (INSTANCE == null) {
            synchronized (ChannelHolder.class) {
                if (INSTANCE == null)
                    INSTANCE = new ChannelHolder();
            }
        }
        return INSTANCE;
    }

    private Map<Integer, Channel> tokenToChannelMap = new ConcurrentHashMap<>();
    private Map<Channel, Integer> channelToTokenMap = new ConcurrentHashMap<>();

    //===================================================================================
    public Channel getChannel(int token) {
        return tokenToChannelMap.get(token);
    }

    public Integer getToken(Channel channel) {
        return channelToTokenMap.get(channel);
    }

    public void addChannel(int token, Channel channel) {
        tokenToChannelMap.put(token, channel);
        channelToTokenMap.put(channel, token);
    }

    public Channel removeChannel(int token) {
        Channel channel = tokenToChannelMap.remove(token);
        channelToTokenMap.remove(channel);

        return channel;
    }
}
