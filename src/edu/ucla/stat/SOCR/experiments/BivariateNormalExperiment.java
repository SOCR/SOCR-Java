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

import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/** This class models the Bivariate Normal Experiment */
public class BivariateNormalExperiment extends Experiment {
    double sigmaX = 1;
    double sigmaY = 1;
    double rho = 0;
    double sum, sampleCov, sampleCor;
    private NormalDistribution xDist = new NormalDistribution(0, 1),
            yDist = new NormalDistribution(0, 1);
    RandomVariable x = new RandomVariable(xDist, "X");
    RandomVariable y = new RandomVariable(yDist, "Y");
    RandomVariableGraph xGraph = new RandomVariableGraph(x);
    RandomVariableGraph yGraph = new RandomVariableGraph(y);
    RandomVariableTable xTable = new RandomVariableTable(x);
    RandomVariableTable yTable = new RandomVariableTable(y);
    BivariateScatterPlot xyScatter = new BivariateScatterPlot(-6, 6, 1, -6, 6, 1);
    JTextArea correlationTable = new SOCRApplet.SOCRTextArea();

    /**
     * This method initializes the experiment, including the scatterplot,
     * parameter scrollbars labels, random variable graphs, and the correlation
     * table
     */
    public BivariateNormalExperiment() {
        createValueSetter("SigmaX", 5, 20, 0.1);
        createValueSetter("SigmaY", 5, 20, 0.1);
        createValueSetter("Rho", Distribution.CONTINUOUS, -1, 1);
        setName("Bivariate Normal Experiment");
        xyScatter.setParameters(0, 0);
        correlationTable.setEditable(false);
        addGraph(xyScatter);
        addGraph(xGraph);
        addGraph(yGraph);
        addTable(xTable);
        addTable(yTable);
        addTable(correlationTable);
        reset();
    }

    /**
     * This method defines the experiment. A point from the bivariate normal
     * distribution is simulated and passed to the scatterplot
     */
    public void doExperiment() {
        double r, theta, u, v;
        super.doExperiment();
        //Simulate a point from the bivariate normal distribution*/
        r = Math.sqrt(-2 * Math.log(Math.random()));
        theta = 2 * Math.PI * Math.random();
        u = r * Math.cos(theta);
        r = Math.sqrt(-2 * Math.log(Math.random()));
        theta = 2 * Math.PI * Math.random();
        v = r * Math.cos(theta);
        x.setValue(sigmaX * u);
        y.setValue(sigmaY * rho * u + sigmaY * Math.sqrt(1 - rho * rho) * v);
        sum = sum + x.getValue() * y.getValue();
        //Draw the point
        xyScatter.drawPoint(x.getValue(), y.getValue());
    }

    /**
     * This method updates the experiment, including the random variable graphs,
     * correlation table, and record table
     */
    public void update() {
        super.update();
        int runs = getTime();
        //Compute the sample correlation and covariance*/
        sampleCov = (sum - runs * x.getIntervalData().getMean()
                * y.getIntervalData().getMean())
                / (runs - 1);
        sampleCor = sampleCov
                / (x.getIntervalData().getSD() * y.getIntervalData().getSD());
        getRecordTable().append(
                "\t" + format(x.getValue()) + "\t" + format(y.getValue()));
        //Update tables and graphs
        xGraph.repaint();
        yGraph.repaint();
        xTable.update();
        yTable.update();
        correlationTable.setText("Correlation\nDist\tData\n" + format(rho) + "\t"
                + format(sampleCor));
        double slope = sampleCov / x.getIntervalData().getVariance();
        double intercept = y.getIntervalData().getMean() - slope
                * x.getIntervalData().getMean();
        if (getTime() > 1) xyScatter.drawSampleLine(slope, intercept);
    }

    /**
     * This method resets the experiment, including the random variable graphs
     * and tables, the correlation table, scatterplot and record table
     */
    public void reset() {
        super.reset();
        sum = 0;
        x.reset();
        y.reset();
        xyScatter.reset();
        xGraph.reset();
        yGraph.reset();
        xTable.update();
        yTable.update();
        getRecordTable().append("\tX\tY");
        correlationTable.setText("Correlation\nDist\tData\n" + format(rho));
    }

    /**
     * This method handles the scrollbar events that occur when parameters are
     * changed.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            sigmaX = getValueSetter(0).getValue();
            xDist.setParameters(0, sigmaX);
        } else if (arg == getValueSetter(1)) {
            sigmaY = getValueSetter(1).getValue();
            yDist.setParameters(0, sigmaY);
        } else if (arg == getValueSetter(2)) {
            rho = getValueSetter(2).getValue();
        }
        setParameters();
        reset();

    }

    public void setParameters() {
        xyScatter.setParameters(rho * sigmaY / sigmaX, 0);
    }
}

