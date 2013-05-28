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

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models a simple interactive scatterplot. The user clicks on the
 * scatterplot to generate points. The regression line and summary statistics
 * are shown.
 */
public class BivariateGame extends Game {
    private int runs = 0;
    private UserScatterPlot xyScatter = new UserScatterPlot(-6, 6, 1, -6, 6, 1);
    private JLabel mouseLabel = new JLabel("(X, Y) = (-6.00, -6.00)");
    private SOCRApplet.SOCRTextArea statsTable = new SOCRApplet.SOCRTextArea();
    private JButton refreshButton = new JButton("Refresh");

    public BivariateGame() {
        setName("Interactive Scatterplot");
        //Event listener
        xyScatter.addMouseListener(this);
        xyScatter.addMouseMotionListener(this);
        refreshButton.addActionListener(this);
        //toolbar
        addTool(refreshButton);
        addTool(mouseLabel);
        mouseLabel.setPreferredSize(new Dimension(200, 20));
        //Graphs
        addGraph(xyScatter);
        addTable(statsTable);
        //reset();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == refreshButton) {
            xyScatter.repaint();
        }
    }

    /**
     * This method resets the experiment, including the scatterplot, the record
     * table, and the statistics table.
     */
    public void reset() {
        super.reset();
        xyScatter.reset();
        getRecordTable().setText("Runs\tX\tY");
        statsTable
                .setText("Data Statistics\nMean(X)\nSD(X)\nMean(Y)\nSD(Y)\nCor(X, Y)");
        applet.repaint();
        //JOptionPane.showMessageDialog(this, "Reset");
    }

    public void mouseClicked(MouseEvent event) {
        if (event.getSource() == xyScatter) {
            runs++;
            IntervalData xData = xyScatter.getXData(), yData = xyScatter.getYData();
            getRecordTable().append(
                    "\n" + runs + "\t" + format(xData.getValue()) + "\t"
                            + format(yData.getValue()));
            statsTable.setText("Data Statistics\nMean(X)\t"
                    + format(xData.getMean()) + "\nSD(X)\t" + format(xData.getSD())
                    + "\nMean(Y)\t" + format(yData.getMean()) + "\nSD(Y)\t"
                    + format(yData.getSD()) + "\nCor(X, Y)\t"
                    + format(xyScatter.getCorrelation()));
        } else super.mouseClicked(event);
    }

    public void mouseMoved(MouseEvent event) {
        if (event.getSource() == xyScatter) {
            mouseLabel.setText("(X, Y) = (" + format(xyScatter.getXMouse()) + ", "
                    + format(xyScatter.getYMouse()) + ")");
        } else super.mouseMoved(event);
    }
}
