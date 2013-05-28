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
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.Forest;

/**
* This class models the fire experiment.  Each tree in a rectangular forest can be on fire,
* burnt, or green.  When a tree is on fire at time t, it may catch any of its neighboring trees
* on fire at time t + 1. A tree on fire at time t is burnt at time t + 1, and remains burnt
* thereafter.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class FireExperiment extends Experiment implements Serializable{
	//Variables
	private double [] prob = new double[4];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Time", "Healthy", "On fire", "Burnt"});
	private Forest forest = new Forest(120, 60);
	private JToolBar paramToolBar = new JToolBar("Parameter Toolbar");
	private JToolBar probToolBar = new JToolBar("Probability Toolbar");
	private Parameter[] probScroll = new Parameter[4];
	private String[] probName = {"Left probability", "Right probability", "Down probability", "Up probability"};
	private String[] probSymbol = {"pl", "pr", "pd", "pu"};
	private JComboBox forestChoice = new JComboBox();
	private JComboBox editChoice = new JComboBox();

	/**
	* This method initializes the experiment, including the toolbars, scrollbars, labels, and forest.
	*/
	public void init(){
		super.init();
		setName("Fire Experiment");
		//Set stop choice to "continuous"
		getStopChoice().setSelectedIndex(0);
		//Probability scrollbars and labels
		for (int i = 0; i < 4; i++){
			prob[i] = 0.5;
			probScroll[i] = new Parameter(0, 1, 0.05, prob[i], probName[i], probSymbol[i]);
			probScroll[i].applyDecimalPattern("0.00");
			probScroll[i].setWidth(140);
			probScroll[i].getSlider().addChangeListener(this);
		}
		//Forest choice
		forestChoice.addItem("Forest: 120 by 60");
		forestChoice.addItem("Forest: 600 by 300");
		forestChoice.addItemListener(this);
		forestChoice.setToolTipText("Forest size");
		//Edit choice
		editChoice.addItemListener(this);
		editChoice.addItem("Set State: Green");
		editChoice.addItem("Set State: Red");
		editChoice.addItem("Set State: Black");
		editChoice.setSelectedIndex(1);
		editChoice.setToolTipText("Click to set state");
		//Toolbars
		paramToolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		paramToolBar.add(editChoice);
		paramToolBar.add(forestChoice);
		probToolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (int i = 0; i < 4; i++) probToolBar.add(probScroll[i]);
		addToolBar(paramToolBar);
		addToolBar(probToolBar);
		//Forest
		forest.burn();
		forest.addMouseListener(this);
		addComponent(forest, 0, 0, 2, 1, 10, 2);
		//addComponent(forest, 0, 0, 1, 1, GridBagConstraints.CENTER);
		//Record Table
		recordTable.setDescription("Number of trees of each type");
		addComponent(recordTable, 0, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The forest consists of a rectangular lattice of trees. Each tree in the forest\n"
			+ "is in one of three possible states: Healthy trees are colored green; on fire trees\n"
			+ "are colored red; burnt trees are colored black. A tree that is on fire at time t will\n"
			+ "be burnt at time t + 1; once a tree is burnt it remains burnt; if a tree is healthy at\n"
			+ "time t and is directly above or below or to the right or to the left of a tree on fire at\n"
			+ "time t, then the healthy tree will catch on fire at time, t + 1 independently, with\n"
			+ "respective probabilities pu,  pd, pr, and pl.\n\n"
			+ "The forest is shown in the large graph box in the center. You can choose either a \n"
			+ "100 by 50 forest or a 500 by 250 forest. To change the state of a tree, select the\n"
			+ "desired state with the list box and then click on the tree. The fire spread\n"
			+ "probabilities can be varied with scroll bars. The number of trees that are healthy,\n"
			+ "on fire, and burnt are recorded at each time unit in the table.";
	}

	/**
	* This method defines the experiment. For each tree on fire, the forest object
	* determines if any of the neighboring trees will catch on fire. The trees currently
	* on fire change to burnt. The number of trees in each state is compute.
	*/
	public void doExperiment(){
		super.doExperiment();
		forest.burn();
		setStopNow(forest.treesOnFire() == 0);
	}

	/**
	* This method resets the experiment, including the forest, and the record table.
	*/
	public void reset(){
		super.reset();
		forest.reset();
		recordTable.reset();
		recordTable.addRecord(new double[]{getTime(), forest.treesHealthy(),
			forest.treesOnFire(), forest.treesBurnt()});
	}

	/**
	* This method updates the experiment, including the forest and the update record.
	*/
	public void update(){
		super.update();
		forest.repaint();
		recordTable.addRecord(new double[]{getTime(), forest.treesHealthy(),
			forest.treesOnFire(), forest.treesBurnt()});
	}

	/**
	* This method handles the scrollbar events associated with changes in the fire-spread
	* probabilities.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		for (int i = 0; i < 4; i++){
			if (e.getSource() == probScroll[i].getSlider()){
				prob[i] = probScroll[i].getValue();
			}
		}
		forest.setProbabilities(prob);
		reset();
	}

	/**
	* This method handles the choice events associated with changing the size of the forest
	* and changing the edit color.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == forestChoice){
			if (forestChoice.getSelectedIndex() == 0) forest.setParameters(120, 60, 5);
			else forest.setParameters(600, 300, 1);
			validate();
			reset();
		}
		else if (e.getSource() == editChoice) forest.setEditState(editChoice.getSelectedIndex());
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the mouse click events that are associated with changing the
	* states of trees. The record table is updated to show the new counts.
	*/
	public void mouseClicked(MouseEvent e){
		if (e.getSource() == forest) recordTable.addRecord(new double[]{getTime(), forest.treesHealthy(),
			forest.treesOnFire(), forest.treesBurnt()});
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}



