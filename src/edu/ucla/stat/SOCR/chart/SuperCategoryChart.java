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
import java.awt.GradientPaint;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a bar chart using category dataset.
 */
public class SuperCategoryChart extends Chart implements PropertyChangeListener {
	 protected CategoryDataset dataset;
	 
	public void init(){
		if (depLabel==null )
			depLabel = new JLabel("Series");
		if (indLabel==null )
		 indLabel = new JLabel("Categories");

		super.init();
		depMax = 1; // max number of dependent var
		indMax = 100; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
	}
	
	protected JFreeChart createLegend(CategoryDataset dataset) {
	        
		//  JFreeChart chart = ChartFactory.createAreaChart(
	        JFreeChart chart = ChartFactory.createLineChart(
	            chartTitle,             // chart title
	            domainLabel, // "Category",               // domain axis label
	            rangeLabel, //"Value",                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        CategoryPlot plot = chart.getCategoryPlot();

	        LineAndShapeRenderer renderer 
            = (LineAndShapeRenderer) plot.getRenderer();
	        renderer.setBaseShapesVisible(true);
	       // renderer.setDrawOutlines(true);
	       // renderer.setUseFillPaint(true);
	       // renderer.setFillPaint(Color.white);
	        renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
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
	   
	  protected  void setChart(){
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
	    		//legendPanel.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));
	    		graphPanel2.add(chartPanel);
	    		if (legendPanelOn) {
	    			legendPane = new JScrollPane(legendPanel);
	            	legendPane.setPreferredSize(new Dimension(CHART_SIZE_X*2/3,CHART_SIZE_Y*2/5));           	
	        		graphPanel2.add(legendPane);
	    		}
	    		graphPanel2.validate();	
	    		summaryPanel.validate();
	    	}
	    }
	    
	  
	/**
	 *  sample code for generating chart using ChartGenerator_JTable 
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("SuperCategoryChart doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
 			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("Bar", "Category Chart", "Category", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
		 
		 dataset = createDataset(false);		
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
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
		}
		
		isDemo=false;
		CategoryDataset dataset = createDataset(isDemo);	 // not a demo, so get data from the table
		
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

		if (isDemo){
			updateStatus("isDemo=true in "+this.getClass().getName()+" class! return null Dataset, check the code!");
			return null;
   		}
		else{

			setArrayFromTable();		
			
			double[][] x = new double[xyLength][independentVarLength];
			String[][] y = new String[xyLength][dependentVarLength];
			boolean[][] skipFlag = new boolean[xyLength][independentVarLength];
			try{
				for (int index=0; index<independentVarLength; index++)
					for (int i = 0; i < xyLength; i++) {
						//System.out.println("indepValues[i][index]=["+indepValues[i][index]+"]");
						if (indepValues[i][index]==null || indepValues[i][index]=="NaN"||indepValues[i][index]=="null" ||indepValues[i][index].length()==0){
							x[i][index] = 0.0;
							skipFlag[i][index]=true;  //skip empty value
						}
						else {
							x[i][index] = Double.parseDouble(indepValues[i][index]);
							skipFlag[i][index]=false;
						}
					}
		   
				for (int index=0; index<dependentVarLength; index++)
					for (int i = 0; i < xyLength; i++) 
						y[i][index] = depValues[i][index];

				// create the dataset... 
				dataset = new DefaultCategoryDataset(); 

				//dependent 
				for (int j=0; j<xyLength; j++)
					for (int i=0; i<independentVarLength; i++){
						String serieName = independentHeaders[i]; 
						if (serieName.indexOf(':')!=-1)
							serieName = independentHeaders[i].substring(0, serieName.indexOf(':'));
						//System.out.println("serieName="+serieName);
						if(!skipFlag[j][i]){
							((DefaultCategoryDataset) dataset).addValue(x[j][i], y[j][0],serieName ); 
							//System.out.println("adding :("+x[j][i]+","+y[j][0]+","+independentHeaders[i]+")" );
						}
						else {
							//System.out.println("skip " +j+","+i);
						}
						
				}
				rangeLabel= (String)dataset.getRowKey(0);
		    	domainLabel = "Category";
				//System.out.println("rangeLabel="+rangeLabel);
            return dataset; 
			}catch(NumberFormatException e)
				{
					showMessageDialog("Data format error!");
					return null;}
			
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
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle, //"Bar Chart Demo",         // chart title
            domainLabel,//"Category",               // domain axis label
            rangeLabel, //"Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            !legendPanelOn,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, new Color(0, 0, 64)
        );
        GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, new Color(0, 64, 0)
        );
        GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, new Color(64, 0, 0)
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
		
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
		convertor.dataset2Table(dataset);				
		JTable tempDataTable = convertor.getTable();
		resetTableRows(tempDataTable.getRowCount()+1);
		resetTableColumns(tempDataTable.getColumnCount()+1);
				
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
  	
  	for (int i=1; i<columnModel.getColumnCount(); i++)
  		columnModel.getColumn(i).setHeaderValue(domainLabel+i);
  
  	dataTable.setTableHeader(new EditableHeader(columnModel));
  }
  
  public void setYLabel(String yLabel){
  	rangeLabel = yLabel;
  	TableColumnModel columnModel = dataTable.getColumnModel();
  	columnModel.getColumn(0).setHeaderValue("Series");	
  }
  
  // this will update the mapping panel    
  public void setMapping(){

	  addButtonDependent();
		int columnCount = dataset.getColumnCount();
		for(int i=0; i<columnCount; i++)
			addButtonIndependent();
  }
	
}
