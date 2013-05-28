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

/**This class models a basic graph for showing the true and estimated values of a positive
parameter*/
public class EstimateGraph extends Graph{
	//Variables
	private double parameter, estimate, upperBound, step;

	/**Constructs a new estimate graph with a specified parameter value, upper bound, and
	step size.*/
	public EstimateGraph(double p, double u, double w){
		upperBound = u;
		step = w;
		parameter = p;
		setScale(0, 2, 0, upperBound);
	}

	/**This method constructs a new estimate graph with a specified parameter value and upper value.*/
	public EstimateGraph(double p, double u){
		this(p, u, u / 10);
	}

	/**This method constructs a new estiamte graph with a specified parameter value p and default
	upper bound of 1.5 * p. */
	public EstimateGraph(double p){
		this(p, 1.5 * p);
	}

	 /**This method sets the true value of the parameter*/
	public void setParameter(double x){
		parameter = x;
	}

	/**This method gets the true value of the parameter*/
	public double getParameter(){
		return parameter;
	}

	/**This method sets the estimated value of the parameter*/
	public void setEstimate(double x){
		estimate = x;
	}

	/**This method gets the estimated value of the parameter*/
	public double getEstimate(){
		return estimate;
	}


	/**This method paints the graph*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Draw x axis
		drawLine(g, 0, 0, 2, 0);
		//Draw y axis
		drawAxis(g, 0, upperBound, step, 0, VERTICAL, 1);
		//Draw bars
		g.setColor(Color.red);
		fillBox(g, 0.5, 0, 1.5, estimate);
		g.setColor(Color.blue);
		drawBox(g, 0.5, 0, 1.5, parameter);
	}

	/**This method specifies the minimum size*/
	public Dimension getMinimumSize(){
		return new Dimension(100, 100);
	}

	/**This method specifies the preferred size*/
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}
}

