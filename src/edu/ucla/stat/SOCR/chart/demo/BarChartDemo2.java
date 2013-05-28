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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a horizontal bar 
 * chart.
 */
public class BarChartDemo2 extends SuperCategoryChart_Bar implements PropertyChangeListener {

	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog( chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("Bar", chartTitle, "Category", "Score (%)", dataTable, no_category, pairs, "horizontal");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample dataset.
     * @param isDemo true use the demo data, false use data from the JTable
     * @return A dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) { 
		if (isDemo){
			//indepLabel = "Category";
			//depLabel = "Score (%)";
        double[][] data = new double[][] {
            {1.0, 43.0, 35.0, 58.0, 54.0, 77.0, 71.0, 89.0},
            {54.0, 75.0, 63.0, 83.0, 43.0, 46.0, 27.0, 13.0},
            {41.0, 33.0, 22.0, 34.0, 62.0, 32.0, 42.0, 34.0}
        };
        return DatasetUtilities.createCategoryDataset("Series ", "Factor ", data);
		}
		else return super.createDataset(false);
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    protected  JFreeChart createChart(CategoryDataset dataset) {
    
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle,         // chart title
            domainLabel,                 // domain axis label
            rangeLabel,                // range axis label
            dataset,                    // data
            PlotOrientation.HORIZONTAL, // orientation
            !legendPanelOn,                       // include legend
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));
        // OPTIONAL CUSTOMISATION COMPLETED.
        
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

		setCategorySummary(dataset);
        return chart;
        
    }
    
   
}
