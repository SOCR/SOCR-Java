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
 * This class models the discrete ArcSine distribution that governs the last
 * zero in a symmetric random walk on an interval.
 * http://www.math.uu.nl/people/gnedin/ArcSin.ps
 * 
 * Example: for 20 tosses with probability about 0.35 one of the players will never be in the
 * winning zone. And the chance of parity 10:10 is only 0.06. The probability law
 * p_{2k,2n}=\choose{2k,k} \times \choose{2n-2k, n-k} \times 2^{-2n}, for all k=0, 1, ..., n
 * is the discrete arcsine's distribution for the time spent in the winning zone.
 */
public class DiscreteArcsineDistribution extends Distribution {

    /**
     * @uml.property name="parameter"
     */
    //Paramter n
    private int parameter;

    /**
     * This general constructor creates a new discrete arcsine distribution with
     * a specified number of steps.
     */
    public DiscreteArcsineDistribution(int n) {
        setParameter(n);
    }

    /**
     * This default constructor creates a new discrete arcsine distribution with
     * 10 steps.
     */
    public DiscreteArcsineDistribution() {
        this(10);
        name = "Discrete ArcSine Distribution";
    }

    public void initialize() {
        createValueSetter("Number of Steps (n)", DISCRETE, 1, 100, 5);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        setParameter(v1);
    }

    /**
     * This method sets the parameter, the number of steps.
     * 
     * @uml.property name="parameter"
     */
    public void setParameter(int n) {
        //if (n%2 == 0) n--;
    	if (n<=0) n=10;
        parameter = n;
        super.setParameters(0, 2*parameter+1, 1, DISCRETE);
    }

    /** This method computes the density function. */
    public double getDensity(double x) {
        int k = (int) ((x+0.5)/2);
        if (k<0 || k>parameter) return 0;
        else return 0.5*comb(2*k, k) * comb(2*(parameter - k), parameter - k)
                / Math.pow(2, 2*parameter);
    }

    /** This method computes the maximum value of the density function. */
    public double getMaxDensity() {
        return getDensity(0);
    }

    /**
     * This method gets the parameter, the number of steps.
     * 
     * @uml.property name="parameter"
     */
    public int getParameter() {
        return parameter;
    }

    /**
     * This method simulates a value from the distribution, by simulating a
     * random walk on the interval.
     */
    public double simulate() {
        int step, lastZero = 0, position = 0;
        for (int i = 1; i <= 2*parameter; i++) {
            if (Math.random() < 0.5) step = 1;
            else step = -1;
            position = position + step;
            if (position == 0) lastZero = i;
        }
        return lastZero;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://eom.springer.de/R/r077380.htm");
    }
}

