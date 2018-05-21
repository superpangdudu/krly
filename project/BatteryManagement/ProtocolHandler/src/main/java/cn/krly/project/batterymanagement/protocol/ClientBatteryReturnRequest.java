package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ClientBatteryReturnRequest extends Message {
    private int transactionId;
    private byte[] batteryId;
    private int result;
    private int slot;
    private byte[] timestamp;

    public boolean init(byte[] data) {
        int offset = PAYLOAD_OFFSET;

        //
        transactionId = data[offset] & 0xFF;
        offset += 1;

        //
        batteryId = new byte[4];
        System.arraycopy(data, offset, batteryId, 0, 4);
        offset += 4;

        //
        result = (data[offset] >> 4) & 0x0F;

        //
        slot = data[offset] & 0x0F;

        offset += 1;

        //
        timestamp = new byte[4];
        System.arraycopy(data, offset, timestamp, 0, 4);

        return true;
    }

    public int getId() {
        return MSG_ID_CLIENT_RETURN_REQUEST;
    }

    public byte[] getData() {
        return new byte[0];
    }

    public int getTransactionId() {
        return transactionId;
    }

    public byte[] getBatteryId() {
        return batteryId;
    }

    public int getResult() {
        return result;
    }

    public int getSlot() {
        return slot;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }
}
