package cn.krly.platform.transceiver.provider;

import cn.krly.platform.transceiver.api.ITransceiverMonitor;
import cn.krly.utility.db.JedisPoolUtils;
import com.alibaba.dubbo.config.annotation.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service(provider = "DefaultProviderConfig")
public class TransceiverMonitorImpl implements ITransceiverMonitor {
    private JedisPool jedisPool = JedisPoolUtils.newJedisPoolInstance("127.0.0.1", 6379);

    @Override
    public void onChannelConnected(int token, String host, int port) {

    }

    @Override
    public void onChannelClosed(int token, String host, int port) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(Integer.toString(token), "host");
        }
    }

    @Override
    public void onChannelRead(int token, byte[] message, String host, int port) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(Integer.toString(token), "host", host + ":" + port);
        }
    }
}
