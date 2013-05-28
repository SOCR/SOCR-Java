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

/**
 * A Java implementation of the PowerFunction distribution with specified Location, Scale and
 * Shape parameters <a href="http://www.mathwave.com/articles/power_function_distribution.html">
 * http://www.mathwave.com/articles/power_function_distribution.html</a>. Formulas according to:
 * http://www.wiley.com/WileyCDA/WileyTitle/productCd-0471371246,descCd-tableOfContents.html 
 */
public class PowerFunctionDistribution extends Distribution {

    /**
     * @uml.property name="Scale"
     */
    //Parameters
    private double scale;

    /**
     * @uml.property name="Shape"
     */
    //Parameters
    private double shape;

    /**
     * Default constructor: creates a Power-Function distribution with scale and shape
     * parameters equal to 1
     */
    public PowerFunctionDistribution() {
        name = "PowerFunction Distribution";
    }

    public PowerFunctionDistribution(double a, double b) {
        setParameters(a, b);
    }

    public PowerFunctionDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public PowerFunctionDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }


    public void initialize() {
        createValueSetter("Shape", CONTINUOUS, 0, 10, 1);
        createValueSetter("Scale", CONTINUOUS, 0, 10, 1);
        setParameters(1, 1);
    }

    public void valueChanged() {
        setParameters(getValueSetter(0).getValue(), getValueSetter(1).getValue());
    }

    /**
     * Set the parameters, compute the normalizing constant NormalizingConst, and specifies the
     * interval and partition
     */
    public void setParameters(double a, double b) {
        double lower, upper;
        //Correct parameters that are out of bounds
        if (a<=0) a = 1;
	  if (b<=0) b = 1;
        //Assign parameters
        shape = a;
	  scale = b;
        //Specifiy the interval and partiton
        upper = scale;
	  lower = 0;
	super.setParameters(lower, upper, 0.01, CONTINUOUS);
    }

    /** Sets the Scale parameter */
    public void setScale(double b) {
        if (b>0) setParameters(shape, b);
	  else setParameters(shape, 1.0);
    }

    /** Sets the Shape parameter */
    public void setShape(double a) {
        setParameters(a, scale);
    }

    /**
     * Get the scale paramter
     * @uml.property name="scale"
     */
    public double getScale() {
        return scale;
    }
    /**
     * Get the shape parameter
     * @uml.property name="shape"
     */
    public double getShape() {
        return shape;
    }

    /** Define the PowerFunction getDensity function */
    public double getDensity(double x) {
        if (x<0 || x> scale) return 0;
        else return shape*Math.pow(x, shape-1)/Math.pow(scale, shape);
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return getDensity(getMode());
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return scale*shape/(shape+1);
    }

    /** Compute the Mode in closed form */
    public double getMode() {
    	  if (shape>1) return scale;
	  else return 0.001;
    }

    /** overwrites the method in distribution for estimating parameters 
	* By assuming that the shape parameter is known, the location and scale parameters could
	* be easily obtained by using the maximum likelihood estimation method. The estimate of
	* the shape parameter p is an open problem, so far. See this paper for an idea of how to implement a
 	* numerical scheme for estimation of the Shape parameter:
	* http://www.jstatsoft.org/v12/i04/v12i04.pdf#search=%22%22exponential%20power%20distribution%22%20estimate%20%22shape%22%22 
	*/
    public void paramEstimate(double[] distData) {
        double scale, shape;
        double sMean = sampleMean(distData);
        // MOM estimates of scale and shape from sample statistics
	  int size = distData.length;
	  double max = distData[0];
	  for (int i = 0; i<size; i++)
	  {	if (max<distData[i]) max = distData[i];	}
	  scale = max;
	  shape = sMean/(1-sMean);	
        setParameters(shape, scale);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return scale*scale*shape/((shape+2)*(shape+1)*(shape+1));
    }


    /** Compute the variance in closed form */
    public double getSD() {
        return Math.sqrt(getVariance());
    }

    /**
     * Compute the cumulative distribution function. The CDF is NOT known in closed form!!!
     */
    public double getCDF(double x) {
        return Math.pow(x/scale, shape);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://www.mathwave.com/articles/power_function_distribution.html");
    }

}
