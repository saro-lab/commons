package me.saro.commons;

import java.io.*;

public class IOUtils {

    public static int BUFSIZE = 8192;

    public static String toString(InputStream is, String charset) throws IOException {
        try (is ; var isr = new InputStreamReader(is, charset)) {
            int len;
            var sb = new StringBuilder(BUFSIZE);
            var buf = new char[BUFSIZE];
            while ((len = isr.read(buf, 0, BUFSIZE)) > -1) {
                sb.append(buf, 0, len);
            }
            return sb.toString();
        }
    }

    public static void bind(InputStream is, byte[] bytes, int offset) throws IOException {
        try (is) {

        }
    }

    public static void link(InputStream is, OutputStream out) throws IOException {
        try (is ; out) {
            int len;
            var buf = new byte[BUFSIZE];
            while ((len = is.read(buf, 0, BUFSIZE)) > -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        }
    }
}
