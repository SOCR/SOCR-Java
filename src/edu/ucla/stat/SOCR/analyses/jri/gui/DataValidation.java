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
/* 	created by annie che 200601.
	this class contains static methods to do data validation such as:
	1. check if independent variables are close to perfectly correlated
	2. check if use use non-numerical value for certain data analyses that only allow numbers.
	3. more will be added when it is needed.
*/

package edu.ucla.stat.SOCR.analyses.jri.gui;

import edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

public class DataValidation {


	public static boolean isDataHighlyCorrelated(double[] x, double[] y) {
		boolean result = false;
		try {
			if ( Math.abs(AnalysisUtility.sampleCorrelation(x,y)) > AnalysisUtility.HIGH_CORRELATION) {
				//System.out.println("correlation = " + AnalysisUtility.sampleCorrelation(x,y));
				result = true;
			}
		} catch (DataIsEmptyException e) {
			//System.out.println(e.toString());
		}
		return result;

	}
}

