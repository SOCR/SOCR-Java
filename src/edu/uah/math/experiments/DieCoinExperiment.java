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
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Color;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Die;
import edu.uah.math.devices.Coin;
import edu.uah.math.devices.CoinBox;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.DieProbabilityDialog;
import edu.uah.math.distributions.DieDistribution;
import edu.uah.math.distributions.BinomialRandomNDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* The die-coin experiment consists of rolling a die, and then tossing a coin
* the number of times shown on the die. The random variable of interest is the number
* of heads.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DieCoinExperiment extends Experiment implements Serializable{
	//Variables
	private double p = 0.5;
	private int dieScore, headCount, stage;
	private int[] coinValues = new int[6];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "Y"});
	private JButton dieButton = new JButton();
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Die die = new Die();
	private CoinBox coinBox = new CoinBox(6, p, 26);
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Probability of heads", "p");
	private DieDistribution dieDist = new DieDistribution(DieDistribution.FAIR);
	private BinomialRandomNDistribution headsDist = new BinomialRandomNDistribution(dieDist, p);
	private RandomVariable heads = new RandomVariable(headsDist, "Y");
	private RandomVariableGraph headsGraph = new RandomVariableGraph(heads);
	private RandomVariableTable headsTable = new RandomVariableTable(heads);
	private DieProbabilityDialog dieProbabilityDialog;
	private Frame frame;
	private Timer timer = new Timer(200, this);

	/**
	* This method initializes the experiment, including the record table, die and coins,
	* the die probability dialog button, the slider for the probability of heads, the
	* toolbar, and the random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Die Coin Experiment");
		//die button
		dieButton.addActionListener(this);
		dieButton.setToolTipText("Die Probabilities");
		dieButton.setIcon(new ImageIcon(getClass().getResource("redDie.gif")));
		//Scroll bar
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(dieButton);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Die
		die.setMinimumSize(new Dimension(32, 32));
		addComponent(die, 0, 0, 1, 1, GridBagConstraints.NONE);
		//Coin Box
		coinBox.setMinimumSize(new Dimension(180, 32));
		addComponent(coinBox, 1, 0, 1, 1, GridBagConstraints.NONE);
		//Record Table
		recordTable.setDescription("X: die score, Y: number of heads");
		addComponent(recordTable, 0, 1, 1, 2);
		//Random Variable Graph
		addComponent(headsGraph, 1, 1, 1, 1);
		//Random Variable Table
		addComponent(headsTable, 1, 2, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including
	* copyright information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The die coin experiment consists of rolling a die and then tossing a coin the number of\n"
			+ "times shown on the die. The die score X and the number of heads Y are recorded on each update.\n"
			+ "The density function and moments of Y are shown in blue in the second graph and are recorded\n"
			+ "in the second table. On each update, the empirical density and moments of Y are shown in red\n"
			+ "in the second graph and are recorded in the second table.  The probability of heads for the \n"
			+ "for the coin can be varied with the scroll bar. You can specify the die distribution by clicking\n"
			+ "on the die probability button; this button bring up the die probability dialog box.\n"
			+ "You can define your own distribution by typing probabilities into the text fields\n"
			+ "of the dialog box, or you can click on one of the buttons in the dialog box to specify one \n"
			+ "of the following special distributions: fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left,\n"
			+ "or skewed right. ";
	}

	/**
	* This method defines the experiment. The die is rolled and then that number
	* of coins are tossed. The number of heads computed and passed to the random
	* variable.
	*/
	public void doExperiment(){
		super.doExperiment();
		die.roll();
		dieScore = die.getValue(); headCount = 0;
		for (int i = 0; i < dieScore; i++){
			coinBox.getCoin(i).toss();
			if (coinBox.getCoin(i).getValue() == 1) headCount++;
		}
		heads.setValue(headCount);
	}

	/**
	* This method runs the the experiment one time, and adds sounds and annimation,
	* depending on the outcome of the experiment.
	*/
	public void step(){
		stop();
		coinBox.setTossed(false);
		die.roll();
		die.setRolled(true);
		dieScore = die.getValue(); headCount = 0;
		////playnote(dieScore);
		stage = 0;
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
	* This method resets the experiment, including the record table, the number of
	* heads random varible, the die and coins, and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		heads.reset();
		headsGraph.reset();
		headsTable.reset();
		die.setRolled(false);
		coinBox.setTossed(false);
	}

	/**
	* This method updates the display in run mode, including the die and coins, the record table,
	* and the random variable graph and table.
	*/
	public void update(){
		super.update();
		die.setRolled(true);
		for (int i = 0; i < 6; i++) coinBox.getCoin(i).setTossed(i < dieScore);
		recordTable.addRecord(new double[]{getTime(), dieScore, headCount});
		headsGraph.repaint();
		headsTable.repaint();
	}
	public void graphUpdate(){
		super.update();
		headsGraph.setShowModelDistribution(showModelDistribution);
		headsGraph.repaint();
		headsTable.setShowModelDistribution(showModelDistribution);
		headsTable.repaint();
	}
	/**
	* This method handles the events associated with step timer and with the
	* die probabilty dialog button. The die distribution is passed to the
	* dialog box, which is then displayed. If the user changes the die
	* probabilities and clicks OK, the die distribution is updated.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == dieButton){
			frame = new Frame();
			dieProbabilityDialog = new DieProbabilityDialog(frame);
			Point fp = new Point(100,100);
			Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
			Point dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			dieProbabilityDialog.setLocation(dp);
			dieProbabilityDialog.setProbabilities(dieDist.getProbabilities());
			dieProbabilityDialog.setVisible(true);
			if (dieProbabilityDialog.isOK()){
				dieDist.setProbabilities(dieProbabilityDialog.getProbabilities());
				die.setProbabilities(dieProbabilityDialog.getProbabilities());
				setDistribution();
				reset();
			}
		}
		else if (e.getSource() == timer){
			if (stage < dieScore){
				Coin coin = coinBox.getCoin(stage);
				coin.toss();
				coin.setTossed(true);
				////playnote(coin.getValue());
				if (coin.getValue() == 1) headCount++;
				stage++;
			}
			else{
				timer.stop();
				super.doExperiment();
				heads.setValue(headCount);
				recordTable.addRecord(new double[]{getTime(), die.getValue(), headCount});
				headsGraph.repaint();
				headsTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method handles the events associated with changes in the probability of
	* heads.
	* @param e the change event.
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			coinBox.setProbability(p);
			setDistribution();
		}
	}

	/**
	* This method updates the distribution of the number of heads when the parameters
	* (the die distribution or the probability of heads changes).
	*/
	public void setDistribution(){
		headsDist.setParameters(dieDist, p);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

