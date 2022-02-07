/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN ------------------------------- MEJORAR DESCRIPCIÓN

	Controlador de la vista VistaServidor.fxml.
 */



package controller;



import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import model.Servidor;



public class ControladorServidor {

	Servidor servidor;



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
	private VBox root_VBox;



	@FXML
	public void initialize() {	    	
		this.servidor.setTextArea(mensajes_textArea);
	}



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

}
