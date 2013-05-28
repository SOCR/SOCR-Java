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
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.FlowLayout;
import java.io.Serializable;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.BertrandFloor;


/**
* Betrand's experiment is to create a random chord on the unit circle and see if the
* lenght of this chord is longer thant the length of a side of the inscribed triangle.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BertrandExperiment extends Experiment implements Serializable{
	//Variables
	public final static int UNIFORM_DISTANCE = 0, UNIFORM_ANGLE = 1, UNIFORM_POINT = 2;
	private int model = UNIFORM_DISTANCE;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "D", "A", "X", "Y", "I"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox modelChoice = new JComboBox();
	private BertrandFloor floor = new BertrandFloor();
	private BernoulliDistribution dist = new BernoulliDistribution(0.5);
	private RandomVariable chord = new RandomVariable(dist, "I");
	private RandomVariableGraph chordGraph = new RandomVariableGraph(chord);
	private RandomVariableTable chordTable = new RandomVariableTable(chord);

	/**
	* Initialize the experiment: Set the name, add the toolbar with the model choices;
	* add the floor graph and the random variable graph; add the record table and the
	* random variable table.
	*/
	public void init(){
		super.init();
		setName("Bertrand Experiment");
		//Model JComboBox
		modelChoice.addItemListener(this);
		modelChoice.addItem("Uniform Distance");
		modelChoice.addItem("Uniform Angle");
		modelChoice.addItem("Uniform Point");
		modelChoice.setToolTipText("Model");
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(modelChoice);
		addToolBar(toolBar);
		//Floor
		floor.setMinimumSize(new Dimension(100, 100));
		addComponent(floor, 0, 0, 1, 1);
		//Random variable graph
		chordGraph.setMomentType(0);
		chordGraph.setMinimumSize(new Dimension(25, 100));
		addComponent(chordGraph, 1, 0, 1, 1);
		//Record Table
		recordTable.setDescription("D: distance, A: angle, (X, Y): chord point, I: chord longer than triangle side");
		recordTable.setMinimumSize(new Dimension(100, 25));
		addComponent(recordTable, 0, 1, 1, 1);
		//Random varaible table
		chordTable.setMinimumSize(new Dimension(25, 25));
		chordTable.setStatisticsType(0);
		addComponent(chordTable, 1, 1, 1, 1);
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
			+ "Bertrand's experiment is to generate a random chord of a circle. In the simulation, \n"
			+ "one point of the chord is fixed at (1, 0) and the other random point (X, Y) is recorded \n"
			+ "on each update in the first table. Also recorded are D, the length of the line segment \n"
			+ "from the center of the circle to the center of the chord, and A, the angle that this line \n"
			+ "segment makes with the horizontal. Variable I indicates the event that the chord is longer \n"
			+ "than the length of a side of the inscribed equilateral triangle. The density of I is shown \n"
			+ "in blue in the distribution graph and and is recorded in the distribution table. \n"
			+ "On each update, the empirical density of I is shown in red in the distribution graph \n"
			+ "and is recorded in the distribution table. Three differnt models can be selected with the \n"
			+ "list box: the model where the distance D is uniformly distributed, the model where the \n"
			+ "angle A is uniformly distributed, and the model where the coordinate X is uniformly distributed.";
	}

	/**
	* This method determines which model is used: random distance or random angle.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == modelChoice){
			model = modelChoice.getSelectedIndex();
			dist.setProbability(floor.getProbability(model));
			reset();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method performs the experiment: compute random distance or angle (depending
	* on the model), compute the x and y coordinates of the chord.
	*/
	public void doExperiment(){
		super.doExperiment();
		if (model == UNIFORM_DISTANCE) floor.setDistance();
		else if (model == UNIFORM_ANGLE) floor.setAngle();
		else floor.setXCoordinate();
		if (floor.chordEvent()) chord.setValue(1);
		else chord.setValue(0);
	}

	/**
	* This method updates the display, including the record table, floor, random variable graph and
	* table.
	*/
	public void update(){
		super.update();
		floor.setChordDrawn(true);
		recordTable.addRecord(new double[] {getTime(), floor.getDistance(), floor.getAngle(),
			floor.getXCoordinate(), floor.getYCoordinate(), chord.getValue()});
		chordGraph.repaint();
		chordTable.repaint();
	}
	
	public void graphUpdate(){
		super.update();
		//floor.setChordDrawn(true);
		//recordTable.addRecord(new double[] {getTime(), floor.getDistance(), floor.getAngle(),
		//	floor.getXCoordinate(), floor.getYCoordinate(), chord.getValue()});
		chordGraph.setShowModelDistribution(showModelDistribution);
		chordGraph.repaint();
		chordTable.setShowModelDistribution(showModelDistribution);
		chordTable.repaint();
	}
	/**
	* This method runs the experiment one time. A sound is played, depending on the
	* outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote(floor.chordEvent());
	}

	/**
	* This method resets the experiment, including the record table, floor, random variable, random variable
	* graph and table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		floor.setChordDrawn(false);
		chord.reset();
		chordGraph.reset();
		chordTable.reset();
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

