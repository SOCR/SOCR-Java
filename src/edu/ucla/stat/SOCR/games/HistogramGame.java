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

package edu.ucla.stat.SOCR.games;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/** This class models an interactive historgram */
public class HistogramGame extends Game implements ItemListener {
    private InteractiveHistogram histogram = new InteractiveHistogram(0, 5, 0.1);
    private DataTable dataTable = new DataTable(histogram.getIntervalData(), 1);
    private JLabel widthLabel = new JLabel(
            "Class Width = 0.01; Number Classes = 50");
    private JComboBox freqJComboBox = new JComboBox();
    private JComboBox statsJComboBox = new JComboBox();

    /** This method initialize the applet */
    public HistogramGame() {
        setName("Interactive Histogram");
        //Event listeners
        freqJComboBox.addItemListener(this);
        statsJComboBox.addItemListener(this);
        createValueSetter(
                "Class Width = 0.01; Number Classes = 50", Distribution.DISCRETE,
                0, 49, 0);
        histogram.addMouseListener(this);
        //freq JComboBox
        freqJComboBox.addItem("Frequency");
        freqJComboBox.addItem("Relative Frequency");
        freqJComboBox.setSelectedIndex(0);
        //Stats choice
        statsJComboBox.addItem("None");
        statsJComboBox.addItem("Mean, Std Deviation");
        statsJComboBox.addItem("Boxplot");
        statsJComboBox.addItem("Median, MAD");
        statsJComboBox.setSelectedIndex(1);
        //Toolbars
        addTool(freqJComboBox);
        addTool(statsJComboBox);
        
        addTool(widthLabel);
        widthLabel.setPreferredSize(new Dimension(250, 20));
        //Graphs
        histogram.showSummaryStats(2);
        addGraph(histogram);
        //Tables
        dataTable.showSummaryStats(2);
        addTable(dataTable);
        reset();
    }

    /**
     * This method handles the choice events associated with changes in the
     * histogram type and changes in the type of summary statistics to display.
     */
    public void itemStateChanged(ItemEvent event) {
        int i;
        if (event.getSource() == freqJComboBox) {
            histogram.setType(freqJComboBox.getSelectedIndex());
        }
        if (event.getSource() == statsJComboBox) {
            i = statsJComboBox.getSelectedIndex();
            if (i == 0) {
                histogram.showSummaryStats(0);
                dataTable.showSummaryStats(0);
            } else {
                histogram.showSummaryStats(i + 1);
                dataTable.showSummaryStats(i + 1);
            }
        }
    }

    /** This method resets the applet */
    public void reset() {
        getRecordTable().setText("Data");
        histogram.reset();
        dataTable.reset();
    }

    /**
     * This method handles the mouse clicks that correspond to the user adding
     * points to the data set.
     */
    public void mouseClicked(MouseEvent event) {
        if (event.getSource() == histogram) dataTable.update();
        getRecordTable().append("\n" + format(histogram.getValue()));
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.ObsffValueSettersa.lang.Object)
     */
    public void update(Observable o, Object arg) {
        int n = 50 - getValueSetter(0).getValueAsInt();
        double w = 5.0 / n;
        histogram.setWidth(w);
        widthLabel
                .setText("Class Width = " + format(w) + "; Number Classes = " + n);
        dataTable.reset();

    }

}

