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

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * The triangle experiment is to break a stick at random and see if the pieces
 * form a triangle. If so, is the triangle acute or obtuse?
 */
public class TriangleExperiment extends Experiment {
    TriangleScatter scatterPlot = new TriangleScatter();
    Triangle triangle = new Triangle();
    RandomVariable typeRV = new RandomVariable(new FiniteDistribution(0, 2, 1,
            new double[] { 0.75, 0.170558458, 0.079441542 }), "U");
    RandomVariableGraph typeGraph = new RandomVariableGraph(typeRV);
    RandomVariableTable typeTable = new RandomVariableTable(typeRV);
    JLabel definitionLabel = new JLabel("X, Y: cutpoints, U: triangle type");

    /**
     * This method initializes the experiment, including the toolbar, triangle,
     * scatterplot, random variable graph and table
     */
    public TriangleExperiment() {
        setName("Triangle Experiment");
        typeGraph.showMoments(0);
        typeGraph.setMargins(35, 20, 20, 20);
        typeTable.showMoments(0);
        addTool(definitionLabel);
        //Construct graph panel
        addGraph(triangle);
        addGraph(scatterPlot);
        addGraph(typeGraph);
        //Construct table panel
        addTable(typeTable);
    }

    /**
     * This method defines the experiment. The stick is randomly broken and the
     * type of triangle is determined. The corresponding point is plotted in the
     * scatterplot
     */
    public void doExperiment() {
        super.doExperiment();
        triangle.setCutPoints();
        typeRV.setValue(triangle.getType());
        scatterPlot.drawPoint(triangle.getCutPoint(0), triangle.getCutPoint(1));
    }

    /**
     * This method runs the the experiment one time, and add sounds depending on
     * the outcome of the experiment.
     */
    public void step() {
        doExperiment();
        update();
        String url = "sounds/" + triangle.getType() + ".au";
        try {
            play(url);
        } catch (Exception e) {
            ;
        }
    }

    /**
     * This method updates the display, including the triangle, the random
     * varible graph and table, and the record table.
     */
    public void update() {
        super.update();
        triangle.repaint();
        typeGraph.repaint();
        typeTable.update();
        getRecordTable().append(
                "\t" + format(triangle.getCutPoint(0)) + "\t"
                        + format(triangle.getCutPoint(1)) + "\t"
                        + triangle.getType());
    }

    /**
     * This method resets the experiment, including the record table, triangle,
     * scatterplot, random variable table and graph
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tX\tY\tU");
        triangle.reset();
        scatterPlot.reset();
        typeRV.reset();
        typeGraph.reset();
        typeTable.reset();
    }
}
