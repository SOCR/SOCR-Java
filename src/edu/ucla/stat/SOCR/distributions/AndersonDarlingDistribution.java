/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be mterm3e to develop and distribute
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
 * This class defines the Anderson-Darling distribution with a specifed parameter n>=1. 
 * <a href="http://www.jstatsoft.org/v09/i02/">
 * http://www.jstatsoft.org/v09/i02/</a> and
 * <a href="en.wikipedia.org/wiki/AndersonDarling_test">
 * en.wikipedia.org/wiki/AndersoDarling_test</a>
 */
public class AndersonDarlingDistribution extends Distribution {

    /**
     * @uml.property name="number of independent observations (degrees)"
     */
    protected static int n;
    protected static final int nMax = 40;	// Max allowable value of n, as the algorithm 
    										// is extremely slow for large n
    NormalDistribution ND = new NormalDistribution(0,1);
    private double maxDensity = 1.0;
    
    /**
     * This general constructor creates a new Anderson-Darling distribuiton with a
     * specified parameter number of independent observations (degrees)
     * @param n number of independent observations (degrees)
     */
    public AndersonDarlingDistribution(int n) {
        setDegrees(n);
        name = "Anderson-Darling Distribution";
    }

    public AndersonDarlingDistribution() {
        this(1);
     }


    public void initialize() {
        createValueSetter("Number of independent observations (degrees)", DISCRETE, 1,20,1);
    }

    public void valueChanged() {
        setDegrees(getValueSetter(0).getValueAsInt());
    }

    /**
     * This method sets the degrees parameter
     * @uml.property name="number of independent observations (degrees)"
     * @param n number of independent observations (degrees)
     */
    public void setDegrees(int n) {
        setParameters(n);
    }

    /**
     * This method sets the degrees parameter
     * @uml.property name="number of independent observations (degrees)"
     * @param n number of independent observations (degrees)
     */
    public void setParameters(int n) {
    	double lowerBound;
    	double upperBound;
 
    	//Correct invalid parameter
    	if (n <= 0) n=1;
        this.n = n;
        
        if (n==1) {
        	lowerBound = 0.38629436111989062;
        	upperBound = 37.816242111357;
        	maxDensity = specialCaseDensity_n_1(0.5);
         } else {
        	 lowerBound = 0.0;
        	 upperBound = nMax;
        	 if (n==2) maxDensity = 2.0;
        	 else if (n<=4) maxDensity = 1.2;
        	 else maxDensity = 1.1;
         }
    	//System.err.println("lowerBound="+lowerBound+"\tupperBound="+upperBound);
        super.setParameters(lowerBound, upperBound, 0.01*(upperBound-lowerBound), CONTINUOUS);
    }

    /**
     * This method returns the degrees parameter
     * @uml.property name="number of independent observations (degrees)"
     */
    public static int getDegrees() {
        return n;
    }

    /**
     * This method returns the degrees parameter
     * @uml.property name="number of independent observations (degrees)"
     */
    public static int getParameters() {
        return n;
    }

    /**
     * This method gets the Anderson-Darling distribution-gradient, as the
     * density is computed from the CDF
     * @param x value to assess the density at
     * @param delta delta for the gradient calculation
     */
    private double distributionGradient(double x, double delta) {
        return (getCDF(x+delta)-getCDF(x-delta))/(2.0*delta);
     }
 
    /**
     * This method gets the special case of the density function for 
     * parameter n=1
     * @param x value to assess the density constant at
     */
    protected static double specialCaseDensity_n_1(double x) {
        final double lowerBound = 0.38629436111989062;
        final double upperBound = 37.816242111357;
        if (x <= lowerBound || x >= upperBound)
           return 0.0;
        final double t = Math.exp (-x-1.0);
        return (2.0*t/Math.sqrt(1.0-4.0*t)); 
     }

    /**
     * This method computed the Anderson-Darling density function
     * @param x value to assess the density at
     */
    public double getDensity(double x) {
        if (n<=0) n=1;
        if (n==1) return specialCaseDensity_n_1(x);

         if (x >= nMax || x <= 0.0) return 0.0;
         
         final double delta = 1.0 / 100.0;
         final double D1 = distributionGradient(x, delta);
         final double D2 = distributionGradient(x, 2.0 * delta);
         final double densityValue = D1+(D1-D2)/3.0;
         
         if (densityValue <= 0.0) return 0.0;
         else return densityValue;
    }

    /** Maximum value of getDensity function
     */
    public double getMaxDensity() {
        return maxDensity;
        //fix this!
    }
    /***/

    /** Maximum value of getDensity function
     */
    public double getMedian() {
        return getCDF(0.5);
    }

    /**
     * Computes the Anderson-Darling CDF (distribution) function for default
     * parameter n==1.
     * See: http://www.jstatsoft.org/v09/i02/
     * @param x value to assess the CDF at
     */
    public double getCDF_n_1(double x) {
        final double lowerBound = 0.38629436111989062;
        final double upperBound = 37.816242111357;

        if (x <= lowerBound) return 0.0;
        else if (x >= upperBound) return 1.0;
        else return Math.sqrt(1.0-4.0*Math.exp(-x -1.0));
    }
     
    /**
     * Computes the Anderson-Darling CDF (distribution) function.
     * See: http://www.jstatsoft.org/v09/i02/
     * @param x value to assess the CDF at
     */
     public double getCDF (double x) {
         if (x <= 0) return 0.0;
         if (x >= nMax) return 1.0;
         if (n==1) return getCDF_n_1(x);
         double cdfValue = cdfTerm3(x);
         if (cdfValue <= 0.0) return 0.0;
         else return cdfValue;
      }
 
    /*
     * Private method to compute the first term 
	 * in the Anderson-Darling CDF. See: http://www.jstatsoft.org/v09/i02/
     */
    private double cdfTerm1 (double x, int j) {
        final double T = (4.0*j+1.0)*(4.0*j+1.0)*1.23370055013617/x;
        if (T > 150.0)
           return 0.0;

        double f, fnew, a, b, c, r;
        int i;
        
        a = 2.22144146907918 * Math.exp (-T) / Math.sqrt (T);

        b = 3.93740248643060 * 2.0 * ND.getCDF(-Math.sqrt (2 * T));
        r = x * 0.125;
        f = a + b * r;
        for (i = 1; i < 200; i++) {
           c = ((i - 0.5 - T) * b + T * a) / i;
           a = b;
           b = c;
           r *= x/(8 * i + 8);
           if (Math.abs (r) < 1e-40 || Math.abs (c) < 1.e-40) return f;
           fnew = f + c * r;
           if (f == fnew) return f;
           f = fnew;
        }
        return f;
     }

    /*
     * Private method to compute the second component of the CDF 
 	 * of the Anderson-Darling CDF
     */
    private double cdfTerm2(double x) {
        // avoids exponent limits; cdfTerm2(0.01)=.528e-52
    	if (x < 0.01) return 0.0; 
        int j;
        double term2, term2new, r;
        r = 1.0/x;
        term2 = r * cdfTerm1 (x, 0);
        for (j = 1; j < 100; j++) {
            r *= (0.5 - j) / j;
            term2new = term2 + (4 * j + 1) * r * cdfTerm1 (x, j);
            if (term2 == term2new) {
               return term2;
            }
            term2 = term2new;
        }
        return term2;
    }
    
    /*
     * Private method to compute the third component of the CDF 
 	 * of the Anderson-Darling CDF
     */
    private double cdfTerm3(double x) {
        double v;
        x = cdfTerm2(x);

        if (x > 0.8) {
           v = (-130.2137+(745.2337-(1705.091-(1950.646-
        		   (1116.360-255.7844*x)*x)*x)*x)*x)/n;
           return x + v;
        }
        final double C = 0.01265 + 0.1757/n;
        if (x < C) {
           v = x/C;
           v = Math.sqrt(v) * (1.0 - v)*(49 * v - 102);
           return (x + v*(0.0037/(n*n) +0.00078/n+0.00006)/n);
        }
        v = (x-C)/(0.8-C);
        v = -0.00022633 + (6.54034 - (14.6538 - (14.458 - 
        		(8.259-1.91864*v)*v)*v)*v)*v;
        return (x + v * (0.04213 + 0.01365/n)/n);
     }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/AndersonDarling_test");
    }
}

