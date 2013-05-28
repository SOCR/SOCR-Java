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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart_QQ;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class QQNormalPlotDemo extends SuperXYChart_QQ implements PropertyChangeListener {

	
	public void init(){
		LEGEND_SWITCH= false;
		super.init();
		depLabel.setText("Data"); // Y

	}
	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getColumnCount();	//one y column only 	 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 0;    // there is no x column
			 pairs[i][1] = 0;    //column y
		 }
		 chart = chartMaker.getXYChart("LineQQ","QQNormalPlot Chart", "Data", "NormalDistribution of Data", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo){

			row_count = 10;
		
			raw_y= new String[row_count];

			raw_y[0]="97"; raw_y[1]="98";raw_y[2]="92";raw_y[3]="94";raw_y[4]="93";
			raw_y[5]="106"; raw_y[6]="94";raw_y[7]="109";raw_y[8]="102";raw_y[9]="96";
		
			do_normalQQ(raw_y, row_count);
			int len = normalQuantiles.length;
		
			XYSeries series1 = new XYSeries("QQ");
			for (int i=0; i<len; i++){
			//for (int i=0; i<row_count; i++){
				//System.out.println("i="+i+" normalQ="+normalQuantiles[i]+" stdRes="+stdResiduals[i]);
				series1.add(normalQuantiles[i], stdResiduals[i]);}
    
			XYSeries series2 = new XYSeries("Reference Line");
			min_x = Math.min(normalQuantiles[0], stdResiduals[0]);
			min_x = min_x-0.125;
			max_x = Math.max (normalQuantiles[len-1],stdResiduals[len-1]);
		//	max_x = Math.max (normalQuantiles[row_count-1],stdResiduals[row_count-1]);
			max_x = max_x+0.125;
			series2.add(min_x,min_x);
			series2.add(max_x,max_x);

			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(series1);
			dataset.addSeries(series2);
                
			return dataset;}
		else return super.createDataset(false);
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle, //"Normal Q-Q plot",      // chart title
            domainLabel,                      // x axis label
            rangeLabel,                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
       // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
		// renderer.setShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		//  renderer.setLinesVisible(false);
		renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
       
		//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.02);
        rangeAxis.setLowerMargin(0.02);

		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        domainAxis.setUpperMargin(0.02);
        domainAxis.setLowerMargin(0.02);

		// domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // OPTIONAL CUSTOMISATION COMPLETED.
		//setQQSummary(dataset);    // very confusing   
        return chart;
        
    }

}
