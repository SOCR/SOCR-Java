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
 * This class models the NegativeHypergeometric distribution with parameters B
 * (population size), b (sample size), and w (number of special-type 1 objects).
 * <a href="http://planetmath.org/encyclopedia/NegativeHypergeometricDistribution.html">
 * http://planetmath.org/encyclopedia/NegativeHypergeometricDistribution.html </a>.
 */
public class NegativeHypergeometricDistribution extends Distribution {

    /**
     * @uml.property name="populationSize"
     */
    private int populationSize; // B

    /**
     * @uml.property name="sampleSize"
     */
    private int sampleSize;		// b

    /**
     * @uml.property name="type1Size"
     */
    private int type1Size;

    double c;

    /**
     * General constructor: creates a new NegativeHypergeometric distribution with
     * specified values of the parameters: 
     * B(population size), w (number of special-type 1 objects) and b (sample size)
     */
    public NegativeHypergeometricDistribution(int B, int w, int b) {
        setParameters(B, w, b);
    }

    /**
     * Default constructor: creates a new NegativeHypergeometric distribution with
     * parameters B = 100, w = 50, b = 10
     */
    public NegativeHypergeometricDistribution() {
        this(100, 50, 10);
        name = "NegativeHypergeometric Distribution";
    }

    public void initialize() {
        createValueSetter("Population-Size", DISCRETE, 0, 1000);
        createValueSetter("NumberOfGoodObjects", DISCRETE, 0, 100);
        createValueSetter("Sample-Size", DISCRETE, 0, 100);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        int v2 = getValueSetter(1).getValueAsInt();
        int v3 = getValueSetter(2).getValueAsInt();
        setParameters(v1, v2, v3);
    }

    /** Set the parameters of the distribution */
    public void setParameters(int B, int w, int b) {
        //Correct for invalid parameters
        if (B < 1) B = 1;
        if (w < 0) w = 0;
        else if (w > B) w = B;
        if (b < 0) b = 0;
        else if (b > B) b = B;
        //Assign parameter values
        populationSize = B;
        type1Size = w;
        sampleSize = b;
        c = comb(populationSize+type1Size-1, type1Size);
        super.setParameters(Math.max(0, sampleSize - populationSize + type1Size),
                Math.min(type1Size, sampleSize), 1, DISCRETE);
    }

    /** Density function 
     * http://planetmath.org/encyclopedia/NegativeHypergeometricDistribution.html 
     * */
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        return comb(sampleSize+k-1, k)
                * comb(populationSize - sampleSize + type1Size - k -1, type1Size - k) / c;
    }

    /** Mean */
    public double getMean() {
        return (double) sampleSize * type1Size / (populationSize+1);
    }

    /** Variance */
    public double getVariance() {
        return (double) sampleSize * type1Size * (populationSize - sampleSize +1)
                * (populationSize + type1Size +1)
                / ( (populationSize + 2)*(populationSize + 1)*(populationSize + 1));
    }

    /** Set population size */
    public void setPopulationSize(int B) {
        setParameters(B, type1Size, sampleSize);
    }

    /**
     * Get population size
     * @uml.property name="populationSize"
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /** Set sub-population size */
    public void setType1Size(int w) {
        setParameters(populationSize, w, sampleSize);
    }

    /**
     * Get sub-population size
     * @uml.property name="type1Size"
     */
    public int getType1Size() {
        return type1Size;
    }

    /** Set sample size */
    public void setSampleSize(int b) {
        setParameters(populationSize, type1Size, b);
    }

    /**
     * Get sample size
     * @uml.property name="sampleSize"
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://planetmath.org/encyclopedia/NegativeHypergeometricDistribution.html");
    }
}

