/*
	Hecho por: Iván Márquez García

	2° D.A.M.



	------------------------------- DESCRIPCI�N -------------------------------

	Esta es la clase que act�a como servidor. Tiene una lista donde almacena
	todas las conexiones funcionales con los clientes conectados.
	
	Su funci�n es aceptar peticiones continuamente en funci�n del m�ximo permitido.
	
	Cuando un cliente se conecta, crea un hilo usando una instancia de
	HiloServidor y lo ejecuta para que este se encargue de las tareas vinculadas
	con el nuevo cliente conectado.
 */



package model;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javafx.application.Platform;

import javafx.scene.control.ListView;
import utils.Mysql;

import static utils.Constants.*;


public class Servidor {

	private final String HOST;
	private final int PORT;

	private ServerSocket serverSocket;
	private boolean conectado;
	private int numMaxConx;
	public ListView<String> listView;
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

	public void setListView(ListView<String> txt) { this.listView = txt; }

	public ArrayList<HiloServidor> getConexiones() { return listaConexiones; }

	public ServerSocket getServerSocket() { return serverSocket; }


	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Enviar mensaje a todos los clientes excepto
			al que sea igual que el indicado como segundo
			par�metro.
		
		PAR�METROS:
			+ String que representa el mensaje a enviar.
			+ HiloServidor que representa el hilo del
				servidor que env�a el mensaje.
	*/
	public void mensajeParaTodos(String mensaje, HiloServidor hlsEmisor) {
		if (hlsEmisor == null) { // enviar a todos
			for (HiloServidor hls : this.listaConexiones) {
				if (hls.getUserSocket() != null)
					hls.mensajeExclusivo(mensaje);
			}
		}
		else { // enviar a todos excepto al que sea igual que hlsEmisor
			for (HiloServidor hls : this.listaConexiones) {
				if (hls.getUserSocket() != null && hls.equals(hlsEmisor) == false)
					hls.mensajeExclusivo(mensaje);
			}
		}
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Indicar a todos los clientes que el servidor
			se va a desconectar y cerrar socket del servidor.
	*/
	public void desconectar() {
		if (conectado == true) {
			conectado = false;
			
			// Indicar a todos los clientes la desconexi�n del servidor
			mensajeParaTodos(SERVER_DISCONNECTION, null);

			try {
				// Cerrar ServerSocket
				if (serverSocket != null && serverSocket.isClosed() == false) {
					String host = serverSocket.getInetAddress().getHostName();
					int port = serverSocket.getLocalPort();

					serverSocket.close();
					serverSocket = null;
					
					listView.getItems().add("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - Servidor desconectado\n");
					listView.scrollTo(listView.getItems().size() - 1);
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
		
		DESCRIPCI�N:
			Elimina de la lista de conexiones aquellas
			conexiones que no sean v�lidas (nulas, cerradas...).
	*/
	public void limpiarConexiones() {
		System.out.println("------------------------------------------------------------");
		System.out.println("n�mero de conexiones antes de limpiar " + listaConexiones.size());

		for (int i = 0; i < listaConexiones.size(); i++) {
			if (listaConexiones.get(i).getUserSocket().isClosed()) {
				System.out.println("\t-" + listaConexiones.get(i).getUsername() + " eliminado");
				listaConexiones.remove(i);
			}
		}
		
		if (listaConexiones.size() > 0) {
			System.out.println("\n");
			for (int i = 0; i < listaConexiones.size(); i++) {
				System.out.println("\t-" + listaConexiones.get(i).getUsername() + " conectado");
			}
		}

		System.out.println("n�mero de conexiones despues de limpiar " + listaConexiones.size());
		System.out.println("------------------------------------------------------------");
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Arranca el servidor.
	*/
	public void arrancar() {
		conectado = true;

		// Crear y ejecutar un nuevo hilo
		new Thread(() -> {
			try {
				// Crear ServerSocket
				serverSocket = new ServerSocket(PORT);

				// Indicar en el �rea de texto que el servidor se ha iniciado
				Platform.runLater(() ->
					listView.getItems().add("[" + new Date() + "] | [HOST: " + serverSocket.getInetAddress().getHostAddress() + " PORT: " + serverSocket.getLocalPort() + "] - Servidor iniciado\n"
				));

				while (conectado == true) {
					synchronized(this) {
						Socket socketNuevoCliente = null;
						try {
							// Escuchar petici�n de conexi�n
							System.out.println("Escuchando con " + listaConexiones.size() + " clientes conectados");
							socketNuevoCliente = serverSocket.accept();
							System.out.println("Nuevo cliente solicita entrar");
	
							DataOutputStream output = new DataOutputStream(socketNuevoCliente.getOutputStream());
	
							// Si el servidor est� lleno rechaza la conexi�n
							if (numMaxConx != -1 && numMaxConx <= listaConexiones.size()) {
								output.writeUTF(RESPONSE_SERVER_FULL);
								socketNuevoCliente.close();
							}
							else { // Si no, comprueba bd para aceptar la conexión
								Socket socket = socketNuevoCliente; // para respetar reglas lambda
								new Thread(() -> {
									try {
										DataInputStream input = new DataInputStream(socket.getInputStream());
										
										String strData = input.readUTF();
									    
									    String[] aux = strData.split(", ");
									    HashMap<String, String> data = new HashMap<String, String>();
									    
									    for (String s : aux) {
									    	String[] pair = s.split("=");
									    	System.out.println(s);
									    	data.put(pair[0], pair[1]);
									    }
									    
										Connection connection = Mysql.connect("127.0.0.1", "root", "root");
										
										if (connection != null) {
											String response = RESPONSE_ERROR;
											if (data.get("type").equals("login"))
												response = Mysql.login(connection, data);
											else if (data.get("type").equals("logup"))
												response = Mysql.logup(connection, data);
											output.writeUTF(response);
										}
										else
											output.writeUTF(RESPONSE_DB_UNABLE_CONNECTION);
										
										if (data.get("type").equals("logup"))
											socket.close();
										
										// Si el socket del nuevo cliente es válido
										if (socket != null && socket.isClosed() == false) {											
											// Add new connection to connections list
											HiloServidor hls = new HiloServidor(data.get("username"), socket, this);
											listaConexiones.add(hls);
											
											// Informar al resto de clientes conectados
											Platform.runLater(() -> {
												mensajeParaTodos("\t\t>> " + hls.getUsername() + " se ha conectado <<", hls);
												listView.getItems().add("[" + new Date() + "] | [HOST: " + hls.getUserSocket().getInetAddress().getHostAddress() +
																	" PORT: " + hls.getUserSocket().getPort() + "] - " + hls.getUsername() + " se ha conectado\n");
											});
											
											// Crear y ejecutar un nuevo hilo para la comunicaci�n con el cliente
											Thread thread = new Thread(hls);
											thread.start();
										}
									}
									catch(IOException ioe) {
										ioe.printStackTrace();
									}
									catch (Exception e) {
										e.printStackTrace();
									}
								}).start();
							}
						}
						catch(IOException e) {
							System.out.println("-----------------------------------------------------------");
							//e.printStackTrace();
							System.out.println("La escucha del servidor se ha interrumpido.");
							System.out.println("-----------------------------------------------------------");
						}
					}
				}
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
