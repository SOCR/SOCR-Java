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

/**This class models the graph of the simple ranodom walk on the interval [0, n].*/
public class RandomWalkGraph extends Graph{
	//Variables
	private int upperTime, maxValue, minValue, lastZero, bound;
	private double probability;
	private int[] value;
	private boolean walked;

	/**This general constructor creates a new random walk graph on the interval [0, n] with
	probability p of a step in the positive direction.*/
	public RandomWalkGraph(int n, double p){
		setParameters(n, p);
		setPointSize(5);
	}

	/**This special constructor creates a new symmetric random walk graph on the interval [0, n].*/
	public RandomWalkGraph(int n){
		this(n, 0.5);
	}

	/**This method sets the parameters: the time parameter n and the probability p of a
	move to the right.*/
	public void setParameters(int n, double p){
		upperTime = n;
		probability = p;
		value = new int[n + 1];
		bound = (int)(3 * Math.sqrt(n));
		setScale(0, n, -bound, bound);
	}

	/**This method sets the time parameter n.*/
	public void setUpperTime(int n){
		setParameters(n, 0.5);
	}

	/**This method gets the time parameter.*/
	public int getUpperTime(int n){
		return upperTime;
	}

	/**This method sets the probabiltiy of a move to the right.*/
	public void setProbability(double p){
		probability = p;
	}

	/**This method gets the probability of a move to the right.*/
	public double getProbability(){
		return probability;
	}

	/**This method performs the random walk, starting at a specified initial value.*/
	public void walk(int initialValue){
		int step;
		value[0] = initialValue;
		maxValue = value[0]; minValue = value[0];
		if (value[0] == 0) lastZero = 0; else lastZero = -1;
		for (int i = 1; i <= upperTime; i++){
			if (Math.random() < probability) step = 1; else step = -1;
			value[i] = value[i - 1] + step;
			if (value[i] > maxValue) maxValue = value[i];
			if (value[i] < minValue) minValue = value[i];
			if (value[i] == 0) lastZero = i;
		}
		walked = true;
	}

	/**This method gets the position of the walk at a specified time.*/
	public int getValue(int i){
		if (i < 0) i = 0; else if (i > upperTime) i = upperTime;
		return value[i];
	}

	/**This method returns the maximum value.*/
	public int getMaxValue(){
		return maxValue;
	}

	/**This method returns the minimum value.*/
	public int getMinValue(){
		return minValue;
	}

	/**This method returns the last zero.*/
	public int getLastZero(){
		return lastZero;
	}

	/**This method paints the random walk graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.setColor(Color.black);
		//Draw x axes
		drawLine(g, 0, 0, upperTime, 0);
		for (int i = 0; i <= upperTime; i++) drawTick(g, i, 0, VERTICAL);
		drawLabel(g, "0", 0, 0, LEFT);
		drawLabel(g, String.valueOf(upperTime), upperTime, 0, RIGHT);
		//Draw vertical axes
		drawAxis(g, -bound, bound, 1, 0, VERTICAL, 1);
		g.setColor(Color.gray);
		drawLine(g, upperTime, -bound, upperTime, bound);
		for (int i = -bound; i <= bound; i++) drawTick(g, upperTime, i, HORIZONTAL);
		//Draw data
		if (walked){
			g.setColor(Color.red);
			for (int i = 0; i < upperTime; i++)	drawLine(g, i, value[i], i + 1, value[i + 1]);
			drawPoint(g, lastZero, 0);
			drawPoint(g, upperTime, minValue);
			drawPoint(g, upperTime, maxValue);
		}
	}

	/**This method resets the random walk graph.*/
	public void reset(){
		walked = false;
		repaint();
	}
}






