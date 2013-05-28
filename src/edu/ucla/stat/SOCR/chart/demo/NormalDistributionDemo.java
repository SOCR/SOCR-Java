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
package edu.ucla.stat.SOCR.chart.demo;
import java.awt.Color;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperNormalDistributionChart;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * This demo shows a normal distribution graph.
 */
public class NormalDistributionDemo extends SuperNormalDistributionChart implements PropertyChangeListener {
/**
 * sample code showing how to use ChartGenerator_JTable to create chart
 */
	public void init(){
	LEGEND_SWITCH= false;
	super.init();
}
	 public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int[][] pairs = new int[1][2];
		 pairs[0][0] = 0;   //mean 
		 pairs[0][1] = 1;   // stdDev
		 chart = chartMaker.getXYChart("ND","Normaldistribution Chart", "X", "Y", dataTable, 1, pairs, "noshape nofill ");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
   /**
 * @uml.property  name="mean"
 */
private  double mean;
   /**
 * @uml.property  name="stdDev"
 */
private  double stdDev;

    /**
     * Creates a dataset with sample values from the normal distribution 
     * function.
     * @param isDemo true use the demo data, false use data from the JTable
     * @return A dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
		if (isDemo){
			mean =  0.0;
			stdDev = 1.0;
			Function2D normal = new NormalDistributionFunction2D(mean, stdDev);
			XYDataset dataset = DatasetUtilities.sampleFunction2D(normal, -10.0, 
                10.0, 100, "Normal");
        return dataset;}
		else {
			setArrayFromTable();
			String[][] x= new String[xyLength][independentVarLength];
			double[][] y= new double[xyLength][dependentVarLength];


			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					if (depValues[i][index]!=null)
						y[i][index] = Double.parseDouble(depValues[i][index]);
				}

			mean = Double.parseDouble(x[0][0]) ;
			stdDev = y[0][0];
			Function2D normal = new NormalDistributionFunction2D(mean, stdDev);
			XYDataset dataset = DatasetUtilities.sampleFunction2D(normal, -10.0, 
                10.0, 100, "Normal");

        return dataset;
		}
    }
    
    /**
     * Creates a line chart using the data from the supplied dataset.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,
            "X", 
            "Y", 
            dataset,
            PlotOrientation.VERTICAL,
            !legendPanelOn,
            true,
            false
        );
        return chart;
    }

 /**
  * use demo data to reset JTable and redraw the graph
  */
 public void resetExample() {

	    XYDataset dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
		convertor.normalDataset2Table(mean, stdDev);
		JTable tempDataTable = convertor.getTable();
		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(tempDataTable.getColumnCount()+2);
				

        for(int i=0;i<tempDataTable.getColumnCount();i++) {
            columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
            }

		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));

        for(int i=0;i<tempDataTable.getRowCount();i++)
            for(int j=0;j<tempDataTable.getColumnCount();j++) {
                dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
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

		// do the mapping
		
		int columnCount = dataTable.getColumnCount();
		for(int i=0; i<columnCount/2-1; i++){
			addButtonIndependent();
			addButtonDependent();
		}
		//updateStatus(url);
  }
     
    
}
