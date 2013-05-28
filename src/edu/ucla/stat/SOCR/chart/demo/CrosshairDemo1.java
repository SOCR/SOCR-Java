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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart_Time;
import edu.ucla.stat.SOCR.chart.data.DateParser;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;

/**
 * An example of a crosshair being controlled by an external UI component.
 */
public class CrosshairDemo1 extends SuperXYChart_Time implements PropertyChangeListener {
	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 String time_type = "Minute";
		 chart = chartMaker.getXYChart("LineTime",chartTitle, "X", "Y", dataTable, no_series, pairs, time_type+" noshape");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
        /**
         * Creates the demo chart.
         * 
         * @return The chart.
         */
        protected JFreeChart createChart(XYDataset dataset) {
            JFreeChart chart1 = ChartFactory.createTimeSeriesChart(
				chartTitle,
                domainLabel, //"Time of Day", 
                rangeLabel, //"Value",
                dataset, 
                !legendPanelOn, 
                true, 
                false
            );

            chart1.setBackgroundPaint(Color.white);
            XYPlot plot = chart1.getXYPlot();
            plot.setOrientation(PlotOrientation.VERTICAL);
            plot.setBackgroundPaint(Color.lightGray);
            plot.setDomainGridlinePaint(Color.white);
            plot.setRangeGridlinePaint(Color.white);
            plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
            
            plot.setDomainCrosshairVisible(true);
            plot.setDomainCrosshairLockedOnData(false);
            plot.setRangeCrosshairVisible(false);
            XYItemRenderer renderer = plot.getRenderer();
            renderer.setBasePaint(Color.black);
			renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());           

			//			setXSummary(dataset); X is time
            return chart1;
        }
        
 protected JFreeChart createLegend(XYDataset dataset) {
	        
	        JFreeChart chart = ChartFactory.createTimeSeriesChart(
	        		chartTitle,
	                domainLabel, //"Time of Day", 
	                rangeLabel,  //"Value",
	                dataset, 
	                !legendPanelOn, 
	                true, 
	                false
	            );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        XYPlot plot = (XYPlot) chart.getPlot();

	        XYItemRenderer renderer = plot.getRenderer();
            renderer.setBasePaint(Color.black);
			renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
	        return chart;
	        
	    }
        /**
         * Creates a sample dataset.
         * 
         * @param name  the dataset name.
         * @param base  the starting value.
         * @param start  the starting period.
         * @param count  the number of values to generate.
         *
         * @return The dataset.
         */
	protected XYDataset createDataset(boolean isDemo){
		if (isDemo){
			String name ="Random 1";
			double base = 100.0;	
			RegularTimePeriod start = new Minute();
			int count =200;	

            TimeSeries series = new TimeSeries(name, start.getClass());
            RegularTimePeriod period = start;
            double value = base;
            for (int i = 0; i < count; i++) {
                series.add(period, value);    
                period = period.next();
                value = value * (1 + (Math.random() - 0.495) / 10.0);
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(series);

            return dataset;}
		else {
			super.setArrayFromTable();
			String[][] x = new String[xyLength][independentVarLength];
			double[][] y = new double[xyLength][dependentVarLength];
			int[][] skipy = new int[xyLength][dependentVarLength];

			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					if (depValues[i][index]==null || depValues[i][index].length()==0)
						skipy[i][index]=1;
					else y[i][index] = Double.parseDouble(depValues[i][index]);


			// create the dataset... 
			TimeSeriesCollection collection = new TimeSeriesCollection();
			TimeSeries series;
		
			for (int ind =0; ind<independentVarLength; ind++){
				if (independentHeaders[ind].indexOf(":")!=-1)
					series = new TimeSeries(independentHeaders[ind].substring(0,independentHeaders[ind].indexOf(":")), Minute.class);
				else 
					series = new TimeSeries(independentHeaders[ind], Minute.class);
				//TimeSeries("Executions", "Year", "Count", Year.class);

				for (int i=0; i<xyLength; i++){
					if(x[i][ind]!=null && y[i][ind]!=1)
					series.add(DateParser.parseMinute(x[i][ind]), y[i][ind]);
				}	
			//	collection.setDomainIsPointsInTime(false);
				collection.addSeries(series);
			}
            return collection;

		}

        }
    

}

