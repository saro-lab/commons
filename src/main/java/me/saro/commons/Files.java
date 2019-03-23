package me.saro.commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;
import java.util.stream.Stream;

import me.saro.commons.function.ThrowableFunction;
import me.saro.commons.function.ThrowablePredicate;

/**
 * file util
 * @author		PARK Yong Seo
 * @since		0.1
 */
public class Files {

    private Files() {
    }

    /**
     * create file use the inputstream
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
        } else {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            } else if (parent.isFile()) {
                throw new IOException("create file error : file exists instend of the directory : " + parent.getAbsolutePath());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file) ; InputStream is = inputStream) {
            Utils.inputStreamReader(is, (buf, len) -> fos.write(buf, 0, len));
        }
    }
    
    /**
     * create file use the string
     * @param file
     * @param overwrite
     * @param value
     * @param charset
     * @throws Exception
     */
    public static void createFile(File file, boolean overwrite, String value, String charset) throws Exception {
        createFile(file, overwrite, new ByteArrayInputStream(value.getBytes(charset)));
    }

    /**
     * get files stream by directory
     * @param directory
     * @return
     */
    public static Stream<File> listFilesStream(File directory) {
        return Stream.of(directory.listFiles());
    }

    /**
     * get files stream by directory
     * @param directory
     * @return
     */
    public static Stream<File> listFilesStream(String directory) {
        return Stream.of(new File(directory).listFiles());
    }
    
    /**
     * read line in the file
     * @param file
     * @param charset
     * @param lineReader
     * @return
     * @throws Exception
     */
    public static <T> T lineReader(File file, String charset, ThrowableFunction<Stream<String>, T> lineReader) throws Exception {
        T t = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            t = Utils.inputStreamLineReader(fis, charset, lineReader);
        }
        return t;
    }
    
    /**
     * read line in the file
     * @param file
     * @param charset
     * @param lineReader
     * @return
     * @throws Exception
     */
    public static <T> T lineReader(String file, String charset, ThrowableFunction<Stream<String>, T> lineReader) throws Exception {
        return lineReader(new File(file), charset, lineReader);
    }
    
    /**
     * to File ext (only lowercase)
     * @param filename
     * @return ex) "gif", "png", "jpg", "zip", "exe", ""
     */
    public static String toFileExt(File filename) {
        String name = filename.getName().toLowerCase();
        int pos;
        return (pos = name.lastIndexOf('.')) != -1 ? name.substring(pos + 1) : "";
    }
    
    /**
     * to File ext (only lowercase)
     * @param filename
     * @return ex) "gif", "png", "jpg", "zip", "exe", ""
     */
    public static String toFileExt(String filename) {
        String name = filename.toLowerCase().replace('\\', '/');
        int pos;
        if ( (pos = name.lastIndexOf('/')) != -1 ) {
            name = name.substring(pos + 1);
        }
        return (pos = name.lastIndexOf('.')) != -1 ? name.substring(pos + 1) : "";
    }
    
    /**
     * valid check for the file ext
     * @param filename
     * @param fileExts only lowercase ex) "gif", "png", "jpg", "zip", "exe", ""
     * @return
     */
    public static boolean validFileExt(File filename, String... fileExts) {
        if (fileExts == null || fileExts.length == 0) {
            throw new IllegalArgumentException("At least one fileExts argument is required.");
        }
        String ext = toFileExt(filename);
        for (String fileExt : fileExts) {
            if (fileExt.equals(ext)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * valid check for the file ext
     * @param filename
     * @param fileExts only lowercase ex) "gif", "png", "jpg", "zip", "exe", ""
     * @return
     */
    public static boolean validFileExt(String filename, String... fileExts) {
        if (fileExts == null || fileExts.length == 0) {
            throw new IllegalArgumentException("At least one fileExts argument is required.");
        }
        String ext = toFileExt(filename);
        for (String fileExt : fileExts) {
            if (fileExt.equals(ext)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * get file infomation
     * @param file
     * @return
     * @throws IOException
     */
    public static BasicFileAttributes toBasicFileAttributes(File file) throws IOException {
        return java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
    }

    /**
     * convert BasicFileAttributes to FileFilter
     * <br>
     * <br>
     * ex) // delete created before 24 hour file in /testpath
     * <br>
     * long before24hour = DateFormat.now().addHours(-24).getTimeInMillis();
     * <br>
		Files.streamFiles("/testpath")
		<br>
		.filter(Files.attributesFilter(attr -> attr.creationTime().toMillis() < before24hour))
		<br>
		.forEach(File::delete);
     * @param file
     * @param filter
     * @return
     */
    public static Predicate<File> attributesFilter(ThrowablePredicate<BasicFileAttributes> filter) {
        return ThrowablePredicate.runtime(e -> filter.test(toBasicFileAttributes(e)));
    }
}
