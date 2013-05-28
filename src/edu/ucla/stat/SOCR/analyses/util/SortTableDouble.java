/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucla.stat.SOCR.analyses.util;
/** 
 * 
 * @author Marios Michaelidis
 * Licence Apache 2.0
 * written in 2012
 *
 */
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/* A simple class (not very efficient)
 * for sorting a double array [][] based 
 * on another single one.
 */

public class SortTableDouble {
	private double vars2[];
	private double [][] all2;

	
	public void sorting(double [] Vars1, double all [][]) {
		
	      vars2= new double [Vars1.length];
	        all2= new double [Vars1.length] [all.length] ;

	        for (int i=0; i< Vars1.length; i++) {
	     	  
	     		   DescriptiveStatistics stats= new DescriptiveStatistics();
	 				 for (int k=0; k< Vars1.length; k++) {
	 					stats.addValue(Vars1[k]);}
	 				double max= stats.getMax();
	 				 for (int k=0; k< Vars1.length; k++) {
	 						if (Vars1[k]==max) {
	 							vars2[i]=Vars1[k];
	 							for (int f=0; f< all.length; f++) {
	 							all2[i][f]=  all[k][f];}
	 							
	 							Vars1[k]=-999999999;
	 							break;
	 						} }}}
	

	
	public double [] scanned_values() {
		return vars2;
	}
	
	public double [] [] value_matrix () {
		return all2;
	}
	
	
}
