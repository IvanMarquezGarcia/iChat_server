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
                input = new DataInputStream(socket.getInputStream());

                // recibir del cliente
                String message = input.readUTF();

                // imprimirla en el área de texto
                Platform.runLater(() -> {
                    cliente.textArea.appendText(message + "\n");
                });
            }
            catch (IOException ex) {
            	// En caso de excepciones de e/s, imprimirlas por consola y detener la escucha
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
    
}
