package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ServerQueryDeviceStatusRequest extends Message {
    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_SERVER_QUERY_STATUS_REQUEST;
    }

    public byte[] getData() {
        return new byte[] { '$', '$', '0', '6', MSG_ID_SERVER_QUERY_STATUS_REQUEST, 0x09 };
    }
}
