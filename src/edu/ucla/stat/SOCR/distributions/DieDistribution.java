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

/** Distribution for a standard 6-sided die */
public class DieDistribution extends FiniteDistribution {
    public final static int FAIR = 0;
    public final static int FLAT16 = 1;
    public final static int FLAT25 = 2;
    public final static int FLAT34 = 3;
    public final static int LEFT = 4;
    public final static int RIGHT = 5;

    /**
     * General Constructor: creates a new die distribution with specified
     * probabilities
     */
    public DieDistribution(double[] p) {
        super(1, 6, 1, p);
    }

    /** Special constructor: creates a new die distribution of a special type */
    public DieDistribution(int n) {
        super(1, 6, 1);
        setProbabilities(n);
    }

    /** Default constructor: creates a new fair die distribution */
    public DieDistribution() {
        this(FAIR);
    	
    	//super(1, 6, 1, new double[] {1.0/6,1.0/6,1.0/6,1.0/6,1.0/6,1.0/6});
    	
        name = "Die Distribution";
    }

    public void initialize() {
        /** createValueSetter("Prob of 1", CONTINUOUS, 0, 1, 1);
        createValueSetter("Prob of 2", CONTINUOUS, 0, 1, 1);
        createValueSetter("Prob of 3", CONTINUOUS, 0, 1, 1);
        createValueSetter("Prob of 4", CONTINUOUS, 0, 1, 1);
        createValueSetter("Prob of 5", CONTINUOUS, 0, 1, 1);
        createValueSetter("Prob of 6", CONTINUOUS, 0, 1, 1);
        **/
    	createValueSetter("Die Type", DISCRETE, 0, 5, FAIR);
    }

    public void valueChanged() {
        /** double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        double v4 = getValueSetter(3).getValue();
        double v5 = getValueSetter(4).getValue();
        double v6 = getValueSetter(5).getValue();
        setProbabilities(new double[] { v1, v2, v3, v4, v5, v6 });
        **/
    	int v = getValueSetter(0).getValueAsInt();
        setProbabilities(v);
        System.out.println("Type changed to: "+ v);
    }

    public void valueChanged(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
        	int v = getValueSetter(0).getValueAsInt();
        	setProbabilities(v);
        	//System.out.println("Type changed to: "+ v); 
        } 
    }
    
    /** Specify probabilities of a special type */
    public void setProbabilities(int n) {
        if (n == FLAT16) setProbabilities(new double[] { 1.0 / 4, 1.0 / 8, 1.0 / 8,
                1.0 / 8, 1.0 / 8, 1.0 / 4 });
        else if (n == FLAT25) setProbabilities(new double[] { 1.0 / 8, 1.0 / 4,
                1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 8 });
        else if (n == FLAT34) setProbabilities(new double[] { 1.0 / 8, 1.0 / 8,
                1.0 / 4, 1.0 / 4, 1.0 / 8, 1.0 / 8 });
        else if (n == LEFT) setProbabilities(new double[] { 1.0 / 21, 2.0 / 21,
                3.0 / 21, 4.0 / 21, 5.0 / 21, 6.0 / 21 });
        else if (n == RIGHT) setProbabilities(new double[] { 6.0 / 21, 5.0 / 21,
                4.0 / 21, 3.0 / 21, 2.0 / 21, 1.0 / 21 });
        else setProbabilities(new double[] { 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6,
                1.0 / 6, 1.0 / 6 });
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://www.socr.ucla.edu/htmls/dist");
    }

}

