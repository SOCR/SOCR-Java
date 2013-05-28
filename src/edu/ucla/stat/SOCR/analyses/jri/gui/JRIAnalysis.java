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

*/


package edu.ucla.stat.SOCR.analyses.jri.gui;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import org.w3c.dom.Document;


public abstract class JRIAnalysis {
	Data data = null;
	short analysisType = -1;
	boolean useJRI = false;
	Result result = null;
	String inputXML = null;
	String outputXML = null;


	/*
		1. convert data to XML input string
		2. send XML String to the server. (this class doesn't need to know about Docuement.)
		3. the server will return an output XML.
		4. convert this output XML to result.
	*/
	public abstract void analyze(Data data, short analysisType) throws Exception;
	//protected abstract void dataToXMLString();
	//protected abstract void callServerForOutput();

	public abstract String getAnalysisType();
	public abstract String getAnalysisInputXMLString(Data data, short analysisType) throws Exception;
}
