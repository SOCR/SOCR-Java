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
import java.awt.Frame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Voters;
import edu.uah.math.devices.ProbabilityDialog;

/**
* This class models the voter experiment.  Each member of a rectangular lattice of voters
* can, at any time, be in one of a finite number of states, represented by colors.  At each time,
* a voter is chosen at random and then a neighbor of the voter is chosen with various
* probabilities.  The state of the chosen voter is then changed to the state of the chosen neighbor.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class VoterExperiment extends Experiment implements Serializable{
	private final static String[] colorName = {"Black", "Blue", "Cyan", "Green", "Purple",
		"Orange", "Pink", "Red", "White", "Yellow"};
	//Variables
	private int colors = 10;
	private boolean stopWhenNewDeath = false;
	//Objects
	private RecordTable recordTable = new RecordTable();
	private Voters voters = new Voters(10, 5, 50);
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox voterChoice = new JComboBox();
	private JComboBox colorChoice = new JComboBox();
	private ProbabilityDialog probabilityDialog;
	private JButton probabilityButton = new JButton();
	private Frame frame;

	/**
	* This method initializes the experiment, including the set of voters, the toolbar
	* with choices and labels.
	*/
	public void init(){
		super.init();
		setName("Voter Experiment");
		//Probability button
		probabilityButton.addActionListener(this);
		probabilityButton.setToolTipText("Neigbor selection probabilities");
		probabilityButton.setIcon(new ImageIcon(getClass().getResource("neighbors.gif")));
		//Stop JComboBox
		JComboBox stopChoice = getStopChoice();
		stopChoice.addItem("Stop when Color Dies");
		stopChoice.setSelectedIndex(5);
		setStopFreq(-1);
		stopWhenNewDeath = true;
		//Voter choice
		voterChoice.setToolTipText("Voter array size");
		voterChoice.addItem("10 by 5");
		voterChoice.addItem("20 by 10");
		voterChoice.addItem("50 by 25");
		voterChoice.addItemListener(this);
		//Color choice
		colorChoice.setToolTipText("Click to set state");
		colorChoice.addItemListener(this);
		for (int i = 0; i < colors; i++) colorChoice.addItem(colorName[i]);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(probabilityButton);
		toolBar.add(voterChoice);
		toolBar.add(colorChoice);
		addToolBar(toolBar);
		//Voters
		voters.addMouseListener(this);
		addComponent(voters, 0, 0, 1, 1, GridBagConstraints.NONE);
		//Record table
		String[] variables = new String[colors + 1];
		variables[0] = "Time";
		for (int i = 0; i < colors; i++) variables[i + 1] = colorName[i];
		recordTable.setVariableNames(variables);
		recordTable.setDescription("Number of sites of each color");
		addComponent(recordTable, 0, 1, 1, 1);
		//Final actions
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
			+ "Each site in the voter array can be in one of 10 states, represented by\n"
			+ "various colors. At each discrete time unit, a site is chosen at random,\n"
			+ "and then a neighbor of the site is chosen at random. The state (color) of\n"
			+ "the site is changed to the state (color) of the neighbor.  The record table\n"
			+ "shows the number of sites of each color on each update. You can change the\n"
			+ "state of a site by selecting the desired color in the list box and then\n"
			+ "clicking on the desired site.";
	}


	/**
	* This method defines the experiment. A voter is chosen at random and then a
	* neighbor of the voter is chosen according to specified probabilities. The
	* state of the voter is changed to the state of the neighbor. The stop condition
	* is tested.
	*/
	public void doExperiment(){
		super.doExperiment();
		voters.doExperiment();
		setStopNow((stopWhenNewDeath & voters.isNewDeath()) | voters.isConsensus());
	}

	/**
	* This method runs the experiment one time, playing a note that depends on the new
	* state of the voter.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote(voters.getNewState());
	}

	/**
	* This method resets the experiment, including the voters, and the record table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		voters.reset();
		double[] record = new double[colors + 1];
		record[0] = getTime();
		for (int i = 0; i < colors; i++) record[i + 1] = voters.getStateCount(i);
		recordTable.addRecord(record);
	}

	/**
	* This method updates the display, including the voters and the record table.
	*/
	public void update(){
		super.update();
		voters.repaint();
		double[] record = new double[colors + 1];
		record[0] = getTime();
		for (int i = 0; i < colors; i++) record[i + 1] = voters.getStateCount(i);
		recordTable.addRecord(record);
	}

	/**
	* This method handles the events associated with the stop choice and the voter choice.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == getStopChoice()){
			int j = getStopChoice().getSelectedIndex();
			if (j == 5){
				setStopFreq(-1);
				stopWhenNewDeath = true;
			}
			else{
				stopWhenNewDeath = false;
				super.itemStateChanged(e);
			}
		}
		else if (e.getSource() == voterChoice){
			switch (voterChoice.getSelectedIndex()){
			case 0:
				voters.setParameters(10, 5, 50);
				break;
			case 1:
				voters.setParameters(20, 10, 25);
				break;
			case 2:
				voters.setParameters(50, 25, 10);
				break;
			}
			reset();
		}
		else if (e.getSource() == colorChoice) voters.setEditState(colorChoice.getSelectedIndex());
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the mouse events that correspond to clicking on a voter.
	* The state of the voter is changed to the edit state, and the record table
	* is updated.
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		if (e.getSource() == voters){
			double[] record = new double[colors + 1];
			record[0] = getTime();
			for (int i = 0; i < colors; i++) record[i + 1] = voters.getStateCount(i);
			recordTable.addRecord(record);
		}
		else super.mouseClicked(e);
	}

	/**
	* This method handles the events for the probability button. The neighbor
	* probabilities are passed to the probability dialog box which is then displayed.
	* If the user clicks OK, the new probabilities are passed to the voter array.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == probabilityButton){
			// Ivo a workaround the problem that the applets do not know their
			// parent's Frames 01/26/05
			frame = new Frame();
			//frame = getFrame(this);
			
			Point fp = new Point(100, 100), dp;
			probabilityDialog = new ProbabilityDialog(frame, "Neighbor Probabilities", voters.getProbabilities());
			Dimension fs = frame.getSize(), ds = probabilityDialog.getSize();
			dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			probabilityDialog.setLocation(dp);
			probabilityDialog.setVisible(true);
			if (probabilityDialog.isOK()){
				voters.setProbabilities(probabilityDialog.getProbabilities());
				reset();
			}
		}
		else super.actionPerformed(e);
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}



