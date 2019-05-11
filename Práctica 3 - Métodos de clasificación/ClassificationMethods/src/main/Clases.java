package main;

import java.util.ArrayList;

/**
 * This object contains the different classes in with the samples are to be 
 * classified.
 * 
 * @author mvicbar
 *
 */
public class Clases {

	private ArrayList<Muestra> irisSetosa;
	private ArrayList<Muestra> irisVersicolor;
	
	public Clases(){
		this.irisSetosa = new ArrayList<Muestra>();
		this.irisVersicolor = new ArrayList<Muestra>();
	}
	
	ArrayList<Muestra> getMuestrasSetosa(){
		return this.irisSetosa;
	}
	
	ArrayList<Muestra> getMuestrasVersicolor(){
		return this.irisVersicolor;
	}
}
