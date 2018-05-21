package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ServerGetRandomKeyRequest extends Message {
    private int transactionId;

    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_SERVER_GET_RANDOM_KEY_REQUEST;
    }

    public byte[] getData() {
        byte[] data = new byte[0x07];
        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = '7';
        data[4] = (byte) (getId() & 0xFF);
        data[5] = (byte) (transactionId & 0xFF);

        data[6] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
