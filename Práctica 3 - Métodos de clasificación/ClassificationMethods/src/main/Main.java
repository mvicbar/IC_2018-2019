package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	
	private static double razonAprendizaje = 0.1;
	private static int kmax = 10;
	private static HashMap<String, ArrayList<Muestra>> clases;
	
	private static ArrayList<Double> distanciasSetosa = new ArrayList<Double>();	// Posiciones 0 - 49, clase setosa. Posiciones 50 - 99, clase versicolor.
	private static ArrayList<Double> distanciasVersicolor = new ArrayList<Double>();
	
	private static ArrayList<Double> gradosPertenenciaSetosa = new ArrayList<Double>();
	private static ArrayList<Double> gradosPertenenciaVersicolor = new ArrayList<Double>();
	
	private static Muestra centroVersicolor, centroSetosa, centroSetosaAnterior, centroVersicolorAnterior;

	public static void main(String[] args) {	
		centroSetosa = new Muestra(4.6, 3.0, 4.0, 0.0);
		centroVersicolor = new Muestra(6.8, 3.4, 4.6, 0.7);
		centroSetosaAnterior = new Muestra(0.0, 0.0, 0.0, 0.0);
		centroVersicolorAnterior = new Muestra(0.0, 0.0, 0.0, 0.0);
		inicializarClases();
		
		if(!criterioFinalizacion()){
			calculoDistanciasACentros();
			calculoGradosPertenencia();
			calculoNuevosCentros();
		}
		
		clasificacionKMedias();
//		entrenamientoLloyd();
//		clasificacionLloyd();
	}
	
	
	private static void clasificacionKMedias() {
		Scanner scanner = new Scanner(System.in);
		String muestra;
		
		System.out.println("Indique el nombre del archivo que contiene la muesta a clasificar (.txt): ");
		muestra = scanner.nextLine();
		
		try {
			BufferedReader bfMuestra = new BufferedReader(new FileReader(muestra));
			
			String line;
			
			line = bfMuestra.readLine();
			String[] l = line.split(",");
			
			Muestra m = new Muestra(Double.parseDouble(l[0]), Double.parseDouble(l[1]), Double.parseDouble(l[2]), Double.parseDouble(l[3]));
			
			double distSetosa = distancia(m, centroSetosa);
			double distVersicolor = distancia(m, centroVersicolor);
			
			if(distSetosa < distVersicolor)
				System.out.println("La muestra pertenece a la clase Iris-setosa");
			else
				System.out.println("La muestra pertenece a la clase Iris-versicolor");
			
			bfMuestra.close();
			scanner.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("No se han podido encontrar los ficheros indicados");
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero");
		}
	}


	private static boolean criterioFinalizacion() {
		double cambioSetosa = Math.sqrt(distancia(centroSetosaAnterior, centroSetosa));
		double cambioVersicolor = Math.sqrt(distancia(centroVersicolorAnterior, centroVersicolor));
		
		return cambioSetosa <= razonAprendizaje && cambioVersicolor <= razonAprendizaje;
	}


	private static void calculoNuevosCentros() {
		double b = 0.0;
		double c1 = 0.0, c2 = 0.0, c3 = 0.0, c4 = 0.0;
		
		centroSetosaAnterior = new Muestra(centroSetosa.getCoordenada1(), centroSetosa.getCoordenada2(), centroSetosa.getCoordenada3(), centroSetosa.getCoordenada4());
		centroVersicolorAnterior = new Muestra(centroVersicolor.getCoordenada1(), centroVersicolor.getCoordenada2(), centroVersicolor.getCoordenada3(), centroVersicolor.getCoordenada4());
		
		for(int i = 0; i < clases.get("Iris-setosa").size(); i++){
			Muestra m = clases.get("Iris-setosa").get(i);
			
			double cuadrado = Math.pow(gradosPertenenciaSetosa.get(i), 2);
			b += cuadrado;
			c1 += m.getCoordenada1() * cuadrado;
			c2 += m.getCoordenada2() * cuadrado;
			c3 += m.getCoordenada3() * cuadrado;
			c4 += m.getCoordenada4() * cuadrado;
		}
		
		centroSetosa.setCoordenada1(c1/b);
		centroSetosa.setCoordenada2(c2/b);
		centroSetosa.setCoordenada3(c3/b);
		centroSetosa.setCoordenada4(c4/b);
		
		c1 = c2 = c3 = c4 = b = 0.0;
		
		for(int j = 0; j < clases.get("Iris-versicolor").size(); j++){
			Muestra m = clases.get("Iris-versicolor").get(j);
			
			double cuadrado = Math.pow(gradosPertenenciaVersicolor.get(j), 2);
			b += cuadrado;
			c1 += m.getCoordenada1() * cuadrado;
			c2 += m.getCoordenada2() * cuadrado;
			c3 += m.getCoordenada3() * cuadrado;
			c4 += m.getCoordenada4() * cuadrado;
		}
		
		centroSetosa.setCoordenada1(c1/b);
		centroSetosa.setCoordenada2(c2/b);
		centroSetosa.setCoordenada3(c3/b);
		centroSetosa.setCoordenada4(c4/b);
		
	}


	private static void calculoGradosPertenencia() {
		
		for(int i = 0; i < clases.get("Iris-setosa").size(); ++i){
			Muestra m = clases.get("Iris-setosa").get(i);
			gradosPertenenciaSetosa.add(((1/distanciasSetosa.get(i))/((1/distanciasSetosa.get(i)) + (1/distanciasVersicolor.get(i)))));
		}

		for(int j = 0; j < clases.get("Iris-versicolor").size(); ++j){
			Muestra m = clases.get("Iris-versicolor").get(j);
			gradosPertenenciaVersicolor.add(((1/distanciasVersicolor.get(j + 50))/((1/distanciasSetosa.get(j + 50)) + (1/distanciasVersicolor.get(j + 50)))));
		}
		
	}


	private static void calculoDistanciasACentros() {
		
		for(Muestra m : clases.get("Iris-setosa")){
			distanciasSetosa.add(distancia(centroSetosa, m));
			distanciasVersicolor.add(distancia(centroVersicolor, m));
		}
		
		for(Muestra m : clases.get("Iris-versicolor")){
			distanciasSetosa.add(distancia(centroSetosa, m));
			distanciasVersicolor.add(distancia(centroVersicolor, m));
		}
	}


	/**
	 * 	Lee el fichero introducido por el usuario y clasifica las muestras en las clases
	 * 	indicadas.
	 * 
	 * @param c
	 */
	static void inicializarClases(){
		clases = new HashMap<String, ArrayList<Muestra>>();
		Scanner scanner = new Scanner(System.in);
		String archivoMuestras;
		
		System.out.println("Indique el nombre del archivo de muestras para el entrenamiento (.txt): ");
		archivoMuestras = scanner.nextLine();
		
		try {
			BufferedReader bfMuestras = new BufferedReader(new FileReader(archivoMuestras));
			
			String line;
			
			while((line = bfMuestras.readLine()) != null){
				String[] valores = line.split(",");
				
				if(!clases.containsKey(valores[valores.length - 1]))
					clases.put(valores[valores.length - 1], new ArrayList<Muestra>());

				Muestra m = new Muestra(Double.parseDouble(valores[0]), Double.parseDouble(valores[1]), Double.parseDouble(valores[2]), Double.parseDouble(valores[3]));
				
				clases.get(valores[valores.length - 1]).add(m);
			}
			
			//inicializarCentros();
			
			bfMuestras.close();
			//scanner.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("No se han podido encontrar los ficheros indicados");
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero");
		}
	}
	
	
	/**
	 * 	Inicializa los centros de las dos clases. Para ello, suma cada componente de todas las muestras de una clase
	 * 	y divide entre el número de muestras. En otras palabras, hace una media aritmética de todas las muestras para
	 * 	sacar el valor de los centros iniciales.
	 * 
	 * @param c Clase que contiene las muestras de las clases clasificadas
	 */
	private static void inicializarCentros() {
		double suma1 = 0, suma2 = 0, suma3 = 0, suma4 = 0;
		
		for(String clase : clases.keySet()){
			for(int i = 0; i < clases.get(clase).size(); ++i){
				suma1 = suma1 + clases.get(clase).get(i).getCoordenada1();
				suma2 = suma2 + clases.get(clase).get(i).getCoordenada2();
				suma3 = suma3 + clases.get(clase).get(i).getCoordenada3();
				suma4 = suma4 + clases.get(clase).get(i).getCoordenada4();
			}
			Muestra m = new Muestra((suma1/clases.get(clase).size()),(suma2/clases.get(clase).size()),(suma3/clases.get(clase).size()),(suma4/clases.get(clase).size()));
			
			if(clase.equals("Iris-setosa"))
				centroSetosa = m;
			else
				centroVersicolor = m;
			
			suma1 = suma2 = suma3 = suma4 = 0;
		}

	}
	
	
	/**
	 * 	Actualiza los centros a partir de las muestras pertenecientes a cada una de las clases.
	 * 
	 * 	En cada iteración se procesan todas las muestras y se comprueba la desviación de los centros
	 * 	con respecto a la razón de aprendizaje. Si la desviación de ambos centros es menor o igual
	 * 	a esta razón de aprendizaje, finaliza el entrenamiento. De no ser así, se continúa hasta un
	 * 	máximo de 10 iteraciones.
	 * 
	 * @param centro1	Centro representante de la clase 1
	 * @param centro2	Centro representate de la clase 2
	 */
	public static void entrenamientoLloyd(){
		Muestra centroSetosaAnterior, centroVersicolorAnterior;
		boolean fin = false;
		
		for(int i = 0; i < kmax && !fin; ++i){
			centroSetosaAnterior = centroSetosa; centroVersicolorAnterior = centroVersicolor;
			
			for(String clase : clases.keySet()){
				for(int j = 0; j < clases.get(clase).size(); ++j){
					Muestra m = clases.get(clase).get(j);
					
					double distSetosa = distancia(m, centroSetosa);
					double distVersicolor = distancia(m, centroVersicolor);
					
					if(distSetosa < distVersicolor)
						actualizarCentro(centroSetosa, m);
					else
						actualizarCentro(centroVersicolor, m);
				}
			}
						
			double e1 = distancia(centroSetosa, centroSetosaAnterior);
			double e2 = distancia(centroVersicolor, centroVersicolorAnterior);
			
			fin = (e1 <= razonAprendizaje && e2 <= razonAprendizaje);
		}
		
	}

	
	/**
	 * 	Actualiza el centro más cercano a la muestra m según la siguiente ecuación:
	 * 
	 * 	nuevoCentro = centroAnterior + razonAprendizaje*[muestra - centroAnterior]
	 * 
	 * @param centro
	 * @param m
	 */
	private static void actualizarCentro(Muestra centro, Muestra m) {
		Muestra resta = new Muestra(m.getCoordenada1() - centro.getCoordenada1(), m.getCoordenada2() - centro.getCoordenada2(), m.getCoordenada3() - centro.getCoordenada3(), m.getCoordenada4() - centro.getCoordenada4());
		
		resta.setCoordenada1(resta.getCoordenada1() * razonAprendizaje);
		resta.setCoordenada2(resta.getCoordenada2() * razonAprendizaje);
		resta.setCoordenada3(resta.getCoordenada3() * razonAprendizaje);
		resta.setCoordenada4(resta.getCoordenada4() * razonAprendizaje);

		centro.setCoordenada1(centro.getCoordenada1() + resta.getCoordenada1());
		centro.setCoordenada2(centro.getCoordenada2() + resta.getCoordenada2());
		centro.setCoordenada3(centro.getCoordenada3() + resta.getCoordenada3());
		centro.setCoordenada4(centro.getCoordenada4() + resta.getCoordenada4());
	}
	

	/**
	 * 	Calcula la distancia euclídea de la muestra m2 a la muestra m1.
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	private static double distancia(Muestra m1, Muestra m2) {
		double d = Math.pow((m1.getCoordenada1() - m2.getCoordenada1()), 2) + Math.pow((m1.getCoordenada2() - m2.getCoordenada2()), 2) + Math.pow((m1.getCoordenada3() - m2.getCoordenada3()), 2) + Math.pow((m1.getCoordenada4() - m2.getCoordenada4()), 2); 
		return d;
	}
	
	/**
	 * 	Pide al usuario que introduzca un fichero que contenga información sobre la muestra a clasificar y, a continuación,
	 * 	lleva a cabo la clasificación indicando a qué clase pertenece.
	 */
	private static void clasificacionLloyd() {
		Scanner scanner = new Scanner(System.in);
		String muestra;
		
		System.out.println("Indique el nombre del archivo que contiene la muesta a clasificar (.txt): ");
		muestra = scanner.nextLine();
		
		try {
			BufferedReader bfMuestra = new BufferedReader(new FileReader(muestra));
			
			String line;
			
			line = bfMuestra.readLine();
			String[] l = line.split(",");
			
			Muestra m = new Muestra(Double.parseDouble(l[0]), Double.parseDouble(l[1]), Double.parseDouble(l[2]), Double.parseDouble(l[3]));
			
			double distSetosa = distancia(m, centroSetosa);
			double distVersicolor = distancia(m, centroVersicolor);
			
			if(distSetosa < distVersicolor)
				System.out.println("La muestra pertenece a la clase Iris-setosa");
			else
				System.out.println("La muestra pertenece a la clase Iris-versicolor");
			
			bfMuestra.close();
			scanner.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("No se han podido encontrar los ficheros indicados");
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero");
		}
		
	}

}
