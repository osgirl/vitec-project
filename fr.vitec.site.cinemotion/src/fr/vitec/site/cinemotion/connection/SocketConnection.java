package fr.vitec.site.cinemotion.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnection {
	public static final String HTTP_BEGIN = "http://";
	
	private static final int TIMEOUT = 200000;
	private static final int DEFAULT_PORT = 80;
	private String url = null;
	private int port; 
	private Socket sock = null;
	
	
	public SocketConnection(String url) {
		this(url, DEFAULT_PORT);
	}
	
	public SocketConnection(String url, int port) {
		url = url.startsWith(HTTP_BEGIN)?url.substring(HTTP_BEGIN.length()):url;
		this.url = url;
		this.port = port;
	}
	
	protected void initSocket(){
		try {
			sock = new Socket(url, port);
			sock.setSoTimeout(TIMEOUT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void closeSocket(){
		try {
			sock.close();
			sock=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected String getFullUrl(){
		return this.getUrl().startsWith(HTTP_BEGIN)?"":HTTP_BEGIN + this.getUrl();
	}
	
	protected BufferedReader sendRequest(String header){
		BufferedReader source = null;
		try {
			OutputStream out=sock.getOutputStream();
			out = sock.getOutputStream();
			out.write(header.getBytes());
			out.flush();
			source = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return source;
	}

	protected int getPort() {
		return port;
	}

	protected void setPort(int port) {
		this.port = port;
	}

	protected Socket getSock() {
		return sock;
	}

	protected void setSock(Socket sock) {
		this.sock = sock;
	}

	protected String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	
}
