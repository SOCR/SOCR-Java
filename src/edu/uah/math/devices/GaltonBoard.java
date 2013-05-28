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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

/**
* This class is a simple model of the classical Galton board. A ball falls through
* a triangular array of pegs. At each peg, the ball may bounce to the right or to the
* left. This class can be used to illustrate Bernoulli trials and various combinatorial
* formulas.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class GaltonBoard extends Graph implements MouseMotionListener, Serializable{
	//Variables
	private int rows, ballRow = 0, ballColumn = 0;
	private double probability, radius = 0.4;
	private boolean pathDrawn;
	private int[] path;
	private String bitString, subset;
	private Color ballColor, pegColor;

	/**
	* This general constructor creates a new Galton board with a specified number for the last row,
	* a specified probability,  and specified ball and peg colors. The rows are numbered from 0 to
	* the last row. The pegs in row j are numbered from 0 to j. The probability governs a step to the right.
	* @param n the number of the last ballRow
	* @param p the probabiltiy of a step to the right
	* @param pc the peg color
	* @param bc the ball color
	*/
	public GaltonBoard(int n, double p, Color pc, Color bc){
		addMouseMotionListener(this);
		setRows(n);
		setMargins(10, 10, 10, 10);
		setProbability(p);
		setColors(pc, bc);
 	}

	/**
	* This special constructor creates a new Galton board with a specified
	* number of rows, a specified probability,  and with default peg color blue and ball color red.
	* @param n the number of rows.
	* @param p the probability of a step to the right
	*/
	public GaltonBoard(int n, double p){
		this(n, p,Color.blue, Color.red);
	}

	/**
	* This special constructor creates a new Galton board with a specified number of rows,
	* and with default probability 0.5, and default peg color blue and ball color red.
	* @param n the number of rows
	*/
	public GaltonBoard(int n){
		this(n, 0.5);
	}

	/**
	* This default constructor creates a new Galton board with 10 rows and with the dafult probabilities and colors.
	*/
	public GaltonBoard(){
		this(10);
	}

	/**
	* This method sets the number of rows in the Galton board. For drawing purposes, the Galton board is given
	* a coordinate system where x varies from -n to n and y from -n to 0.
	* @param n the number of rows
	*/
	public void setRows(int n){
		if (n < 0) n = 0;
		rows = n;
		setScale(-rows - radius, rows + radius, -rows - radius, 3 * radius);
		reset();
	}


	/**
	* This method returns the number of rows.
	* @return the number of rows
	*/
	public int getRows(){
	return rows;
	}

	/**
	* This method sets the probability of a step to the right.
	* @param p the probability
	*/
	public void setProbability(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1;
		probability = p;
	}

	/**
	* This method returns the probability of a step to the right.
	* @return the probability
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method sets the radius of the pegs and the ball. The admissiable value are 0 < r <= 0.5
	* @param r the radius
	*/
	public void setRadius(double r){
		if (r <= 0 | r > 0.5) r = 0.4;
		radius = r;
		repaint();
	}

	/**
	* This method returns the radius of the pegs and the ball.
	* @return the radius
	*/
	public double getRadius(){
		return radius;
	}

	/**
	* This method draws the Galton board.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		int x, y;
		super.paintComponent(g);
		//Draw pegs
		g.setColor(pegColor);
		for (int j = 0; j <= rows; j++) for (int i = -j; i <= j; i = i + 2)  fillCircle(g, i, -j, radius);
		//Draw data
		g.setColor(ballColor);
		//Draw path if necessary
		if (pathDrawn){
			x = 0; y = 0; fillCircle(g, x, y + 2 * radius, radius);
			for (int j = 0; j < rows; j++){
				y = y - 1;
				x = x + 2 * path[j] - 1;
				fillCircle(g, x, y + 2 * radius, radius);
			}
		}
		//Draw current ball position
		fillCircle(g, -ballRow + 2 * ballColumn, -ballRow + 2 * radius, radius);
	}

	/**
	* This method sets the path of the ball through the Galton board.
	* @param p the array of 0's (left moves) and 1's (right moves)
	*/
	public void setPath(int[] p){
		if (p.length == rows) path = p;
		//Compute the final position
		ballRow = rows;
		ballColumn = 0;
		for (int i = 0; i < rows; i++) ballColumn = ballColumn + path[i];
	}

	/**
	* This method sets the path of the ball through the Galton board
	* randomly, according to the probability.
	*/
	public void setPath(){
		int[] p = new int[rows];
		for (int i = 0; i < rows; i++) if (Math.random() < probability) p[i] = 1;
		setPath(p);
	}

	/**
	* This method moves the ball to a specified position on the next row.
	* @param i 1 for a move right, 0 for a move left
	*/
	public void moveBall(int i){
		if (i < 0) i = 0; else if (i > 1) i = 1;
		if (ballRow < rows){
			path[ballRow] = i;
			ballRow = ballRow + 1;
			ballColumn = ballColumn + i;
			repaint();
		}
	}

	/**
	* This method moves the ball randomly to the next row, according to the probability.
	*/
	public int moveBall(){
		int i;
		if (Math.random() < probability) i = 1; else i = 0;
		moveBall(i);
		return i;
	}

	/**
	* This method returns the column number of the ball (an integer between 0 and the row number of the ball).
	* @return the column number
	*/
	public int getBallColumn(){
		return ballColumn;
	}

	/**
	* This method returns the row number of the ball (an integer between 0 and the number of rows);
	* @return the row number
	*/
	public int getBallRow(){
		return ballRow;
	}

	/**
	* This method draws the ball at a specified peg.
	* @param x the x-coordinate of the peg
	* @param y the y-coordinate of the peg
	*/
	public void drawBall(int x, int y){
		Graphics g = getGraphics();
		g.setColor(ballColor);
		fillCircle(g, x, y + 2 * radius, radius);
	}

	/**
	* This method returns the bit string that corresponds to the path of
	* the ball through the Galton board, up to the current ball location.
	* @return the string of 0's and 1's
	*/
	public String getBitString(){
		String bitString = "";
		for (int i = 0; i < ballRow; i++) bitString = bitString + path[i];
		return bitString;
	}

	/**
	* This method returns the subset of {1, 2, ..., n} (where n is the number of rows) that corresponds to the
	* path of the ball through the Galton board, up to the current ball location.
	* @return the subset
	*/
	public String getSubset(){
		String subset = "";
		for (int i = 0; i < ballRow; i++) if (path[i] == 1) {
			if (subset.equals("")) subset = subset + (i + 1);
			else subset = subset + "," + (i + 1);
		}
		if (subset.equals("")) return "empty";
		else return "{" + subset + "}";
	}

	/**
	* This method resets the Galton board, including the path, bit string,
	* and subset.
	*/
	public void reset(){
		ballRow = 0;
		ballColumn = 0;
		path = new int[rows];
		pathDrawn = false;
		repaint();
	}

	/**
	* This method handles the mouse move event. The peg coordinates are given
	* in the tool tip.
	* @param e the mouse event
	*/
	public void mouseMoved(MouseEvent e){
		if (e.getSource() == this){
			int mouseX = (int)Math.rint(getXScale(e.getX())), mouseY = (int)Math.rint(getYScale(e.getY()));
			int mouseRow = -mouseY, mouseColumn = (mouseX - mouseY) / 2;
			if (0 <= mouseRow & mouseRow <= rows & 0 <= mouseColumn & mouseColumn <= mouseRow)
				setToolTipText("row " + mouseRow + ", column " + mouseColumn);
			else setToolTipText("Galton board");
		}
	}

	/**
	* This method sets the colors.
	* @param pc the peg color
	* @param bc the ball color
	*/
	public void setColors(Color pc, Color bc){
		pegColor = pc;
		ballColor = bc;
	}

	/**
	* This method sets the peg color.
	* @param c the peg color
	*/
	public void setPegColor(Color c){
		pegColor = c;
	}

	/**
	* This method returns the peg color.
	* @return the peg color
	*/
	public Color getPegColor(){
		return pegColor;
	}

	/**
	* This method sets the ball color.
	* @param c the ball color
	*/
	public void setBallColor(Color c){
		ballColor = c;
	}

	/**
	* This method returns the ball color.
	* @return the ball color
	*/
	public Color getBallColor(){
		return ballColor;
	}

	/**
	* This method sets the state of the path drawn boolean variable.
	* @param b true if the path of the ball is drawn
	*/
	public void setPathDrawn(boolean b){
		pathDrawn = b;
	}

	/**
	* This method returns the path drawn boolean variable.
	* @return true if the path of the ball is drawn
	*/
	public boolean isPathDrawn(){
		return pathDrawn;
	}

	//These mouse events are not handled.
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
}

