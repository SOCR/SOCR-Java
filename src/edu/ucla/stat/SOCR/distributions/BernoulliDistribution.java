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
 * The Bernoulli distribution with parameter p <a
 * href="http://mathworld.wolfram.com/BernoulliDistribution.html">
 * http://mathworld.wolfram.com/BernoulliDistribution.html </a>.
 */
public class BernoulliDistribution extends BinomialDistribution {

    /**
     * This default constructor creates a new Bernoulli distribution with
     * parameter p = 0.5
     */
    public BernoulliDistribution() {
        name = "Bernoulli Distribution";
    }

    /**
     * This general constructor creates a new Bernoulli distribution with a
     * specified parameter
     */
    public BernoulliDistribution(double p) {
        super(1, p);
        name = "Bernoulli("+p+") Distribution";
    }

    public void initialize() {
        createValueSetter("Success Probability", CONTINUOUS, 0, 1);
        setParameters(1, 0.5);
        name = "Bernoulli(0.5) Distribution";
    }

    public void valueChanged() {
        setParameters(1, getValueSetter(0).getValue());
        name = "Bernoulli("+getValueSetter(0).getValue()+") Distribution";
    }

    /**
     * This method overrides the corresponding method in BinomialDistribution so
     * that the number of trials 1 cannot be changed
     */
    public void setTrials(int n) {
        super.setTrials(1);
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return 1;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/BernoulliDistribution.html");
    }

}

