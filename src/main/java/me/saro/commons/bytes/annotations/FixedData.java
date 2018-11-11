package me.saro.commons.bytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  FixedData
 * @author      PARK Yong Seo
 * @since       1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedData {
    
    /**
     * byte size
     * @return
     */
    int size() default -1;
    
    /**
     * base fill
     * @return
     */
    byte fill() default ' ';
    
    /**
     * charset
     * @return
     */
    String charset() default "UTF-8";
}
