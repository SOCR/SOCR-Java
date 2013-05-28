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

/**This class models a standard, 6-sided die with specified colors and probabilities.*/
public class Die extends JComponent{
	private int value, size;
	private Color backColor, spotColor;
	private double[] prob = new double[6];
	public final static int FAIR = 0, FLAT16 = 1, FLAT25 = 2, FLAT34 = 3, LEFT = 4, RIGHT = 5;

	/**This general constructor creates a new die with specified probabilities, size, back color,
	and spot color*/
	public Die(double[] p, int s, Color bc, Color sc){
		setProbabilities(p);
		setColors(bc, sc);
		setSize(s);
		roll();
	}

	/**This special constructor creates a new die with and specified probabilities and size,
	and with default back color red and spot color white*/
	public Die(double[] p, int s){
		this(p, s, Color.red, Color.white);
	}

	/**This special constructor creates a new die with specified probabilities and with default
	size of 32 pixels and default colors.*/
	public Die(double[] p){
		this(p, 32);
	}

	/**This special constructor creates a new die with a specified probabilities of special type and
	specified size, and with the default colors.*/
	public Die(int t, int s){
		setProbabilities(t);
		setColors(Color.red, Color.white);
		setSize(s);
		roll();
	}

	/**This special constructor creates a new fair die with specified probabilities of special type,
	and with the default size and colors.*/
	public Die(int t){
		this(t, 32);
	}

	/**Thid default constructor creates a new fair die, and with default size and colors.*/
	public Die(){
		this(FAIR);
	}

	/**This method paints the die*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		int r = (int)Math.rint(3.0 * size / 32),		//Spot radius
			d1 = 2 + r,									//First position
			d2 = (int)Math.rint(2 + size / 2.0 - r),	//Second postion
			d3 = 2 + size - 3 * r;						//Third position
		g.setColor(backColor);
		g.fillRoundRect(2, 2, size, size, 2, 2);
		g.setColor(spotColor);
		switch(value){
		case 1:
			g.fillOval(d2, d2, 2 * r, 2 * r);
			break;
		case 2:
			g.fillOval(d1, d1, 2 * r, 2 * r);
			g.fillOval(d3, d3, 2 * r, 2 * r);
			break;
		case 3:
			g.fillOval(d1, d1, 2 * r, 2 * r);
			g.fillOval(d2, d2, 2 * r, 2 * r);
			g.fillOval(d3, d3, 2 * r, 2 * r);
			break;
		case 4:
			g.fillOval(d1, d1, 2 * r, 2 * r);
			g.fillOval(d3, d1, 2 * r, 2 * r);
			g.fillOval(d1, d3, 2 * r, 2 * r);
			g.fillOval(d3, d3, 2 * r, 2 * r);
			break;
		case 5:
			g.fillOval(d1, d1, 2 * r, 2 * r);
			g.fillOval(d3, d1, 2 * r, 2 * r);
			g.fillOval(d1, d3, 2 * r, 2 * r);
			g.fillOval(d3, d3, 2 * r, 2 * r);
			g.fillOval(d2, d2, 2 * r, 2 * r);
			break;
		case 6:
			g.fillOval(d1, d1, 2 * r, 2 * r);
			g.fillOval(d3, d1, 2 * r, 2 * r);
			g.fillOval(d1, d3, 2 * r, 2 * r);
			g.fillOval(d3, d3, 2 * r, 2 * r);
			g.fillOval(d1, d2, 2 * r, 2 * r);
			g.fillOval(d3, d2, 2 * r, 2 * r);
			break;
		}
	}

	/**This method sets the value of the die to a specified value*/
	public void setValue(int x){
		//Correct for invalid value
		if (x < 1) x = 1; else if (x > 6) x = 6;
		value = x;
	}

	/**This method sets the value of the die to a random value, as determined by the probabilities*/
	public void roll(){
		double p = Math.random(), sum = 0;
		for (int i = 0; i < 6; i++){
			if (sum < p & p <= sum + prob[i]) value = i + 1;
			sum = sum + prob[i];
		}
	}

	/**This method gets the value of the die*/
	public int getValue(){
		return value;
	}

	/**This method sets the probabilities to specified values.*/
	public void setProbabilities(double[] p){
		if (p.length != 6) p = new double[6];
		double sum = 0;
		for (int i = 0; i < 6; i++){
			if (p[i] < 0) p[i] = 0;
			sum = sum + p[i];
		}
		if (sum == 0) for (int i = 0; i < 6; i++) prob[i] = 1.0 / 6;
		else for (int i = 0; i < 6; i++) prob[i] = p[i] / sum;
	}

	/**This method sets the probabilities to a special type*/
	public void setProbabilities(int n){
		if (n == FLAT16)
			setProbabilities(new double[] {1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 4});
		else if (n == FLAT25)
			setProbabilities(new double[] {1.0 / 8, 1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 8});
		else if (n == FLAT34)
			setProbabilities(new double[] {1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 8, 1.0 / 8});
		else if (n == LEFT)
			setProbabilities(new double[] {1.0 / 21, 2.0 / 21, 3.0 / 21, 4.0 / 21, 5.0 / 21, 6.0 / 21});
		else if (n == RIGHT)
			setProbabilities(new double[] {6.0 / 21, 5.0 / 21, 4.0 / 21, 3.0 / 21, 2.0 / 21, 1.0 / 21});
		else
			setProbabilities(new double[] {1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6});
	}

	/**This method returns the probability of face i.*/
	public double getProbability(int i){
		if (i < 0) i = 0; else if (i > 5) i = 5;
		return prob[i];
	}

	/**This method returns the probability vector.*/
	public double[] getProbabilities(){
		return prob;
	}

	/**This method sets the colors*/
	public void setColors(Color b, Color s){
		backColor = b;
		spotColor = s;
	}

	/**This method sets the back color*/
	public void setBackColor(Color bc){
		backColor = bc;
	}

	/**This method gets the back color*/
	public Color getBackColor(){
		return backColor;
	}

	/**This method sets the spot color.*/
	public void setSpotColor(Color sc){
		spotColor = sc;
	}

	/**The method gets the spot color*/
	public Color getSpotColor(){
		return spotColor;
	}

	/**this method sets the size of the die.*/
	public void setSize(int s){
		size = s;
		super.setSize(size + 4, size + 4);
	}

	/**Specify the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(size + 4, size + 4);
	}

	/**Specify the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(size + 4, size + 4);
	}
}

