/*
	Hecho por: Iván Márquez García
		
	2° D.A.M.
	
	Proyecto final - iChat


	------------------------------- DESCRIPCIÓN -------------------------------
	
	Clase que representa al usuario.
*/

package model;



public class User {

	private final int id;
	private String username;
	private String language;
	
	
	public User(int id, String username, String language) {
		this.id = id;
		this.username = username;
		this.language = language;
	}
	
	
	public int getId() { return id; }
	
	public String getUsername() { return username; }
	
	public String getLanguage() { return language; }
	
	
	
	
}
