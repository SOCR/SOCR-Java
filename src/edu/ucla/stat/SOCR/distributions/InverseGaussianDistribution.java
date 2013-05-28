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

import JSci.physics.particles.Lambda;
import edu.ucla.stat.SOCR.core.*;

//import JSci.maths.statistics.InverseGaussianDistribution;
/**
 * This class encapsulates the normal distribution with specified (mean, SD)
 * parameters. <a href="http://en.wikipedia.org/wiki/Inverse_Gaussian_distribution">
 * http://en.wikipedia.org/wiki/Inverse_Gaussian_distribution </a>.
 */
public class InverseGaussianDistribution extends Distribution {
    //Paramters
    public final static double NormConst = 1/Math.sqrt(2 * Math.PI); 
    // Inverse-Gaussian (Wald) distribution normalization constant

    /**
     * @uml.property name="mu"
     */
    private double mu;

    /**
     * @uml.property name="lambda"
     */
    private double lambda;

    /**
     * Default constructor creates a new Wald distribution with specified
     * parameter values
     */
    public InverseGaussianDistribution() {
    	this(1.0, 1.0);
    }

    /**
     * This general constructor creates a new Wald distribution with specified
     * parameter values
     */
    public InverseGaussianDistribution(double m, double l) {
        setParameters(m, l);
    }

    public InverseGaussianDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public InverseGaussianDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);

    }

    /* Three constructors with one more boolean variable as parameter to add
    initialized() call. This is done to distinguish from those three above to
    prevent break any call to the above constructors. The purpose was mainly solving modeler.MixedFit_Modeler and util.normalMixture's  initialize() not being called. annie che 200605. */
    public InverseGaussianDistribution(double mu, double sigma, boolean calledByModeler) {
		initialize();
		setParameters(mu, sigma);
    }

    public InverseGaussianDistribution(double[] distData, boolean calledByModeler) {
		initialize();
		paramEstimate(distData);

    }
    public InverseGaussianDistribution(float[] distData, boolean calledByModeler) {
		initialize();
		double[] distDat = new double[distData.length];
		for (int i = 0; i < distData.length; i++)
			distDat[i] = (double) distData[i];
		paramEstimate(distDat);

    }

    public void initialize() { // this is not called anywhere in the SOCR code???
        createValueSetter("Mean", CONTINUOUS, 0, 200);
        createValueSetter("Shape", CONTINUOUS, 0, 100);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    public void paramEstimate(double[] distData) {
        double mn = getMean(distData);
        double var = getVariance(distData);
        setParameters(mn, Math.pow(mn, 3)/var);
        //MOM estimates from here: http://en.wikipedia.org/wiki/Inverse_Gaussian_distribution
    }

    /** This method sets the parameters */
    public void setParameters(double m, double l) { // m = mean; l = shape

		double lower, upper, width;
		//Correct for invalid Lambda
		if (l <= 0)
		{	l = 1;
			try {
				ValueSetter valueSetter = getValueSetter(1);
				getValueSetter(1).setValue(l); 
			} catch (Exception e) {
			}
		}
		else {
			try {
				ValueSetter valueSetter = getValueSetter(1);
				//System.out.println("InverseGaussianDistribution setParameters s > 0 valueSetter = " + valueSetter);
			} catch (Exception e) {
				//System.out.println("+++++++++++++++++>InverseGaussianDistribution setParameters s > 0 valueSetter EXCEPTION = " + e.toString());
			}
		}

		//Correct for invalid Mu
		if (m <= 0) 
		{	m = 1;
			try {
				ValueSetter valueSetter = getValueSetter(0);
				getValueSetter(0).setValue(m); 
			} catch (Exception e) {
			}
		}
		else {
			try {
				ValueSetter valueSetter = getValueSetter(0);
			} catch (Exception e) {
			}
		}

		mu = m;
		lambda = l;
		upper = mu + 6 * getSD();
		lower = 0.0;
		width = (upper - lower) / 100;
		super.setParameters(lower, upper, width, CONTINUOUS);
		name = "InverseGaussian ("+mu+", "+lambda+") Distribution";
    }

    /** This method defines the getDensity function */
    public double getDensity(double x) {
        if (x<=0) return 0;
        else return NormConst*Math.sqrt(lambda/(Math.pow(x,3)))*
        	Math.exp(Math.pow(x-mu,2)*(-lambda)/(2*mu*mu*x));
    }

    /** This method returns the maximum value of the getDensity function --
    public double getMaxDensity() {
        return getDensity(mu);
    }
    ****/

    /** These methods return the mean */
    public double getMean() {
        return mu;
    }

    /** These methods return the variance */
    public double getVariance() {
        return Math.pow(mu,3)/lambda;
    }

    /**
     * This method returns the location parameter
     *
     * @uml.property name="mu"
     */
    public double getMu() {
        return mu;
    }

    /** This method sets the location parameter */
    public void setMu(double m) {
        setParameters(m, lambda);
    }

    /**
     * This method gets the scale parameter
     *
     * @uml.property name="lambda"
     */
    public double getLambda() {
        return lambda;
    }

    /** This method sets the shape parameter */
    public void setLambda(double l) {
        setParameters(mu, l);
    }

    /** This method computes the cumulative distribution function -- This is known in closed-form
     * http://en.wikipedia.org/wiki/Inverse_Gaussian_distribution
     */
    public double getCDF(double x) {
        NormalDistribution ND = new NormalDistribution(0,1);
        return ND.getCDF((x/mu - 1)*Math.sqrt(lambda/x)) +
        		Math.exp(2*lambda/mu)*ND.getCDF(-(x/mu + 1)*Math.sqrt(lambda/x));
    }

    /**
     * Inverse of the cumulative InverseGaussian distribution function.
     * @return the value X for which P(x&lt;X).
     */
     public double inverseCDF(double probability) {
		if (0<=probability && probability <=1)
               return findRoot(probability, -1.0, -3.0, 3.0); 
		   // Distribution.findRoot(double prob, double guess, double xLo, double xHi); 
	 	else if (probability <0) return 0;
		else return 1.0;  // (1 < probability)
     }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Inverse_Gaussian_distribution");
    }
}

