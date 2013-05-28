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

package edu.ucla.stat.SOCR.analyses.xml;

import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

public class XMLComposer {
	private static String INDENPEDENT = "INDENPEDENT";
	private static String DENPEDENT = "DENPEDENT";
	private static String LINEAR_REGRESSION = "LINEAR_REGRESSION";
	private static String QUANTITATIVE = "QUANTITATIVE";
	private static String DATA_DELIMITATOR = ", "; // delimitator

	private String variableNameX = "X";
	private String variableNameY = "Y";
	private String variableTypeX = "";
	private String variableTypeY = "";
	
	private String dataTypeX = "";
	private String dataTypeY = "";
	private String dataX = "";
	private String dataY = "";

	private int analysisType = -1;	
	private String modelType = "";
	
	// carriage return and tab characters are here to make XML better looking.
	private String inputXML = "";

	public XMLComposer(double[] x, double[] y, String xName, String yName, int analysisType) {
		this.variableNameX = xName;
		this.variableNameY = yName;
		setAnalysisType(analysisType);
		setVariableTypeX(analysisType);
		setVariableTypeY(analysisType);
		setDataTypeX(analysisType);
		setDataTypeY(analysisType);
		composeXML(x, y);
	}
	public String getComposedXML() {
		return inputXML;
	}
	private void composeXML(double[] x, double[] y) {
		String xString = "";
		String yString = "";
		
		// minus one because the last datum does not need delimitator.
		for (int i = 0; i < x.length - 1; i++) {
			xString += x[i] + DATA_DELIMITATOR;
		}
		// and in case x and y do not have the same length.
		for (int i = 0; i < x.length; i++) {
			yString += y[i] + DATA_DELIMITATOR;
		}
		
		this.dataX = xString;
		this.dataY = yString;
		
		inputXML = 
		"<analysis_input>\n" + 
		"\t<analysis_model>\n" + 
		"\t\t<analysis_name>" + 
		modelType + 	
		"</analysis_name>\n" + 		
		"\t</analysis_model>\n" + 
		"\t<data>\n" + 
		"\t\t<varaible>\n" + 
		"\t\t\t<variable_name>" + 
		variableNameX + 
		"</variable_name>\n" + 
		"\t\t\t<variable_type>" +
		variableTypeX +
		"</variable_type>\n" + 
		"\t\t\t<data_type>"+
		dataTypeX + 
		"</data_type>\n" + 
		"\t\t\t<data_value>" + 
		dataX +
		"</data_value>\n" + 
		"\t\t</variable>\n" + 
		"\t\t<varaible>\n" + 
		"\t\t\t<variable_name>" +
		variableNameY +
		"</variable_name>\n" + 
		"\t\t\t<variable_type>" +
		variableTypeY + 
		"</variable_type>\n" + 
		"\t\t\t<data_type>" +
		dataTypeY +
		"</data_type>\n" + 
		"\t\t\t<data_value>" +
		dataY +
		"</data_value>\n" +
		"\t\t</variable>\n" +
		"\t</data>\n" +
		"</analysis_input>";
	}
	private void setAnalysisType(int analysisType) {
		switch (analysisType) {
			case AnalysisType.SIMPLE_LINEAR_REGRESSION:
				{ this.modelType = LINEAR_REGRESSION; }
		}

	}
	private void setVariableNameX(int input) {

	}
	private void setVariableNameY(int input) {

	}
	private void setVariableTypeX(int input) {
		switch (input) {
			case AnalysisType.SIMPLE_LINEAR_REGRESSION:
				{ this.variableTypeX = INDENPEDENT; }
		}

	}
	private void setVariableTypeY(int input) {
		switch (input) {
			case AnalysisType.SIMPLE_LINEAR_REGRESSION:
				{ this.variableTypeY = DENPEDENT; }
		}

	}
	private void setDataTypeX(int input) {
		switch (input) {
			case AnalysisType.SIMPLE_LINEAR_REGRESSION:
				{ this.dataTypeX = QUANTITATIVE; }
		}

	}
	private void setDataTypeY(int input) {
		switch (input) {
			case AnalysisType.SIMPLE_LINEAR_REGRESSION:
				{ this.dataTypeY = QUANTITATIVE; 
				}
		}

	}

	public String composeXML(String[] x, double[] y) {
		return inputXML;
	}

	public static void main(String args[]) {
		double[] x = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		String xName = "some_x_var";
		String yName = "some_y_var";
		XMLComposer xmlComposer = new XMLComposer(x, y, xName, yName, AnalysisType.SIMPLE_LINEAR_REGRESSION);
		
		String xml = xmlComposer.getComposedXML();
		System.out.println(xml);
	}
}
