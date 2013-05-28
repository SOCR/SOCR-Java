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

import java.awt.BasicStroke;
import java.awt.Color;
import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
/**
 * A simple demonstration of the {@link XYStepRenderer} class.
 */
public class XYStepRendererDemo1 extends SuperXYChart implements PropertyChangeListener {
    /**
     * Creates a sample chart.
     * 
     * @param dataset  a dataset for the chart.
     * 
     * @return A sample chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,
            domainLabel,
            rangeLabel,
            dataset,
            PlotOrientation.VERTICAL,
            !legendPanelOn,
            true,
            false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        XYStepRenderer renderer = new XYStepRenderer();
        renderer.setBaseStroke(new BasicStroke(2.0f));
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        renderer.setDefaultEntityRadius(6);
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        plot.setRenderer(renderer);
		setXSummary(dataset);
        return chart;
    }
    
    protected JFreeChart createLegend(XYDataset dataset) {
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,             // chart title
            domainLabel,               // domain axis label
            rangeLabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips
            false                     // url
        );
 
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();

        XYStepRenderer renderer = new XYStepRenderer();
        renderer.setBaseStroke(new BasicStroke(2.0f));
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        renderer.setDefaultEntityRadius(6);
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        plot.setRenderer(renderer);
        return chart;
        
    }
    
    public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("XYStepRendererDemo1 doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getXYChart("Step","Step Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

    /**
     * Creates a sample dataset.
     * 
     * @return A dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
		if (isDemo){
        XYSeries series1 = new XYSeries("Series 1");
        series1.add(1.0, 3.0);
        series1.add(2.0, 4.0);
        series1.add(3.0, 2.0);
        //series1.add(5.0, 6.0);
        series1.add(6.0, 3.0);
        XYSeries series2 = new XYSeries("Series 2");
        series2.add(1.0, 7.0);
        series2.add(2.0, 6.0);
        series2.add(3.0, 9.0);
        series2.add(4.0, 5.0);
        series2.add(6.0, 4.0);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;}
		else return super.createDataset(false);
    }

}
