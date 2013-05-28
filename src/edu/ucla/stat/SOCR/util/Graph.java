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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.ucla.stat.SOCR.distributions.Domain;
import edu.ucla.stat.SOCR.modeler.gui.ModelerColor;
import edu.ucla.stat.SOCR.modeler.gui.ModelerDimension;

/** This class defines a basic two-dimensional graph to be sub-classed. The class
provides basic drawing methods*/
public class Graph extends JPanel {
	//Variables
	public int pointSize = 2, leftMargin, rightMargin, topMargin, bottomMargin;
	public double xMin, xMax, yMin, yMax;
	//Constants
	public final static int LEFT = 0, RIGHT = 1, ABOVE = 2, BELOW = 3;
	public final static int VERTICAL = 0, HORIZONTAL = 1;
	public final static int MIDPOINTS = 0, BOUNDS = 1;
	//Objects
	private Font f = new Font("sansserif", Font.PLAIN, 11);
	private DecimalFormat decimalFormat = new DecimalFormat();
	protected int currentXUpperBound = -5;
	protected int currentXLowerBound = 5;
	
	protected boolean showModelDistribution = true;
	//Constructors

	/**This general constructor creates a new graph with specified ranges on the x an
	y axes*/
	public Graph(double x0, double x1, double y0, double y1){
		////System.out.println("Graph constructor 4 doubles");
          setScale(x0, x1, y0, y1);
		setMargins(ModelerDimension.GRAPH_MARGIN_LEFT, ModelerDimension.GRAPH_MARGIN_RIGHT, ModelerDimension.GRAPH_MARGIN_BOTTOM, ModelerDimension.GRAPH_MARGIN_TOP);
		setBackground(Color.white);
		setFont(f);
	}

	/**This default constructor creates a new graph with x and y between 0 and 1*/
	public Graph(){
		this(0, 1, 0, 1);
	}

	//Methods for setting and getting scale parameters

	/**This method sets the minimum and maximum values on the x and y axes*/
	public void setScale(double x0, double x1, double y0, double y1){
		////System.out.println("Graph setScale x0 = " + x0);
		xMin = x0; xMax = x1;
		yMin = y0; yMax = y1;
	}

	/**This method returns the minimum x value*/
	public double getXMin(){
		return xMin;
	}

	/**This method returns the maximum x value*/
	public double getXMax(){
		return xMax;
	}

	/**This method returns the minimum y value*/
	public double getYMin(){
		return yMin;
	}

	/**This method returns the maximum y value*/
	public double getYMax(){
		return yMax;
	}

	//Methods for setting and getting margin parameters

	/**This method sets the margin (in pixels)*/
	public void setMargins(int l, int r, int b, int t){
		leftMargin = l; rightMargin = r;
		bottomMargin = b; topMargin = t;
	}

	//Methods for converting between graph units (in pixels) and scale units

	/**This method computes the x coordinate in graph units for a given x in scale units*/
	public int xGraph(double x){
		////////System.out.println("util.Graph xGraph x = " + x);

		int result = leftMargin + (int)(((x - xMin)/(xMax - xMin)) * (getSize().width - leftMargin - rightMargin));
		////////System.out.println("util.Graph xGraph x result = " + result);

		return result;
	}

	/**This method computes the y coordinate in graph units for a given y in scale units*/
	public int yGraph(double y){
		////////System.out.println("util.Graph yGraph y = " + y);

		int result = getSize().height - bottomMargin - (int)(((y - yMin)/(yMax - yMin)) * (getSize().height - bottomMargin - topMargin));
		////////System.out.println("util.Graph yGraph y result ===== " + result);

		return result;
	}
	/**This method computes the x coordinate in graph units for a given x in scale units*/
	public double xGraphDouble(double x){
		////////System.out.println("util.Graph xGraph x = " + x);

		double result = leftMargin + (double)(((x - xMin)/(xMax - xMin)) * (getSize().width - leftMargin - rightMargin));
		////////System.out.println("util.Graph xGraph x result = " + result);

		return result;
	}

	/**This method computes the y coordinate in graph units for a given y in scale units*/
	public double yGraphDouble(double y){
		////////System.out.println("util.Graph yGraph y = " + y);

		double result = getSize().height - bottomMargin - (double)(((y - yMin)/(yMax - yMin)) * (getSize().height - bottomMargin - topMargin));
		////////System.out.println("util.Graph yGraph y result ===== " + result);

		return result;
	}
	/**This method computes the x scale units for a given x in coordinate in graph units*/
	public double xGraphInverse(double xInput){
		double term =  (getSize().width - leftMargin - rightMargin);
		double result = (xMax - xMin)  * (xInput - leftMargin) / term + xMin;
		return result;
	}
	/**This method computes the y scale units for a given y in coordinate in graph units*/
	public double yGraphInverse(double yInput){
		double term =  (getSize().height - bottomMargin - topMargin);
		double result = (- yMax + yMin)  * (yInput - (getSize().height - bottomMargin)) / term + yMin;

		return result;
	}

	/**This method computes the x coordinate in scale units for a given x in graph units*/
	public double xScale(int x){
		return xMin + ((double)(x - leftMargin)/(getSize().width - leftMargin - rightMargin)) * (xMax - xMin);
	}

	/**This method computes the y coordinate in scale units for a given y in graph units*/
	public double yScale(int y){
		return yMin + ((double)(getSize().height - y - bottomMargin)/(getSize().height  - bottomMargin - topMargin)) * (yMax - yMin);
	}

	/**This method convert x scale units to x pixels*/
	public int xPixels(double x){
		return xGraph(xMin + x) - xGraph(xMin);
	}

	/**This method converts y scale units to y pixels*/
	public int yPixels(double y){
		return yGraph(yMin + y) - yGraph(yMin);
	}

	//Methods for drawling lines, labels and axes

	/**This method draws a line between (x1, y1) and (x2, y2), where the coordinates are in scale units*/
	public void drawLine(Graphics g, double x1, double y1, double x2, double y2){
		//////System.out.println("Graph drawLine x1 = " + x1 + ", y1 = " + y1 + ", x2 = " + x2 + ", y2 = " + y2 + ", color = " + g.getColor());
		double m = 1;
		int x1result = (int) (m * xGraph(x1));
		int y1result = (int) (m * yGraph(y1));
		int x2result = (int) (m * xGraph(x2));
		int y2result = (int) (m * yGraph(y2));
		////////System.out.println("Graph drawLine p1 = c(" + x1result + ", " + y1result + "); p2 = c(" + x2result + ", " + y2result + ");");
		g.drawLine(xGraph(x1), yGraph(y1), xGraph(x2), yGraph(y2));

	}

	/**This method draws a tick mark at the specified x and y coordinates (in scale units),
	i pixels in the negative direction, j in the positive direction. The variable s is style, either horizontal or vertical. */
	public void drawTick(Graphics g, double x, double y, int i, int j, int orientation){
		int a = xGraph(x), b = yGraph(y);
		if (orientation == VERTICAL) g.drawLine(a, b - j, a, b + i);
		else g.drawLine(a - i, b, a + j, b);
	}

	/**This method draws a tick mark at the specified x and y coordinates (in scale units), 3 pixels in the positive direction and 3 in the negative direction*/
	public void drawTick(Graphics g, double x, double y, int orientation){
		drawTick(g, x, y, 3, 3, orientation);
	}

	/**This method draws label s to the left, right, above, or below (x, y) (in scale units)*/
	public void drawLabel(Graphics g, String s, double x, double y, int location){
		//////System.out.println("Graph drawLabel s = " + s + ", x = " + x + ", y = " + y + ", location = " + location);
		// s = label of the x- or y-axis's left/right most points.
		FontMetrics fm = this.getFontMetrics(this.getFont());
		int h = fm.getHeight(), w = fm.stringWidth(s);
		switch(location){
		case LEFT:
			g.drawString(s, xGraph(x) - w - 3, yGraph(y) + h / 2);
			break;
		case RIGHT:
			g.drawString(s, xGraph(x) + 3, yGraph(y) + h / 2);
			break;
		case ABOVE:
			g.drawString(s, xGraph(x) - w / 2, yGraph(y) + 1);
			break;
		case BELOW:
			g.drawString(s, xGraph(x) - w / 2, yGraph(y) + h);
			break;
		}
	}

	/**This method draws an axis corresponding to a partition of an interval at position c relative to the other variable. The orientation indicates style, either horizongal or vertical. The type means that either the midpoints or the bounds are indicated with tick marks. The pattern is the format pattern for the labels*/
	public void drawAxis(Graphics g, Domain domain, double c, int orientation, int type){
		this.drawAxisWithDomain(g, domain, c, orientation, type, null);
	}
	public void drawAxis(Graphics g, Domain domain, double c, int orientation, int type, ArrayList list){
		this.drawAxisWithDomain(g, domain, c, orientation, type, list);
	}
	protected void drawAxisWithDomain(Graphics g, Domain domain, double c, int orientation, int type, ArrayList list){
//System.out.println("******Graph drawAxisWithDomain type="+type +" ArryList="+list);
		double t;
		double currentUpperBound = domain.getUpperBound(); // of the model (distribution)
		double currentLowerBound = domain.getLowerBound();
		int domainSize = domain.getSize();
		if (orientation == HORIZONTAL){
			this.drawLine(g, currentLowerBound, c, currentUpperBound, c);
			//Draw tick marks, depending on type
			for (int i = 0; i < domainSize; i++){
				if (type == MIDPOINTS) {
					t = domain.getValue(i);
				} else {
					t = domain.getBound(i);
				}
				g.setColor(ModelerColor.HISTOGRAM_TICKMARK);
				//g.setStroke(new BasicStroke(3.05f));
				//System.out.println("******Graph drawAxisWithDomain draw tick at t="+t);

				drawTick(g, t, c, VERTICAL);
			}
			if (type == BOUNDS) {
				t =  domain.getUpperBound();
				drawTick(g, t, c, VERTICAL);
			}
			//Draw labels
			if (type == MIDPOINTS) {
				t = domain.getLowerValue();
			} else {
				t = domain.getLowerBound();
			}
			drawLabel(g, format(t), t, c, BELOW);
			if (type == MIDPOINTS) {
				t = domain.getUpperValue();
				} else {
				t = domain.getUpperBound();
			}
			drawLabel(g, format(t), t, c, BELOW);

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					try {
						t = Double.parseDouble(((String)list.get(i)));
						drawLabel(g, format(t), t, c, BELOW);
					} catch (Exception e) {
					}

				}
			}
		}
		else{
			//Draw thte line
			drawLine(g, c, domain.getLowerBound(), c, domain.getUpperBound());
			//drawLine(g, c, -10, c, 10);

			//Draw tick marks, depending on type
			for (int i = 0; i < domain.getSize(); i++){
				if (type == MIDPOINTS) t = domain.getValue(i); else t = domain.getBound(i);
				drawTick(g, c, t, HORIZONTAL);
			}
			if (type == BOUNDS) drawTick(g, c, domain.getUpperBound(), HORIZONTAL);
			//Draw labels
			if (type == MIDPOINTS) t = domain.getLowerValue(); else t = domain.getLowerBound();

			g.setColor(ModelerColor.HISTOGRAM_LABEL);
			drawLabel(g, format(t), c, t, LEFT);
			if (type == MIDPOINTS) t = domain.getUpperValue(); else t = domain.getUpperBound();

			drawLabel(g, format(t), c, t, LEFT);
		}
		int sum = Math.abs(currentXUpperBound) + Math.abs(currentXLowerBound); //
		int diff = Math.abs(currentXUpperBound) - Math.abs(currentXLowerBound); //

		//if (modelType == Modeler.FOURIER_TYPE || modelType == Modeler.WAVELET_TYPE) {
		drawLabel(g, format(0), 0, 0, BELOW); //
	}

	/**This method draws an axis corresponding to the partition of [a, b] into subintervals of width w,
	as in the previous method*/
	public void drawAxis(Graphics g, double a, double b, double w, double c, int orientation, int type){
		Domain domain = new Domain(a, b, w);
		this.drawAxisWithDomain(g, new Domain(a, b, w), c, orientation, type, null);

	}
	public void drawAxis(Graphics g, double a, double b, double w, double c, int orientation, int type, ArrayList list){
		Domain domain = new Domain(a, b, w);
		this.drawAxisWithDomain(g, new Domain(a, b, w), c, orientation, type, list);

	}
	/**This method draws an axis corresponding to the partition of [a, b] into subintervals of width w,
	with tick marks at the partition bounds*/
	public void drawAxis(Graphics g, double a, double b, double w, double c, int orientation){
		this.drawAxis(g, a, b, w, c, orientation, BOUNDS, null);
	 }
	public void drawAxis(Graphics g, double a, double b, double w, double c, int orientation, ArrayList list){
		this.drawAxis(g, a, b, w, c, orientation, BOUNDS, null);
	 }
	//Methods for drawing boxes

	/**This method draws a box between the specified corner points in scale units*/
	public void drawBox(Graphics g, double x0, double y0, double x1, double y1){
		g.drawRect(xGraph(x0), yGraph(y1), xGraph(x1) - xGraph(x0), yGraph(y0) - yGraph(y1));
	}

 	/**This method fills a box between the specified corner points in scale units*/
	public void fillBox(Graphics g, double x0, double y0, double x1, double y1){
		////System.out.println("Graph fillRect x0 = " + x0 + ", xGraph(x0) = " + xGraph(x0));
		////System.out.println("Graph fillRect y0 = " + y0 + ", yGraph(y0) = " + yGraph(y0));
		////System.out.println("Graph fillRect x1 = " + x1 + ", xGraph(x1) = " + xGraph(x1));
		////System.out.println("Graph fillRect y1 = " + y1 + ", yGraph(y1) = " + yGraph(y1));
		g.fillRect(xGraph(x0), yGraph(y1), xGraph(x1) - xGraph(x0), yGraph(y0) - yGraph(y1));
	}

	/**This method draws a point at the specified x and y coordinates (in scale units)*/
	public void drawPoint(Graphics g, double x, double y){
		g.fillRect(xGraph(x) - pointSize / 2, yGraph(y) - pointSize / 2, pointSize, pointSize);
	}

	/**This method sets the point size (in pixels)*/
	public void setPointSize(int n){
		pointSize = n;
	}

	/**This method returns the points size*/
	public int getPointSize(){
		return pointSize;
	}

	/**The following method draws a symmetric, horizontal boxplot, centered at x of radius r
	(in scale units). The variable y is the vertical position, in pixels*/
	public void drawBoxPlot(Graphics g, double x, double r, int y){
		g.drawRect(xGraph(x - r), y - 3, xGraph(x + r) - xGraph(x - r), 6);
		g.drawLine(xGraph(x), y - 6, xGraph(x), y + 6);
	}

	/**The following method fills a symmetric, horizontal boxplot, centered at x of radius r
	(in scale units). The variable y is the vertical position in pixels*/
	public void fillBoxPlot(Graphics g, double x, double r, int y){
		g.fillRect(xGraph(x - r), y - 3, xGraph(x + r) - xGraph(x - r), 6);
		g.drawLine(xGraph(x), y - 6, xGraph(x), y + 6);
	}

	/**The following method draws a five-number, horizontal boxplot. The variable y is the vertical
	position, in pixels*/
	public void drawBoxPlot(Graphics g, double x1, double x2, double x3, double x4, double x5, int y){
		g.drawLine(xGraph(x1), y, xGraph(x5), y);
		g.drawLine(xGraph(x1), y - 3, xGraph(x1), y + 3);
		g.drawLine(xGraph(x5), y - 3, xGraph(x5), y + 3);
		g.drawRect(xGraph(x2), y - 3, xGraph(x4) - xGraph(x2), 6);
		g.drawLine(xGraph(x3), y - 6, xGraph(x3), y + 6);
	}

	/**The following method fills a five-number, horizontal boxplot. The variable y is the vertical
	position, in pixels*/
	public void fillBoxPlot(Graphics g, double x1, double x2, double x3, double x4, double x5, int y){
		g.drawLine(xGraph(x1), y, xGraph(x5), y);
		g.drawLine(xGraph(x1), y - 6, xGraph(x1), y + 6);
		g.drawLine(xGraph(x5), y - 6, xGraph(x5), y + 6);
		g.fillRect(xGraph(x2), y - 3, xGraph(x4) - xGraph(x2), 6);
		g.drawLine(xGraph(x3), y - 8, xGraph(x3), y + 8);
	}

	/**Draw a box at the specified x and y coordinates (in scale units)
	Length l (in scale units) width: i pixels in the negative direction, j in the positive direction*/
	public void drawBox(Graphics g, int style, double x, double y, double l, int i, int j){
		if (style == VERTICAL) g.drawRect(xGraph(x) - i, yGraph(y + l), i + j, yGraph(y) - yGraph(y + l));
		else if (style  == HORIZONTAL) g.drawRect(xGraph(x), yGraph(y) - i, xGraph(x + l) - xGraph(x), i + j);
	}

	public void fillBox(Graphics g, int style, double x, double y, double l, int i, int j){
		if (style == VERTICAL) g.fillRect(xGraph(x) - i, yGraph(y + l), i + j, yGraph(y) - yGraph(y + l));
		else if (style  == HORIZONTAL) g.fillRect(xGraph(x), yGraph(y) - i, xGraph(x + l) - xGraph(x), i + j);
	}

	//Methods for drawing circles

	/**The following methods draw a circle with center (x, y) and radius r (in scale units)*/
	public void drawCircle(Graphics g, double x, double y, double r){
		if (r > 0) g.drawOval(xGraph(x - r), yGraph(y + r), xGraph(x + r) - xGraph(x - r), yGraph(y - r) - yGraph(y + r));
		else drawPoint(g, x, y);
	}

	/**The following methods fill a circle with center (x, y) and radius r (in scale units)*/
	public void fillCircle(Graphics g, double x, double y, double r){
		if (r > 0) g.fillOval(xGraph(x - r), yGraph(y + r), xGraph(x + r) - xGraph(x - r), yGraph(y - r) - yGraph(y + r));
		else drawPoint(g, x, y);
	}

	//Formating
	public String format(double x){
		return decimalFormat.format(x);
	}

	/**This class method tests to see if a specified number is real*/
	public static boolean isReal(double x){
		if (Double.isInfinite(x) | Double.isNaN(x)) return false;
		else return true;
	}

	public static void main(String[] args) {
		Graph test = new Graph(-1100, 1300, -2200, 4500);
		double x = test.xGraphDouble(183);
		double y = test.yGraphDouble(550);
		//System.out.println(x);
		//System.out.println(y);
		double xi = test.xGraphInverse(x);
		double yi = test.yGraphInverse(y);
		//System.out.println(xi);
		//System.out.println(yi);

	}
	public void setShowModelDistribution(boolean b ){
		showModelDistribution = b; 
	 }
}


