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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.lang.NumberFormatException;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.ucla.stat.SOCR.util.EditableHeader;
import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.gui.SOCROptionPane;


/**
 * A simple demonstration application showing how to create a Bar chart using 
 * data from a {@link IntervalXYDataset}.
 */
public class SuperIntervalXYChart extends Chart implements PropertyChangeListener {

	protected String[] raw_y;
	protected int data_count;
	
	public void init(){

		if (indLabel==null)
			indLabel = new JLabel("X");
		if (depLabel==null)
			depLabel = new JLabel("Y");

		super.init();
		depMax = 50; // max number of dependent var
		indMax = 50; // max number of independent var

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
		 showMessageDialog("SuperIntervalXYChart doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = i*2;    //column 1 stores value
			 pairs[i][1] = i*2+1;    //column 0 stores time
		 }
		 chart = chartMaker.getXYChart("Bar","Category Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	/**
	 *  create chart using data from the dataTable
	 */
	public void doChart(){

		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			SOCROptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}

		isDemo = false;
		IntervalXYDataset dataset = createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();
	}

	  
	  /**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */  
    protected IntervalXYDataset createDataset(boolean isDemo) {
		if(isDemo)	{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else {
			setArrayFromTable();

			String[][] x = new String[xyLength][independentVarLength] ;
			double[][] y = new double[xyLength][dependentVarLength];
			int[][] skipy = new int[xyLength][dependentVarLength];

			for (int index=0; index<independentVarLength; index++){
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
			}
		   
		//	System.out.println("superIntervalXYChart domainLabel="+domainLabel);
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					 if(depValues[i][index]==null ||depValues[i][index]=="null"||depValues[i][index]=="NaN"||depValues[i][index].length()==0)
						 skipy[i][index] = 1; //skip it
					 else 	y[i][index] = Double.parseDouble(depValues[i][index]);

			
			//TimeSeriesCollection collection = new TimeSeriesCollection();
		//	TimeSeries series;
			//SimpleDateFormat df = new SimpleDateFormat();

			// create the dataset... 
			XYSeriesCollection dataset = new XYSeriesCollection(); 
			XYSeries series;	

			for (int ind =0; ind<independentVarLength; ind++){

				int start_ind   = independentHeaders[ind].lastIndexOf(":");
				if 	(start_ind< 0)
					start_ind =0;
				int start_dep   = dependentHeaders[ind].lastIndexOf(":");
				if 	(start_dep< 0)
					start_dep =0;

				String serieName = independentHeaders[ind].substring(0, start_ind);
				if (serieName.length()==0)
					serieName="Serie"+ind;
				if (start_ind>0)
					domainLabel = independentHeaders[ind].substring(0, start_ind);
				else domainLabel = independentHeaders[ind];
				
				if (start_dep>0)
					rangeLabel = dependentHeaders[ind].substring(0, start_dep);
				else rangeLabel = dependentHeaders[ind];
				//				series = new TimeSeries(serieName,indName,depName, Year.class);
				//series = new TimeSeries(serieName,Year.class);
				series = new XYSeries(serieName);
				//TimeSeries("Executions", "Year", "Count", Year.class);
				
				try{
					for (int i=0; i<xyLength; i++)
						if (x[i][ind]!=null && skipy[i][ind]!=1){
							series.add(Double.parseDouble(x[i][ind]), y[i][ind]);
						}
				}catch(NumberFormatException e){
					SOCROptionPane.showMessageDialog(this, "Wrong data format, enter integer for Year please. Check the Mapping also.");
					return null;
				}
				//System.out.println("adding:"+serieName);
				//collection.setDomainIsPointsInTime(false);
				//collection.addSeries(series);
				dataset.addSeries(series);
			}
			return dataset;	
		}
	} 

    /**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */  
  protected IntervalXYDataset createDatasetForSingleColumn(boolean isDemo) {
		if(isDemo)	{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else {
			trimColumn = true;
			
			setArrayFromTable();
				
		//	domainLabel = dependentHeaders[0];
			
			double[] x_start = new double[xyLength];
			double[] x_end = new double[xyLength];
			double[] y = new double[xyLength];
		
			data_count = xyLength;
			raw_y = new String[data_count];
			
			for (int i = 0; i < xyLength; i++) {
					x_start[i] = i+1;
					x_end[i]=x_start[i]+1;
			}
					
			for (int i = 0; i < xyLength; i++) 
			{
				if (depValues[i][0]==null || depValues[i][0].length()==0)
					y[i]=0.0;
				else {
					y[i] = Double.parseDouble(depValues[i][0]);
					raw_y[i] = depValues[i][0];
				}
				
				
			}

			IntervalXYDataset dataset = new SimpleIntervalXYDataset(data_count, x_start, x_end, y);
			// create the dataset... 
			
			return dataset;	
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
  	TableColumnModel columnModel = dataTable.getColumnModel();
  	for (int i=0; i<columnModel.getColumnCount()/2; i++)
  		columnModel.getColumn(2*i).setHeaderValue(xLabel+":"+i);
		dataTable.setTableHeader(new EditableHeader(columnModel));
  }
  
  public void setYLabel(String yLabel){
  	rangeLabel = yLabel;
  	TableColumnModel columnModel = dataTable.getColumnModel();
  	for (int i=0; i<columnModel.getColumnCount()/2; i++)
  		columnModel.getColumn(2*i+1).setHeaderValue(yLabel+":"+i);
		dataTable.setTableHeader(new EditableHeader(columnModel));    		
		
  }
  
  // this will update the mapping panel    
  public void setMapping(){

      int seriesCount = dataTable.getColumnCount()/2;
      for (int i=0; i<seriesCount; i++){
			addButtonIndependent();
			addButtonDependent();
		}
  }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
  protected  JFreeChart createChart(IntervalXYDataset dataset) {
      JFreeChart chart = ChartFactory.createXYBarChart(
          chartTitle,
          domainLabel,
          false,
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

      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	     
      NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
      domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	     
      renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
      return chart;
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
	
}
