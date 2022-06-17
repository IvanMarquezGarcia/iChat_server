/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	Proyecto final - iChat
	
	
	------------------------------- DESCRIPCIÓN -------------------------------
	
	Este es el archivo para ejecutar la apliciación gráfica asociada a un
	servidor.
*/



package application;



import javafx.application.Application;

import java.io.IOException;

import java.net.URL;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import controller.ControladorServidorConfig;



public class AplicacionServidor extends Application {

    @Override
    public void start(Stage stage) {
    	URL rutaRecurso = getClass().getResource("/view/VistaServidorConfig.fxml");
    	FXMLLoader fxmlLoader = new FXMLLoader(rutaRecurso);
    	
    	ControladorServidorConfig cs = new ControladorServidorConfig();
    	
    	fxmlLoader.setController(cs);
    	
		try {
			Scene scene = new Scene(fxmlLoader.load(), 375, 280);
			
			stage.setTitle("Configuraci�n de Servidor de eiChat");
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

    // M�todo para lanzar la aplicaci�n gr�fica
    public static void main(String[] args) {
        launch(args);
    }
}
