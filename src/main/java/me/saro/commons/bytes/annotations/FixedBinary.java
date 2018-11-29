package me.saro.commons.bytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  binary data in FixedDataFormat
 * @author      PARK Yong Seo
 * @since       1.4
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedBinary {
    
    /**
     * offset
     * @return
     */
    int offset() default -1;
    
    /**
     * array length
     * use in array data
     * @return
     */
    int arrayLength() default -1;
}
