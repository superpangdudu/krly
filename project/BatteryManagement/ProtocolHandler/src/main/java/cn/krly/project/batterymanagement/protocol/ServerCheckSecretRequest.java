package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ServerCheckSecretRequest extends Message {
    private int transactionId;
    private byte[] randomKey;
    private byte[] mac;

    public boolean init(byte[] data) {
        return false;
    }

    public int getId() {
        return MSG_ID_SERVER_CHECK_SECRET_REQUEST;
    }

    public byte[] getData() {
        byte[] data = new byte[0x0F];
        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = 'F';
        data[4] = MSG_ID_SERVER_CHECK_SECRET_REQUEST;

        int offset = PAYLOAD_OFFSET;

        //
        data[offset] = (byte) (transactionId & 0xFF);
        offset += 1;

        //
        byte[] secret = ProtocolUtils.encrypt(randomKey, mac);
        System.arraycopy(secret, 0, data, offset, 8);

        //
        data[14] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setRandomKey(byte[] randomKey) {
        this.randomKey = randomKey;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }
}
