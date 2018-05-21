package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ServerCheckSecretResponse extends Message {
    private byte transactionId;
    private byte status;

    public boolean init(byte[] data) {
        return false;
    }

    public int getId() {
        return MSG_ID_SERVER_CHECK_SECRET_RESPONSE;
    }

    public byte[] getData() {
        byte[] data = new byte[0x08];

        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = '7';
        data[4] = (byte) (getId() & 0xFF);
        data[5] = (byte) (transactionId & 0xFF);
        data[6] = status;

        data[data.length - 1] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = (byte) (transactionId & 0xFF);
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
