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
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.Serializable;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.NeedleScatter;
import edu.uah.math.devices.NeedleFloor;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.EstimateGraph;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.RecordTable;

/**
* Buffon's needle experiment consists of dropping a needle on a wooden floor.
* The event of interest is that the needle crosses a floorboard crack.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BuffonNeedleExperiment extends Experiment implements Serializable{
	//Variables
	private double angle, distance, estimate;
	private double length = 0.5;
	//Objects
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter lengthSlider = new Parameter(0.05, 1, 0.05, length, "Needle Length", "L");
	private NeedleScatter scatterPlot = new NeedleScatter(length);
	private NeedleFloor floor = new NeedleFloor(length);
	private EstimateGraph piGraph = new EstimateGraph(Math.PI);
	private StatisticsTable piTable = new StatisticsTable("pi Estimate", new String[]{"Value", "Error"});
	private BernoulliDistribution crossDist = new BernoulliDistribution(2 * length / Math.PI);
	private RandomVariable crossRV = new RandomVariable(crossDist, "I");
	private RandomVariableGraph crossGraph = new RandomVariableGraph(crossRV);
	private RecordTable recordTable = new RecordTable(new String[] {"Run", "X", "Y", "I"});
	private RandomVariableTable crossTable = new RandomVariableTable(crossRV);

	/**
	* This method initialize the experiment, including the tables, graphs, toolbar.
	*/
	public void init(){
		//Initialize
		super.init();
		setName("Buffon's Needle Experiment");
		//Length slider
		lengthSlider.applyDecimalPattern("0.00");
		lengthSlider.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(lengthSlider);
		addToolBar(toolBar);
		//Floor
		addComponent(floor, 0, 0, 1, 1);
		//Scatter plot
		scatterPlot.setToolTipText("(X, Y) Scatterplot");
		addComponent(scatterPlot, 1, 0, 1, 1);
		//Cross graph
		crossGraph.setMomentType(0);
		crossGraph.setMargins(35, 20, 20, 20);
		addComponent(crossGraph, 2, 0, 1, 1);
		//pi Graph
		piGraph.setToolTipText("Pi Estimate");
		addComponent(piGraph, 3, 0, 1, 1);
		//Record Table
		recordTable.setDescription("X: angle, Y: distance, I: needle crosses crack");
		addComponent(recordTable, 0, 1, 2, 1);
		//Cross table
		crossTable.setStatisticsType(0);
		addComponent(crossTable, 2, 1, 1, 1);
		//pi table
		piTable.setShow(StatisticsTable.DATA);
		addComponent(piTable, 3, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "Buffon's needle experiment consists of dropping a needle on hardwood floor, with floorboards \n"
			+ "of width 1. The experiment is shown graphically in the first graph box. The angle X of \n"
			+ "the needle relative to the floorboard cracks and the distance Y from the center of the needle\n"
			+ "to the lower crack are recorded in the first table on each update. Each point (X, Y) is shown\n"
			+ "as a red dot in the scatterplot. The variable I indicates the event that the needle crosses\n"
			+ "a crack. The density of I is shown in blue in the distribution graph and is recorded in the \n"
			+ "distribution table. On each update, the empirical density of I is shown in red in the \n"
			+ "distribution graph and is also recorded in the distribution table. Finally, pi is shown \n"
			+ "in blue in the last graph and is recorded in the last table, and on each update, Buffon's \n"
			+ "estimate of pi is shown in red in the last graph and is recorded in the last table. \n"
			+ "The parameter is the needle length L, which can be varied with a scrollbar.";
	}

	/**
	* This method handles slider events associated with changes in the length of the needle.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == lengthSlider.getSlider()){
			length = lengthSlider.getValue();
			floor.setLength(length);
			scatterPlot.setLength(length);
			crossDist.setProbability(2 * length / Math.PI);
			reset();
		}
		else super.stateChanged(e);
	}

	/**
	* This method defines the experiment. The distance and angle variables are
	* randomly generated and the cross event tested. The estimate of &pi; is computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		floor.setValues();
		distance = floor.getDistance(); angle = floor.getAngle();
		if (floor.crossEvent()) crossRV.setValue(1); else crossRV.setValue(0);
		estimate = 2 * length * getTime() / crossRV.getIntervalData().getFreq(1);
		scatterPlot.addPoint(angle, distance);
	}

	/**
	* This method updates the displays, including the floor, the pi estimate graph,
	* the corss event random variable, and the random variable table and graph,
	* record table.
	*/
	public void update(){
		super.update();
		floor.setNeedleDropped(true);
		scatterPlot.repaint();
		piGraph.setEstimate(estimate);
		piGraph.repaint();
		crossGraph.repaint();
		crossTable.repaint();
		recordTable.addRecord(new double[]{getTime(), angle, distance, crossRV.getValue()});
		piTable.setDataValues(new double[]{estimate, estimate - Math.PI});
	}
	public void graphUpdate(){
		super.update();
	//	floor.setNeedleDropped(true);
		scatterPlot.setShowModelDistribution(showModelDistribution);
		scatterPlot.repaint();
	//	piGraph.setEstimate(estimate);
		piGraph.setShowModelDistribution(showModelDistribution);
		crossGraph.setShowModelDistribution(showModelDistribution);
		piGraph.repaint();
		crossGraph.repaint();
		crossTable.setShowModelDistribution(showModelDistribution);
		piTable.setShowModelDistribution(showModelDistribution);
		crossTable.repaint();
		piTable.repaint();
	
	}
	/**
	* This method runs the experiment one time and plays sounds depending on
	* the outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote((int)crossRV.getValue());
	}

	/**
	* This method resets the experiment, including the record table, the pi estimate table,
	* the floor, the scatter plot, the pi estimate graph, the cross event random variable,
	* and the random variable graph and table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		floor.setNeedleDropped(false);
		scatterPlot.reset();
		piGraph.setEstimate(0);
		piGraph.repaint();
		piTable.reset();
		crossRV.reset();
		crossGraph.reset();
		crossTable.reset();
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}
