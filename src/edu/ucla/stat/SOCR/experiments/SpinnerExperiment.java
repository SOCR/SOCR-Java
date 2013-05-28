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

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/*
 * This experiment spins a spinner (the number of divisions is specified by the
 * user) and records the value of each spin. The value is a number from 1 to n,
 * where n is the number of divisions.
 */
public class SpinnerExperiment extends Experiment implements Runnable {
    //Variables
    private int n = 4;
    private int spinValue;
    //Objects
    private JPanel toolbar = new JPanel();
    private JButton probButton = new JButton("Spinner Probabilities");
    private Spinner spinner = new Spinner();
    private RandomVariable rv = new RandomVariable(new DiscreteUniformDistribution(
            1, n, 1), "N");
    private RandomVariableGraph rvGraph = new RandomVariableGraph(rv);
    private RandomVariableTable rvTable = new RandomVariableTable(rv);
    private Thread thread;
    private ProbabilityDialog probDialog;

    private DieDistribution dieDist = new DieDistribution(DieDistribution.FAIR);

    /** This method initializes the experiment */
    public SpinnerExperiment() {
        setName("Spinner Experiment");
        createValueSetter("n=", Distribution.DISCRETE, 1, 6, n);
        probButton.addActionListener(this);
        //Graphs
        addGraph(spinner);
        addGraph(rvGraph);
        //Toolbar
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(probButton);
        addToolbar(toolbar);
        //Table
        addTable(rvTable);
    }

    /** This method sets up the probability dialog box, when the applet starts */
    public void start() {
        probDialog = new ProbabilityDialog(GUIUtil.getTopestParent(applet), "Spinner Probabilities", 10);
        probDialog.setLabels(new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
                "9", "10" });
        probDialog.setProbabilities(new double[] { 0.15, 0.05, 0.08, 0.12, 0.14,
                0.06, 0.07, 0.13, 0.15, 0.05 });
    }

    /** This method controls the scrollbar for the number of divisions */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        spinner.setDivisions(n);
        rv = new RandomVariable(new DiscreteUniformDistribution(1, n, 1), "N");
        rvGraph.setRandomVariable(rv);
        rvTable.setRandomVariable(rv);
        reset();

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == probButton) {
            /** ********************* */
            Point fp = applet.getLocationOnScreen(), dp;
            Dimension fs = applet.getSize(), ds = probDialog.getSize();
            dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2
                    - ds.height / 2);
            probDialog.setLocation(dp);
            probDialog.setProbabilities(dieDist.getProbabilities());
            probDialog.setVisible(true);
            if (probDialog.isOK()) {
                dieDist.setProbabilities(probDialog.getProbabilities());
                reset();
            }
            /*******************************************************************
             * if (frame ==null) System.err.println("NULL Frame Object in
             * DiceExperiment!"); else {Point fp = frame.getLocationOnScreen(),
             * dp; Dimension fs = frame.getSize(); Dimension ds =
             * dieProbabilityDialog.getSize(); dp = new Point(fp.x + fs.width /
             * 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
             * dieProbabilityDialog.setLocation(dp); } if (frame ==null) {
             * System.err.println("NULL dieProbabilityDialog Object in
             * DiceExperiment!"); dieProbabilityDialog = new
             * DieProbabilityDialog(new Frame()); }
             * dieProbabilityDialog.setProbabilities(dieDist.getProbabilities());
             * dieProbabilityDialog.show(); if (dieProbabilityDialog.isOK()){
             * dieDist.setProbabilities(dieProbabilityDialog.getProbabilities());
             * setDistributions(); }
             ******************************************************************/

        } else super.actionPerformed(e);
    }

    /** This method does the experiment */
    public void doExperiment() {
        super.doExperiment();
        spinner.spin();
        rv.setValue(spinner.getValue());
    }

    /** This method updates the display as the experiment runs */
    public void update() {
        spinValue = spinner.getValue();
        super.update();
        spinner.repaint();
        getRecordTable().append("\t" + spinValue);
        rvGraph.repaint();
        rvTable.update();
    }

    /** This method resets the experiment */
    public void reset() {
        super.reset();
        getRecordTable().append("\tN");
        rv.reset();
        rvGraph.repaint();
        rvTable.update();
        spinner.setAngle(0);
        spinner.repaint();
    }

    /* This method runs the experiment once. */
    public void step() {
        thread = new Thread(spinner);
        doExperiment();
        thread.start();
        update();
    }
}
