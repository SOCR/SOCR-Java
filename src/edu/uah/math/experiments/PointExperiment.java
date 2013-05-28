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
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.CoinBox;
import edu.uah.math.devices.Coin;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.Functions;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the famous problem of points experiment. Two player engage
* in Bernoulli trials. The event of interest is that player 1 wins n points before
* playeer 2 wins m points.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class PointExperiment extends Experiment implements Serializable{
	private int n = 10, m = 9, trials, heads;
	private double p = 0.5, winProb = 0.407;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "Y", "I"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private CoinBox coinBox = new CoinBox(n + m - 1, p, 26);
	private Parameter nScroll = new Parameter(1, 30, 1, n, "Player 1's points", "n");
	private Parameter mScroll = new Parameter(1, 30, 1, m, "Player 0's points", "m");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Probability that player 1 wins a point", "p");
	private BernoulliDistribution winDist = new BernoulliDistribution(winProb);
	private RandomVariable winRV = new RandomVariable(winDist, "I");
	private RandomVariableGraph winGraph = new RandomVariableGraph(winRV);
	private RandomVariableTable winTable = new RandomVariableTable(winRV);
	private Timer timer = new Timer(100, this);

	/**
	* This method initializes the experiment, including the toolbar with
	* scrollbars and labels, the random variable table and graph, the record
	* table and the coin panel.
	*/
	public void init(){
		super.init();
		setName("Problem of Points Experiment");
		//Sliders
		nScroll.getSlider().addChangeListener(this);
		mScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(nScroll);
		toolBar.add(mScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Coin box
		coinBox.setMinimumSize(new Dimension(100, 100));
		addComponent(coinBox, 0, 0, 1, 1);
		//Random variable graph
		winGraph.setMinimumSize(new Dimension(100, 100));
	 	winGraph.setMomentType(0);
		addComponent(winGraph, 1, 0, 1, 1);
		//Record table
		recordTable.setDescription("X: number of heads, Y: number of tails, I: winning player");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random variable table
		winTable.setStatisticsType(0);
		addComponent(winTable, 1, 1, 1, 1);
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
			+ "The experiment consists of tossing coins, each with probability of heads p, until either\n"
			+ "n heads occur or m tails occur. The event that n heads occur before m tails is indicated\n"
			+ "by the random variable I. On each update, the results of the coin tosses are shown in the\n"
			+ "first graph. On each update, the number of heads, the number of tails, and the value of I\n"
			+ "are recorded in the first table. The density of I is shown in blue in the second graph\n"
			+ "and is recorded in the second table. On each update, the empirical density of I is shown\n"
			+ "in red in the second graph and is recorded in the second table. The parameters n, m, and p\n"
			+ "can be varied with scroll bars.";
	}

	/**
	* This method handles the scrollbar events for adjusting the number of
	* points needed by the two players, and the probability of winning a point.
	* @param e change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == mScroll.getSlider()){
			m = (int)mScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			setParameters();
		}
	}

	/**
	* This method sets the parameter of the win distribution when the number
	* of points needed for a player or the point probability have changed.
	*/
	public void setParameters(){
		coinBox.setProbability(p);
		coinBox.setCoinCount(n + m - 1);
		winProb = 0;
		for (int i = n; i < n + m; i++)
			winProb = winProb + Functions.comb(i - 1, n - 1) * Math.pow(p, n) * Math.pow(1 - p, i - n);
		winDist.setProbability(winProb);
		reset();
	}

	/**
	* This method defines the experiment. Bernoulli trials are conducted until
	* one player has won her required number of points.
	*/
	public void doExperiment(){
		super.doExperiment();
		trials = 0; heads = 0;
		Coin coin;
		while ((heads < n) & (trials - heads < m)){
			coin = coinBox.getCoin(trials);
			coin.toss();
			if (coin.getValue() == 1) heads++;
			trials++;
		}
		if (heads >= n) winRV.setValue(1);
		else winRV.setValue(0);
	}

	/**
	* This method runs the experiment one time with additional sound and annimation.
	*/
	public void step(){
		stop();
		trials = 0;
		heads = 0;
		coinBox.setTossed(false);
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
	* This method updates the display, including the coins, the record table,
	* and the random variable graph and table.
	*/
	public void update(){
		super.update();
		for (int i = 0; i < n + m - 1; i++) coinBox.getCoin(i).setTossed(i < trials);
		recordTable.addRecord(new double[]{getTime(), heads, trials - heads, winRV.getValue()});
		winGraph.repaint();
		winTable.repaint();
	}

	/**
	* This method resets the experiment, including the coins, the record table,
	* and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		coinBox.setTossed(false);;
		winRV.reset();
		winGraph.reset();
		winTable.reset();
	}

	/**
	* This method handles events associated with the step process. The coins are shown one at a
	* time until there are either n heads or m tails.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			Coin coin;
			if (heads < n & trials - heads < m){
				coin = coinBox.getCoin(trials);
				coin.toss();
				coin.setTossed(true);
				//playnote(coin.getValue());
				if (coin.getValue() == 1) heads++;
				trials++;
			}
			else{
				timer.stop();
				super.doExperiment();
				if (heads >= n) winRV.setValue(1);
				else winRV.setValue(0);
				recordTable.addRecord(new double[]{getTime(), heads, trials - heads, winRV.getValue()});
				winGraph.repaint();
				winTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

