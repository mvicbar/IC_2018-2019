package main;

import java.util.ArrayList;

public class Nodo {

	private String atributo;
	private ArrayList<Nodo> ramas;
	private ArrayList<String> valores;
	private double merito;
	
	public Nodo(){
		this.ramas = new ArrayList<Nodo>();
		this.valores = new ArrayList<String>();
	}
	
	public Nodo(String atributo, ArrayList<Nodo> ramas){
		this.atributo = atributo;
		this.ramas = ramas;
	}
	
	public Nodo(String atributo, double merito, ArrayList<String> valores, ArrayList<Nodo> ramas){
		this.atributo = atributo;
		this.merito = merito;
		this.valores = valores;
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
	
	public void addRama(Nodo n){
		this.ramas.add(n);
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
