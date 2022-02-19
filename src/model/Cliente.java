/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------

	Esta es la clase que representa al cliente. Este enviará y recibirá
	mensajes a y desde el servidor usando las clases HiloLector e HiloServidor.
	
 */



package model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.net.Socket;
import java.net.SocketException;

import javafx.scene.control.ListView;

import javafx.scene.text.Text;



public class Cliente implements Serializable {

	private static final long serialVersionUID = 3294218137349451778L;
	
	private String nombre;
	private byte estado;	// -1 = error; 0 = desconectado; 1 = conectado;
	
	private transient Socket socket;
	private transient DataOutputStream output;
	public transient Text errorText;
	public transient ListView<Mensaje> listView;



	public Cliente() {
		this("Anónimo");
	}
	
	public Cliente(String nombre) {
		this.nombre = nombre;
		this.estado = 0;
	}



	public byte getEstado() { return estado; }
	
	public void setErrorText(Text t) { this.errorText = t; }
	
	public void setMensajesList(ListView<Mensaje> listView) { this.listView = listView;	}
	
	public void setError_text(Text t) { this.errorText = t;	}
	
	public void setNombre(String n) { this.nombre = n; }
	
	public String getNombre() { return nombre; }
	
	public void setSocket(Socket s) { this.socket = s; }
	
	public Socket getSocket() { return socket; }
	
	public void setOutput(DataOutputStream dos) { this.output = dos; }

	public DataOutputStream getOutput() { return output; }
	
	
	
	/*
	ESTADO: FUNCIONAL 
	
	DESCRIPCIÓN:
		Indica al servidor que el cliente se va a
		desconectar y cierra los flujos del socket
		del cliente y el propio socket 
	*/
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
    		
	    	estado = 0;
    	}
		catch(SocketException se) {
    		System.out.println("-----------------------------------------------------------");
    		//se.printStackTrace();
    		System.out.println("La comunicación con el servidor se ha interrumpido.");
    		System.out.println("-----------------------------------------------------------");
    		
    		estado = 0;
    	}
    	catch(IOException ioe) {
    		estado = -1;
    		
    		System.out.println("-----------------------------------------------------------");
    		//ioe.printStackTrace();
    		System.out.println("Error al desconectar el cliente");
    		System.out.println("-----------------------------------------------------------");
    	}
	}
	
	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Solicita conexión al servidor y conecta al
			cliente con este.
		
		RETORNO:
			int indicando el estado de conexión del cliente.
	*/
	public int conectar() {
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
				//ex.printStackTrace();
				System.out.println("Error al conectar con el servidor");
				System.out.println("-----------------------------------------------------------");
			}
		}
		
		return estado;
	}

}
