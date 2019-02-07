package me.saro.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import lombok.SneakyThrows;

/**
 * ByteData<br>
 * thread-non-safe :<br>
 * Be sure to use it in a single thread.
 * @author      PARK Yong Seo
 * @since       2.1
 */
public class ByteData {
    
    final public static int ALIGN_LEFT = 0;
    final public static int ALIGN_RIGHT = 1;
    final private static byte[] BYTES_NEW_LINE = new byte[]{'\r', '\n'};
    
    // buffer
    byte[] buf;
    int size;
    final int capacity;
    final String charset;
    
    // point
    int writePointer;
    int readPointer;
    
    /**
     * constructor
     * @param capacity
     * @param charset
     */
    private ByteData(int capacity, String charset) {
        if (capacity < 10) {
            throw new IllegalArgumentException("capacity is must more then 10");
        }
        this.capacity = capacity;
        this.charset = charset;
        this.buf = new byte[this.capacity];
        this.size = this.buf.length;
        
        this.writePointer = 0;
        this.readPointer = 0;
    }
    
    /**
     * create<br>
     * capacity : 8192
     * @return
     */
    public static ByteData create() {
        return new ByteData(8192, "UTF-8");
    }
    
    /**
     * create<br>
     * capacity : 8192
     * @param charset
     * @return
     */
    public static ByteData create(String charset) {
        return new ByteData(8192, charset);
    }
    
    /**
     * create<br>
     * capacity : 8192
     * @param capacity
     * @param charset
     * @return
     */
    public static ByteData create(int capacity, String charset) {
        return new ByteData(capacity, charset);
    }
    
    /**
     * write data
     * @param data
     * @param offset
     * @param length
     * @return
     */
    public ByteData write(byte[] data, int offset, int length) {
        allocate(writePointer + length);
        System.arraycopy(data, offset, buf, writePointer, length);
        writePointer += length;
        return this;
    }
    
    /**
     * write data
     * @param data
     * @return
     */
    public ByteData write(byte[] data) {
        return write(data, 0, data.length);
    }
    
    /**
     * write data
     * @param data
     * @return
     */
    public ByteData write(String data) throws IOException {
        return write(data.getBytes(charset));
    }
    
    /**
     * write fixed size
     * @param data
     * @param fixedSize
     * @param fill
     * @param ALIGN
     * @return
     * @throws IOException
     */
    public ByteData writeFixed(String data, int fixedSize, byte fill, int ALIGN) throws IOException {
        byte[] bytes = data.getBytes(charset);
        int fillSize = fixedSize - bytes.length;
        if (fillSize < 0) {
            throw new IOException("data over fixedSize : data["+data+"] fixedSize["+fixedSize+"] charset["+charset+"]");
        }
        
        write : switch (ALIGN) {
            case ALIGN_LEFT :
                write(bytes);
                writeFill(fill, fillSize);
                break write;
            case ALIGN_RIGHT : 
                writeFill(fill, fillSize);
                write(bytes);
                break write;
            default : 
                throw new IllegalArgumentException("ALIGN is ALIGN_LEFT or ALIGN_RIGHT");
        }
        return this;
    }
    
    /**
     * write fixed size
     * @param data
     * @param fixedSize
     * @param fill
     * @return
     * @throws IOException
     */
    public ByteData writeFixedLeft(String data, int fixedSize, byte fill) throws IOException {
        return writeFixed(data, fixedSize, fill, ALIGN_LEFT);
    }
    
    /**
     * write fixed size
     * @param data
     * @param fixedSize
     * @param fill
     * @return
     * @throws IOException
     */
    public ByteData writeFixedRight(String data, int fixedSize, byte fill) throws IOException {
        return writeFixed(data, fixedSize, fill, ALIGN_RIGHT);
    }
    
    /**
     * write fixed size
     * @param data
     * @param fixedSize
     * @param fill
     * @param ALIGN
     * @return
     * @throws IOException
     */
    public ByteData writeFixed(int data, int fixedSize, byte fill, int ALIGN) throws IOException {
        return writeFixed(Integer.toString(data), fixedSize, fill, ALIGN);
    }
    
    /**
     * write fixed size
     * @param data
     * @param fixedSize
     * @param fill
     * @param ALIGN
     * @return
     * @throws IOException
     */
    public ByteData writeFixed(long data, int fixedSize, byte fill, int ALIGN) throws IOException {
        return writeFixed(Long.toString(data), fixedSize, fill, ALIGN);
    }
    
    /**
     * writeZeroFill
     * @param data
     * @param fixedSize
     * @return
     * @throws IOException
     */
    public ByteData writeZeroFill(int data, int fixedSize) throws IOException {
        return write(Utils.zerofill(data, fixedSize));
    }
    
    /**
     * writeZeroFill
     * @param data
     * @param fixedSize
     * @return
     * @throws IOException
     */
    public ByteData writeZeroFill(long data, int fixedSize) throws IOException {
        return write(Utils.zerofill(data, fixedSize));
    }
    
    /**
     * write inputstream<br>
     * inputstream will not be closed
     * @param is
     * @return
     * @throws IOException 
     */
    public ByteData write(InputStream is) throws IOException {
        int bufsize = Math.max(8192, capacity);
        byte[] buf = new byte[bufsize];
        int len;
        while ( (len = is.read(buf)) >= 0 ) {
            write(buf, 0, len);
        }
        return this;
    }
    
    /**
     * write inputstream<br>
     * inputstream will not be closed
     * @param is
     * @param limit input stream stop size
     * @return
     * @throws IOException
     */
    public ByteData write(InputStream is, int limit) throws IOException {
        int bufsize = Math.max(8192, capacity);
        byte[] buf = new byte[bufsize];
        int len;
        int sum = 0;
        input : while ( (len = is.read(buf)) >= 0 ) {
            write(buf, 0, len);
            if ((sum += len) >= limit) {
                break input;
            }
        }
        return this;
    }
    
    /**
     * write file input stream
     * @param file
     * @return
     * @throws IOException
     */
    public ByteData write(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return write(fis);
        }
    }
    
    /**
     * write file input stream
     * @param file
     * @param fileCharset
     * @return
     * @throws IOException
     */
    public ByteData write(File file, String fileCharset) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(file) ;
                InputStreamReader isr = new InputStreamReader(fis, fileCharset)
            ) {
            int len;
            int bufsize = Math.max(4096, capacity / 2);
            char[] buf = new char[bufsize];
            while ( (len = isr.read(buf)) >= 0 ) {
                write(new String(buf, 0, len));
            }
        }
        return this;
    }
    
    /**
     * insert<br>
     * does not move write pointer<br>
     * @see rectifyWritePointer();
     * @param data
     * @param dataOffset
     * @param dataLength
     * @param offset
     * @return
     */
    public ByteData insert(byte[] data, int dataOffset, int dataLength, int offset) {
        allocate(offset + dataLength);
        System.arraycopy(data, dataOffset, buf, offset, dataLength);
        return this;
    }
    
    /**
     * insert<br>
     * does not move write pointer<br>
     * @see rectifyWritePointer();
     * @param data
     * @param offset
     * @return
     */
    public ByteData insert(byte[] data, int offset) {
        return insert(data, 0, data.length, offset);
    }
    
    /**
     * insert<br>
     * does not move write pointer<br>
     * @see rectifyWritePointer();
     * @param data
     * @param offset
     * @return
     * @throws IOException
     */
    public ByteData insert(String data, int offset) throws IOException {
        return insert(data.getBytes(charset), offset);
    }
    
    /**
     * 
     * @param data
     * @param fixedSize
     * @param fill
     * @param ALIGN
     * @param offset
     * @return
     * @throws IOException
     */
    public ByteData insertFixed(String data, int fixedSize, byte fill, int ALIGN, int offset) throws IOException {
        byte[] bytes = data.getBytes(charset);
        int fillSize = fixedSize - bytes.length;
        if (fillSize < 0) {
            throw new IOException("data over fixedSize : data["+data+"] fixedSize["+fixedSize+"] charset["+charset+"]");
        }
        
        insert : switch (ALIGN) {
            case ALIGN_LEFT :
                insert(bytes, offset);
                insertFill(fill, offset + bytes.length, fillSize);
                break insert;
            case ALIGN_RIGHT : 
                insertFill(fill, offset, fillSize);
                insert(bytes, fillSize + offset);
                break insert;
            default : 
                throw new IllegalArgumentException("ALIGN is ALIGN_LEFT or ALIGN_RIGHT");
        }
        return this;
    }
    
    /**
     * insertZeroFill
     * @param data
     * @param fixedSize
     * @return
     * @throws IOException
     */
    public ByteData insertZeroFill(int data, int fixedSize, int offset) throws IOException {
        return insert(Utils.zerofill(data, fixedSize), offset);
    }
    
    /**
     * insertZeroFill
     * @param data
     * @param fixedSize
     * @return
     * @throws IOException
     */
    public ByteData insertZeroFill(long data, int fixedSize, int offset) throws IOException {
        return insert(Utils.zerofill(data, fixedSize), offset);
    }
    
    /**
     * insert fill
     * @param fill
     * @param offset
     * @param length
     * @return
     */
    public ByteData insertFill(byte fill, int offset, int length) {
        allocate(offset + length);
        Arrays.fill(buf, offset, offset + length, fill);
        return this;
    }
    
    /**
     * insert fill space
     * @param offset
     * @param length
     * @return
     */
    public ByteData insertFillSpace(int offset, int length) {
        return insertFill((byte)' ', offset, length);
    }
    
    /**
     * write fill
     * @param fill
     * @param length
     * @return
     */
    public ByteData writeFill(byte fill, int length) {
        if (length == 0) {
            return this;
        }
        byte[] buf = new byte[length];
        Arrays.fill(buf, fill);
        return write(buf);
    }
    
    /**
     * write fill space
     * @param length
     * @return
     */
    public ByteData writeFillSpace(int length) {
        return writeFill((byte)' ', length);
    }
    
    /**
     * move the <b>write pointer (next write index pointer)</b><br>
     *  moveWritePointer(3) : next write index point 3, length 2
     * @param index
     * @return
     */
    public ByteData moveWritePointer(int index) {
        allocate(index);
        writePointer = index;
        return this;
    }
    
    /**
     * rectify the <b>write pointer (next write index pointer)</b><br>
     * create and insert method and to String is ""<br>
     * because insert method is not moving write pointer<br>
     * this method is moving pointer last data of buffer<br>
     * ex) create<br>
     * [0 0 0 0 0 0 0 0] / write pointer:0<br>
     * insert("a", 3);
     * [0 0 0 97 0 0 0 0] / write pointer:0<br> 
     * rectifyWritePointer();<br>
     * [0 0 0 97 0 0 0 0] / write pointer:4
     * @return
     */
    public ByteData rectifyWritePointer() {
        for (int i = (size - 1) ; i >= 0 ; i--) {
            if (buf[i] != 0) {
                writePointer = i + 1;
                return this;
            }
        }
        writePointer = 0;
        return this;
    }
    
    /**
     * null bytes fill the  spaces char<br>
     * [0 0 0 0 97 (writePointer)0 0 0]<br>
     * fillSpace()<br>
     * [32 32 32 32 97 (writePointer)0 0 0]
     * @return
     */
    public ByteData fillSpace() {
        for (int i = 0 ; i < writePointer ; i++) {
            if (buf[i] == 0) {
                buf[i] = ' ';
            }
        }
        return this;
    }
    
    /**
     * write \n
     * @return
     */
    public ByteData writeLine1() {
        return write(BYTES_NEW_LINE, 1, 1);
    }
    
    /**
     * write \r\n
     * @return
     */
    public ByteData writeLine2() {
        return write(BYTES_NEW_LINE, 0, 2);
    }
    
    /**
     * read ignore
     * @param size
     * @return
     */
    public ByteData readIgnore(int size) {
        readPointer += size;
        if (readPointer > writePointer) {
            throw new IndexOutOfBoundsException("out of index : readPointer["+readPointer+"], writePointer["+writePointer+"]");
        }
        return this;
    }
    
    /**
     * read ignore
     * @param match
     * @return
     * @throws IOException
     */
    public ByteData readIgnoreMatch(byte match) throws IOException {
        for (int i = readPointer ; i < writePointer ; i++) {
            if (buf[i] == match) {
                readPointer = i + 1;
                return this;
            }
        }
        throw new IOException("not found char["+match+"]");
    }
    
    /**
     * move read pointer to next line
     * @return
     * @throws IOException 
     */
    public ByteData readIgnoreCurrentLine() throws IOException {
        try {
            return readIgnoreMatch((byte)'\n');
        } catch (IOException e) {
            throw new IOException("not found new line (\\r\\n or \\n)");
        }
    }
    
    /**
     * new ByteData
     * @param offset
     * @param length
     * @return
     */
    public ByteData newByteData(int offset, int length) {
        ByteData data = new ByteData(this.capacity, this.charset);
        return data.write(toBytes(offset, length));
    }
    
    /**
     * data size
     * @return
     */
    public int size() {
        return writePointer;
    }
    
    /**
     * to string
     */
    @SneakyThrows
    public String toString() {
        return new String(buf, 0, writePointer, charset);
    }
    
    /**
     * to bytes
     * @return
     */
    public byte[] toBytes() {
        return Arrays.copyOfRange(buf, 0, writePointer);
    }
    
    /**
     * to bytes
     * @param offset
     * @param length
     * @return
     */
    public byte[] toBytes(int offset, int length) {
        int end = offset + length;
        if (end > writePointer) {
            throw new IndexOutOfBoundsException("out of index : offset["+offset+"], length["+length+"], ByteDataSize["+writePointer+"]");
        }
        return Arrays.copyOfRange(buf, offset, length);
    }
    
    /**
     * allocate
     * @param totalSize
     */
    private void allocate(int totalSize) {
        if (size < totalSize) {
            int allocate = (((totalSize / capacity) + 1) * capacity);
            buf = Arrays.copyOf(buf, allocate);
            size = allocate;
        }
    }
}
