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

/**The floor in Buffon's needle experiment*/
public class NeedleFloor extends Graph{
	//Variables
	private double distance, angle, length;
	boolean needleDropped = false;

	/**Constructs a new floow with a specified needle length*/
	public NeedleFloor(double l){
		super(-1, 1, -0.5, 1.5);
		setMargins(0, 0, 0, 0);
		setBackground(Color.yellow);
		setLength(l);
	}

	/**Paint the floor*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double x, y1, y2;
		//Draw floorboard cracks
		g.setColor(Color.black);
		drawLine(g, -1, 0, 1, 0);
		drawLine(g, -1, 1, 1, 1);
		//Draw needle
		if(needleDropped){
			g.setColor(Color.red);
			x = (length / 2) * Math.cos(angle);
			y1 = distance + (length / 2) * Math.sin(angle);
			y2 = distance - (length / 2) * Math.sin(angle);
			drawLine(g, x, y1, -x, y2);
		}
	}

	/**Reset the floor*/
	public void reset(){
		needleDropped = false;
		repaint();
	}

	/**Set the length of the needle*/
	public void setLength(double l){
		//correct for invalid parameter
		if (l < 0) l = 0; else if (l > 1) l = 1;
		length = l;
	}

	/**Get the needle length*/
	public double getLength(){
		return length;
	}

	/**Set the distance and angle to specified values*/
	public void setValues(double d, double a){
		//Correct for invalid values
		if (d < 0) d = 0; else if (d > 1) d = 1;
		if (a < 0) a = 0; else if (a > Math.PI) a = Math.PI;
		distance = d;
		angle = a;
		needleDropped = true;
	}

	/**Set the distance and angle to random values*/
	public void setValues(){
		setValues(Math.random(), Math.random() * Math.PI);
	}

	/**Get the distance*/
	public double getDistance(){
		return distance;
	}

	/**Get the angle*/
	public double getAngle(){
		return angle;
	}

	/**Determine if the cross event has occurred*/
	public boolean crossEvent(){
		if (distance < 0.5 * length * Math.sin(angle) |
			distance > 1 - 0.5 * length * Math.sin(angle)) return true;
		else return false;
	}

	/**Get the probability of the cross event*/
	public double getProbability(){
		return 2 * length / Math.PI;
	}

	public Dimension setPreferredSize(){
		return new Dimension(200, 200);
	}

	public Dimension setMinimumSize(){
		return new Dimension(100, 100);
	}
}

