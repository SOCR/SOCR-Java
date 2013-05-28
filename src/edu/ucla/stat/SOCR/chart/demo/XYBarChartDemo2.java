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
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperIntervalXYChart_Time;
import edu.ucla.stat.SOCR.chart.data.DateParser;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a bar chart with a date axis for the domain values.
 */
public class XYBarChartDemo2 extends SuperIntervalXYChart_Time implements PropertyChangeListener {

	public void init(){
		//LEGEND_SWITCH =false;  // JFreeChart bug, can't show the legend Panel the order of the legend is wrong
		super.init();
	}
	
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = i*2;    //column 1 stores value
			 pairs[i][1] = i*2+1;    //column 0 stores time
		 }
		 chart = chartMaker.getXYChart("Bar",chartTitle, "Category", "value", dataTable, no_series, pairs,"Day");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

    protected IntervalXYDataset createDataset(boolean isDemo) {
		if (isDemo){
        TimeSeries series1 = new TimeSeries("Series 1", Day.class);
        series1.add(new Day(1, 1, 2003), 54.3);
        series1.add(new Day(2, 1, 2003), 20.3);
        series1.add(new Day(3, 1, 2003), 43.4);
        series1.add(new Day(4, 1, 2003), -12.0);

        TimeSeries series2 = new TimeSeries("Series 2", Day.class);
        series2.add(new Day(1, 1, 2003), 8.0);
        series2.add(new Day(2, 1, 2003), 16.0);
        series2.add(new Day(3, 1, 2003), 21.0);
        series2.add(new Day(4, 1, 2003), 5.0);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        //dataset.setDomainIsPointsInTime(false);
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
		}
		else {
			setArrayFromTable();
			String[][] x = new String[xyLength][independentVarLength] ;
			double[][] y = new double[xyLength][dependentVarLength];
			int[][] skipy = new int[xyLength][dependentVarLength];


			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					if (depValues[i][index]==null||depValues[i][index].length()==0)
						skipy[i][index] = 1;
					else
					y[i][index] = Double.parseDouble(depValues[i][index]);
				}

			// create the dataset... 
			TimeSeriesCollection collection = new TimeSeriesCollection();
			TimeSeries series;
		
			for (int ind =0; ind<independentVarLength; ind++){
				int start_ind   = independentHeaders[ind].lastIndexOf(":");
				if 	(start_ind< 0)
					start_ind =0;
				
				String serieName = independentHeaders[ind].substring(0, start_ind);
				if (serieName.length()==0)
					serieName="Serie"+ind;
				
				series = new TimeSeries(serieName, Day.class);
				//TimeSeries("Executions", "Year", "Count", Year.class);

				for (int i=0; i<xyLength; i++){
					if(x[i][ind]!=null && skipy[i][ind]!=1){							
						series.add(DateParser.parseDay(x[i][ind]), y[i][ind]);
					}
				}	
				//collection.setDomainIsPointsInTime(false);
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
    protected  JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart(
           chartTitle,      // chart title
            domainLabel,                     // domain axis label
            true,
            rangeLabel,                        // range axis label
            dataset,                    // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                       // include legend
            //false,   // when choose no legend, the color order used in chart is different 
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new ClusteredXYBarRenderer());

		XYItemRenderer renderer = plot.getRenderer();
        renderer.setBaseToolTipGenerator(
            new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("d-MMM-yy"), new DecimalFormat("#,##0.00")
            )
        );
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        // OPTIONAL CUSTOMISATION COMPLETED.

		//setXSummary(dataset);  X is time
        return chart;        
    }
    
    protected JFreeChart createLegend(IntervalXYDataset dataset) {
        
    	   JFreeChart chart = ChartFactory.createXYBarChart(
    	           chartTitle,      // chart title
    	           domainLabel,                     // domain axis label
    	            true,
    	            rangeLabel,                        // range axis label
    	            dataset,                    // data
    	            PlotOrientation.VERTICAL,
    	            true,                       // include legend
    	            true,
    	            false
    	        );

    	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
    	        XYPlot plot = chart.getXYPlot();
    	        plot.setRenderer(new ClusteredXYBarRenderer());

    			XYItemRenderer renderer = plot.getRenderer();
    	        renderer.setBaseToolTipGenerator(
    	            new StandardXYToolTipGenerator(
    	                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
    	                new SimpleDateFormat("d-MMM-yy"), new DecimalFormat("#,##0.00")
    	            )
    	        );
    			renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
    	        // OPTIONAL CUSTOMISATION COMPLETED.

    			//setXSummary(dataset);  X is time
    	        return chart;        
        
    }
    protected JFreeChart createLegendChart(JFreeChart origchart) {
    	
    	JFreeChart legendChart = new JFreeChart("", null, new HiddenPlot(),false);
    	
    	legendChart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot)origchart.getPlot();

        LegendTitle legendTitle = new LegendTitle(plot, 
                new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0), 
                new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0)); 
        legendChart.addLegend(legendTitle); 
        
        return legendChart;
        
    }
    
    public void setDataTable (String input){
    	
    	hasExample= true;
    	 StringTokenizer lnTkns = new StringTokenizer(input,"#");
    	String line;
        int lineCt = lnTkns.countTokens();
        resetTableRows(lineCt);
        int r = 0;
        while(lnTkns.hasMoreTokens()) {
        	line = lnTkns.nextToken();
        	
       //	String tb[] =line.split("\t");
            StringTokenizer serieTkns = new StringTokenizer(line, ";");// IE use "space" Mac use tab as cell separator
            int serieCnt = serieTkns.countTokens();
            String tb[]= new String[serieCnt];
            int r1=0;
            while(serieTkns.hasMoreTokens()) {
            	tb[r1]=serieTkns.nextToken();
            	r1++;
            }
            
            //System.out.println("tb.length="+tb.length);
        	int colCt=tb.length*2;
        	resetTableColumns(colCt);
            for (int i=0; i<tb.length; i++){
            	StringTokenizer cellTkns = new StringTokenizer(tb[i], ",");
            	dataTable.setValueAt(cellTkns.nextToken(), r, 2*i);
            	dataTable.setValueAt(cellTkns.nextToken(), r, 2*i+1);
            }           
        	r++;
        }
        
        // this will update the mapping panel     
        resetTableColumns(dataTable.getColumnCount());
      
    }
}
