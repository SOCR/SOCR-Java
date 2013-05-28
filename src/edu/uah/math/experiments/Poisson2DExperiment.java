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

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.ScatterPlot;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.PoissonDistribution;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the 2 dimensional Poisson process. Random points in a square
* are displayed in a scatterplot. The size of the square and the Poisson rate
* parameter can be adjusted.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Poisson2DExperiment extends Experiment implements Serializable{
	//Variables
	private double width = 2, rate = 1;
	private int pointCount;
	//Ojbects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "N"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter widthScroll = new Parameter(0.5, 5, 0.5, width, "Width", "w");
	private Parameter rateScroll = new Parameter(0.5, 5, 0.5, rate, "Rate", "r");
	private PoissonDistribution dist = new PoissonDistribution(rate * width * width);
	private RandomVariable points = new RandomVariable(dist, "N");
	private RandomVariableGraph pointsGraph = new RandomVariableGraph(points);
	private RandomVariableTable pointsTable = new RandomVariableTable(points);
	private ScatterPlot scatterPlot = new ScatterPlot(new Domain(0, width, 0.1, Domain.CONTINUOUS));

	/**
	* This method initializes the experiment, including the toolbar with scrollbars
	* and labels, the scatterplot, and the random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Two-Dimensional Poisson Experiment");
		//Sliders
		rateScroll.applyDecimalPattern("0.0");
		rateScroll.getSlider().addChangeListener(this);
		widthScroll.applyDecimalPattern("0.0");
		widthScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(widthScroll);
		toolBar.add(rateScroll);
		addToolBar(toolBar);
		//Scatterplot
		scatterPlot.setToolTipText("(x, y) scatterplot");
		scatterPlot.setPointSize(2);
		addComponent(scatterPlot, 0, 0, 1, 1);
		//Random Variable Graph
		addComponent(pointsGraph, 1, 0, 1, 1);
		//Record table
		recordTable.setDescription("N: number of points");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random Variable Table
		addComponent(pointsTable, 1, 1, 1, 1);
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
			+ "The experiment is to run a Poisson process in the plane, and record the number of points N\n"
			+ "in the square [0, w] x [0, w]. The points are shown as red dots in the scatterplot.\n"
			+ "The number of points N is recorded on each update. The density and moments of N are shown\n"
			+ "in in blue in the distribution graph and are recorded in the distribution table. On each\n"
			+ "update, the empirical density of N is shown in red in the distribution graph and is recorded\n"
			+ "in the distribution table. The parameters of the experiment are the rate r of the process\n"
			+ "and the side length w of the square, which can be varied with scroll bars.";
	}

   /**
   * This method defines the experiment.  The number of poins is simulated as a value
   * from the Poisson distribution.  Then, the points uniformly distributed in the
   * scatterplot.
   */
	public void doExperiment(){
		double x, y;
		super.doExperiment();
		pointCount = (int)points.simulate();
		scatterPlot.resetData();
		for(int i = 0; i < pointCount; i++){
			x = width * Math.random();
			y = width * Math.random();
			scatterPlot.addPoint(x, y);
		}
   }

   /**
   * This method resets the experiment, including the scatterplot, record table, random
   * variable graph and table.
   */
	public void reset(){
		super.reset();
		recordTable.reset();
		scatterPlot.reset();
		points.reset();
		pointsGraph.reset();
		pointsTable.reset();
	}

   /**
   * This method updates the experiment, including the scatterplot, record table,
   * random variable graph and table.
   */
	public void update(){
		super.update();
		scatterPlot.repaint();
		recordTable.addRecord(new double[]{getTime(), pointCount});
		pointsGraph.repaint();
		pointsTable.repaint();
	}
	
	public void graphUpdate(){
		super.update();
	//	scatterPlot.setShowModelDistribution(showModelDistribution);
		pointsGraph.setShowModelDistribution(showModelDistribution);
		pointsGraph.repaint();
		pointsTable.setShowModelDistribution(showModelDistribution);
		pointsTable.repaint();
	}

   /**
   * This method handles the scrollbar events for changing the rate and width
   * properties.
   * @param e the change event
   */
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == rateScroll.getSlider()){
			rate = rateScroll.getValue();
			setDistribution();
		}
		if (e.getSource() == widthScroll.getSlider()){
			width = widthScroll.getValue();
			scatterPlot.setDomain(new Domain(0, width, 0.1, Domain.CONTINUOUS));
			setDistribution();
		}
 	}

	/**
	* This method sets the Poisson distribution when the parameters have changed.
	*/
	public void setDistribution(){
		dist.setParameter(rate * width * width);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}


