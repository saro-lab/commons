package me.saro.commons.bytes.fd.annotations;

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
public @interface DateData {
    
    /**
     * FixedDateType
     * @return
     */
    DateDataType type() default DateDataType.millis8;
    
    /**
     * offset
     * @return
     */
    int offset() default -1;
}
