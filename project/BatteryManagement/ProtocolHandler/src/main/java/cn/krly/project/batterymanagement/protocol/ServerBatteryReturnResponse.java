package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ServerBatteryReturnResponse extends Message {
    private byte transactionId;
    private byte[] batteryId;
    private byte status;

    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_SERVER_RETURN_RESPONSE;
    }

    public byte[] getData() {
        byte[] data = new byte[0x0C];

        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = 'C';
        data[4] = (byte) (getId() & 0xFF);

        int offset = PAYLOAD_OFFSET;

        //
        data[offset] = transactionId;
        offset += 1;

        //
        System.arraycopy(batteryId, 0, data, offset, 4);

        data[data.length - 1] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = (byte) (transactionId & 0xFF);
    }

    public void setBatteryId(byte[] batteryId) {
        this.batteryId = batteryId;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
