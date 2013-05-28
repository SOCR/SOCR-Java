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


public class LogisticRegressionResult extends LinearModelResult {
	public static final String TAG_z_value = "z_value";

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
	public static final String TAG_predicted = "fitted";


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
	public LogisticRegressionResult(HashMap<String,Object> texture) {
		super(texture);
	}
	public LogisticRegressionResult(HashMap<String,Object> texture, HashMap<String,Object> graph) {
		super(texture, graph);
	}
	public LogisticRegressionResult(String xmlResultString) {
		super(xmlResultString);
		//useLocal = false;
		this.dom = DomParserUtility.xmlStringToDocument(xmlResultString);
		new LogisticRegressionResult(this.dom);
	}
	public LogisticRegressionResult(Document dom) {
		super(dom);
		this.dom = dom;

		//System.out.println("LogisticRegressionResult dom start : this.alpha " + this.alpha);
		// construct Intercept

		parseIntercept();
		System.out.println("LogisticRegressionResult parseIntercept : this.alpha " + this.alpha);
		this.alpha = intercept.estimate;

		//////System.out.println("LogisticRegressionResult intercept : this.alpha " + this.alpha);
		this.alphaSE = intercept.se;
		//this.setAlpha(intercept.estimate);
		this.alphaTStat = intercept.tStat;
		this.alphaPValue = intercept.pValue;

		//System.out.println("LogisticRegressionResult this.alpha : " + this.alpha);
		//System.out.println("LogisticRegressionResult : " + intercept.se);
		//System.out.println("LogisticRegressionResult : " + intercept.tStat);
		//System.out.println("LogisticRegressionResult : " + intercept.pValue);
		// construct Predictors
		parsePredictors();
		//System.out.println("parsePredictors done");

		// construct PlotData.
		parsePlotData();
		//System.out.println("parsePlotData done");

		//System.out.println("LogisticRegressionResult end constructor this.alpha : " + this.alpha);
		//System.out.println("LogisticRegressionResult end constructor getAlpha : " + this.getAlpha());

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
		//System.out.println("LogisticRegressionResult getAlpha = " + this.alpha);
		return this.alpha;

	}
	public void setAlpha(double input) {
		this.alpha = input;
		//////System.out.println("LogisticRegressionResult setAlpha = " + this.alpha);

	}
	public double getBeta() {

		parsePredictors();
		beta = predictor.estimate;
		//System.out.println("LogisticRegressionResult getBeta = " + this.beta);
		return this.beta;

	}
	public double getAlphaSE() {

		parseIntercept();
		alphaSE = intercept.se;
		//System.out.println("LogisticRegressionResult getAlphaSE = " + this.alphaSE);
		return this.alphaSE;

	}
	public double getBetaSE() {

		parsePredictors();
		betaSE = predictor.se;
		//System.out.println("LogisticRegressionResult betaSE = " + this.betaSE);
		return this.betaSE;

	}
	public double getAlphaTStat() {

		parseIntercept();
		alphaTStat = intercept.tStat;
		//System.out.println("LogisticRegressionResult alphaTStat = " + this.alphaTStat);

		return this.alphaTStat;

	}
	public double getBetaTStat() {
		parsePredictors();
		betaTStat = predictor.se;
		//System.out.println("LogisticRegressionResult betaSE = " + this.betaSE);

		return this.betaTStat;
	}
	public String getAlphaPValue() {

		parsePredictors();
		alphaPValue = intercept.pValue;
		//System.out.println("LogisticRegressionResult getAlphaPValue = " + this.alphaPValue);

		return this.alphaPValue;


	}
	public String getBetaPValue() {
		parsePredictors();
		betaPValue = predictor.pValue;
		//System.out.println("LogisticRegressionResult betaPValue = " + this.betaPValue);

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
	
	protected Intercept getIntercept(Element element) {

		System.out.println("LinearModelResult getIntercept");
		String estimate = DomParserUtility.getTextValue(element,TAG_estimate);
		String se = DomParserUtility.getTextValue(element,TAG_standard_error);
		String zStat = DomParserUtility.getTextValue(element,TAG_z_value);
		String pValue = DomParserUtility.getTextValue(element,TAG_p_value);
		System.out.println("LinearModelResult estimate = " + estimate);
		System.out.println("LinearModelResult se = " + se);
		System.out.println("LinearModelResult zStat = " + zStat);
		System.out.println("LinearModelResult pValue = " + pValue);

		Intercept e = new Intercept(estimate,se,zStat,pValue);
		return e;
	}
	protected Predictor getPredictor(Element element) {
		String name = DomParserUtility.getTextValue(element,TAG_variable_name);
		String estimate = DomParserUtility.getTextValue(element,TAG_estimate);
		String se = DomParserUtility.getTextValue(element,TAG_standard_error);
		String zStat = DomParserUtility.getTextValue(element,TAG_z_value);
		String pValue = DomParserUtility.getTextValue(element,TAG_p_value);

		Predictor e = new Predictor(name, estimate,se,zStat,pValue);
		return e;
	}
	public static void main(String args[]) {
		LogisticRegressionResult result = new LogisticRegressionResult(outputTest);
		System.out.println("in main " + result.getAlpha());
		System.out.println("in main " + result.getBeta());
		System.out.println("in main " + result.getAlphaSE());
		System.out.println("in main " + result.getBetaSE());
		System.out.println("in main " + result.getAlphaTStat());
		System.out.println("in main " + result.getBetaTStat());
		System.out.println("in main " + result.getAlphaPValue());
		System.out.println("in main " + result.getBetaPValue());
		System.out.println("in main " + result.getBetaName());

		////System.out.println("in main " + result.getResiduals());
		////System.out.println("in main " + result.getPredicted());
		double[] resid = result.getResiduals();
		double[] predi = result.getPredicted();
		for (int i = 0; i < resid.length; i++) {
			////System.out.println("resid["+i+"]= " + resid[i] + ", predi["+i+"] = " + predi[i]);
		}
	}

	
	private static String outputTest = "<analysis_output><linear_regression_result><coefficients><intercept><estimate>-5.3094534</estimate><standard_error>1.13365365</standard_error><z_value>-4.683488</z_value><p_value>2.820338e-06</p_value></intercept><predictor><name>var_x</name><estimate>0.1109211</estimate><standard_error>0.02405982</standard_error><z_value>4.610224</z_value><p_value>4.022356e-06</p_value></predictor></coefficients><statistics><residual_standard_error><value/><degrees_freedom_residual/></residual_standard_error></statistics><plot_data><fitted>0.04347876 0.05962145 0.06615278 0.07334379 0.07334379 0.08124847 0.08124847  0.09942218 0.09942218 0.10980444 0.12112505 0.12112505 0.12112505 0.12112505  0.12112505 0.12112505 0.14679324 0.14679324 0.16123662 0.16123662 0.17680662  0.17680662 0.17680662 0.17680662 0.17680662 0.19353324 0.19353324 0.21143583  0.21143583 0.21143583 0.23052110 0.23052110 0.23052110 0.25078125 0.25078125  0.27219215 0.27219215 0.29471199 0.29471199 0.31828021 0.31828021 0.34281708  0.34281708 0.34281708 0.34281708 0.36822381 0.36822381 0.36822381 0.39438351  0.39438351 0.39438351 0.39438351 0.42116276 0.42116276 0.44841400 0.44841400  0.47597858 0.47597858 0.47597858 0.50369030 0.50369030 0.50369030 0.53137935  0.53137935 0.53137935 0.55887652 0.55887652 0.58601724 0.61264546 0.61264546  0.63861714 0.63861714 0.66380304 0.68809096 0.68809096 0.68809096 0.71138714  0.71138714 0.71138714 0.73361695 0.73361695 0.73361695 0.73361695 0.73361695  0.73361695 0.75472490 0.75472490 0.75472490 0.77467399 0.77467399 0.79344462  0.79344462 0.81103299 0.82744940 0.82744940 0.84271622 0.85686593 0.85686593  0.86993915 0.91246455 </fitted><residuals>-1.045450 -1.063397 -1.070835 -1.079145 13.634372 -1.088430 -1.088430 -1.110395  -1.110395 -1.123346 -1.137815 -1.137815 -1.137815 -1.137815 -1.137815  8.255910  -1.172046 -1.172046 -1.192229 -1.192229 -1.214779 -1.214779  5.655887 -1.214779  -1.214779 -1.239975 -1.239975 -1.268126  4.729561 -1.268126 -1.299579  4.337992  -1.299579 -1.334722 -1.334722 -1.373988  3.673872 -1.417859  3.393141 -1.466877  -1.466877 -1.521646 -1.521646 -1.521646  2.917006 -1.582838 -1.582838  2.715739  -1.651210 -1.651210  2.535602  2.535602 -1.727601  2.374379 -1.812954  2.230082  -1.908319 -1.908319  2.100935 -2.014871  1.985347  1.985347 -2.133922 -2.133922   1.881895 -2.266939  1.789304 -2.415560 -2.581614  1.632265  1.565883  1.565883   1.506471 -3.206062  1.453296  1.453296  1.405704  1.405704  1.405704 -3.753991  -3.753991  1.363109  1.363109  1.363109  1.363109 -4.077053  1.324985  1.324985   1.290865  1.290865 -4.841313  1.260326  1.232994  1.208532  1.208532  1.186638  -6.986447  1.167042  1.149504  1.095930 </residuals><normal_scores><theoretical_quantiles>  0.16365849  0.13830421  0.11303854  0.08784484  2.57582930  0.03760829   0.06270678 -0.01253347  0.01253347 -0.03760829 -0.16365849 -0.13830421  -0.11303854 -0.08784484 -0.06270678  2.17009038 -0.21470157 -0.18911843  -0.26631061 -0.24042603 -0.37185609 -0.34512553  1.95996398 -0.31863936  -0.29237490 -0.42614801 -0.39885507 -0.48172685  1.81191067 -0.45376219  -0.53883603  1.69539771 -0.51007346 -0.59776013 -0.56805150 -0.62800601   1.59819314 -0.65883769  1.51410189 -0.72247905 -0.69030882 -0.82389363  -0.78919165 -0.75541503  1.43953147 -0.89647336 -0.85961736  1.37220381  -0.97411388 -0.93458929  1.25356544  1.31057911 -1.01522203  1.20035886  -1.05812162  1.15034938 -1.15034938 -1.10306256  1.10306256 -1.20035886   1.01522203  1.05812162 -1.31057911 -1.25356544  0.97411388 -1.37220381   0.93458929 -1.43953147 -1.51410189  0.89647336  0.82389363  0.85961736   0.78919165 -1.59819314  0.72247905  0.75541503  0.62800601  0.65883769   0.69030882 -1.81191067 -1.69539771  0.51007346  0.53883603  0.56805150   0.59776013 -1.95996398  0.45376219  0.48172685  0.39885507  0.42614801  -2.17009038  0.37185609  0.34512553  0.29237490  0.31863936  0.26631061  -2.57582930  0.24042603  0.21470157  0.18911843</theoretical_quantiles><standardized_residuals>-0.2864880 -0.3368999 -0.3554857 -0.3750239  2.1963203 -0.3955511 -0.3955511  -0.4397153 -0.4397153 -0.4634209 -0.4882518 -0.4882518 -0.4882518 -0.4882518  -0.4882518  1.9742245 -0.5414033 -0.5414033 -0.5697729 -0.5697729 -0.5993642  -0.5993642  1.7886318 -0.5993642 -0.5993642 -0.6301910 -0.6301910 -0.6622610   1.6937984 -0.6622610 -0.6955761  1.6460219 -0.6955761 -0.7301315 -0.7301315  -0.7659148  1.5500272 -0.8029063  1.5019373 -0.8410779 -0.8410779 -0.8803938  -0.8803938 -0.8803938  1.4059329 -0.9208097 -0.9208097  1.3581764 -0.9622735  -0.9622735  1.3106950  1.3106950 -1.0047256  1.2635755 -1.0480993  1.2169059  -1.0923217 -1.0923217  1.1707746 -1.1373146  1.1252685  1.1252685 -1.1829954  -1.1829954  1.0804728 -1.2292786  1.0364689 -1.2760765 -1.3233009  0.9511383   0.9099479  0.9099479  0.8698200 -1.4666637  0.8308047  0.8308047  0.7929440   0.7929440  0.7929440 -1.5628231 -1.5628231  0.7562718  0.7562718  0.7562718   0.7562718 -1.6108516  0.7208136  0.7208136  0.6865875  0.6865875 -1.7064792   0.6536036  0.6218649  0.5913680  0.5913680  0.5621034 -1.8945436  0.5340561   0.5072066  0.4112647 </standardized_residuals></normal_scores><root_standardized_residuals>0.5362699 0.5817969 0.5977004 0.6139679 1.4858125 0.6305975 0.6305975 0.6649296  0.6649296 0.6826218 0.7006556 0.7006556 0.7006556 0.7006556 0.7006556 1.4089025  0.7377111 0.7377111 0.7567108 0.7567108 0.7760083 0.7760083 1.3405451 0.7760083  0.7760083 0.7955891 0.7955891 0.8154377 1.3040885 0.8154377 0.8355374 1.2853202  0.8355374 0.8558703 0.8558703 0.8764177 1.2467820 0.8971596 1.2270527 0.9180754  0.9180754 0.9391431 0.9391431 0.9391431 1.1867947 0.9603395 0.9603395 1.1663206  0.9816402 0.9816402 1.1456551 1.1456551 1.0030193 1.1248281 1.0244493 1.1038695  1.0459009 1.0459009 1.0828091 1.0673435 1.0616760 1.0616760 1.0887445 1.0887445  1.0404983 1.1100704 1.0193035 1.1312864 1.1523573 0.9769665 0.9558745 0.9558745  0.9348655 1.2143514 0.9139630 0.9139630 0.8931898 0.8931898 0.8931898 1.2543406  1.2543406 0.8725685 0.8725685 0.8725685 0.8725685 1.2738479 0.8521211 0.8521211  0.8318692 0.8318692 1.3117780 0.8118337 0.7920347 0.7724915 0.7724915 0.7532220  1.3829250 0.7342429 0.7155696 0.6441798 </root_standardized_residuals><cooks_distance>0.0008632379 0.0013083297 0.0014912159 0.0016922923 0.0580428657 0.0019115803  0.0019115803 0.0024023893 0.0024023893 0.0026712373 0.0029528561 0.0029528561  0.0029528561 0.0029528561 0.0029528561 0.0482778209 0.0035420549 0.0035420549  0.0038422444 0.0038422444 0.0041408752 0.0041408752 0.0368767291 0.0041408752  0.0041408752 0.0044342505 0.0044342505 0.0047194867 0.0308716499 0.0047194867  0.0049951510 0.0279724461 0.0049951510 0.0052619923 0.0052619923 0.0055237273  0.0226229834 0.0057878202 0.0202529795 0.0060661812 0.0060661812 0.0063756931  0.0063756931 0.0063756931 0.0162593303 0.0067384705 0.0067384705 0.0146599983  0.0071817615 0.0071817615 0.0133240791 0.0133240791 0.0077374168 0.0122378028  0.0084408810 0.0113788129 0.0093297025 0.0093297025 0.0107179875 0.0104416044  0.0102215885 0.0102215885 0.0118122164 0.0118122164 0.0098535554 0.0134726135  0.0095777552 0.0154468506 0.0177497026 0.0091698284 0.0089815500 0.0089815500  0.0087751868 0.0266041864 0.0085366376 0.0085366376 0.0082575194 0.0082575194  0.0082575194 0.0338836799 0.0338836799 0.0079346308 0.0079346308 0.0079346308  0.0079346308 0.0378018000 0.0075691556 0.0075691556 0.0071657153 0.0071657153  0.0458856247 0.0067313702 0.0062746584 0.0058047362 0.0058047362 0.0053306657  0.0611715360 0.0048608688 0.0044027494 0.0027904653</cooks_distance></plot_data></linear_regression_result><error><error_code/><error_message/></error></analysis_output>";

}
