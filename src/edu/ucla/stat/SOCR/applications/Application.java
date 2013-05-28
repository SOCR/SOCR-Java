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
package edu.ucla.stat.SOCR.applications;

import java.awt.Container;
import java.awt.datatransfer.Clipboard;
import java.net.URL;
import java.util.Observer;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JTable;

import edu.ucla.stat.SOCR.chart.Chart;
import edu.ucla.stat.SOCR.core.IExperiment;
import edu.ucla.stat.SOCR.core.MultiplePartsPanel;
import edu.ucla.stat.SOCR.core.SOCRApplet2;

public class Application extends MultiplePartsPanel implements Observer, IExperiment{
	 public Clipboard clipboard;
	 protected int numStocks = 2;
	 
	 public static Application getInstance(String classname) throws Exception {

	        return (Application) Class.forName(classname).newInstance();
	    }

	 public void init(){
		
	}
	 public void pasteData(Clipboard c){
	    	
	    }
	 
	 public void setNumberStocks(String number){
		numStocks= Integer.parseInt(number);
		init();
	 }
	public void addTool2(JComponent c) {
	        toolbar2.add(c);
	    }
	 public void emptyTool() {
	    	toolbars.remove(toolbar);
	    
	    }
	 
	 public void emptyTool2() {
	    	toolbars.remove(toolbar2);
	    }
	 public void updateGraph(JComponent c) {
		// System.out.println("Application.updateGraph get called");
	        graphs.removeAll();
	        graphs.add(c);
	        graphs.validate();
	    }
	 
	 public void setApplet(JApplet a) {
	        applet = (SOCRApplet2) a;
		//  System.err.println("Set the MultiplePartsPanel.applet="+applet);
	    }
	 
	 public String getLocalAbout() {
		 
		   String helpName = "demo"+System.getProperty("file.separator")+fName+".html";
		   URL helpUrl = Chart.class.getResource(helpName);
		   StringBuffer fBuf =  new StringBuffer();
		   fBuf.append("General Application Control:\n");
		   return fBuf.toString();
	 }
	 
	 public void setTangent(boolean t){
		
		}
	 
	 public String getLocalHelp() {
		 return"";
	 }
	 
	 public String getWikiAbout(){
		   return new String("http://wiki.stat.ucla.edu/socr/index.php/About_pages_for_SOCR_Applications");
	   }

	   public String getWikiHelp(){
		   return new String("http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_Applications");
	   }

	public Container getDisplayPane() {
		/* JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
	                getMainPanel(), getTextPanel() );
		
		
	        return container;*/
		return getMainPanel();
	}

	public void doExperiment() {
		// TODO Auto-generated method stub
		
	}

	public String getAppletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOnlineDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public JTable getResultTable() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStopFreq() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void graphUpdate() {
		// TODO Auto-generated method stub
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void setShowModelDistribution(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public void setStopFreq(int i) {
		// TODO Auto-generated method stub
		
	}

	public void setStopNow(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void step() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}
public void loadSlider(double[] r1, double[] c1, double[] m1 ){
		
	}


}
