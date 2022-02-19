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



public class MensajeCellFactory implements Callback<ListView<Mensaje>, ListCell<Mensaje>>{
	
	@Override
  public ListCell<Mensaje> call(ListView<Mensaje> param) {
      return new ListCell<Mensaje>(){
          @Override
          public void updateItem(Mensaje m, boolean empty) {
              super.updateItem(m, empty);
              
              BackgroundFill bf = new BackgroundFill(Color.rgb(80, 80, 80), CornerRadii.EMPTY, Insets.EMPTY);
              Background b = new Background(bf);
              setBackground(b);

              if (empty || m == null)
            	  setText(null);
              else {
            	  setText(m.getContenido());

            	  if (m.isPropio() == false) {
            		  setTextFill(Color.WHITE);
            		  setAlignment(Pos.CENTER_LEFT);
            	  }
            	  else {
            		  setTextFill(Color.rgb(200, 200, 200));
            		  setAlignment(Pos.CENTER_RIGHT);
            	  }
              }
          }
      };
	}

}
