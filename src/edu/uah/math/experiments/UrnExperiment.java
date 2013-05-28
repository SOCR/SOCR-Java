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
import java.awt.Color;
import java.io.Serializable;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Urn;
import edu.uah.math.devices.Ball;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.HypergeometricDistribution;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class defines a basic experiment based on drawing balls from an urn.
* The balls are red and green.
* The method sampling (with or without replacement) can be specified, along with
* the population size, sample size, and number of red balls.
* The random variable of interest is the number of red balls in the sample.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class UrnExperiment extends Experiment implements Serializable{
	//Variables
	public final static int WITH_REPLACEMENT = 1, WITHOUT_REPLACEMENT = 0;
	private int type = WITHOUT_REPLACEMENT, totalBalls = 50, totalRed = 25, sampleSize = 10, sum, trial;
	private double totalEstimate, redEstimate;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "Y", "M", "U", "V"});
	private Urn urn = new Urn(sampleSize, 26);
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private HypergeometricDistribution dist0 = new HypergeometricDistribution(totalBalls, totalRed, sampleSize);
	private BinomialDistribution dist1 = new BinomialDistribution(sampleSize, (double)totalRed / totalBalls);
	
	private LocationScaleDistribution averageDist = new LocationScaleDistribution(dist1, 0, 1.0 /sampleSize);

	
	private RandomVariable rvSum = new RandomVariable(dist0, "Y");
	private RandomVariable averageRV = new RandomVariable(averageDist, "M");
	
	private RandomVariableGraph rvGraph = new RandomVariableGraph(rvSum);
	private RandomVariableTable rvTable = new RandomVariableTable(rvSum);
	//Scrollbars and labels
	private Parameter totalScroll = new Parameter(1, 100, 1, totalBalls, "Population size", "N");
	private Parameter redScroll = new Parameter(0, totalBalls, 1, totalRed, "Number of red balls", "R");
	private Parameter sampleScroll = new Parameter(1, totalBalls, 1, sampleSize, "Sample size", "n");
	
	//Choices
	private JComboBox samplingChoice = new JComboBox();
	private JComboBox rvChoice = new JComboBox();
	
	//Timer
	private Timer timer = new Timer(100, this);

	/**
	* This method initialize the experiment, including the record table, the sliders
	* for the number of balls, the sample size, and the number of red balls, the choice
	* box for the type of sampling, the tool bar, the urn, and the graph and table for
	* the number of red balls.
	*/
	public void init(){
		super.init();
		setName("Ball and Urn Experiment");
		//Population size slider
		totalScroll.setWidth(140);
		totalScroll.getSlider().addChangeListener(this);
		//Red ball slider
		redScroll.setWidth(140);
		redScroll.getSlider().addChangeListener(this);
		//Sample size slider
		sampleScroll.setWidth(140);
		sampleScroll.getSlider().addChangeListener(this);
		
		//Sample choice
		samplingChoice.addItemListener(this);
		samplingChoice.setToolTipText("Sampling model");
		samplingChoice.addItem("Replacement: No");
		samplingChoice.addItem("Replacement: Yes");

		//Sample choice
		rvChoice.addItemListener(this);
		rvChoice.setToolTipText("Random Variable: Y: Number of Red Balls; M: Proportion of Red Balls");
		rvChoice.addItem("Y: Number of Red balls");
		rvChoice.addItem("M: Proportion of Red balls");

		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(samplingChoice);
		toolBar.add(rvChoice);
		toolBar.add(totalScroll);
		toolBar.add(redScroll);
		toolBar.add(sampleScroll);
		addToolBar(toolBar);
		//Graphs
		urn.setMinimumSize(new Dimension(100, 100));
		addComponent(urn, 0, 0, 1, 1);
		rvGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(rvGraph, 1, 0, 1, 1);
		//Tables
		recordTable.setDescription("Y: Number of red balls in the sample; "+
				"M: Proportion of red balls in the sample; U: Estimate of R; V: Estimate of N");
		addComponent(recordTable, 0, 1, 1, 1);
		addComponent(rvTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive informaion, and instructions.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment consists of selecting n balls at random from an urn with N balls, R of which\n"
			+ "are red and the other N - R green. The number (Y) and proportion (M) of red balls in the sample is recorded on\n"
			+ "each update. The distribution and moments of Y are shown in blue in the distribution graph\n"
			+ "and are recorded in the distribution table. On each update, the empirical density and moments\n"
			+ "of Y are shown in red in the distribution graph and are recorded in the distribution table.\n"
			+ "Either of two sampling models can be selected with the list box: with replacement and without\n"
			+ "replacement. The parameters N, R, and n can be varied with scroll bars.";
	}

	/**
	* This method handles the events associated with the choice box that determines
	* the type of sampling.
	* @param e the item event.
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == samplingChoice){
			type = samplingChoice.getSelectedIndex();
			if (type == WITH_REPLACEMENT) { 
				sampleScroll.setRange(1, 100, 1, sampleSize);
			}
			else{
				sampleSize = Math.min(sampleSize, totalBalls);
				sampleScroll.setRange(1, Math.min(100, totalBalls), 1,  sampleSize);
			}
			setParameters();
		} else if(e.getSource() == rvChoice){
			if (rvChoice.getSelectedIndex() == 0){
				rvGraph.setRandomVariable(rvSum);
				rvTable.setRandomVariable(rvSum);
			}
			else{
				rvGraph.setRandomVariable(averageRV);
				rvTable.setRandomVariable(averageRV);
			}
			rvGraph.repaint();
			rvTable.repaint();
		} else super.itemStateChanged(e);
	}

	/**
	* This method handles the slider events associated with changing the parameters
	* (the number of balls, the number of red balls, and the sample size).
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == totalScroll.getSlider()){
			totalBalls = (int)totalScroll.getValue();
			totalRed = Math.min(totalRed, totalBalls);
			redScroll.setRange(0, totalBalls, 1, totalRed);
			if (type == WITHOUT_REPLACEMENT){
				sampleSize = Math.min(sampleSize, totalBalls);
				sampleScroll.setRange(1, Math.min(100, totalBalls), 1, sampleSize);
			}
			setParameters();
		}
		else if (e.getSource() == redScroll.getSlider()){
			totalRed = (int)redScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			urn.setBallCount(sampleSize);
			setParameters();
		}
	}

	/**
	* This method sets the distribution for the random variable in terms of
	* the parameters (the number of balls, the number of red balls, and the
	* sample size.
	*/
	public void setParameters(){
		dist0.setParameters(totalBalls, totalRed, sampleSize);
		dist1.setParameters(sampleSize, (double)totalRed / totalBalls);
		
		if (type == WITH_REPLACEMENT) { 
			rvSum.setDistribution(dist1);
			averageDist = new LocationScaleDistribution(dist1, 0, 1.0 /sampleSize);
			//averageDist = new LocationScaleDistribution(dist0, 0, 1.0 /sampleSize);
			averageRV.setDistribution(averageDist);
		} else { 
			rvSum.setDistribution(dist0);
			averageDist = new LocationScaleDistribution(dist0, 0, 1.0 /sampleSize);
			averageRV.setDistribution(averageDist);
		}
		reset();
	}

	/**
	* This method defines the experment. The balls are chosen at random
	* and the number of red balls in the sample computed.
	*/
	public void doExperiment(){
		sum = 0;
		super.doExperiment();
		
		urn.sample(totalBalls, type);
		// Should this be: urn.sample(sampleSize, type);?
		
		for (int i = 0; i < sampleSize; i++) setBall(i);
		rvSum.setValue(sum);
		averageRV.setValue((1.0*sum)/sampleSize);

		redEstimate = (double)totalBalls * sum / sampleSize;
		totalEstimate = (double)sampleSize * totalRed / sum;
	}

	/**
	* This method configures a specified ball.
	*/
	private void setBall(int i){
		Ball ball = urn.getBall(i);
		if (ball.getValue() <= totalRed){
			sum++;
			ball.setBallColor(RED);
		}
		else ball.setBallColor(GREEN);
	}

	/**
	* This method updates the display, including the urn, the random variable
	* graph and table, and the record table.
	*/
	public void update(){
		super.update();
		urn.setDrawn(true);
		rvGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.repaint();
		recordTable.addRecord(new double[]{
				getTime(), sum, (1.0*sum)/sampleSize, redEstimate, totalEstimate});
		rvTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		rvGraph.setShowModelDistribution(showModelDistribution);
	//	urn.setDrawn(true);
		rvGraph.repaint();
		//recordTable.addRecord(new double[]{getTime(), sum, redEstimate, totalEstimate});
		rvTable.setShowModelDistribution(showModelDistribution);
		rvTable.repaint();
	}
	/**
	* This method runs the experiment one time, with additional annimation and sound..
	*/
	public void step(){
		stop();
		sum = 0;
		trial = 0;
		
		urn.sample(totalBalls, type);
		// Should this be: urn.sample(sampleSize, type);
		
		urn.setDrawn(false);
		timer.start();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the urn, the random variable,
	* the random variable graph and table, and the record table.
	*/
	public void reset(){
		timer.stop();
		//Initialize variables
		super.reset();
		recordTable.reset();
		urn.setDrawn(false);
		rvSum.reset();
		averageRV.reset();

		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method handles the events associated with the timer. The balls are shown one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < sampleSize){
				setBall(trial);
				urn.getBall(trial).setDrawn(true);
				//playnote(urn.getBall(trial).getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				redEstimate = (double)totalBalls * sum / sampleSize;
				totalEstimate = (double)sampleSize * totalRed / sum;
				
				rvSum.setValue(sum);
				averageRV.setValue((1.0*sum)/sampleSize);
				
				rvGraph.repaint();
				recordTable.addRecord(new double[]{
						getTime(), sum, (1.0*sum)/sampleSize, redEstimate, totalEstimate});
				rvTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

