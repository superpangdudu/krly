package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/16.
 */
public class ServerSyncTimeResponse extends Message {
    private long timeMills = System.currentTimeMillis();

    public boolean init(byte[] data) {
        return true;
    }

    public int getId() {
        return MSG_ID_SERVER_SYNC_TIME_RESPONSE;
    }

    public byte[] getData() {
        int time = (int) (timeMills / 1000);
        byte[] timeBuf = ProtocolUtils.intToByte(time);

        byte[] data = new byte[0x0A];
        data[0] = '$';
        data[1] = '$';
        data[2] = '0';
        data[3] = 'A';
        data[4] = (byte) getId();
        data[5] = timeBuf[0];
        data[6] = timeBuf[1];
        data[7] = timeBuf[2];
        data[8] = timeBuf[3];

        data[data.length - 1] = ProtocolUtils.checkSum(data, 2);

        return data;
    }

    public void setTimeMills(long timeMills) {
        this.timeMills = timeMills;
    }
}
