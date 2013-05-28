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

/**This is a special graph used in the sign test experiment.  The graph shows the getDensity
of the random variable, the median, and an hypothesized test median.*/
public class MedianGraph extends RandomVariableGraph{
	double median, testMedian;

	/**This general constructor creates a new median graph with a specified random variable and
	a specified test median.*/
	public MedianGraph(RandomVariable v, double m){
		super(v);
		testMedian = m;
		showMoments(0);
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw Medians
		g.setColor(Color.blue);
		double y0 = yScale(getSize().height - 10);
		drawTick(g, median, y0, 6, 6, VERTICAL);
		if (Math.abs(testMedian - median) < 0.001) g.setColor(Color.black);
		else g.setColor(Color.green);
		drawTick(g, testMedian, y0, 6, 6, VERTICAL);
	}

	/**This method resets the graph.*/
	public void reset(){
		super.reset();
		median = getRandomVariable().getDistribution().getMedian();
	}

	/**This method sets the test median to a specified value.*/
	public void setTestMedian(double m){
		testMedian = m;
	}
}
