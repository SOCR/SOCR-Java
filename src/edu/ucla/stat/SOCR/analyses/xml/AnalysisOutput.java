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
import java.util.ArrayList;

import edu.ucla.stat.SOCR.analyses.result.*;

public class AnalysisOutput {
	//private String inputXml = "";
	//private String outputXml = "";
	private XMLtree xmlDocumentTree = null;
	private HashMap<String,Object> texture = null;
	private boolean useXmlString = true;
	private ArrayList<String> varList = new ArrayList<String>();

	AnalysisResult result = new AnalysisResult(texture);

	public static void main(String args[]) {
		// the input part
		double[] x = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		//double[] x2 = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] y = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		String xName = "var_x";
		String yName = "var_y";

		/*
		step 1: XMLComposer build the input xml from scratch, using the double array data
		step 2: pass the built inputXml String to JRIRequestDispatcher and fetch the outputXml result.
		step 3. parse the result and put them in HashMap (Result class) but this is done in the Constructor.
		*/
		AnalysisOutput output = new AnalysisOutput();
		output.addToVarList("intercept");
		output.addToVarList(xName);
		//output.add(xName2); second variable
		XMLComposer xmlComposer = new XMLComposer(x, y, xName, yName, AnalysisType.SIMPLE_LINEAR_REGRESSION);
		String inputXml = xmlComposer.getComposedXML();
		System.out.println(inputXml);

		String outputXml = JRIRequestDispatcher.getOutputXmlString(inputXml);

		output.getXmlDocument(outputXml);
		//System.out.println("AnalysisOutput outputXml = " + outputXml);
		output.getRegressorNode("variable_name");//degreed_freedom_residual");
		// apply to member variable xmlDocumentTree. Now xmlDocumentTree has all the info about the output xml document.

		//System.out.println(fileName);
	}

	public AnalysisOutput() {
	}

	public void getXmlDocument(String inputString) {
		// inputString can be
		// A. xmlString; or B. file name (testing only)
		// if we want A then set useXmlString = true, otherwise false.
		// default should be A
		//System.out.println("fileName = " + fileName);
		//xmlDocumentTree = new XMLtree(fileName);
		xmlDocumentTree = new XMLtree(fileName);//inputString);//, useXmlString);
		System.out.println("getXmlDocument xmlDocumentTree = " + xmlDocumentTree.getXMLroot()); // get the root document of this xml from the String.

		texture = new HashMap<String,Object>();

		//texture.put();
		String content = getEstimates();
		//System.out.println("AnalysisOutput getValueByName = " + getEstimates());

	}
	public void addToVarList(String varName) {
		varList.add(varName);
	}
	public String getEstimates(/*String varName*/) {
		// get whatever from the Document and return the result as a String/int/double/whatever type.
		// need to get the node where the variable_name = varName
		System.out.println("AnalysisOutput getEstimates xmlDocumentTree = " + xmlDocumentTree);
		String valueByName = xmlDocumentTree.getValueByName("intercept");
		System.out.println("AnalysisOutput getEstimates valueByName = " + valueByName);

		return valueByName;
	}

	public String getRegressorNode(String varName) {

		NodeList currentNodeList = xmlDocumentTree.getElementsByName(varName);
		System.out.println("\ncurrentNodeList = " + currentNodeList + " length = " + currentNodeList.getLength());
		Node currentNode = null, child = null, sibling = null, parent = null;
		NodeList childList = null;

		if (currentNodeList != null) { // change to try
			currentNode = currentNodeList.item(0);
			System.out.println("AnalysisOutput getRegressorNode node name = " + currentNode.getNodeName());

			sibling = currentNode.getPreviousSibling();
			System.out.println("AnalysisOutput getRegressorNode sibling name = " + sibling.getNodeName());

			parent = currentNode.getParentNode();
			System.out.println("AnalysisOutput getRegressorNode parent name = " + parent.getNodeName());

			//System.out.println("getValue = " + xmlDocumentTree.getValueByName(currentNode));
		}
		else {
			System.out.println("AnalysisOutput getRegressorNode node is null");
		}


		return xmlDocumentTree.getValueByName("estimate");
	}

	private static String fileName = "C:\\stat\\SOCR\\test\\xml\\LinearModelOutput.xml"; 
	// this is the input XML.
}
