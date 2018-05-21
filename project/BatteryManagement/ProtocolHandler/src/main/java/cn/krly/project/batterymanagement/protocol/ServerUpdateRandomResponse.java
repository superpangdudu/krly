package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ServerUpdateRandomResponse extends Message {
    private byte transactionId;

    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_SERVER_UPDATE_RANDOM_RESPONSE;
    }

    public byte[] getData() {
        byte[] data = new byte[0x07];

        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = '7';
        data[4] = (byte) getId();
        data[5] = transactionId;

        data[data.length - 1] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = (byte) (transactionId & 0xFF);
    }
}
