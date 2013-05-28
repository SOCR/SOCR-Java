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
package edu.ucla.stat.SOCR.experiments;

import java.util.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the famous problem of points experiment. Two player engage
 * in Bernoulli trials. The event of interest is that player 1 wins n points
 * before playeer 2 wins m points
 */
public class PointExperiment extends Experiment {
    private int                   n        = 10, m = 9, coins = 60, trials, heads;
    double                        p        = 0.5, winProb = 0.407;
    private CoinBox               coinBox  = new CoinBox(coins);
    private BernoulliDistribution winDist  = new BernoulliDistribution(winProb);
    private RandomVariable        winRV    = new RandomVariable(winDist, "I");
    private RandomVariableGraph   winGraph = new RandomVariableGraph(winRV);
    private RandomVariableTable   winTable = new RandomVariableTable(winRV);

    /**
     * This method initializes the experiment, including the toolbar with
     * scrollbars and labels, the random variable table and graph, the record
     * table and the coin panel
     */
    public PointExperiment() {
        setName("Problem of Points Experiment");
        createValueSetter("n", Distribution.DISCRETE, 1, coins / 2, n);
        createValueSetter("m", Distribution.DISCRETE, 1, coins / 2, m);
        createValueSetter("p", 0, 100, 0.01);

        winGraph.showMoments(0);
        addGraph(coinBox);
        addGraph(winGraph);
        //Table panel
        winTable.showMoments(0);
        addTable(winTable);
    }

    /**
     * This method handles the scrollbar events for adjusting the number of
     * points needed by the two players, and the probability of winning a point
     */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        m = getValueSetter(1).getValueAsInt();
        p = getValueSetter(2).getValue();
        setParameters();
    }

    /**
     * This method sets the parameter of the win distribution when the number of
     * points needed for a player or the point probability have changed
     */
    public void setParameters() {
        coinBox.setProbability(p);
        winProb = 0;
        for (int i = n; i < n + m; i++)
            winProb = winProb + Distribution.comb(i - 1, n - 1) * Math.pow(p, n)
                    * Math.pow(1 - p, i - n);
        winDist.setProbability(winProb);
        reset();
    }

    /**
     * This method defines the experiment. Bernoulli trials are conducted until
     * one player has won her required number of points
     */
    public void doExperiment() {
        super.doExperiment();
        trials = 0;
        heads = 0;
        Coin coin;
        while ((heads < n) & (trials - heads < m)) {
            coin = coinBox.getCoin(trials);
            coin.toss();
            heads = heads + coin.getValue();
            trials++;
        }
        if (heads >= n) winRV.setValue(1);
        else winRV.setValue(0);
    }

    /**
     * This method runs the experiment one time and plays a sound depending on
     * the outcome
     */
    public void step() {
        doExperiment();
        update();
        try {
            if (heads >= n) play("sounds/1.au");
            else play("sounds/0.au");
        } catch (Exception e) {
            ;
        }
    }

    /**
     * This method updates the display, including the coins, the record table,
     * and the random variable graph and table
     */
    public void update() {
        super.update();
        coinBox.showCoins(trials);
        getRecordTable().append(
                "\t" + heads + "\t" + (trials - heads) + "\t"
                        + format(winRV.getValue()));
        winGraph.repaint();
        winTable.update();
    }

    /**
     * This method resets the experiment, including the coins, the record table,
     * and the random variable graph and table
     */
    public void reset() {
        super.reset();
        coinBox.showCoins(0);
        getRecordTable().append("\tHeads\tTails \tI");
        winRV.reset();
        winGraph.reset();
        winTable.reset();
    }
}

