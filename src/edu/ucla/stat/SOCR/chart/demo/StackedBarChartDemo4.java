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
import java.awt.GradientPaint;
import java.awt.Paint;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.ui.VerticalAlignment;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A simple demonstration application showing how to create a stacked bar chart
 * using data from a {@link CategoryDataset}.
 */
public class StackedBarChartDemo4 extends SuperCategoryChart_Bar implements PropertyChangeListener {
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
		 chart = chartMaker.getCategoryChart("Bar", chartTitle, "Category", "value", dataTable, no_category, pairs, "stacked");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
 
	public void init(){
		turnLegendPanelOn();
		super.init();
	
	}
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultCategoryDataset result = new DefaultCategoryDataset();

        result.addValue(20.3, "Product 1 (US)", "Jan 04");
        result.addValue(27.2, "Product 1 (US)", "Feb 04");
        result.addValue(19.7, "Product 1 (US)", "Mar 04");
        result.addValue(19.4, "Product 1 (Europe)", "Jan 04");
        result.addValue(10.9, "Product 1 (Europe)", "Feb 04");
        result.addValue(18.4, "Product 1 (Europe)", "Mar 04");
        result.addValue(16.5, "Product 1 (Asia)", "Jan 04");
        result.addValue(15.9, "Product 1 (Asia)", "Feb 04");
        result.addValue(16.1, "Product 1 (Asia)", "Mar 04");
        result.addValue(13.2, "Product 1 (Middle East)", "Jan 04");
        result.addValue(14.4, "Product 1 (Middle East)", "Feb 04");
        result.addValue(13.7, "Product 1 (Middle East)", "Mar 04");

        result.addValue(23.3, "Product 2 (US)", "Jan 04");
        result.addValue(16.2, "Product 2 (US)", "Feb 04");
        result.addValue(28.7, "Product 2 (US)", "Mar 04");
        result.addValue(12.7, "Product 2 (Europe)", "Jan 04");
        result.addValue(17.9, "Product 2 (Europe)", "Feb 04");
        result.addValue(12.6, "Product 2 (Europe)", "Mar 04");
        result.addValue(15.4, "Product 2 (Asia)", "Jan 04");
        result.addValue(21.0, "Product 2 (Asia)", "Feb 04");
        result.addValue(11.1, "Product 2 (Asia)", "Mar 04");
        result.addValue(23.8, "Product 2 (Middle East)", "Jan 04");
        result.addValue(23.4, "Product 2 (Middle East)", "Feb 04");
        result.addValue(19.3, "Product 2 (Middle East)", "Mar 04");

        result.addValue(11.9, "Product 3 (US)", "Jan 04");
        result.addValue(31.0, "Product 3 (US)", "Feb 04");
        result.addValue(22.7, "Product 3 (US)", "Mar 04");
        result.addValue(15.3, "Product 3 (Europe)", "Jan 04");
        result.addValue(14.4, "Product 3 (Europe)", "Feb 04");
        result.addValue(25.3, "Product 3 (Europe)", "Mar 04");
        result.addValue(23.9, "Product 3 (Asia)", "Jan 04");
        result.addValue(19.0, "Product 3 (Asia)", "Feb 04");
        result.addValue(10.1, "Product 3 (Asia)", "Mar 04");
        result.addValue(13.2, "Product 3 (Middle East)", "Jan 04");
        result.addValue(15.5, "Product 3 (Middle East)", "Feb 04");
        result.addValue(10.1, "Product 3 (Middle East)", "Mar 04");
        
        return result;}
		else return super.createDataset(false);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset for the chart.
     * 
     * @return A sample chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createStackedBarChart(
            chartTitle,  // chart title
            domainLabel,                  // domain axis label
            rangeLabel,                     // range axis label
            dataset,                     // data
            PlotOrientation.VERTICAL,    // the plot orientation
            !legendPanelOn,                        // legend
            true,                        // tooltips
            false                        // urls
        );
        
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap("G1");
        map.mapKeyToGroup("Product 1 (US)", "G1");
        map.mapKeyToGroup("Product 1 (Europe)", "G1");
        map.mapKeyToGroup("Product 1 (Asia)", "G1");
        map.mapKeyToGroup("Product 1 (Middle East)", "G1");
        map.mapKeyToGroup("Product 2 (US)", "G2");
        map.mapKeyToGroup("Product 2 (Europe)", "G2");
        map.mapKeyToGroup("Product 2 (Asia)", "G2");
        map.mapKeyToGroup("Product 2 (Middle East)", "G2");
        map.mapKeyToGroup("Product 3 (US)", "G3");
        map.mapKeyToGroup("Product 3 (Europe)", "G3");
        map.mapKeyToGroup("Product 3 (Asia)", "G3");
        map.mapKeyToGroup("Product 3 (Middle East)", "G3");
        renderer.setSeriesToGroupMap(map); 
        
        renderer.setItemMargin(0.10);
        renderer.setDrawBarOutline(false);
        Paint p1 = new GradientPaint(0.0f, 0.0f, new Color(0x22, 0x22, 0xFF), 
                0.0f, 0.0f, new Color(0x88, 0x88, 0xFF));
        renderer.setSeriesPaint(0, p1);
        renderer.setSeriesPaint(4, p1);
        renderer.setSeriesPaint(8, p1);
         
        Paint p2 = new GradientPaint(0.0f, 0.0f, new Color(0x22, 0xFF, 0x22), 
                0.0f, 0.0f, new Color(0x88, 0xFF, 0x88));
        renderer.setSeriesPaint(1, p2); 
        renderer.setSeriesPaint(5, p2); 
        renderer.setSeriesPaint(9, p2); 
        
        Paint p3 = new GradientPaint(0.0f, 0.0f, new Color(0xFF, 0x22, 0x22), 
                0.0f, 0.0f, new Color(0xFF, 0x88, 0x88));
        renderer.setSeriesPaint(2, p3);
        renderer.setSeriesPaint(6, p3);
        renderer.setSeriesPaint(10, p3);
            
        Paint p4 = new GradientPaint(0.0f, 0.0f, new Color(0xFF, 0xFF, 0x22), 
                0.0f, 0.0f, new Color(0xFF, 0xFF, 0x88));
        renderer.setSeriesPaint(3, p4);
        renderer.setSeriesPaint(7, p4);
        renderer.setSeriesPaint(11, p4);
        renderer.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(
                        GradientPaintTransformType.HORIZONTAL));

		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

        SubCategoryAxis domainAxis = new SubCategoryAxis("Product / Month");
        domainAxis.setCategoryMargin(0.05);
        domainAxis.addSubCategory("Product 1");
        domainAxis.addSubCategory("Product 2");
        domainAxis.addSubCategory("Product 3");
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainAxis(domainAxis);
        //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        plot.setRenderer(renderer);
		//   plot.setFixedLegendItems(createLegendItems());

		setCategorySummary(dataset);
        return chart;
        
    }
    
    protected JFreeChart createLegend(CategoryDataset dataset) {
        
		//  JFreeChart chart = ChartFactory.createAreaChart(
    	JFreeChart chart = ChartFactory.createStackedBarChart(
                chartTitle,  // chart title
                domainLabel,                  // domain axis label
                rangeLabel,                     // range axis label
                dataset,                     // data
                PlotOrientation.VERTICAL,    // the plot orientation
                true,                        // legend
                true,                        // tooltips
                false                        // urls
            );
	 
	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
    	 GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
         KeyToGroupMap map = new KeyToGroupMap("G1");
         map.mapKeyToGroup("Product 1 (US)", "G1");
         map.mapKeyToGroup("Product 1 (Europe)", "G1");
         map.mapKeyToGroup("Product 1 (Asia)", "G1");
         map.mapKeyToGroup("Product 1 (Middle East)", "G1");
         map.mapKeyToGroup("Product 2 (US)", "G2");
         map.mapKeyToGroup("Product 2 (Europe)", "G2");
         map.mapKeyToGroup("Product 2 (Asia)", "G2");
         map.mapKeyToGroup("Product 2 (Middle East)", "G2");
         map.mapKeyToGroup("Product 3 (US)", "G3");
         map.mapKeyToGroup("Product 3 (Europe)", "G3");
         map.mapKeyToGroup("Product 3 (Asia)", "G3");
         map.mapKeyToGroup("Product 3 (Middle East)", "G3");
         renderer.setSeriesToGroupMap(map); 
         
         renderer.setItemMargin(0.10);
         renderer.setDrawBarOutline(false);
         Paint p1 = new GradientPaint(0.0f, 0.0f, new Color(0x22, 0x22, 0xFF), 
                 0.0f, 0.0f, new Color(0x88, 0x88, 0xFF));
         renderer.setSeriesPaint(0, p1);
         renderer.setSeriesPaint(4, p1);
         renderer.setSeriesPaint(8, p1);
          
         Paint p2 = new GradientPaint(0.0f, 0.0f, new Color(0x22, 0xFF, 0x22), 
                 0.0f, 0.0f, new Color(0x88, 0xFF, 0x88));
         renderer.setSeriesPaint(1, p2); 
         renderer.setSeriesPaint(5, p2); 
         renderer.setSeriesPaint(9, p2); 
         
         Paint p3 = new GradientPaint(0.0f, 0.0f, new Color(0xFF, 0x22, 0x22), 
                 0.0f, 0.0f, new Color(0xFF, 0x88, 0x88));
         renderer.setSeriesPaint(2, p3);
         renderer.setSeriesPaint(6, p3);
         renderer.setSeriesPaint(10, p3);
             
         Paint p4 = new GradientPaint(0.0f, 0.0f, new Color(0xFF, 0xFF, 0x22), 
                 0.0f, 0.0f, new Color(0xFF, 0xFF, 0x88));
         renderer.setSeriesPaint(3, p4);
         renderer.setSeriesPaint(7, p4);
         renderer.setSeriesPaint(11, p4);
         renderer.setGradientPaintTransformer(
                 new StandardGradientPaintTransformer(
                         GradientPaintTransformType.HORIZONTAL));

 		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

         SubCategoryAxis domainAxis = new SubCategoryAxis("Product / Month");
         domainAxis.setCategoryMargin(0.05);
         domainAxis.addSubCategory("Product 1");
         domainAxis.addSubCategory("Product 2");
         domainAxis.addSubCategory("Product 3");
         
         CategoryPlot plot = (CategoryPlot) chart.getPlot();
         plot.setDomainAxis(domainAxis);
         //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
         plot.setRenderer(renderer);
	        return chart;
	        
	    }
	    

    /**
     * Creates the legend items for the chart.  In this case, we set them 
     * manually because we only want legend items for a subset of the data 
     * series.
     * 
     * @return The legend items.
     */
    private static LegendItemCollection createLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("US", "-", null, null, 
            Plot.DEFAULT_LEGEND_ITEM_BOX, new Color(0x22, 0x22, 0xFF));
        LegendItem item2 = new LegendItem("Europe", "-", null, null,
            Plot.DEFAULT_LEGEND_ITEM_BOX, new Color(0x22, 0xFF, 0x22));
        LegendItem item3 = new LegendItem("Asia", "-", null, null, 
            Plot.DEFAULT_LEGEND_ITEM_BOX, new Color(0xFF, 0x22, 0x22));
        LegendItem item4 = new LegendItem("Middle East", "-", null, null,
            Plot.DEFAULT_LEGEND_ITEM_BOX, new Color(0xFF, 0xFF, 0x22));
        result.add(item1);
        result.add(item2);
        result.add(item3);
        result.add(item4);
        return result;
    }
/*	
	protected void initMixPanel(){
		dataPanel2 = new JPanel();
		dataPanel2.setLayout(new BoxLayout(dataPanel2, BoxLayout.Y_AXIS));

		graphPanel2 = new JPanel();
		graphPanel2.setLayout(new BoxLayout(graphPanel2, BoxLayout.Y_AXIS));

		mixPanel = new JPanel(new BorderLayout());
		//		resetChart();

		setMixPanel();
	}*/


}
