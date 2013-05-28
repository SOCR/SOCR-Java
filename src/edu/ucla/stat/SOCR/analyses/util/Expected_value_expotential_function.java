package edu.ucla.stat.SOCR.analyses.util;

/** 
 * 
 * @author Marios Michaelidis
 * Licence Apache 2.0
 * written in 2012
 *
 */

/* Expotential function to be used in Logistic Regression
 * and the logit transformation
 */

public class Expected_value_expotential_function {

	public double getvalue(double x){
		
		double y= 1/(1+ Math.exp(-x));
		
		return y;
	}
	
}
