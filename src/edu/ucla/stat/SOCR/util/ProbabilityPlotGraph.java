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

/**This class models the probability plot graph in the probability plot
experiment.*/
public class ProbabilityPlotGraph extends Graph{
	double[] orderStatistics;
	double[] testQuantiles;
	public boolean haveData;

	/**This general constructor creates a new probability plot graph with
	specified upper and lower bounds.*/
	public ProbabilityPlotGraph(double xMin, double xMax, double yMin, double yMax){
		super(xMin, xMax, yMin, yMax);
	}

	/**This method draws the probability plot graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw x axis
		double xMin = getXMin(), xMax = getXMax(), yMin = getYMin(), yMax = getYMax();
		g.setColor(Color.black);
		drawLine(g, xMin, yMin, xMax, yMin);
		for (double x = xMin; x <= xMax; x++) drawTick(g, x, yMin, VERTICAL);
		drawLabel(g, format(xMin), xMin, yMin, BELOW);
		drawLabel(g, format(xMax), xMax, yMin, BELOW);
		//Draw y axis
		drawLine(g, xMin, yMin, xMin, yMax);
		for (double y = yMin; y <= yMax; y++) drawTick(g, xMin, y, HORIZONTAL);
		drawLabel(g, format(yMin), xMin, yMin, LEFT);
		drawLabel(g, format(yMax), xMin, yMax, LEFT);
		g.setColor(Color.red);
		if (haveData){
			for (int i = 0; i < testQuantiles.length; i ++)	drawPoint(g, testQuantiles[i], orderStatistics[i]);
		}
	}

	/**This method sets the test getQuantiles.*/
	public void setQuantiles(double[] tq){
		testQuantiles = tq;
	}

	/**This method sets the order statistics.*/
	public void setStatistics(double[] os){
		orderStatistics = os;
		haveData = true;
	}
}

