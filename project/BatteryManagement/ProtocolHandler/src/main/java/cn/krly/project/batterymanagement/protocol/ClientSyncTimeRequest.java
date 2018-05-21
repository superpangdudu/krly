package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ClientSyncTimeRequest extends Message {
    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_CLIENT_SYNC_TIME_REQUEST;
    }

    public byte[] getData() {
        return null;
    }
}
