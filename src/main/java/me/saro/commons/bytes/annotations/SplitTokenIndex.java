package me.saro.commons.bytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * text data in FixedDataFormat
 * @author      PARK Yong Seo
 * @since       1.4
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SplitTokenOrder {
    
    /**
     * zero-based index
     * @return
     */
    int index() default -1;
    
    /**
     * read nullable <br>
     * true : length == 0 : null <br>
     * false : length == 0 : "" (default)
     * @return
     */
    boolean nullable() default false;
}
