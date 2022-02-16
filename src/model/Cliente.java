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



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;

import javafx.scene.control.TextArea;

import javafx.scene.text.Text;



public class Cliente implements Serializable {

	private String nombre;
	private byte estado;	// -1 = error; 0 = desconectado; 1 = conectado;
	private transient Socket socket;
	private transient DataOutputStream output;
	public Text errorText;
	public TextArea textArea;

	// host y puerto del servidor
//	private String hostServidor;
//	private int portServidor;



	public Cliente() {
		// this("Anónimo", "localhost", 1234);
	}
	
	public Cliente(String nombre/*, String host, int port*/) {
		this.nombre = nombre;
		this.estado = 0;
//		this.hostServidor = host;
//		this.portServidor = port;
	}



	public byte getEstado() {
		return estado;
	}
	
	public void setErrorText(Text t) {
		this.errorText = t;
	}
	
	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}
	
	public void setError_text(Text t) {
		this.errorText = t;
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
	
	public void setOutput(DataOutputStream dos) {
		this.output = dos;
	}

	public DataOutputStream getOutput() {
		return output;
	}
	
	
	
	public void desconectar() {
		try {
    		// Indicar desconexión al servidor
    		output.writeUTF("|/\\\\/\\//\\|");
    		
    		// Desconectar
    		if (socket.isInputShutdown() == false)
    			socket.shutdownInput();
    		
    		if (socket.isOutputShutdown() == false)
    			socket.shutdownOutput();
    		
    		if (socket.isClosed() == false) {
    			socket.close();
    			socket = null;
    		}
    		/*
	    	socket.shutdownInput();
	    	socket.shutdownOutput();
	    	socket.close();
	    	socket = null;
	    	*/
	    	estado = 0;
    	}
		catch(SocketException se) {
    		System.out.println("-----------------------------------------------------------");
    		se.printStackTrace();
    		System.out.println("La comunicación con el servidor se ha interrumpido.");
    		System.out.println("-----------------------------------------------------------");
    		
    		estado = 0;
    	}
    	catch(IOException ioe) {
    		estado = -1;
    		
    		System.out.println("-----------------------------------------------------------");
    		ioe.printStackTrace();
    		System.out.println("Error al desconectar el cliente");
    		System.out.println("-----------------------------------------------------------");
    	}
	}
	
	public void sendMessage(String message) {
		try {
			output.writeUTF(message);
			output.flush();

		} catch (IOException ex) {
			System.out.println("-----------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("Error al enviar el mensaje al servidor");
			System.out.println("-----------------------------------------------------------");
		}
	}
	
	public int conectar() {
		// implementar check
		// implementar check
		// implementar check
		// implementar check
		// implementar check
		// implementar check
		// implementar check
		// implementar check
		
		if (estado == 0) {
			try {
				// Solicitar "hueco" en el servidor
				String mensaje = new DataInputStream(socket.getInputStream()).readUTF();
				
				System.out.println("mensaje de entrada: " + mensaje);
				
				if (mensaje.equals("S_lleno_#no#mas#peticiones#_"))
					System.out.println("denegado");
				else { // Si servidor acepta petición:
					// Crear un flujo de salida
					output = new DataOutputStream(socket.getOutputStream());
					
					
					ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
					oos.writeObject(this);
	
					// Crear un HiloLector para leer mensajes del servidor constantemente
					HiloLectorCliente task = new HiloLectorCliente(this);
	
					Thread thread = new Thread(task);
					thread.start();
	
					estado = 1;
				}
			} catch (IOException ex) {
				estado = -1;
				
				System.out.println("-----------------------------------------------------------");
				ex.printStackTrace();
				System.out.println("Error al conectar con el servidor");
				System.out.println("-----------------------------------------------------------");
			}
		}
		
		return estado;
	}

}
