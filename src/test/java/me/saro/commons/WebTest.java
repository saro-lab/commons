package me.saro.commons;

import org.junit.jupiter.api.Test;

import me.saro.commons.web.Web;
import me.saro.commons.web.WebResult;

public class WebTest {

    @Test
    public void test() throws Exception {
        //webTest();
    }
    
    public void webTest() {
        
        printResult("normal", Web.get("https://saro.me").toPlainText());
        printResult("error type cast", Web.get("https://saro.me").toMapByJsonObject());
        printResult("error http status", Web.get("https://saro.me/error").toPlainText());
    }
    
    public <T> void printResult(String title, WebResult<T> res) {
        System.out.println("## - " + title);
        System.out.println(res);
        System.out.println();
        
        System.out.println("header");
        res.getHeaders().forEach((k, v) -> System.out.println(" - " + k + " : " + v));
        System.out.println();
        
        System.out.println("success : " + res.isSuccess());
        System.out.println("hasBody : " + res.hasBody());
        System.out.println("body\n" + res.getBody());
        System.out.println();
        System.out.println("error body\n" + res.getErrorBody());
        System.out.println();
        System.out.println("error body(D)\n" + res.getErrorBody("N/A"));
        System.out.println();
        System.out.println("exception\n" + Converter.toString(res.getException()));
        System.out.println();
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

}
