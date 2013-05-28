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
import javax.swing.JComponent;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import java.io.Serializable;
import edu.uah.math.distributions.Functions;

/**
* This class models a forest as a rectangular lattice of trees.  Each tree at any time
* is in one of three states: healthy, on fire, or burnt. If a tree is on fire at time t, then
* any of the four neighboring green trees can catch fire at time t + 1 with specified probabilities.
* Trees catch on fire independently.  A tree on fire at time t is burnt at time t + 1. Once a
* tree is burnt, it remains burnt.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Forest extends JComponent implements MouseListener, MouseMotionListener, Serializable{
	//Constants
	public final static int HEALTHY = 0, ON_FIRE = 1, BURNT = 2;
	public final static int LEFT = 0, RIGHT = 1, DOWN = 2, UP = 3;
	//Variables
	private int rows, columns, treeSize, editState = ON_FIRE;
	private int[][] states;
	private double[] probabilities = new double[4];
	private Color healthyColor = Color.green, onFireColor = Color.red, burntColor = Color.black;
	//Objects
	private Vector<Point> fire = new Vector<Point>(), burnt = new Vector<Point>();

	/**
	* This general constructor creates a new forest with a specified number of rows and
	* columns, specified fire spread probabilities, and a preferred tree size.
	* @param c the number of columns
	* @param r the number of rows
	* @param p the array of fire spread probabilities
	* @param s the preferred tree size
	*/
	public Forest(int c, int r, double[] p, int s){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setParameters(c, r, s);
		setProbabilities(p);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	/**
	* This special constructor creates a new forest with a specified number of rows and
	* columns, specified fire spread probabilities, and with default tree size 5.
	* @param c the number of columns
	* @param r the number of rows
	* @param p the array of probabilities
	*/
	public Forest(int c, int r, double[] p){
		this(c, r, p, 5);
	}

	/**
	* This special constructor creates a new forest with a specified number of rows and
	* columns, fire spread probabilities 0.5, and with default tree size 5.
	* @param c the number of columns
	* @param r the number of rows
	*/
	public Forest(int c, int r){
		this(c, r, new double[]{0.5, 0.5, 0.5, 0.5});
	}


	/**
	* This default constructor creates a new forest with 100 rows and 100 columns,
	* default fire spread probabilities 0.5, and default tree size 5.
	*/
	public Forest(){
		this(100, 100);
	}

	/**
	* This method sets the parameters: the number of rows, the number of columns, and the
	* tree width.
	* @param c the number of columns
	* @param r the number of rows
	* @param s the size of a tree
	*/
	public void setParameters(int c, int r, int s){
		//Assign the parameters
		if (c < 1) c = 1; columns = c;
		if (r < 1) r = 1; rows = r;
		if (s < 1) s = 1; treeSize = s;
		setMinimumSize(new Dimension(columns * treeSize, rows * treeSize));
		reset();
	}

	public void setParameters(int c, int r){
		setParameters(c, r, treeSize);
	}

	/**
	* This method sets the number of columns in the forest lattice.
	* @param c the number of columns
	*/
	public void setColumns(int c){
		setParameters(c, rows);
	}

	/**
	* This method gets the number of columns in the forest lattice.
	* @return the number of columns
	*/
	public int getColumns(){
		return columns;
	}

	/**
	* This method sets the number of rows in the forest lattice.
	* @param r the number of rows
	*/
	public void setRows(int r){
		setParameters(columns, r);
	}

	/**
	* This method gets the number of rows in the forest lattice.
	* @return the number of rows
	*/
	public int getRows(){
		return rows;
	}

	/**
	* This method sets the array of fire spread probabilities. Index 0 is left,
	* index 1 is right, index 2 is down, index 3 is up.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		if (p.length != 4) p = new double[] {0.5, 0.5, 0.5, 0.5};
		else for (int i = 0; i < 4; i++) probabilities[i] = Functions.getProbability(p[i]);
	}

	/**
	* This method sets an individual fire spread probability.
	* @param i the index (0 left, 1 right, 2 down, 3 up).
	* @param p the probability
	*/
	public void setProbabilities(int i, double p){
		probabilities[i] = Functions.getProbability(p);
	}

	/**
	* This method returns the array of fire spread probabilities. Index
	* 0 is left, index 1 is right, index 2 is down, index 3 is up.
	* @return the array of probabilities.
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method gets the fire spread probabilities.
	* @param i direction index (LEFT, RIGHT, DOWN, UP)
	* @return the fire spread probability in the specified direction
	*/
	public double getProbabilities(int i){
		if (i < 0) i = 0; else if (i > 3) i = 3;
		return probabilities[i];
	}

	/**
	* This method sets the tree edit state.  When a tree is clicked on, the state of the
	* tree will change to the specified state.
	* @param s the new state (ON_FIRE, BURNT, HEALTHY)
	*/
	public void setEditState(int s){
		if (s < 0) s  = 0; else if (s > 2) s = 2;
		editState = s;
	}

	/**
	* This method returns the edit state. This is the state that a tree is changed to when
	* clicked.
	* @return the edit state
	*/
	public int getEditState(){
		return editState;
	}

	/**
	* This method sets the state of a specified tree.
	* @param x the x coordinate of the tree
	* @param y the y coordinate of the tree
	* @param s the new state (ON_FIRE, BURNT, HEALTHY)
	*/
	public void setStates(int x, int y, int s){
		Point p = new Point(x,y);
		if (x >= 0 & x < columns & y >= 0 & y < rows){
			int oldState = states[x][y];
			states[x][y] = s;
			if (s == ON_FIRE){
				if (oldState == HEALTHY) fire.addElement(p);
				else if (oldState == BURNT){
					fire.addElement(p);
					burnt.removeElement(p);
				}
			}
			else if (s == BURNT){
				if (oldState == HEALTHY) burnt.addElement(p);
				else if (oldState == ON_FIRE){
					burnt.addElement(p);
					fire.removeElement(p);
				}
			}
		}
	}

	/**
	* This method sets all of the states of the trees.
	* @param s the double array of states
	*/
	public void setStates(int[][] s){
		states = s;
		fire.removeAllElements();
		burnt.removeAllElements();
		Point p;
		for (int i = 0; i < columns; i++)
			for (int j = 0; j < rows; j++){
				if (states[i][j] == ON_FIRE) fire.addElement(new Point(i, j));
				else if (states[i][j] == BURNT) fire.addElement(new Point(i, j));
			}
	}

	/**
	* This method gets the array of states.
	* @return the array of state
	*/
	public int[][] getStates(){
		return states;
	}

	/**
	* This method gets the state of an individual tree.
	* @param x the column number
	* @param y the row number
	* @return the state of the tree
	*/
	public int getStates(int x, int y){
		if (x < 0) x = 0; else if (x > columns - 1) x = columns - 1;
		if (y < 0) y = 0; else if (y > rows - 1) y = rows - 1;
		return states[x][y];
	}

	/**
	* This method sets the tree in the center of the forest on fire.
	*/
	public void setFire(){
		setStates(columns / 2, rows / 2, ON_FIRE);
	}

	/**
	* This method paints the forest.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Paint the forest
		g.setColor(healthyColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		Point p;
		//Paint the trees on fire
		g.setColor(onFireColor);
		for (int i = 0; i < fire.size(); i++){
			p = (Point) fire.elementAt(i);
			g.fillRect(p.x * treeSize, p.y * treeSize, treeSize, treeSize);
		}
		//Paint the burnt trees
		g.setColor(burntColor);
		for (int i = 0; i < burnt.size(); i++){
			p = (Point) burnt.elementAt(i);
			g.fillRect(p.x * treeSize, p.y * treeSize, treeSize, treeSize);
		}
	}

	/**
	* This method lets the forest burn for one time unit.
	*/
	public void burn(){
		int x, y, x1, y1;
		Point p, q;
		Vector f = (Vector) fire.clone();
		for (int i = 0; i < f.size(); i++){
			p = (Point) f.elementAt(i);
			burnt.addElement(p);
			boolean b = fire.removeElement(p);
			x = p.x; y = p.y;
			states[x][y] = BURNT;
			if (x > 0){
				x1 = x - 1;
				if (states[x1][y] == HEALTHY & Math.random() < probabilities[LEFT]){
					states[x1][y] = ON_FIRE;
					fire.addElement(new Point(x1, y));
				}
			}
			if (x < columns - 1){
				x1 = x + 1;
				if (states[x1][y] == HEALTHY & Math.random() < probabilities[RIGHT]){
					states[x1][y] = ON_FIRE;
					fire.addElement(new Point(x1, y));
				}
			}
			if (y > 0){
				y1 = y - 1;
				if (states[x][y1] == HEALTHY & Math.random() < probabilities[UP]){
					states[x][y1] = ON_FIRE;
					fire.addElement(new Point(x, y1));
				}
			}
			if (y < rows - 1){
				y1 = y + 1;
				if (states[x][y1] == HEALTHY & Math.random() < probabilities[DOWN]){
					states[x][y1] = ON_FIRE;
					fire.addElement(new Point(x, y1));
				}
			}
		}
	}

	/**
	* This method resets the forest to a completely green forest.
	*/
	public void reset(){
		fire.removeAllElements();
		burnt.removeAllElements();
		states = new int[columns][rows];
		repaint();
	}

	/**
	* This method returns the number of trees that are on fire.
	* @return the number of trees on fire
	*/
	public int treesOnFire(){
		return fire.size();
	}

	/**
	* This method returns the number of burnt trees.
	* @return the number of burnt trees
	*/
	public int treesBurnt(){
		return burnt.size();
	}

	/**
	* This method returns the number of green trees.
	* @return the number of green trees
	*/
	public int treesHealthy(){
		return rows * columns - fire.size() - burnt.size();
	}

	/**
	* This method handles the mouse click event.The selected tree is set to the
	currently specified state (ON_FIRE, HEALTHY, or BURNT).
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		if (e.getSource() == this){
			int xMouse = e.getX() / treeSize;
			int yMouse = e.getY() / treeSize;
			setStates(xMouse, yMouse, editState);
			repaint();
		}
	}

	/**
	* This method handles the mouse move event. The mouse coordinates are given
	* in the tool tip.
	* @param e the mouse event
	*/
	public void mouseMoved(MouseEvent e){
		if (e.getSource() == this){
			int xMouse = e.getX() / treeSize;
			int yMouse = e.getY() / treeSize;
			if (xMouse < columns & yMouse < rows) setToolTipText("(" + xMouse + ", " + yMouse + ")");
			else setToolTipText("Forest");
		}
	}

	/**
	* This method sets the color of the healty trees.
	* @param c the color of healthy trees
	*/
	public void setHealthyColor(Color c){
		healthyColor = c;
	}

	/**
	* This method returns the color of healthy trees
	* @return the color of healthy trees
	*/
	public Color getHealthyColor(){
		return healthyColor;
	}

	/**
	* This method sets the color of the on fire trees.
	* @param c the color of on fire trees
	*/
	public void setOnFireColor(Color c){
		onFireColor = c;
	}

	/**
	* This method returns the color of on fire trees
	* @return the color of on fire trees
	*/
	public Color getOnFireColor(){
		return onFireColor;
	}

	/**
	* This method sets the color of the burnt trees.
	* @param c the color of burnt trees
	*/
	public void setBurntColor(Color c){
		burntColor = c;
	}

	/**
	* This method returns the color of burnt trees
	* @return the color of burnt trees
	*/
	public Color getBurntColor(){
		return burntColor;
	}

	//These mouse events are not handled.
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
}