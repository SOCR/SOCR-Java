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
import java.util.*;

/**This class defines a simple timeline for displaying random points in time*/
public class Timeline extends Graph{
	private double lower, upper, width;
	private int type;
	private Vector<Double> times = new Vector<Double>();
	private Vector<Color> colors = new Vector<Color>();

	/**The general constructor creates a new timeline corresponding to a specified domain*/
	public Timeline(double a, double b, double w, int t){
		setMargins(20, 20, 20, 10);
		setPointSize(4);
		setBackground(Color.white);
		type = t;
		setRange(a, b, w);
	}

	/**This special constructor creates new timeline with a specified lower
	value, upper value, width, and integer format*/
	public Timeline(double a, double b, double w){
		this(a, b, w, 0);
	}

	/**This special constrcutor creates a new timeline with a specified lower
	and upper values, width 1 and integer format pattern*/
	public Timeline(double a, double b){
		this(a, b, 1);
	}

	/**This default constructor creates a new timeline with lower value 0,
	uppervalue 10, width 1, and integer format pattern*/
	public Timeline(){
		this(0, 10, 1);
	}

	/**This method paints the timeline*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw x axis
		g.setColor(Color.black);
		drawAxis(g, lower, upper, width, 0, HORIZONTAL, type);
		//Draw data
		if (! times.isEmpty()){
			for (int i = 0; i < times.size(); i++){
				g.setColor(colorAt(i));
				drawPoint(g, timeAt(i), 0);
			}
		}
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(600, 30);
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(300, 30);
	}

	/**This method adds a new point with the default color*/
	public void addTime(double t){
		Double d = new Double(t);
		times.addElement(d);
		colors.addElement(Color.red);
	}

	/**This method adds a new time with a specified color*/
	public void addTime(double t, Color c){
		Double d = new Double(t);
		times.addElement(d);
		colors.addElement(c);
	}

	/**This method returns the time at a specified index*/
	public double timeAt(int i){
		Double d = (Double)times.elementAt(i);
		return d.doubleValue();
	}

	/**This method returns the color at a specified index*/
	public Color colorAt(int i){
		return (Color)colors.elementAt(i);
	}

	/**This method resets the data*/
	public void resetData(){
		times.removeAllElements();
		colors.removeAllElements();
	}

	/**This method resets the data and repaints the timeline*/
	public void reset(){
		resetData();
		repaint();
	}

	/**This method sets the upper and lower values*/
	public void setRange(double a, double b){
		setRange(a, b, 1);
	}

	/**This method sets the upper and lower values and the width */
	public void setRange(double a, double b, double w){
		width = w;
		if (type == 0){
			lower = a - 0.5 * width;
			upper = b + 0.5 * width;
		}
		else{
			lower = a;
			upper = b;
		}
		setScale(lower, upper, 0, 0);
	}
}