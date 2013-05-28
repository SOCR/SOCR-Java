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
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.Coin;
import edu.uah.math.devices.CoinBox;
import edu.uah.math.devices.BetaGraph;

/**
* This class models the beta coin experiment.  The probability of heads p
* for a coin is given a prio rbeta distribution.  The coin is tossed n time, and the
* posterior distribution of p is obtained.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BetaCoinExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 10, heads, trial;
	private double p = 0.5, a = 1, b = 1, pEstimate, distBias, distMSE, dataBias, dataMSE;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "U"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private CoinBox coinBox = new CoinBox(n, p, 26);
	private Parameter nScroll = new Parameter(1, 60, 1, n, "Number of tosses", "n");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Probability of heads", "p");
	private Parameter aScroll = new Parameter(1, 10, 0.5, a, "Left beta parameter", "a");
	private Parameter bScroll = new Parameter(1, 10, 0.5, b, "Right beta parameter", "b");
	private StatisticsTable statTable = new StatisticsTable("U", new String[]{"Bias", "MSE"});
	private Timer timer = new Timer(100, this);
	private BetaGraph betaGraph = new BetaGraph(a, b, n, p);

	/**
	* This method initializes the experiment by setting up the toolbar, scrollbars.
	* label, coin panel, and graphs.
	*/
	public void init(){
		super.init();
		setName("Beta Coin Experiment");
		//n Slider
		nScroll.setWidth(125);
		nScroll.getSlider().addChangeListener(this);
		//p Slider
		pScroll.applyDecimalPattern("0.00");
		pScroll.setWidth(150);
		pScroll.getSlider().addChangeListener(this);
		//a Scroll
		aScroll.applyDecimalPattern("0.0");
		aScroll.setWidth(125);
		aScroll.getSlider().addChangeListener(this);
		//b Scroll
		bScroll.applyDecimalPattern("0.0");
		bScroll.setWidth(125);
		bScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(nScroll);
		toolBar.add(pScroll);
		toolBar.add(aScroll);
		toolBar.add(bScroll);
		addToolBar(toolBar);
		//Coin box
		coinBox.setMinimumSize(new Dimension(100, 100));
		addComponent(coinBox, 0, 0, 1, 1);
		//Beta graph
		betaGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(betaGraph, 1, 0, 1, 1);
		//Record Table
		recordTable.setDescription("Y: number of heads, U: estimator of p");
		addComponent(recordTable, 0, 1, 1, 1);
		//Statistics Table
		statTable.setDescription("Bias and mean square error of U as an estimator of p");
		addComponent(statTable, 1, 1, 1, 1);
		//Final actions
		validate();
		setParameters();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment is to toss a coin n times, where the probability of heads is p.\n"
			+ "The probability of heads is modeled with a prior beta distribution, having parameters\n"
			+ "a and b. The prior density and the true probability of heads are shown in blue in\n"
			+ "the graph on the right. On each update, the number of heads Y is recorded in the first\n"
			+ "table. On each update, the posterior beta density, which has parameters a + Y and b + n - Y\n"
			+ "is shown in red in graph on the right. Also, the Bayesian estimate of p, U = (a + Y) / (a + b + n)\n"
			+ "is recorded in the first table on each update. Finally, the second table gives the true\n"
			+ "distBias and mean square error of U, and on each update gives the empirical distBias and mean\n"
			+ "square error, based on the all of the runs of the experiment. The parameters n, p,\n"
			+ "a, and b can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment: the coins are tossed and then the
	* estimate of p and the empirical distBias and empirical mean square error are
	* computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		coinBox.toss();
		heads = coinBox.getHeadCount();
		pEstimate = (double)(heads + a) / (n + a + b);
		int runs = getTime();
		dataBias = ((runs - 1) * dataBias + (pEstimate - p)) / runs;
		dataMSE =  ((runs - 1) * dataMSE + (pEstimate - p) * (pEstimate - p)) / runs;
 	}

 	/**
 	* This method runs the experiment one time, with additional sound and annimation.
 	*/
 	public void step(){
		stop();
		trial = 0;
		coinBox.setTossed(false);
		coinBox.toss();
		timer.start();
	}

	/**
	* This method stops the step proceess, if necessary, and then calls the usual run method.
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
	* This method resets the experiment, including the record table, the beta
	* graph, the statistcs table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		coinBox.setTossed(false);
		//betaGraph.setParameters(a, b, n, p);
		betaGraph.setPosteriorDrawn(false);
		statTable.reset();
	}

	/**
	* This method updates the experiment, including the record table, statistics
	* table, and beta graph.
	*/
	public void update(){
		super.update();
		coinBox.setTossed(true);
		recordTable.addRecord(new double[] {getTime(), heads, pEstimate});
		statTable.setDataValues(new double[]{dataBias, dataMSE});
		betaGraph.setSuccesses(heads);
		betaGraph.setPosteriorDrawn(true);
	}
	
	public void graphUpdate(){
		super.update();
		//coinBox.setTossed(true);
		//recordTable.addRecord(new double[] {getTime(), heads, pEstimate});
		//statTable.setDataValues(new double[]{dataBias, dataMSE});
		betaGraph.setShowModelDistribution(showModelDistribution);
		//betaGraph.setSuccesses(heads);
		//betaGraph.setPosteriorDrawn(true);
		betaGraph.repaint();
		statTable.setShowModelDistribution(showModelDistribution);
		statTable.repaint();
	}
	/**
	* This method handles the timer events associated with the step process. The coins are
	* shown one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < n){
				Coin coin = coinBox.getCoin(trial);
				coin.setTossed(true);
				//playnote(coin.getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				heads = coinBox.getHeadCount();
				pEstimate = (double)(heads + a) / (n + a + b);
				int runs = getTime();
				dataBias = ((runs - 1) * dataBias + (pEstimate - p)) / runs;
				dataMSE =  ((runs - 1) * dataMSE + (pEstimate - p) * (pEstimate - p)) / runs;
				recordTable.addRecord(new double[] {runs, heads, pEstimate});
				statTable.setDataValues(new double[]{dataBias, dataMSE});
				betaGraph.setSuccesses(heads);
				betaGraph.setPosteriorDrawn(true);
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method handles the scroll events for changing the probability of
	* heads, the left and right beta parameters, and the number of heads.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			coinBox.setCoinCount(n);
			betaGraph.setTrials(n);
			setParameters();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			coinBox.setProbability(p);
			betaGraph.setProbability(p);
			setParameters();
		}
		else if (e.getSource() == aScroll.getSlider()){
			a = aScroll.getValue();
			betaGraph.setLeft(a);
			setParameters();
		}
		else if (e.getSource() == bScroll.getSlider()){
			b = bScroll.getValue();
			betaGraph.setRight(b);
			setParameters();
		}
	}

	/**
	* This method sets the true distBias and mean square error, after the scrollbar
	* parameters have changed.
	*/
	public void setParameters(){
		distBias = (a - p * a - p * b) / (a + b + n);
		distMSE = (p * (n - 2 * a * a - 2 * a * b)
			+ p * p * (a * a + b * b + 2 * a * b - n ) + a * a)
			/ ((n + a + b) * (n + a + b));
		statTable.setDistributionValues(new double[]{distBias, distMSE});
		reset();
	}
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

