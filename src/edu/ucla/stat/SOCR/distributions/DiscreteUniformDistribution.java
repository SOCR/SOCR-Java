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

import java.util.*;

import edu.ucla.stat.SOCR.core.*;

/**
 * The discrete uniform distribution on a finite set. <a
 * href="http://mathworld.wolfram.com/UniformDistribution.html">
 * http://mathworld.wolfram.com/UniformDistribution.html </a>.
 */
public class DiscreteUniformDistribution extends Distribution {
    double values;

    public DiscreteUniformDistribution(double a, double b, double w) {
        setParameters(a, b, w);
        name = "Discrete Uniform Distribution";
    }

    public DiscreteUniformDistribution() {
        this(1, 6, 1);
    }

    public void initialize() {
        createValueSetter("Minimum", DISCRETE, -100, 100);
        createValueSetter("Maximum", DISCRETE, -99, 101);
        createValueSetter("Step", DISCRETE, 1, 10);
    }

    public void valueChanged(Observable o, Object arg) {
        if (arg == getValueSetter(2)) return; //step change don't have to
                                              // update

        int v1 = getValueSetter(0).getValueAsInt();
        int v2 = getValueSetter(1).getValueAsInt();
        int v3 = getValueSetter(2).getValueAsInt();
        if (v2 < (v1 + v3)) {
            if (arg == getValueSetter(0)) { //to adjust max or step
                v2 = v1 + v3;
                if (v2 > 101) {
                    v2 = 101;
                    v3 = v2 - v1;
                    getValueSetter(2).setValue(v3);
                }
                getValueSetter(1).setValue(v2);
            } else if (arg == getValueSetter(1)) {
                v1 = v2 - v3;
                if (v1 < -100) {
                    v1 = -100;
                    v3 = v2 - v1;
                    getValueSetter(2).setValue(v3);
                }
                getValueSetter(0).setValue(v1);
            }
            return;
        }
        setParameters(v1, v2, v3);
    }

    public void setParameters(double a, double b, double w) {
        super.setParameters(a, b, w, DISCRETE);
    }

    public double getDensity(double x) {
        if (getDomain().getLowerValue() <= x & x <= getDomain().getUpperValue()) 
        	return 1.0 / getDomain().getSize();
        else return 0;
    }

    public double getMaxDensity() {
        return 1.0 / getDomain().getSize();
    }

    public double simulate() {
        return (int)(getDomain().getLowerValue() + 
        		Math.random() * getDomain().getSize()* getDomain().getWidth());
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/UniformDistribution.html");
    }

}

