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

/**This class models a stick of unit length that can be broken in two pieces.
The variable of interest is whether the pieces form no triangle, an acute triangle,
or an obtuse triangle.*/
public class Triangle extends Graph{
	private double a, b, c;  //The side lengths in increasing order.
	private double x, y; //The cutpoints
	private int type; //The type of triangle: 0 (nothing), 1 (obtuse), 2 (acute)

	/**This general constructor creates a new triangle object*/
	public Triangle(){
		super(0, 1, 0, 1);
		setMargins(5, 5, 5, 5);
		setBackground(Color.yellow);
		reset();
  	}

  	/**This method sets the cutpoints to specified values. These values should be between 0 and 1*/
  	public void setCutPoints(double s, double t){
		if (s < 0) s = 0; else if (s > 1) s = 1;
		x = s;
		if (t < 0) t = 0; else if (t > 1) t = 1;
		y = t;
		if (x < y) setSides(x, y - x, 1 - y);
		else setSides(y, x - y, 1 - x);
	}

	/**This method sets the cutpoints to random values in (0, 1)*/
	public void setCutPoints(){
		setCutPoints(Math.random(), Math.random());
	}

	/**This method returns the cutpoints.*/
	public double getCutPoint(int i){
		if (i == 0) return x;
		else return y;
	}

  	/**This method sets the sides to specified values and determines the type of triangle.
  	The values should be nonnegative and should sum to 1*/
  	public void setSides(double u, double v, double w){
		//Assign side lengths in increasing order
		if (u <= v & v <= w){
	  		a = u; b = v; c = w;
		}
		else if (u <= w & w <= v){
	  		a = u; b = w; c = v;
		}
		else if (v <= u & u <= w){
	  		a = v; b = u; c = w;
		}
		else if (v <= w & w <= u){
	  		a = v; b = w; c = u;
		}
		else if (w <= u & u <= v){
	  		a = w; b = u; c = v;
		}
		else{
	  		a = w; b = v; c = u;
		}
		//Determine type of triangle
		if (a < b + c & b < a + c & c < a + b){		//Triangle
			if (c * c < a * a + b * b) type = 2;	//Acute
	  		else type = 1;							//Obtuse
		}
		else type = 0;	//Not a triangle
  	}

  	/**This method returns the length of the i'th side, in increasing order*/
  	public double getSize(int i){
		if (i == 0) return a;
		else if (i == 1) return b;
		else return c;
	}

  	//This method returns the type of triangle
  	public int getType(){
		return type;
  	}

  	public void paintComponent(Graphics g){
		super.paintComponent(g);

		double x0, y0, u, v, cos, sin;
		g.setColor(Color.red);
		x0 = (1 - c) / 2; y0 = 1.0 / 3;		//Shift values
		drawLine(g, x0, y0, x0 + c, y0);	//Draw base
		//If the pieces do not form a triangle, draw the broken stick
		if (type == 0){
	  		drawLine(g, x0, y0, x0, y0 + a);
	  		drawLine(g, x0 + c, y0, x0 + c, y0 + b);
		}
		//Draw acute triangle
		else if (type == 2){
	  		cos = (a * a + c * c - b * b) / (2 * a * c);
	  		sin = Math.sqrt(1 - cos * cos);
	  		u = a * cos;
	  		v = a * sin;
	  		drawLine(g, x0, y0, x0 + u, y0 + v);
	  		drawLine(g, x0 + c, y0, x0 + u, y0 + v);
		}
		//Draw obtuse triangle
		else{
	  		u = (a * a + c * c - b * b) / (2 * c);
	  		v = Math.sqrt(a - u);
	  		drawLine(g, x0, y0, x0 + u, y0 + v);
	  		drawLine(g, x0 + c, y0, x0 + u, y0 + v);
		}
  	}

  	/**This method resets the stick*/
  	public void reset(){
		setCutPoints(0, 1);	//An unbroken stick
		repaint();
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
