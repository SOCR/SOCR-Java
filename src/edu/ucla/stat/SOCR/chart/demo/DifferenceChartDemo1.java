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
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart_Time;
import edu.ucla.stat.SOCR.chart.data.DateParser;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;

/**
 * A simple demonstration of the {@link XYDifferenceRenderer}.
 */
public class DifferenceChartDemo1 extends SuperXYChart_Time implements PropertyChangeListener {
	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("DifferenceChartDemo1 doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 String time_type = "Day";
		 chart = chartMaker.getXYChart("DifferenceTime","Line Chart", "Time", "Value", dataTable, no_series, pairs, time_type);	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    protected XYDataset createDataset(boolean isDemo) {

		if(isDemo){
        TimeSeries series1 = new TimeSeries("Random 1");
        TimeSeries series2 = new TimeSeries("Random 2");
        double value1 = 0.0;
        double value2 = 0.0;
        Day day = new Day();
        for (int i = 0; i < 200; i++) {
            value1 = value1 + Math.random() - 0.5;
            value2 = value2 + Math.random() - 0.5;
            series1.add(day, value1);
            series2.add(day, value2);
            day = (Day) day.next();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset; }
		else {
			super.setArrayFromTable();
			String[][] x = new String[xyLength][independentVarLength];
			double[][] y = new double[xyLength][dependentVarLength];

			int[][] skipy = new int[xyLength][dependentVarLength];
			
			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					if (depValues[i][index]==null ||depValues[i][index].length()==0)
						skipy[i][index]=1;
					else y[i][index] = Double.parseDouble(depValues[i][index]);
				}

			// create the dataset... 
			TimeSeriesCollection collection = new TimeSeriesCollection();
			TimeSeries series;
		
			for (int ind =0; ind<independentVarLength; ind++){
				if(independentHeaders[ind].indexOf(":")!=-1)
					series = new TimeSeries(independentHeaders[ind].substring(0,independentHeaders[ind].indexOf(":")), Day.class);
				else {					
					series = new TimeSeries(independentHeaders[ind], Day.class);
					//System.out.println("series name is "+independentHeaders[ind]);
				}
			
				//TimeSeries("Executions", "Year", "Count", Year.class);

				for (int i=0; i<xyLength; i++){
					if (x[i][ind]!=null && skipy[i][ind]!=1)
					series.add(DateParser.parseDay(x[i][ind]), y[i][ind]);
					//System.out.println("adding"+DateParser.parseDay(x[i][ind])+","+ y[i][ind]);
				}	
			//	collection.setDomainIsPointsInTime(false);
				collection.addSeries(series);
			}
            return collection;

		}
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            chartTitle,
            domainLabel, rangeLabel,
            dataset,
            !legendPanelOn,  // legend
            true,  // tool tips
            false  // URLs
        );
        chart.setBackgroundPaint(Color.white);
       
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new XYDifferenceRenderer(
            Color.green, Color.red, false)
        );
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        ValueAxis domainAxis = new DateAxis("Time");
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        plot.setDomainAxis(domainAxis);
        plot.setForegroundAlpha(0.5f);  

		//setXSummary(dataset) X is time;          
        return chart; 

    }
    protected JFreeChart createLegend(XYDataset dataset) {
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
        		  chartTitle,
                  domainLabel, rangeLabel,
                  dataset,
                  !legendPanelOn,  // legend
                  true,  // tool tips
                  false  // URLs
              );
 
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();

        
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        return chart;
        
    }
}
