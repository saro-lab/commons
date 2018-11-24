package me.saro.commons.ssh;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import me.saro.commons.function.ThrowableConsumer;

/**
 * SSH Shell
 * @since 1.2
 */
public class SSHShell implements Closeable {

    final String charset;
    final Session session;
    final ChannelShell shell;
    final OutputStream outputStream;
    final InputStream inputStream;
    final InputStreamReader inputStreamReader;
    final BufferedReader bufferedReader;
    final Thread readLineThread;

    /**
     * ssh
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param charset
     * @throws IOException
     */
    private SSHShell(String host, int port, String user, String pass, String charset, ThrowableConsumer<String> readLines) throws IOException {
        try {
            this.charset = charset;
            this.session = new JSch().getSession(user, host, port);
            this.session.setPassword(pass);
            this.session.setConfig("StrictHostKeyChecking", "no");
            this.session.connect();
            this.shell = (ChannelShell) this.session.openChannel("shell");
            this.shell.connect();
            this.inputStream = shell.getInputStream();
            this.outputStream = shell.getOutputStream();
            this.inputStreamReader = new InputStreamReader(this.inputStream, charset);
            this.bufferedReader = new BufferedReader(this.inputStreamReader);
            this.readLineThread = getReadLineThread(readLines);
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
     * @param charset
     * @return
     * @throws IOException
     */
    public static SSHShell open(String host, int port, String user, String pass, String charset, ThrowableConsumer<String> readLines) throws IOException {
        return new SSHShell(host, port, user, pass, charset, readLines);
    }

    /**
     * buffered stream reader
     * @param readLines
     * @return
     */
    private Thread getReadLineThread(ThrowableConsumer<String> readLines) {
        Thread thread = new Thread(() -> {
            try {
                this.bufferedReader.lines().forEach(ThrowableConsumer.runtime(readLines));
            } catch (Exception e) {
                // UncheckedIOException just shutdown
                if (!(e instanceof UncheckedIOException)) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        return thread;
    }

    /**
     * send commend
     * @param cmd
     * @return
     * @throws IOException
     */
    public void cmd(String... cmds) throws IOException {
        outputStream.write(Optional.ofNullable(cmds)
                .filter(e -> e.length > 0).map(Stream::of).map(e -> e.collect(Collectors.joining("\n", "", "\n")))
                .orElseThrow(() -> new IllegalArgumentException("there is no command")).getBytes(charset));
        outputStream.flush();
    }
    
    /**
     * send commend "exit" and wait EOF<br><br>
     * this method will ignore exceptions
     * <br>
     * <b>there is the same next 2 line</b><br>
     * cmd("exit");<br>
     * joinEOF();<br>
     */
    public void cmdExitAndJoinEOF() throws IOException, InterruptedException {
        try {
            cmd("exit");
        } catch (IOException e) {
        }
        try {
            joinEOF();
        } catch (InterruptedException e) {
        }
    }

    /**
     * cmd("exit");
     * and wait output
     * @throws InterruptedException
     */
    public void joinEOF() throws InterruptedException {
        if (readLineThread.isAlive()) {
            readLineThread.join();
        }
    }

    /**
     * connected
     * @return
     */
    public boolean isConnected() {
        return shell.isConnected();
    }

    /**
     * close<br>
     * include cmdExitAndJoinEOF();
     */
    @Override
    public void close() throws IOException {

        try {
            if (!shell.isEOF()) {
                cmdExitAndJoinEOF();
            }
        } catch (Exception e) {
        }

        try (
                OutputStream os = this.outputStream ;
                BufferedReader br = this.bufferedReader ; 
                InputStreamReader isr = this.inputStreamReader ; 
                InputStream is = this.inputStream
                ) {
        } catch (Exception e) {
        }
        
        try {
            if (readLineThread.isAlive()) {
                readLineThread.interrupt();
            }
        } catch (Exception e) {
        }

        try {
            if (shell.isConnected()) {
                shell.disconnect();
            }
        } catch (Exception e) {
        }

        try {
            if (session.isConnected()) {
                session.disconnect();
            }
        } catch (Exception e) {
        }
    }
}
