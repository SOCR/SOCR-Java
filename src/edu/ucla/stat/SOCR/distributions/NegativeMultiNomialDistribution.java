/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/

package edu.ucla.stat.SOCR.distributions;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import edu.ucla.stat.SOCR.util.BesselArithmetic;
import edu.ucla.stat.SOCR.core.*;

/**
 * The Negative-Multinomial distribution with parameter-vector (x_o,p), 
 * where gamma = x_o>=0, and   p=(p_1,0, p_r). 
 * 
 * k=(k_0, k_1, k_2, ..., k_r), with sum(k_i)=n and p_k>=0, 1<=k<=n.
 * <a href="http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6V1D-4H7T8P0-1&_user=4423&_rdoc=1&_fmt=&_orig=search&_sort=d&_docanchor=&view=c&_searchStrId=1066378625&_rerunOrigin=google&_acct=C000059605&_version=1&_urlVersion=0&_userid=4423&md5=c09a50a9f4651d5f493f5190f48bc1da">
 * http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6V1D-4H7T8P0-1&_user=4423&_rdoc=1&_fmt=&_orig=search&_sort=d&_docanchor=&view=c&_searchStrId=1066378625&_rerunOrigin=google&_acct=C000059605&_version=1&_urlVersion=0&_userid=4423&md5=c09a50a9f4651d5f493f5190f48bc1da</a>
 * is a generalization of Negative-Binomial Distribution. 
 * 
 * http://dx.doi.org/10.1016/j.spl.2005.09.009
 * 
 */
	
public class NegativeMultiNomialDistribution extends Distribution implements ActionListener {
	
	   	protected int gamma; // x[0]
	   	protected int dimension;
		protected int k; 		// Number of Outcomes; k==p.length==x.length
		protected double p[]; 	// Probabilities/likelihoods of all outcomes
		protected int x[]; 		// X-values to estimate the density/CDF

	    public int type = Distribution.DISCRETE;;

	    // Provides a reference to the SOCRTextArea statusTextArea = new SOCRTextArea(); //right side bottom part
	    SOCRDistributions containerSOCRDistributions;	

     	// This is the button that allows setting the (mu, sigma) Normal distribution
    	// parametersfor the mixture component distributions.
	    private JButton NegativeMultiNomialDistributionParametersButton = 
	    		new JButton("Negative-MultiNomial Parameter Settings");
	    // private SOCRTextArea NegativeMultiNomialDistributionResultsArea = new SOCRTextArea();
	    // private javax.swing.JScrollPane NegativeMultiNomialDistributionResultsAreaScroll = 
	    // new javax.swing.JScrollPane(NegativeMultiNomialDistributionResultsArea);
    	
	    private Frame frame;
	    private edu.uah.math.devices.SOCR_MultiNomialDistributionParametersDialog multinomialParametersDialog;
	    private String[] columnNames, rowNames;
    
	    /**
	     * This default constructor creates a new MultiNomialDistribution(2, {0.5, 0.5})
	     */
	    public NegativeMultiNomialDistribution() {
	    	name = "Negative-Multinomial Distribution";
	    	gamma=2;
	    	double[] p = new double[1];
	    	p[0]=0.1;
	    	int[] x = new int[1];
	    	x[0]=1;
	    	setParameters(gamma, p, x);
	    }

	    /**
	     * This general constructor creates a new NegativeMultiNomialDistribution with specified parameters:
	     * @param n Total Sum of X-values (Frequencies)
	     * @param p array of Probabilities/likelihoods of all outcomes
	     * @param x x-vector
	     */  
	    public NegativeMultiNomialDistribution(int gamma, double[] p, int[] x) {	
	    	name = "Negative Multinomial Distribution";
			setParameters(gamma, p, x);			
	    }	

	   /**
	    * Creates a NegativeMultinomial Distribution object with parameters 
	    * @param gamma 
	    * @param p outcome probabilities 
	    */
	   public NegativeMultiNomialDistribution (int gamma, double p[])  {
	      setParameters(gamma, p, x);
	   }

	   /**
	    * This method computes the density function of the Negative Multinomail distribution
	    * @param x a vector of outcomes i
	    * @return the probability density of the vector x
	    */
	   public double getDensity(int x[]) {
	        return getProbability(gamma, p, x);
	   }

	   public double[][] getCovariance() {
	      return getCovariance (gamma, p);
	   }

	   public double[][] getCorrelation() {
	      return getCorrelation (gamma, p);
	   }

	   private static void verifyParameters (int gamma, double p[]) {
			double totalProb = 0.0;	

	      	if (gamma <= 0) {
	      		System.err.println("Gamma must be > 0!!!");
	      		gamma=1;
	      	}
	      
			if (p.length <1) {
				System.err.println("Number of outcomes must be at least 1 (k>0)!!!");
				p = new double[1];
				p[0]=0.1;
			}
			for(int i = 0; i < p.length; i++)	
			{	
				if(p[i] < 0.0 || p[i] > 1.0) {
					System.err.println("The probability of each Outcome must be non-negative (P>=0)!!!");
					//p[i]=0; // no need for this, as we correct total probability later!
				}
				totalProb += p[i];	
			}	

			/** 
			if(totalProb <= 0.0|| totalProb >= 1.0 ) {
				System.err.println("At least one outcome probability must be positive (totalProb>0)!!!");
				System.err.println("Setting the total-probability equal to 1.0!!!");
				totalProb=0.9;
			}
			for(int i = 0; i < p.length; i++)	
					p[i] *= (0.9/totalProb);
			***/
	   }

	    /**
	     * This method computes the density function of the Negative Multinomail distribution
	     * @param x a vector of outcomes i
	     * @return the probability density of the vector x
	     */
	   private double getProbability (int x[]) {
		   return getProbability (gamma, p, x);
	   }
	   
	    /**
	     * This method computes the density function of the Negative Multinomail distribution
	     * @param gamma gamma value
	     * @param p probabilities
	     * @param x a vector of outcomes i
	     * @return the probability density of the vector x
	     */
	   private static double getProbability (int gamma, double p[], int x[]) {
	      double p0 = 0.0;
	      double sumPi= 0.0;
	      double sumXi= 0.0;
	      double sumLnXiFact = 0.0;
	      double sumXiLnPi = 0.0;

	      verifyParameters (gamma, p);

	      if (x.length != p.length) System.err.println("x and p must have the same size");

	      for (int i = 0; i < p.length;i++)
	      {
	         sumPi += p[i];
	         sumXi += x[i];
	         sumLnXiFact += BesselArithmetic.logFactorial(x[i]);
	         sumXiLnPi += x[i] * Math.log (p[i]);
	      }
	      p0 = 1.0 - sumPi;

	      return Math.exp (Distribution.logGamma(gamma + sumXi) - (Distribution.logGamma(gamma) 
	    		  + sumLnXiFact) + gamma * Math.log (p0) + sumXiLnPi);
	   }

	    /**
	     * This method computes the density function of the Negative Multinomail distribution
	     * @param gamma gamma value
	     * @param p probabilities
	     * @param x a vector of outcomes i
	     * @return double the NMD CDF for the vector x
	     */
	   private static double getCDF (int gamma, double p[], int x[]) {
		   verifyParameters (gamma, p);
		   System.err.println("Negative-Multinomial CDF is not implemented yet ...");
		   return 0.0;
	   }

	    /**
	     * This method computes the density function of the Negative Multinomail distribution
	     * @param gamma gamma value
	     * @param p probabilities
	     * @return double[] mean vector
	     */
	   private static double[] getMean (int gamma, double p[]) {
	      double p0 = 0.0;
	      double sumPi= 0.0;
	      double mean[] = new double[p.length];

	      verifyParameters (gamma, p);

	      for (int i = 0; i < p.length;i++)
	         sumPi += p[i];
	      p0 = 1.0 - sumPi;

	      for (int i = 0; i < p.length; i++)
	         mean[i] = gamma * p[i] / p0;

	      return mean;
	   }

	   /**
	    * Computes the covariance matrix of the negative multinomial distribution
	    * @param gamma gamma value
	    * @param p probabilities
	    * @return double[][] covariance matrix
		**/
	   private static double[][] getCovariance (int gamma, double p[]) {
	      double p0 = 0.0;
	      double sumPi= 0.0;
	      double cov[][] = new double[p.length][p.length];

	      verifyParameters (gamma, p);

	      for (int i = 0; i < p.length;i++)
	         sumPi += p[i];
	      p0 = 1.0 - sumPi;

	      for (int i = 0; i < p.length; i++)
	      {
	         for (int j = 0; j < p.length; j++)
	            cov[i][j] = gamma * p[i] * p[j] / (p0 * p0);

	         cov[i][i] = gamma * p[i] * (p[i] + p0) / (p0 * p0);
	      }

	      return cov;
	   }

	   /**
	    * Computes the correlation matrix of the negative multinomial distribution
	    * @param gamma gamma value
	    * @param p probabilities
	    * @return double[][] correlation matrix
		**/
	   private static double[][] getCorrelation (int gamma, double[] p) {
	      double corr[][] = new double[p.length][p.length];
	      double sumPi= 0.0;
	      double p0;

	      verifyParameters (gamma, p);
	      
	      for (int i = 0; i < p.length;i++)
	         sumPi += p[i];
	      p0 = 1.0 - sumPi;

	      for (int i = 0; i < p.length; i++) {
	         for (int j = 0; j < p.length; j++)
	            corr[i][j] = Math.sqrt(p[i] * p[j] /((p0 + p[i]) * (p0 + p[j])));
	         corr[i][i] = 1.0;
	      }
	      return corr;
	   }

	   /**
	    * MLE estimates of the NMD parameters P_hat
	    * @param x the list of observations used to evaluate parameters
	    * @param n the number of observations used to evaluate parameters
	    * @param d the dimension of each vector
	    * @return returns the parameters
	    * This getMLE() method needs to become static, after we fix this problem with
	    * the MLE estiamte of parameter[0] !!! See below.
	    */
	   public double[] getMLE (int x[][], int n, int d) {
	      double parameters[] = new double[d + 1];
	      int ups[] = new int[n];
	      double mean[] = new double[d];

	      int i, j, l;
	      int M;
	      int prop;

	      // Initialization
	      for (i = 0; i < d; i++) mean[i] = 0;

	      // Ups_j = Sum_k x_ji
	      // mean_i = Sum_n x_ji / n
	      for (j = 0; j < n; j++) {
	         ups[j] = 0;
	         for (i = 0; i < d; i++) {
	            ups[j] += x[j][i];
	            mean[i] += x[j][i];
	         }
	      }
	      for (i = 0; i < d; i++) mean[i] /= n;

	      // M = Max(Ups_j)
	      M = ups[0];
	      for (j = 1; j < n; j++) 
	         if (ups[j] > M) M = ups[j];

	      if (M >= Integer.MAX_VALUE) System.err.println("gamma/p_i too large!!!");

	      double Fl[] = new double[M];
	      for (l = 0; l < M; l++) {
	         prop = 0;
	         for (j = 0; j < n; j++)
	            if (ups[j] > l) prop++;

	         Fl[l] = (double) prop / (double) n;
	      }

	      LikelihoodFunction f = new LikelihoodFunction (n, (int)M, ups, Fl);
	      parameters[0] = edu.ucla.stat.SOCR.util.AnalysisUtility.functionRootBrentDekker(
	    		  1e-9, 1e9, f, 1e-5);

	      // REVIEW this code!!! parameter[0] estimate is too large!!!!! 
	      // the code below Uses the exact value of p0, instead of estimating it from data!!!
	      // See: http://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_Distrib_Dists 
	      
	      int totalSum=0; 
	      for (i = 0; i < d; i++) totalSum += getProbabilities()[i];
	      parameters[0] =  1.0 - totalSum;
	      // Review code above!!!!!!!!!!!!!!!!!!!!!!
	      // getMLE() method needs to become static, after we fix this problem with
	      // the MLE estimate of parameter[0] !!!
	      
	      double lambda[] = new double[d];
	      double sumLambda = 0.0;
	      for (i = 0; i < d; i++) {
	         lambda[i] = mean[i] / parameters[0];
	         sumLambda += lambda[i];
	      }

	      for (i = 0; i < d; i++) {
	         parameters[i + 1] = lambda[i] / (1.0 + sumLambda);
	         if (parameters[i + 1] > 1.0)
	        	 System.err.println ("p_i > 1!!!!!!");
	      }
	      return parameters;
	   }


	   /**
	    * Returns the parameter gamma of this object.
	    * @return gamma 
	    */
	   public int getGamma() {
	      return gamma;
	   }

	   /**
	    * Returns the parameter gamma of this object.
	    * @return gamma 
	    */
	   public double[] getProbabilities() {
		   return this.p;
	   }

	   /**
	    * Returns the parameter vector p of this object.
	    * @return double[] p (probabilities vector)
	    */
	   public double[] getP() {
	      return p;
	   }


	   /**
	    * Sets the parameters gamma and p of this object.
	    * @param gamma gamma = x[0]
	    * @param double[] p probability vector
	    * @param double[] x frequencies vector
	    */
	   public void setParameters (int gamma, double p[], int[] x) {
	      double sumPi = 0.0;
	      int sumX = 0;

	      verifyParameters (gamma, p);
	      
	      if (gamma <= 0) {
	    	  System.err.println("gamma must be > 0!!!\n Setting gamma=1.0!!!");
	    	  this.gamma = 1;
	      }

	      this.gamma = gamma;
	      this.dimension = p.length;
	      this.p = new double[dimension];
	      this.x = new int[dimension];
	      
	      for (int i = 0; i < dimension; i++)
	      {
	         if ((p[i] < 0) || (p[i] >= 1)) 
	        	 System.err.println("p is not a probability vector!!!!");

	         sumPi += p[i];
	         this.p[i] = p[i];
	         this.x[i] = x[i];
	         sumX += x[i];
	      }

	      if (sumPi >= 1.0) System.err.println("p is not a probability vector!!!");
	      
	      if (p.length <1) {
				System.err.println("Number of outcomes must be at least 1 (k>0)!!!");
				p = new double[1];
				p[0]=0.5;
	      }
	      for(int i = 0; i < p.length; i++)	{	
				if(p[i] < 0.0 || p[i] > 1.0) {
					System.err.println("The probability of each Outcome must be non-negative (P>=0)!!!");
					// p[i]=0; // no need for this, as we correct total probability later!
				}
				sumPi += p[i];	
	      }	

	      if(sumPi <= 0.0) {
				System.err.println("At least one outcome probability must be positive (totalProb>0)!!!");
				System.err.println("Setting the total-probability equal to 1.0!!!");
				p[0]=0.1;
				sumPi=0.1;
	      }
	      if(sumPi >= 1.0) {
				for(int i = 0; i < p.length; i++)	
					p[i] *= (0.9/sumPi);
	      }
	      this.setDomain(0, 10, 10, this.type);
	      name = "Negative MultiNomial Distribution";
	   }
    
	   public void initialize() {
	    	containerSOCRDistributions = getSOCRDistributions();	
	    	containerSOCRDistributions.setShowGraph(false);
	    	containerSOCRDistributions.setShowCutOffs(false);
	    	containerSOCRDistributions.getGraphPanel().setPreferredSize(new Dimension(300,0));
	    	containerSOCRDistributions.getSOCRTextArea().setText("");
	        createValueSetter("k=Number of Negative-Multinomial Outcomes (k=p.length=x.length>0)", 
	        		DISCRETE, 1, 10, 1);
	        createValueSetter("Gamma=x[0] frequency!!!", DISCRETE, 0, 20, 1);
	        
	        NegativeMultiNomialDistributionParametersButton.addActionListener(this);
	        NegativeMultiNomialDistributionParametersButton.setName(
	        		"Negative Multinomial Distribution Settings");
	        NegativeMultiNomialDistributionParametersButton.setToolTipText(
	        		"Select the p-values and Gamma-value for the "+
	        		"Negative Multinomial Distribution");
	     
	        try { NegativeMultiNomialDistributionParametersButton.setIcon(
	        		new ImageIcon(getClass().getResource("SOCR_MixtureModelDistributionIcon.jpg")));
	        } catch (Exception e) {}
	        
	        createComponentSetter("Select Negative Multinomial Distribution Parameters", 
	        		NegativeMultiNomialDistributionParametersButton);        
	        
	       // createComponentSetter("Multinomial Distribution Results", 
	       // NegativeMultiNomialDistributionResultsAreaScroll);
	        
	        rowNames = new String[] {"probabilities", "X-values"};
	        
	        // Number of Outcomes; k==p.length==x.length
	        int v1 = getValueSetter(0).getValueAsInt();  
	        
	        // Total Sum of X-values; n=x[0]+x[1]+...+x[k]
	        int v2 = getValueSetter(1).getValueAsInt();        
	        p = new double[v1];
	        x = new int[v1];
	        columnNames = new String[v1];
	        this.gamma=v2;
        
	        //      Set up default parameters each time the Number of Outcomes is changed!
	        for (int i=0; i < v1; i++) {
	        	p[i] = 0.9/v1;
	        	if (i==0) x[i] = v2;
	        	else x[i] = 0;
	        	columnNames[i]=new String("Outcome"+(i+1));
	        }
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        int v2 = getValueSetter(1).getValueAsInt();
        gamma=v2;
        double[] p = new double[v1];
        int[] x = new int[v1];
        
        columnNames = new String[v1];
        
        // Set up default parameters each time the NUmber of mixtures is changed!
        for (int i=0; i < v1; i++) {   	
        	p[i] = 0.9/v1;
        	if (i==0) x[i] = v2; 
        	else x[i] = 0;
        	columnNames[i] = new String("Outcome_"+(i+1));
        }
        setParameters(gamma, p, x);
    }

	/**
	* This method handles the events associated with the changing the Multinomial probabilities
	* or x-values.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == NegativeMultiNomialDistributionParametersButton){
			frame = new Frame();
			Point fp = new Point(20,20); 
			Point dp;
			
			multinomialParametersDialog = 
				new edu.uah.math.devices.SOCR_MultiNomialDistributionParametersDialog(
					frame, "Negative MultinNomial Distribution Parameters", getProbabilities(), 
						getXvalues(), columnNames, rowNames);
			Dimension fs = frame.getSize(), ds = multinomialParametersDialog.getSize();
			dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			multinomialParametersDialog.setLocation(dp);
			multinomialParametersDialog.setVisible(true);
			if (multinomialParametersDialog.isOK()){
				setParameters(this.gamma, multinomialParametersDialog.getProbabilities(),
						multinomialParametersDialog.getXvalues()
				);
				multinomialParametersDialog.setVisible(false);
				
				// Call the update of the core.SOCRDistributions.statusTextArea TextArea
				containerSOCRDistributions.getSOCRTextArea().
					setText(getNegativeMultiNomialTestResults());
			}
		}
	}
	
    /**
     * This method gets the array of Negative Multinomail parameters (x-Values[]).
     * @return xValues the array of the X-values of the Multinomial Distributions
     */
    public int[] getXvalues() {
        int[] allParameters = new int[p.length];
        for (int j=0; j< p.length; j++) {
         		allParameters[j] = x[j];
         }
        return allParameters;
    }

    /**
     * This method gets the array of Multinomail parameters (x-Values[]).
     * @return xValuesSum the Sum of the array of the X-values of the Multinomial Distributions
     */
    public int getXvaluesSum() {
        int sum = 0;
        for (int j=0; j< x.length; j++) {
         		sum += x[j];
         }
        return sum;
    }

    /** Define the Multinomial getDensity function */
    public double getDensity(double x) {
        return 1.0;
    }

    /**
     * This method returns a Text Array with All cumulative Negative-Multinomial results
     */
	public String getNegativeMultiNomialTestResults() {
		String results="\tNegative Multi-nomial Distribution\n";
		results += "Parameters:\n";
		results += "Number of Outcomes: k="+p.length+"\nP-values={";
		for(int i = 0; i < p.length; i++) {
			if (i==x.length-1) results += this.p[i];
			else results += this.p[i]+",";
		}
		results += "}\n\nGamma (x[0] frequency) ="+this.gamma+"\n\n";
		results += "X-values={";
		for(int i = 0; i < x.length; i++) {
			if (i==x.length-1) results += this.x[i];
			else results += this.x[i]+",";
		}
		results += "}\nPDF="+this.getProbability(x)+"\n";
		results += "\nMean-Vector:\n{";
		double[] mean = this.getMean(this.gamma, this.p);
		for(int i = 0; i < p.length; i++) {	
			results += mean[i]+" ";
		}
		results += "}\n";
		
		results += "\nVariance-Covariance:\n";
		double[][] cov = this.getCovariance();
		for(int i = 0; i < p.length; i++) {	
			for(int j = 0; j < p.length; j++)
				results += cov[i][j]+" ";
			results += "\n";
		}
		results += " (approximate)\n";
		results += "\nCorrelations:\n";
		double[][] cor = this.getCorrelation();
		for(int i = 0; i < p.length; i++) {	
			for(int j = 0; j < p.length; j++)
				results += cor[i][j]+" ";
			results += "\n";
		}
		results += "\n";
		
		results += "\nMLE probability-vector estimation:\n{";
		int[][] xMockUp = new int[1][dimension];
		for (int l=0; l<dimension; l++) xMockUp[0][l]=x[l];
		double[] paramMLEEstim = getMLE (xMockUp, 1, dimension);
		for (int l=0; l<dimension+1; l++) 
			results += paramMLEEstim[l]+" ";
		results += "}\n\n";
		return results;
	}
	
    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
           "http://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_Distrib_Dists");
    }
    
	private static class LikelihoodFunction implements edu.ucla.stat.SOCR.util.MathFunction {
		protected double Fl[];
		protected int ups[];
		protected int n;
		protected int M;
		protected int sumUps;

		public LikelihoodFunction (int n, int m, int ups[], double Fl[]) {
		    this.n = n;
		    this.M = m;
		         
		    this.Fl = new double[Fl.length];
		    System.arraycopy (Fl, 0, this.Fl, 0, Fl.length);
		    this.ups = new int[ups.length];
		    System.arraycopy (ups, 0, this.ups, 0, ups.length);

		    sumUps = 0;
		    for (int i = 0; i < ups.length; i++) sumUps += ups[i];
		}

		public double evaluate (double gamma) {
		    double sum = 0.0;
		    for (int l = 0; l < M; l++) sum += (Fl[l] / (gamma + (double) l));
		    return (sum - Math.log1p (sumUps / (n * gamma)));
		}
	}
	
}
