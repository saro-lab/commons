package me.saro.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * text util
 * @author		PARK Yong Seo
 * @since		5.0
 */
public class Texts {

    /**
     *
     * @param text
     * @param token
     * @return
     */
    public static List<String> split(String text, char token) {
        var ca = text.toCharArray();
        var len = ca.length;
        var i = 0;
        var p = 0;
        var rv = new ArrayList<String>();
        for (; i < len ; i++) {
            if (ca[i] == token) {
                rv.add(new String(ca, p, i - p));
                p = i + 1;
            }
        }
        rv.add(new String(ca, p, i - p));
        return rv;
    }

    /**
     *
     * @param text
     * @param token
     * @return
     */
    public static String next(String text, String token) {
        return text.substring(text.indexOf(token) + token.length());
    }

    /**
     *
     * @param text
     * @param token
     * @return
     */
    public static String lastNext(String text, String token) {
        return text.substring(text.lastIndexOf(token) + token.length());
    }

    /**
     *
     * @param text
     * @param token
     * @return
     */
    public static String prev(String text, String token) {
        return text.substring(0, text.indexOf(token));
    }

    /**
     *
     * @param text
     * @param token
     * @return
     */
    public static String lastPrev(String text, String token) {
        return text.substring(0, text.lastIndexOf(token));
    }

}
