/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García
		
	2° D.A.M.
	
	Práctica "Chat Colectivo" - Programación de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCIÓN -------------------------------
	
	Controlador de la vista VistaCliene.fxml.
*/



package controller;



import java.io.IOException;
import java.net.Socket;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Cliente;



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
	
	
	
	@FXML
	public void initialize() {
		this.cliente.setTextArea(mensajes_textArea);
		nombre_Text.setText(cliente.getNombre());
		cliente.errorText = error_text;
		
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
    private ScrollPane mensajes_scrollPane;

    @FXML
    private TextArea mensajes_textArea;

    @FXML
    private Text nombre_Text;

    @FXML
    private VBox root;
    
    @FXML
    void cerrarChat(MouseEvent event) {
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
				e.printStackTrace();
				System.out.println("Error al desconectar el cliente");
				System.out.println("---------------------------------------------------------------------");
				
				System.exit(1);
			}
    	}
    }
    
    
    @FXML
    void desconectarChat(MouseEvent event) {
    	cliente.desconectar();
    }
    
    
    @FXML
    void enviar_buttonFXClicked(MouseEvent event) {
    	if (cliente.getEstado() == 1) {
	    	try {
	            String message = input_textField.getText().trim();
	
	            // Si el mensaje o username no son especificados
	            // no se envía nada al servidor
	            if (message.length() > 0) {
	                // Enviar mensaje al servidor
	                cliente.getOutput().writeUTF("[" + cliente.getNombre() + "]: " + message + "");
	                
	                cliente.getOutput().flush(); // Limpiar flujo de salida
	
	                input_textField.clear(); // Limpiar cuadro de escritura
	            }
	        } catch (IOException ex) {
	        	String errorMsg = "Error al enviar";
	        	System.out.println("---------------------------------------------------------------------");
				ex.printStackTrace();
				System.out.println(errorMsg);
				System.out.println("---------------------------------------------------------------------");
				
				cliente.errorText.setText(errorMsg);
				cliente.errorText.setVisible(true);
	        }
    	}
    }

}
