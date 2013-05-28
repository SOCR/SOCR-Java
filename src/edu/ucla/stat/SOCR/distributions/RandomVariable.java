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

public class RandomVariable {

    /**
     * @uml.property name="distribution"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private Distribution distribution;

    /**
     * @uml.property name="intervalData"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private IntervalData intervalData;

    /**
     * @uml.property name="name"
     */
    private String name;

    /**
     * General constructor: create a new random variable with a specified
     * probability distribution and name
     */
    public RandomVariable(Distribution d, String n) {
        distribution = d;
        name = n;
        intervalData = new IntervalData(distribution.getDomain(), name);

    }

    /**
     * Special constructor: create a new random variable with a specified
     * probability distribution and the name X
     */
    public RandomVariable(Distribution d) {
        this(d, "X");
    }

    /**
     * Assign the probability distribution and create a corresponding data
     * distribution
     * 
     * @uml.property name="distribution"
     */
    public void setDistribution(Distribution d) {
        distribution = d;
        intervalData.setDomain(distribution.getDomain());
    }

    /**
     * Get the probability distribution
     * 
     * @uml.property name="distribution"
     */
    public Distribution getDistribution() {
        return distribution;
    }

    /**
     * Get the data distribution
     * 
     * @uml.property name="intervalData"
     */
    public IntervalData getIntervalData() {
        return intervalData;
    }

    /** Assign a value to the random variable */
    public void setValue(double x) {
        intervalData.setValue(x);
    }

    /** Get the current value of the random variable */
    public double getValue() {
        return intervalData.getValue();
    }

    /**
     * Simulate a value of the probability distribution and assign the value to
     * the data distribution
     */
    public void sample() {
        intervalData.setValue(distribution.simulate());
    }

    /**
     * Simulate a value of the probability distribution, assign the value to the
     * data distribution and return the value
     */
    public double simulate() {
        double x = distribution.simulate();
        intervalData.setValue(x);
        return x;
    }

    /** Reset the data distribution */
    public void reset() {
        intervalData.setDomain(distribution.getDomain());
    }

    /**
     * Get the name of the random variable
     * 
     * @uml.property name="name"
     */
    public String getName() {
        return name;
    }

    /**
     * Assign a name to the random variable
     * 
     * @uml.property name="name"
     */
    public void setName(String n) {
        name = n;
        intervalData.setName(name);
    }

}

