package me.saro.commons.__old.bytes.fd;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import me.saro.commons.__old.bytes.DateFormat;
import me.saro.commons.__old.bytes.Bytes;
import me.saro.commons.__old.bytes.fd.annotations.DateData;
import me.saro.commons.__old.bytes.fd.annotations.DateDataType;
import me.saro.commons.__old.bytes.fd.annotations.FixedDataClass;

/**
 * FixedMethodDateType
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public class FixedMethodDateType implements FixedMethod {
    
    final DateData meta;
    final String parentClassName;
    final FixedDataClass fixedDataClassInfo;

    FixedMethodDateType(FixedDataClass fixedDataClassInfo, String parentClassName, DateData dateDate) {
        this.fixedDataClassInfo = fixedDataClassInfo;
        this.meta = dateDate;
        this.parentClassName = parentClassName;
    }

    @Override
    public FixedMethodConsumer toBytes(Method method) {
        int offset = meta.offset();
        boolean le = !fixedDataClassInfo.bigEndian();
        DateDataType type = meta.type();
        String genericReturnTypeName = method.getGenericReturnType().getTypeName();
        
        switch (genericReturnTypeName) {
            case "java.util.Date" : 
                switch (type) {
                    case millis8 :return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes(((Date)method.invoke(val)).getTime()), le), 0, bytes, offset + idx, 8);
                    case unix8 : return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes((((Date)method.invoke(val))).getTime() / 1000), le), 0, bytes, offset + idx, 8);
                    case unix4 : return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes((int)((((Date)method.invoke(val))).getTime() / 1000L)), le), 0, bytes, offset + idx, 4);
                }
                break;
            case "java.util.Calendar" : 
                switch (type) {
                    case millis8 :return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes(((Calendar)method.invoke(val)).getTimeInMillis()), le), 0, bytes, offset + idx, 8);
                    case unix8 : return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes((((Calendar)method.invoke(val)).getTimeInMillis()) / 1000), le), 0, bytes, offset + idx, 8);
                    case unix4 : return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes((int)((((Calendar)method.invoke(val)).getTimeInMillis()) / 1000)), le), 0, bytes, offset + idx, 4);
                }
                break;
            case "me.saro.commons.__old.bytes.DateFormat" :
                switch (type) {
                    case millis8 :return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes(((DateFormat)method.invoke(val)).getTimeInMillis()), le), 0, bytes, offset + idx, 8);
                    case unix8 : return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes((((DateFormat)method.invoke(val)).getTimeInMillis()) / 1000), le), 0, bytes, offset + idx, 8);
                    case unix4 : return (bytes, idx, val) -> System.arraycopy(Bytes.reverse(Bytes.toBytes((int)((((DateFormat)method.invoke(val)).getTimeInMillis()) / 1000)), le), 0, bytes, offset + idx, 4);
                }
                break;
        }
        throw new IllegalArgumentException("does not support type, support type [Date, Calendar, DateFormat] : "+genericReturnTypeName+" " + method.getName() + "() in " + parentClassName);
    }

    @Override
    public FixedMethodConsumer toClass(Method method) {
        int offset = meta.offset();
        boolean le = !fixedDataClassInfo.bigEndian();
        DateDataType type = meta.type();
        String genericParameterTypeName = method.getGenericParameterTypes()[0].getTypeName();
        
        switch (genericParameterTypeName) {
        case "java.util.Date" : 
            switch (type) {
                case millis8 :return (bytes, idx, val) -> method.invoke(val, new Date(Bytes.toLong(Bytes.copy(bytes, idx + offset, 8, le))));
                case unix8 : return (bytes, idx, val) -> method.invoke(val, new Date(Bytes.toLong(Bytes.copy(bytes, idx + offset, 8, le)) * 1000));
                case unix4 : return (bytes, idx, val) -> method.invoke(val, new Date(Integer.toUnsignedLong(Bytes.toInt(Bytes.copy(bytes, idx + offset, 4, le))) * 1000L));
            }
            break;
        case "java.util.Calendar" : 
            switch (type) {
                case millis8 :return (bytes, idx, val) -> method.invoke(val, DateFormat.parse(Bytes.toLong(Bytes.copy(bytes, idx + offset, 8, le))).toCalendar());
                case unix8 : return (bytes, idx, val) -> method.invoke(val, DateFormat.parse(Bytes.toLong(Bytes.copy(bytes, idx + offset, 8, le)) * 1000).toCalendar());
                case unix4 : return (bytes, idx, val) -> method.invoke(val, DateFormat.parse(Integer.toUnsignedLong(Bytes.toInt(Bytes.copy(bytes, idx + offset, 4, le))) * 1000L).toCalendar());
            }
            break;
        case "me.saro.commons.__old.bytes.DateFormat" :
            switch (type) {
                case millis8 :return (bytes, idx, val) -> method.invoke(val, DateFormat.parse(Bytes.toLong(Bytes.copy(bytes, idx + offset, 8, le))));
                case unix8 : return (bytes, idx, val) -> method.invoke(val, DateFormat.parse(Bytes.toLong(Bytes.copy(bytes, idx + offset, 8, le)) * 1000));
                case unix4 : return (bytes, idx, val) -> method.invoke(val, DateFormat.parse(Integer.toUnsignedLong(Bytes.toInt(Bytes.copy(bytes, idx + offset, 4, le))) * 1000L));
            }
            break;
    }
        throw new IllegalArgumentException("does not support type, support type [Date, Calendar, DateFormat] : " + method.getName() + "("+genericParameterTypeName+") in the " + parentClassName);
    }

}
