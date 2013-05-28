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
// general linear model, is a parent class of all the linear model results.

package edu.ucla.stat.SOCR.analyses.result;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.StringReader;
import edu.ucla.stat.SOCR.analyses.xml.DomParserUtility;

public class LinearModelResult extends AnalysisResult{
	public static final String ALPHA = "ALPHA";
	public static final String BETA = "BETA";
	public static final String BETA_VAR = "BETA_VAR"; // vector of variance of beta
	public static final String BETA_SE = "BETA_SE"; // vector of se of beta

	public static final String R_SQUARED = "R_SQUARED";

	public static final String DF = "DF"; // DF in general
	public static final String DF_TOTAL = "DF_TOTAL"; // DF corrected total
	public static final String DF_ERROR = "DF_ERROR";
	public static final String DF_MODEL = "DF_MODEL";
	public static final String SLR_MEAN_X = "SLR_MEAN_X";
	public static final String SLR_MEAN_Y = "SLR_MEAN_Y";

	public static final String RSS_TOTAL = "RSS_TOTAL"; // SS corrected total
	public static final String RSS_ERROR = "RSS_ERROR";
	public static final String RSS_MODEL = "RSS_MODEL";

	public static final String MSS_ERROR = "MSS_ERROR";
	public static final String MSS_MODEL = "MSS_MODEL";

	public static final String ANOVA_Y_MEAN = "ANOVA_Y_MEAN";
	public static final String ANOVA_Y_GROUP_MEANS = "ANOVA_Y_GROUP_MEANS";

	public static final String SCORE = "SCORE";
	public static final String Z_SCORE = "Z_SCORE";

	public static final String VARIABLE_LIST = "VARIABLE_LIST";
	public static final String DF_GROUP = "DF_GROUP";
	public static final String RSS_GROUP = "RSS_GROUP";
	public static final String MSE_GROUP = "MSE_GROUP";
	public static final String F_VALUE_GROUP = "F_VALUE_GROUP";
	public static final String P_VALUE_GROUP = "P_VALUE_GROUP";
	/********** BEGIN RESIDUALS FROM MODELING RESULTS: MOSTLY FOR PLOTTING **********/
	public static final String SORTED_RESIDUALS = "SORTED_RESIDUALS";
	public static final String SORTED_RESIDUALS_INDEX = "SORTED_RESIDUALS_INDEX";
	public static final String SORTED_NORMAL_QUANTILES = "SORTED_NORMAL_QUANTILES";
	public static final String SORTED_STANDARDIZED_RESIDUALS = "SORTED_STANDARDIZED_RESIDUALS";
	public static final String SORTED_STANDARDIZED_NORMAL_QUANTILES = "SORTED_STANDARDIZED_NORMAL_QUANTILES";
	/********** END RESIDUALS FROM MODELING RESULTS: MOSTLY FOR PLOTTING **********/


	/************************ FOR THE REMOTE SERVER VERSION ******************/
	public static final String TAG_analysis_output = "TAG_analysis_output";
	public static final String TAG_linear_regression_result = "linear_regression_result";
	public static final String TAG_coefficients = "coefficients";
	public static final String TAG_intercept = "intercept";
	public static final String TAG_estimate = "estimate";
	public static final String TAG_standard_error = "standard_error";
	public static final String TAG_t_value = "t_value";
	public static final String TAG_p_value = "p_value";
	public static final String TAG_predictor = "predictor";
	public static final String TAG_variable_name = "variable_name";

	public static final String TAG_statistics = "statistics";
	public static final String TAG_residual_standard_error = "residual_standard_error";
	public static final String TAG_value = "value";
	public static final String TAG_degrees_freedom = "degrees_freedom";
	public static final String TAG_multiple_r_squared = "multiple_r_squared";
	public static final String TAG_adjusted_r_squared = "adjusted_r_squared";
	public static final String TAG_f_statistics = "f_statistics";

	public static final String TAG_degrees_freedom_model = "degrees_freedom_model";
	public static final String TAG_degrees_freedom_error = "degrees_freedom_error";
	public static final String TAG_plot_data = "plot_data";
	public static final String TAG_predicted = "predicted";
	public static final String TAG_residuals = "residuals";
	public static final String TAG_normal_scores = "normal_scores";
	public static final String TAG_theoretical_quantiles = "theoretical_quantiles";
	public static final String TAG_standardized_residuals = "standardized_residuals";
	public static final String TAG_root_standardized_residuals = "root_standardized_residuals";
	public static final String TAG_cooks_distance = "cooks_distance";

	protected Intercept intercept;
	protected Predictor predictor;
	protected PlotData plotData;

	protected ArrayList<Element> predictorList = new ArrayList<Element>();

	public LinearModelResult(HashMap<String,Object> texture) {
		super(texture);
	}
	public LinearModelResult(HashMap<String,Object> texture, HashMap<String,Object> graph) {
		super(texture, graph);
	}

	public LinearModelResult(String xmlResultString) {
		super(xmlResultString);
	}
	public LinearModelResult(Document dom) {
		super(dom);
	}
	
	protected class Intercept{
		double estimate;
		double se;
		double tStat;
		String pValue;

		Intercept(String estimate, String se, String tStat, String pValue) {
			this.estimate = Double.parseDouble(estimate);
			this.se = Double.parseDouble(se);
			this.tStat = Double.parseDouble(tStat);
			this.pValue = pValue;
		}

	}

	protected class Predictor{
		String name;
		double estimate;
		double se;
		double tStat;
		String pValue;

		Predictor(String name, String estimate, String se, String tStat, String pValue) {
			this.name = name;
			this.estimate = Double.parseDouble(estimate);
			this.se = Double.parseDouble(se);
			this.tStat = Double.parseDouble(tStat);
			this.pValue = pValue;
		}
	}
	protected class PlotData{
		double[] predicted;
		double[] residuals;


		PlotData(double[] predicted, double[] residuals) {
			this.predicted = predicted;
			this.residuals = residuals;
			for (int i = 0; i < predicted.length; i++) {
				//System.out.println("PlotData Constructor this.predicted["+i+"]= " + this.predicted[i]);
				//System.out.println("PlotData Constructor this.residuals["+i+"]= " + this.residuals[i]);

			}

		}
	}

	protected void parseIntercept(){
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName(TAG_intercept);
		if(nl != null && nl.getLength() > 0) {
			//for(int i = 0 ; i < nl.getLength();i++) {
			Element el = (Element)nl.item(0);
			 //System.out.println("LinearModelResult parseIntercept if el == " + el);

			intercept = getIntercept(el);
		}
	}
	protected void parsePredictors(){
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName(TAG_predictor);
		//Predictor predictor = null;
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
			 	//System.out.println("LinearModelResult parsePredictors if el == " + el);
				predictor = getPredictor(el);
				predictorList.add(el);
			}
		}
	}
	protected void parsePlotData(){
		//System.out.println("LinearModelResult parsePlotData starts");
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName(TAG_plot_data);
		//plotData = null;
		//System.out.println("LinearModelResult parsePlotData before if");

		if(nl != null && nl.getLength() > 0) {
			//System.out.println("LinearModelResult parsePlotData if nl != " + nl);

			//for(int i = 0 ; i < nl.getLength();i++) {
			Element el = (Element)nl.item(0);

			//System.out.println("LinearModelResult parsePlotData if el == " + el);
			plotData = getPlotData(el);
		}
		//System.out.println("LinearModelResult parsePlotData done");

	}
	protected Intercept getIntercept(Element element) {
		String estimate = DomParserUtility.getTextValue(element,TAG_estimate);
		String se = DomParserUtility.getTextValue(element,TAG_standard_error);
		String tStat = DomParserUtility.getTextValue(element,TAG_t_value);
		String pValue = DomParserUtility.getTextValue(element,TAG_p_value);

		Intercept e = new Intercept(estimate,se,tStat,pValue);
		return e;
	}
	protected Predictor getPredictor(Element element) {
		String name = DomParserUtility.getTextValue(element,TAG_variable_name);
		String estimate = DomParserUtility.getTextValue(element,TAG_estimate);
		String se = DomParserUtility.getTextValue(element,TAG_standard_error);
		String tStat = DomParserUtility.getTextValue(element,TAG_t_value);
		String pValue = DomParserUtility.getTextValue(element,TAG_p_value);

		Predictor e = new Predictor(name, estimate,se,tStat,pValue);
		return e;
	}
	protected PlotData getPlotData(Element element) {
		String predictedString = DomParserUtility.getTextValue(element,TAG_predicted);
		String residualsString = DomParserUtility.getTextValue(element,TAG_residuals);
		StringTokenizer stkPredicted = new StringTokenizer(predictedString, ",");
		StringTokenizer stkResiduals = new StringTokenizer(residualsString, ",");
		ArrayList<String> predictedList = new ArrayList<String>();
		ArrayList<String> residualsList = new ArrayList<String>();
		int index = 0;
		String tempP = null;
		String tempR = null;
     	while (stkPredicted.hasMoreTokens()) {
			tempP = stkPredicted.nextToken();
        	predictedList.add(index, tempP);
        	//System.out.println(tempP);
        	index++;
     	}
     	index = 0;
     	while (stkResiduals.hasMoreTokens()) {
        	tempR = stkResiduals.nextToken();
        	residualsList.add(index, tempR);
        	//System.out.println(tempR);

        	index++;
     	}
		double[] predicted = new double[predictedList.size()];
		double[] residuals = new double[residualsList.size()];
        //System.out.println("residualsList.size() = " + residualsList.size());

		for (int i = 0; i < residualsList.size(); i++) {
			predicted[i] = Double.parseDouble((String)predictedList.get(i));
			residuals[i] = Double.parseDouble((String)residualsList.get(i));
			//System.out.println("getPlotData predicted["+i+"]= " + predicted[i]);
			//System.out.println("getPlotData residuals["+i+"]= " + residuals[i]);

		}

		PlotData e = new PlotData(predicted, residuals);
		return e;
	}
}