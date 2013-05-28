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

/**This class defines a special graph used in the hypothesis testing experiment for the mean
in the standard normal model.*/
public class MeanTestGraph extends RandomVariableGraph{
	//Variables
	private double testMean;

	/**This general constructor creates a new mean test graph with a specified random variable
	and a specified test mean.*/
	public MeanTestGraph(RandomVariable v, double tm){
		super(v);
		showMoments(0);
		testMean = tm;
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		RandomVariable v = getRandomVariable();
		//Draw Mean
		g.setColor(Color.blue);
		double y0 = yScale(getSize().height - 10);
		drawTick(g, v.getDistribution().getMean(), y0, 6, 6, VERTICAL);
		//Draw test Mean
		g.setColor(Color.green);
		drawTick(g, testMean, y0, 6, 6, VERTICAL);
		if (v.getIntervalData().getSize() > 0){
			g.setColor(Color.red);
			drawTick(g, v.getIntervalData().getMean(), y0, 6, 6, VERTICAL);
		}
	}

	/**This method sets the test mean.*/
	public void setTestMean(double tm){
		testMean = tm;
	}
}


