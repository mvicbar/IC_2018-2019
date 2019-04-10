package main;

import java.util.Comparator;

public class Pair implements Comparable {
	public String first;
	public Double second;
	
	public Pair(String f, Double s){
		this.first = f;
		this.second = s;
	}

	public static Comparator<Pair> pairComparator = new Comparator<Pair>(){

		@Override
		public int compare(Pair p1, Pair p2) {
			return p1.second < p2.second ? 0 : -1;
		}
		
	};

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public Double getSecond() {
		return second;
	}

	public void setSecond(Double second) {
		this.second = second;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
