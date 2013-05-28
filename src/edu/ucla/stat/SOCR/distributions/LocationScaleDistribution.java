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
 * This class applies a location-scale tranformation to a given distribution. In
 * terms of the corresponding random variable X, the transformation is Y = a +
 * bX
 */
public class LocationScaleDistribution extends Distribution {

    /**
     * @uml.property name="dist"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private Distribution dist;

    private double location;
    private double scale;

    /**
     * This general constructor creates a new location-scale transformation on a
     * given distribuiton with given location and scale parameters
     */
    public LocationScaleDistribution(Distribution d, double a, double b) {
        setParameters(d, a, b);
    }

    /**
     * This method sets the parameters: the distribution and the location and
     * scale parameters
     * 
     * @uml.property name="dist"
     */
    public void setParameters(Distribution d, double a, double b) {
        dist = d;
        location = a;
        scale = b;
        Domain domain = dist.getDomain();
        double l, u, w = domain.getWidth();
        int t = dist.getType();
        if (t == DISCRETE) {
            l = domain.getLowerValue();
            u = domain.getUpperValue();
        } else {
            l = domain.getLowerBound();
            u = domain.getUpperBound();
        }
        if (scale == 0) super.setParameters(location, location, 1, DISCRETE);
        else if (scale < 0) super.setParameters(location + scale * u, location
                + scale * l, -scale * w, t);
        else super.setParameters(location + scale * l, location + scale * u, scale
                * w, t);
    }

    /** This method defines the getDensity function */
    public double getDensity(double x) {
        if (scale == 0) {
            if (x == location) return 1;
            else return 0;
        } else return dist.getDensity((x - location) / scale);
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return dist.getMaxDensity();
    }

    /** This mtehod returns the mean */
    public double getMean() {
        return location + scale * dist.getMean();
    }

    /** This method returns the variance */
    public double getVariance() {
        return (scale * scale) * dist.getVariance();
    }

    /** This method returns a simulated value from the distribution */
    public double simulate() {
        return location + scale * dist.simulate();
    }

    /** This method returns the cumulative distribution function */
    public double getCDF(double x) {
        if (scale > 0) return dist.getCDF((x - location) / scale);
        else return 1 - dist.getCDF((x - location) / scale);
    }

    /** This method returns the getQuantile function */
    public double getQuantile(double p) {
        if (scale > 0) return location + scale * dist.getQuantile(p);
        else return location + scale * dist.getQuantile(1 - p);
    }
}

