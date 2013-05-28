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
 * Gompertz distribution with a specified shape parameter and scale parameter. <a
 * href="http://en.wikipedia.org/wiki/Shifted_Gompertz_distribution">
 * http://en.wikipedia.org/wiki/Shifted_Gompertz_distribution</a>.
 */
public class GompertzDistribution extends Distribution {

    /**
     * @uml.property name="shape"
     */
    //Parameters == \beta > 0
    private double shape;

    /**
     * @uml.property name="scale"
     */
    //Parameters == \nu > 0
    private double scale;

    /**
     * @uml.property name="scale"
     */
    //Parameters == upperBound for Distribution Plotting > 0
    private int upperBound;

    //Parameters = Normalization constant
    private double c;

    public GompertzDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public GompertzDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            rData[i] = (double) distData[i];

        paramEstimate(rData);
    }

    /**
     * General Constructor: creates a new Gompertz distribution with shape
     * parameter k, scale parameter b and upper-bount parameter (limiting distribution plot)
     */
    public GompertzDistribution(double beta, double nu, int upperBound) {
        setParameters(beta, nu,upperBound);
        name = "Gompertz Distribution";
    }

    /**
     * General Constructor: creates a new Gompertz distribution with shape
     * parameter k and scale parameter b
     */
    public GompertzDistribution(double beta, double nu) {
        this(beta, nu, 20);
    }

    /**
     * Default Constructor: creates a new Gompertz distribution with shape
     * parameter 1 and scale parameter 1
     */
    public GompertzDistribution() {
        this(2, 2);
    }

    public void initialize() {
        createValueSetter("Shape (beta)", CONTINUOUS, 0, 41);
        createValueSetter("Scale (nu)", CONTINUOUS, 0, 41);
        createValueSetter("Upper Bound for Distributuion Plot", CONTINUOUS, 1, 100);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        int v3 = getValueSetter(2).getValueAsInt();
        setParameters(v1, v2, v3);
    }

    /** Set parameters and assign the default partition */
    public void setParameters(double beta, double nu, int _upperBound) {
        //Correct invalid parameters
        if (beta <= 0) beta = 1;
        if (nu <= 0) nu = 1;
        shape = beta;
        scale = nu;
        //Normalizing constant
        c = shape;
        //Assign default partition:
        upperBound = _upperBound;    	//getMean() + 4 * getSD();
        super.setParameters(0, upperBound, 0.001 * upperBound, CONTINUOUS);
    }

    /**
     * Get shape parameters
     * @uml.property name="shape"
     */
    public double getShape() {
        return shape;
    }

    /**
     * Get scale parameters
     * @uml.property name="scale"
     */
    public double getScale() {
        return scale;
    }

    /** Density function */
    public double getDensity(double x) {
        if (x <= 0 || x >= upperBound) return 0;
        else return c*Math.exp(-scale*x)*Math.exp(-shape*Math.exp(-scale*x))
        		*(1+shape*(1-Math.exp(-scale*x)));
    }

    /** Maximum value of getDensity function */
    public double getMaxDensity() {
        double mode;
        if (shape <= 0.5) mode = 0.001;
        else mode = -(1.0/scale) * 
        	Math.log((3+shape-Math.sqrt(shape*shape+2*shape+5))/(2*shape));
        return getDensity(mode);
    }

    /** Cumulative distribution function */
    public double getCDF(double x) {
        if (x<=0) return 0;
        else return (1-Math.exp(-scale*x))*Math.exp(-shape*Math.exp(-scale*x));
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Shifted_Gompertz_distribution");
    }

    /**
     * @uml.property name="shape"
     -- SEE:
     http://dx.doi.org/10.1016/j.amc.2006.03.020 
     
    public void paramEstimate(double[] distData) {
        //rate = 1/sampleMean(distData);
        //setRate(rate);
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        scale = sVar / sMean;
        shape = sMean / scale;
        setParameters(scale, shape);
    }
    ****/
}

