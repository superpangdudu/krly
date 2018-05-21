package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ServerBatteryRentRequest extends Message {
    private byte transactionId;

    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_SERVER_RENT_REQUEST;
    }

    public byte[] getData() {
        byte[] data = new byte[0x08];
        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = '8';
        data[4] = (byte) (getId() & 0xFF);
        data[5] = transactionId;
        data[6] = 0;

        data[7] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = (byte) (transactionId & 0xFF);
    }
}
