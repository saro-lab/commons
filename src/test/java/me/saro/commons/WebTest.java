package me.saro.commons;

import org.junit.Test;

public class WebTest {
	
	@Test
    public void test() throws Exception {
		
		// ssl cert ignore test
		//String sslErrorUrl = "https://cert-error.saro.me";
		//assertFalse(Web.get(sslErrorUrl).toPlainText().isSuccess());
		//assertTrue(Web.get(sslErrorUrl).setIgnoreCertificate(true).toPlainText().isSuccess());
		
		
    }
	
}
