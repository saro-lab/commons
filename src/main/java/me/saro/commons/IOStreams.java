package me.saro.commons;

import me.saro.commons.function.ThrowableFunction;

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IOStreams {

    public static int BUFSIZE = 8192;

    /**
     *
     * @param is
     * @param charset
     * @return
     * @throws IOException
     */
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

    /**
     *
     * @param src
     * @param descBytes
     * @param descBytesOffset
     * @throws IOException
     */
    public static void bind(InputStream src, byte[] descBytes, int descBytesOffset) throws IOException {
        try (src) {
            int len;
            int idx = descBytesOffset;
            var buf = new byte[BUFSIZE];
            while ((len = src.read(buf, 0, BUFSIZE)) > -1) {
                System.arraycopy(buf, 0, descBytes, idx, len);
                idx += len;
            }
        }
    }

    /**
     *
     * @param is
     * @param out
     * @throws IOException
     */
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

    /**
     *
     * @param is
     * @param charset
     * @param process
     * @param <R>
     * @return
     * @throws Exception
     */
    public static <R> R lines(InputStream is, String charset, ThrowableFunction<Stream<String>, R> process) throws Exception {
        try (is ; var isr = new InputStreamReader(is, charset) ; var br = new BufferedReader(isr)) {
            return process.apply(br.lines());
        }
    }

    /**
     *
     * @param iterable
     * @param parallel
     * @param <T>
     * @return
     */
    public static <T> Stream<T> toStream(Iterable<T> iterable, boolean parallel) {
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

    /**
     *
     * @param enumeration
     * @param parallel
     * @param <T>
     * @return
     */
    public static <T> Stream<T> toStream(Enumeration<T> enumeration, boolean parallel) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {
            public T next() {
                return enumeration.nextElement();
            }
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }
        }, Spliterator.ORDERED), parallel);
    }
}

