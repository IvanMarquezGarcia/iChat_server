/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García
		
	2° D.A.M.
	
	Práctica "Chat Colectivo" - Programación de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCIÓN ------------------------------- MEJORAR DESCRIPCIÓN
	
	Esta el la parte del servidor. Usa la clase TaskClientConnection para
	gestionar con un hilo cada nueva conexión.
	
	It also uses TaskClientConnection.java file to use in a thread which represents each new connection
*/



package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Servidor {
	
	private final String HOST;
	private final int PORT;
	private ServerSocket serverSocket;
	private boolean conectado;
	private boolean limpiar;
	private int numMaxConx;
    public TextArea textArea;
   
    // Lista de conexiones
    private ArrayList<HiloServidor> listaConexiones = new ArrayList<HiloServidor>(0);
	
	
	
	public Servidor(int port) {
		this("localhost", port, -1);
	}
	
	public Servidor(int port, int max) {
		this("localhost", port, max);
	}
	
	public Servidor(String host, int port, int max) {
		this.HOST = host;
		this.PORT = port;
		this.numMaxConx = max;
		conectado = false;
		limpiar = false;
	}
	
	
	public String getHost() {
		return HOST;
	}
	
	public int getPort() {
		return PORT;
	}
	
	public boolean isConectado() {
		return conectado;
	}
	
	public void setTextArea(TextArea txt) {
		this.textArea = txt;
	}
	
	public void setLimpiar(boolean b) {
		this.limpiar = b;
	}
	
	public ArrayList<HiloServidor> getConexiones() {
		return listaConexiones;
	}
	
	
	
	// Enviar mensaje a todos los clientes
    public void mensajeParaTodos(String message) {
        for (HiloServidor clientConnection : this.listaConexiones) {
        	if (clientConnection.socketCliente != null)
        		clientConnection.sendMessage(message);
        }
    }
    
	// Desconectar todos los clientes y matar proceso servidor
    public void desconectar() {
    	if (conectado == true) {
    		conectado = false;

    		limpiarConexiones();
    		mensajeParaTodos("|/\\\\/\\//\\|");
    		
	    	try {
	    		if (listaConexiones.size() > 0) {
	    			Iterator hlsIterator = listaConexiones.iterator();
	    			
			    	while (hlsIterator.hasNext() == true) {
			    		HiloServidor hls = (HiloServidor) hlsIterator.next();
			    		
			    		if (hls != null && hls.socketCliente != null) {
				    		if (hls.socketCliente.isInputShutdown() == false)
				    			hls.socketCliente.shutdownInput();
				    		
				    		if (hls.socketCliente.isOutputShutdown() == false)
				    			hls.socketCliente.shutdownOutput();
				    		
				    		if (hls.socketCliente.isClosed() == false) {
				    			hls.socketCliente.close();
				    			hls.socketCliente = null;
				    		}
				    		
				    		hlsIterator.remove();
			    		}
			    	}
	    		}
	
	    		if (serverSocket != null && serverSocket.isClosed() == false) {
	    			String host = serverSocket.getInetAddress().getHostName();
	    			int port = serverSocket.getLocalPort();
	    			
	    			serverSocket.close();
	    			serverSocket = null;
	    			
	    			textArea.appendText("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - Servidor desconectado\n");
	    		}
			} catch (IOException e) {
				System.out.println("-----------------------------------------------------------");
				e.printStackTrace();
				System.out.println("Error al desconectar el servidor");
				System.out.println("-----------------------------------------------------------");
			}
    	}
    }
    
    private void limpiarConexiones() {
    	System.out.println("------------------------------------------------------------");
		System.out.println("número de conexiones antes de limpiar " + listaConexiones.size());
		limpiar = false;
		
		Iterator<HiloServidor> iteradorConexiones = listaConexiones.iterator();
		
		while(iteradorConexiones.hasNext()) {
			HiloServidor hls = iteradorConexiones.next();
			
			if (hls.socketCliente == null) {
    			iteradorConexiones.remove();
    			hls = null;
    		}
		}
    	
    	listaConexiones.trimToSize();
    	System.gc();
    	
    	System.out.println("------------------------------------------------------------");
		System.out.println("número de conexiones despues de limpiar " + listaConexiones.size());
    }
	
	public void arrancar() {
		conectado = true;
		
		// Crear y ejecutar un nuevo hilo
        new Thread(() -> {
            try {
                // Crear ServerSocket
            	serverSocket = new ServerSocket(PORT);
                
                // Indicar en el área de texto que el servidor se ha iniciado
                Platform.runLater(()
                        -> textArea.appendText("[" + new Date() + "] | [HOST: " + serverSocket.getInetAddress().getHostAddress() + " PORT: " + serverSocket.getLocalPort() + "] - Servidor iniciado\n"));

                while (conectado == true) {
                	if (limpiar == true)
                		limpiarConexiones();
                	
                	if (numMaxConx != -1 && numMaxConx > listaConexiones.size()) {
	                	// Escuchar peticiones de conexión
	                	Socket socketNuevoCliente = null;
	                	try {
	                		socketNuevoCliente = serverSocket.accept();
	                	}
	                	catch(IOException e) {
	                		System.out.println("-----------------------------------------------------------");
	                		e.printStackTrace();
	                		System.out.println("La escucha del servidor se ha interrumpido.");
	                		System.out.println("-----------------------------------------------------------");
	                	}
	                    
	                	if (socketNuevoCliente != null) {
		                    textArea.appendText("[" + new Date() + "] | [HOST: " + socketNuevoCliente.getInetAddress().getHostAddress() + " PORT: " + socketNuevoCliente.getPort() + "] - Nuevo cliente conectado\n");
		                    
		                    // Añadir la nueva conexión a la lista de conexiones
		                    HiloServidor connection = new HiloServidor(socketNuevoCliente, this);
		                    listaConexiones.add(connection);
		
		                    // Crear y ejecutar un nuevo hilo para la conexión
		                    Thread thread = new Thread(connection);
		                    thread.start();
	                	}
                	}
                	else if (numMaxConx == -1) {
	                	// Escuchar peticiones de conexión
	                	Socket socketNuevoCliente = null;
	                	try {
	                		socketNuevoCliente = serverSocket.accept();
	                	}
	                	catch(SocketException se) {
	                		System.out.println("-----------------------------------------------------------");
	                		se.printStackTrace();
	                		System.out.println("La escucha del servidor se ha interrumpido.");
	                		System.out.println("-----------------------------------------------------------");
	                	}
	                	catch(IOException ioe) {
	                		System.out.println("-----------------------------------------------------------");
	                		ioe.printStackTrace();
	                		System.out.println("Error de e/s.");
	                		System.out.println("-----------------------------------------------------------");
	                	}
	                    
	                	if (socketNuevoCliente != null) {
		                    textArea.appendText("[" + new Date() + "] | [HOST: " + socketNuevoCliente.getInetAddress().getHostAddress() + " PORT: " + socketNuevoCliente.getPort() + "] - Nuevo cliente conectado\n");
		                    
		                    // Añadir la nueva conexión a la lista de conexiones
		                    HiloServidor connection = new HiloServidor(socketNuevoCliente, this);
		                    listaConexiones.add(connection);
		
		                    // Crear y ejecutar un nuevo hilo para la conexión
		                    Thread thread = new Thread(connection);
		                    thread.start();
	                	}
                	}
                }
            } catch (IOException ex) {
            	System.out.println("-----------------------------------------------------------");
                ex.printStackTrace();
                System.out.println("Error al abrir el socket del servidor");
                System.out.println("-----------------------------------------------------------");
            }
        }).start();
	}

}
