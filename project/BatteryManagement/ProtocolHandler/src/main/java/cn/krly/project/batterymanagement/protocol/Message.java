package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/15.
 */
public abstract class Message {
    public static final int MSG_ID_CLIENT_HEARTBEAT = 0xBB;
    public static final int MSG_ID_SERVER_HEARTBEAT = 0xDD;

    public static final int MSG_ID_GET_SERVER_RESPONSE = 0xFF;
    public static final int MSG_ID_GET_SERVER_REQUEST = 0xFF;

    public static final int MSG_ID_SERVER_RENT_REQUEST = 0x01;
    public static final int MSG_ID_SERVER_RETURN_RESPONSE = 0x02;
    public static final int MSG_ID_SERVER_QUERY_STATUS_REQUEST = 0x03;
    public static final int MSG_ID_SERVER_QUERY_VERSION_REQUEST = 0x04;
    public static final int MSG_ID_SERVER_GET_RANDOM_KEY_REQUEST = 0x05;
    public static final int MSG_ID_SERVER_CHECK_SECRET_REQUEST = 0x06;
    public static final int MSG_ID_SERVER_RENT_CONFIRM_RESPONSE = 0x07;
    public static final int MSG_ID_SERVER_UPDATE_RANDOM_RESPONSE = 0x08;
    public static final int MSG_ID_SERVER_CHECK_SECRET_RESPONSE = 0x09;
    public static final int MSG_ID_SERVER_SYNC_TIME_RESPONSE = 0x0A;

    public static final int MSG_ID_CLIENT_RENT_RESPONSE = 0x01;
    public static final int MSG_ID_CLIENT_RETURN_REQUEST = 0x02;
    public static final int MSG_ID_CLIENT_QUERY_STATUS_RESPONSE = 0x03;
    public static final int MSG_ID_CLIENT_QUERY_VERSION_RESPONSE = 0x04;
    public static final int MSG_ID_CLIENT_GET_RANDOM_KEY_RESPONSE = 0x05;
    public static final int MSG_ID_CLIENT_CHECK_SECRET_RESPONSE = 0x06;
    public static final int MSG_ID_CLIENT_UPDATE_RANDOM_REQUEST = 0x07;
    public static final int MSG_ID_CLIENT_CHECK_SECRET_REQUEST = 0x08;
    public static final int MSG_ID_CLIENT_SYNC_TIME_REQUEST = 0x09;

    //===================================================================================
    public static final int HEADER_LENGTH = 2;
    public static final int LENGTH_FLAG_LENGTH = 2;
    public static final int CMD_FLAG_LENGTH = 1;
    public static final int KEY_POSITION = HEADER_LENGTH + LENGTH_FLAG_LENGTH;
    public static final int CHECKSUM_LENGTH = 1;
    public static final int PAYLOAD_OFFSET = HEADER_LENGTH + LENGTH_FLAG_LENGTH + CMD_FLAG_LENGTH;

    //===================================================================================
    public abstract boolean init(byte[] data);
    public abstract int getId();
    public abstract byte[] getData();

    //===================================================================================
    protected final boolean isFromClient(byte[] data) {
        if (data[0] != '#'
                || data[1] != '#')
            return false;
        return true;
    }

    protected final boolean isFromServer(byte[] data) {
        if (data[0] != '$'
                || data[1] != '$')
            return false;
        return true;
    }
}
