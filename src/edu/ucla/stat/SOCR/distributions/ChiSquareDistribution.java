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
 * This class defines the chi-square distribution with a specifed degrees of
 * freedom. <a href="http://mathworld.wolfram.com/Chi-SquaredDistribution.html">
 * http://mathworld.wolfram.com/Chi-SquaredDistribution.html </a>.
 */
public class ChiSquareDistribution extends GammaDistribution {

    /**
     * @uml.property name="degrees"
     */
    int degrees;

    /**
     * This general constructor creates a new chi-square distribuiton with a
     * specified degrees of freedom parameter
     */
    public ChiSquareDistribution(int n) {
        setDegrees(n);
    }

    public ChiSquareDistribution() {
        this(1);
        name = "Chi-Square Distribution";
    }

    public ChiSquareDistribution(double[] distData) {
        paramEstimate(distData);
    }
    public ChiSquareDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public void initialize() {
        createValueSetter("Degrees of Freedom", DISCRETE, 1, 20,1);
    }

    public void valueChanged() {
        setDegrees(getValueSetter(0).getValueAsInt());
    }

    /**
     * This method sets the degrees of freedom
     * 
     * @uml.property name="degrees"
     */
    public void setDegrees(int n) {
        //Correct invalid parameter
        if (n <= 0) n = 1;
        degrees = n;
        super.setParameters(0.5 * degrees, 2);
    }

    /**
     * This method returns the degrees of freedom
     * 
     * @uml.property name="degrees"
     */
    public int getDegrees() {
        return degrees;
    }

    /**
     * This method simulates a value from the distribuiton, as the sum of
     * squares of independent, standard normal distribution
     */
    public double simulate() {
        double V, Z, r, theta;
        V = 0;
        for (int i = 1; i <= degrees; i++) {
            r = Math.sqrt(-2 * Math.log(Math.random()));
            theta = 2 * Math.PI * Math.random();
            Z = r * Math.cos(theta);
            V = V + Z * Z;
        }
        return V;
    }

    /**
     * @uml.property name="shape"
     */
    public void paramEstimate(double[] distData) {
        double sMean = sampleMean(distData);
        setDegrees( (int)(sMean));
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/Chi-SquaredDistribution.html");
    }

}

