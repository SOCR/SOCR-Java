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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperBoxAndWhiskerChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategoryCellLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a box-and-whisker 
 * chart.
 */
public class BoxAndWhiskerChartDemo2 extends SuperBoxAndWhiskerChart implements PropertyChangeListener {
	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
	
	protected String[] raw_y;
	protected int data_count;
	
	
	public void init(){

	//	depLabel = new JLabel("Series");
		indLabel = new JLabel("Categories");

		mapDep=false;
		
		super.init();
		depMax = 1; // max number of dependent var
		indMax = 10; // max number of independent var

		updateStatus(url);	
	  
		resetExample();
		validate();
		
	}
	
	
	public void doChart(){

		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			showMessageDialog(DATA_MISSING_MESSAGE);
			resetChart();
			return;
		}
		if (independentIndex < 0 || independentLength == 0) {
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
  
    
    protected  BoxAndWhiskerCategoryDataset createDataset(boolean isDemo) {

		if (isDemo){
			 	SERIES_COUNT = 1;
		        CATEGORY_COUNT = 1;
		        VALUE_COUNT = 10;
				values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
		   

		        DefaultBoxAndWhiskerCategoryDataset result 
		            = new DefaultBoxAndWhiskerCategoryDataset();

		       
		        List values = createValueList(0, 20.0, VALUE_COUNT);
		        values_storage[0][0]= vs;
		        result.add(values, "", "Data");
		      
		        
		        raw_y = new String[VALUE_COUNT];
		        StringTokenizer st = new StringTokenizer(vs,DELIMITERS);
		        data_count = st.countTokens();
		    
		        	for (int i=0; i<data_count; i++){
		 		   raw_y[i]=st.nextToken();
		        	}
		        return result;}
	
		else{
			
			setArrayFromTable();

			int row_count = xyLength;
			//System.out.println("row_count="+row_count);
			raw_y= new String[row_count];

			data_count=0;
			String v_list= new  String();
			independentVarLength = 1;
			
			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) {
					raw_y[i] = indepValues[i][index];
					try{
						Double.parseDouble(raw_y[i]);
						data_count++;
						v_list+=raw_y[i]+",";
					}catch(Exception e ){
						System.out.println("Skipping "+raw_y[i]);
						}
					
				}

			// create the dataset... 
			DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset(); 
			SERIES_COUNT = 1;
			CATEGORY_COUNT = 1;
			values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
			   	
			
			dataset.add(createValueList(v_list), "", independentHeaders[0]); 
			values_storage[0][0]=vs;
			
            return dataset; 
		}
	}	
	
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(BoxAndWhiskerCategoryDataset dataset) {
        
        CategoryAxis domainAxis = new CategoryAxis(null);
        NumberAxis rangeAxis = new NumberAxis(rangeLabel);
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(
            dataset, domainAxis, rangeAxis, renderer
        );
        JFreeChart chart = new JFreeChart(chartTitle, plot);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        
        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		renderer.setLegendItemLabelGenerator(new SOCRCategoryCellLabelGenerator(dataset, values_storage,SERIES_COUNT, CATEGORY_COUNT));

		//columnCount -- category count
		//RowCount -- serie count
		

		domainAxis.setLowerMargin(0.44);
		domainAxis.setUpperMargin(0.44);
		if (dataset.getColumnCount()==1)
			renderer.setItemMargin(0.5);
			//	domainAxis.setCategoryMargin(domainAxis.getCategoryMargin()*2);
			/*		   
			System.out.println("1lowerMargin="+domainAxis.getLowerMargin());
			System.out.println("ItemMargin="+renderer.getItemMargin());
			System.out.println("CategoryMargin="+domainAxis.getCategoryMargin());*/

	   	
	   		renderer.setItemMargin(renderer.getItemMargin()*2);
			
			/*System.out.println("2lowerMargin="+domainAxis.getLowerMargin());
			System.out.println("ItemMargin="+renderer.getItemMargin());
			System.out.println("CategoryMargin="+domainAxis.getCategoryMargin());*/
		
		
	
        return chart;
        
    }
    
    public void resetExample() {

	    BoxAndWhiskerCategoryDataset dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();
		
        hasExample = true;
        
        convertor.Y2Table(raw_y, data_count);			
		JTable tempDataTable = convertor.getTable();
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
      	addButtonIndependent();//Y
		updateStatus(url);
  }
    public void setMapping(){
    	
    	addButtonIndependent();
    		
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
