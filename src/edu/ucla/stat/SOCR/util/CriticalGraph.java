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

/**This class defines a graph used in interval estimation and hypothesis testing experiments.
The graph shows the getDensity of specified random varaiable and an interval along the x-axis.
The random critical value is shown as a red vertical line.  The event of interest is whether
the critical value falls in the the specified interva.*/
public class CriticalGraph extends RandomVariableGraph{
	//Variables
	private double lowerCritical, upperCritical;

	/**This general constructor creates a new random variable graph with a specified random
	variable.*/
	public CriticalGraph(RandomVariable v){
		super(v);
		showMoments(0);
	}

	/**This method sets the critical values.  These are used to define the interval that is
	shown as a horizontal blue bar.*/
	public void setCriticalValues(double lc, double uc){
		if (lc > Double.NEGATIVE_INFINITY) lowerCritical = lc;
		else lowerCritical = xScale(0);
		if (uc < Double.POSITIVE_INFINITY) upperCritical = uc;
		else upperCritical = xScale(getSize().width);
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.setColor(Color.blue);
		double y0 = yScale(getSize().height - 10);
		drawBox(g, HORIZONTAL, lowerCritical, y0, upperCritical - lowerCritical, 3, 3);
	}
}

