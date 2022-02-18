/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------
	Representa un hilo que se encarga de las tareas de una conexión (leer
	y enviar al resto desde el cliente asociado a este hilo).
 */



package model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.SocketException;

import java.util.Date;

import javafx.application.Platform;



public class HiloServidor implements Runnable {

	private Cliente cliente;
	private Servidor servidor;
	private DataInputStream input;
	private DataOutputStream output;

	public HiloServidor(Cliente cliente, Servidor servidor) {
		this.cliente = cliente;
		this.servidor = servidor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}



	@Override
	public void run() {
		try {
			// Iniciar flujos de entrada y salida
			input = new DataInputStream(cliente.getSocket().getInputStream());
			output = new DataOutputStream(cliente.getSocket().getOutputStream());
			
			while (servidor.isConectado() == true) {
				// Reciber mensaje de cliente
				String mensaje = input.readUTF();

				// String para indicar la desconexión: |/\\\\/\\//\\|
				if (mensaje.equals("|/\\\\/\\//\\|")) {
					String host = cliente.getSocket().getInetAddress().getHostName();
					int port = cliente.getSocket().getPort();
					
					cliente.getSocket().shutdownInput();
					cliente.getSocket().shutdownOutput();
					cliente.getSocket().close();
					
					input.close();
					input = null;
					
					output.close();
					output = null;
					
					clienteDesconectado();
					
					// Informar al resto de clientes conectados
					Platform.runLater(() -> {
						servidor.textArea.appendText("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - " + cliente.getNombre() + " se ha desconectado\n");
						servidor.mensajeParaTodos("\t\t>> " + cliente.getNombre() + " se ha desconectado <<", this);
					});
					
					break;
				}
				else {
					// Enviar mensaje a todos
					if (output != null)
						servidor.mensajeParaTodos(mensaje, this);
	
					// Mostrar mensaje en el área de texto
					Platform.runLater(() -> {                    
						servidor.textArea.appendText(mensaje + "\n");
					});
				}
			}
		}
		catch(SocketException se) {
    		System.out.println("-----------------------------------------------------------");
    		se.printStackTrace();
    		System.out.println("La escucha del servidor se ha interrumpido.");
    		System.out.println("-----------------------------------------------------------");
    	}
		catch (IOException ioe) {
			System.out.println("-----------------------------------------------------------");
			ioe.printStackTrace();
			System.out.println("Error de e/s");
			System.out.println("-----------------------------------------------------------");
		}
		finally {
			try {
				if (cliente.getSocket().isClosed() == false)
					cliente.getSocket().close();
			}
			catch (IOException ex) {
				System.out.println("-----------------------------------------------------------");
				ex.printStackTrace();
				System.out.println("Error al cerrar el socket del cliente");
				System.out.println("-----------------------------------------------------------");
			}
		}
	}

	// Enviar mensaje al cliente asociado a este hilo del servidor
	public void mensajeExclusivo(String message) {
		try {
			cliente.getOutput().writeUTF(message);
			cliente.getOutput().flush();

		} catch (IOException ex) {
			System.out.println("-----------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("Error al enviar el mensaje al cliente");
			System.out.println("-----------------------------------------------------------");
		}
	}
	
	
	// Indicar que el cliente se ha desconectado
	public void clienteDesconectado() {
		servidor.limpiarConexiones();
	}

}
