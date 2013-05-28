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
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Card;
import edu.uah.math.devices.CardHand;
import edu.uah.math.devices.Parameter;

/**
* The card experiment consists of drawing a sample of cards, without replacement,
* form a standard deck.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CardExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 5, trial;
	private double[] record;
	//Objects
	private JToolBar toolBar = new JToolBar();
	private CardHand hand = new CardHand(n);
	private Parameter cardScroll = new Parameter(1, 14, 1, n, "Number of cards", "n");
	private RecordTable recordTable = new RecordTable();
	private Timer timer = new Timer(200, this);
	private ActionListener listener;

	/**
	* This method initializes the experiment: including the scrollbar, cards, record table.
	*/
	public void init(){
		
		super.init();
		setName("CardExperiment");
		//Card slider
		cardScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(cardScroll);
		addToolBar(toolBar);
		//Cards
		addComponent(hand, 0, 0, 1, 1);
		//Record table
		recordTable.setDescription("Yi: denomination of card i, Zi: suit of card i");
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
			+ "The experiment consists of dealing n cards at random from a standard deck of 52 cards.\n"
			+ "The denomination Yi and suit Zi of the i'th card are recorded for i = 1, ..., n on each update.\n"
			+ "The denominations are encoded as follows: 1 (ace), 2-10, 11 (jack), 12 (queen), 13 (king). \n"
			+ "The suits are encoded as follows: 0 (clubs), 1 (diamonds), 2 (hearts), 3 (spades). \n"
			+ "The parameter n can be varied with a scroll bar.";
	}

	/**
	* This method defines the experimnet: a sample of cards is chosen from
	* the deck.
	*/
	public void doExperiment(){
		super.doExperiment();
		hand.deal();
	}

	public void addListener(ActionListener l){
		listener =l;
	}
	
	/**
	* This method runs the experiment one time, with additional sound and annimation.
	*/
	public void step(){
		stop();
		record = new double[2 * n + 1];
		hand.setFaceUp(false);
		hand.deal();
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
	* This method resets the experiment. The card backs are shown and the record table
	* is reset.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		String[] variables = new String[2 * n + 1];
		variables[0] = "Run";
		for (int i = 0; i < n; i++){
			variables[2 * i + 1] = "Y" + (i + 1);
			variables[2 * i + 2] = "Z" + (i + 1);
		}
		recordTable.setVariableNames(variables);
		hand.setFaceUp(false);
	}

	/**
	* This method updates the display by showing the cards in the sample and updating the
	* record text.
	*/
	public void update(){
		super.update();
		record = new double[2 * n + 1];
		record[0] = getTime();
		for (int i = 0; i < n; i++){
			record[2 * i + 1] = hand.getValues(i);
			record[2 * i + 2] = hand.getSuits(i);
		}
		recordTable.addRecord(record);
		hand.setFaceUp(true);
	}

	/**
	* This method handles scrollbar event associated with a change in the sample size.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == cardScroll.getSlider()){
			n = (int)cardScroll.getValue();
			hand.setCardCount(n);
			reset();
		}
	}

	public JPanel getAnimation(){
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(toolBars);
		p.add(hand);
		return p;
	}
	/**
	* This method handles the timer events associated with the step process. The cards
	* are shown one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < n){
				Card card = hand.getCard(trial);
				card.setFaceUp(true);
				record[2 * trial + 1] = card.getValue();
				record[2 * trial + 2] = card.getSuit();
				//playnote(card.getCardNumber());
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
		// System.out.println("card getResultTable row count"+recordTable.getTable().getRowCount());
	    	return  recordTable.getTable();
	    }
}

