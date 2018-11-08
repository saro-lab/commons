package me.saro.commons.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP
 */
public class SFTP implements FTP {

    ChannelSftp channel;
    Session session;
    final JSch ftp = new JSch();
    
    public SFTP(String host, int port, String user, String pass) throws IOException {
        try {
            session = ftp.getSession(user, host, port);
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public boolean path(String pathname) throws IOException {
        try {
            channel.cd(pathname);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    @Override
    public String path() throws IOException {
        try {
            return channel.pwd();
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<String> listFiles() throws IOException {
        try {
            return ((Stream<LsEntry>)channel.ls(path()).stream())
            .map(e -> (LsEntry)e)
            .filter(e -> !e.getAttrs().isDir())
            .map(e -> e.getFilename())
            .collect(Collectors.toList());
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> listDirectories() throws IOException {
        try {
            return ((Stream<LsEntry>)channel.ls(path()).stream())
            .map(e -> (LsEntry)e)
            .filter(e -> e.getAttrs().isDir())
            .filter(e -> !".".equals(e.getFilename()) && !"..".equals(e.getFilename()))
            .map(e -> e.getFilename())
            .collect(Collectors.toList());
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean delete(String pathname) throws IOException {
        try {
            channel.rm(pathname);
            return true;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean send(String saveFileName, File localFile) throws IOException {
        try (InputStream input = new FileInputStream(localFile)) {
            channel.put(input, saveFileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean send(File localFile) throws IOException {
        return send(localFile.getName(), localFile);
    }

    @Override
    public boolean recv(String remoteFileName, File localFile) throws IOException {
        if (localFile.exists()) {
            localFile.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            channel.get(remoteFileName, fos);
            return true;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean mkdir(String createDirectoryName) throws IOException {
        try {
            channel.mkdir(createDirectoryName);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        try {
            channel.disconnect();
        } catch (Exception e) {
        }
        try {
            session.disconnect();
        } catch (Exception e) {
        }
    }
}
