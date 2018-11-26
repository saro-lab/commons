package me.saro.commons.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP
 */
public class SFTP implements FTP {

    final ChannelSftp channel;
    final Session session;
    
    public SFTP(String host, int port, String user, String pass) throws IOException {
        try {
            session = new JSch().getSession(user, host, port);
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
    
    private List<String> list(Predicate<LsEntry> filter) throws IOException {
        try {
            List<String> list = new ArrayList<String>();
            channel.ls(path(), e -> {
                if (filter.test(e)) {
                    list.add(e.getFilename());
                }
                return LsEntrySelector.CONTINUE;
            });
            return list;
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public List<String> listFiles(Predicate<String> filter) throws IOException {
        return list(e -> !e.getAttrs().isDir() && filter.test(e.getFilename()));
    }
    
    @Override
    public List<String> listFiles() throws IOException {
        return list(e -> !e.getAttrs().isDir());
    }

    @Override
    public List<String> listDirectories(Predicate<String> filter) throws IOException {
        return list(e -> e.getAttrs().isDir() && !e.getFilename().matches("[\\.]{1,2}") && filter.test(e.getFilename()));
    }
    
    @Override
    public List<String> listDirectories() throws IOException {
        return list(e -> e.getAttrs().isDir() && !e.getFilename().matches("[\\.]{1,2}"));
    }
    
    @Override
    public boolean hasFile(String filename) throws IOException {
        try {
            return Optional.ofNullable(channel.lstat(path() + "/" + filename))
                    .filter(e -> !e.isDir()).isPresent();
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
            throw new IOException(e);
        }
    }
    
    @Override
    public boolean hasDirectory(String directoryname) throws IOException {
        try {
            return Optional.ofNullable(channel.lstat(path() + "/" + directoryname))
                    .filter(e -> e.isDir()).isPresent();
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
            throw new IOException(e);
        }
    }

    @Override
    public boolean delete(String filename) throws IOException {
        try {
            if (hasFile(filename)) {
                channel.rm(filename);
                return true;
            } else if (hasDirectory(filename)) {
                channel.rmdir(filename);
                return true;
            }
           return false;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean send(String saveFilename, File localFile) throws IOException {
        try (InputStream input = new FileInputStream(localFile)) {
            channel.put(input, saveFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean recv(String remoteFilename, File localFile) throws IOException {
        if (!hasFile(remoteFilename)) {
            return false;
        }
        if (localFile.exists()) {
            localFile.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            channel.get(remoteFilename, fos);
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
