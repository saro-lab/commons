package me.saro.commons.fd.ia;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 
 * @author		PARK Yong Seo
 * @since		0.2
 */
@Target(ElementType.FIELD)
public @interface BinaryData {
	int offset() default -1;
	DataFlow flow() default DataFlow.all;
}
