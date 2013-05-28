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

/** This class models the Cauchy distribution */
public class CauchyDistribution extends StudentDistribution {
    //Constructor
    public CauchyDistribution() {
        super(1);
        name = "Cauchy Distribution";
    }

    /**
     * override just not let create valueSetters
     */
    public void initialize() {
    	name = "Cauchy Distribution";
    }

    /** This method sets the degrees of freedom to 1. */
    public void setDegrees(int n) {
        super.setDegrees(1);
        name = "Cauchy Distribution";
    }

    /**
     * This method computes the CDF. This overrides the corresponding method in
     * StudentDistribution.
     */
    public double getCDF(double x) {
        return 0.5 + Math.atan(x) / Math.PI;
    }

    /**
     * This method computes the quantile function. This overrides the
     * corresponding method in StudentDistribution.
     */
    public double getQuantile(double p) {
        return Math.tan(Math.PI * (p - 0.5));
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/CauchyDistribution.html");
    }

}

