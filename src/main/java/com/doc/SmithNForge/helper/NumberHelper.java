package com.doc.SmithNForge.helper;

import java.util.Random;

public class NumberHelper {

	static Random rnd = new Random();
	
	public static float randomNumber(double min, double max) {
		
		return (float) (min + (max - min) * rnd.nextDouble());
	}
	
	public static float rounded(double toRound) {
		
		return (float) (Math.round(toRound * 100.0) / 100.0);
	}
}
