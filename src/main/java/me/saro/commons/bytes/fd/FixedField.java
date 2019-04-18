package me.saro.commons.bytes.fd;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.saro.commons.bytes.fd.annotations.BinaryData;
import me.saro.commons.bytes.fd.annotations.DateData;
import me.saro.commons.bytes.fd.annotations.TextData;

@AllArgsConstructor @NoArgsConstructor
public class FixedField {
    Field field;
    int offset;
    
    TextData text;
    BinaryData binary;
    DateData date;
}
