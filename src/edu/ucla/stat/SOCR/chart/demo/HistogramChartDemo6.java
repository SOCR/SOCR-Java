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

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateTickMarkPosition;
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
import edu.ucla.stat.SOCR.gui.SOCROptionPane;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a bar chart using
 * an {@link XYPlot}.
 */
public class HistogramChartDemo6 extends SuperIntervalXYChart implements PropertyChangeListener {
	double[] x_start, x_end;
	String[] raw_x;
	double [] y_freq;
	
	public void init(){

		indLabel = new JLabel("X-Range");
		depLabel = new JLabel("Frequency");
 
		super.init();
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
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
	            false, //!legendPanelOn,
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
			
			raw_x = new String[9];	
			y_freq = new double[9];
			data_count= 9;
			domainLabel="TailLength";
			rangeLabel="NumberDeermice";
			
			y_freq[0]= 1;y_freq[1]= 3;y_freq[2]= 11; y_freq[3]= 18;y_freq[4]= 21;
			y_freq[5]= 20;y_freq[6]= 9;y_freq[7]= 2; y_freq[8]= 1;
		

			raw_x[0]= "52-53";raw_x[1]= "54-55";raw_x[2]= "56-57"; raw_x[3]= "58-59";raw_x[4]= "60-61";
			raw_x[5]= "62-63";raw_x[6]= "64-65";raw_x[7]= "66-67"; raw_x[8]= "68-69";
			
			parseRange(raw_x, 9);
			
			IntervalXYDataset dataset = new SimpleIntervalXYDataset(9, x_start, x_end, y_freq);
			return dataset;
	
			
		}  else {
			trimColumn = true;
			
			setArrayFromTable();
			raw_x = new String[xyLength] ;//independentVarLength=1
			double[] y = new double[xyLength];//dependentVarLength=1
			
		//	System.out.println("xyLength="+xyLength);
			for (int i = 0; i < xyLength; i++){ 
				raw_x[i] = indepValues[i][0];
				//System.out.println("raw_x[i]="+raw_x[i]);
			}
			
			for (int i = 0; i < xyLength; i++) 
				if (depValues[i][0]!=null && depValues[i][0]!="null"&&depValues[i][0].length()!=0) 
					y[i] = Double.parseDouble(depValues[i][0]);
				else y[i]=0.0;
	
			domainLabel = independentHeaders[0];
			rangeLabel = dependentHeaders[0];
			parseRange(raw_x, xyLength);
			IntervalXYDataset dataset = new SimpleIntervalXYDataset(xyLength, x_start, x_end, y);	
			return dataset;
		}
    }
  

    protected void parseRange(String[] ranges, int len){
    //	System.out.println("len ="+len);
    	x_start = new double[len];
    	x_end = new double[len];
    	for (int i=0; i<len; i++){
    		//System.out.println("ranges[i] ="+ranges[i]);
    		StringTokenizer st = new StringTokenizer(ranges[i], "-,");  		
    		x_start[i] = Double.parseDouble(st.nextToken());
    		x_end[i] = Double.parseDouble(st.nextToken());
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

		//convertor.dataset2Table(dataset);	
        convertor.data2Table(raw_x, y_freq, domainLabel, rangeLabel, data_count);		
		JTable tempDataTable = convertor.getTable();
		resetTableRows(tempDataTable.getRowCount()+1);

		columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));
		
		resetTableColumns(tempDataTable.getColumnCount());
        for(int i=0;i<tempDataTable.getColumnCount();i++) {
            columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
			//  System.out.println("updateExample tempDataTable["+i+"] = " +tempDataTable.getColumnName(i));
        }
        
        for(int i=0;i<tempDataTable.getRowCount();i++)
            for(int j=0;j<tempDataTable.getColumnCount();j++) {
                dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
		 }
        columnModel = dataTable.getColumnModel();
		dataTable.setTableHeader(new EditableHeader(columnModel));

        dataPanel.removeAll();
        JScrollPane tablePanel = new JScrollPane(dataTable);
    	tablePanel.setRowHeaderView(headerTable);
		dataPanel.add(tablePanel);
		dataPanel.add(new JScrollPane(summaryPanel));
		dataTable.setGridColor(Color.gray);
		dataTable.setShowGrid(true);
		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
		try { 
			dataTable.setDragEnabled(true);  
		} catch (Exception e) {
		}

        dataPanel.validate();

        setMapping();
	
		updateStatus(url);
  }
}
