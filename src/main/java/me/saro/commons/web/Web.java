package me.saro.commons.web;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import me.saro.commons.JsonReader;
import me.saro.commons.function.ThrowableConsumer;
import me.saro.commons.function.ThrowableFunction;

/**
 * Web Client
 * @author      PARK Yong Seo
 * @since       0.1
 */
public interface Web {
    /**
     * create get method Web
     * @param url
     * @return
     */
    public static Web get(String url) {
        return new WebImpl(url, "GET");
    }

    /**
     * create post method Web
     * @param url
     * @return
     */
    public static Web post(String url) {
        return new WebImpl(url, "POST");
    }

    /**
     * create put method Web
     * @param url
     * @return
     */
    public static Web put(String url) {
        return new WebImpl(url, "PUT");
    }

    /**
     * create patch method Web
     * @param url
     * @return
     */
    public static Web patch(String url) {
        return new WebImpl(url, "PATCH");
    }

    /**
     * create delete method Web
     * @param url
     * @return
     */
    public static Web delete(String url) {
        return new WebImpl(url, "DELETE");
    }

    /**
     * create custom method Web
     * @param url
     * @return
     */
    public static Web custom(String url, String method) {
        return new WebImpl(url, method);
    }
    
    /**
     * Connect Timeout
     * @param connectTimeout
     * @return
     */
    public Web setConnectTimeout(int connectTimeout);
    
    /**
     * Read Timeout
     * @param readTimeout
     * @return
     */
    public Web setReadTimeout(int readTimeout);
    
    /**
     * set request Charset
     * @param charset
     * @return
     */
    public Web setRequestCharset(String charset);
    
    /**
     * set response charset
     * @param charset
     * @return
     */
    public Web setResponseCharset(String charset);
    
    /**
     * ignore https certificate
     * <br>
     * this method not recommend
     * <br>
     * ignore certificate is defenseless the MITM(man-in-the-middle attack)
     * @param ignoreCertificate
     * @return
     */
    public Web setIgnoreCertificate(boolean ignoreCertificate);
    
    /**
     * add url parameter
     * <br>
     * always append url parameter even post method
     * <br>
     * is not body write
     * @param name
     * @param value
     * @return
     */
    public Web addUrlParameter(String name, String value);
    
    /**
     * set header
     * @param name
     * @param value
     * @return
     */
    public Web setHeader(String name, String value);
    
    /**
     * set header ContentType
     * @param value
     * @return
     */
    public Web setContentType(String value);
    
    /**
     * write body binary
     * @param bytes
     * @return
     */
    public Web writeBody(byte[] bytes);
    
    /**
     * write Body text
     * @param text
     * @return
     */
    public Web writeBody(String text);
    
    /**
     * write json class
     * <br>
     * use jackson lib
     * @param toJsonObject
     * @return
     * @see
     * com.fasterxml.jackson.databind.ObjectMapper
     */
    public Web writeJsonByClass(Object toJsonObject);
    
    /**
     * writeBodyParameter
     * <br>
     * <b>WARNING : </b> is not json type
     * <br>
     * <br>
     * web
     * <br>
     *  .writeBodyParameter("aa", "11")
     * <br>
     * .writeBodyParameter("bb", "22");
     * <br>
     * <b>equals</b>
     * <br>
     * aa=11&amp;bb=22
     * @param name
     * @param value
     * @return
     */
    public Web writeBodyParameter(String name, String value);
    
    /**
     * to Custom result
     * @param result
     * @param function
     * @return
     */
    public <R> WebResult<R> toCustom(WebResult<R> result, ThrowableFunction<InputStream, R> function);
    
    /**
     * to Custom result
     * @param function
     * @return
     */
    public <R> WebResult<R> toCustom(ThrowableFunction<InputStream, R> function);
    
    /**
     * to Map result by JsonObject
     * @return
     */
    public WebResult<Map<String, Object>> toMapByJsonObject();
    
    /**
     * to Map List result by JsonArray
     * @return
     */
    public WebResult<List<Map<String, Object>>> toMapListByJsonArray();
    
    /**
     * to JsonReader
     * @return
     */
    public WebResult<JsonReader> toJsonReader();
    
    /**
     * to Json result by TypeReference
     * @param typeReference
     * @return
     */
    public <T> WebResult<T> toJsonTypeReference(TypeReference<T> typeReference);
    
    /**
     * to text result
     * @return
     */
    public WebResult<String> toPlainText();
    
    /**
     * save file and return WebResult
     * @return WebResult[WebResult]
     */
    public WebResult<File> saveFile(File file, boolean overwrite);
    
    /**
     * readRawResultStream
     * @param reader
     * @return it has Body
     */
    public WebResult<String> readRawResultStream(ThrowableConsumer<InputStream> reader);
}
