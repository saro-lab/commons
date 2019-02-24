package me.saro.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import me.saro.commons.function.ThrowableTriConsumer;
import me.saro.commons.web.BasicWeb;
import me.saro.commons.web.WebResult;

/**
 * zip util class
 * @author      PARK Yong Seo
 * @since       1.3
 */
public class Zips {
    
    private Zips() {
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
    public static void openStreamNotClose(InputStream inputStream, ThrowableTriConsumer<String, ZipEntry, InputStream> callbackFileInputstream) throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry ze;
        while ((ze = zipInputStream.getNextEntry()) != null) {
            if (!ze.isDirectory()) {
                callbackFileInputstream.accept(ze.getName(), ze, zipInputStream);
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
    public static void openFromFile(File zipfile, ThrowableTriConsumer<String, ZipEntry, InputStream> callbackFileInputstream) throws Exception {
        try (InputStream is = new FileInputStream(zipfile)) {
            openStreamNotClose(is, callbackFileInputstream);
        }
    }

    /**
     * open zip from web
     * @param web
     * @param callbackFileInputstream
     * (String fileName, ZipEntry zipEntry, InputStream inputStream)
     * @throws Exception 
     */
    public static void openFromWeb(BasicWeb web, ThrowableTriConsumer<String, ZipEntry, InputStream> callbackFileInputstream) throws Exception {
        WebResult<String> res; 
        if ((res = web.readRawResultStream(is -> {
            openStreamNotClose(is, callbackFileInputstream);
        })).getException() != null) {
            throw res.getException();
        }
    }
}
