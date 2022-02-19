/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN ------------------------------- MEJORAR DESCRIPCIÓN

	Esta el la parte del servidor. Usa la clase TaskClientConnection para
	gestionar con un hilo cada nueva conexión.

	It also uses TaskClientConnection.java file to use in a thread which represents each new connection
 */



package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Servidor {

	private final String HOST;
	private final int PORT;
	private ServerSocket serverSocket;
	private boolean conectado;
	private int numMaxConx;
	public TextArea textArea;

	// Lista de conexiones
	private ArrayList<HiloServidor> listaConexiones = new ArrayList<HiloServidor>(0);



	public Servidor(int port) {
		this("localhost", port, -1);
	}

	public Servidor(int port, int max) {
		this("localhost", port, max);
	}

	public Servidor(String host, int port, int max) {
		this.HOST = host;
		this.PORT = port;
		this.numMaxConx = max;
		conectado = false;
	}


	public String getHost() {
		return HOST;
	}

	public int getPort() {
		return PORT;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setTextArea(TextArea txt) {
		this.textArea = txt;
	}

	public ArrayList<HiloServidor> getConexiones() {
		return listaConexiones;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}



	// Enviar mensaje a todos los clientes
	public void mensajeParaTodos(String mensaje, HiloServidor hlsEmisor) {
		if (hlsEmisor == null) { // enviar a todos
			for (HiloServidor hls : this.listaConexiones) {
				if (hls.getCliente().getSocket() != null)
					hls.mensajeExclusivo(mensaje);
			}
		}
		else { // enviar a todos excepto a emisor
			for (HiloServidor hls : this.listaConexiones) {
				if (hls.getCliente().getSocket() != null && hls.equals(hlsEmisor) == false)
					hls.mensajeExclusivo(mensaje);
			}
		}
	}

	// Desconectar todos los clientes y matar proceso servidor
	public void desconectar() {
		if (conectado == true) {
			conectado = false;
			
			mensajeParaTodos("|/\\\\/\\//\\|", null);
			
			//limpiarConexiones();

			try {
				/*
				if (listaConexiones.size() > 0) {
					while (listaConexiones.size() > 0) {
						HiloServidor hls = (HiloServidor) listaConexiones.get(0);

						if (hls != null && hls.getCliente().getSocket() != null) {
							if (hls.getCliente().getSocket().isInputShutdown() == false)
								hls.getCliente().getSocket().shutdownInput();

							if (hls.getCliente().getSocket().isOutputShutdown() == false)
								hls.getCliente().getSocket().shutdownOutput();

							if (hls.getCliente().getSocket().isClosed() == false) {
								hls.getCliente().getSocket().close();
								Socket s = hls.getCliente().getSocket(); s = null;
							}
							
							hls = null;
							
							listaConexiones.remove(0);
						}
					}
					Iterator<HiloServidor> clientesIterator = listaConexiones.iterator();

					while (clientesIterator.hasNext() == true) {
						HiloServidor hls = (HiloServidor) clientesIterator.next();

						if (hls != null && hls.getCliente().getSocket() != null) {
							if (hls.getCliente().getSocket().isInputShutdown() == false)
								hls.getCliente().getSocket().shutdownInput();

							if (hls.getCliente().getSocket().isOutputShutdown() == false)
								hls.getCliente().getSocket().shutdownOutput();

							if (hls.getCliente().getSocket().isClosed() == false) {
								hls.getCliente().getSocket().close();
								Socket s = hls.getCliente().getSocket(); s = null;
							}

							clientesIterator.remove();
						}
					}
				}*/

				if (serverSocket != null && serverSocket.isClosed() == false) {
					String host = serverSocket.getInetAddress().getHostName();
					int port = serverSocket.getLocalPort();

					serverSocket.close();
					serverSocket = null;
					
					textArea.appendText("[" + new Date() + "] | [HOST: " + host + " PORT: " + port + "] - Servidor desconectado\n");
				}
			} catch (IOException e) {
				System.out.println("-----------------------------------------------------------");
				e.printStackTrace();
				System.out.println("Error al desconectar el servidor");
				System.out.println("-----------------------------------------------------------");
			}
		}
	}

	
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
						// Escuchar peticiones de conexión
						Socket socketNuevoCliente = null;
						try {
							System.out.println("Escuchando con " + listaConexiones.size() + " clientes conectados");
							socketNuevoCliente = serverSocket.accept();
							System.out.println("nuevo cliente solicita entrar");
	
							DataOutputStream output = new DataOutputStream(socketNuevoCliente.getOutputStream());
	
							if (numMaxConx != -1 && numMaxConx <= listaConexiones.size()) {
								output.writeUTF("S_lleno_#no#mas#peticiones#_"); // String para indicar que servidor está lleno
								socketNuevoCliente.close();
							}
							else
								output.writeUTF("aceptado"); // String para indicar que servidor está lleno
						}
						catch(IOException e) {
							System.out.println("-----------------------------------------------------------");
							e.printStackTrace();
							System.out.println("La escucha del servidor se ha interrumpido.");
							System.out.println("-----------------------------------------------------------");
						}
	
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
								textArea.appendText("[" + new Date() + "] | [HOST: " + c.getSocket().getInetAddress().getHostAddress() + " PORT: " + c.getSocket().getPort() + "] - " + c.getNombre() + " se ha conectado\n");
							});
							
							// Crear y ejecutar un nuevo hilo para la comunicacón con el cliente
							Thread thread = new Thread(hls);
							thread.start();
						}
					}
				}
			}
			catch (ClassNotFoundException cnfe) {
				System.out.println("-----------------------------------------------------------");
				cnfe.printStackTrace();
				System.out.println("Error al encontrar la clase < Cliente >");
				System.out.println("-----------------------------------------------------------");
			}
			catch (IOException ex) {
				System.out.println("-----------------------------------------------------------");
				ex.printStackTrace();
				System.out.println("Error al abrir el socket del servidor");
				System.out.println("-----------------------------------------------------------");
			}
		}).start();
	}

}
