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
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RedBlackGraph;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.EstimateGraph;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the red and black experiment. A player bets on a sequence of
* Bernoulli trials at even stakes. The player starts with an intial fortune and
* must quit when she is either ruined or reaches a target fortune. The initial
* and target fortunes can be varied as well as the probability of success. Either of
* two classical strategies can be chosen: bold play or timid play.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RedBlackExperiment extends Experiment implements Serializable{
	//Parameters and variables
	public static final int TIMID_PLAY = 0, BOLD_PLAY = 1;
	private int strategy = TIMID_PLAY, initial = 8, target = 16, current, bet, trials;
	private double p = 0.5, successProb = 0.5, trialsDistMean = 64, trialsDataMean;
	private double[] boldProb = new double[129];
	private double[] boldMean = new double[129];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "J", "N"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private RedBlackGraph rbGraph = new RedBlackGraph(8, 16);
	private RandomVariable success = new RandomVariable(new BernoulliDistribution(0.5), "J");
	private RandomVariableGraph successGraph = new RandomVariableGraph(success);
	private RandomVariableTable successTable = new RandomVariableTable(success);
	private EstimateGraph trialsGraph = new EstimateGraph(trialsDistMean);
	private StatisticsTable trialsTable = new StatisticsTable("N", new String[]{"Mean"});
	private JComboBox strategyChoice = new JComboBox();
	private JComboBox targetChoice = new JComboBox();
	private Parameter initialScroll = new Parameter(0, target, 1, initial, "Initial fortune", "x");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Trial win probability", "p");
	private Timer timer = new Timer(50, this);

	/**
	* This method initializes the experiment, including the record table, the sliders for
	* the probability of success and the initial fortune, the choice box fo the type
	* of strategy and the target fortune, the toolbar, the red-black, success, and
	* number of trials graphs, and the success and number of trials tables.
	*/
	public void init(){
		super.init();
		setName("Red and Black Experiment");
		//Parameters
		initialScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Strategy choice
		strategyChoice.addItemListener(this);
		strategyChoice.setToolTipText("Strategy");
		strategyChoice.addItem("Timid Play");
		strategyChoice.addItem("Bold Play");
		//Target choice
		targetChoice.addItemListener(this);
		targetChoice.setToolTipText("Target fortune");
		for (int i = 1; i < 8; i++) targetChoice.addItem("a = " + Math.pow(2, i));
		targetChoice.setSelectedIndex(3);
		//Construct toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(strategyChoice);
		toolBar.add(targetChoice);
		toolBar.add(initialScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Red Black Graph
		addComponent(rbGraph, 0, 0, 1, 1);
		//Success Graph
		successGraph.setMomentType(0);
		successGraph.setMargins(35, 20, 20, 20);
		addComponent(successGraph, 1, 0, 1, 1);
		//Trials Graph
		trialsGraph.setMargins(50, 20, 20, 20);
		trialsGraph.setToolTipText("Theoretical and empirical mean of N");
		addComponent(trialsGraph, 2, 0, 1, 1);
		//Record Table
		recordTable.setDescription("J: player wins, N: number of trials");
		addComponent(recordTable, 0, 1, 1, 1);
		//Success Table
		successTable.setStatisticsType(0);
		addComponent(successTable, 1, 1, 1, 1);
		//Trials Table
		trialsTable.setDescription("Theoretical and empirical mean of N");
		addComponent(trialsTable, 2, 1, 1, 1);
		//Final actions
		computeBoldParameters();
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
			+ "In the red and black experiment, a player starts with an initial fortune x and bets (at\n"
			+ "even stakes) on independent trials for which the probability of winning is p. Play \n"
			+ "continues until the player is either ruined or reaches a fixed target fortune a.  Either\n"
			+ "of two player strategies can be selected from a list box. With timid play, the player bets\n"
			+ "1 on each trial. With bold play, the player bets her entire fortune or what is needed to\n"
			+ "reach the target (whichever is smaller). Variable J indicates the event that the player\n"
			+ "wins (reaches her target) and variable N is the number of trialsplayed. These variables\n"
			+ "are recorded on each update. The first graph shows the initial andtarget fortuens and the\n"
			+ "final outcome. The density of J is shown in the middle graph and table, and the mean of N\n"
			+ "is shown in the last graph and table. The parameters x and p can be varied with scroll\n"
			+ "bars, and the targetfortune a can be chosen from a list box.";
	}

	/**
	* This method define the experiment. The Bernoulli trials are simulated and the outcome
	* of the game determined, based on the strategy. The number of trials is recorded.
	*/
	public void doExperiment(){
		super.doExperiment();
		trials = 0; current = initial;
		while (current > 0 & current < target) play();
		if (current == target) success.setValue(1); else success.setValue(0);
		int runs = getTime();
		trialsDataMean = ((runs - 1) * trialsDataMean + trials) / runs;
	}

	/**
	* This method plays one trial and adusts the current fortune. This method should
	* only be called when the current fortune is strictly between 0 and the target fortune.
	*/
	public void play(){
		if (strategy == TIMID_PLAY) bet = 1;
		else if (strategy == BOLD_PLAY){
			if (current < target - current) bet = current;
			else bet = target - current;
		}
		if (Math.random() <= p) current = current + bet;
		else current = current - bet;
		trials++;
	}

	/**
	* This method runs the experiment one time, adding additional annimation and sound.
	*/
	public void step(){
		stop();
		trials = 0;
		current = initial;
		rbGraph.setCurrent(current);
		rbGraph.repaint();
		timer.start();
	}

	/**
	* This method stops the step process if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the step process if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method updates the experiment, including the red-black graph, the success,
	* number of trials graphs, and the record, success, and number of trials tables.
	*/
	public void update(){
		super.update();
		rbGraph.setCurrent(current);
		rbGraph.repaint();
		successGraph.repaint();
		trialsGraph.setEstimate(trialsDataMean);
		trialsGraph.repaint();
		recordTable.addRecord(new double[]{getTime(), success.getValue(), trials});
		successTable.repaint();
		trialsTable.setDataValues(new double[]{trialsDataMean});;
	}
	
	public void graphUpdate(){
		super.update();
		trialsGraph.setShowModelDistribution(showModelDistribution);
		successGraph.setShowModelDistribution(showModelDistribution);
		trialsGraph.repaint();
		successGraph.repaint();
		trialsTable.setShowModelDistribution(showModelDistribution);
		successTable.setShowModelDistribution(showModelDistribution);
		trialsTable.repaint();
		successTable.repaint();
	}

	/**
	* This method resets the experiment, including the success random variable, the
	* red-black graph, the success graph, the number of trials graph, the record table,
	* the success table, and the number of trials table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		success.reset();
		rbGraph.setCurrent(initial);
		rbGraph.repaint();
		successGraph.repaint();
		trialsGraph.setEstimate(0);
		trialsGraph.repaint();
		successTable.reset();
		trialsTable.reset();
	}

	/**
	* This method handles the timer events associated with the step process. Until the game
	* is over, the graph is updated.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (current > 0 & current < target){
				play();
				rbGraph.setCurrent(current);
				rbGraph.repaint();
			}
			else{
				timer.stop();
				super.doExperiment();
				if (current == target) success.setValue(1); else success.setValue(0);
				int runs = getTime();
				trialsDataMean = ((runs - 1) * trialsDataMean + trials) / runs;
				trialsGraph.setEstimate(trialsDataMean);
				//playnote(current == target);
				trialsGraph.repaint();
				trialsTable.setDataValues(new double[]{trialsDataMean});
				recordTable.addRecord(new double[]{getTime(), success.getValue(), trials});
				successGraph.repaint();
				successTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method handles the choice events associated with a change in strategy
	* or a change in the target fortune.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == strategyChoice){
			strategy = strategyChoice.getSelectedIndex();
			setParameters();
		}
		else if (e.getSource() == targetChoice){
			target = (int)(Math.pow(2, targetChoice.getSelectedIndex() + 1));
			initial = Math.min(initial, target);
			initialScroll.setRange(0, target, 1, initial);
			setParameters();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with a change in the initial
	* fortune or a change in the probability of success.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == initialScroll.getSlider()){
			initial = (int)initialScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			computeBoldParameters();
			setParameters();
		}
	}

	/**
	* This method sets the parameters:  the success probability and the mean of the
	* number of trials.
	*/
	public void setParameters(){
		rbGraph.setParameters(initial, target);
		if (strategy == TIMID_PLAY){
			if (p == 0){
				successProb = 0;
				trialsDistMean = initial;
			}
			else if (p == 0.5){
				successProb = (double)initial / target;
				trialsDistMean = initial * (target - initial);

			}
			else{
				successProb = (Math.pow((1 - p) / p, initial) - 1) / (Math.pow((1 - p) / p, target) - 1);
				trialsDistMean = (initial - target * successProb) / (1 - 2 * p);
			}
		}
		else{
			int index = (int)Math.pow(2, 6 - targetChoice.getSelectedIndex()) * initial;
			successProb = boldProb[index];
			trialsDistMean = boldMean[index];
			}
		success = new RandomVariable(new BernoulliDistribution(successProb), "J");
		successGraph.setRandomVariable(success);
		successTable.setRandomVariable(success);
		trialsGraph.setParameter(trialsDistMean);
		trialsTable.setDistributionValues(new double[]{trialsDistMean});
		reset();
	}

	/**
	* This method computes the probability of reaching the target fortune and the
	* expected number of trials under the bold strategy.
	*/
	public void computeBoldParameters(){
		int order = 0;
		int index, ones;
		double a, b, c, d, e, f;
		boldProb[0] = 0;
		boldMean[128] = 0;
		for (int i = 0; i <= 1; i++){
			if (i == 1){order = 1; a = 1 - p;} else a = p;
			for (int j = 0; j <= 1; j++){
				if (j == 1){order = 2; b = 1 - p;} else b = p;
				for (int k = 0; k <= 1; k++){
				   if (k == 1){order = 3; c = 1 - p;} else c = p;
				   for (int l = 0; l <= 1; l++){
					  	if (l == 1){order = 4; d = 1 - p;} else d = p;
						for (int m = 0; m <= 1; m++){
						if (m == 1){order = 5; e = 1 - p;} else e = p;
						for (int n = 0; n <= 1; n++){
							if (n == 1){order = 6; f = 1 - p;} else f = p;
							for (int r = 0; r <= 1; r++){
								if (r == 1) order = 7;
								index = i * 64 + j * 32 + k * 16 + l * 8 + m * 4 + n * 2 + r;
								ones = i + j + k + l + m + n + r;
								boldProb[index + 1] = boldProb[index] + Math.pow(1 - p, ones) * Math.pow(p, 7 - ones);
								if (order == 0) boldMean[index] = 0;
								else if (order == 1) boldMean[index] = 1;
								else if (order == 2) boldMean[index] = 1 + a;
								else if (order == 3) boldMean[index] = 1 + a + a * b;
								else if (order == 4) boldMean[index] = 1 + a + a * b + a * b * c;
								else if (order == 5) boldMean[index] = 1 + a + a * b + a * b * c + a * b * c * d;
								else if (order == 6) boldMean[index] = 1 + a + a * b + a * b * c + a * b * c * d + a * b * c * d * e;
								else if (order == 7) boldMean[index] = 1 + a + a * b + a * b * c + a * b * c * d + a * b * c * d * e + a * b * c * d * e * f;
							}
						 }
					  }
				   }
				}
			}
		}
	}

	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}
