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
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.ChiSquareDistribution;
import edu.uah.math.distributions.FisherDistribution;
import edu.uah.math.distributions.StudentDistribution;
import edu.uah.math.distributions.BetaDistribution;
import edu.uah.math.distributions.WeibullDistribution;
import edu.uah.math.distributions.ParetoDistribution;
import edu.uah.math.distributions.LogisticDistribution;
import edu.uah.math.distributions.LogNormalDistribution;
import edu.uah.math.distributions.ExtremeValueDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This is a basic experiment for simulating the values from a number of
* distributions. The true and empirical densities are shown in a graph and table.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RandomVariableExperiment extends Experiment implements Serializable{
	//Variables
	private int distributionType = 0;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox distributionChoice = new JComboBox();
	private Parameter parameter1Scroll = new Parameter(-20, 20, 0.5, 0, "Mean", "\u03bc");
	private Parameter parameter2Scroll = new Parameter(0.5, 20, 0.5, 1, "Standard deviation", "\u03c3");
	private RandomVariable rv = new RandomVariable(new NormalDistribution(0, 1), "X");
	private RandomVariableTable rvTable = new RandomVariableTable(rv);
	private RandomVariableGraph rvGraph = new RandomVariableGraph(rv);

	/**
	* This method initializes the experiment, including the toolbar with the
	* distribution choice, scrollbars and label, and the random variable graph
	* and table.
	*/
	public void init(){
		super.init();
		setName("Random Variable Experiment");
		//Parameters
		parameter1Scroll.applyDecimalPattern("0.0");
		parameter1Scroll.getSlider().addChangeListener(this);
		parameter2Scroll.applyDecimalPattern("0.0");
		parameter2Scroll.getSlider().addChangeListener(this);
		//Distributions
		distributionChoice.addItemListener(this);
		distributionChoice.setToolTipText("Distribution");
		distributionChoice.addItem("Normal");
		distributionChoice.addItem("Gamma");
		distributionChoice.addItem("Chi-square");
		distributionChoice.addItem("Student");
		distributionChoice.addItem("F");
		distributionChoice.addItem("Beta");
		distributionChoice.addItem("Weibull");
		distributionChoice.addItem("Pareto");
		distributionChoice.addItem("Logistic");
		distributionChoice.addItem("Lognormal");
		distributionChoice.addItem("Extreme Value");
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(distributionChoice);
		toolBar.add(parameter1Scroll);
		toolBar.add(parameter2Scroll);
		addToolBar(toolBar);
		//Random Variable Graph
		addComponent(rvGraph, 0, 0, 2, 1);
		//Record Table
		recordTable.setDescription("X: random variable");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random Variable Table
		addComponent(rvTable, 1, 1, 1, 1);
		//Final Actions
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
			+ "The experiment simulates a value of a random variable with a specified distribution. The value\n"
			+ "is recorded on each update. The normal, gamma, chi-square, student t, F, beta, Weibull, Pareto,\n"
			+ "logistic, and lognormal distributions can be chosen from the list box. In each case, the parameters\n"
			+ "of the distribution can be varied with scroll bars. The density and moments of the distribution\n"
			+ "are shown in the distribution graph, and the moments are given in the distribution table.";
	}

	/**
	* This method defines the experiment: a value from the selected random
	* variable is simulated.
	*/
	public void doExperiment(){
		super.doExperiment();
		rv.sample();
	}

	/**
	* This method handles the choice events for changing the selected
	* distribution.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == distributionChoice){
			distributionType = distributionChoice.getSelectedIndex();
			switch(distributionType){
			case 0:	 //Normal
				parameter1Scroll.setProperties(-20, 20, 0.5, 0, "Mean", "\u03bc");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Standard deviation", "\u03c3");
				break;
			case 1:	 //Gamma
				parameter1Scroll.setProperties(1, 20, 0.5, 1, "Shape", "k");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Scale", "b");
				break;
			case 2:	 //Chi-square
				parameter1Scroll.setProperties(1, 100, 1, 2, "Degrees of freedom", "n");
				parameter2Scroll.setVisible(false);
				break;
			case 3:	 //Student
				parameter1Scroll.setProperties(1, 100, 1, 3, "Degrees of freedom", "n");
				parameter2Scroll.setVisible(false);
				break;
			case 4:	 //Fisher
				parameter1Scroll.setProperties(1, 100, 1, 5, "Numerator degrees of freedom", "m");
				parameter2Scroll.setProperties(1, 100, 1, 5, "Denominator degrees of freedom", "n");
				break;
			case 5:	 //Beta
				parameter1Scroll.setProperties(0.5, 20, 0.5, 1, "Left parameter", "a");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Right parameter", "b");
				break;
			case 6:   //Weibull
				parameter1Scroll.setProperties(1, 20, 0.5, 1, "Shape", "k");
				parameter2Scroll.setProperties(0.5, 20, 0.5, 1, "Scale", "b");
				break;
			case 7:   //Pareto
				parameter1Scroll.setProperties(1, 20, 1, 1, "Shape", "a");
				parameter2Scroll.setVisible(false);
				break;
			case 8:   //Logistic
				parameter1Scroll.setVisible(false);
				parameter2Scroll.setVisible(false);
				break;
			case 9:	 //Lognormal
				parameter1Scroll.setProperties(-2, 2, 1, 0, "Mean", "\u03bc");
				parameter2Scroll.setProperties(0.5, 2, 1, 0.5, "Standard deviation", "\u03c3");
				break;
			case 10: //Extreme Value
				parameter1Scroll.setVisible(false);
				parameter2Scroll.setVisible(false);
				break;
			}
			setDistribution();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scrollbar events for changing the parameters
	* of the selected distsribution.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == parameter1Scroll.getSlider()) setDistribution();
		else if (e.getSource() == parameter2Scroll.getSlider())	setDistribution();
		else super.stateChanged(e);
	}

	/**
	* This method updates the experiment, including the record table, random
	* variable graph and table.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), rv.getValue()});
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
	* This method resets the experiment, including the record table and the
	* random variable graph and table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		rv.reset();
		rvTable.reset();
		rvGraph.reset();
	}

	/**
	* This method sets the distribution, after the selected distribution or the
	* parameters of the distribution have changed.
	*/
	public void setDistribution(){
		double mu, sigma, a, b, k;
		int n, m;
		switch(distributionType){
		case 0:	 //Normal
			mu = parameter1Scroll.getValue();
			sigma = parameter2Scroll.getValue();
			rv = new RandomVariable(new NormalDistribution(mu, sigma), "X");
			break;
		case 1:	 //Gamma
			k = parameter1Scroll.getValue();
			b = parameter2Scroll.getValue();
			rv = new RandomVariable(new GammaDistribution(k, b), "X");
			break;
		case 2:	 //Chi-square
			n = (int)parameter1Scroll.getValue();
			rv = new RandomVariable(new ChiSquareDistribution(n), "X");
			break;
		case 3:	 //Student
			n = (int)parameter1Scroll.getValue();
			rv = new RandomVariable(new StudentDistribution(n), "X");
			break;
		case 4:	 //Fisher
			m = (int)parameter1Scroll.getValue();
			n = (int)parameter2Scroll.getValue();
			rv = new RandomVariable(new FisherDistribution(m, n), "X");
			break;
		case 5:	 //Beta
			a = parameter1Scroll.getValue();
			b = parameter2Scroll.getValue();
			rv = new RandomVariable(new BetaDistribution(a, b), "X");
			break;
		case 6:   //Wiebull
			k = parameter1Scroll.getValue();
			b = parameter2Scroll.getValue();
			rv = new RandomVariable(new WeibullDistribution(k, b), "X");
			break;
		case 7:   //Pareto
			a = parameter1Scroll.getValue();
			rv = new RandomVariable(new ParetoDistribution(a), "X");
			break;
		case 8:   //Logistic
			rv = new RandomVariable(new LogisticDistribution(), "X");
			break;
		case 9:   //Lognormal
			mu = parameter1Scroll.getValue();
			sigma = parameter2Scroll.getValue();
			rv = new RandomVariable(new LogNormalDistribution(mu, sigma), "X");
			break;
		case 10: //Extreme Value
			rv = new RandomVariable(new ExtremeValueDistribution(), "X");
			break;
		}
		rvGraph.setRandomVariable(rv);
		rvTable.setRandomVariable(rv);
		reset();
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

