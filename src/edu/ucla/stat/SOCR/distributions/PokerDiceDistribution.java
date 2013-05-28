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
// Poker Dice Distribution
package edu.ucla.stat.SOCR.distributions;

import edu.ucla.stat.SOCR.core.*;

public class PokerDiceDistribution extends Distribution {
    final static int c = 7776;

    public PokerDiceDistribution() {
        super.setParameters(0, 6, 1, DISCRETE);
        name = name();
    }

    public double getDensity(double x) {
        double d = 0;
        int i = (int) (x+0.5);
        switch (i) {
        case 0:
            d = 720.0 / c;
            break;
        case 1:
            d = 3600.0 / c;
            break;
        case 2:
            d = 1800.0 / c;
            break;
        case 3:
            d = 1200.0 / c;
            break;
        case 4:
            d = 300.0 / c;
            break;
        case 5:
            d = 150.0 / c;
            break;
        case 6:
            d = 6.0 / c;
            break;
        }
        return d;
    }

    public String name() {
        return "Poker Dice Distribution";
    }
}

