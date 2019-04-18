package me.saro.commons.bytes.fd;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Fixed Data Mapper
 * @author      PARK Yong Seo
 * @since       3.1.0
 */
public interface FixedData {
    
    Map<Class<?>, FixedData> STORE = new HashMap<>();
    
    /**
     * get instance
     * @param clazz
     * @return
     */
    static FixedData getInstance(Class<?> clazz) {
        FixedData mapper;
        synchronized (STORE) {
            mapper = STORE.get(clazz);
            if (mapper == null) {
                STORE.put(clazz, mapper = new FixedDataImpl(clazz));
            }
        }
        return mapper;
    }
    
    /**
     * get byte size of the "FixedData" class
     * @return
     */
    int size();
    
    /**
     * bytes to class
     * @param bytes
     * @param offset
     * @return
     */
    <T> T toClass(byte[] bytes, int offset);
  
    /**
     * the class to bind(write) the bytes
     * @param data
     * @param out
     * @param offset
     * @return
     */
    byte[] bindBytes(Object data, byte[] out, int offset);
    
    /**
     * bytes to class
     * @param bytes
     * @return
     */
    default <T> T toClass(byte[] bytes) {
        return toClass(bytes, 0);
    }
    
    /**
     * class to bytes
     * @param data
     * @return
     */
    default byte[] toBytes(Object data) {
        return bindBytes(data, new byte[size()]);
    }
    
    /**
     * the class to bind(write) the bytes
     * @param data
     * @param out
     * @return
     */
    default byte[] bindBytes(Object data, byte[] out) {
        return bindBytes(data, out, 0);
    }
    
    /**
     * the class to bind(write) the outputstream<br>
     * is not close
     * @param data
     * @param out
     * @return
     * @throws IOException
     */
    default OutputStream bindBytes(Object data, OutputStream out) throws IOException {
        out.write(toBytes(data));
        out.flush();
        return out;
    }
}
