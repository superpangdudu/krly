package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ClientCheckSecretRequest extends Message {
    private int transactionId;
    private byte[] secret;

    public boolean init(byte[] data) {
        int offset = PAYLOAD_OFFSET;

        // transaction id
        transactionId = data[offset] & 0xFF;
        offset += 1;

        //
        secret = new byte[8];
        System.arraycopy(data, offset, secret, 0, 8);

        return true;
    }

    public int getId() {
        return MSG_ID_CLIENT_CHECK_SECRET_REQUEST;
    }

    public byte[] getData() {
        return new byte[0];
    }

    public int getTransactionId() {
        return transactionId;
    }

    public byte[] getSecret() {
        return secret;
    }
}
