package me.saro.commons.web;

import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Web Ignore Certificate
 * @author		PARK Yong Seo
 * @since		1.0.0
 */
public class WebIgnoreCertificate {
	private WebIgnoreCertificate() {
	}
	
	final static TrustManager[] ALL_TRUST_MANAGER;
	
	final static HostnameVerifier IGNORE_HOSTNAME_VERIFIER;
    
    final static SSLContext ALL_TRUST_SSLCONTEXT;
    
    static {
    	ALL_TRUST_MANAGER = new TrustManager[] {
    			new X509TrustManager() {
    				@Override
    				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    					return null;
    				}
    				@Override
    				public void checkClientTrusted(X509Certificate[] certs, String authType) {
    				}
    				@Override
    				public void checkServerTrusted(X509Certificate[] certs, String authType) {
    				}
    			}
    	};
    	
    	SSLContext sslContext = null;
    	try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, ALL_TRUST_MANAGER, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
    	
    	ALL_TRUST_SSLCONTEXT = sslContext;
    	
    	IGNORE_HOSTNAME_VERIFIER = new HostnameVerifier() {
    		@Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
    
    /**
     * casing ignore certificate to HttpURLConnection
     * @param connection
     */
	public static void ignoreCertificate(HttpURLConnection connection) {
		((HttpsURLConnection)connection).setSSLSocketFactory(ALL_TRUST_SSLCONTEXT.getSocketFactory());
		((HttpsURLConnection)connection).setHostnameVerifier(IGNORE_HOSTNAME_VERIFIER);
		
	}
}
