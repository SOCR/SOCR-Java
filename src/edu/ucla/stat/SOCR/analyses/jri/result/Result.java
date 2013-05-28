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

package edu.ucla.stat.SOCR.analyses.jri.result;

import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.StringReader;

public abstract class Result {
	// Very commonly used ones.
	/** t statistic of a test. */
	protected Document dom;
	protected String xmlResultString;

	public static final String T_STAT = "T_STAT";

	/** the mean of a sample. */
	public static final String SAMPLE_MEAN = "SAMPLE_MEAN";

	/** the variance of a sample. */
	public static final String SAMPLE_VAR = "SAMPLE_VAR";

	/** the variance of a sample. */
	public static final String DF = "DF"; // DF in general
	public static final String F_VALUE = "F_VALUE";
	public static final String P_VALUE = "P_VALUE";

	/** the residuals (as a double[]) of a model. */
	public static final String RESIDUALS = "RESIDUALS";

	/** the predived values (as a double[]) of a model. */
	public static final String PREDICTED = "PREDICTED";

	/** the mean of . */
	public static final String MEAN_X = "MEAN_X";
	public static final String MEAN_Y = "MEAN_Y";
	public static final String MEAN_DIFF = "MEAN_DIFF";


	public static final String WILCOXON_RANK_SUM_SMALLER = "WILCOXON_RANK_SUM_SMALLER";
	public static final String WILCOXON_R_PRIME = "WILCOXON_R_PRIME";
	public static final String WILCOXON_R_MIN  = "WILCOXON_R_MIN";

	public static final String SIGNED_TEST_W_STAT = "SIGNED_TEST_W_STAT";
	public static final String SIGNED_TEST_W_MEAN = "SIGNED_TEST_W_MEAN";
	public static final String SIGNED_TEST_W_VAR  = "SIGNED_TEST_W_VAR";
	public static final String SIGNED_TEST_W_SCORE  = "SIGNED_TEST_W_SCORE";
	public static final String SIGN_TEST_STAT  = "SIGN_TEST_STAT";

	public static final String JAVA_ERROR = "JAVA_ERROR";

	protected HashMap texture;
	protected HashMap graph;
	//protected boolean useLocal = true;
	public HashMap getTexture() {
		return texture;
	}
	public HashMap getGraph() {
		return graph;
	}
	public Result(HashMap texture) {
		this.texture = texture;
	}
	public Result(HashMap texture, HashMap graph) {
		this.texture = texture; 	// will hold texture results.
		this.graph = graph;		// will hold results related to graphs.
	}

	public Result(String xmlResultString) {
		// arguement parsed from xmlString means the output string from R, supposedly validated with DTD.
		this.xmlResultString = xmlResultString;
	}
	public Result(Document dom) {
		// arguement parsed from xmlString means the output string from R, supposedly valid
		this.dom = dom;
	}

}
