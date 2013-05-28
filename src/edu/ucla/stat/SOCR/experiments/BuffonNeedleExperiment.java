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
 * Buffon's needle experiment consists of dropping a needle on a wooden floor.
 * The event of interest is that the needle crosses a floorboard crack.
 */
public class BuffonNeedleExperiment extends Experiment {
    //Variables
    private double angle, distance, estimate;
    private double length = 0.5;
    //Objects
    private JLabel definitionLabel = new JLabel(
            "X: angle, Y: distance, I: crack crossing");
    private JPanel toolbar = new JPanel();
    private NeedleScatter scatterPlot = new NeedleScatter(length);
    private NeedleFloor floor = new NeedleFloor(length);
    private EstimateGraph piGraph = new EstimateGraph(Math.PI, 5, 1);
    private JTextArea piTable = new SOCRApplet.SOCRTextArea();
    private BernoulliDistribution crossDist = new BernoulliDistribution(2 * length
            / Math.PI);
    private RandomVariable crossRV = new RandomVariable(crossDist, "I");
    private RandomVariableGraph crossGraph = new RandomVariableGraph(crossRV);
    private RandomVariableTable crossTable = new RandomVariableTable(crossRV);

    /** Initialize the experiment: tables, graphs, toolbar */
    public BuffonNeedleExperiment() {
        setName("Buffon's Needle Experiment");
        createValueSetter("L", Distribution.CONTINUOUS, 0, 1);

        crossGraph.showMoments(0);
        crossGraph.setMargins(35, 20, 20, 20);
        crossTable.showMoments(0);
        piTable.setEditable(false);
        //Toolbar
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Graphs
        addGraph(floor);
        addGraph(scatterPlot);
        addGraph(crossGraph);
        addGraph(piGraph);
        //Tables
        addTable(crossTable);
        addTable(piTable);
        //Size
        scatterPlot.setSize(150, 150);
        floor.setSize(150, 150);
        crossGraph.setSize(150, 150);
        piGraph.setSize(150, 150);
        reset();
    }

    /** Scrollbar event: set the length of the needle */
    public void update(Observable o, Object arg) {
        length = getValueSetter(0).getValue();
        if (length == 0) length = 0.05;
        floor.setLength(length);
        scatterPlot.setLength(length);
        crossDist.setProbability(2 * length / Math.PI);
        reset();
    }

    /** This method defines the experiment */
    public void doExperiment() {
        super.doExperiment();
        floor.setValues();
        distance = floor.getDistance();
        angle = floor.getAngle();
        if (floor.crossEvent()) crossRV.setValue(1);
        else crossRV.setValue(0);
        estimate = 2 * length * getTime() / crossRV.getIntervalData().getFreq(1);
        scatterPlot.drawPoint(angle, distance);
    }

    /**
     * Update the displays: floor, pi graph, random variable table and graph,
     * record table
     */
    public void update() {
        super.update();
        floor.repaint();
        piGraph.setEstimate(estimate);
        piGraph.repaint();
        crossGraph.repaint();
        crossTable.update();
        getRecordTable().append(
                "\t" + format(angle) + "\t" + format(distance) + "\t"
                        + format(crossRV.getValue()));
        piTable.setText("Pi\t3.142\n" + "Pi Est\t" + format(estimate));
    }

    /**
     * This method runs the experiment one time and plays sounds depending on
     * the outcome
     */
    public void step() {
        doExperiment();
        update();
        try {
            if (crossRV.getValue() == 1) play("sounds/1.au");
            else play("sounds/0.au");
        } catch (Exception e) {
            ;
        }
    }

    /**
     * Reset the experiment: record table, pi table, floor, scatter plot, pi
     * graph random variable graph and table
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tX\tY\tI");
        piTable.setText("Pi\t3.142\nPi Est");
        floor.reset();
        scatterPlot.reset();
        piGraph.setEstimate(0);
        piGraph.repaint();
        crossRV.reset();
        crossGraph.reset();
        crossTable.reset();
    }
}
