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
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.ucla.stat.SOCR.chart.SuperCategoryChart_vertical;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a line chart using 
 * data from a {@link CategoryDataset}.
 */
public class LineChartDemo1b extends SuperCategoryChart_vertical implements PropertyChangeListener {

	public void init(){
		turnLegendPanelOn();
		super.init();
		depMax = 1; // max number of dependent var
		indMax = 100; // max number of independent var
		
	}
    /**
     * Creates a sample dataset.
     * 
     * @return The dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(212, "Case1", "0");
        dataset.addValue(504, "Case1", "1");
        dataset.addValue(1520, "Case1", "2");
        dataset.addValue(1842, "Case1", "3");
        dataset.addValue(2991, "Case1", "4");
        dataset.addValue(112, "Case2", "0");
        dataset.addValue(404, "Case2", "1");
        dataset.addValue(1620, "Case2", "2");
        dataset.addValue(1742, "Case2", "3");
        dataset.addValue(2091, "Case2", "4");
        return dataset;}
		else return super.createDataset(false);
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
            chartTitle,   // chart title
            domainLabel,                       // domain axis label
            rangeLabel,                   // range axis label
            dataset,                         // data
            PlotOrientation.VERTICAL,        // orientation
            !legendPanelOn,                           // include legend
            true,                            // tooltips
            false                            // urls
        );
     
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        // customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // customise the renderer...
        LineAndShapeRenderer renderer 
            = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setBaseFillPaint(Color.white);
        renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

		setCategorySummary(dataset);
        return chart;
    }
    
 
}
