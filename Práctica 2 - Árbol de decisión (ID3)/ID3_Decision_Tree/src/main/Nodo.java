package main;

import java.util.ArrayList;

public class Nodo {

	private String atributo;
	private ArrayList<Nodo> ramas;
	private ArrayList<String> valores;
	private double merito;
	
	public Nodo(String atributo, ArrayList<Nodo> ramas){
		this.atributo = atributo;
		this.ramas = ramas;
	}

	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}

	public ArrayList<Nodo> getRamas() {
		return ramas;
	}

	public void setRamas(ArrayList<Nodo> ramas) {
		this.ramas = ramas;
	}

	public ArrayList<String> getValores() {
		return valores;
	}

	public void setValores(ArrayList<String> valores) {
		this.valores = valores;
	}

	public double getMerito() {
		return merito;
	}

	public void setMerito(double merito) {
		this.merito = merito;
	}
	
	
}
