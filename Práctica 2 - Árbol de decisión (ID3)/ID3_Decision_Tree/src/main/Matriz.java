package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 	Clase que representa la tabla que contiene los atributos y los ejemplos de los cuales
 * 	se deduce el árbol de decisión.
 * 
 * 	Los atributos se guardan en un array de Strings, mientras que los ejemplos se guardan en 
 * 	un ArrayList, siendo cada ejemplo un array de Strings.
 * @author victo
 *
 */
public class Matriz {
	private int F;
	private int C;
	
	private String[] atributos;
	private ArrayList<String[]> ejemplos;
	
	public Matriz(){
		this.F = -1;
		this.C = -1;
		this.atributos = new String[10];
		this.ejemplos = new ArrayList<String[]>();
	}
	
	public Matriz(int F, int C, String[] atributos, ArrayList<String[]> ejemplos){
		this.F = F;
		this.C = C;
		this.atributos = atributos;
		this.ejemplos = ejemplos;
	}
	
	public int getF() {
		return F;
	}
	public void setF(int f) {
		F = f;
	}
	public int getC() {
		return C;
	}
	public void setC(int c) {
		C = c;
	}
	public String[] getAtributos() {
		return atributos;
	}
	public void setAtributos(String[] atributos) {
		this.atributos = atributos;
	}
	public ArrayList<String[]> getEjemplos() {
		return ejemplos;
	}
	public void setEjemplos(ArrayList<String[]> ejemplos) {
		this.ejemplos = ejemplos;
	}
	
	
	/**
	 * Lee los ficheros indicados por el usuario e introduce los datos en la matriz.
	 * @param nombreFicheroAtributos	Fichero donde se indican los nombres de los atributos
	 * @param nombreFicheroEjemplos		Fichero donde se indican los ejemplos
	 */
	public void leerFicheros(String nombreFicheroAtributos, String nombreFicheroEjemplos){
		
		try {
			BufferedReader bfAtrs = new BufferedReader(new FileReader(nombreFicheroAtributos));
			BufferedReader bfEjs = new BufferedReader(new FileReader(nombreFicheroEjemplos));
			
			this.atributos = bfAtrs.readLine().split(",");
			this.C = this.atributos.length;
			String lineaEjemplo;
			
			this.ejemplos = new ArrayList<String[]>();
			while((lineaEjemplo = bfEjs.readLine()) != null){
				this.ejemplos.add(lineaEjemplo.split(","));
			}
			
			this.F = this.ejemplos.size();
			
			bfAtrs.close();
			bfEjs.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("No se han podido encontrar los ficheros indicados");
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero");
		}
		
	}
	
	
	/**
	 * Parte la matriz y la devuelve sin la comuna del 'atributo' y sólo con los ejemplos donde el valor para ese
	 * atributo coincide con 'valor'
	 * @param atributo	Nombre del atributo que tiene que ser eliminado de la matriz
	 * @param valor		Nombre del valor del atributo que deben tener los ejemplos
	 */
	public Matriz partirMatriz(String atributo, String valor){
		int columnaAtributo = -1;
		boolean found = false;
		
		//	Se localiza el índice del atributo dentro del array de atributos
		int i = 0;
		while(i < this.atributos.length && !found){
			found = this.atributos[i] == atributo;
			i++;
		}
		
		//	Una vez encontrado, se elimina el atributo del array
		if(found){
			columnaAtributo = i - 1;
			for(int j = i - 1; j < this.atributos.length - 1; ++j)
				this.atributos[j] = this.atributos[j+1];
		}
		
		this.C = this.C - 1;
		
		ArrayList<String[]> ejemplosValor = new ArrayList<String[]>();
		
		//	Se buscan y guardan las filas donde el valor del atributo es el obtenido por parámetro
		for(int f = 0; f < this.F; f++){
			if(this.ejemplos.get(f)[columnaAtributo].equals(valor)){
				ejemplosValor.add(this.ejemplos.get(f));
			}
		}
		
		//	Por cada fila guardada, se elimina la columna del atributo
		for(String[] ejemplo : ejemplosValor){
			for(int j = columnaAtributo; j < ejemplo.length - 1; j++)
				ejemplo[j] = ejemplo[j + 1];
		}
		
		this.ejemplos = ejemplosValor;
		this.F = ejemplosValor.size();
		
		return this;
	}
	
	/**
	 * Hace una copia de una matriz de atributos y ejemplos
	 */
	public void copiarTabla(Matriz tabla){
		int F = tabla.F;
		int C = tabla.C;
		
		this.F = F; this.C = C;
		
		//	Se hace una copia del array de atributos
		String[] atributos = new String[tabla.atributos.length];
		
		for(int i = 0; i < atributos.length; i++)
			atributos[i] = tabla.atributos[i];
		
		//	Se hace una copia de cada array de ejemplo
		ArrayList<String[]> ejemplos = new ArrayList<String[]>();
		
		for(int i = 0; i < tabla.ejemplos.size(); i++){
			String[] e = tabla.ejemplos.get(i);
			String[] ej = new String[e.length];
			for(int j = 0; j < e.length; j++)
				ej[j] = e[j];
			
			ejemplos.add(ej);
		}
		
		this.atributos = atributos;
		this.ejemplos = ejemplos;
	}

}
