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

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class Gini {

	private double gini;
	private double Roc;
	private Object AUC;
	private Object Genie;
	
	public void getgenie(double predictedvalues[], String depsa [] ) {
		
		// Check if the length of the 2 basic arrays is equal
		
		if (predictedvalues.length!=depsa.length ) {
			
			try {
				throw new MyException("Your arrays need to have the same length.");
			} catch (MyException e) {
				e.printStackTrace();
				return;
			}
			
		}
		// create a HashSet to get the distinct values
		
		 Set<String> targetal = new HashSet<String>();

			//populate the sets with the variables arrays
		 
				    for (int i = 0; i < depsa.length; i++)
				    	targetal.add(depsa[i]);
				    
				 // Convert the sets to string arrays
				    
				    final Object[] Targets = targetal.toArray(new String[targetal.size()]);
			       
				    int Targetsize=  Targets.length;
				    if (Targetsize!=2) {
				    	try {
							throw new MyException("You need to have exactly 2 distinct categories");
						} catch (MyException e) {
							e.printStackTrace();
							return;
						}
				    }
				    
				    final String [] Tar= new  String [2];
				    Tar[0]=Targets[0].toString();
				    Tar[1]=Targets[1].toString();
		
				 // Put some formats for better output 
				    DecimalFormat decim = new DecimalFormat("0.000");
				    final DecimalFormat deci = new DecimalFormat("0.00");
				    
		//Calculate counts of basic categories
				    
		
		double firstc=0;
		double secon=0;
		for (int i=0; i< predictedvalues.length; i++) {
       	         if(Tar[0].equals(depsa[i])) { firstc++;} else {secon++;}}
		
		//Calculate total combinations for the Gini coef.
		
		double combination= firstc*secon;
		int fir= (int)firstc;
		int sec= (int)secon;
		
		//Calculate the predictions for first and second category
		
		double [] p_first= new double[fir];
		double [] p_second= new double[sec];
		int o=0;
		int k=0;
		for (int i=0; i< predictedvalues.length; i++) {
  	         if(Tar[0].equals(depsa[i])) {
  	        	p_first[o]=predictedvalues[i];
  	        	o++;
  	         } else{
  	        	 p_second[k]=predictedvalues[i] ;
  	        	 k++;
  	         }
		}
		
		//Calculate the counts of each case of the first category that are higher that the total of those of category 2
		
		double [] countall= new double[p_first.length];
		double [] countall_plus= new double[p_first.length];
		for (int i=0; i< p_first.length; i++) {
			for (int j=0; j< p_second.length; j++) {
				if ( p_first[i]>p_second[j]){
					countall[i]++;}
				if (p_first[i]>=p_second[j]){
					countall_plus[i]++;}
			}
		}
		
		// calculate the sum of the counts of the previous categories
		
	 DescriptiveStatistics stats= new DescriptiveStatistics();
	 DescriptiveStatistics stats_plus= new DescriptiveStatistics();
	 for (int i=0; i< p_first.length; i++) {
		stats.addValue(countall[i]);
		stats_plus.addValue(countall_plus[i]);
	 }
	 
	 double sumall= stats.getSum();
	 double sumallplus= stats_plus.getSum();
	 
	Roc= ((sumall/combination) + (sumallplus/combination))/2;
	  gini= ((2*Roc)-1)*100;
	 AUC= decim.format(Roc);
	 Genie= deci.format(gini) + "%";
		
		
	
		
	}
	// Returns the Gini coef. in double
	public double getGini(){
	return gini;
	
	}
	// Returns the Area under the RoC curve in double
	public double getRoc(){
		return Roc;
		
		}
	// Returns the Area under the RoC curve formated as Object
	public Object getRoc_object(){
		return AUC;
		
		}
	// Returns the Gini coef. formated as a % (Object)
	public Object gegenie_object(){
		return Genie;
		
		}
	
}
