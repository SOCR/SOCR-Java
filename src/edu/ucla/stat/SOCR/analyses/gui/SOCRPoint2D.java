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
package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;
import java.awt.*;

import java.awt.*;

public class SOCRPoint2D {
    public static final double NEARVALUE = 5;
    double x, y;
    int Precision;
    boolean selected = false;

    public SOCRPoint2D() {
	x = 0.0;
	y = 0.0;
	Precision = 4;
    }

    public SOCRPoint2D(double X, double Y) {
	x = X;
	y = Y;
	Precision = 4;
    }

    public SOCRPoint2D(SOCRPoint2D pt) {
	x = pt.x;
	y = pt.y;
	Precision = 4;
    }

    public SOCRPoint2D(int prec) {
	x = 0.0;
	y = 0.0;
	Precision = prec;
    }

    public SOCRPoint2D(int prec, double X, double Y) {
	x = X;
	y = Y;
	Precision = prec;
    }

    public SOCRPoint2D(int prec, SOCRPoint2D pt) {
	x = pt.x;
	y = pt.y;
	Precision = prec;
    }

    public void setValues(double nx, double ny) {
	double pw = Math.pow(10.0,Precision);
	x = Math.round( nx * pw ) / pw;
	y = Math.round( ny * pw ) / pw;
    }

    public String getXString() {
	return String.valueOf(x);
    }
    public String getYString() {
	return String.valueOf(y);
    }

    public boolean isSelected() {
      return selected;
    }

    public void setSelected(boolean sel) {
      selected = sel;
    }

    public double distance(SOCRPoint2D pt) {
	return Math.sqrt( (pt.x-x)*(pt.x-x) + (pt.y-y)*(pt.y-y) );
    }

    public boolean near(SOCRPoint2D pt) {
	if ( distance(pt) < NEARVALUE )
	    return true;
	return false;
    }

    public String print() {
	return new String("("+x+","+y+")");
    }

  public String toString() {
    return new String("("+x+","+y+")");
  }
}
