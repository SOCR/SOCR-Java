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

import java.awt.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the general birthday experiment: a sample of a specified
 * size is chosen with replacement from a population of a specified size. The
 * random variable of interest is the number of distinct values in the sample
 */
public class GeneralBirthdayExperiment extends Experiment {
    //Variables
    private int popSize = 50, sampleSize = 20, balls = 60, occupiedCount;
    //Objects
    private Urn urn = new Urn(balls);
    private JPanel toolbar = new JPanel();
    private BirthdayDistribution dist = new BirthdayDistribution(popSize,
            sampleSize);
    private RandomVariable occupiedRV = new RandomVariable(dist, "V");
    private RandomVariableGraph occupiedGraph = new RandomVariableGraph(occupiedRV);
    private RandomVariableTable occupiedTable = new RandomVariableTable(occupiedRV);
    private JLabel definitionLabel = new JLabel("V: number of distinct values");

    /**
     * This method initializes the experiment, including the set up of the
     * toolbar with scrollbars, the panel containing the balls, the random
     * variable graph and table.
     */
    public GeneralBirthdayExperiment() {
        setName("General Birthday Experiment");
        //Event listeners
        createValueSetter("N", Distribution.DISCRETE, 1, 100, popSize);
        createValueSetter("n", Distribution.DISCRETE, 1, balls, sampleSize);
        //Toolbar
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Graphs
        addGraph(urn);
        addGraph(occupiedGraph);
        //Tables
        addTable(occupiedTable);
        reset();
    }

    /**
     * This method handles the scrollbar events for adjusting the population
     * size and sample size.
     */
    public void update(Observable o, Object arg) {
        popSize = getValueSetter(0).getValueAsInt();
        sampleSize = getValueSetter(1).getValueAsInt();
        setParameters();
    }

    /**
     * This method sets the paramerters of the distribution when these
     * parameters have been changed with the scrollbars
     */
    public void setParameters() {
        dist.setParameters(popSize, sampleSize);
        reset();
    }

    /**
     * This method defines the experiment. A sample of the specified size is
     * selected with replacement. The number of distinct values is recorded and
     * duplicate sample values recorded
     */
    public void doExperiment() {
        super.doExperiment();
        occupiedCount = 0;
        boolean match;
        Ball ball;
        urn.sample(sampleSize, popSize, Urn.WITH_REPLACEMENT);
        for (int i = 0; i < sampleSize; i++) {
            match = false;
            ball = urn.getBall(i);
            for (int j = 0; j < i; j++)
                if (ball.getValue() == urn.getBall(j).getValue()) match = true;
            if (match) ball.setColor(Color.red);
            else {
                ball.setColor(Color.green);
                occupiedCount++;
            }
        }
        occupiedRV.setValue(occupiedCount);
    }

    /**
     * This method updates the display, including the ball, random variable
     * graph and table and the record table
     */
    public void update() {
        super.update();
        urn.showBalls(sampleSize);
        getRecordTable().append("\t" + occupiedCount);
        occupiedGraph.repaint();
        occupiedTable.update();
    }

    /**
     * This method resets the experiment, including the balls, the random
     * variable graph and table, and the record table
     */
    public void reset() {
        super.reset();
        urn.showBalls(0);
        getRecordTable().append("\tV");
        occupiedRV.reset();
        occupiedGraph.reset();
        occupiedTable.reset();
    }
}

