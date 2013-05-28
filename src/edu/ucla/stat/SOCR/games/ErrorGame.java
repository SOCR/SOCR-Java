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

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class is an applet for exploring the measures of center and dispersion
 * in terms of error functions.
 */
public class ErrorGame extends Game implements ItemListener {
    //Variables
    private InteractiveHistogram histogram = new InteractiveHistogram(0, 5, 0.1);
    private ErrorGraph errorGraph = new ErrorGraph(histogram.getIntervalData(), 0);
    private DataTable dataTable = new DataTable(histogram.getIntervalData(), 1);
    private JComboBox freqJComboBox = new JComboBox();
    private JComboBox errorJComboBox = new JComboBox();
    private JLabel widthLabel = new JLabel("Class Width = 0.1; Classes = 50");

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        int n = 50 - getValueSetter(0).getValueAsInt();
        double w = 5.0 / n;
        histogram.setWidth(w);
        widthLabel
                .setText("Class Width = " + format(w) + "; Number Classes = " + n);
        dataTable.reset();
        applet.validate();
        applet.repaint();
    }

    public ErrorGame() {
        setName("Interactive Histogram with Error Graph");
        widthLabel.setPreferredSize(new Dimension(200, 20));
        createValueSetter("Class Width = 0.1; Classes = 50",
                Distribution.DISCRETE, 0, 49, 0);
        //Event listeners
        histogram.addMouseListener(this);
        freqJComboBox.addItemListener(this);
        errorJComboBox.addItemListener(this);
        //JComboBoxs
        freqJComboBox.addItem("Freq");
        freqJComboBox.addItem("Rel Freq");
        errorJComboBox.addItem("Mean Square");
        errorJComboBox.addItem("Mean Absolute");
        //Toolbar
        addTool(freqJComboBox);
        addTool(errorJComboBox);
        addTool(widthLabel);
        //Graphs
        addGraph(histogram);
        addGraph(errorGraph);
        //Tables
        addTable(dataTable);
        reset();
    }

    /**
     * This method handles the mouse events generated when the user clicks on
     * the interactive histogram.
     */
    public void mouseClicked(MouseEvent event) {
        if (event.getSource() == histogram) {
            dataTable.update();
            getRecordTable().append("\n" + format(histogram.getValue()));
            errorGraph.repaint();
        }
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        getRecordTable().setText("Data");
        histogram.reset();
        dataTable.reset();
        errorGraph.repaint();
    }

    /**
     * This method handles the choice events associated with the changing the
     * type of the frequency distribution, the type of error graph, and the type
     * of summary statistics.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == freqJComboBox) {
            histogram.setType(freqJComboBox.getSelectedIndex());
        } else if (event.getSource() == errorJComboBox) {
            if (errorJComboBox.getSelectedIndex() == 0) {
                histogram.showSummaryStats(2);
                errorGraph.setErrorType(0);
            } else {
                histogram.showSummaryStats(4);
                errorGraph.setErrorType(1);
            }
            dataTable.update();
        }

    }
}

