package me.saro.commons.converter;

/**
 * Converter Hash Algorithm
 * @author		PARK Yong Seo
 * @since		0.2
 */
public enum HashAlgorithm {
	MD5 ("MD5")	
,	SHA1 ("SHA1")
	
,	SHA_224 ("SHA-224 ")
,	SHA_256 ("SHA-256")
,	SHA_384 ("SHA-384")
,	SHA_512_224 ("SHA-512/224")
,	SHA_512_256 ("SHA-512/224")

,	SHA3_224 ("SHA3-224")
,	SHA3_256 ("SHA3-256")
,	SHA3_384 ("SHA3-384")
,	SHA3_512 ("SHA3-512")
;
	final private String value;
	HashAlgorithm(String value) {
		this.value = value;
	}
	public String value() {
		return value;
	}
}
