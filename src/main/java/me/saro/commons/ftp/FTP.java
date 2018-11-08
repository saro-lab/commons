package me.saro.commons.ftp;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * ftp
 * <br>
 * simple ftp class
 * @author		PARK Yong Seo
 * @since		0.2
 * @see
 * org.apache.commons.net.ftp.FTPClient
 * com.jcraft.jsch.JSch
 */
public interface FTP extends Closeable {
    
    public static FTP openFTP(InetAddress host, int port, String user, String pass) throws IOException {
        return new FTPS(host, port, user, pass, false);
    }
    
    public static FTP openFTP(String host, int port, String user, String pass) throws IOException {
        return new FTPS(InetAddress.getByName(host), port, user, pass, false);
    }
    
    public static FTP openFTPS(InetAddress host, int port, String user, String pass) throws IOException {
        return new FTPS(host, port, user, pass, true);
    }
    
    public static FTP openFTPS(String host, int port, String user, String pass) throws IOException {
        return new FTPS(InetAddress.getByName(host), port, user, pass, true);
    }
    
    public static FTP openSFTP(String host, int port, String user, String pass) throws IOException {
        return new SFTP(host, port, user, pass);
    }
    
    /**
     * move path
     * @param path
     * @return
     * @throws IOException
     */
    public boolean path(String pathname) throws IOException;

    /**
     * get now path
     * @return
     * @throws IOException
     */
    public String path() throws IOException;
    
    /**
     * get file list in now path
     * @return
     * @throws IOException
     */
    public List<String> listFiles() throws IOException;
    
    /**
     * get directory list in now path
     * @return
     * @throws IOException
     */
    public List<String> listDirectories() throws IOException;
    
    /**
     * remove file
     * @param file
     * @return
     * @throws IOException
     */
    public boolean delete(String pathname) throws IOException;
    
    /**
     * send file
     * @param saveServerFileName
     * @param localFile
     * @return
     * @throws IOException
     */
    public boolean send(String saveFileName, File localFile) throws IOException;
    
    /**
     * send file
     * @param localFile
     * @return
     * @throws IOException
     */
    public boolean send(File localFile) throws IOException;

    /**
     * recv file
     * @param serverFileName
     * @param localFile
     * @return
     * @throws IOException
     */
    public boolean recv(String remoteFileName, File localFile) throws IOException;
    
    /**
     * make new directory
     * @param createDirectoryName
     * @return
     * @throws IOException
     */
    public boolean mkdir(String createDirectoryName) throws IOException;
    
    /**
     * close
     */
    public void close();
}
