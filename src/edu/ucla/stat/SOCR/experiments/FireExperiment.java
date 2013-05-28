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
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the fire experiment. Each tree in a rectangular forest can
 * be on fire, burnt, or green. When a tree is on fire at time t, it may catch
 * any of its neighboring trees on fire at time t + 1. A tree on fire at time t
 * is burnt at time t + 1, and remains burnt thereafter.
 */
public class FireExperiment extends Experiment {
    //Variables
    private double[] prob = new double[4];
    //Objects
    private Forest forest = new Forest(100, 50, 5);
    private JPanel toolbar = new JPanel();
    private JLabel mouseJLabel = new JLabel("			");
    private String[] probString = { "pl = ", "pr = ", "pd = ", "pu = " };
    private JComboBox forestJComboBox = new JComboBox();
    private JComboBox editJComboBox = new JComboBox();

    /**
     * This method initializes the experiment, including the toolbars,
     * scrollbars, labels, forest.
     */
    public FireExperiment() {
        setName("Fire Experiment");
        setStopFreq(0);
        //Forest
        forest.setEditState(Forest.RED);
        forest.addMouseListener(this);
        forest.addMouseMotionListener(this);
        //Probability scrollbars and labels
        for (int i = 0; i < 4; i++) {
            prob[i] = 0.5;
            createValueSetter(probString[i], Distribution.CONTINUOUS, 0, 1);
        }
        //Forest choice
        forestJComboBox.addItem("Forest: 100 by 50");
        forestJComboBox.addItem("Forest: 500 by 250");
        forestJComboBox.addItemListener(this);
        //Edit choice
        editJComboBox.addItemListener(this);
        editJComboBox.addItem("Set State: Green");
        editJComboBox.addItem("Set State: Red");
        editJComboBox.addItem("Set State: Black");
        editJComboBox.setSelectedIndex(1);
        //Toolbar
        addTool(forestJComboBox);
        addTool(editJComboBox);
        toolbar.add(mouseJLabel);
        addToolbar(toolbar);
        //Graphs
        addGraph(forest);
        reset();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        super.doExperiment();
        forest.update();
        setStopNow(forest.treesOnFire() == 0);
    }

    /**
     * This method resets the experiment, including the forest, and the record
     * table.
     */
    public void reset() {
        super.reset();
        forest.reset();
        getRecordTable().setText("time\tGreen\tOn Fire\tBurnt");
        getRecordTable().append(
                "\n" + getTime() + "\t" + forest.treesGreen() + "\t"
                        + forest.treesOnFire() + "\t" + forest.treesBurnt());
    }

    /**
     * This method updates the experiment, including the forest and the update
     * record.
     */
    public void update() {
        super.update();
        getRecordTable().append(
                "\t" + forest.treesGreen() + "\t" + forest.treesOnFire() + "\t"
                        + forest.treesBurnt());
    }

    /**
     * This method handles the scrollbar events associated with changes in the
     * fire-spread probabilities.
     */
    public void adjustmentValueChanged(AdjustmentEvent event) {
        for (int i = 0; i < 4; i++) {
            prob[i] = getValueSetter(i).getValue();
        }
        forest.setProbabilities(prob[0], prob[1], prob[2], prob[3]);
        reset();
    }

    /**
     * This method handles the choice events associated with changing the size
     * of the forest and changing the edit color.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == forestJComboBox) {
            if (forestJComboBox.getSelectedIndex() == 0) forest.setParameters(100,
                    50, 5);
            else forest.setParameters(500, 250, 1);
            reset();
        } else if (event.getSource() == editJComboBox) forest
                .setEditState(editJComboBox.getSelectedIndex());
        else super.itemStateChanged(event);
    }

    /**
     * This method handles the mouse move events, so the the mouse position can
     * be tracked.
     */
    public void mouseMoved(MouseEvent event) {
        if (event.getSource() == forest) mouseJLabel.setText(forest.getX() + ", "
                + forest.getY());
        else super.mouseMoved(event);
    }

    /**
     * This method handles the mouse click events that are associated with
     * changing the states of trees.
     */
    public void mouseClicked(MouseEvent event) {
        if (event.getSource() == forest)
                getRecordTable()
                        .append(
                                "\n" + getTime() + "\t" + forest.treesGreen()
                                        + "\t" + forest.treesOnFire() + "\t"
                                        + forest.treesBurnt());

    }
}

