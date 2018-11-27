package me.saro.commons.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import lombok.Getter;

/**
 * FTP, FTPS
 */
public class FTPS implements FTP {

    final @Getter FTPClient ftp;
    
    public FTPS(InetAddress host, int port, String user, String pass, boolean isFTPS) throws IOException {
        if (isFTPS) {
            ftp = new FTPSClient(true);
        } else {
            ftp = new FTPClient();
        }
        try {
            ftp.connect(host, port);
            if (isFTPS) {
                FTPSClient fs = (FTPSClient)ftp;
                fs.execPBSZ(0);
                fs.execPROT("P");
            }
            ftp.enterLocalPassiveMode();
            ftp.setUseEPSVwithIPv4(false);
            ftp.login(user, pass);
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
    
    /**
     * BINARY FILE MODE<br>
     * default : BINARY FILE MODE
     * @throws IOException
     */
    public void enterBinaryFileMode() throws IOException {
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
    }

    /**
     * ASCII FILE MODE<br>
     * default : BINARY FILE MODE
     * @throws IOException
     */
    public void enterAsciiFileMode() throws IOException {
        ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
    }
    
    /**
     * on passive mode<br>
     * default value : passive mode
     */
    public void enterLocalPassiveMode() {
        ftp.enterLocalPassiveMode();
    }
    
    /**
     * on active mode<br>
     * default value : passive mode
     */
    public void enterLocalActiveMode() {
        ftp.enterLocalActiveMode();
    }
    
    /**
     * user Extended Passive Mode with IPv4<br>
     * default false
     * @param selected
     */
    public void setUseEPSVwithIPv4(boolean selected) {
        ftp.setUseEPSVwithIPv4(selected);
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
        FTPFile ff = ftp.mlistFile(path() + "/" + filename);
        return ff != null && ff.isFile();
    }
    
    @Override
    public boolean hasDirectory(String directoryname) throws IOException {
        FTPFile ff = ftp.mlistFile(path() + "/" + directoryname);
        return ff != null && ff.isDirectory();
    }

    @Override
    public List<String> listFiles(Predicate<String> filter) throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isFile()).map(e -> e.getName()).filter(filter).collect(Collectors.toList());
    }
    
    @Override
    public List<String> listFiles() throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isFile()).map(e -> e.getName()).collect(Collectors.toList());
    }
    
    @Override
    public List<String> listDirectories(Predicate<String> filter) throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isDirectory()).map(e -> e.getName()).filter(filter).collect(Collectors.toList());
    }
    
    @Override
    public List<String> listDirectories() throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isDirectory()).map(e -> e.getName()).collect(Collectors.toList());
    }

    @Override
    public boolean delete(String filename) throws IOException {
        if (hasFile(filename)) {
            return ftp.deleteFile(filename);
        } else if (hasDirectory(filename)) {
            return ftp.removeDirectory(filename);
        }
        return false;
    }

    @Override
    public boolean send(String saveFilename, File localFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(localFile)) {
            return ftp.storeFile(saveFilename, fis);
        }
    }

    @Override
    public boolean recv(String remoteFilename, File localFile) throws IOException {
        if (hasFile(remoteFilename)) {
            if (localFile.exists()) {
                localFile.delete();
            }
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                return ftp.retrieveFile(remoteFilename, fos);
            }   
        } else {
            return false;
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
