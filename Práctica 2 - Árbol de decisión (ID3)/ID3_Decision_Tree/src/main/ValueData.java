package main;

import java.util.ArrayList;

/**
 * Representa los datos del valor de un atributo relevantes para el c�lculo del m�rito
 * de dicho atributo (ejemplos totales en los que aparece, n�mero de ejemplos positivos,
 * y n�mero de ejemplos negativos).
 * @author victo
 *
 */
public class ValueData {

	private int total;
	private int positive;
	private int negative;

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPositive() {
		return positive;
	}
	public void setPositive(int positive) {
		this.positive = positive;
	}
	public int getNegative() {
		return negative;
	}
	public void setNegative(int negative) {
		this.negative = negative;
	}
	
	
	
}
