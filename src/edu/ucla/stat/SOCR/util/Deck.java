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

/**This class is a container that holds cards.*/
public class Deck extends JPanel{
	private int cardCount;
	private int[] deck = new int[52];
	public Card[] card;

	/**This general constructor creates a new deck that can show a specified number of cards.*/
	public Deck(int n){
		setLayout(new FlowLayout(FlowLayout.CENTER));
		cardCount = n;
		//System.out.println("Inside Deck::cardCount="+cardCount+" 1 of 2 ...");

		card = new Card[cardCount];
		for (int i = 0; i < cardCount; i++){
			card[i] = new Card();
			add(card[i]);
		}
		//System.out.println("Inside Deck::cardCount="+cardCount+" 2 of 2 !");

		
		deal(cardCount);
		validate();
	}

	/**This default constructor creates a new deck that can show 5 cards.*/
	public Deck(){
		this(5);
	}

	/**Ths method gets the number of cards.*/
	public int getCardCount(){
		return cardCount;
	}

	/**This method gets the i't card.*/
	public Card getCard(int i){
		if (i < 0) i = 0; else if (i > cardCount - 1) i = cardCount - 1;
		return card[i];
	}

	/**This method deals n cards.*/
	public void deal(int n){
		for (int i = 0; i < 52; i++) deck[i] = i;
		int k, u, temp;
		//Deal Cards
		for (int i = 0; i <  n; i++){
			k = 52 - i;
			u = (int)(Math.random() * k);
			card[i].setScore(deck[u]);
			temp = deck[k - 1];
			deck[k - 1] = deck[u];
			deck[u] = temp;
		}
	}

	/**This method shows or hides the cards.*/
	public void showCards(int n, boolean b){
		for (int i = 0; i < cardCount; i++){
			if (i < n){
				card[i].setVisible(true);
				card[i].showCard(b);
			}
			else card[i].setVisible(false);
		}
		repaint();
	}
}






