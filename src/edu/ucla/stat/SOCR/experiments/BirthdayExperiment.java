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
 * The Birthday Experiment consists of selecting a sample of a specified size,
 * with replacement, from a population of a specified size. A match occurs if a
 * population object is selected more than once.
 */
public class BirthdayExperiment extends Experiment {
    //Variables
    private int popSize = 365, sampleSize = 20, balls = 60;
    private boolean matches;
    private double p = 0.411;
    //Objects
    private JPanel toolbar = new JPanel();
    private Urn urn = new Urn(balls);
    private BernoulliDistribution matchDist = new BernoulliDistribution(p);
    private RandomVariable matchRV = new RandomVariable(matchDist, "I");
    private RandomVariableGraph matchGraph = new RandomVariableGraph(matchRV);
    private RandomVariableTable matchTable = new RandomVariableTable(matchRV);
    private JLabel definitionJLabel = new JLabel("I: Duplication");

    /** Initialize the experiment: Add labels, scrollbars, graphs, and tables */
    public BirthdayExperiment() {
        setName("Brithday Experiment");
        createValueSetter("N", Distribution.DISCRETE, 5, 400, popSize);
        createValueSetter("n", Distribution.CONTINUOUS, 1, balls, sampleSize);

        //Miscellaneous
        matchGraph.showMoments(0);
        matchTable.showMoments(0);
        //Toolbars
        toolbar.add(definitionJLabel);
        addToolbar(toolbar);
        //Graph panel
        addGraph(urn);
        addGraph(matchGraph);
        //Table panel
        addTable(matchTable);
        reset();
    }

    /** Scrollbar events: change the population size or the sample size */
    public void update(Observable o, Object arg) {
        popSize = getValueSetter(0).getValueAsInt();
        sampleSize = getValueSetter(1).getValueAsInt();
        setParameters();
    }

    /** Compute the probability of a match */
    public void setParameters() {
        double prod = 1;
        if (sampleSize > popSize) p = 1.00;
        else {
            for (int i = 1; i <= sampleSize; i++)
                prod = prod * (popSize - i + 1) / popSize;
            p = 1 - prod;
        }
        matchDist.setProbability(p);
        reset();
    }

    /** This method defines the birthday experiment */
    public void doExperiment() {
        super.doExperiment();
        matches = false;
        Ball ball;
        urn.sample(sampleSize, popSize, Urn.WITH_REPLACEMENT);
        for (int i = 0; i < sampleSize; i++) {
            ball = urn.getBall(i);
            ball.setColor(Color.green);
            for (int j = 0; j < i; j++) {
                if (ball.getValue() == urn.getBall(j).getValue()) {
                    ball.setColor(Color.red);
                    matches = true;
                    break;
                }
            }
        }
        if (matches) matchRV.setValue(1);
        else matchRV.setValue(0);
    }

    /** Update graphs and tables */
    public void update() {
        super.update();
        urn.showBalls(sampleSize);
        getRecordTable().append("\t" + format(matchRV.getValue()));
        matchGraph.repaint();
        matchTable.update();
    }

    /** Reset the experiment */
    public void reset() {
        super.reset();
        urn.showBalls(0);
        getRecordTable().append("\tI");
        matchRV.reset();
        matchGraph.reset();
        matchTable.reset();
    }

    /** Single step the experiment */
    public void step() {
        doExperiment();
        update();
        try {
            if (matches) play("sounds/1.au");
            else play("sounds/0.au");
        } catch (Exception e) {
            ;
        }

    }
}

