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
Some of the code was adopted from http://totheriver.com/learn/xml/xmltutorial.html.
With some modifications to meet SOCR needs.

This runs at client side (applet). It compose an XML input String from the data
and analysis type.
*/

package edu.ucla.stat.SOCR.analyses.jri.xml;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.jri.gui.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//For jdk1.5 with built in xerces parser
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

//For JDK 1.3 or JDK 1.4  with xerces 2.7.1
// in xercesImpl.jar.
//import org.apache.xml.serialize.XMLSerializer;
//import org.apache.xml.serialize.OutputFormat;

public class LogisticRegression extends JRIAnalysis{
	public static final String TAG_analysis_input = "analysis_input";
	public static final String TAG_analysis_model = "analysis_model";
	public static final String TAG_analysis_name = "analysis_name";
	public static final String TAG_data = "data";
	public static final String TAG_variable = "variable";
	public static final String TAG_variable_name = "variable_name";
	public static final String TAG_variable_type = "variable_type";
	public static final String TAG_data_type = "data_type";
	public static final String TAG_data_value = "data_value";
	public static final String RI_ANALYSIS_TYPE = "LOSIGTIC_REGRESSION";

	private final static int NUMBER_OF_Y_VAR = 1;
	//private final static String INTERCEPT = "INTERCEPT";
	private final static String Y_DATA_TYPE = DataType.FACTOR;
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	//private HashMap resultMap = null;
	private String[] varNames = null;
	private ArrayList<String> varNameList = new ArrayList<String>();
	private String[] varList = null;

	private Data data = new Data();
	private String type = "LogisticRegression";

	private String xmlInputString = null;
	private String xmlOutputString = null;

	private List<Dependent> dependentList;
	private List<Regressor> regressorList;
	private List<AnalysisModel> xmlList;
	private Document dom;

	// the method analyze extracts data and puts them into Document
	public void analyze(Data data, short analysisType) throws DataException, WrongAnalysisException{
		System.out.println("jri.LogisticRegression analysisType = " + analysisType);

		if (!(analysisType == AnalysisType.LOGISTIC_REGRESSION)) {
			////System.out.println("jri.LogisticRegression analysisType is wrong");
			throw new WrongAnalysisException();
		}
		else
			System.out.println("jri.LogisticRegression analysis Type is OK");

		this.data = data;
		createDocument();
		System.out.println("jri.LogisticRegression createDocument is OK");

		Element rootEle = dom.createElement(TAG_analysis_input);
		Element modelEle = dom.createElement(TAG_analysis_model);
		Element modelNamnEle = dom.createElement(TAG_analysis_name);

		Element dataEle = dom.createElement(TAG_data);
		//System.out.println("jri.LogisticRegression dataEle = " + dataEle);

		rootEle.appendChild(modelEle);
		rootEle.appendChild(dataEle);
		modelEle.appendChild(modelNamnEle);

		Text analysisNameText = dom.createTextNode("LOGISTIC");
		modelNamnEle.appendChild(analysisNameText);
		StringBuffer sb = new StringBuffer();
		System.out.println("jri.LogisticRegression analysisNameText = " + analysisNameText);

		HashMap xMap = data.getMapX(); // data not submitted in here?
		HashMap yMap = data.getMapY();


		System.out.println("jri.LogisticRegression xMap = " + xMap);
		System.out.println("jri.LogisticRegression yMap = " + yMap);
		Set keySet = xMap.keySet();
		Iterator iterator = keySet.iterator();
		System.out.println("jri.LogisticRegression keySet = " + keySet);
		System.out.println("jri.LogisticRegression iterator = " + iterator);

		String keys = "";
		ArrayList<double[]> x = new ArrayList<double[]>();
		double y[] = null;
		int xIndex = 0;
		//varNameList.add(0, INTERCEPT);
		Column xColumn = null;
		System.out.println("jri.LogisticRegression xMap iterator.hasNext() = " + iterator.hasNext());

		while (iterator.hasNext()) {

			keys = (String) iterator.next();
			System.out.println("LogisticRegression keys = " + keys);

			try {
				Class cls = keys.getClass();
			} catch (Exception e) {
				System.out.println("LogisticRegression Exception = " + e);
			}

			xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();
			System.out.println("LogisticRegression xDataType = " + xDataType);

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new DataException();
			}
			double xVector[] = xColumn.getDoubleArray();

			for (int i = 0; i < xVector.length; i++) {
				System.out.println("xVector["+i+"] = " + xVector[i]);
				sb.append(xVector[i] + ",");
			}

			String dataString = sb.toString();
			sb = new StringBuffer();

			dataString = dataString.substring(0, dataString.length() - 1);
			System.out.println("dataString = " + dataString);
			x.add(xIndex, xVector);
			System.out.println("xIndex = " + xIndex);
			varNameList.add(xIndex, keys);
			xIndex++;
			System.out.println("keys = " + keys);

			//regressorList.add(new Regressor(keys, null, null, dataString));
			Element varEle = dom.createElement(TAG_variable);

			Element varNameEle = dom.createElement(TAG_variable_name);
			Element varTypeEle = dom.createElement(TAG_variable_type);
			Element dataTypeEle = dom.createElement(TAG_data_type);
			Element dataValueEle = dom.createElement(TAG_data_value);

			Text varNameText = dom.createTextNode(keys);
			Text varTypeText = dom.createTextNode("INDEPENDENT");
			Text dataTypeText = dom.createTextNode("QUANTITATIVE");
			Text dataValueText = dom.createTextNode(dataString);

			varEle.appendChild(varNameEle);
			varEle.appendChild(varTypeEle);
			varEle.appendChild(dataTypeEle);
			varEle.appendChild(dataValueEle);

			varNameEle.appendChild(varNameText);
			varTypeEle.appendChild(varTypeText);
			dataTypeEle.appendChild(dataTypeText);
			dataValueEle.appendChild(dataValueText);

			dataEle.appendChild(varEle);
			//addToInputDocument(tag, content);
		}
		System.out.println("LogisticRegression done while xMap");
		keySet = yMap.keySet();
		iterator = keySet.iterator();
		////System.out.println("jri.LogisticRegression yMap iterator.hasNext() = " + iterator.hasNext());
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			////System.out.println("LogisticRegression while yMap keys = " + keys);

			try {
				Class cls = keys.getClass();
				////System.out.println("LogisticRegression cls.getName = " +cls.getName());
			} catch (Exception e) {
				////System.out.println("ymap Exception = " + e);
			}
		}
		////System.out.println("LogisticRegression done while yMap");


		Column yColumn = (Column) yMap.get(keys);

		////System.out.println("LogisticRegression yColumn = " + yColumn);

		////System.out.println("LogisticRegression " + yColumn.getDataType());
		String yDataType = yColumn.getDataType();

		//if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
		//	throw new DataException("\ny data type MUST be QUANTITATIVE but the input is of type " + yDataType);
		//}
		y = yColumn.getDoubleArray();
		////System.out.println("LogisticRegression  y.length = "+y.length );
		sb = new StringBuffer();

		for (int i = 0; i < y.length; i++) {
			System.out.println("y["+i+"] = " + y[i]);
			sb.append(y[i] + ",");
		}
		String dataString = sb.toString();
		dataString = dataString.substring(0, dataString.length() - 1);
//		dependentList.add(new Dependent(keys, null, null, dataString));
		Element varEle = dom.createElement(TAG_variable);
		////System.out.println("LogisticRegression  dom = "+dom);
		////System.out.println("LogisticRegression  varEle = "+varEle);

		Element varNameEle = dom.createElement(TAG_variable_name);
		Element varTypeEle = dom.createElement(TAG_variable_type);
		Element dataTypeEle = dom.createElement(TAG_data_type);
		Element dataValueEle = dom.createElement(TAG_data_value);
		////System.out.println("LogisticRegression  varNameEle = "+varNameEle);
		////System.out.println("LogisticRegression  varTypeEle = "+varTypeEle);
		////System.out.println("LogisticRegression  dataTypeEle = "+dataTypeEle);
		////System.out.println("LogisticRegression  dataValueEle = "+dataValueEle);

		Text varNameText = dom.createTextNode(keys);
		Text varTypeText = dom.createTextNode("DEPENDENT");
		Text dataTypeText = dom.createTextNode("FACTOR");
		Text dataValueText = dom.createTextNode(dataString);

		varEle.appendChild(varNameEle);
		varEle.appendChild(varTypeEle);
		varEle.appendChild(dataTypeEle);
		varEle.appendChild(dataValueEle);

		varNameEle.appendChild(varNameText);
		varTypeEle.appendChild(varTypeText);
		dataTypeEle.appendChild(dataTypeText);
		dataValueEle.appendChild(dataValueText);
		////System.out.println("LogisticRegression  dataEle = "+dataEle);

		dataEle.appendChild(varEle);

		varList = new String[varNameList.size()];
		for (int i = 0; i < varNameList.size(); i++) {
			//System.out.println("LogisticRegression varNameList["+i+"] = " + (String)varNameList.get(i));
			varList[i] = (String)varNameList.get(i);
		}
		dom.appendChild(rootEle);
		////System.out.println("LogisticRegression rootEle = " + rootEle);
		//return dom;
	}

	public String getAnalysisType() {
		return type;
	}

/////////////////////////////////////////////////////////////////////////////////////////

	private void loadData(){
		xmlList.add(new AnalysisModel(RI_ANALYSIS_TYPE));
		//myData.add(new Varible(RI_ANALYSIS_TYPE));

	}
	private void createDocument() {

		//get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);

		try {
			//get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			////System.out.println("createDocument db " + db);
			//create an instance of DOM
			dom = db.newDocument();
			////System.out.println("createDocument dom " + dom);
		}	catch(ParserConfigurationException pce) {
			//dump it
			////System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}
	}

	private String printToFile(){
		try
		{
			//print
			//OutputFormat format = new OutputFormat(dom);
			//format.setIndenting(true);

			//to generate output to console use this serializer
			//XMLSerializer serializer = new XMLSerializer(////System.out, format);

			//to generate a file output use fileoutputstream instead of ////System.out
			//XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File("analysis_output.xml")), format);
			//XMLSerializer serializer = new XMLSerializer(new String(), format);
			//serializer.serialize(dom);
			//return serializer;
			StringWriter sw = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(sw, new OutputFormat(dom));
			serializer.serialize(dom);

			String s = sw.getBuffer().toString();
			////System.out.println("RI printToFile done s = " + s);
			return s;

		} catch(IOException ie) {
		    ie.printStackTrace();
		}
		return null;
	}

	public String getAnalysisInputXMLString(Data data, short analysisType) {
		System.out.println("jri.LogisticRegression getAnalysisInputXMLString start");
		String result = null;
		try {
			System.out.println("jri.LogisticRegression getAnalysisInputXMLString before analyze");

			this.analyze(data, analysisType);
			System.out.println("jri.LogisticRegression getAnalysisInputXMLString after analyze");

			result = this.printToFile();
			System.out.println("jri.LogisticRegression STRING RESULT = " + result + " FINISH");

		} catch (Exception e) {
			System.out.println(e);
		}
		////System.out.println("jri.LogisticRegression getAnalysisInputXMLString finish");

		return result;
	}

	public static void main(String args[]) {
		Data data = new Data();
		double[] x1 = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] x2 = {60,94,91,81,80,92,74,89,96,87,86,94,94,94,79,50,92,82,94,78};
		double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		data.appendX("midterm1", x1, DataType.QUANTITATIVE);
		data.appendX("midterm2", x2, DataType.QUANTITATIVE);
		data.appendY("final", y, DataType.QUANTITATIVE);
		//create an instance
		LogisticRegression test = new LogisticRegression();
		test.getAnalysisInputXMLString(data, AnalysisType.MULTI_LINEAR_REGRESSION);

		//String resultString  = ((RIAnalysis)cls.newInstance()).getAnalysisInputXMLString(this, analysisType);
	}



	/////////////////// Private Classes /////////////////////////////
	private class AnalysisModel {
		private String analysisName;

		public AnalysisModel(String analysisName) {
			this.analysisName = analysisName;
		}

		public String getAnalysisName() {
			return analysisName;
		}

		public void getAnalysisName(String analysisName) {
			this.analysisName = analysisName;
		}


		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(" { AnalysisModel Details --");
			sb.append("analysisName:" + getAnalysisName());
			sb.append(". } \n");
			return sb.toString();
		}
	}

	private class Variable {
		String variable_name;
		String variable_type;
		String data_type;
		String data_value;

		Variable(String variable_name, String variable_type, String data_type, String data_value) {
			this.variable_name = variable_name;
			this.variable_type = variable_type;
			this.data_type = data_type;
			this.data_value = data_value;
		}
		public String getVariableName() {
			return this.variable_name;
		}
	}
	private class Dependent extends Variable {
		Dependent(String variable_name, String variable_type, String data_type, String data_value) {
			super(variable_name, "DENPEDENT", "QUANTITATIVE", data_value);
		}

	}
	private class Regressor extends Variable {
		Regressor(String variable_name, String variable_type, String data_type, String data_value) {
			super(variable_name, "INDENPEDENT", "QUANTITATIVE", data_value);

		}
	}

}
