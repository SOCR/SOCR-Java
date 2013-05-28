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

package edu.ucla.stat.SOCR.core;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.distributions.exception.*;

/**
 * Distribution: An abstract implmentation of a real probability distribution.
 * All distributions should subclass this class.
 * <p>
 * It require a no argument constructor, if it need a handler for client to
 * select values, it should create some valueSetters by calling
 * <b>createsValueSetter() </b>. It is better to invoke createsValueSetter() ,
 * in the method <b>initialize() </b>. If you invoke them at constructor, then
 * its subclass will creates these valueSetters too. You also must override
 * <b>valueChanged() </b> to do updates corrosponding any changes in
 * valueSetters. 
 * It is an Observer of its value changed, and observable for its value changes
 */
public abstract class Distribution extends SOCRValueSettable implements Pluginable {
	//Constants
	public final static int DISCRETE = 0 ;
	//Constants
	public final static int CONTINUOUS = 1 ;
	//Constants
	public final static int MIXED = 2 ;
	//Constants
	public final static double MAXMGFYVAL = 10000.0;
	//Constants
	public final static double MINMGFXVAL = 0.0;
	//Constants
	public final static double MAXMGFXVAL = 15.0;

	SOCRDistributions distributionContainer;
	SOCRDistributionFunctors functorContainer;
	/**
	 * 
	 * @uml.property name="applet" 
	 */
	protected JApplet applet;

	private Observable observable = new Observable() {
		public void notifyObservers() {
			setChanged();
			super.notifyObservers(Distribution.this);
		}
	};

	/**
	 * 
	 * @uml.property name="type" 
	 */
	private int type;

	/**
	 * 
	 * @uml.property name="domain"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Domain domain;

	/**
	 * @uml.property name="mgfDomain"
	 */
	private Domain mgfDomain;

	/**
	 * @uml.property name="pgfDomain"
	 */
	private Domain pgfDomain;

	/**
	 * 
	 * @uml.property name="name" 
	 */
	protected String name = "";

	public static Distribution getInstance(String classname) throws Exception {
		Class cls = Class.forName(classname);
		if (cls == null) return null;
		return (Distribution)  cls.newInstance();
	}

	/**
	 * 
	 * @uml.property name="applet"
	 */
	public void setApplet(JApplet applet) {
		this.applet = applet;
	}


	public SOCRDistributions getSOCRDistributions() {
		return this.distributionContainer;
	}
	
	public SOCRDistributionFunctors getSOCRDistributionFunctors() {
		return this.functorContainer;
	}

	public void addObserver(Observer observer) {
		observable.addObserver(observer);
	
		if (observer.getClass().getName().equals("edu.ucla.stat.SOCR.core.SOCRDistributions"))
			this.distributionContainer = (SOCRDistributions)observer;
		else if (observer.getClass().getName().equals("edu.ucla.stat.SOCR.core.SOCRDistributionFunctors"))
			this.functorContainer = (SOCRDistributionFunctors)observer;
	}
	
	/**
	 * used for some subclass to initialize before being used
	 */   
	public void initialize() {}

	public void update(Observable o, Object arg) {
		valueChanged(o, arg);
		observable.notifyObservers();
	}

	public void valueChanged() {}

	public void valueChanged(Observable o, Object arg) { valueChanged();};

	/**
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * The getDensity method is abstract and must be overridden for any specific
	 * distribuiton
	 */
	public abstract double getDensity(double x);

	/**
	 * This method defines a partition of an interval that acts as a default
	 * domain for the distribution, for purposes of data collection and for
	 * default computations. For a discrete distribution, the specified
	 * parameters define the midpoints of the partition (these are typically the
	 * values on which the distribution is defined, although truncated if the
	 * true set of values is infinite). For a continuous distribution, the
	 * parameters define the boundary points of the interval on which the
	 * distribuiton is defined (truncated if the true interval is infinite)
	 */
	public void setParameters(double a, double b, double w, int t) {
		if (t < 0)
			t = 0;
		else if (t > 2)
			t = 2;
		type = t;
		//System.out.println("core:Distribution setParameters get called, setting domain");
		if (type == DISCRETE)
			domain = new Domain(a - 0.5 * w, b + 0.5 * w, w);
		else
			domain = new Domain(a, b, w);
	}

	/**
	 * This method defines a partition of an interval that acts as a default
	 * domain for the moment generating function of the distribution, for purposes of 
	 * data collection and for default computations. The parameters define the 
	 * boundary points of the interval on which the moment generating function 
	 * is defined (truncated if the true interval is infinite)
	 */
	public void setMGFParameters() {

		// change 100 to a proper value talk to Dr. Dinov about this - RG
		double mgf_root = findGFRoot(0, 100, .25, MINMGFXVAL, MAXMGFXVAL);
		double max_mgf_xval = mgf_root;
		double w = (max_mgf_xval-MINMGFXVAL)/100; 
		mgfDomain = new Domain(MINMGFXVAL, max_mgf_xval,w);
	}

	public void setMGFParameters(double xlo, double xhi)
	{
		double max_slope = 50.0;
		this.setMGFParameters(xlo, xhi, max_slope);
	}

	public void setMGFParameters(double xlo, double xhi, double max_slope)
	{
		if (xlo == 0.0)
			xlo = MINMGFXVAL;
		if (xhi == 0.0)
			xhi = MAXMGFXVAL;
		double initial_guess = .25;
		if (xhi <= initial_guess || xlo >= initial_guess)
			initial_guess = (xhi-xlo)/2.0;
		this.setMGFParameters(xlo, xhi, max_slope, initial_guess);
	}

	public void setMGFParameters(double xlo, double xhi, double max_slope, double initial_guess)
	{

		double max_mgf_xval = findGFRoot(0, max_slope, initial_guess, xlo, xhi);
		double w = (max_mgf_xval-xlo)/100;
		//System.out.println("MGF Domain: (" + xlo + "," + max_mgf_xval +  ") MaxMGFXVal: " + max_mgf_xval);
		mgfDomain = new Domain(xlo, max_mgf_xval, w);
	}
	
	/**
	 * This method defines a partition of an interval that acts as a default
	 * domain for the probability generating function of the distribution, for purposes of 
	 * data collection and for default computations. The parameters define the 
	 * boundary points of the interval on which the probability generating function 
	 * is defined (truncated if the true interval is infinite)
	 */
	public void setPGFParameters() {

		// change 100 to a proper value talk to Dr. Dinov about this - RG
		double max_slope = 100;
		double mgf_root = findGFRoot(1, max_slope, .25, MINMGFXVAL, MAXMGFXVAL);
		double max_mgf_xval = mgf_root;
		double w = (max_mgf_xval-MINMGFXVAL)/100; 
		pgfDomain = new Domain(MINMGFXVAL, max_mgf_xval,w);
	}

	public void setPGFParameters(double xlo, double xhi)
	{
		double max_slope = 100.0;
		this.setPGFParameters(xlo, xhi, max_slope);
	}

	public void setPGFParameters(double xlo, double xhi, double max_slope)
	{
		if (xlo == 0.0)
			xlo = MINMGFXVAL;
		if (xhi == 0.0)
			xhi = MAXMGFXVAL;
		double initial_guess = .25;
		if (xhi <= initial_guess || xlo >= initial_guess)
			initial_guess = (xhi-xlo)/2.0;
		this.setPGFParameters(xlo, xhi, max_slope, initial_guess);
	}

	public void setPGFParameters(double xlo, double xhi, double max_slope, double initial_guess)
	{
		double max_mgf_xval = findGFRoot(1, max_slope, initial_guess, xlo, xhi);
		double w = (max_mgf_xval-xlo)/100;
		//System.out.println("MGF Domain: (" + xlo + "," + max_mgf_xval +  ") MaxMGFXVal: " + max_mgf_xval);
		pgfDomain = new Domain(xlo, max_mgf_xval, w);
	}

	/**
	 * This method sets the domain of the distribution for purposes of data
	 * collection and for default computations. For a discrete distribution, the
	 * domain specifies the values on which the distribution is defined
	 * (although truncated if the true set of values is infinite). For a
	 * continuous distribution, domain defines the interval on which the
	 * distribuiton is defined (truncated if the true interval is infinite).
	 * 
	 * @param d
	 *            the domain
	 * 
	 * @uml.property name="domain"
	 */
	protected void setDomain(Domain d) {
		domain = d;
		type = domain.getType();
		observable.notifyObservers();
	}

	/**
	 * This method sets the moment generating function's domain of the distribution 
	 * for purposes of data collection and for default computations. 
	 * For a discrete distribution, the domain specifies the values on which the distribution 
	 * is defined (although truncated if the true set of values is infinite). For a
	 * continuous distribution, domain defines the interval on which the
	 * distribution is defined (truncated if the true interval is infinite).
	 * 
	 * @param d
	 *            the domain
	 * 
	 * @uml.property name="domain"
	 */
	protected void setMGFDomain(Domain d) {
		mgfDomain = d;
		type = mgfDomain.getType();
	}
	
	/**
	 * This method sets the moment generating function's domain of the distribution 
	 * for purposes of data collection and for default computations. 
	 * For a discrete distribution, the domain specifies the values on which the distribution 
	 * is defined (although truncated if the true set of values is infinite). For a
	 * continuous distribution, domain defines the interval on which the
	 * distribution is defined (truncated if the true interval is infinite).
	 * 
	 * @param d
	 *            the domain
	 * 
	 * @uml.property name="domain"
	 */
	protected void setPGFDomain(Domain d) {
		pgfDomain = d;
		type = pgfDomain.getType();
	}


	/**
	 * This method sets the domain of the distribution for purposes of data
	 * collection and for default computations.
	 * 
	 * @param a
	 *            lower value or bound of the domain
	 * @param b
	 *            the upper value or bound of the domain
	 * @param w
	 *            the width (step size) of the domain
	 * @param t
	 *            the type of domain (DISCRETE or CONTINUOUS)
	 */
	protected void setDomain(double a, double b, double w, int t) {
		setDomain(new Domain(a, b, w, t));
	}

	/**
	 * This method sets the MGF domain of the distribution for purposes of data
	 * collection and for default computations.
	 * 
	 * @param a
	 *            lower value or bound of the MGF domain
	 * @param b
	 *            the upper value or bound of the MGF domain
	 * @param w
	 *            the width (step size) of the MGF domain
	 * @param t
	 *            the type of domain (DISCRETE or CONTINUOUS)
	 */
	protected void setMGFDomain(double a, double b, double w) {
		setMGFDomain(new Domain(a, b, w));
	}
	
	/**
	 * This method sets the PGF domain of the distribution for purposes of data
	 * collection and for default computations.
	 * 
	 * @param a
	 *            lower value or bound of the MGF domain
	 * @param b
	 *            the upper value or bound of the MGF domain
	 * @param w
	 *            the width (step size) of the MGF domain
	 * @param t
	 *            the type of domain (DISCRETE or CONTINUOUS)
	 */
	protected void setPGFDomain(double a, double b, double w) {
		setPGFDomain(new Domain(a, b, w));
	}

	/**
	 * This method returns the domain of the distribution.
	 * 
	 * @uml.property name="domain"
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * This method returns the domain of the mgf for that particular distribution.
	 * 
	 * @uml.property name="mgfDomain"
	 */
	public Domain getMgfDomain() {
		return mgfDomain;
	}
	
	/**
	 * This method returns the domain of the pgf for that particular distribution.
	 * 
	 * @uml.property name="mgfDomain"
	 */
	public Domain getPGFDomain() {
		return pgfDomain;
	}

	/**
	 * This method returns the type of the distribution (discrete or continuous)
	 * 
	 * @uml.property name="type"
	 */
	public final int getType() {
		return type;
	}

	public void paramEstimate(double[] distData) {

	}

	/**
	 * This method returns the largest (finite) value of the getDensity function
	 * on the finite set of domain values. This method should be overridden if
	 * the maximum value is known in closed form
	 */
	public double getMaxDensity() {
		double max = 0, d;
		for (int i = 0; i < domain.getSize(); i++) {
			d = getDensity(domain.getValue(i));
			if (d > max & d < Double.POSITIVE_INFINITY)
				max = d;
		}
		return max;
	}

	/**
	 * This method returns a default approximate mean, based on the finite set
	 * of domain values. This method should be overriden if the mean is known in
	 * closed form
	 */
	public double getMean() {
		double sum = 0, x, w;
		if (type == DISCRETE)
			w = 1;
		else
			w = domain.getWidth();
		for (int i = 0; i < domain.getSize(); i++) {
			x = domain.getValue(i);
			sum = sum + x * getDensity(x) * w;
		}
		return sum;
	}

	/**
	 * This method returns the sample mean of a data series
	 */
	public double getMean(double[] distData) {
		double sumX = 0;
		if (distData.length == 0) return (0);
		else sumX = distData[0];

		for (int i = 1; i < distData.length; i++)
			sumX += distData[i];
		double result = (sumX / distData.length);
		return result;
	}

	/**
	 * This method returns a default approximate variance. This method should be
	 * overriden if the variance is known in closed form
	 */
	public double getVariance() {
		double sum = 0, mu = getMean(), x, w;
		if (type == DISCRETE)
			w = 1;
		else
			w = domain.getWidth();
		for (int i = 0; i < domain.getSize(); i++) {
			x = domain.getValue(i);
			sum = sum + (x - mu) * (x - mu) * getDensity(x) * w;
		}
		return sum;
	}
	
	/**
	 * This method returns the sample variance of a data series
	 */
	public double getVariance(double[] distData) {
		double sumX = 0;
		double sampleMean = getMean(distData);
		if (distData.length<2) return 0;
		
		for (int i = 0; i < distData.length; i++)
			sumX += Math.pow(distData[i]-sampleMean, 2);
		
		return (sumX/(distData.length-1));
	}

	/**
	 * This method returns the n-th sample moment of a data series
	 */
	public double getSampleMoment(int _power, double[] distData) {
		int pow = 1;
		double sumX_pow = 0;

		if (_power <= 0) return 1;
		else pow = _power;

		if (distData.length == 0) return (0);
		else sumX_pow = Math.pow(distData[0], pow);

		for (int i = 1; i < distData.length; i++)
			sumX_pow += Math.pow(distData[i], pow);
		return (sumX_pow / distData.length);
	}

	/**
	 * This method returns the standard deviation, as the square root of the
	 * variance
	 */
	public double getSD() {
		return Math.sqrt(getVariance());
	}

	/**
	 * This method returns an approximate moment generating  
	 * function. This should be overriden if the MGF is known in closed form
	 */
	public double getMGF(double t) throws NoMGFException
	{
		double sum=0, w, x;
		w = domain.getWidth();
		int j = domain.getIndex(t);
		if (j < 0)
			return 0;
		else if (j >= domain.getSize())
			return 1; // check this out to see if correct??
		else{
			for (int i=0; i<=j; i++)
			{   
				x = domain.getValue(i);
				sum = sum + Math.exp(x*t)*getDensity(x)*w;
			}   
			if (type == CONTINUOUS){
				x = domain.getValue(j) - 0.5*w;
				sum = sum + getDensity((t+x)/2)*(t-x)*Math.exp(x*t);	
			}   
		}
		return sum;
	}
	
	/**
	 *  This method should be overriden if the PGF is known in closed form
	 */
	public double getPGF(double t) 
	{ 
		return 0;
	}


	/**
	 * This method returns a default approximate cumulative distribution
	 * function. This should be overriden if the CDF is known in closed form
	 */
	public double getCDF(double x) {
		double sum = 0, w, y;
		if (type == DISCRETE)
			w = 1;
		else
			w = domain.getWidth();
		int j = domain.getIndex(x);
		if (j < 0)
			return 0;
		else if (j >= domain.getSize())
			return 1;
		else {
			for (int i = 0; i <= j; i++)
				sum = sum + getDensity(domain.getValue(i)) * w;
			if (type == CONTINUOUS) {
				y = domain.getValue(j) - 0.5 * w;
				sum = sum + getDensity((x + y) / 2) * (x - y);
			}
		}
		if (sum<0) sum=0;
		else if (sum>1) sum=1;
		return sum;
	}

	/**
	 * This method computes an approximate getQuantile function. This should be
	 * overriden if the getQuantile function is known in closed form
	 */
	public double getQuantile(double p) {
		double sum = 0, x, w;
		if (type == DISCRETE)
			w = 1;
		else
			w = domain.getWidth();
		if (p <= 0)
			return domain.getLowerValue();
		else if (p >= 1)
			return domain.getUpperValue();
		else {
			int n = domain.getSize(), i = 0;
			x = domain.getValue(i);
			sum = getDensity(x) * w;
			while ((sum < p) & (i < n)) {
				i++;
				x = domain.getValue(i);
				sum = sum + getDensity(x) * w;
			}
			return x;
		}
	}

	/**
	 * This method computes a default simulation of a value from the
	 * distribution, as a random getQuantile. This method should be overridden
	 * if a better method of simulation is known.
	 */
	public double simulate() {
		return getQuantile(Math.random());
	}

	// estimate sample mean from data
	public double sampleMean(double[] array) {
		double mean = 0;
		double sumX = array[0];
		for (int i = 1; i < array.length; i++)
			sumX += array[i];
		mean = sumX / array.length;
		return mean;
	}

	public double sampleVar(double[] array, double mean) {
		double variance = 1;
		double sumX2 = array[0] * array[0];
		for (int i = 1; i < array.length; i++)
			sumX2 += array[i] * array[i];
		//variance=mean*mean-sumX2/(array.length*array.length);
		variance = -mean * mean + sumX2 / array.length;
		return variance;
	}

	/**
	 * This method returns an online description of this distribution. This
	 * method should be overwritten for each specific distribution.
	 */
	public String getOnlineDescription() {
		return "http://mathworld.wolfram.com/topics/StatisticalDistributions.html";
	}

	/**
	 * This method returns a general Distribution usage off-line help. This
	 * method should be common for all distributions.
	 */
	public String getLocalHelp() {
		return "Introduction: These interactive distribution applets allow you to:\n\n"+
		"\t1. Dynamically compute any area under any of the implemented distributions\n"+
		"\t2. Calculate quantiles and percentiles.\n\n"+
		"How to use the Interactive Distribution Applets:\n\n"+
		"\t1. Select a distribution from the drop-down list in the top-left corner\n"+
		"\t2. Select the paramenters of the distributiion accordingly (make sure these make sense!)\n"+
		"\t3. Control the left-limit of the area of interest: click the mouse on the graph canvas near the LEFT end of the support of the distribution and drag it to the right\n"+
		"\t\t Alternatively you can use the text areas on the bottom-left to enter the left/right limits numerically\n"+
		"\t4. Control the right-limit of the area of interest: click the mouse near the RIGHT end of the support of the distribution and drag it to the left\n"+
		"\t5. Play with the left and right limits to select the desired area or to find the corresponding tail probabilities.\n\n"+
		"Notes: \n"+
		"\t1. You can change the parameters of the distribution at any time. We have optimized the applet to show a maximal field-of-view for each distribution\n"+
		"\t for any set of parameters. As a consequence, changing the the set of parameters may effect only the scales of the vertical and horizontal axes.\n"+
		"\t2. To report bugs or make recommendations please visit: http://www.socr.ucla.edu/" 
		;
	}



	/**
	 * This method computes a default approximate median. This method should be
	 * overriden when there is a closed form expression for the median.
	 */
	public double getMedian() {
		return getQuantile(0.5);
	}

	/** This method computes the failure rate function */
	public double getFailureRate(double x) {
		return getDensity(x) / (1 - getCDF(x));
	}

	//Class methods
	/**
	 * This method computes the number of permuatations of k objects chosen from
	 * a population of n objects.
	 */
	public static double perm(double n, int k) {
		double prod;
		if (k > n | k < 0)
			return 0;
		else {
			prod = 1;
			for (int i = 1; i <= k; i++)
				prod = prod * (n - i + 1);
			return prod;
		}
	}

	/** This method computes k!, the number of permutations of k objects. */
	public static double factorial(int k) {
		return perm(k, k);
	}

	/**
	 * This method computes the number of combinations of k objects chosen from
	 * a population of n objects
	 */
	public static double comb(double n, int k) {
		return perm(n, k) / factorial(k);
	}

	/** This method computes the log of the gamma function. */
	public static double logGamma(double x) {
		double coef[] = { 76.18009173, -86.50532033, 24.01409822, -1.231739516,
				0.00120858003, -0.00000536382 };
		double step = 2.50662827465, fpf = 5.5, t, tmp, ser;
		t = x - 1;
		tmp = t + fpf;
		tmp = (t + 0.5) * Math.log(tmp) - tmp;
		ser = 1;
		for (int i = 1; i <= 6; i++) {
			t = t + 1;
			ser = ser + coef[i - 1] / t;
		}
		return tmp + Math.log(step * ser);
	}

	/** This method computes the gamma function. */
	public static double gamma(double x) {
		return Math.exp(logGamma(x));
	}

	/**
	 * This method computes the CDF of the gamma distribution with shape
	 * parameter a and scale parameter 1
	 */
	public static double gammaCDF(double x, double a) {
		if (x <= 0)
			return 0;
		else if (x < a + 1)
			return gammaSeries(x, a);
		else
			return 1 - gammaCF(x, a);
	}

	/** This method computes a gamma series that is used in the gamma CDF. */
	private static double gammaSeries(double x, double a) {
		//Constants
		int maxit = 100;
		double eps = 0.0000003;
		//Variables
		double sum = 1.0 / a, ap = a, gln = logGamma(a), del = sum;
		for (int n = 1; n <= maxit; n++) {
			ap++;
			del = del * x / ap;
			sum = sum + del;
			if (Math.abs(del) < Math.abs(sum) * eps)
				break;
		}
		return sum * Math.exp(-x + a * Math.log(x) - gln);
	}

	/**
	 * This method computes a gamma continued fraction function function that is
	 * used in the gamma CDF.
	 */
	private static double gammaCF(double x, double a) {
		//Constants
		int maxit = 100;
		double eps = 0.0000003;
		//Variables
		double gln = logGamma(a), g = 0, gOld = 0, a0 = 1, a1 = x, b0 = 0, b1 = 1, fac = 1;
		double an, ana, anf;
		for (int n = 1; n <= maxit; n++) {
			an = 1.0 * n;
			ana = an - a;
			a0 = (a1 + a0 * ana) * fac;
			b0 = (b1 + b0 * ana) * fac;
			anf = an * fac;
			a1 = x * a0 + anf * a1;
			b1 = x * b0 + anf * b1;
			if (a1 != 0) {
				fac = 1.0 / a1;
				g = b1 * fac;
				if (Math.abs((g - gOld) / g) < eps)
					break;
				gOld = g;
			}
		}
		return Math.exp(-x + a * Math.log(x) - gln) * g;
	}

	/** The method computes the beta CDF. */
	public static double betaCDF(double x, double a, double b) {
		double bt;
		if ((x == 0) | (x == 1))
			bt = 0;
		else
			bt = Math.exp(logGamma(a + b) - logGamma(a) - logGamma(b) + a
					* Math.log(x) + b * Math.log(1 - x));
		if (x < (a + 1) / (a + b + 2))
			return bt * betaCF(x, a, b) / a;
		else
			return 1 - bt * betaCF(1 - x, b, a) / b;
	}

	/**
	 * This method computes a beta continued fractions function that is used in
	 * the calculations of the beta CDF. See Numerical Recepies in C for
	 * incomplete Beta function and the continuous fraction representation.
	 */
	private static double betaCF(double x, double a, double b) {
		int maxit = 100;
		double eps = 0.0000003, am = 1, bm = 1, az = 1, qab = a + b, qap = a + 1, qam = a - 1, bz = 1
		- qab * x / qap, tem, em, d, bpp, bp, app, aOld, ap;
		for (int m = 1; m <= maxit; m++) {
			em = m;
			tem = em + em;
			d = em * (b - m) * x / ((qam + tem) * (a + tem));
			ap = az + d * am;
			bp = bz + d * bm;
			d = -(a + em) * (qab + em) * x / ((a + tem) * (qap + tem));
			app = ap + d * az;
			bpp = bp + d * bz;
			aOld = az;
			am = ap / bpp;
			bm = bp / bpp;
			az = app / bpp;
			bz = 1;
			if (Math.abs(az - aOld) < eps * Math.abs(az))
				break;
		}
		return az;
	}

	/**
	 * Inverse of the cumulative distribution function (CDF) of the specified distribution.
	 * @param probability - a probability value in [0, 1]
	 * @return the value X for which P(X)==P(x &lt; X) = probability.
	 */
	public double inverseCDF(double probability) {
		if (0<probability && probability <1) {
			double localMedian = getQuantile(0.5);
			//return findRoot(probability, localMedian, localMedian-6.0, localMedian+6.0); 
			return findRoot(probability, localMedian, getQuantile(0.00001), getQuantile(0.9999)); 
			// Distribution.findRoot(double prob, double guess, double xLo, double xHi); 
		}
		else if (probability <=0) return 0;
		else return 1.0;  // (1 < probability)
	}

	/**
	 * This method approximates the value of X for which P(x&lt;X)=<I>prob</I>.
	 * It applies a combination of a Newton-Raphson procedure and bisection method
	 * with the value <I>guess</I> as a starting point. Furthermore, to ensure convergency
	 * and stability, one should supply an inverval [<I>xLo</I>,<I>xHi</I>] in which the probalility
	 * distribution reaches the value <I>prob</I>. 
	 */
	protected double findRoot(double prob, double guess, double xLo, double xHi) {
		if (prob<0 || 1<prob) return 0;
		if (xLo>guess || guess >xHi) return 0;

		final double accuracy=1.0e-7;
		final int maxIteration=150;
		double x=guess, xNew=guess;
		double error, pdf, dx=1000.0;
		int i=0;
		while(Math.abs(dx)>accuracy && i++<maxIteration) { // Apply Newton-Raphson step
			error=getCDF(x)-prob;
			if(error<0.0) xLo=x;
			else xHi=x;
			pdf=getDensity(x);
			if(pdf!=0.0) {          // Avoid division by zero
				dx=error/pdf;
				xNew=x-dx;
			}
			// If the NR fails to converge (which for example may be the
			// case if the initial guess is to rough) we apply a bisection
			// step to determine a more narrow interval around the root.
			if(xNew<xLo || xNew>xHi || pdf==0.0) {
				xNew=(xLo+xHi)/2.0;
				dx=xNew-x;
			}
			x=xNew;
			//System.out.println("xNew="+xNew);
		}
		return x;
	}

	/**
	 * This method approximates the value of X for which P(x&lt;X)=<I>prob</I>.
	 * It applies a combination of a Newton-Raphson procedure and bisection method
	 * with the value <I>guess</I> as a starting point. Furthermore, to ensure convergency
	 * and stability, one should supply an inverval [<I>xLo</I>,<I>xHi</I>] in which the probalility
	 * distribution reaches the value <I>prob</I>. 
	 */
	protected double findGFRoot(int genFun, double prob, double guess, double xLo, double xHi) {
		if (prob<0) return 0;
		if (xLo>guess || guess >xHi) return 0;

		final double accuracy=1.0e-7;
		final int maxIteration=150;
		double x=guess, xNew=guess;
		double error =0.0, pdf, dx=1000.0;
		int i=0;
		while(Math.abs(dx)>accuracy && i++<maxIteration) 
		{ // Apply Newton-Raphson step
			
			error = getGFDerivative(genFun, x) - prob;

			if(error<0.0) 
				xLo=x;
			else
				xHi=x;
			pdf=getGFSecondDerivative(genFun, x);

			if(pdf!=0.0) 
			{          // Avoid division by zero
				dx=error/pdf;
				xNew=x-dx;
			}
			// If the NR fails to converge (which for example may be the
			// case if the initial guess is to rough) we apply a bisection
			// step to determine a more narrow interval around the root.
			if(xNew<xLo || xNew>xHi || pdf==0.0) {
				xNew=(xLo+xHi)/2.0;
				dx=xNew-x;
			}
			x=xNew;

		}
		return x;
	}

	/**
	 * This method approximates a derivative for a Generating Function using
	 * Newton's Quotient. f(x+h)-f(x)/h
	 * Parameters genFun 
	 * if genFun == 0 use moment generating function
	 * if genFun == 1 use probabilty generating function
	 */
	protected double getGFDerivative(int genFun, double x)
	{
		double h = .00001;
		double newtonQuotient = 0.0;
		if (genFun == 0)
		{
			try {
				newtonQuotient = (getMGF(x+h)-getMGF(x))/h;
			} 
			catch (NoMGFException e) {System.err.println(e.getMessage());}
		}
		else if (genFun == 1)
			newtonQuotient = (getPGF(x+h)-getPGF(x))/h;
		return newtonQuotient;
	}

	/**
	 * This method approximates the second derivative for a Generating Function using
	 * Newton's Quotient. f(x+h)-f(x)/h
	 * Parameters genFun 
	 * if genFun == 0 use moment generating function
	 * if genFun == 1 use probabilty generating function
	 */
	protected double getGFSecondDerivative(int genFun, double x)
	{
		double h = .00001;
		double newtonQuotient = 0.0;
		newtonQuotient = (getGFDerivative(genFun, x+h)-getGFDerivative(genFun, x))/h;	
		return newtonQuotient;
	}



	/* (non-Javadoc)
	 * @see edu.ucla.stat.SOCR.core.Pluginable#getContentPane()
	 */
	public Container getDisplayPane() {
		throw new IllegalStateException("overrite getDisplayPane()");
	}

}
