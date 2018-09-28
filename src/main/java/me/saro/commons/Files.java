package me.saro.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * file util
 * @author		PARK Yong Seo
 * @since		1.0.0
 */
public class Files {

	private Files() {
	}
	
	/**
	 * create parent directory for child file
	 * <br>
	 * ex1) createParentDirectoryForFile(new File('/aaa/bbb/ccc/aaa.txt'))
	 * <br>
	 * <br>
	 * create directory : /aaa/bbb/ccc
	 * <br>
	 * <br>
	 * ex2) createParentDirectoryForFile(new File('/aaa/bbb/ccc'))
	 * <br>
	 * create directory : /aaa/bbb
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean createParentDirectoryForFile(File directoryChildFile) throws IOException {
		File parent = directoryChildFile.getParentFile();
		if (parent == null) {
			throw new IOException("create file error : invalid child file for create directory : " + directoryChildFile.getAbsolutePath());
		}
		
		if (parent.mkdirs()) {
			return true;
		}
		throw new IOException("create directory error : " + parent.getAbsolutePath());
	}
	
	/**
	 * create file useing the inputstream
	 * @param file
	 * @param overwrite
	 * @param inputStream
	 * @throws Exception
	 */
	public static void createFile(File file, boolean overwrite, InputStream inputStream) throws Exception {
		if (file.exists()) {
			if (overwrite) {
				file.delete();
			} else {
				throw new IOException("create file error : already exists file : " + file.getAbsolutePath());
			}
		}
		
		createParentDirectoryForFile(file);
		
		try (FileOutputStream fos = new FileOutputStream(file) ; InputStream is = inputStream) {
			Utils.inputStreamReader(is, (buf, len) -> fos.write(buf, 0, len));
		}
	}
}
