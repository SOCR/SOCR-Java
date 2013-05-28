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

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperXYChart;
import edu.ucla.stat.SOCR.chart.data.DateParser;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create an area chart with a date axis for
 * the domain values.
 */
public class XYAreaChartDemo2 extends SuperXYChart implements PropertyChangeListener {

	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("XYAreaChartDemo2 doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 String time_type = "Day";
		chart = chartMaker.getXYChart("AreaTime","Area Chart", "X", "Y", dataTable, no_series, pairs, time_type);
		 //chart = chartMaker.getXYAreaChart_Time("Area Chart", "X", "Y", dataTable, no_series, pairs, time_type);
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    protected XYDataset createDataset(boolean isDemo) {
		if (isDemo){
        TimeSeries series1 = new TimeSeries("Random 1");
        double value = 0.0;
        Day day = new Day();
        for (int i = 0; i < 200; i++) {
            value = value + Math.random() - 0.5;
            series1.add(day, value);
            day = (Day) day.next();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection(series1);
        return dataset; }
		else {
			setArrayFromTable();

			if (independentVarLength != dependentVarLength){
			JOptionPane.showMessageDialog(this, "The number of X and Y doesn't match!");
			resetChart();
			return null;
			}

			String[][] x= new String[xyLength][independentVarLength];
			double[][] y= new double[xyLength][dependentVarLength];
			int[][] skipy= new int[xyLength][dependentVarLength];


			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					if (depValues[i][index]==null || depValues[i][index].length()==0)
						skipy[i][index]=1;
					else y[i][index] = Double.parseDouble(depValues[i][index]);
				}

			TimeSeriesCollection dataset = new TimeSeriesCollection();
			TimeSeries series;

			for (int i=0; i<independentVarLength; i++){
				String serieName = independentHeaders[i];
				if (independentHeaders[i].lastIndexOf(":")!=-1)
					serieName= independentHeaders[i].substring(0, independentHeaders[i].lastIndexOf(":"));
				series = new TimeSeries(serieName);
				for (int j=0; j<xyLength; j++){
					if(x[j][i]!=null && skipy[j][i]!=1)
						series.add(DateParser.parseDay(x[j][i]), y[j][i]);
					//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
				}
				dataset.addSeries(series);
			}
		
			return dataset;
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
        JFreeChart chart = ChartFactory.createXYAreaChart(
														  chartTitle,
            "Time", "Value",
            dataset,
            PlotOrientation.VERTICAL,
            !legendPanelOn,  // legend
            true,  // tool tips
            false  // URLs
        );
        XYPlot plot = chart.getXYPlot();

        ValueAxis domainAxis = new DateAxis("Time");
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        plot.setDomainAxis(domainAxis);
        plot.setForegroundAlpha(0.5f);  
        

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setBaseToolTipGenerator(
            new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("#,##0.00")
            )
        );
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
		// setXSummary(dataset);	 X is time
        return chart;      
    }

    protected JFreeChart createLegend(XYDataset dataset) {
        
        JFreeChart chart = ChartFactory.createXYAreaChart(
            chartTitle,             // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips
            false                     // url
        );
 
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        return chart;
        
    }

  public void resetExample() {
	    isDemo = true;
	    XYDataset dataset= createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;

		convertor.dataset2Table((TimeSeriesCollection)dataset);				
		JTable tempDataTable = convertor.getTable();
		int seriesCount = tempDataTable.getColumnCount()/2;
		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(seriesCount*2+2);

        for(int i=0;i<seriesCount*2;i++) {
            columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
            }

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
