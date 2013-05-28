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
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.GaltonBoard;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the classical Galton board experiment, illustrating the
* binomial distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class GaltonBoardExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 10, x, rvIndex = 0;;
	private double p = 0.5, m;
	private int[] outcome = new int[n];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "M"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private GaltonBoard galtonBoard = new GaltonBoard(n, p);
	private Parameter nScroll = new Parameter(1, 50, 1, n, "Number of rows", "n");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Right probability", "p");
	private JComboBox rvChoice = new JComboBox();
	private BinomialDistribution sumDist = new BinomialDistribution(n, p);
	private LocationScaleDistribution averageDist = new LocationScaleDistribution(sumDist, 0, 1.0 / n);
	private RandomVariable sumRV = new RandomVariable(sumDist, "X");
	private RandomVariable averageRV = new RandomVariable(averageDist, "M");
	private RandomVariableGraph rvGraph = new RandomVariableGraph(sumRV);
	private RandomVariableTable rvTable = new RandomVariableTable(averageRV);
	private Timer timer = new Timer(150, this);

	/**
	* This method initializes the experiment, including the Galton board,
	* random variable table and graph, and the toolbar.
	*/
	public void init(){
		super.init();
		setName("Galton Board Experiment");
		//Event listeners
		nScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		rvChoice.addItemListener(this);
		//JComboBox
		rvChoice.setToolTipText("X: number of moves right; M: proportion of moves right");
		rvChoice.addItem("X");
		rvChoice.addItem("M");
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rvChoice);
		toolBar.add(nScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Galton Board
		galtonBoard.setMinimumSize(new Dimension(200, 200));
		galtonBoard.setMargins(rvGraph.getLeftMargin(), 10, 10, 10);
		addComponent(galtonBoard, 0, 0, 1, 1);
		//Random Variable Graph
		rvGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(rvGraph, 0, 1, 1, 1);
		//Record Table
		recordTable.setDescription("X: number of moves right; M: proportion of moves right");
		addComponent(recordTable, 1, 0, 1, 1);
		//Random Variable Table
		addComponent(rvTable, 1, 1, 1, 1);
		//Final Actions
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
			+ "The Galton board experiment consists of performing n Bernoulli trials with probability\n"
			+ "of success p. The trial outcome are represented graphically as a path in the Galton board:\n"
			+ "success corresponds to a bounce to the right and failure to a bounce to the left. The number\n"
			+ "of successes X and the proportion of successes M are recorded on each update. Graphically, X\n"
			+ "is the number of the peg in the last row of the Galton board that the ball hits.\n"
			+ "The distribution and moments of the selected variable (X or M) are given in the distribution\n"
			+ "graph and the distribution table. The parameters n and p can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment: n Bernoulli trials are performed and
	* the the values of the random variable are computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		galtonBoard.setPath();
		x = galtonBoard.getBallColumn();
		m = (double)x / n;
		sumRV.setValue(x);
		averageRV.setValue(m);
 	}

 	/**
 	* This method runs the experiment one time, by dropping the ball through
 	* the Galton board.  Run mode is stopped, if necessary.
 	*/
 	public void step(){
		stop();
		//galtonBoard.reset();
		galtonBoard.setPath();
		update();
		timer.start();
	}

	/**
	* This method starts run mode, by first turning off the step timer if necessary.
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
	* This method resets the experiment, including the Galton board, the record
	* table, the random variable graph and table. The step timer is stopped if necessary.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		sumRV.reset();
		averageRV.reset();
		galtonBoard.reset();
		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method updates the display, including the Galton board, the record
	* table, the random variable graph and table.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), x, m});
		galtonBoard.setPathDrawn(true);
		galtonBoard.repaint();
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
	* This method handles the scroll events that adjust the number of trials and
	* the probability of success.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			setDistribution();
			outcome = new int[n];
			galtonBoard.setRows(n);
			reset();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			galtonBoard.setProbability(p);
			setDistribution();
			reset();
		}
	}

	/**
	* This method handles the choice event, for selecting the random variable
	*to display.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if(e.getSource() == rvChoice){
			int rvIndex = rvChoice.getSelectedIndex();
			if (rvIndex == 0){
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

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (galtonBoard.getBallRow() < galtonBoard.getRows()){
				//playnote(galtonBoard.moveBall());
				//rvGraph.repaint();
			}
			else{
				super.doExperiment();
				x = galtonBoard.getBallColumn();
				m = (double)x / n;
				sumRV.setValue(x);
				averageRV.setValue(m);
				recordTable.addRecord(new double[]{getTime(), x, m});
				rvGraph.repaint();
				rvTable.repaint();
				timer.stop();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the parameters of the distributions, and is called when
	* one of these parameters is changed with the scrollbar.
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

