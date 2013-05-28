/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucla.stat.SOCR.analyses.util;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class pca_analysis {
	/** 
	 * 
	 * @author Marios Michaelidis
	 * Licence Apache 2.0
	 * written in 2012
	 *
	 */
	private double [] Eigenvalues;// the array with the Eigen Values
	double EigenVecor [][] ;// the matrix with the Eigen Vectors
	double Perchentages [] ;//The proportions of Variances for each component
	
	public void componize(double [][] covariate){
		
		// replace the initial data set the same values minus the means from each variable-column
		
	for (int j=0; j < covariate[0].length; j++ ) {
		
		DescriptiveStatistics stats = new DescriptiveStatistics();	
				
   	 for  (int k=0 ; k < covariate.length ; k++){
   	 stats.addValue(covariate[k][j]);}
		
		double mean=stats.getMean();

		for  (int k=0 ; k < covariate.length ; k++){
		   covariate[k][j]= covariate[k][j]-mean;}
	
	// here ends the iteration that replaces all values with the value minus the mean of the respective column		
	}	
	
	// We get the Covariance matrix for the "adjusted by mean" data set
	RealMatrix covariance_matrix=  new PearsonsCorrelation(covariate).getCorrelationMatrix();		
	
	// we get the Eigen values and the Eigen Vectors Via Eigen Value Decomposition.
	
	EigenDecomposition Eig = new EigenDecomposition(covariance_matrix, 1);	
	// get the Eigen Values	
	double Eigenvaluess [] =Eig.getRealEigenvalues();	
	// Get the Eigen vectors	
	RealMatrix Eigenvec = Eig.getV();	
	double [][] EigenVecors=Eigenvec.getData();    
	//Sort everything to get the Vectors with the highest significance	
	SortTableDouble sort = new SortTableDouble();   
	 sort.sorting (Eigenvaluess,EigenVecors );    
	 EigenVecor =sort.value_matrix();	
	 Eigenvalues=sort.scanned_values();
	 Perchentages= new double[Eigenvalues.length];
 	 for  (int k=0 ; k < Eigenvalues.length ; k++){
 		Perchentages[k]= Eigenvalues[k]/Eigenvalues.length	;} 
	}

	public double [] getsorted_Eigenvalues() {
		
		return Eigenvalues;
		
	}
	
   public double [][] getsorted_EigenVecor() {
		
		return EigenVecor;

	}
   
   public double [] getsorted_Proportions() {
		
		return Perchentages;
	
	}
}