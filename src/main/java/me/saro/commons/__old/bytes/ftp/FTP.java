package me.saro.commons.__old.bytes.ftp;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.function.Predicate;

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
    
    /**
     * open ftp
     * @param host
     * ip or domain
     * @param port
     * port (ftp basic port 21)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    public static FTP openFTP(InetAddress host, int port, String user, String pass) throws IOException {
        return new FTPS(host, port, user, pass, false);
    }
    
    /**
     * open ftp
     * @param host
     * ip or domain
     * @param port
     * port (ftp basic port 21)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    public static FTP openFTP(String host, int port, String user, String pass) throws IOException {
        return new FTPS(InetAddress.getByName(host), port, user, pass, false);
    }
    
    /**
     * open ftps
     * @param host
     * ip or domain
     * @param port
     * port (ftps basic port 990)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    public static FTP openFTPS(InetAddress host, int port, String user, String pass) throws IOException {
        return new FTPS(host, port, user, pass, true);
    }
    
    /**
     * open ftps
     * @param host
     * ip or domain
     * @param port
     * port (ftps basic port 990)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    public static FTP openFTPS(String host, int port, String user, String pass) throws IOException {
        return new FTPS(InetAddress.getByName(host), port, user, pass, true);
    }
    
    /**
     * open sftp
     * @param host
     * ip or domain
     * @param port
     * port (sftp basic port 22)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
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
     * get now path<br>
     * same method path(), pwd()
     * @return
     * @throws IOException
     */
    public String path() throws IOException;
    
    /**
     * print working directory<br>
     * same method path(), pwd()
     * @return
     * @throws IOException
     */
    default public String pwd() throws IOException {
        return path();
    }
    
    /**
     * get file list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    public List<String> listFiles(Predicate<String> filter) throws IOException;
    
    /**
     * get file list in now path
     * @return
     * @throws IOException
     */
    public List<String> listFiles() throws IOException;
    
    /**
     * get directory list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    public List<String> listDirectories(Predicate<String> filter) throws IOException;
    
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
    default public boolean send(File localFile) throws IOException {
        return send(localFile.getName(), localFile);
    }

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
                throw new IOException("fail recv file ["+path() + "/" + file + "]");
            }
        }
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
