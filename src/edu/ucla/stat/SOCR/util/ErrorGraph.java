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

/**This class defines a special graph used in conjunction with an interactive histogram.  The graph shows
the mean square error function or the mean absolute error function corresponding to a data set.*/
public class ErrorGraph extends Graph{
	//Variables
	private int errorType;
	private IntervalData data;

	/**This general constructor creates a new error graph of a specified type and with a specified
	data set.*/
	public ErrorGraph(IntervalData d, int t){
		data = d;
		errorType = t;
		setMargins(35, 20, 30, 10);
	}

	/**This method paints the graph.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		Domain domain = data.getDomain();
		double xMin = domain.getLowerBound(), xMax = domain.getUpperBound(), yMax, x0, x1, y0, y1, c;
		if (data.getSize() > 0) yMax = Math.rint(Math.max(1, Math.max(error(xMin), error(xMax))));
		else yMax = 1;
		setScale(xMin, xMax, 0, yMax);
		//Draw x axis
		g.setColor(Color.black);
		drawAxis(g, domain, 0, HORIZONTAL, BOUNDS);
		//Draw y axis
		drawAxis(g, 0, yMax, 0.1 * yMax, domain.getLowerBound(), VERTICAL);
		//Draw graph of error function
		g.setColor(Color.red);
		if (data.getSize() > 0){
			for (int i = 0; i < domain.getSize(); i++){
				x0 = domain.getBound(i);
				x1 = domain.getBound(i + 1);
				y0 = error(x0);
				y1 = error(x1);
				drawLine(g, x0, y0, x1, y1);
			}
			c = getCenter();
			drawLine(g, c, 0, c, error(c));
		}
	}

	/**This method computes the error function.*/
	public double error(double t){
		Domain domain = data.getDomain();
		double sum = 0, x;
		for (int i = 0; i < domain.getSize(); i++){
			x = domain.getValue(i);
			if (errorType == 0) sum = sum + data.getRelFreq(x) * (t - x) * (t - x);
			else sum = sum + data.getRelFreq(x) * Math.abs(t - x);
		}
		return sum;
	}

	/**This method returns the center:  the mean for type 0 and the median for type 1.*/
	public double getCenter(){
		if (errorType == 0) return data.getMean();
		else return data.getMedian();
	}

	/**This method sets the data set.*/
	public void setData(IntervalData d){
		data = d;
		repaint();
	}

	/**This method sets the error type.*/
	public void setErrorType(int errorType){
		this.errorType = errorType;
		repaint();
	}
}

