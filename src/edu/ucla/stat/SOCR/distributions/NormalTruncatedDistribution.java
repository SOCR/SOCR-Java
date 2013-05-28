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

import edu.ucla.stat.SOCR.core.*;

//import JSci.maths.statistics.NormalTruncatedDistribution;
/**
 * This class encapsulates the Truncated Normal distribution with specified parameters:
 * (mean, SD, leftSupportLimit, rightSupportLimit)
 * <a href="http://en.wikipedia.org/wiki/Truncated_normal_distribution">
 * http://en.wikipedia.org/wiki/Truncated_normal_distribution </a>.
 */
public class NormalTruncatedDistribution extends Distribution {
    //Paramters
    public final static double C = Math.sqrt(2 * Math.PI); // normal constant

    /**
     * @uml.property name="mu"
     */
    private double mu;

    /**
     * @uml.property name="sigma"
     */
    private double sigma;
    private double cSigma;

    /**
     * @uml.property name="leftSupportLimit"
     */
    private double leftSupportLimit;

    /**
     * @uml.property name="rightSupportLimit"
     */
    private double rightSupportLimit;

    NormalDistribution ND = new NormalDistribution(0, 1); // this distribution is needed for calculations later on.
    
    /**
     * This general constructor creates a new normal distribution with specified
     * parameter values
     */
    public NormalTruncatedDistribution(double mu, double sigma, double _left, double _right) {
        setParameters(mu, sigma, _left, _right);
    }

    public NormalTruncatedDistribution(double[] distData) {
        paramEstimate(distData);

    }

    public NormalTruncatedDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);

    }

    /* Three constructors with one more boolean variable as parameter to add
    initialized() call. This is done to distinguish from those three above to
    prevent break any call to the above constructors. The purpose was mainly solving modeler.MixedFit_Modeler and util.normalMixture's  initialize() not being called. annie che 200605. */
    public NormalTruncatedDistribution(double mu, double sigma, double _left, double _right, boolean calledByModeler) {
		initialize();
		setParameters(mu, sigma, _left, _right);
    }

    public NormalTruncatedDistribution(double[] distData, boolean calledByModeler) {
		initialize();
		paramEstimate(distData);

    }
    public NormalTruncatedDistribution(float[] distData, boolean calledByModeler) {
		initialize();
		double[] distDat = new double[distData.length];
		for (int i = 0; i < distData.length; i++)
			distDat[i] = (double) distData[i];
		paramEstimate(distDat);

    }
    /** This default constructor creates a new standard normal distribution */
    public NormalTruncatedDistribution() {
        this(0, 1, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public void initialize() { // this is not called anywhere in the SOCR code???
        createValueSetter("Mean", CONTINUOUS, -200, 200);
        createValueSetter("Standard Deviation", CONTINUOUS, 0, 100, 1);
        createValueSetter("Left Support Limit", CONTINUOUS, -100, 100, -10);
        createValueSetter("Right Support Limit", CONTINUOUS, -100, 100, 10);
    }


    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        double v4 = getValueSetter(3).getValue();
        setParameters(v1, v2, v3, v4);
    }

    public void paramEstimate(double[] distData) {
        double mn = getMean(distData);
        double std = Math.sqrt(getVariance(distData));
        double min=-mn, max=mn;
        if (distData.length > 0) {
        	min=distData[0];
        	max=distData[0];
        	for (int i=0; i<distData.length; i++) {
        		if (min>distData[i]) min = distData[i];
        		if (max<distData[i]) max = distData[i];
        	}
        }
        setParameters(mn, std, min, max);
    }

    /** This method sets the parameters */
    public void setParameters(double m, double s, double _left, double _right) { 
    	// m = mean, s = STD, _left = leftSupportLimit, _right = rightSupportLimit

		double lower, upper, width;
		//Correct for invalid sigma
		if (s <= 0) // if constant data. then STD is 0.
		{	s = 1;
			try {
				ValueSetter valueSetter = getValueSetter(1); // why use 1? Because the second parameter (index=1) is s=SD.
				//System.out.println("NormalTruncatedDistribution setParameters s <= 0 valueSetter = " + valueSetter);
				getValueSetter(1).setValue(1); // why use 1 in method?
				/* getValueSetter method is in super^2 SOCRValueSettable
				setValue is in class VelueSetter. Looks like it's trying
				to set something to the variance 1. ??? */
			} catch (Exception e) {
				//System.out.println("------------------->NormalTruncatedDistribution setParameters s <= 0 valueSetter EXCEPTION = " + e.toString());
			}
		}
		else {
			try {
				ValueSetter valueSetter = getValueSetter(1); // why use 1?
				//System.out.println("NormalTruncatedDistribution setParameters s > 0 valueSetter = " + valueSetter);
			} catch (Exception e) {
				//System.out.println("+++++++++++++++++>NormalTruncatedDistribution setParameters s > 0 valueSetter EXCEPTION = " + e.toString());
			}
		}
		
		if (_left >= _right) // limits are impossible
		{	_right = _left +1;
			try {
				ValueSetter valueSetter2 = getValueSetter(2); 
				ValueSetter valueSetter3 = getValueSetter(3); 
				getValueSetter(2).setValue(_left); getValueSetter(2).repaint();
				getValueSetter(3).setValue(_right); getValueSetter(3).repaint();
			} catch (Exception e) {
				System.out.println("------------------->NormalTruncatedDistribution setParameters valueSetter EXCEPTION = " + e.toString());
			}
		}
		
		mu = m;
		sigma = s;
		leftSupportLimit = _left;
		rightSupportLimit = _right;
		
		cSigma = C * sigma;
		
		upper = rightSupportLimit;
		lower = leftSupportLimit;
		width = (upper - lower) / 10000;
		
		name = "Normal Truncated ("+mu+", "+sigma+", "+leftSupportLimit+", "+rightSupportLimit+
				") Distribution";
		
		super.setParameters(lower, upper, width, CONTINUOUS);
    }

    /** This method defines the getDensity function */
    public double getDensity(double x) {
        double z = (x - mu) / sigma;
        double density = 0;
        if (x<leftSupportLimit || x>rightSupportLimit) density = 0;
        else {
        	density = ND.getDensity(z)/(sigma*
        		(ND.getCDF((rightSupportLimit-mu)/sigma) - ND.getCDF((leftSupportLimit-mu)/sigma)));
        }
        if (density <0) density = 0;
        return density;
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        double max = getDensity(leftSupportLimit);
        if (max < getDensity(rightSupportLimit)) 
        	max=getDensity(rightSupportLimit);
        if (leftSupportLimit <= mu && mu <= rightSupportLimit) 
        	max=getDensity(mu);
    	return max;
    }

    /** These methods return the mean */
    public double getMean() {
        return (mu + sigma * ( ND.getDensity((leftSupportLimit-mu)/sigma)- 
        		ND.getDensity((rightSupportLimit-mu)/sigma))/
        		(ND.getCDF((rightSupportLimit-mu)/sigma)- 
                		ND.getCDF((leftSupportLimit-mu)/sigma)));
    }

    /** These methods return the variance */
    public double getVariance() {
    	return (sigma*sigma * ( 1+
    			(((leftSupportLimit-mu)/sigma)*ND.getDensity((leftSupportLimit-mu)/sigma)- 
    			((rightSupportLimit-mu)/sigma)*ND.getDensity((rightSupportLimit-mu)/sigma))/
        		(ND.getCDF((rightSupportLimit-mu)/sigma)- 
                		ND.getCDF((leftSupportLimit-mu)/sigma))) -
                Math.pow( (ND.getDensity((leftSupportLimit-mu)/sigma)- 
            			ND.getDensity((rightSupportLimit-mu)/sigma))/
                		(ND.getCDF((rightSupportLimit-mu)/sigma)- 
                        		ND.getCDF((leftSupportLimit-mu)/sigma))
                		,2));
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
        setParameters(m, sigma, leftSupportLimit, rightSupportLimit);
    }

    /**
     * This method gets the scale parameter
     *
     * @uml.property name="sigma"
     */
    public double getSigma() {
        return sigma;
    }

    /** This method sets the scale parameter */
    public void setSigma(double s) {
        setParameters(mu, s, leftSupportLimit, rightSupportLimit);
    }

    /**
     * This method gets the leftSupportLimit parameter
     * @uml.property name="leftSupportLimit"
     */
    public double getLeftSupportLimit() {
        return leftSupportLimit;
    }

    /** This method sets the leftSupportLimit parameter */
    public void setLeftSupportLimit(double left) {
        setParameters(mu, sigma, left, rightSupportLimit);
    }

    /**
     * This method gets the rightSupportLimit parameter
     * @uml.property name="rightSupportLimit"
     */
    public double getRightSupportLimit() {
        return rightSupportLimit;
    }

    /** This method sets the rightSupportLimit parameter */
    public void setRightSupportLimit(double right) {
        setParameters(mu, sigma, leftSupportLimit, right);
    }
    
    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Truncated_normal_distribution");
    }
}

