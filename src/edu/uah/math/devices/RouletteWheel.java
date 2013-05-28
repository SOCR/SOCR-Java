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
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import java.io.Serializable;

/**
* This class models the standard American roulette wheel. The wheel has
* 38 slots labeled 0, 00, and 1 to 36. Of the slots labeled 1-26, 18 are red
* and 18 are green. The roulette wheel object is supplied with an image of
* a real roulette wheel.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RouletteWheel extends JComponent implements Serializable{
	//Variables
	private int score;
	private Color ballColor = Color.yellow;
	private final static int[] scoreX = {94, 119, 84, 156, 47, 170, 32, 157, 46, 120, 84, 149,
		55, 130, 73, 162, 41, 172, 32, 41, 163, 33, 170, 56,
		150, 74, 129, 95, 106, 63, 140, 36, 167, 37, 168, 65, 141, 106};
	private final static int[] scoreY = {31, 168, 33, 142, 57, 100, 99, 56, 142, 32, 167, 47,
		151, 163, 36, 132, 65, 87, 111, 132, 65, 88, 110, 46,
		149, 162, 34, 168, 30, 157, 40, 122, 76, 76, 122, 40, 157, 169};
	private final static int[] scores = {0, 2, 14, 35, 23, 4, 16, 33, 21, 6, 18, 31, 19,
		8, 12, 29, 25, 10, 27, 37, 1, 13, 36, 24, 3, 15, 34, 22, 5, 17, 32, 20, 7, 11, 30, 26, 9, 28};
	boolean ballDrawn = false;
	static Image wheelImage;

	/**
	* This default constructor creates a new roulette wheel.
	*/
	public RouletteWheel(){
		setToolTipText("Roulette Wheel");
		ImageIcon icon = new ImageIcon(getClass().getResource("RouletteWheel.gif"));
		wheelImage = icon.getImage();
	}

	/**
	* This method paints the roulette wheel by drawing the image
	* and drawing the ball in the appropriate slot.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(wheelImage, 0, 0, this);
		if (ballDrawn){
			g.setColor(ballColor);
			g.fillOval(scoreX[score], scoreY[score], 10, 10);
		}
	}

	/**
	* This method sets the current score of the roulette wheel (the number of the slot containing the ball).
	* @param s the score
	*/
	public void setScore(int s){
		if (s < 0) s = 0;
		else if (s > 37) s = 37;
		score = s;
	}

	/**
	* This method "spins" the wheel by selecting the score at ranom.
	* @return the random score
	*/
	public int spin(){
		setScore((int)(38 * Math.random()));
		return score;
	}

	/**
	* This method returns the current score.
	* @return the number of the slot containing the ball
	*/
	public int getScore(){
		return score;
	}

	/**
	* This method returns the score corresponding to a given index. The index starts at 0, at the top
	* of the wheel, and progresses counterclockwise.
	* @param i the index
	*/
	public int getScore(int i){
		return scores[i];
	}

	/**
	* This method returns the label of the current score. The label is the
	* same as the score, except that score 37 corresponds to 00.
	* @return the label of the score
	*/
	public String getLabel(){
		if (score == 37) return "00";
		else return String.valueOf(score);
	}

	/**
	* This method returns the x-coordinate of the upper left corner of the slot corresponding to a given
	* score.
	* @param s the score
	*/
	public int getScoreX(int s){
		if (s < 0) s = 0; else if (s > 37) s = 37;
		return scoreX[s];
	}

	/**
	* This method returns the y-coordinate of the upper left corner of the slot
	* corresponding to a given score.
	* @param s the score
	*/
	public int getScoreY(int s){
		if (s < 0) s = 0; else if (s > 37) s = 37;
		return scoreY[s];
	}

	/**
	* This method returns the color of a given score.
	* @param s the score
	* @return the color
	*/
	public Color getScoreColor(int s){
		if (s < 0) s = 0; else if (s > 37) s = 37;
		if (s == 0 | s == 37) return Color.green;
		else{
			int i = getIndex(s);
			if (i == 2 * (i / 2)) return Color.red;
			else return Color.black;
		}
	}

	/**
	* This method returns the color of the current score.
	* @return the color
	*/
	public Color getScoreColor(){
		return getScoreColor(score);
	}

	/**
	* This method sets the boolean state of the ball.
	* @param b true if the ball is drawn
	*/
	public void setBallDrawn(boolean b){
		ballDrawn = b;
		repaint();
	}

	/**
	* This method returns the boolean state of the ball
	* @return true if the ball is drawn
	*/
	public boolean isBallDrawn(){
		return ballDrawn;
	}

	/**
	* This method sets the ball color.
	* @param c the ball color
	*/
	public void setBallColor(Color c){
		ballColor = c;
	}

	/**
	* This method returns the ball color
	* @return the ball color
	*/
	public Color getBallColor(){
		return ballColor;
	}

	/**
	* This method sets the index of the score (starting at 0 and progressing counterclockwise).
	* @param i the index
	*/
	public void setScoreIndex(int i){
		if (i < 0) i = 0; else if (i > 37) i = 37;
		setScore(scores[i]);
	}

	/**
	* This method gets the index corresponding to a given score. The index starts at 0 and progresses
	* counterclockwise.
	* @param s the score
	*/
	public int getIndex(int s){
		int i = 0;
		if (s < 0) s = 0; else if (s > 37) s = 37;
		for (int j = 0; j < 38; j++) if (scores[j] == s) i = j;
		return i;
	}

	/**
	* This method returns the index of the current score.
	* @return the index of the current score
	*/
	public int getScoreIndex(){
		return getIndex(score);
	}


	/**
	* This method specifies the minimum size.
	* @return the 220 by 220 dimension
	*/
	public Dimension getMinimumSize(){
		return new Dimension(220, 220);
	}

	/**
	* This method returns the preferred size.
	* @return the 220 by 220 dimension
	*/
	public Dimension getPreferredSize(){
		return getMinimumSize();
	}

	/**
	* This method returns the preferred size.
	* @return the 220 by 220 dimension
	*/
	public Dimension MaximumSize(){
		return getMinimumSize();
	}

}
