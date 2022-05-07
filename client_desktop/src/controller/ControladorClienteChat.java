/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García
		
	2° D.A.M.
	
	Práctica "Chat Colectivo" - Programación de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCIÓN -------------------------------
	
	Controlador de la vista VistaClienteChat.fxml.
*/



package controller;



import java.io.IOException;

import java.net.Socket;
import java.net.URL;

import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import javafx.scene.text.Text;

import javafx.stage.Stage;

import model.Cliente;
import model.Mensaje;
import model.MensajeCellFactory;



public class ControladorClienteChat {

	private Cliente cliente;
	
	private double xOffset;
	private double yOffset;
	
	
	
	public ControladorClienteChat() {
		this(null);
	}
	
	public ControladorClienteChat(Cliente c) {
		this.cliente = c;
	}
	
	
    
	public void setCliente(Cliente c) {
		this.cliente = c;
	}
	
	public Cliente getCliente() {
		return cliente;
	}


 
	@FXML
	private ImageView desconectar_ImageView;

    @FXML
    private Text error_text;
	
    @FXML
    private Button enviar_button;

    @FXML
    private HBox info_Hbox;

    @FXML
    private Text info_text;

    @FXML
    private HBox input_HBox;

    @FXML
    private TextField input_textField;

    @FXML
    private ListView<Mensaje> mensajes_ListView;

    @FXML
    private Text nombre_Text;

    @FXML
    private VBox root;
    
    
    
    @FXML
	public void initialize() {
		this.cliente.setMensajesList(mensajes_ListView);
		nombre_Text.setText(cliente.getNombre());
		cliente.setError_text(error_text);
		
		// Hacer que la ventana se pueda mover
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = ((Stage) root.getScene().getWindow()).getX() - event.getScreenX();
                yOffset = ((Stage) root.getScene().getWindow()).getY() - event.getScreenY();
            }
        });
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	((Stage) root.getScene().getWindow()).setX(event.getScreenX() + xOffset);
            	((Stage) root.getScene().getWindow()).setY(event.getScreenY() + yOffset);
            }
        });
		
		// Capturador de evento para la tecla ENTER
		EventHandler<KeyEvent> eh = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER)
					enviarMensaje();
			}
		};
		
		// Asociar capturador de evento al nodo raíz
		root.setOnKeyReleased(eh);
		
		// Instanciar MensajeCellFactory y asociar
		// con el ListView del chat
		MensajeCellFactory mcf = new MensajeCellFactory();
		mensajes_ListView.setCellFactory(mcf);
	}
    
    
    /*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Cerrar aplicación.
    */
    @FXML
    void cerrar(MouseEvent event) {
    	if (cliente.getEstado() == 1) {
			cliente.desconectar();
			
			if (cliente.getEstado() == 0)
				System.exit(0);
			else {
				System.out.println("---------------------------------------------------------------------");
				System.out.println("Error al desconectar el cliente");
				System.out.println("---------------------------------------------------------------------");
				System.exit(1);
			}
    	}
    	else {
    		try {
    			Socket s = cliente.getSocket();
    			
    			if (s != null) {
    				if (s.isClosed() == false)
	    				s.close();
    				
    				s = null;
    			}
    			
				System.exit(0);
			} catch (IOException e) {
				System.out.println("---------------------------------------------------------------------");
				//e.printStackTrace();
				System.out.println("Error al desconectar el cliente");
				System.out.println("---------------------------------------------------------------------");
				System.exit(1);
			}
    	}
    }
    
    
    /*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Cerrar ventana de chat y volver al login
    */
    @FXML
    void desconectar(MouseEvent event) {
    	cliente.desconectar();
    	
    	// Crear controlador para vista de login
    	ControladorClienteLogin cdc = new ControladorClienteLogin(cliente);
    	
    	// Cargar vista de login
    	URL rutaVistaDatosCliente = getClass().getResource("/view/VistaClienteLogin.fxml");
    	FXMLLoader vistaDatosClienteLoader = new FXMLLoader(rutaVistaDatosCliente);
    	
    	// Asociar controlador a la vista
    	vistaDatosClienteLoader.setController(cdc);
    	
    	try {
			Scene scene = new Scene(vistaDatosClienteLoader.load(), 420, 280);
			
			// Cerrar ventana de chat
	    	Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        stage.close();
			
	        // Abrir ventana de login
			stage.setTitle("Log in | eiChat");
			stage.setScene(scene);
			stage.setResizable(false);
			
			cdc.initialize();
	    	cdc.setNombreDatos();
			
	    	stage.show();
		} catch (IOException e) {
			System.out.println("---------------------------------------------------------------------");
			//e.printStackTrace();
			System.out.println("Error al cargar la ventana");
			System.out.println("---------------------------------------------------------------------");
		}
    }
    
    
    /*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Manejador de envento para
			botón enviar.
    */
    @FXML
    void enviar_buttonFXClicked(MouseEvent event) {
    	enviarMensaje();
    }
    
    
    /*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Enviar mensaje al servidor.
    */
    public void enviarMensaje() {
    	if (cliente.getEstado() == 1) {
    		try {
	            String mensaje = input_textField.getText().trim();
	
	            if (mensaje.length() > 0) {
	                // Enviar mensaje al servidor
	                cliente.getOutput().writeUTF("[" + cliente.getNombre() + "] > " + mensaje + "");
	                
	                cliente.getOutput().flush(); // Limpiar flujo de salida
	
	                input_textField.clear(); // Limpiar cuadro de escritura
	                
	                mensajes_ListView.getItems().add(new Mensaje(mensaje + " < [Tú]", true));
	                mensajes_ListView.scrollTo(mensajes_ListView.getItems().size() - 1);
	            }
	        } catch (IOException ex) {
	        	String errorMsg = "Error al enviar mensaje";

	        	System.out.println("---------------------------------------------------------------------");
				// ex.printStackTrace();
				System.out.println(errorMsg);
				System.out.println("---------------------------------------------------------------------");
				
				cliente.errorText.setText(errorMsg);
				cliente.errorText.setVisible(true);
	        }
    	}
    }

}
