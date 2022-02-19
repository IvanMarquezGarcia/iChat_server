/*
	Hecho por:
		Eloy Guillermo Villadóniga Márquez
		e
		Iván Márquez García

	2° D.A.M.

	Práctica "Chat Colectivo" - Programación de Servicios y Procesos



	------------------------------- DESCRIPCIÓN -------------------------------
	
	Representa a un mensaje del chat.
 */



package model;



public class Mensaje {
	
	String contenido;
	boolean propio;
	
	
	public Mensaje(String contenido) {
		this(contenido, false);
	}
	
	public Mensaje(String contenido, boolean propio) {
		this.contenido = contenido;
		this.propio = propio;
	}

	
	public String getContenido() {
		return contenido;
	}

	public boolean isPropio() {
		return propio;
	}
	
}
