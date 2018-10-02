package me.saro.commons.function;

/**
 * InputStream consumer
 * 
 * @author
 * PARK Yong Seo
 * 
 * @since
 * 0.1
 */
@FunctionalInterface
public interface StreamReadConsumer {

	/**
	 * read InputStream
	 * @param buf 
	 * buffer
	 * @param len
	 * buffer data length
	 * @throws Exception
	 */
	void accept(byte[] buf, int len) throws Exception;
}
