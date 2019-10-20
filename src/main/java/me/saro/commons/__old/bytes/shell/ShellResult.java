package me.saro.commons.__old.bytes.shell;

import lombok.Data;
import me.saro.commons.__old.bytes.Utils;

/**
 * shell result
 * @author      PARK Yong Seo
 * @since       3.0.2
 */
@Data
public class ShellResult {
    private int exitResult;
    private String outputNormal;
    private String outputError;
    
    /**
     * get output (normal or error)
     * @return it is not null ""
     */
    public String getOutput() {
        return Utils.evl(outputNormal, outputError, "");
    }
}

