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
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.Timeline;

/**
* This class implements the binomial experiment. The experiment consists of performing n Bernoulli trials,
* each with probability of success p. The successes are recorded as red dots on a timeline marked from 1 to n.
* The number of successes X and the proportion of successes M are recorded on each update. Either X or M can
* be selected with the list box. The discrete probability density function and moments of the selected
* variable are shown in blue in the distribution graph and are recorded in the distribution table. On each
* update, the empirical probability density function and moments of the selected variable are shown in red
* in the distribution graph and are recorded in the distribution table. The parameters n and p can be
* varied with scroll bars.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BinomialTimelineExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 10, x;
	private double p = 0.5, m, currentTime;
	//Ojbects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "M"});
	private JToolBar toolBar = new JToolBar("Parameter toolbar");
	private JComboBox rvChoice = new JComboBox();
	private Parameter nScroll = new Parameter(1, 100, 1, n, "Number of coins", "n");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Probability of heads", "p");
	private BinomialDistribution sumDist = new BinomialDistribution(n, p);
	private LocationScaleDistribution averageDist = new LocationScaleDistribution(sumDist, 0, 1.0 / n);
	private RandomVariable sumRV = new RandomVariable(sumDist, "X");
	private RandomVariable averageRV = new RandomVariable(averageDist, "M");
	private RandomVariableGraph rvGraph = new RandomVariableGraph(sumRV);
	private RandomVariableTable rvTable = new RandomVariableTable(sumRV);
	private Timeline timeline = new Timeline(new Domain(1, n, 1, Domain.DISCRETE), "t");
	private Timer timer = new Timer(200, this);

	/**
	* This method initializes the experiment, including the timeline, toolbar, record table, random
	* variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Binomial Timeline Experiment");
		//Sliders
		nScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//rv choice
		rvChoice.addItemListener(this);
		rvChoice.setToolTipText("X: number of successes; M: proportion of successes");
		rvChoice.addItem("X");
		rvChoice.addItem("M");
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rvChoice);
		toolBar.add(nScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Timeline
		timeline.setMinimumSize(new Dimension(100, 60));
		addComponent(timeline, 0, 0, 2, 1, 10, 2);
		//Random Variable graph
		rvGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(rvGraph, 0, 1, 2, 1);
		//Record Table
		recordTable.setDescription("X: number of successes; M: proportion of successes");
		addComponent(recordTable, 0, 2, 1, 1);
		//Random variable table
		addComponent(rvTable, 1, 2, 1, 1);
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
			+ "The experiment consists of performing n Bernoulli trials, each with probability of success p.\n"
			+ "The successes are recorded as red dots on a timeline marked from 1 to n. The number of \n"
			+ "successes X and the proportion of successes M are recorded on each update. Either X or M \n"
			+ "can be selected with the list box. The discrete probability density function and moments \n"
			+ "of the selected variable are shown in blue in the distribution graph and are recorded in \n"
			+ "the distribution table. On each update, the empirical probability density function and \n"
			+ "moments of the selected variable are shown in red in the distribution graph and are recorded\n"
			+ "in the distribution table. The parameters n and p can be varied with scroll bars.";
	}

	/**
	* This method performs the experiment by simulating n Bernoulli trials, and computing the number
	* and proportion of successes.
	*/
	public void doExperiment(){
		super.doExperiment();
		performTrials();
		sumRV.setValue(x);
		averageRV.setValue(m);
	}

	public void performTrials(){
		timeline.resetData();
		x = 0;
		for (int i = 0; i < n; i++){
			if (Math.random() < p){
				x++;
				timeline.addTime(i + 1);
			}
		}
		m = (double)x / n;
	}

	/**
	* This method runs the experiment one time, with additional annimation and sound.
	*/
	public void step(){
		stop();
		performTrials();
		currentTime = 0;
		timeline.setCurrentTime(currentTime);
		timer.start();
	}

	/**
	* This method stops the step timer, if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the step timer, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the timeline, record table, random variable,
	* random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		timeline.reset();
		sumRV.reset();
		averageRV.reset();
		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method updates the experiment, including the timeline, record table, and the random
	* variable graph and table.
	*/
	public void update(){
		super.update();
		timeline.setCurrentTime(n);
		recordTable.addRecord(new double[]{getTime(), x, m});
		rvGraph.repaint();
		rvTable.repaint();
	}
	public void graphUpdate(){
		super.update();
	//	timeline.setCurrentTime(n);
	//	recordTable.addRecord(new double[]{getTime(), x, m});
		rvGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.repaint();
		rvTable.setShowModelDistribution(showModelDistribution);
		rvTable.repaint();
	}
	/**
	* This method handles the events associated with the sliders. These control the number
	* of trials n and the probability of success p.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			timeline.setDomain(new Domain(1, n, 1, Domain.DISCRETE));
			timer.setDelay(1000 / n);
			setDistribution();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			setDistribution();
		}
	}

	/**
	* This method handles the events associated with the combo box that is used to switch between
	* the random variables: the number of successes and the proportion of successes.
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
	* This method handles the events associated with the timer. The random points up to the
	* current time are drawn.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (currentTime < n){
				currentTime++;
				timeline.setCurrentTime(currentTime);
			}
			else{
				timer.stop();
				//playnote(x);
				super.doExperiment();
				sumRV.setValue(x);
				averageRV.setValue(m);
				update();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the distributions of the random variables when the parameters change.
	*/
	public void setDistribution(){
		sumDist.setParameters(n, p);
		averageDist.setParameters(sumDist, 0, 1.0 / n);
		reset();
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
	  
}

