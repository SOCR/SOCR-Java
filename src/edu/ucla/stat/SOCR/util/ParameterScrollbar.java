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
import javax.swing.JScrollBar;

/**A basic scrollbar for adjusting the value of a real parameter*/
public class ParameterScrollbar extends JScrollBar{
	private double parameter, min, max, stepSize;
	private int steps;

	/**General constructor: creates a new scrollbar for adjusting a parameter.
	The possible values are from min to max in increments of stepSize*/
	public ParameterScrollbar(double min, double max, double stepSize, double parameter){
		setOrientation(HORIZONTAL);
		setVisibleAmount(1);
		setRange(min, max, stepSize, parameter);
	}

	/**Default constructor: creates a new parameter scollbar with min value 0,
	max value 1, step size 1/10, and value 1/2*/
	public ParameterScrollbar(){
		this(0, 1, 0.1, 0.5);
	}

	/**Set the minimum value of the parameter*/
	public void setMin(double a){
		setRange(a, max, stepSize, parameter);
	}

	/**Set the maximum value of the parameter*/
	public void setMax(double b){
		setRange(min, b, stepSize, parameter);
	}

	/**Set the step size*/
	public void setStepSize(double s){
		setRange(min, max, s, parameter);
	}

	/**Set the value of the parameter*/
	public void setParameter(double p){
		//Adjust for invalid parameter value
		if (p < min) p = min; else if (p > max) p = max;
		parameter = p;
		//Set scrollbar value
		setValue((int)Math.rint((parameter - min) / stepSize));
	}

	/**Set the range of the parameter: min, max, step size, and value*/
	public void setRange(double a, double b, double s, double p){
		//Adjust for invalid values
		if (a > b) a = b;
		if (s < 0) s = 1;
		if (p < a) p = a; else if (p > b) p = b;
		min = a; max = b; stepSize = s;
		parameter = p;
		steps = (int)Math.rint((max - min) / stepSize);
		//Set scrollbar values
		setMinimum(0);
		setMaximum(steps + 1);
		setValue((int)((parameter - min) / stepSize));
	}


	/**Get the value of the parameter*/
	public double getParameter(){
		parameter = min + getValue() * stepSize;
		if (parameter > max) parameter = max;
		return parameter;
	}

	/**Get the minimum value of the parameter*/
	public double getMin(){
		return min;
	}

	/**Get the maximum value of the paraemter*/
	public double getMax(){
		return max;
	}

	/**Get the step size*/
	public double getStepSize(){
		return stepSize;
	}

	/**Get the number of steps*/
	public int getSteps(){
		return steps;
	}
}
