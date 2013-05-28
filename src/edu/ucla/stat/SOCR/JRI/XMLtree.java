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

package edu.ucla.stat.SOCR.JRI;

import java.lang.reflect.Array;

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import java.io.StringWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;


 public class XMLtree{
	static final boolean DEBUG = false;

    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.DOMParser";

	DOMParser parser;
	//DOMParser parser;

    Document rootDoc;

	public XMLtree(){
        parser = new DOMParser();
		try {
			parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace",true);}
		catch(org.xml.sax.SAXException e){
			System.out.println("SAXException");
				}
   }

    public XMLtree(String fName){
		parser = new DOMParser();
		try {
			parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace",true);}
		catch(org.xml.sax.SAXException e){
			System.out.println("SAXException");
				}
        flush(fName);
	}

    public XMLtree(String fName, boolean useFile){
		if(useFile) {
			;
		}
		else {
			;
		}
	}

	 public XMLtree(Document input_root){
		 parser = new DOMParser();
		try {
			parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace",true);}
		catch(org.xml.sax.SAXException e){
			System.out.println("SAXException");
				}
		rootDoc = input_root;
	 }

    public void flush(String fName){
	    rootDoc = getRoot(fName);
	}

	 public void flush(Document root){
	    rootDoc = root;
	}

	 public Document  getXMLroot(){
		 return rootDoc;
	 }

	protected  Document getRoot(String fileName){
		if (DEBUG) System.out.println("START getRoot");

		if (fileName ==null || fileName.equals(""))
			return null;

		try{
			parser.parse(fileName);
			Document document = parser.getDocument();
			return document;
		}catch (Exception e){
			System.err.println("Error: invalid XML document,"+fileName+": can't get root");
			System.exit(1);
		}

		if (DEBUG) System.out.println("END getRoot");
		return null;

	}//getRoot

   public String getValue(Node el){

		if (DEBUG) System.out.println("starting getValue: Node");

		StringBuffer str = new StringBuffer();

		int type = el.getNodeType();
		String nName = el.getNodeName();
		String 	nValue = el.getNodeValue();

		switch(type){
			case Node.ELEMENT_NODE:{
				if (DEBUG) System.out.println(" type = ele:"+type+":" +nName+":"+nValue);
				str.append(getValue(el.getChildNodes()));
				break;
			}
			case Node.TEXT_NODE:{
				if (DEBUG) System.out.println(" type = TEXT:"+type+":"+nName+":"+nValue );
				//leaf
				if (nValue!=null)
					str.append(nValue);
				break;
			}
			default:	{
				if (DEBUG) System.out.println(" type ="+type+":"+nName+":"+nValue );
				break;
			}
		}
		if (DEBUG) System.out.println("getValue Node: returning: "+str.toString());

		return str.toString();
		}//getValue();


		//if multiple elements are found, append them together use ","
	 public String getValue(NodeList elements){
			if (DEBUG) System.out.println("starting getValue: Nodes");

			StringBuffer str = new StringBuffer();

			int elementCount = elements.getLength();
			if (DEBUG) System.out.println("NodeCount ="+elementCount);


			if	(elementCount == 1){
				str.append(getValue(elements.item(0)));
			}
			else {
				for (int i = 0; i < elementCount; i++) {
					Node el = elements.item(i);
					if (i>0) str.append(", "+getValue(el));
					else  str.append(getValue(el));
				}
			}
			if (DEBUG) System.out.println("getValue Nodes: returning :"+str.toString());

			return str.toString();
		}//getValue(NodeList)

	 public NodeList getElementsByName(String elName){
		 NodeList elements = rootDoc.getElementsByTagName(elName);
			return elements;
	 }

	//** get the specified element value,
		 public String getValueByName(String elName){

			if (DEBUG) System.out.println("getValueByName :elName =" +elName);
			StringBuffer str = new StringBuffer();;
			NodeList elements = rootDoc.getElementsByTagName(elName);

			if( elements==null ||elements.getLength()==0)
				return 	null;

			if (DEBUG) System.out.println("NodeCount ="+elements.getLength());
            return getValue(elements);

    } // getValueByName(Document,String)

	 //** get the specified element value,
	  public String getValueByName(Element rootEl, String elName){

			if (DEBUG) System.out.println("getValueByName :elName =" +elName);
			StringBuffer str = new StringBuffer();;
			NodeList elements = rootEl.getElementsByTagName(elName);

			if( elements==null ||elements.getLength()==0)
				return 	null;

			if (DEBUG) System.out.println("NodeCount ="+elements.getLength());
            return getValue(elements);

    } // getValueByName(Element,String)

	 public void insert(String elname, String value){
		 // el.setNodeValue(value);
		 if (DEBUG) System.out.println("-------inserting:elname="+elname+" value="+value);
		 NodeList els = getElementsByName(elname);
		 if (els.getLength() ==1){
			 if (DEBUG) System.out.println("found one :"+elname);
			 Node el = els.item(0);
			 el.appendChild(rootDoc.createTextNode(value));
			 // print(els);
		 }
		 else  System.out.println("found " +els.getLength()+":"+elname);

	 }

	 public void insertChildren(String elname, String value){
		 // el.setNodeValue(value);
		 String[] st = value.split(" +");

		 // System.out.println("-------inserting Child:elname="+elname+" value="+value);
		 NodeList els = getElementsByName(elname);

		 if (els.getLength() ==1){
			 if (DEBUG) System.out.println("found one :" + elname);

			 NodeList cs = (els.item(0)).getChildNodes();
			 // System.out.println("<-----");
			 // print(cs);
			 // System.out.println("----->");

			 int i=0;
			 for (int j =0; j< cs.getLength();) {

				 // System.out.println("i="+i+"j="+j);
				 if ((cs.item(j)).getNodeType()==Node.TEXT_NODE)
					 j++;
				 else {
					 // System.out.println("inserting:--"+ st[i]);
					 (cs.item(j)).appendChild(rootDoc.createTextNode(st[i]));
					 i++;	j++;
				 }

			 }

		 }
		 else System.out.println("found " +els.getLength()+":"+elname);

	 }
	 public void insertSiblingWithChildren(String elname, String value){
		 // el.setNodeValue(value);
		 String[] st = value.split(" +");

		 // System.out.println("-------inserting Sibling :elname="+elname+" value="+value);
		 NodeList els = getElementsByName(elname);

		 if (els.getLength() ==1){
			 if (DEBUG) System.out.println("found one :" + elname);

			 Node sibling = els.item(0).cloneNode(true);
			 // ((els.item(0)).getParentNode()).appendChild(sibling);

			 ((els.item(0)).getParentNode()).insertBefore(sibling, (els.item(0)).getNextSibling());
			 NodeList cs = sibling.getChildNodes();

			 int i=0;
			 for (int j =0; j< cs.getLength();) {

				 // System.out.println("i="+i+"j="+j);
				 if ((cs.item(j)).getNodeType()==Node.TEXT_NODE)
					 j++;
				 else {
					 // System.out.println("inserting:--"+ st[i]);
					 Node child= cs.item(j).getFirstChild();
					 (cs.item(j)).replaceChild(rootDoc.createTextNode(st[i]),child);
					 i++;	j++;
				 }

			 }

		 }
		 else System.out.println("found " +els.getLength()+":"+elname);

	 }

	 public void print(){
		// print the whole xml doc
		 System.out.println("********************");
		 NodeList elements = rootDoc.getChildNodes();
		 print(elements);
		 System.out.println("********************");
	 }

	 protected void print(NodeList elements){

        // is there anything to do?
        if (elements == null) {
            return;
        }

        // print all elements
		int elementCount = elements.getLength();
		if 	(elementCount == 0)
			return;

		System.out.println("node count:"+elementCount);

		for (int i = 0; i < elementCount; i++) {
 			Node el = elements.item(i);

			int type = el.getNodeType();
			String nName = el.getNodeName();
			String 	nValue = el.getNodeValue();

			switch(type){
			case Node.ELEMENT_NODE:{
				System.out.println(i+" type = ele:"+type+":" +nName+":"+nValue);
				printEl((Element)el);
				break;
			}
			case Node.TEXT_NODE:{
				System.out.println(i+" type = TEXT:"+type+":"+nName+":"+nValue );
				/*leaf
				if (elementCount == 1)
				System.out.println(nValue);	*/
				break;
			}
			case	Node.DOCUMENT_TYPE_NODE	: {
				System.out.println(i+" type = docType:"+type+":"+nName+":"+nValue );
				break;
			}
			default:	{
				System.out.println(i+" type ="+type+":"+nName+":"+nValue );
				break;
			}
			}
		}

    } // print(NodeList)

    //
    // Protected static methods
    //
    //** Prints the specified element.
    protected  void printEl(Element element) {

        System.out.print("<");
        System.out.print(element.getNodeName());
        System.out.print(">");

		System.out.print("\n");
		print(element.getChildNodes());
		return;

    } // print(Rengine,Element,NamedNodeMap)

	 public void writeXMLFile(String out_fName){

		try{
			BufferedWriter out=  new BufferedWriter(new FileWriter(out_fName));
			StringWriter  stringOut = new StringWriter();        //Writer will be a String

			OutputFormat    format  = new OutputFormat(rootDoc);   //Serialize DOM
            XMLSerializer    serial = new XMLSerializer(stringOut, format );
            serial.asDOMSerializer();                            // As a DOM Serializer

            serial.serialize( rootDoc.getDocumentElement() );

            out.write(stringOut.toString() ); //Spit out DOM as a String
			out.close();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
	}


}

