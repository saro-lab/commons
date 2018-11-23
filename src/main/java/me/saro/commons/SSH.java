package me.saro.commons;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SSH
 * @since 1.1
 */
public class SSH implements Closeable {
    
    final Session session;
    final String charset;
    
    /**
     * ssh
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param charset
     * @throws IOException
     */
    private SSH(String host, int port, String user, String pass, String charset) throws IOException {
        try {
            this.charset = charset;
            session = new JSch().getSession(user, host, port);
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();;
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
    
    /**
     * open ssh
     * @param host
     * @param port
     * @param user
     * @param pass
     * @return
     * @throws IOException
     */
    public static SSH open(String host, int port, String user, String pass) throws IOException {
        return new SSH(host, port, user, pass, "UTF-8");
    }
    
    /**
     * open ssh
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param charset
     * @return
     * @throws IOException
     */
    public static SSH open(String host, int port, String user, String pass, String charset) throws IOException {
        return new SSH(host, port, user, pass, charset);
    }
    
    /**
     * send just commend and return result
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param charset
     * @param cmd
     * @return
     * @throws IOException
     */
    public static String just(String host, int port, String user, String pass, String charset, String cmd) throws IOException {
        String rv;
        try (SSH ssh = SSH.open(host, port, user, pass, charset)) {
            rv = ssh.cmd(cmd);
        }
        return rv;
    }
    
    /**
     * send commend
     * @param cmd
     * @return
     * @throws IOException
     */
    public String cmd(String cmd) throws IOException {
        ChannelExec channel = null;
        String rv = null;
        
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cmd.getBytes(charset));
            InputStream is = channel.getInputStream();
            channel.connect();
            rv  = read(is);
            
        } catch (JSchException je) {
            throw new IOException(je);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                }
            }
        }
        
        return rv;
    }
    
    /**
     * read cmd
     * @return
     * @throws IOException 
     */
    private String read(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        int ch;
        while ( (ch = is.read()) != 0xffffffff ) {
            baos.write(ch);
        }
        return baos.toString(charset);
    }
    
    /**
     * close
     */
    @Override
    public void close() throws IOException {
        try {
            session.disconnect();
        } catch (Exception e) {
        }
    }
}
