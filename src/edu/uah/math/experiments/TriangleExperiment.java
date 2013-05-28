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
import java.io.Serializable;
import java.awt.Dimension;

import javax.swing.JTable;

import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.Stick;
import edu.uah.math.devices.TriangleScatter;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* The triangle experiment is to break a stick at random and see if the pieces form
* an acute triangle, an obtuse triangle, or no triangle at all.
* @author Kyle Siegrist
* @author Dawn Duehring
*/
public class TriangleExperiment extends Experiment implements Serializable{
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "Y", "U"});
	private TriangleScatter scatterPlot = new TriangleScatter();
	private Stick stick = new Stick();
	private RandomVariable typeRV = new RandomVariable(new FiniteDistribution(
		0, 2, 1, new double[] {0.75, 0.170558458, 0.079441542}), "U");
	private RandomVariableGraph typeGraph = new RandomVariableGraph(typeRV);
	private RandomVariableTable typeTable = new RandomVariableTable(typeRV);

	/**
	* This method initializes the experiment, including the toolbar, triangle, scatterplot, random
	* variable graph and table.
	*/
	public void init(){
		//Initialize
		super.init();
		setName("Triangle Experiment");
		//Stick
		stick.setMinimumSize(new Dimension(100, 100));
		addComponent(stick, 0, 0, 1, 1);
		//Scatterplot
		scatterPlot.setMinimumSize(new Dimension(100, 100));
		addComponent(scatterPlot, 1, 0, 1, 1);
		//Random Variable Graph
		typeGraph.setMomentType(0);
		typeGraph.setMargins(35, 20, 20, 20);
		typeGraph.setMinimumSize(new Dimension(150, 100));
		addComponent(typeGraph, 2, 0, 1, 1);
		//Record Table
		recordTable.setDescription("X, Y: cutpoints, U: triangle type");
		addComponent(recordTable, 0, 1, 2, 1);
		//Random Variable Table
		typeTable.setStatisticsType(0);
		addComponent(typeTable, 2, 1, 1, 1);
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
			+ "Two points are chosen at random in the interval [0, 1]; X denotes the first point chosen\n"
			+ "and Y denotes the second point chosen. Random variable U gives the type of triangle that\n"
			+ "can be formed from the three sub-intervals: U = 0, the pieces do not form a triangle; U = 1,\n"
			+ "the pieces form an obtuse triangle; U = 2, the pieces form an acute triangle. The first\n"
			+ "picture box shows the outcome of the experiment graphically. On each update, the cut points\n"
			+ "X and Y are shown as red dots, and the triangle is sketched when U = 1 or U = 2. The second\n"
			+ "picture box shows the sample space and the three events of interest: U = 2 consists of the 2\n"
			+ "interior regions; U = 1 consists of the 6 middle regions; U = 0 consists of the outer regions.\n"
			+ "On each run, (X, Y) is shown as a red dot in the scatterplot, and is recorded in the first\n"
			+ "table on each update. The probability density function of U is shown in blue in the graph box\n"
			+ "and is recorded in the graph table. On each update, the empirical density of U is shown in red\n"
			+ "in the graph box and is recorded in the graph table. Additionally, the value of U is recorded\n"
			+ "in the first table on each update";
	}

	/**
	* This method defines the experiment. The stick is randomly broken and the type of
	* triangle is determined.  The corresponding point is plotted in the scatterplot.
	*/
	public void doExperiment(){
		super.doExperiment();
		stick.setCutPoints();
		typeRV.setValue(stick.getType());
		scatterPlot.addPoint(stick.getCutX(), stick.getCutY());
	}

	/**
	* This method runs the the experiment one time, and adds sounds depending on the outcome
	* of the experiment.
	*/
	public void step(){
		doExperiment();
		update();
		playnote(stick.getType());
	}

	/**
	* This method updates the display, including the triangle, the random varible graph and table,
	* and the record table.
	*/
	public void update(){
		super.update();
		stick.repaint();
		scatterPlot.repaint();
		typeGraph.repaint();
		typeTable.repaint();
		recordTable.addRecord(new double[] {getTime(), stick.getCutX(), stick.getCutY(), stick.getType()});
	}

	public void graphUpdate(){
		super.update();
		scatterPlot.setShowModelDistribution(showModelDistribution);
		typeGraph.setShowModelDistribution(showModelDistribution);
		scatterPlot.repaint();
		typeGraph.repaint();
		typeTable.setShowModelDistribution(showModelDistribution);
		typeTable.repaint();
	}
	/**
	* This method resets the experiment, including the record table, triangle, scatterplot,
	* random variable table and graph.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		stick.reset();
		scatterPlot.reset();
		typeRV.reset();
		typeGraph.reset();
		typeTable.reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}