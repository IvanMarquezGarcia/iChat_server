/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------

	Esta es la clase que actúa como servidor. Tiene una lista donde almacena
	todas las conexiones funcionales con los clientes conectados.
	
	Su función es aceptar peticiones continuamente en función del máximo permitido.
	
	Cuando un cliente se conecta, crea un hilo usando una instancia de
	HiloServidor y lo ejecuta para que este se encargue de las tareas vinculadas
	con el nuevo cliente conectado.
 */



package model;



import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Date;

import javafx.application.Platform;

import javafx.scene.control.TextArea;



public class Servidor {

	private final String HOST;
	private final int PORT;

	private ServerSocket serverSocket;
	private boolean conectado;
	private int numMaxConx;
	public TextArea textArea;
	private ArrayList<HiloServidor> listaConexiones = new ArrayList<HiloServidor>(0);


	public Servidor(int port) { this("localhost", port, -1); }

	public Servidor(int port, int max) { this("localhost", port, max); }

	public Servidor(String host, int port, int max) {
		this.HOST = host;
		this.PORT = port;
		this.numMaxConx = max;
		conectado = false;
	}


	public String getHost() { return HOST; }

	public int getPort() { return PORT; }

	public boolean isConectado() { return conectado; }

	public void setTextArea(TextArea txt) { this.textArea = txt; }

	public ArrayList<HiloServidor> getConexiones() { return listaConexiones; }

	public ServerSocket getServerSocket() { return serverSocket; }


	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Enviar mensaje a todos los clientes excepto
			al que sea igual que el indicado como segundo
			parámetro.
		
		PARÁMETROS:
			+ String que representa el mensaje a enviar.
			+ HiloServidor que representa el hilo del
				servidor que envía el mensaje.
	*/
	public void mensajeParaTodos(String mensaje, HiloServidor hlsEmisor) {
		if (hlsEmisor == null) { // enviar a todos
			for (HiloServidor hls : this.listaConexiones) {
				if (hls.getCliente().getSocket() != null)
					hls.mensajeExclusivo(mensaje);
			}
		}
		else { // enviar a todos excepto al que sea igual que hlsEmisor
			for (HiloServidor hls : this.listaConexiones) {
				if (hls.getCliente().getSocket() != null && hls.equals(hlsEmisor) == false)
					hls.mensajeExclusivo(mensaje);
			}
		}
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Indicar a todos los clientes que el servidor
			se va a desconectar y cerrar socket del servidor.
	*/
	public void desconectar() {
		if (conectado == true) {
			conectado = false;
			
			// Indicar a todos los clientes la desconexión del servidor
			mensajeParaTodos("|/\\\\/\\//\\|", null);
			
			//limpiarConexiones();

			try {
				// Cerrar ServerSocket
				if (serverSocket != null && serverSocket.isClosed() == false) {
					String host = serverSocket.getInetAddress().getHostName();
					int port = serverSocket.getLocalPort();

					serverSocket.close();
					serverSocket = null;
					
					textArea.appendText("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - Servidor desconectado\n");
				}
			} catch (IOException e) {
				System.out.println("-----------------------------------------------------------");
				//e.printStackTrace();
				System.out.println("Error al desconectar el servidor");
				System.out.println("-----------------------------------------------------------");
			}
		}
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Elimina de la lista de conexiones aquellas
			conexiones que no sean válidas (nulas, cerradas...).
	*/
	public void limpiarConexiones() {
		System.out.println("------------------------------------------------------------");
		System.out.println("número de conexiones antes de limpiar " + listaConexiones.size());

		for (int i = 0; i < listaConexiones.size(); i++) {
			if (listaConexiones.get(i).getCliente().getSocket().isClosed()) {
				System.out.println("\t-" + listaConexiones.get(i).getCliente().getNombre() + " eliminado");
				listaConexiones.remove(i);
			}
		}
		
		if (listaConexiones.size() > 0) {
			System.out.println("\n");
			for (int i = 0; i < listaConexiones.size(); i++) {
				System.out.println("\t-" + listaConexiones.get(i).getCliente().getNombre() + " conectado");
			}
		}

		System.out.println("número de conexiones despues de limpiar " + listaConexiones.size());
		System.out.println("------------------------------------------------------------");
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Arranca el servidor.
	*/
	public void arrancar() {
		conectado = true;

		// Crear y ejecutar un nuevo hilo
		new Thread(() -> {
			try {
				// Crear ServerSocket
				serverSocket = new ServerSocket(PORT);

				// Indicar en el área de texto que el servidor se ha iniciado
				Platform.runLater(() ->
					textArea.appendText("[" + new Date() + "] | [HOST: " + serverSocket.getInetAddress().getHostAddress() + " PORT: " + serverSocket.getLocalPort() + "] - Servidor iniciado\n"
				));

				while (conectado == true) {
					synchronized(this) {
						Socket socketNuevoCliente = null;
						try {
							// Escuchar petición de conexión
							System.out.println("Escuchando con " + listaConexiones.size() + " clientes conectados");
							socketNuevoCliente = serverSocket.accept();
							System.out.println("Nuevo cliente solicita entrar");
	
							DataOutputStream output = new DataOutputStream(socketNuevoCliente.getOutputStream());
	
							// Si el servidor está lleno rechaza la conexión
							if (numMaxConx != -1 && numMaxConx <= listaConexiones.size()) {
								output.writeUTF("S_lleno_#no#mas#peticiones#_"); // String para indicar que servidor está lleno
								socketNuevoCliente.close();
							}
							else // Si no, acepta la conexión
								output.writeUTF("aceptado");
						}
						catch(IOException e) {
							System.out.println("-----------------------------------------------------------");
							//e.printStackTrace();
							System.out.println("La escucha del servidor se ha interrumpido.");
							System.out.println("-----------------------------------------------------------");
						}
	
						// Si el socket del nuevo cliente es válido
						if (socketNuevoCliente != null && socketNuevoCliente.isClosed() == false) {
							// Recontruir cliente recibido
							ObjectInputStream ois = new ObjectInputStream(socketNuevoCliente.getInputStream()); 
							Cliente c = (Cliente) ois.readObject();
							c.setSocket(socketNuevoCliente);
							c.setOutput(new DataOutputStream(c.getSocket().getOutputStream()));
							
							// Añadir la nueva conexión a la lista de conexiones
							HiloServidor hls = new HiloServidor(c, this);
							listaConexiones.add(hls);
							
							// Informar al resto de clientes conectados
							Platform.runLater(() -> {
								mensajeParaTodos("\t\t>> " + c.getNombre() + " se ha conectado <<", hls);
								textArea.appendText("[" + new Date() + "] | [HOST: " + c.getSocket().getInetAddress().getHostAddress() +
													" PORT: " + c.getSocket().getPort() + "] - " + c.getNombre() + " se ha conectado\n");
							});
							
							// Crear y ejecutar un nuevo hilo para la comunicación con el cliente
							Thread thread = new Thread(hls);
							thread.start();
						}
					}
				}
			}
			catch (ClassNotFoundException cnfe) {
				System.out.println("-----------------------------------------------------------");
				//cnfe.printStackTrace();
				System.out.println("Error al encontrar la clase < Cliente >");
				System.out.println("-----------------------------------------------------------");
			}
			catch (IOException ex) {
				System.out.println("-----------------------------------------------------------");
				//ex.printStackTrace();
				System.out.println("Error al abrir el socket del servidor");
				System.out.println("-----------------------------------------------------------");
			}
		}).start();
	}

}
