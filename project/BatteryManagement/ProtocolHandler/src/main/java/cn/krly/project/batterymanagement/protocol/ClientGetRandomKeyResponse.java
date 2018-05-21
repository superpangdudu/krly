package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ClientGetRandomKeyResponse extends Message {
    private int transactionId;
    private byte[] randomKey;

    public boolean init(byte[] data) {
        int offset = PAYLOAD_OFFSET;

        //
        transactionId = data[offset] & 0xFF;
        offset += 1;

        //
        randomKey = new byte[8];
        System.arraycopy(data, offset, randomKey, 0, 8);

        return true;
    }

    public int getId() {
        return MSG_ID_CLIENT_GET_RANDOM_KEY_RESPONSE;
    }

    public byte[] getData() {
        return new byte[0];
    }

    public int getTransactionId() {
        return transactionId;
    }

    public byte[] getRandomKey() {
        return randomKey;
    }
}
