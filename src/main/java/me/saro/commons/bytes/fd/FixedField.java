package me.saro.commons.bytes.fd;

import java.lang.reflect.Field;

import me.saro.commons.bytes.fd.annotations.BinaryData;
import me.saro.commons.bytes.fd.annotations.DateData;
import me.saro.commons.bytes.fd.annotations.TextData;

public class FixedFieldMapper {
    
    public FixedField(Field field, Class<?> clazz) {
        this.field = field;
        this.text = field.getDeclaredAnnotation(TextData.class);
        this.binary = field.getDeclaredAnnotation(BinaryData.class);
        this.date = field.getDeclaredAnnotation(DateData.class);
    }
    
    final Class<?> clazz;
    final Field field;
    
    final TextData text;
    final BinaryData binary;
    final DateData date;
    
    int offset;
    
    public boolean hasDataAnnotation() {
        return text != null || binary != null || date != null;
    }
}
