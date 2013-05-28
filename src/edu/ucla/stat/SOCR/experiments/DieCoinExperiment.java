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
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * The die-coin experiment consists of rolling a die, and then tossing a coin
 * the number of times shown on the die
 */
public class DieCoinExperiment extends Experiment {
    //Variables
    private double p = 0.5;
    private int dieScore, headCount;
    //Objects
    private JLabel definitionLabel = new JLabel("X: Die Score, Y: Number of Heads");
    private JButton dieButton = new JButton("Die Probabilities");
    private JPanel toolbar = new JPanel();
    private JPanel objectPanel = new JPanel();
    private Die die = new Die();
    private Coin[] coin = new Coin[6];
    private DieDistribution dieDist = new DieDistribution(DieDistribution.FAIR);
    private BinomialRandomNDistribution headsDist = new BinomialRandomNDistribution(
            dieDist, p);
    private RandomVariable heads = new RandomVariable(headsDist, "Y");
    private RandomVariableGraph headsGraph = new RandomVariableGraph(heads);
    private RandomVariableTable headsTable = new RandomVariableTable(heads);
    private DieProbabilityDialog dieProbabilityDialog;

    //Intialize
    public DieCoinExperiment() {
        setName("Die Coin Experiment");
        dieButton.addActionListener(this);
        createValueSetter("p", Distribution.CONTINUOUS, 0, 1);

        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(dieButton);
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Graphs
        objectPanel.setLayout(new GridLayout(1, 7, 1, 1));
        objectPanel.add(die);
        for (int i = 0; i < 6; i++) {
            coin[i] = new Coin(p, 32);
            objectPanel.add(coin[i]);
        }
        addGraph(objectPanel);
        addGraph(headsGraph);
        //Table
        addTable(headsTable);
        reset();
    }

    public void doExperiment() {
        super.doExperiment();
        headCount = 0;
        dieScore = (int) (dieDist.simulate());
        die.setValue(dieScore);
        for (int i = 0; i < dieScore; i++) {
            if (Math.random() < p) {
                coin[i].setValue(1);
                headCount++;
            } else coin[i].setValue(0);
        }
        heads.setValue(headCount);
    }

    /**
     * This method runs the the experiment one time, and add sounds depending on
     * the outcome of the experiment.
     */
    public void step() {
        doExperiment();
        update();
        String url = "sounds/" + headCount + ".au";
        try {
            play(url);
        } catch (Exception e) {
            ;
        }
    }

    public void reset() {
        super.reset();
        heads.reset();
        getRecordTable().append("\tX\tY");
        headsGraph.reset();
        headsTable.reset();
        die.setVisible(false);
        for (int i = 0; i < 6; i++)
            coin[i].setVisible(false);
    }

    public void update() {
        super.update();
        die.setVisible(true);
        die.repaint();
        for (int i = 0; i < 6; i++) {
            coin[i].setVisible(i < dieScore);
            coin[i].repaint();
        }
        getRecordTable().append("\t" + dieScore + "\t" + headCount);
        headsGraph.repaint();
        headsTable.update();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dieButton) {
            Point fp = applet.getLocationOnScreen(), dp;
            Dimension fs = applet.getSize();
            Dimension ds = dieProbabilityDialog.getSize();
            dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2
                    - ds.height / 2);

            dieProbabilityDialog.setProbabilities(dieDist.getProbabilities());
            dieProbabilityDialog.setVisible(true);
            if (dieProbabilityDialog.isOK()) {
                dieDist.setProbabilities(dieProbabilityDialog.getProbabilities());
                setDistribution();
            }
        } else super.actionPerformed(e);
    }

    public void update(Observable o, Object arg) {
        p = getValueSetter(0).getValue();
        setDistribution();
    }

    public void setDistribution() {
        headsDist.setParameters(dieDist, p);
        reset();
    }
}

