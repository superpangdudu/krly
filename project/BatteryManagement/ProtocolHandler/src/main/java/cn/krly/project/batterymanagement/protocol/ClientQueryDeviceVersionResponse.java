package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ClientQueryDeviceVersionResponse extends Message {
    private String firmwareVersion;
    private String hardwareVersion;
    private String IMEI;
    private String ICCID;
    private byte[] MAC;

    public boolean init(byte[] data) {
        int offset = PAYLOAD_OFFSET;

        // firmware version
        StringBuilder sb = new StringBuilder();
        sb.append("V");

        int number = data[offset++] & 0xFF;
        sb.append(number);
        sb.append(".");

        number = data[offset++] & 0xFF;
        sb.append(number);

        firmwareVersion = sb.toString();

        // hardware version
        sb = new StringBuilder();
        sb.append("V");

        number = data[offset++] & 0xFF;
        sb.append(number);
        sb.append(".");

        number = data[offset++] & 0xFF;
        sb.append(number);

        hardwareVersion = sb.toString();

        // IMEI
        byte[] id = new byte[8];
        System.arraycopy(data, offset, id, 0, 8);

        IMEI = ProtocolUtils.hexToString(id);
        IMEI = IMEI.substring(1); // remove the first char

        offset += 8;

        // ICCID
        byte[] tmp = new byte[20];
        System.arraycopy(data, offset, tmp, 0, 20);
        ICCID = new String(tmp);

        offset += 20;

        // MAC
        MAC = new byte[6];
        System.arraycopy(data, offset, MAC, 0, 6);

        return true;
    }

    public int getId() {
        return MSG_ID_CLIENT_QUERY_VERSION_RESPONSE;
    }

    public byte[] getData() {
        return null;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public String getIMEI() {
        return IMEI;
    }

    public String getICCID() {
        return ICCID;
    }

    public byte[] getMAC() {
        return MAC;
    }
}
