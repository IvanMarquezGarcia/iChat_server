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

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import model.Cliente;



public class ControladorCliente {

	private Cliente cliente;
	
	
	
	public ControladorCliente() {
		this(null);
	}
	
	public ControladorCliente(Cliente c) {
		this.cliente = c;
	}
	
	
	
	@FXML
	public void initialize() {
		this.cliente.setTextArea(mensajes_textArea);
		nombre_Text.setText(cliente.getNombre());
	}
	
	
    
	public void setCliente(Cliente c) {
		this.cliente = c;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	
	
	
	
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
    private VBox root_VBox;
    
    
    
//    public Text getNombreText() {
//    	return nombre_text;
//    }
    
    
    @FXML
    void cerrarChat(MouseEvent event) {
    	try {
			cliente.getSocket().close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
    }
    
    
    
    @FXML
    void enviar_buttonFXClicked(MouseEvent event) {
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
            System.err.println(ex);
        }
    }

}
