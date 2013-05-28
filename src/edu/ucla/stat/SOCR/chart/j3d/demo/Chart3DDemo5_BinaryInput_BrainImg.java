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
public class  Chart3DDemo5_BinaryInput_BrainImg extends Chart3DDemo4_BinaryInput{

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
    		String pathName = "binary256x256.data";
    		URL url = getClass().getResource(pathName);
    		try {
				data = new SOCRBinnedBinary2DData(url, 256, 256);
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

   
   
	
}
