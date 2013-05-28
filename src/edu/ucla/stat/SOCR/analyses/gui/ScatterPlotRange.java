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
import java.util.*;

public class ScatterPlotRange {
    public static double TOLERANCE = 0.0001;
    public double start, end;

    public ScatterPlotRange (double s, double e) {
	start = s;
	end = e;
	if ( width() < TOLERANCE ) {
	    end = start+2.0*TOLERANCE;
	    // //System.out.println("Range too small: using ["+start+","+end+"]");
	}
    }

    public ScatterPlotRange (String str) {
	if ( str==null || !stringToRange(str) ) {
	    // //System.out.println("Invalid Range: using [0,1]");
	    start = 0.0;
	    end = 1.0;
	}
	if ( width() < TOLERANCE ) {
	    end = start+2.0*TOLERANCE;
	    // //System.out.println("Range too small: using ["+start+","+end+"]");
	}
    }
    public double width() {
	return Math.abs(end-start);
    }
    public boolean stringToRange(String range) {
	StringTokenizer parser = new StringTokenizer(range,"..");
	if ( parser.countTokens() != 2 )
	    return false;
	String tmp = parser.nextToken();
	Double val;
	try {
	    val=Double.valueOf(tmp);
	} catch (NumberFormatException e) {
	    return false;
	}
	start = val.doubleValue();
	tmp = parser.nextToken();
	try {
	    val=Double.valueOf(tmp);
	} catch (NumberFormatException e) {
	    return false;
	}
	end = val.doubleValue();
	return true;
    }
}
