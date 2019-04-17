package me.saro.commons.bytes.annotations;

/**
 *  Date Type
 * @author      PARK Yong Seo
 * @since       1.4
 */
public enum FixedDateType {
    
    /**
     * time millis 8byte binary
     */
    millis8,
    
    /**
     * unix time 4byte binary
     */
    unixTime4,
    
    /**
     * unix time 8byte binary
     */
    unixTime8,
    
    /**
     * textFormat
     */
    textFormat
}
