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
import java.lang.NumberFormatException;
import java.util.StringTokenizer;

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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a bar chart.
 */
public class SuperXYChart extends Chart implements PropertyChangeListener {
	protected XYDataset dataset;
	
	public void init(){

		indLabel = new JLabel("X");
		depLabel = new JLabel("Y");

		super.init();
		depMax = 50; // max number of dependent var
		indMax = 50; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
	}
	
	/**
	 *  sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperXYChart doTest get called!");
		
		 int no_series = (dataTable.getColumnCount()-2)/2;		 
		 int[][] pairs = new int[no_series][2];
		 for (int i=0; i<no_series; i++){
			 pairs[i][0] = 2*i;    //column x
			 pairs[i][1] = 2*i+1;    //column y
		 }
		 chart = chartMaker.getXYChart("Line","Line Chart", "X", "Y", dataTable, no_series, pairs,"");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
	 protected JFreeChart createLegend(XYDataset dataset) {
	        
	        JFreeChart chart = ChartFactory.createXYLineChart(
	            chartTitle,             // chart title
	            domainLabel,               // domain axis label
	            rangeLabel,                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // url
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        XYPlot plot = (XYPlot) chart.getPlot();

	        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
	        renderer.setBaseShapesVisible(true);
	        renderer.setBaseShapesFilled(true);
	        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
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
	    
	    protected void setChart(){
	    	// update graph
	    //	System.out.println("setChart called");
		
	    	graphPanel.removeAll();
	    	graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

	    	chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y));

	    	if (legendPanelOn){   	
	    		JFreeChart chart2 = createLegendChart(createLegend(dataset));
	    		legendPanel = new ChartPanel(chart2,false);
		    	//legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y*2/3));
	    	}
	    		
	    	graphPanel.add(chartPanel);
	    	JScrollPane	 legendPane = new JScrollPane(legendPanel);
	    	if (legendPanelOn){
	    		legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/5));
	    		graphPanel.add(legendPane);
	    	}
	    	
	    	graphPanel.validate();

	    	// get the GRAPH panel to the front
	    	if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex())!=ALL) {
	    		tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
	    		graphPanel.removeAll();
	    		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
				
				graphPanel.add(chartPanel);
				
		    	if (legendPanelOn){
		    		legendPane = new JScrollPane(legendPanel);
		    		legendPane.setPreferredSize(new Dimension(CHART_SIZE_X,CHART_SIZE_Y/5));
		    		graphPanel.add(legendPane);
		    	}
		    	graphPanel.validate();
	    	}
	    	else {
	    		graphPanel2.removeAll();
	    		chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/3));
	    		//legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/5,CHART_SIZE_Y*2/5));
	    		graphPanel2.add(chartPanel);
	    		if (legendPanelOn) {
	    			legendPane = new JScrollPane(legendPanel);
	            	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X*2/5,CHART_SIZE_Y*2/5));           	
	        		graphPanel2.add(legendPane);
	    		}
	    		graphPanel2.validate();	
	    		summaryPanel.validate();
	    	}
	    }
	    
	  
	public void doChart(){

		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}
		
		isDemo =false;
		dataset = createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, isDemo); 
		setChart();
	}

   
	 /**
	   * 
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */   
    protected  XYDataset createDataset(boolean isDemo) {
		if (isDemo)	{
				updateStatus("isDemo==true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null; }
		else{
			
			setArrayFromTable();

			if (independentVarLength != dependentVarLength){
			showMessageDialog("The number of X and Y doesn't match!");
			resetChart();
			return null;
		}

			String[][] x= new String[xyLength][independentVarLength];
			double[][] y= new double[xyLength][dependentVarLength];
			int [][] skipy = new int[xyLength][dependentVarLength];

			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			try{
				for (int index=0; index<dependentVarLength; index++)
					for (int i = 0; i < xyLength; i++) 
						if (depValues[i][index]!=null && depValues[i][index]!="null" && depValues[i][index].length()!=0) 
							y[i][index] = Double.parseDouble(depValues[i][index]);
						else skipy[i][index]= 1;
			}catch(NumberFormatException e)
				{
					showMessageDialog("dependent Data format error!");
					return null;}

			// create the dataset... 
			XYSeriesCollection dataset = new XYSeriesCollection(); 
			XYSeries series;	

			//dependent 
			try{
			for (int i=0; i<independentVarLength; i++){
				String serieName ;
				if (independentHeaders[i].lastIndexOf(":")!=-1)
					serieName= independentHeaders[i].substring(0, independentHeaders[i].lastIndexOf(":"));
				else serieName= independentHeaders[i];
				
				series = new XYSeries(serieName);
				for (int j=0; j<xyLength; j++){
					if (x[j][i]!=null && x[j][i]!="null"&&x[j][i]!="NaN"&&x[j][i].length()!=0 && skipy[j][i]!=1)
						series.add(Double.parseDouble(x[j][i]), y[j][i]);
					//else series.add(0.0, y[j][i]);
					//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
				}
				dataset.addSeries(series);
			}}catch(NumberFormatException e)
				{
					showMessageDialog("independent Data format error!");
					return null;}
		
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
    protected JFreeChart createChart(XYDataset dataset) {
         // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
  
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
		//		convertor.dataset2Table((TimeSeriesCollection)dataset);				
		convertor.dataset2Table(dataset);				
		JTable tempDataTable = convertor.getTable();

		resetTableRows(tempDataTable.getRowCount());
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
		setMapping();
		updateStatus(url);
  }

   public void setDataTable (String input){
		  hasExample= true;
		  StringTokenizer lnTkns = new StringTokenizer(input,"#");
		  String line;
		  int colCt = lnTkns.countTokens();
		  resetTableColumns(colCt*2);
		  int r = 0;
		  while(lnTkns.hasMoreTokens()) {
			  line = lnTkns.nextToken();
		     		
	    //	String tb[] =line.split("\t");
	         StringTokenizer pairTkns = new StringTokenizer(line, ";");// IE use "space" Mac use tab as cell separator
	         int cellCnt = pairTkns.countTokens();
	         String tb[]= new String[cellCnt];
	         int r1=0;
	           while(pairTkns.hasMoreTokens()) {
	            tb[r1]=pairTkns.nextToken();
	            r1++;
	           }
	         //System.out.println("tb.length="+tb.length);
	     	int rowCt=tb.length;
	     	resetTableRows(rowCt);
	         for (int i=0; i<tb.length; i++){
	        	 StringTokenizer cellTkns = new StringTokenizer(tb[i], ",");
	         	dataTable.setValueAt(cellTkns.nextToken(), i,2*r);
	         	dataTable.setValueAt(cellTkns.nextToken(), i,2*r+1);
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
	  		columnModel.getColumn(2*i).setHeaderValue(domainLabel+i);
	  
	  	dataTable.setTableHeader(new EditableHeader(columnModel));
	  }
	  
	  public void setYLabel(String yLabel){
	  	rangeLabel = yLabel;
	  	TableColumnModel columnModel = dataTable.getColumnModel();
	  	for (int i=0; i<columnModel.getColumnCount()/2; i++)
	  		columnModel.getColumn(2*i+1).setHeaderValue(rangeLabel+i);
	  }
	  
	  // this will update the mapping panel    
	  public void setMapping(){

		  int columnCount = dataTable.getColumnCount();
			for(int i=0; i<columnCount/2; i++){
				addButtonIndependent();
				addButtonDependent();
			}
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
   
	/*public String getLocalAbout() {
		String about ="\n \t A scatterplot or scatter graph is a graph used in statistics to visually display and\n compare two or more sets of related quantitative, or numerical, data by displaying only finitely\n many points, each having a coordinate on a horizontal and a vertical axis.\n";

		return super.getLocalAbout()+about;
		}*/

}
