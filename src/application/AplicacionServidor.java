/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García
		
	2° D.A.M.
	
	Práctica "Chat Colectivo" - Programación de Servicios y Procesos
	
	
	
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
import controller.ControladorServidor;

import model.Servidor;



public class AplicacionServidor extends Application {

    @Override
    public void start(Stage stage) {
    	URL rutaRecurso = getClass().getResource("/view/VistaServidor.fxml");
    	FXMLLoader fxmlLoader = new FXMLLoader(rutaRecurso);
    	
    	Servidor servidor = new Servidor(1234);
    	
    	ControladorServidor cs = new ControladorServidor(servidor);
    	
    	fxmlLoader.setController(cs);
    	
		try {
			Scene scene = new Scene(fxmlLoader.load(), 785, 895);
			
			stage.setTitle("Servidor de eiChat");
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

    // Método para lanzar la aplicación gráfica
    public static void main(String[] args) {
        launch(args);
    }
}
