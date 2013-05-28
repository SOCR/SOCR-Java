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
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperIntervalXYChart_Time;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a bar chart using
 * an {@link XYPlot}.
 */
public class XYBarChartDemo1 extends SuperIntervalXYChart_Time implements PropertyChangeListener {

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
		 chart = chartMaker.getXYChart("Bar",chartTitle, "Category", "value", dataTable, no_series, pairs,"Year");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

    protected  JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart(
            chartTitle,
            domainLabel,
            true,
            rangeLabel,
            dataset,
            PlotOrientation.VERTICAL,
            !legendPanelOn,
            true,
            false
        );


        // then customise it a little...
		// chart.addSubtitle(new TextTitle("Source: http://www.amnestyusa.org/abolish/listbyyear.do"));
		  chart.setBackgroundPaint(Color.white);
        
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new ClusteredXYBarRenderer());
		XYItemRenderer renderer = plot.getRenderer();

        StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator(
            "{1} = {2}", new SimpleDateFormat("yyyy"), new DecimalFormat("0")
        );
		renderer.setBaseToolTipGenerator(generator);
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        axis.setLowerMargin(0.01);
        axis.setUpperMargin(0.01);
		//		setXSummary(dataset);  X  is time
        return chart;
    }
    
    /**
     * Creates a sample dataset.
     */
    protected IntervalXYDataset createDataset(boolean isDemo) {
		if (isDemo){
        TimeSeries t1 = new TimeSeries("Executions", "Year", "Count", Year.class);
        try {
            t1.add(new Year(1976), new Integer(0));
            t1.add(new Year(1977), new Integer(1));
            t1.add(new Year(1978), new Integer(0));
            t1.add(new Year(1979), new Integer(2));
            t1.add(new Year(1980), new Integer(0));
            t1.add(new Year(1981), new Integer(1));
            t1.add(new Year(1982), new Integer(2));
            t1.add(new Year(1983), new Integer(5));
            t1.add(new Year(1984), new Integer(21));
            t1.add(new Year(1985), new Integer(18));
            t1.add(new Year(1986), new Integer(18));
            t1.add(new Year(1987), new Integer(25));
            t1.add(new Year(1988), new Integer(11));
            t1.add(new Year(1989), new Integer(16));
            t1.add(new Year(1990), new Integer(23));
            t1.add(new Year(1991), new Integer(14));
            t1.add(new Year(1992), new Integer(31));
            t1.add(new Year(1993), new Integer(38));
            t1.add(new Year(1994), new Integer(31));
            t1.add(new Year(1995), new Integer(56));
            t1.add(new Year(1996), new Integer(45));
            t1.add(new Year(1997), new Integer(74));
            t1.add(new Year(1998), new Integer(68));
            t1.add(new Year(1999), new Integer(98));
            t1.add(new Year(2000), new Integer(85));
            t1.add(new Year(2001), new Integer(66));
            t1.add(new Year(2002), new Integer(71));
            t1.add(new Year(2003), new Integer(65));

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        TimeSeriesCollection tsc = new TimeSeriesCollection(t1);
       // tsc.setDomainIsPointsInTime(false);
        return tsc;
		}  else return super.createDataset(false);
    }

}
