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

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a normal distribution chart.
 */
public class SuperNormalDistributionChart extends SuperXYChart implements PropertyChangeListener {

	public void init(){

		indLabel = new JLabel("Mean");
		depLabel = new JLabel("StdDev");

		super.init();
		depMax = 50; // max number of dependent var
		indMax = 50; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
	}

	/**
	 *  sample code for generating chart using ChartGenerator_JTable 
	 */
	 public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperNormalDistributionChart doTest get called!");
		
		 int[][] pairs = new int[1][2];
		 pairs[0][0] = 0;   //mean 
		 pairs[0][1] = 1;   // stdDev
		 chart = chartMaker.getXYChart("ND","Normaldistribution Chart", "X", "Y", dataTable, 1, pairs, "noshape nofill ");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
         // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,      // chart title
            domainLabel,                      // x axis label
            rangeLabel,                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
  
	}	
    
    public void setDataTable (String input){
		  hasExample= true;
		  StringTokenizer lnTkns = new StringTokenizer(input,";#");
		  resetTableColumns(2);		
		  resetTableRows(1);		
		 
		  dataTable.setValueAt(lnTkns.nextToken(), 0, 0);
		  dataTable.setValueAt(lnTkns.nextToken(), 0, 1);
	    

	     
	     // this will update the mapping panel     
	     resetTableColumns(dataTable.getColumnCount());
	 }
    
	  public void setXLabel(String xLabel){
			
		  	domainLabel = xLabel;

		  	TableColumnModel columnModel = dataTable.getColumnModel();  	
		  	columnModel.getColumn(0).setHeaderValue("Mean");
		  	dataTable.setTableHeader(new EditableHeader(columnModel));
		  }
		  
		  public void setYLabel(String yLabel){
		  	rangeLabel = yLabel;
		  	TableColumnModel columnModel = dataTable.getColumnModel();		  
		  	columnModel.getColumn(1).setHeaderValue("StdDev");
		  	dataTable.setTableHeader(new EditableHeader(columnModel));
		  }
		  
		  // this will update the mapping panel    
		  public void setMapping(){
			
			  addButtonIndependent();
			  addButtonDependent();
				
		  }
}
