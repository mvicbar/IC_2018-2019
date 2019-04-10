package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static Matriz tabla = new Matriz();
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String archivoAtributos, archivoEjemplos;
		
		System.out.println("Árbol de decisión - ID3");
		
		System.out.println("Indica el nombre del archivo de atributos: ");
		archivoAtributos = scanner.nextLine();
		System.out.println("Indica el nombre del archivo de ejemplos: ");
		archivoEjemplos = scanner.nextLine();
		
		tabla.leerFicheros(archivoAtributos, archivoEjemplos);
		
		//		Cálculo y ordenación de los méritos de todos los atributos
		PriorityQueue<Pair> meritos =  new PriorityQueue<Pair>();
		ArrayList<String> valoresAtributo = new ArrayList<String>();
		HashMap<String, ArrayList<String>> atrValores = new HashMap<String, ArrayList<String>>();
			
		for(String atributo : tabla.atributos){
			double m = merito(atributo, tabla.ejemplos, tabla.atributos, valoresAtributo);
			atrValores.put(atributo, valoresAtributo);
			meritos.add(new Pair(atributo, m));
		}
			
		String mejorAtributo = meritos.element().getFirst();
			
		Nodo raiz = new Nodo(mejorAtributo, meritos.element().getSecond(), atrValores.get(mejorAtributo), new ArrayList<Nodo>());
		
		System.out.println("El nodo raíz es: " + raiz.getAtributo());
		
		algoritmoID3(raiz, tabla.ejemplos, tabla.atributos);

		System.out.println("Hola");
		
	}

	/**
	 * Calcula el mérito de un atributo
	 * @param atributo	Atributo para el cuál se debe calcular el mérito
	 * @param valores	Lista de ejemplos con los valores de todos los atributos
	 * @param atributos	Lista de atributos
	 * @return	El mérito del atributo
	 */
	public static double merito(String atributo, ArrayList<String[]> valores, String[] atributos, ArrayList<String> valoresAtr){
		double merito = 0.0;
		int columnaAtributo = 0; boolean found = false;
		
		while(columnaAtributo < atributos.length && !found){
			found = atributos[columnaAtributo] == atributo;
			columnaAtributo++;
		}
		
		columnaAtributo = columnaAtributo - 1; int columnaDecision = atributos.length - 1;
		
		HashMap<String, ValueData> valoresAtributo = new HashMap<String, ValueData>();
		
		//	Rellena las tablas de cada valor del atributo, con sus ejemplos positivos y negativos
		for(int f = 0; f < valores.size(); ++f){
			
			if(valoresAtributo.containsKey(valores.get(f)[columnaAtributo])){
				ValueData valueData = valoresAtributo.get(valores.get(f)[columnaAtributo]);
				valueData.setTotal(valueData.getTotal() + 1);
				
				if(valores.get(f)[columnaDecision].equals("si"))
					valueData.setPositive(valueData.getPositive() + 1);
				else
					valueData.setNegative(valueData.getNegative() + 1);
			}else{
				ValueData d = new ValueData();
				d.setTotal(1);
				
				if(valores.get(f)[columnaDecision].equals("si"))
					d.setPositive(1);
				else
					d.setNegative(1);
				
				valoresAtributo.put(valores.get(f)[columnaAtributo], d);
			}
		}
		
		for(String v : valoresAtributo.keySet()){
			valoresAtr.add(v);
			ValueData vd = valoresAtributo.get(v);
			merito += (vd.getTotal()/valores.size()) * infor(vd.getTotal(), vd.getPositive(), vd.getNegative());
		}
		
		return merito;
	}
	
	
	/**
	 * Calcula la entropía (cantidad de información) del valor de un atributo
	 * @param total	Número total de ejemplos en los que aparece el valor concreto
	 * @param positivos	Número de ejemplos positivos
	 * @param negativos	Número de ejemplos negativos
	 * @return	Entropía del valor del atributo
	 */
	public static double infor(int total, int positivos, int negativos){
		double entropy = 0.0, p, n;
		
		n = (negativos/total);
		p = (positivos/total);	
		
		//	Se comprueba si alguno de los dos valores ('positivos' o 'negativos')
		//	es igual a 0, ya que en ese caso el logaritmo daría error y por ello
		//	no se debe calcular
		if(positivos == 0.0 && negativos != 0.0)
			entropy = - n * Math.log(n);
		else if(positivos != 0.0 && negativos == 0.0)
			entropy = -p * Math.log(p);
		else
			entropy = -p * Math.log(p) - n * Math.log(n);
		
		
		return entropy;
	}
	
	
	public static void algoritmoID3(Nodo raiz, ArrayList<String[]> listaEjemplos, String[] listaAtributos){
		//	Si la lista de ejemplos está vacía, el algoritmo termina
		if(listaEjemplos.isEmpty())
			raiz.addRama(null);
		
		//	Si todos los ejemplos en la lista de ejemplos son positivos, se devuelve un nodo hoja positivo
		boolean allPositive = true;
		int f = 0;
		while(allPositive && f < listaEjemplos.size()){
			String[] ejemplo = listaEjemplos.get(f);
			allPositive = ejemplo[ejemplo.length - 1].equals("si");
			f++;
		}
		
		if(allPositive)
			raiz.addRama(new Nodo("Si", new ArrayList<Nodo>()));
		
		
		//	Si todos los ejemplos en la lista de ejemplos son negativos, se devuelve un nodo hoja negativo
		boolean allNegative = true;
		f = 0;
		while(allNegative && f < listaEjemplos.size()){
			String[] ejemplo = listaEjemplos.get(f);
			allNegative = ejemplo[ejemplo.length - 1].equals("no");
			f++;
		}
		
		if(allNegative)
			raiz.addRama(new Nodo("No", new ArrayList<Nodo>()));
		
		//	Cálculo y ordenación de los méritos de todos los atributos
		PriorityQueue<Pair> meritos =  new PriorityQueue<Pair>();
		ArrayList<String> valoresAtributo = new ArrayList<String>();
		HashMap<String, ArrayList<String>> atrValores = new HashMap<String, ArrayList<String>>();
		
		for(String atributo : listaAtributos){
			double m = merito(atributo, listaEjemplos, listaAtributos, valoresAtributo);
			atrValores.put(atributo, valoresAtributo);
			meritos.add(new Pair(atributo, m));
		}
		
		String mejorAtributo = meritos.element().getFirst();
		
		Nodo mejor = new Nodo(mejorAtributo, meritos.element().getSecond(), atrValores.get(mejorAtributo), new ArrayList<Nodo>());
		
		for(String valor : mejor.getValores()){
			tabla.partirMatriz(mejor.getAtributo(), valor);

			algoritmoID3(mejor, tabla.ejemplos, tabla.atributos);
		}

	}
	
}
