package me.saro.commons.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * shell
 * @author      PARK Yong Seo
 * @since       3.0.2
 */
public class Shell {

    final public static String SHELL_CHARSET = System.getProperty("sun.jnu.encoding", "UTF-8");
    
    /**
     * execute
     * @param cmds
     * @return
     * @throws IOException
     */
    public static ShellResult execute(String... cmds) throws IOException {

        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmds);
        ShellResult res = new ShellResult();

        try (BufferedReader normal = new BufferedReader(new InputStreamReader(proc.getInputStream(), SHELL_CHARSET))) {
            res.setOutputNormal(normal.lines().collect(Collectors.joining("\n")));
        }

        try (BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream(), SHELL_CHARSET))) {
            res.setOutputError(error.lines().collect(Collectors.joining("\n")));
        }

        res.setExitResult(proc.exitValue());

        return res;
    }
}
