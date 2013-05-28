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

/** Point mass at x0. */
public class PointMassDistribution extends Distribution {

    /**
     * @uml.property name="x0"
     */
    //Paramter
    double x0;

    //Constructor
    public PointMassDistribution(double x0) {
        setParameters(x0);
    }

    public PointMassDistribution() {
        this(0);
    }

    /**
     * @uml.property name="x0"
     */
    public void setParameters(double x0) {
        this.x0 = x0;
        super.setParameters(x0, x0, 1, DISCRETE);
        name = "Point Mass ("+x0+") Distribution";
    }

    public double getDensity(double x) {
        if (x == x0) return 1;
        else return 0;
    }

    public double getMaxDensity() {
        return 1;
    }

    /**
     * @uml.property name="x0"
     */
    public double getMean() {
        return x0;
    }

    public double getVariance() {
        return 0;
    }

    public double getParameter(int i) {
        return x0;
    }

    public double simulate() {
        return x0;
    }

    public double getQuantile(double p) {
        return x0;
    }

    public double CDF(double x) {
        if (x < x0) return 0;
        else return 1;
    }

    public String getName() {
        return "Point Mass Distribution";
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Dirac_distribution");
    }

}

