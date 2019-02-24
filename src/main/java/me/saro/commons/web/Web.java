package me.saro.commons.web;

public class Web {
    /**
     * create get method Web
     * @param url
     * @return
     */
    public static WebBuilder get(String url) {
        return new WebBuilder(url, "GET");
    }

    /**
     * create post method Web
     * @param url
     * @return
     */
    public static WebBuilder post(String url) {
        return new WebBuilder(url, "POST");
    }

    /**
     * create put method Web
     * @param url
     * @return
     */
    public static WebBuilder put(String url) {
        return new WebBuilder(url, "PUT");
    }

    /**
     * create patch method Web
     * @param url
     * @return
     */
    public static WebBuilder patch(String url) {
        return new WebBuilder(url, "PATCH");
    }

    /**
     * create delete method Web
     * @param url
     * @return
     */
    public static WebBuilder delete(String url) {
        return new WebBuilder(url, "DELETE");
    }

    /**
     * create custom method Web
     * @param url
     * @return
     */
    public static WebBuilder custom(String url, String method) {
        return new WebBuilder(url, method);
    }
}
