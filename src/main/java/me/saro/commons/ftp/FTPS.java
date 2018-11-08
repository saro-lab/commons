package me.saro.commons.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

/**
 * FTP, FTPS
 */
public class FTPS implements FTP {

    final FTPClient ftp;
    
    public FTPS(InetAddress host, int port, String user, String pass, boolean isFTPS) throws IOException {
        ftp = isFTPS ? new FTPSClient() : new FTPClient();
        try {
            ftp.connect(host, port);
            ftp.login(user, pass);
            // set based control keep alive reply timeout
            ftp.setControlKeepAliveReplyTimeout(60000);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
           try {
               ftp.disconnect();
           } catch (Exception e1) {
           }
           throw e;
        }
    }
    
    @Override
    public boolean path(String pathname) throws IOException {
        return ftp.changeWorkingDirectory(pathname);
    }

    @Override
    public String path() throws IOException {
        return ftp.printWorkingDirectory();
    }
    
    @Override
    public boolean hasFile(String filename) throws IOException {
        throw new IOException("not support");
    }
    
    @Override
    public boolean hasDirectory(String directoryname) throws IOException {
        throw new IOException("not support");
    }

    @Override
    public List<String> listFiles() throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isFile()).map(e -> e.getName()).collect(Collectors.toList());
    }
    
    @Override
    public List<String> listDirectories() throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isDirectory()).map(e -> e.getName()).collect(Collectors.toList());
    }

    @Override
    public boolean delete(String filename) throws IOException {
        return ftp.deleteFile(filename);
    }

    @Override
    public boolean send(String saveFileName, File localFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(localFile)) {
            return ftp.storeFile(saveFileName, fis);
        }
    }

    @Override
    public boolean send(File localFile) throws IOException {
        return send(localFile.getName(), localFile);
    }

    @Override
    public boolean recv(String remoteFilename, File localFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            return ftp.retrieveFile(remoteFilename, fos);
        }
    }
    
    @Override
    public boolean mkdir(String createDirectoryName) throws IOException {
        ftp.mkd(createDirectoryName);
        return true;
    }

    @Override
    public void close() {
        try {
            ftp.disconnect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
