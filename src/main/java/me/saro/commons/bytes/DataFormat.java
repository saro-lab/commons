package me.saro.commons.bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.function.Supplier;

import me.saro.commons.function.ThrowableSupplier;

/**
 * DataFormat
 * @author      PARK Yong Seo
 * @since       1.4
 */
public interface DataFormat<T> {
    
    /**
     * bytes to class
     * @param bytes
     * @param offset
     * @return
     */
    public T toClass(byte[] bytes, int offset);
    
    default public T toClass(byte[] bytes) {
        return toClass(bytes, 0);
    }
    
    public T toClassWithCheckSize(byte[] bytes);
    
    public T toClassWithCheckSize(String line);
    
    default public T toClassWithCheckSize(String line, String charset) {
        try {
            return toClassWithCheckSize(line.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void bindBytes(byte[] outputBytes, int offset, T obj);
    
    public void bindBytes(OutputStream out, T obj) throws IOException;
    
    public byte[] toBytes(T obj);
    
    /**
     * create DataFormat
     * @param clazz
     * @param newInstance
     * @return
     */
    public static <T> DataFormat<T> createFixedData(Class<T> clazz, Supplier<T> newInstance) {
        return new FixedDataFormat<>(clazz, newInstance);
    }
    
    /**
     * create DataFormat<br>
     * user defualt constructor
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> DataFormat<T> createFixedData(Class<T> clazz) {
        return createFixedData(clazz, ThrowableSupplier.runtime(() -> (T)clazz.getDeclaredConstructors()[0].newInstance()));
    }
}
