/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	Proyecto final - iChat


	------------------------------- DESCRIPCIÓN -------------------------------

	Controlador de la vista VistaServidor.fxml.
*/



package controller;



import javafx.event.EventHandler;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import javafx.stage.Stage;
import model.MensajeCellFactory;
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
    private Text infoDir_Text;

    @FXML
    private Text infoPuerto_Text;
	
	@FXML
	private Text info_Text;

	@FXML
	private ListView<String> mensajes_ListView;

	@FXML
	private VBox root;


	@FXML
	public void initialize() {	    	
		this.servidor.setListView(mensajes_ListView);
		this.infoPuerto_Text.setText("Puerto: " + servidor.getPort());
		this.infoDir_Text.setText("Host: " + servidor.getHost());
		
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
		
		MensajeCellFactory mcf = new MensajeCellFactory();
		servidor.listView.setCellFactory(mcf);
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Cerrar aplicaci�n.
	*/
	@FXML
    void cerrar(MouseEvent event) {
		servidor.desconectar();
		System.exit(0);
    }
	
	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Descnectar servidor.
	*/
	@FXML
	void desconectar(MouseEvent event) {
		servidor.desconectar();
	}

	
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCI�N:
			Arrancar servidor.
	*/
	@FXML
	void arrancar(MouseEvent event) {
		servidor.arrancar();
	}

}
