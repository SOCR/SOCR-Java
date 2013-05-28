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

/**This class is a container that holds coins.*/
public class CoinBox extends JPanel{
	private int coinCount;
	private Coin[] coin;

	/**This general constructor creates a new coin box with a specified number of coins,
	a specified size, and specified head and tail colors.*/
	public CoinBox(int n, double p, int s, Color hc, Color tc){
		setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		coinCount = n;
		coin = new Coin[coinCount];
		for (int i = 0; i < coinCount; i++){
			coin[i] = new Coin(p, s, hc, tc);
			add(coin[i]);
		}
	}

	/**This special constructor creates a new coin box with a specified number of coins,
	and specified probability and size, and default head color red, and default tail color green.*/
	public CoinBox(int n, double p, int s){
		this(n, p, s, Color.red, Color.green);
	}

	/**This special constructor creates a new coin box with a specified number of coins and
	probability	and with default size 24 pixels, default head color red, and default tail color green.*/
	public CoinBox(int n, double p){
		this(n, p, 24, Color.red, Color.green);
	}

	/**This default constructor creates a new coin box with a specified number of coins, and with
	the default probability, size, and colors.*/
	public CoinBox(int n){
		this(n, 0.5);
	}

	/**This default constructor creates a new coin box with 10 coins and with the default probability
	size, and colors.*/
	public CoinBox(){
		this(10);
	}

	/**Ths method gets the number of coins.*/
	public int getCoinCount(){
		return coinCount;
	}

	/**This method gets the i't coin.*/
	public Coin getCoin(int i){
		if (i < 0) i = 0; else if (i > coinCount - 1) i = coinCount - 1;
		return coin[i];
	}

	/**This method sets the probability of heads for all of the coins.*/
	public void setProbability(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1;
		for (int i = 0; i < coinCount; i++) coin[i].setProbability(p);
	}

	/**This method sets the colors for all of the coins.*/
	public void setColors(Color hc, Color tc){
		for (int i = 0; i < coinCount; i++) coin[i].setColors(hc, tc);

	}

	/**This method tosses the first n coins, setting them to random values, as determined by the
	probability of heads for each coin.  All of the other coins are set to invisible.*/
	public void toss(int n){
		if (n < 0) n = 0; else if (n > coinCount) n = coinCount;
		for (int i = 0; i < n; i++)	coin[i].toss();
	}

	/**This method tosses all of the coins.*/
	public void toss(){
		toss(coinCount);
	}

	/**This method returns the number of heads among the first n coins.*/
	public int getHeadCount(int n){
		int headCount = 0;
		for (int i = 0; i < n; i++) headCount = headCount + coin[i].getValue();
		return headCount;
	}

	/**This method returns the number of heads among all of the coins.*/
	public int getHeadCount(){
		return getHeadCount(coinCount);
	}

	/**This method shows a specified number of the coins.*/
	public void showCoins(int n){
		if (n < 0) n = 0; else if (n > coinCount) n = coinCount;
		for (int i = 0; i < coinCount; i++) coin[i].setVisible(i < n);
		repaint();

	}
}






