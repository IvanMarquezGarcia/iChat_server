/*
	Hecho por:
		Eloy Guillermo Villad�niga M�rquez
		e
		Iv�n M�rquez Garc�a

	2� D.A.M.

	Pr�ctica "Chat Colectivo" - Programaci�n de Servicios y Procesos



	------------------------------- DESCRIPCI�N -------------------------------

	Representa un hilo que se encarga de las tareas de una conexi�n entre el
	servidor y un cliente.
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

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Ejecuta la tarea de lectura de datos
			servidor <- cliente de manera continua
			he informa al resto de clientes.
	*/
	@Override
	public void run() {
		try {
			// Iniciar flujos de entrada y salida
			input = new DataInputStream(cliente.getSocket().getInputStream());
			output = new DataOutputStream(cliente.getSocket().getOutputStream());
			
			while (servidor.isConectado() == true) {
				// Reciber mensaje de cliente
				String mensaje = input.readUTF();

				// String para indicar la desconexi�n del cliente asociado: |/\\\\/\\//\\|
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
					
					servidor.limpiarConexiones();
					
					// Informar al resto de clientes conectados
					Platform.runLater(() -> {
						servidor.listView.getItems().add("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - " +
														cliente.getNombre() + " se ha desconectado\n");
						servidor.listView.scrollTo(servidor.listView.getItems().size() - 1);
						servidor.mensajeParaTodos("\t\t>> " + cliente.getNombre() + " se ha desconectado <<", this);
					});
					
					break;
				}
				else {
					// Enviar mensaje a todos
					if (output != null)
						servidor.mensajeParaTodos(mensaje, this);
	
					// Mostrar mensaje en el �rea de texto
					Platform.runLater(() -> {                    
						servidor.listView.getItems().add(mensaje + "\n");
						servidor.listView.scrollTo(servidor.listView.getItems().size() - 1);
					});
				}
			}
		}
		catch(SocketException se) {
    		System.out.println("-----------------------------------------------------------");
    		//se.printStackTrace();
    		System.out.println("La escucha del servidor se ha interrumpido.");
    		System.out.println("-----------------------------------------------------------");
    	}
		catch (IOException ioe) {
			System.out.println("-----------------------------------------------------------");
			//ioe.printStackTrace();
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
				//ex.printStackTrace();
				System.out.println("Error al cerrar el socket del cliente");
				System.out.println("-----------------------------------------------------------");
			}
		}
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Enviar mensaje al cliente asociado a este
			hilo del servidor.
		
		PAR�METROS:
		 + String que representa el mensaje a enviar
	*/
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

}
