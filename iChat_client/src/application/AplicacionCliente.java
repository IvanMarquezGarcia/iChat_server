/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	Práctica "Chat Colectivo" - Programación de Servicios y Procesos
	
	
	
	------------------------------- DESCRIPCIÓN -------------------------------
	
	Este es el archivo para ejecutar la apliciación gráfica asociada a un
	cliente.
*/



package application;



import javafx.application.Application;

import java.io.IOException;

import java.net.URL;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import controller.ControladorClienteLogin;

import model.Cliente;



public class AplicacionCliente extends Application {
    
    @Override
    public void start(Stage stage) {
    	Cliente cliente = new Cliente();
    	ControladorClienteLogin cdc = new ControladorClienteLogin(cliente);
    	
    	URL rutaVistaDatosCliente = getClass().getResource("/view/VistaClienteLogin.fxml");
    	FXMLLoader vistaDatosClienteLoader = new FXMLLoader(rutaVistaDatosCliente);
    	
    	vistaDatosClienteLoader.setController(cdc);
    	
    	try {
			Scene scene = new Scene(vistaDatosClienteLoader.load(), 420, 280);
			
			stage.setTitle("Log in | eiChat");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
	    	stage.show();
		} catch (IOException e) {
			System.out.println("---------------------------------------------------------------------");
			e.printStackTrace();
			System.out.println("Error al cargar la ventana");
			System.out.println("---------------------------------------------------------------------");
		}
    }

    
    
    public static void main(String[] args) {
        launch(args);
    }

}
