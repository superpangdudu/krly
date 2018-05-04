package cn.krly.utility.common;

import java.io.*;


public class SerializeUtils {

    public static <T extends Serializable> byte[] serialize(T object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(object);

            byte[] data = byteArrayOutputStream.toByteArray();
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserialize(byte[] data) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            Object object = objectInputStream.readObject();
            return (T) object;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
