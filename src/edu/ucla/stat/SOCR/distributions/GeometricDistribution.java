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

/**
 * The geometric distribution with parameter p. <a
 * href="http://mathworld.wolfram.com/GeometricDistribution.html">
 * http://mathworld.wolfram.com/GeometricDistribution.html </a>.
 */
public class GeometricDistribution extends NegativeBinomialDistribution {

    /**
     * General Constructor: creates a new geometric distribution with parameter
     * p
     */
    public GeometricDistribution(double p) {
        super(1, p);
    }

    /**
     * Default Constructor: creates a new geometric distribution with parameter
     * 0.5
     */
    public GeometricDistribution() {
        this(0.5);
        name = "Geometric Distribution";
    }

    /**
     * Constructor: Creates a new Geometric distribution from 
     * a series of observations by parameter estimation.
     */
    public GeometricDistribution(double[] distData) {
        paramEstimate(distData);
    }

    /**
     * Constructor: Creates a new Geometric distribution from 
     * a series of observations by parameter estimation.
     */
    public GeometricDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public void initialize() {
        createValueSetter("Success Probability", CONTINUOUS, 0, 1);
        getValueSetter(0).setValue(0.5);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        setParameters(v1);
    }

    /** Override set parameters */
    public void setParameters(double p) {
        if (p <= 0.001) { 
        	if (getProbability() < 0.001) setProbability(0.001);
        	p = getProbability();
        	
        	getValueSetter(0).setValue(p);
        }
        if (p > 1) { 
        	p = 1;
        	//getValueSetter(0).setValue(1);
        }
        //Assign parameters
        setProbability(p);
        //Set truncated values
        super.setParameters(getSuccesses(), Math.ceil(getMean() + 4 * getSD()), 1, DISCRETE);
        super.setMGFParameters(0.0,Math.log(1.0/(1.0-getProbability())),1000.0);
        super.setPGFParameters(0, Math.log(1/(1-getProbability())), 200, .05);
    }

    /** Get Parameters Method is inherited by super-class!!!
    public void setProbability() {}
    */
    
    /** 
     * Overwrites the method in distribution for estimating parameters 
	 * REF: MOM estimates according to:
	 * http://en.wikipedia.org/wiki/Geometric_distribution  
	 */
    public void paramEstimate(double[] distData) {
        double _p=0.5;
        double sMean = sampleMean(distData);
        // MOM estimates of location (a) & scale (b) parameters from sample statistics
        if (sMean!=0) _p = 1.0/sMean;
        setParameters(_p);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/GeometricDistribution.html");
    }
}

