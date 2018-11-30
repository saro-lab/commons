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
    String token() default "";
    
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
     * ignore not found getter
     * @return
     */
    boolean ignoreNotFoundGetter() default false;
    
    /**
     * ignore not found setter
     * @return
     */
    boolean ignoreNotFoundSetter() default false;
    
    /**
     * charset
     * @return
     */
    String charset() default "UTF-8";
}
