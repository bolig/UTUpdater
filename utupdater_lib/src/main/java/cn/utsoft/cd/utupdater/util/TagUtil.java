package cn.utsoft.cd.utupdater.util;

/**
 * Created by 李波 on 2017/2/9.
 * Function:
 * Desc:
 */
public class TagUtil {

    public static String tag(String... str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
        }
        return sb.toString();
    }
}
