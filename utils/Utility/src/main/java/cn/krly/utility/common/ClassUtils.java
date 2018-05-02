package cn.krly.utility.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Administrator on 2018/4/27.
 */
public class ClassUtils {
    public static Class<?> getClassFromJar(String jarPath, String className) throws Exception {
        File jarFile = new File(jarPath);
        URL url = jarFile.toURI().toURL();

        ClassLoader classLoader = new URLClassLoader(new URL[] {url},
                Thread.currentThread().getContextClassLoader());
        Class<?> clazz = classLoader.loadClass(className);

        return clazz;
    }

    public static void main(String[] args) throws Exception {
        Class clazz = getClassFromJar("e:/simple.jar", "cn.krly.platform.transceiver.SimpleTCPClient");
        System.out.println(clazz);
    }

}
