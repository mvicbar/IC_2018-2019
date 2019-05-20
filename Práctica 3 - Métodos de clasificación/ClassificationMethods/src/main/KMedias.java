package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class KMedias {
	
	private static double tolerancia = 0.1;
	
	private static Muestra centroVersicolor = new Muestra(6.8, 3.4, 4.6, 0.7);
	private static Muestra centroSetosa = new Muestra(4.6, 3.0, 4.0, 0.0);
	
	private static Muestra centroSetosaAnterior = new Muestra(0.0, 0.0, 0.0, 0.0);
	private static Muestra centroVersicolorAnterior = new Muestra(0.0, 0.0, 0.0, 0.0);
	
	private static ArrayList<Double> gradosPertenenciaSetosa = new ArrayList<Double>();
	private static ArrayList<Double> gradosPertenenciaVersicolor = new ArrayList<Double>();
	
	private static HashMap<String, ArrayList<Double>> distanciasSetosa = new HashMap<String, ArrayList<Double>>();
	private static HashMap<String, ArrayList<Double>> distanciasVersicolor = new HashMap<String, ArrayList<Double>>();

	public static int clasificacionKMedias() {
		Scanner scanner = new Scanner(System.in);
		String muestra;
		
		System.out.println("Indique el nombre del archivo que contiene la muesta a clasificar (.txt): ");
		muestra = scanner.nextLine();
		
		if(!muestra.equals("0")){
			try {
				BufferedReader bfMuestra = new BufferedReader(new FileReader(muestra));
				
				String line;
				
				line = bfMuestra.readLine();
				String[] l = line.split(",");
				
				Muestra m = new Muestra(Double.parseDouble(l[0]), Double.parseDouble(l[1]), Double.parseDouble(l[2]), Double.parseDouble(l[3]));
				
				double distSetosa = Main.distancia(m, centroSetosa);
				double distVersicolor = Main.distancia(m, centroVersicolor);
				
				if(distSetosa < distVersicolor)
					System.out.println("\nLa muestra pertenece a la clase IRIS-SETOSA\n");
				else
					System.out.println("\nLa muestra pertenece a la clase IRIS-VERSICOLOR\n");
				
				bfMuestra.close();
				//scanner.close();
					
			} catch (FileNotFoundException e) {
				System.out.println("No se han podido encontrar los ficheros indicados");
			} catch (IOException e) {
				System.out.println("Error en la lectura del fichero");
			}
			
			return 1;
		}else
			return 0;
	}
	
	
	/**
	 * Comprueba si la desviación de los centros con respecto a sus valores anteriores es
	 * menor que una constante.
	 * @return
	 */
	public static boolean criterioFinalizacion() {
		double cambioSetosa = Math.sqrt(Main.distancia(centroSetosaAnterior, centroSetosa));
		double cambioVersicolor = Math.sqrt(Main.distancia(centroVersicolorAnterior, centroVersicolor));
		
		return cambioSetosa <= tolerancia && cambioVersicolor <= tolerancia;
	}
	
	
	/**
	 * Calcula los nuevos centros de las clases en función de los grados de pertenencia
	 * de cada muestra a cada clase.
	 * @param clases
	 */
	public static void calculoNuevosCentros(HashMap<String, ArrayList<Muestra>> clases) {
		double b = 0.0;
		double c1 = 0.0, c2 = 0.0, c3 = 0.0, c4 = 0.0;
		
		centroSetosaAnterior = new Muestra(centroSetosa.getCoordenada1(), centroSetosa.getCoordenada2(), centroSetosa.getCoordenada3(), centroSetosa.getCoordenada4());
		centroVersicolorAnterior = new Muestra(centroVersicolor.getCoordenada1(), centroVersicolor.getCoordenada2(), centroVersicolor.getCoordenada3(), centroVersicolor.getCoordenada4());
		
		for(int i = 0; i < clases.get("Iris-setosa").size(); i++){
			Muestra muestraSetosa = clases.get("Iris-setosa").get(i);
			Muestra muestraVersicolor = clases.get("Iris-versicolor").get(i);
			
			double cuadradoSetosa = Math.pow(gradosPertenenciaSetosa.get(i), 2);
			double cuadradoVersicolor = Math.pow(gradosPertenenciaSetosa.get(i + 50), 2);
			b += cuadradoSetosa + cuadradoVersicolor;
			c1 += (muestraSetosa.getCoordenada1() * cuadradoSetosa) + (muestraVersicolor.getCoordenada1() * cuadradoVersicolor);
			c2 += (muestraSetosa.getCoordenada2() * cuadradoSetosa) + (muestraVersicolor.getCoordenada2() * cuadradoVersicolor);
			c3 += (muestraSetosa.getCoordenada3() * cuadradoSetosa) + (muestraVersicolor.getCoordenada3() * cuadradoVersicolor);
			c4 += (muestraSetosa.getCoordenada4() * cuadradoSetosa) + (muestraVersicolor.getCoordenada4() * cuadradoVersicolor);
		}
		
		centroSetosa.setCoordenada1(c1/b);
		centroSetosa.setCoordenada2(c2/b);
		centroSetosa.setCoordenada3(c3/b);
		centroSetosa.setCoordenada4(c4/b);
		
		c1 = c2 = c3 = c4 = b = 0.0;
		
		for(int j = 0; j < clases.get("Iris-versicolor").size(); j++){
			Muestra muestraVersicolor = clases.get("Iris-versicolor").get(j);
			Muestra muestraSetosa = clases.get("Iris-setosa").get(j);
			
			double cuadradoSetosa = Math.pow(gradosPertenenciaVersicolor.get(j), 2);
			double cuadradoVersicolor = Math.pow(gradosPertenenciaVersicolor.get(j + 50), 2);
			b += cuadradoSetosa + cuadradoVersicolor;
			c1 += (muestraSetosa.getCoordenada1() * cuadradoSetosa) + (muestraVersicolor.getCoordenada1() * cuadradoVersicolor);
			c2 += (muestraSetosa.getCoordenada2() * cuadradoSetosa) + (muestraVersicolor.getCoordenada2() * cuadradoVersicolor);
			c3 += (muestraSetosa.getCoordenada3() * cuadradoSetosa) + (muestraVersicolor.getCoordenada3() * cuadradoVersicolor);
			c4 += (muestraSetosa.getCoordenada4() * cuadradoSetosa) + (muestraVersicolor.getCoordenada4() * cuadradoVersicolor);
		}
		
		centroSetosa.setCoordenada1(c1/b);
		centroSetosa.setCoordenada2(c2/b);
		centroSetosa.setCoordenada3(c3/b);
		centroSetosa.setCoordenada4(c4/b);
		
	}
	
	
	/**
	 * Calcula el grado de pertenencia de cada muestra a cada una de las clases.
	 * @param clases
	 */
	public static void calculoGradosPertenencia(HashMap<String, ArrayList<Muestra>> clases) {
		
		for(int i = 0; i < clases.get("Iris-setosa").size(); ++i){
			gradosPertenenciaSetosa.add((1/distanciasSetosa.get("Iris-setosa").get(i))/((1/distanciasSetosa.get("Iris-setosa").get(i)) + (1/distanciasVersicolor.get("Iris-setosa").get(i))));
			gradosPertenenciaVersicolor.add((1/distanciasVersicolor.get("Iris-setosa").get(i))/((1/distanciasSetosa.get("Iris-setosa").get(i)) + (1/distanciasVersicolor.get("Iris-setosa").get(i))));
		}

		for(int j = 0; j < clases.get("Iris-versicolor").size(); ++j){
			gradosPertenenciaVersicolor.add((1/distanciasVersicolor.get("Iris-versicolor").get(j))/((1/distanciasVersicolor.get("Iris-versicolor").get(j)) + (1/distanciasSetosa.get("Iris-versicolor").get(j))));
			gradosPertenenciaSetosa.add((1/distanciasSetosa.get("Iris-versicolor").get(j))/((1/distanciasVersicolor.get("Iris-versicolor").get(j)) + (1/distanciasSetosa.get("Iris-versicolor").get(j))));
		}
		
	}
	
	
	/**
	 * Calcula la distancia de cada una de las muestras a cada uno de los centros
	 * de las clases.
	 * @param clases
	 */
	public static void calculoDistanciasACentros(HashMap<String, ArrayList<Muestra>> clases) {
		ArrayList<Double> distSetosa = new ArrayList<Double>();
		ArrayList<Double> distVersicolor = new ArrayList<Double>();
		
		//	Se recorren las muestras pertenencientes a la clase Iris-setosa
		for(Muestra m : clases.get("Iris-setosa")){
			distSetosa.add(Main.distancia(centroSetosa, m));	//	se calcula la distancia de cada muestra a cada uno de los centros
			distVersicolor.add(Main.distancia(centroVersicolor, m));
		}
		
		distanciasSetosa.put("Iris-setosa", distSetosa);	// Se añaden las distancias de las muestras de la clase Iris-setosa al centro de la clase Iris-setosa
		distanciasVersicolor.put("Iris-setosa", distVersicolor);	// Se añaden las distancias de las muestras de la clase Iris-setosa al centro de la clase Iris-versicolor
		distSetosa.clear(); distVersicolor.clear();
		
		//	Análogo al anterior
		for(Muestra m : clases.get("Iris-versicolor")){
			distSetosa.add(Main.distancia(centroSetosa, m));
			distVersicolor.add(Main.distancia(centroVersicolor, m));
		}
		
		distanciasSetosa.put("Iris-versicolor", distSetosa);
		distanciasVersicolor.put("Iris-versicolor", distVersicolor);
	}

}
