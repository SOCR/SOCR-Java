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
 * This class models the Gilbrat's distribution: A continuous distribution in
 * which the logarithm of a variable x has a Standard Normal distribution. It is
 * a special case of the Log-Normal distribution. <a
 * http://en.wikipedia.org/wiki/Log-normal_distribution">
 * http://en.wikipedia.org/wiki/Log-normal_distribution </a>.
 */
public class GilbratsDistribution extends LogNormalDistribution {
    //variables
    public final static double C = Math.sqrt(2 * Math.PI);

    /**
     * @uml.property name="mu"
     */
    private double mu;

    /**
     * @uml.property name="sigma"
     */
    private double sigma;

    /** This general constructor creates a new Gilbrat's distribution. */
    public GilbratsDistribution() {
        name = "Gilbrats Distribution";
        setParameters(0, 1);
    }

    /** This method sets the parameters, computes the default interval */
    public void setParameters(double m, double s) {
        if (s <= 0) s = 1;
        mu = m;
        sigma = s;
        double upper = getMean() + 3 * getSD();
        super.setParameters(0, upper, 0.01 * upper, CONTINUOUS);
    }

    /** This method computes the getDensity function */
    public double getDensity(double x) {
        double z = (Math.log(x) - mu) / sigma;
        return Math.exp(-z * z / 2) / (x * C * sigma);
    }

    /** This method computes the maximum value of the getDensity function */
    public double getMaxDensity() {
        double mode = Math.exp(mu - sigma * sigma);
        return getDensity(mode);
    }

    /** This method computes the mean */
    public double getMean() {
        return Math.exp(mu + sigma * sigma / 2);
    }

    /** This method computes the variance */
    public double getVariance() {
        double a = mu + sigma * sigma;
        return Math.exp(2 * a) - Math.exp(mu + a);
    }

    /** This method simulates a value from the distribution */
    public double simulate() {
        double r = Math.sqrt(-2 * Math.log(Math.random()));
        double theta = 2 * Math.PI * Math.random();
        return Math.exp(mu + sigma * r * Math.cos(theta));
    }

    /**
     * This method returns mu
     * 
     * @uml.property name="mu"
     */
    public double getMu() {
        return mu;
    }

    /** This method sets mu */
    public void setMu(double m) {
        setParameters(m, sigma);
    }

    /**
     * This method gets sigma
     * 
     * @uml.property name="sigma"
     */
    public double getSigma() {
        return sigma;
    }

    /** This method sets sigma */
    public void setSigma(double s) {
        setParameters(mu, s);
    }

    /** This method computes the cumulative distribution function */
    public double getCDF(double x) {
        double z = (Math.log(x) - mu) / sigma;
        if (z >= 0) return 0.5 + 0.5 * gammaCDF(z * z / 2, 0.5);
        else return 0.5 - 0.5 * gammaCDF(z * z / 2, 0.5);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Log-normal_distribution");
    }

}

