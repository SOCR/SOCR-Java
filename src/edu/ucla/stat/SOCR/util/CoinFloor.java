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

/**This class models the floor in Buffon's coin experiment*/
public class CoinFloor extends Graph{
	//Variables
	private double xCenter = 0, yCenter = 0, radius = 0.25;
	private boolean coinDropped = false;

	/**This general constructor creates a new Buffon floor with coin radius r*/
	public CoinFloor(double r){
		super(-0.5, 0.5, -0.5, 0.5);
		setMargins(10, 10, 10, 10);
		setBackground(Color.yellow);
		setRadius(r);
	}

	/**This default constructor creates a new Buffon floor with coin radius 0.25*/
	public CoinFloor(){
		this(0.25);
	}

	/**This method paints the floor tile and the coin*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw floorboard cracks
		g.setColor(Color.black);
		drawLine(g, -0.5, -0.5, 0.5, -0.5);
		drawLine(g, -0.5, 0.5, 0.5, 0.5);
		drawLine(g, -0.5, -0.5, -0.5, 0.5);
		drawLine(g, 0.5, -0.5, 0.5, 0.5);
		//Draw the coin
		if (coinDropped){
			g.setColor(Color.red);
			fillCircle(g, xCenter, yCenter, radius);
		}
	}

	/**This method sets the coin radius to a specified value*/
	public void setRadius(double r){
		//Correct for invalid parameters
		if (r < 0) r = 0;
		if (r > 0.5) r = 0.5;
		radius = r;
	}

	/**This method gets the coin radius*/
	public double getRadius(){
		return radius;
	}

	/**The method resets the floor*/
	public void reset(){
		coinDropped = false;
		repaint();
	}

	/**This method sets the center of the coin to specified values*/
	public void setValues(double x, double y){
		//Correct for invalid parameters
		if (x < -0.5) x = -0.5; else if (x > 0.5) x = 0.5;
		if (y < -0.5) y = -0.5; else if (y > 0.5) y = 0.5;
		xCenter = x; yCenter = y;
		coinDropped = true;
	}

	/**This method sets the center of the coin to random values*/
	public void setValues(){
		setValues(-0.5 + Math.random(), -0.5 + Math.random());
	}

	/**This method gets the x coordinate of the coin center*/
	public double getXCenter(){
		return xCenter;
	}

	/**This method gets the y coordinate of the coin center*/
	public double getYCenter(){
		return yCenter;
	}

	/**This method determines if the crossing event has occurred.*/
	public boolean crossEvent(){
		if (radius - 0.5 < xCenter & xCenter < 0.5 - radius
			& radius - 0.5 < yCenter & yCenter < 0.5 - radius) return false;
		else return true;
	}

	/**This method gets the probability of the cross event*/
	public double getProbability(){
		return 4 * radius * (1 - radius);
	}

	/**This method specifiies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}
}

