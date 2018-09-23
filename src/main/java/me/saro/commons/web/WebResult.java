package me.saro.commons.web;

import java.util.Optional;

import lombok.Getter;
import lombok.ToString;

/**
 * web result
 * @author		PARK Yong Seo
 * @since		1.0.0
 */
@ToString(exclude = {"data"})
public class WebResult<T> {

    WebResult() {
    }
    
    // http status
    @Getter int status;
    
    // exception
    @Getter Exception exception;
    
    // response body data
    T body;

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
}