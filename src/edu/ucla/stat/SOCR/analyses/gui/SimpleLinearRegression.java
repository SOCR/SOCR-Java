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

/* 	modified annie che 200508.
	separate the gui part from the model part
*/

/*
	should change all the var to Java convention
*/


package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.beans.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.gui.Chart;
import edu.ucla.stat.SOCR.analyses.xml.XMLComposer;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.example.SimpleLinearRegressionExamples;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

/** this class is for Simple Linear Regression only. */
public class SimpleLinearRegression extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.
	// Perhaps I will figure out a better way to do it. annieche.

	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	private double[] xData = null;
	private double[] yData = null;
	private double[] predicted = null;
	private double[] predictedUpper = null;
	private double[] predictedLower = null;
	private double[] varPredict = null;
	private double[] sdPredict = null;

	private double[] residuals = null;
	private double[] sortedResiduals = null;
	private double[] sortedStandardizedResiduals = null;
	private int[] sortedResidualsIndex = null;
	private double[] sortedNormalQuantiles = null;
	private double[] sortedStandardizedNormalQuantiles = null;
	private double slope, intercept;

	private String dependentHeader = null, independentHeader = null;

	static int times = 0;

	FileDialog fileDialog;
	Frame fileDialogFrame = new Frame();
	File file;
	//FileInputStream fstream;
	private String fileName = "";
	private boolean useHeader = true;
	private String header = null;
	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	//private JScrollPane graphPane= new JScrollPane();
	
	SimpleLinearRegressionResult result = null;
	double beta = 0, alpha = 0, meanX = 0, meanY = 0, sdX=0, sdY=0, meanPredicted=0, sdPredicted=0, meanResiduals=0, sdResiduals=0,sdAlpha = 0, sdBeta = 0;
	double tStatAlpha = 0, tStatBeta = 0;
	double corrXY = 0, rSquare = 0;
	double pvAlpha = 0, pvBeta = 0;
	String betaName = null;
	int xLength = 0;
	int yLength = 0;

	
  	/**Initialize the Analysis*/
	public void init(){

		showInput = false;
		showSelect = false;
		showVisualize= false;
		super.init();

		analysisType = AnalysisType.SIMPLE_LINEAR_REGRESSION;

		//////System.out.println("SLR analysisType = " + analysisType);
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = SimpleLinearRegressionExamples.availableExamples;
		useGraph = true;


		onlineDescription = "http://en.wikipedia.org/wiki/Linear_regression";
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

		chartFactory = new Chart();
		resetGraph();
		validate();
//		reset();
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
		dependentHeader = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		independentHeader = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();
		Data data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<String> yList = new ArrayList<String>();
		yLength=0;
		xLength=0;
		try {
			for (int k =0; k < dataTable.getRowCount(); k++) {
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
				}
			}


		} catch (Exception e) {
			//////////System.out.println("Exception In outer catch: " + e );
		}
		if (xLength <= 0 || yLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}

		// Call the Controller method getAnalysis() delegate the work to Model
		//System.out.println("gui SLR xLength = " + xLength );
		//System.out.println("gui SLR yLength = " + yLength );
		xData = new double[xLength];
		yData = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			yData[i] = Double.parseDouble((String)yList.get(i));
			//resultPanelTextArea.append(" Y = "+yData[i] );
		}
		for (int i = 0; i < xLength; i++) {
			xData[i] = Double.parseDouble((String)xList.get(i));
			//resultPanelTextArea.append(" X = "+xData[i] );
		}

		/*********** plotting data is ready here ***************/
		/* where: x = data of x coordiante entered from the user */
		/*        y = data of y coordiante entered from the user */

		data.appendX("X", xData, DataType.QUANTITATIVE);
		data.appendY("Y", yData, DataType.QUANTITATIVE);

		try {
			result = (SimpleLinearRegressionResult)data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);

		} catch (Exception e) {
			//////////System.out.println(e);
		}

		// Retreive the data from Data Object using HashMap

		
		residuals = new double[xLength];
		predicted = new double[xLength];
		predictedUpper = new double[xLength];
		predictedLower = new double[xLength];
		varPredict = new double[xLength];
		sdPredict = new double[xLength];

		
		try {
			residuals = result.getResiduals();

		} catch (NullPointerException e) {
			//////System.out.println("residuals Exception " + e);
		}
		try {
			meanResiduals = result.getMeanResiduals();

		} catch (NullPointerException e) {
			//////System.out.println("residuals Exception " + e);
		}
		try {
			sdResiduals = result.getSDResiduals();

		} catch (NullPointerException e) {
			//////System.out.println("residuals Exception " + e);
		}
		try {
			predicted = result.getPredicted();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			meanPredicted = result.getMeanPredicted();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			sdPredicted = result.getSDPredicted();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			varPredict = result.getVarPredict();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			sdPredict = result.getSDPredict();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			predictedUpper = result.getPredictedUpperBound();

		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			predictedLower = result.getPredictedLowerBound();

		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}

		try {
			beta = result.getBeta();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			betaName = result.getBetaName();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		////////System.out.println("SimpleLinearRegression betaName = " + betaName);
		try {
			alpha = result.getAlpha();
		} catch (NullPointerException e) {
		}

		try {
			meanX = result.getMeanX();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		try {
			meanY = result.getMeanY();//((Double)
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sdX = result.getSDX();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sdY = result.getSDY();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		try {
			sdAlpha = result.getAlphaSE();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sdBeta = result.getBetaSE();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_SE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			tStatAlpha =  result.getAlphaTStat();//((Double)result.getTexture().get(SimpleLinearRegressionResult.ALPHA_T_STAT)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			tStatBeta = result.getBetaTStat();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_T_STAT)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			pvAlpha = result.getAlphaPValue();//((Double)result.getTexture().get(SimpleLinearRegressionResult.ALPHA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			pvBeta = result.getBetaPValue();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			corrXY = result.getCorrelationXY();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			rSquare = result.getRSquare();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);

		try {
			sortedResiduals = result.getSortedResiduals();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedResiduals = result.getSortedStandardizedResiduals();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_STANDARDIZED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		try {
			sortedResidualsIndex = result.getSortedResidualsIndex();//(int[])residualMap.get(SimpleLinearRegressionResult.SORTED_RESIDUALS_INDEX);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedNormalQuantiles = result.getSortedNormalQuantiles();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		
		updateResults();
		
		slope = beta;
		intercept = alpha;
		doGraph();

	}

	public void updateResults(){

		if (result==null)
		return;
	
		result.setDecimalFormat(dFormat);	
	

	setDecimalFormat(dFormat);
	
	resultPanelTextArea.setText("\n"); //clear first
	resultPanelTextArea.append("\tSample Size = " + xLength + " \n" );
	resultPanelTextArea.append("\n\tDependent Variable = " + dependentHeader + " \n" );
	resultPanelTextArea.append("\n\tIndependent Variable = "  + independentHeader + " \n" );
	resultPanelTextArea.append("\n\tSimple Linear Regression Results:\n" );

	resultPanelTextArea.append("\n\tMean of " + independentHeader + " = " + result.getFormattedDouble(meanX));
	resultPanelTextArea.append("\n\tMean of " +   dependentHeader + " = " + result.getFormattedDouble(meanY));


	resultPanelTextArea.append("\n\n\tRegression Line:\n\t\t" + dependentHeader + " = " + result.getFormattedDouble(alpha));
	if (beta >= 0) {
		resultPanelTextArea.append(" + " + beta + "   " + independentHeader);
	}
	else {
		resultPanelTextArea.append(" " + beta + "   " + independentHeader);
	}


	resultPanelTextArea.append("\n\n\tCorrelation(" + independentHeader + ", " + dependentHeader + ") = " + result.getFormattedDouble(corrXY));
	resultPanelTextArea.append("\n\tR-Square = " + result.getFormattedDouble(rSquare));
	resultPanelTextArea.append("\n\n");


	resultPanelTextArea.append("\n\tIntercept: ");
	resultPanelTextArea.append("\n\t\tParameter Estimate: " + result.getFormattedDouble(alpha));
	resultPanelTextArea.append("\n\t\tStandard Error:     " +result.getFormattedDouble(sdAlpha));
	resultPanelTextArea.append("\n\t\tT-Statistics:        " + result.getFormattedDouble(tStatAlpha));
	/*
	if (pvAlpha==0.0)) {
		resultPanelTextArea.append("\n\t\tP-Value:            <2E-16");
	}
	else {
		resultPanelTextArea.append("\n\t\tP-Value:            " + pvAlpha);
	}
	*/
	//resultPanelTextArea.append("\n\t\tP-Value:            " + AnalysisUtility.enhanceSmallNumber(pvAlpha));
	resultPanelTextArea.append("\n\t\tP-Value:            " + result.getFormattedDouble(pvAlpha));


	resultPanelTextArea.append("\n");
	resultPanelTextArea.append("\n\tSlope: ");
	resultPanelTextArea.append("\n\t\tParameter Estimate: " + result.getFormattedDouble(beta));
	resultPanelTextArea.append("\n\t\tStandard Error:     " +result.getFormattedDouble(sdBeta));
	resultPanelTextArea.append("\n\t\tT-Statistics:        " + result.getFormattedDouble(tStatBeta));
	/*if (pvBeta.equals("0.0")) {
		resultPanelTextArea.append("\n\t\tP-Value:            <2E-16");
	}
	else {
	*/
	//resultPanelTextArea.append("\n\t\tP-Value:            " + AnalysisUtility.enhanceSmallNumber(pvBeta));
	resultPanelTextArea.append("\n\t\tP-Value:            " + result.getFormattedDouble(pvBeta));

	/*
	resultPanelTextArea.append("\nSLOPE             = " + beta);
	resultPanelTextArea.append("\nSTANDARD ERROR of INTERCEPT      = " + sdAlpha);
	resultPanelTextArea.append("\nSTANDARD ERROR of SLOPE       = " + sdBeta);
	resultPanelTextArea.append("\nT-STAT of ALPHA  = " + tStatAlpha);
	resultPanelTextArea.append("\nT_STAT of BETA   = " + tStatBeta);
	resultPanelTextArea.append("\nP-VALUE of ALPHA = " + pvAlpha);
	resultPanelTextArea.append("\nP-VALUE of BETA  = " + pvBeta);
	*/
/*

	resultPanelTextArea.append("\nPREDICTED VALUES = " );
	resultPanelTextArea.append("\nPREDICTED        = " );
*/
	resultPanelTextArea.append("\n\n\t" + independentHeader + "\t" + dependentHeader + "\tPredicted\tResidual");
	
	/*	int NumberDigitKept = 3;
	String[] xTruncated = AnalysisUtility.truncateDigits(xData, NumberDigitKept);
	String[] yTruncated = AnalysisUtility.truncateDigits(yData, NumberDigitKept);
	String[] predictedTruncated = AnalysisUtility.truncateDigits(predicted, NumberDigitKept);
	String[] residualTruncated = AnalysisUtility.truncateDigits(residuals, NumberDigitKept);*/

	for (int i = 0; i < xLength; i++) {
		try {
			resultPanelTextArea.append("\n\t" + result.getFormattedDouble(xData[i]) + "\t" + result.getFormattedDouble(yData[i]) + "\t" +result.getFormattedDouble(predicted[i]) + "\t" + result.getFormattedDouble(residuals[i]) );
		} catch (Exception e) {
		}
	}
	resultPanelTextArea.append("\n\nMean\t" +result.getFormattedDouble(meanX)+"\t"+result.getFormattedDouble(meanY)+"\t"+result.getFormattedDouble(meanPredicted)+"\t"+result.getFormattedDouble(meanResiduals));
	resultPanelTextArea.append("\nSD\t" +result.getFormattedDouble(sdX)+"\t"+result.getFormattedDouble(sdY)+"\t"+result.getFormattedDouble(sdPredicted)+"\t"+result.getFormattedDouble(sdResiduals));
	

/*
	resultPanelTextArea.append("\nRESIDUALS SORTED= " );

	for (int i = 0; i < xLength; i++) {
		resultPanelTextArea.append(" " + result.getFormattedDouble(sortedResiduals[i]));
	}
	resultPanelTextArea.append("\nRESIDUALS INDEX SORTED= " );

	for (int i = 0; i < xLength; i++) {
		resultPanelTextArea.append(" " + result.getFormattedDouble(sortedResidualsIndex[i]));
	}
	resultPanelTextArea.append("\nRESIDUALS NORMAL QUANTILES = " );

	for (int i = 0; i < xLength; i++) {
		resultPanelTextArea.append(" " + result.getFormattedDouble(sortedNormalQuantiles[i]));
	}
	*/
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

   	protected void doGraph() {
		// graphComponent is available here
		// data: variables double xData, yData, residuals, predicted are available here after doAnalysis() is run.
		//////System.out.println("start doGraph 1");
		graphPanel.removeAll();

		JPanel innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		graphPanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
		//ScrollPaneLayout scrollPaneLayout = new ScrollPaneLayout();
		//graphPane.setScrollPaneLayout(scrollPaneLayout.VERTICAL_SCROLLBAR_ALWAYS);
		//graphPane.setLayout(scrollPaneLayout);


		// 1. scatter plot of data: yData vs. xData
		/*JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot of " + dependentHeader + " vs X " + independentHeader, independentHeader, dependentHeader, xData, yData);//getChart(title, xlabel, ylabel, xdata,ydata)
		ChartPanel chartPanel = new ChartPanel(scatterChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);*/

		JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot of " + dependentHeader + " vs " + independentHeader, independentHeader, dependentHeader, dependentHeader + " Value  " , xData, yData,  "Regression Line", intercept, slope, "");
		ChartPanel chartPanel = new ChartPanel(scatterChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);


		Color[] colorArray = new Color[] {Color.CYAN, Color.BLUE, Color.CYAN};

		// 1.5 add prediction interval
		scatterChart = chartFactory.getQQChart("Scatter Plot of " + dependentHeader + " vs " + independentHeader, independentHeader, dependentHeader, dependentHeader + " Value  " , xData, yData,  "Regression Line", intercept, slope, "");
		double[][] xArray = new double[3][];
		xArray[0] = xData;
		xArray[1] = xData;
		xArray[2] = xData;
		double[][] yArray = new double[3][];
		yArray[0] = predictedUpper;
		yArray[1] = predicted;
		yArray[2] = predictedLower;
		int lineSetCount = 3; // 3 lines >///<
		String[] groupNames = {"Predicted + 2 SE", "Predicted", "Predicted - 2 SE"};

		String[] dotGroupNames = {"Observed"};
		double[][] xDataDot = new double[1][];
		double[][] yDataDot = new double[1][];
		xDataDot[0] = xData;
		yDataDot[0] = yData;
		Color[] dotColor = {Color.RED};
		for (int i = 0; i < predictedUpper.length; i++) {
			//System.out.print(varPredict[i]);
			//System.out.print(", " + sdPredict[i]);
			//System.out.print(", " + predictedLower[i]);
			//System.out.print(", " + predicted[i]);
			//System.out.print(", " + predictedUpper[i] + "\n");
		}

		//double[][] lineX  --double[number of line][number of dots in line]
		//double[][] dotX  --double[number of dots group][number of dots in the group]
		// "other" can be "nolegend" to turn off the legend box at the buttom


  		/* public JFreeChart getLineAndDotChart(
			String title,
			String xLabel,
			String yLabel,
			int numberOfLines,
			String[] lineNames,
			double[][] lineX,
			double[][] lineY,
			Color[] lineColors,

            	int numberOfDotsGroups,
            	String[] dotGroupNames,
            	double[][] dotX,
            	double[][] dotY,
            	Color[] dotColors,
            	String other){
	 	*/

		//try {
			scatterChart = chartFactory.getLineAndDotChart("Scatter Plot with Predition Interval", independentHeader, dependentHeader, lineSetCount, groupNames, xArray, yArray, colorArray, 1, dotGroupNames, xDataDot, yDataDot, dotColor, "excludeszero");
			chartPanel = new ChartPanel(scatterChart, false);
			chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
			innerPanel.add(chartPanel);
		//} catch (Exception e) {
		//	////System.out.println("getLineAndDotChart e = " + e);
		//}

		// 2. residual on Covariate plot: residuals vs. xData
		JFreeChart rxChart = chartFactory.getQQChart("Residual on Covariate Plot", independentHeader, "Residuals", "Residual Value  ", xData, residuals,  "At Residual = 0", 0, 0, "");
		chartPanel  = new ChartPanel(rxChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);

		// 3. residual on fit plot: residuals vs. xData
		JFreeChart residualOnFitChart = chartFactory.getQQChart("Residual on Fit Plot", "Predicted " + dependentHeader, "Residuals", "Residual Value  ", predicted, residuals,  "At Residual = 0", 0, 0, "excludeszero");
		////////System.out.println("predicted min = " + AnalysisUtility.min(predicted));
		////////System.out.println("predicted max = " + AnalysisUtility.max(predicted));
		chartPanel  = new ChartPanel(residualOnFitChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);

		//JFreeChart scatterChart = chartFactory.getLineChart("Residual on Covariate Plot, Residual vs. " + independentHeaderArray[i], independentHeaderArray[i], dependentHeader, xDataDouble, residuals);
		// 4. Normal QQ plot: need residuals and standardized normal scores

		//JFreeChart qqChart = chartFactory.getQQChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", "Standardized Residual Value  ", sortedNormalQuantiles, sortedStandardizedResiduals,  "At Standardized Residual = 0", 0, 0, "");
		//JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals);
		//JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedNormalQuantiles, sortedStandardizedResiduals);
		JFreeChart qqChart = chartFactory.getLineChart("Residual Normal QQ Plot", "Theoretical Quantiles", "Standardized Residuals", sortedStandardizedNormalQuantiles, sortedStandardizedResiduals, "noline");
		chartPanel = new ChartPanel(qqChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);

		// 5. scale-location plot -- maybe later.

		graphPanel.validate();

	}

	protected void resetGraph()
	{
		////////System.out.println("reset graph in SLR");
		JFreeChart chart = chartFactory.createChart(); // an empty  chart
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(400,300));
		graphPanel.removeAll();
		graphPanel.add(chartPanel);

	}

    public String getOnlineDescription(){
        return onlineDescription;
    }
}