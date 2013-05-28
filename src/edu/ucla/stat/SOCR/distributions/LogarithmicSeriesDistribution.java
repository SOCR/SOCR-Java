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
import edu.ucla.stat.SOCR.distributions.exception.ParameterOutOfBoundsException;

/**
 * A Java implmentation of the LogarithmicSeries distribution with specified 
 * shape (shape) parameters <a href="http://en.wikipedia.org/wiki/Logarithmic_distribution">
 * http://en.wikipedia.org/wiki/Logarithmic_distribution </a>.
 */
public class LogarithmicSeriesDistribution extends Distribution {

    /**
     * @uml.property name="shape"
     */
    //Parameters
    private double shape;

    //Constant scaling parameter
    private double NormalizingConst;

    /**
     * Default constructor: creates a default Logarithmic Series distribution
     */
    public LogarithmicSeriesDistribution() {
        name = "LogarithmicSeries Distribution";
    }

    public LogarithmicSeriesDistribution(double a) {
        setParameters(a);
    }

    public LogarithmicSeriesDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public LogarithmicSeriesDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }


    public void initialize() {
        createValueSetter("Shape", CONTINUOUS, 0, 1, 0);
        setParameters(0.5);
    }

    public void valueChanged() {
        setParameters(getValueSetter(0).getValue());
    }

    /**
     * Set the parameters, compute the normalizing constant NormalizingConst, and specifies the
     * interval and partition
     */
    public void setParameters(double a) {
        double lower=1, upper;
        //Correct parameters that are out of bounds
        if (a<0 || a>1) a = 0.5;
        shape = a;
        upper=3/(1.1-shape);
        //Compute the normalizing constant
        NormalizingConst = -1/(Math.log(1-shape));
        //Specifiy the interval and partiton
        super.setParameters(lower, (int)(upper+0.5), 1, DISCRETE);
        
        // based on the shape parameter and some interpolation I found that the MGF bounds 
        // should be equal to (1/-1.246)*ln(shape/.994)
        double upperDomain = 1.0;
        
        //  avoid division by zero
        if (shape != 1.0)  upperDomain = Math.log(shape/.994)/-1.246;
        super.setMGFParameters(0,upperDomain,20,upperDomain/2.0);
        super.setPGFParameters(0, 1.0/shape,20);
    }

    /** Sets the shape parameter */
    public void setShape(double a) {
        if (a>=0 && a <=1) setParameters(a);
        else setParameters(0.5);
    }

    /**
     * Get the shape parameter
     *
     * @uml.property name="shape"
     */
    public double getShape() {
        return shape;
    }

    /** Define the LogarithmicSeries getDensity function */
    public double getDensity(double x) {
        if (x<1) return 0;
        else return (Math.pow(shape, (int)(x)) * NormalizingConst/(int)(x));
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return getDensity(getMode());
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return NormalizingConst*shape*(1-shape);
    }

    /** Compute the mean in closed form */
    public double getMode() {
        return 1.0;
    }

    /** overwrites the method in distribution for estimating parameters */
    public void paramEstimate(double[] distData) {
        double deno=0, shape;
        double sMean = sampleMean(distData);
	  for (int i=0; i<distData.length; i++)
		deno += distData[i]*distData[i];
	  shape = 1-deno/(distData.length*sMean);
        setParameters(shape);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return NormalizingConst*shape*(1-NormalizingConst*shape)/((1-shape)*(1-shape));
    }


    /** Compute the variance in closed form */
    public double getSD() {
        return Math.sqrt(getVariance());
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) throws ParameterOutOfBoundsException
    {
    	if (shape == 1.0)
    		return 0; // for purposes of graphing on the graphPanel.  Shape of 1 fails, but slider has 1.0 as a scale option
    	else
    	{
    		if (shape <= 0.0 || shape >= 1.0)
    			throw new ParameterOutOfBoundsException("Shape Parameter must be between 0 and 1");
    		else	
    			return Math.log(1-shape*Math.exp(t))/Math.log(1-shape);
    	}
    }
    
    /** Computes the probability generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getPGF(double t)
    {
    		if (shape == 1.0)
    			return 0;
    		else
    			return Math.log(1-shape*t)/Math.log(1-shape);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Logarithmic_distribution");
    }

}
