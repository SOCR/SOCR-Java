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

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperIntervalXYChart;
import edu.ucla.stat.SOCR.chart.SuperHistogramChart;
import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a bar chart using
 * an {@link XYPlot}.
 */
public class HistogramChartDemo4 extends SuperIntervalXYChart implements PropertyChangeListener {

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
		 chart = chartMaker.getXYChart("Bar",chartTitle, "Value", "Frequency", dataTable, no_series, pairs,"");	
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
	            false,//!legendPanelOn,
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
	        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		     
	        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
	        return chart;
	    }
    
    /**
     * Creates a sample dataset.
     */
    protected IntervalXYDataset createDataset(boolean isDemo) {
		if (isDemo){
			domainLabel = "Data";
			rangeLabel = "Frequency";
			double[] x_start, x_end;
			int [] y_freq;
			x_start = new double[12];x_end = new double[12];y_freq = new int[12];
			
			x_start[0]= 1976;x_start[1]= 1977;x_start[2]= 1978; x_start[3]= 1979;x_start[4]= 1980;
			x_start[5]= 1981;x_start[6]= 1982;x_start[7]= 1983; x_start[8]= 1984;x_start[9]= 1985;x_start[10]= 1986;x_start[11]= 1987;
			
			for (int i=0; i<12; i++)
				x_end[i] =x_start[i]+1;
			
			y_freq[0]= 0;y_freq[1]= 1;y_freq[2]= 0; y_freq[3]= 2;y_freq[4]= 0;
			y_freq[5]= 1;y_freq[6]= 2;y_freq[7]= 5; y_freq[8]= 21;y_freq[9]= 18;y_freq[10]= 18;y_freq[11]= 25;
			
			
			IntervalXYDataset dataset = new SimpleIntervalXYDataset(12, x_start, x_end, y_freq);
			return dataset;
			/*
       XYSeries t1 = new XYSeries("");
        try {
            t1.add(new Double(1976), new Integer(0));
            t1.add(new Double(1977), new Integer(1));
            t1.add(new Double(1978), new Integer(0));
            t1.add(new Double(1979), new Integer(2));
            t1.add(new Double(1980), new Integer(0));
            t1.add(new Double(1981), new Integer(1));
            t1.add(new Double(1982), new Integer(2));
            t1.add(new Double(1983), new Integer(5));
            t1.add(new Double(1984), new Integer(21));
            t1.add(new Double(1985), new Integer(18));
            t1.add(new Double(1986), new Integer(18));
            t1.add(new Double(1987), new Integer(25));
           

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        XYSeriesCollection tsc = new XYSeriesCollection(t1);
     	
        return tsc;*/
			
		}  else {
			IntervalXYDataset ds = super.createDataset(false);
			domainLabel = independentHeaders[0];
			rangeLabel = dependentHeaders[0];
			return ds;
		}
    }
  

    /**
     * reset dataTable to default (demo data), and refesh chart
     */
  public void resetExample() {
	    isDemo = true;
		
	    IntervalXYDataset dataset= createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;

		convertor.dataset2Table(dataset);				
		JTable tempDataTable = convertor.getTable();
		int seriesCount = tempDataTable.getColumnCount()/2;
		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(seriesCount*2);

		//correct the column name
		  columnModel.getColumn(0).setHeaderValue("Data");
		  columnModel.getColumn(1).setHeaderValue("Frequency");
		  domainLabel = "Data";
		  rangeLabel = "Frequency";
		  
     /*   for(int i=0;i<seriesCount*2;i++) {
            columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
            }*/

		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));

        for(int i=0;i<tempDataTable.getRowCount();i++)
            for(int j=0;j<tempDataTable.getColumnCount();j++) {
                dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
		 }
        dataPanel.removeAll();
        dataPanel.add(new JScrollPane(dataTable));
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);

		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try { 
			dataTable.setDragEnabled(true);  
		} catch (Exception e) {
		}

        dataPanel.validate();

		// do the mapping
		for (int i=0; i<seriesCount; i++){
			addButtonIndependent();
			addButtonDependent();
		}

		updateStatus(url);
  }
}
