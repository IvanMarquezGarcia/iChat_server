/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García
		
	2° D.A.M.
	
	Práctica "Chat Colectivo" - Programación de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCIÓN ------------------------------- MEJORAR DESCRIPCIÓN
	Permite al cliente recibir información del servidor de forma continua.
*/



package model;



import java.io.DataInputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;



public class HiloLector implements Runnable {
	
    Socket socket;
    Cliente cliente;
    DataInputStream input;

    
    
    public HiloLector(Socket socket, Cliente cliente) {
        this.socket = socket;
        this.cliente = cliente;
    }

    
    
    @Override 
    public void run() {
        while (true) {
            try {
                // Iniciar flujo de entrada
                try {
                	input = new DataInputStream(socket.getInputStream());
                	
                	cliente.errorText.setVisible(false);
                	cliente.errorText.setText("Error de conexión");
                } catch(SocketException se) {
                	if (socket == null) {
                		String errorMsg = "Flujo de salida cerrado";
                		
                		System.out.println(errorMsg);
                		
                		cliente.errorText.setText(errorMsg);
                		cliente.errorText.setVisible(true);
                	}
                }

                // recibir del cliente
                String mensaje = null;
                try {
                	mensaje = input.readUTF();
                } catch(SocketException se) {
                	if (socket == null)
                		System.out.println("Flujo de entrada cerrado.");
                }

                if (mensaje != null) {
                	String msg = mensaje;
	                // imprimirla en el área de texto
	                Platform.runLater(() -> {
	                    cliente.textArea.appendText(msg + "\n");
	                });
                }
            }
            catch (IOException ex) {
            	// En caso de excepciones de e/s, imprimirlas por consola y detener la escucha
            	ex.printStackTrace();
                System.out.println("\nError en la comunicación con el servidor");
                break;
            }
        }
    }
    
}
