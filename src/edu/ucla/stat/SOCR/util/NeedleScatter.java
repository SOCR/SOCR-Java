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

/**Scatterplot for Buffon's needle experiment*/
public class NeedleScatter extends ScatterPlot{
	//Variables
	private double length;

	/**General construct: creates a new needle scatterplot with a specified needle length*/
	public NeedleScatter(double l){
		super(0, Math.PI, 0, 1);
		setLength(l);
	}

	/**Default constructor: creates a new scatterplot with needle length 1/2*/
	public NeedleScatter(){
		this(0.5);
	}

	/**Set the needle length*/
	public void setLength(double l){
		//Correct for invalid parameter value
		if (l < 0) l = 0; else if (l > 1) l = 1;
		length = l;
	}

	/**Get the needle length*/
	public double getLength(){
		return length;
	}

	/**Paint the graph*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double x1, y0, y1;
		//Draw curves
		g.setColor(Color.blue);
		for (double x = 0; x + 0.1 <= Math.PI; x = x + 0.1){
			x1 = x + 0.1;
			y0 = 0.5 * length * Math.sin(x);
			y1 = 0.5 * length * Math.sin(x1);
			drawLine(g, x, y0, x1, y1);
			drawLine(g, x, 1 - y0, x1, 1 - y1);
		}
	}
}

