/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------

	Esta es la clase que representa al cliente. Este enviará y recibirá
	mensajes a y desde el servidor y el resto de clientes conectados.

	Usa la clase HiloLector (Runnable) para crear un nuevo hilo con el objetivo
	de escuchar al servidor de forma constante.
 */



package model;



import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

import javafx.scene.control.TextArea;



public class Cliente {

	private final String NOMBRE;
	private DataOutputStream output;
	private Socket socket;
	public TextArea textArea;

	// host y puerto del servidor
	private String hostServidor;
	private int portServidor;



	public Cliente(String nombre, String host, int port) {
		this.NOMBRE = nombre;
		this.hostServidor = host;
		this.portServidor = port;
		
		try {
			// Crear un socket para conector con el servidor
			socket = new Socket(hostServidor, portServidor);

			// Crear un flujo de salida
			output = new DataOutputStream(socket.getOutputStream());

			// Crear un HiloLector para leer mensajes del servidor constantemente
			HiloLector task = new HiloLector(socket, this);

			Thread thread = new Thread(task);
			thread.start();
		} catch (IOException ex) {
			// En caso de excepción, indicarlo y mostrarla por consola
			ex.printStackTrace();
			System.out.println(ex.toString());
		}
	}



	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}
	
	public String getNombre() {
		return NOMBRE;
	}

	public DataOutputStream getOutput() {
		return output;
	}

}
