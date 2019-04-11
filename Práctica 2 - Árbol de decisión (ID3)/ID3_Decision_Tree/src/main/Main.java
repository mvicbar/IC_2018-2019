package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

	
	public static void main(String[] args) {
		Matriz tabla = new Matriz();
		Scanner scanner = new Scanner(System.in);
		String archivoAtributos, archivoEjemplos;
		
		System.out.println("Árbol de decisión - ID3");
		
		System.out.println("Indique el nombre del archivo de atributos (.txt): ");
		archivoAtributos = scanner.nextLine();
		System.out.println("Indique el nombre del archivo de ejemplos (.txt): ");
		archivoEjemplos = scanner.nextLine();
		
		tabla.leerFicheros(archivoAtributos, archivoEjemplos);
		
		Nodo raiz = new Nodo();
		algoritmoID3(raiz, tabla);
		
		if(tabla.getC() != -1 && tabla.getF() != -1){
		
			ArrayList<ArrayList<String>> reglas = new ArrayList<ArrayList<String>>();
			ArrayList<String> regla = new ArrayList<String>();
			imprimirReglas(raiz, reglas, regla);
			
			System.out.println("");
			System.out.println("Estas son las reglas deducidas del árbol de decisión: \n");
			
			for(ArrayList<String> r : reglas){
				String re = "";
				for(String s : r){
					re = re + s;
				}
				
				System.out.println(re);
			}
					
			scanner.close();
		}
	}

	/**
	 * Calcula el mérito de un atributo
	 * @param atributo	Atributo para el cuál se debe calcular el mérito
	 * @param valores	Lista de ejemplos con los valores de todos los atributos
	 * @param atributos	Lista de atributos
	 * @return	El mérito del atributo
	 */
	public static double merito(String atributo, Matriz tabla, ArrayList<String> valoresAtr) {
		double merito = 0.0;
		int columnaAtributo = 0; boolean found = false;
		ArrayList<String[]> valores = tabla.getEjemplos();
		String[] atributos = tabla.getAtributos();
		
		while(columnaAtributo < tabla.getC() && !found){
			found = atributos[columnaAtributo].equals(atributo);
			columnaAtributo++;
		}
		
		columnaAtributo = columnaAtributo - 1; int columnaDecision = atributos.length - 1;
		
		HashMap<String, ValueData> valoresAtributo = new HashMap<String, ValueData>();
		
		//	Rellena las tablas de cada valor del atributo, con sus ejemplos positivos y negativos
		for(int f = 0; f < tabla.getF(); ++f){
			
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
	public static double infor(double total, double positivos, double negativos){
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
	
	/**
	 * Algoritmo recursivo que, dado una tabla con atributos y ejemplos, crea un árbol de decisión
	 * @param raiz	Nodo en el que se va a crear en árbol de decisión
	 * @param tabla	Tabla con los atributos y ejemplos
	 */
	public static void algoritmoID3(Nodo raiz, Matriz tabla){
		ArrayList<String[]> listaEjemplos = tabla.getEjemplos();
		
		//	Si la lista de ejemplos está vacía, el algoritmo termina
		if(listaEjemplos.isEmpty())
			raiz.addRama(null, null);
		
		//	Si todos los ejemplos en la lista de ejemplos son positivos, se devuelve un nodo hoja positivo
		boolean allPositive = true;
		int f = 0;
		while(allPositive && f < listaEjemplos.size()){
			String[] ejemplo = listaEjemplos.get(f);
			allPositive = ejemplo[ejemplo.length - 1].equals("si");
			f++;
		}
		
		if(allPositive)
			raiz.setAtributo("Sí");
		else{
			
			//	Si todos los ejemplos en la lista de ejemplos son negativos, se devuelve un nodo hoja negativo
			boolean allNegative = true;
			f = 0;
			while(allNegative && f < listaEjemplos.size()){
				String[] ejemplo = listaEjemplos.get(f);
				allNegative = ejemplo[ejemplo.length - 1].equals("no");
				f++;
			}
			
			if(allNegative)
				raiz.setAtributo("No");
			else{
		
				//	Cálculo y ordenación de los méritos de todos los atributos
				TreeMap<Double, String> meritos = new TreeMap<Double, String>();
				HashMap<String, ArrayList<String>> atrValores = new HashMap<String, ArrayList<String>>();
				
				for(int i = 0; i < tabla.getC() - 1; i++){
					String atributo = tabla.getAtributos()[i];
					ArrayList<String> valoresAtributo = new ArrayList<String>();
					double m = merito(atributo, tabla, valoresAtributo);
					atrValores.put(atributo, valoresAtributo);
					meritos.put(m, atributo);
				}
					
				String mejorAtributo = meritos.get(meritos.firstKey());
					
				raiz.setAtributo(mejorAtributo);
				raiz.setMerito(meritos.firstKey());
				raiz.setRamas(new HashMap<String, Nodo>());
								
				for(String valor : atrValores.get(mejorAtributo)){
					raiz.addRama(valor, new Nodo("", new HashMap<String, Nodo>()));
				}
				
				for(String valor : raiz.ramas.keySet()){
					Matriz partida = new Matriz();
					partida.copiarTabla(tabla);
					partida.partirMatriz(mejorAtributo, valor);
					Nodo hijo = raiz.getRamas().get(valor);
					algoritmoID3(hijo, partida);
				}
				
			}
		}

	}
	
	
	/**
	 * Rellena un ArrayList con las reglas deducidas del árbol de decisión
	 * @param raiz	Nodo que se está evaluando
	 * @param reglas	ArrayList que contiene todas las reglas
	 * @param regla	Reglas que se está formando en ese momento
	 */
	public static void imprimirReglas(Nodo raiz, ArrayList<ArrayList<String>> reglas, ArrayList<String> regla){
		
		regla.add(raiz.getAtributo().toUpperCase());
		
		for(String valor : raiz.getRamas().keySet()){
			regla.add(" - ");
			regla.add(valor + " -> ");
			Nodo hijo = raiz.getRamas().get(valor);
			imprimirReglas(hijo, reglas, regla);
		}
		
		if(raiz.getRamas().isEmpty()){
		
			ArrayList<String> aux = new ArrayList<String>();
			for(String s : regla)
				aux.add(s);
			
			reglas.add(aux);		
		}
		
		if(regla.size() != 1){
			regla.remove(regla.size() - 1);
			regla.remove(regla.size() - 1);
			regla.remove(regla.size() - 1);
		}
	}
	
}
