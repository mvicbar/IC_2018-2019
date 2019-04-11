package main;


/**
 * Representa los datos del valor de un atributo relevantes para el c�lculo del m�rito
 * de dicho atributo (ejemplos totales en los que aparece, n�mero de ejemplos positivos,
 * y n�mero de ejemplos negativos).
 * 
 * @author victo
 *
 */
public class ValueData {

	private double total;
	private double positive;
	private double negative;

	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getPositive() {
		return positive;
	}
	public void setPositive(double positive) {
		this.positive = positive;
	}
	public double getNegative() {
		return negative;
	}
	public void setNegative(double negative) {
		this.negative = negative;
	}
	
	
	
}
