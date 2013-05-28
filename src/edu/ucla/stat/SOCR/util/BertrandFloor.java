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

/**This class models the floor in Betrand's experiment*/
public class BertrandFloor extends Graph{
 	//Variables
	private double x = 1, y = 0, distance = 1, angle = 0;
	private boolean chordEvent = false;
	//Constants
	public final static int UNIFORM_DISTANCE = 0, UNIFORM_ANGLE = 1;

	/**This default constructor creates a new floor on the unit square
	[-1, 1] x [-1, 1]*/
	public BertrandFloor(){
		super(-1, 1, -1, 1);
		setMargins(10, 10, 10, 10);
		setBackground(Color.yellow);
	}

	/**This method draws the floor*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw circle
		g.setColor(Color.blue);
		drawCircle(g, 0, 0, 1);
		//Draw triangle
		drawLine(g, 1, 0, -0.5, 0.866);
		drawLine(g, 1, 0, -0.5, -0.866);
		drawLine(g, -0.5, 0.866, -0.5, -0.866);
		//Draw chord
		g.setColor(Color.red);
		drawLine(g, 1, 0, x, y);
	}

	/**This method resets the data*/
	public void reset(){
		setDistance(1);
		repaint();
	}

	/**This method sets the distance parameter to a specified value*/
	public void setDistance(double d){
		//Correct for invalid parameters
		if (d < 0) d = 0;
		else if (d > 1) d = 1;
		//Set parameters
		distance = d;
		angle = Math.acos(distance);
		setCoordinates();
	}

	/**This method sets the distance parameter to a random value*/
	public void setDistance(){
		setDistance(Math.random());
	}

	/**This method gets the distance parameter*/
	public double getDistance(){
		return distance;
	}

	/**This method sets the angle parameter to a specified value*/
	public void setAngle(double a){
		//Correct for invalid parameters
		if (a < 0) a = 0;
		else if (a > Math.PI / 2) a = Math.PI / 2;
		//Set Parameters
		angle = a;
		distance = Math.cos(angle);
		setCoordinates();
	}

	/**This method sets the angle to a random value*/
	public void setAngle(){
		setAngle((Math.PI / 2) * Math.random());
	}

	/**This method gets the angle parameter*/
	public double getAnlge(){
		return angle;
	}

	/**This emthod computes the x and y coordinates of the chord, from the distance
	and angle parameters*/
	public void setCoordinates(){
		x = 2 * distance * distance - 1;
		y = 2 * Math.sqrt(1 - distance * distance) * distance;
	}

	/**This method gets the x-corrdinate of the chord*/
	public double getXCoordinate(){
		return x;
	}

	/**This method gets the y-corrdinate of the chord*/
	public double getYCoordinate(){
		return y;
	}

	/**This method determines if the length of the chord is longer than the length of the
	triangle side*/
	public boolean chordEvent(){
		return (distance < 0.5);
	}

	/**This method gets the probability of the chord event*/
	public double getProbability(int model){
		if (model == UNIFORM_DISTANCE) return 0.5;
		else return 1.0 / 3;
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}
}

