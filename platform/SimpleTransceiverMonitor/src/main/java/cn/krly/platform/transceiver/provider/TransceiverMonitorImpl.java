package cn.krly.platform.transceiver.provider;

import cn.krly.platform.transceiver.api.ITransceiverMonitor;
import cn.krly.utility.db.JedisPoolUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

@Service(provider = "DefaultProviderConfig")
public class TransceiverMonitorImpl implements ITransceiverMonitor {
    @Value("${app.redis.host}")
    private String redisHost;

    @Value("${app.redis.port}")
    private int redisPort;

    private JedisPool jedisPool;

    @PostConstruct
    public void init() {
        jedisPool = JedisPoolUtils.newJedisPoolInstance(redisHost, redisPort);
    }

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
