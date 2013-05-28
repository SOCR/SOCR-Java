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

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the beta coin experiment. The probability of heads p for a
 * coin is given a prio rbeta distribution. The coin is tossed n time, and the
 * posterior distribution of p is obtained.
 */
public class BetaCoinExperiment extends Experiment {
    private int n = 10, coins = 60, heads;
    private double p = 0.5, a = 1, b = 1, pEstimate, bias, mse, empiricalBias,
            empiricalMSE;
    private CoinBox coinBox = new CoinBox(coins);
    private JTextArea statTable = new SOCRApplet.SOCRTextArea();
    private JLabel definitionJLabel = new JLabel("Y: # heads, U: p Estimate");
    private BetaGraph betaGraph = new BetaGraph(a, b, n, p);

    /**
     * This method initializes the experiment by setting up the toolbar,
     * scrollbars. label, coin panel, and graphs
     */
    public BetaCoinExperiment() {
        setName("Beta Coin Experiment");
    }
    
    public void initialize() {
        createValueSetter("n", Distribution.DISCRETE, 1, 60, n);
        createValueSetter("p", Distribution.CONTINUOUS, 0, 1, 0);
        createValueSetter("a", Distribution.CONTINUOUS, 1, 10, 1);
        createValueSetter("b", Distribution.CONTINUOUS, 1, 10, 1);
        SOCRToolBar toolbar3 = new SOCRToolBar();
        toolbar3.add(definitionJLabel);
        addToolbar(toolbar3);

        addGraph(coinBox);
        addGraph(betaGraph);

        addTable(statTable);
        setParameters();
    }

    /**
     * This method defines the experiment: the coins are tossed and then the
     * estimate of p and the empirical bias and empirical mean square error are
     * computed
     */
    public void doExperiment() {
        super.doExperiment();
        coinBox.toss(n);
        heads = coinBox.getHeadCount(n);
        pEstimate = (heads + a) / (n + a + b);
        int runs = getTime();
        empiricalBias = ((runs - 1) * empiricalBias + (pEstimate - p)) / runs;
        empiricalMSE = ((runs - 1) * empiricalMSE + (pEstimate - p)
                * (pEstimate - p)) / runs;
    }

    /**
     * This method resets the experiment, including the record table, the beta
     * graph, the statistcs table
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tY\tU");
        coinBox.showCoins(0);
        betaGraph.setParameters(a, b, n, p);
        betaGraph.repaint();
        statTable.setText("U\tDist\tData\n" + "Bias\t" + format(bias) + "\nMSE\t"
                + format(mse));
    }

    /**
     * This method updates the experiment, including the record table,
     * statistics table, and beta graph
     */
    public void update() {
        super.update();
        coinBox.showCoins(n);
        getRecordTable().append("\t" + heads + "\t" + format(pEstimate));
        statTable.setText("U\tDist\tData\n" + "Bias\t" + format(bias) + "\t"
                + format(empiricalBias) + "\nMSE\t" + format(mse) + "\t"
                + format(empiricalMSE));
        betaGraph.setValue(heads);
        betaGraph.repaint();
    }

    /**
     * This method sets the true bias and mean square error, after the scrollbar
     * parameters have changed
     */
    public void setParameters() {
        coinBox.setProbability(p);
        bias = (a - p * a - p * b) / (a + b + n);
        mse = (p * (n - 2 * a * a - 2 * a * b) + p * p
                * (a * a + b * b + 2 * a * b - n) + a * a)
                / ((n + a + b) * (n + a + b));
        reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        p = getValueSetter(1).getValue();
        a = getValueSetter(2).getValue();
        b = getValueSetter(3).getValue();
        setParameters();
    }
}

