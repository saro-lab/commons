package me.saro.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import me.saro.commons.lambdas.ThrowableTriConsumer;
import me.saro.commons.web.Web;

/**
 * common utils
 * @author		PARK Yong Seo
 * @since		1.0.0
 */
public class Utils {
	
	private Utils() {
	}
	
	final static long TIME_MILLIS_UNIT_DAY = 86_400_000;
	final static char[] BASE62_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

	/**
	 * Null Value Logic
	 * 
	 * @param list
	 * nullable data
	 * @return
	 *  - first not null data
	 *  <br>
	 *  - if has not null data return null
	 */
	@SafeVarargs
	public static <T> T nvl(T... list) {
		for (T t : list) {
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Empty Value Logic
	 * 
	 * @param list
	 * nullable String
	 * @return
	 *  - first not null and not empty string
	 *  <br>
	 *  - if not found return null
	 */
	public static String evl(String... list) {
		for (String val : list) {
			if (val != null && !val.isEmpty()) {
				return val;
			}
		}
		return null;
	}
	
	/**
	 * create random string
	 * @param mold
	 * base mold for create random string
	 * @param len
	 * create langth
	 * @return
	 * random string
	 */
	public static String createRandomString(char[] mold, int len) {
		char[] rv = new char[len];
		int charLen = mold.length;

		for (int i = 0 ;i < len ; i++) {
			rv[i] = mold[(int)(Math.random() * charLen)];
		}

		return new String(rv);
	}
	
	/**
	 * create random string
	 * @param mold
	 * base mold for create random string
	 * @param min
	 * min length
	 * @param max
	 * max length
	 * @return
	 * create random string
	 * min &lt;= return value &lt;= max
	 */
	public static String createRandomString(char[] mold, int min, int max) {
		return createRandomString(mold, (int)random(min, max));
	}

	/**
	 * create random base62 string
	 * <br>
	 * base62 : [ A-Z a-z 0-9 ]
	 * @param min
	 * min length
	 * @param max
	 * max length
	 * @return
	 * 
	 */
	public static String createRandomBase62String(int min, int max) {
		return createRandomString(BASE62_CHARS, (int)random(min, max));
	}
	
	/**
	 * get random
	 * @param min
	 * min length
	 * @param max
	 * max length
	 * @return
	 * min &lt;= return value &lt;= max
	 */
	public static long random(long min, long max) {
		if (min == max) {
			return min;
		} else if (min > max) {
			throw new IllegalArgumentException("'lessThen' have to over the value then 'min'");
		}
		return min + (int)(Math.random() * ((max + 1) - min));
	}
	
	/**
	 * read zip file
	 * <br>
	 * <b>WARNING : </b> is not auto closed
	 * @param inputStream
	 * @param callbackFileInputstream
	 * (String fileName, ZipEntry zipEntry, InputStream inputStream)
	 * @throws Exception
	 */
	public static void openZipStreamNotClose(InputStream inputStream, ThrowableTriConsumer<String, ZipEntry, InputStream> callbackFileInputstream) throws Exception {
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		ZipEntry ze;
		while ((ze = zipInputStream.getNextEntry()) != null) {
			if (!ze.isDirectory()) {
				callbackFileInputstream.accept(ze.getName(), ze, inputStream);
			}
			zipInputStream.closeEntry();
		}
	}
	
	/**
	 * open zip from file
	 * @param zipfile
	 * @param callbackFileInputstream
	 * (String fileName, ZipEntry zipEntry, InputStream inputStream)
	 * @throws Exception
	 */
	public static void openZipFromFile(File zipfile, ThrowableTriConsumer<String, ZipEntry, InputStream> callbackFileInputstream) throws Exception {
		try (InputStream is = new FileInputStream(zipfile)) {
			openZipStreamNotClose(is, callbackFileInputstream);
		}
	}
	
	/**
	 * open zip from web
	 * @param web
	 * @param callbackFileInputstream
	 * (String fileName, ZipEntry zipEntry, InputStream inputStream)
	 * @throws IOException
	 */
	public static void openZipFromWeb(Web web, ThrowableTriConsumer<String, ZipEntry, InputStream> callbackFileInputstream) throws IOException {
		if (!web.readRawResultStream(is -> {
			openZipStreamNotClose(is, callbackFileInputstream);
		})) {
			throw new IOException("Fail load WEB URL");
		}
	}
}
