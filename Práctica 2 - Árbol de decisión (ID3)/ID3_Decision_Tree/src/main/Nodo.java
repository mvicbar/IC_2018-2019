package main;

import java.util.ArrayList;

public class Nodo {

	private String atributo;
	private ArrayList<Nodo> ramas;
	private double merito;
	
	public Nodo(String atributo, ArrayList<Nodo> ramas){
		this.atributo = atributo;
		this.ramas = ramas;
	}
}
