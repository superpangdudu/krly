package com.krly.project.batterymanagement.batteryserver;

import cn.krly.project.batterymanagement.protocol.ProtocolUtils;
import cn.krly.utility.common.SerializeUtils;
import cn.krly.utility.db.JedisPoolUtils;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/18.
 */
public class BatteryServiceImpl implements IBatteryService {
    private static volatile BatteryServiceImpl INSTANCE;

    private BatteryServiceImpl() {
    }

    public static BatteryServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (BatteryServiceImpl.class) {
                if (INSTANCE == null)
                    INSTANCE = new BatteryServiceImpl();
            }
        }
        return INSTANCE;
    }

    //===================================================================================
    private JedisPool jedisPool = JedisPoolUtils.newJedisPoolInstance("127.0.0.1", 6379);

    private static final String KEY_HEARTBEAT = "heartbeat";
    private static final String KEY_ID = "IMEI";
    private static final String KEY_MAC = "mac";
    private static final String KEY_RANDOM = "random";
    private static final String KEY_TRANSACTION = "transaction";
    private static final String KEY_FIRMWARE_VERSION = "firmware";
    private static final String KEY_HARDWARE_VERSION = "hardware";
    private static final String KEY_LOCAL = "local";
    private static final String KEY_REMOTE = "remote";
    private static final String KEY_BATTERIES_COUNT = "battery_count";
    private static final String KEY_BATTERY_PREFIX = "battery:";
    private static final String KEY_SLOT_PREFIX = "slot:";
    private static final String KEY_SLOT = "slot";
    private static final String KEY_CONTAINER = "container";
    private static final String KEY_USER = "user";
    private static final String KEY_STATUS = "status";
    private static final String KEY_BINARY = "binary";

    private static final String None = "0";

    //===================================================================================
    @Override
    public void onBatteryContainerHeartBeat(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long timeMillis = System.currentTimeMillis();
            jedis.hset(id, KEY_HEARTBEAT, timeMillis.toString());
        }
    }

    @Override
    public void onBatteryContainerInfoUpdate(BatteryContainerInfo info) {
        byte[] binary = SerializeUtils.serialize(info);

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(info.getIMEI(), KEY_ID, info.getIMEI());
            jedis.hset(info.getIMEI().getBytes(), KEY_MAC.getBytes(), info.getMac());
            jedis.hset(info.getIMEI(), KEY_FIRMWARE_VERSION, info.getFirmwareVersion());
            jedis.hset(info.getIMEI(), KEY_HARDWARE_VERSION, info.getHardwareVersion());
            jedis.hset(info.getIMEI(), KEY_LOCAL, info.getLocalHost());
            jedis.hset(info.getIMEI(), KEY_REMOTE, info.getRemoteHost());
            jedis.hset(info.getIMEI().getBytes(), KEY_BINARY.getBytes(), binary);
        }
    }

    @Override
    public void onBatteryContainerStatusUpdate(String id, BatteryInfo[] batteryInfoArray) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(id, KEY_BATTERIES_COUNT, Integer.toString(batteryInfoArray.length));

            int pos = 0;
            for (BatteryInfo batteryInfo : batteryInfoArray) {
                // 在充电台下写入每个slot的电池信息
                if (batteryInfo.getId() == -1)
                    jedis.hset(id, KEY_SLOT_PREFIX + pos, None);
                else
                    jedis.hset(id, KEY_SLOT_PREFIX + pos, Long.toString(batteryInfo.getId()));

                // 写入电池基本信息
                if (batteryInfo.getId() == -1)
                    continue;

                String key = KEY_BATTERY_PREFIX + Long.toString(batteryInfo.getId());
                jedis.hset(key, KEY_CONTAINER, id);
                jedis.hset(key, KEY_SLOT, Integer.toString(batteryInfo.getSlot()));
                jedis.hset(key, KEY_STATUS, Integer.toString(batteryInfo.getStatus()));
                jedis.hset(key, KEY_USER, None);
            }
        }
    }

    @Override
    public boolean isBatteryContainerOnline(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hexists(id, KEY_ID);
        }
    }

    @Override
    public void onBatteryContainerUnregistered(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(id);

            String value = jedis.hget(id, KEY_SLOT);
            if (value == null || value.equals(""))
                return;

            int slot = Integer.parseInt(value);
            for (int n = 0; n < slot; n++) {
                value = jedis.hget(id, KEY_SLOT_PREFIX + n);
                if (value == null || value.equals("") || value.equals(None))
                    continue;

                jedis.hdel(value);
            }
        }
    }

    @Override
    public void onBatteryContainerUpdateRandom(String id, int transactionId, byte[] random) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(id.getBytes(), KEY_RANDOM.getBytes(), random);
            jedis.hset(id.getBytes(), KEY_TRANSACTION.getBytes(), Integer.toString(transactionId).getBytes());
        }
    }

    @Override
    public void onBatteryContainerCheckSecret(String id, int transactionId, boolean isPassed) {
    }

    @Override
    public void onBatteryContainerCheckServerSecret(String id, int status) {
    }

    @Override
    public void onBatteryContainerRent(String id, int status, long batteryId, int slotId, byte[] timestamp) {
        // TODO 检查失败状态

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(id, KEY_SLOT_PREFIX + slotId, None);

            //
            String key = KEY_BATTERY_PREFIX + Long.toString(batteryId);
            jedis.hset(key, KEY_CONTAINER, None);
            jedis.hset(key, KEY_SLOT, None);
        }

        //
        sendMessage("rent", id, status, batteryId, slotId, timestamp);
    }

    @Override
    public void onBatteryContainerReturned(String id, int status, long batteryId, int slotId, byte[] timestamp) {
        // TODO 检查失败状态

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(id, KEY_SLOT_PREFIX + slotId, Long.toString(batteryId));

            //
            String key = KEY_BATTERY_PREFIX + Long.toString(batteryId);
            jedis.hset(key, KEY_CONTAINER, id);
            jedis.hset(key, KEY_SLOT, Integer.toString(slotId));
            jedis.hset(key, KEY_STATUS, Integer.toString(status));
            jedis.hset(key, KEY_USER, None);
        }

        //
        sendMessage("return", id, status, batteryId, slotId, timestamp);
    }

    @Override
    public byte[] getBatterContainerSecret(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] random = jedis.hget(id.getBytes(), KEY_RANDOM.getBytes());
            byte[] mac = jedis.hget(id.getBytes(), KEY_MAC.getBytes());

            return ProtocolUtils.encrypt(random, mac);
        }
    }

    @Override
    public byte nextBatteryContainerTransactionId(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            long transactionId = jedis.hincrBy(id, KEY_TRANSACTION, 1L);
            return (byte) (transactionId & 0xFF);
        }
    }

    //===================================================================================
    private void sendMessage(String operation, String id, int status, long batteryId, int slotId, byte[] timestamp) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("op", operation);
        paramMap.put("id", id);
        paramMap.put("status", status);
        paramMap.put("batteryId", batteryId);
        paramMap.put("slotId", slotId);
        paramMap.put("timestamp", timestamp);

        String jsonString = JSON.toJSONString(paramMap);
        MessageSender.getInstance().send(jsonString);
    }
}
