package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	private static HashMap<String, ArrayList<Muestra>> clases;

	public static void main(String[] args) {	
		Scanner sc = new Scanner(System.in);
		
		System.out.println("M�TODOS DE CLASIFICACI�N");
		System.out.println("Cargando las clases del fichero 'Iris2Clases.txt'...");
		inicializarClases();
		System.out.println("Carga terminada.\n");
		int opcion = -1;
		
		while (opcion != 0) {
			 
			System.out.println("Por favor, elija un m�todo de clasificaci�n:");
			System.out.println("\t1. Agrupamiento borroso (k-medias)");
			System.out.println("\t2. Algoritmo de Lloyd");
			System.out.println("\t0. Salir\n");
 
            try {
 
    			System.out.println("Opcion: ");
    			opcion = sc.nextInt();

 
                switch (opcion) {
                    case 1:
                    	while(!KMedias.criterioFinalizacion()){
                    		KMedias.calculoDistanciasACentros(clases);
                    		KMedias.calculoGradosPertenencia(clases);
                    		KMedias.calculoNuevosCentros(clases);
        				}
        				
                    	int n1 = -1;
                    	
                    	while(n1 != 0)
                    		n1 = KMedias.clasificacionKMedias();
                    	
                        break;
                    case 2:
        				Lloyd.entrenamientoLloyd(clases);
        				
        				int n2 = -1;
        				while(n2 != 0)
        					n2 = Lloyd.clasificacionLloyd();
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un n�mero");
                sc.next();
            }
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
		
		try {
			BufferedReader bfMuestras = new BufferedReader(new FileReader("Iris2Clases.txt"));
			
			String line;
			
			while((line = bfMuestras.readLine()) != null){
				String[] valores = line.split(",");
				
				if(!clases.containsKey(valores[valores.length - 1]))
					clases.put(valores[valores.length - 1], new ArrayList<Muestra>());

				Muestra m = new Muestra(Double.parseDouble(valores[0]), Double.parseDouble(valores[1]), Double.parseDouble(valores[2]), Double.parseDouble(valores[3]));
				
				clases.get(valores[valores.length - 1]).add(m);
			}
			
			bfMuestras.close();		
			
		} catch (FileNotFoundException e) {
			System.out.println("No se han podido encontrar los ficheros indicados");
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero");
		}
	}

	/**
	 * 	Calcula la distancia eucl�dea de la muestra m2 a la muestra m1.
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static double distancia(Muestra m1, Muestra m2) {
		double d = Math.pow((m1.getCoordenada1() - m2.getCoordenada1()), 2) + Math.pow((m1.getCoordenada2() - m2.getCoordenada2()), 2) + Math.pow((m1.getCoordenada3() - m2.getCoordenada3()), 2) + Math.pow((m1.getCoordenada4() - m2.getCoordenada4()), 2); 
		return d;
	}

}
