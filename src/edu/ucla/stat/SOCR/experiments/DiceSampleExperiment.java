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
import edu.ucla.stat.SOCR.util.*;

/**
 * A simple experiment that consists of rolling n dice, thus sampling from the
 * underlying die distribution
 */
public class DiceSampleExperiment extends Experiment {
    //Variables
    private int n = 2;
    //Objects
    private JLabel definitionLabel = new JLabel("Xi: Score on Die i");
    private JButton dieButton = new JButton("Die Probabilities");
    private JPanel toolbar = new JPanel();
    private DiceBoard diceBoard = new DiceBoard(48);
    private DieProbabilityDialog dieProbabilityDialog;

    /** Initialize the experiment */
    public DiceSampleExperiment() {
        setName("Dice Sample Experiment");
    }
    
    public void initialize() {
        dieButton.addActionListener(this);
        createValueSetter("n", Distribution.DISCRETE, 1, 48, n);
        JPanel localToolBarPanel = new JPanel();
        toolbar.add(localToolBarPanel, BorderLayout.SOUTH);
        //toolbar.setLayout(new BorderLayout(BorderLayout.SOUTH));
        localToolBarPanel.add(dieButton);
        localToolBarPanel.add(definitionLabel);
        addToolbar(toolbar);
        //Graphs
        addGraph(diceBoard);
        reset();
    }

    /** Perform the experiment */
    public void doExperiment() {
        super.doExperiment();
        diceBoard.roll(n);
    }

    /** Reset the experiment */
    public void reset() {
        super.reset();
        diceBoard.showDice(0);
        String recordText = "";
        for (int i = 0; i < n; i++)
            recordText = recordText + "\tX" + (i + 1);
        getRecordTable().append(recordText);
    }

    /** Update the display */
    public void update() {
        super.update();
        diceBoard.showDice(n);
        String recordText = "";
        for (int i = 0; i < n; i++)
            recordText = recordText + "\t" + diceBoard.getDie(i).getValue();
        getRecordTable().append(recordText);
        applet.validate();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dieButton) {
            Point fp = applet.getLocationOnScreen(), dp;
            Dimension fs = applet.getSize(), ds = dieProbabilityDialog.getSize();
            dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2
                    - ds.height / 2);
            dieProbabilityDialog.setLocation(dp);

            dieProbabilityDialog.setProbabilities(diceBoard.getProbabilities());
            dieProbabilityDialog.setVisible(true);
            if (dieProbabilityDialog.isOK()) {
                diceBoard.setProbabilities(dieProbabilityDialog.getProbabilities());
                reset();
            }
        } else super.actionPerformed(e);
    }

    /** Scrollbar events: select the number of dice */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        reset();
    }
}

