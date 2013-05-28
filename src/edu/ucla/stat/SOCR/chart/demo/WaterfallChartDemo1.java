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
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A sample waterfall chart.
 */
public class WaterfallChartDemo1 extends SuperCategoryChart_Bar implements PropertyChangeListener {

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
		 chart = chartMaker.getCategoryChart("Bar", "Product Cost Breakdown",
		            "Expense Category",
		            "Cost Per Unit", dataTable, no_category, pairs, "waterfall");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample dataset for the demo.
     * 
     * @return A sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(15.76, "Product 1", "Labour");
        dataset.addValue(8.66, "Product 1", "Administration");
        dataset.addValue(4.71, "Product 1", "Marketing");
        dataset.addValue(3.51, "Product 1", "Distribution");
        dataset.addValue(32.64, "Product 1", "Total Expense");
        return dataset;}
		else return super.createDataset(false);
    }
    
    /**
     * Returns the chart.
     * 
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
        
        JFreeChart chart = ChartFactory.createWaterfallChart(
            chartTitle,
            domainLabel,
            rangeLabel,
            dataset,
            PlotOrientation.VERTICAL,
            !legendPanelOn,
            true,
            false
        );
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        
        ValueAxis rangeAxis = plot.getRangeAxis();
        
        // create a custom tick unit collection...
        DecimalFormat formatter = new DecimalFormat("##,###");
        formatter.setNegativePrefix("(");
        formatter.setNegativeSuffix(")");
        TickUnits standardUnits = new TickUnits();
        standardUnits.add(new NumberTickUnit(5, formatter));
        standardUnits.add(new NumberTickUnit(10, formatter));
        standardUnits.add(new NumberTickUnit(20, formatter));
        standardUnits.add(new NumberTickUnit(50, formatter));
        standardUnits.add(new NumberTickUnit(100, formatter));
        standardUnits.add(new NumberTickUnit(200, formatter));
        standardUnits.add(new NumberTickUnit(500, formatter));
        standardUnits.add(new NumberTickUnit(1000, formatter));
        standardUnits.add(new NumberTickUnit(2000, formatter));
        standardUnits.add(new NumberTickUnit(5000, formatter));
        rangeAxis.setStandardTickUnits(standardUnits);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

        DecimalFormat labelFormatter = new DecimalFormat("$##,###.00");
        labelFormatter.setNegativePrefix("(");
        labelFormatter.setNegativeSuffix(")");
        renderer.setBaseItemLabelGenerator(
            new StandardCategoryItemLabelGenerator("{2}", labelFormatter)
        );
        renderer.setBaseItemLabelsVisible(true);

		setCategorySummary(dataset);
        return chart;
    }
    
     
}
