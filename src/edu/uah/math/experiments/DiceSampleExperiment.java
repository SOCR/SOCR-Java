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
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Die;
import edu.uah.math.devices.DiceBoard;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.DieProbabilityDialog;

/**
* This class models a simple experiment that consists of rolling n dice, thus sampling
* from the underlying die distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DiceSampleExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 2, trial;
	private double[] record;
	//Objects
	private RecordTable recordTable = new RecordTable();
	private JButton dieButton = new JButton();
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private DiceBoard diceBoard = new DiceBoard(n);
	private Parameter nScroll = new Parameter(1, 60, 1, n, "Number of dice", "n");
	private DieProbabilityDialog dieProbabilityDialog;
	private Frame frame;
	private Timer timer = new Timer(100, this);

	/**
	* This method initializes the experiment, including the record table, die probability
	* dialog button, toolbar, slider for the number of dice, and the dice board.
	*/
	public void init(){
		super.init();
		setName("Dice Sample Experiment");
		//Die button
		dieButton.addActionListener(this);
		dieButton.setToolTipText("Die Probabilities");
		dieButton.setIcon(new ImageIcon(getClass().getResource("redDie.gif")));
		//n scroll
		nScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(dieButton);
		toolBar.add(nScroll);
		addToolBar(toolBar);
		//Graphs
		diceBoard.setMinimumSize(new Dimension(200, 100));
		addComponent(diceBoard, 0, 0, 1, 1);
		//Record Table
		recordTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		recordTable.setDescription("X(i): score of die i");
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
			+ "The experiment consists of rolling n dice, each governed by the same probability distribution.\n"
			+ "You can specify the die distribution by clicking on the die probability button;\n"
			+ "this button bring up the die probability dialog box. You can define your own distribution\n"
			+ "by typing probabilities into the text fields of the dialog box, or you can click on one \n"
			+ "of the buttons in the dialog box to specify one of the following special distributions:\n"
			+ "fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, or skewed right. The number of dice\n"
			+ "can be varied with a scroll bar. The scores of each die are recorded in the table.";
	}

	/**
	* This method defines the experiment
	*/
	public void doExperiment(){
		super.doExperiment();
		diceBoard.roll();
	}

	/**
	* This method runs the experiment one time, with additional annimation and sound.
	*/
	public void step(){
		stop();
		diceBoard.setRolled(false);
		record = new double[n + 1];
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
	* This method stops the step process, if necessary, and then resets the experiment
	* by resetting the record table and the dice board.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		String[] variables = new String[n + 1];
		variables[0] = "Run";
		for (int i = 0; i < n; i++) variables[i + 1] = "X" + (i + 1);
		recordTable.setVariableNames(variables);
		diceBoard.setRolled(false);
	}

	/**
	* This method update the display, including the dice board and the record table.
	*/
	public void update(){
		super.update();
		record = new double[n + 1];
		record[0] = getTime();
		for (int i = 0; i < n; i++) record[i + 1] = diceBoard.getValues(i);
		recordTable.addRecord(record);
		diceBoard.setRolled(true);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == dieButton){
			frame = new Frame();
			dieProbabilityDialog = new DieProbabilityDialog(frame);
			Point fp = new Point(100,100);
			Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
			Point dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			dieProbabilityDialog.setLocation(dp);
			dieProbabilityDialog.setProbabilities(diceBoard.getProbabilities());
			dieProbabilityDialog.setVisible(true);
			if (dieProbabilityDialog.isOK()){
				diceBoard.setProbabilities(dieProbabilityDialog.getProbabilities());
				reset();
			}
		}
		else if (e.getSource() == timer){
			if (trial < n){
				Die die = diceBoard.getDie(trial);
				die.roll();
				die.setRolled(true);
				//playNote(die.getValue());
				record[trial + 1] = die.getValue();
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
	}

	/**JSlider events: select the number of dice*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			diceBoard.setDieCount(n);
			reset();
		}
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

