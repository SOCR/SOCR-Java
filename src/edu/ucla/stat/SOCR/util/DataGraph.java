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
import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;

/*DataGraph is a basic graph for displaying the getDensity and moments for a specified
dataset */
public class DataGraph extends Graph{
	//Constants
	public final static int FREQ = 0, REL_FREQ = 1, DENSITY = 2;
	public final static int NONE = 0, MSD = 1, BOX = 2, MAD = 3, MMM = 4;
	//Variables
 	private int axisType, summaryStats = 1, type = REL_FREQ, intervals;
 	private double xMin, xMax, yMax, width;
	//Objects
 	private IntervalData data;
	private String dataPattern = "0.0";
	private Domain domain;

	/**This general constructor creates a new data graph with a specified data set and axis type.*/
	public DataGraph(IntervalData d, int t){
		setMargins(35, 20, 30, 10);
		axisType = t;
		setIntervalData(d);
	}

	/**This default constructor creates a new data graph with a new data set on the interval [0, 1]
	with subintervals of length 0.1.*/
	public DataGraph(){
		this(new IntervalData(0, 1, 0.1), 1);
	}

	/**This method paints the graph of the getDensity function, empirical getDensity function,
	moment bar, and empirical moment bar*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double x, d;
		int size = data.getSize();
		//Determine yMax
		if (type == FREQ) yMax = data.getMaxFreq();
		else if (type == REL_FREQ) yMax = data.getMaxRelFreq();
		else yMax = data.getMaxDensity();
		if (yMax == 0) yMax = 1;
		//Set graph scale
		setScale(xMin, xMax, 0, yMax);
		g.setColor(Color.black);
		//Draw axes
 		drawAxis(g, 0, yMax, 0.1 * yMax, xMin, VERTICAL);
		drawAxis(g, domain, 0, HORIZONTAL, axisType);
		//Draw data
		if (size > 0){
			g.setColor(Color.red);
			for (int i = 0; i < intervals; i++){
				x = domain.getValue(i);
				if (type == FREQ) d = data.getRelFreq(x);
				else if (type == REL_FREQ) d = data.getRelFreq(x);
				else  d = data.getDensity(x);
				fillBox(g, x - width / 2, 0, x + width / 2, d);
			}
			//Draw summary statistics
			int j = getSize().height - 10;
			if (summaryStats == 1) fillBoxPlot(g, data.getMean(), data.getSD(), j);
		}
	}

	 /**This method assigns the data and sets up graph paramters*/
	public void setIntervalData(IntervalData d){
		data = d;
		domain = data.getDomain();
		xMin = domain.getLowerBound();
		xMax = domain.getUpperBound();
		width = domain.getWidth();
		intervals = domain.getSize();
		repaint();
	}

	/**This method returns the data set.*/
	public IntervalData getIntervalData(){
		return data;
	}

	/**This method sets the plot style*/
	public void setType(int i){
		if (i < 0) i = 0; else if (i > 2) i = 2;
		type = i;
		repaint();
	}

	/**This method sets the axis type.*/
	public void setAxisType(int i){
		if (i < 0) i = 0; else if (i > 1) i = 1;
		axisType = i;
	}

	/**This method specifies the moments to display */
	public void showSummaryStats(int n){
		summaryStats = n;
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}
}

