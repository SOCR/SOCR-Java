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

/**This class shows the getDensity function or cumulative distribution function of a specified distribuiton.
A specified value and getQuantile are shown.*/
public class QuantileGraph extends Graph{
	//Constants
	public final static int PDF = 0, CDF = 1;
	//Variables
	private double xMin, xMax, yMax, width, getQuantile;
	private int type = PDF, values;
	//Objects
	private Distribution distribution;
	private Domain domain;

	/**This general constructor creates a new getQuantile graph with a specified distribution
	and getQuantile.*/
	public QuantileGraph(Distribution distribution, double getQuantile){
		setMargins(35, 20, 20, 20);
		setDistribution(distribution);
		setQuantile(getQuantile);
	}

	/**This special constructor creates a new getQuantile graph with a specified distribution and the
	median (quanitle of order 0.5).*/
	public QuantileGraph(Distribution distribution){
		this(distribution, distribution.getMedian());
	}

	/**This default constructor creates a new getQuantile graph with the standard normal distribution
	and the median.*/
	public QuantileGraph(){
		this(new NormalDistribution(0, 1), 0);
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double x1, x2, y1, y2;
		Polygon polygon;
		//Draw axes
		g.setColor(Color.black);
		drawAxis(g, 0, yMax, 0.1 * yMax, xMin, VERTICAL);
		drawAxis(g, domain, 0, HORIZONTAL, distribution.getType());
		//Draw graph
		//Continous PDF
		if ((distribution.getType() == distribution.CONTINUOUS) & (type == PDF)){
			for (int i = 0; i < values; i++){
				x1 = domain.getBound(i);
				x2 = x1 + width;
				y1 = distribution.getDensity(x1);
				y2 = distribution.getDensity(x2);
				if (x1 <= getQuantile){
					g.setColor(Color.red);
					int[] xPoints = {xGraph(x1), xGraph(x1), xGraph(x2), xGraph(x2)};
					int[] yPoints = {yGraph(0), yGraph(y1), yGraph(y2), yGraph(0)};
					polygon = new Polygon(xPoints, yPoints, 4);
					g.fillPolygon(polygon);
				}
				else{
					g.setColor(Color.blue);
					drawLine(g, x1, y1, x2, y2);
				}
			}
		}
		//Continuous CDF
		else if ((distribution.getType() == distribution.CONTINUOUS) & (type == CDF)){
			g.setColor(Color.blue);
			for (int i = 0; i < values; i++){
				x1 = domain.getBound(i);
				x2 = x1 + width;
				y1 = distribution.getCDF(x1);
				y2 = distribution.getCDF(x2);
				drawLine(g, x1, y1, x2, y2);
			}
			g.setColor(Color.red);
			y1 = distribution.getCDF(getQuantile);
			drawLine(g, getQuantile, 0, getQuantile, y1);
			drawLine(g, xMin, y1, getQuantile, y1);
		}
	}

	/**This method sets the distribution.*/
	public void setDistribution(Distribution d){
		distribution = d;
		domain = distribution.getDomain();
		xMin = domain.getLowerBound();
		xMax = domain.getUpperBound();
		values = domain.getSize();
		width = domain.getWidth();
		setType(type);
	}

	/**This method returns the distribution.*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**This method sets the getQuantile.*/
	public void setQuantile(double getQuantile){
		this.getQuantile = getQuantile;
	}

	/**This method specifies the minimum size.*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}

	/**This method specifies the preferred size.*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}

	/**This method sets the graph type to show either the PDF or CDF.*/
	public void setType(int t){
		type = t;
		if (type == CDF) yMax = 1;
		else yMax = 1.2 * distribution.getMaxDensity();
		setScale(xMin, xMax, 0, yMax);
		repaint();
	}
}

