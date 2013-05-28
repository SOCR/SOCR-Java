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
 * This class models the Zipf-Mandelbrot distribution with parameters 
 * N be the number of elements;
 * k be their rank (the value of the random-variable!);
 * w be the value of the power-exponent characterizing the distribution;
 * q be the (rank-)shift [0, \infty)
 * <a href="http://en.wikipedia.org/wiki/Zipf-Mandelbrot_law">
 * http://en.wikipedia.org/wiki/Zipf-Mandelbrot_law </a>.
 */
public class ZipfMandelbrotDistribution extends Distribution {

    /**
     * @uml.property name="populationSize"
     */
    private int populationSize; // B

    /**
     * @uml.property name="power"
     */
    private double power;

    /**
     * @uml.property name="shift"
     */
    private int shift;

    // Normalizing constant
    double c;

    /**
     * General constructor: creates a new  ZipfMandelbrot distribution with
     * specified values of the parameters: 
     * B(population size), w (number of special-type 1 objects) and b (sample size)
     */
    public  ZipfMandelbrotDistribution(int p, double w, int s) {
        setParameters(p, w, s);
        name = " Zipf-Mandelbrot Distribution";
    }

    /**
     * Default constructor: creates a new  ZipfMandelbrot distribution with
     * parameters B = 100, w = 50, b = 10
     */
    public  ZipfMandelbrotDistribution() {
        this(100, 2.0, 0);
        name = "Zipf-Mandelbrot Distribution";
    }

    public void initialize() {
        createValueSetter("Population-Size", DISCRETE, 0, 1000, 100);
        createValueSetter("Power Exponent", CONTINUOUS, 0, 10, 2);
        createValueSetter("Shift", DISCRETE, 0, 100, 0);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        double v2 = getValueSetter(1).getValue();
        int v3 = getValueSetter(2).getValueAsInt();
        setParameters(v1, v2, v3);
    }

    /** Set the parameters of the distribution */
    public void setParameters(int p, double w, int s) {
        //Correct for invalid parameters
        if (p < 1) p = 100;
        if (w <= 0) w = 2.0;
        if (s < 0) s = 0;

        //Assign parameter values
        populationSize = p;
        power = w;
        shift = s;
        
        // Compute the normalizing constant (Using the harmonic number function
        // http://en.wikipedia.org/wiki/Zipf-Mandelbrot_law
        c = getHarmonicNumber(populationSize, power, shift);
        
        super.setParameters(1, populationSize, 1, DISCRETE);
    }

    /** 
     * Compute the Harmonic number 
     * http://en.wikipedia.org/wiki/Zipf-Mandelbrot_law
     * parametersL sample-size, power, shift
     */
    public double getHarmonicNumber(int ss, double p, int s) {
        double result =0.0;
        for (int i=1; i<=ss; i++)
        	result += 1.0/Math.pow(i+s, p);
        return result;
    }

    /** Density function 
     * http://planetmath.org/encyclopedia/ ZipfMandelbrotDistribution.html 
     * */
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        if (1<=k && k <= populationSize) return 1.0 / (c*Math.pow(k+shift, power));
        else return 0.0;
    }

    /** Mean */
    public double getMean() {
        return (getHarmonicNumber(populationSize, power-1, shift)/
        		getHarmonicNumber(populationSize, power, shift) - shift);
    }

    /** Mode */
    public double getMode() {
        return (1.0);
    }

    /** Set population size */
    public void setPopulationSize(int ss) {
        setParameters(ss, power, shift);
    }

    /**
     * Get population size
     * @uml.property name="populationSize"
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /** Set sub-population size */
    public void setPower(double w) {
        setParameters(populationSize, w, shift);
    }

    /**
     * Get sub-population size
     * @uml.property name="power"
     */
    public double getPower() {
        return power;
    }

    /** Set Shift */
    public void setShift(int s) {
        setParameters(populationSize, power, s);
    }

    /**
     * Get Shift
     * @uml.property name="shift"
     */
    public int getShifte() {
        return shift;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://en.wikipedia.org/wiki/Zipf-Mandelbrot_law");
    }
}

