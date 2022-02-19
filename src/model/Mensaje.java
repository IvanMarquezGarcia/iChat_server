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
