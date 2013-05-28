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
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Image;
import java.io.Serializable;

/**
* This class models a playing card from a standard deck.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Card extends JComponent implements Serializable{
	//Variables
	private int suit, value, cardNumber;
	private boolean faceUp = false;
	private final static Image[] images = new Image[53];
	//Constants
	public final static int ACE = 1, JACK = 11, QUEEN = 12, KING = 13;
	public final static int CLUBS = 0, DIAMONDS = 1, HEARTS = 2, SPADES = 3;
	public final static String[] suitName = {"Clubs", "Diamonds", "Hearts", "Spades"};
	public final static String[] valueName = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven",
		"Eight", "Nine", "Ten", "Jack", "Queen", "King"};

	/**
	* This general constructor creates a new card of a specified value and suit
	* and specified state (up or down).
	* @param i the value (1 to 10, 11 jack, 12 queen, 13 king)
	* @param j the suit (0 clubs, 1 diamonds, 2 hearts, 3 spades)
	* @param b true if the card face is showing
	*/
	public Card(int i, int j, boolean b){
		faceUp = b;
		setToolTipText("Card");
		setScore(i, j);
	}

	/**
	* This special constructor creates a new card of a specified value and
	* suit that is face down.
	* @param i the value (1 to 10, 11 jack, 12 queen, 13 king)
	* @param j the suit (0 clubs, 1 diamonds, 2 hearts, 3 spades)
	*/
	public Card(int i, int j){
		this(i, j, false);
	}

	/**
	* This general constructor creates a new card of a specified number and
	* a specified state (face up or down)
	* @param n the card number (0 to 12 clubs, 13 to 25 diamonds, 26 to 38 hearts, 39 to 51 spades).
	* @param b true if the card face is showing
	*/
	public Card(int n, boolean b){
		setToolTipText("Card");
		faceUp = b;
		setCardNumber(n);
	}

	/**
	* This special constructor creates a new card of a specified number
	* that is face down.
	* @param n the card number (0 to 12 clubs, 13 to 25 diamonds, 26 to 38 hearts, 39 to 51 spades).
	*/
	public Card(int n){
		this(n, false);
	}

	/**
	* This default constructor creates a new card randomly chosen from the deck
	* that is face down.
	*/
	public Card(){
		this((int)(52 * Math.random()));
	}

	/**
	* This method sets the card to a value, as specified by a number from 0 to 51.
	* @param n the card number (0 to 12 clubs, 13 to 25 diamonds, 26 to 38 hearts, 39 to 51 spades).
	*/
	public void setCardNumber(int n){
		//Correct for invalid index
		if (n < 0) n = 0; else if (n > 51) n = 51;
		//Set the card number, suit and value
		cardNumber = n;
		suit = n / 13;
		value = n % 13 + 1;
	}

	/**
	* This method returns the number of the card from 0 to 51. Cards 0 to 12 are the
	* clubs (in order); cards 13 to 25 are the diamonds (in order); cards 26 to 38
	* are the hearts (in order); and cards 39 to 51 are the spades (in order).
	*/
	public int getCardNumber(){
		return cardNumber;
	}

	/**
	* This methood sets the card to a specific card, as specified by the suit and value.
	* @param i the suit (0 clubs, 1 diamonds, 2 hearts, 3 spades).
	* @param j the value (1-10, 11 jack, 12 queen, 13 king).
	*/
	public void setScore(int i, int j){
		//Correct for invalid parameters
		if (i < 1) i = 1; else if (i > 13) i = 13;
		if (j < 0) j = 0; else if (j > 3) j = 3;
		//Set the value, suit, and card number
		value = i;
		suit = j;
		cardNumber = value + 13 * suit - 1;
	}

	/**
	* This method paints the card by drawing the image.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		ImageIcon icon;
		Image image;
		if (faceUp){
			image = images[cardNumber];
			if (image == null){
				icon = new ImageIcon(getClass().getResource("cards/card" + cardNumber + ".gif"));
				image = icon.getImage();
				images[cardNumber] = image;
			}
		}
		else{
			image = images[52];
			if (image == null){
				icon = new ImageIcon(getClass().getResource("cards/card52.gif"));
				image = icon.getImage();
				images[52] = image;
			}
		}
		g.drawImage(image, 0, 0, this);
	}

	/**
	* This method sets the suit of the card.
	* @param j the suit (0 clubs, 1 diamonds, 2 hearts, 3 spades)
	*/
	public void setSuit(int j){
		setScore(value, j);
	}

	/**
	* This method gets the suit of the card.
	* @return the suit (0 clubs, 1 diamonds, 2 hearts, and 3 spades).
	*/
	public int getSuit(){
		return suit;
	}

	/**
	* This method sets the value (denomination) of the card
	* @param i the value (1 ace, 2-10, 11 jack, 12 queen, 13 king)
	*/
	public void setValue(int i){
		setScore(i, suit);
	}

	/**
	* This method gets the value (denomination) of the card.
	* @return the value (1 ace, 2-10, 11 jack, 12 queen, 13 king).
	*/
	public int getValue(){
		return value;
	}

	/**
	* This method specifies if the front or back of the card is shown.
	* @param b true if the card face is shown, false if the card back is shown.
	*/
	public void setFaceUp(boolean b){
		faceUp = b;
		repaint();
	}

	/**
	* This method returns the showing state of the card.
	* @return true if card face is showing, false if card back is showing
	*/
	public boolean isFaceUp(){
		return faceUp;
	}

	/**
	* This method returns the preferred size.
	* @return the 71 by 96 dimension
	*/
	public Dimension getPreferredSize(){
		return new Dimension(71, 96);
	}

	/**
	* This method returns the minimum size.
	* @return the 71 by 96 dimension
	*/
	public Dimension getMinimumSize(){
		return new Dimension(71, 96);
	}


}

