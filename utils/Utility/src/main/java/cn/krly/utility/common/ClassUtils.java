package cn.krly.utility.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getClassNamesInPackage(String packageName) {
        packageName = packageName.replace(".", File.separator);
        String rootPath = ClassUtils.class.getResource("/").getPath();

        String scanPath = rootPath + packageName;
        List<String> fileList = scan(scanPath);

        //
        rootPath = rootPath.replaceFirst("/", "")
                .replace("/", "\\");

        List<String> classList = new ArrayList<>();
        for (String filePath : fileList) {
            String className = filePath.replace(rootPath, "").replace("\\", ".").replace(".class", "");
            classList.add(className);
        }

        return classList;
    }

    //===================================================================================
    private static List<String> scan(String path) {
        List<String> classList = new ArrayList<>();

        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tmp : files)
                classList.addAll(scan(tmp.getPath()));

        } else {
            if (file.getName().endsWith(".class") == false)
                return new ArrayList<>();

            classList.add(file.getPath());
        }

        return classList;
    }


    public static void main(String[] args) throws Exception {
        Class clazz = getClassFromJar("e:/simple.jar", "cn.krly.platform.transceiver.SimpleTCPClient");
        System.out.println(clazz);
    }

}
