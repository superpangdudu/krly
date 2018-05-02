package cn.krly.utility.common;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class HttpsUtils {
    public static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    //===================================================================================
    public static byte[] post(String url) {
        return post(url, "");
    }

    public static byte[] post(String url, String content) {
        return post(url, content, "UTF-8");
    }

    public static byte[] post(String url, String content, String charset) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,
                    new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());

            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.connect();

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.write(content.getBytes(charset));
            out.flush();
            out.close();

            InputStream inputStream = conn.getInputStream();
            return getInputData(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] get(String url) {
        return get(url, "UTF-8");
    }

    public static byte[] get(String url, String charset) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,
                    new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());

            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.connect();

            InputStream inputStream = conn.getInputStream();
            return getInputData(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //===================================================================================
    private static byte[] getInputData(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inputStream.close();

        return outStream.toByteArray();
    }

    //===================================================================================
    public static void main(String[] args) {
        byte[] data = HttpsUtils.post("https://www.baidu.com");
        try {
            String str = new String(data, "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
