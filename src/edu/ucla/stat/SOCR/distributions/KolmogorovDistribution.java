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
 * This class defines the Kolmogorov distribution with a specifed parameter n>=1. 
 * <a href="http://www.jstatsoft.org/v08/i18">
 * http://www.jstatsoft.org/v08/i18</a> and
 * <a href="http://en.wikipedia.org/wiki/Kolmogorov%E2%80%93Smirnov_test">
 * http://en.wikipedia.org/wiki/Kolmogorov%E2%80%93Smirnov_test</a>
 */
public class KolmogorovDistribution extends Distribution {

    /**
     * @uml.property name="number of independent observations (degrees)"
     */
    protected static int n;
    protected static final int nMax = 20;	// Max allowable value of n, as the algorithm 
    										// is extremely slow for large n

    
    /**
     * This general constructor creates a new Kolmogorov distribuiton with a
     * specified parameter number of independent observations (degrees)
     * @param n number of independent observations (degrees)
     */
    public KolmogorovDistribution(int n) {
        setDegrees(n);
        name = "Kolmogorov Distribution";
    }

    public KolmogorovDistribution() {
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
        //Correct invalid parameter
    	if(n<1) n=1;
        else if (n>nMax) n=nMax;
    	this.n=n;
    	
    	double lowerBound = 0.5/n;
    	double upperBound = 1;
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
     * This method gets the Kolmogorov distribution-gradient, as the
     * density is computed from the CDF
     * @param x value to assess the density at
     * @param delta delta for the gradient calculation
     */
    private double distributionGradient(double x, double delta) {
        return (getCDF(x+delta)-getCDF(x-delta))/(2.0*delta);
     }
 
    /**
     * This method gets the density-constant term
     * @param x value to assess the density constant at
     */
    protected static double densityConstant(double x) {
        if ((x >= 1.0) || (x <= 0.5/getDegrees()))
           return 0.0;
        if (getDegrees() == 1)
           return 2.0;

        if (x <= 1.0/getDegrees()) {
           double w;
           double t = 2.0*x - 1.0/getDegrees();
           if (n <= nMax) {
              w = 2.0*getDegrees()*factorial(getDegrees());
              w *= Math.pow (t, (double)(getDegrees() - 1));
              return w;
           }
           w = logGamma(getDegrees()) + (getDegrees()-1) * Math.log (t);
           return 2*getDegrees()*Math.exp (w);
        }

        if (x >= 1.0 - 1.0/getDegrees())
           return 2.0*getDegrees()*Math.pow (1.0-x, (double)(getDegrees()-1));

        return -1.0;
     }

    /**
     * This method computed the Kolmogorov density function
     * @param x value to assess the density at
     */
    public double getDensity(double x) {
        double densConst = densityConstant(x);
        if (densConst != -1.0)
           return densConst;

        double delta = 1.0/200.0;
        final double D1 = distributionGradient(x, delta);
        final double D2 = distributionGradient(x, 2.0*delta);
        densConst = D1 + (D1-D2)/3.0;
        if (densConst<=0.0)
           return 0.0;
        return densConst;
    }

    /** Maximum value of getDensity function --
    public double getMaxDensity() {
        return getDensity(getMode());
    }
    ****/

    /**
     * Computes the Kolmogorov distribution function.
     * See: http://www.jstatsoft.org/v08/i18
     * @param x value to assess the CDF at
     */
    public double getCDF(double x) {
       double cdfConst = cdfConst(x);
       if (cdfConst != -1.0)
          return cdfConst;

       double s;
       final boolean PREC7 = true;
       // The next 3 lines reduce the result accuracy in the right tail to 7 digits
       if (PREC7) {
          s = x*x*getDegrees();
          if (s>7.24 || (s>3.76 && getDegrees()>99))
             return 1-2*Math.exp(-(2.000071+0.331/Math.sqrt(
            		 (double)getDegrees())+1.409/getDegrees())*s);
       }

       int k, m, i, j, g, eH, eQ;
       k = (int) (getDegrees()*x) + 1;
       m = 2 * k - 1;
       double h = k - getDegrees()*x;
       double H[] = new double[m*m];
       double Q[] = new double[m*m];

       for (i = 0; i < m; i++)
          for (j = 0; j < m; j++)
             if (i - j + 1 < 0)
                H[i * m + j] = 0;
             else
                H[i * m + j] = 1;

       for (i = 0; i < m; i++) {
          H[i * m] -= Math.pow (h, (double) (i + 1));
          H[(m - 1) * m + i] -= Math.pow (h, (double) (m - i));
       }
       H[(m - 1) * m] += (2 * h - 1 > 0 ? Math.pow (2 * h - 1, (double) m) : 0);
       for (i = 0; i < m; i++)
          for (j = 0; j < m; j++)
             if (i - j + 1 > 0)
                for (g = 1; g <= i - j + 1; g++)
                   H[i * m + j] /= g;
       eH = 0;
       eQ = mPower (H, eH, Q, m, getDegrees())[0];
       s = Q[(k - 1) * m + k - 1];
       for (i = 1; i <= getDegrees(); i++) {
          s = s * i / getDegrees();
          if (s < 1.0e-140) {
             s *= 1.0e140;
             eQ -= 140;
          }
       }
       s *= Math.pow(10.0, (double)eQ);
       return s;
    }
 
    /*
     * Private method to compute the product of the power-term components
     * in the infinite series definition of the Kolmogorov CDF
     */
    private static void mMultiply (double[] A, double[] B, double[] C, int m){
        int i, j, k;
        double s;
        for (i = 0; i < m; i++)
           for (j = 0; j < m; j++) {
              s = 0.0;
              for (k = 0; k < m; k++)
                 s += A[i * m + k] * B[k * m + j];
              C[i * m + j] = s;
           }
     }

    /*
     * Private method to compute the CDF constant terms part of the
     * the infinite series definition of the Kolmogorov CDF
     */
    protected static double cdfConst (double x) {
    	   		// For nx^2>18, F_bar(x) is smaller than 5e-15
    	      if ((getDegrees()*x*x >= 18.0) || (x >= 1.0))
    	         return 1.0;

    	      if (x <= 0.5/getDegrees())
    	         return 0.0;

    	      if (getDegrees() == 1)
    	         return 2.0 * x - 1.0;

    	      if (x <= 1.0/getDegrees()) {
    	         double w;
    	         double t = 2.0*x - 1.0/getDegrees();
    	         if (getDegrees() <= nMax) {
    	            w = logGamma(getDegrees());
    	            return w * Math.pow (t, (double)getDegrees());
    	         }
    	         w = logGamma(getDegrees()) + getDegrees()*Math.log(t);
    	         return Math.exp (w);
    	      }

    	      if (x >= 1.0-1.0/getDegrees()) {
    	         return 1.0-2.0*Math.pow (1.0-x, (double)getDegrees());
    	      }
    	      return -1.0;
    }
    

    /*
     * Private method to compute the power-term in the infinite series definition of the 
     * Kolmogorov CDF
     */
    private static int[] mPower (double A[], int eA, double V[], int m, long n){
        int eV[], i;
        eV = new int [1];
        if (n == 1) {
           for (i = 0; i < m * m; i++)
            V[i] = A[i];
           eV[0] = eA;
           return eV;
        }
        eV = mPower (A, eA, V, m, n / 2);
        double B[] = new double[m*m];
        mMultiply (V, V, B, m);
        final int eB = 2 * eV[0];

        if (n % 2 == 0) {
           for (i = 0; i < m * m; i++)
              V[i] = B[i];
           eV[0] = eB;
        } else {
           mMultiply (A, B, V, m);
           eV[0] = eA + eB;
        }

        if (V[(m / 2) * m + (m / 2)] > 1.0e140) {
           for (i = 0; i < m * m; i++)
              V[i] = V[i] * 1.0e-140;
           eV[0] += 140;
        }
        return eV;
     }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Kolmogorov%E2%80%93Smirnov_test");
    }
}

