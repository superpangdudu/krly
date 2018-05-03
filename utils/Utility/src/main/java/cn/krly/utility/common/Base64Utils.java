package cn.krly.utility.common;

import java.util.Base64;

/**
 * Created by Administrator on 2018/5/2.
 */
public class Base64Utils {
    private static final char[] CODE_MAP = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '+', '/'
    };

    public static String encode(byte[] data) {
        if (data == null || data.length == 0)
            return "";

        String padding = "";
        int paddingLength = data.length % 3;
        if (paddingLength == 2)
            padding = "=";
        else if (paddingLength == 1)
            padding = "==";

        StringBuilder sb = new StringBuilder(data.length * 4 / 3 + 4);
        for (int offset = 0; offset < data.length; offset += 3) {
            byte first = data[offset];
            byte second = ((offset + 1) >= data.length) ? 0 : data[offset + 1];
            byte third = ((offset + 2) >= data.length) ? 0 : data[offset + 2];

            encode(first, second, third, sb);
        }

        if (paddingLength > 0)
            sb.replace(sb.length() - padding.length(), sb.length(), padding);

        return sb.toString();
    }

    public static byte[] decode(String data) {
        int messageLength = data.length() / 4 * 3;

        if (data.endsWith("=="))
            messageLength -= 2;
        else if (data.endsWith("="))
            messageLength -= 1;

        byte[] message = new byte[messageLength];

        for (int offset = 0; offset < data.length(); offset += 4) {
            int first = getValue(data.charAt(offset));
            int second = getValue(data.charAt(offset + 1));
            int third = getValue(data.charAt(offset + 2));
            int fourth = getValue(data.charAt(offset + 3));

            decode(first, second, third, fourth, offset / 4 * 3, message);
        }

        return message;
    }

    //===================================================================================
    private static void encode(byte first, byte second, byte third, StringBuilder sb) {
        int value = first >> 2;
        sb.append(CODE_MAP[value]);

        value = (first & 0x03) << 4;
        value |= (second & 0xF0) >> 4;
        sb.append(CODE_MAP[value]);

        value = (second & 0x0F) << 2;
        sb.append(CODE_MAP[value]);

        value = third & 0x3F;
        sb.append(CODE_MAP[value]);
    }

    private static void decode(int first, int second, int third, int fourth, int offset, byte[] buffer) {
        int value = (first << 2) | ((second & 0x30) >> 4);
        buffer[offset] = (byte) value;

        if (offset + 1 >= buffer.length)
            return;

        value = ((second & 0x0F) << 4) | ((third & 0x3C) >> 2);
        buffer[offset + 1] = (byte) value;

        if (offset + 2 >= buffer.length)
            return;

        value = ((third & 0x03) << 6) | fourth;
        buffer[offset + 2] = (byte) value;
    }

    private static int getValue(char data) {
        if (data == '=')
            return 0;
        if (data >= '0' && data <= '9')
            return data + 4;
        if (data >= 'a' && data <= 'z')
            return data - 71;
        if (data >= 'A' && data <= 'Z')
            return data - 65;
        if (data == '+')
            return 62;
        return 63;
    }

    public static void main(String[] args) {
        System.out.println(Base64Utils.encode("11".getBytes()));

        byte[] buf = Base64Utils.decode("MTExMTE=");

        //assert(buf.equals());

        long startTime = System.currentTimeMillis();

//        for (int n = 0; n < 100000000; n++) {
//            byte[] bufA = Base64.getEncoder().encode("1234567890hahawawakaka".getBytes());
//            String s = new String(bufA);
//        }
//        System.out.println("Base64.encode took: " + (System.currentTimeMillis() - startTime));
//
//        startTime = System.currentTimeMillis();
//        for (int n = 0; n < 100000000; n++) {
//            String value = encode("1234567890hahawawakaka".getBytes());
//        }
//        System.out.println("Base64.encode took: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int n = 0; n < 100000000; n++) {
            Base64.getDecoder().decode("MTIzNDU2Nzg5MGhhaGF3YXdha2FrYQ==");
        }
        System.out.println("Base64.decode took: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int n = 0; n < 100000000; n++) {
            decode("MTIzNDU2Nzg5MGhhaGF3YXdha2FrYQ==");
        }
        System.out.println("Base64.encode took: " + (System.currentTimeMillis() - startTime));
    }

}
