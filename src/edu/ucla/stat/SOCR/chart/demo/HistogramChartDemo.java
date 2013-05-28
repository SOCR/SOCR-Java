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
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis; 
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset;
import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperHistogramChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a bar chart using
 * an {@link XYPlot}.
 */
public class HistogramChartDemo extends SuperHistogramChart implements PropertyChangeListener {

	public int windowSize = 1;
	
	/**
	 *  sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("histogramChartDemo doTest get called!");
		
		 int no_series = dataTable.getColumnCount();	//one y column only 	 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 0;    // there is no x column
			 pairs[i][1] = 0;    //column y
		 }
		 chart = chartMaker.getXYChart("Histogram","Histogram Chart", "Value", "frequency", dataTable, no_series, pairs,String.valueOf(bin_size));	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
    protected  JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart(
            chartTitle,
            domainLabel,
            false,
            rangeLabel,
            dataset,
            PlotOrientation.VERTICAL,
           // !legendPanelOn,
            false, // no legend
            true,
            false
        );


        // then customise it a little...
		// chart.addSubtitle(new TextTitle("Source: http://www.amnestyusa.org/abolish/listbyyear.do"));
	    chart.setBackgroundPaint(Color.white);
        
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new ClusteredXYBarRenderer());
        XYItemRenderer renderer = plot.getRenderer();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	     
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    //    domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	     
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        return chart;
    }
    
    /**
     * Creates a sample dataset.
     */
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected IntervalXYDataset createDataset(boolean isDemo) {
        if (isDemo){
      
        /*	row_count = 20;
		
        	raw_x= new String[row_count];

        	raw_x[0]="97"; raw_x[1]="98";raw_x[2]="92";raw_x[3]="94";raw_x[4]="93";
        	raw_x[5]="106"; raw_x[6]="94";raw_x[7]="109";raw_x[8]="102";raw_x[9]="96";
	
        	raw_x[10]="103"; raw_x[11]="98";raw_x[12]="92";raw_x[13]="94";raw_x[14]="93";
        	raw_x[15]="106"; raw_x[16]="94";raw_x[17]="109";raw_x[18]="102";raw_x[19]="95";*/
        	
        	row_count = 10;
        	raw_x= new String[row_count];
        	raw_x[0]="0.0077937"; raw_x[1]="0.0205743";raw_x[2]="0.0313581";raw_x[3]="0.0136532";raw_x[4]="-0.011547";
        	raw_x[5]="-0.0046848"; raw_x[6]="0.0261796";raw_x[7]="0.0166328";raw_x[8]="0.02388";raw_x[9]="-0.0038399";

        	default_bin = (int)Math.sqrt(row_count);  
        	set_binSize(default_bin);
  
        	do_histogram(raw_x, row_count);
        	
        	IntervalXYDataset dataset = new SimpleIntervalXYDataset(bin_count, x_start, x_end, y_freq);
        	rangeLabel= "Frequency";
        	domainLabel = "Data";
        	return dataset;}
        else return super.createDataset(false);
        
    }
}
