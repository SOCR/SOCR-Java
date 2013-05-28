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
import java.awt.GradientPaint;
import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a bar chart.
 */
public class BarChartDemo1 extends SuperCategoryChart_Bar implements PropertyChangeListener {
	/**
	 * sample code showing how to create a  chart using ChartGenerator_JTable class
	 */
	public void doTest(){
		 JFreeChart chart;
		 ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
		 
		 resetChart();
		 showMessageDialog(chartTitle+" doTest get called!");
		
		 int no_series = dataTable.getRowCount()-1;
		 int no_category = dataTable.getColumnCount()-2;
		 int[][] pairs = new int[no_category][2];
		 for (int i=0; i<no_category; i++){
			 pairs[i][0] = i+1;    //column i store category i
			 pairs[i][1] = 0;    //column 0 stores series name
		 }
		 chart = chartMaker.getCategoryChart("Bar", chartTitle, "Category", "value", dataTable, no_category, pairs, "");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }

    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {

        if(isDemo){
        // row keys...
        String series1 = "First";
        String series2 = "Second";
        String series3 = "Third";

        // column keys...
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);

        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);

        dataset.addValue(4.0, series3, category1);
        dataset.addValue(3.0, series3, category2);
        dataset.addValue(2.0, series3, category3);
        dataset.addValue(3.0, series3, category4);
        dataset.addValue(6.0, series3, category5);
        
        return dataset;}
		else {
			return super.createDataset(false);
		}
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
    	domainLabel = "Category";
		rangeLabel = "Value";
        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle,         // chart title
            domainLabel,               // domain axis label
            rangeLabel,                  // range axis label
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
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
		/*Summary s = new Summary(dataset);



        LegendTitle legend = new LegendTitle(chart.getPlot());
        
        BlockContainer wrapper = new BlockContainer(new BorderArrangement());
        wrapper.setBorder(new BlockBorder(1.0, 1.0, 1.0, 1.0));

		LegendItemSource[] legendSource= legend.getSources();
		LegendItemCollection old_legendItems = legendSource[0].getLegendItems();
		LegendItemCollection new_legendItems = new LegendItemCollection();

		int legendCount  = old_legendItems.getItemCount();
		for (int i=0; i<legendCount; i++){
			LegendItem old_legendItem = old_legendItems.get(i);

			String legendLabel = old_legendItem.getLabel();
			System.out.println("legendLabel="+old_legendLabel);
			LegendItem new_legendItem = new LegendItem(
													   old_legendItem.getLabel()+s.getSummary(i),
													   old_legendItem.getDescription(),
													   old_legendItem.getToolTipText(),
													   old_legendItem.getURLText(),
													   old_legendItem.isShapeVisible(),
													   old_legendItem.getShape(),
													   old_legendItem.isShapeFilled(),
													   old_legendItem.getFillPaint(),
													   old_legendItem.isShapeOutlineVisible(),
													   old_legendItem.getOutlinePaint(),
													   old_legendItem.getOutlineStroke(),
													   old_legendItem.isLineVisible(),
													   old_legendItem.getLine(),
													   old_legendItem.getLineStroke(),
													   old_legendItem.getlinePaint()
													   );
			new_legendItems.add(new_legendItem);
			}
        

        // *** this is important - you need to add the item container to
        // the wrapper, otherwise the legend items won't be displayed when
        // the wrapper is drawn... ***
        BlockContainer items = legend.getItemContainer();
        items.setPadding(2, 10, 5, 2);
        wrapper.add(items);
        legend.setWrapper(wrapper);
        
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.addSubtitle(legend);*/

		setCategorySummary(dataset);
        return chart;
        
    }
    
protected JFreeChart createLegend(CategoryDataset dataset) {
	domainLabel = "Category";
	rangeLabel = "Value";
		//  JFreeChart chart = ChartFactory.createAreaChart(
	        JFreeChart chart = ChartFactory.createBarChart(
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

	        BarRenderer renderer = (BarRenderer) plot.getRenderer();
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
	       // renderer.setDrawOutlines(true);
	       // renderer.setUseFillPaint(true);
	       // renderer.setFillPaint(Color.white);
	        renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
	        return chart;
	        
	    }


}
