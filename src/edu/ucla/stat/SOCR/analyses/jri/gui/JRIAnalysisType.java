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
/* annieche 200608.
This class determines whether to call the online version of analysis component,
or the R version. Decision is up to the caller. But defaul if online (the jar
that comes with the applet).
*/


package edu.ucla.stat.SOCR.analyses.jri.gui;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.jri.*;

public class JRIAnalysisType {
	Data data = null;
	short analysisType = -1;
	boolean useRI = false;
	Result result = null;

	public JRIAnalysisType() {//Data data, short analysisType, boolean useRI) {
		//this.data = data;
		//this.analysisType = analysisType;
		//this.useRI = useRI;
	}
	public static String lookup(short type) throws WrongAnalysisException {
		String analysisClass = "edu.ucla.stat.SOCR.analyses.jri.xml.";
		//change this to read from XML file
		//System.out.println("JRIAnalysisType  lookup analysisClass = " + analysisClass);
		switch (type) {
			case AnalysisType.SIMPLE_LINEAR_REGRESSION:
				analysisClass = analysisClass + "LinearModel";
				break;
			case AnalysisType.MULTI_LINEAR_REGRESSION:
				analysisClass = analysisClass + "LinearModel";
				break;
			case AnalysisType.ANOVA_ONE_WAY:
				analysisClass = analysisClass + "AnovaOneWay";
				break;

			case AnalysisType.ANOVA_TWO_WAY:
				analysisClass = analysisClass + "AnovaTwoWay";
				break;

/*
			case AnalysisType.ANOVA_TWO_WAY_INTERACTION:
				analysisClass = analysisClass + "SimpleAnova";
				break;
*/
			case AnalysisType.LOGISTIC_REGRESSION:
				analysisClass = analysisClass + "LogisticRegression";
				break;
			case AnalysisType.TIME_SERIES_ARIMA:
				analysisClass = analysisClass + "TimeSeriesArima";
				break;
			case AnalysisType.TWO_INDEPENDENT_WILCOXON:
				analysisClass = analysisClass + "TwoIndependentWilcoxon";
				break;
			case AnalysisType.TWO_PAIRED_T:
				analysisClass = analysisClass + "TwoPairedT";
				break;
			case AnalysisType.TWO_PAIRED_SIGNED_RANK:
				analysisClass = analysisClass + "TwoPairedSignedRank";
				break;
			case AnalysisType.TWO_PAIRED_SIGN_TEST:
				analysisClass = analysisClass + "TwoPairedSignTest";
				break;
			//case AnalysisType.CHI_SQUARE_MODEL_FIT:
			//	analysisClass = analysisClass + "PearsonChiSquared";
			//	break;
			default: analysisClass = null;

		}
		//System.out.println("In AnalysisType after switch analysisClass = " + analysisClass);
		if (analysisClass == null) {
			//System.out.println("JRIAnalysisType WrongAnalysisException");

			throw new WrongAnalysisException("The analysis requestged does not exist.");
		}
		return analysisClass;
	}

	public Result getResult() throws Exception{
		if (useRI) {;

		}
		else {;

		}
		return null;
	}
	public String getResultXML() {
		// does
		// 1. construct xml input string
		// 2. pass the string to a DOM parser and parse, pass to RI.
		// 3. ask
		//String xmlInputString = dataToXMLString(data, AnalysisType.MULTI_LINEAR_REGRESSION);
		//String xmlOutputString = AnalysisServlet

		return null;
	}
}
