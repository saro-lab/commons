package me.saro.commons.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * web result
 * @author		PARK Yong Seo
 * @since		0.1
 */
@ToString(exclude = {"body", "headers"})
public class WebResult<T> {

    WebResult() {
    }

    // http status
    @Getter @Setter(lombok.AccessLevel.PACKAGE) private int status = -1;

    // exception
    @Getter @Setter(lombok.AccessLevel.PACKAGE) private Exception exception;

    // headers
    @Getter @Setter(lombok.AccessLevel.PACKAGE) private Map<String, List<String>> headers = Collections.emptyMap();

    // response body data
    @Setter(lombok.AccessLevel.PACKAGE) private T body;
    
    @Setter(lombok.AccessLevel.PACKAGE) private String errorBody;

    /**
     * is status 2xx
     * @return
     */
    public boolean isSuccess() {
        return status >= 200 && status < 300;
    }

    /**
     * is status 3xx
     * @return
     */
    public boolean isStatus3xx() {
        return status >= 300 && status < 400;
    }

    /**
     * is status 4xx
     * @return
     */
    public boolean isStatus4xx() {
        return status >= 400 && status < 500;
    }

    /**
     * is status 5xx
     * @return
     */
    public boolean isStatus5xx() {
        return status >= 500 && status < 600;
    }

    /**
     * get response body data
     * @return Optional response body data
     */
    public Optional<T> getBody() {
        return Optional.ofNullable(body);
    }
    
    /**
     * get response body data
     * @param orElse
     * @return
     */
    public T getBody(T orElse) {
        return body != null ? body : orElse;
    }
    
    /**
     * get response error body data
     * @return Optional response body data
     */
    public Optional<String> getErrorBody() {
        return Optional.ofNullable(errorBody);
    }
    
    /**
     * get response error body data
     * @param orElse
     * @return
     */
    public String getErrorBody(String orElse) {
        return errorBody != null ? errorBody : orElse;
    }
}