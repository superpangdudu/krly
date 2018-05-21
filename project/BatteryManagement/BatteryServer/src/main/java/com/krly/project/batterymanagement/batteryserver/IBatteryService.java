package com.krly.project.batterymanagement.batteryserver;

/**
 * Created by Administrator on 2018/5/17.
 */
public interface IBatteryService {
    void onBatteryContainerHeartBeat(String id);
    void onBatteryContainerInfoUpdate(BatteryContainerInfo info);
    void onBatteryContainerStatusUpdate(String id, BatteryInfo[] batteryInfoArray);

    boolean isBatteryContainerOnline(String id);
    void onBatteryContainerUnregistered(String id);

    void onBatteryContainerUpdateRandom(String id, int transactionId, byte[] random);
    void onBatteryContainerCheckSecret(String id, int transactionId, boolean isPassed);

    void onBatteryContainerCheckServerSecret(String id, int status);

    void onBatteryContainerRent(String id, int status, long batteryId, int slotId, byte[] timestamp);
    void onBatteryContainerReturned(String id, int status, long batteryId, int slotId, byte[] timestamp);

    byte[] getBatterContainerSecret(String id);
    byte nextBatteryContainerTransactionId(String id);
}
