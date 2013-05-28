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
import java.text.DecimalFormat;

import javax.swing.JTextArea;

import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.core.SOCRApplet;
import edu.ucla.stat.SOCR.distributions.IntervalData;

/**This class models a simple data table*/
public class DataTable extends JTextArea{
	//Variables
	private int summaryStats = 1, valueCount, type=1;
	//Objects
	private IntervalData data;
	private DecimalFormat decimalFormat = new DecimalFormat();
	private String title, text;

	/**Constructor*/
	public DataTable(IntervalData d, int t){
		this(d);
		type = t;
	}

        /**Constructor*/
        public DataTable(IntervalData d){
                super(5, 22);
                init();
                setData(d);
        }
        
        private void init() {
            setFont(SOCRApplet.textFont);
            setBackground(SOCRApplet.textColor);
            setEditable(false);
        }

	/**This method sets the data set */
	public void setData(IntervalData d){
		data = d;
		title = data.getName() + "\tData";
		reset();
	}

	/**This method resets the table. Call this method, in particular, whenever the distribution
	 of the random variable changes*/
	public void reset(){
		valueCount = data.getDomain().getSize();
		update();
	}

	/**This method determines what moments to display*/
	public void showSummaryStats(int i){
		summaryStats = i;
		update();
	}

	/**This method updates the table*/
	public void update(){
		int pointCount = data.getSize();
		double x;
		text = title;
		//Show moments
		switch(summaryStats){
		case 1:		//True mean and absolute deviation
			text = text + "\nMean";
			if (pointCount > 0) text = text + "\t" + format(data.getMean());
			text = text + "\nSD";
			if (pointCount > 0) text = text + "\t" + format(data.getSD());
			break;
		case 2:		//Interval mean and absolutde deviation
			text = text + "\nMean";
			if (pointCount > 0) text = text + "\t" + format(data.getIntervalMean());
			text = text + "\nSD";
			if (pointCount > 0) text = text + "\t" + format(data.getIntervalSD());
			break;
		case 3:
			text = text + "\nMin";
			if (pointCount > 0) text = text + "\t" + format(data.getMinValue());
			text = text + "\nQ1";
			if (pointCount > 0) text = text + "\t" + format(data.getQuartile(1));
			text = text + "\nQ2";
			if (pointCount > 0) text = text + "\t" + format(data.getQuartile(2));
			text = text + "\nQ3";
			if (pointCount > 0) text = text + "\t" + format(data.getQuartile(3));
			text = text + "\nmax";
			if (pointCount > 0) text = text + "\t" + format(data.getMaxValue());
			break;
		case 4:
			text = text + "\nMedian";
			if (pointCount > 0) text = text + "\t" + format(data.getMedian());
			text = text + "\nMAD";
			if (pointCount > 0) text = text + "\t" + format(data.getMAD());
			break;
		}
		//Show empirical distribution if discrete
		if (type == Distribution.DISCRETE){
			for (int i = 0; i < valueCount; i++){
				x = data.getDomain().getValue(i);
				text = text + "\n" + format(x);
				if (pointCount > 0) text = text + "\t" + format(data.getRelFreq(x));
			}
		}
		else{
			for (int i = 0; i < valueCount; i++){
				text = text + "\n[" + format(data.getDomain().getBound(i))
					+ ", " + format(data.getDomain().getBound(i + 1)) + ")";
				x = data.getDomain().getValue(i);
				if (pointCount > 0) text = text + "\t" + format(data.getRelFreq(x));
			}
		}
		setText(text);
	}

	/**This method formats a specified number.*/
	public String format(double x){
		if (Double.isInfinite(x) & x < 0) return "-inf";
		else if (Double.isInfinite(x) & x > 0) return "inf";
		else if (Double.isNaN(x)) return "*";
		else return decimalFormat.format(x);
	}
}


