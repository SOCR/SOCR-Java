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
import java.awt.Font;
import java.awt.Graphics2D;

/**
* This class defines a coin with specified colors, value, and probability
* of heads. The state of the coin can be tossed or not tossed. If tossed,
* the coin is drawn with a color and label that depend on the outcome (heads
* or tails). If not tossed, the coin is shown as an outline in the background
* color.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Coin extends JComponent{
	private int value;
	private double probability;
	private boolean tossed;
	private Color headColor, tailColor, textColor = Color.white;
	public final static int TAILS = 0, HEADS = 1;
	public final static String[] labels = {"T", "H"};


	/**
	* This general constructor creates a new coin with a probability, preferred size, and
	* colors.
	* @param p the probability of heads.
	* @param s the preferred size of the coin
	* @param a the color of the head side.
	* @param b the color of the tail side.
	* @param c the color of the text
	*/
	public Coin(double p, int s, Color a, Color b, Color c){
		setPreferredSize(new Dimension(s, s));
		setProbability(p);
		setColors(a, b, c);
		setToolTipText("Coin");
		toss();
	}

	/**
	* This special constructor creates a new coin with a specified probability and
	* size, and with the default colors of red for heads and green for tails.
	* @param p the probability of heads.
	* @param s the size of the coin.
	*/
	public Coin(double p, int s){
		this(p, s, Color.red, new Color(0, 180, 0), Color.white);
	}

	/**
	* This special  constructor creates a coin with a specified probability of
	* heads, and with the default size 40,  and with head color red and tail color green.
	*/
	public Coin(double p){
		this(p, 32);
	}


	/**
	* This default constructor creates a new fair coin with a specified value,
	* and with the default size and with head color red and tail color green.
	*/
	public Coin(){
		this(0.5);
	}

	/**
	* This method paints the coin.
	* @param g the graphics context.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int size = (int)Math.min(getSize().height, getSize().width);
		//System.out.println("Coin size="+size);
		if (tossed & value == 1) g.setColor(headColor);
		else if (tossed & value == 0) g.setColor(tailColor);
		else g.setColor(getBackground());
		g.fillOval(2, 2, size - 4, size - 4);
		g.setColor(Color.black);
		g.drawOval(2, 2, size - 4, size - 4);
		if (tossed){
			Font font = new Font("Arial", Font.BOLD, (size - 4) / 2);
			g.setColor(textColor);
 			g.setFont(font);
			int x = g.getFontMetrics(font).stringWidth(labels[value]);
			int y = g.getFontMetrics(font).getAscent();
			g.drawString(labels[value], size / 2 - x / 2, size / 2 + y / 2);
		}
	}
	
	public void paintCoin(Graphics2D g2, int x, int y ){
		super.paintComponent(g2);
		int size = 26;
		System.out.println("tossed ="+tossed +" value="+value);
		if (tossed & value == 1) g2.setColor(Color.red);
		else if (tossed & value == 0) g2.setColor(Color.green);
		else g2.setColor(getBackground());
		g2.fillOval(x, y, size - 4, size - 4);
		g2.setColor(Color.black);
		g2.drawOval(x, y, size - 4, size - 4);
		if (tossed){
			Font font = new Font("Arial", Font.BOLD, (size - 4) / 2);
			g2.setColor(textColor);
 			g2.setFont(font);
			int xx = g2.getFontMetrics(font).stringWidth(labels[value]);
			int yy = g2.getFontMetrics(font).getAscent();
			g2.drawString(labels[value], size / 2 - xx / 2, size / 2 + yy / 2);
		}
	}

	/**
	* This method sets the coin to a specified value.
	* @param x the value of the coin (0 tails, 1 heads)
	*/
	public void setValue(int x){
		if (x < 0) x = 0;
		if (x > 1) x = 1;
		value = x;
	}

	/**
	* This method gets the value of the coin.
	* @return the value of the coin (0 tails, 1 heads).
	*/
	public int getValue(){
		return value;
	}

	/**
	* This method sets the value to a random value, as determined by the probability
	* of heads.
	*/
	public void toss(){
		if (Math.random() < probability) value = 1;
		else value = 0;
	}

	/**
	* This method sets the state of the coin (tossed or untossed).
	* @param b true if the coin is tossed.
	*/
	public void setTossed(boolean b){
		tossed = b;
		repaint();
	}

	/**
	* This method returns the state of the coin (tossed or untossed).
	* @return true if the coin is tossed
	*/
	public boolean isTossed(){
		return tossed;
	}

	/**
	* This method sets the probability of heads.
	* @param p the probability of heads.
	*/
	public void setProbability(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1;
		probability = p;
	}

	/**
	* This method gets the probability of heads.
	* @return the probability of heads.
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method sets the colors.
	* @param a the head color.
	* @param b the tail color.
	* @param c the text color
	*/
	public void setColors(Color a, Color b, Color c){
		headColor = a;
		tailColor = b;
		textColor = c;
	}

	/**
	* This method sets the head color.
	* @param c the head color.
	*/
	public void setHeadColor(Color c){
		setColors(c, tailColor, textColor);
	}

	/**
	* This method gets the head color.
	* @return the head color.
	*/
	public Color getHeadColor(){
		return headColor;
	}

	/**
	* This method sets the tail color.
	* @param c the tail color.
	*/
	public void setTailColor(Color c){
		setColors(headColor, c, textColor);
	}

	/**
	* This method gets the tail color.
	* @return the tail color.
	*/
	public Color getTailColor(){
		return tailColor;
	}

	/**
	* This method sets the text color.
	* @param c the text color.
	*/
	public void setTextColor(Color c){
		setColors(headColor, tailColor, c);
	}

	/**
	* This method gets the text color.
	* @return the text color.
	*/
	public Color getTextColor(){
		return textColor;
	}
}

