/*
Copyright (C) 2001-2004 Dawn Duehring, Kyle Siegrist,

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
import java.awt.Point;
import java.awt.Frame;
import java.awt.Dimension;
import java.io.Serializable;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Spinner;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.ProbabilityDialog;

/**
* This experiment spins a spinner (the number of divisions is specified by the
* user) and records the value of each spin.  The value is a number from 1 to n,
* where n is the number of divisions.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class SpinnerExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 4, value;
	private double angle, spinAngle, spinAngleIncrement = 5;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "N"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter nScroll = new Parameter(1, 200, 1, n, "Number of divisions", "n");
	private JButton probButton = new JButton();
	private Spinner spinner = new Spinner();
	private RandomVariable rv = new RandomVariable(new FiniteDistribution(1, n, 1, spinner.getProbabilities()), "N");
	private RandomVariableGraph rvGraph = new RandomVariableGraph(rv);
	private RandomVariableTable rvTable = new RandomVariableTable(rv);
	private Timer timer = new Timer(5, this);
	private ProbabilityDialog probDialog;

	/**
	* This method  initializes the experiment, including the record table, the slider
	* for the number of divisions, the button for the spinner probability dialog box,
	* the random variable graph and table, and the toolbar.
	*/
	public void init(){
		super.init();
		setName("Spinner Experiment");
		//Parameter
		nScroll.getSlider().addChangeListener(this);
		//Probability button
		probButton.addActionListener(this);
		probButton.setToolTipText("Spinner Probabilities");
		probButton.setIcon(new ImageIcon(getClass().getResource("spinner.gif")));
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(probButton);
		toolBar.add(nScroll);
		addToolBar(toolBar);
		//Spinner
		spinner.setMinimumSize(new Dimension(200, 200));
		addComponent(spinner, 0, 0, 1, 1);
		//Random Variable Graph
		rvGraph.setMinimumSize(new Dimension(300, 200));
		addComponent(rvGraph, 1, 0, 1, 1);
		//Record Table
		recordTable.setDescription("X: angle; N: spinner score");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random Variable Table
		addComponent(rvTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including
	* copyright information, descriptive information, and instructions.
	* @return applet information.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment consists spinning a spinner and recording the outcome (the sectors are numbered).\n"
			+ "The number of sectors in the spinner can be varied with the scrollbar and the probability\n"
			+ "of each sector (which is proportional to the area of the sector) can be set with the probability\n"
			+ "dialog. The probability density function and moments of the random variable (the spinner score)\n"
			+ "are shown in blue in the distribution graph and recorded numerically in the distribution table.\n"
			+ "When the experiment runs, the empirical density and moments are shown in red in the distribution\n"
			+ "graph and recorded in the distribution table. In single step mode, the spinner pointer actually\n"
			+ "spins, and a sound is played whose tone depends on the outcome.";
	}

	/**
	* This method handles the slider events for the number of divisions.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()) {
			n = (int)nScroll.getValue();
			spinner.setDivisions(n);
			rv = new RandomVariable(new FiniteDistribution(1, n, 1, spinner.getProbabilities()), "N");
			rvGraph.setRandomVariable(rv);
			rvTable.setRandomVariable(rv);
			reset();
		}
	}

	/**
	* This method handles the events for the probability button. The spinner
	* probabilities and labels are passed to the probability dialog box which is then displayed.
	* If the user clicks OK, the new probabilities are passed to the random variable.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		//Probability dialog
		if (e.getSource() == probButton){
			// Ivo a workaround the problem that the applets do not
			// know their parent's Frames 01/26/05
			Frame frame = new Frame(); // getFrame(this);
			Point fp = new Point(20,20); // frame.getLocationOnScreen();
			Point dp;
			probDialog = new ProbabilityDialog(frame, "Spinner Probabilities", spinner.getProbabilities());
			Dimension fs = frame.getSize(), ds = probDialog.getSize();
			dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			probDialog.setLocation(dp);
			probDialog.setVisible(true);
			if (probDialog.isOK()){
				spinner.setProbabilities(probDialog.getProbabilities());
				rv = new RandomVariable(new FiniteDistribution(1, n, 1, spinner.getProbabilities()), "N");
				rvGraph.setRandomVariable(rv);
				rvTable.setRandomVariable(rv);
				reset();
			}
		}
		//Step timer events
		else if (e.getSource() == timer){
			if (spinAngle > 720 & Math.abs((spinAngle % 360) - angle) < spinAngleIncrement){
				timer.stop();
				super.doExperiment();
				spinner.setAngle(angle);
				rv.setValue(value);
				playnote(value);
				update();
			}
			else{
				spinAngle = spinAngle + spinAngleIncrement;
				spinner.setAngle(spinAngle);
				spinner.repaint();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method defines the experiment. The spinner is spun and the value passed
	* to the random variable
	*/
	public void doExperiment(){
		super.doExperiment();
		spinner.spin();
		angle = spinner.getAngle();
		value = spinner.getValue();
		rv.setValue(value);
	}

	/**
	* This method updates the display, including the spinner, the record table,
	* and the random variable graph and table.
	*/
	public void update(){
		super.update();
		spinner.repaint();
		recordTable.addRecord(new double[]{getTime(), angle, value});
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
	* This method runs the experiment once, and shows the spinner spinning. A sound is
	* played, depending on the outcome.
	*/
	public void step(){
		stop();
		spinner.spin();
		angle = spinner.getAngle();
		value = spinner.getValue();
		spinAngle = 0;
		spinner.setAngle(spinAngle);
		spinner.repaint();
		timer.start();

	}

	/**
	* This method puts the simulation in run mode, by first stoping the step timer, and then doing
	* the usual run procedure.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the experiment, by first stopping the step timer and then calling the usual
	* stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the record table, the random variable,
	* the random variable graph and table, and the spinner.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		rv.reset();
		rvGraph.repaint();
		rvTable.reset();
		spinner.setAngle(0);
		spinner.repaint();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}