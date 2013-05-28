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
package edu.uah.math.games;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.Domain;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RedBlackGraph;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.DataTable;

/**
* This class models the red-black game.  A player plays Bernoulli trials against
* the house at even stakes until she loses her fortune or reaches a specified
* target fortune.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RedBlackGame extends Game implements Serializable{
	//Variables
	private int runs = 0, initial = 8, target = 16, bet = 1, current, trials;
	private double p = 0.5, trialsMean;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Game", "J", "N"});
	private RecordTable gameTable = new RecordTable(new String[]{"Trial", "I", "X"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private RedBlackGraph rbGraph = new RedBlackGraph(8, 16);
	private IntervalData success = new IntervalData(new Domain(0, 1, 1, Domain.DISCRETE), "J");
	private Histogram successGraph = new Histogram(success);
	private DataTable successTable = new DataTable(success);
	private StatisticsTable trialsTable = new StatisticsTable("N", new String[]{"Mean"});
	private JComboBox targetChoice = new JComboBox();
	private JButton playButton = new JButton();
	private JButton gameButton = new JButton();
	private Parameter initialScroll = new Parameter(0, target, 1, initial, "Initial fortune", "x");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Trial win probability", "p");
	private Parameter betScroll = new Parameter(0, initial, 1, bet, "Bet", "v");

	/**
	* This method initializes the experiment, including the play button,
	* the new game button, the slider for the probability of success, the slider
	* for the amount of bet, the slider for the intial fortune, the choice box
	* for the target fortune, the toolbar, the graph for the success random variable,
	* the red-black graph, the table for the success random variable, the table for the
	* outcome of the game, the table for the number of trials, and the record table.
	*/
	public void init(){
		super.init();
		setName("Red and Black Game");
		//Play Button
		playButton.setToolTipText("Play");
		playButton.setIcon(new ImageIcon(getClass().getResource("step.gif")));
		playButton.addActionListener(this);
		//New Game Button
		gameButton.setToolTipText("New Game");
		gameButton.setIcon(new ImageIcon(getClass().getResource("reset.gif")));
		gameButton.addActionListener(this);
		//Probability slider
		pScroll.applyDecimalPattern("0.0");
		pScroll.setWidth(150);
		pScroll.getSlider().addChangeListener(this);
		//Bet slider
		betScroll.setWidth(150);
		betScroll.getSlider().addChangeListener(this);
		//Initial fortune slider
		initialScroll.setWidth(150);
		initialScroll.getSlider().addChangeListener(this);
		//Target choice
		targetChoice.addItemListener(this);
		targetChoice.setToolTipText("Target fortune");
		for (int i = 1; i < 8; i++) targetChoice.addItem("a = " + Math.pow(2, i));
		targetChoice.setSelectedIndex(3);
		//Construct toolbar
		addTool(gameButton);
		addTool(playButton);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(targetChoice);
		toolBar.add(initialScroll);
		toolBar.add(betScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Red Black Graph
		addComponent(rbGraph, 0, 0, 1, 1);
		//Success Graph
		successGraph.setStatisticsType(0);
		successGraph.setMargins(35, 20, 20, 20);
		addComponent(successGraph, 1, 0, 1, 1);
		//Game Table
		gameTable.setDescription("I: trial win indicator; X: current fortune");
		addComponent(gameTable, 2, 0, 1, 1);
		//Record Table
		recordTable.setDescription("J: game outcome, N: number of trials");
		addComponent(recordTable, 0, 1, 1, 1);
		//Success Table
		successTable.setStatisticsType(0);
		addComponent(successTable, 1, 1, 1, 1);
		//Trials Table
		trialsTable.setShow(StatisticsTable.DATA);
		addComponent(trialsTable, 2, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method resets the experiment, including the success random variable,
	* the record table, the table for the number of trials, and the table and
	* graph for the success random variable.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		success.reset();
		resetGame();
		trialsTable.reset();
		successTable.repaint();
		successGraph.repaint();
	}

	/**
	* This method handles the events associated with the play and
	* new game buttons.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == playButton){
			int i;
			trials++;
			if (Math.random() <= p){
				current = current + bet;
				i = 1;
			}
			else{
				current = current - bet;
				i = 0;
			}
			rbGraph.setCurrent(current);
			rbGraph.repaint();
			gameTable.addRecord(new double[]{trials, i, current});
			resetBet();
			if ((current == 0) | (current == target)){
				runs++;
				if (current == target) success.setValue(1);
				else success.setValue(0);
				//playnote((int)success.getValue());
				trialsMean = ((runs - 1) * trialsMean + trials) / runs;
				successGraph.repaint();
				successTable.repaint();
				trialsTable.setDataValues(new double[]{trialsMean});
				recordTable.addRecord(new double[]{runs, success.getValue(), trials});
				playButton.setEnabled(false);
			}
		}
		else if (e.getSource() == gameButton) resetGame();
		else super.actionPerformed(e);
	}

	/**
	* This method handles the choice events assoicated with changing the
	* target fortune.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == targetChoice){
			target = (int)(Math.pow(2, targetChoice.getSelectedIndex() + 1));
			initial = Math.min(initial, target);
			initialScroll.setRange(1, target, 1, initial - 1);
			rbGraph.setParameters(initial, target);
			reset();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changing the initial
	* fortune or the probability of success.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == betScroll.getSlider()){
			bet = (int)betScroll.getValue();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			reset();
		}
		else if (e.getSource() == initialScroll.getSlider()){
			initial = (int)initialScroll.getValue();
			rbGraph.setParameters(initial, target);
			reset();
		}
	}

	/**
	* This method resets the game, including the red-black graph, the game table.
	*/
	public void resetGame(){
		trials = 0;
		current = initial;
		playButton.setEnabled(true);
		resetBet();
		gameTable.reset();;
		rbGraph.setCurrent(current);
		rbGraph.repaint();
	}

	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "This is a computer model for the Gambeler's Ruin (red-black) game.  \n"+
			"A player plays Bernoulli trials against the house at even stakes\n"+
			"until she loses her fortune or reaches a specified target fortune.\n";
	}
	
	/**
	* This method resets the bet.
	*/
	public void resetBet(){
		bet = Math.min(current, target - current);
		betScroll.setRange(0, Math.min(current, target - current), 1, bet);
	}
}

