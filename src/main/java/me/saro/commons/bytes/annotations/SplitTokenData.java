package me.saro.commons.bytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * split token data
 * @author      PARK Yong Seo
 * @since       1.4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SplitTokenData {
    /**
     * token
     * @return
     */
    String toekn() default "";
    
    /**
     * 
     * @return
     */
    String replaceToken() default "";
    
    /**
     * field count
     * @return
     */
    int fieldCount() default -1;
    
    /**
     * charset
     * @return
     */
    String charset() default "UTF-8";
}
