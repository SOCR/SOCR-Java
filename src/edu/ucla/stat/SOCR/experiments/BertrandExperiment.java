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

import java.awt.event.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * Betrand's experiment: Create a random chord on the unit circle and see if the
 * lenght of this chord is longer thant the length of a side of the inscribed
 * triangle.
 */
public class BertrandExperiment extends Experiment {
    public final static int UNIFORM_DISTANCE = 0;
    public final static int UNIFORM_ANGLE = 1;
    private int model = UNIFORM_DISTANCE;
    private JComboBox modelJComboBox = new JComboBox();
    private BertrandFloor floor = new BertrandFloor();
    private BernoulliDistribution dist = new BernoulliDistribution(0.5);
    private RandomVariable chord = new RandomVariable(dist, "I");
    private RandomVariableGraph chordGraph = new RandomVariableGraph(chord);
    private RandomVariableTable chordTable = new RandomVariableTable(chord);
    private JLabel definitionLabel = new JLabel(
            "D: distance, A: angle, (X, Y): point, I: D < 1/2");

    /**
     * Initialize the experiment: Set the name, add the toolbar with the model
     * choices; add the floor graph and the random variable graph; add the
     * record table and the random variable table
     */
    public BertrandExperiment() {
        setName("Bertrand Experiment");
    }
    
    public void initialize() {
        modelJComboBox.addItemListener(this);
        modelJComboBox.addItem("Uniform Distance");
        modelJComboBox.addItem("Uniform Angle");

        SOCRToolBar toolbar3 = new SOCRToolBar();
        toolbar3.add(modelJComboBox);
        toolbar3.add(definitionLabel);
        addToolbar(toolbar3);

        chordGraph.showMoments(0);
        addGraph(floor);
        addGraph(chordGraph);

        chordTable.showMoments(0);
        addTable(chordTable);
    }

    /** Determine which model is used: random distance or random angle */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == modelJComboBox) {
            model = modelJComboBox.getSelectedIndex();
            if (model == UNIFORM_DISTANCE) dist.setProbability(0.5);
            else dist.setProbability(1.0 / 3);
            reset();
        } else super.itemStateChanged(event);
    }

    /**
     * Perform the experiment: compute random distance or angle (depending on
     * the model), compute the x and y coordinates of the chord
     */
    public void doExperiment() {
        super.doExperiment();
        if (model == UNIFORM_DISTANCE) floor.setDistance();
        else floor.setAngle();
        if (floor.chordEvent()) chord.setValue(1);
        else chord.setValue(0);
    }

    /**
     * Update the display: record table, floor, random variable graph and table
     */
    public void update() {
        super.update();
        floor.repaint();
        getRecordTable().append(
                "\t" + format(floor.getDistance()) + "\t"
                        + format(floor.getAnlge()) + "\t"
                        + format(floor.getXCoordinate()) + "\t"
                        + format(floor.getYCoordinate()) + "\t"
                        + format(chord.getValue()));
        chordGraph.repaint();
        chordTable.update();
    }

    /** Single step. Perform the experiment and add sounds */
    public void step() {
        doExperiment();
        update();
        try {
            if (floor.chordEvent()) play("sounds/1.au");
            else play("sounds/0.au");
        } catch (Exception e) {
            ;
        }
    }

    /**
     * Reset the experiment: record table, floor, random variable, random
     * variable graph and table
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tD\tA\tX\tY\tI");
        floor.reset();
        chord.reset();
        chordGraph.reset();
        chordTable.reset();
    }
}

