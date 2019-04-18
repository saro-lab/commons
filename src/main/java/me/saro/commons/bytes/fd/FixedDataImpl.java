package me.saro.commons.bytes.fd;

import com.sun.xml.internal.txw2.IllegalAnnotationException;

import me.saro.commons.bytes.fd.annotations.FixedDataClass;

/**
 * Fixed Data Mapper Impl<br>
 * thead-safe
 * @author      PARK Yong Seo
 * @since       3.1.0
 */
public class FixedDataImpl implements FixedData {

    final Class<?> clazz;
    final FixedDataClass fixedDataClassInfo;
    
    FixedDataImpl(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class must not null !!");
        }
        
        this.clazz = clazz;
        this.fixedDataClassInfo = clazz.getDeclaredAnnotation(FixedDataClass.class);
        init();
    }
    
    private void init() {
        if (fixedDataClassInfo == null) {
            throw new IllegalAnnotationException(clazz.getName() + " is not defined @FixedDataClass");
        }
        
        // get field have data annotation
        
    }
    
    @Override
    public int size() {
        return fixedDataClassInfo.size();
    }

    @Override
    public byte[] bindBytes(Object data, byte[] out, int offset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T toClass(byte[] bytes, int offset) {
        // TODO Auto-generated method stub
        return null;
    }
}
