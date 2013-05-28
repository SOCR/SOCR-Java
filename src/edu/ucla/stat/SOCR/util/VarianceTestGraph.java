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
import edu.ucla.stat.SOCR.distributions.*;

/**This class models a special graph used in the hypothesis testing experiment for the variance
in the standard normal model.*/
public class VarianceTestGraph extends RandomVariableGraph{
	//Variables
	private double mean, stdDev, testStdDev;
	private IntervalData data;

	/**This general constructor creates a ndw variance test graph with a specified random
	variable and a specified test standard deviation.*/
	public VarianceTestGraph(RandomVariable v, double t){
		super(v);
		showMoments(0);
		testStdDev = t;
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw Mean, standard deviation bar
		g.setColor(Color.blue);
		int j = getSize().height - 10;
		drawBoxPlot(g, mean - testStdDev, mean - stdDev, mean, mean + stdDev, mean + testStdDev, j);
		if (data.getSize() > 0){
			g.setColor(Color.red);
			fillBoxPlot(g, mean, data.getSD(), j);
		}
	}

	/**This method sets the test standard deviation.*/
	public void setTestStdDev(double t){
		testStdDev = t;
	}

	/**This method resets the graph.*/
	public void reset(){
		super.reset();
		RandomVariable v = getRandomVariable();
		mean = v.getDistribution().getMean();
		stdDev = v.getDistribution().getSD();
		data = v.getIntervalData();
	}
}


