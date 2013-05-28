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
import java.awt.GradientPaint;
import java.awt.Paint;
import java.beans.PropertyChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;

import edu.ucla.stat.SOCR.chart.ChartGenerator_JTable;
import edu.ucla.stat.SOCR.chart.SuperCategoryChart_Bar;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.chart.gui.CustomBarRenderer;

/**
 * A bar chart that uses a custom renderer to display different colors within a
 * single series.  The colors use GradientPaint and the chart is animated.
 */
public class BarChartDemo9 extends SuperCategoryChart_Bar implements PropertyChangeListener {
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
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    protected CategoryDataset createDataset(boolean isDemo) {
		if (isDemo){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(410.0, "Network Traffic", "Monday");
        dataset.addValue(680.0, "Network Traffic", "Tuesday");
        dataset.addValue(530.0, "Network Traffic", "Wednesday");
        dataset.addValue(570.0, "Network Traffic", "Thursday");
        dataset.addValue(330.0, "Network Traffic", "Friday");
        return dataset;}
		else return super.createDataset(false);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a sample chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle,       // chart title
            domainLabel,                  // domain axis label
            rangeLabel,                 // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // the plot orientation
            !legendPanelOn,                    // include legend
            true,
            false
        );

        TextTitle title = chart.getTitle();
        title.setBorder(0, 0, 1, 0);
        title.setBackgroundPaint(new GradientPaint(0f, 0f, Color.red, 350f, 
                0f, Color.white, true));
        title.setExpandToFitSpace(true);

        chart.setBackgroundPaint(new GradientPaint(0f, 0f, Color.yellow, 350f, 
                0f, Color.white, true));

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setNoDataMessage("NO DATA!");
        plot.setBackgroundPaint(null);
        plot.setInsets(new RectangleInsets(10, 5, 5, 5));
        plot.setOutlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setRangeGridlineStroke(new BasicStroke(1.0f));
        Paint[] colors = createPaint();
        CustomBarRenderer renderer = new CustomBarRenderer(colors);
        renderer.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(
                        GradientPaintTransformType.CENTER_HORIZONTAL));
        plot.setRenderer(renderer);
        
		renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

        // change the margin at the top of the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
     //   rangeAxis.setRange(0.0, 800.0);
        rangeAxis.setTickMarkPaint(Color.black);

		setCategorySummary(dataset);
        return chart;

    }
        
    /**
     * Returns an array of paint objects that will be used for the bar colors.
     * 
     * @return An array of paint objects.
     */
    private static Paint[] createPaint() {
        Paint[] colors = new Paint[5];
        colors[0] = new GradientPaint(0f, 0f, Color.white, 0f, 0f, Color.red);
        colors[1] = new GradientPaint(0f, 0f, Color.white, 0f, 0f, Color.green);
        colors[2] = new GradientPaint(0f, 0f, Color.white, 0f, 0f, Color.blue);
        colors[3] = new GradientPaint(0f, 0f, Color.white, 0f, 0f, Color.orange);
        colors[4] = new GradientPaint(0f, 0f, Color.white, 0f, 0f, Color.magenta);
        return colors;
    }
    
}
