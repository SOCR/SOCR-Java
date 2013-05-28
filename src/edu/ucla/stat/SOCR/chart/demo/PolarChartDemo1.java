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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.PolarItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart;
import edu.ucla.stat.SOCR.chart.gui.*;

/**
 * A simple demonstration application showing how to create a polar chart.
 */
public class PolarChartDemo1 extends  SuperXYChart implements PropertyChangeListener {

	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("PolarChartDemo1 doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getXYChart("Polar", "Polar Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo){
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries s1 = new XYSeries("Series 1");
        s1.add(0.0, 2.0);
        s1.add(90.0, 13.0);
        s1.add(180.0, 9.0);
        s1.add(270.0, 8.0);
        result.addSeries(s1);
        
        XYSeries s2 = new XYSeries ("Series 2");
        s2.add(90.0, -11.2);
        s2.add(180.0, 21.4);
        s2.add(250.0, 17.3);
        s2.add(355.0, 10.9);
        result.addSeries(s2);
        return result;}
		else return super.createDataset(false);
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createPolarChart(
            chartTitle, dataset, true, false, false);

        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        PolarPlot plot = (PolarPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        if(isDemo){
        	plot.addCornerTextItem("Corner Item 1");
        	plot.addCornerTextItem("Corner Item 2");
        }
        
		plot.setRenderer(new SOCRPolarItemRenderer());
		//PolarItemRenderer renderer = plot.getRenderer();
		//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
		setXSummary(dataset);
		if (legendPanelOn)
	        	chart.removeLegend();
		 
        return chart;
        
       
    }
 
}
