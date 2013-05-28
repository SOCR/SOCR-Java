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

/**This class models the scatterplot for Buffon's coin experiment*/
public class CoinScatter extends ScatterPlot{
	private double radius;

	/**This general constructor creates a new Buffon coin scatterplot with a
	specified radius*/
	public CoinScatter(double r){
		super(-0.5, 0.5, -0.5, 0.5);
		setRadius(r);
	}

	/**This method sets the radius*/
	public void setRadius(double r){
		//Correct invalid parameter
		if (r < 0) r = 0;
		if (r > 0.5) r = 0.5;
		radius = r;
	}

	/**This method gets the radius*/
	public double getRadius(){
		return radius;
	}

	/**This method paints the scatterplot*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw the event
		g.setColor(Color.blue);
		drawLine(g, radius - 0.5, radius - 0.5, 0.5 - radius, radius - 0.5);
		drawLine(g, radius - 0.5, 0.5 - radius, 0.5 - radius, 0.5 - radius);
		drawLine(g, radius - 0.5, radius - 0.5, radius - 0.5, 0.5 - radius);
		drawLine(g, 0.5 - radius, radius - 0.5, 0.5 - radius, 0.5 - radius);
		//Draw axes and points
	}
}

