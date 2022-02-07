/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------
	Representa un hilo que se encarga de las tareas de una conexión (leer
	desde el cliente y enviar al resto).
 */



package model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.util.Date;

import javafx.application.Platform;



public class HiloLectorServidor implements Runnable {

	Socket socket;
	Servidor servidor;
	DataInputStream input;
	DataOutputStream output;

	public HiloLectorServidor(Socket socket, Servidor server) {
		this.socket = socket;
		this.servidor = server;
	}

	@Override
	public void run() {
		try {
			// Iniciar flujos de entrada y salida
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			while (true) {
				// Reciber mensaje de cliente
				String mensaje = input.readUTF();

				// String para indicar la desconexión: |/\\\\/\\//\\|
				if (mensaje.equals("|/\\\\/\\//\\|")) {
					String host = socket.getInetAddress().getHostName();
					int port = socket.getPort();
					
					socket.shutdownInput();
					socket.shutdownOutput();
					socket.close();
					socket = null;
					
					input.close();
					input = null;
					
					output.close();
					output = null;
					
					servidor.textArea.appendText("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - Cliente desconectado\n");
					
					clienteDesconectado();
					
					break;
				}
				else {
					// Enviar mensaje a todos
					servidor.mensajeParaTodos(mensaje);
	
					// Mostrar mensaje en el área de texto
					Platform.runLater(() -> {                    
						servidor.textArea.appendText(mensaje + "\n");
					});
				}
			}
		}
		catch (IOException ex) { ex.printStackTrace(); }
		finally {
			try {
				if (socket != null && socket.isClosed() == false)
					socket.close();
			}
			catch (IOException ex) { ex.printStackTrace(); }
		}
	}

	
	// Enviar mensaje de vuelta a cliente - sustituir en el cliente para
	// optimizar servidor
	public void sendMessage(String message) {
		try {
			output.writeUTF(message);
			output.flush();

		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
	
	
	// Indicar que el cliente se ha desconectado
	public void clienteDesconectado() {
		servidor.setLimpiar(true);
	}

}
