package me.saro.commons.bytes.fd.annotations;

/**
 *  Date Type
 * @author      PARK Yong Seo
 * @since       1.4
 */
public enum DateDataType {
    
    /**
     * time millis : 8byte long
     */
    millis8,
    
    /**
     * unix time : 4byte int
     */
    unix4,
    
    /**
     * unix time : 8byte long
     */
    unix8
}
