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

/**This class defines a special graph used in the mean estimation experiment.*/
public class MeanEstimateGraph extends RandomVariableGraph{
	//Variables
	private double lowerEstimate, upperEstimate;

	/**This general constructor creates a new mean estimate graph with a specified random
	variable.*/
	public MeanEstimateGraph(RandomVariable v){
		super(v);
		showMoments(0);
	}

	/**This method sets the lower and upper bounds of the interval estimate.*/
	public void setEstimates(double le, double ue){
		if (le > Double.NEGATIVE_INFINITY) lowerEstimate = le;
		else lowerEstimate = xScale(0);
		if (ue < Double.POSITIVE_INFINITY) upperEstimate = ue;
		else upperEstimate = xScale(getSize().width);
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		RandomVariable v = getRandomVariable();
		//Draw Mean
		g.setColor(Color.blue);
		int j = getSize().height - 10;
		double y0 = yScale(j);
		drawTick(g, v.getDistribution().getMean(), y0, 6, 6, VERTICAL);
		if (v.getIntervalData().getSize() > 0){
			g.setColor(Color.red);
			fillBoxPlot(g, v.getIntervalData().getMean(), (upperEstimate - lowerEstimate) / 2, j);
		}
	}

}


