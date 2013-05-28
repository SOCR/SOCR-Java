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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.date.MonthConstants;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart;
import edu.ucla.stat.SOCR.chart.data.DateParser;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * A demo application showing how to display category data against a date axis.
 */
public class EventFrequencyDemo1 extends SuperCategoryChart implements PropertyChangeListener {
	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog("EventFrequencyDemo doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 String time_type = "Day";
		 chart = chartMaker.getCategoryChart("EventFreqTime", "Event Frequency Chart", "Category", "Date", dataTable, no_category, pairs, "Day Horizontal");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
	
    /**
     * Creates a sample chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return A sample chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle,      // title
            domainLabel,                  // domain axis label
            rangeLabel,                     // range axis label
            dataset,                     // dataset
            PlotOrientation.HORIZONTAL,  // orientation
            !legendPanelOn,                        // include legend
            true,                        // tooltips
            false                        // URLs
        );

        chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xCC));
        
        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0f);
        plot.setRangeAxis(new DateAxis("Date"));
        CategoryToolTipGenerator toolTipGenerator 
            = new StandardCategoryToolTipGenerator(
                  "", DateFormat.getDateInstance()
              );
        LineAndShapeRenderer renderer = new LineAndShapeRenderer(false, true);
        renderer.setBaseToolTipGenerator(toolTipGenerator);
        plot.setRenderer(renderer);

		// setCategorySummary(dataset); time
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

	        LineAndShapeRenderer renderer = new LineAndShapeRenderer(false, true);
	        plot.setRenderer(renderer);
	        return chart;
	        
	    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // initialise the data...
        Day d1 = new Day(12, MonthConstants.JUNE, 2002);
        Day d2 = new Day(14, MonthConstants.JUNE, 2002);
        Day d3 = new Day(15, MonthConstants.JUNE, 2002);
        Day d4 = new Day(10, MonthConstants.JULY, 2002);
        Day d5 = new Day(20, MonthConstants.JULY, 2002);
        Day d6 = new Day(22, MonthConstants.AUGUST, 2002);

        dataset.setValue(
            new Long(d1.getMiddleMillisecond()), "Series 1", "Requirement 1"
        );
        dataset.setValue(
            new Long(d1.getMiddleMillisecond()), "Series 1", "Requirement 2"
        );
        dataset.setValue(
            new Long(d2.getMiddleMillisecond()), "Series 1", "Requirement 3"
        );
        dataset.setValue(
            new Long(d3.getMiddleMillisecond()), "Series 2", "Requirement 1"
        );
        dataset.setValue(
            new Long(d4.getMiddleMillisecond()), "Series 2", "Requirement 3"
        );
        dataset.setValue(
            new Long(d5.getMiddleMillisecond()), "Series 3", "Requirement 2"
        );
        dataset.setValue(
            new Long(d6.getMiddleMillisecond()), "Series 1", "Requirement 4"
        );

        return dataset;}	
		else{
			setArrayFromTable();
			String[][] x = new String[xyLength][independentVarLength];
			String[][] y = new String[xyLength][dependentVarLength];


			for (int index=0; index<independentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					x[i][index] = indepValues[i][index];
		   
			for (int index=0; index<dependentVarLength; index++)
				for (int i = 0; i < xyLength; i++) 
					y[i][index] = depValues[i][index];

			// create the dataset... 
			final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 

			//dependent 
			for (int j=0; j<xyLength; j++)
				for (int i=0; i<independentVarLength; i++){
					//System.out.println("*"+x[j][i]+"*");
					if (x[j][i]!=null && !x[j][i].equals("NaN") && x[j][i].length()!=0 && y[j][0]!=null){
						Day day = DateParser.parseDay(x[j][i]);
						dataset.addValue(new Long(day.getMiddleMillisecond()), y[j][0], independentHeaders[i]); 
					}
				}
			
			return dataset;
		}

    }

 public void resetExample() {

	 dataset= createDataset(true);	
		
		JFreeChart chart = createChart(dataset);	
		chartPanel = new ChartPanel(chart, false); 

		//		setSummary(dataset);
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

		columnModel = dataTable.getColumnModel();		dataTable.setTableHeader(new EditableHeader(columnModel));

		DecimalFormat f = new DecimalFormat("#.#E0");
        for(int i=0;i<tempDataTable.getRowCount();i++)
            for(int j=0;j<tempDataTable.getColumnCount();j++) {
				if (j!= 0){
					try{
						long time = f.parse((String)tempDataTable.getValueAt(i,j)).longValue();
						Date date = new Date(time);
						dataTable.setValueAt(new Day(date).toString(),i,j);
					}catch(ParseException e){
						dataTable.setValueAt("NaN",i,j);}
				}
				else                 dataTable.setValueAt(tempDataTable.getValueAt(i,j),i,j);
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
		addButtonDependent();
		int columnCount = dataset.getColumnCount();
		for(int i=0; i<columnCount; i++)
			addButtonIndependent();
		
		updateStatus(url);
  }


}
