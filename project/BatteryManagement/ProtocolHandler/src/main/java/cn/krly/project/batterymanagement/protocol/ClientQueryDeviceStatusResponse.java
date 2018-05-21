package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ClientQueryDeviceStatusResponse extends Message {
    public static class Battery {
        public long id;
        public int status;
        public int slot;
    }

    private byte[] data;
    private String deviceId;
    private Battery[] batteryBuf = new Battery[8];

    public boolean init(byte[] data) {
        int payloadOffset = PAYLOAD_OFFSET;

        byte[] id = new byte[8];
        System.arraycopy(data, PAYLOAD_OFFSET, id, 0, 8);

        deviceId = ProtocolUtils.hexToString(id);
        deviceId = deviceId.substring(1); // remove the first char
        payloadOffset += 8;

        //
        int status = data[payloadOffset] & 0xFF;
        int flag = 1;
        int offset = payloadOffset + 1;

        for (int n = 0; n < 8; n++) {
            if ((status & flag) > 0) {
                Battery battery = new Battery();

                battery.id = ((data[offset] & 0xFF) << 24) & 0xFFFFFFFF
                        + (data[offset + 1] & 0xFF) << 16
                        + (data[offset + 2] & 0xFF) << 8
                        + (data[offset + 3] & 0xFF);
                battery.status = data[offset + 4] & 0xFF;
                battery.slot = 0;

                //
                batteryBuf[n] = battery;
            }

            flag = flag << 1;
            offset += 5;
        }

        this.data = data;
        return false;
    }

    public int getId() {
        return MSG_ID_CLIENT_QUERY_STATUS_RESPONSE;
    }

    public byte[] getData() {
        return data;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Battery[] getBatteries() {
        return batteryBuf;
    }
}
