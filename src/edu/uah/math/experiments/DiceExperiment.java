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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;
import edu.uah.math.distributions.DieDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.OrderStatisticDistribution;
import edu.uah.math.distributions.ConvolutionDistribution;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.DiceBoard;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.DieProbabilityDialog;

/**
* This class models a basic experiment that consists of rolling n dice. The random
* variables of interest are the sum of scores, the average score, the maximum score,
* the minimum score, and the number of ones. The number of dice and the die distribution
* can be varied.
* @author Kyle Siegrist
* @author Dawn Duehring
* @author Ivo Dinov
* @version March, 2008
*/
public class DiceExperiment extends Experiment implements Serializable{
	//Variables
	private int rvIndex = 0, n = 1, dice = 30, x, sum, min, max, trial;
	private int ones, twos, threes, fours, fives, sixes;
	private double average;
	//GUI Objects
	private JButton dieButton = new JButton();
	private JComboBox rvChoice = new JComboBox();
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private DiceBoard diceBoard = new DiceBoard(n);
	private RecordTable recordTable = new RecordTable(new String[] {"Run", "Y", "M", "U", "V", 
			"ONES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES"});
	//Distributions
	private DieDistribution dieDist = new DieDistribution(DieDistribution.FAIR);
	private ConvolutionDistribution sumDist = new ConvolutionDistribution(dieDist, n);
	private LocationScaleDistribution averageDist = new LocationScaleDistribution(sumDist, 0, 1.0);
	private OrderStatisticDistribution minDist = new OrderStatisticDistribution(dieDist, n, 1);
	private OrderStatisticDistribution maxDist = new OrderStatisticDistribution(dieDist, n, n);
	private BinomialDistribution onesDist = new BinomialDistribution(n, dieDist.getDensity(1));
	private BinomialDistribution twosDist = new BinomialDistribution(n, dieDist.getDensity(2));
	private BinomialDistribution threesDist = new BinomialDistribution(n, dieDist.getDensity(3));
	private BinomialDistribution foursDist = new BinomialDistribution(n, dieDist.getDensity(4));
	private BinomialDistribution fivesDist = new BinomialDistribution(n, dieDist.getDensity(5));
	private BinomialDistribution sixesDist = new BinomialDistribution(n, dieDist.getDensity(6));

	private RandomVariable[] rv = new RandomVariable[10];
	private RandomVariableGraph rvGraph;
	private RandomVariableTable rvTable;
	private Parameter nScroll = new Parameter(1, dice, 1, n, "Number of dice", "n");
	private Frame frame;
	private DieProbabilityDialog dieProbabilityDialog;
	private Timer timer = new Timer(100, this);

	/**
	* This method initializes the experiment, including the random variables, the record
	* table, the buttons for the die probability dialog, the random variable choice, the
	* slider for the number of dice, the toolbar, the dice board, and the random variable
	* graph and table.
	*/
	public void init(){
		super.init();
		//Random variables
		rv[0] = new RandomVariable(sumDist, "Y");
		rv[1] = new RandomVariable(averageDist, "M");
		rv[2] = new RandomVariable(minDist, "U");
		rv[3] = new RandomVariable(maxDist, "V");
		rv[4] = new RandomVariable(onesDist, "ONES");
		rv[5] = new RandomVariable(twosDist, "TWOS");
		rv[6] = new RandomVariable(threesDist, "THREES");
		rv[7] = new RandomVariable(foursDist, "FOURS");
		rv[8] = new RandomVariable(fivesDist, "FIVES");
		rv[9] = new RandomVariable(sixesDist, "SIXES");

		//Name
		setName("Dice Experiment");
		//Die button
		dieButton.addActionListener(this);
		dieButton.setToolTipText("Die Probabilities");
		dieButton.setIcon(new ImageIcon(getClass().getResource("redDie.gif")));
		rvGraph = new RandomVariableGraph(rv[rvIndex]);
		rvTable = new RandomVariableTable(rv[rvIndex]);
		//Random variable choice
		rvChoice.addItemListener(this);
		rvChoice.setToolTipText("Y: Sum of scores; M: Average score; U: Min score; "+
				"V:Max score; ONES: Number of 1's; TWOS: Number of 2's, ..., SIXES: Number of 6's");
		rvChoice.addItem("Y");
		rvChoice.addItem("M");
		rvChoice.addItem("U");
		rvChoice.addItem("V");
		rvChoice.addItem("ONES");
		rvChoice.addItem("TWOS");
		rvChoice.addItem("THREES");
		rvChoice.addItem("FOURS");
		rvChoice.addItem("FIVES");
		rvChoice.addItem("SIXES");
		
		//Slider
		nScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rvChoice);
		toolBar.add(dieButton);
		toolBar.add(nScroll);
		addToolBar(toolBar);
		//Dice board
		diceBoard.setMinimumSize(new Dimension(100, 100));
		addComponent(diceBoard, 0, 0, 1, 1);
		//Random variable graph
		rvGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(rvGraph, 1, 0, 1, 1);
		//Record table
		recordTable.setDescription("Y: Sum of scores; M: Average score; U: Min score; "+
				"V:Max score; ONES: Number of 1's; TWOS: Number of 2's, ..., SIXES: Number of 6's");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random variable table
		addComponent(rvTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright information,
	* descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment consists of rolling n dice, each governed by the same probability distribution.\n"
			+ "You can specify the die distribution by clicking on the die probability button;\n"
			+ "this button bring up the die probability dialog box. You can define your own distribution\n"
			+ "by typing probabilities into the text fields of the dialog box, or you can click on one \n"
			+ "of the buttons in the dialog box to specify one of the following special distributions:\n"
			+ "fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, or skewed right. The following random variables\n"
			+ "are recorded on each update: the sum of the scores Y, the average score M, the minimum score U\n"
			+ "the maximum score V, the number of 1's, 2's, ..., and 6's. Any one of these variables can be selected\n"
			+ "with a list box. The density and moments of the selected variable are shown in blue in the \n"
			+ "distribution graph and are recorded in the distribution table. When the simulation runs, \n"
			+ "the empirical density and moments are shown in red in the distribution graph and are recorded\n"
			+ "in the distribution table. The parameter n can be varied with a scroll bar.";
	}

	/**
	* This method defines the experiment. The dice are rolled and the values
	* of the random variables computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		rollDice();
		//Set values for random varaibles
		rv[0].setValue(sum);
		rv[1].setValue(average);
		rv[2].setValue(min);
		rv[3].setValue(max);
		rv[4].setValue(ones);
		rv[5].setValue(twos);
		rv[6].setValue(threes);
		rv[7].setValue(fours);
		rv[8].setValue(fives);
		rv[9].setValue(sixes);
	}

	/**
	* This method rolls the dice and computes the values of the random variables.
	*/
	private void rollDice(){
		sum = 0; min = 6; max = 1; 
		ones = 0; twos=0; threes=0; fours=0; fives=0; sixes=0;
		
		for (int i = 0; i < n; i++){
			x = (int)dieDist.simulate();
			diceBoard.setValues(i, x);
			sum = sum + x;
			if (x < min) min = x;
			if (x > max) max = x;
			
			if (x == 1) ones++;
			else if (x == 2) twos++;
			else if (x == 3) threes++;
			else if (x == 4) fours++;
			else if (x == 5) fives++;
			else if (x == 6) sixes++;
		}
		average = (double)sum / n;
	}

	/**
	* This method runs the experiment one time, adding additional sound and annimation.
	*/
	public void step(){
		stop();
		diceBoard.setRolled(false);
		rollDice();
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
	* This method resets the experiment, including the record table, the dice,
	* the random variables, and the graph and table for the selected variable.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		for (int i = 0; i < 10; i++) rv[i].reset();
		rvGraph.reset();
		rvTable.reset();
		diceBoard.setRolled(false);
	}

	/**
	* This method updates the display, including the record table, the dice,
	* and the graph and table for the selected variable.
	*/
	public void update(){
		super.update();
		diceBoard.setRolled(true);
		recordTable.addRecord(new double[] {
				getTime(), sum, average, min, max, ones, twos, threes, fours, fives, sixes});
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
	* This method handles the events associated with the die probabilities
	* button and with the step timer. If the die probability button is pressed, the current die
	* probabilities are loaded into the dialog box which is then displayed. For the step timer,
	* the dice are shown one at a time.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == dieButton){
			//System.out.println("Inside dieButton actionPerformed\n");

			// Ivo a workaround the problem that the applets do not know their
			// parent's Frames 01/26/05
			frame = new Frame();
			//frame = getFrame(this);

			//System.out.println("frame="+frame+"\t getFrame(this);\n");
			dieProbabilityDialog = new DieProbabilityDialog(frame);
			//System.out.println("dieProbabilityDialog \n");
			//Point fp = frame.getLocationOnScreen();
			Point fp = new Point(100, 100);
			//System.out.println("fp = frame.getLocationOnScreen() \n");
			Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
			//System.out.println("ds = dieProbabilityDialog.getSize(); \n");
			Point dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			//System.out.println("dp = new Point \n");
			dieProbabilityDialog.setLocation(dp);
			//System.out.println("dieProbabilityDialog.setLocation(dp); \n");
			dieProbabilityDialog.setProbabilities(dieDist.getProbabilities());

			// System.out.println("dieProbabilityDialog.setProbabilities(dieDist.getProbabilities()); \n");
			dieProbabilityDialog.setVisible(true);
			if (dieProbabilityDialog.isOK()){
			  dieDist.setProbabilities(dieProbabilityDialog.getProbabilities());
			  setDistributions();
			}
		}
		else if (e.getSource() == timer){
			if (trial < n){
				diceBoard.getDie(trial).setRolled(true);
				//playNote(diceBoard.getDie(trial).getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				rv[0].setValue(sum);
				rv[1].setValue(average);
				rv[2].setValue(min);
				rv[3].setValue(max);
				rv[4].setValue(ones);
				rv[5].setValue(twos);
				rv[6].setValue(threes);
				rv[7].setValue(fours);
				rv[8].setValue(fives);
				rv[9].setValue(sixes);
				
				recordTable.addRecord(new double[] {
						getTime(), sum, average, min, max, ones, twos, threes, fours, fives, sixes});
				rvGraph.repaint();
				rvTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method handles the choice events, including the choice of the
	* die distribution and the choice of the selected variable.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == rvChoice){
			rvIndex = rvChoice.getSelectedIndex();
			rvGraph.setRandomVariable(rv[rvIndex]);
			rvTable.setRandomVariable(rv[rvIndex]);
			rvGraph.repaint();
			rvTable.repaint();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events for the number of dice.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()) {
			n = (int)nScroll.getValue();
			diceBoard.setDieCount(n);
			setDistributions();
		}
	}

	/**
	* This method sets the parameters of the distributions when the parameters
	* have changed.
	*/
	public void setDistributions(){
		sumDist.setParameters(dieDist, n);
		averageDist.setParameters(sumDist, 0, 1.0 / n);
		minDist.setParameters(dieDist, n, 1);
		maxDist.setParameters(dieDist, n, n);
		onesDist.setParameters(n, dieDist.getDensity(1));
		twosDist.setParameters(n, dieDist.getDensity(2));
		threesDist.setParameters(n, dieDist.getDensity(3));
		foursDist.setParameters(n, dieDist.getDensity(4));
		fivesDist.setParameters(n, dieDist.getDensity(5));
		sixesDist.setParameters(n, dieDist.getDensity(6));
		
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

