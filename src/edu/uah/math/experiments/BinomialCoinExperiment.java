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
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.Coin;
import edu.uah.math.devices.CoinBox;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;

/**
* This experiment consists of tossing n coins, each with probability of heads p.
* The number of heads X and the proportion of heads M are recorded on each update.
* Either X or M can be selected with the list box. The discrete probability density
* function and moments of the selected variable are shown in blue in the distribution
* graph and are recorded in the distribution table. On each update, the empirical
* probability density function and moments of the selected variable are shown in red
* in the distribution graph and are recorded in the distribution table. The parameters
* n and p can be varied with scroll bars.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BinomialCoinExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 10, rvIndex = 0, x, trial;
	private int maxNumberOfCoins = 500;
	
	private double p = 0.5, m;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "M"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox rvChoice = new JComboBox();
	private CoinBox coinBox = new CoinBox(n, p, 16);
	private Parameter nScroll = new Parameter(1, maxNumberOfCoins, 1, n, "Number of coins", "n");
	private Parameter pScroll = new Parameter(0, 1, 0.01, p, "Probability of heads", "p");
	private BinomialDistribution sumDist = new BinomialDistribution(n, p);
	private LocationScaleDistribution averageDist = new LocationScaleDistribution(sumDist, 0, 1.0 / n);
	private RandomVariable sumRV = new RandomVariable(sumDist, "X");
	private RandomVariable averageRV = new RandomVariable(averageDist, "M");
	private RandomVariableGraph rvGraph = new RandomVariableGraph(sumRV);
	private RandomVariableTable rvTable = new RandomVariableTable(sumRV);
	private Timer timer = new Timer(100, this);


	/**
	* This method initializes the experiment, including the coin box,
	* the toolbar with the random variable choice, and the random variable table and
	* graph.
	*/
	public void init(){
		super.init();
		setName("Binomial Coin Experiment");
		//Parameters
		nScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		 //JComboBox
		rvChoice.addItemListener(this);
 		rvChoice.addItem("X");
		rvChoice.addItem("M");
		rvChoice.setToolTipText("X: Number of heads; M: Proportion of heads");
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rvChoice);
		toolBar.add(nScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Coin Box
		coinBox.setMinimumSize(new Dimension(100, 100));
		addComponent(coinBox, 0, 0, 1, 1);
		//Random variable graph
		rvGraph.setMinimumSize(new Dimension(200, 100));
		addComponent(rvGraph, 1, 0, 2, 1, 20, 10);
		//Record table
		recordTable.setDescription("X: number of heads, M: proportion of heads");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random variable table
		addComponent(rvTable, 1, 1, 1, 1);
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
			+ "The experiment consists of tossing n coins, each with probability of heads p. \n"
			+ "The number of heads X and the proportion of heads M are recorded on each update. \n"
			+ "Either X or M can be selected with the list box. The discrete probability density \n"
			+ "function and moments of the selected variable are shown in blue in the distribution \n"
			+ "graph and are recorded in the distribution table. On each update, the empirical \n"
			+ "probability density function and moments of the selected variable are shown in red \n"
			+ "in the distribution graph and are recorded in the distribution table. The parameters \n"
			+ "n and p can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment. The coins are tossed and the number of heads
	* and proportion of heads are computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		coinBox.toss();
		x = coinBox.getHeadCount();
		m = (double)x / n;
		sumRV.setValue(x);
		averageRV.setValue(m);
	}

	/**
	* This method starts the step process.
	*/
	public void step(){
		stop();
		coinBox.setTossed(false);
		coinBox.toss();
		trial = 0;
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
	* This method resets the experiment, including the record table, coin box,
	* the random variables, and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		coinBox.setTossed(false);
		sumRV.reset();
		averageRV.reset();
		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method updates the experiment, including the coin box, the record table
	* and the random variable and graph.
	*/
	public void update(){
		super.update();
		coinBox.setTossed(true);
		rvGraph.repaint();
		rvTable.repaint();
		recordTable.addRecord(new double[] {getTime(), x, m});
	}
	public void graphUpdate(){
		super.update();
		//coinBox.setTossed(true);
		rvGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.repaint();
		rvTable.setShowModelDistribution(showModelDistribution);
		rvTable.repaint();
		//recordTable.addRecord(new double[] {getTime(), x, m});
	}
	/**
	* This method handles the events associated with changes in the slider. These sliders
	* are used to adjust the parameters of the experiment: the number of coins and
	* the probability of heads.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			coinBox.setCoinCount(n);
			setDistribution();
			reset();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			setDistribution();
			reset();
		}
	}

	/**
	* This method handles events associated with the choice of the random variable,
	* the number of heads or the proportion of heads can be chosen. The density
	* and moments and the empirical density and moments are shown in the random
	* variable graph and in the random variable table.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if(e.getSource() == rvChoice){
			if (rvChoice.getSelectedIndex() == 0){
				rvGraph.setRandomVariable(sumRV);
				rvTable.setRandomVariable(sumRV);
			}
			else{
				rvGraph.setRandomVariable(averageRV);
				rvTable.setRandomVariable(averageRV);
			}
			rvGraph.repaint();
			rvTable.repaint();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the timer events associated with the step process. The coins are shown
	* one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			Coin coin;
			if (trial < n){
				coin = coinBox.getCoin(trial);
				coin.setTossed(true);
				//playnote(coin.getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				x = coinBox.getHeadCount();
				m = (double)x / n;
				sumRV.setValue(x);
				averageRV.setValue(m);
				rvGraph.repaint();
				rvTable.repaint();
				recordTable.addRecord(new double[] {getTime(), x, m});
			}
		}
		else super.actionPerformed(e);
	}


	/**
	* This method sets the appropriate distribution of the random variable. This
	* method is called when the parameters are changed or when the type of random
	* variable is changed. The {@link #reset() reset} method is called.
	*/
	public void setDistribution(){
		coinBox.setProbability(p);
		sumDist.setParameters(n, p);
		averageDist.setParameters(sumDist, 0, 1.0 / n);
		reset();
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

