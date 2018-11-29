package me.saro.commons.bytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  text data in FixedDataFormat
 * @author      PARK Yong Seo
 * @since       1.4
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedText {
    
    /**
     * offset
     * @return
     */
    int offset() default -1;
    
    /**
     * byte length
     * @return
     */
    int length() default -1;
    
    /**
     * base fill
     * @return
     */
    byte fill() default ' ';
    
    /**
     * is unsigned of the number type<br>
     * support : byte, short, int, long
     * @return
     */
    boolean unsigned() default false;
    
    /**
     * radix of the number type<br>
     * support : byte, short, int, long
     */
    int radix() default 10;
    
    /**
     * charset<br>
     * support only String
     * @return
     */
    String charset() default "";
    
    /**
     * align
     * @return
     * @see
     * FixedTextAlign
     */
    FixedTextAlign align() default FixedTextAlign.left;
}
