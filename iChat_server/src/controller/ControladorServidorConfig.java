/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------

	Controlador de la vista VistaServidorConfig.fxml.
*/



package controller;



import java.io.IOException;

import java.net.URL;

import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import javafx.stage.Stage;

import model.Servidor;



public class ControladorServidorConfig {

	private double xOffset;
	private double yOffset;
	
	
    @FXML
    private Button cerrar_button;

    @FXML
    private Button crear_button;

    @FXML
    private Text error_text;

    @FXML
    private TextField limiteConxDatos_textField;

    @FXML
    private TextField nombreDatos_textField;

    @FXML
    private TextField puertoDatos_textField;

    @FXML
    private VBox root;
    
    
    
    @FXML
    public void initialize() {
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
    }

    
    
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Cerrar aplicación.
	*/
    @FXML
    void cerrar(MouseEvent event) {
    	System.exit(0);
    }

    
	/*
		ESTADO: FUNCIONAL 
		
		DESCRIPCIÓN:
			Abrir una ventana asociada a un servidor
			con las características indicadas en la
			vista asociada a este controlador.
	*/
    @FXML
    void crear(MouseEvent event) {
    	error_text.setVisible(false);
    	
    	if (puertoDatos_textField.getText().length() > 0) {
    		try {
    			int puerto = Integer.parseInt(puertoDatos_textField.getText());
    			
    			if (puerto > 1024 && puerto < 49151) {
    				URL rutaRecurso = getClass().getResource("/view/VistaServidor.fxml");
        	    	FXMLLoader fxmlLoader = new FXMLLoader(rutaRecurso);
        	    	
        	    	Servidor s = null;
        	    	
        	    	if (limiteConxDatos_textField.getText().length() > 0) {
        	    		try {
        	    			int max = Integer.parseInt(limiteConxDatos_textField.getText());
    	    	    		s = new Servidor(puerto, max);
        	    		}
        	    		catch (NumberFormatException nfe){
        	    			error_text.setText("Límite inválido");
        	    		}
        	    	}
        	    	else {
    	    			s = new Servidor(puerto);
        			}
        	    	
        	    	if (s != null) {
    	    			ControladorServidor cs = new ControladorServidor(s);
    	    	    	
    	    	    	fxmlLoader.setController(cs);
    	    	    	
    	    			try {    				
    	    		        Scene scene = new Scene(fxmlLoader.load());
    	    				
    	    				// Cerrar ventana de datos
    	    		    	Node source = (Node) event.getSource();
    	    		        Stage stage = (Stage) source.getScene().getWindow();
    	    		        stage.close();
    	    		        
    	    		        // Abrir ventana de servidor
    	    				stage.setTitle("Servidor de eiChat");
    	    				stage.setScene(scene);
    	    				stage.setResizable(false);
    	    		    	stage.show();
    	    			} catch (IOException e) {
    	    				System.out.println("---------------------------------------------------------------------");
    	    				e.printStackTrace();
    	    				System.out.println("Error al cargar la ventana");
    	    				System.out.println("---------------------------------------------------------------------");
    	    			}
        			}
    			}
    			else {
    				error_text.setText("Prueba con otro puerto");
    			}
    		}
    		catch(NumberFormatException nfe) {
    			System.out.println("---------------------------------------------------------------------");
				// nfe.printStackTrace();
				System.out.println("Puerto no válido");
				System.out.println("---------------------------------------------------------------------");
    			
    			error_text.setText("Puerto no válido");
        		error_text.setVisible(true);
    		}
    	}
    	else {
    		error_text.setText("Puerto no válido");
    		error_text.setVisible(true);
    	}
    }

}
