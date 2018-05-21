package cn.krly.project.batterymanagement.protocol;

/**
 * Created by Administrator on 2018/5/15.
 */
public class ProtocolUtils {

    public static byte checkSum(byte[] data, int from) {
        long checkSum = 0;

        for (int n = from; n < data.length; n++)
            checkSum += data[n] & 0xFF;

        return (byte) (checkSum & 0xFF);
    }

    public static int toLength(byte high, byte low) {
        String s = new String(new byte[] { high, low });
        return Integer.parseInt(s, 16);
    }

    public static byte[] encrypt(byte[] rand, byte[] mac) {
        if (rand == null || rand.length != 8)
            return null;
        if (mac == null || mac.length != 6)
            return null;

        byte[] data = new byte[8];
        byte[] key = new byte[8];
        long delta = 0x8D2668A8L;
        long sum = 0;

        System.arraycopy(rand, 0, data, 0, 8);
        System.arraycopy(mac, 0, key, 0, 6);

        long tmp = 0;
        for (int n = 0; n < 6; n++)
            tmp += key[n] & 0xFF;
        key[6] = (byte) (tmp & 0xFF);

        long k0 = key[0] & 0xFF;
        long k1 = key[1] & 0xFF;
        long k2 = key[2] & 0xFF;
        long k3 = key[3] & 0xFF;
        long k4 = key[4] & 0xFF;
        long k5 = key[5] & 0xFF;

        long v1 = (k2 << k3) & 0xFF;
        long v2 = (k0 & k1) & 0xFF;
        long v3 = (k4 & k5) & 0xFF;
        long v4 = (v2 ^ v1) & 0xFF;
        long v5 = (v4 ^ v3) & 0xFF;

        key[7] = (byte) (v5 & 0xFF);

        for (int n = 0; n < 32; n++) {
            sum = sum + delta;

            data[0] = (byte) ((data[0] & 0xFF) + (compute(data[5], data[4], data[1], data[0], key[0], key[6], sum) & 0xFF));
            data[1] = (byte) ((data[1] & 0xFF) + (compute(data[2], data[0], data[7], data[2], key[2], key[2], sum) & 0xFF));
            data[2] = (byte) ((data[2] & 0xFF) + (compute(data[3], data[1], data[2], data[7], key[5], key[3], sum) & 0xFF));
            data[3] = (byte) ((data[3] & 0xFF) + (compute(data[7], data[6], data[5], data[4], key[7], key[7], sum) & 0xFF));
            data[4] = (byte) ((data[4] & 0xFF) + (compute(data[1], data[3], data[3], data[6], key[4], key[5], sum) & 0xFF));
            data[5] = (byte) ((data[5] & 0xFF) + (compute(data[4], data[2], data[6], data[5], key[3], key[4], sum) & 0xFF));
            data[6] = (byte) ((data[6] & 0xFF) + (compute(data[6], data[7], data[4], data[3], key[6], key[1], sum) & 0xFF));
            data[7] = (byte) ((data[7] & 0xFF) + (compute(data[0], data[5], data[0], data[1], key[1], key[0], sum) & 0xFF));
        }

        return data;
    }

    private static byte compute(byte data1, byte data2, byte data3, byte data4, byte key1, byte key2, long sum) {
        //(((data1 * 256 + data2) >> 4) + key1) ^ (data3 + sum) ^ ((data4 >> 5) + key2);

        long tmp1 = data1 & 0xFF;
        long tmp2 = data2 & 0xFF;
        long tmp3 = data3 & 0xFF;
        long tmp4 = data4 & 0xFF;
        long k1 = key1 & 0xFF;
        long k2 = key2 & 0xFF;

        long v1 = ((tmp1 * 256 + tmp2) >> 4) + k1;
        long v2 = tmp3 + sum;
        long v3 = (tmp4 >> 5) + k2;
        long v4 = v1 ^ v2 ^ v3;

        byte data = (byte) (v4 & 0xFF);
        return data;
    }

    public static byte[] intToByte(int number) {
        byte[] data = new byte[4];

        data[0] = (byte) ((number >> 24) & 0xFF);
        data[1] = (byte) ((number >> 16) & 0xFF);
        data[2] = (byte) ((number >> 8) & 0xFF);
        data[3] = (byte) (number & 0xFF);

        return data;
    }

    public static int hexByteToInt(byte[] data) {
        String str = new String(data);
        return Integer.parseInt(str, 16);
    }

    public static String hexToString(byte[] data) {
        char[] hexCode = "0123456789ABCDEF".toCharArray();

        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(hexCode[(b >> 4) & 0xF]);
            sb.append(hexCode[(b & 0xF)]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        long timeMills = System.currentTimeMillis();
        int time = (int) (timeMills / 1000);
        byte[] timeBuf = Integer.toString(time).getBytes();

//        boolean[] batteryStatus = new boolean[8];
//        int status = 0xEA;
//        int flag = 1;
//        for (int n = 0; n < 8; n++) {
//            batteryStatus[n] = (status & flag) > 0;
//            flag = flag << 1;
//        }

//        byte[] lengthBuf = Integer.toHexString(175 & 0xFFFF).getBytes();
//        String s = new String(lengthBuf);
//        int n = Integer.parseInt(s, 16);

//        int port = 54321;
//        byte[] d = new byte[2];
//
//        d[0] = (byte) ((port >> 8) & 0xFF);
//        d[1] = (byte) (port & 0xFF);

        byte[] rand = new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        byte[] mac = new byte[] {(byte) 0xA0, 0x5A, 0x5C, 0x00, 0x01, 0x6F};

        byte[] data = encrypt(rand, mac);
        String value = hexToString(data);
    }
}
