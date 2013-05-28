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

//import JSci.maths.statistics.NormalDistribution;
/**
 * This class encapsulates the normal distribution with specified (mean, SD)
 * parameters. <a href="http://mathworld.wolfram.com/NormalDistribution.html">
 * http://mathworld.wolfram.com/NormalDistribution.html </a>.
 */
public class NormalDistribution extends Distribution {
    //Paramters
    public final static double C = Math.sqrt(2 * Math.PI); // normal constant

    /**
     * @uml.property name="mu"
     */
    private double mu;

    /**
     * @uml.property name="sigma"
     */
    private double sigma;

    private double cSigma;

    /**
     * This general constructor creates a new normal distribution with specified
     * parameter values
     */
    public NormalDistribution(double mu, double sigma) {
        setParameters(mu, sigma);
    }

    public NormalDistribution(double[] distData) {
        paramEstimate(distData);

    }

    public NormalDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);

    }

    /* Three constructors with one more boolean variable as parameter to add
    initialized() call. This is done to distinguish from those three above to
    prevent break any call to the above constructors. The purpose was mainly solving modeler.MixedFit_Modeler and util.normalMixture's  initialize() not being called. annie che 200605. */
    public NormalDistribution(double mu, double sigma, boolean calledByModeler) {
		initialize();
		setParameters(mu, sigma);
    }

    public NormalDistribution(double[] distData, boolean calledByModeler) {
		initialize();
		paramEstimate(distData);

    }
    public NormalDistribution(float[] distData, boolean calledByModeler) {
		initialize();
		double[] distDat = new double[distData.length];
		for (int i = 0; i < distData.length; i++)
			distDat[i] = (double) distData[i];
		paramEstimate(distDat);

    }
    /** This default constructor creates a new standard normal distribution */
    public NormalDistribution() {
        this(0, 1);
    }

    public void initialize() { // this is not called anywhere in the SOCR code???
        createValueSetter("Mean", CONTINUOUS, -200, 200);
        createValueSetter("Standard Deviation", CONTINUOUS, 0, 100, 1);
    }


    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    public void paramEstimate(double[] distData) {
        double mn = getMean(distData);
        double std = Math.sqrt(getVariance(distData));
        setParameters(mn, std);
    }

    /** This method sets the parameters */
    public void setParameters(double m, double s) { // m = mean and s = STD.

		double lower, upper, width;
		//Correct for invalid sigma
		if (s <= 0) // if constant data. then STD is 0.
		{	s = 1;
			try {
				ValueSetter valueSetter = getValueSetter(1); // why use 1?
				//System.out.println("NormalDistribution setParameters s <= 0 valueSetter = " + valueSetter);
				getValueSetter(1).setValue(1); // why use 1 in method?
				/* getValueSetter method is in super^2 SOCRValueSettable
				setValue is in class VelueSetter. Looks like it's trying
				to set something to the variance 1. ??? */
			} catch (Exception e) {
				//System.out.println("------------------->NormalDistribution setParameters s <= 0 valueSetter EXCEPTION = " + e.toString());
			}
		}
		else {
			try {
				ValueSetter valueSetter = getValueSetter(1); // why use 1?
				//System.out.println("NormalDistribution setParameters s > 0 valueSetter = " + valueSetter);
			} catch (Exception e) {
				//System.out.println("+++++++++++++++++>NormalDistribution setParameters s > 0 valueSetter EXCEPTION = " + e.toString());
			}
		}
		mu = m;
		sigma = s;
		cSigma = C * sigma;
		upper = mu + 4 * sigma;
		lower = mu - 4 * sigma;
		width = (upper - lower) / 100;
		
		name = "Normal ("+mu+", "+sigma+") Distribution";
		
		super.setParameters(lower, upper, width, CONTINUOUS);
		super.setMGFParameters();
		
    }

    /** This method defines the getDensity function */
    public double getDensity(double x) {
        double z = (x - mu) / sigma;
        return Math.exp(-z * z / 2) / cSigma;
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return getDensity(mu);
    }

    /** This method returns the median */
    public double getMedian() {
        return mu;
    }

    /** These methods return the mean */
    public double getMean() {
        return mu;
    }

    /** These methods return the variance */
    public double getVariance() {
        return sigma * sigma;
    }

    /** This method simulates a value from the distribution */
    public double simulate() {
        double r = Math.sqrt(-2 * Math.log(Math.random()));
        double theta = 2 * Math.PI * Math.random();
        return mu + sigma * r * Math.cos(theta);
    }

    /**
     * This method returns the location parameter
     *
     * @uml.property name="mu"
     */
    public double getMu() {
        return mu;
    }

    /** This method sets the location parameter */
    public void setMu(double m) {
        setParameters(m, sigma);
    }

    /**
     * This method gets the scale parameter
     *
     * @uml.property name="sigma"
     */
    public double getSigma() {
        return sigma;
    }

    /** This method sets the scale parameter */
    public void setSigma(double s) {
        setParameters(mu, s);
    }

    /** This method computes the cumulative distribution function */
    public double getCDF(double x) {
        double z = (x - mu) / sigma;
        if (z >= 0) return 0.5 + 0.5 * gammaCDF(z * z / 2, 0.5);
        else return 0.5 - 0.5 * gammaCDF(z * z / 2, 0.5);
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t)
    {
    	return Math.exp(mu*t+(.5*sigma*sigma*t*t));
    }

    /**
     * Inverse of the cumulative (General) Normal distribution function.
     * @return the value X for which P(x&lt;X).
     --
     public double inverseCDF(double probability) {
        System.err.println("probability="+probability+"\t mu="+getMu()+
        		"\tSD="+getSD()+"inverseCDF="+(getMu() + 
        		getSigma()*inverseStdNormalCDF(probability)));
    	 return getMu() + getSigma()*inverseStdNormalCDF(probability);
     }
     ***/

    /**
     * Inverse of the cumulative Standar-Normal distribution function.
     * @return the value X for which P(x&lt;X).
     * 
     * Returns the inverse of the cdf of the normal distribution.
     * Rational approximations giving 16 decimals of precision.
     * J.M. Blair, C.A. Edwards, J.H. Johnson, "Rational Chebyshev
     * approximations for the Inverse of the Error Function", in
     * Mathematics of Computation, Vol. 30, 136, pp 827, (1976)
     */
     public double inverseStdNormalCDF(double probability) {
         int i;
         boolean negatif;
         double y, z, v, w;
         double x = probability;

         if (probability <= 0.0) return Double.NEGATIVE_INFINITY;
         if (probability >= 1.0) return Double.POSITIVE_INFINITY;

         // Transform x as argument of InvErf
         x = 2.0 * x - 1.0;
         if (x < 0.0) {
            x = -x;
            negatif = true;
         } else negatif = false;

         if (x <= 0.75) {
            y = x * x - 0.5625;
            v = w = 0.0;
            for (i = 6; i >= 0; i--) {
               v = v * y + InvP1[i];
               w = w * y + InvQ1[i];
            }
            z = (v / w) * x;

         } else if (x <= 0.9375) {
            y = x * x - 0.87890625;
            v = w = 0.0;
            for (i = 7; i >= 0; i--) {
               v = v * y + InvP2[i];
               w = w * y + InvQ2[i];
            }
            z = (v / w) * x;

         } else {
            if (probability > 0.5)
               y = 1.0 / Math.sqrt (-Math.log (1.0 - x));
            else
               y = 1.0 / Math.sqrt (-Math.log (2.0 * probability));
            v = 0.0;
            for (i = 10; i >= 0; i--)
               v = v * y + InvP3[i];
            w = 0.0;
            for (i = 8; i >= 0; i--)
               w = w * y + InvQ3[i];
            z = (v / w) / y;
         }

         if (negatif) {
            if (probability < 1.0e-105) {
               final double constant = 1.77245385090551602729;
               w = Math.exp (-z * z)/constant;  // pdf
               y = 2.0 * z * z;
               v = 1.0;
               double term = 1.0;
               // Asymptotic series for erfc(z) (apart from exp factor)
               for (i = 0; i < 6; ++i) {
                  term *= -(2 * i + 1) / y;
                  v += term;
               }
               // Apply 1 iteration of Newton solver to get last few decimals
               z -= probability / w - 0.5*v / z;

            }
            return -(z*1.41421356237309504880);
         } else return (z*1.41421356237309504880);
      }

     /**
      * Compute the Gauss Error Function
      * http://en.wikipedia.org/wiki/Error_function.
      */
      static public double GaussErrorFunction(double value) {
 		NormalDistribution StdNormal = new NormalDistribution (0,1);
    	if (value ==0) return 0;
 		else return ( (2.0/(Math.sqrt(Math.PI)) * (StdNormal.getCDF(value)-StdNormal.getCDF(0.0))) );
      } 

      /**
       * Compute the Gauss Error Function
       * http://en.wikipedia.org/wiki/Error_function.
       */
       static public double errorFunction(double value) {
  		return GaussErrorFunction(value);
       } 

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/NormalDistribution.html");
    }

    /*
     * Arrays of constants necessary for computing the Inverse Normal CDF function 
     * according to the algorithm of
     * J.M. Blair, C.A. Edwards, J.H. Johnson, "Rational Chebyshev
     * approximations for the Inverse of the Error Function", in
     * Mathematics of Computation, Vol. 30, 136, pp 827, (1976)
     */
    private static final double[] InvP1 = {
        0.160304955844066229311E2,
       -0.90784959262960326650E2,
        0.18644914861620987391E3,
       -0.16900142734642382420E3,
        0.6545466284794487048E2,
       -0.864213011587247794E1,
        0.1760587821390590
    };

    private static final double[] InvQ1 = {
        0.147806470715138316110E2,
       -0.91374167024260313396E2,
        0.21015790486205317714E3,
       -0.22210254121855132366E3,
        0.10760453916055123830E3,
       -0.206010730328265443E2,
        0.1E1
    };

    private static final double[] InvP2 = {
       -0.152389263440726128E-1,
        0.3444556924136125216,
       -0.29344398672542478687E1,
        0.11763505705217827302E2,
       -0.22655292823101104193E2,
        0.19121334396580330163E2,
       -0.5478927619598318769E1,
        0.237516689024448000
    };

    private static final double[] InvQ2 = {
      -0.108465169602059954E-1,
       0.2610628885843078511,
      -0.24068318104393757995E1,
       0.10695129973387014469E2,
      -0.23716715521596581025E2,
       0.24640158943917284883E2,
      -0.10014376349783070835E2,
       0.1E1
    };

    private static final double[] InvP3 = {
        0.56451977709864482298E-4,
        0.53504147487893013765E-2,
        0.12969550099727352403,
        0.10426158549298266122E1,
        0.28302677901754489974E1,
        0.26255672879448072726E1,
        0.20789742630174917228E1,
        0.72718806231556811306,
        0.66816807711804989575E-1,
       -0.17791004575111759979E-1,
        0.22419563223346345828E-2
    };

    private static final double[] InvQ3 = {
        0.56451699862760651514E-4,
        0.53505587067930653953E-2,
        0.12986615416911646934,
        0.10542932232626491195E1,
        0.30379331173522206237E1,
        0.37631168536405028901E1,
        0.38782858277042011263E1,
        0.20372431817412177929E1,
        0.1E1
    };
}

