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


/**This class models a scatterplot for a bivariate distribution.  The means, standard
deviations, and regression line are shown*/
public class BivariateScatterPlot extends ScatterPlot{
	double distSlope, distIntercept, sampleSlope, sampleIntercept;
	double xMin, xMax, yMin, yMax;
	boolean sampleLine = false;

	public BivariateScatterPlot(double x0, double x1, double s, double y0, double y1, double t){
		super(x0, x1, s, y0, y1, t);
		xMin = x0; xMax = x1;
	}

	public void setParameters(double distSlope, double distIntercept){
		this.distSlope = distSlope;
		this.distIntercept = distIntercept;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.setColor(Color.blue);
		double y1 = distIntercept + distSlope * xMin;
		double y2 = distIntercept + distSlope * xMax;
		drawLine(g, xMin, y1, xMax, y2);
		if (sampleLine){
		sampleLine = false;
		drawSampleLine(sampleSlope, sampleIntercept);
		}
	}

	public void drawSampleLine(double m, double b){
		double y1, y2;
		Graphics g = this.getGraphics();
		g.setXORMode(Color.green);
		if (sampleLine){
			y1 = sampleIntercept + sampleSlope * xMin;
			y2 = this.sampleIntercept + this.sampleSlope * xMax;
			drawLine(g, xMin, y1, xMax, y2);
		}
		sampleSlope = m;
		sampleIntercept = b;
		y1 = sampleIntercept + sampleSlope * xMin;
		y2 = sampleIntercept + sampleSlope * xMax;
		drawLine(g, xMin, y1, xMax, y2);
		sampleLine = true;
	}

	public void reset(){
		super.reset();
		sampleLine = false;
	}
}

