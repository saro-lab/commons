package me.saro.commons.bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import me.saro.commons.bytes.annotations.SplitTokenData;

/**
 * Split Token 
 * @author      PARK Yong Seo
 * @since       1.4
 */
public class SplitDataFormat<T> extends AbstractDataFormat implements DataFormat<T> {

    final Class<T> clazz;
    final SplitTokenData splitTokenData;
    final Supplier<T> newInstance;
    final List<FixedDataBytesToClass> toClassOrders = Collections.synchronizedList(new ArrayList<>());
    final List<FixedDataClassToBytes> toBytesOrders = Collections.synchronizedList(new ArrayList<>());
    
    SplitDataFormat(Class<T> clazz, Supplier<T> newInstance) {
        this.clazz = clazz;
        splitTokenData = clazz.getDeclaredAnnotation(SplitTokenData.class);
        this.newInstance = newInstance;
        init();
    }
    
    @Override
    public T toClass(byte[] bytes, int offset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T toClassWithCheckSize(byte[] bytes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T toClassWithCheckSize(String line) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void bindBytes(byte[] outputBytes, int offset, T obj) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void bindBytes(OutputStream out, T obj) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public byte[] toBytes(T obj) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void init() {
        
    }
    
    /**
     * to class orders
     */
    @FunctionalInterface
    private static interface FixedDataBytesToClass {
        void order(Object obj, byte[] bytes, int offset) throws Exception;
    }
    
    /**
     * to bytes orders
     */
    @FunctionalInterface
    private static interface FixedDataClassToBytes {
        void order(Object obj, byte[] bytes, int offset) throws Exception;
    }
}
