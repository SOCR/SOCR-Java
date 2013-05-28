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
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Coin;
import edu.uah.math.devices.CoinBox;
import edu.uah.math.devices.Parameter;

/**
* This class models a basic experiment that tosses n coins, generating a random sample from the
* Bernoulli distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CoinSampleExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 10, trial;
	private double p = 0.5;
	double[] record;
	//Objects
	private RecordTable recordTable = new RecordTable();
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private CoinBox coinBox = new CoinBox(n, p, 26);
	private Parameter nScroll = new Parameter(1, 100, 1, 10, "Number of coins", "n");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Probability of heads", "p");
	private Timer timer = new Timer(100, this);
	private ActionListener listener;

	/**
	* This method initializes the experiment, including the record table, sliders for the
	* number of coins and the probabiltiy of heads, the toolbar, and the coin box.
	*/
	public void init(){
		super.init();
		setName("Coin Sample Experiment");
		//Sliders
		nScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(nScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Coin Box
		coinBox.setMinimumSize(new Dimension(100, 100));
		addComponent(coinBox, 0, 0, 1, 1);
		//RecordTable
		recordTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		addComponent(recordTable, 0, 1, 1, 1);
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
			+ "The experiment consists of tossing n coins, each with probability of heads p.\n"
			+ "Variable Ij gives the outcome (1 for heads, 0 for tails) for coin j.\n"
			+ "The results are recorded on each update. The parameters n and p can be \n"
			+ "varied with scroll bars.";
	}

	public JPanel getAnimation(){
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(toolBars);
		p.add(coinBox);
		return p;
	}
	/**
	* This method defines the experiment by tossing the coins.
	*/
	public void doExperiment(){
		super.doExperiment();
		coinBox.toss();
	}

	public void addListener(ActionListener l){
		listener =l;
	}
	/**
	* This method runs the experiment one time, with additional annimation and sound.
	*/
	public void step(){
		stop();
		coinBox.setTossed(false);
		record = new double[n + 1];
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

	/**s
	* This method stops the step process, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the record table and the coin box.
	*/
	public void reset(){
		super.reset();
		String[] variables = new String[n + 1];
		variables[0] = "Run";
		for (int i = 0; i < n; i++) variables[i + 1] = "I" + (i + 1);
		recordTable.setVariableNames(variables);
		coinBox.setTossed(false);
	}

	/**
	* This method updates the display, including the coin box and record table.
	*/
	public void update(){
		super.update();
		coinBox.setTossed(true);
		record = new double[n + 1];
		record[0] = getTime();
		for (int i = 0; i < n; i++)	record[i + 1] = coinBox.getValues(i);
		recordTable.addRecord(record);
	}

	/**
	* This method handles the slider events associated with changes in the number of
	* coins and the probability of heads.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			coinBox.setCoinCount(n);
			reset();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			coinBox.setProbability(p);
			reset();
		}
	}

	/**
	* This method handles the timer events associated with the step process. The coins are
	* shown one at at ime.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < n){
				Coin coin = coinBox.getCoin(trial);
				coin.toss();
				coin.setTossed(true);
				record[trial + 1] = coin.getValue();
				//playnote(coin.getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				record[0] = getTime();
				recordTable.addRecord(record);
			}
		}
		else super.actionPerformed(e);
		
		if (listener!=null)
			listener.actionPerformed(e);
	}
	
	public JTable getResultTable(){
		// System.out.println("coin getResultTable row count"+recordTable.getTable().getRowCount());
	    	return  recordTable.getTable();
	    }
}

