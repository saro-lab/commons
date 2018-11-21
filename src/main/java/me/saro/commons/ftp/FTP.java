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
     * change directory<br>
     * same method path(), cd()
     * @return
     * @throws IOException 
     */
    default public boolean cd(String pathname) throws IOException {
        return path(pathname);
    }
    
    /**
     * move path<br>
     * same method path(), cd()
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
     * has file in path
     * @param filename
     * @return
     * @throws IOException
     */
    public boolean hasFile(String filename) throws IOException;
    
    /**
     * has directory in path
     * @param directoryname
     * @return
     * @throws IOException
     */
    public boolean hasDirectory(String directoryname) throws IOException;
    
    /**
     * remove file
     * @param file
     * @return
     * @throws IOException
     */
    public boolean delete(String filename) throws IOException;
    
    /**
     * send file
     * @param saveFilename
     * @param localFile
     * @return
     * @throws IOException
     */
    public boolean send(String saveFilename, File localFile) throws IOException;
    
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
    public boolean recv(String remoteFilename, File localFile) throws IOException;
    
    /**
     * recv file list
     * @param remoteFilenameList
     * @param localDirectory
     * @return
     */
    default public void recv(List<String> remoteFilenameList, File localDirectory) throws IOException {
        if (!localDirectory.isDirectory()) {
            throw new IOException("["+localDirectory.getAbsolutePath()+"] is not Directory");
        }
        for (String file : remoteFilenameList) {
            if (!recv(file, new File(localDirectory, file))) {
                throw new IOException("failrecv file ["+path() + "/" + file + "]");
            }
        }
    }
    
    /**
     * recv file to the local directory
     * @param remoteFilename
     * @param localDirectory
     * @return
     * @throws IOException
     */
    default public boolean recvToDirectory(String remoteFilename, File localDirectory) throws IOException {
        if (!localDirectory.exists() || !localDirectory.isDirectory()) {
            throw new IOException(localDirectory.getAbsolutePath() + " is not Directory");
        }
        return recv(remoteFilename, new File(localDirectory, remoteFilename));
    }
    
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
