package me.saro.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * ftp
 * <br>
 * simple ftp class
 * <br>
 * this class is just casing the FTPClient (apache common net)
 * @author		PARK Yong Seo
 * @since		1.0.0
 * @see
 * org.apache.commons.net.ftp.FTPClient
 * org.apache.commons.net.ftp.FTPFile
 */
public class Ftp implements Cloneable {

	FTPClient ftp;

	private Ftp(FTPClient ftp) {
	}

	/**
	 * open ftp
	 * @param host
	 * ftp host
	 * @param port
	 * ftp port
	 * @param user
	 * username
	 * @param pass
	 * password
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static Ftp open(InetAddress host, int port, String user, String pass) throws SocketException, IOException {
		FTPClient ftp = null;
		Ftp self = new Ftp(ftp);
		try {
			ftp = new FTPClient();
			ftp.connect(host, port);
			ftp.login(user, pass);
		} catch (IOException e) {
			self.throwConnectionError();
		}
		return self.setFileTypeBinary();
	}

	/**
	 * move remote path (working directory)
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean setWorkingDirectory(String path) throws IOException {
		return ftp.changeWorkingDirectory(path);
	}

	/**
	 * get now remote path (working directory)
	 * @return
	 * @throws IOException
	 */
	public String getWorkingDirectory() throws IOException {
		return ftp.printWorkingDirectory();
	}

	/**
	 * read file list in now working directory
	 * @return
	 * @throws IOException
	 */
	public List<FTPFile> listFiles() throws IOException {
		return Converter.asList(ftp.listFiles());
	}
	
	/**
	 * read file list in now working directory
	 * @param filter
	 * @return
	 * @throws IOException
	 */
	public List<FTPFile> listFiles(Predicate<FTPFile> filter) throws IOException {
		return Converter.asList(ftp.listFiles()).stream().filter(filter).collect(Collectors.toList());
	}

	/**
	 * read directory list in now working directory
	 * @return
	 * @throws IOException
	 */
	public List<FTPFile> listDirectories() throws IOException {
		return Converter.asList(ftp.listDirectories());
	}
	
	/**
	 * read directory list in now working directory
	 * @param filter
	 * @return
	 * @throws IOException
	 */
	public List<FTPFile> listDirectories(Predicate<FTPFile> filter) throws IOException {
		return Converter.asList(ftp.listDirectories()).stream().filter(filter).collect(Collectors.toList());
	}

	/**
	 * send file
	 * @param saveServerFileName
	 * @param localFile
	 * @return
	 * @throws IOException
	 */
	public boolean send(String saveServerFileName, File localFile) throws IOException {
		try (FileInputStream fis = new FileInputStream(localFile)) {
			return ftp.storeFile(saveServerFileName, fis);
		}
	}

	/**
	 * read file
	 * @param readServerFileName
	 * @param localFile
	 * @return
	 * @throws IOException
	 */
	public boolean read(String readServerFileName, File localFile) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(localFile)) {
			return ftp.retrieveFile(readServerFileName, fos);
		}
	}
	
	/**
	 * file type ascii mode
	 * @return
	 * @throws IOException
	 */
	public Ftp setFileTypeAscii() throws IOException {
		if (!ftp.setFileType(FTPClient.ASCII_FILE_TYPE)) {
			throwConnectionError();
		}
		return this;
	}

	/**
	 * file type binary mode
	 * @return
	 * @throws IOException
	 */
	public Ftp setFileTypeBinary() throws IOException {
		if (!ftp.setFileType(FTPClient.BINARY_FILE_TYPE)) {
			throwConnectionError();
		}
		return this;
	}

	/**
	 * ftp close
	 * @throws IOException
	 */
	public void close() throws IOException {
		ftp.disconnect();
	}

	/**
	 * connection error
	 * @throws IOException
	 */
	private void throwConnectionError() throws IOException {
		try {
			ftp.disconnect();
		} catch (Exception e) {}
		throw new IOException("FTP Connection error");
	}
}
