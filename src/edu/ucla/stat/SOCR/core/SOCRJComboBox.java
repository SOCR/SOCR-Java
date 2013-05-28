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
package edu.ucla.stat.SOCR.core;

import java.io.*;

import javax.swing.*;
import java.util.*;

/**
 * helper class of SOCR project, it is a JComboBox intitiated 
 * items from a inputstream, and return a selected Item as
 * the name of a classname.
 * 
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class SOCRJComboBox extends JComboBox {
    public SOCRJComboBox(InputStream input) {
        String line = null;
        Vector<ComboBoxItem> vector = new Vector<ComboBoxItem>(10);
        try {
            BufferedReader rder = new BufferedReader( new InputStreamReader(input));

            while ( (line = rder.readLine()) != null) {
                line.trim();
                if (line.startsWith("#") || line.equals("")) continue;
                vector.add(new ComboBoxItem(line));
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
        setModel(new DefaultComboBoxModel(vector));
    }
    /**
     * use this class to get Selected Implement Class
     */
    public String getSelectedClassName() {
        return ((ComboBoxItem)getSelectedItem()).classname;
    }
    
    public String getItemName(int idx) {
    	return ((ComboBoxItem)getItemAt(idx)).name;
    }
    
    public String getItemClassName(int idx) {
    	return ((ComboBoxItem)getItemAt(idx)).classname;
    }
    private static class ComboBoxItem {
        public ComboBoxItem(String line) {
            StringTokenizer tks = new StringTokenizer(line, ",");
            for ( int i = 0; tks.hasMoreTokens(); i++) {
                if (i == 0) name = tks.nextToken().trim();
                if (i == 1) classname = tks.nextToken().trim();
            }
        }
        
        public String toString() { return name;}
        private String name;
        private String classname;
    }
}
