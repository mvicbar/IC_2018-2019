package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Lloyd {

	private static double razonAprendizaje = 0.1;
	private static int kmax = 10;
	private static Muestra centroVersicolor = new Muestra(6.8, 3.4, 4.6, 0.7);
	private static Muestra centroSetosa = new Muestra(4.6, 3.0, 4.0, 0.0);

	/**
	 * 	Actualiza los centros a partir de las muestras pertenecientes a cada una de las clases.
	 * 
	 * 	En cada iteraci�n se procesan todas las muestras y se comprueba la desviaci�n de los centros
	 * 	con respecto a la raz�n de aprendizaje. Si la desviaci�n de ambos centros es menor o igual
	 * 	a esta raz�n de aprendizaje, finaliza el entrenamiento. De no ser as�, se contin�a hasta un
	 * 	m�ximo de 10 iteraciones.
	 * 
	 * @param centro1	Centro representante de la clase 1
	 * @param centro2	Centro representate de la clase 2
	 */
	public static void entrenamientoLloyd(HashMap<String, ArrayList<Muestra>> clases){
		Muestra centroSetosaAnterior, centroVersicolorAnterior;
		boolean fin = false;
		
		for(int i = 0; i < kmax && !fin; ++i){
			centroSetosaAnterior = centroSetosa; centroVersicolorAnterior = centroVersicolor;
			
			for(String clase : clases.keySet()){
				for(int j = 0; j < clases.get(clase).size(); ++j){
					Muestra m = clases.get(clase).get(j);
					
					double distSetosa = Main.distancia(m, centroSetosa);
					double distVersicolor = Main.distancia(m, centroVersicolor);
					
					if(distSetosa < distVersicolor)
						actualizarCentro(centroSetosa, m);
					else
						actualizarCentro(centroVersicolor, m);
				}
			}
						
			double e1 = Main.distancia(centroSetosa, centroSetosaAnterior);
			double e2 = Main.distancia(centroVersicolor, centroVersicolorAnterior);
			
			fin = (e1 <= razonAprendizaje && e2 <= razonAprendizaje);
		}
		
	}
	
	/**
	 * 	Actualiza el centro m�s cercano a la muestra m seg�n la siguiente ecuaci�n:
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
	 * 	Pide al usuario que introduzca un fichero que contenga informaci�n sobre la muestra a clasificar y, a continuaci�n,
	 * 	lleva a cabo la clasificaci�n indicando a qu� clase pertenece.
	 */
	public static void clasificacionLloyd() {
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
			
			double distSetosa = Main.distancia(m, centroSetosa);
			double distVersicolor = Main.distancia(m, centroVersicolor);
			
			if(distSetosa < distVersicolor)
				System.out.println("La muestra pertenece a la clase Iris-setosa");
			else
				System.out.println("La muestra pertenece a la clase Iris-versicolor");
			
			bfMuestra.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("No se han podido encontrar los ficheros indicados");
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero");
		}
		
	}
	
}
