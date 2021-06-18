package org.sj.tools.jcastiglione.io;

public class Srz {

	public static String dtos(double d){
		return String.format("%f", d);
	}

	public static String itos(int i){
		return String.format("%d", i);
	}

	public static String btos(boolean b) {
		return b? "true":"false";
	}

}
