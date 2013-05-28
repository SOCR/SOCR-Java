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
 * Buffon's coin experiment consists of tossing a coin on a floor covered in
 * square tiles. The event of interest is that the coin crosses a crack between
 * tiles.
 */
public class BuffonCoinExperiment extends Experiment {
    //Variables
    private double x, y, r = 0.1;
    private int crossValue;
    //Objects
    private JPanel toolbar = new JPanel();
    private CoinScatter scatter = new CoinScatter(r);
    private CoinFloor floor = new CoinFloor(r);
    private BernoulliDistribution crossDist = new BernoulliDistribution(floor
            .getProbability());
    private RandomVariable crossRV = new RandomVariable(crossDist, "I");
    private RandomVariableGraph crossGraph = new RandomVariableGraph(crossRV);
    private RandomVariableTable crossTable = new RandomVariableTable(crossRV);
    private JLabel definitionLabel = new JLabel(
            "(X,Y): coin center, I: crack crossing");

    /** Initialize the experiment: graphs, labels, scrollbars, tables */
    public BuffonCoinExperiment() {
        setName("Buffon's Coin Experiment");
        createValueSetter("r=", 0, 5, 0.1);
        crossGraph.showMoments(0);
        crossGraph.setMargins(35, 20, 20, 20);
        crossTable.showMoments(0);
        //Toolbars
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Graph panel
        addGraph(floor);
        addGraph(scatter);
        addGraph(crossGraph);
        //Table panel
        addTable(crossTable);
        //resize();
        reset();
    }

    /** Scrollbar event: change the radius of the coin */
    public void update(Observable o, Object arg) {
        r = getValueSetter(0).getValue();
        floor.setRadius(r);
        scatter.setRadius(r);
        crossDist.setProbability(floor.getProbability());
        reset();
    }

    /**
     * This method defines the experiment: randomly select the coin center and
     * check to see if the tile crack event has occurred.
     */
    public void doExperiment() {
        super.doExperiment();
        floor.setValues();
        if (floor.crossEvent()) crossValue = 1;
        else crossValue = 0;
        crossRV.setValue(crossValue);
        x = floor.getXCenter();
        y = floor.getYCenter();
        scatter.drawPoint(x, y);
    }

    /** Update the display: floor, random variable graph and table, record table */
    public void update() {
        super.update();
        //Update record table
        getRecordTable().append(
                "\t" + format(x) + "\t" + format(y) + "\t" + crossValue);
        //Update floor
        floor.repaint();
        //Update random variable displays
        crossGraph.repaint();
        crossTable.update();
    }

    /** Peform the experiment one time, and play sounds depending on the outcome */
    public void step() {
        doExperiment();
        update();
        try {
            if (crossValue == 1) play("sounds/1.au");
            else play("sounds/0.au");
        } catch (Exception e) {
            ;
        }
    }

    /** Reset the experiment */
    public void reset() {
        super.reset();
        getRecordTable().append("\tX\tY\tI");
        floor.reset();
        scatter.reset();
        crossRV.reset();
        crossGraph.reset();
        crossTable.reset();
    }
}

