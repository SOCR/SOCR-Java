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
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart_Time;
import edu.ucla.stat.SOCR.chart.data.DateParser;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
/**
 * A crosshair demo.
 */
public class CrosshairDemo3 extends SuperXYChart_Time implements PropertyChangeListener {
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
		 String time_type = "Month";
		 chart = chartMaker.getXYChart("LineTime",chartTitle, "X", "Y", dataTable, no_series, pairs, time_type);	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
        protected JFreeChart createChart(XYDataset dataset) {

            JFreeChart c = ChartFactory.createTimeSeriesChart(
               chartTitle,// "Legal & General Unit Trust Prices",
               domainLabel, 
               rangeLabel,// "Date", "Price Per Unit",
                dataset,
                !legendPanelOn,
                true,
                false
            );

            c.setBackgroundPaint(Color.white);

            XYPlot plot = (XYPlot) c.getPlot();
            plot.setBackgroundPaint(Color.lightGray);
            plot.setDomainGridlinePaint(Color.white);
            plot.setRangeGridlinePaint(Color.white);
            plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            
            XYItemRenderer renderer = plot.getRenderer();
            if (renderer instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) renderer;
                rr.setBaseShapesVisible(true);
                rr.setBaseShapesFilled(true);
            }
			renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());           
            
            DateAxis axis = (DateAxis) plot.getDomainAxis();
            axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
            
			//setXSummary(dataset) X is time;
            return c;

        }
       protected JFreeChart createLegend(XYDataset dataset) {
	        
	        JFreeChart chart = ChartFactory.createTimeSeriesChart(
	        		chartTitle,
	                domainLabel,
	                rangeLabel,
	                 dataset,
	                 true,
	                 true,
	                 false
	             );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        XYPlot plot = (XYPlot) chart.getPlot();

	        XYItemRenderer renderer = plot.getRenderer();
            if (renderer instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) renderer;
                rr.setBaseShapesVisible(true);
                rr.setBaseShapesFilled(true);
            }
			renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
	        return chart;
	        
	    }
        /**
         * Creates a dataset, consisting of two series of monthly data.
         *
         * @return the dataset.
         */
        protected XYDataset createDataset(boolean  isDemo) {
			if (isDemo){
				chartTitle=  "Legal & General Unit Trust Prices";
				domainLabel = "Date";
				rangeLabel="Price Per Unit";
            TimeSeries s1 = new TimeSeries(
                "L&G European Index Trust", Month.class
            );
            s1.add(new Month(2, 2001), 181.8);
            s1.add(new Month(3, 2001), 167.3);
            s1.add(new Month(4, 2001), 153.8);
            s1.add(new Month(5, 2001), 167.6);
            s1.add(new Month(6, 2001), 158.8);
            s1.add(new Month(7, 2001), 148.3);
            s1.add(new Month(8, 2001), 153.9);
            s1.add(new Month(9, 2001), 142.7);
            s1.add(new Month(10, 2001), 123.2);
            s1.add(new Month(11, 2001), 131.8);
            s1.add(new Month(12, 2001), 139.6);
            s1.add(new Month(1, 2002), 142.9);
            s1.add(new Month(2, 2002), 138.7);
            s1.add(new Month(3, 2002), 137.3);
            s1.add(new Month(4, 2002), 143.9);
            s1.add(new Month(5, 2002), 139.8);
            s1.add(new Month(6, 2002), 137.0);
            s1.add(new Month(7, 2002), 132.8);

            TimeSeries s2 = new TimeSeries("L&G UK Index Trust", Month.class);
            s2.add(new Month(2, 2001), 129.6);
            s2.add(new Month(3, 2001), 123.2);
            s2.add(new Month(4, 2001), 117.2);
            s2.add(new Month(5, 2001), 124.1);
            s2.add(new Month(6, 2001), 122.6);
            s2.add(new Month(7, 2001), 119.2);
            s2.add(new Month(8, 2001), 116.5);
            s2.add(new Month(9, 2001), 112.7);
            s2.add(new Month(10, 2001), 101.5);
            s2.add(new Month(11, 2001), 106.1);
            s2.add(new Month(12, 2001), 110.3);
            s2.add(new Month(1, 2002), 111.7);
            s2.add(new Month(2, 2002), 111.0);
            s2.add(new Month(3, 2002), 109.6);
            s2.add(new Month(4, 2002), 113.2);
            s2.add(new Month(5, 2002), 111.6);
            s2.add(new Month(6, 2002), 108.8);
            s2.add(new Month(7, 2002), 101.6);

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(s1);
            dataset.addSeries(s2);

          //  dataset.setDomainIsPointsInTime(true);

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
					series = new TimeSeries(independentHeaders[ind].substring(0,independentHeaders[ind].indexOf(":")), Month.class);
				else 
					series = new TimeSeries(independentHeaders[ind], Month.class);
				//TimeSeries("Executions", "Year", "Count", Year.class);

				for (int i=0; i<xyLength; i++){
					if(x[i][ind]!=null && y[i][ind]!=1)
					series.add(DateParser.parseMonth(x[i][ind]), y[i][ind]);
				}	
				//collection.setDomainIsPointsInTime(false);
				collection.addSeries(series);
			}
            return collection;
			}

        }
        
  
}
