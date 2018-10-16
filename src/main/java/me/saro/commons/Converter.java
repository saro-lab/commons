package me.saro.commons;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.saro.commons.converter.HashAlgorithm;
import me.saro.commons.converter.NamingConvention;
import me.saro.commons.function.ThrowableBiFunction;
import me.saro.commons.function.ThrowableFunction;


/**
 * Converter
 * @author		PARK Yong Seo
 * @since		0.1
 */
public class Converter {

    private Converter() {
    }

    // BASE BUFFER
    final static int BUFSIZE = 4096;

    // JSON
    final static ObjectMapper JSON_MAPPER = new ObjectMapper();
    final static TypeReference<Map<String, Object>> JSON_MAP = new TypeReference<Map<String,Object>>() {};
    final static TypeReference<List<Map<String, Object>>> JSON_MAP_LIST = new TypeReference<List<Map<String,Object>>>() {};

    // HAX convert array [00 ~ FF]
    final static char[][] BYTE_TO_HEX_STR_MAP = IntStream.range(0, 256).boxed()
            .map(i -> String.format("%02x", i).toCharArray()).toArray(char[][]::new);

    /**
     * split csv line
     * @param line
     * csv line
     * @return
     * line value array
     */
    public static String[] splitCsvLine(String line) {
        if (line == null) {
            return new String[0];
        }
        String[] rv = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String tmp;
        for (int i = 0 ; i < rv.length ; i++) {
            if ((tmp = rv[i]).indexOf('"') == 0) {
                rv[i] = tmp.substring(1, Math.max(tmp.length() - 1, 1));
            }
        }
        return rv;
    }

    /**
     * asList
     * <br>
     * this asList different Arrays.asList
     * <br>
     * this method List is ArrayList
     * <br>
     * can editable list
     * @param args
     * @return
     */
    @SafeVarargs
    public static <T> List<T> asList(T... args) {
        List<T> list = new ArrayList<>();
        if (args != null) {
            for (T t : args) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * InputStream to ByteArrayOutputStream
     * <br>
     * this function not close inputstream
     * @param is
     * @param bufferSize
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream is, final int bufferSize) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
        byte[] buf = new byte[bufferSize];
        int len;
        while ((len = is.read(buf, 0, bufferSize)) > 0) {
            baos.write(buf, 0, len);
        }
        return baos;
    }

    /**
     * InputStream to byte[]
     * @param is
     * @param bufferSize
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(InputStream is, final int bufferSize) throws IOException {
        return toByteArrayOutputStream(is, bufferSize).toByteArray();
    }

    /**
     * deserialize json (Object, Array) to Class by TypeReference
     * <br>
     * casing IOException to RuntimeException
     * @param json
     * @param typeReference
     * @return
     * @see
     * com.fasterxml.jackson.databind.ObjectMapper
     */
    public static <T> T toClassByJson(String json, TypeReference<T> typeReference) {
        try {
            return json != null ? JSON_MAPPER.readValue(json, typeReference) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * deserialize json Object to Map [String, Object]
     * <br>
     * casing IOException to RuntimeException
     * @param jsonObject
     * @return
     * jsonObject != null to Map [String, Object]
     * jsonObject == null to empty Map
     * @see
     * com.fasterxml.jackson.databind.ObjectMapper
     */
    public static Map<String, Object> toMapByJsonObject(String jsonObject) {
        try {

            return jsonObject != null ? JSON_MAPPER.readValue(jsonObject, JSON_MAP) : Collections.emptyMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * deserialize json Arrays to List [ Map [String, Object] ]
     * <br>
     * casing IOException to RuntimeException
     * @param jsonArray
     * @return
     * jsonArray != null to List [ Map [String, Object] ]
     * jsonArray == null to empty List
     * @see
     * com.fasterxml.jackson.databind.ObjectMapper
     */
    public static List<Map<String, Object>> toMapListByJsonArray(String jsonArray) {
        try {
            return jsonArray != null ? JSON_MAPPER.readValue(jsonArray, JSON_MAP_LIST) : Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * to Hash
     * @param hashAlgorithm
     * SHA3 need to min jdk version 10
     * @param data
     * @return
     * @since 0.2
     */
    public static byte[] toHash(HashAlgorithm hashAlgorithm, byte[] data) {
        try {
            return MessageDigest.getInstance(hashAlgorithm.value()).digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * to Hash
     * @param hashAlgorithm
     * @param text
     * SHA3 need to min jdk version 10
     * @param charset
     * @return
     * @since 0.2
     */
    public static byte[] toHash(HashAlgorithm hashAlgorithm, String text, String charset) {
        return toHash(hashAlgorithm, text.getBytes(Charset.forName(charset)));
    }

    /**
     * to Hash
     * <br>
     * charset is UTF-8
     * @param hashAlgorithm
     * @param text
     * SHA3 need to min jdk version 10
     * @return
     * @since 0.2
     */
    public static byte[] toHash(HashAlgorithm hashAlgorithm, String text) {
        return toHash(hashAlgorithm, text.getBytes(Charset.forName("UTF-8")));
    }

    /**
     * bytes to hex
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        StringBuilder rv = new StringBuilder((bytes.length * 2) + 10);
        for (byte b : bytes) {
            rv.append(BYTE_TO_HEX_STR_MAP[((int) b) & 0xff]);
        }
        return rv.toString();
    }

    /**
     * class to json String
     * @param clazz
     * @return
     * json String
     * @see
     * com.fasterxml.jackson.databind.ObjectMapper
     */
    public static String toJson(Object clazz) {
        try {
            return JSON_MAPPER.writeValueAsString(clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Iterable to stream
     * @param iterable
     * @param parallel
     * @return
     */
    public static <T> Stream<T> toStream(Iterable<T> iterable, boolean parallel) {
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

    /**
     * Iterable to stream
     * @param iterable
     * @return
     */
    public static <T> Stream<T> toStream(Iterable<T> iterable) {
        return toStream(iterable, false);
    }

    /**
     * Enumeration to Stream
     * @param enumeration
     * @param parallel
     * @return
     */
    public static <T> Stream<T> toStream(Enumeration<T> enumeration, boolean parallel) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new Iterator<T>() {
                    public T next() {
                        return enumeration.nextElement();
                    }
                    public boolean hasNext() {
                        return enumeration.hasMoreElements();
                    }
                },
                Spliterator.ORDERED), parallel
                );
    }

    /**
     * Enumeration to Stream
     * @param enumeration
     * @return
     */
    public static <T> Stream<T> toStream(Enumeration<T> enumeration) {
        return toStream(enumeration, false);
    }

    /**
     * text InputStream to Stream<String line>
     * <br>
     * <b>WARNING : </b> this method not close InputStream
     * @param inputStream
     * @param charset
     * @return
     * @throws IOException
     */
    public static Stream<String> toStreamLineNotCloseByTextInputStream(InputStream inputStream, String charset) throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream, Charset.forName(charset))).lines();
    }

    /**
     * Enumeration to List
     * @param enumeration
     * @return
     * @see
     * java.util.Collections
     */
    public static <T> List<T> toList(Enumeration<T> enumeration) {
        return Collections.list(enumeration);
    }

    /**
     * Iterable to List
     * @param iterable
     * @return
     */
    public static <T> List<T> toList(Iterable<T> iterable) {
        return toStream(iterable, false).collect(Collectors.toList());
    }

    /**
     * simple to map
     * <br>
     * use LinkedHashMap
     * <br>
     * <b>WARNING : </b> this function not checked entries class type
     * @param entries
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(Object... entries) {
        Map<K, V> map = new LinkedHashMap<>();
        if (entries != null) {
            int len = entries.length;
            if (len % 2 == 1) {
                throw new IllegalArgumentException("parameter is (key1, val1, ... key[n], val[n])");
            }
            for (int i = 0 ; i < len ;) {
                K k = (K)entries[i++];
                V v = (V)entries[i++];
                map.put(k, v);
            }
        }
        return map;
    }

    /**
     * ResultSet to Stream
     * @param resultSet
     * ResultSet
     * @param map
     * now cursor resultset
     * @return
     * @throws SQLException
     */
    public static <R> Stream<R> toStreamByResultSet(ResultSet resultSet, ThrowableFunction<ResultSet, R> map) throws SQLException {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator <R> (Long.MAX_VALUE, Spliterator.ORDERED) {
            @Override
            public boolean tryAdvance(Consumer<? super R> action) {
                try {
                    if(!resultSet.next()) {
                        return false;
                    }
                    action.accept(map.apply(resultSet));
                    return true;
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, false);
    }

    /**
     * ResultSet to Stream
     * @param rresultSets
     * ResultSet
     * @param map
     *  (String[] columnNames, Object[] columnValues) : return
     *  columnNames and columnValues index is Zero-based db selected orderd
     * @return
     * @throws SQLException
     */
    public static <R> Stream<R> toStreamByResultSet(ResultSet resultSet, ThrowableBiFunction<String[], Object[], R> map) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0 ; i <= columnCount ; ) {
            columnNames[i++] = rsmd.getColumnName(i);
        }
        return toStreamByResultSet(resultSet, rs -> {
            Object[] vals = new Object[columnCount];
            for (int i = 0 ; i <= columnCount ; ) {
                vals[i++] = rs.getObject(i);
            }
            return map.apply(columnNames, vals);
        });
    }

    /**
     * Exception to String 
     * @param e
     * Exception
     * @return
     * errorMessage\n
     * <br>
     * (StackTrace + \n)...
     */
    public static String toString(Exception e) {
        StringBuilder sb = new StringBuilder(512);
        sb.append(e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            sb.append('\n').append(stackTraceElement.toString());
        }
        return sb.toString();
    }

    /**
     * InputStream to String
     * <br>
     * <b>WARNING : </b> this method not close InputStream
     * @param is
     * @param charset
     * @return
     * @throws IOException
     */
    public static String toStringNotClose(InputStream is, String charset) throws IOException {
        int len;
        StringBuilder sb = new StringBuilder(BUFSIZE);
        char[] buf = new char[BUFSIZE];
        try {
            // not close InputStreamReader
            // cus InputStreamReader effect InputStream
            InputStreamReader isr = new InputStreamReader(is, charset);
            while ((len = isr.read(buf, 0, BUFSIZE)) > -1) {
                sb.append(buf, 0, len);
            }
        } catch (IOException e) {
            throw e;
        }
        return sb.toString();
    }

    /**
     * text file to String
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static String toString(File file, String charset) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return toStringNotClose(fis, charset);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * InputStream to String
     * @param inputStream
     * @param charset
     * @return
     * @throws IOException
     */
    public static String toString(InputStream inputStream, String charset) throws IOException {
        try (InputStream is = inputStream) {
            return toStringNotClose(is, charset);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * naming convention
     * @param fromNamingConvention
     * @param naming
     * @return
     * @since 0.2
     */
    public static List<String> namingConvention(NamingConvention fromNamingConvention, String naming) {
        if (naming.isEmpty()) {
            return Collections.emptyList();
        }
        switch (fromNamingConvention) {
        case dashes : return Arrays.asList(naming.toLowerCase().split("\\_"));
        case underscores : return Arrays.asList(naming.toLowerCase().split("\\-"));
        case camelCase : case pascalCase : {
            List<String> list = new ArrayList<>();
            char[] ch = naming.toCharArray();
            ch[0] = Character.toLowerCase(ch[0]);
            int pos = 0, i = 1;
            for ( ; i < ch.length ; i++ ) {
                if (Character.isUpperCase(ch[i])) {
                    ch[i] = Character.toLowerCase(ch[i]);
                    list.add(new String(ch, pos, i));
                    pos = i;
                }
            }
            list.add(new String(ch, pos, i));
            return list;
        }
        }
        throw new IllegalArgumentException("unknown naming convention");
    }

    /**
     * naming convention
     * @param toNamingConvention
     * @param words
     * @return
     * @since 0.2
     */
    public static String namingConvention(NamingConvention toNamingConvention, List<String> words) {
        Stream<String> wordStream = words.stream();
        switch (toNamingConvention) {
        case dashes : return wordStream.collect(Collectors.joining("-"));
        case underscores : return wordStream.collect(Collectors.joining("_"));
        case pascalCase : return wordStream.map(e -> e.substring(0, 1).toUpperCase() + e.substring(1)).collect(Collectors.joining(""));
        case camelCase : {
            switch (words.size()) {
            case 0 : return "";
            case 1 : return words.get(0);
            default :
                return words.get(0) + wordStream.skip(1).map(e -> e.substring(0, 1).toUpperCase() + e.substring(1)).collect(Collectors.joining(""));
            }
        }
        }
        throw new IllegalArgumentException("unknown naming convention");
    }

    /**
     * naming convention
     * @param fromNamingConvention
     * @param toNamingConvention
     * @param words
     * @return
     * @since 0.2
     */
    public static String namingConvention(NamingConvention fromNamingConvention, NamingConvention toNamingConvention, String naming) {
        return namingConvention(toNamingConvention, namingConvention(fromNamingConvention, naming));
    }

}
