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
import java.awt.GradientPaint;
import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A bar chart with only two bars - here the 'maxBarWidth' attribute in the 
 * renderer prevents the bars from getting too wide.
 */
public class BarChartDemo4 extends SuperCategoryChart_Bar implements PropertyChangeListener {

	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("Bar", chartTitle, "Category", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo) {
        // row keys...
        String series1 = "First";
        String series2 = "Second";

        // column keys...
        String category1 = "Category 1";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, series1, category1);
        dataset.addValue(5.0, series2, category1);
        
        return dataset;}
		else return super.createDataset(false);
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle,         // chart title
            domainLabel,               // domain axis label
            rangeLabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(new Color(0xBBBBDD));

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        
        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.10);
        
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        
        // OPTIONAL CUSTOMISATION COMPLETED.
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
		setCategorySummary(dataset);
        return chart;
        
    }

    protected JFreeChart createLegend(CategoryDataset dataset) {
        
		//  JFreeChart chart = ChartFactory.createAreaChart(
	        JFreeChart chart = ChartFactory.createBarChart(
	            chartTitle,             // chart title
	            domainLabel,               // domain axis label
	            rangeLabel,                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        CategoryPlot plot = chart.getCategoryPlot();

	        BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        GradientPaint gp0 = new GradientPaint(
	                0.0f, 0.0f, Color.blue, 
	                0.0f, 0.0f, Color.lightGray
	            );
	            GradientPaint gp1 = new GradientPaint(
	                0.0f, 0.0f, Color.green, 
	                0.0f, 0.0f, Color.lightGray
	            );
	            
	            renderer.setSeriesPaint(0, gp0);
	            renderer.setSeriesPaint(1, gp1);
	       // renderer.setDrawOutlines(true);
	       // renderer.setUseFillPaint(true);
	       // renderer.setFillPaint(Color.white);
	        renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
	        return chart;
	        
	    }
    

}
