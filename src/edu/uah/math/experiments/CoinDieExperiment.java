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
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Coin;
import edu.uah.math.devices.Die;
import edu.uah.math.devices.DiceBoard;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.DieProbabilityDialog;
import edu.uah.math.distributions.DieDistribution;
import edu.uah.math.distributions.MixtureDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* The coin-die experiment consists of tossing a coin, and then rolling one of
* two dice, depending on the value of the coin. The outcome of the coin and die are
* recorded. The random variable of interest is the die score.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CoinDieExperiment extends Experiment implements Serializable{
	//Variables
	private double p = 0.5;
	private int coinScore, dieScore;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[] {"Run", "X", "Y"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private DiceBoard diceBoard = new DiceBoard(2);
	private Coin coin = new Coin(p);
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Probability of heads", "p");
	private JButton die0Button = new JButton();
	private JButton die1Button = new JButton();
	private DieDistribution die0Dist = new DieDistribution(DieDistribution.FAIR);
	private DieDistribution die1Dist = new DieDistribution(DieDistribution.FLAT16);
	private MixtureDistribution dieDist = new MixtureDistribution(die0Dist, die1Dist, p);
	private RandomVariable dieRV = new RandomVariable(dieDist, "Y");
	private RandomVariableGraph dieRVGraph = new RandomVariableGraph(dieRV);
	private RandomVariableTable dieRVTable = new RandomVariableTable(dieRV);
	private DieProbabilityDialog dieProbabilityDialog;
	private Frame frame;
	private Timer timer = new Timer(500, this);

	/**
	* This method initializes the experiment, including the record table, the die
	* probability buttons, the probability slider, the toolbar, the die and coin, and
	* the random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Coin Die Experiment");
		//die buttons
		die0Button.addActionListener(this);
		die0Button.setToolTipText("Green die probabilities");
		die0Button.setIcon(new ImageIcon(getClass().getResource("greenDie.gif")));
		die1Button.addActionListener(this);
		die1Button.setToolTipText("Red die probabilities");
		die1Button.setIcon(new ImageIcon(getClass().getResource("redDie.gif")));
		//Probability scroll
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(die0Button);
		toolBar.add(die1Button);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Coin
		coin.setMinimumSize(new Dimension(32, 32));
		addComponent(coin, 0, 0, 1, 1, GridBagConstraints.NONE);
		//Dice board
		diceBoard.getDie(0).setBackColor(coin.getTailColor());
		diceBoard.getDie(0).setToolTipText("Green die");
		diceBoard.getDie(1).setBackColor(coin.getHeadColor());
		diceBoard.getDie(1).setToolTipText("Red die");
		diceBoard.setMinimumSize(new Dimension(88, 44));
		addComponent(diceBoard, 1, 0, 1, 1, GridBagConstraints.NONE);
		//Random variable graph
		dieRVGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(dieRVGraph, 1, 1, 1, 1);
		//Record Table
		recordTable.setDescription("Run: index; X: coin score; Y: die score");
		addComponent(recordTable, 0, 1, 1, 2);
		//Random variable table
		addComponent(dieRVTable, 1, 2, 1, 1);
		//Final actions
		setDistribution();
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
			+ "The coin die experiment consists of first tossing a coin, and then rolling a red die if\n"
			+ "the coin lands head or a green die if the coin lands tails. The value of the coin\n"
			+ "(1 for heads and 0 for tails) and the score on the die that is rolled are recorded\n"
			+ "on each update. You can specify the die distribution of the dice by clicking on the\n"
			+ "die probability buttons; these buttons bring up the die probability dialog box.\n"
			+ "You can define your own distribution by typing probabilities into the text fields of\n"
			+ "the dialog box, or you can click on one of the buttons in the dialog box to specify one\n"
			+ "of the following special distributions: fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left,\n"
			+ "or skewed right. he probability of heads p for the coin can be varied with a scroll bar.\n"
			+ "The density and moments of the die that is rolled are shown in the distribution graph\n"
			+ "and distribution table.";
	}

	/**
	* This method defines the experiment. The coin is tossed and then the appropriate
	* die rolled.
	*/
	public void doExperiment(){
		super.doExperiment();
		coin.toss();
		coinScore = coin.getValue();
		diceBoard.getDie(coinScore).roll();
		dieScore = diceBoard.getDie(coinScore).getValue();
		dieRV.setValue(dieScore);
	}

	/**
	* This method runs the the experiment one time, and adds sounds depending on the outcome
	* of the experiment.
	*/
	public void step(){
		stop();
		diceBoard.setRolled(false);
		coin.toss();
		coin.setTossed(true);
		coinScore = coin.getValue();
		//playNote(coinScore);
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
	* This method resets the experiment, including the coin, die, die random variable,
	* record table, and random variable graph and table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		coin.setTossed(false);
		diceBoard.setRolled(false);
		dieRV.reset();
		dieRVGraph.reset();
		dieRVTable.reset();
	}

	/**
	* This method updates the display, including the coin, die, record table, and the
	* random variable graph and table.
	*/
	public void update(){
		super.update();
		coin.setTossed(true);
		diceBoard.getDie(coinScore).setRolled(true);
		diceBoard.getDie(1 - coinScore).setRolled(false);
		recordTable.addRecord(new double[] {getTime(), coinScore, dieScore});
		dieRVGraph.repaint();
		dieRVTable.repaint();
	}
	public void graphUpdate(){
		super.update();
		dieRVGraph.setShowModelDistribution(showModelDistribution);
		dieRVGraph.repaint();
		dieRVTable.setShowModelDistribution(showModelDistribution);
		dieRVTable.repaint();
	}

	/**
	* This method handles the button events associated with changes in the die
	* probabilities.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		frame = new Frame();
		dieProbabilityDialog = new DieProbabilityDialog(frame);
		if (e.getSource() == die0Button){
			Point fp = new Point (100,100);
			Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
			Point dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			dieProbabilityDialog.setLocation(dp);
			dieProbabilityDialog.setProbabilities(die0Dist.getProbabilities());
			dieProbabilityDialog.setTitle("Green die probabilities");
			dieProbabilityDialog.setVisible(true);
			if (dieProbabilityDialog.isOK()){
				die0Dist.setProbabilities(dieProbabilityDialog.getProbabilities());
				setDistribution();
			}
		}
		else if (e.getSource() == die1Button){
			Point fp = new Point (100,100);
			Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
			Point dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			dieProbabilityDialog.setLocation(dp);
			dieProbabilityDialog.setProbabilities(die1Dist.getProbabilities());
			dieProbabilityDialog.setTitle("Red die probabilities");
			dieProbabilityDialog.setVisible(true);
			if (dieProbabilityDialog.isOK()){
				die1Dist.setProbabilities(dieProbabilityDialog.getProbabilities());
				setDistribution();
			}
		}
		else if (e.getSource() == timer){
			timer.stop();
			super.doExperiment();
			Die die = diceBoard.getDie(coinScore);
			die.roll();
			die.setRolled(true);
			dieScore = die.getValue();
			diceBoard.getDie(1 - coinScore).setRolled(false);
			//playNote(dieScore);
			dieRV.setValue(dieScore);
			recordTable.addRecord(new double[] {getTime(), coinScore, dieScore});
			dieRVGraph.repaint();
			dieRVTable.repaint();
		}
		else super.actionPerformed(e);
	}

	/**
	* This method handles events associated with the probability slider, which sets
	* the probability of heads for the coin.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			setDistribution();
		}
	}

	/**
	* This method sets the distribution of the die random variable. The method
	* is called whenever the probability of heads changes or when the die distributions
	* change.
	*/
	public void setDistribution(){
		coin.setProbability(p);
		diceBoard.getDie(0).setProbabilities(die0Dist.getProbabilities());
		diceBoard.getDie(1).setProbabilities(die1Dist.getProbabilities());
		dieDist.setParameters(die0Dist, die1Dist, p);
		reset();
	}
	
	public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

