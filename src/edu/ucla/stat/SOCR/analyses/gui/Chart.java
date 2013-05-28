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
package edu.ucla.stat.SOCR.analyses.gui;

import java.util.List;
import java.util.StringTokenizer;
import java.awt.Color;
import java.awt.Paint;

import java.lang.reflect.Array;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.*;
import org.jfree.data.Range;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;


import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;


/**This class defines a basic type of Statistical Chart that can be
 * subclassed by the specific types of chart
 * (e.g., ANOVA, Regression, prediction, etc.)*/

public class Chart {

	

    protected final int CHART_SIZE_X = 500;
    protected final int CHART_SIZE_Y = 400;

	// this generates an empty chart, used in init
	public JFreeChart createChart(){
		JFreeChart chart = ChartFactory.createXYLineChart(null,null,null,null,   PlotOrientation.VERTICAL,        // orientation
            false,                           // include legend
            true,                            // tooltips
            false                            // urls
        );

		return chart;
	}

	 protected List<Double> createValueList(String in){
		   String vs = in;
		   final String DELIMITERS = ",;\t ";
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
				System.out.println("Data format error!");
				return null;
			}
	        return result;
	    }
	 
	public JFreeChart getBoxAndWhiskerChart(String title, String xLabel, String yLabel, int sCount, int cCount, int xyLength, String[][] input){

		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset(); 
		for (int s=0; s<sCount; s++)
			for (int c=0; c<cCount; c++){
				String v= "" ;
            	for (int i = 0; i<xyLength; i++)
            		if(input[i][s]!=null && input[i][s].length()!=0)
            			v= v+input[i][s]+"," ;
            	
            	//values_storage[s][c]= v;
				dataset.add(createValueList(v), "Series " + s, "Category " + c); 
				
			}

		JFreeChart chart = createBoxAndWhiskerChart(title, xLabel, yLabel,  dataset);
		return chart;
	
	}
	// this will return a box_whisker chart
	public JFreeChart getBoxAndWhiskerChart(String title, String xLabel, String yLabel, int seriesCount, int categoryCount, String[] seriesName, String[][] categoryName, double[][][] values){

		BoxAndWhiskerCategoryDataset dataset = createBoxAndWhiskerDataset(seriesCount, categoryCount, seriesName, categoryName, values);
		JFreeChart chart = createBoxAndWhiskerChart(title, xLabel, yLabel,  dataset);
		return chart;
	}
	
	// this will give you a line chart
	public JFreeChart getLineChart(String title, String xLabel, String yLabel, double[] x, double[] y){

		XYDataset dataset = createXYDataset(x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, "");
		return chart;
	}

	public JFreeChart getLineChart(String title, String lineName, String xLabel, String yLabel, double[] x, double[] y){

		XYDataset dataset = createXYDataset(lineName, x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, "");
		return chart;
	}

	public JFreeChart getLineChart(String title, String xLabel, String yLabel, double[] x, double[] y, String other){

		XYDataset dataset = createXYDataset(x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, other);
		return chart;
	}
	public JFreeChart getLineChart(String title, String lineName, String xLabel, String yLabel, double[] x, double[] y, String other){

		XYDataset dataset = createXYDataset(lineName, x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, other);
		return chart;
	}

	public JFreeChart getLineChart(String title, String xLabel, String yLabel, double[] x, double[] y, Color[] colors,  String other){

		XYDataset dataset = createXYDataset(x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, colors, other);
		return chart;
	}

	public JFreeChart getLineChart(String title, String lineName, String xLabel, String yLabel, double[] x, double[] y, Color[] colors,  String other){

		XYDataset dataset = createXYDataset(lineName, x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, colors, other);
		return chart;
	}

	//double x[numberOfLines][numberOfPoints]
	//double y[numberOfLines][numberOfPoints]
	//String lineNames[numberOfLines]
	public JFreeChart getLineChart(String title, String xLabel, String yLabel, int numberOfLines, String[] lineNames, double[][] x, double[][] y, String other){

		XYDataset dataset = createXYDataset(numberOfLines, lineNames, x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, other);
		return chart;
	}

	public JFreeChart getLineChart(String title, String xLabel, String yLabel, int numberOfLines, String[] lineNames, double[][] x, double[][] y, Color[] colors, String other){

		XYDataset dataset = createXYDataset(numberOfLines, lineNames, x,y);
		JFreeChart chart = createLineChart(title, xLabel, yLabel,  dataset, colors, other);
		return chart;
	}

	//double[][] lineX  --double[number of line][number of dots in line]
	//double[][] dotX  --double[number of dots group][number of dots in the group]
	//"other" can be "nolegend" tp turn off the legend box at the buttom
 	public JFreeChart getLineAndDotChart(String title, String xLabel, String yLabel, int numberOfLines, String[] lineNames, double[][] lineX, double[][] lineY, Color[] lineColors, 
            int numberOfDotsGroups, String[] dotGroupNames, double[][] dotX, double[][] dotY, Color[] dotColors, String other){

		XYDataset dataset = createXYDataset(numberOfLines, lineNames, lineX,lineY, numberOfDotsGroups, dotGroupNames, dotX, dotY);
		JFreeChart chart = createLineAndDotChart(title, xLabel, yLabel,  dataset, numberOfLines, lineColors, numberOfDotsGroups, dotColors, other);
		return chart;
	}
	
	public JFreeChart getQQChart(String title, String xLabel, String yLabel, String serie1, double[] x, double[] y,  String serie2, double y_intercept, double slope, String other){

		XYDataset dataset = createXYDataset(serie1, x,y, serie2, y_intercept, slope);
		JFreeChart chart = createQQChart(title, xLabel, yLabel,  dataset, other);
		return chart;
	}
	/*public JFreeChart getQQChartWithoutBlueLine(String title, String xLabel, String yLabel, String serie1, double[] x, double[] y,  String serie2, double y_intercept, double slope, String other){

		XYDataset dataset = createXYDataset(serie1, x,y, serie2, y_intercept, slope);
		JFreeChart chart = createQQChart(title, xLabel, yLabel,  dataset, other, false, true);
		return chart;
	}

	public JFreeChart getQQChartWithoutRedDots(String title, String xLabel, String yLabel, String serie1, double[] x, double[] y,  String serie2, double y_intercept, double slope, String other){

		XYDataset dataset = createXYDataset(serie1, x,y, serie2, y_intercept, slope);
		JFreeChart chart = createQQChart(title, xLabel, yLabel,  dataset, other, true, false);
		return chart;
	}*/

	

    private XYDataset createXYDataset(double x[], double y[]) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries serie = new XYSeries("Single");
		for (int i=0; i<Array.getLength(x); i++)
			serie.add(x[i],y[i]);

		dataset.addSeries(serie);

		return dataset;
	}

    private XYDataset createXYDataset(String lineName, double x[], double y[]) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries serie = new XYSeries(lineName);
		for (int i=0; i<Array.getLength(x); i++)
			serie.add(x[i],y[i]);

		dataset.addSeries(serie);

		return dataset;
	}

    private XYDataset createXYDataset(int numberOfLines, String[] lineNames, double x[][], double y[][]) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (int j=0; j<numberOfLines; j++){
			XYSeries serie = new XYSeries(lineNames[j]);
			int len = Array.getLength(x[j]);
			for (int i=0; i<len; i++)
				serie.add(x[j][i],y[j][i]);

			dataset.addSeries(serie);
		}

		return dataset;
	}

    protected XYDataset createXYDataset(String serie1, double x[], double y[], String serie2, double y_intercept, double slope) {

			XYSeries series1 = new XYSeries(serie1);
			for (int i=0; i<Array.getLength(x); i++){
				series1.add(x[i], y[i]);}

			XYSeries series2 = new XYSeries(serie2);
			double min_x, min_y, max_x, max_y;

			min_x= 1000; max_x=-1000;
			for (int i=0; i<Array.getLength(x); i++){
				if (min_x>x[i])
					  min_x = x[i];
				if (max_x<x[i])
						  max_x = x[i];
			}

			min_y = slope*min_x + y_intercept;
			max_y = slope*max_x + y_intercept;

			series2.add(min_x, min_y);
			series2.add(max_x, max_y);

			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(series1);
			dataset.addSeries(series2);

			return dataset;
    }

    // for line and dot chart
    private XYDataset createXYDataset(int numberOfLines, String[] lineNames, double lx[][], double ly[][], int numberOfDotGroup, String[] dotGroupNames, double dx[][], double dy[][]) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (int j=0; j<numberOfLines; j++){
			XYSeries serie = new XYSeries(lineNames[j]);
			int len = Array.getLength(lx[j]);
			for (int i=0; i<len; i++)
				serie.add(lx[j][i],ly[j][i]);

			dataset.addSeries(serie);
		}
		for (int j=0; j<numberOfDotGroup; j++){
			XYSeries serie = new XYSeries(dotGroupNames[j]);
			int len = Array.getLength(dx[j]);
			for (int i=0; i<len; i++)
				serie.add(dx[j][i],dy[j][i]);

			dataset.addSeries(serie);
		}

		return dataset;
	}
    private BoxAndWhiskerCategoryDataset createBoxAndWhiskerDataset(int seriesCount, int categoryCount, String[] seriesName, String[][] categoryName, double[][][] values ){

		List<Double> list;

        DefaultBoxAndWhiskerCategoryDataset result
            = new DefaultBoxAndWhiskerCategoryDataset();

        for (int s = 0; s <seriesCount; s++) {
            for (int c = 0; c <categoryCount; c++) {
				list = new java.util.ArrayList<Double>();
				for (int i=0; i<Array.getLength(values[s][c]);i++)
					list.add(new Double(values[s][c][i]));
                result.add(list, seriesName[s], categoryName[s][c]);
            }
        }
        return result;

	}

  private JFreeChart createLineChart(String title, String xLabel, String yLabel, XYDataset dataset, String other) {


        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,   // chart title
            xLabel,                     // domain axis label
            yLabel,                   // range axis label
            dataset,                         // data
            PlotOrientation.VERTICAL,        // orientation
            true,                           // include legend
            true,                            // tooltips
            false                            // urls
        );



        XYPlot plot = chart.getXYPlot();
        chart.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.lightGray);
        //plot.setNoDataMessage("No data available");

        // customise the range axis...

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperMargin(0.02);
        rangeAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
        domainAxis.setLowerMargin(0.02);


        // customise the renderer...
        XYLineAndShapeRenderer renderer
            = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseLinesVisible(true);
        renderer.setDrawOutlines(true);
        renderer.setBaseShapesFilled(true);
		renderer.setUseFillPaint(true);
		renderer.setBaseFillPaint(Color.white);

		if (other.toLowerCase().indexOf("noline")!=-1){
			renderer.setBaseShapesVisible(true);
	        renderer.setBaseLinesVisible(false);
		}

		if (other.toLowerCase().indexOf("noshape")!=-1){
			renderer.setBaseShapesVisible(false);
	        renderer.setBaseLinesVisible(true);
		}

		if (other.toLowerCase().indexOf("excludeszero")!=-1){
			rangeAxis.setAutoRangeIncludesZero(false);
			domainAxis.setAutoRangeIncludesZero(false);
		}

        return chart;
    }

  private JFreeChart createLineChart(String title, String xLabel, String yLabel, XYDataset dataset, Color[] colors, String other) {


      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart(
          title,   // chart title
          xLabel,                     // domain axis label
          yLabel,                   // range axis label
          dataset,                         // data
          PlotOrientation.VERTICAL,        // orientation
          true,                           // include legend
          true,                            // tooltips
          false                            // urls
      );



      XYPlot plot = chart.getXYPlot();
      chart.setBackgroundPaint(Color.white);
      plot.setRangeGridlinePaint(Color.lightGray);
      plot.setDomainGridlinePaint(Color.lightGray);
      //plot.setNoDataMessage("No data available");

      // customise the range axis...

      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
      domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setUpperMargin(0.02);
      rangeAxis.setLowerMargin(0.02);
      domainAxis.setUpperMargin(0.02);
      domainAxis.setLowerMargin(0.02);


      // customise the renderer...
      XYLineAndShapeRenderer renderer
          = (XYLineAndShapeRenderer) plot.getRenderer();
      renderer.setBaseShapesVisible(true);
      renderer.setBaseLinesVisible(true);
      renderer.setDrawOutlines(true);
      renderer.setBaseShapesFilled(true);
      renderer.setUseFillPaint(true);
      renderer.setBaseFillPaint(Color.white);

      for(int i =0; i<colors.length; i++){
    	  renderer.setSeriesPaint(i, colors[i]);
      }

      if (other.toLowerCase().indexOf("noline")!=-1){
			renderer.setBaseShapesVisible(true);
	        renderer.setBaseLinesVisible(false);
		}

      if (other.toLowerCase().indexOf("noshape")!=-1){
			renderer.setBaseShapesVisible(false);
	        renderer.setBaseLinesVisible(true);
      }

      if (other.toLowerCase().indexOf("excludeszero")!=-1){
			rangeAxis.setAutoRangeIncludesZero(false);
			domainAxis.setAutoRangeIncludesZero(false);
      }

      if (other.toLowerCase().indexOf("color")!=-1){
			renderer.setBaseShapesVisible(false);
	        renderer.setBaseLinesVisible(true);
      }

      return chart;
  }

//other can be "nolegend"
  private JFreeChart createLineAndDotChart(String title, String xLabel, String yLabel, XYDataset dataset, int numberOfLines, Color[] lineColors, int numberOfDotsGroups, Color[] dotColors, String other) {

	  boolean legend=true;
	  if (other.toLowerCase().indexOf("noledend")!=-1){
		  legend=false;
	  }

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart(
          title,   // chart title
          xLabel,                     // domain axis label
          yLabel,                   // range axis label
          dataset,                         // data
          PlotOrientation.VERTICAL,        // orientation
          legend,                           // include legend
          true,                            // tooltips
          false                            // urls
      );

 

      XYPlot plot = chart.getXYPlot();
      chart.setBackgroundPaint(Color.white);
      plot.setRangeGridlinePaint(Color.lightGray);
      plot.setDomainGridlinePaint(Color.lightGray);
      //plot.setNoDataMessage("No data available");

      // customise the range axis...

      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
      domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
  
      rangeAxis.setUpperMargin(0.02);
      rangeAxis.setLowerMargin(0.02);
      domainAxis.setUpperMargin(0.02);
      domainAxis.setLowerMargin(0.02);

      // customise the renderer...
      XYLineAndShapeRenderer renderer
          = (XYLineAndShapeRenderer) plot.getRenderer();
      for (int i=0; i<numberOfLines; i++){
    	  renderer.setSeriesShapesVisible(i,false);
    	  renderer.setSeriesLinesVisible(i,true);
      }
      for (int i=numberOfLines; i<numberOfLines+numberOfDotsGroups; i++){
    	  renderer.setSeriesShapesVisible(i,true);
    	  renderer.setSeriesLinesVisible(i,false);
      }
     // renderer.setDrawOutlines(true);
     // renderer.setBaseShapesFilled(true);
   //   renderer.setUseFillPaint(true);
    //  renderer.setFillPaint(Color.white);
     
      //set line color
      for(int i =0; i<lineColors.length; i++){
    	  renderer.setSeriesPaint(i, lineColors[i]);
      }
		
      //    set dot color
      for(int i =0; i<dotColors.length; i++){
    	  renderer.setSeriesPaint(numberOfLines+i, dotColors[i]);
      }
     
      if (other.toLowerCase().indexOf("excludeszero")!=-1){
			rangeAxis.setAutoRangeIncludesZero(false);
			domainAxis.setAutoRangeIncludesZero(false);
		}
      
      return chart;
  }

  private JFreeChart createBoxAndWhiskerChart(String title, String xLabel, String yLabel, BoxAndWhiskerCategoryDataset dataset) {

	    CategoryAxis domainAxis = new CategoryAxis(xLabel);
        NumberAxis rangeAxis = new NumberAxis(yLabel);

		// CategoryItemRenderer renderer = new BoxAndWhiskerRenderer();
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(
            dataset, domainAxis, rangeAxis, renderer
        );
        JFreeChart chart = new JFreeChart(title, plot);

        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		//columnCount -- category count
		//RowCount -- serie count
		if(dataset.getColumnCount()* dataset.getRowCount()<5){

			domainAxis.setLowerMargin(0.2);
			domainAxis.setUpperMargin(0.2);
	   		if (dataset.getColumnCount()==1)
				renderer.setItemMargin(0.5);
			//	domainAxis.setCategoryMargin(domainAxis.getCategoryMargin()*2);

			//System.out.println("lowerMargin="+domainAxis.getLowerMargin());
			//System.out.println("ItemMargin="+renderer.getItemMargin());
			//System.out.println("CategoryMargin="+domainAxis.getCategoryMargin());

		}

		else if(dataset.getColumnCount()* dataset.getRowCount()<10){
			domainAxis.setLowerMargin(domainAxis.getLowerMargin()*2);
			domainAxis.setUpperMargin(domainAxis.getUpperMargin()*2);
	   		if (dataset.getColumnCount()==1)
				renderer.setItemMargin(renderer.getItemMargin()*2);
			else
				domainAxis.setCategoryMargin(domainAxis.getCategoryMargin()*2);
	//System.out.println("lowerMargin="+domainAxis.getLowerMargin());
			//System.out.println("ItemMargin="+renderer.getItemMargin());
			//System.out.println("CategoryMargin="+domainAxis.getCategoryMargin());

		}

        return chart;
    }
  
 protected JFreeChart createQQChart(String title, String xLabel, String yLabel, XYDataset dataset, String other) {
      
      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart(
          title,      // chart title
          xLabel,                      // x axis label
          yLabel,                      // y axis label
          dataset,                  // data
          PlotOrientation.VERTICAL,
          true,                     // include legend
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
		// renderer.setShapesVisible(true);
      renderer.setBaseShapesFilled(true);
		//  renderer.setLinesVisible(false);
      renderer.setSeriesLinesVisible(1, true);
      renderer.setSeriesShapesVisible(1, false);
      renderer.setSeriesLinesVisible(0, false);
      renderer.setSeriesShapesVisible(0, true);
     
		//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

      // change the auto tick unit selection to integer units only...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setAutoRangeIncludesZero(false);
      rangeAxis.setUpperMargin(0.02);
      rangeAxis.setLowerMargin(0.02);

		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
      domainAxis.setAutoRangeIncludesZero(false);
      domainAxis.setUpperMargin(0.02);
      domainAxis.setLowerMargin(0.02);

		// domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // OPTIONAL CUSTOMISATION COMPLETED.
		//setQQSummary(dataset);    // very confusing   
      return chart;
      
  }

 /* protected JFreeChart createQQChart(String title, String xLabel, String yLabel, XYDataset dataset, String other) {
		return  	createQQChart(title, xLabel, yLabel, dataset, other, true, true);
  } // default: first flag, true, second flag: true, to have things printed

  protected JFreeChart createQQChart(String title, String xLabel, String yLabel, XYDataset dataset, String other, boolean legendFlagBlueLine, boolean legendFlagRedDot) {

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart(
          title,      // chart title
          xLabel,                      // x axis label
          yLabel,                      // y axis label
          dataset,                  // data
          PlotOrientation.VERTICAL,
          true,                     // include legend
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
		// renderer.setShapesVisible(true);

      renderer.setShapesFilled(true);
		//  renderer.setLinesVisible(false);
      renderer.setSeriesLinesVisible(1, legendFlagBlueLine); // blue line
      renderer.setSeriesShapesVisible(1, false); // determine whether there's dots
      renderer.setSeriesLinesVisible(0, false); // connect red dots.
      renderer.setSeriesShapesVisible(0, true); // show red dots.

		//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

      // change the auto tick unit selection to integer units only...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setAutoRangeIncludesZero(false);
      rangeAxis.setUpperMargin(0.02);
      rangeAxis.setLowerMargin(0.02);

		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
      domainAxis.setAutoRangeIncludesZero(false);
      domainAxis.setUpperMargin(0.02);
      domainAxis.setLowerMargin(0.02);

		// domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // OPTIONAL CUSTOMISATION COMPLETED.
		//setQQSummary(dataset);    // very confusing
      return chart;

  }*/
 
}