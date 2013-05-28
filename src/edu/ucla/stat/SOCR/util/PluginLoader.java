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
package edu.ucla.stat.SOCR.util;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Vector;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class PluginLoader implements FilenameFilter {
    File file;
    String Module;
    //String[] fileList;
    Vector<String> fileList = new Vector<String>();
    ClassLoader Loader;
    Vector<String> jarFiles = new Vector<String>();
    Vector<String> classFiles = new Vector<String>();
    Vector<String> classFileLocations = new Vector<String>();
    //HttpURLConnection urlCon = new HttpURLConnection(new URL("http://www.loni.ucla.edu/~dkrishna/Prototype1.1/plugin/modeler"));
    URL url; 
    //FilenameFilter filter = new FilenameFilter
    //JarResources jarresources;
    protected SimpleClassLoader classLoader; 
    public PluginLoader(URL pluginLocation, ClassLoader loader) {
    Loader = loader;
    //Module = module;
    //file = new File
    //filePath.
    url = pluginLocation;
    try{
    
    	if(url.toString().substring(0,3).compareTo("http")==0) {
    	
    
  
    	
    	System.out.println("url location is ="+url.toString());
    	InputStream ips = url.openStream();
	  Reader r = new InputStreamReader(ips);
	  ParserDelegator parser = new ParserDelegator();
	  //HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback();
	  HTMLEditorKit.ParserCallback callback = 
	    new HTMLEditorKit.ParserCallback () {
	      public void handleText(char[] dat, int pos) {
	        String data = new String(dat);
	        
	       
	      	if(accept(new File("."), data)) 
	      	 fileList.addElement(data);
	      	//System.out.println("\nHere is the data at position " +pos+"  " + data.toString());
	      	 System.out.println("\ngot a file in list"+data);
	      }
	     
	  };
	  parser.parse(r, callback, false);
    	}else {
    	file = new File(url.getPath());
    	System.out.println("File location is ="+file.getPath());
    	//fileList = file.list(this);
       String[] tempList = file.list(this);
    	for(int i=0;i<tempList.length;i++)
    		fileList.add(tempList[i]);
    	}
    	
    	
 
       } catch(Exception e) {e.printStackTrace(); }
    //System.out.println("\n filepath and module" +filePath+module);
    
    
    classLoader = new SimpleClassLoader(url,Loader);
    
    
    System.out.println(file.getAbsolutePath());
    fillVectors();
  
    }
    
    public final boolean accept(File dir, String name) {
    //System.out.println(name+"\n");
    //name.s
    if(name.length()>4 && name.substring(name.length()-4).compareTo(".jar")==0)
        return true;
    else {
    
    if(name.length()>6 &&  name.substring(name.length()-6).compareTo(".class")==0)
        return true;
    else
        return false;
    }
    }    

    public final int getClassCount() {
        if(fileList !=null)
            return fileList.size();
        else
            return 0;
    
    }
    
    public final String getClassString(int i) {
    if(classFiles.size() >0 && i< classFiles.size()) {
        String tempClassName = (String)classFiles.get(i);
        //(String)classFiles.get(i)
        return tempClassName.substring(0,tempClassName.length() -6);
    }
        else
            return "";
    }
    
    public final Class loadClass(int i) {
    try{
        // change to the vector
        String tempClassName = (String)classFiles.get(i);
        return classLoader.loadClass(tempClassName.substring(0,tempClassName.length() -6));
        
        }catch(Exception e) {
        e.printStackTrace();
        
        return null;
        }
    
    }
    
    public final void fillVectors() {
        for(int i=0;i<fileList.size();i++) {
            if(((String)fileList.get(i)).substring(((String)fileList.get(i)).length()-4).compareTo(".jar")!=0) {
                //its a .class file. so just add it to the class list
                if(testClass(((String)fileList.get(i)).substring(0,((String)fileList.get(i)).length()-6),"null")) {
                classFiles.addElement(((String)fileList.get(i)));
                classFileLocations.addElement("null");
                }
          //  }else {
                //its a jar file.... more processing required.
                
         //   }
            
        }
        
    }
    }

    public boolean testClass(String className, String classLocation) {
    
    return false;
    
    }
}