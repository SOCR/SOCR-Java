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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;

/**
 * A line chart demo showing the use of a custom drawing supplier.
 */
public class LineChartDemo5  extends SuperCategoryChart implements PropertyChangeListener {

     /**
     * Creates a sample dataset.
     * @param isDemo true use the demo data, false use data from the JTable
     * @return a sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
        if (isDemo){
        // row keys...
        String series1 = "First";
        String series2 = "Second";
        String series3 = "Third";

        // column keys...
        String type1 = "Type 1";
        String type2 = "Type 2";
        String type3 = "Type 3";
        String type4 = "Type 4";
        String type5 = "Type 5";
        String type6 = "Type 6";
        String type7 = "Type 7";
        String type8 = "Type 8";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, type1);
        dataset.addValue(4.0, series1, type2);
        dataset.addValue(3.0, series1, type3);
        dataset.addValue(5.0, series1, type4);
        dataset.addValue(5.0, series1, type5);
        dataset.addValue(7.0, series1, type6);
        dataset.addValue(7.0, series1, type7);
        dataset.addValue(8.0, series1, type8);

        dataset.addValue(5.0, series2, type1);
        dataset.addValue(7.0, series2, type2);
        dataset.addValue(6.0, series2, type3);
        dataset.addValue(8.0, series2, type4);
        dataset.addValue(4.0, series2, type5);
        dataset.addValue(4.0, series2, type6);
        dataset.addValue(2.0, series2, type7);
        dataset.addValue(1.0, series2, type8);

        dataset.addValue(4.0, series3, type1);
        dataset.addValue(3.0, series3, type2);
        dataset.addValue(2.0, series3, type3);
        dataset.addValue(3.0, series3, type4);
        dataset.addValue(6.0, series3, type5);
        dataset.addValue(3.0, series3, type6);
        dataset.addValue(4.0, series3, type7);
        dataset.addValue(3.0, series3, type8);
           
        return dataset;}
		else return super.createDataset(false);
        
    }
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
		 chart = chartMaker.getCategoryChart("Line", chartTitle, "Type", "Value", dataTable, no_category, pairs,"horizontal");	
		 chartPanel = new ChartPanel(chart, false); 
			
		 setChart();
	 }
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */    
    protected JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createLineChart(
            chartTitle,      // chart title
            domainLabel,                   // domain axis label
            rangeLabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            !legendPanelOn,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        chart.setBackgroundPaint(Color.white);

        Shape[] shapes = new Shape[3];
        int[] xpoints;
        int[] ypoints;

        // right-pointing triangle
        xpoints = new int[] {-3, 3, -3};
        ypoints = new int[] {-3, 0, 3};
        shapes[0] = new Polygon(xpoints, ypoints, 3);

        // vertical rectangle
        shapes[1] = new Rectangle2D.Double(-2, -3, 3, 6);

        // left-pointing triangle
        xpoints = new int[] {-3, 3, 3};
        ypoints = new int[] {0, -3, 3};
        shapes[2] = new Polygon(xpoints, ypoints, 3);

        DrawingSupplier supplier = new DefaultDrawingSupplier(
            DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
            shapes
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setDrawingSupplier(supplier);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
            0, 
            new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
                1.0f, new float[] {10.0f, 6.0f}, 0.0f
            )
        );
        plot.getRenderer().setSeriesStroke(
            1, 
            new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            )
        );
        plot.getRenderer().setSeriesStroke(
            2, 
            new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {2.0f, 6.0f}, 0.0f
            )
        );

        // customise the renderer...
        LineAndShapeRenderer renderer 
            = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(
            new StandardCategoryItemLabelGenerator()
        );
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

        // customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);

		setCategorySummary(dataset);
        return chart;
        
    }
    
    protected JFreeChart createLegend(CategoryDataset dataset) {
    	  JFreeChart chart = ChartFactory.createLineChart(
    	            chartTitle,      // chart title
    	            domainLabel,                   // domain axis label
    	            rangeLabel,                  // range axis label
    	            dataset,                  // data
    	            PlotOrientation.VERTICAL, // orientation
    	            true,                     // include legend
    	            true,                     // tooltips
    	            false                     // urls
    	        );

    	        chart.setBackgroundPaint(Color.white);

    	        Shape[] shapes = new Shape[3];
    	        int[] xpoints;
    	        int[] ypoints;

    	        // right-pointing triangle
    	        xpoints = new int[] {-3, 3, -3};
    	        ypoints = new int[] {-3, 0, 3};
    	        shapes[0] = new Polygon(xpoints, ypoints, 3);

    	        // vertical rectangle
    	        shapes[1] = new Rectangle2D.Double(-2, -3, 3, 6);

    	        // left-pointing triangle
    	        xpoints = new int[] {-3, 3, 3};
    	        ypoints = new int[] {0, -3, 3};
    	        shapes[2] = new Polygon(xpoints, ypoints, 3);

    	        DrawingSupplier supplier = new DefaultDrawingSupplier(
    	            DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE,
    	            DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
    	            DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
    	            DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
    	            shapes
    	        );
    	        CategoryPlot plot = chart.getCategoryPlot();
    	        plot.setOrientation(PlotOrientation.HORIZONTAL);
    	        plot.setBackgroundPaint(Color.lightGray);
    	        plot.setDomainGridlinePaint(Color.white);
    	        plot.setRangeGridlinePaint(Color.white);
    	        plot.setDrawingSupplier(supplier);

    	        // set the stroke for each series...
    	        plot.getRenderer().setSeriesStroke(
    	            0, 
    	            new BasicStroke(
    	                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
    	                1.0f, new float[] {10.0f, 6.0f}, 0.0f
    	            )
    	        );
    	        plot.getRenderer().setSeriesStroke(
    	            1, 
    	            new BasicStroke(
    	                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
    	                1.0f, new float[] {6.0f, 6.0f}, 0.0f
    	            )
    	        );
    	        plot.getRenderer().setSeriesStroke(
    	            2, 
    	            new BasicStroke(
    	                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
    	                1.0f, new float[] {2.0f, 6.0f}, 0.0f
    	            )
    	        );

    	        // customise the renderer...
    	        LineAndShapeRenderer renderer 
    	            = (LineAndShapeRenderer) plot.getRenderer();
    	        renderer.setBaseShapesVisible(true);
    	        renderer.setBaseItemLabelsVisible(true);
    	        renderer.setBaseItemLabelGenerator(
    	            new StandardCategoryItemLabelGenerator()
    	        );
    			renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
	        return chart;
	        
	    }
    

}
