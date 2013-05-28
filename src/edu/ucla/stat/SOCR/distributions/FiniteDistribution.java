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

import java.util.Observable;

import edu.ucla.stat.SOCR.core.*;

/**
 * A basic discrete distribution on a finite set of points, with specified
 * probabilities
 */
public class FiniteDistribution extends Distribution {
    private int n;

    /**
     * @uml.property name="prob"
     */
    private double[] prob;

    /**
     * Constructs a new finite distribution on a finite set of points with a
     * specified array of probabilities
     */
    public FiniteDistribution(double a, double b, double w, double[] p) {
        setParameters(a, b, w, p);
        name = "Finite Point-Set Distribution";
    }

    /** Constructs the uniform distribuiton on the finite set of points */
    public FiniteDistribution(double a, double b, double w) {
    	super.setParameters(a, b, w, DISCRETE);
    	n = getDomain().getSize();
        prob = new double[n];
        for (int i = 0; i < n; i++)
            prob[i] = 1.0 / n;
        setParameters(a, b, w, prob);
    }

    /**
     * This special constructor creates a new uniform distribution on {1, 2,
     * ..., 10}.
     */
    public FiniteDistribution() {
        this(1, 10, 1);
    }

    public void initialize() {
        createValueSetter("Minimum", DISCRETE, -100, 100, 1);
        createValueSetter("Maximum", DISCRETE, -99, 101, 10);
        createValueSetter("Step", DISCRETE, 1, 10, 1);
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
        setParameters(v1, v2, v3, prob);
    }

    /** This method sets the parameters: the domain and the probabilities. */
    public void setParameters(double a, double b, double w, double[] p) {
        super.setParameters(a, b, w, DISCRETE);
        n = getDomain().getSize();
        prob = new double[n];
        if (p.length != n) p = new double[n];
        double sum = 0;
        for (int i = 0; i < n; i++) {
            if (p[i] < 0) p[i] = 0;
            sum = sum + p[i];
        }
        if (sum == 0) for (int i = 0; i < n; i++)
            prob[i] = 1.0 / n;
        else for (int i = 0; i < n; i++)
            prob[i] = p[i] / sum;
    }

    /** Density function */
    public double getDensity(double x) {
        int j = getDomain().getIndex(x);
        if (0 <= j & j < n) return prob[j];
        else return 0;
    }

    /** Set the probabilities */
    public void setProbabilities(double[] p) {
        if (p.length != n) p = new double[n];
        double sum = 0;
        for (int i = 0; i < n; i++) {
            if (p[i] < 0) p[i] = 0;
            sum = sum + p[i];
        }
        if (sum == 0) for (int i = 0; i < n; i++)
            prob[i] = 1.0 / n;
        else for (int i = 0; i < n; i++)
            prob[i] = p[i] / sum;
    }

    /** This method gets the probability for a specified index */
    public double getProbability(int i) {
        if (i < 0) i = 0;
        else if (i >= n) i = n - 1;
        return prob[i];
    }

    /**
     * This method gets the probability vector.
     * @uml.property name="prob"
     */
    public double[] getProbabilities() {
        return prob;
    }

}

