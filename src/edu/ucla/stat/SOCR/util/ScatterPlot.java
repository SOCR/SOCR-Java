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
import java.awt.geom.*;
import java.util.*;

/**This class defines a basic two-dimensional scatterplot that can be sub-classed.*/
public class ScatterPlot extends Graph{
	//Variables
	private Vector<Double> xCoordinates = new Vector<Double>();
	private Vector<Double> yCoordinates = new Vector<Double>();
	private double xMin, xMax, xStep, yMin, yMax, yStep;

	/**This general constructor creates a new scatterplot on the rectangle
	[x0, x1] x [y0, y1] with specified step sizes*/
	public ScatterPlot(double x0, double x1, double s, double y0, double y1, double t){
		super(x0, x1, y0, y1);
		setMargins(25, 20, 25, 20);
		setParameters(x0, x1, s, y0, y1, t);
	}

	/**This special constructor creates a new scatterplot corresponding to the specified intervals,
	each divided into 10 subintervals*/
	public ScatterPlot(double x0, double x1, double y0, double y1){
		this(x0, x1, 0.1 * (x1 - x0), y0, y1, 0.1 * (y1 - y0));
	}

	/**This method sets the parameters:  the partitions of the intervals.*/
	public void setParameters(double x0, double x1, double s, double y0, double y1, double t){
		setScale(x0, x1, y0, y1);
		xStep = s; yStep = t;
		xMin = x0; xMax = x1; yMin = y0; yMax = y1;
	}

	/**This method sets the parameters: the intervals.*/
	public void setParameters(double x0, double x1, double y0, double y1){
		setParameters(x0, x1, 0.1 * (x1 - x0), y0, y1, 0.1 * (y1 - y0));
	}

	/**This method paint the scatterplots, including the x and y axes and the boundary box.
	The data points are plotted in red*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double step;
  		//Draw x axis
		g.setColor(Color.black);
		drawAxis(g, xMin, xMax, xStep, yMin, HORIZONTAL);
		//Draw y axis
		drawAxis(g, yMin, yMax, yStep, xMin, VERTICAL);
		//Draw bounding box
		g.setColor(Color.gray);
		drawLine(g, xMax, yMin, xMax, yMax);
		drawLine(g, xMin, yMax, xMax, yMax);
		//Draw points
		if (! xCoordinates.isEmpty()){
			g.setColor(Color.red);
			for (int i = 0; i < xCoordinates.size(); i++){
				drawPoint(g, getX(i), getY(i));
			}
		}
	}

	/**This method adds a specified data point to the dataset*/
	public void addPoint(double x, double y){
		xCoordinates.addElement(new Double(x));
		yCoordinates.addElement(new Double(y));
	}

	/**This method adds a specified data point to the data and
	 * draws the point in the scatterplot*/
	public void drawPoint(double x, double y){
		addPoint(x, y);
		Graphics g = getGraphics();
		g.setColor(Color.red);
		drawPoint(g, x, y);

	}

	/**This method adds a random data point to the dataset*/
	public void addPoint(){
		addPoint(xMin + (xMax - xMin) * Math.random(), yMin + (yMax - yMin) * Math.random());
	}

	/**This method adds a random data point to the dataset and draws
	 * the point in the scatterplot*/
	public void drawPoint(){
		drawPoint(xMin + (xMax - xMin) * Math.random(), yMin + (yMax - yMin) * Math.random());
	}

	/**This method gets the x-coordinate at index i*/
	public double getX(int i){
		return ((Double)xCoordinates.elementAt(i)).doubleValue();
	 }

	/**This method gets the y-coordinate at index i*/
	public double getY(int i){
		return ((Double)yCoordinates.elementAt(i)).doubleValue();
	 }

	/**This method clears the dataset */
	public void clear(){
		xCoordinates.removeAllElements();
		yCoordinates.removeAllElements();
	}

	/**This method resets the scatterplot by clear the data and redrawing*/
	public void reset(){
		clear();
		repaint();
	}

	 /**Specify the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}

	/**Specify the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}
}