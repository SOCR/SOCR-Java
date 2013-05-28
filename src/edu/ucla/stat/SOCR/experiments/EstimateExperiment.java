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

/**
 * This class defines a basic experiment to illustrate point estimates of a
 * parameter. This experiment must be subclassed for a specific estimation
 * process.
 */
public class EstimateExperiment extends Experiment {
    private int sampleSize = 10;

    private RandomVariable randomVariable;
    private JTextArea statTable = new SOCRApplet.SOCRTextArea();
    private RandomVariableGraph rvGraph;

    public EstimateExperiment() {
        this(new RandomVariable(new BetaDistribution(0, 1)));
    }

    /**
     * This method initializes the experiment, including the toolbar with the
     * sample size scroll and label, the random variable graph and table, and
     * the statistics table.
     */
    public EstimateExperiment(RandomVariable rv) {
        randomVariable = rv;
    }
    
    public void initialize() {
        createValueSetter("n", Distribution.DISCRETE, 1, 100, 10);
        initializePane();
    }
    
    protected void initializePane() {
        rvGraph = new RandomVariableGraph(randomVariable);
        rvGraph.showMoments(0);
        //Initialize tables
        statTable.setEditable(false);
        addTable(statTable);
        //Initialize graphs
        addGraph(rvGraph);
        reset();

    }

    /** This method updates the experiment. */
    public void update() {
        super.update();
        rvGraph.repaint();
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        randomVariable.reset();
        rvGraph.reset();
    }

    /** This method resets the random variable and its graph. */
    public void resetSample() {
        randomVariable.reset();
        rvGraph.reset();
    }

    /** This method returns the sample size */
    public int getSampleSize() {
        return sampleSize;
    }

    /** This method adds text to the statistics table */
    public void setStatistics(String text) {
        statTable.setText(text);
    }

    public void update(Observable o, Object arg) {
        sampleSize = getValueSetter(0).getValueAsInt();
        reset();
    }

}

