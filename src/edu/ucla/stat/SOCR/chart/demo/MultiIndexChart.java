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
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperMultiIndexChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategoryCellLabelGenerator;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.  
 * 
 */
public class  MultiIndexChart extends SuperMultiIndexChart implements PropertyChangeListener {
	
	public void init(){

		LEGEND_SWITCH= false;
		super.init();
		
	}
     /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected XYDataset createDataset1(boolean isDemo) {
        if (isDemo){
        	row_count = 11;
        	raw_x2 = new String[2][row_count];      
        	/*raw_x2[0][0] = "1.0"; 	raw_x2[0][1] = "2.0"; 	raw_x2[0][2] = "3.0"; 	raw_x2[0][3] = "4.0"; 	raw_x2[0][4] = "5.0";
        	raw_x2[0][5] = "6.0"; 	raw_x2[0][6] = "76.0"; 	raw_x2[0][7] = "78.0"; 	raw_x2[0][8] = "7.0"; 	raw_x2[0][9] = "8.0";
         
        	raw_x2[1][0] = "1.0"; 	raw_x2[1][1] = "3.0"; 	raw_x2[1][2] = "5.0"; 	raw_x2[1][3] = "9.0"; 	raw_x2[1][4] = "7.0";
        	raw_x2[1][5] = "4.0"; 	raw_x2[1][6] = "70.0"; 	raw_x2[1][7] = "30.0"; 	raw_x2[1][8] = "9.0"; 	raw_x2[1][9] = "7.0";raw_x2[1][10] = "20.0";
        	*/
        XYSeries series1 = new XYSeries("Data 1");
        series1.add(1, 1.0);
        series1.add(2, 2.0);
        series1.add(3, 3.0);
        series1.add(4, 4.0);
        series1.add(5, 5.0);
        series1.add(6, 6.0);
        series1.add(7, 76.0);
        series1.add(8, 78.0);
        series1.add(9, 7.0);
        series1.add(10, 8.0);
        for (int i=0; i<10; i++)
        	raw_x2[0][i]=series1.getY(i).toString();
        
        XYSeries series2 = new XYSeries("Data 2");
        series2.add(1, 1.0);
        series2.add(2, 3.0);
        series2.add(3, 5.0);
        series2.add(4, 9.0);
        series2.add(5, 7.0);
        series2.add(6, 4.0);
        series2.add(7, 70.0);
        series2.add(8, 30.0);
        series2.add(9, 9.0);
        series2.add(10, 7.0);
        series2.add(11, 20.0);  
        for (int i=0; i<11; i++)
        	raw_x2[1][i]=series2.getY(i).toString();
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        
        dataset.addSeries(series2);
                
        return dataset;}
		else return super.createDataset(false);
        
    }
    
   
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        
      //   create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle,      // chart title
            domainLabel,                      // x axis label 
            rangeLabel,                      // y axis label  
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
     
        plot.setBackgroundPaint(Color.white);
	    plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
      //  renderer.setSeriesShape(0, java.awt.Shape.round);
        renderer.setBaseShapesVisible(false);
        renderer.setBaseShapesFilled(false);
        renderer.setBaseLinesVisible(true);
       
		renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

        //change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.05);
        rangeAxis.setLowerMargin(0.05);
        

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setAutoRangeIncludesZero(false);     
       //  domainAxis.setTickLabelsVisible(false);
       //  domainAxis.setTickMarksVisible(false);      
        domainAxis.setUpperMargin(0.05);
        domainAxis.setLowerMargin(0.05);

        
        // OPTIONAL CUSTOMISATION COMPLETED.
	    setYSummary(dataset);     
	 
	    return chart;
    }
 
    
    public void resetExample() {
      //  System.out.println("resetExample get called");
 	   XYDataset dataset= createDataset1(true);	
		
	   JFreeChart chart = createChart(dataset);	
 	   chartPanel = new ChartPanel(chart, false); 
	   setChart();

       hasExample = true;
   
    //System.out.println("independentVarLength="+independentVarLength);
   //  System.out.println("raw+x="+raw_x[0]);
       int colCount= dataset.getSeriesCount();
        String[] columnName= new String[colCount];
        for (int i=0; i<colCount; i++)
        	columnName[i]=dataset.getSeriesKey(i).toString();
        
		convertor.multiY2Table(raw_x2, row_count, colCount, columnName);
		//convertor.dataset2Table(dataset);				
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
				//System.out.println("setTabledata "+tempDataTable.getValueAt(i,j));
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
        for (int i=0; i<colCount; i++)
        	addButtonIndependent();//Y

        rangeLabel="";
		for (int j =0; j<colCount; j++)
			rangeLabel+=columnName[j]+"/";
		rangeLabel =  rangeLabel.substring(0,  rangeLabel.length()-1);

		updateStatus(url);
}
    

}
