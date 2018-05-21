package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ClientHeartBeatRequest extends Message {
    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return Message.MSG_ID_CLIENT_HEARTBEAT;
    }

    public byte[] getData() {
        return null;
    }
}
