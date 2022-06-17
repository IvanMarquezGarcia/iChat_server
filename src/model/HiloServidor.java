/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	Proyecto final - iChat


	------------------------------- DESCRIPCIÓN -------------------------------

	Representa un hilo que se encarga de las tareas de una conexión entre el
	servidor y un cliente.
 */



package model;



import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import java.util.Date;

import javafx.application.Platform;

import static utils.Constants.*;



public class HiloServidor implements Runnable {

	//private DataInputStream input;
	//private DataOutputStream output;
	private MessageObjectInputStream input;
	private ObjectOutputStream output;
	private Socket userSocket;
	private Servidor servidor;
	private User user;

	
	public HiloServidor(Socket s, Servidor servidor, User user) {
		this.userSocket = s;
		this.servidor = servidor;
		this.user = user;
	}

	
	public Socket getUserSocket() {
		return userSocket;
	}
	
	public User getUser() {
		return user;
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
			input = new MessageObjectInputStream(userSocket.getInputStream());
			output = new ObjectOutputStream(userSocket.getOutputStream());
			
			while (servidor.isConectado() == true) {
				// Reciber mensaje de cliente
				Mensaje mensaje = (Mensaje) input.readObject();

				// user is going to disconnect
				if (mensaje.getContenido().equals(SEND_BYE)) {
					userSocket.shutdownInput();
					userSocket.shutdownOutput();
					userSocket.close();
					
					input.close();
					input = null;
					
					output.close();
					output = null;
					
					break;
				}
				else {
					// Enviar mensaje a todos
					if (output != null)
						servidor.mensajeParaTodos(mensaje, this);
	
					// Mostrar mensaje en el �rea de texto
					Platform.runLater(() -> {                    
						servidor.listView.getItems().add(mensaje.getSender() + " > " + mensaje.getContenido());
						servidor.listView.scrollTo(servidor.listView.getItems().size() - 1);
					});
				}
			}
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println("-----------------------------------------------------------");
    		cnfe.printStackTrace();
       		System.out.println("Error al conseguir clase <Mensaje>");
    		System.out.println("-----------------------------------------------------------");
    		try { userSocket.close(); } catch (IOException e) { e.printStackTrace(); }
		}
		catch(SocketException se) {
			System.out.println("-----------------------------------------------------------");
    		se.printStackTrace();
    		System.out.println("La escucha del servidor se ha interrumpido.");
    		System.out.println("-----------------------------------------------------------");
    		try { userSocket.close(); } catch (IOException e) { e.printStackTrace(); }
    	}
		catch (EOFException eofe) {
			System.out.println("-----------------------------------------------------------");
			eofe.printStackTrace();
			System.out.println("Fin de flujo inesperado.");
			System.out.println("-----------------------------------------------------------");
			try { userSocket.close(); } catch (IOException e) { e.printStackTrace(); }
		}
		catch (IOException ioe) {
			System.out.println("-----------------------------------------------------------");
			ioe.printStackTrace();
			System.out.println("Error de e/s");
			System.out.println("-----------------------------------------------------------");
			try { userSocket.close(); } catch (IOException e) { e.printStackTrace(); }
		}
		finally {
			try {
				if (userSocket.isClosed() == false)
					userSocket.close();
				
				servidor.limpiarConexiones();
				
				// Informar al resto de clientes conectados
				String host = userSocket.getInetAddress().getHostName();
				int port = userSocket.getPort();
				Platform.runLater(() -> {
					servidor.listView.getItems().add("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - " +
													user.getUsername() + "/" + user.getUsername() + " se ha desconectado\n");
					servidor.listView.scrollTo(servidor.listView.getItems().size() - 1);
					servidor.mensajeParaTodos(new Mensaje("\t\t>> " + user.getUsername() + "/" + user.getUsername() + " se ha desconectado <<", "es", -1), this);
				});
			}
			catch (IOException ex) {
				System.out.println("-----------------------------------------------------------");
				ex.printStackTrace();
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
	public void mensajeExclusivo(Mensaje message) {
		try {
			//ObjectOutputStream output = new ObjectOutputStream(userSocket.getOutputStream());
			output.writeObject(message);
			output.flush();

		} catch (IOException ex) {
			System.out.println("-----------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("Error al enviar el mensaje al cliente");
			System.out.println("-----------------------------------------------------------");
		}
	}

}
