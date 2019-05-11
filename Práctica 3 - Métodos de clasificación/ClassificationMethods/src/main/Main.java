package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Muestra centro1 = new Muestra(4.6, 3.0, 4.0, 0.0);
		Muestra centro2 = new Muestra(6.8, 3.4, 4.6, 0.7);
		Clases clases = new Clases();
		
		leerFicheros(clases);
		
		entrenamientoLloyd(clases, centro1, centro2);

	}
	
	
	/**
	 * 	Lee el fichero introducido por el usuario y clasifica las muestras en las clases
	 * 	indicadas.
	 * 
	 * @param clases
	 */
	static void leerFicheros(Clases clases){
		Scanner scanner = new Scanner(System.in);
		String archivoMuestras;
		
		System.out.println("Indique el nombre del archivo de muestras para el entrenamiento (.txt): ");
		archivoMuestras = scanner.nextLine();
		
		try {
			BufferedReader bfMuestras = new BufferedReader(new FileReader(archivoMuestras));
			
			String line;
			
			while((line = bfMuestras.readLine()) != null){
				String[] l = line.split(",");
			
				Muestra m = new Muestra(Double.parseDouble(l[0]), Double.parseDouble(l[1]), Double.parseDouble(l[2]), Double.parseDouble(l[3]));
				
				if(l[4].equals("Iris-setosa"))
					clases.getMuestrasSetosa().add(m);
				else if(l[4].equals("Iris-versicolor"))
					clases.getMuestrasVersicolor().add(m);
			
			}
			
			bfMuestras.close();
			scanner.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("No se han podido encontrar los ficheros indicados");
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero");
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
	public static void entrenamientoLloyd(Clases clases, Muestra centro1, Muestra centro2){
		final double razonAprendizaje = 0.1;
		final int kmax = 10;
		Muestra centro1Anterior, centro2Anterior;
		boolean fin = false;
		
		for(int i = 0; i < kmax && !fin; ++i){
			centro1Anterior = centro1;
			centro2Anterior  = centro2;
			
			for(int j = 0; j < clases.getMuestrasSetosa().size(); ++j){
				Muestra m = clases.getMuestrasSetosa().get(j);
				double d1 = distancia(m, centro1);
				double d2 = distancia(m, centro2);
				
				if(d1 < d2)
					actualizarCentro(centro1, m);
				else
					actualizarCentro(centro2, m);
			}
			
			for(int j = 0; j < clases.getMuestrasVersicolor().size(); ++j){
				Muestra m = clases.getMuestrasVersicolor().get(j);
				double d1 = distancia(m, centro1);
				double d2 = distancia(m, centro2);
				
				if(d1 < d2)
					actualizarCentro(centro1, m);
				else
					actualizarCentro(centro2, m);
			}
			
			double e1 = distancia(centro1, centro1Anterior);
			double e2 = distancia(centro2, centro2Anterior);
			
			fin = (e1 <= razonAprendizaje && e2 <= razonAprendizaje);
		}
		
	}

	
	/**
	 * 	Actualiza el centro más cercano a la muestra m según la siguiente ecuación:
	 * 
	 * 	nuevoCentro = centroAnterior + razonAprendizaje*[muestra - centroAnterior]
	 * 
	 * @param centro1
	 * @param m
	 */
	private static void actualizarCentro(Muestra centro1, Muestra m) {
		// TODO Auto-generated method stub
		
	}
	

	/**
	 * 	Calcula la distancia euclídea de la muestra m2 a la muestra m1.
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	private static double distancia(Muestra m1, Muestra m2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
