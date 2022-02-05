/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García
		
	2° D.A.M.
	
	Práctica "Chat Colectivo" - Programación de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCIÓN ------------------------------- MEJORAR DESCRIPCIÓN
	Representa cada nueva conexión.
*/



package model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

import javafx.application.Platform;



public class TaskClientConnection implements Runnable {

    Socket socket;
    Servidor server;
    DataInputStream input;
    DataOutputStream output;

    public TaskClientConnection(Socket socket, Servidor server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Iniciar flujos de entrada y salida
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            while (true) {
                // Reciber mensaje de cliente
                String message = input.readUTF();

                // Enviar mensaje a todos
                server.mensajeParaTodos(message);
                
                // Mostrar mensaje en el área de texto
                Platform.runLater(() -> {                    
                    server.textArea.appendText(message + "\n");
                });
            }
        }
        catch (IOException ex) { ex.printStackTrace(); }
        finally {
            try { socket.close(); }
            catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    //send message back to client - NI IDEA DE QUÉ HACE ESTO
    public void sendMessage(String message) {
          try {
            output.writeUTF(message);
            output.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        } 
       
    }

}
