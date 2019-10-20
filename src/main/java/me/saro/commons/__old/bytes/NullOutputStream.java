package me.saro.commons.__old.bytes;

import java.io.IOException;
import java.io.OutputStream;

/**
 * null output stream<br>
 * this stream does not action<br>
 * this class using that throw out to the output stream data
 * @author      PARK Yong Seo
 * @since       2.0
 */
public class NullOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {
    }
    
}
