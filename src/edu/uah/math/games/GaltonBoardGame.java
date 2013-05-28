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
import java.io.Serializable;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.GaltonBoard;
import edu.uah.math.distributions.Functions;

/**
* This class models a simple Galton board game.  The user clicks on buttons to move right or left.
* The applet shows the path through the Galton board, the corresponding bit string, and the corresponding
* subset.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class GaltonBoardGame extends Game implements Serializable{
	private int row, column;
	private String bitString, subset;
	//Object
	private RecordTable recordTable = new RecordTable(new String[] {"Row", "Column", "Bit", "Subset"});
	private JButton leftButton = new JButton();
	private JButton rightButton = new JButton();
	private GaltonBoard galtonBoard = new GaltonBoard(25);

	/**
	* This method initializes the experiment, including the buttons for moving the ball,
	* the Galton board, and the record table.
	*/
	public void init(){
		super.init();
		setName("Galton Board Game");
		//Left button
		leftButton.addActionListener(this);
		leftButton.setToolTipText("Move left");
		leftButton.setIcon(new ImageIcon(getClass().getResource("left.gif")));
		//Right button
		rightButton.addActionListener(this);
		rightButton.setToolTipText("Move right");
		rightButton.setIcon(new ImageIcon(getClass().getResource("right.gif")));
		//Toolbar
		addTool(leftButton);
		addTool(rightButton);
		//Galton board
		galtonBoard.setMinimumSize(new Dimension(200, 200));
		addComponent(galtonBoard, 0, 0, 1, 1);
		//Record table
		recordTable.setDescription("bit 0: move left, bit 1: move right");
		addComponent(recordTable, 0, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "Click the left arrow button to move the ball to the left and the right arrow button to move\n"
			+ "the ball to the right. The bit string is recorded in the table below, as well as the\n"
			+ "corresponding subset. Finally, the last table entry gives the current ball location and\n"
			+ "the corresponding binomial coefficient.";

	}

	/**
	* This method handles the actions associated with the left and right buttons.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		row++;
		if (row <= 15){
			if (e.getSource() == leftButton){
				galtonBoard.moveBall(0);
				//playnote(0);
				recordTable.addRecord(new double[]{row, column, 0, Double.NaN});
			}
			else if (e.getSource() == rightButton){
				column++;
				galtonBoard.moveBall(1);
				//playnote(1);
				recordTable.addRecord(new double[]{row, column, 1, row});
			}
			else super.actionPerformed(e);
		}
	}

	/**
	* This method resets the experiment, including the Galton board and
	* the record table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		galtonBoard.reset();
		row = 0; column = 0;
	}
}

