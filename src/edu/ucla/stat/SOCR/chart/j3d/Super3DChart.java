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
package edu.ucla.stat.SOCR.chart.j3d;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.j3d.gui.ChartPanel3D;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinned2DData;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;

/**
  * A simple demonstration application showing how to create a bar chart.
 */
public class Super3DChart extends Chart3D implements PropertyChangeListener {

    protected ChartPanel3D chart3DPanel;
    protected static final int CHART_SIZE_Y = 600;
    protected SOCRBinned2DData data;
    protected URL codeBase;
	
	public void init(){
			
		super.init();
	
	//	depMax = 10; // max number of dependent var
		indMax = 100; // max number of independent var

		updateStatus(url);	
	  try{
		  Runtime r = Runtime.getRuntime();
		  r.gc();
		  System.gc();
		  resetExample();
	  }catch(OutOfMemoryError e){
		  SOCROptionPane.showMessageDialog(this, "out of memory error");
			e.printStackTrace();
	  }
		validate();		
	}
	
	protected void initGraphPanel(){
		//System.out.println("initGraphPanel called");
		setCodeBase();
		graphPanel = new JPanel();	
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
		try {
			chart3DPanel = new ChartPanel3D(codeBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		chart3DPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));	
		graphPanel.add(new JScrollPane(chart3DPanel));
		graphPanel.validate();
	}
	
	protected void resetChart(){
		try {
			chart3DPanel = new ChartPanel3D(codeBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		chart3DPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
	
		graphPanel.removeAll();
		graphPanel.add(new JScrollPane(chart3DPanel));
		graphPanel.validate();
		// get the GRAPH panel to the front
		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));

	}

	public JPanel getChart3DPanel(){
		return chart3DPanel;
	}

	
	protected void setChart(){
			// update graph
			//System.out.println("setChart called");

			graphPanel.removeAll();
			chart3DPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

			graphPanel.add(new JScrollPane(chart3DPanel));
			graphPanel.validate();

			// get the GRAPH panel to the front
			tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
		/*	if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) 
				tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			else {
				graphPanel2.removeAll();
				chart3DPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
				graphPanel2.add(new JScrollPane(chart3DPanel));
				graphPanel2.validate();	
				summaryPanel.validate();
			}*/
	}

	protected void setGraphPanel(){
		chart3DPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));
		graphPanel.add(chart3DPanel);
	}
	  
	 public void doChart(){

	    	//System.out.println("dochart get called");
	    	
			if(dataTable.isEditing())
				dataTable.getCellEditor().stopCellEditing();
			if (! hasExample ) {
				showMessageDialog(DATA_MISSING_MESSAGE);
				resetChart();
				return;
			}

			graphPanel.removeAll();
			graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

			SOCRBinned2DData data = createBinned2DData(false); // not demo
			chart3DPanel.setData(data);
			chart3DPanel.validate();
			
			graphPanel.add(new JScrollPane(chart3DPanel));
			graphPanel.validate();


			// get the GRAPH panel to the front
			tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
			graphPanel.removeAll();
			graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
				
			graphPanel.add(new JScrollPane(chart3DPanel));		
			graphPanel.validate();
			
	    }
	 
	
	public SOCRBinned2DData getData(){
		return data;
	}
	 /**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */   
    protected  SOCRBinned2DData createBinned2DData(boolean isDemo) {
    	
    	data= null;
    	
    	try {
			data= new SOCRBinned2DData(codeBase, "test.data");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        if (isDemo){   	
        	return data;     
        	}
		else {
			data.getDataFromJTable(dataTable);
			return data;   
		}
	} 
   
   public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
			dataTable.doLayout();

			System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
		}
	
	public Container getDisplayPane() {
		return this.getContentPane();
	}
   
	public void setCodeBase() {
    	codeBase = parentApplet.getCodeBase(); 
	}
	/*
	 * public String getLocalAbout() {
		String about ="\n \t A scatterplot or scatter graph is a graph used in statistics to visually display and\n compare two or more sets of related quantitative, or numerical, data by displaying only finitely\n many points, each having a coordinate on a horizontal and a vertical axis.\n";

		return super.getLocalAbout()+about;
		}
		*/
	  /**
     * reset dataTable to default (demo data), and refesh chart
     */
	public String getPathName(String fileName){
		
		if(codeBase==null)
			setCodeBase();
		
		System.out.println("codeBase="+codeBase);
		String pathName = codeBase+"3d_data"+System.getProperty("file.separator")+fileName;
		
		if(pathName.indexOf("file:")!=-1)
			pathName = pathName.substring(pathName.indexOf(':')+1);   	
		
		System.out.println("pathName="+pathName);
		return pathName;
	}

}
