package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ID3 {

	/**
	 * Calcula el mérito de un atributo
	 * @param atributo	Atributo para el cuál se debe calcular el mérito
	 * @param valores	Lista de ejemplos con los valores de todos los atributos
	 * @param atributos	Lista de atributos
	 * @return	El mérito del atributo
	 */
	double merito(String atributo, ArrayList<String[]> valores, String[] atributos){
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
				
				if(valores.get(f)[columnaDecision] == "si")
					valueData.setPositive(valueData.getPositive() + 1);
				else
					valueData.setNegative(valueData.getNegative() + 1);
			}else{
				ValueData d = new ValueData();
				d.setTotal(1);
				
				if(valores.get(f)[columnaDecision] == "si")
					d.setPositive(1);
				else
					d.setNegative(1);
				
				valoresAtributo.put(valores.get(f)[columnaAtributo], d);
			}
		}
		
		for(String v : valoresAtributo.keySet()){
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
	double infor(int total, int positivos, int negativos){
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
	
	
	public Nodo algoritmoID3(ArrayList<String[]> listaEjemplos, String[] listaAtributos){
		//	Si la lista de ejemplos está vacía, el algoritmo termina
		if(listaEjemplos.isEmpty())
			return null;
		
		//	Si todos los ejemplos en la lista de ejemplos son positivos, se devuelve un nodo hoja positivo
		boolean allPositive = false;
		int f = 0;
		while(!allPositive && f < listaEjemplos.size()){
			String[] ejemplo = listaEjemplos.get(f);
			allPositive = ejemplo[ejemplo.length - 1] == "si";
		}
		
		if(allPositive)
			return new Nodo("Si", new ArrayList<Nodo>());
		
		
		//	Si todos los ejemplos en la lista de ejemplos son negativos, se devuelve un nodo hoja negativo
		boolean allNegative = false;
		f = 0;
		while(!allNegative && f < listaEjemplos.size()){
			String[] ejemplo = listaEjemplos.get(f);
			allNegative = ejemplo[ejemplo.length - 1] == "no";
		}
		
		if(allNegative)
			return new Nodo("no", new ArrayList<Nodo>());
		
		//	Cálculo y ordenación de los méritos de todos los atributos
		PriorityQueue<Pair> meritos =  new PriorityQueue<Pair>();
		
		for(String atributo : listaAtributos)
			meritos.add(new Pair(atributo, merito(atributo, listaEjemplos, listaAtributos)));
		
		String mejorAtributo = meritos.element().getFirst();
		
		// TODO: añadir al nodo la lista de valores del atributo
		
		return null;
		
	}
}
