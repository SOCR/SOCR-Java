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
/* annieche 20060120 */

/* this class takes an XML String of OUTPUT and retreives value to construct a HashMap of analyses.result.Result.
classes in analyses.gui can get this Result HashMap and get the data.
*/

package edu.ucla.stat.SOCR.analyses.xml;

import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.JRI.*;
import org.w3c.dom.*;
import java.util.HashMap;

import edu.ucla.stat.SOCR.analyses.result.*;

public class JRIRequestDispatcher {
	public static String getOutputXmlString(String input) {
		return outputXml;
	}

	public static String outputXml = /* this xml is for testing only */
	"<analysis_output>\n" +
		"\t<linear_regression_result>\n" +
			"\t\t<coefficients>\n" +
				"\t\t\t<intercept>\n" +
					"\t\t\t\t<estimate>1230</estimate>\n" +
					"\t\t\t\t<standard_error/>\n" +
					"\t\t\t\t<t_value/>\n" +
					"\t\t\t\t<p_value/>\n" +
				"\t\t\t</intercept>\n" +
				"\t\t\t<predictor>\n" +
					"\t\t\t\t<variable_name>X1</variable_name>\n" +
					"\t\t\t\t<estimate>3450</estimate>\n" +
					"\t\t\t\t<standard_error/>\n" +
					"\t\t\t\t<t_value/>\n" +
					"\t\t\t\t<p_value/>\n" +
				"\t\t\t</predictor>\n" +
				"\t\t\t<predictor>\n" +
					"\t\t\t\t<variable_name>X2</variable_name>\n" +
					"\t\t\t\t<estimate>67.90</estimate>\n" +
					"\t\t\t\t<standard_error/>\n" +
					"\t\t\t\t<t_value/>\n" +
					"\t\t\t\t<p_value/>\n" +
				"\t\t\t</predictor>\n" +
			"\t\t</coefficients>\n" +
			"\t\t<statistics>\n" +
				"\t\t\t<residual_standard_error>\n" +
					"\t\t\t\t<value/>\n" +
					"\t\t\t\t<degrees_freedom_residual/>\n" +
				"\t\t\t</residual_standard_error>\n" +
				"\t\t\t<multiple_r_squared></multiple_r_squared>\n" +
				"\t\t\t<adjusted_r_squared></adjusted_r_squared>\n" +
				"\t\t\t<f_statistics>\n" +
					"\t\t\t\t<value/>\n" +
					"\t\t\t\t<degrees_freedom_model/>\n" +
					"\t\t\t\t<degrees_freedom_error/>\n" +
					"\t\t\t\t<p_value/>\n" +
				"\t\t\t</f_statistics>\n" +
			"\t\t</statistics>\n" +
			"\t\t<plot_data>\n" +
				"\t\t\t<predicted>\n" +
				"\t\t\t</predicted>\n" +
				"\t\t\t<residuals>\n" +
				"\t\t\t</residuals>\n" +
				"\t\t\t<normal_scores>\n" +
					"\t\t\t\t<theoretical_quantiles></theoretical_quantiles>\n" +
					"\t\t\t\t<standardized_residuals></standardized_residuals>\n" +
				"\t\t\t</normal_scores>\n" +
				"\t\t\t<root_standardized_residuals></root_standardized_residuals>\n" +
				"\t\t\t<cooks_distance></cooks_distance>\n" +
			"\t\t</plot_data>\n" +
		"\t</linear_regression_result>\n" +
		"\t<error>\n" +
			"\t\t<error_code/>\n" +
			"\t\t<error_message/>\n" +
		"\t</error>\n" +
	"</analysis_output>";
}
