/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.experiments;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.UrnChain;
import edu.uah.math.devices.TransitionMatrixDialog;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.DataTable;
import edu.uah.math.devices.Parameter;

/**
* This class models a basic Markov chain with state space {0, 1, ..., n}.
* The number of states can be varied and
* the transition probabilites can be specified. The states are represented by balls, and the
* current state is colored red.  The initial state can be specified by clicking on a ball
* when the time is 0. The graph and table show the proportion of time spent in each state.
* As time increases, these proportions converge to the steady-state probabilities of the
* chain.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class MarkovChainExperiment extends Experiment implements Serializable{
	//Variables
	private int numStates = 5;
	private double num;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[] {"Time", "X"});
	private UrnChain chain = new UrnChain(numStates);
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Histogram histogram = new Histogram(chain.getData());
	private DataTable dataTable = new DataTable(chain.getData());
	//Scrollbars and labels
	private Parameter stateScroll = new Parameter(1, 15, 1, numStates - 1, "Last state", "n");
	private JButton probButton = new JButton();
	private TransitionMatrixDialog probDialog;
	private Frame frame;

	/**
	* This method initialize the experiment, including the toolbar, record table, Markov chain,
	* histogram, and data table.
	*/
	public void init(){
		super.init();
		setName("Markov Chain Experiment");
		//Transition probability button
		probButton.addActionListener(this);
		probButton.setToolTipText("Transition Probabilities");
		probButton.setIcon(new ImageIcon(getClass().getResource("transition.gif")));
		//Event Listeners
		stateScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(probButton);
		toolBar.add(stateScroll);
		addToolBar(toolBar);
		//Urn Chain
		chain.setToolTipText("Markov chain: at time 0, click to set the initial state");
		for (int i = 0; i < numStates; i++) chain.getBall(i).addMouseListener(this);
		chain.setMinimumSize(new Dimension(100, 40));
		addComponent(chain, 0, 0, 2, 1, 10, 0);
		//Histogram
		histogram.setStatisticsType(Histogram.NONE);
		addComponent(histogram, 0, 1, 2, 1);
		//Record Table
		recordTable.setDescription("X: state");
		addComponent(recordTable, 0, 2, 1, 1);
		//Data Table
		dataTable.setDistributionType(DataTable.REL_FREQ);
		dataTable.setStatisticsType(DataTable.NONE);
		addComponent(dataTable, 1, 2, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive informaion, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "This applet illustrates a basic Markov chain on the states{0, 1, ..., n}.\n"
			+ "The states are represented as balls, and the current state is colored red.\n"
			+ "The number of states can be varied, as well as the transition probabilities.\n"
			+ "The initial state can be specified by clicking on a ball when the time is 0.\n"
			+ "The graph and table show the proportion of time spent in each state.  As the\n"
			+ "time increases, these proportions converge to the steady-state distribution\n"
			+ " of the chain.";
	}

	/**
	* This method handles the scrollbar events for changing the number of states.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
	    if (e.getSource() == stateScroll.getSlider()){
			numStates = (int)stateScroll.getValue() + 1;
			chain.setBallCount(numStates);
			for (int i = 0; i < numStates; i++) chain.getBall(i).addMouseListener(this);
			histogram.setIntervalData(chain.getData());
			dataTable.setData(chain.getData());
			reset();
		 }
	}

	/**
	* This method handles the action event associated with the probability button.
	* The transition probability dialog is diplayed.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == probButton){

			// Ivo a workaround the problem that the applets do not know their
			// parent's Frames 01/26/05
			frame = new Frame();
			//frame = getFrame(this);

			probDialog = new TransitionMatrixDialog(frame, numStates);
			//Point fp = frame.getLocationOnScreen(), dp;
			Point dp, fp = new Point(100, 100);
			Dimension fs = frame.getSize(), ds = probDialog.getSize();
			dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			probDialog.setLocation(dp);
			probDialog.setProbabilities(chain.getProbabilities());
			probDialog.setVisible(true);
			if (probDialog.isOK()){
				chain.setProbabilities(probDialog.getProbabilities());
				histogram.setIntervalData(chain.getData());
				dataTable.setData(chain.getData());
				reset();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the initial state when the user clicks on a ball.
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		for (int i = 0; i < numStates; i++)
			if (e.getSource() == chain.getBall(i) && getTime() == 0){
				chain.setInitialState(i);
				reset();
			}
	}

	/**
	* This method performs the next step in the process. A note is played, depending on
	* the state.
	*/
	public void step(){
		doExperiment();
		//playnote(chain.getState());
		update();
	}

	/**
	* This method resets the experiment, including the Markov chain, the record table
	* the histogram and data table.
	*/
	public void reset(){
		super.reset();
		chain.reset();
		recordTable.reset();
		recordTable.addRecord(new double[]{getTime(),chain.getState()});
		histogram.repaint();
		dataTable.reset();
	}

	/**
	* This method defines the experiment. The Markov chain moves to the next state.
	*/
	public void doExperiment(){
		super.doExperiment();
		chain.move();
	}

	/**
	* This method updates the experiment, including the record table, Markov chain,
	* histogram, and data table.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), chain.getState()});
		chain.repaint();
		histogram.repaint();
		dataTable.repaint();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}
