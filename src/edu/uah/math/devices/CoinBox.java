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

/**This class is a container that holds coins.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CoinBox extends JPanel{
	private int coinCount, size;
	private double probability;
	private boolean tossed;
	private Color headColor, tailColor, textColor;

	/**
	* This general constructor creates a new coin box with a specified number of coins,
	* specified preferred size, a specified probability of heads, and specified head,
	* tail, and text colors.
	* @param n the number of coins.
	* @param p the probability of heads
	* @param s the preferred size
	* @param a the head color.
	* @param b the tail color.
	* @param c the text color
	*/
	public CoinBox(int n, double p, int s, Color a, Color b, Color c){
		if (s < 10) s = 10;
		size = s;
		//setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
			// Possibly move to public 
		setLayout(new java.awt.GridLayout(10, 10, 0, 0));
		//setMaximumSize(new java.awt.Dimension(250, 300));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setToolTipText("Coin Box");
		setProbability(p);
		setColors(a, b, c);
		setCoinCount(n);
	}

	/**
	* This special constructor creates a new coin box with a specified number of coins,
	* probability, and size, and with default head color red, tail color green, and text color white
	* @param n the number of coins.
	* @param p the probability of heads
	* @param s the preferred size.
	*/
	public CoinBox(int n, double p, int s){
		this(n, p, s, Color.red, new Color(0, 180, 0), Color.white);
	}

	/**
	* This special constructor creates a new coin box with a specified number of coins,
	* and probability, and with default size 40, head color red, tail color green, and
	* text color white
	* @param n the number of coins.
	* @param p the probability of heads
	*/
	public CoinBox(int n, double p){
		this(n, p, 40, Color.red, new Color(0, 128, 0), Color.white);
	}

	/**
	* This special constructor creates a new coin box with a specified number of coins,
	* and with the default probability, size, and colors.
	* @param n the number of coins.
	*/
	public CoinBox(int n){
		this(n, 0.5);
	}

	/**
	* This default constructor creates a new coin box with 10 coins and with the default probability,
	* and colors.
	*/
	public CoinBox(){
		this(10);
	}

	/**
	* Ths method gets the number of coins.
	* @return the number of coins.
	*/
	public int getCoinCount(){
		return coinCount;
	}

	/**
	* This method sets the number of coins. If the new coin count is greater than the
	* old, an appropriate number of new coins are added. If the new coin count is
	* smaller than the old, an appropriate number of coins are removed. The coin count
	* is then updated.
	* @param n the number of coins in the coin box
	*/
	public void setCoinCount(int n){
		Coin coin;
		if (n < 0) n = 0;
		if (n > coinCount){
			for (int i = coinCount; i < n; i++){
				coin = new Coin(probability, size, headColor, tailColor, textColor);
				coin.setToolTipText("Coin " + (i + 1));
				add(coin);
			}
		}
		else if (n < coinCount) for (int i = n; i < coinCount; i++) remove(n);
		coinCount = n;
		revalidate();
	}

	/**
	* This method gets a specified coin.
	* @param i the index of the coin.
	* @return the coin.
	*/
	public Coin getCoin(int i){
		if (i < 0) i = 0; else if (i > coinCount - 1) i = coinCount - 1;
		return (Coin)getComponent(i);
	}

	/**
	* This method sets the probability of heads for all of the coins.
	* @param p the probability of heads.
	*/
	public void setProbability(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1;
		probability = p;
		for (int i = 0; i < coinCount; i++) ((Coin)getComponent(i)).setProbability(probability);
	}

	/**
	* This method returns the probability of heads.
	* @return the probability of heads
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method sets the colors for all of the coins.
	* @param a the head color
	* @param b the tail color
	* @param c the text color
	*/
	public void setColors(Color a, Color b, Color c){
		headColor = a;
		tailColor = b;
		textColor = c;
		for (int i = 0; i < coinCount; i++) ((Coin)getComponent(i)).setColors(headColor, tailColor, textColor);
	}

	/**
	* This method sets the head color for all of the coins.
	* @param c the head color
	*/
	public void setHeadColor(Color c){
		setColors(c, tailColor, textColor);
	}

	/**
	* This method returns the head color.
	* @return the head color
	*/
	public Color getHeadColor(){
		return headColor;
	}

	/**
	* This method sets the tail color for all of the coins.
	* @param c the tail color
	*/
	public void setTailColor(Color c){
		setColors(headColor, c, textColor);
	}

	/**
	* This method returns the tail color.
	* @return the tail color
	*/
	public Color getTailColor(){
		return tailColor;
	}

	/**
	* This method sets the text color for all of the coins.
	* @param c the text color
	*/
	public void setTextColor(Color c){
		setColors(headColor, tailColor, c);
	}

	/**
	* This method returns the tail color.
	* @return the text color
	*/
	public Color getTextColor(){
		return textColor;
	}

	/**
	* This method tosses the coins, setting them to random values, as determined by the
	* probability of heads for each coin.
	*/
	public void toss(){
		for (int i = 0; i < coinCount; i++) ((Coin)getComponent(i)).toss();
	}

	/**
	* This method returns the number of heads
	* @return the number of heads.
	*/
	public int getHeadCount(){
		int headCount = 0;
		for (int i = 0; i < coinCount; i++) headCount = headCount + ((Coin)getComponent(i)).getValue();
		return headCount;
	}

	/**
	* This method returns the number of tails
	* @return the number of tails.
	*/
	public int getTailCount(){
		int tailCount = 0;
		for (int i = 0; i < coinCount; i++) tailCount += 1 - ((Coin)getComponent(i)).getValue();
		return tailCount;
	}

	/**
	* This method sets the state of the all of the coins (tossed or untossed).
	* Tossed coins are shown with a label and color that depend on the outcome.
	* Untossed coins are shown unlabeled in the background color.
	* @param b true if the coins are tossed.
	*/
	public void setTossed(boolean b){
		tossed = b;
		for (int i = 0; i < coinCount; i++) getCoin(i).setTossed(b);
		repaint();
	}

	/**
	* This method returns the state of the all of the coins (tossed or untossed).
	* Tossed coins are shown with a label and color that depend on the outcome.
	* Untossed coins are shown unlabeled in the background color.
	* @return true if the coins are tossed.
	*/
	public boolean isTossed(){
		return tossed;
	}

	/**
	* This method sets the array of values.
	* @param v the array of values
	*/
	public void setValues(int[] v){
		if (v.length == coinCount)
			for (int i = 0; i < coinCount; i++) getCoin(i).setValue(v[i]);
	}

	/**
	* This method returns the array of values.
	* @return the array of values
	*/
	public int[] getValues(){
		int[] v = new int[coinCount];
		for (int i = 0; i < coinCount; i++) v[i] = getCoin(i).getValue();
		return v;
	}

	/**
	* This method sets the value of an individual coin.
	* @param i the index
	* @param x the value of the coin
	*/
	public void setValues(int i, int x){
		if (i < 0) i = 0; else if (i > coinCount - 1) i = coinCount - 1;
		getCoin(i).setValue(x);
	}

	/**
	* This method returns the value of an individual coin
	* @param i the index
	* @return the value of the coin with the index
	*/
	public int getValues(int i){
		if (i < 0) i = 0; else if (i > coinCount - 1) i = coinCount - 1;
		return getCoin(i).getValue();
	}
}






