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
import java.util.StringTokenizer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_StatA;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a "statistical" 
 * bar chart using data from a {@link CategoryDataset}.
 */
public class StatisticalBarChartDemo1a extends SuperCategoryChart_StatA implements PropertyChangeListener {

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
		 chart = chartMaker.getCategoryChart("BarStat", chartTitle, "Type", "value", dataTable, no_category, pairs, "");	
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
            chartTitle, // chart title
            domainLabel,                         // domain axis label
            rangeLabel,                        // range axis label
            dataset,                        // data
            PlotOrientation.VERTICAL,       // orientation
            !legendPanelOn,                           // include legend
            true,                           // tooltips
            false                           // urls
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        // customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        // customise the renderer...
        StatisticalBarRenderer renderer = new StatisticalBarRenderer();
        renderer.setErrorIndicatorPaint(Color.black);
		//	renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
        plot.setRenderer(renderer);

        // OPTIONAL CUSTOMISATION COMPLETED.
       return chart;
    }
    

	/**
	 * @param isdemo data come from demo(true) or dataTable(false)
	 */
    protected CategoryDataset createDataset(boolean isDemo) {
        if (isDemo){
        DefaultStatisticalCategoryDataset dataset 
            = new DefaultStatisticalCategoryDataset();
        dataset.add(10.0, 2.4, "Series 1", "C1");
        dataset.add(15.0, 4.4, "Series 1", "C2");
        dataset.add(13.0, 2.1, "Series 1", "C3");
        dataset.add(7.0, 1.3, "Series 1", "C4");
        dataset.add(22.0, 2.4, "Series 2", "C1");
        dataset.add(18.0, 4.4, "Series 2", "C2");
        dataset.add(28.0, 2.1, "Series 2", "C3");
        dataset.add(17.0, 1.3, "Series 2", "C4");
        return dataset;}
		else return super.createDataset(false);
                
    }
    
}
