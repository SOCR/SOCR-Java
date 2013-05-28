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
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;

/**
* This class defines a basic experiment to illustrate point estimates of a parameter.
* This experiment must be subclassed for a specific estimation process.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class EstimateExperiment extends Experiment{
	//Variables
	private int sampleSize = 10;
	//Objects
	private Parameter sampleScroll = new Parameter(1, 1000, 1, sampleSize, "Sample size", "n");
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private RandomVariable randomVariable;
	private RandomVariableGraph rvGraph;

	/**
	* This method initializes the experiment, including the toolbar with the sample
	* size scroll and label, the random variable graph and table, and the statistics table.
	* @param rv the sampling random variable
	*/
	public void init(RandomVariable rv){
		super.init();
		sampleScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(sampleScroll);
		addToolBar(toolBar);
		//Random variable
		randomVariable = rv;
		//Random variable graph
		rvGraph = new RandomVariableGraph(randomVariable);
		rvGraph.setMomentType(0);
		rvGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(rvGraph, 0, 0, 2, 1);
		reset();
	}

	/**
	* This method updates the experiment, by updating the random variable graph.
	*/
	public void update(){
		super.update();
		rvGraph.repaint();
	}
	
	public void graphUpdate(){
		super.update();
		rvGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.repaint();
	}
	/**
	* This method resets the experiment, including the random variable and the
	* random variable graph.
	*/
	public void reset(){
		super.reset();
		randomVariable.reset();
		rvGraph.reset();
	}

	/**
	* This method resets the random variable and its graph.
	*/
	public void resetSample(){
		randomVariable.reset();
		rvGraph.reset();
	}

	/**
	* This method handles the scrollbar event for changing the sample size.
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			reset();
		}
	}

	/**
	* This method returns the sample size.
	* @return the sample size
	*/
	public int getSampleSize(){
		return sampleSize;
	}

	/**
	* This method adds a new component to the second toolbar.
	* @param c the tool
	*/
	public void addTool(Component c){
		toolBar.add(c);
	}

}

