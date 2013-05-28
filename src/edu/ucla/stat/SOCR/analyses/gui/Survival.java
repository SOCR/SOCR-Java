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

/* 	modified annieche 200508.
	separate the gui part from the model part
*/

package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.beans.*;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.SurvivalExamples;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

public class Survival extends Analysis implements PropertyChangeListener {

	//public JTabbedPane tabbedPanelContainer;
	private JToolBar toolBar;
	private Frame frame;
	protected JLabel timeLabel = new JLabel("TIME");
	protected JLabel censorLabel = new JLabel("CENSORED");
	protected JLabel groupNameLabel = new JLabel("GROUP NAMES");
	private	double[][] survivalTime = null;
	private	double[][] survivalRate = null;
	private	double[][] upperCI = null;
	private	double[][] lowerCI = null;
	private	double[][] survivalSE = null;
	private	double[][] censoredTime = null;
	private	double[][] censoredRate = null;
	private	double[] maxTime = null;

	private	int[][]    atRisk = null;
	private	String[]   groupNames = null;
	private 	int plotSize = 0;
	private	boolean useGroupNames = true;
	
	int df = 0; String numberCaseCensored = null;
	double sampleMean = 0, sampleVar = 0;
	String timeList = null;
	double t_stat=0, p_value=0;
	String p_valueS = null;
	int timeLength = 0;
	SurvivalResult result = null;
	
	/**Initialize the Analysis*/
	public void init(){
		showInput = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		
		//System.err.println("Test11");

		analysisType = AnalysisType.SURVIVAL;
		analysisName = "Survival Analysis";
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = false;
		useServerExample = false;
		useStaticExample = SurvivalExamples.availableExamples;

		//System.err.println("Test12");
		
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName(analysisName);
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		validate();
		reset();
	}


	/** Create the actions for the buttons */
	protected void createActionComponents (JToolBar toolBar){
		super.createActionComponents(toolBar);
	}


	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){
	}

	/**This method defines the specific statistical Analysis to be carried our on the user specified data. ANOVA is done in this case. */
	public void doAnalysis(){
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();

		String timeHeader =  null;
		String censorHeader  = null;
		String groupNameHeader  = null;

		try {
			timeHeader= columnModel.getColumn(timeIndex).getHeaderValue().toString().trim();
		} catch (Exception e) {
		}
		try {
			censorHeader = columnModel.getColumn(censorIndex).getHeaderValue().toString().trim();
		} catch (Exception e ) {
		}
		try {
			groupNameHeader = columnModel.getColumn(groupNamesIndex).getHeaderValue().toString().trim();
		} catch (Exception e ) {
		}

		////System.out.println("doAnalysis timeHeader = " + timeHeader+ ", timeIndex = " + timeIndex);
		////System.out.println("doAnalysis censorHeader = " + censorHeader+ ", censorIndex = " + censorIndex);
		////System.out.println("doAnalysis groupNameHeader = " + groupNameHeader+ ", groupNamesIndex = " + groupNamesIndex);

		// DO NOT NEED TO PRINT THIS FOR ONE_T
		// ONE_T has only one variable, we use Y to contain it (not X).
		// Reason of not using X but Y: Y is at the top of the panel.
		if (! hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}
		/*
		if (timeIndex < 0 || censorIndex < 0 || groupNamesIndex < 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}*/

		if (timeIndex < 0 || censorIndex < 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}
		if (groupNamesIndex < 0) {
			useGroupNames = false;
		}


		Data data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		/************* TIME *************/
		String cellValue = null;

		ArrayList<String> timeArrayList = new ArrayList<String>();
		timeLength=0;
		try {
			for (int k =0; k < dataTable.getRowCount(); k++) {
				try {
					cellValue = ((String)dataTable.getValueAt(k, timeIndex)).trim();
					if (cellValue != null && !cellValue.equals("")) {
						timeArrayList.add(timeLength , cellValue);
						timeLength++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?


					}
			}

		} catch (Exception e) {
		}


		if (timeLength <=0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}

		double[] time = new double[timeLength];
		for (int i = 0; i < timeLength; i++) {
			time[i] = Double.parseDouble((String)timeArrayList.get(i));
			//////////////System.out.println("gui.Survival doAnalysis time[" + i + "] = " + time[i]);
		}


		/************* CENSOR *************/
		int censorLength = 0;

		ArrayList<String> censorArrayList = new ArrayList<String>();
		try {
			for (int k =0; k < dataTable.getRowCount(); k++) {
				try {
					cellValue = ((String)dataTable.getValueAt(k, censorIndex)).trim();
					if (cellValue != null && !cellValue.equals("")) {
						censorArrayList.add(censorLength , cellValue);
						censorLength++;
						}
						else {
							continue; // to the next for
						}
					} catch (Exception e) { // do nothing?
					}
			}

		} catch (Exception e) {
		}


		if (censorLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}

		byte[] censor = new byte[censorLength];
		for (int i = 0; i < censorLength; i++) {
			censor[i] = Byte.parseByte((String)censorArrayList.get(i));
		}


		/************* TREATMENT (GROUPS) *************/
		int treatLength = 0;
		String[] treat = null;
		////System.out.println("gui.Survival doAnalysis useGroupNames = " + useGroupNames);
		////System.out.println("gui.Survival doAnalysis groupNamesIndex = " + groupNamesIndex);


		if (useGroupNames) {
			ArrayList<String> treatArrayList = new ArrayList<String>();
			try {
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k, groupNamesIndex)).trim();
						if (cellValue != null && !cellValue.equals("")) {
							treatArrayList.add(treatLength , cellValue);
							treatLength++;
							}
							else {
								continue; // to the next for
							}
						} catch (Exception e) { // do nothing?
						}
				}

			} catch (Exception e) {
				////System.out.println("gui.Survival doAnalysis useGroupNames Exception = " + e);

			}
			if (treatLength <= 0) {
				JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
				return;
			}

			treat = new String[treatLength];
			for (int i = 0; i < treatLength; i++) {
				treat[i] = (String)treatArrayList.get(i);
				////System.out.println("gui.Survival doAnalysis treat["+i+"] = " + treat[i]);
			}
		}
		else { // default only one group.
			treatLength = timeLength;
			treat = new String[treatLength];

			for (int i = 0; i < treatLength; i++) {
				treat[i] = "GROUP_ONE";
				//////////System.out.println("gui.Survival doAnalysis treat["+i+"] = " + treat[i]);
			}
		}

		/*String[] treat = new String[treatLength];
		*/
		//for (int i = 0; i < treatLength; i++) {
			//treat[i] = (String)treatArrayList.get(i);
			//////////System.out.println("gui.Survival doAnalysis treat["+i+"] = " + treat[i]);
		//}

		// But the Analyses tools package takes the data in X HashMap.

		//double[] testTime = {1,10,22,7,3,32,12,23,8,22,17,6,2,16,11,34,8,32,12,25,2,11,5,20,4,19,15,6,8,17,23,35,5,6,11,13,4,9,1,6,8,10};
		//byte[] censor = {1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,0};
		//String[] treat = {"control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",   "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP"};

		//////////System.out.println("now get result ");

		try {
			result = (SurvivalResult) data.getSurvivalResult(time, censor, treat);
			//////////System.out.println("result = " + result);
			//result = (NormalPowerResult) data.getNormalPower(100, 3500, 0.05, 2500 , 2450, hypothesisType);
		} catch (Exception e) {
			//////////System.out.println("in gui result Exception " + e);
		}

		/* every one has have its own, otherwise one Exception spills the whole. */

		
		try {
			survivalTime = result.getSurvivalTime();//((Double)result.getTexture().get(OneTResult.SAMPLE_MEAN)).doubleValue();
		} catch (NullPointerException e) {
			//////////System.out.println("survivalTime NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("survivalTime Exception = " + e);
		}
		try {
			survivalRate = result.getSurvivalRate();//((Double)result.getTexture().get(OneTResult.SAMPLE_VAR)).doubleValue();
		} catch (NullPointerException e) {
			//////////System.out.println("survivalRate NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("survivalRate Exception = " + e);
		}
		try {
			upperCI = result.getSurvivalUpperCI();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			//////////System.out.println("upperCI NullPointerException = " + e);

		} catch (Exception e) {
			//////////////System.out.println("upperCI Exception = " + e);
		}
		try {
			lowerCI = result.getSurvivalLowerCI();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			//////////System.out.println("lowerCI NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("lowerCI Exception = " + e);
		}
		try {
			atRisk = result.getSurvivalAtRisk();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			//////////System.out.println("atRisk NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("atRisk Exception = " + e);
		}
		try {
			survivalSE = result.getSurvivalSE();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			//////////System.out.println("survivalSE NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("survivalSE Exception = " + e);
		}

		try {
			groupNames = result.getSurvivalGroupNames();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {

		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}

		try {
			timeList = result.getSurvivalTimeList();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			//////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}


		try {
			censoredTime = result.getCensoredTimeArray();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			////////System.out.println("Exception = " + e);
		}


		try {
			numberCaseCensored = result.getNumberCensored();
		} catch (NullPointerException e) {
			//System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			//System.out.println("numberCaseCensored Exception = " + e);
		}


		try {
			censoredRate = result.getCensoredRateArray();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			////////System.out.println("Exception = " + e);
		}
		try {
			maxTime = result.getMaxTime();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			////////System.out.println("Exception = " + e);
		}
		////////System.out.println("Print maxTime  = " + maxTime);

		for (int i = 0; i < maxTime.length; i++) {
			////////System.out.println("maxTime["+i+"] = " + maxTime[i]);
		}

		////////System.out.println("gui censoredTime.length = " +  censoredTime.length);
		for (int i = 0; i < censoredTime.length; i++) {
			////////System.out.println("gui groupName = " + groupNames[i]+", censoredTime["+i+"].length = " + censoredTime[i].length);
			if (censoredTime[i].length > 0) {
				for (int j = 0; j < censoredTime[i].length; j++) {
					////////System.out.println("gui censored["+i+"]["+j + "] = " + censoredTime[i][j] + " rate = " + censoredRate[i][j]);
				}
			}

		}
	
		t_stat = sampleMean/Math.sqrt(sampleVar/(df+1));
		p_value =
			1-(new edu.ucla.stat.SOCR.distributions.StudentDistribution(df)).getCDF(t_stat);
		p_valueS = AnalysisUtility.enhanceSmallNumber(p_value);
			
		updateResults();
		doGraph();
	}
	
	public void updateResults(){

		if (result==null)
		return;
	
		result.setDecimalFormat(dFormat);	
	

	setDecimalFormat(dFormat);
	
	resultPanelTextArea.setText("\n"); //clear first
	
	resultPanelTextArea.append("\tResult of Survival Analysis Using Kaplan-Meier Model:\n" );
	resultPanelTextArea.append("\n\tSamepl Size = " + timeLength );
	resultPanelTextArea.append("\n\tNumber of Censored Cases= " + numberCaseCensored );

	resultPanelTextArea.append("\n\tNumber of Groups Cases= " + groupNames.length );

	resultPanelTextArea.append("\n\tGroups  = ");
	for (int i = 0; i < groupNames.length; i++) {
		resultPanelTextArea.append("   " + groupNames[i]+"\t");
	}
	resultPanelTextArea.append("\n");

	resultPanelTextArea.append("\n\n\n\tSurvival Times (Censored Cases Marked +) = " + timeList);
	//resultPanelTextArea.setForeground(Color.BLUE);
	resultPanelTextArea.append("\n\n\n\tTime\tNo. At Risk\tRate\tSE of Rate\tUpper CI\tLower CI ");
	//resultPanelTextArea.setForeground(Color.BLACK);
	/*int NumberDigitKept = 6;
	String[][] survivalRateTruncated = AnalysisUtility.truncateDigits(survivalRate, NumberDigitKept);
	String[][] survivalSETruncated = AnalysisUtility.truncateDigits(survivalSE, NumberDigitKept);
	String[][] upperCITruncated = AnalysisUtility.truncateDigits(survivalRate, NumberDigitKept);
	String[][] lowerCITruncated = AnalysisUtility.truncateDigits(survivalRate, NumberDigitKept);*/

	for (int i = 0; i < survivalRate.length; i++) {
		resultPanelTextArea.append("\n\n\tGroup = " + groupNames[i]+"\n");
		for (int j = 0; j < survivalRate[i].length; j++) {
			if (upperCI[i][j] == 1) {
				resultPanelTextArea.append("\n\t" +  survivalTime[i][j] +"\t" + atRisk[i][j] + "\t" + result.getFormattedDouble(survivalRate[i][j]) +"\t" + result.getFormattedDouble(survivalSE[i][j]) + "\t" + result.getFormattedDouble(upperCI[i][j])+"\t" + result.getFormattedDouble(lowerCI[i][j]));
			} else {
				resultPanelTextArea.append("\n\t" +  survivalTime[i][j] +"\t" + atRisk[i][j] + "\t" + result.getFormattedDouble(survivalRate[i][j]) +"\t" + result.getFormattedDouble(survivalSE[i][j]) + "\t" + result.getFormattedDouble(upperCI[i][j])+"\t" + result.getFormattedDouble(lowerCI[i][j]));
			}
			plotSize++;
		}
	}
	//////////System.out.println("plotSize= " + plotSize);
	//resultPanelTextArea.append("\nDEGREES OF FREEDOM  = " + df);
	//resultPanelTextArea.append("\nT-statistic  = " + t_stat);
	//resultPanelTextArea.append("\np-value  = " + p_valueS);
	resultPanelTextArea.append("\n\n\n\n");
	resultPanelTextArea.setForeground(Color.BLUE);
	
	}
	
   	protected void doGraph() {
		graphPanel.removeAll();
		////////System.out.println("in doGraph");
		//graphPanel.add(description, BorderLayout.SOUTH);
		JFreeChart scatterChart = null;
		ChartPanel chartPanel1 = null;
		ChartPanel chartPanel2 = null;
		Chart	chartFactory = new Chart();
		String lineType = "excludesZeroNoShape";
/*
		double[] survivalTimePlot = new double[plotSize];
		double[] survivalRatePlot = new double[plotSize];
		//////////System.out.println("doGraph survivalTimePlot.length = " + survivalTimePlot.length);
		//////////System.out.println("doGraph survivalRatePlot.length = " + survivalRatePlot.length);
		int newIndex = 0;
		for (int i = 0; i < survivalRate.length; i++) {
			for (int j = 0; j < survivalRate[i].length; j++) {
				survivalTimePlot[newIndex] = survivalTime[i][j];
				survivalRatePlot[newIndex] = survivalRate[i][j];
				newIndex++;
			}

		}
*/
/*

		double[][] survivalTimePlot = new double[survivalTime.length][];
		double[][] survivalRatePlot = new double[survivalRate.length][];
		// create the first point (time = 0, rate = 1)
		for (int i = 0; i < survivalTime.length; i++) {
			survivalTimePlot[i] = new double[survivalTime[i].length + 1];
			survivalRatePlot[i] = new double[survivalRate[i].length + 1];
			survivalTimePlot[i][0] = 0;
			survivalRatePlot[i][0] = 1;
			for (int j = 1; j < survivalTimePlot[i].length; j++) {
				survivalTimePlot[i][j] = survivalTime[i][j - 1];
				survivalRatePlot[i][j] = survivalRate[i][j - 1];
			}
		}
*/
		boolean useCI = true; // whether to plot the confidence interval.
		////////System.out.println("useCI = " + useCI);
		double[][] survivalTimePlot = new double[survivalTime.length][];
		double[][] survivalRatePlot = new double[survivalRate.length][];
		double[][] survivalRateUpperCIPlot = new double[survivalRate.length][];
		double[][] survivalRateLowerCIPlot = new double[survivalRate.length][];

		// create the first point (time = 0, rate = 1)
		double tempTime = 0, tempRate = 0, tempRateUpperCI = 0, tempRateLowerCI = 0;
		int increment = 2;
		int arrayLength = 0;
		int k = 0; // index for survivalTime array, not for survivalTimePlot.
		for (int i = 0; i < survivalTime.length; i++) {
			arrayLength = 2 * survivalRate[i].length + increment;
			k = 0;
			// why add 2? one for the initial point, one for the last point.
			survivalTimePlot[i] = new double[arrayLength];
			survivalRatePlot[i] = new double[arrayLength];

			survivalRateUpperCIPlot[i] = new double[arrayLength];
			survivalRateLowerCIPlot[i] = new double[arrayLength];

			survivalTimePlot[i][0] = 0;
			survivalRatePlot[i][0] = 1;

			survivalRateUpperCIPlot[i][0] = 1;
			survivalRateLowerCIPlot[i][0] = 1;
			int j = 1;
			////////System.out.println(" before loop survivalTimePlot["+i+"].length= " +survivalTimePlot[i].length);
			for (j = 1; j < survivalTimePlot[i].length-1; j++) {
				//////////System.out.println("j = " + j + " survivalTimePlot["+i+"].length = " + survivalTimePlot[i].length + ", k = " + k);
				////////System.out.println("in loop j = " + j);
				if (j % 2 == 1) {

					////////System.out.println("in if j = " + j + ", k = " + k);
					survivalTimePlot[i][j] = survivalTime[i][k];
					survivalRatePlot[i][j] = survivalRatePlot[i][j-1];
					survivalRateUpperCIPlot[i][j] = survivalRateUpperCIPlot[i][j-1];
					survivalRateLowerCIPlot[i][j] = survivalRateLowerCIPlot[i][j-1];

					tempTime = survivalTimePlot[i][j];
					tempRate = survivalRatePlot[i][j];
					tempRateUpperCI = survivalRateUpperCIPlot[i][j];
					tempRateLowerCI = survivalRateLowerCIPlot[i][j];


				}
				else {
					//////////System.out.println("in elsej = " + j);
					survivalTimePlot[i][j] = tempTime;
					survivalRatePlot[i][j] = survivalRate[i][k];
					survivalRateUpperCIPlot[i][j] = upperCI[i][k];
					survivalRateLowerCIPlot[i][j] = lowerCI[i][k];

					k++;

				}
				////////System.out.println("i = " + i + ", j = " + j + ", k = " + k + ", time["+i+"]["+j+"] = " + survivalTimePlot[i][j] + ". rate["+i+"]["+j+"] = " + survivalRatePlot[i][j]);

			}
			int index = j;//survivalTimePlot[i].length-1;//survivalTimePlot[i].length;
			////////System.out.println("index = " + index);
			////////System.out.println("maxTime["+i+"] = " + maxTime[i]);

			survivalTimePlot[i][index] = maxTime[i];

			////////System.out.println("survivalTimePlot["+i+"]["+index+"] = " + survivalTimePlot[i][index]);

			survivalRatePlot[i][index] = survivalRatePlot[i][index-1];

			survivalRateUpperCIPlot[i][index] = survivalRateUpperCIPlot[i][index-1];
			survivalRateLowerCIPlot[i][index] = survivalRateLowerCIPlot[i][index-1];

		}

		int numberGroups = 0, lineSetCount = 0, tempIndex = 0;
		double[][] finalTimeArray  = null;
		double[][] finalRateArray  = null;
		String[] finalGroupNames  = null;
		int numberOfLines = 0;
		//lineSetCount * groupNames.length;
		Color[] colorArray = null;//new Color[numberOfLines];
		////////System.out.println("lineSetCount = " + lineSetCount);
		if (useCI) {
			numberGroups = groupNames.length;

			lineSetCount = 3; // use 4 if using censor points.
			tempIndex = 0;
			numberOfLines = lineSetCount * numberGroups;
			finalTimeArray = new double[numberOfLines][];
			finalRateArray = new double[numberOfLines][];
			finalGroupNames = new String[numberOfLines];
			////////System.out.println("numberOfLines = " + numberOfLines);
			colorArray = new Color[numberOfLines];
			// why 3? one for rate, one for upper CI, one for lower CI.

			for (int i = 0; i < numberGroups; i++) {
				// repeat the same time.
				finalTimeArray[tempIndex] = survivalTimePlot[i];
				finalTimeArray[tempIndex+1] = survivalTimePlot[i];
				finalTimeArray[tempIndex+2] = survivalTimePlot[i];
				//finalTimeArray[tempIndex+3] = censoredTime[i];

				finalRateArray[tempIndex] = survivalRatePlot[i];
				finalRateArray[tempIndex+1] = survivalRateUpperCIPlot[i];
				finalRateArray[tempIndex+2] = survivalRateLowerCIPlot[i];
				//finalRateArray[tempIndex+3] = censoredRate[i];

				finalGroupNames[tempIndex] = groupNames[i];
				finalGroupNames[tempIndex+1] = groupNames[i] + " Upper CI";
				finalGroupNames[tempIndex+2] = groupNames[i] + " Lower CI";
				//finalGroupNames[tempIndex+3] = groupNames[i] + " Censored";
				if (i % 5 == 0) {
					colorArray[tempIndex] = Color.RED;
					colorArray[tempIndex+1] = Color.PINK;
					colorArray[tempIndex+2] =Color.PINK;

				} else if (i % 5 == 1) {
					colorArray[tempIndex] = Color.BLUE;
					colorArray[tempIndex+1] = Color.CYAN;
					colorArray[tempIndex+2] = Color.CYAN;

				} else if (i % 5 == 2) {
					colorArray[tempIndex] = Color.GRAY;
					colorArray[tempIndex+1] = Color.LIGHT_GRAY;
					colorArray[tempIndex+2] = Color.LIGHT_GRAY;

				} else if (i % 5 == 3) {
					colorArray[tempIndex] = Color.MAGENTA;
					colorArray[tempIndex+1] = Color.PINK;
					colorArray[tempIndex+2] = Color.PINK;

				} else {
					colorArray[tempIndex] = Color.GREEN;
					colorArray[tempIndex+1] = Color.YELLOW;
					colorArray[tempIndex+2] = Color.YELLOW;
				}


				tempIndex += lineSetCount;

			}
		} else {
			numberGroups = groupNames.length;

			lineSetCount = 2;
			tempIndex = 0;
			finalTimeArray = new double[lineSetCount * numberGroups][];
			finalRateArray = new double[lineSetCount * numberGroups][];
			finalGroupNames = new String[lineSetCount * numberGroups];

			// why 3? one for rate, one for upper CI, one for lower CI.

			for (int i = 0; i < numberGroups; i++) {
				// repeat the same time.
				finalTimeArray[tempIndex] = survivalTimePlot[i];
				finalTimeArray[tempIndex+1] = censoredTime[i];

				finalRateArray[tempIndex] = survivalRatePlot[i];
				finalRateArray[tempIndex+1] = censoredRate[i];

				finalGroupNames[tempIndex] = groupNames[i];
				//finalGroupNames[tempIndex+1] = groupNames[i] + " Censored";

				tempIndex += lineSetCount;
			}


		}

		//scatterChart = chartFactory.getLineChart("Rate vs. Time", "Time", "Rate", groupNames.length, groupNames, survivalTimePlot, survivalRatePlot, lineType);
		//scatterChart = chartFactory.getLineChart("Rate vs. Time", "Time", "Rate", lineSetCount * groupNames.length, finalGroupNames, finalTimeArray, finalRateArray, lineType); // this one has color.

		//public JFreeChart getLineChart(String title, String xLabel, String yLabel, double[] x, double[] y, Color[] colors,  String other)

		//public JFreeChart getLineChart(String title, String xLabel, String yLabel, int numberOfLines, String[] lineNames, double[][] x, double[][] y, Color[] colors, String other)
		//int numberOfLines = lineSetCount * groupNames.length;
		//Color[] colors = new Color[numberOfLines];

		//scatterChart = chartFactory.getLineChart("Rate vs. Time", "Time", "Rate", numberOfLines, finalGroupNames, finalTimeArray, finalRateArray, colorArray, lineType);

		scatterChart = chartFactory.getLineChart("Rate vs. Time", "Time", "Rate", 
				lineSetCount * groupNames.length, finalGroupNames, finalTimeArray, 
				finalRateArray, colorArray, lineType); // this one has color.

		//for (int i = 0; i < survivalTime.length; i++) {
		//scatterChart  = chartFactory.getLineChart("Rate vs. Time", "Time", "Rate", survivalTimePlot, survivalRatePlot, lineType);
		//}
		scatterChart.setTitle("Expected Survival Times with 95% Confidence Limits");
		chartPanel1 = new ChartPanel(scatterChart, false);
		chartPanel1.setPreferredSize(new Dimension(plotWidth,plotHeight));
		
		JPanel innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		graphPanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
		
		innerPanel.add(chartPanel1);

		// Add a second plot without the confidence intervals (confidence limits)
		int lineSetCount1 = lineSetCount-2;
		int tempIndex1 = 0;
		double [][] finalTimeArray1 = new double[lineSetCount1 * numberGroups][];
		double [][] finalRateArray1 = new double[lineSetCount1 * numberGroups][];
		String [] finalGroupNames1 = new String[lineSetCount1 * numberGroups];
		for (int i = 0; i < numberGroups; i++) {
			// repeat the same time.
			finalTimeArray1[tempIndex1] = survivalTimePlot[i];
			//finalTimeArray1[tempIndex1+1] = censoredTime[i];
			finalRateArray1[tempIndex1] = survivalRatePlot[i];
			//finalRateArray1[tempIndex1+1] = censoredRate[i];
			finalGroupNames1[tempIndex1] = groupNames[i];
			tempIndex1 += lineSetCount1;
		}
		JFreeChart scatterChart1 = chartFactory.getLineChart(
				"Rate vs. Time", "Time", "Rate", (lineSetCount1) * groupNames.length, 
				finalGroupNames1, finalTimeArray1, 
				finalRateArray1, colorArray, lineType); // this one has color.
		scatterChart1.setTitle("Expected Survival Times (Only)");
		chartPanel2 = new ChartPanel(scatterChart1, false);
		chartPanel2.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel2);

		graphPanel.validate();

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
    public String getOnlineDescription(){
        return new String("http://en.wikipedia.org/wiki/Survival_analysis");
    }
    protected void setMappingPanel() {
		//////////////System.out.println("Survival mappingPanel = " + mappingPanel);
        listIndex = new int[dataTable.getColumnCount()];
        for(int j=0;j<listIndex.length;j++)
            listIndex[j]=1;
        bPanel = new JPanel(new BorderLayout());
	   mappingInnerPanel = new JPanel(new GridLayout(1,3));
        mappingPanel.add(mappingInnerPanel,BorderLayout.CENTER);
	   mappingInnerPanel.setBackground(Color.WHITE);

        addButton1.addActionListener(this);
        addButton2.addActionListener(this);
        addButton3.addActionListener(this);
        removeButton1.addActionListener(this);
        removeButton2.addActionListener(this);
        removeButton3.addActionListener(this);
        ////////////////System.out.println("Survival mappingPanel = " + mappingPanel);

        lModel1 = new DefaultListModel();
        lModel2 = new DefaultListModel();
        lModel3 = new DefaultListModel();
        lModel4 = new DefaultListModel();

        int cellWidth = 10;

        listAdded = new JList(lModel1);
        listAdded.setSelectedIndex(0);
        listTime = new JList(lModel2);
        listCensor = new JList(lModel3);
        listGroupNames = new JList(lModel4);

        paintTable(listIndex);
        listAdded.setFixedCellWidth(cellWidth);
        listTime.setFixedCellWidth(cellWidth);
        listCensor.setFixedCellWidth(cellWidth);
        listGroupNames.setFixedCellWidth(cellWidth);

        //tools1.setBackground(Color.RED);
        //tools2.setBackground(Color.GREEN);
        //tools3.setBackground(Color.BLUE);

        listAdded.setBackground(Color.WHITE);
        listTime.setBackground(Color.WHITE);
        listCensor.setBackground(Color.WHITE);
        listGroupNames.setBackground(Color.WHITE);


        tools1.add(timeLabel);
        tools2.add(censorLabel);
        tools3.add(groupNameLabel);

        tools1.add(addButton1);
        tools1.add(removeButton1);

        tools2.add(addButton2);
        tools2.add(removeButton2);

        tools3.add(addButton3);
        tools3.add(removeButton3);

	   JPanel panelLeft = new JPanel(new GridLayout(3,1,50, 50));
	   JPanel panelCenter = new JPanel(new GridLayout(3,1,50, 50));
	   JPanel panelRight = new JPanel(new GridLayout(3,1,50, 50));
	   //JPanel dummyPanelLeft1 = null;//new JPanel();
	   //JPanel dummyPanelBottom = null;//new JPanel();
        //dummyPanelMiddle.setVisible(false);
        //dummyPanelBottom.setVisible(false);

	   mappingInnerPanel.add(panelLeft,  BorderLayout.WEST);
	   mappingInnerPanel.add(panelCenter,  BorderLayout.CENTER);
	   mappingInnerPanel.add(panelRight,  BorderLayout.EAST);

        panelLeft.add(new JScrollPane(listAdded)); // var cadidates.

        panelCenter.add(tools1);
        panelRight.add(new JScrollPane(listTime));

        panelCenter.add(tools2);
        panelRight.add(new JScrollPane(listCensor));

        panelCenter.add(tools3);
        panelRight.add(new JScrollPane(listGroupNames));
    }
    
    public void paintTable(int[] lstInd) {

        lModel1.clear();
        lModel2.clear();
        lModel3.clear();
        lModel4.clear();

        for(int i=0;i<lstInd.length;i++) {
       	 //////////////System.out.println("Survival paintTable lstInd["+i+"]= " + lstInd[i]);

             switch(lstInd[i]) {
                case 0:
                    break;
                case 1:
                    lModel1.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listAdded.setSelectedIndex(0);

                    break;
                case 2:
                    lModel2.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listTime.setSelectedIndex(0);
                    break;
                case 3:
                    lModel3.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listCensor.setSelectedIndex(0);
                    break;
                 case 4:
                    lModel4.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listGroupNames.setSelectedIndex(0);
                    break;

                default:
                    break;

            }
		//String temp = columnModel.getColumn(i).getHeaderValue().toString().trim();
        	////////////System.out.println("Survival paintTable columnModel.getColumn = " + temp);
        }
        ////////////System.out.println("Survival paintTable columnModel.getColumn = " + temp);
        //////////////System.out.println("Survival paintTable lModel2 = " + lModel2);
        //////////////System.out.println("Survival paintTable lModel3 = " + lModel3);
        //////////////System.out.println("Survival paintTable lModel4 = " + lModel4);
        listAdded.setSelectedIndex(0);
    }
    public void actionPerformed(ActionEvent event) {
	    //////System.out.println("Survival actionPerformed");
    	if(event.getSource() == addButton1) {
	    //////System.out.println("Survival actionPerformed addButton1");
    		addButtonTime();
    	}
    	else if (event.getSource() == removeButton1) {
	    //////System.out.println("Survival actionPerformed removeButton1");
    		removeButtonTime();
    	}
    	else if (event.getSource() == addButton2) {
 	    //////System.out.println("Survival actionPerformed addButton2");
   		addButtonCensor();
    	}
    	else if (event.getSource() == removeButton2) {
	    //////System.out.println("Survival actionPerformed removeButton2");
    		removeButtonCensor();
    	}
    	else if (event.getSource() == addButton3) {
	    //////System.out.println("Survival actionPerformed addButton3");
    		addButtonGroupNames();
    	}
    	else if (event.getSource() == removeButton3) {
	    //////System.out.println("Survival actionPerformed removeButton3");
    		removeButtonGroupNames();
    	}

    }

    protected void addButtonTime() {
            int ct1=-1;
            int sIdx = listAdded.getSelectedIndex();
            int idx2 = lModel2.getSize();
            if(sIdx >-1 && idx2 <1) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
                        timeIndex=i;
                        break;
                    }
                }
                listIndex[timeIndex]=2;
 		   //////////////System.out.println("Survival addButtonTime lModel1 = " + lModel1);
		   //////////////System.out.println("Survival addButtonTime lModel2 = " + lModel2);
		   //////////////System.out.println("Survival addButtonTime lModel3 = " + lModel3);
		   //////////////System.out.println("Survival addButtonTime lModel4 = " + lModel4);
               paintTable(listIndex);
            }
	}
    
	protected void removeButtonTime() {

            int ct1=-1;
            int idx1 = 0;
            int sIdx = listTime.getSelectedIndex();
 	    //////////////System.out.println("removeButtonTime listTime.getSelectedIndex()=" + sIdx);
           if(sIdx >-1) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==2)
                        ct1++;
                    if(ct1==sIdx) {
                        idx1=i;
                        break;
                    }
                }

                listIndex[idx1]=1;
		   //////////////System.out.println("Survival removeButtonTime lModel1 = " + lModel1);
		   //////////////System.out.println("Survival removeButtonTime lModel2 = " + lModel2);
		   //////////////System.out.println("Survival removeButtonTime lModel3 = " + lModel3);
		   //////////////System.out.println("Survival removeButtonTime lModel4 = " + lModel4);
                paintTable(listIndex);
            }
	}
	
	protected void addButtonCensor() {
            int ct1=-1;
            int sIdx = listAdded.getSelectedIndex();
            int idx3 = lModel3.getSize();
            //////////////////////////////System.out.println("addButtonIndependent listAdded = " + listAdded);
            if(sIdx >-1 && idx3 <1) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
                        censorIndex=i;
                       break;
                    }
                }
                listIndex[censorIndex]=3;
		   //////////////System.out.println("Survival addButtonCensor lModel1 = " + lModel1);
		   //////////////System.out.println("Survival addButtonCensor lModel2 = " + lModel2);
		   //////////////System.out.println("Survival addButtonCensor lModel3 = " + lModel3);
		   //////////////System.out.println("Survival addButtonCensor lModel4 = " + lModel4);
                paintTable(listIndex);
            }
    }
	
	protected void removeButtonCensor() {
		int ct1=-1;
		int idx1 = 0;
		int sIdx = listCensor.getSelectedIndex();
	    //////////////System.out.println("removeButtonCensor listCensor.getSelectedIndex()=" + sIdx);
		if(sIdx >-1) {
			for(int i=0;i<listIndex.length;i++) {
				if(listIndex[i] ==3)
				ct1++;
				if(ct1==sIdx) {
					idx1=i;
					break;
				}
			}
			listIndex[idx1]=1;
		   //////////////System.out.println("Survival removeButtonCensor lModel1 = " + lModel1);
		   //////////////System.out.println("Survival removeButtonCensor lModel2 = " + lModel2);
		   //////////////System.out.println("Survival removeButtonCensor lModel3 = " + lModel3);
		   //////////////System.out.println("Survival removeButtonCensor lModel4 = " + lModel4);
			paintTable(listIndex);
		}
    }
	
	protected void addButtonGroupNames() {
	    //////////////System.out.println("click addButtonGroupNames");
            int ct1=-1;
            int sIdx = listAdded.getSelectedIndex();
            int idx4 = lModel4.getSize();
            if(sIdx >-1 && idx4 <1) {
                for(int i=0;i<listIndex.length;i++) {
                    if(listIndex[i] ==1)
                        ct1++;
                    if(ct1==sIdx) {
                        groupNamesIndex=i;
                       break;
                    }
                }
                listIndex[groupNamesIndex]=4;
                paintTable(listIndex);
            }

    }
	
	protected void removeButtonGroupNames() {
	    //////////////System.out.println("click removeButtonGroupNames");
		int ct1=-1;
		int idx1 = 0;
		int sIdx = listGroupNames.getSelectedIndex();
	    //////////////System.out.println("removeButtonGroupNames listGroupNames.getSelectedIndex()=" + sIdx);
		if(sIdx >-1) {
			for(int i=0;i<listIndex.length;i++) {
				if(listIndex[i] ==4) // i just realized this number has to be the same as the list label (lModel4) -- it's really confusing and i don't like it.
				ct1++;
				if(ct1==sIdx) {
					idx1=i;
					break;
				}
			}
			listIndex[idx1]=1;
			paintTable(listIndex);
		}
    }
	
	public void reset(){
		super.reset();
     	hasExample = false;
     	timeIndex = -1;
     	censorIndex = -1;
     	groupNamesIndex = -1;
		removeButtonGroupNames();
		removeButtonCensor();
		removeButtonTime();
	}
	
	protected void removeButtonIndependentAll() {
		//super.removeButtonIndependentAll();
     	timeIndex = -1;
     	censorIndex = -1;
     	groupNamesIndex = -1;

	}
	
	protected void removeButtonDependent() {
		//super.removeButtonDependent();
     	timeIndex = -1;
     	censorIndex = -1;
     	groupNamesIndex = -1;

	}
	
	protected void setResultPanel() {
		super.setResultPanel();
		resultPanelTextArea.setRows(30);
		resultPanelTextArea.setColumns(90);
	}
}