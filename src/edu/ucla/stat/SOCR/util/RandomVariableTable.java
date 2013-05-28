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

import java.text.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.gui.*;

/** Random variable table*/
public class RandomVariableTable extends SOCRApplet.SOCRTextArea{
	//Variables
	private int moments = 1, values, type;
	//Objects
	private RandomVariable randomVariable;
	private Distribution dist;
	private IntervalData data;
	private Domain domain;
	private String title, text;
	private DecimalFormat decimalFormat = new DecimalFormat();

	/**Constructor*/
	public RandomVariableTable(RandomVariable v){
		setRandomVariable(v);
	}

	/**Set the random variable */
	public void setRandomVariable(RandomVariable rv){
		randomVariable = rv;
		reset();
	}

	/**Reset the table. Call this method, in particular, whenever the distribution
	of the random variable changes.*/
	public void reset(){
		dist = randomVariable.getDistribution();
		data = randomVariable.getIntervalData();
		domain = dist.getDomain();
		values = domain.getSize();
		type = dist.getType();
		title = randomVariable.getName() + "\tDist" + "\tData";

		update();
	}

	/**Determine what moments to display*/
	public void showMoments(int moments){
		this.moments = moments;
	}

	/**Update the table*/
	public void update(){
		int points = data.getSize();
		double x;
		text = title;
		//Show moments
		if (moments == 1){
			text = text + "\nMean\t" + format(dist.getMean());
			if (points > 0) text = text + "\t" + format(data.getMean());
			text = text + "\nSD\t" + format(dist.getSD());
			if (points > 0) text = text + "\t" + format(data.getSD());
		}
		//Show empirical distribution if discrete
		if (type == Distribution.DISCRETE){
			for (int i = 0; i < values; i++){
				x = domain.getValue(i);
				text = text + "\n" + format(x) + "\t" + format(dist.getDensity(x));
				if (points > 0) text = text + "\t" + format(data.getRelFreq(x));
			}
		}
		setText(text);
	}

	/**This method sets the maximum number of decimals to display in the formatting*/
	public void setDecimals(int n){
		decimalFormat.setMaximumFractionDigits(n);
	}

	/**This method format a number*/
	public String format(double x){
		return decimalFormat.format(x);
	}

	/**This method sets the decimal format*/
	public void setDecimalFormat(DecimalFormat df){
		decimalFormat = df;
	}

}

