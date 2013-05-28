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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import edu.ucla.stat.SOCR.distributions.Domain;
import edu.ucla.stat.SOCR.distributions.IntervalData;

/**This class models an interactive histogram.  The user can click on the horizontal axes to add points
to the data set.*/
public class InteractiveHistogram extends Histogram implements MouseListener{
	//Variables
	private int count;
	private IntervalData data;
	private Vector<Double> values = new Vector<Double>();

	/**This general constructor creates a new interactive histogram corresponding to a specified
	domain.*/
	public InteractiveHistogram(double a, double b, double w) {
		this.addMouseListener(this);
		data = new IntervalData(a, b, w);
		setIntervalData(data);
		count = data.getDomain().getSize();
	}

	/**This method sets the width of the domain.  A new interval data set is created, and
	the values are added to the data set.*/
	public void setWidth(double width){
		data.setDomain(new Domain(data.getDomain().getLowerBound(), data.getDomain().getUpperBound(), width));
		for (int i = 0; i < values.size(); i++) data.setValue(((Double)values.elementAt(i)).doubleValue());
		repaint();
	}

	/**This method resets the interactive histogram.*/
	public void reset(){
		values.removeAllElements();
		data.reset();
		repaint();
	}

	/**This method returns the values from the data set.*/
	public double getValue(int i){
		if (i < 0) i = 0; else if (i >= values.size()) i = values.size() - 1;
		return ((Double)values.elementAt(i)).doubleValue();
	}

	/**This method returns the last value.*/
	public double getValue(){
		return getValue(values.size() - 1);
	}

	/**This method handles the events corresponding to mouse clicks.  If the user clicks on the
	horizontal axes, the corresponding value is added to the data set.*/
	public void mouseClicked(MouseEvent event){
		int y0 = yGraph(0);
		int y = event.getY();
		double x = xScale(event.getX());
		// Determine if a valid point has been selected
		if ((data.getDomain().getLowerBound() <= x) & (x <= data.getDomain().getUpperBound()) & (y0 - 10 < y) & (y < y0 + 10)){
			data.setValue(x);
			repaint();
			values.addElement(new Double(x));
		}
	}

	//The following method correspond to mouse events that are not handled.
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
}