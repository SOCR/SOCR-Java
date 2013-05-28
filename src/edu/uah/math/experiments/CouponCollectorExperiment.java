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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.distributions.CouponDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.CellGrid;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;

/**
* The coupon collector experiment consists of selecting a sampling, with
* replacement from a finite population. The random variable of interest is the sample
* size needed to get a specified number of elements in the population.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CouponCollectorExperiment extends Experiment implements Serializable{
	//Variables
	private int cells = 20, occupied = 10, ballCount, occupiedCount, currentCell;
	private int[] cellCount;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[] {"Run", "W"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private CellGrid cellGrid = new CellGrid(cells);
	private Parameter cellsScroll = new Parameter(1, 50, 1, cells, "Number of cells", "m");
	private Parameter occupiedScroll = new Parameter(1, cells, 1, occupied, "Number to be occupied", "k");
	private CouponDistribution dist = new CouponDistribution(cells, occupied);
	private RandomVariable ballsRV = new RandomVariable(dist, "W");
	private RandomVariableGraph ballsGraph = new RandomVariableGraph(ballsRV);
	private RandomVariableTable ballsTable = new RandomVariableTable(ballsRV);
	private Timer timer = new Timer(100, this);

	/**
	* This method initialize the experiment, including the record table, the sliders
	* for the population size and the number of distinct values to be obtained,
	* the toolbar, the cell grid, and the random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Coupon Collector Experiment");
		//Event listeners
		cellsScroll.getSlider().addChangeListener(this);
		occupiedScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(cellsScroll);
		toolBar.add(occupiedScroll);
		addToolBar(toolBar);
		//Cell Grid
		cellGrid.setMinimumSize(new Dimension(100, 100));
		addComponent(cellGrid, 0, 0, 1, 1);
		//Random variable graph
		ballsGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(ballsGraph, 1, 0, 1, 1);
		//Record table
		recordTable.setDescription("W: number of balls");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random variable table
		addComponent(ballsTable, 1, 1, 1, 1);
		//Finalize
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
			+ "The coupon collector experiment consists of sampling with replacement from the\n"
			+ "population {1, 2, ..., m} until k distinct values are obtained. The first graph gives\n"
			+ "the counts for each population value that occurred in the sample. Random variable W\n"
			+ "gives the sample size and is recorded in the first table on each update. The density\n"
			+ "and moments of W are shown in blue in the second graph and are recorded in the second table.\n"
			+ "On each update, the empirical density and moments of W are shown in red in the second graph\n"
			+ "and are recorded in the second table. The parameters m and k can be varied with scroll bars.";
	}

	/**
	* This method handles the slider events associated with the population size
	* (the number of cells), and the number of distinct values to be obtained..
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == cellsScroll.getSlider()){
			cells = (int)cellsScroll.getValue();
			if (occupied > cells) occupied = cells;
			occupiedScroll.setMax(cells);
			occupiedScroll.setValue(occupied);
			cellGrid.setCells(cells);
			setParameters();
		}
		else if (e.getSource() == occupiedScroll.getSlider()){
			occupied = (int)occupiedScroll.getValue();
			setParameters();
		}
	}

	/**
	* This method sets distribution of the sample size random variable, and is
	* called whenever the parameters (the population size or the number of distinct
	* values to be obtained) varies.
	*/
	public void setParameters(){
		dist.setParameters(cells, occupied);
		reset();
	}

	/**
	* This method defines the experiment. Items from the population are selected at
	* random until the appropriate number of distinct values is obtained. The sample
	* size is then recorded.
	*/
	public void doExperiment(){
		super.doExperiment();
		ballCount = 0;
		occupiedCount = 0;
		cellCount = new int[cells];
		while (occupiedCount < occupied){
			currentCell = (int)(cells * Math.random());
			ballCount++;
			if (cellCount[currentCell] == 0) occupiedCount++;
			cellCount[currentCell]++;
		}
		ballsRV.setValue(ballCount);
	}

	/**
	* This method updates the display, including the cell grid, record table, and
	* the random variable graph and table.
	*/
	public void update(){
		super.update();
		cellGrid.setCellCount(cellCount);
		cellGrid.setShow(CellGrid.COUNTS);
		recordTable.addRecord(new double[]{getTime(), ballCount});
		ballsGraph.repaint();
		ballsTable.repaint();
	}
	public void graphUpdate(){
		super.update();
		ballsGraph.setShowModelDistribution(showModelDistribution);
		ballsGraph.repaint();
		ballsTable.setShowModelDistribution(showModelDistribution);
		ballsTable.repaint();
	}
	/**
	* This method runs the experiment one time. The balls are shown going into the cells.
	*/
	public void step(){
		stop();
		ballCount = 0;
		occupiedCount = 0;
		cellCount = new int[cells];
		cellGrid.reset();
		timer.start();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method resets the experiment, including the record table, cell grid, the
	* sample size random varible, and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		cellGrid.reset();
		ballsRV.reset();
		ballsGraph.reset();
		ballsTable.reset();
	}

	/**
	* This method handles the events associated with the step timer.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (occupiedCount < occupied){
				currentCell = (int)(cells * Math.random());
				ballCount++;
				if (cellCount[currentCell] == 0) occupiedCount++;
				cellCount[currentCell]++;
				cellGrid.setCurrentCell(currentCell);
				cellGrid.setShow(CellGrid.BALL);
				//playnote(currentCell);
			}
			else{
				timer.stop();
				super.doExperiment();
				ballsRV.setValue(ballCount);
				update();
			}
		}
		else super.actionPerformed(e);
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}


