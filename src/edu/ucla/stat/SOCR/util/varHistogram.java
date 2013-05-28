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


import java.awt.Graphics;
import java.util.ArrayList;

import edu.ucla.stat.SOCR.distributions.Domain;
import edu.ucla.stat.SOCR.distributions.IntervalData;
import edu.ucla.stat.SOCR.modeler.Modeler;
import edu.ucla.stat.SOCR.modeler.gui.ModelerColor;

/*Histogram is a basic graph for displaying the density and moments for a specified
interval data set. */
public class varHistogram extends Graph {
	//Constants
	public final static int FREQ = 0, REL_FREQ = 1, DENSITY = 2; // looks like relative frequency.
	public final static int NONE = 0, MSD = 1, BOX = 2, MAD = 3, MMM = 4;
	//Variables
 	protected int axisType, summaryStats = 1, type = REL_FREQ, intervals;
 	//public double xMin, xMax, yMax, yMin;
 	protected double width; // change to protected because I need to modify it in ModelHistogram.
	protected int currentXUpperBound = -5;
	protected int currentXLowerBound = 5;
	//protected double[] modelX = null, modelY = null;
	//Objects
 	//private IntervalData data;
 	protected IntervalData data;

	protected Domain domain = null; // x domain
	//private Font f = new Font("sansserif", Font.PLAIN, 11);
	protected int modelType;
	/**This general constructor creates a new data graph with a specified data set and axis type.*/
	protected ArrayList listOfTicks = new ArrayList();

	public varHistogram(IntervalData d, int t){
		//System.out.println("varHistogram constructor 1");
		setMargins(35, 20, 30, 10);
		axisType = t;
		setIntervalData(d);
          yMax = 20;
          xMin = -5;
          xMax = 5;
	}
	public varHistogram(IntervalData d, int t, int modelType){
		this(d, t);
		this.modelType = modelType;
		//System.out.println("varHistogram constructor 2");
	}
	/**This default constructor creates a new data graph with a new data set on the interval [0, 1]
	with subintervals of length 0.1.*/
	public varHistogram(){
		this(new IntervalData(0, 1, 0.1), 1); // calls constructor 1 with default values
		//System.out.println("varHistogram constructor 3");
		////System.out.println("varHistogram constructor" );
	}

	/**This method paints the graph of the getDensity function, empirical getDensity function,
	moment bar, and empirical moment bar*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		////System.out.println("varHistogram paintComponent" );
		double x, d;
		int size = data.getSize();
		domain = data.getDomain();
		double newWidth = (xMax - xMin) / 10; // 10 looks good on graph.

		width = domain.getWidth();
		intervals = domain.getSize();
		//Determine yMax
		if (type == DENSITY) {
			yMax = data.getMaxDensity();
		}
		else if (type == REL_FREQ) {
			yMax = data.getMaxRelFreq();
		}

          if (yMax == 0) {
			yMax = 1; // why???
		}

		setScale(xMin, xMax, 0, yMax);
		g.setColor(ModelerColor.HISTOGRAM_AXIS); // this is the coordinate grids.
		if (modelType == Modeler.FOURIER_TYPE || modelType == Modeler.WAVELET_TYPE) {
			xMin = 0;
			setScale(xMin, xMax, -yMax, yMax);
	 		super.drawAxis(g, -yMax, yMax, 0.1 * yMax, xMin, VERTICAL); // 6 args
			super.drawAxis(g, xMin, xMax, (xMax - xMin) / 10, 0, HORIZONTAL, axisType, listOfTicks); // c must be 0.

		} else {
	 		super.drawAxis(g, -yMax, yMax, 0.1 * yMax, xMin, VERTICAL); // 6 args
			super.drawAxis(g, xMin, xMax, (xMax - xMin) / 10, 0, HORIZONTAL, axisType, listOfTicks); // c must be 0.
		}

		//Draw data
		////System.out.println("varHistogram paintComponent fillBox intervals = " + intervals);
		////System.out.println("varHistogram paintComponent modelType = " + this.modelType );
		////System.out.println("varHistogram paintComponent size = " + size);
		////System.out.println("varHistogram paintComponent data = " + data);

		if (size > 0){
			for (int i = 0; i < intervals; i++){
				x = domain.getValue(i);
				if (type == FREQ) {
					d = data.getFreq(x);
					////////System.out.println("varHistogram fillBox type == FREQ");
				}
				else if (type == REL_FREQ) {
					d = data.getRelFreq(x);
					////////System.out.println("varHistogram fillBox type == REL_FREQ");

				}
				else  {
					d = data.getDensity(x);
					////////System.out.println("varHistogram fillBox else");

				}
				if (modelType == Modeler.CONTINUOUS_DISTRIBUTION_TYPE
					|| modelType == Modeler.DISCRETE_DISTRIBUTION_TYPE) {
					g.setColor(ModelerColor.HISTOGRAM_BAR_FILL); // Color.red. This is the bar fill-in. annie che.
					if (d > 0)
						////System.out.println("varHistogram fillBox x = " + x + ", x - width / 2 = " + (x - width / 2) + ", x + width / 2 = " + (x + width / 2) + ", d = " + d);
					fillBox(g, x - width / 2, 0, x + width / 2, d);
					g.setColor(ModelerColor.HISTOGRAM_BAR_OUTLINE); // Color.black. This is the bar border. annie che.
					drawBox(g, x - width / 2, 0, x + width / 2, d);
				}

			}
			//Draw summary statistics
			if (modelType == Modeler.CONTINUOUS_DISTRIBUTION_TYPE
				|| modelType == Modeler.DISCRETE_DISTRIBUTION_TYPE) {

				g.setColor(ModelerColor.MODEL_OUTLINE); // Color.red
				int j = getSize().height - 10;
				switch(summaryStats){
				case 1:		//True mean and standard deviation
					fillBoxPlot(g, data.getMean(), data.getSD(), j);
					break;
				case 2:		//Interval mean and standard deviation
					fillBoxPlot(g, data.getIntervalMean(), data.getIntervalSD(), j);
					break;
				case 3:		//Five number summary
					fillBoxPlot(g, data.getMinValue(), data.getQuartile(1), data.getMedian(), data.getQuartile(3), data.getMaxValue(), j);
					break;
				case 4:		//mMedian and mean absolute deviation
					fillBoxPlot(g, data.getMedian(), data.getMAD(), j);
					break;
				}
			}
  		}
	}

	 /**This method assigns the data and sets up graph paramters*/
	public void setIntervalData(IntervalData d){
		data = d;
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
		repaint();
	}

	public void setxMax(double xm) {
		xMax = xm;
	}
	public void setxMin(double xm) {
		xMin = xm;
	}
	public void setyMax(double ym) {
		yMax = ym;
	}

     // added for varHistogram's sub classes like ModelHostogram (its only child so far!).
 	public void setPlotXMin(double input) {
		this.xMin = input;
	}
	public void setPlotXMax(double input) {
		this.xMax = input;
	}
	public void setPlotYMax(double input) {
		this.yMax = input;
	}
	public void setPlotYMin(double input) {
		this.yMin = input;
	}
	public void setModelType(int modelType) {
		this.modelType = modelType;
	}
	public void setListOfTicks(ArrayList listOfTicks) {
		this.listOfTicks = listOfTicks;
	}
	public ArrayList getListOfTicks(int modelType) {
		return this.listOfTicks;
	}



}
