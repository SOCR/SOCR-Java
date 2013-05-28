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
 * This class models the HyperGeometric distribution with parameters m
 * (population size), n (sample size), and r (number of type 1 objects). <a
 * href="http://mathworld.wolfram.com/HypergeometricDistribution.html">
 * http://mathworld.wolfram.com/HypergeometricDistribution.html </a>.
 */
public class HypergeometricDistribution extends Distribution {

    /**
     * @uml.property name="populationSize"
     */
    private int populationSize;

    /**
     * @uml.property name="sampleSize"
     */
    private int sampleSize;

    /**
     * @uml.property name="type1Size"
     */
    private int type1Size;

    double c;	// normalizing constant!

    /**
     * General constructor: creates a new hypergeometric distribution with
     * specified values of the parameters
     */
    public HypergeometricDistribution(int m, int r, int n) {
        setParameters(m, r, n);
    }

    /**
     * Default constructor: creates a new hypergeometric distribuiton with
     * parameters m = 100, r = 50, n = 10
     */
    public HypergeometricDistribution() {
        this(100, 50, 10);
        name = "HyperGeometric Distribution";

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
        	// April 16, 2008! 
        	// Ivo changed this as the sample-size (n) and the number-of-red (r) were swapped!!
        	// prototype: setParameters(population-size, number-of-red, sample-size);
        setParameters(v1, v2, v3);
    }

    /** Set the parameters of the distribution */
    public void setParameters(int m, int r, int n) {
        //Correct for invalid parameters
        if (m < 1) m = 1;
        if (r < 0) r = 0;
        else if (r > m) r = m;
        if (n < 0) n = 0;
        else if (n>115) n=100;	//calculations fail for larger sample-sizes
        else if (n > m) n = m;
        
        //Assign parameter values
        populationSize = m;
        type1Size = r;
        sampleSize = n;
        
        /******
        	try {
        		getValueSetter(0).setValue(populationSize);
            	getValueSetter(1).setValue(type1Size);
            	getValueSetter(2).setValue(sampleSize);
        	} catch (Exception e) {
        		System.out.println("Error = "+e);
        	}
        *************/
        
        c = comb(populationSize, sampleSize);
        super.setParameters(Math.max(0, sampleSize - populationSize + type1Size),
                Math.min(type1Size, sampleSize), 1, DISCRETE);
    }

    /** Density function */
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        return comb(type1Size, k)
                * comb(populationSize - type1Size, sampleSize - k) / c;
    }

    /** Maximum value of the getDensity function */
    public double getMaxDensity() {
        double mode = Math.floor(((double) (sampleSize + 1) * (type1Size + 1))
                / (populationSize + 2));
        return getDensity(mode);
    }

    /** Mean */
    public double getMean() {
        return (double) sampleSize * type1Size / populationSize;
    }

    /** Variance */
    public double getVariance() {
        return (double) sampleSize * type1Size * (populationSize - type1Size)
                * (populationSize - sampleSize)
                / (populationSize * populationSize * (populationSize - 1));
    }

    /** Set population size */
    public void setPopulationSize(int m) {
        setParameters(m, type1Size, sampleSize);
    }

    /**
     * Get population size
     * 
     * @uml.property name="populationSize"
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /** Set sub-population size */
    public void setType1Size(int r) {
        setParameters(populationSize, r, sampleSize);
    }

    /**
     * Get sub-population size
     * 
     * @uml.property name="type1Size"
     */
    public int getType1Size() {
        return type1Size;
    }

    /** Set sample size */
    public void setSampleSize(int n) {
        setParameters(populationSize, type1Size, n);
    }

    /**
     * Get sample size
     * 
     * @uml.property name="sampleSize"
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /** Simulate a value from the distribution */
    public double simulate() {
        int j, k, u, m0;
        double x = 0;
        m0 = (int) populationSize;
        int[] b = new int[m0];
        for (int i = 0; i < m0; i++)
            b[i] = i;
        for (int i = 0; i < sampleSize; i++) {
            k = m0 - i;
            u = (int) (k * Math.random());
            if (u < type1Size) x = x + 1;
            j = b[k - 1];
            b[k - 1] = b[u];
            b[u] = j;
        }
        return x;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://mathworld.wolfram.com/HypergeometricDistribution.html");
    }

}

