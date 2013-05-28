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
package edu.ucla.stat.SOCR.chart.j3d.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import org.jfree.data.xy.XYDataset;

import edu.ucla.stat.SOCR.chart.j3d.Super3DChart;
import edu.ucla.stat.SOCR.chart.j3d.gui.ChartPanel3D;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinned2DData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinnedMatrix2DData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinnedTriplet2DData;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class  Chart3DDemo3_MatrixInput extends Super3DChart implements PropertyChangeListener {
	
	public void init(){
		inputFileType = 3;
		super.init();	
	}

     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected SOCRBinned2DData createBinned2DData(boolean isDemo) {
    	
    	data= null;
    	
    	try {
    		//String pathName = getPathName("matrix10x5.data");
    		// if data is packed at jars/3d_data dir
    		//String pathName = "3d_data"+System.getProperty("file.separator")+"matrix10x5.data";
    		//data = new SOCRBinnedMatrix2DData(codeBase, pathName);   	

    		// this data file is packed in the jar file
    		String pathName = "matrix10x5.data";
    		URL url = getClass().getResource(pathName);
    		data = new SOCRBinnedMatrix2DData(url);
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
    
    
    public void resetExample() {
       // System.out.println("resetExample get called");
 	 
        SOCRBinned2DData data = this.createBinned2DData(true);	
		
    	try {
    		chart3DPanel = new ChartPanel3D(codeBase);
    		chart3DPanel.setData(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setChart();

		hasExample = true;

		resetTableRows(data.xBins());
		resetTableColumns(data.yBins());
	
		for(int i=0; i<data.xBins(); i++)
			columnModel.getColumn(i).setHeaderValue(String.valueOf(i+1));

		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));

		for(int i=0;i<data.xBins();i++)
         for(int j=0;j<data.yBins();j++) {
             dataTable.setValueAt(data.zAt(i, j), i, j);
           //  System.out.println("resetExample setDataTabel"+data.zAt(i, j));
		 }
   
         dataPanel.removeAll();
         dataPanel.add(new JScrollPane(dataTable));
         dataTable.setGridColor(Color.gray);
         dataTable.setShowGrid(true);
         dataTable.doLayout();
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try { 
			dataTable.setDragEnabled(true);  
		} catch (Exception e) {
		}

        dataPanel.validate();
        setValueField();
        
		// do the mapping
        if(SHOW_MAP_PANEL)
        	setMapping();
	//	updateStatus(url);
}
   
	
}
