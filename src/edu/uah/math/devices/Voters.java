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
package edu.uah.math.devices;
import java.util.Vector;
import java.io.Serializable;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import edu.uah.math.distributions.Functions;

/**
* This class models a rectangular array of voters (a metaphor for a site that can be
* in one of several states. Each voter at each time can be in one of a finite set of
* states representd as colors. The voters are actually represented as colored squares.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class Voters extends JComponent implements MouseListener, MouseMotionListener, Serializable{
	//Constants
	private final static int LEFT = 0, RIGHT = 1, DOWN = 2, UP = 3;
	//Variables
	private int rows, columns, voterSize = 5, statesAlive, editState = 0, totalStates,
		voterX, voterY, neighborX, neighborY, newState;
	private int[] stateCount = new int[10];
	private int[][] states;
	private boolean newDeath, consensus;
	private double[] probabilities = new double[]{0.25, 0.25, 0.25, 0.25};
	private Color[] colors;

	/**
	* This general constructor creates a new array of voters with a specified number
	* of rows and columns, a specified array of colors (states), a specified probability
	* array, and a preferred voter size.
	* @param m the number of columns
	* @param n the number of rows
	* @param s the size of a voter
	* @param c the array of colors (states)
	* @param p the array of probabilities
	*/
	public Voters(int m, int n, int s, Color[] c, double[] p){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setParameters(m, n, s);
		setColors(c);
		setProbabilities(p);
	}

	/**
	* This special constructor creates a new array of voters with a specified number
	* of rows and columns, a specified array of colors (states), a specified size,
	* a specified color array and default probabilities.
	* @param m the number of columns
	* @param n the number of rows
	* @param s the size of a voter
	* @param c the array of colors (states)
	*/
	public Voters(int m, int n, int s, Color[] c){
		this(m, n, s, c, new double[]{0.25, 0.25, 0.25, 0.25});
	}

	/**
	* This special constructor creates a new array of voters with a specified number
	* of rows and columns, a specified size, and default colors and probabilities..
	* @param m the number of columns
	* @param n the number of rows
	* @param s the size of a voter
	*/
	public Voters(int m, int n, int s){
		this(m, n, s, new Color[] {Color.black, Color.blue, Color.cyan, Color.green,
			Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow});
	}

	/**
	* This special constructor creates a new array of voters with a specified number
	* of rows and columns, and default voter size, colors and probabilities.
	* @param m the number of columns
	* @param n the number of rows
	*/
	public Voters(int m, int n){
		this(m, n, 20);
	}

	/**
	* This default constructor creates a new array of voters with 10 rows, 10 columns
	* and the default uniform probability array, and default preferred voter size.
	*/
	public Voters(){
		this(10, 5);
	}

	/**
	* This method sets the parameters: the number of rows and columns, and the array of
	* states (colors).
	* @param m the number of columns
	* @param n the number of rows
	* @param s the voter size
	*/
	public void setParameters(int m, int n, int s){
		if (m < 1) m = 1; columns = m;
		if (n < 1) n = 1; rows = n;
		if (s < 1) s = 5; voterSize = s;
		setMinimumSize(new Dimension(columns * voterSize, rows * voterSize));
		reset();
	}

	/**
	* This method sets the parameters: the number of rows and columns.
	* @param m the number of columns
	* @param n the number of rows
	*/
	public void setParameters(int m, int n){
		setParameters(m, n, voterSize);
	}


	/**
	* This method sets the number of columns.
	* @param m the number of columns
	*/
	public void setColumns(int m){
		setParameters(m, rows);
	}

	/**
	* This method returns the number of columns.
	* @return the number of columns
	*/
	public int getColumns(){
		return columns;
	}

	/**
	* This method sets the number of rows.
	* @param n the number of rows
	*/
	public void setRows(int n){
		setParameters(columns, n);
	}

	/**
	* This method returns the number of rows.
	* @return the number of rows
	*/
	public int getRows(){
		return rows;
	}

	/**
	* This method sets the array of color (states).
	* @param c the array of colors
	*/
	public void setColors(Color[] c){
		colors = c;
		totalStates = colors.length;
	}

	/**
	* This method returns the array of colors (states).
	* @return the array of colors
	*/
	public Color[] getColors(){
		return colors;
	}

	/**
	* This method returns a particular state (color).
	* @param i the index
	* @return the color corresponding to the index
	*/
	public Color getColors(int i){
		if (i < 0) i = 0; else if (i > totalStates - 1) i = totalStates - 1;
		return colors[i];
	}

	/**
	* This method sets the probabilities for selecting a neighbor. Index 0 is the
	* left neighbor, index 1 the right neigbor, index 2 the bottom neighbor,
	* and index 3 the top neighbor.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		probabilities = Functions.getProbabilities(p);
	}

	/**
	* This method sets and individual probability for selecting a neighbor. Index 0 is the
	* left neighbor, index 1 the right neigbor, index 2 the bottom neighbor,
	* and index 3 the top neighbor.
	*/
	public void setProbabilities(int i, double p){
		if (i < 0) i = 0; else if (i > 3) i = 3;
		probabilities[i] = p;
		probabilities = Functions.getProbabilities(probabilities);
	}

	/**
	* This method gets the array of probabilities.
	* @return the array of probabilities for choosing the neighbor.
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method returns an individual probability. Index 0 is the
	* left neighbor, index 1 the right neigbor, index 2 the bottom neighbor,
	* and index 3 the top neighbor.
	* @param i the index
	* @return the probability corresponding to the index.
	*/
	public double getProbabilites(int i){
		if (i < 0) i = 0; else if (i > 3) i = 3;
		return probabilities[i];
	}

	/**
	* This method sets the edit state, the state that a voter will be changed to when
	* a user clicks on the voter.
	* @param s the new state
	*/
	public void setEditState(int s){
		editState = s;
	}

	/**
	* This method returns the edit state, the state that a voter will be changed to
	* when a user clicks on the voter.
	* @return the edit state
	*/
	public int getEditState(){
		return editState;
	}

	/**
	* This method changes the state of a specified voter to a specified new state.
	* @param x the x-coordinate of the voter
	* @param y the y-coordiante of the voter
	* @param s the new state
	*/
	public void setStates(int x, int y, int s){
		if (x < 0) x = 0; else if (x > columns - 1) x = columns - 1;
		if (y < 0) y = 0; else if (y > rows - 1) y = rows - 1;
		int currentState = states[x][y];
		//Update state counts
		stateCount[currentState]--;
		stateCount[s]++;
		//Change the state
		states[x][y] = s;
		//Determine if a state has died, and if so, update the number of states still alive.
		if (stateCount[currentState] == 0){
			newDeath = true;
			statesAlive--;
			if (statesAlive == 1) consensus = true;
		}
	}

	/**
	* This method sets the array of states.
	* @param s the array of states
	*/
	public void setStates(int[][] s){
		states = s;
		for (int i = 0; i < totalStates; i++) stateCount[i] = 0;
		for (int i = 0; i < columns; i++)
			for (int j = 0; j < rows; j++) stateCount[states[i][j]]++;
		statesAlive = 0;
		for (int i = 0; i < totalStates; i++) if (stateCount[i] > 0) statesAlive++;
		if (statesAlive == 1) consensus = true;
	}

	/**
	* This method returns the state of a specified voter.
	* @param x the x-coordinate of the voter
	* @param y the y-coordinate of the voter
	* @return the state of the voter
	*/
	public int getStates(int x, int y){
		return states[x][y];
	}

	/**
	* This method returns the array of states.
	* @return the array of states
	*/
	public int[][] getStates(){
		return states;
	}

	/**
	* This method paints the voter canvas. Each square representing a voter
	* is painted with the color representing the state of the voter.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for (int x = 0; x < columns; x++){
			for (int y = 0; y < rows; y++){
				g.setColor(colors[states[x][y]]);
				g.fillRect(x * voterSize, y * voterSize, voterSize, voterSize);
			}
		}
	}

	/**
	* This method resets the voters. Each voter is assigned a random state.
	*/
	public void reset(){
		int s;
		states = new int[columns][rows];
		//Clear the state counts
		for (int i = 0; i < totalStates; i++) stateCount[i] = 0;
		//Assign each voter a random state and determine the new state counts.
		for (int x = 0; x < columns; x++){
			for (int y = 0; y < rows; y++){
				s = (int)(totalStates * Math.random());
				states[x][y] = s;
				stateCount[s]++;
			}
		//Determine how many states exist.
		}
		statesAlive = 0;
		for (int i = 0; i < totalStates; i++) if (stateCount[i] > 0) statesAlive++;
		consensus = (statesAlive == 1);
		repaint();
	}

	/**
	* This method returns the number of voters in a specified state.
	* @param i the state
	* @return the number of voters in that state
	*/
	public int getStateCount(int i){
		return stateCount[i];
	}

	/**
	* This method performs the voter experiment. A voter is chosen at random and
	* then a neighbor of that voter is chosen according to the probability parameters.
	* The state of the first voter is changed to that of the neighbor.
	*/
	public void doExperiment(){
		double r;
		newDeath = false;
		//Select a random vater
		voterX = (int)(columns * Math.random());
		voterY = (int)(rows * Math.random());
		//Select a random neighbor of the voter.
		r = Math.random();
		if (r < probabilities[LEFT]){
			neighborX = voterX - 1;
			if (neighborX < 0) neighborX = columns - 1;
			neighborY = voterY;
		}
		else if (r < probabilities[LEFT] + probabilities[RIGHT]){
			neighborX = voterX + 1;
			if (neighborX >= columns) neighborX = 0;
			neighborY = voterY;
		}
		else if (r < probabilities[LEFT] + probabilities[RIGHT] + probabilities[DOWN]){
			neighborX = voterX;
			neighborY = voterY + 1;
			if (neighborY >= rows) neighborY = 0;
		}
		else {
			neighborX = voterX;
			neighborY = voterY - 1;
			if (neighborY < 0) neighborY = rows - 1;
		}
		//Change the state of the voter to the state of the neighbor.
		newState = states[neighborX][neighborY];
		setStates(voterX, voterY, newState);
	}

	/**
	* This method returns the number of states alive (that is, at least one voter
	* has this state.
	* @return the number of nonempty states
	*/
	public int getStatesAlive(){
		return statesAlive;
	}

	/**
	* This method checks to see if a state has died.
	* @return true if the last run of the experiment caused a new state to die
	*/
	public boolean isNewDeath(){
		return newDeath;
	}

	/**
	* This method checks to see if the voters are all in the same state.
	* @return true if all voters are in the same state
	*/
	public boolean isConsensus(){
		return consensus;
	}

	/**
	* This method changes the state of the voter clicked on to the current edit state.
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		int xMouse = e.getX() / voterSize;
		int yMouse = e.getY() / voterSize;
		if (xMouse < columns & yMouse < rows) setStates(xMouse, yMouse, editState);
		repaint();
	}

	/**
	* This method returns the currently selected voter.
	* @return the voter
	*/
	public Point getVoter(){
		return new Point(voterX, voterY);
	}

	/**
	* This method returns the new state.
	*/
	public int getNewState(){
		return newState;
	}

	/**
	* This emthod returns the currently selected neighbor.
	* @return the neighbor
	*/
	public Point getNeighbor(){
		return new Point(neighborX, neighborY);
	}

	/**
	* This method tracks the voter under the mouse cursor.
	* @param e the mouse event
	*/
	public void mouseMoved(MouseEvent e){
		int xMouse = e.getX() / voterSize;
		int yMouse = e.getY() / voterSize;
		if (xMouse < columns & yMouse < rows) setToolTipText("x = " + xMouse + ", y = " + yMouse);
		else setToolTipText("Voter array");
	}

	//These events are not handled
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
}