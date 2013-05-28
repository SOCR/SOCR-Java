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
package edu.ucla.stat.SOCR.chart;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.lang.NumberFormatException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.Statistics;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategoryCellLabelGenerator;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a statistical bar chart using category dataset.
 */
public class SuperCategoryChart_Stat_Raw_Vertical extends SuperCategoryChart_Stat_Raw implements PropertyChangeListener {

    protected static final int CATEGORY_COUNT=1;
   
	
	public void init(){
		 mapIndep= false;
		 depLabel = new JLabel("Series");
		 indLabel = new JLabel("Categories");

		 super.init();
		 indMax = 1;  //category
		 depMax = 100;  //series
		
		 updateStatus(url);	
		 
		 resetExample();
		 validate();
		
	}
/**
 * sample code for generating chart using ChartGenerator_JTable 
 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperCategoryChart_Stat_Raw_Vertical doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("BarStatRaw", "Category Chart", "Type", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	/**
	 * create chart using data from the dataTable
	 */
	public void doChart(){

		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}
		if (dependentIndex < 0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}
		
		isDemo = false;
		dataset = createDataset(isDemo);	 // not a demo, so get data from the table
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();

		//updateStatus("Chart has been updated, click GRAPH to view it.");
	}

 /**
  * 
  * @param isDemo data come from demo(true) or dataTable(false)
  * @return
  */ 
    protected  CategoryDataset createDataset(boolean isDemo) {
		double mean, stdDev;
		if (isDemo){
				 SERIES_COUNT = 3;
				// CATEGORY_COUNT = 1;
				 VALUE_COUNT = 10;

				 values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
   

				 DefaultStatisticalCategoryDataset dataset 
					 = new DefaultStatisticalCategoryDataset();

				 for (int s = 0; s < SERIES_COUNT; s++) {
					 for (int c = 0; c < CATEGORY_COUNT; c++) {
						 Double[] values = createValueList(0, 20.0, VALUE_COUNT);
						 values_storage[s][c]= vs;
						 mean = Statistics.calculateMean(values);
						 stdDev = Statistics.getStdDev(values);
						 dataset.add(mean,stdDev, "Series " + s, "Category " + c);
					 }
				 }
    

				 return dataset;}
	
		else{
			
			//			setXYArray();
			setArrayFromTable();
			//System.out.println("independentVarLength"+independentVarLength);
			//System.out.println("dependentVarLength"+dependentVarLength); //3
			
			independentVarLength=1;  //number of category  ==1
			
			String[][] x= new String[xyLength][dependentVarLength];
			String[][] y= new String[xyLength][independentVarLength];

			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++)
				   x[i][index] = depValues[i][index];

		   
		/*	for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					y[i][index] = depValues[i][index];*/

			SERIES_COUNT = dependentVarLength;
			//CATEGORY_COUNT = 1;
		
			DefaultStatisticalCategoryDataset dataset 
				= new DefaultStatisticalCategoryDataset();

			values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];

			for (int s = 0; s < SERIES_COUNT; s++) {
            for (int c = 0; c < CATEGORY_COUNT; c++) {
            	String v= "" ;
            	for (int i = 0; i<xyLength; i++)
            		v= v+x[i][s]+"," ;
            	
                Double[] values = createValueList(v);
				values_storage[s][c]= v;
				mean = Statistics.calculateMean(values, false);
				stdDev = Statistics.getStdDev(values);
				//System.out.println("Adding "+ dependentHeaders[s]+ " Category "+c);
                dataset.add(mean, stdDev,  dependentHeaders[s], "Category "+c);
            }
		}
	
            return dataset;   
		}
	}	


 
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
          // create the chart...
          
              // create the chart...
        JFreeChart chart
			= ChartFactory.createLineChart(
            chartTitle, // chart title
            domainLabel,                         // domain axis label
            rangeLabel,                        // range axis label
            dataset,                        // data
            PlotOrientation.VERTICAL,       // orientation
            !legendPanelOn,                           // include legend
            true,                           // tooltips
            false                           // urls
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        // customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        // customise the renderer...
        StatisticalBarRenderer renderer = new StatisticalBarRenderer();
        renderer.setErrorIndicatorPaint(Color.black);
        plot.setRenderer(renderer);

        // OPTIONAL CUSTOMISATION COMPLETED.
       return chart;
  
	}	
    
    protected JFreeChart createLegend(CategoryDataset dataset) {
        
		//  JFreeChart chart = ChartFactory.createAreaChart(
	        JFreeChart chart = ChartFactory.createLineChart(
	            chartTitle,             // chart title
	            domainLabel,               // domain axis label
	            rangeLabel,                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        CategoryPlot plot = chart.getCategoryPlot();

	        StatisticalBarRenderer renderer = new StatisticalBarRenderer();
	        renderer.setErrorIndicatorPaint(Color.black);
			renderer.setLegendItemLabelGenerator(new SOCRCategoryCellLabelGenerator(dataset, values_storage, SERIES_COUNT, CATEGORY_COUNT));
	        plot.setRenderer(renderer);
	        return chart;
	        
	    }

	    protected JFreeChart createLegendChart(JFreeChart origchart) {
	    	
	    	JFreeChart legendChart = new JFreeChart("", null, new HiddenPlot(),false);
	    	
	    	legendChart.setBackgroundPaint(Color.white);
	        CategoryPlot plot = origchart.getCategoryPlot();

	        LegendTitle legendTitle = new LegendTitle(plot, 
	                new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0), 
	                new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0)); 
	        legendChart.addLegend(legendTitle); 
	        
	        return legendChart;
	        
	    }
	    
       
   public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
			dataTable.doLayout();

			System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
		}
	
	public Container getDisplayPane() {
		return this.getContentPane();
	}
	
/**
 * reset dataTable to default (demo data), and refesh chart
 */
	  public void resetExample() {

  	    dataset= createDataset(true);	
  		
  		JFreeChart chart = createChart(dataset);	
  		chartPanel = new ChartPanel(chart, false); 
  		setChart();

  		hasExample = true;
  		convertor.valueList2Table_vertical(values_storage, SERIES_COUNT, CATEGORY_COUNT);				
  		JTable tempDataTable = convertor.getTable();
  		//		resetTable();
  		resetTableRows(tempDataTable.getRowCount()+1);
  		resetTableColumns(tempDataTable.getColumnCount());
  				

          for(int i=0;i<tempDataTable.getColumnCount();i++) {
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
  		dataTable.doLayout();
  		// this is a fix for the BAD SGI Java VM - not up to date as of dec. 22, 2003
  		try { 
  			dataTable.setDragEnabled(true);  
  		} catch (Exception e) {
  		}

          dataPanel.validate();

  		// do the mapping
  		//addButtonDependent();
          setMapping();
  	
  		
  		updateStatus(url);
    }
  
public void setMapping(){
	int columnCount =dataTable.getColumnCount();
		for(int i=0; i<columnCount; i++){
			addButtonDependent();
		}
}

public void setDataTable (String input){
	  hasExample= true;
	  StringTokenizer lnTkns = new StringTokenizer(input,"#");
	  String line;
	  int lineCt = lnTkns.countTokens();
	 
	  int r = 0;
	  while(lnTkns.hasMoreTokens()) {
		  line = lnTkns.nextToken();
	     		
  //	String tb[] =line.split("\t");
       StringTokenizer cellTkns = new StringTokenizer(line, ",");// IE use "space" Mac use tab as cell separator
       int cellCnt = cellTkns.countTokens();
       String tb[]= new String[cellCnt];
       int r1=0;
         while(cellTkns.hasMoreTokens()) {
          tb[r1]=cellTkns.nextToken();
          r1++;
         }
       //System.out.println("tb.length="+tb.length);
   	int colCt=tb.length;
   	//vertical
    resetTableRows(colCt);
   	resetTableColumns(lineCt);
   	
       for (int i=0; i<tb.length; i++){
       	//System.out.println(tb[i]);
       	if (tb[i].length()==0)
       		tb[i]="0";
       	dataTable.setValueAt(tb[i], i,r);  //vertical
       }           
   	r++;
   }
   
   // this will update the mapping panel     
   resetTableColumns(dataTable.getColumnCount());
}
  
public void setXLabel(String xLabel){
	
  	domainLabel = xLabel;
  	TableColumnModel columnModel = dataTable.getColumnModel();
  	
  	for (int i=0; i<columnModel.getColumnCount(); i++)
  		columnModel.getColumn(i).setHeaderValue("serie"+i);
  
  	dataTable.setTableHeader(new EditableHeader(columnModel));
  }
  
  public void setYLabel(String yLabel){
  	rangeLabel = yLabel;
  	   		
		
  }
  
	/*protected void setSummary(CategoryDataset ds){
		summaryPanel.removeAll();
		summaryPanel.setText("Summary:\n");
		
		Summary s = new Summary(ds);
		int seriesCount = s.getSeriesCount();
		
		for (int i=0; i<seriesCount; i++)
			summaryPanel.append(s.getSerieName(i)+": " +s.getSummary(i));
			}*/

}
