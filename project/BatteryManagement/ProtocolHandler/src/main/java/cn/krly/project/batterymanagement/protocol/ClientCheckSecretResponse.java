package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ClientCheckSecretResponse extends Message {
    private int transactionId;
    private boolean isPassed;

    public boolean init(byte[] data) {
        int offset = PAYLOAD_OFFSET;

        //
        transactionId = data[offset] & 0xFF;
        offset += 1;

        //
        isPassed = data[offset] > 0;

        return true;
    }

    public int getId() {
        return MSG_ID_CLIENT_CHECK_SECRET_RESPONSE;
    }

    public byte[] getData() {
        return new byte[0];
    }

    public int getTransactionId() {
        return transactionId;
    }

    public boolean getIsPassed() {
        return isPassed;
    }
}
