package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ServerGetServerResponse extends Message {
    private String host;
    private int port;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_GET_SERVER_RESPONSE;
    }

    public byte[] getData() {
        byte[] portBuf = new byte[2];
        portBuf[0] = (byte) ((port >> 8) & 0xFF);
        portBuf[1] = (byte) (port & 0xFF);

        byte[] hostBuf = host.getBytes();

        int packageLength = PAYLOAD_OFFSET + 2 + hostBuf.length + CHECKSUM_LENGTH;
        byte[] data = new byte[packageLength];

        data[0] = '$';
        data[1] = '$';

        byte[] lengthBuf = Integer.toHexString(port & 0xFF).getBytes();
        data[2] = lengthBuf[0];
        data[3] = lengthBuf[1];

        data[4] = (byte) (getId() & 0xFF);

        data[5] = portBuf[0];
        data[6] = portBuf[1];

        System.arraycopy(hostBuf, 0, data, 7, hostBuf.length);

        data[packageLength - 1] = ProtocolUtils.checkSum(data, 2);

        return data;
    }
}
