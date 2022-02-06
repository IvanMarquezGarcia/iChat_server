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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Servidor {
	
	private final String HOST;
	private final int PORT;
	private ServerSocket serverSocket;
    public TextArea textArea;
   
    // Lista de conexiones
    private List<TaskClientConnection> connectionList = new ArrayList<TaskClientConnection>();
	
	
	
	public Servidor(int port) {
		this("localhost", port);
	}
	
	public Servidor(String host, int port) {
		this.HOST = host;
		this.PORT = port;
	}
	
	
	
	public void setTextArea(TextArea txt) {
		this.textArea = txt;
	}
	
	public String getHost() {
		return HOST;
	}
	
	public int getPort() {
		return PORT;
	}
	
	
	
	// Enviar mensaje a todos los clientes
    public void mensajeParaTodos(String message) {
        for (TaskClientConnection clientConnection : this.connectionList) {
            clientConnection.sendMessage(message);
        }
    }
    
    public void desconectar() {
    	// desconectar todos los clientes y matar proceso servidor
    }
	
	public void arrancar() {
		// Crear y ejecutar un nuevo hilo
        new Thread(() -> {
            try {
                // Crear ServerSocket
            	serverSocket = new ServerSocket(PORT);
                
                // Indicar en el area de texto que el servidor se ha iniciado
                Platform.runLater(()
                        -> textArea.appendText("[" + new Date() + "] | [HOST: " + serverSocket.getInetAddress().getHostAddress() + " PORT: " + serverSocket.getLocalPort() + "] - Nuevo servidor iniciado\n"));

                while (true) {
                	// Escuchar peticiones de conexion
                    // Listen for a connection request, add new connection to the list
                    Socket socket = serverSocket.accept();
                    
                    textArea.appendText("[" + new Date() + "] | [HOST: " + socket.getInetAddress().getHostAddress() + " PORT: " + socket.getPort() + "] - Nuevo cliente conectado\n");
                    
                    // Añadir la nueva conexión a la lista de conexiones
                    TaskClientConnection connection = new TaskClientConnection(socket, this);
                    connectionList.add(connection);

                    // Crear y ejecutar un nuevo hilo para la conexión
                    Thread thread = new Thread(connection);
                    thread.start();

                }
            } catch (IOException ex) {
            	// Si se da una excepción, imprimirla en el area de texto
                textArea.appendText(ex.toString() + '\n');
            }
        }).start();
	}

}
