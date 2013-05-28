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

import edu.uah.math.devices.DieProbabilityDialog;
import edu.uah.math.devices.ProbabilityDialog;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.ucla.stat.SOCR.core.*;

/**
 * The Mixture distribution with parameter-vector p=(p1, p2, ..., pn) <a
 * href="http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_2D_PointSegmentation_EM_Mixture">
 * http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_2D_PointSegmentation_EM_Mixture</a> is the
 * (linear) mixure of an array of distributions according to the mixing
 * parameters.
 */

public class MixtureDistribution extends Distribution implements ActionListener {

    /**
     * @uml.property name="distributions"
     * @uml.associationEnd multiplicity="(0 -1)"
     */
    Distribution[] distributions;

    int n;
    int type;

    /**
     * @uml.property name="probabilities"
     */
    double[] probabilities;

    	// This is the button that allows setting the (mu, sigma) Normal distribution
    	// parametersfor the mixture component distributions.
    private JButton mixtureDistributionParametersButton = new JButton("Mixture Parameter Settings");
    private Frame frame;
    private edu.uah.math.devices.SOCR_MixtureDistributionParametersDialog mixtureParametersDialog;
    private double[] means, SDs, weights;
    private String[] columnNames, rowNames;
    
    /**
     * This general constructor creates the mixture of a given array of
     * distributitons using a given array of probabilities as the mixing
     * parameters.
     *
     * @param d the array of distributions to be mixed
     * @param p the array of mixing probabilities
     */
    public MixtureDistribution(Distribution[] d, double[] p) {
        setParameters(d, p);
    }

    /**
     * This special constructor creates the mixture of two distributions using a
     * specified number and its complement as the mixing probabilities.
     *
     * @param d0 the index 0 distributions
     * @param d1 the index 1 distributions
     * @param a the index 1 mixing parameter (the index 0 parameter is 1 &minus;
     *        a)
     */
    public MixtureDistribution(Distribution d0, Distribution d1, double a) {
        double[] mixParams =  {a, 1-a};
        Distribution[] dists = {d0, d1};
        setParameters(dists, mixParams);
    }

    /**
     * This special constructor creates the mixture of two distributions with
     * equal mixing probabilities
     *
     * @param d0 the index 0 distribution
     * @param d1 the index 1 distribution
     */
    public MixtureDistribution(Distribution d0, Distribution d1) {
        this(d0, d0, 0.5);
    }

    /**
     * This default constructor creates the mixture of two standard normal
     * distributions with equal mixing parameters. The result is simply another
     * standard normal distribution.
     */
    public MixtureDistribution() {
        double[] mixParams =  {0.5, 0.5};
        Distribution[] dists = {new NormalDistribution(-2, 1), 
        		new NormalDistribution(2, 1)};
        setParameters(dists, mixParams);
        // this(new NormalDistribution(-2, 1), new BetaDistribution(2, 1), 0.5);
    }

    public void initialize() {
        createValueSetter("Number of Normal/Gaussian Mixture Components", DISCRETE, 1, 10, 1);
        mixtureDistributionParametersButton.addActionListener(this);
        mixtureDistributionParametersButton.setName("Mixture Parameter Settings");
        mixtureDistributionParametersButton.setToolTipText(
        		"Select Mean/SD/Weight for each Normal Mixture Component");

        // addObserver(this);	//  ??????????
       
        try { mixtureDistributionParametersButton.setIcon(
        		//new ImageIcon("http://socr.ucla.edu/images.dir/SOCR_MixtureModelDistributionIcon.png"));
        		new ImageIcon(getClass().getResource("SOCR_MixtureModelDistributionIcon.jpg")));
        } catch (Exception e) {}
        
        createComponentSetter("Select Normal Mixture Mean/SD/Weight",
        		mixtureDistributionParametersButton);        
        rowNames = new String[] {"mean", "SD", "weight"};
        
        int v1 = getValueSetter(0).getValueAsInt();        
        means = new double[v1];
        SDs = new double[v1];
        weights = new double[v1];     
        distributions = new Distribution[v1];
        columnNames = new String[v1];
        n=v1;
        
        for (int i=0; i < v1; i++) {
        		// Set up default parameters each time the NUmber of mixtures is changed!
        	means[i] = -2*(v1-1) + i*4;
        	SDs[i] = 1.0;
        	weights[i] = 1.0/v1;
        	columnNames[i]=new String("Dist"+(i+1));
        	distributions[i] = new NormalDistribution(means[i], SDs[i]);
        }
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        n=v1;
        double[] mixParams =  new double[v1];
        
        double[] _means = new double[v1];
        double[] _SDs = new double[v1];
        double[] _weights = new double[v1];
        
        distributions = new Distribution[v1];
        columnNames = new String[v1];
        
        for (int i=0; i < v1; i++) {   	
        		// Set up default parameters each time the NUmber of mixtures is changed!
        	_means[i] = -2*(v1-1) + i*4;
        	_SDs[i] = 1.0;
        	_weights[i] = 1.0/v1;
        	
        	columnNames[i] = new String("Dist_"+(i+1));
        }
    	setMeansSDsWeights(_means, _SDs, _weights);
    }

	/**
	* This method handles the events associated with the die probabilities
	* button and with the step timer. If the die probability button is pressed, the current die
	* probabilities are loaded into the dialog box which is then displayed. For the step timer,
	* the dice are shown one at a time.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == mixtureDistributionParametersButton){
			frame = new Frame();
			Point fp = new Point(20,20); 
			Point dp;
			
			mixtureParametersDialog = 
				new edu.uah.math.devices.SOCR_MixtureDistributionParametersDialog(
						frame, "Mixture Distribution Parameters", getMeansSDsWeights(), 
						columnNames, rowNames);
			Dimension fs = frame.getSize(), ds = mixtureParametersDialog.getSize();
			dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			mixtureParametersDialog.setLocation(dp);
			mixtureParametersDialog.setVisible(true);
			if (mixtureParametersDialog.isOK()){
				setMeansSDsWeights(mixtureParametersDialog.getMeans(),
						mixtureParametersDialog.getSDs(),
						mixtureParametersDialog.getWeights()
					);
				mixtureParametersDialog.setVisible(false);
			}
		}
	}

    /**
     * This method sets up the domain of the general mixture distributions in
     * terms of the distributions being mixed.
     *
     * @param d the array of distributions being mixed
     * @param p the array of mixing probabilities
     * @uml.property name="probabilities"
     */
    public void setParameters(Distribution[] d, double[] p) {
        double minLower = Double.POSITIVE_INFINITY, maxUpper = Double.NEGATIVE_INFINITY;
        double minWidth = Double.POSITIVE_INFINITY;
        
        double a, b, w;
        Domain domain;
        distributions = d;
        int t0 = distributions[0].getType(), t;
        n = distributions.length;
        boolean mixed = false;
        
        for (int i = 0; i < n; i++) {
            domain = distributions[i].getDomain();
            t = distributions[i].getType();

            if (t == DISCRETE) a = domain.getLowerValue();
            else a = domain.getLowerBound();
            if (a < minLower) minLower = a;
            
            if (t == DISCRETE) b = domain.getUpperValue();
            else b = domain.getUpperBound();
            
            if (b > maxUpper) maxUpper = b;
            w = domain.getWidth();
            if (w < minWidth) minWidth = w;
            if (t != t0) mixed = true;
        }
        
        if (mixed) t = 2;
        else t = t0;
                
        //Assign probabilities. Correct errors if necessary
        if (p.length != n) {
            p = new double[n];
            for (int i = 0; i < n; i++)
                p[i] = 1.0 / n;
        } else probabilities = Functions.getProbabilities(p);
        
        means = new double[d.length];
        SDs = new double[d.length];
        weights = new double[d.length];
        double totalWeight=0.0;
        
        for (int i=0; i< d.length; i++) {
    		means[i]=distributions[i].getMean();
    		SDs[i]=distributions[i].getSD();
    		weights[i]=p[i];
    		totalWeight += weights[i];
        }

        if (totalWeight>0)		// Equi-distribute the weights if their sum!=1
        	for (int i=0; i< d.length; i++) weights[i] /= totalWeight;

        // How to communicate the Change of the Mixture parameters to the GraphPanel????
        // updated the Distribution.setDomain() method to include observable.notifyObservers();
        this.setDomain(minLower, maxUpper, minWidth, t);
        name = "Mixture Distribution";
    }

    /**
     * This method gets up the 3 arrays of mixture parameters (Means[], SDs[] and Weights[]).
     *
     * @param means the arrays of the means of the Normal Distributions in the mixture
     * @param SDs the arrays of the Standard-Deviations of the Normal Distributions in the mixture
     * @param weights the arrays of the weights of the Normal Distributions in the mixture
     */
    public double[][] getMeansSDsWeights() {
        double[][] allParameters = new double[3][means.length];
        for (int j=0; j< means.length; j++) {
        		allParameters[0][j] = means[j];
        		allParameters[1][j] = SDs[j];
        		allParameters[2][j] = weights[j];
        }
        return allParameters;
    }

    
    /**
     * This method sets up the 3 arrays of mixture parameters (Means[], SDs[] and Weights[]).
     *
     * @param means the arrays of the means of the Normal Distributions in the mixture
     * @param SDs the arrays of the Standard-Deviations of the Normal Distributions in the mixture
     * @param weights the arrays of the weights of the Normal Distributions in the mixture
     */
    public void setMeansSDsWeights(double[] _means, double[] _SDs, double[] _weights) {
        means = _means;
        SDs = _SDs;
        weights = _weights;
        probabilities = weights;
        
        int v1 = means.length;
        distributions = new Distribution[v1];
        for (int i=0; i < v1; i++)
        	distributions[i] = new NormalDistribution(means[i], SDs[i]);
     
    	setParameters(distributions, weights);
    }
    
    /**
     * This method sets up the domain of for the mixture of two distributions.
     *
     * @param d0 the index 0 distributions
     * @param d1 the index 1 distributions
     * @param a the index 1 mixing parameter (the index 0 parameter is 1 &minus;
     *        a)
     */
    public void setParameters(Distribution d0, Distribution d1, double a) {
        setParameters(new Distribution[] { d0, d1 }, new double[] { 1 - a, a });
    }

    /**
     * This method computes the density function of the mixture distributions as
     * a linear combination of the densities of the given distributions using
     * the mixing probabilities.
     *
     * @param x a number in the domain of the distributions
     * @return the probability density at x
     */
    public double getDensity(double x) {
        double d = 0;
        for (int i = 0; i < n; i++)
            d += probabilities[i] * distributions[i].getDensity(x);
        return d;
    }

    /**
     * This method computes the cumulative distributions function of the mixture
     * distributions as a linear combination of the CDFs of the given
     * distributions, using the mixing probabilities.
     */
    public double getCDF(double x) {
        double sum = 0;
        for (int i = 0; i < n; i++)
            sum += probabilities[i] * distributions[i].getCDF(x);
        return sum;
    }

    /**
     * This method computes the mean of the mixture distributions as a linear
     * combination of the means of the given distributions, using the mixing
     * probabilities.
     *
     * @return the mean
     */
    public double getMean() {
        double sum = 0;
        for (int i = 0; i < n; i++)
            sum += probabilities[i] * distributions[i].getMean();
        return sum;
    }

    /**
     * This method computes the variance of the mixture distributions in terms
     * of the variances and means of the given distributions and the mixing
     * parameters.
     *
     * @return the variance
     */
    public double getVariance() {
        double sum = 0, mu = getMean(), m;
        for (int i = 0; i < n; i++) {
            m = distributions[i].getMean();
            sum += probabilities[i] * (distributions[i].getVariance() + m * m);
        }
        return (sum - (mu * mu));
    }

    /**
     * This method simulates a value from the mixture distributions. This is
     * done by selecting an index at random, according to the mixing parameters,
     * and then simulating a value from the randomly chosen distributions.
     */
    public double simulate() {
        double sum = 0, p = Math.random();
        int i = 0;//-1;
        	//System.out.println("MixtureDist simulate initial p = " + p);
        	//System.out.println("MixtureDist simulate initial probabilities.length = " + probabilities.length);
        	//for (int j = 0; j < probabilities.length; j++) {
		    //System.out.println("MixtureDist simulate initial probabilities["+j+"] = " + probabilities[j]);
        	//}
        	//System.out.println("MixtureDist simulate initial probabilities = " + probabilities);
        	//while (sum < p & i < n) {
        while (sum < p && i < n) { // change to short curcuit &&. annie che. 20060720.
        	//for (int k = 0; k < n; k++) {
			//System.out.println("MixtureDist simulate while BEFORE sum = " + sum + ", p = " + p + ", i = " + i + ", n = " + n);
			//if (sum < p)
        	
			sum += probabilities[i];
			i++;
			//System.out.println("MixtureDist simulate while AFTER  sum = " + sum + ", p = " + p + ", i = " + i + ", n = " + n);
        }
        return distributions[(i-1)].simulate();
    }

    /**
     * This method sets the distributions.
     *
     * @param d the array of distributions
     */
    public void setDistributions(Distribution[] d) {
        int n = d.length;
        if (n<=0) return;
        double [] probabilities = new double [n];
        for (int i=0; i<n; i++) probabilities[i]=1.0/n;
    	setParameters(d, probabilities);
    }

    /**
     * This method sets a particular distribution.
     *
     * @param i the index
     * @param d the distribution
     */
    public void setDistributions(int i, Distribution d) {
        if (i < 0) i = 0;
        else if (i > n - 1) i = n - 1;
        distributions[i] = d;
        setParameters(distributions, probabilities);
    }

    /**
     * This method returns the array of distributions.
     *
     * @return the array of distributions
     * @uml.property name="distributions"
     */
    public Distribution[] getDistributions() {
        return distributions;
    }

    /**
     * This method returns a particular distribution.
     *
     * @param i the index
     * @return the distribution corresponding to the index
     */
    public Distribution getDistributions(int i) {
        if (i < 0) i = 0;
        else if (i > n - 1) i = n - 1;
        return distributions[i];
    }

    /**
     * This method sets the probabilities.
     *
     * @param p the array of probabilities
     */
    public void setProbabilities(double[] p) {
        setParameters(distributions, p);
    }

    /**
     * This method sets a particular probability.
     *
     * @param i the index
     * @param p the probability
     */
    public void setProbabilities(int i, double p) {
        if (i < 0) i = 0;
        else if (i > n - 1) i = n - 1;
        probabilities[i] = p;
        setParameters(distributions, probabilities);
    }

    /**
     * This method returns the array of probabilities.
     *
     * @return the array of distributions
     * @uml.property name="probabilities"
     */
    public double[] getProbabilities() {
        return probabilities;
    }

    /**
     * This method returns a particular probability .
     *
     * @param i the index
     * @return the probability corresponding to the index
     */
    public double getProbability(int i) {
        if (i < 0) i = 0;
        else if (i > n - 1) i = n - 1;
        return probabilities[i];
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_2D_PointSegmentation_EM_Mixture");
    }

}

