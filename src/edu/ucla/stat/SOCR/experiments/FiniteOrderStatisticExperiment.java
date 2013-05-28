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
 * This class models the experiment of selecting a sample of a specified size
 * (without replacement) from from the population 1, 2, ..., N. The order
 * statistics are the random variables of interest.
 */
public class FiniteOrderStatisticExperiment extends Experiment {
    private int populationSize = 10, sampleSize = 5, order = 1;
    private int[] orderStatistic = new int[sampleSize];

    private Urn urn = new Urn(60);
    private FiniteOrderStatisticDistribution dist = new FiniteOrderStatisticDistribution(
            populationSize, sampleSize, order);
    private RandomVariable orderRV = new RandomVariable(dist, "X(1)");
    private RandomVariableGraph orderGraph = new RandomVariableGraph(orderRV);
    private RandomVariableTable orderTable = new RandomVariableTable(orderRV);

    /**
     * This method initializes the experiment, including the ball panel, the
     * random variable table and graph, the toolbar with the scrollbars and
     * labels, and the record table.
     */
    public FiniteOrderStatisticExperiment() {
        setName("Finite Order Statistic Experiment");
        createValueSetter("N", Distribution.DISCRETE, 1, 100, populationSize);
        createValueSetter("n", Distribution.DISCRETE, 1, populationSize, sampleSize);
        createValueSetter("k", Distribution.DISCRETE, 1, sampleSize, order);
        addGraph(urn);
        addGraph(orderGraph);
        //Tables
        addTable(orderTable);
        reset();
    }

    /**
     * This event handles the scroll events that correspond to changes in the
     * parameters: the population size, the sample size, and the order.
     */
    public void update(Observable o, Object arg) {

        if (arg == getValueSetter(0)) {
            populationSize = getValueSetter(0).getValueAsInt();
            if (sampleSize > populationSize) {
                getValueSetter(1).setValue(populationSize);
                return;
            }
            orderStatistic = new int[sampleSize];
            setDistributions();
        } else if (arg == getValueSetter(1)) {
            sampleSize = getValueSetter(1).getValueAsInt();
            if (order > sampleSize) {
                getValueSetter(2).setValue(sampleSize);
                return;
            }

            orderStatistic = new int[sampleSize];
            setDistributions();
        } else if (arg == getValueSetter(2)) {
            order = getValueSetter(2).getValueAsInt();
            setDistributions();
        }
    }

    /** This method sets the distribution after the parameters have changed. */
    public void setDistributions() {
        dist.setParameters(populationSize, sampleSize, order);
        orderRV.setName("X(" + order + ")");
        reset();
    }

    /**
     * This method resets the experiment, including the random variable table
     * and graph, and the ball panel.
     */
    public void reset() {
        super.reset();
        urn.showBalls(0);
        orderRV.reset();
        orderTable.reset();
        String recordText = "";
        for (int i = 1; i <= sampleSize; i++)
            recordText = recordText + "\tX(" + i + ")";
        getRecordTable().append(recordText);
        orderGraph.reset();
    }

    /**
     * This method defines the experiment. The sample is selected and the values
     * of the order statistics computed.
     */
    public void doExperiment() {
        super.doExperiment();
        boolean smallest;
        int x;
        urn.sample(sampleSize, populationSize, Urn.WITHOUT_REPLACEMENT);
        for (int i = 0; i < sampleSize; i++) {
            smallest = true;
            x = urn.getBall(i).getValue();
            for (int j = i - 1; j >= 0; j = j - 1) {
                if (orderStatistic[j] <= x) {
                    orderStatistic[j + 1] = x;
                    smallest = false;
                    break;
                }
                orderStatistic[j + 1] = orderStatistic[j];
            }
            if (smallest) orderStatistic[0] = x;
        }
        orderRV.setValue(orderStatistic[order - 1]);
    }

    /**
     * This method updates the experiment, including the ball panel, the random
     * variable and graph, and the record table.
     */
    public void update() {
        super.update();
        urn.showBalls(sampleSize);
        String orderText = "";
        for (int i = 0; i < sampleSize; i++) {
            orderText = orderText + "\t" + orderStatistic[i];
        }
        getRecordTable().append(orderText);
        orderGraph.repaint();
        orderTable.update();
    }
}

