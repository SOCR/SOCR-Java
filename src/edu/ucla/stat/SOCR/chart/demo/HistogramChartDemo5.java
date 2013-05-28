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
import javax.swing.table.TableColumnModel;

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
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a bar chart using
 * an {@link XYPlot}.
 */
public class HistogramChartDemo5 extends SuperIntervalXYChart implements PropertyChangeListener {

	public void init(){

		//indLabel = new JLabel("X");
		depLabel = new JLabel("Frequency");
 
		mapIndep=false;
		super.init();
		depMax = 10; // max number of dependent var
		indMax = 10; // max number of independent var

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
		  domainLabel= "Row_Number";
		  rangeLabel= "Frequency";
			
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
			double[] x_start, x_end;
			double [] y_freq;
			x_start = new double[12];x_end = new double[12];y_freq = new double[12];
			data_count= 12;
			raw_y = new String[12];
			
			for (int i=0; i<12; i++){
				x_start[i] = i+1;
				x_end[i] =x_start[i]+1;
			}
			
			y_freq[0]= 0;y_freq[1]= 1;y_freq[2]= 0; y_freq[3]= 2;y_freq[4]= 0;
			y_freq[5]= 1.5;y_freq[6]= 2;y_freq[7]= 5; y_freq[8]= 21;y_freq[9]= 18;
			y_freq[10]= 18;y_freq[11]= 25;
		

			raw_y[0]= "0";raw_y[1]= "1";raw_y[2]= "0"; raw_y[3]= "2";raw_y[4]= "0";
			raw_y[5]= "1.5";raw_y[6]= "2";raw_y[7]= "5"; raw_y[8]= "21";raw_y[9]= "18";
			raw_y[10]= "18";raw_y[11]= "25";
			
			domainLabel= "Row_Number";
			rangeLabel= "Frequency";
			 
			IntervalXYDataset dataset = new SimpleIntervalXYDataset(12, x_start, x_end, y_freq);
			return dataset;
	
			
		}  else {
			
			return super.createDatasetForSingleColumn(false);
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
        convertor.Y2Table(raw_y, data_count);		
		JTable tempDataTable = convertor.getTable();
		//int seriesCount = tempDataTable.getColumnCount()/2;
		resetTableRows(tempDataTable.getRowCount()+1);
		//resetTableColumns(seriesCount*2);
		resetTableColumns(1);

		//correct the column name
		//  columnModel.getColumn(0).setHeaderValue("Data");
		columnModel.getColumn(0).setHeaderValue("Frequency");
		  
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
	
        setMapping();
	
		updateStatus(url);
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
         StringTokenizer cellTkns = new StringTokenizer(line, " \t\f,");// IE use "space" Mac use tab as cell separator
         int cellCnt = cellTkns.countTokens();
         String tb[]= new String[cellCnt];
         int r1=0;
           while(cellTkns.hasMoreTokens()) {
            tb[r1]=cellTkns.nextToken();
            r1++;
           }
         //System.out.println("tb.length="+tb.length);
     	int colCt=tb.length;
     	resetTableColumns(colCt);
         for (int i=0; i<tb.length; i++){
         	//System.out.println(tb[i]);
         	if (tb[i].length()==0)
         		tb[i]="0";
         	dataTable.setValueAt(tb[i], r,i);
         }           
     	r++;
     }
     
     // this will update the mapping panel     
     resetTableColumns(dataTable.getColumnCount());
 }
  public void setXLabel(String xLabel){
	  	domainLabel = xLabel;
	  
	  }
  
  public void setYLabel(String yLabel){
	  	rangeLabel = yLabel;
	  	TableColumnModel columnModel = dataTable.getColumnModel();	  	
	  	columnModel.getColumn(0).setHeaderValue(yLabel);
	  	dataTable.setTableHeader(new EditableHeader(columnModel));  
  }
	  
  
  public void setMapping(){
	 	addButtonDependent();
  }
  
}
