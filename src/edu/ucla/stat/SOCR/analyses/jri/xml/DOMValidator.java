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
// a test class found from the web.

/**
 * DOMValidator.java
 * Copyright (c) 2002 by Dr. Herong Yang
 */
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.helpers.DefaultHandler;
class DOMValidator {
   public static void main(String[] args) {
      try {
      	 File x = new File("invalid_dtd.xml");//args[0]);
         DocumentBuilderFactory f
            = DocumentBuilderFactory.newInstance();
         f.setValidating(true); // Default is false
         DocumentBuilder b = f.newDocumentBuilder();
         // ErrorHandler h = new DefaultHandler();
         ErrorHandler h = new MyErrorHandler();
         b.setErrorHandler(h);
         Document d = b.parse(x);
      } catch (ParserConfigurationException e) {
         System.out.println(e.toString());
      } catch (SAXException e) {
         System.out.println(e.toString());
      } catch (IOException e) {
         System.out.println(e.toString());
      }
   }
   private static class MyErrorHandler implements ErrorHandler {
      public void warning(SAXParseException e) throws SAXException {
         System.out.println("Warning: ");
         printInfo(e);
      }
      public void error(SAXParseException e) throws SAXException {
         System.out.println("Error: ");
         printInfo(e);
      }
      public void fatalError(SAXParseException e) throws SAXException {
         System.out.println("Fattal error: ");
         printInfo(e);
      }
      private void printInfo(SAXParseException e) {
         System.out.println("   Public ID: "+e.getPublicId());
         System.out.println("   System ID: "+e.getSystemId());
         System.out.println("   Line number: "+e.getLineNumber());
         System.out.println("   Column number: "+e.getColumnNumber());
         System.out.println("   Message: "+e.getMessage());
      }
   }
}
/*
To test DOMValidator, I created the following XML file with DTD statements, invalid_dtd.xml:

<?xml version="1.0"?>
<!-- dictionary_dtd.xml
     Copyright (c) 2002 by Dr. Herong Yang
-->
<!DOCTYPE dictionary [
 <!ELEMENT dictionary (note, word+)>
 <!ELEMENT note ANY>
 <!ELEMENT word (update?, name, definition+, usage*)>
 <!ELEMENT update EMPTY>
 <!ATTLIST update
  date CDATA #REQUIRED
  editor CDATA #IMPLIED
 >
 <!ELEMENT name (#PCDATA)>
 <!ATTLIST name is_acronym (true | false) "false">
 <!ELEMENT definition (#PCDATA)>
 <!ELEMENT usage (#PCDATA | i)*>
 <!ELEMENT i (#PCDATA)>
 <!ENTITY herong "Dr. Herong Yang">
]>
<dictionary>
 <note>Copyright (c) 2002 by &herong;</note>
 <word>
  <name is_acronym="true" language="EN">POP</name>
  <definition>Post Office Protocol</definition>
  <definition>Point Of Purchase</definition>
 </word>
 <word>
  <update date="2002-12-23"/>
  <name is_acronym="yes">XML</name>
  <definition>eXtensible Markup Language</definition>
  <note>XML comes from SGML</note>
 </word>
 <word>
  <update editor="Herong Yang"/>
  <name>markup</name>
  <definition>The amount added to the cost price to calculate
the selling price - <i>Webster</i></definition>
 </word>
</dictionary>
*/
