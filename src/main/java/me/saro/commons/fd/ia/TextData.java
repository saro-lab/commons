package me.saro.commons.fd.ia;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 
 * @author		PARK Yong Seo
 * @since		0.2
 */
@Target(ElementType.FIELD)
public @interface TextData {
    int offset() default -1;
    int length() default -1;
    boolean fill() default false;
    char fillCharacter() default ' ';
    DataAlign align() default DataAlign.left; 
    DataFlow flow() default DataFlow.all;
}
