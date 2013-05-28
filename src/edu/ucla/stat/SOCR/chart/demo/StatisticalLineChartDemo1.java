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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Stat;

/**
 * A simple demonstration application showing how to create a "statistical" 
 * line chart using data from a {@link CategoryDataset}.
 */
public class StatisticalLineChartDemo1 extends SuperCategoryChart_Stat implements PropertyChangeListener {

    /**
     * Creates a sample dataset.
     * 
     * @return The dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
        if (isDemo){
        DefaultStatisticalCategoryDataset dataset 
            = new DefaultStatisticalCategoryDataset();
        dataset.add(10.0, 2.4, "Row 1", "C1(Mean, stdDev)");
        dataset.add(15.0, 4.4, "Row 1", "C2(Mean, stdDev)");
        dataset.add(13.0, 2.1, "Row 1", "C3(Mean, stdDev)");
        dataset.add(7.0, 1.3, "Row 1", "C4(Mean, stdDev)");
        dataset.add(22.0, 2.4, "Row 2", "C1(Mean, stdDev)");
        dataset.add(18.0, 4.4, "Row 2", "C2(Mean, stdDev)");
        dataset.add(28.0, 2.1, "Row 2", "C3(Mean, stdDev)");
        dataset.add(17.0, 1.3, "Row 2", "C4(Mean, stdDev)");
        return dataset;}
		else return super.createDataset(false);
                
    }
    public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("StatisticalLineChartDemo1 doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("LineStat", "LineChart", "Type", "value", dataTable, no_category, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createLineChart(
            chartTitle,       // chart title
            domainLabel,                    // domain axis label
            rangeLabel,                   // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,  // orientation
            !legendPanelOn,                      // include legend
            true,                      // tooltips
            false                      // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setUpperMargin(0.0);
        domainAxis.setLowerMargin(0.0);
        
        // customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        // customise the renderer...
        StatisticalLineAndShapeRenderer renderer 
            = new StatisticalLineAndShapeRenderer(true, false);
        plot.setRenderer(renderer);

        // OPTIONAL CUSTOMISATION COMPLETED.
       return chart;
    }

    protected JFreeChart createLegend(CategoryDataset dataset) {
        
		//  JFreeChart chart = ChartFactory.createAreaChart(
	        JFreeChart chart = ChartFactory.createLineChart(
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

	        StatisticalLineAndShapeRenderer renderer 
            = new StatisticalLineAndShapeRenderer(true, false);
        plot.setRenderer(renderer);

	
	        return chart;
	        
    }
}
