package me.saro.commons.fd;

import lombok.Data;

/**
 * 
 * @author		PARK Yong Seo
 * @since		0.2
 */
@Data
public class FixedDataMapper {
	Class<?> clazz;
	String charset;
	
	private FixedDataMapper(Class<?> clazz, String charset) {
		this.clazz = clazz;
		this.charset = charset;
	}
	
	public static FixedDataMapper create(Class<?> clazz, String charset) {
		return new FixedDataMapper(clazz, charset).init();
	}
	
	private FixedDataMapper init() {
		
		//Stream.of(clazz.getDeclaredFields()).map(predicate)
		//Stream.of(clazz.getDeclaredMethods()).filter(e -> e.getAnnotation(annotationClass));
		
		return this;
	}
}
