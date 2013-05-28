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
 * The coupon collector experiment consists of selecting a sampling, with
 * replacement from a finite population. The random variable of interest is the
 * sample size needed to get a specified number of elements in the population
 */
public class CouponCollectorExperiment extends Experiment {
    //Variables
    private int cells = 20, occupied = 10, ballCount;
    private int[] cellCount;
    //Objects
    private JPanel toolbar = new JPanel();
    private CellGrid cellGrid = new CellGrid(cells);
    private CouponDistribution dist = new CouponDistribution(cells, occupied);
    private RandomVariable ballsRV = new RandomVariable(dist, "W");
    private RandomVariableGraph ballsGraph = new RandomVariableGraph(ballsRV);
    private RandomVariableTable ballsTable = new RandomVariableTable(ballsRV);
    private JLabel definitionLabel = new JLabel("W: # balls");

    /** Initialize the experiment */
    public CouponCollectorExperiment() {
        setName("Coupon Collector Experiment");
        createValueSetter("N", Distribution.DISCRETE, 1, 100, cells);
        createValueSetter("k", Distribution.DISCRETE, 1, 42, occupied);
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Graphs
        addGraph(cellGrid);
        addGraph(ballsGraph);
        //Tables
        addTable(ballsTable);
        //Finalize
        reset();
    }

    /**
     * Scrollbar events: select the population size (the number of cells), and
     * the number to be occuppied.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            cells = getValueSetter(0).getValueAsInt();
            int occupiedMax = occupiedMax(cells);
            if (occupied > occupiedMax) occupied = occupiedMax;
            cellGrid.setCells(cells);
            setParameters();
        } else {
            occupied = getValueSetter(1).getValueAsInt();

        }
        setParameters();
    }

    /** Set parameters */
    public void setParameters() {
        dist.setParameters(cells, occupied);
        reset();
    }

    /** Perform the experiment */
    public void doExperiment() {
        super.doExperiment();
        int cellIndex;
        int occupiedCount = 0;
        ballCount = 0;
        cellCount = new int[cells];
        while (occupiedCount < occupied) {
            cellIndex = (int) (cells * Math.random());
            ballCount++;
            if (cellCount[cellIndex] == 0) occupiedCount++;
            cellCount[cellIndex]++;
        }
        ballsRV.setValue(ballCount);
    }

    /** Update display */
    public void update() {
        super.update();
        cellGrid.setCellCount(cellCount);
        cellGrid.repaint();
        getRecordTable().append("\t" + ballCount);
        ballsGraph.repaint();
        ballsTable.update();
    }
    
    public void graphUpdate() {
        super.update();
        ballsGraph.setShowModelDistribution(showModelDistribution);
        ballsGraph.repaint();
     
    }

    /** Reset the experiment */
    public void reset() {
        super.reset();
        getRecordTable().append("\tW");
        cellGrid.reset();
        ballsRV.reset();
        ballsGraph.reset();
        ballsTable.reset();
    }

    public int occupiedMax(int m) {
        if (m <= 10) return m;
        else if (m <= 20) return 10 + 8 * (m - 10) / 10;
        else if (m <= 40) return 18 + 7 * (m - 20) / 10;
        else if (m <= 60) return 32 + 6 * (m - 40) / 10;
        else if (m <= 80) return 44 + 5 * (m - 60) / 10;
        else return 54 + 4 * (m - 80) / 10;
    }
}

