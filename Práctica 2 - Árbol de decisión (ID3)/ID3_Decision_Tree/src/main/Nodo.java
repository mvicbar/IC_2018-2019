package main;

import java.util.HashMap;

/**
 * 	Clase que representa un nodo del árbol de decisión.
 * 
 * 	Cuenta con un atributo de tipo String que contiene el atributo al que hace referencia el nodo.
 * 	Los hijos del nodo se guardan en un HashMap que relaciona cada valor del atributo, con el árbol
 * 	que corresponde.
 * 	También guarda el mérito del atributo.
 * @author victo
 *
 */
public class Nodo {

	private String atributo;
	HashMap<String, Nodo> ramas;
	private double merito;
	
	public Nodo(){
		this.ramas = new HashMap<String, Nodo>();
	}
	
	public Nodo(String atributo, HashMap<String, Nodo> ramas){
		this.atributo = atributo;
		this.ramas = ramas;
	}
	
	public Nodo(String atributo, double merito, HashMap<String, Nodo> ramas){
		this.atributo = atributo;
		this.merito = merito;
		this.ramas = ramas;
	}

	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}

	public HashMap<String, Nodo> getRamas() {
		return ramas;
	}
	
	public void addRama(String valor, Nodo n){
		this.ramas.put(valor, n);
	}

	public void setRamas(HashMap<String, Nodo> ramas) {
		this.ramas = ramas;
	}

	public double getMerito() {
		return merito;
	}

	public void setMerito(double merito) {
		this.merito = merito;
	}
	
	
}
