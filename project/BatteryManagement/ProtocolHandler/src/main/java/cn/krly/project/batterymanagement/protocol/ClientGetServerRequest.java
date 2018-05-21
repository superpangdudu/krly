package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ClientGetServerRequest extends Message {

    private String deviceId = "";
    private byte[] data = null;

    public boolean init(byte[] data) {
        //
        byte[] id = new byte[8];
        System.arraycopy(data, PAYLOAD_OFFSET, id, 0, 8);

        deviceId = ProtocolUtils.hexToString(id);
        deviceId = deviceId.substring(1);
        this.data = data;

        return true;
    }

    public int getId() {
        return MSG_ID_GET_SERVER_REQUEST;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
