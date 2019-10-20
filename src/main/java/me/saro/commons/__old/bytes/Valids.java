package me.saro.commons.__old.bytes;

/**
 * Valids
 * @author		PARK Yong Seo
 * @since		0.1
 */
public class Valids {

    private Valids() {
    }

    /**
     * mail check
     * <br>
     * - not allow Top-Level Domain
     * <br>
     * - not support unicode
     */
    final public static String IS_MAIL = "[_a-z0-9\\-]+(\\.[_a-z0-9\\-])*@([_a-z0-9\\-]+\\.)+[a-z]{2,}";

    /**
     * mail check
     * <br>
     * - not allow Top-Level Domain
     * <br>
     * - not support unicode
     * <p>
     * <b>cautious:</b>
     * <br>
     * this function not requirements rfc spec
     * <br>
     * - https://tools.ietf.org/html/rfc5321
     * <br>
     * - https://tools.ietf.org/html/rfc5322
     * <p>
     * <b>For example</b>
     * <br>
     * rfc valid : abc@abc
     * <br>
     * isMail not valid : abc@abc (Top-Level Domain)
     * <p>
     * rfc5 valid : abc@saro.me
     * <br>
     * isMail valid : abc@saro.me
     * 
     * @param mail address
     * @param maxLength limit mail length
     * @return is valid
     */
    public static boolean isMail(String mail, int maxLength) {
        return mail != null && mail.length() <= maxLength && mail.matches(IS_MAIL);
    }
    
    /**
     * all parameters is not null
     * @param objs
     * @return
     */
    public static boolean isNotNull(Object... objs) {
        if (objs != null) {
            for (Object obj : objs) {
                if (obj == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * all parameters is not blank
     * @param texts
     * @return
     */
    public static boolean isNotBlank(String... texts) {
        if (texts != null) {
            for (String text : texts) {
                if (text == null || text.matches("[\\s]*")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * check date validation
     * @param date
     * @param format
     * @return
     */
    public static boolean isDate(String date, String format) {
        try {
            return date.equals(DateFormat.parse(date, format).toString(format));
        } catch (Exception e) {
            return false;
        }
    }
}
