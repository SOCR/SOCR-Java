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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.chart;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CompassPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryStepRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.renderer.xy.YIntervalRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.Statistics;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;
import org.jfree.util.SortOrder;

import edu.ucla.stat.SOCR.analyses.data.DataCase;
import edu.ucla.stat.SOCR.analyses.exception.DataException;
import edu.ucla.stat.SOCR.chart.data.DateParser;
import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset;
import edu.ucla.stat.SOCR.chart.data.SimpleIntervalXYDataset2;
import edu.ucla.stat.SOCR.chart.gui.Rotator;
import edu.ucla.stat.SOCR.chart.gui.SOCRPolarItemRenderer;
import edu.ucla.stat.SOCR.chart.gui.SOCRSpiderWebPlot;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.util.RandomGenerator;
import edu.ucla.stat.SOCR.util.RegressionLine;


/**This class provide the API interface for creating many type's charts
 * including PieChart, BarChart, LineChart, etc
 * <p>
 * JTable is used as input data type for the chart.
 * <p>
 * The output data type is JFreeChart which is implemented using the Java 2D APIs
 * and can be easily included in GUI.
 * */

public class ChartGenerator_JTable{

    protected int CHART_SIZE_X = 500;
    protected int CHART_SIZE_Y = 400;

    private final String DELIMITERS = ",;\t ";
    private int no_series, no_category;
    private JTable dataTable;
    private TableColumnModel columnModel;
    private String[][] depValues;   //y
    private String[][] indepValues;   //x
    private int xyLength;
    private double[] stdResiduals, normalQuantiles;

    private String[] dependentHeaders;
    private String[] independentHeaders;

	private String dimension;     // 3D/2D
	private String rotation;      // clockwise/counter-clockwise
	private PlotOrientation orientation;   // horizontial/vertical
	private String layout;        // stacked/layered/waterfall
	private String timeType;      //year/month/day/minute
	private String lineCondition;   //noline/nofill/noshape
	private int bin_size;    // for histogram chart

    public void setChartSize(int x, int y){
    	 CHART_SIZE_X = x;
    	 CHART_SIZE_Y = y;
    }


/***************************************  return chart ************************************************************/
	/**
	 *  this generates an empty chart, used in init
	 *
	 *  @param title the chart title
	 */
	public JFreeChart createEmptyChart(String title){
		JFreeChart chart = ChartFactory.createXYLineChart(title,null,null,null,   PlotOrientation.VERTICAL,        // orientation
            false,                           // include legend
            true,                            // tooltips
            false                            // urls
        );
		return chart;
	}

	/**
	 *  this will generate a Pie Chart
	 *
	 *  @param title chart title
	 *  @param table chart data
	 *  @param pairs data table mapping info
	 *  @param other can be 2D/3D/ring/clockwise/counter_colckwise
	 *  */
	public JFreeChart getPieChart(String title, JTable table, int[][] pairs, String other){
		dataTable = table;
		columnModel = dataTable.getColumnModel();
		no_series = 1;

		setOtherCondition(other);
		setArrayFromTable(1, pairs);
		DefaultPieDataset dataset = createPieDataset();
		JFreeChart chart = createPieChart(title,  dataset);
		return chart;
	}

	/**
	 *  given category data, this method will generate a Bar Chart, Line Chart, Area Chart or Step Chart
	 *
	 *  @param charType can be bar/barstat/barstatraw/line/linestat/area/step/areatime/spiderweb/eventfreqtime
	 *  @param title chart title
	 *  @param xlabel X axis label
	 *  @param yLabel Y axis label
	 *  @param table chart data
	 *  @param numberOfCategory number of category in dataTable
	 *  @param pairs data table mapping info
	 *  @param other can be 3d/stacked/layered/waterfall for bar chart, noshap/noline/nofill for line chart, minute/day/month/year for areatime chart
	 *  */
	public JFreeChart getCategoryChart(String chartType, String title,  String xLabel, String yLabel, JTable table, int numberOfCategory, int[][] pairs, String other){

		String type;
		if (chartType == null || chartType.length()==0)
			type = "bar";
		else type = chartType.toLowerCase();

		dataTable = table;
		columnModel = dataTable.getColumnModel();
     	no_category =  numberOfCategory;

     	setOtherCondition(other);
		setArrayFromTable(no_category, pairs);
		CategoryDataset dataset;


		if (type.equals("bar")){
			dataset= createCategoryDataset();
			JFreeChart chart = createCategoryBarChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("barstat")){
			dataset = createCategoryStatDataset();
			JFreeChart chart = createCategoryBarStatChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("barstatraw")){
			dataset = createCategoryStatRawDataset();
			JFreeChart chart = createCategoryBarStatChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("line")){
			dataset= createCategoryDataset();
			JFreeChart chart = createCategoryLineChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("linestat")){
			dataset= createCategoryStatDataset();
			JFreeChart chart = createCategoryLineStatChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("linestatraw")){
			dataset= createCategoryStatRawDataset();
			JFreeChart chart = createCategoryLineStatChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("step")){
			dataset= createCategoryDataset();
			JFreeChart chart = createCategoryStepChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("area")){
			dataset= createCategoryDataset();
			JFreeChart chart = createCategoryAreaChart(title,  xLabel, yLabel, dataset);
			return chart;
		}else if (type.equals("areatime")){
			dataset= createCategoryDataset_Time(timeType);
			JFreeChart chart = createCategoryAreaChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("eventfreqtime")){
			dataset= createCategoryDataset_Time(timeType);
			JFreeChart chart = createCategoryEventFreqChart_Time(title,  xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("spiderweb")){
			dataset= createCategoryDataset();
			JFreeChart chart = createCategorySpiderWebChart(title, dataset);
			return chart;
		}
		else //default bar
		{
			dataset= createCategoryDataset();
			JFreeChart chart = createCategoryBarChart(title,  xLabel, yLabel, dataset);
			return chart;
		}
	}

	/**
	 *  given XY data, this method will generate a Bar Chart, Line Chart, Area Chart etc
	 *
	 *  @param charType can be line/linetime/lineqq/lineqqdd/bar/area/step/areatime/nd/differencetime/symbolicaxis/polar
	 *  @param title chart title
	 *  @param xlabel X axis label
	 *  @param yLabel Y axis label
	 *  @param table chart data
	 *  @param numberOfSeries number of series in dataTable
	 *  @param pairs data table mapping info
	 *  @param other can be noshap/noline/nofill for line chart, minute/day/month/year for areatime chart and differencetime chart
	 *  */
	public JFreeChart getXYChart(String chartType, String title, String xLabel, String yLabel, JTable table, int numberOfSeries, int[][] pairs, String other){

		String type;
		if (chartType == null || chartType.length()==0)
			type = "line";
		else type = chartType.toLowerCase();

		dataTable = table;
		columnModel = dataTable.getColumnModel();
		no_series = numberOfSeries;

		setOtherCondition(other);
		setArrayFromTable(numberOfSeries, pairs);
		XYDataset dataset;

		//System.out.println("type="+type+" timetype="+timeType);
		if (type.equals("line")){
			dataset = createXYDataset();
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("linetime")){
			dataset = createXYDataset_Time(timeType);
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		if (type.equals("lineqq")){
			dataset = createXYQQDataset();
			lineCondition ="lineqq";
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		if (type.equals("lineqqdd")){
			dataset = createXYQQDDDataset();
			lineCondition ="lineqqdd";
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("bar")){
			IntervalXYDataset dataset1 = createIntervalXYDataset_Time(timeType);
			JFreeChart chart = createXYBarChart(title, xLabel, yLabel, dataset1);
			return chart;
		}
		else if (type.equals("histogram")){
			IntervalXYDataset dataset1 = createIntervalXYDataset(bin_size);
			JFreeChart chart = createXYBarChart(title, xLabel, yLabel, dataset1);
			return chart;
		}
		if (type.equals("index")){
			dataset = createXYDataset_Index();
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		if (type.equals("dot")){
			dataset = createXYDataset_Dot();
			lineCondition ="noline";
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("area")){
			dataset = createXYDataset();
			JFreeChart chart = createXYAreaChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("areatime")){
			//System.out.println("areaTime");
			dataset = createXYDataset_Time(timeType);
			JFreeChart chart = createXYAreaChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("step")){
			dataset = createXYDataset();
			JFreeChart chart = createXYStepChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("nd")){
			dataset = createXYNDDataset();
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("differencetime")){
			dataset = createXYDataset_Time(timeType);
			JFreeChart chart = createXYDifferenceChart(title, xLabel, yLabel, dataset);
			return chart;
		}
		else if (type.equals("symbolicaxis")){
			dataset = createXYDataset();
			JFreeChart chart = createXYSymbolicAxisChart(title,dataset);
			return chart;
		}
		else if (type.equals("polar")){
			dataset = createXYDataset();
			JFreeChart chart = createXYPolarChart(title,dataset);
			return chart;
		}
		else {//default line
			dataset = createXYDataset();
			JFreeChart chart = createXYLineChart(title, xLabel, yLabel, dataset);
			return chart;
		}
	}

	/**
	 *  given XY data, this method will generate a  stacked Area Chart
	 *
	 *  @param title chart title
	 *  @param xlabel X axis label
	 *  @param yLabel Y axis label
	 *  @param table chart data
	 *  @param numberOfSeries number of series in dataTable
	 *  @param pairs data table mapping info
	 *  @param other is not currently used
	 *  */
	public JFreeChart getTableXYAreaChart(String title, String xLabel, String yLabel, JTable table, int numberOfSeries, int[][] pairs, String other){
		dataTable = table;
		columnModel = dataTable.getColumnModel();
		no_series = numberOfSeries;

		setOtherCondition(other);
		setArrayFromTable(numberOfSeries, pairs);
		TableXYDataset dataset = createTableXYDataset();
		JFreeChart chart = createTableXYAreaChart(title, xLabel, yLabel, dataset);
		return chart;
	}

	/**
	 *  given XY data, this method will generate a YInterval Chart
	 *
	 *  @param title chart title
	 *  @param xlabel X axis label
	 *  @param yLabel Y axis label
	 *  @param table chart data
	 *  @param numberOfSeries number of series in dataTable
	 *  @param pairs data table mapping info
	 *  @param other is not currently used
	 *  */
	public JFreeChart getYIntervalChart(String title, String xLabel, String yLabel, JTable table, int numberOfSeries, int[][] pairs, String other){
		dataTable = table;
		columnModel = dataTable.getColumnModel();
		no_series = numberOfSeries;

		setOtherCondition(other);
		setArrayFromTable(numberOfSeries, pairs);
		IntervalXYDataset dataset = createIntervalXYDataset();
		JFreeChart chart = createYIntervalChart(title, xLabel, yLabel, dataset);
		return chart;
	}

	/**
	 *  given category data, this method will generate a BoxAndWhisker Chart
	 *
	 *  @param title chart title
	 *  @param xlabel X axis label
	 *  @param yLabel Y axis label
	 *  @param table chart data
	 *  @param numberOfCategory number of Catgory in dataTable
	 *  @param pairs data table mapping info
	 *  @param other is not currently used
	 *  */
	public JFreeChart getBoxAndWhiskerCategoryChart(String title,  String xLabel, String yLabel, JTable table, int numberOfCategory, int[][] pairs, String other){
		dataTable = table;
		columnModel = dataTable.getColumnModel();
     	no_category =  numberOfCategory;

     	setOtherCondition(other);
		setArrayFromTable(no_category, pairs);
		BoxAndWhiskerCategoryDataset dataset = createBoxAndWhiskerCategoryDataset();
		JFreeChart chart = createBoxAndWhiskerChart(title,  xLabel, yLabel, dataset);
		return chart;
	}

	/**
	 *  given XYZ data, this method will generate a Bubble Chart
	 *
	 *  @param title chart title
	 *  @param xlabel X axis label
	 *  @param yLabel Y axis label
	 *  @param table chart data
	 *  @param numberOfSeries number of Series in dataTable
	 *  @param pairs data table mapping info
	 *  @param other is not currently used
	 *  */
	public JFreeChart getXYZBubbleChart(String title, String xLabel, String yLabel, JTable table, int numberOfSeries, int[][] pairs, String other){
		dataTable = table;
		columnModel = dataTable.getColumnModel();
		no_series = numberOfSeries;

		setOtherCondition(other);
		XYZDataset dataset = createXYZDatasetFromTable(numberOfSeries, pairs);
		JFreeChart chart = createXYZBubbleChart(title, xLabel, yLabel, dataset);
		return chart;
	}

	/**
	 *  given a sigle data, this method will generate a compass Chart
	 *
	 *  @param title chart title
	 *  @param table chart data
	 *  @param numberOfSeries number of Series  in dataTable
	 *  @param pairs data table mapping info
	 *  @param other is not currently used
	 *  */
	public JFreeChart getCompassChart(String title, JTable table, int numberOfSeries, int[][] pairs, String other){
		dataTable = table;
		columnModel = dataTable.getColumnModel();
		no_series = numberOfSeries;

		setOtherCondition(other);
		ValueDataset dataset = createValueDatasetFromTable(numberOfSeries, pairs);
		JFreeChart chart = createCompassChart(title, dataset);
		return chart;
	}
	//-----------------------------Table ->  XY Array ----------------------------
	/**
	 *
	 */
	private void setArrayFromTable(int numberOfSeries, int[][] pairs){
		int independentVarLength = numberOfSeries;
		int dependentVarLength = numberOfSeries;

		int[] independentVar = new int[independentVarLength];
		int[] dependentVar = new int[dependentVarLength];

		dependentHeaders = new String[dependentVarLength];
		independentHeaders = new String[independentVarLength];

		//independentVars store the column index for indep
	    int indep,dep;
		for (int i = 0; i <numberOfSeries ; i++) {
			//System.out.println("indep=pairs["+i+"][0]="+pairs[i][0]);
			//System.out.println("dep=pairs["+i+"][1]="+pairs[i][1]);
			 indep = pairs[i][0];     //x,  value(pie)
			 dep = pairs[i][1];       //y,  name(pie)
			 independentHeaders[i] = columnModel.getColumn(indep).getHeaderValue().toString().trim();
			 independentVar[i] = indep;

			 dependentHeaders[i] = columnModel.getColumn(dep).getHeaderValue().toString().trim();
			 dependentVar[i] = dep;
		}

		int xLength = 0;
		int yLength = 0;

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<String> yList = new ArrayList<String>();

		try{
			//dependent Y
			yLength = dataTable.getRowCount();
			depValues = new String[yLength][dependentVarLength];
			indepValues = new String[yLength][independentVarLength];

			//dependent Y
			for (int index2 = 0; index2 < dependentVarLength; index2++) {
				yList = new ArrayList<String>();
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k,dependentVar[index2])).trim();
						if (cellValue != null && !cellValue.equals("")) {
							yList.add(cellValue);
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) {
						//System.out.println("Exception: "+e);
					}
				}
				yList.trimToSize();
				for (int i = 0; i < yList.size(); i++) {
					depValues[i][index2] = (String)yList.get(i);
					//System.out.println("depValues["+i+"]["+index2+"]="+depValues[i][index2]);
				}
			}

			xyLength = yList.size();;

			//independents
			for (int index2 = 0; index2 < independentVarLength; index2++) {
				xList = new ArrayList<String>();

				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
					cellValue = ((String)dataTable.getValueAt(k, independentVar[index2])).trim();
					if (cellValue != null && !cellValue.equals("")) {
						xList.add(cellValue);
					}
					else {
						continue; // to the next for
					}
					} catch (Exception e) {
						//System.out.println("Exception: " + e );
					}
				}
				xList.trimToSize();
				for (int i = 0; i < xList.size(); i++) {
					indepValues[i][index2] = (String)xList.get(i);
					//System.out.println("indepValues["+i+"]["+index2+"]="+indepValues[i][index2]);

				}
			}
		}catch (Exception e) {
			System.out.println("Exception In outer catch: " + e );
		}
}

	/**
	 *
	 * @param numberOfSeries
	 * @param pairs
	 * @return
	 */
	private ValueDataset createValueDatasetFromTable(int numberOfSeries, int[][] pairs){
		double[] x;
		int[] xVar = new int[numberOfSeries];

		String[] xHeaders = new String[numberOfSeries];

		//independentVars store the column index for indep

		for (int i = 0; i <numberOfSeries ; i++) {
			//System.out.println("x=pairs["+i+"][0]="+pairs[i][0]);
			 xVar[i] = pairs[i][0];     //x,  value(pie)
			 xHeaders[i] = columnModel.getColumn(xVar[i]).getHeaderValue().toString().trim();
		}

		int xLength = 0;

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();

		xLength = dataTable.getRowCount();
		x = new double[xLength];

		try {
			for (int index2 = 0; index2 < numberOfSeries; index2++) {
				xList = new ArrayList<String>();
				xLength =  0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k,xVar[index2])).trim();
						if (cellValue != null && !cellValue.equals("")) {
							xList.add(xLength , cellValue);
							xLength ++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?
					}
				}
				try{
				for (int i = 0; i < xLength; i++) {
						x[i]= Double.parseDouble((String)xList.get(i));
						//System.out.println("y["+i+"]="+y[i]);
				}
				}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
					}
			}
		}catch (Exception e) {
			System.out.println("Exception In outer catch: " + e );
		}
			ValueDataset dataset = new DefaultValueDataset(new Double(x[0]));
            return dataset;
	}

	/**
	 *
	 * @param numberOfSeries
	 * @param pairs
	 * @return
	 */
	private XYZDataset createXYZDatasetFromTable(int numberOfSeries, int[][] pairs){

		double[] x,y,z;
		int[] xVar = new int[numberOfSeries];
		int[] yVar = new int[numberOfSeries];
		int[] zVar = new int[numberOfSeries];

		String[] yHeaders = new String[numberOfSeries];
		String[] xHeaders = new String[numberOfSeries];
		String[] zHeaders = new String[numberOfSeries];
		//independentVars store the column index for indep

		for (int i = 0; i <numberOfSeries ; i++) {
			//System.out.println("x=pairs["+i+"][0]="+pairs[i][0]);
			//System.out.println("y=pairs["+i+"][1]="+pairs[i][1]);
			//System.out.println("z=pairs["+i+"][1]="+pairs[i][2]);
			 xVar[i] = pairs[i][0];     //x,  value(pie)
			 yVar[i] = pairs[i][1];       //y,  name(pie)
			 zVar[i] = pairs[i][2];
			 xHeaders[i] = columnModel.getColumn(xVar[i]).getHeaderValue().toString().trim();
			 yHeaders[i] = columnModel.getColumn(yVar[i]).getHeaderValue().toString().trim();
			 zHeaders[i] = columnModel.getColumn(zVar[i]).getHeaderValue().toString().trim();
		}

		int xLength = 0;
		int yLength = 0;
		int zLength = 0;

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<String> yList = new ArrayList<String>();
		ArrayList<String> zList = new ArrayList<String>();

		yLength = dataTable.getRowCount();
		y = new double[yLength];
		z = new double[yLength];
		x = new double[yLength];

		try {
			//dependent Y
			for (int index2 = 0; index2 < numberOfSeries; index2++) {
				yList = new ArrayList<String>();
				yLength =  0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k,yVar[index2])).trim();
						if (cellValue != null && !cellValue.equals("")) {
							yList.add(yLength , cellValue);
							yLength ++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?
					}
				}
				try{
				for (int i = 0; i < yLength; i++) {
						y[i]= Double.parseDouble((String)yList.get(i));
						//System.out.println("y["+i+"]="+y[i]);
				}
				}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
					}
			}

			xyLength = yLength;

			for (int index2 = 0; index2 < numberOfSeries; index2++) {
				zList = new ArrayList<String>();
				zLength =0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k,zVar[index2])).trim();
						if (cellValue != null && !cellValue.equals("")) {
							zList.add(zLength, cellValue);
							zLength ++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?
					}
				}
				try{
				for (int i = 0; i < zLength; i++) {
					z[i]= Double.parseDouble((String)zList.get(i));
					//System.out.println("z["+i+"]="+z[i]);
				}
				}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
					}
			}

			for (int index2 = 0; index2 < numberOfSeries; index2++) {
				xList = new ArrayList<String>();
				xLength = 0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
					cellValue = ((String)dataTable.getValueAt(k, xVar[index2])).trim();
					if (cellValue != null && !cellValue.equals("")) {
						xList.add(xLength , cellValue);
						xLength++;
					}
					else {
						continue; // to the next for
					}
					} catch (Exception e) {
					}
				}
				try{
					for (int i = 0; i < xLength; i++) {
						x[i]= Double.parseDouble((String)xList.get(i));
						//System.out.println("x["+i+"]="+x[i]);

					}
				}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
				   }
			}
		}catch (Exception e) {
			System.out.println("Exception In outer catch: " + e );
		}

		int len = Array.getLength(x);

	/*	Object[] xData = new Object[len];
		Object[] yData = new Object[len];
		Object[] zData = new Object[len];

		for (int i=0; i<len; i++){
			xData[i]=new Double(x[i]);
			yData[i]=new Double(y[i]);
			zData[i]=new Double(z[i]);
		}
		// create the dataset...
		DefaultContourDataset dataset = new DefaultContourDataset((Comparable)xHeaders[0].substring(0,xHeaders[0].indexOf(":")), xData, yData, zData);*/
		
		
		double[][] data = new double[3][len];
		for (int i=0; i<len; i++){
			data[0][i]=x[i];
			data[1][i]=y[i];
			data[2][i]=z[i];
		}
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		dataset.addSeries((Comparable)xHeaders[0].substring(0,xHeaders[0].indexOf(":")), data);

        return dataset;
	}

	/**
	 *
	 * @param otherCondition
	 */
	private void setOtherCondition(String otherCondition){
		if (otherCondition==null || otherCondition.length()== 0){
		dimension = "2D";
		rotation = "";
		orientation = PlotOrientation.VERTICAL;
		layout = "";
		timeType="";
		lineCondition ="";
		bin_size =1;
		return;
		}

	if (otherCondition.toLowerCase().indexOf("3d")!=-1)
			dimension = "3D";
	else dimension="2D";

	if (otherCondition.toLowerCase().indexOf("counter_clockwise")!=-1)
		rotation = "counter_clockwise";
	else if (otherCondition.toLowerCase().indexOf("clockwise")!=-1)
		rotation = "clockwise";
	else if (otherCondition.toLowerCase().indexOf("ring")!=-1)
		rotation = "ring";
	else rotation= "";

	if (otherCondition.toLowerCase().indexOf("horizontal")!=-1)
		orientation= PlotOrientation.HORIZONTAL ;
	else if (otherCondition.toLowerCase().indexOf("vertical")!=-1)
		orientation = PlotOrientation.VERTICAL;
	else orientation = PlotOrientation.VERTICAL;

	if (otherCondition.toLowerCase().indexOf("stacked")!=-1)
		layout = "stacked";
	else if (otherCondition.toLowerCase().indexOf("layered")!=-1)
		layout = "layered";
	else if (otherCondition.toLowerCase().indexOf("waterfall")!=-1)
		layout = "waterfall";
	else layout = "";

	if (otherCondition.toLowerCase().indexOf("year")!=-1)
		timeType="year";
	else if (otherCondition.toLowerCase().indexOf("month")!=-1)
		timeType="month";
	else if (otherCondition.toLowerCase().indexOf("day")!=-1)
		timeType="day";
	else if (otherCondition.toLowerCase().indexOf("minute")!=-1)
		timeType="minute";
	else timeType= "";

	lineCondition ="";
	if (otherCondition.toLowerCase().indexOf("noline")!=-1)
		lineCondition+=" noline";
	if (otherCondition.toLowerCase().indexOf("nofill")!=-1)
		lineCondition+=" nofill";
	if (otherCondition.toLowerCase().indexOf("noshape")!=-1)
	    lineCondition+=" noshape";

	try {
		bin_size= Integer.parseInt(otherCondition);
	}
	catch(NumberFormatException e){
		bin_size = 1;
	}
	}
/***************************************  create Dataset  ************************************************************/
	private DefaultPieDataset createPieDataset() {
				try{

				double[][] x= new double[xyLength][1];
				String[][] y= new String[xyLength][1];

				for (int index=0; index<1; index++)
					for (int i = 0; i < xyLength; i++){
						//System.out.println("x["+i+"]["+index+"]="+indepValues[i][index]);
						if (indepValues[i][index]==null || indepValues[i][index]=="null")
							x[i][index]=0.0;
						else x[i][index] = Double.parseDouble(indepValues[i][index]);
					}

				for (int index=0; index<1; index++)
					for (int i = 0; i < xyLength; i++) {
						//System.out.println("y["+i+"]["+index+"]="+depValues[i][index]);
						y[i][index] = depValues[i][index];
					}


	            // create the dataset...
				final DefaultPieDataset dataset = new DefaultPieDataset();
				for (int i=0; i<xyLength; i++){
					dataset.setValue(y[i][0], x[i][0]);
					//System.out.println("adding:"+y[i][0]+","+x[i][0]);

				}
				return dataset;
				}catch(NumberFormatException e)
					{
						System.out.println("Data format error!");
						return null;}

	      }

	 private XYDataset createXYNDDataset(){
		 	String[][] x= new String[xyLength][no_series];
			double[][] y= new double[xyLength][no_series];


			for (int index=0; index<no_series; index++)
				for (int i = 0; i < xyLength; i++)
					x[i][index] = indepValues[i][index];

			for (int index=0; index<no_series; index++)
				for (int i = 0; i < xyLength; i++)
					y[i][index] = Double.parseDouble(depValues[i][index]);

			double mean = Double.parseDouble(x[0][0]) ;
			double stdDev = y[0][0];
			Function2D normal = new NormalDistributionFunction2D(mean, stdDev);
			XYDataset dataset = DatasetUtilities.sampleFunction2D(normal, -10.0,
                10.0, 100, "Normal");

        return dataset;
	 }

	 private XYDataset createXYDataset_Time(String time_type){
		 //System.out.println("timetype="+time_type);
		 String[][] x= new String[xyLength][no_series];
			double[][] y= new double[xyLength][no_series];

			for (int index=0; index<no_series; index++)
				for (int i = 0; i < xyLength; i++)
					x[i][index] = indepValues[i][index];

			for (int index=0; index<no_series; index++)
				for (int i = 0; i < xyLength; i++)
					y[i][index] = Double.parseDouble(depValues[i][index]);

			TimeSeriesCollection dataset = new TimeSeriesCollection();
			TimeSeries series;

			for (int i=0; i<no_series; i++){
				String serieName = independentHeaders[i].substring(0, independentHeaders[i].lastIndexOf(":"));
				if (time_type.equalsIgnoreCase("Day"))
					series = new TimeSeries(serieName, Day.class);
				else if (time_type.equalsIgnoreCase("Minute"))
					series = new TimeSeries(serieName, Minute.class);
				else if (time_type.equalsIgnoreCase("Month"))
					series = new TimeSeries(serieName, Month.class);
				else series = new TimeSeries(serieName);

				for (int j=0; j<xyLength; j++){
					if (time_type.equalsIgnoreCase("Day"))
						series.add(DateParser.parseDay(x[j][i]), y[j][i]);
					else if (time_type.equalsIgnoreCase("Minute"))
						series.add(DateParser.parseMinute(x[j][i]), y[j][i]);
					else if (time_type.equalsIgnoreCase("Month"))
						series.add(DateParser.parseMonth(x[j][i]), y[j][i]);
					//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
				}
				dataset.addSeries(series);
			}

			return dataset;
	 }

	private XYDataset createXYDataset_Index(){
		 String[] raw_x= new String[xyLength];
		 double[] raw_xvalue = new double[xyLength];
		 int row_count = 0;
		
		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++) {
				raw_x[i] = indepValues[i][index];
				//System.out.println("raw_x="+raw_x[i]);
				try{					
					raw_xvalue[row_count]= Double.parseDouble(raw_x[i]);
					row_count ++;
				}catch(Exception e)
				{
				 System.out.println("wrong data " +raw_x[i]);
				}
			}
	   
		double[] y_freq= new double[row_count];
		
		for (int i =0; i<row_count; i++){				
			y_freq[i]=i+1;
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("X");
		for (int i=0; i<row_count;i++)
			  series1.add(y_freq[i], raw_xvalue[i]);
		dataset.addSeries(series1);
	
        return dataset; 
	
	}
	
	private XYDataset createXYDataset_Dot(){
		 String[] raw_x= new String[xyLength];
		 double[] raw_xvalue = new double[xyLength];
		 int row_count = 0;
		
		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++) {
				raw_x[i] = indepValues[i][index];
				//System.out.println("raw_x="+raw_x[i]);
				try{					
					raw_xvalue[row_count]= Double.parseDouble(raw_x[i]);
					row_count ++;
				}catch(Exception e)
				{
				 System.out.println("wrong data " +raw_x[i]);
				}
			}
	   
		double[] y_freq= new double[row_count];
		
		for (int i =0; i<row_count; i++){				
			y_freq[i]=0.1;
		}
		for (int i=1; i<row_count; i++){			
			for (int j = 0; j<i; j++)
				if (raw_xvalue[i]==raw_xvalue[j])
					y_freq[i]+= 0.1;
		}
	
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("X");
		for (int i=0; i<row_count;i++)
			  series1.add(y_freq[i], raw_xvalue[i]);
		dataset.addSeries(series1);
	
       return dataset; 
	
	}

	private XYDataset createXYDataset(){

		String[][] x= new String[xyLength][no_series];
		double[][] y= new double[xyLength][no_series];

		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++)
				x[i][index] = indepValues[i][index];

		try{
			for (int index=0; index<no_series; index++)
				for (int i = 0; i < xyLength; i++)
					if (depValues[i][index]!="null")
						y[i][index] = Double.parseDouble(depValues[i][index]);
		}catch(NumberFormatException e)
			{
				System.out.println("Data format error!");
				return null;}

		// create the dataset...
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series;

		//dependent
		try{
		for (int i=0; i<no_series; i++){
			String serieName = independentHeaders[i].substring(0, independentHeaders[i].lastIndexOf(":"));
			series = new XYSeries(serieName);
			for (int j=0; j<xyLength; j++){
				if (x[j][i]!="null"&&x[j][i]!="NaN")
					series.add(Double.parseDouble(x[j][i]), y[j][i]);
				//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
			}
			dataset.addSeries(series);
		}}catch(NumberFormatException e)
			{
				System.out.println("Data format error!");
				return null;}

        return dataset;
	}
	private XYDataset createXYQQDataset(){
		int row_count = xyLength;
		String[] raw_y= new String[row_count];

		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++)
				if (depValues[i][index]!="null") {
					raw_y[i] = depValues[i][index];
				}
		//"""POISSON"
		do_normalQQ(raw_y, row_count, "NORMAL");

		// create the dataset...
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series;

		series = new XYSeries("QQ");
		double min_x = Math.min(normalQuantiles[0], stdResiduals[0]);
		min_x = min_x-0.125;
		double max_x = Math.max (normalQuantiles[xyLength-1],stdResiduals[xyLength-1]);
		max_x = max_x+0.125;

		for (int j=0; j<xyLength; j++){
			series.add(normalQuantiles[j], stdResiduals[j]);
		}
		dataset.addSeries(series);

		XYSeries series2 = new XYSeries("Reference Line");
		series2.add(min_x,min_x);
		series2.add(max_x,max_x);

		dataset.addSeries(series2);
        return dataset;
	}
	private XYDataset createXYQQDDDataset(){
		int row_count = xyLength;
		String[] raw_x= new String[row_count];
		String[] raw_y= new String[row_count];

		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++) {
				raw_x[i] = indepValues[i][index];
			}

		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++)
				if (depValues[i][index]!="null")
					raw_y[i] = depValues[i][index];

		do_ddQQ(raw_x, raw_y, row_count);

		// create the dataset...
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series;

		series = new XYSeries("QQ");

		for (int j=0; j<xyLength; j++){
			series.add(normalQuantiles[j], stdResiduals[j]);
		}
		dataset.addSeries(series);
		try{
		XYSeries series2 = new XYSeries("Reference Line");
		RegressionLine refLine = new RegressionLine(normalQuantiles, stdResiduals);
		double intercept = refLine.getIntercept();
		double slope = refLine.getSlope();

		double min_x = Math.min(normalQuantiles[0], stdResiduals[0]);
		double min_y = slope*min_x +intercept;

		double max_x = Math.max (normalQuantiles[xyLength-1],stdResiduals[xyLength-1]);
		double max_y = slope*max_x+intercept;

		series2.add(min_x,min_y);
		series2.add(max_x,max_y);

		dataset.addSeries(series2);
		}	catch(DataException e){
			System.out.println("cought dataException" );
			e.printStackTrace();
		}

        return dataset;
	}

	private TableXYDataset createTableXYDataset(){
		String[][] x= new String[xyLength][no_series];
		double[][] y= new double[xyLength][no_series];

		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++)
				x[i][index] = indepValues[i][index];


		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++) {
				if (depValues[i][index]=="null")
					y[i][index]=0.0;
				else  y[i][index] = Double.parseDouble(depValues[i][index]);
			}

		// create the dataset...
		DefaultTableXYDataset dataset = new DefaultTableXYDataset();
		XYSeries series;

		for (int i=0; i<no_series; i++){
			series = new XYSeries(independentHeaders[i], true, false);
			for (int j=0; j<dataTable.getRowCount()-1; j++){
				series.add(Double.parseDouble(x[j][i]), y[j][i]);
				//System.out.println("adding :("+x[j][i]+","+y[j][i]+","+independentHeaders[i]+")" );
			}
			dataset.addSeries(series);
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

	private  CategoryDataset createCategoryDataset() {

			double[][] x = new double[xyLength][no_category];
			String[][] y = new String[xyLength][1];   // series name

			try{
				for (int index=0; index<no_category; index++)
					for (int i = 0; i < xyLength; i++)
						x[i][index] = Double.parseDouble(indepValues[i][index]);

				for (int index=0; index<1; index++)
					for (int i = 0; i < xyLength; i++)
						y[i][index] = depValues[i][index];

				// create the dataset...
				final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

				//dependent
				for (int j=0; j<xyLength; j++)
					for (int i=0; i<no_category; i++){
						dataset.addValue(x[j][i], y[j][0], independentHeaders[i]);
					//					System.out.println("adding :("+x[j][i]+","+y[j][0]+","+independentHeaders[i]+")" );
				}

            return dataset;
			}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
					return null;}

	}

    private CategoryDataset createCategoryDataset_Time(String time_type){
    		String[][] x = new String[xyLength][no_category];
		String[][] y = new String[xyLength][1];


		for (int index=0; index<no_category; index++)
			for (int i = 0; i < xyLength; i++)
				x[i][index] = indepValues[i][index];

		for (int index=0; index<1; index++)
			for (int i = 0; i < xyLength; i++)
				y[i][index] = depValues[i][index];

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		//dependent
		for (int j=0; j<xyLength; j++)
			for (int i=0; i<no_category; i++){
				if (x[j][i]!="NaN"){
					if (time_type.equalsIgnoreCase("Day")){
						Day time = DateParser.parseDay(x[j][i]);
						dataset.addValue(new Long(time.getMiddleMillisecond()), y[j][0], independentHeaders[i]);
					} else if (time_type.equalsIgnoreCase("Month")){
						Month time = DateParser.parseMonth(x[j][i]);
						dataset.addValue(new Long(time.getMiddleMillisecond()), y[j][0], independentHeaders[i]);
					}else if (time_type.equalsIgnoreCase("Minute")){
						Minute time = DateParser.parseMinute(x[j][i]);
						dataset.addValue(new Long(time.getMiddleMillisecond()), y[j][0], independentHeaders[i]);
					}
				}
			}

		return dataset;
    }

    private  CategoryDataset createCategoryStatDataset() {
     	double[][] xmean = new double[xyLength][no_category];
		double[][] xstdDev =  new double[xyLength][no_category];
		String[][] y = new String[xyLength][1];
		String [] ss = new String[2] ;

		try{
		for (int index=0; index<no_category; index++)
			for (int i = 0; i < xyLength; i++) {
				StringTokenizer st = new StringTokenizer(indepValues[i][index],DELIMITERS);
				ss[0]= st.nextToken();
				ss[1]= st.nextToken();
				//				ss = indepValues[i][index].split("( *)+,+( *)");
				xmean[i][index] = Double.parseDouble(ss[0]);
				xstdDev[i][index] = Double.parseDouble(ss[1]);
				}


		for (int index=0; index<1; index++)
			for (int i = 0; i < xyLength; i++)
					y[i][index] = depValues[i][index];

		// create the dataset...
		final DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();

			//dependent
			for (int j=0; j<xyLength; j++)
				for (int i=0; i<no_category; i++){
					dataset.add(xmean[j][i], xstdDev[j][i], y[j][0], independentHeaders[i]);
					//System.out.println("adding :("+x[j][i]+","+y[j]+","+independentHeaders[i]+")" );
				}

            return dataset;
			}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
					return null;}
    }

    private  CategoryDataset createCategoryStatRawDataset() {

		String[][] x= new String[xyLength][no_category];
		String[][] y= new String[xyLength][1];

		for (int index=0; index<no_category; index++)
			for (int i = 0; i < xyLength; i++)
			   x[i][index] = indepValues[i][index];


		for (int index=0; index<1; index++)
			for (int i = 0; i < xyLength; i++)
				y[i][index] = depValues[i][index];

		int SERIES_COUNT = xyLength;
		int CATEGORY_COUNT = no_category;

		DefaultStatisticalCategoryDataset dataset
			= new DefaultStatisticalCategoryDataset();

		String[][] values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];

		for (int s = 0; s < SERIES_COUNT; s++) {
        for (int c = 0; c < CATEGORY_COUNT; c++) {
            Double[] values = createValueList(x[s][c]);
			values_storage[s][c]= x[s][c];
			double mean = Statistics.calculateMean(values);
			double stdDev = Statistics.getStdDev(values);
            dataset.add(mean, stdDev, y[s][0], independentHeaders[c]);
        }
	}

        return dataset;
    }

    private  BoxAndWhiskerCategoryDataset createBoxAndWhiskerCategoryDataset() {
    		String[][] x= new String[xyLength][no_category];
		String[][] y= new String[xyLength][1];

		for (int index=0; index<no_category; index++)
			for (int i = 0; i < xyLength; i++)
			   x[i][index] = indepValues[i][index];


		for (int index=0; index<1; index++)
			for (int i = 0; i < xyLength; i++)
				y[i][index] = depValues[i][index];

		// create the dataset...
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		int SERIES_COUNT = xyLength;
		int CATEGORY_COUNT = no_category;
		String[][] values_storage = new String[SERIES_COUNT][CATEGORY_COUNT];
		
		for (int s=0; s<SERIES_COUNT; s++)
			for (int c=0; c<CATEGORY_COUNT; c++){
				dataset.add(createList(x[s][c]), y[s][0], independentHeaders[c]);
				values_storage[s][c] = x[s][c];				
			}

        return dataset;
    }

    private IntervalXYDataset createIntervalXYDataset(){
    	 	double[] x;
		double[] y1, y2;

		y1 = new double[xyLength];
		y2 = new double[xyLength];
		x = new double[xyLength];
		String yy[] = new String[2];


		for (int i = 0; i < xyLength; i++)
				x[i]= Double.parseDouble(indepValues[i][0]);


		for (int i = 0; i < xyLength; i++) {
			StringTokenizer st = new StringTokenizer(depValues[i][0],DELIMITERS);
			yy[0]= st.nextToken();
			yy[1]= st.nextToken();
			//yy = depValues[i][0].split("( *)+,+( *)") ;
			y1[i] = Double.parseDouble(yy[0]);
			y2[i] = Double.parseDouble(yy[1]);
		}

        return new SimpleIntervalXYDataset2(xyLength, x, y1, y2);
    }

    // for histogram
    private IntervalXYDataset createIntervalXYDataset(int bin_size){

		int row_count = xyLength;
		String[] raw_x= new String[row_count];

		for (int index=0; index<1; index++)
			for (int i = 0; i < xyLength; i++) {
				raw_x[i] = depValues[i][index];
			}

		double[] raw_xvalue = new double[row_count];

     	try{
     		for (int i=0; i<row_count; i++){
     			raw_xvalue[i]= Double.parseDouble(raw_x[i]);
     		}
     		}
		catch(NumberFormatException e)
			{
			System.out.println("Data format error!");
			}

		double min_x=1000;
		double max_x=-1000;
		int max_y=0;

		for (int i =0; i<row_count; i++){
			if (raw_xvalue[i]>max_x)
				max_x= raw_xvalue[i];
			if (raw_xvalue[i]<min_x)
				min_x= raw_xvalue[i];
		}

		int max_bin = (int)Math.round((max_x-min_x))+1;
		int space_bin = (int)Math.round(max_bin/10);
		//binSlider.setMaximum(max_bin);
		//binSlider.setMajorTickSpacing(space_bin);
		int bin_count = (int)(max_bin/bin_size)+1;

		double[] y_freq= new double[bin_count];
		for (int i =0; i<row_count; i++){
			int j = (int)((raw_xvalue[i]-min_x)/bin_size);
			y_freq[j]+=1;
		}

		double[] x_start = new double[bin_count];
		double[] x_end = new double[bin_count];

		for (int i=0; i<bin_count; i++){
		   x_start[i]=min_x+i*bin_size;
		   x_end[i]=x_start[i]+bin_size;
		}

		IntervalXYDataset dataset = new SimpleIntervalXYDataset(bin_count, x_start, x_end, y_freq);

        return dataset;
    }

    private IntervalXYDataset createIntervalXYDataset_Time(String timeType){
     	String[][] x = new String[xyLength][no_series] ;
		double[][] y = new double[xyLength][no_series];


		for (int index=0; index<no_series; index++){
			for (int i = 0; i < xyLength; i++)
				x[i][index] = indepValues[i][index];
		}

		for (int index=0; index<no_series; index++)
			for (int i = 0; i < xyLength; i++)
				y[i][index] = Double.parseDouble(depValues[i][index]);


		TimeSeriesCollection collection = new TimeSeriesCollection();
		TimeSeries series;
		SimpleDateFormat df = new SimpleDateFormat();

		for (int ind =0; ind<no_series; ind++){

			int start_ind   = independentHeaders[ind].lastIndexOf(":");
			if 	(start_ind< 0)
				start_ind =0;
			int start_dep   = dependentHeaders[ind].lastIndexOf(":");
			if 	(start_dep< 0)
				start_dep =0;

			String serieName = independentHeaders[ind].substring(0, start_ind);
			if (serieName.length()==0)
				serieName="Serie"+ind;
			String indName = independentHeaders[ind].substring(start_ind+1);
			String depName = dependentHeaders[ind].substring(start_dep+1);

			try{
				if (timeType.equalsIgnoreCase("year")){
					series = new TimeSeries(serieName,Year.class);
					for (int i=0; i<xyLength; i++)
						series.add(new Year(Integer.parseInt(x[i][ind])), y[i][ind]);
					//System.out.println("adding:"+serieName);
				//	collection.setDomainIsPointsInTime(false);
					collection.addSeries(series);
				}else if (timeType.equalsIgnoreCase("month")){
					series = new TimeSeries(serieName, Month.class);
					for (int i=0; i<xyLength; i++)
						series.add(DateParser.parseMonth(x[i][ind]), y[i][ind]);
//					System.out.println("adding:"+serieName);
					//collection.setDomainIsPointsInTime(false);
					collection.addSeries(series);
				}else if (timeType.equalsIgnoreCase("day")){
					series = new TimeSeries(serieName, Day.class);
					for (int i=0; i<xyLength; i++)
						series.add(DateParser.parseDay(x[i][ind]), y[i][ind]);
//					System.out.println("adding:"+serieName);
					//collection.setDomainIsPointsInTime(false);
					collection.addSeries(series);
				}
				else if (timeType.equalsIgnoreCase("minute")){
					series = new TimeSeries(serieName, Minute.class);
					for (int i=0; i<xyLength; i++)
						series.add(DateParser.parseMinute(x[i][ind]), y[i][ind]);
//					System.out.println("adding:"+serieName);
				//	collection.setDomainIsPointsInTime(false);
					collection.addSeries(series);
				}
			}
			catch(NumberFormatException e2){
					System.out.println("Wrong data format, enter integer for Year please. Check the Mapping also.");
					return null;
				}
			}
		return collection;
    }

//--------- setup chart  -----------------------------------------------------

	 private JFreeChart createPieChart(String titleLabel, PieDataset dataset) {
		// System.out.println("rotation="+rotation);
	        if (dimension.equalsIgnoreCase("3D")){
	        		JFreeChart chart = ChartFactory.createPieChart3D(
		            titleLabel,  // chart title
		            dataset,                // data
		            true,                   // include legend
		            true,
		            false
		        );


		        PiePlot3D plot = (PiePlot3D) chart.getPlot();
		        if (rotation.equalsIgnoreCase("clockwise")){
		        		plot.setStartAngle(290);
		        		plot.setDirection(Rotation.CLOCKWISE);
		        		Rotator rotator = new Rotator(plot);
		             rotator.start();
		        		}
		        else if (rotation.equalsIgnoreCase("counter_clockwise")){
	        		    plot.setStartAngle(290);
	        		    plot.setDirection(Rotation.ANTICLOCKWISE);
	        		    Rotator rotator = new Rotator(plot);
	        	        rotator.start();
	        		}
		        plot.setForegroundAlpha(0.5f);
		        plot.setNoDataMessage("No data to display");
		        return chart;
	        }//end of 3D

	        //2D ring
	        if (rotation.equalsIgnoreCase("ring")){
	        	JFreeChart chart = ChartFactory.createRingChart(
	                    titleLabel,  // chart title
	                    dataset,             // data
	                    false,               // include legend
	                    true,
	                    false
	                );

	                RingPlot plot = (RingPlot) chart.getPlot();
	                plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
	                plot.setNoDataMessage("No data available");
	                plot.setCircular(false);
	                plot.setLabelGap(0.02);
	                return chart;
        }

	        //2D
			JFreeChart chart = ChartFactory.createPieChart(
	            titleLabel,  // chart title
	            dataset,             // data
	            true,                // include legend
	            true,
	            false
	        );
	        TextTitle title = chart.getTitle();
	        title.setToolTipText("A title tooltip!");

	        PiePlot plot = (PiePlot) chart.getPlot();
	        if (rotation.equalsIgnoreCase("clockwise")){
	        		plot.setStartAngle(290);
	        		plot.setDirection(Rotation.CLOCKWISE);
	        		Rotator rotator = new Rotator(plot);
	        		rotator.start();
	        }
	        else if (rotation.equalsIgnoreCase("counter_clockwise")){
	        	     plot.setStartAngle(290);
	        	     plot.setDirection(Rotation.ANTICLOCKWISE);
	        	     Rotator rotator = new Rotator(plot);
	        	     rotator.start();
	        }

	        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
	        plot.setNoDataMessage("No data available");
	        plot.setCircular(false);
	        plot.setLabelGap(0.02);

	        return chart;
	}

  private JFreeChart createXYLineChart(String title, String xLabel, String yLabel, XYDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,   // chart title
            xLabel,                     // domain axis label
            yLabel,                   // range axis label
            dataset,                         // data
            orientation,        // orientation
            true,                           // include legend
            true,                            // tooltips
            false                            // urls
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setNoDataMessage("No data available");

        // customise the renderer...
        XYLineAndShapeRenderer renderer
            = (XYLineAndShapeRenderer) plot.getRenderer();
        // customise the range axis...


        if (lineCondition.indexOf("qq")!=-1){
			renderer.setBaseShapesFilled(true);
			renderer.setSeriesLinesVisible(1, true);
			renderer.setSeriesShapesVisible(1, false);
			renderer.setSeriesLinesVisible(0, false);
			renderer.setSeriesShapesVisible(0, true);

			NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setAutoRangeIncludesZero(false);
	        rangeAxis.setUpperMargin(0);
	        rangeAxis.setLowerMargin(0);

			// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
	        domainAxis.setAutoRangeIncludesZero(false);
	        domainAxis.setUpperMargin(0);
	        domainAxis.setLowerMargin(0);
	        return chart;
        }

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        if (timeType.length()!=0){
        		setDateAxis(plot);
        }else{
        		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
  			domainAxis.setAutoRange(false);
  			domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }



       // System.out.println("lineCondition "+lineCondition);
        if (lineCondition.indexOf("noshape")!=-1)
        		renderer.setBaseShapesVisible(false);
        else  renderer.setBaseShapesVisible(true);

        if (lineCondition.indexOf("noline")!=-1)
        		renderer.setBaseLinesVisible(false);

        if (lineCondition.indexOf("nofill")!=-1){
        		renderer.setBaseShapesFilled(false);
        		renderer.setBaseFillPaint(Color.white);
        		renderer.setDrawOutlines(true);}
         else {
        	 renderer.setBaseShapesFilled(true);
        	 renderer.setUseFillPaint(false);
        	 }

        return chart;
    }

  private JFreeChart createXYDifferenceChart(String title, String xLabel, String yLabel, XYDataset dataset) {
	  JFreeChart chart = ChartFactory.createTimeSeriesChart(
	            title,
	            xLabel, yLabel,
	            dataset,
	            true,  // legend
	            true,  // tool tips
	            false  // URLs
	        );
	        chart.setBackgroundPaint(Color.white);

	        XYPlot plot = chart.getXYPlot();
	        plot.setRenderer(new XYDifferenceRenderer(
	            Color.green, Color.red, false)
	        );
	        plot.setBackgroundPaint(Color.lightGray);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinePaint(Color.white);
	        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

			XYItemRenderer renderer = plot.getRenderer();
			//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
			setDateAxis(plot);

	     /*   ValueAxis domainAxis = new DateAxis("Time");
	        domainAxis.setLowerMargin(0.0);
	        domainAxis.setUpperMargin(0.0);
	        plot.setDomainAxis(domainAxis);
	        plot.setForegroundAlpha(0.5f);  */

			//setXSummary(dataset) X is time;
	        return chart;
  }

  private JFreeChart createXYBarChart(String title, String xLabel, String yLabel, IntervalXYDataset dataset) {

	  if (timeType.length()!=0){
		  JFreeChart chart = ChartFactory.createXYBarChart(
				  title,      // chart title
				  xLabel,                     // domain axis label
				  true,
				  yLabel,                        // range axis label
				  dataset,                    // data
				  orientation,
				  true,                       // include legend
				  true,
				  false
		  );

		  chart.setBackgroundPaint(Color.white);
		  // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		  XYPlot plot = chart.getXYPlot();
		  plot.setRenderer(new ClusteredXYBarRenderer());

		  setDateAxis(plot);
		  plot.setBackgroundPaint(Color.lightGray);
		  plot.setRangeGridlinePaint(Color.white);
		  return chart;
	  }
	  else {
		  JFreeChart chart = ChartFactory.createXYBarChart(
			        title,      // chart title
			        xLabel,                     // domain axis label
			        false,
			        yLabel,                        // range axis label
			        dataset,                    // data
			       orientation,
			        true,                       // include legend
			        true,
			        false
			    );
		  // then customise it a little...

		  chart.setBackgroundPaint(Color.white);

		  XYPlot plot = chart.getXYPlot();
		  plot.setRenderer(new ClusteredXYBarRenderer());
		  XYItemRenderer renderer = plot.getRenderer();

		  NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		  rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		  NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		  domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		  return chart;
	  }

       // OPTIONAL CUSTOMISATION COMPLETED.
}

  private JFreeChart createXYAreaChart(String title, String xLabel, String yLabel, XYDataset dataset) {

      JFreeChart chart = ChartFactory.createXYAreaChart(
			title,
          xLabel, yLabel,
          dataset,
          orientation,
          true,  // legend
          true,  // tool tips
          false  // URLs
      );

      chart.setBackgroundPaint(Color.white);

      XYPlot plot = chart.getXYPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setForegroundAlpha(0.65f);
      plot.setDomainGridlinePaint(Color.white);
      plot.setRangeGridlinePaint(Color.white);

      if (timeType.length()!=0){
    	  	setDateAxis(plot);
      }
      else   {
    	  	ValueAxis domainAxis = plot.getDomainAxis();
    	  	domainAxis.setTickMarkPaint(Color.black);
    	  	domainAxis.setLowerMargin(0.0);
    	  	domainAxis.setUpperMargin(0.0);

    	  	ValueAxis rangeAxis = plot.getRangeAxis();
    	  	rangeAxis.setTickMarkPaint(Color.black);
      }

      //XYItemRenderer renderer = plot.getRenderer();
      //renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
      return chart;
  }

  private JFreeChart createXYSymbolicAxisChart(String title, XYDataset dataset) {
	  SymbolAxis domainAxis = new SymbolAxis("Domain",
              new String[] {"A", "B", "C", "D"});
      SymbolAxis rangeAxis = new SymbolAxis("Range",
              new String[] {"V", "X", "Y", "Z"});
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
		//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

      XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
      JFreeChart chart = new JFreeChart("SymbolicAxis Demo 1", plot);

      if (lineCondition.indexOf("noshape")!=-1)
  		renderer.setBaseShapesVisible(false);
  else  renderer.setBaseShapesVisible(true);

  if (lineCondition.indexOf("noline")!=-1)
  		renderer.setBaseLinesVisible(false);

  if (lineCondition.indexOf("nofill")!=-1){
  		renderer.setBaseShapesFilled(false);
  		renderer.setBaseFillPaint(Color.white);
  		renderer.setDrawOutlines(true);}
   else {
  	 renderer.setBaseShapesFilled(true);
  	 renderer.setUseFillPaint(false);
  	 }

      	renderer.setUseFillPaint(true);
		//renderer.setFillPaint(Color.white);

      return chart;
  }
  private JFreeChart createXYPolarChart(String title,  XYDataset dataset) {

	  // create the chart...
      JFreeChart chart = ChartFactory.createPolarChart(
          title, dataset, true, false, false);

      chart.setBackgroundPaint(Color.white);

      // get a reference to the plot for further customisation...
      PolarPlot plot = (PolarPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.lightGray);

		plot.setRenderer(new SOCRPolarItemRenderer());
		//PolarItemRenderer renderer = plot.getRenderer();
		//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

      // set the range axis to display integers only...
      NumberAxis rangeAxis = (NumberAxis) plot.getAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      return chart;

  }

  private JFreeChart createTableXYAreaChart(String title, String xLabel, String yLabel, TableXYDataset dataset) {
	  JFreeChart chart = ChartFactory.createStackedXYAreaChart(
	            title,      // chart title
	            xLabel,                      // x axis label
	            yLabel,                      // y axis label
	            dataset,                  // data
	           orientation,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );

			XYPlot plot = (XYPlot) chart.getPlot();
	        StackedXYAreaRenderer2 renderer = new StackedXYAreaRenderer2();
	       // renderer.setToolTipGenerator(new StandardXYToolTipGenerator());
	        plot.setRenderer(0, renderer);

	        return chart;
  }
  private JFreeChart createXYStepChart(String title, String xLabel, String yLabel, XYDataset dataset) {
	  JFreeChart chart = ChartFactory.createXYLineChart(
	            title,
	            xLabel,
	            yLabel,
	            dataset,
	           orientation,
	            true,
	            true,
	            false
	        );
	        XYPlot plot = (XYPlot) chart.getPlot();
	        XYStepRenderer renderer = new XYStepRenderer();
	      //  renderer.setStroke(new BasicStroke(2.0f));
	     //   renderer.setToolTipGenerator(new StandardXYToolTipGenerator());
	        renderer.setDefaultEntityRadius(6);
			//renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());

	        plot.setRenderer(renderer);

	        return chart;
  }

  private JFreeChart createCategoryLineChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {
	  // create the chart...
      JFreeChart chart = ChartFactory.createLineChart(
          title,   // chart title
          xLabel,                       // domain axis label
          yLabel,                       // range axis label
          dataset,                         // data
          orientation,        // orientation
          true,                           // include legend
          true,                            // tooltips
          false                            // urls
      );

      chart.setBackgroundPaint(Color.white);

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setRangeGridlinePaint(Color.white);

      // customise the range axis...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // customise the renderer...
      LineAndShapeRenderer renderer
          = (LineAndShapeRenderer) plot.getRenderer();
//    System.out.println("lineCondition "+lineCondition);
      if (lineCondition.indexOf("noshape")!=-1)
      		renderer.setBaseShapesVisible(false);
      else  renderer.setBaseShapesVisible(true);

      if (lineCondition.indexOf("noline")!=-1)
      		renderer.setBaseLinesVisible(false);

      if (lineCondition.indexOf("nofill")!=-1){
      		renderer.setBaseShapesFilled(false);
      		renderer.setBaseFillPaint(Color.white);
      		renderer.setDrawOutlines(true);}
       else {
      	 renderer.setBaseShapesFilled(true);
      	 renderer.setUseFillPaint(false);
      	 }
      /*renderer.setShapesVisible(true);
      renderer.setDrawOutlines(true);
      renderer.setUseFillPaint(true);
      renderer.setFillPaint(Color.white);*/
     // renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

      return chart;

  }
  private JFreeChart createCategoryBarChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {

	//  System.out.println("layout = "+layout);
	  JFreeChart chart;
	  if (dimension.equalsIgnoreCase("3d")){

		  	chart = ChartFactory.createBarChart3D(
	            title,      // chart title
	            xLabel,               // domain axis label
	            yLabel,                  // range axis label
	            dataset,                  // data
	            orientation, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
		  	 CategoryPlot plot = chart.getCategoryPlot();
		        plot.setDomainGridlinesVisible(true);
		        CategoryAxis axis = plot.getDomainAxis();
		        axis.setCategoryLabelPositions(
		                CategoryLabelPositions.createUpRotationLabelPositions(
		                        Math.PI / 8.0));
		        BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
				//renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
		        renderer.setDrawBarOutline(false);
		        return chart;
	  }
	  if (layout.equalsIgnoreCase("stacked")){
		 chart = ChartFactory.createStackedBarChart(
		            title,  // chart title
		            xLabel,                  // domain axis label
		            yLabel,                     // range axis label
		            dataset,                     // data
		            orientation,    // the plot orientation
		            true,                        // legend
		            true,                        // tooltips
		            false                        // urls
		        );
	  }
	  else if (layout.equalsIgnoreCase("waterfall")){
		  chart = ChartFactory.createWaterfallChart(
		            title,
		            xLabel,
		            yLabel,
		            dataset,
		            orientation,
		            true,
		            true,
		            false
		        );
		  CategoryPlot plot = chart.getCategoryPlot();
		  BarRenderer renderer = (BarRenderer) plot.getRenderer();
	      renderer.setDrawBarOutline(false);

	      DecimalFormat labelFormatter = new DecimalFormat("$##,###.00");
	      labelFormatter.setNegativePrefix("(");
	      labelFormatter.setNegativeSuffix(")");
	      renderer.setBaseItemLabelGenerator(
	              new StandardCategoryItemLabelGenerator("{2}", labelFormatter)
	          );
		  renderer.setBaseItemLabelsVisible(true);
	  }
	  else {
		  chart = ChartFactory.createBarChart(
        title,         // chart title
        xLabel,               // domain axis label
        yLabel,                  // range axis label
        dataset,                  // data
        orientation, // orientation
        true,                     // include legend
        true,                     // tooltips?
        false                     // URLs?
    );}

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
    if (layout.equalsIgnoreCase("stacked")){
    		StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator());
    }
    else if (layout.equalsIgnoreCase("layered")) {
        LayeredBarRenderer renderer = new LayeredBarRenderer();
        renderer.setDrawBarOutline(false);
        plot.setRenderer(renderer);
        plot.setRowRenderingOrder(SortOrder.DESCENDING);
    }
    else {
    	     BarRenderer renderer = (BarRenderer) plot.getRenderer();
         renderer.setDrawBarOutline(false);
         }

   /*	CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setCategoryLabelPositions(
        CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
    );*/
    // OPTIONAL CUSTOMISATION COMPLETED.

    return chart;
}

  private JFreeChart createCategorySpiderWebChart(String title, CategoryDataset dataset) {
	  // get a reference to the plot for further customisation...
      SOCRSpiderWebPlot plot = new SOCRSpiderWebPlot(dataset);
      JFreeChart chart = new JFreeChart(
          title, TextTitle.DEFAULT_FONT, plot, false
      );

		LegendTitle legend = new LegendTitle(plot);
      legend.setPosition(RectangleEdge.BOTTOM);

		//renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
      chart.addSubtitle(legend);

      return chart;
  }
  private JFreeChart createCategoryStepChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {
	  CategoryItemRenderer renderer = new CategoryStepRenderer(true);

      CategoryAxis domainAxis = new CategoryAxis(xLabel);
      NumberAxis rangeAxis = new NumberAxis(yLabel);
      CategoryPlot plot = new CategoryPlot(
          dataset, domainAxis, rangeAxis, renderer
      );
      JFreeChart chart = new JFreeChart(title, plot);
      return chart;
  }
  private JFreeChart createCategoryAreaChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {
	  JFreeChart chart = ChartFactory.createAreaChart(
	            title,             // chart title
	            xLabel,               // domain axis label
	            yLabel,                  // range axis label
	            dataset,                  // data
	            orientation, // orientation
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );

	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);

	        CategoryPlot plot = chart.getCategoryPlot();
	        plot.setForegroundAlpha(0.5f);

	        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
	        plot.setBackgroundPaint(Color.lightGray);
	        plot.setDomainGridlinesVisible(true);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinesVisible(true);
	        plot.setRangeGridlinePaint(Color.white);

			AreaRenderer renderer = (AreaRenderer)plot.getRenderer();
	        //renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());

	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        rangeAxis.setLabelAngle(0 * Math.PI / 2.0);
	        // OPTIONAL CUSTOMISATION COMPLETED.

	        return chart;
  }

  private JFreeChart createCategoryBarStatChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {

      // create the chart...
      JFreeChart chart = ChartFactory.createLineChart(
          title, // chart title
          xLabel,                         // domain axis label
          yLabel,                        // range axis label
          dataset,                        // data
          orientation,       // orientation
          true,                           // include legend
          true,                           // tooltips
          false                           // urls
      );

      chart.setBackgroundPaint(Color.white);

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setRangeGridlinePaint(Color.white);

      // customise the range axis...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setAutoRangeIncludesZero(true);

      // customise the renderer...
      StatisticalBarRenderer renderer = new StatisticalBarRenderer();
      renderer.setErrorIndicatorPaint(Color.black);
		//	renderer.setLegendItemLabelGenerator(new SOCRCategorySeriesLabelGenerator());
      plot.setRenderer(renderer);

      // OPTIONAL CUSTOMISATION COMPLETED.
     return chart;
  }
  private JFreeChart createCategoryLineStatChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {
	  // create the chart...
      JFreeChart chart = ChartFactory.createLineChart(
          title,       // chart title
          xLabel,                    // domain axis label
          yLabel,                   // range axis label
          dataset,                   // data
          orientation,  // orientation
          true,                      // include legend
          true,                      // tooltips
          false                      // urls
      );

      // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
      chart.setBackgroundPaint(Color.white);

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setRangeGridlinePaint(Color.white);

      CategoryAxis domainAxis = plot.getDomainAxis();
      domainAxis.setUpperMargin(0.0);
      domainAxis.setLowerMargin(0.0);

      // customise the range axis...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setAutoRangeIncludesZero(true);

      // customise the renderer...
      StatisticalLineAndShapeRenderer renderer
          = new StatisticalLineAndShapeRenderer(true, false);
      plot.setRenderer(renderer);

      // OPTIONAL CUSTOMISATION COMPLETED.
     return chart;
  }
  private JFreeChart createCategoryEventFreqChart_Time(String title, String xLabel, String yLabel, CategoryDataset dataset) {

      JFreeChart chart = ChartFactory.createBarChart(
          title,      // title
          xLabel,                  // domain axis label
          yLabel,                     // range axis label
          dataset,                     // dataset
          orientation,  // orientation
          true,                        // include legend
          true,                        // tooltips
          false                        // URLs
      );

      chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xCC));

      // get a reference to the plot for further customisation...
      CategoryPlot plot = chart.getCategoryPlot();
      plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0f);
      plot.setRangeAxis(new DateAxis(yLabel));
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

  private JFreeChart createYIntervalChart(String title, String xLabel, String yLabel, IntervalXYDataset dataset) {
	  JFreeChart chart = ChartFactory.createScatterPlot(
	            title,  // chart title
	            xLabel,                      // domain axis label
	            yLabel,                      // range axis label
	            dataset,                  // data
	            orientation,
	            true,                     // include legend
	            true,
	            false
	        );

	        XYPlot plot = chart.getXYPlot();
	        plot.setRenderer(new YIntervalRenderer());

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

		}

		else if(dataset.getColumnCount()* dataset.getRowCount()<10){
			domainAxis.setLowerMargin(domainAxis.getLowerMargin()*2);
			domainAxis.setUpperMargin(domainAxis.getUpperMargin()*2);
	   		if (dataset.getColumnCount()==1)
				renderer.setItemMargin(renderer.getItemMargin()*2);
			else
				domainAxis.setCategoryMargin(domainAxis.getCategoryMargin()*2);
		}

        return chart;
    }

  private JFreeChart createXYZBubbleChart(String title, String xLabel, String yLabel, XYZDataset dataset) {
      JFreeChart chart = ChartFactory.createBubbleChart(
			title,
          xLabel,
          yLabel,
          dataset,
          orientation,
          true,
          true,
          false
      );
      XYPlot plot = (XYPlot) chart.getPlot();
      plot.setForegroundAlpha(0.65f);

      XYItemRenderer renderer = plot.getRenderer();
      renderer.setSeriesPaint(0, Color.blue);
		//renderer.setLegendItemLabelGenerator(new SOCRXYZSeriesLabelGenerator());

      // increase the margins to account for the fact that the auto-range
      // doesn't take into account the bubble size...
      NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
      domainAxis.setLowerMargin(0.15);
      domainAxis.setUpperMargin(0.15);
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setLowerMargin(0.15);
      rangeAxis.setUpperMargin(0.15);
      return chart;
  }

  private JFreeChart createCompassChart(String title, ValueDataset dataset) {
      // create the chart...
     CompassPlot plot = new CompassPlot(dataset);
     plot.setSeriesNeedle(7);
     plot.setSeriesPaint(0, Color.red);
     plot.setSeriesOutlinePaint(0, Color.red);
     plot.setRoseHighlightPaint(Color.CYAN);
     JFreeChart chart = new JFreeChart(plot);

     return chart;

	}
//----------------------------------------------------------

  /**
   *
   */
  private void setDateAxis(XYPlot plot){
	  XYItemRenderer renderer = plot.getRenderer();
		StandardXYToolTipGenerator generator;
		if (timeType.equalsIgnoreCase("Year"))
			generator = new StandardXYToolTipGenerator(
	            "{1} = {2}", new SimpleDateFormat("yyyy"), new DecimalFormat("0")
	        );
		else if (timeType.equalsIgnoreCase("Day"))
			generator= new StandardXYToolTipGenerator(
	                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
	                new SimpleDateFormat("d-MMM-yy"), new DecimalFormat("#,##0.00")
	            );
		else if (timeType.equalsIgnoreCase("Month"))
			generator= new StandardXYToolTipGenerator(
	                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
	                new SimpleDateFormat("MM-yy"), new DecimalFormat("#,##0.00")
	            );
	    else
	        generator = new StandardXYToolTipGenerator(
	                      "", DateFormat.getDateInstance(),new DecimalFormat("#,##0.00")
	                  );

	    renderer.setBaseToolTipGenerator(generator);

	    DateAxis domainAxis = new DateAxis("Time");
	    domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        plot.setDomainAxis(domainAxis);
        //plot.setForegroundAlpha(0.5f);
  }

  /** parse string list into double array
   *
   * @param in
   * @return
   */
  private Double[] createValueList(String in){
	   String vs = in;
	   //  String[] values = in.split("( *)+,+( *)");
	   //int count = java.lang.reflect.Array.getLength(values);

	   StringTokenizer st = new StringTokenizer(in,DELIMITERS);
	   int count = st.countTokens();
	   String[] values = new String[count];
	   for (int i=0; i<count; i++)
		   values[i]=st.nextToken();

	   Double[] result = new Double[count];
	   try{
	   for (int i = 0; i < count; i++) {
            double v = Double.parseDouble(values[i]);
            result[i] = new Double(v);
	   }}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
					return null;}


        return result;
    }

  /** parse string list into List
   *
   * @param in
   * @return
   */
 private List<Double> createList(String in){
	   String vs = in;
	   //   String[] values = in.split("( *)+,+( *)");
	   //	   int count = java.lang.reflect.Array.getLength(values);

	   StringTokenizer st = new StringTokenizer(in,DELIMITERS);
	   int count = st.countTokens();
	   String[] values = new String[count];
	   for (int i=0; i<count; i++)
		   values[i]=st.nextToken();

       List<Double> result = new java.util.ArrayList<Double>();
       try{
    	   for (int i = 0; i < count; i++) {
    		   double v = Double.parseDouble(values[i]);
    		   result.add(new Double(v));
    	   }
       }catch(NumberFormatException e){
			System.out.println("Data format error!");
			return null;
       }
       return result;
   }

  /** normal QQ plot
   *
   * @param raw_y
   * @param row_count
   * @param disChoice
   */
  private void do_normalQQ(String[] raw_y, int row_count, String disChoice){
	     String NORMAL="NORMAL";
		 String POISSON="POISSON";
		try{
			//double[] x = new double[row_count];
			double[] y = new double[row_count];

		for (int i=0; i<row_count; i++){
			//	x[i]= Double.parseDouble(raw_x[i]);
			y[i]= Double.parseDouble(raw_y[i]);
		}
		//X
		normalQuantiles = new double[row_count];
		DataCase[] sorted = AnalysisUtility.getSortedRedidual(y);
		for (int i = 0; i < sorted.length; i++) {
				normalQuantiles[i]= sorted[i].getValue()	;
			}
		//Y
		if (disChoice == NORMAL)
			stdResiduals = RandomGenerator.getGeneratedArray(y, RandomGenerator.NORMAL);
		else if (disChoice == POISSON)
				stdResiduals = RandomGenerator.getGeneratedArray(y, RandomGenerator.POISSON);
		sorted = AnalysisUtility.getSortedRedidual(stdResiduals);
		for (int i = 0; i < sorted.length; i++) {
			stdResiduals[i]= sorted[i].getValue()	;
		}

		}catch(DataException e){
			System.out.println(e);
		}catch(NumberFormatException e)
			{
				System.out.println("Data format error!");
			  }
	}

/** Data vs Data  QQ plot
 *
 */
	private void do_ddQQ(String[] raw_x, String[] raw_y, int row_count){
			double[] x = new double[row_count];
			double[] y = new double[row_count];

			try{
				for (int i=0; i<row_count; i++){
					x[i]= Double.parseDouble(raw_x[i]);
					y[i]= Double.parseDouble(raw_y[i]);
				}
			}catch(NumberFormatException e)
				{
					System.out.println("Data format error!");
				 }

			//stdResiduals = RandomGenerator.getGeneratedArray(x, RandomGenerator.NORMAL);
			stdResiduals = AnalysisUtility.getQuantileArray(x);
			DataCase[] sortedResidual = AnalysisUtility.getSortedRedidual(stdResiduals);
			for (int i = 0; i < sortedResidual.length; i++) {
				stdResiduals[i]= sortedResidual[i].getValue()	;
				}

			//normalQuantiles = RandomGenerator.getGeneratedArray(y, RandomGenerator.NORMAL);
			normalQuantiles = AnalysisUtility.getQuantileArray(y);
			DataCase[] sortedQuantiles = AnalysisUtility.getSortedRedidual(normalQuantiles);
			for (int i = 0; i < sortedQuantiles.length; i++) {
				normalQuantiles[i]= sortedQuantiles[i].getValue();
				}

	}

}

