package me.saro.commons;

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IOStreams {

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

    public static <T> Stream<T> toStream(Iterable<T> iterable, boolean parallel) {
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

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

