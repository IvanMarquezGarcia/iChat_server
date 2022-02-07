package controller;




import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
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

import model.Cliente;




public class ControladorClienteLogin {

	private Cliente cliente;
	
	private double xOffset;
	private double yOffset;
	
	
	
	public ControladorClienteLogin() {
		this(null);
	}
	
	public ControladorClienteLogin(Cliente c) {
		this.cliente = c;
	}
	
	
    
	public void setCliente(Cliente c) {
		this.cliente = c;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	
	
    @FXML
    private VBox root;
	
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
		        URL rutaVistaCliente = getClass().getResource("/view/VistaClienteChat.fxml");
		    	FXMLLoader vistaClienteLoader = new FXMLLoader(rutaVistaCliente);
		    	
		    	ControladorClienteChat cc = new ControladorClienteChat(cliente);
		    	
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
					System.out.println("---------------------------------------------------------------------");
					e.printStackTrace();
					System.out.println("Error al cargar la ventana");
					System.out.println("---------------------------------------------------------------------");
				}
			}
	    	catch (ConnectException ce) {
	    		String errorMsg = "Error al conectar";
	    		
	    		System.out.println("---------------------------------------------------------------------");
	    		ce.printStackTrace();
	    		System.out.println(errorMsg);
				System.out.println("---------------------------------------------------------------------");
				
				error_text.setText(errorMsg);
				error_text.setVisible(true);
	    	}
	    	catch (NumberFormatException nfe) {
				String errorMsg = "Puerto no válido";
	    		
				System.out.println("---------------------------------------------------------------------");
				nfe.printStackTrace();
				System.out.println(errorMsg);
				System.out.println("---------------------------------------------------------------------");
				
				error_text.setText(errorMsg);
				error_text.setVisible(true);
				
				System.exit(0);
    		}
	    	catch (IOException ioe) {
	    		String errorMsg = "Error de E/S";
	    		
	    		System.out.println("---------------------------------------------------------------------");
	    		ioe.printStackTrace();
				System.out.println(errorMsg);
				System.out.println("---------------------------------------------------------------------");
				
				error_text.setText(errorMsg);
				error_text.setVisible(true);
				
				System.exit(0);
	    	}
    	}
    }
    
    @FXML
	public void initialize() {
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
