package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ServerBatteryRentConfirmResponse extends Message {
    private byte transactionId;
    private byte[] batteryId;

    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_SERVER_RENT_CONFIRM_RESPONSE;
    }

    public byte[] getData() {
        byte[] data = new byte[0x0B];
        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = 'B';
        data[4] = (byte) (getId() & 0xFF);
        data[5] = transactionId;

        System.arraycopy(batteryId, 0, data, 6, 4);

        data[data.length - 1] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = (byte) (transactionId & 0xFF);
    }

    public void setBatteryId(byte[] batteryId) {
        this.batteryId = batteryId;
    }
}
