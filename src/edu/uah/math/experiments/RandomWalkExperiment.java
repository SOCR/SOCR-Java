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
import java.io.Serializable;
import java.awt.event.ItemEvent;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomWalkGraph;
import edu.uah.math.distributions.WalkPositionDistribution;
import edu.uah.math.distributions.WalkMaxDistribution;
import edu.uah.math.distributions.DiscreteArcsineDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the simple random walk on the time interval [0, 2n]. The random variables of
* interest are the final position, the maximum position, and the last zero.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RandomWalkExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 10, rvIndex = 0;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "Y", "M", "L"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private RandomWalkGraph walkGraph = new RandomWalkGraph(n);
	private RandomVariable[] walkRV = new RandomVariable[3];
	private Parameter nScroll = new Parameter(10, 100, 2, 10, "Number of steps", "n");
	private JComboBox rvChoice = new JComboBox();
	private RandomVariableGraph rvGraph;
	private RandomVariableTable rvTable;

	/**
	* This method initializes the experiment, including the toolbar containing the scrollbar, choice,
	* and label, the graph panel containing the random walk graph and the random variable graph,
	* and the table panel containing the record table and the radnom walk table.
	*/
	public void init(){
		super.init();
		setName("Randowm Walk Experiment");
		//Random Variables
		walkRV[0] = new RandomVariable(new WalkPositionDistribution(n, 0.5), "Y");
		walkRV[1] = new RandomVariable(new WalkMaxDistribution(n), "M");
		walkRV[2] = new RandomVariable(new DiscreteArcsineDistribution(n), "L");
		rvGraph = new RandomVariableGraph(walkRV[rvIndex]);
		rvTable = new RandomVariableTable(walkRV[rvIndex]);
		//Parameter
		nScroll.getSlider().addChangeListener(this);
		//rvChoice
		rvChoice.addItemListener(this);
		rvChoice.setToolTipText("Y: final position; M: maximum position; L: last zero");
		rvChoice.addItem("Y");
		rvChoice.addItem("M");
		rvChoice.addItem("L");
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rvChoice);
		toolBar.add(nScroll);
		addToolBar(toolBar);
		//Random Walk Graph
		addComponent(walkGraph, 0, 0, 1, 1);
		//Random Variable Graph
		addComponent(rvGraph, 1, 0, 1, 1);
		//Record Table
		recordTable.setDescription("Y: final position, M: maximum position, L: last zero");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random Variable Table
		addComponent(rvTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment consists of tossing n fair coins. The position of the random walk after\n"
			+ "j tosses is the number of heads minus the number of tails. The random variables of interest\n"
			+ "are the final position, the maximum position, the time of the last zero. The random walk\n"
			+ "is shown in red in the left graph on each update. The maximum and minimum values are shown\n"
			+ "as red dots on the right vertical axis; the last zero is shown as a red dot on the\n"
			+ "horizontal axis. The value of each of the three random variables is recorded in the first\n"
			+ "table on each update. Any of the three variables can be selected from the list box. The\n"
			+ "density and moments of the selected variable are shown in blue in the second graph and\n"
			+ "are recorded in the second table. On each update, the empirical density and moments are\n"
			+ "shown in red in the second graph and are recorded in the second table.";
	}

	/**
	* This method defines the experiment.  The random walk is performed and the values of the
	* random variables computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		walkGraph.walk(0);
		walkRV[0].setValue(walkGraph.getValues(n));
		walkRV[1].setValue(walkGraph.getMaxValue());
		walkRV[2].setValue(walkGraph.getLastZero());
	}

	/**
	* This method resets the experiment, including the random walk graph,
	* the random variable table and graph, and the record table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		walkGraph.setWalkShown(false);
		for (int i = 0; i < 3; i++) walkRV[i].reset();
		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method updates the experiment, including the random walk graph, the random
	* variable table and graph, and the record table.
	*/
	public void update(){
		super.update();
		walkGraph.setWalkShown(true);
		recordTable.addRecord(new double[]{getTime(), walkGraph.getValues(n),
			walkGraph.getMaxValue(), walkGraph.getLastZero()});
		rvGraph.repaint();
		rvTable.repaint();
	}
	
	public void graphUpdate(){
		super.update();
		rvGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.repaint();
		rvTable.setShowModelDistribution(showModelDistribution);
		rvTable.repaint();
	}

	/**
	* This method handles the event associated with the random variable choice.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == rvChoice){
			rvIndex = rvChoice.getSelectedIndex();
			rvGraph.setRandomVariable(walkRV[rvIndex]);
			rvTable.setRandomVariable(walkRV[rvIndex]);
			rvGraph.repaint();
			rvTable.repaint();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the events associated with the time parameter scrollbar.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()) {
			n = (int)nScroll.getValue();
			walkRV[0] = new RandomVariable(new WalkPositionDistribution(n, 0.5), "Y");
			walkRV[1] = new RandomVariable(new WalkMaxDistribution(n), "M");
			walkRV[2] = new RandomVariable(new DiscreteArcsineDistribution(n), "L");
			walkGraph.setSteps(n);
			rvGraph.setRandomVariable(walkRV[rvIndex]);
			rvTable.setRandomVariable(walkRV[rvIndex]);
			reset();
		}
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}



