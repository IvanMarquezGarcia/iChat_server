package controller;




import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseEvent;

import javafx.scene.text.Text;

import javafx.stage.Stage;

import model.Cliente;




public class ControladorDatosCliente {

	private Cliente cliente;
	
	
	
	public ControladorDatosCliente() {
		this(null);
	}
	
	public ControladorDatosCliente(Cliente c) {
		this.cliente = c;
	}
	
	
	
	@FXML
	public void initialize() {}
	
	
    
	public void setCliente(Cliente c) {
		this.cliente = c;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	
	
	@FXML
    private Button cerrar_button;

    @FXML
    private Button conectar_button;

    @FXML
    private TextField hostDatos_textField;

    @FXML
    private TextField nombreDatos_textField;

    @FXML
    private TextField puertoDatos_textField;
    
    @FXML
    private Text error_text;
    

    @FXML
    void cerrar(MouseEvent event) {
    	System.exit(0);
    }

    @FXML
    void conectar(MouseEvent event) {
    	error_text.setVisible(false);
    	
    	String nombre = nombreDatos_textField.getText().trim();
    	String puerto = puertoDatos_textField.getText().trim();
    	
    	if (nombre.length() > 0 && puerto.length() > 0) {
	    	String host = hostDatos_textField.getText().trim();
	    	
	    	if (host.length() == 0)
	    		host = "127.0.0.1";
	    	
	    	try {
				Socket s = new Socket(host, Integer.parseInt(puerto));
				
				cliente.setNombre(nombre);
		    	cliente.setSocket(s);
		    	
		    	// Cerrar ventana de datos
		    	Node source = (Node) event.getSource();
		        Stage stage = (Stage) source.getScene().getWindow();
		        stage.close();
		        
		        
		        // Abrir ventana de chat
		        URL rutaVistaCliente = getClass().getResource("/view/VistaCliente.fxml");
		    	FXMLLoader vistaClienteLoader = new FXMLLoader(rutaVistaCliente);
		    	
		    	ControladorCliente cc = new ControladorCliente(cliente);
		    	
		    	vistaClienteLoader.setController(cc);
		    	
		    	//cliente.setTextArea(cc.getTextArea());
		    	
		    	//cc.getNombreText().setText(cliente.getNombre());
		    	
				try {
					Scene scene = new Scene(vistaClienteLoader.load(), 530, 600);
					
					stage.setTitle("Cliente de eiChat | " + cliente.getNombre());
					stage.setScene(scene);
			    	stage.show();
			    	
			    	cliente.conectar();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error al cargar la ventana");
				}
			}
	    	catch (ConnectException ce) {
	    		ce.printStackTrace();
				
	    		String error = "Error al conectar";
	    		
				System.out.println("---------------------------------------------------------------------");
				System.out.println(error);
				
				error_text.setText(error);
				error_text.setVisible(true);
	    	}
	    	catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				
				String error = "Puerto no válido";
	    		
				System.out.println("---------------------------------------------------------------------");
				System.out.println(error);
				
				error_text.setText(error);
				error_text.setVisible(true);
				
				System.exit(0);
    		}
	    	catch (IOException ioe) {
	    		ioe.printStackTrace();
	    		
	    		String error = "Error de E/S";
	    		
				System.out.println("---------------------------------------------------------------------");
				System.out.println(error);
				
				error_text.setText(error);
				error_text.setVisible(true);
				
				System.exit(0);
	    	}
    	}
    }
    
}
