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
/*
	200508 Annie Che.
*/

package edu.ucla.stat.SOCR.analyses.jri.result;

import java.util.HashMap;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import org.w3c.dom.Document;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.StringReader;
import edu.ucla.stat.SOCR.analyses.xml.DomParserUtility;


public class SimpleLinearRegressionResult extends LinearModelResult {

	/** The mean of The independent varialbe's data. */
	public static final String MEAN_X = "MEAN_X";

	/** The mean of The dependent varialbe's data. */
	public static final String MEAN_Y = "MEAN_Y";

	/** estimated intercept of The model. */
	public static final String ALPHA = "ALPHA";

	/** estimated slope of The model. */
	public static final String BETA = "BETA";

	/** estimated standard deviation of The intercept ALPHA. */
	public static final String ALPHA_SE = "ALPHA_SE";

	/** estimated standard deviation of The slope BETA. */
	public static final String BETA_SD = "BETA_SD";

	/** The p-value of testing if ALPHA equals to zero. */
	public static final String ALPHA_P_VALUE = "ALPHA_P_VALUE";

	/** The p-value of testing if BETA equals to zero. */
	public static final String BETA_P_VALUE= "BETA_P_VALUE";

	/** The p-value of testing if ALPHA equals to zero. */
	public static final String ALPHA_T_STAT = "ALPHA_T_STAT";

	/** The t-statistic of testing if BETA equals to zero. */
	public static final String BETA_T_STAT = "BETA_T_STAT";

	/** The name of BETA variable. */
	public static final String BETA_NAME = "BETA_NAME";

	/** The predicted value of The model. It maps to a double[]. */
	public static final String PREDICTED = "PREDICTED";

	/** The predicted residuals of The model. It maps to a double[]. */
	public static final String RESIDUALS = "RESIDUALS";

	/** PREDICTED double[] after being sorted ascendantly. It maps to a double[]. */
	public static final String SORTED_RESIDUALS = "SORTED_RESIDUALS";

	/** The SORTED_RESIDUALS's mapping to the original index. It maps to a int[]. */
	public static final String SORTED_RESIDUALS_INDEX = "SORTED_RESIDUALS_INDEX";

	public static final String SORTED_STANDARDIZED_RESIDUALS = "SORTED_STANDARDIZED_RESIDUALS";

	/** Normal quantiles of the sorted residuals. It maps to a double[]. */
	public static final String SORTED_NORMAL_QUANTILES = "SORTED_NORMAL_QUANTILES";


	public double alpha;
	public double beta;
	public double alphaSE;
	public double betaSE;
	public double alphaTStat;
	public double betaTStat;
	public String alphaPValue;
	public String betaPValue;
	public String betaName;

	/*********************** Constructors ************************/
	public SimpleLinearRegressionResult(HashMap<String,Object> texture) {
		super(texture);
	}
	public SimpleLinearRegressionResult(HashMap<String,Object> texture, HashMap<String,Object> graph) {
		super(texture, graph);
	}
	public SimpleLinearRegressionResult(String xmlResultString) {
		super(xmlResultString);
		//useLocal = false;
		this.dom = DomParserUtility.xmlStringToDocument(xmlResultString);
		new SimpleLinearRegressionResult(this.dom);
	}
	public SimpleLinearRegressionResult(Document dom) {
		super(dom);
		this.dom = dom;

		//System.out.println("SimpleLinearRegressionResult dom start : this.alpha " + this.alpha);
		// construct Intercept

		parseIntercept();
		//System.out.println("SimpleLinearRegressionResult parseIntercept : this.alpha " + this.alpha);
		this.alpha = intercept.estimate;

		//////System.out.println("SimpleLinearRegressionResult intercept : this.alpha " + this.alpha);
		this.alphaSE = intercept.se;
		//this.setAlpha(intercept.estimate);
		this.alphaTStat = intercept.tStat;
		this.alphaPValue = intercept.pValue;

		//System.out.println("SimpleLinearRegressionResult this.alpha : " + this.alpha);
		//System.out.println("SimpleLinearRegressionResult : " + intercept.se);
		//System.out.println("SimpleLinearRegressionResult : " + intercept.tStat);
		//System.out.println("SimpleLinearRegressionResult : " + intercept.pValue);
		// construct Predictors
		parsePredictors();
		//System.out.println("parsePredictors done");

		// construct PlotData.
		parsePlotData();
		//System.out.println("parsePlotData done");

		//System.out.println("SimpleLinearRegressionResult end constructor this.alpha : " + this.alpha);
		//System.out.println("SimpleLinearRegressionResult end constructor getAlpha : " + this.getAlpha());

	}
	/*********************** Accessors ************************/
	public double getMeanX() {
		return ((Double)texture.get(MEAN_X)).doubleValue();
	}
	public double getMeanY() {
		return ((Double)texture.get(MEAN_Y)).doubleValue();
	}
	public double getAlpha() {

		parseIntercept();
		setAlpha(intercept.estimate);
		//System.out.println("SimpleLinearRegressionResult getAlpha = " + this.alpha);
		return this.alpha;

	}
	public void setAlpha(double input) {
		this.alpha = input;
		//////System.out.println("SimpleLinearRegressionResult setAlpha = " + this.alpha);

	}
	public double getBeta() {

		parsePredictors();
		beta = predictor.estimate;
		//System.out.println("SimpleLinearRegressionResult getBeta = " + this.beta);
		return this.beta;

	}
	public double getAlphaSE() {

		parseIntercept();
		alphaSE = intercept.se;
		//System.out.println("SimpleLinearRegressionResult getAlphaSE = " + this.alphaSE);
		return this.alphaSE;

	}
	public double getBetaSE() {

		parsePredictors();
		betaSE = predictor.se;
		//System.out.println("SimpleLinearRegressionResult betaSE = " + this.betaSE);
		return this.betaSE;

	}
	public double getAlphaTStat() {

		parseIntercept();
		alphaTStat = intercept.tStat;
		//System.out.println("SimpleLinearRegressionResult alphaTStat = " + this.alphaTStat);

		return this.alphaTStat;

	}
	public double getBetaTStat() {
		parsePredictors();
		betaTStat = predictor.se;
		//System.out.println("SimpleLinearRegressionResult betaSE = " + this.betaSE);

		return this.betaTStat;
	}
	public String getAlphaPValue() {

		parsePredictors();
		alphaPValue = intercept.pValue;
		//System.out.println("SimpleLinearRegressionResult getAlphaPValue = " + this.alphaPValue);

		return this.alphaPValue;


	}
	public String getBetaPValue() {
		parsePredictors();
		betaPValue = predictor.pValue;
		//System.out.println("SimpleLinearRegressionResult betaPValue = " + this.betaPValue);

		return this.betaPValue;

	}

	public String getBetaName() {

		parsePredictors();
		betaName = predictor.name;
		//System.out.println("SLR Result bataName = " + betaName);
		return this.betaName;

	}
	public double[] getResiduals() {

		parsePlotData();
		for (int i = 0; i < plotData.residuals.length; i++) {
			//System.out.println("getResiduals plotData.residuals["+i+"]= " + plotData.residuals[i]);
		}

		return plotData.residuals;

	}
	public double[] getPredicted() {

		for (int i = 0; i < plotData.residuals.length; i++) {
			//System.out.println("getPredicted plotData.predicted["+i+"]= " + plotData.predicted[i]);
		}

		return plotData.predicted;

	}

	/*******************************************************************/
	public double[] getSortedResiduals() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_RESIDUALS);
	}
	public double[] getSortedStandardizedResiduals() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_STANDARDIZED_RESIDUALS);
	}
	public int[] getSortedResidualsIndex() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (int[])resultHashMap.get(SORTED_RESIDUALS_INDEX);
	}
	public double[] getSortedNormalQuantiles() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_NORMAL_QUANTILES);
	}
	public double[] getSortedStandardizedNormalQuantiles() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_STANDARDIZED_NORMAL_QUANTILES);
	}

	public static void main(String args[]) {
		SimpleLinearRegressionResult result = new SimpleLinearRegressionResult(outputTest);
		System.out.println("in main " + result.getAlpha());
		System.out.println("in main " + result.getBeta());
		System.out.println("in main " + result.getAlphaSE());
		System.out.println("in main " + result.getBetaSE());
		System.out.println("in main " + result.getAlphaTStat());
		System.out.println("in main " + result.getBetaTStat());
		System.out.println("in main " + result.getAlphaPValue());
		System.out.println("in main " + result.getBetaPValue());
		System.out.println("in main " + result.getBetaName());

		System.out.println("in main " + result.getResiduals());
		System.out.println("in main " + result.getPredicted());
		double[] resid = result.getResiduals();
		double[] predi = result.getPredicted();
		for (int i = 0; i < resid.length; i++) {
			System.out.println("resid["+i+"]= " + resid[i] + ", predi["+i+"] = " + predi[i]);
		}
	}

	private static String outputTest = "<analysis_output>" +
		"<linear_regression_result>" +
			"<coefficients>" +
				"<intercept>" +
					"<estimate>14.5</estimate>" +
					"<standard_error>14.2</standard_error>" +
					"<t_value>1.021</t_value>" +
					"<p_value>0.3218</p_value>" +
				"</intercept>" +
				"<predictor>" +
					"<variable_name>X1</variable_name>" +
					"<estimate>0.563</estimate>" +
					"<standard_error>0.118</standard_error>" +
					"<t_value>4.773</t_value>" +
					"<p_value>0.002</p_value>" +
				"</predictor>" +
				"<predictor>" +
					"<variable_name>X2</variable_name>" +
					"<estimate>0.268</estimate>" +
					"<standard_error>0.157</standard_error>" +
					"<t_value>1.702</t_value>" +
					"<p_value>0.107</p_value>" +
				"</predictor>" +
			"</coefficients>" +
			"<statistics>" +
				"<residual_standard_error>" +
					"<value>" + "</value>" +
					"<degrees_freedom_residual>" + "</degrees_freedom_residual>" +
				"</residual_standard_error>" +
				"<multiple_r_squared>" + "</multiple_r_squared>" +
				"<adjusted_r_squared>" + "</adjusted_r_squared>" +
				"<f_statistics>" +
					"<value>" + "</value>" +
					"<degrees_freedom_model>" + "</degrees_freedom_model>" +
					"<degrees_freedom_error>" + "</degrees_freedom_error>" +
					"<p_value>" + "</p_value>" +
				"</f_statistics>" +
			"</statistics>" +
			"<plot_data>" +
				"<fitted>" + "293.0241 413.0855 499.3122 600.7222 95.8560" +
				"</fitted>" +
				"<residuals>"+ "8.9758502 -13.0854827 0.6878046 -0.7221948 .1440228" +
				"</residuals>" +
				"<normal_scores>" +
					"<theoretical_quantiles>" + "</theoretical_quantiles>" +
					"<standardized_residuals>" + "</standardized_residuals>" +
				"</normal_scores>" +
				"<root_standardized_residuals>" + "</root_standardized_residuals>" +
				"<cooks_distance>" + "</cooks_distance>" +
			"</plot_data>" +
		"</linear_regression_result>" +
		"<error>" +
			"<error_code>" + "</error_code>" +
			"<error_message>" + "</error_message>" +
		"</error>" +
	"</analysis_output>" ;

}
