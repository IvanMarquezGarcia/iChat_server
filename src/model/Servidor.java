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
    public TextArea textArea;
   
    // Lista de conexiones
    private ArrayList<HiloLectorServidor> listaConexiones = new ArrayList<HiloLectorServidor>(0);
	
	
	
	public Servidor(int port) {
		this("localhost", port);
	}
	
	public Servidor(String host, int port) {
		this.HOST = host;
		this.PORT = port;
		conectado = false;
		limpiar = false;
	}
	
	
	public String getHost() {
		return HOST;
	}
	
	public int getPort() {
		return PORT;
	}
	
	public void setTextArea(TextArea txt) {
		this.textArea = txt;
	}
	
	public void setLimpiar(boolean b) {
		this.limpiar = b;
	}
	
	public ArrayList<HiloLectorServidor> getConexiones() {
		return listaConexiones;
	}
	
	
	
	// Enviar mensaje a todos los clientes
    public void mensajeParaTodos(String message) {
        for (HiloLectorServidor clientConnection : this.listaConexiones) {
        	if (clientConnection.socket != null)
        		clientConnection.sendMessage(message);
        }
    }
    
	// Desconectar todos los clientes y matar proceso servidor
    public void desconectar() {
    	if (conectado == true) {
    		conectado = false;
    		
	    	try {
	    		if (listaConexiones.size() > 0) {
			    	for (HiloLectorServidor hls : listaConexiones) {
			    		if (hls != null && hls.socket != null) {
				    		if (hls.socket.isInputShutdown() == false)
				    			hls.socket.shutdownInput();
				    		
				    		if (hls.socket.isOutputShutdown() == false)
				    			hls.socket.shutdownOutput();
				    		
				    		if (hls.socket.isClosed() == false) {
				    			hls.socket.close();
				    			hls.socket = null;
				    		}
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
				e.printStackTrace();
			}
    	}
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
                	if (limpiar == true) {
                		System.out.println("------------------------------------------------------------");
                		System.out.println("número de conexiones antes de limpiar " + listaConexiones.size());
                		limpiar = false;
                		
                		Iterator<HiloLectorServidor> iteradorConexiones = listaConexiones.iterator();
                		
                		while(iteradorConexiones.hasNext()) {
                			HiloLectorServidor hls = iteradorConexiones.next();
                			
                			if (hls.socket == null) {
            	    			iteradorConexiones.remove();
            	    			hls = null;
            	    		}
                		}
            	    	
            	    	listaConexiones.trimToSize();
            	    	System.gc();
            	    	
            	    	System.out.println("------------------------------------------------------------");
                		System.out.println("número de conexiones despues de limpiar " + listaConexiones.size());
                	}
                	// Escuchar peticiones de conexión
                	Socket socket = null;
                	try {
                		socket = serverSocket.accept();
                	} catch(SocketException se) {
                		System.out.println("La escucha del servidor se ha interrumpido.");
                	}
                    
                	if (socket != null) {
	                    textArea.appendText("[" + new Date() + "] | [HOST: " + socket.getInetAddress().getHostAddress() + " PORT: " + socket.getPort() + "] - Nuevo cliente conectado\n");
	                    
	                    // Añadir la nueva conexión a la lista de conexiones
	                    HiloLectorServidor connection = new HiloLectorServidor(socket, this);
	                    listaConexiones.add(connection);
	
	                    // Crear y ejecutar un nuevo hilo para la conexión
	                    Thread thread = new Thread(connection);
	                    thread.start();
                	}
                }
            } catch (IOException ex) {
            	// Si se da una excepción, imprimirla en el área de texto
                textArea.appendText(ex.toString() + '\n');
                
                ex.printStackTrace();
            }
        }).start();
	}

}
