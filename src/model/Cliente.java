/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a

	2� D.A.M.

	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos



	------------------------------- DESCRIPCI�N -------------------------------

	Esta es la clase que representa al cliente. Este enviar� y recibir�
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

	private String nombre;
	private DataOutputStream output;
	private Socket socket;
	public TextArea textArea;

	// host y puerto del servidor
//	private String hostServidor;
//	private int portServidor;



	public Cliente() {
		// this("An�nimo", "localhost", 1234);
	}
	
	public Cliente(String nombre, String host, int port) {
		this.nombre = nombre;
//		this.hostServidor = host;
//		this.portServidor = port;
	}



	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}
	
	public void setNombre(String n) {
		this.nombre = n;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setSocket(Socket s) {
		this.socket = s;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public DataOutputStream getOutput() {
		return output;
	}
	
	
	
	public void conectar() {
		// hacer check
		
		try {
			// Crear un socket para conector con el servidor
			//socket = new Socket(hostServidor, portServidor);

			// Crear un flujo de salida
			output = new DataOutputStream(socket.getOutputStream());

			// Crear un HiloLector para leer mensajes del servidor constantemente
			HiloLector task = new HiloLector(socket, this);

			Thread thread = new Thread(task);
			thread.start();
		} catch (IOException ex) {
			// En caso de excepci�n, indicarlo y mostrarla por consola
			ex.printStackTrace();
			System.out.println("--------------------------------------------------------");
			System.out.println("Error al conectar");
		}
	}

}
