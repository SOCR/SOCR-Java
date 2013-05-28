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

import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.ExtendedStackedBarRenderer;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a stacked bar chart
 * using data from a {@link CategoryDataset}.
 */
public class StackedBarChartDemo3 extends SuperCategoryChart_Bar implements PropertyChangeListener {
    
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
		 chart = chartMaker.getCategoryChart("Bar", chartTitle, "Category", "value", dataTable, no_category, pairs, "stacked");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Series 1", "Jan");
        dataset.addValue(12.0, "Series 1", "Feb");
        dataset.addValue(13.0, "Series 1", "Mar");
        dataset.addValue(4.0, "Series 2", "Jan");
        dataset.addValue(3.0, "Series 2", "Feb");
        dataset.addValue(2.0, "Series 2", "Mar");
        dataset.addValue(2.0, "Series 3", "Jan");
        dataset.addValue(3.0, "Series 3", "Feb");
        dataset.addValue(2.0, "Series 3", "Mar");
        dataset.addValue(2.0, "Series 4", "Jan");
        dataset.addValue(3.0, "Series 4", "Feb");
        dataset.addValue(4.0, "Series 4", "Mar");
        return dataset;}
		else return super.createDataset(false);
    }
        
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset for the chart.
     * 
     * @return a sample chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createStackedBarChart(
            chartTitle,  // chart title
            domainLabel,                  // domain axis label
            rangeLabel,                     // range axis label
            dataset,                     // data
            PlotOrientation.VERTICAL,    // the plot orientation
            !legendPanelOn,                        // legend
            false,                       // tooltips
            false                        // urls
        );
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryItemRenderer renderer = new ExtendedStackedBarRenderer();
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(
                new StandardCategoryItemLabelGenerator());
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setRenderer(renderer);
        
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLowerMargin(0.15);
        rangeAxis.setUpperMargin(0.15);

		StackedBarRenderer renderer2 = (StackedBarRenderer) plot.getRenderer();
		renderer2.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

		setCategorySummary(dataset);
        return chart;
        
    }

}
