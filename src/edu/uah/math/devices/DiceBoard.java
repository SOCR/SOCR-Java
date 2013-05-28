/*Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
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
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;
import java.awt.Color;
import java.io.Serializable;

/**
* This class is a container that holds dice. The number of dice can be varied,
* and the probabilities, back color, and spot color can be set for all of the
* dice at once. An individual die can be obtained so that the properties of
* that die can be set or returned.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DiceBoard extends JPanel implements Serializable{
	private int dieCount, size;
	private boolean rolled;
	private double[] probabilities = new double[6];
	private Color spotColor, backColor;
	public final static int FAIR = 0, FLAT16 = 1, FLAT25 = 2, FLAT34 = 3, LEFT = 4, RIGHT = 5;

	/**
	* This general constructor creates a new dice board with a specified number of dice,
	* a specified preferred size, specified probability distribution, and specified colors.
	* @param n the number of dice
	* @param p the probability distribution that governs each die
	* @param s the preferred size
	* @param bc the back color of each die
	* @param sc the spot color of each die
	*/
	public DiceBoard(int n, double[] p, int s, Color bc, Color sc){
		if (n < 1) n = 1;
		dieCount = n;
		if (s < 10) s = 10;
		size = s;
		spotColor = sc;
		backColor = bc;
		Die die;
		setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setToolTipText("Dice Board");
		for (int i = 0; i < dieCount; i++){
			die = new Die(p, size, backColor, spotColor);
			die.setToolTipText("Die " + (i + 1));
			add(die);
		}
		probabilities = ((Die)getComponent(0)).getProbabilities();
	}

	/**
	* This special constructor creates a new dice board with a specified number of dice,
	* size, probability distribution, and with default back color red and default
	* spot color white.
	* @param n the number of dice
	* @param p the probability distribution that governs each die
	* @param s the preferred size
	*/
	public DiceBoard(int n, double[] p, int s){
		this(n, p, s, Color.red, Color.white);
	}

	/**
	* This special constructor creates a new dice board with a specified number of dice,
	* a spcified probability distribution, and with default size 36, back color red and
	* spot color white.
	* @param n the number of dice
	* @param p the probability distribution that governs each die
	*/
	public DiceBoard(int n, double[] p){
		this(n, p, 36);
	}

	/**
	* This general constructor creates a new dice board with a specified number of dice, a
	* specified probability distribution of special type, and with specified size and
	* colors.
	* @param n the number of dice
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, or skewed right)
	* @param s the preferred size of the dice
	* @param bc the back color
	* @param sc the spot color
	*/
	public DiceBoard(int n, int t, int s, Color bc, Color sc){
		if (n < 1) n = 1;
		dieCount = n;
		if (s < 10) s = 10;
		size = s;
		spotColor = sc;
		backColor = bc;
		Die die;
		setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setToolTipText("Dice Board");
		for (int i = 0; i < dieCount; i++){
			die = new Die(t, size, backColor, spotColor);
			die.setToolTipText("Die " + (i + 1));
			add(die);
		}
		probabilities = ((Die)getComponent(0)).getProbabilities();
	}

	/**
	* This sepcial constructor creates a new dice board with a specified number of dice,
	* of a special type, a specified size, and with default colors.
	* @param n the number of dice
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, or skewed right)
	* @param s the preferred size
	*/
	public DiceBoard(int n, int t, int s){
		this(n, t, s, Color.red, Color.white);
	}

	/**
	* This sepcial constructor creates a new dice board with a specified number of dice,
	* of a special type, a specified size, and with size and colors.
	* @param n the number of dice
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, or skewed right)
	*/
	public DiceBoard(int n, int t){
		this(n, t, 36, Color.red, Color.white);
	}

	/**
	* This sepcial constructor creates a new dice board with a specified number of fair dice, and with
	* default size and colors.
	* @param n the number of dice
	*/
	public DiceBoard(int n){
		this(n, FAIR);
	}

	/**
	* This default constructor cretes a new dice board with 10 fair dice, and with
	* default size and colors.
	*/
	public DiceBoard(){
		this(10);
	}

	/**
	* This method gets the number of dice.
	* @return the number of dice
	*/
	public int getDieCount(){
		return dieCount;
	}

	/**
	* This method sets the number of dice. If the new die count is greater than the
	* old, an appropriate number of new dice are added. If the new die count is
	* smaller than the old, an appropriate number of dice are removed. The die count
	* updated.
	* @param n the number of dice in the dice board
	*/
	public void setDieCount(int n){
		Die die;
		if (n < 1) n = 1;
		if (n > dieCount){
			for (int i = dieCount; i < n; i++){
				die = new Die(probabilities, size, backColor, spotColor);
				die.setToolTipText("Die " + (i + 1));
				add(die);
			}
			dieCount = n;
		}
		else if (n < dieCount){
			for (int i = n; i < dieCount; i++) remove(n);
			dieCount = n;
		}
		validate();
	}


	/**
	* This method returns a specified die.
	* @param i the index of the die
	* @return the die corresponding to the index.
	*/
	public Die getDie(int i){
		if (i < 0) i = 0; else if (i > dieCount - 1) i = dieCount - 1;
		return (Die)getComponent(i);
	}

	/**
	* This method sets the probability distribution for all of the dice.
	* @param p the array of probabilities.
	*/
	public void setProbabilities(double[] p){
		for (int i = 0; i < dieCount; i++) ((Die)getComponent(i)).setProbabilities(p);
		probabilities = ((Die)getComponent(0)).getProbabilities();
	}

	/**
	* This method sets the probability distribution of all of the dice to a special type.
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, or skewed right)
	*/
	public void setSpecialProbabilities(int t){
		for (int i = 0; i < dieCount; i++) ((Die)getComponent(i)).setSpecialProbabilities(t);
		probabilities = ((Die)getComponent(0)).getProbabilities();
	}

	/**
	* This method returns the probability distribution. This is the common distribution
	* unless some of the dice distributions have been changed individually.
	* @return the array of probabilities
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method returns an individual probability in the common probability distribution.
	* @param i the index
	* @return the probability corresponding to the index
	*/
	public double getProbabilities(int i){
		if (i < 0) i = 0;
		else if (i > dieCount - 1) i = dieCount - 1;
		return probabilities[i];
	}

	/**
	* This method sets the colors of all of the dice.
	* @param bc the back color
	* @param sc the spot color
	*/
	public void setColors(Color bc, Color sc){
		for (int i = 0; i < dieCount; i++) ((Die)getComponent(i)).setColors(bc, sc);
		backColor = bc;
		spotColor = sc;
	}

	/**
	* This method sets the back color for all of the dice.
	* @param c the new back color
	*/
	public void setBackColor(Color c){
		setColors(c, spotColor);
	}

	/**
	* This method returns the common back color.
	* @return the back color
	*/
	public Color getBackColor(){
		return backColor;
	}

	/**
	* This method sets the spot color for all of the dice.
	* @param c the new spot color
	*/
	public void setSpotColor(Color c){
		setColors(backColor, c);
	}

	/**
	* This method returns the common spot color
	* @return the spot color
	*/
	public Color getSpotColor(){
		return spotColor;
	}

	/**
	* This method rolls the dice.
	*/
	public void roll(){
		for (int i = 0; i < dieCount; i++) ((Die)getComponent(i)).roll();
	}

	/**
	* This method returns the sum of the scores of the first n dice.
	* @return the sum of the scores
	*/
	public int getSum(){
		int sum = 0;
		for (int i = 0; i < dieCount; i++) sum = sum + ((Die)getComponent(i)).getValue();
		return sum;
	}

	/**
	* This method returns the count for a given score for a specified number of dice.
	* @param x the die score (1 to 6)
	* @return the number of dice with score x.
	*/
	public int getCount(int x){
		if (x < 1) x = 1; else if (x > 6) x = 6;
		int count = 0;
		for (int i = 0; i < dieCount; i++) if (((Die)getComponent(i)).getValue() == x) count++;
		return count;
	}

	/**
	* This method sets the state of the dice (rolled or not rolled).
	* If rolled, the dice are shown with the specified back color and spot color.
	* If not rolled, the dice are shown in outline in the background color.
	* @param b the state of the dice (rolled or not rolled)
	*/
	public void setRolled(boolean b){
		rolled = b;
		for (int i = 0; i < dieCount; i++) getDie(i).setRolled(rolled);
		repaint();
	}

	/**
	* This method returns the state of the dice (rolled or not rolled).
	* If rolled, the dice are shown with the specified back color and spot color.
	* If not rolled, an outline of each die is shown in the background color.
	* @return the state of the die (rolled or not rolled)
	*/
	public boolean isRolled(){
		return rolled;
	}

	/**
	* This method sets the array of values.
	* @param v the array of values
	*/
	public void setValues(int[] v){
		if (v.length == dieCount)
			for (int i = 0; i < dieCount; i++) getDie(i).setValue(v[i]);
	}

	/**
	* This method returns the array of values.
	* @return the array of values
	*/
	public int[] getValues(){
		int[] v = new int[dieCount];
		for (int i = 0; i < dieCount; i++) v[i] = getDie(i).getValue();
		return v;
	}

	/**
	* This method sets the value of an individual coin.
	* @param i the index
	* @param x the value of the coin
	*/
	public void setValues(int i, int x){
		if (i < 0) i = 0; else if (i > dieCount - 1) i = dieCount - 1;
		getDie(i).setValue(x);
	}

	/**
	* This method returns the value of an individual coin
	* @param i the index
	* @return the value of the coin with the index
	*/
	public int getValues(int i){
		if (i < 0) i = 0; else if (i > dieCount - 1) i = dieCount - 1;
		return getDie(i).getValue();
	}


}






