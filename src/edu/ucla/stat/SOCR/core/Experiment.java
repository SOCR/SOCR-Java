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

package edu.ucla.stat.SOCR.core;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class defines a basic, discrete time stochastic process that can be
 * subclassed
 */
public class Experiment extends MultiplePartsPanel implements Runnable,
        ActionListener, ItemListener, IExperiment {
    //Variables
    private int time = 0, updateCount = 0, stopCount = 0, updateFreq = 1,
            stopFreq = 10;
    private boolean stopNow = false;
    //JButtons
    private JButton stepJButton = new JButton("Step"), runJButton = new JButton(
            "Run"), stopJButton = new JButton("Stop");

    protected boolean showModelDistribution = true;
    /**
     * 
     * @uml.property name="updateChoice" 
     */
    private JComboBox updateChoice = new JComboBox(), stopChoice = new JComboBox();


    private Thread simulation = null;

    public static IExperiment getInstance(String classname) throws Exception {
        Class cls = Class.forName(classname);
        if (cls == null) return null;
        return (IExperiment) cls.newInstance();
    }
    
    public void  pasteData(Clipboard c){
    	
    }
    
    /**
     * used for some sublcass to initialize before be used
     */   
    public void initialize() {}
   

    /**
     * This method initializes the experiment, by setting up the basic
     * simulation buttons (step, run, stop, reset), and the update and stop
     * choices on the main toolbar. The record table that records the results of
     * the experiment is initialized.
     */
    public Experiment() {
        updateChoice.addItem("Result Report Freq: 1");
        updateChoice.addItem("Result Report Freq: 10");
        updateChoice.addItem("Result Report Freq: 100");
        updateChoice.addItem("Result Report Freq: 1000");
        updateChoice.setSelectedIndex(0);

        stopChoice.addItem("Run Continuously");
        stopChoice.addItem("Number of Experiments: 10");
        stopChoice.addItem("Number of Experiments: 100");
        stopChoice.addItem("Number of Experiments: 1000");
        stopChoice.addItem("Number of Experiments: 10000");
        stopChoice.setSelectedIndex(1);

        addTool(stepJButton);
        addTool(runJButton);
        addTool(stopJButton);
        
        addTool2(updateChoice);
        addTool2(stopChoice);

        getRecordTable().setText("Experimental Results");
        stepJButton.addActionListener(this);
        runJButton.addActionListener(this);
        stopJButton.addActionListener(this);

        stopChoice.addItemListener(this);
        updateChoice.addItemListener(this);
    }

    /**
     * This method gets the time parameter of the stochastic process. If the
     * process is to replicate a basic random experiment, then the time
     * parameter is the number of runs.
     */
    public int getTime() {
        return time;
    }

    /** This method stops the simulation thread */
    public void stop() {
        simulation = null;
        stopCount = 0;
    }

    /** This method runs the simulation thread */
    public void run() {
        Thread thread = Thread.currentThread();
        while (simulation == thread) {
            doExperiment();
            stopCount++;
            updateCount++;
          //  System.out.println("Experiment: updateCount=" + updateCount);
            if (updateCount == updateFreq) update();
            if ((stopCount == stopFreq) || stopNow) {
                stop();
                if (updateCount != 0) update();
            }
            // 12/17/03 try {Thread.sleep(10);}
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                stop();
            }
        }
       // update();
    }


	// Needs to be overwritten by objects that extend this class
    public String getName()
    {
	return new String ("SOCR Experiments: http://www.socr.ucla.edu \n");
    }

	// Needs to be overwritten by objects that extend this class
    public String getAppletInfo()
    {
	return new String ("SOCR Experiments: http://www.socr.ucla.edu \n");
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == stepJButton) {
            stop();
            step();
         }  else if (event.getSource() == runJButton) {
            if (simulation == null) {
                simulation = new Thread(this);
                simulation.start();
            }
         } else if (event.getSource() == stopJButton) {
            stop();
            if (updateCount != 0) update();
        }
     }

    /**
     * This method handles the choice events, associated with the update and
     * stop choices.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == updateChoice) {
            updateFreq = (int) (Math.pow(10, updateChoice.getSelectedIndex()));
        } else if (event.getSource() == stopChoice) {
            int j = stopChoice.getSelectedIndex();
            if (j > 0) stopFreq = (int) (Math.pow(10, j));
            else stopFreq = -1;
        }
    }

    /**
     * This method defines what the experiment actually does, and should be
     * overridden
     */
    public void doExperiment() {
        time++;
    }

    /**
     * This method is the default step method, that runs the process one time
     * unit. This method can be overridden to add sound or other elements
     */
    public void step() {
        doExperiment();
        update();
    }

    /**
     * This method is the default reset method, that resets the process to its
     * initial state. This method should be overridden.
     */
    public void reset() {
        stop();
        time = 0;
        getRecordTable().setText("Experiment Number:\tObservation:");
        update();
    }

    /**
     * This method is the default update method and defines how the display is
     * updated. This method should be overridden.
     */
    public void update() {
        updateCount = 0;
        getRecordTable().append("\n" +getTime());
    }
    
    public void resetUpdateCount(){
    	updateCount = 0;
    }

    public void graphUpdate() {
    }
    /**
     * This method defines the boolean variable that stops the process, when the
     * simulation is in run mode
     */
    public void setStopNow(boolean b) {
        stopNow = b;
    }

    /** This method returns the stop frequency */
    public int getStopFreq() {
        return stopFreq;
    }

    /** This method sets the stop frequency */
    public void setStopFreq(int i) {
        stopFreq = i;
    }

    /**
     * This method returns the update choice.
     * 
     * @uml.property name="updateChoice"
     */
    public JComboBox getUpdateChoice() {
        return updateChoice;
    }

    /**
     * This method returns the stop choice.
     * 
     * @uml.property name="stopChoice"
     */
    public JComboBox getStopChoice() {
        return stopChoice;
    }

    public String getOnlineDescription() {
        return new String("http://socr.stat.ucla.edu/");
    }

    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.Pluginable#getContentPane()
     */
    public Container getDisplayPane() {
        JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                getMainPanel(), getTextPanel() );
        return container;
    }
    public void setShowModelDistribution(boolean flag) {
		// TODO Auto-generated method stub
		showModelDistribution = flag;
	}
    
    public JTable getResultTable(){
    	return null;
    }

}

