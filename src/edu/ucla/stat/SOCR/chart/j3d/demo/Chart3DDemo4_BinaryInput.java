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
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JScrollPane;

import edu.ucla.stat.SOCR.chart.Chart;
import edu.ucla.stat.SOCR.chart.j3d.Super3DChart;
import edu.ucla.stat.SOCR.chart.j3d.gui.ChartPanel3D;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinned2DData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinnedBinary2DData;
import edu.ucla.stat.SOCR.chart.j3d.gui.SOCRBinnedTriplet2DData;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class  Chart3DDemo4_BinaryInput extends Super3DChart implements PropertyChangeListener {
	
	public void init(){
		inputFileType = 4;
		super.init();	
	}

     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected SOCRBinned2DData createBinned2DData(boolean isDemo) {
    	
    	SOCRBinnedBinary2DData data=null;
    	
    	  		
    		//String pathName = getPathName("2D_data.data");
    		//String pathName = "3d_data"+System.getProperty("file.separator")+"2D_data.data";
    		//	data = new SOCRBinnedBinary2DData(codeBase, pathName, 256, 256);  
    	
    	// this data file is packed in the jar file
    		String pathName = "binary16x16.data";
    		URL url = getClass().getResource(pathName);
    		try {
				data = new SOCRBinnedBinary2DData(url, 16, 16);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
    		//System.out.println("Chart3DDemo4_BinaryInput fileName="+pathName+ " codeBase="+codeBase.toString());
    		
    		//System.out.println("Chart3DDemo4_BinaryInput after");
    	
		
    	
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
 	 
        SOCRBinned2DData data = createBinned2DData(true);	
       
    	try {
  
    		chart3DPanel = new ChartPanel3D(codeBase);
    		System.gc();
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
