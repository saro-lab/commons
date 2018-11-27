package me.saro.commons;

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
        } else {
            File parent = file.getParentFile();
            if (parent == null) {
                throw new IOException("create file error : invalid child file for create directory : " + file.getAbsolutePath());
            }
            parent.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(file) ; InputStream is = inputStream) {
            Utils.inputStreamReader(is, (buf, len) -> fos.write(buf, 0, len));
        }
    }

    /**
     * get files stream by path
     * @param directory
     * @return
     */
    public static Stream<File> streamFiles(File directory) {
        return Stream.of(directory.listFiles());
    }

    /**
     * get files stream by path
     * @param directory
     * @return
     */
    public static Stream<File> streamFiles(String directory) {
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
