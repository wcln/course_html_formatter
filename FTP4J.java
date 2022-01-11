package Perepelken.colin;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

// http://www.sauronsoftware.it/projects/ftp4j/manual.php#6

public class FTP4J {
	
	/**** FTP SETTINGS *****/
	private final String DOMAIN_NAME = "";
	private final String LOGIN = "";
	private final String PASSWORD = "";
	/************************/
	
	private FTPClient client;
	    
    public void connectToServer() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
    	TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
    		public X509Certificate[] getAcceptedIssuers() {
    			return null;
    		}
    		public void checkClientTrusted(X509Certificate[] certs, String authType) {
    		}
    		public void checkServerTrusted(X509Certificate[] certs, String authType) {
    		}
    	} };
    	SSLContext sslContext = null;
    	try {
    		sslContext = SSLContext.getInstance("SSL");
    		sslContext.init(null, trustManager, new SecureRandom());
    	} catch (NoSuchAlgorithmException e) {
    		e.printStackTrace();
    	} catch (KeyManagementException e) {
    		e.printStackTrace();
    	}
    	SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
    	client = new FTPClient();
    	client.setSSLSocketFactory(sslSocketFactory);
        client.setSecurity(FTPClient.SECURITY_FTPES); // FTP over explicit TLS/SSL
        client.connect(DOMAIN_NAME); //conect to FTP server (in my case a vsftp on centos 6.4)
        client.login(LOGIN, PASSWORD);//login to FTP Server
    }

    public void transfer(String src, String dest) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        client.changeDirectory(dest); //tell FTP4J where on the Ftp Server to send your file that you want to upload.
        File fileUpload = new File (src); //point FTP4J to the file you want to upload
        client.upload(fileUpload); //upload it
        System.out.println("Upload Successful.");
    }
    
    public void disconnectFromServer() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
    	client.disconnect(true);
    }

}