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
// annie che 20061203.

// this class is similar to mytest.java
// but the input and output are changed to pure Strings and
// the Stings are converted to and from XML Documents.

// this class is to be called by the AnalysisServlet class.
// the servlet handles the input and output to/from the applet
// but this class handles the R-interface part by calling Ranalysis class.


package edu.ucla.stat.SOCR.servlet.analyses;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import edu.ucla.stat.SOCR.JRI.Ranalysis;
import edu.ucla.stat.SOCR.JRI.XMLtree;

public class RInterface {

	public String getRAnalysisResult(PrintWriter pw, String inputXML) throws Exception {
		System.out.println("RInterface getRAnalysisResult start");

		String outputXML = null;
		//String errorMsg = "";
		//Ranalysis r = new Ranalysis(Rargs, in_fileName, t_fileName, out_fileName);

		// read data in from XML file
		//InputSource is = new InputSource(new StringReader(inputXML));
		// from http://java.sun.com/xml/tutorial_intro.html
		try {
			System.out.println("RInterface getRAnalysisResult try inputXML = " + inputXML + "; ");

			//Document in_xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(inputXML)));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);

			Document in_xml = dbf.newDocumentBuilder().parse(new InputSource(inputXML));
			System.out.println("RInterface getRAnalysisResult in_xml = " + in_xml + "; ");

			//Document in_xml = DocumentBuilderFactory.newDocumentBuilder().parse(is);
			Document template_xml, out_xml;

			XMLtree myXML = new XMLtree();
			System.out.println("RInterface getRAnalysisResult myXML = " + myXML + "; ");

			//myXML.flush(in_fileName);
			//in_xml = myXML.getXMLroot();

			// read XML template and get ready for the output XML
			//myXML.flush(t_fileName);
			template_xml = myXML.getXMLroot();
			System.out.println("RInterface getRAnalysisResult template_xml " + template_xml + "; ");

			// call R to do the analysis

			Ranalysis r = new Ranalysis(in_xml, template_xml);
			System.out.println("RInterface getRAnalysisResult r " + r);
			out_xml = r.getAnalysisResults();

			System.out.println("RInterface getRAnalysisResult out_xml " + out_xml + "; ");

			pw.print("RInterface out_xml = " + out_xml + "; ");
			outputXML  = getStringFromDocument(out_xml);

			//write output to file
			//myXML.flush(out_xml);
			//myXML.writeXMLFile(out_fileName);
			//System.out.println("mytest:done writeXMLfile");
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			System.out.println("RInterface getRAnalysisResult javax.xml.parsers.ParserConfigurationException e = " + e + "; ");
			throw new javax.xml.parsers.ParserConfigurationException("RInterface getRAnalysisResult javax.xml.parsers.ParserConfigurationException e = " + e.toString());

		} catch (org.xml.sax.SAXException e) {
			System.out.println("RInterface getRAnalysisResult org.xml.sax.SAXException e = " + e + "; ");
			throw new org.xml.sax.SAXException("RInterface getRAnalysisResult org.xml.sax.SAXException e = " + e.toString());

		} catch (java.io.IOException e) {
			System.out.println("RInterface getRAnalysisResult java.io.IOException e = " + e + "; ");
			throw new java.io.IOException("RInterface getRAnalysisResult java.io.IOException e = " + e.toString());

		}

		return outputXML;
	}

	// from http://www.theserverside.com/discussions/thread.tss?thread_id=26060

	//method to convert Document to String
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
		  DOMSource domSource = new DOMSource(doc);
		  StringWriter writer = new StringWriter();
		  StreamResult result = new StreamResult(writer);
		  TransformerFactory tf = TransformerFactory.newInstance();
		  Transformer transformer = tf.newTransformer();
		  transformer.transform(domSource, result);
		  return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
		  ex.printStackTrace();
		  return null;
	    }
	}
	private static String inputXmlString = "<analysis_input><analysis_model><analysis_name>LINEAR_MODEL</analysis_name></analysis_model><data><variable><variable_name>x</variable_name><variable_type>INDENPEDENT</variable_type><data_type>QUANTITATIVE</data_type><data_value>68.0, 49.0, 60.0, 68.0, 97.0, 82.0, 59.0, 50.0, 73.0, 39.0, 71.0, 95.0, 61.0, 72.0, 87.0, 40.0, 66.0, 58.0, 58.0 77.0</data_value></variable><variable><variable_name>t</variable_name><variable_type>DENPEDENT</variable_type><data_type>QUANTITATIVE</data_type><data_value>75.0, 63.0, 57.0, 88.0, 88.0, 79.0, 82.0, 73.0, 90.0, 62.0, 70.0,96.0, 76.0, 75.0, 85.0, 40.0, 74.0, 70.0, 75.0, 72.0</data_value></variable></data></analysis_input>";

	public static void main(String args[]) {
		edu.ucla.stat.SOCR.servlet.analyses.RInterface ri = new edu.ucla.stat.SOCR.servlet.analyses.RInterface();
		try {
			String outputXmlStringR = ri.getRAnalysisResult(null, inputXmlString);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}//end of  class
