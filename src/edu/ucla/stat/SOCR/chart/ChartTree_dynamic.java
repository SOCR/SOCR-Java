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

package edu.ucla.stat.SOCR.chart;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import java.io.*;
import javax.swing.*;
import java.util.*;
import java.net.*;

import org.jfree.chart.plot.CategoryPlot;


/**
 * Create the JTree using the implementedCharts file.
 */
public class ChartTree_dynamic{
	private DefaultMutableTreeNode root;
	URL codeBase;
	String inputFile;
	String treeRootName;


	public JTree getTree(){
		JTree tree = new JTree(root);
        return tree;
    }

	public ChartTree_dynamic(String implementedFile, URL codeBase){
		this.codeBase = codeBase;
		inputFile = implementedFile;
		
		BufferedReader rder = startReaderBuffer();
		String line = readLine(rder);
			if 	(line.equalsIgnoreCase("[root]"))
				createTreeModel(rder);
	}
	
	public ChartTree_dynamic(String implementedFile, URL codeBase, String rName){	
		treeRootName = rName;
		this.codeBase = codeBase;
		inputFile = implementedFile;
		
		BufferedReader rder = startReaderBuffer();
		String line = readLine(rder);
			if 	(line.equalsIgnoreCase("[root]"))
				createTreeModel(rder);
	}


	// start from the beginning again
	private BufferedReader startReaderBuffer(){
		
		try{
			InputStream in = (new URL(codeBase,inputFile).openStream());
			BufferedReader rder = new BufferedReader(new InputStreamReader(in));
			return rder;	
		}
		catch(IOException e) {
            e.printStackTrace();
			return null;
			}
	}

	private String readLine(BufferedReader rder){
		String line = null;
		try {
            while ( (line = rder.readLine()) != null) {
                line.trim();
                if (line.startsWith("#") || line.equals("")) continue;
				else {
					//System.out.println("reading:"+line);
					return line;}
			}
		} catch ( IOException e) { 
            e.printStackTrace();
			return null;
        }
		return line;
	}
		
// create the top level nodes 
    private void createTreeModel(BufferedReader rder) {
		//System.out.println("CreateTreeModel:start");		

		String[] nodeList;
		String line;	
		if (treeRootName==null)
			root = new DefaultMutableTreeNode("SOCRCharts");
		else root = new DefaultMutableTreeNode(treeRootName);

		StringBuffer  sb = new StringBuffer();

		while ( (line = readLine(rder)) != null && !(line.toLowerCase().startsWith("["))) {
			if (line.toLowerCase().startsWith("subcategory")){
				line = line.substring(line.indexOf("=")+1);
				sb.append(line.trim());
				sb.append(",");
			}  //ignore other cases 
		}
					
		StringTokenizer tk = new StringTokenizer(new String(sb),",");
		nodeList  = new String[tk.countTokens()];
		int i = 0;
		while (tk.hasMoreTokens()){
			nodeList[i] = tk.nextToken(); 
			i++;
		}

		for (i=0; i<nodeList.length; i++){
			//System.out.println(nodeList[i]);
			root.add(createChartsNode(nodeList[i]));
		}
    }
    
    /**
     * Creates the tree node and its subnods
     * 
     * @return A populated tree node.
     */
    private MutableTreeNode createChartsNode(String name) {
		//System.out.println("adding-"+name+"-");
       
		BufferedReader rder = startReaderBuffer();

		DefaultMutableTreeNode node_root = new DefaultMutableTreeNode(name);
		String line, className, chartName;
		StringBuffer  sb = new StringBuffer();
	
		while ( (line = readLine(rder)) != null) {
			if (line.toLowerCase(). equalsIgnoreCase("["+name+"]")){
				while ( (line = readLine(rder)) != null) {
					
					if (line.toLowerCase().startsWith("item")){
						line = line.substring(line.indexOf("=")+1);
						StringTokenizer tk = new StringTokenizer(line,"=,; ");


						chartName = tk.nextToken().trim();
						className = tk.nextToken().trim();
						//	System.out.println("className =["+className+"]");
						DefaultMutableTreeNode n = new DefaultMutableTreeNode(  new DemoDescription(className, chartName)); 
						node_root.add(n);
					}//item
					
					else if	 (line.toLowerCase().startsWith("subcategory")){
						line = line.substring(line.indexOf("=")+1);
						try{
							rder.mark(100);
							node_root.add(createChartsNode(line.trim()));
							rder.reset();}
						catch(IOException e)
							{e.printStackTrace();}
					}//subCategory

					else if (line.toLowerCase().startsWith("[")){
						//System.out.println("end of "+name);
						return node_root;}
						
				}
			}
			
		}
		return node_root;
	}

}
    

