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
import java.awt.event.*;
import edu.ucla.stat.SOCR.distributions.*;

/**This class models an interactive scatterplot*/
public class UserScatterPlot extends ScatterPlot implements MouseListener, MouseMotionListener{
	private IntervalData xData, yData;
	private int n;
	private double xMouse, yMouse;
	private double sum, covariance, correlation;
	private double slope, intercept;
	private boolean sampleLines;

	/**This general constructor creates a new user scatterplot correpsonding to specified
	intervals*/
	public UserScatterPlot(double x0, double x1, double s, double y0, double y1, double t){
		super(x0, x1, s, y0, y1, t);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		xData = new IntervalData(x0, x1, s);
		yData = new IntervalData(y0, y1, t);
		setSize(250, 250);
	}

	/**This method paints the scatterplot*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		if (sampleLines){
			sampleLines = false;
			drawSampleLines();
		}
	}

	/**This method draws the sample lines*/
	public void drawSampleLines(){
		double y1, y2;
		Graphics g = getGraphics();
		double xMin = xData.getDomain().getLowerBound(), xMax = xData.getDomain().getUpperBound();
		g.setXORMode(Color.green);
		drawLine(g, xData.getMean() - xData.getSD(), yData.getMean(), xData.getMean() + xData.getSD(), yData.getMean());
		g.setXORMode(Color.blue);
		drawLine(g, xData.getMean(), yData.getMean() - yData.getSD(), xData.getMean(), yData.getMean() + yData.getSD());
		y1 = intercept + slope * xMin;
		y2 = intercept + slope * xMax;
		g.setXORMode(Color.red);
		drawLine(g, xMin, y1, xMax, y2);
		sampleLines = true;
		g.dispose();
	}

	/**This method handles the mouse click event.  The new point is added to the dataset*/
	public void mouseClicked(MouseEvent event){
		xMouse = xScale(event.getX());
		yMouse = yScale(event.getY());
		if (xData.getDomain().getLowerBound() < xMouse & xMouse < xData.getDomain().getUpperBound()
			& yData.getDomain().getLowerBound() < yMouse & yMouse < yData.getDomain().getUpperBound()){
			n++;
			if (sampleLines) drawSampleLines();
			drawPoint(xMouse, yMouse);
			xData.setValue(xMouse);
			yData.setValue(yMouse);
			sum = sum + xMouse * yMouse;
			covariance = (sum -  n * xData.getMean() * yData.getMean()) / (n - 1);
			correlation = covariance / (xData.getSD() * yData.getSD());
			slope = covariance / xData.getVariance();
			intercept = yData.getMean() - slope * xData.getMean();
			if (n > 1) drawSampleLines();
		}
	}

	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}

	/**This method handles the mouse move event.  The coordinates of the mouse (in scale
	units) are tracked*/
	public void mouseMoved(MouseEvent event){
		xMouse = xScale(event.getX());
		yMouse = yScale(event.getY());
	}

	/**This method resets the experiment*/
	public void reset(){
		super.reset();
		sum = 0;
		n = 0;
		xData.reset();
		yData.reset();
		sampleLines = false;
	}

	/**This method returns the covariance*/
	public double getCogetVariance(){
		return covariance;
	}

	/**This method returns the correlation*/
	public double getCorrelation(){
		return correlation;
	}

	/**This method returns the x coordinate of the mouse*/
	public double getXMouse(){
		return xMouse;
	}

	/**This method returns the y coordinate of the mouse*/
	public double getYMouse(){
		return yMouse;
	}

	/**This method returns the x data*/
	public IntervalData getXData(){
		return xData;
	}

	/**This method returns the y data*/
	public IntervalData getYData(){
		return yData;
	}
}

