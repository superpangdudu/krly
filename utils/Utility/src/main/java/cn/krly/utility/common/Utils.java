package cn.krly.utility.common;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/28.
 */
public class Utils {
    public static String MD5(String source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(source.getBytes());

            /**
             * Since the BigInteger ignores the beginning '0's,
             * the result MD5 string length may less than 32 bytes,
             * additional prefix appending is needed.
             */
            String md5 = new BigInteger(1, messageDigest.digest()).toString(16).toUpperCase();

            int appendingPrefixLength = 32 - md5.length();
            StringBuilder sb = new StringBuilder();
            while (appendingPrefixLength > 0) {
                sb.append("0");
                --appendingPrefixLength;
            }

            if (sb.length() > 0)
                md5 = sb.toString() + md5;

            return md5;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean isEmptyString(String text) {
        if (text == null || text.equals(""))
            return true;
        return false;
    }

    public static boolean isMobile(String phone){
        return phone.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$");
    }

    public static String getStringValue(Map<String, String> map, String key) {
        Object value = map.get(key);
        if (value == null)
            value = "";

        return value.toString();
    }

    public static int getIntegerValue(Map<String, String> map, String key) {
        Object value = map.get(key);
        if (value == null)
            value = "0";

        int ret = 0;
        try {
            ret = Integer.parseInt(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static long getLongValue(Map<String, String> map, String key) {
        Object value = map.get(key);
        if (value == null)
            value = "0";

        long ret = 0;
        try {
            ret = Long.parseLong(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String getUrlParams(Map<String, String> map) {
        if (null == map || map.size() == 0)
            return null;

        String url = "?";
        int i = 1;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (i == map.size())
                url += entry.getKey() + "=" + entry.getValue();
            else
                url += entry.getKey() + "=" + entry.getValue() + "&";
            ++ i;
        }

        return url;
    }

    public static Date getTime(String time) {
        return getFormatTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDate(String date) {
        return getFormatTime(date, "yyyy-MM-dd");
    }

    private static Date getFormatTime(String time, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> strToList(String str) {
        String[] ids = str.split(",");
        return new ArrayList<>(Arrays.asList(ids));
    }

    public static List<Integer> strToIntList(String str) {
        List<Integer> list = new ArrayList<>();
        String[] ids = str.split(",");
        for (String id : ids)
            list.add(Integer.valueOf(id));

        return list;
    }

    public static String toRMBYuanString(int cents) {
        int quotient = cents / 100;
        int reminder = cents % 100;

        String ret = quotient + ".";
        if (reminder == 0) {
            ret += "00";
            return ret;
        }

        if (reminder < 10) {
            ret += "0" + reminder;
            return ret;
        }

        return ret + reminder;
    }

    /*
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        log.error("/wxpay/web/prepay X-Real-IP :{}", ip);
        ip = request.getHeader("X-Forwarded-For");
        log.error("/wxpay/web/prepay X-Forwarded-For :{}", ip);
        if (!StringUtils.isBlank(ip)
                && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
    */
}
