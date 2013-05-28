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
import edu.ucla.stat.SOCR.core.SOCRApplet.SOCRTextArea;

/**
 * The Multinomial distribution with parameter-vector (k,p), where n=number of trials and event-probabilities 
 * p=(p1, p2, ..., pn), with sum(p_k)=1 and p_k>=0, 1<=k<=n.
 * <a href="http://en.wikipedia.org/wiki/Multinomial_distribution">
 * http://en.wikipedia.org/wiki/Multinomial_distribution</a> is a generalization of the binomial distribution.
 */

public class MultiNomialDistribution extends Distribution implements ActionListener {

	protected int k; 		// Number of Outcomes; k==p.length==x.length
	protected int n; 		// Total Sum of X-values; n=x[0]+x[1]+...+x[k]
	protected double p[]; 	// Probabilities/likelihoods of all outcomes
	protected int x[]; 	// X-values to estimate the density/CDF

    public int type = Distribution.DISCRETE;;

    SOCRDistributions containerSOCRDistributions;	
    // Provides a reference to the SOCRTextArea statusTextArea = new SOCRTextArea(); //right side bottom part
    
     	// This is the button that allows setting the (mu, sigma) Normal distribution
    	// parametersfor the mixture component distributions.
    private JButton multinomialDistributionParametersButton = new JButton("MultiNomial Parameter Settings");
   // private SOCRTextArea multinomialDistributionResultsArea = new SOCRTextArea();
   // private javax.swing.JScrollPane multinomialDistributionResultsAreaScroll = new javax.swing.JScrollPane(multinomialDistributionResultsArea);
    	
    private Frame frame;
    private edu.uah.math.devices.SOCR_MultiNomialDistributionParametersDialog multinomialParametersDialog;
    private String[] columnNames, rowNames;
    
 	/**
  	* This general constructor creates a new MultiNomialDistribution with specified parameters:
   	* @param n Total Sum of X-values (Frequencies)
  	* @param p array of Probabilities/likelihoods of all outcomes
  	*/  
	public MultiNomialDistribution(int n, double[] p, int[] x)
	{	name = "Multinomial Distribution";
		setParameters(n, p, x);			
	}	

    /**
     * This default constructor creates a new MultiNomialDistribution(2, {0.5, 0.5})
     */
    public MultiNomialDistribution() {
    	name = "Multinomial Distribution";
    	int n=2;
    	double[] p = new double[2];
    	p[0]=0.5; p[1]=0.5;
    	int[] x = new int[2];
    	x[0]=1; x[1]=1;
    	setParameters(n, p, x);
    }

    public void initialize() {
    	containerSOCRDistributions = getSOCRDistributions();	
    	containerSOCRDistributions.setShowGraph(false);
    	containerSOCRDistributions.setShowCutOffs(false);
    //	containerSOCRDistributions.packControlPaneExternalCall();
    	containerSOCRDistributions.getGraphPanel().setPreferredSize(new Dimension(300,0));
    	containerSOCRDistributions.getSOCRTextArea().setText("");
        createValueSetter("k=Number of Multinomial Outcomes (k=p.length=x.length)", DISCRETE, 1, 10, 1);
        createValueSetter("n=Sum of frequencies, X-values, n=x[0]+x[1]+...+x[k]", DISCRETE, 0, 20, 1);
        
        multinomialDistributionParametersButton.addActionListener(this);
        multinomialDistributionParametersButton.setName("Multinomial Distribution Settings");
        multinomialDistributionParametersButton.setToolTipText(
        		"Select the p-values and x-values for all outcome in the Multinomial Distribution");
     
        try { multinomialDistributionParametersButton.setIcon(
        		new ImageIcon(getClass().getResource("SOCR_MixtureModelDistributionIcon.jpg")));
        } catch (Exception e) {}
        
        createComponentSetter("Select Multinomial Distribution Parameters", multinomialDistributionParametersButton);        
        
       // createComponentSetter("Multinomial Distribution Results", multinomialDistributionResultsAreaScroll);
        
        rowNames = new String[] {"probabilities", "X-values"};
        
        int v1 = getValueSetter(0).getValueAsInt();        // Number of Outcomes; k==p.length==x.length
        int v2 = getValueSetter(1).getValueAsInt();        // Total Sum of X-values; n=x[0]+x[1]+...+x[k]
        p = new double[v1];
        x = new int[v1];
        columnNames = new String[v1];
        n=v2;
        
        //      Set up default parameters each time the Number of Outcomes is changed!
        for (int i=0; i < v1; i++) {
        	p[i] = 1.0/v1;
        	if (i==0) x[i] = v2;
        	else x[i] = 0;
        	columnNames[i]=new String("Outcome"+(i+1));
        }
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        int v2 = getValueSetter(1).getValueAsInt();
        int n=v2;
        double[] p = new double[v1];
        int[] x = new int[v1];
        
        columnNames = new String[v1];
        
        //      Set up default parameters each time the NUmber of mixtures is changed!
        for (int i=0; i < v1; i++) {   	
        	p[i] = 1.0/v1;
        	if (i==0) x[i] = v2; 
        	else x[i] = 0;
        	columnNames[i] = new String("Outcome_"+(i+1));
        }
        setParameters(n, p, x);
    }

	/**
	* This method handles the events associated with the changing the Multinomial probabilities
	* or x-values.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == multinomialDistributionParametersButton){
			frame = new Frame();
			Point fp = new Point(20,20); 
			Point dp;
			
			multinomialParametersDialog = 
				new edu.uah.math.devices.SOCR_MultiNomialDistributionParametersDialog(
						frame, "MultinNomial Distribution Parameters", getProbabilities(), 
						getXvalues(), columnNames, rowNames);
			Dimension fs = frame.getSize(), ds = multinomialParametersDialog.getSize();
			dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			multinomialParametersDialog.setLocation(dp);
			multinomialParametersDialog.setVisible(true);
			if (multinomialParametersDialog.isOK()){
				setParameters(multinomialParametersDialog.getSumXvalues(),
						multinomialParametersDialog.getProbabilities(),
						multinomialParametersDialog.getXvalues()
					);
				multinomialParametersDialog.setVisible(false);
				
				// Call the update of the core.SOCRDistributions.statusTextArea TextArea
				containerSOCRDistributions.getSOCRTextArea().setText(getMultiNomialTestResults());
				// containerSOCRDistributions.repaint();
				//multinomialDistributionResultsArea.setText(getMultiNomialTestResults());
			}
		}
	}
	
    /**
     * This method gets the array of Multinomail parameters (probabilities[]).
     * @param probabilities the array of the probailities of the Multinomial Distributions
     */
    public double[] getProbabilities() {
        double[] allParameters = new double[p.length];
        for (int j=0; j< p.length; j++) {
        		allParameters[j] = p[j];
         }
        return allParameters;
    }

    /**
     * This method gets the array of Multinomail parameters (x-Values[]).
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

    public double getProbability(int x[])	
	{
		return getProbability(n, p, x);
	}

    /** Define the Multinomial getDensity function */
    public double getDensity(double x) {
        return 1.0;
    }

	public int getN()
	{
		return n;
	}

	public double[] getP()
	{	
		return p;	
	}	

	public double getCDF(int x[])
	{
		return getCDF(n, p, x);
	}


    /**
     * This method returns the variance-covariance matrix of the Multinomail distribution, which is
     * defined as sigma(i,j) = E[(Xi - mu(i))(Xj - mu(j))].
     * @return VarianceMatrix variance-covariance matrix of the Multinomial Distributions
     */
	public double[][] getCovariance()
	{
		return getCovariance(n, p);
	}

	public double[][] getCorrelation()
	{
		return getCorrelation(n, p);
	}

	private static void verifyParameters(int n, double p[])
	{	
		double totalProb = 0.0;	
		if (p.length <2 || n<0) {
			System.err.println("Number of outcomes must be at least 2 (k>1) & n>=0 !!!");
			n=1;
			p = new double[2];
			p[0]=0.5; p[1]=0.5;
		}
		for(int i = 0; i < p.length; i++)	
		{	
			if(p[i] < 0.0 || p[i] > 1.0) {
				System.err.println("The probability of each Outcome must be non-negative (P>=0)!!!");
				// p[i]=0; // no need for this, as we correct total probability later!
			}
			totalProb += p[i];	
		}	

		if(totalProb <= 0.0) {
			System.err.println("At least one outcome probability must be positive (totalProb>0)!!!");
			System.err.println("Setting the total-probability equal to 1.0!!!");
			p[0]=1;
			totalProb=1.0;
		}
		if(totalProb != 1.0)	
		{	for(int i = 0; i < p.length; i++)	
				p[i] /= totalProb;
		}
		return;	
	}	

    /**
     * This method computes the density function of the Multinomail distribution
     * @param x a vector of outcomes i
     * @return the probability density of the vector x
     */
    public double getDensity(int x[]) {
        return getProbability(this.n, this.p, x);
    }

    /**
     * This method computes the density function of the Multinomail distribution
     * @param n Total sum ov x-values vector (total frequencies
     * @param p multinomial probabilities
     * @param x X-values (frequency) vector
     * @return CDF CDF value
     */
	public static double getProbability(int n, double p[], int x[])
	{	verifyParameters(n, p);
		double sumXFact = 0.0;	
		int sumX = 0;	
		double sumPX = 0.0;	
		if(x.length != p.length) 
			System.err.println("The variable (x) and probability (p) vectors must have the same dimensions!!!");
		for(int i = 0; i < p.length; i++) {	
			sumX += x[i];	
			sumXFact += BesselArithmetic.logFactorial(x[i]);
			sumPX += (double)x[i] * Math.log(p[i]);
		}	

		if(sumX != n) { 
			System.err.println("WARNING!!! SumX-values("+sumX+") != n("+n+") !!!!!!");
			return 0.0;	
		}
		else return Math.exp((BesselArithmetic.logFactorial(n)- sumXFact) + sumPX);
	}	
	
    /**
     * http://controls.engin.umich.edu/wiki/index.php/Multinomial_distributions
     * @param n total sum of the x-value (frequency) vector
     * @param p Multinomial Probabilities
     * @param x X-values (frequencies)
     * @return CDF CDF value
     */
	public static double getCDF(int n, double p[], int x[])
	{	verifyParameters(n, p);
		boolean end = false;	
		double sum = 0.0;	
		if(x.length != p.length) 
			System.err.println("The variable (x) and probability (p) vectors must have the same dimensions!!!");
		int[] is = new int[x.length];	
		for(int i = 0; i < is.length; i++)	is[i] = 0;	

		sum = 0.0;	
		do	
		{	/*System.err.print("Current 'is' vector: {"+is[0]);
			for(int j = 1; j < is.length; j++) {
				System.err.print(", "+is[j]);
			}
			System.err.println("}!!!");*/
						
			if(end) break;
			
			is[0] = n - is[0];
			
			sum += getProbability(n, p, is);
			
			is[0] = n - is[0];
			
			//System.err.println("Sum = "+sum);
			is[0]++;
			if(is[0] > x[0]) {
				
				// Why should we zero the null element, this element should only be < n!!!!
				// No other restrictions!!!
				is[0] = 0;
					
				int j;	
				for(j = 1; j < x.length && is[j] ==	x[j]; is[j++] = 0);
				if(j == x.length) end = true;	
				else is[j]++;	
				
			/*	System.err.print("INSIDE 'is' vector: {"+is[0]);
				for(int l = 1; l < is.length; l++) {
					System.err.print(", "+is[l]);
				}
				System.err.println("}!!!");*/
				
			}	
		}  while(true);	
		double cdf = 1.0-sum+getProbability(n,p,x);
		if (cdf<0) cdf=0;
		else if (cdf>1) cdf=1;
		return cdf;	
	}	

	public static double[] getMean(int n, double p[])
	{	verifyParameters(n, p);
		double[] mean = new double[p.length];
		for(int i = 0; i < p.length; i++) mean[i] = (double)n * p[i];	
		return mean;	
	}	

	public static double[][] getCovariance(int n, double p[])
	{	verifyParameters(n, p);	
		double[][] cov = new double[p.length][p.length];
		for(int i = 0; i < p.length; i++) {	
			for(int j = 0; j < p.length; j++) cov[i][j] = (double)(-n) * p[i] * p[j];
			cov[i][i] = (double)n * p[i] * (1.0 - p[i]);
		}	
		return cov;	
	}	

	public static double[][] getCorrelation(int n, double p[])
	{	verifyParameters(n, p);
		double[][] corr = new double[p.length][p.length];
		for(int i = 0; i < p.length; i++) {	
			for(int j = 0; j < p.length; j++)	
				corr[i][j] = -Math.sqrt((p[i] * p[j]) / ((1.0-p[i]) * (1.0-p[j])));
			corr[i][i] = 1.0D;	
		}	
		return corr;	
	}	

    /**
     * This method returns a Text Array with All cumulative Multinomial results
     */
	public String getMultiNomialTestResults() {
		String results="\tMulti-nomial Distribution\n";
		results += "Parameters:\n";
		results += "Number of Outcomes: k="+this.k+"\nP-values={";
		for(int i = 0; i < p.length; i++) {
			if (i==x.length-1) results += this.p[i];
			else results += this.p[i]+",";
		}
		results += "}\nTotal Sum of X-values (Frequencies): k="+this.getXvaluesSum()+"\n";
		results += "X-values={";
		for(int i = 0; i < x.length; i++) {
			if (i==x.length-1) results += this.x[i];
			else results += this.x[i]+",";
		}
		results += "}\nPDF="+this.getProbability(x)+"\n";
		results += "\nMean-Vector:\n";
		double[] mean = this.getMean(n, p);
		for(int i = 0; i < p.length; i++) {	
			results += mean[i]+" ";
		}
		results += "\n";
		
		results += "\nCDF="+this.getCDF(n,p,x)+"\n";
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
		return results;
	}
	
    /**
     * This method returns the array of MLE estimates for the Mulinomial Probabilities.
     * @param x double array of X-values -- list of observations used to evaluate prob-parameters
     * @param m the number of observations used to evaluate parameters
     * @param d the dimension of each observation
     * @param n the number of independent trials for each series 
     * @return probabilities the array of the probailities of the Multinomial Distributions (P-hat vector)
     */
	public static double[] getMLE(int[][] x, int m, int d, int n)
	{	
		double parameters[] = new double[d];	
		double xBar[] = new double[d];	
		double N = 0.0;	
		if(m <= 0) System.err.println("getMLE: The variable m must be positive (m>0)!!!");
		if(d <= 0) System.err.println("getMLE: The variable d must be positive (d>0)!!!");
		for(int i = 0; i < d; i++) xBar[i] = 0.0D;	

		for(int v = 0; v < m; v++)	
			for(int c = 0; c < d; c++) xBar[c] += x[v][c];

		for(int i = 0; i < d; i++)
		{
			xBar[i] = xBar[i] / (double)n;
			N += xBar[i];
		}	

		if(N != (double)n) System.err.println("getMLE: The variable n is correctly set!!!");
		for(int i = 0; i < d; i++) parameters[i] = xBar[i] / (double)n;	
		return parameters;	
	}

	public void setParameters(int n, double	p[], int x[])
	{	
		double totalProb = 0.0;	
		int totalOutcomes = 0;
		
		if(n < 0) {
			System.err.println("Sum-X-values must be non-negative (n>=0)!!!");
			n=1;
		}
		if(p.length < 2 || x.length <2 || p.length != x.length) {
			System.err.println("The number of Outcomes must equal the"+
					" number of X-values and be at least 2 (p.length==x.length > 1)!!!");
			p = new double[2];
			x= new int[2];
			p[0]=0.5; p[1]=0.5;
			x[0]=1; x[1] = 1;
			n=2;
		}
		
		this.n = n;	
		int dimension = p.length;
		this.k = dimension;
		this.p = new double[dimension];	
		this.x = new int[dimension];	
		
		for(int i = 0; i < dimension; i++)	
		{	if(p[i] < 0.0 || p[i] > 1.0) p[i] = 0.0;
			totalProb += p[i];
			
			if(x[i] < 0 || x[i] > n) x[i] = 0;
			totalOutcomes += x[i];
			//System.err.println("Frequency X-value["+(i+1)+"]="+x[i]);
		}
		
		// Correct p-values, if necessary
		if (totalProb<=0) totalProb = 1;
		for(int i = 0; i < dimension; i++) this.p[i] = p[i]/totalProb;

		// Correct X-values, if their sum is NOT equal to n
		if (totalOutcomes != n ) { 
			totalOutcomes = n;
			for(int i = 0; i < dimension; i++) {
				if(i==0)  x[i] = totalOutcomes;
				else x[i]=0;
			}
		}
		for(int i = 0; i < dimension; i++) this.x[i] = x[i];
		
		// How to communicate the Change of the Multinomial parameters to the GraphPanel????
        // updated the Distribution.setDomain() method to include observable.notifyObservers();
        this.setDomain(0, n, n, this.type);
        name = "MultiNomial Distribution";
		return;
	}	

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_Distrib_Multinomial");
    }
}

