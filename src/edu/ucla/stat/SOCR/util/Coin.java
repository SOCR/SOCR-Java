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

/**This class defines a coin with specified colors, size, and value, and probability of heads.*/
public class Coin extends JComponent{
	private int value, size;
	private double probability;
	private Color headColor, tailColor;
	private Font font;

	/**This general constructor creates a new coin with a probability, size, and
	colors*/
	public Coin(double p, int s, Color hc, Color tc){
		setProbability(p);
		setSize(s);
		setColors(hc, tc);
		toss();
	}

	/**This special constructor creates a new coin with a specified size and
	the default colors of red for heads and green for tails*/
	public Coin(double p, int s){
		this(p, s, Color.red, Color.green);
	}

	/**This special constructor creates a new coin with a specified probability and with the default
	colors and size.*/
	public Coin(double p){
		this(p, 24);
	}

	/**This special constructor creates a new fair coin with a specified value, head color red,
	tail color green, and default size of 24 pixels*/
	public Coin(){
		this(0.5);
	}

	/**This method paints the coin*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		String label;
 		g.setFont(font);
 		if (value == 1){
			label = "H";
			g.setColor(headColor);
		}
		else{
			label = "T";
			g.setColor(tailColor);
		}
		int x = g.getFontMetrics(font).stringWidth(label);
		int y = g.getFontMetrics(font).getAscent();
		g.fillOval(2, 2, size - 4, size - 4);
		g.setColor(Color.black);
		g.drawOval(2, 2, size - 4, size - 4);
		g.drawString(label, size / 2 - x / 2, size / 2 + y / 2);
	}

	/**This method sets the coin to a specified value*/
	public void setValue(int x){
		if (x != 0) x = 1;
		value = x;
	}

	/**This method gets the value of the coin*/
	public int getValue(){
		return value;
	}

	/**This method sets the value to a random value, as determined by the probability of heads.*/
	public void toss(){
		if (Math.random() < probability) value = 1;
		else value = 0;
	}

	/**This method sets the probability of heads.*/
	public void setProbability(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1;
		probability = p;
	}

	/**This method gets the probability of heads.*/
	public double getProbability(){
		return probability;
	}

	/**This method sets the size of the coin.*/
	public void setSize(int s){
		if (s < 1) s = 1;
		size = s;
		font = new Font("SansSerif", Font.PLAIN, (size - 4) / 2);
		super.setSize(size, size);
	}

	/**This method sets the colors*/
	public void setColors(Color hc, Color tc){
		headColor = hc;
		tailColor = tc;
	}

	/**This method sets the head color*/
	public void setHeadColor(Color hc){
		headColor = hc;
	}

	/**This method gets the head color*/
	public Color getHeadColor(){
		return headColor;
	}

	/**This method sets the tail color*/
	public void setTailColor(Color tc){
		tailColor = tc;
	}

	/**This method gets the tail color*/
	public Color getTailColor(){
		return tailColor;
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(size, size);
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(size, size);
	}
}

