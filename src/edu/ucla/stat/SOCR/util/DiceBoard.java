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

/**This class is a container that holds dice.*/
public class DiceBoard extends JPanel{
	private int dieCount;
	private Die[] die;
	public final static int FAIR = 0, FLAT16 = 1, FLAT25 = 2, FLAT34 = 3, LEFT = 4, RIGHT = 5;

	/**This general constructor creates a new dice board with a specified number
		of dice, probability distribution, specified size, and specified colors.*/
	public DiceBoard(int n, double[] p, int s, Color bc, Color sc){
		//setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		setDiceBoardLayout();

		if (n < 0) n = 0;
		dieCount = n;
		die = new Die[dieCount];
		for (int i = 0; i < dieCount; i++){
			die[i] = new Die(p, s, bc, sc);
			add(die[i]);
		}
	}

	/**This special constructor creates a new dice board with a specified number of dice,
	probability distribution and size, and with default back color red and default spot color green.*/
	public DiceBoard(int n, double[] p, int s){
		this(n, p, s, Color.red, Color.white);
	}

	/**This special  constructor creates a new dice board with a specified number of dice and
	specified probability distribution, and with default colors and size.*/
	public DiceBoard(int n, double[] p){
		this(n, p, 32);
	}

	/**This special constructor creates a new dice board with a specified number of dice, a
	specified probability distribution of special type, and a specified size, and with default colors.*/
	public DiceBoard(int n, int t, int s){
		//setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		setDiceBoardLayout();

		if (n < 0) n = 0;
		dieCount = n;
		die = new Die[dieCount];
		for (int i = 0; i < dieCount; i++){
			die[i] = new Die(t, s);
			add(die[i]);
		}
	}

	/**This special constructor ctreates a ndw dice board with a specified number of dice and a specified
	special distribution.  The size and colors are default.*/
	public DiceBoard(int n, int t){
		this(n, t, 32);
	}

	/**This sepcial constructor creates a new dice board with a specified number of fair dice, and with
	default size and colors.*/
	public DiceBoard(int n){
		this(n, FAIR);
	}

	/**This default constructor cretes a new dice board with 10 fair dice, and with
	default size and colors.*/
	public DiceBoard(){
		this(10);
	}

	/** Sets the default DiceBoard Layout Manager */
	public void setDiceBoardLayout()
	{	GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
         	setLayout(gridbag);
         	constraints.fill = GridBagConstraints.VERTICAL;
		gridbag.setConstraints(this, constraints);
	}

	/**Ths method gets the number of dice.*/
	public int getDieCount(){
		return dieCount;
	}

	/**This method returns the i'th die.*/
	public Die getDie(int i){
		if (i < 0) i = 0; else if (i > dieCount - 1) i = dieCount - 1;
		return die[i];
	}

	/**This method sets the probability distribution for all of the dice.*/
	public void setProbabilities(double[] p){
		for (int i = 0; i < dieCount; i++) die[i].setProbabilities(p);
	}

	/**This method sets the probability distribution of all of the dice to a special type.*/
	public void setProbabilities(int t){
		for (int i = 0; i < dieCount; i++) die[i].setProbabilities(t);
	}

	/**This method returns the probability distribution of die 0. This is the common distribution
	unless some of the dice distributions have been changed individually.*/
	public double[] getProbabilities(){
		return die[0].getProbabilities();
	}

	/**This method sets the colors of all of the dice.*/
	public void setColors(Color bc, Color sc){
		for (int i = 0; i < dieCount; i++) die[i].setColors(bc, sc);
	}

	/**This method rolls the first n dice.*/
	public void roll(int n){
		if (n < 0) n = 0; else if (n > dieCount) n = dieCount;
		for (int i = 0; i < n; i++) die[i].roll();
	}

	/**This method rolls all of the dice.*/
	public void roll(){
		roll(dieCount);
	}

	/**This method returns the sum of the scores of the first n dice.*/
	public int getSum(int n){
		if (n < 0) n = 0; else if (n > dieCount) n = dieCount;
		int sum = 0;
		for (int i = 0; i < n; i++) sum = sum + die[i].getValue();
		return sum;
	}

	/**This method returns the sum of all of the dice.*/
	public int getSum(){
		return getSum(dieCount);
	}

	/**This method returns the count for score x among the first n dice.*/
	public int getCount(int x, int n){
		if (x < 1) x = 1; else if (x > 6) x = 6;
		if (n < 0) n = 0; else if (n > dieCount) n = dieCount;
		int count = 0;
		for (int i = 0; i < n; i++) if (die[i].getValue() == x) count++;
		return count;
	}

	/**This method returns the count for score x among all of the dice.*/
	public int getCount(int x){
		return getCount(x, dieCount);
	}

	/**This method shows a specified number of the dice.*/
	public void showDice(int n){
		super.repaint();

		if (n < 0) n = 0; else if (n > dieCount) n = dieCount;
		for (int i = 0; i < dieCount; i++) die[i].setVisible(i < n);
		repaint();
	}

}






