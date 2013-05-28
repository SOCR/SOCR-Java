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

/**This class defines the specialized scatterplot for the triangle experiment*/
public class TriangleScatter extends ScatterPlot {

	/**This default constructor creates a new triangle scatterplot*/
	public TriangleScatter(){
		super(0, 1, 0, 1);
	}

	/**This method paints the scatterplot by drawing the event curves and then calling the
	corresponding method in the superclass.*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double x1, y, y1;
		g.setColor(Color.blue);
		drawLine(g, 0.5, 0, 0.5, 1);
		drawLine(g, 0, 0.5, 1, 0.5);
		drawLine(g, 0.5, 0, 1, 0.5);
		drawLine(g, 0, 0.5, 0.5, 1);
		for (double x = 0; x + 0.01 <= 0.5; x = x + 0.01){
			x1 = x + 0.01;
			y = 1 / (2 * (1 - x));
			y1 = 1 / (2 * (1 - x1));
			drawLine(g, x, y, x1, y1);
	  		drawLine(g, y, x, y1, x1);
		}
		for (double x = 0; x + 0.01 <= 0.5; x = x + 0.01){
			x1 = x + 0.01;
			y = (1 - 2 * x * x) / (2 * (1 - x));
			y1 = (1 - 2 * x1 * x1) / (2 * (1 - x1));
			drawLine(g, x, y, x1, y1);
	  		drawLine(g, y, x, y1, x1);
		}
		for (double x = 0.5; x + 0.01 <= 1; x = x + 0.01){
			x1 = x + 0.01;
			y = (1 - 2 * x + 2 * x * x) / (2 * x);
			y1 = (1 - 2 * x1 + 2 * x1 * x1) / (2 * x1);
			drawLine(g, y, x, y1, x1);
	  		drawLine(g, x, y, x1, y1);
		}
	}
}
