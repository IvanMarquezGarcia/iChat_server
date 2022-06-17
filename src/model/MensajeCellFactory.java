/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	Proyecto final - iChat


	------------------------------- DESCRIPCIÓN -------------------------------
	
	Clase que se encarga de crear y mostrar mensajes en el ListView del chat.
 */



package model;



import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import javafx.scene.paint.Color;

import javafx.util.Callback;



public class MensajeCellFactory implements Callback<ListView<String>, ListCell<String>>{
	
	@Override
  public ListCell<String> call(ListView<String> param) {
      return new ListCell<String>(){
    	  {
    		  // ajustar celda al ancho de la lista
    		  setWrapText(true);
    		  setPrefWidth(0);
    	  }
          @Override
          public void updateItem(String str_msg, boolean empty) {
              super.updateItem(str_msg, empty);
              
              BackgroundFill bf = new BackgroundFill(Color.rgb(80, 80, 80), CornerRadii.EMPTY, Insets.EMPTY);
              Background b = new Background(bf);
              setBackground(b);

              if (empty || str_msg == null)
            	  setText(null);
              else {
            	  setText(str_msg);
            	  setTextFill(Color.WHITE);
            	  setAlignment(Pos.CENTER_LEFT);
              }
          }
      };
	}

}
