package me.saro.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
    private static boolean createParentDirectoryForFile(File directoryChildFile) throws IOException {
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
