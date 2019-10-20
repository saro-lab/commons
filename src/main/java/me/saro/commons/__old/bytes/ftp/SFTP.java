package me.saro.commons.__old.bytes.ftp;

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

import lombok.Getter;

/**
 * SFTP
 */
public class SFTP implements FTP {

    final @Getter ChannelSftp sftp;
    final Session session;
    
    public SFTP(String host, int port, String user, String pass) throws IOException {
        try {
            session = new JSch().getSession(user, host, port);
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public boolean path(String pathname) throws IOException {
        try {
            sftp.cd(pathname);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    @Override
    public String path() throws IOException {
        try {
            return sftp.pwd();
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    private List<String> list(Predicate<LsEntry> filter) throws IOException {
        try {
            List<String> list = new ArrayList<String>();
            sftp.ls(path(), e -> {
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
            return Optional.ofNullable(sftp.lstat(path() + "/" + filename))
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
            return Optional.ofNullable(sftp.lstat(path() + "/" + directoryname))
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
                sftp.rm(filename);
                return true;
            } else if (hasDirectory(filename)) {
                sftp.rmdir(filename);
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
            sftp.put(input, saveFilename);
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
            sftp.get(remoteFilename, fos);
            return true;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean mkdir(String createDirectoryName) throws IOException {
        try {
            sftp.mkdir(createDirectoryName);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        try {
            sftp.disconnect();
        } catch (Exception e) {
        }
        try {
            session.disconnect();
        } catch (Exception e) {
        }
    }
}
