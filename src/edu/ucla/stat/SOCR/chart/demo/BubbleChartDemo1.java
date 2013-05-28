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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYZDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperBubbleChart;
import edu.ucla.stat.SOCR.chart.data.SampleXYZDataset;
import edu.ucla.stat.SOCR.chart.gui.SOCRScaledBubbleSeriesLabelGenerator;

/**
 * A bubble chart demo.
 */
public class BubbleChartDemo1 extends SuperBubbleChart implements PropertyChangeListener {

	protected XYZDataset createDataset(boolean isDemo){
		if (isDemo){
			domainLabel = "X";
			rangeLabel = "Y";
		 return new SampleXYZDataset();
		}
		else return super.createDataset(false); 
	}
	
	/**
	 * sample code showing how to create a  bubble chart using ChartGenerator_JTable class
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperXYZChart doTest get called!");
		
		 int no_series = (dataTable.getColumnCount())/3;		 
		 int[][] pairs = new int[no_series][3];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 3*i;    //column x
			 pairs[i][1] = 3*i+1;    //column y
			 pairs[i][2] = 3*i+2;    //column y
		 }
	
		 chart = chartMaker.getXYZBubbleChart("Bubble Chart", domainLabel, rangeLabel, dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(XYZDataset dataset) {
        JFreeChart chart = ChartFactory.createBubbleChart(
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
        plot.setForegroundAlpha(0.65f);
        
    //    SOCRBubbleRenderer renderer = new SOCRBubbleRenderer(dataset);
    //    plot.setRenderer((XYItemRenderer)renderer);
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
		renderer.setLegendItemLabelGenerator(new SOCRScaledBubbleSeriesLabelGenerator(zScale));           

        // increase the margins to account for the fact that the auto-range 
        // doesn't take into account the bubble size...
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setLowerMargin(0.15);
        domainAxis.setUpperMargin(0.15);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLowerMargin(0.15);
        rangeAxis.setUpperMargin(0.15);
        return chart;
    }

}
