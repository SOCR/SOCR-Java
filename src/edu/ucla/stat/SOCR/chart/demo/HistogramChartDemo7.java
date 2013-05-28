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
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A sample waterfall chart.
 */
public class HistogramChartDemo7 extends SuperCategoryChart_Bar implements PropertyChangeListener {
	protected String[] raw_x;
	protected double[] raw_y;
	
	public void init(){

		indLabel = new JLabel("X");
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
		
		
	 }
    /**
     * Creates a sample dataset for the demo.
     * 
     * @return A sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
			domainLabel="TailLength";
			rangeLabel="NumberDeermice";
			
			raw_x = new String[9];	
			raw_y = new double[9];
			raw_y[0]= 1;raw_y[1]= 3;raw_y[2]= 11; raw_y[3]= 18;raw_y[4]= 21;
			raw_y[5]= 20;raw_y[6]= 9;raw_y[7]= 2; raw_y[8]= 1;
		

			raw_x[0]= "52-53";raw_x[1]= "54-55";raw_x[2]= "56-57"; raw_x[3]= "58-59";raw_x[4]= "60-61";
			raw_x[5]= "62-63";raw_x[6]= "64-65";raw_x[7]= "66-67"; raw_x[8]= "68-69";
			xyLength=9;
			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();      
        
			for (int j=0; j<xyLength; j++)
					((DefaultCategoryDataset)dataset).addValue(raw_y[j],"TailLength" , raw_x[j]); 

        return dataset;}
		else {
			 
			 	trimColumn = true;
				setArrayFromTable();
				
				raw_x = new String[xyLength] ;//independentVarLength=1
				raw_y = new double[xyLength];//dependentVarLength=1
							
				for (int i = 0; i < xyLength; i++) 
					raw_x[i] = indepValues[i][0];
								   
								
				for (int i = 0; i < xyLength; i++) 
					if (depValues[i][0]!=null && depValues[i][0]!="null"&&depValues[i][0].length()!=0) 
						raw_y[i] = Double.parseDouble(depValues[i][0]);
					else raw_y[i]= 0.0;
				
				domainLabel = independentHeaders[0];
				rangeLabel = dependentHeaders[0];								
				dataset = new DefaultCategoryDataset(); 
									
				for (int j=0; j<xyLength; j++)
					for (int i=0; i<independentVarLength; i++){
						((DefaultCategoryDataset)dataset).addValue(raw_y[j], independentHeaders[i], raw_x[j]); 
						//System.out.println("adding :("+x[j][i]+","+y[j][0]+","+independentHeaders[i]+")" );
					}
			
				return dataset;
			}
    }
    
    /**
     * Returns the chart.
     * 
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
        
        JFreeChart chart = ChartFactory.createBarChart(
        	chartTitle,
            domainLabel,
            rangeLabel,
            dataset,
            PlotOrientation.VERTICAL,
            false, //!legendPanelOn,
            true,
            false
        );
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        
        ValueAxis rangeAxis = plot.getRangeAxis();

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

        DecimalFormat labelFormatter = new DecimalFormat("##,###.##");
        labelFormatter.setNegativePrefix("(");
        labelFormatter.setNegativeSuffix(")");
        renderer.setBaseItemLabelGenerator(
            new StandardCategoryItemLabelGenerator("{2}", labelFormatter)
        );
        renderer.setBaseItemLabelsVisible(true);

		//setCategorySummary(dataset);
        return chart;
    }
    
    public void resetExample() {
	    isDemo = true;
	    dataset= createDataset(isDemo);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 
		setChart();

        hasExample = true;

		//convertor.dataset2Table(dataset);	
        convertor.data2Table(raw_x, raw_y, domainLabel, rangeLabel, xyLength);	
        
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
    
    public void setXLabel(String xLabel){
		
      	domainLabel = xLabel;
      	TableColumnModel columnModel = dataTable.getColumnModel();
      	columnModel.getColumn(0).setHeaderValue(xLabel);
      	dataTable.setTableHeader(new EditableHeader(columnModel));
      }
      
      public void setYLabel(String yLabel){
      	rangeLabel = yLabel;
      	TableColumnModel columnModel = dataTable.getColumnModel();     		
      	columnModel.getColumn(1).setHeaderValue(yLabel);
      //	System.out.println("histogram7 setting yLabel="+yLabel);
      	dataTable.setTableHeader(new EditableHeader(columnModel));    	   		
      }
      
      
      public void setMapping(){
    	//  System.out.println("Histogram 7 mapping get called");
    	  addButtonIndependent();
    	  addButtonDependent();
      }
      
}
