/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a

	2� D.A.M.

	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos



	------------------------------- DESCRIPCI�N -------------------------------

	Esta es la clase que representa al cliente en la parte del servidor. La
	instancia de esta clase servir�n al server para obtener informaci�n del
	cliente representado por la instancia, como nombre o informaci�n del socket.
	
 */



package model;



import java.io.DataOutputStream;
import java.io.Serializable;

import java.net.Socket;



public class Cliente implements Serializable {

	private static final long serialVersionUID = 3294218137349451778L;
	
	private String nombre;
	private byte estado;	// -1 = error; 0 = desconectado; 1 = conectado;
	
	private transient Socket socket;
	private transient DataOutputStream output;



	public Cliente() {
		this("An�nimo");
	}
	
	public Cliente(String nombre) {
		this.nombre = nombre;
		this.estado = 0;
	}



	public byte getEstado() { return estado; }
	
	public void setNombre(String n) { this.nombre = n; }
	
	public String getNombre() { return nombre; }
	
	public void setSocket(Socket s) { this.socket = s; }
	
	public Socket getSocket() { return socket; }
	
	public void setOutput(DataOutputStream dos) { this.output = dos; }

	public DataOutputStream getOutput() { return output; }

}
