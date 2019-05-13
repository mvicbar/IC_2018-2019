package main;

/**
 * This class represents a sample with four coordinates.
 * 
 * @author mvicbar
 *
 */
public class Muestra {

	private double s1;
	private double s2;
	private double s3;
	private double s4;
	
	public Muestra(double s1, double s2, double s3, double s4){
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
	}
	
	double getCoordenada1(){
		return this.s1;
	}
	
	double getCoordenada2(){
		return this.s2;
	}

	double getCoordenada3(){
		return this.s3;
	}

	double getCoordenada4(){
		return this.s4;
	}

	public void setCoordenada1(double s1) {
		this.s1 = s1;
	}

	public void setCoordenada2(double s2) {
		this.s2 = s2;
	}

	public void setCoordenada3(double s3) {
		this.s3 = s3;
	}

	public void setCoordenada4(double s4) {
		this.s4 = s4;
	}

	
}
