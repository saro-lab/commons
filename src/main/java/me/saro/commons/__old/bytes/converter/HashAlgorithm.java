package me.saro.commons.__old.bytes.converter;

/**
 * Converter Hash Algorithm
 * @author		PARK Yong Seo
 * @since		0.2
 */
public enum HashAlgorithm {
    
    /** MD5 : not recommend */
        MD5 ("MD5")
    /** SHA 1 : not recommend */
    ,   SHA1 ("SHA1")
    
    /** SHA 2 : 224 */
    ,   SHA_224 ("SHA-224 ")
    /** SHA 2 : 256 */
    ,   SHA_256 ("SHA-256")
    /** SHA 2 : 384 */
    ,   SHA_384 ("SHA-384")
    /** SHA 2 : 512/224 */
    ,   SHA_512_224 ("SHA-512/224")
    /** SHA 2 : 512/256 */
    ,   SHA_512_256 ("SHA-512/256")
    
    /** SHA 3 : 224 */
    ,   SHA3_224 ("SHA3-224")
    /** SHA 3 : 256 */
    ,   SHA3_256 ("SHA3-256")
    /** SHA 3 : 384 */
    ,   SHA3_384 ("SHA3-384")
    /** SHA 3 : 512 */
    ,   SHA3_512 ("SHA3-512")
    ;
    
    /**
     * value member
     */
    final private String value;
    
    /**
     * constructor
     * @param value
     */
    HashAlgorithm(String value) {
        this.value = value;
    }
    
    /**
     * get value
     * @return
     */
    public String value() {
        return value;
    }
}
