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
import java.awt.GridBagLayout;
import java.net.*;
import javax.swing.JPanel;



public class ModelerPluginLoader extends PluginLoader{

	public ModelerPluginLoader(URL filePath,ClassLoader loader) {
	super(filePath,loader);
	
	}
	
	
	 public boolean testClass(String className, String classLocation) {
	    try{
	    Class c = classLoader.loadClass(className);
	    Object[] obj = new Object[1];
	    obj[0] = new JPanel(new GridBagLayout());
	   // boolean v = c.getConstructor(new Class[] {JPanel.class}).newInstance(obj);
	    boolean v =  edu.ucla.stat.SOCR.modeler.Modeler.class.isInstance(c.getConstructor(new Class[] {JPanel.class}).newInstance(obj));
	    System.out.println(v);
	    return v;
	    }catch(Exception e) {
	        e.printStackTrace();
	    return false;
	    }
	    
	    }
	
	

}
