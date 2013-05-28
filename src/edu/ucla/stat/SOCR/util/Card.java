/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/

package edu.ucla.stat.SOCR.util;
import java.awt.*;
import javax.swing.*;

/**This ckass models a playing card from a standard deck*/
public class Card extends JComponent{
	//Variables
	private int suit, value, cardNumber;
	private boolean showCard = false;
	private static Image[] cardImage = new Image[53];
	//Constants
	public final static int ACE = 1, JACK = 11, QUEEN = 12, KING = 13;
	public final static int CLUBS = 0, DIAMONDS = 1, HEARTS = 2, SPADES = 3;

	/**This general constructor creates a new card of a specified value and suit*/
	public Card(int i, int j){
		setScore(i, j);
	}

	/**This general constructor creates a new card of a specified number (from 0 to
	51*/
	public Card(int n){
		setScore(n);
	}

	/**This default constructor creates a new card randomly chosen from the deck*/
	public Card(){
		this((int)(52 * Math.random()));
	}

	/**This method paints the card*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		try { 	
			if (showCard) g.drawImage(cardImage[cardNumber], 0, 0, this);
			else g.drawImage(cardImage[52], 0, 0, this);
			
			}
		catch (Exception e ) {
		 System.out.println("STACK::"+e); }

		//System.err.println("Okay in Card.java::paint() 2 of 2");
	}

	/**This method sets the card to a value, as specified by a number from 0 to 51:
	0 to 12 are the clubs, 13 to 25 the diamonds, 26 to 38 the heards, and
	39 to 51 the spades*/
	public void setScore(int n){
		//Correct for invalid index
		if (n < 0) n = 0; else if (n > 51) n = 51;
		//Set the card number, suit and value
		cardNumber = n;
		suit = n / 13;
		value = n % 13 + 1;
	}

	/**This class method assigns an image to card number i. Images 0 to 51 are the card fronts
	and image 52 is the card back*/
	public static void setImage(Image image, int i){
		cardImage[i] = image;
	}

	/**This methood sets the card to a specific card, as specified by the suit and value:
	suit 0: clubs, suit 1: diamonds, suit 2: hearts, suit 3: spades.
	values 1 to 12 are ace through king*/
	public void setScore(int i, int j){
		//Correct for invalid parameters
		if (i < 1) i = 1; else if (i > 13) i = 13;
		if (j < 0) j = 0; else if (j > 3) j = 3;
		//Set the value, suit, and card number
		value = i;
		suit = j;
		cardNumber = value + 13 * suit - 1;
	}

	/**This method gets the suit of the card: 0 for clubs, 1 for diamonds, 2 for hearts, and
	3 for spades.*/
	public int getSuit(){
		return suit;
	}

	/**This method gets the value (denomination) of the card: 1 for ace, 2 through 10, 11 for
	jack, 12 for queen, 13 for king.*/
	public int getValue(){
		return value;
	}

	/**This method gets the number of the card: 0-12 are the clubs, 13-25 are the diamonds
	26-38 are the hearts, and 39-51 are the spades.*/
	public int getCardNumber(){
		return cardNumber;
	}

	/**This method determines if the front or back of the card is shown*/
	public void showCard(boolean b){
		showCard = b;
	}

	/**This method specifies the minimum size of the card*/
	public Dimension getMinimumSize(){
		return new Dimension(71, 96);
	}

	/**This method specifies the preferred size of the card*/
	public Dimension getPreferredSize(){
		return new Dimension(71, 96);
	}
}

