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
import java.util.List;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategoryCellLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  *A simple demonstration application showing how to create a BoxAndWhisker chart using Category dataset.
 */
public class SuperBoxAndWhiskerChart_Vertical extends SuperBoxAndWhiskerChart implements PropertyChangeListener {
	protected BoxAndWhiskerCategoryDataset dataset;

	protected static final int CATEGORY_COUNT=1;
    
		
	public void init(){
		mapIndep= false;
		depLabel = new JLabel("Series");
		indLabel = new JLabel("Categories");

		super.init();
		depMax = 100; // max number of dependent var
		indMax = 1; // max number of independent var

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
		 showMessageDialog("SuperBoxAndWhiskerChart doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getBoxAndWhiskerCategoryChart("Category Chart", "Category", "value", dataTable, no_category, pairs,"");	
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
		if (dependentIndex < 0 ) {
			showMessageDialog(VARIABLE_MISSING_MESSAGE);
			resetChart();
			return;
			}

		 dataset = createDataset(false);	 // not a demo, so get data from the table
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		
		setChart();
		//updateStatus("Chart has been updated, click GRAPH to view it.");
	}

 
    /**
     * 
     * @param isDemo data come from demo(true) or dataTable(false)
     * @return
     */
    protected  BoxAndWhiskerCategoryDataset createDataset(boolean isDemo) {

		if (isDemo){
				 SERIES_COUNT = 3;
				// CATEGORY_COUNT = 5;
				 VALUE_COUNT = 20;

				 values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];

				 DefaultBoxAndWhiskerCategoryDataset result 
					 = new DefaultBoxAndWhiskerCategoryDataset();
				 for (int s = 0; s < SERIES_COUNT; s++) {
					 for (int c = 0; c < CATEGORY_COUNT; c++) {
						 List values = createValueList(0, 20.0, VALUE_COUNT);
						 values_storage[s][c]= vs;
						 result.add(values, "Series " + s, "Category " + c);
					 }
				 }
				 return result;}
	
		else{
			
			setArrayFromTable();
			
			String[][] x= new String[xyLength][dependentVarLength];
			String[][] y= new String[xyLength][independentVarLength];

			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++)
				   x[i][index] = depValues[i][index];


			// create the dataset... 
			DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset(); 
			SERIES_COUNT = dependentVarLength;
			//CATEGORY_COUNT = independentVarLength;
			
			values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
			 
			for (int s=0; s<SERIES_COUNT; s++)
				for (int c=0; c<CATEGORY_COUNT; c++){
					String v= "" ;
	            	for (int i = 0; i<xyLength; i++)
	            		if(x[i][s]!=null && x[i][s].length()!=0)
	            			v= v+x[i][s]+"," ;
	            	
	            	values_storage[s][c]= v;
					dataset.add(createValueList(v), dependentHeaders[s], "Category " + c); 
					
				}
			
            return dataset; 
		}
	}	
	
    /**
	 * 
	 * @param lowerBound
	 * @param upperBound
	 * @param count
	 * @return
	 */
   protected List<Double> createValueList(
		   double lowerBound, double upperBound, int count) {
	   vs = "";
	   DecimalFormat f=	new DecimalFormat("#0.00");

	   List<Double> result = new java.util.ArrayList<Double>();
        for (int i = 0; i < count; i++) {
            double v = lowerBound + (Math.random() * (upperBound - lowerBound));
            result.add(new Double(v));  
			if (vs=="")
				vs += f.format(v);
			else vs += " , "+f.format(v);
        }
        return result;
    }

   /**
    * 
    * @param in
    * @return
    */
   protected List<Double> createValueList(String in){
	   vs = in;
	   //   String[] values = in.split("( *)+,+( *)");
	   //	   int count = java.lang.reflect.Array.getLength(values);

	   StringTokenizer st = new StringTokenizer(in,DELIMITERS);
	   int count = st.countTokens();
	   String[] values = new String[count];
	   for (int i=0; i<count; i++)
		   values[i]=st.nextToken();

	 //  System.out.println("count="+count);
        List<Double> result = new java.util.ArrayList<Double>();
		try{
			for (int i = 0; i < count; i++) {
				//System.out.println("values["+i+"]=*"+values[i]+"*");
				double v;
				if (values[i]!=null && values[i].length()!=0 && !values[i].equals("null")){
					 v = Double.parseDouble(values[i]);
					 result.add(new Double(v));   
				}
			}
		}catch(NumberFormatException e){
			showMessageDialog("Data format error!");
			return null;
		}
        return result;
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(BoxAndWhiskerCategoryDataset dataset) {
          // create the chart...
          
        CategoryAxis domainAxis = new CategoryAxis(null);
        NumberAxis rangeAxis = new NumberAxis("Value");
        CategoryItemRenderer renderer = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(
            dataset, domainAxis, rangeAxis, renderer
        );
        JFreeChart chart = new JFreeChart(chartTitle, plot);
    	
        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

         return chart;
  
	}	
       
    protected JFreeChart createLegend(BoxAndWhiskerCategoryDataset dataset) {
        
    	 CategoryAxis domainAxis = new CategoryAxis(null);
         NumberAxis rangeAxis = new NumberAxis("Value");
         BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
         CategoryPlot plot = new CategoryPlot(
             dataset, domainAxis, rangeAxis, renderer
         );
         JFreeChart chart = new JFreeChart(chartTitle, plot);
         
   
 		renderer.setLegendItemLabelGenerator(new SOCRCategoryCellLabelGenerator(dataset, values_storage,SERIES_COUNT, CATEGORY_COUNT));

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
	 *  reset dataTable to default (demo data), and refesh chart
	 */
  public void resetExample() {

	   dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;
		convertor.valueList2Table_vertical (values_storage, SERIES_COUNT, CATEGORY_COUNT);				
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
}

