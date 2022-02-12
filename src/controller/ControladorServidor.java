/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------

	Controlador de la vista VistaServidor.fxml.
*/



package controller;



import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.VBox;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Servidor;



public class ControladorServidor {

	Servidor servidor;
	
	private double xOffset;
	private double yOffset;



	public ControladorServidor(Servidor s) {
		this.servidor = s;
	}


	
	@FXML
	private ImageView cerrar_ImageView;

	@FXML
	private Button conectar_button;

	@FXML
	private Button desconectar_button;

	@FXML
	private Text info_Text;

	@FXML
	private ScrollPane mensajes_scrollPane;

	@FXML
	private TextArea mensajes_textArea;

	@FXML
	private VBox root;

	@FXML
    void cerrarServidor(MouseEvent event) {
		servidor.desconectar();
		System.exit(0);
    }
	
	@FXML
	void desconectar(MouseEvent event) {
		servidor.desconectar();
	}

	@FXML
	void arrancar(MouseEvent event) {
		servidor.arrancar();
	}
	
	@FXML
	public void initialize() {	    	
		this.servidor.setTextArea(mensajes_textArea);
		
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

}
