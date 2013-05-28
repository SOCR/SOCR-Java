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

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;

/**This class defines a basic graph for displaying the distribution
getDensity and moments, and the data getDensity and moments for a specified
random variable */
public class RandomVariableGraph extends Graph{
	//Variables
	private int moments = 1, type, values;
	private double xMin, xMax, yMax, width;
	//Constants
	public static final int DISCRETE = Distribution.DISCRETE, CONTINTUOUS = Distribution.CONTINUOUS;
	public static final int NONE = 0, MSD = 1;
	//Objects
 	private RandomVariable randomVariable;
 	private Distribution dist;
 	private IntervalData data;
 	private Domain domain;

	/**This general constructor creates a new random varaible graph with a specified random variable.*/
	public RandomVariableGraph(RandomVariable rv){
		setMargins(35, 20, 30, 10);
		setRandomVariable(rv);
	}

	/**This method paints the graph of the getDensity function, empirical getDensity function,
	moment bar, and empirical moment bar*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		double x, x1, x2, d, w;
		int j = getSize().height - 10;
		int points = data.getSize();
		//Draw axes
		g.setColor(Color.black);
		drawAxis(g, 0, yMax, 0.1 * yMax, xMin, VERTICAL);
		drawAxis(g, domain, 0, HORIZONTAL, type);
		//Draw data getDensity
		if (points > 0){
			g.setColor(Color.red);
			for (int i = 0; i < values; i++){
				x = domain.getValue(i);
				if (type == DISCRETE) d = data.getRelFreq(x);
				else d = data.getDensity(x);
				fillBox(g, x - width / 2, 0, x + width / 2, d);
			}
			if (moments == 1) fillBoxPlot(g, data.getMean(), data.getSD(), j);
		}
		
		if (!showModelDistribution)
			return;
  		//Draw distribution getDensity
		g.setColor(Color.blue);
		if (type == DISCRETE){
			for (int i = 0; i < values; i++){
				x = domain.getValue(i);
 				drawBox(g, x - width / 2, 0, x + width / 2, dist.getDensity(x));
 			}
		}
		else{
			for (int i = 0; i < values; i++){
				x1 = domain.getBound(i);
				x2 = x1 + width;
				drawLine(g, x1, dist.getDensity(x1), x2, dist.getDensity(x2));
			}
		 }
		//Draw boxplot
		if (moments == 1) drawBoxPlot(g, dist.getMean(), dist.getSD(), j);
	}

	/**This method assigns the random variable and sets up graph paramters*/
	public void setRandomVariable(RandomVariable rv){
		randomVariable = rv;
		reset();
	}

	/**This method returns the random variable associated with the graph.*/
	public RandomVariable getRandomVariable(){
		return randomVariable;
	}

	/**This method resets the random variable graph. This method should be called whenever the
	distribution of the random variable changes..*/
	public void reset(){
		//Get basic paramters
		dist = randomVariable.getDistribution();
		data = randomVariable.getIntervalData();
		type = dist.getType();
		setDomain(dist.getDomain());
	}

	/**This method sets the domain for the horizontal axis. This may be different than the the
	distribution domain.*/
	public void setDomain(Domain d){
		domain = d;
		xMin = domain.getLowerBound();
		xMax = domain.getUpperBound();
		width = domain.getWidth();
		values = domain.getSize();
		yMax = 1.2 * dist.getMaxDensity();
		if (type == DISCRETE & yMax > 1) yMax = 1;
		//Set the scale;
		setScale(xMin, xMax, 0, yMax);
		repaint();
	}

	/**This method specifies the moments to display */
	public void showMoments(int n){
		moments = n;
	}

	/**This method gets the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}

	/**This method gets the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}
}

