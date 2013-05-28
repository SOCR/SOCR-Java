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
It's Online, Therefore, It Exists!
****************************************************/
/* annie che. 200608.

Most of the code was adopted from http://totheriver.com/learn/xml/xmltutorial.html.
With some modifications to meet SOCR needs.

This class runs on the server side. It takes a string of input and generates R statements.

*/

//package edu.ucla.stat.SOCR.servlet.analyses;


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



//For jdk1.5 with built in xerces parser
//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

//For JDK 1.3 or JDK 1.4  with xerces 2.7.1
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;


public class LinearModel {
	//No generics
	List dependentList;
	List regressorList;

	Document dom;

	private String xmlInputString = "";
	private static String testString = "<analysis_input><analysis_model> <analysis_name>LINEAR_MODEL</analysis_name></analysis_model><data><variable><variable_name>var_x</variable_name><variable_type>INDENPEDENT</variable_type><data_type>QUANTITATIVE</data_type><data_value>68.0, 49.0, 60.0, 68.0, 97.0, 82.0, 59.0, 50.0, 73.0, 39.0, 71.0, 95.0, 61.0, 72.0, 87.0, 40.0, 66.0, 58.0, 58.0, </data_value></variable><variable><variable_name>var_y</variable_name><variable_type>DENPEDENT</variable_type> <data_type>QUANTITATIVE</data_type><data_value>75.0, 63.0, 57.0, 88.0, 88.0, 79.0, 82.0, 73.0, 90.0, 62.0, 70.0, 96.0, 76.0, 75.0, 85.0, 40.0, 74.0, 70.0, 75.0, 72.0, </data_value></variable></data></analysis_input>";
	//private String fileName = "C:\\STAT\\SOCR\\test\\xml\\LinearModel.xml";

	/************************* Constructor *******************************/
	public LinearModel(String xmlInputString){
		this.xmlInputString = testString;//xmlInputString;
		//create a list to hold the employee objects
		regressorList = new ArrayList();
		dependentList = new ArrayList();

	}

	public String generateRStatements() {

		//parse the xml file and get the dom object
		parseXmlFile();

		//get each employee element and create a Regressor object
		parseDocument();

		//Iterate through the list and print the data
		//printData();
		return printRStatements();

	}


	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Reader in = new BufferedReader(new FileReader(args[0]));
			InputSource input = new InputSource(new StringReader(xmlInputString));

			// Tell the Xerces parser to parse the input source
			//db.parse(input);
			//parse using builder to get DOM representation of the XML file
			//System.out.println("Linear Model fileName = " + fileName);
			//dom = db.parse(fileName); // use an input file of XML.
			dom = db.parse(input); // use String declared at the top.

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}


	private void parseDocument(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();

		//get a nodelist of <employee> elements
		NodeList nl = docEle.getElementsByTagName("variable");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the employee element
				Element el = (Element)nl.item(i);

				//get the Regressor object
				Variable var = getVariable(el);


				//add it to list
				if(var.variable_type.equalsIgnoreCase("INDENPEDENT")) {
					regressorList.add(var);
				}
				else if (var.variable_type.equalsIgnoreCase("DENPEDENT")) {
					dependentList.add(var);
				}
			}
		}
	}


	/**
	 * I take an employee element and read the values in, create
	 * an Regressor object and return it
	 * @param empEl
	 * @return
	 */
	private Variable getVariable(Element empEl) {

		//for each <employee> element get text or int values of
		//name ,id, age and name
		String analysis_name = getTextValue(empEl,"analysis_name");
		String variable_name = getTextValue(empEl,"variable_name");
		String variable_type = getTextValue(empEl,"variable_type");
		String data_type = getTextValue(empEl,"data_type");
		String data_value = getTextValue(empEl,"data_value");

		//String type = empEl.getAttribute("type");
		//System.out.println("get variable_name = " + variable_name +", variable_type = " + variable_type + ", data_value = " + data_value );
		//Create a new Regressor with the value read from the xml nodes

		Variable variable = new Variable(variable_name, variable_type, data_value);

		return variable;
	}


	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}


	/**
	 * Calls getTextValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}

	/**
	 * Iterate through the list and print the
	 * content to console
	 */
	private void printData(){

		//System.out.println("No of Regressors '" + regressorList.size() + "'.");
		//System.out.println("No of Dependent '" + dependentList.size() + "'.");
		StringBuffer sb = new StringBuffer();

		Iterator it = regressorList.iterator();
		while(it.hasNext()) {
			System.out.println("printData regressor: " + it.next().toString());
		}

		it = dependentList.iterator();
		while(it.hasNext()) {
			System.out.println("printData depedent: " + it.next().toString());
		}

	}

	private String printRStatements(){

		StringBuffer sb = new StringBuffer();
		StringBuffer sbRegressorList = new StringBuffer();

		Iterator it = regressorList.iterator();
		Variable r = null;
		Variable d = null;
		while(it.hasNext()) {
			r = (Variable)(it.next());
			sb.append(r.variable_name);
			sb.append(" = c(");
			sb.append(r.data_value);
			sb.append("); ");
			sbRegressorList.append(r.variable_name + " + ");
		}
		sb.append("\n");
		it = dependentList.iterator();
		while(it.hasNext()) {
			d = (Variable)(it.next());
			sb.append(d.variable_name);
			sb.append(" = c(");
			sb.append(d.data_value);
			sb.append("); ");
		}
		sb.append("\n");

		String regressorList = sbRegressorList.toString();
		regressorList = regressorList.substring(0,regressorList.length()-3);
		sb.append("m = lm(" + d.variable_name + " ~ " + regressorList + ")");
		System.out.println("\n\nR Statements = \n" + sb.toString() );
		return sb.toString();
	}

	/*************************** Main test code ***********************************/
	public static void main(String[] args){
		//create an instance
		//String xmlInputStringTest = "";
		System.out.println(testString);
		LinearModel test = new LinearModel(new String(testString));

		//call run example
		String rStatements = test.generateRStatements();

		System.out.println(rStatements);

		// the class and method to call to get the output from R. (in XML)
		/////String outputXmlString = SomeClass.someMethod(rStatements);
		// OR, like below if we submit input in XML form.
		/////String outputXmlString = SomeClass.someMethod(rStatements);

	}


	/********************* Private classes declarations ********************/

	private class Variable {
		String variable_name;
		String variable_type;
		//String data_type;
		String data_value;

		Variable(String variable_name, String variable_type, String data_value) {
			this.variable_name = variable_name;
			this.variable_type = variable_type;
			this.data_value = data_value;

		}
	}
	private class Dependent extends Variable {
		Dependent(String variable_name, String variable_type, String data_value) {
			super(variable_name, variable_type, data_value);
		}

	}
	private class Regressor extends Variable {
		Regressor(String variable_name, String variable_type, String data_value) {
			super(variable_name, variable_type, data_value);

		}
	}
}

