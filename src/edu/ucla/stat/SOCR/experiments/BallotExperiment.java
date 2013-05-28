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
 * This class implements the classical ballot experiment: the winner has a
 * specified number of votes and the loser has a specified, smaller number of
 * votes. The votes are counted in a random order. The event of interest is that
 * the winner is always ahead in the vote count
 */
public class BallotExperiment extends Experiment {
    private int winnerCount = 10, loserCount = 5;

    private BallotGraph ballotGraph = new BallotGraph(winnerCount, loserCount);
    private BernoulliDistribution dist = new BernoulliDistribution(ballotGraph
            .getProbability());
    private RandomVariable indicatorRV = new RandomVariable(dist, "I");
    private RandomVariableGraph rvGraph = new RandomVariableGraph(indicatorRV);
    private RandomVariableTable rvTable = new RandomVariableTable(indicatorRV);

    public BallotExperiment() {
        setName("Ballot Experiment");
    }
    
    public void initialize() {
        createValueSetter("Winner", Distribution.DISCRETE, 1, 20, winnerCount);
        createValueSetter("Loser", Distribution.DISCRETE, 0, winnerCount-1, loserCount);

        rvGraph.showMoments(0);
        addGraph(ballotGraph);
        addGraph(rvGraph);

        rvTable.showMoments(0);
        addTable(rvTable);
    }

    /**
     * This method defines the experiment: the random walk is performed and the
     * ballot event is determined.
     */
    public void doExperiment() {
        super.doExperiment();
        ballotGraph.walk();
        if (ballotGraph.ballotEvent()) indicatorRV.setValue(1);
        else indicatorRV.setValue(0);
        rvTable.update();
    }

    /**
     * This method runs the the experiment one time, and add sounds depending on
     * the outcome of the experiment.
     */
    public void step() {
        doExperiment();
        update();
        try {
            if (indicatorRV.getValue() == 1) play("images/sounds/1.au");
            else play("images/sounds/0.au");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method resets the experiment, including the record table, the ballot
     * graph, the random variable graph and table, and the indicator variable
     * for the ballot event.
     */
    public void reset() {
        super.reset();
        getRecordTable().append("");
        ballotGraph.reset();
        indicatorRV.reset();
        rvGraph.reset();
        rvTable.reset();
    }

    /**
     * This method updates the record table, the random variable graph and
     * table, and the ballot graph.
     */
    public void update() {
        super.update();
        ballotGraph.repaint();
        getRecordTable().append("\t" + format(indicatorRV.getValue()));
        rvGraph.repaint();
        rvTable.update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        winnerCount = getValueSetter(0).getValueAsInt();
        loserCount = getValueSetter(1).getValueAsInt();
        if (loserCount >= winnerCount) {
            if (arg == getValueSetter(0)) {
                getValueSetter(1).setValue(winnerCount - 1);
            } else if (arg == getValueSetter(1)) {
                getValueSetter(0).setValue(loserCount + 1);
            }
            return;
        }
        setDistribution();
        super.update();
    }

    /**
     * This method sets the parameters of the ballot graph and the distribution
     * of the indicator random variable.
     */
    public void setDistribution() {
        double p = (double) (winnerCount - loserCount) / (winnerCount + loserCount);
        ballotGraph.setParameters(winnerCount, loserCount);
        dist.setProbability(ballotGraph.getProbability());
        reset();
    }
}
