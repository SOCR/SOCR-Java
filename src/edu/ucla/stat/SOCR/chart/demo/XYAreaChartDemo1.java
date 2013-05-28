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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
/**
 * A simple demonstration application showing how to create an area chart.
 */
public class XYAreaChartDemo1 extends SuperXYChart implements PropertyChangeListener {
    

	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("XYAreaChartDemo1 doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getXYChart("Area", "Area Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

    protected XYDataset createDataset(boolean isDemo) {
		if (isDemo){
        XYSeries series1 = new XYSeries("Random 1");
        series1.add(new Integer(1), new Double(500.2));
        series1.add(new Integer(2), new Double(694.1));
        series1.add(new Integer(3), new Double(-734.4));
        series1.add(new Integer(4), new Double(453.2));
        series1.add(new Integer(5), new Double(500.2));
        series1.add(new Integer(6), new Double(300.7));
        series1.add(new Integer(7), new Double(734.4));
        series1.add(new Integer(8), new Double(453.2));

        XYSeries series2 = new XYSeries("Random 2");
        series2.add(new Integer(1), new Double(700.2));
        series2.add(new Integer(2), new Double(534.1));
        series2.add(new Integer(3), new Double(323.4));
        series2.add(new Integer(4), new Double(125.2));
        series2.add(new Integer(5), new Double(653.2));
        series2.add(new Integer(6), new Double(432.7));
        series2.add(new Integer(7), new Double(564.4));
        series2.add(new Integer(8), new Double(322.2));

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.setIntervalWidth(0.0);
        return dataset;}
		else return super.createDataset(false);
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
            
        JFreeChart chart = ChartFactory.createXYAreaChart(
			 chartTitle,
            "Domain (X)", "Range (Y)",
            dataset,
            PlotOrientation.VERTICAL,
            !legendPanelOn,  // legend
            true,  // tool tips
            false  // URLs
        );
        
        chart.setBackgroundPaint(Color.white);
        
        XYPlot plot = (XYPlot) chart.getPlot();        
        plot.setBackgroundPaint(Color.lightGray);
        plot.setForegroundAlpha(0.65f);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickMarkPaint(Color.black);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setTickMarkPaint(Color.black);
 
		/*  XYPointerAnnotation pointer = new XYPointerAnnotation(
            "Test", 5.0, -500.0, 3.0 * Math.PI / 4.0
        );
        pointer.setTipRadius(0.0); 
        pointer.setBaseRadius(35.0); 
        pointer.setFont(new Font("SansSerif", Font.PLAIN, 9)); 
        pointer.setPaint(Color.blue); 
        pointer.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT); 
        plot.addAnnotation(pointer);*/

		XYItemRenderer renderer = plot.getRenderer();
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
		setXSummary(dataset);
        return chart;
        
    }
    
    protected JFreeChart createLegend(XYDataset dataset) {
        
        JFreeChart chart = ChartFactory.createXYAreaChart(
            chartTitle,             // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips
            false                     // url
        );
 
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        return chart;
        
    }
}
