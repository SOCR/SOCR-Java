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
import edu.ucla.stat.SOCR.distributions.RandomVariable;

/**This class defines a special graph used in the interval estimate experiment for the variance.*/
public class VarianceEstimateGraph extends RandomVariableGraph{
	//Variables
	private double lowerEstimate, upperEstimate, mean, stdDev;

	/**This general constructor creates a new variance estimate graph corresponding to a
	specified random variable.*/
	public VarianceEstimateGraph(RandomVariable v){
		super(v);
		showMoments(0);
		}

	/**This method sets the interval estimate.*/
	public void setEstimates(double lowerEstimate, double upperEstimate){
		this.lowerEstimate = lowerEstimate;
		if (upperEstimate < Double.POSITIVE_INFINITY) this.upperEstimate = upperEstimate;
		else this.upperEstimate = xScale(getSize().width);
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		RandomVariable v = getRandomVariable();
		double mean = v.getDistribution().getMean(),
			stdDev = v.getDistribution().getSD();
		//Draw Mean
		g.setColor(Color.blue);
		int j = getSize().height - 10;
		drawBoxPlot(g, mean, stdDev, j);
		if (v.getIntervalData().getSize() > 0){
			g.setColor(Color.red);
			fillBoxPlot(g, mean - upperEstimate, mean - lowerEstimate,
				mean, mean + lowerEstimate, mean + upperEstimate, j);
		}
	}
}


