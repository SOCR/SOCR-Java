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

/* 	modified by annie che 200508.
	separate the gui part from the model part
*/

package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.analyses.gui.*;
import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.*;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.example.ExampleData;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.AnovaOneWayExamples;
import edu.ucla.stat.SOCR.util.AnalysisUtility;


import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

/** One-Way ANOVA */
public class AnovaOneWay extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.


	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	private String[] xData = null;
	private double[] yData = null;
	private double[][][] yValue = null;
	private double[] predicted = null;
	private double[] residuals = null;
	private double[] sortedResiduals = null;
	private double[] sortedStandardizedResiduals = null;
	private int[] sortedResidualsIndex = null;
	private double[] sortedNormalQuantiles = null;
	private double[] sortedStandardizedNormalQuantiles = null;

	private String dependentHeader = null, independentHeader = null;

	private String[][] xNameData = null;
	private int seriesCount = 0; // box plot row size
	private int categoryCount = 1; // box plot column size, which is 1 for Anova 1 way.
	private int categoryIndex = 0;
	private String[] seriesName = null;
	private HashMap boxPlotNameValueMap = null;
	
	private AnovaOneWayResult result;
	int dfCTotal, dfError, dfModel;
	String rssModelString, rssErrorString;
	String mssModelString, mssErrorString;
	String rssTotalString, fValueString;
	String  rSquareString;
	double pValue;
	int xLength;
	int yLength;

	private String xAxisLabel = null, yAxisLabel = null, boxPlotTitle = null, residualBoxPlotTitle = null;
	/**Initialize the Analysis*/
	public void init(){
		showInput = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		analysisType = AnalysisType.ANOVA_ONE_WAY;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = new boolean[]{true, true,true,true,true,false,false,false,false,false};//AnovaOneWayExamples.availableExamples;

		useGraph = true;
		onlineDescription = "http://mathworld.wolfram.com/ANOVA.html";
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("One Way ANOVA");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

		
		resetGraph();

		validate();
		//reset();
	}


	/** Create the actions for the buttons */
       protected void createActionComponents(JToolBar toolBar){
           super.createActionComponents(toolBar);
      }


	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){
	}

	/**This method defines the specific statistical Analysis to be carried our on the user specified data. ANOVA is done in this case. */
	public void doAnalysis(){
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}
		if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}
		////////System.out.println("\nSample size=" + dataTable.getRowCount() + " \n" );
		dependentHeader = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		independentHeader = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();

		Data data = new Data();
		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/
		xLength = 0;
		yLength = 0;

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<String> yList = new ArrayList<String>();
		try {
			for (int k =0; k < dataTable.getRowCount(); k++) {
				//////////System.out.println("dataTable = " + dataTable.getValueAt(0, 0));
				try {
					cellValue = ((String)dataTable.getValueAt(k, dependentIndex)).trim();

					if (cellValue != null && !cellValue.equals("")) {
						yList.add(yLength , cellValue);
						yLength++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?
						//////////System.out.println("e = " + e);
					}
			}

			for (int k =0; k < dataTable.getRowCount(); k++) {
				try {
					cellValue = ((String)dataTable.getValueAt(k, independentIndex)).trim();
					if (cellValue != null && !cellValue.equals("")) {
						xList.add(xLength , cellValue);
						xLength++;
					}
					else {
						continue; // to the next for
					}
				} catch (Exception e) {
					showError("Exception: " + e );
				}
			}
		} catch (Exception e) {
			showError("Exception In outer catch: " + e );
		}
		if (yLength <= 0 || xLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}
		xData = new String[xLength];
		yData = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			yData[i] = Double.parseDouble((String)yList.get(i));
			////////System.out.println(" " +yData[i] );
		}
		for (int i = 0; i < xLength; i++) {
			xData[i] = (String)xList.get(i); //Integer.parseInt((String)xList.get(i));
			xData[i] = xData[i].trim();
			////System.out.println(" "+xData[i] );
		}

		data.appendX("X", xData, DataType.FACTOR);
		data.appendY("Y", yData, DataType.QUANTITATIVE);

		result = null;
		try {
			result = (AnovaOneWayResult)data.getAnalysis(AnalysisType.ANOVA_ONE_WAY);

		} catch (Exception e) {
			//////System.out.println("result NullPointerException  = " + e);
		}
		dfCTotal = 0; dfError = 0; dfModel = 0;
		
		residuals = new double[xLength];
		predicted = new double[xLength];

		xAxisLabel = independentHeader;
		yAxisLabel = dependentHeader;
		boxPlotTitle = "Box Plot "+ yAxisLabel + " vs. " + xAxisLabel;
		residualBoxPlotTitle =  "Box Plot Residual vs. " + xAxisLabel;

		updateResults(); //set format in this method
		
		//if (useGraph)
		////////System.out.println("\nAnovaOneWay now call doGraph");
		doGraph();


	}

	public void updateResults(){
		if (result==null)
			return;
		
		result.setDecimalFormat(dFormat);
		try {
			residuals = result.getResiduals();//((double[])result.getTexture().get(AnovaOneWayResult.RESIDUALS));
		} catch (NullPointerException e) {
			//////System.out.println("NullPointerException  = " + e);
		}

		try {
			predicted = result.getPredicted();//((double[])result.getTexture().get(AnovaOneWayResult.PREDICTED));
		} catch (NullPointerException e) {
			////////System.out.println("NullPointerException  = " + e);
		}
		try {
			seriesCount = result.getFactorGroupNumber();//((Integer)(result.getTexture().get(AnovaOneWayResult.BOX_PLOT_ROW_SIZE))).intValue();//Integer.parseInt((String)result.getTexture().get(AnovaOneWayResult.BOX_PLOT_ROW_SIZE));
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
		}
		/*
		try {
			seriesName = (String[])result.getTexture().get(AnovaOneWayResult.BOX_PLOT_ROW_FACTOR_NAME);
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
		}*
		try {
			boxPlotNameValueMap = (HashMap)result.getTexture().get(AnovaOneWayResult.BOX_PLOT_NAME_VALUE_MAP);
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
			e.printStackTrace();
		}
		/*
		try {
			xNameData = (String[][])result.getTexture().get(AnovaOneWayResult.BOX_PLOT_FACTOR_NAME);
		} catch (NullPointerException e) {
			resultPanelTextArea.append("\nException = " + e);
		}
		*/
		try {
			yValue = result.getBoxPlotResponseValues();//(double[][][])result.getTexture().get(AnovaOneWayResult.BOX_PLOT_RESPONSE_VALUE);
		} catch (Exception e) {
			////////System.out.println("\nException = " + e);
		}

		try {
			dfCTotal = result.getDFTotal();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_TOTAL))).intValue();
		} catch (Exception e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			dfError = result.getDFError();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_ERROR))).intValue();
		} catch (Exception e) {
			//////System.out.println("\nException = " + e);
		}


		try {

			dfModel= result.getDFModel();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_MODEL))).intValue();
		} catch (Exception e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			rssModelString= result.getFormattedDouble(result.getRSSModel());//((String)result.getTexture().get(AnovaOneWayResult.RSS_MODEL));
		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}


		try {
			rssErrorString = result.getFormattedDouble(result.getRSSError());//((String)result.getTexture().get(AnovaOneWayResult.RSS_ERROR));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			mssModelString = result.getFormattedDouble(result.getMSSModel());//((String)result.getTexture().get(AnovaOneWayResult.MSS_MODEL));
		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			mssErrorString = result.getFormattedDouble(result.getMSSError());//((String)result.getTexture().get(AnovaOneWayResult.MSS_ERROR));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			rssTotalString = result.getFormattedDouble(result.getRSSTotal());//((String)result.getTexture().get(AnovaOneWayResult.RSS_TOTAL));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			fValueString = result.getFormattedDouble(result.getFValue());//((String)result.getTexture().get(AnovaOneWayResult.F_VALUE));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			pValue = result.getPValue();//((String)result.getTexture().get(AnovaOneWayResult.P_VALUE));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);

		try {
			sortedResiduals = result.getSortedResiduals();//(double[])residualMap.get(AnovaOneWayResult.SORTED_RESIDUALS);
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
		}
		try {
			sortedStandardizedResiduals = result.getSortedStandardizedResiduals();//(double[])residualMap.get(AnovaOneWayResult.SORTED_STANDARDIZED_RESIDUALS);
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
		}

		try {
			sortedResidualsIndex = result.getSortedResidualsIndex();//(int[])residualMap.get(AnovaOneWayResult.SORTED_RESIDUALS_INDEX);
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
		}
		try {
			sortedNormalQuantiles = result.getSortedNormalQuantiles();//(double[])residualMap.get(AnovaOneWayResult.SORTED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
		}
		try {
			sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();//(double[])residualMap.get(AnovaOneWayResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			////////System.out.println("\nException = " + e);
		}
		try {
			rSquareString = result.getFormattedDouble(result.getRSquare());//((String)result.getTexture().get(AnovaOneWayResult.P_VALUE));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}
		
		resultPanelTextArea.setText("\n");//clear first
		resultPanelTextArea.append("\tSample Size = " + xLength + " \n" );
		resultPanelTextArea.append("\n\tIndependent Variable = " + independentHeader );
		resultPanelTextArea.append("\n\tDependent Variable  = " + dependentHeader + " \n" );

		resultPanelTextArea.append("\n\tResults of One-Way Analysis of Variance:");
		//resultPanelTextArea.append(+ dependentHeader + " \n" );

		// Print Standard 1-Way ANOVA, table, according to:
		//	http://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_ANOVA_1Way
		/*
		 * Variance Source 	 Degrees of Freedom (df) 	 Sum of Squares (SS) 	 Mean Sum of Squares (MS) 	 F-Statistics 	 P-value
		 * Treatment Effect (Between Group) 	k-1 	\sum_{i=1}^{k}{n_i(\bar{y}_{i,.}-\bar{y})^2} 	MST(Between)={SST(Between)\over df(Between)} 	F_o = {MST(Between)\over MSE(Within)} 	P(F(df(Between),df(Within)) > Fo)
		 * Error (Within Group) 	n-k 	\sum_{i=1}^{k}{\sum_{j=1}^{n_i}{(y_{i,j}-\bar{y}_{i,.})^2}} 	MST(Within)={SSE(Within)\over df(Within)}
		 * Total 	n-1 	\sum_{i=1}^{k}{\sum_{j=1}^{n_i}{(y_{i,j} - \bar{y})^2}} 	
		 */
		resultPanelTextArea.append("\n\tStandard 1-Way ANOVA Table. See:");
		resultPanelTextArea.append("\n\thttp://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_ANOVA_1Way\n");
		
		resultPanelTextArea.append("\t==============================================================================================\n");
		resultPanelTextArea.append("\tVarianceSource \t DF \t RSS \t MSS \t F-Statistics \t P-value");
		resultPanelTextArea.append("\n\tTreatmentEffect (B/w Groups)"+"\t"+dfModel+"\t"+rssModelString+"\t"+
				mssModelString+"\t "+fValueString+"\t "+AnalysisUtility.enhanceSmallNumber(pValue));
		resultPanelTextArea.append("\n\tError"+"\t\t"+dfError+"\t"+rssErrorString+"\t"+mssErrorString);
		resultPanelTextArea.append("\n\tTotal:"+"\t\t"+dfCTotal+"\t"+rssTotalString);
		resultPanelTextArea.append("\n\t==============================================================================================\n");

		resultPanelTextArea.append("\n\n\tModel:");
		resultPanelTextArea.append("\n\tDegrees of Freedom = " + dfModel);
		resultPanelTextArea.append("\n\tResidual Sum of Squares = " + rssModelString);
		resultPanelTextArea.append("\n\tMean Square Error = " + mssModelString);


		resultPanelTextArea.append("\n\n\tError:");
		resultPanelTextArea.append("\n\tDegrees of Freedom = " + dfError);
		resultPanelTextArea.append("\n\tResidual Sum of Squares = " + rssErrorString);
		resultPanelTextArea.append("\n\tMean Square Error = " + mssErrorString);

		resultPanelTextArea.append("\n\n\tCorrected Total:");
		resultPanelTextArea.append("\n\tDegrees of Freedom = " + dfCTotal);
		resultPanelTextArea.append("\n\tResidual Sum of Squares = " + rssTotalString);
		resultPanelTextArea.append("\n\n\tF-Value = " + fValueString);
		resultPanelTextArea.append("\n\tP-Value = " + AnalysisUtility.enhanceSmallNumber(pValue));

		resultPanelTextArea.append("\n\n\tR-Square = " + rSquareString);

		resultPanelTextArea.setForeground(Color.BLUE);
	}
	/** convert a generic string s to a fixed length one. */
    	public String monoString(String s) {
		String sAdd = new String(s + "                                      ");
		return sAdd.substring(0,14);
    	}

	/** convert a generic double s to a "nice" fixed length string */
    	public String monoString(double s) {
		final double zero = 0.00001;
        	Double sD = new Double(s);
		String sAdd = new String();
		if(s>zero)
	    		sAdd = new String(sD.toString());
		else  sAdd = "<0.00001";

		sAdd=sAdd.toLowerCase();
		int i=sAdd.indexOf('e');
		if(i>0)
	    		sAdd = sAdd.substring(0,4)+"E"+sAdd.substring(i+1,sAdd.length());
		else if(sAdd.length()>10)
				sAdd = sAdd.substring(0,10);

		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}

	/** convert a generic integer s to a fixed length string */
    	public String monoString(int s) {
		Integer sD = new Integer(s);
		String sAdd = new String(sD.toString());
		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}


    /** Implementation of PropertyChageListener.*/
    public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		System.err.println("From RegCorrAnal:: propertyName =" +propertyName +"!!!");

		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));

			System.err.println("From RegCorrAnal:: data UPDATED!!!");
		}
   }
	public Container getDisplayPane() {
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		return this.getContentPane();
	}
	public String getOnlineDescription() {
	    return onlineDescription;
	    //return new String("http://en.wikipedia.org/wiki/Multiple_regression");
    }
   	protected void doGraph() {

		// graph Component is available here
		// data: variables double xData, yData, residuals, predicted are available here after doAnalysis() is run.
		////System.out.println("\nAnovaOneWay doGraph call removeAll");

		graphPanel.removeAll();
		JPanel innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		graphPanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		// 1. Box plot of data: yData vs. xData (where xData is categorical)

		//int seriesCount = 3; // seriesCount
		//int categoryCount= 1;
		//double[][][] yValue = new double[seriesCount][categoryCount][];
		//String[] seriesName = {"1", "2", "3"};
		/*
		String[][] xNameData = new String[seriesCount][categoryCount]; // xNameData: category names.
		xNameData[0] = new String[] {""};
		xNameData[1] = xNameData[0];
		xNameData[2] = xNameData[0];
		*/
		// data is for testing only.
		// the data below gives hallow triagle and circles.
		// static test data only
/*		yValue[0][0] = new double[] {14.67,13.72,13.84,13.90,14.56,13.88,14.30,14.11,13.84,13.90,14.56,13.88, 15, 16, 17};

		yValue[1][0] = new double[] {13.94,14.40,14.14,14.59,13.59,14.24,14.05,11,12,13, 15, 20, 8};

		yValue[2][0] = new double[] {14.24,14.05,14.65,13,10,19,20,10,11,12,23, 20, 23, 24};
*/


		/* the parameters to be passed are:
		1.boxPlotTitle, xAxisLabel, YAxisLabel.
		2. serieCount, categoryCount
		3. seriesname (e.g. sex, race, etc)
		4. category's name (e.g. height, weight, etc)--xData
		5. yValue(double),
		*/
		/*
		JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot of " + dependentHeader + " vs " + independentHeader, independentHeader, dependentHeader, dependentHeader + " Value  " , xData, yData,  "Regression Line", intercept, slope, "");
		ChartPanel chartPanel = new ChartPanel(scatterChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);
		*/



		double xDataDouble[] = new double[xData.length];
		String groupLegend = "";
		boolean useStringLegend = false;
		TreeSet<String> treeSet = new TreeSet<String>();
		try {
			for (int i = 0; i < xData.length; i++) {
				xDataDouble[i] = (new Double(xData[i])).doubleValue();
			}
		} catch (Exception e) {
			useStringLegend = true;
			for (int i = 0; i < xData.length; i++) {
				treeSet.add((String)xData[i]);
			}
		}
		int groupSize = treeSet.size();

		////System.out.println("TreeSet.size() = " + groupSize);

		Iterator<String> iterator = treeSet.iterator();
		int groupIndex = 1;
		String groupName = null;
		while (iterator.hasNext()) {
			groupName = (String)iterator.next();
			//xDataDouble[groupIndex-1] = groupIndex;
			groupLegend += ("\t" + groupName + "="+groupIndex + "  ");
			for (int i = 0; i < xData.length; i++) {
				if (xData[i].equalsIgnoreCase(groupName)){ // xData[i] is a String.
					xDataDouble[i] = (double)groupIndex;
				}
			}
			groupIndex++;

		}

		if (useStringLegend) {
			groupLegend = "Group Names: " + groupLegend;
			groupLegend = groupLegend.substring(0, groupLegend.length()-2);
			//System.out.println("groupLegend = " + groupLegend);
			// 1. scatter plot of data: yData vs. xData
		}


		//JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot", independentHeader, "Residuals", "Residual Value", xData, residuals, "At Residual = 0", 0, 0, "");

		JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot", independentHeader, "Residuals", "Residual Value", xDataDouble, residuals, "   " + groupLegend, 0, 0, "");
		//JFreeChart scatterChart = chartFactory.getLineChart("Scatter Plot", independentHeader, dependentHeader, xDataDouble, yData);//getChart(title, xlabel, ylabel, xdata,ydata)


		ChartPanel chartPanel = new ChartPanel(scatterChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);


		/*
		if (useStringLegend) {
			JPanel labelPanel = new JPanel();
			labelPanel.setBackground(Color.WHITE);
			JLabel legendLabel1 = new JLabel(groupLegend);
			legendLabel1.setBackground(Color.WHITE);
			labelPanel.add(legendLabel1, BorderLayout.NORTH);
			innerPanel.add(labelPanel);
		}
		*/
		// this is only a test for having more than one charts in a boxlayout

		// 1.5. box plot of data
		//mapToArray(boxPlotNameValueMap);
		/*
		JFreeChart boxChart = chartFactory.getBoxAndWhiskerChart(boxPlotTitle, xAxisLabel, yAxisLabel, seriesCount, categoryCount,  seriesName, xNameData, yValue);
		chartPanel = new ChartPanel(boxChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		graphPanel.add(chartPanel);
		*/

		// 2. residual on fit plot: residuals vs. xData
		//JFreeChart rxChart = chartFactory.getLineChart("Residual on Covariate Plot", independentHeader, "Residuals", xDataDouble, residuals);

		JFreeChart rxChart = chartFactory.getQQChart("Residual on Covariate Plot", independentHeader, "Residuals", "Residuals", xDataDouble, residuals,  "   " + groupLegend, 0, 0, "");

		chartPanel = new ChartPanel(rxChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);

		// 3. residual on fit plot: residuals vs. predicted

		//JFreeChart rfChart = chartFactory.getLineChart("Residual on Fit Plot", "Predicted", "Residuals", predicted, residuals);

		JFreeChart rfChart = chartFactory.getQQChart("Residual on Fit Plot", "Predicted " + dependentHeader, "Residuals", "Residuals", predicted, residuals,  "At Residual = 0", 0, 0, "");
		chartPanel = new ChartPanel(rfChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);

		// 4. Normal QQ plot: need residuals and standardized normal scores
		//JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals);
		int len = sortedNormalQuantiles.length;
		double slope = (sortedStandardizedResiduals[len-1]-sortedStandardizedResiduals[0])/(sortedNormalQuantiles[len-1]-sortedNormalQuantiles[0]);
		double y0=sortedStandardizedResiduals[len-1]-slope*sortedNormalQuantiles[len-1];
		
	//	max_x = Math.max (normalQuantiles[row_count-1],stdResiduals[row_count-1]);
		
		JFreeChart qqChart = chartFactory.getQQChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", "Standardized Residual Value  ", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals,  "At Standardized Residual = 0", y0, slope, "noshape");
		//JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals, "noline");

		chartPanel = new ChartPanel(qqChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);


		graphPanel.validate();

	}

	protected void resetGraph()
	{
		chartFactory = new Chart();
		JFreeChart chart = chartFactory.createChart(); // an empty  chart
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(400,300));
		graphPanel.removeAll();
		graphPanel.add(chartPanel);
		graphPanel.validate();

	}
	protected void showError(String errorString) {
		resultPanelTextArea.append(errorString);
	}

    //public String getOnlineHelp(){
    //    return new String("http://en.wikipedia.org/wiki/Statistical_analysis");
    //}	// Provided in Analysis.java

	public void mapToArray(HashMap<String,ArrayList<String>> map) { // used by AnovaTwoWay too??
		////////System.out.println("GUI AnovaOneWay start mapToArray map = "+ map);

		int rowSize = map.size();
		////////System.out.println("GUI AnovaOneWay rowSize = "+ rowSize);

		//int colSize = 1; // no column actually.
		yValue= new double[rowSize][categoryCount][];
		seriesName = new String[rowSize];
		int rowIndex = 0;
		Set<String> xKeySet = map.keySet();
		Iterator<String> xIterator = xKeySet.iterator();
		String xKey = null;

		ArrayList<String> xArrayList = null; //( (ArrayList) xMap.get(xKey) );
		//xKeyInt = Integer.parseInt(xKey);
		//int groupIndex = 0;
		int arraySize = 0;
		while (xIterator.hasNext()) {
			xKey = (String)xIterator.next();
			seriesName[rowIndex] = xKey;
			xArrayList = ( (ArrayList<String>) map.get(xKey) );
			arraySize = xArrayList.size();
			////////System.out.println("GUI AnovaOneWay arraySize = "+ arraySize);
			yValue[rowIndex][categoryIndex] = new double[arraySize];
			////////System.out.println("GUI AnovaOneWayValue = "+ yValue[rowIndex][categoryIndex]);
			for (int j = 0; j < arraySize; j++) {
				//////////System.out.println("GUI AnovaOneWay in xArrayList.get(j) = " + Double.parseDouble( (String) xArrayList.get(j) ) );
				yValue[rowIndex][categoryIndex][j] = Double.parseDouble( (String) xArrayList.get(j) );

				////////System.out.println("GUI AnovaOneWay " + " In mapToArray current entry[" +j + "] = " +Double.parseDouble((String) xArrayList.get(j)));

			}
			rowIndex++;
			////////System.out.println("Model AnovaOneWay rowIndex = " + rowIndex);
		}
		//return yValue;
	}

}


