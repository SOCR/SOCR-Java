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

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;



public class Logistic_Regression {

	/* This is a logistic regression approach via the Newton-Rapson Method.
	 * You need to insert the predictor matrix, namely matrix [Rows] [Columns]
	 * Your Target Variable need to be either '1' or '0'. The algorithm will check for that in the beginning 
	 * The Constant is just a string that stores "yes" or "no" in case you require a constant or not
	 * Here we will make some declarations*/
	
	private String Con; // the String that holds the information on whether to add a constant or not
	private double Target_values[]; // this will hold the the target values of 1 or 0
	private double beta[]; // these are meant to be the regression's coefficients
	private double normal [][];  // this will be the main matrix
	private double transposed [][]; // this will be the main matrix but transposed
	private double probabilities []; // this will hold the probabilities, e.g 1/(1+exp(-(x0+ ax1+bx2....zxn)))
	private double residuals [] ; // this will hold the residuals, e.g Target[i]-probabilities[i] or (yi-pi)
//	private double diagonal [][] ; // this is the Diagonal square matrix always with the same length of rows and columns and equal with [Target.length] [Target.length]. rejected as an option since it was holding too much RAM
	private double instead_of_diagonal [][] ; // as explained right above, with this double we will try to overcome teh diagonal by doing the calculations manually, thus saving memory and speed.
	/* next we have the matrices the will be used in Newtons-Rapsons Algorithm */
	private double maximumLikelihood=0; // this will store the Deviance or -2LogLikelihood
	private double standard_error []; // this will hold the standard errors of the coefficients
	private double wald_statistics []; // this will hold the wald statistics
	private double p_values []; // this will hold the probability values of the Wald statistics
	
	
	private RealMatrix transposed_matrix ;
	private	RealMatrix diagonal_matrix;
	private RealMatrix residuals_matrix;
	private RealMatrix second ;
	private RealMatrix vriance_covariance  ; // This will hold the asymptotic covariance matrix
	private RealMatrix Fourth ;
	private RealMatrix Five ;
	private RealMatrix beta_matrix ;
	private RealMatrix value_matrix ;
	private RealMatrix p ;
	
	public void regression(double matrix [][],  double Target[], String Constant, double tolerance, int maxim_Iteration) {
		
		//replace the maximum iterations thresold in case of error
		
		if (maxim_Iteration<1) {
			maxim_Iteration=1;
		}
		//instancentiate the target value
		
		Target_values= new double [Target.length];
		
		Con=Constant;
		
		// check if the length is the same in the covariates and the target
		
          if (matrix.length!=Target.length ) {
			
			try {
				throw new MyException("Your arrays need to have the same length.");
			} catch (MyException e) {
				e.printStackTrace();
				return;
			}
			
		}
		// check if tolerance is higher than 0 and correct it if not
		
		if (tolerance<=0) {
			tolerance = 0.0001;
		}
		// Initially we check if the values of the Target are 1 or 0 as they normally should be. It will throw exception if it doesn't. We also store it a new array.
		
		for (int i=0 ; i< Target.length; i++){
			
			if (Target[i]!=0.0 && Target[i]!=1.0){
				
				try {
					throw new MyException("Your Target Variable need to be bianry and have only the values of '0' and '1'");
				} catch (MyException e) {
					e.printStackTrace();
					return;
				}
			}
			
			Target_values[i]=Target[i];
		}
		
		// check if the constant is included and create the matrix of the normal predictors in either case
		
		if (Constant.equals("no") || Constant.equals("NO") || Constant.equals("n") || Constant.equals("N") ){
			
			// in that case the matrix will be equal with the given matrix
			
			normal = new double [matrix.length][matrix[0].length];
			
			// populate the matrix
			
			for (int i=0; i < matrix.length; i++) {
				for (int j=0; j < matrix[0].length; j++) {
					
					normal[i][j]= matrix[i][j];
					
					
				}
				
			}
			
		} else {
			// if the constant is included
			
			normal = new double [matrix.length][matrix[0].length + 1];
			
			// add the constant column first. Basically add a column where all the values are '1'
			
			for (int i=0; i < matrix.length; i++) {
				
				normal[i][0]=1.0;
				
			}
			
			for (int i=0; i < matrix.length; i++) {
				for (int j=0; j < matrix[0].length; j++) {
					
					normal[i][j+1]= matrix[i][j];
					
					
				}
				
			}
			
		}
		
	
		// Get the transposed Matrix that is needed later for the Newton-Rapson Computations
		
		
		transposed= new double [normal[0].length][normal.length];
		
		
		// populate the matrix
		
		for (int i=0; i < normal.length; i++) {
			for (int j=0; j < normal[0].length; j++) {
				
				transposed[j][i]= normal[i][j];
				
				
			}
			
		}
		
		
		/* We are ready to start the Newton-Rapson Method
		 * We will iterate as many times as needed so as the difference of the coeficiennts to be minimal
		 * the formula in terms of matrices is the following 
		 * new_Beta [] = Old_Beta + inverse matrix of ( transposed[][]* diagonal[][] * normal[][])* transposed[][]* residuals[]  */
		
		
		// We will initiate and populate the beta
		
		
		beta = new double [normal[0].length];
		
		for (int i=0; i < beta.length; i++ ) {
			beta[i]=0;
	
		}
		
		//initiate the probabilities , the residuals and the diagonal
		
		probabilities= new double [normal.length];
		residuals= new double[ normal.length];
		instead_of_diagonal = new double [normal[0].length][normal.length];
		
		/* diagonal= new double [normal.length][normal.length]; // omitted cause it was reserving too much space
		
		put the value 0 to all the cases of the diagonal
		
		for (int i=0; i<normal.length; i++) {
			for (int j=0; j<normal.length; j++) {
				diagonal[i][j]=0;
		}
		}
		
		*/
		
		// initiate one variable to hold the teration's tolerance
		
		double iteration_tol=1;
		
		// initiate the iteration count
		
		int it=0;
		
		// Begin the big Iteration and the Optimization Algorithm
		
		while (iteration_tol> tolerance) {
			
			
			// put a check in the beginning that will stop the process if solution is not identified. This problem is known as 'failed to converge'
			
            if (it>=maxim_Iteration) {
	
					break;
							
			}
		// calculate the Probabilities	
			
			
	    // matrix to hold the betas
		beta_matrix = new Array2DRowRealMatrix(beta);
			// One more with three rows, two columns
			
		 value_matrix = new Array2DRowRealMatrix(normal);
	
			
			 p = value_matrix.multiply(beta_matrix);

			// get the linear predicted values
			
			final double [] linear_predictedvalues= p.getColumn(0);	
			
			// transform he linear values to probabilities through the logit transformation 1/1(1+exp(x)) and the residuals and the diagonal all in one row
			// the following function does exactly that, namely :
		    //	public class Expected_value_expotential_function {
		    // public double getvalue(double x){
			// double y= 1/(1+ Math.exp(-x));
		    //	return y;	
			//	}	
			// }
			
			Expected_value_expotential_function ex = new Expected_value_expotential_function();
			
			for (int i=0; i < probabilities.length;i++) {
				// the probabilities
				probabilities[i]= ex.getvalue(linear_predictedvalues[i]);
		
				
				// The residuals
				
				residuals[i]= Target[i]-probabilities[i];
				
				
				// populate all the fields that will be used instead of the diagonal
				
				for (int j=0; j< normal[0].length; j++){
				instead_of_diagonal [j][i]= 	transposed[j][i]* probabilities[i];
				}
				
			}
			
			
			// Start multiplying the matrices as per the N-R formula
			
			 transposed_matrix = new Array2DRowRealMatrix(transposed);
			 diagonal_matrix = new Array2DRowRealMatrix(instead_of_diagonal);
			 residuals_matrix = new Array2DRowRealMatrix(residuals);
			// first = transposed_matrix.multiply(diagonal_matrix); // this was regarding the diagonal that was removed
			// System.out.println("first is done");
			 second = diagonal_matrix.multiply(value_matrix);
			 org.apache.commons.math3.linear.LUDecomposition pam= new org.apache.commons.math3.linear.LUDecomposition(second);
			 vriance_covariance=pam.getSolver().getInverse();
			 Fourth = vriance_covariance.multiply(transposed_matrix);
			 Five = Fourth.multiply(residuals_matrix);
	
			 
			double betas []= Five.getColumn(0);	
			
			DescriptiveStatistics stats= new DescriptiveStatistics();
			
			
			// sum the new betas
			
			for (int i=0; i < betas.length; i++) {
				
				
				stats.addValue(Math.abs(betas[i]));
			}
			
			 iteration_tol=stats.getMax();
			 
			// System.out.println(iteration_tol);
			 
			
			// the new betas are the previous betas , plus the new ones
			
                for (int i=0; i < beta.length; i++) {
				
				beta[i]=beta[i]+betas[i];
			//	System.out.println("difference is: " + betas[i] + " and the beta is : " + beta[i]);
			}
			
			
               	
			it=it+1;
			System.out.println("Iteration :" + it);
	//------------------ Here ends Newtons-Rapson Algorithm and the Big While-----------------
		}
		
		// calculate all the statistics with the new beta
		
		
		
		beta_matrix = new Array2DRowRealMatrix(beta);
		// One more with three rows, two columns
		
	 value_matrix = new Array2DRowRealMatrix(normal);

		
		 p = value_matrix.multiply(beta_matrix);

		// get the linear predicted values
		
		final double [] linear_predictedvalues= p.getColumn(0);	
		
		// transform he linear values to probabilities through the logit transformation 1/1(1+exp(x)) and the residuals and the diagonal all in one row
		// the following function does exactly that, namely :
	    //	public class Expected_value_expotential_function {
	    // public double getvalue(double x){
		// double y= 1/(1+ Math.exp(-x));
	    //	return y;	
		//	}	
		// }
		
		Expected_value_expotential_function ex = new Expected_value_expotential_function();
		
		for (int i=0; i < probabilities.length;i++) {
			// the probabilities
			probabilities[i]= ex.getvalue(linear_predictedvalues[i]);
	
			
			// the residuals
			residuals[i]= Target[i]-probabilities[i];
			
			
			// populate all the fields that will be used instead of the diagonal
			
			for (int j=0; j< normal[0].length; j++){
			instead_of_diagonal [j][i]= 	transposed[j][i]* probabilities[i]*(1-probabilities[i]);
			}
			
		}
		
		
		// Start multiplying the matrices as per the N-R formula
		
		 transposed_matrix = new Array2DRowRealMatrix(transposed);
		 diagonal_matrix = new Array2DRowRealMatrix(instead_of_diagonal);
		 residuals_matrix = new Array2DRowRealMatrix(residuals);
		// first = transposed_matrix.multiply(diagonal_matrix); // this was regarding the diagonal that was removed
		// System.out.println("first is done");
		 second = diagonal_matrix.multiply(value_matrix);
		 org.apache.commons.math3.linear.LUDecomposition pam= new org.apache.commons.math3.linear.LUDecomposition(second);
		 vriance_covariance=pam.getSolver().getInverse();
		
		// now start calculating all the other stats
		

		
		// Here we calculate the deviance or the -2LogLikelihood
		double store_likelihoods []= new double [normal.length];
		 maximumLikelihood=0;
		
		/* the formula for the loglikelihood in terms of the variables
		 * that are already in use in this algorithm is :
		 * the sum of all rows of probabilities[i]^Target_values[i] * (1-probabilities[i])^(1-Target_values[i]) */
		
		for (int i=0; i <store_likelihoods.length;i++){
			
			store_likelihoods[i]=Target_values[i]*Math.log(probabilities[i]) + ((1-Target_values[i])*Math.log(1-probabilities[i]))  ;	
			
			maximumLikelihood= maximumLikelihood +store_likelihoods[i];
		}
		maximumLikelihood= -2*maximumLikelihood;
		
		
		
		// now we will calculate the wald statistics
		
		standard_error = new double [beta.length];
		
		// we get the standard errors from the variance-covariance matrix
		
		for (int i=0; i < standard_error.length; i ++) {
			standard_error[i]= Math.sqrt(vriance_covariance.getEntry(i, i));
			//System.out.println(standard_error[i] + " and " + Math.pow(standard_error[i], 2));
		
		}
		
		// now we calculate the Wald statistics which are (beta[i]/standard_error[i])^2
		 ChiSquaredDistribution chi= new ChiSquaredDistribution(1);
		 wald_statistics= new double [beta.length];
		 p_values= new double [beta.length];
		 
		for (int i=0; i < beta.length; i++){
			
			wald_statistics[i]= Math.pow((beta[i]/standard_error[i]), 2);
			
			p_values[i]= 1-chi.cumulativeProbability(wald_statistics[i]);
			
		}
		
		// Here ends the main Logistic method
	}
	
	// return the probabilities
	public double [] getprobabilities(){
		return probabilities; 
		
	}
	
   // return the residuals
	public double [] getresiduals(){
		return residuals; 
		
	}
	
	public double [] getbetas(){
		return beta; 
		
	}
	
	public double [] getWald(){
		return wald_statistics; 
		
	}
	
	public double [] getWald_P_Values(){
		return p_values; 
		
	}
	
	// return the odds or the probability an event to happen versus not to happen
	public double [] get_odds(){
		
		double [] odds= new double [beta.length];
		if (Con.equals("no") || Con.equals("NO") || Con.equals("n") || Con.equals("N") ){
			
			//calculate the odds if there is no constant
			for (int i=0;i<beta.length;i++){
				
				odds[i]= (1/(1+ Math.exp(-beta[i])))/(1-(1/(1+ Math.exp(-beta[i]))));
			}
			
		} else {
			
			//calculate the odds if there is a constant
			 odds [0]= (1/(1+Math.exp(-beta[0])))/(1-(1/(1+Math.exp(-beta[0]))));
			 
				for (int i=1;i<beta.length;i++){
					
					odds[i]= (1/(1+ Math.exp(-beta[0]-beta[i])))/(1-(1/(1+ Math.exp(-beta[0]-beta[i]))));
				}
				
			}
		return odds;
	}
	
	// return the minus 2 log-likelihood statistic that shows how much information the model explained
	public double  getMAXIMUMlikelihood(){
		
		
		return maximumLikelihood;
	}
	
	// this is another measure of fit (Akaikes Information Criterion)  that is equal with deviance * 2 * (number of parameters)
	
	public double  getAIC(){
	

		return maximumLikelihood + 2 * normal[0].length;
	}
	
	// this is another measure of fit also called  Bayesian Information Criterion or Schwartz criterion, that is equal with deviance +  (number of parameters) * ln(sample size)
	
	public double  getBIC(){
		

		return maximumLikelihood +  normal[0].length * Math.log(normal.length);
	}
	
	
	
	// Here ends the class
}


