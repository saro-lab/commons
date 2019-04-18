package me.saro.commons.bytes;

/**
 * FixedDataConverter
 * @author      PARK Yong Seo
 * @since       3.0.3
 */
public interface _FixedDataConverter {
    byte[] toBytes();
    void bindBytes(byte[] bytes);
}
