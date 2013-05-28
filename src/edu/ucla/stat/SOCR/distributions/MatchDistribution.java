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

/** The distribution of the number of matches in a random permutation */
public class MatchDistribution extends Distribution {

    /**
     * @uml.property name="parameter"
     */
    int parameter;

    int[] b;

    /**
     * This general constructor creates a new matching distribution with a
     * specified parameter
     */
    public MatchDistribution(int n) {
        setParameter(n);
    }

    /**
     * this default constructor creates a new mathcing distribuiton with
     * parameter 5
     */
    public MatchDistribution() {
        this(5);
        name = "Matching Distribution";

    }

    public void initialize() {
        createValueSetter("Sample-Size (n)", DISCRETE, 1, 50);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        setParameter(v1);
    }

    /**
     * This method sets the parameter of the distribution (the size of the
     * random permutation
     * 
     * @uml.property name="parameter"
     */
    public void setParameter(int n) {
        if (n < 1) n = 1;
        parameter = n;
        super.setParameters(0, parameter, 1, DISCRETE);
        b = new int[n];
    }

    /** This method computes the getDensity function */
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        double sum = 0;
        int sign = -1;
        for (int j = 0; j <= parameter - k; j++) {
            sign = -sign;
            sum = sum + sign / factorial(j);
        }
        return sum / factorial(k);
    }

    /** This method gives the maximum value of the getDensity function */
    public double getMaxDensity() {
        if (parameter == 2) return getDensity(0);
        else return getDensity(1);
    }

    /** This method returns the mean */
    public double getMean() {
        return 1;
    }

    /** This method returns the variance */
    public double getVariance() {
        return 1;
    }

    /**
     * This method gets the parameter
     * 
     * @uml.property name="parameter"
     */
    public int getParameter() {
        return parameter;
    }

    /**
     * This method simulates a value from the distribution, by generating a
     * random permutation and computing the number of matches
     */
    public double simulate() {
        int j, k, u;
        double matches = 0;
        for (int i = 0; i < parameter; i++)
            b[i] = i + 1;
        for (int i = 0; i < parameter; i++) {
            j = parameter - i;
            u = (int) (j * Math.random());
            if (b[u] == i + 1) matches = matches + 1;
            k = b[j - 1];
            b[j - 1] = b[u];
            b[u] = k;
        }
        return matches;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://eom.springer.de/M/m120220.htm");
    }

}

