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

package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.example.ExampleData;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.analyses.example.TwoIndependentWilcoxonExamples;

import java.util.ArrayList;

/** Two Independent T Test */
public class TwoIndependentWilcoxon extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.

	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;

	//objects
	private JToolBar toolBar;

	private Frame frame;
	public static int SIZE_LARGE_SAMPLE = 10;
	
	double rankSumSmall = 0, rankSumLarge = 0;
	double uStatSmall = 0, uStatLarge = 0;
	double meanY = 0, meanX = 0, tStat = 0 ;
	double meanU = 0, varU = 0, zScore = 0;
	double pValue1 = 0, pValue2 = 0;
	String groupNameSmall = "", groupNameLarge = ""; // small means small in group size
	int df = 0;
	String uFormulaExp = null, uFormulaVar = null, dataSummary1 = null, dataSummary2 = null;
	boolean isLargeSample = false;
	String whosSmaller = "";
	int xLength = 0;
	int yLength = 0;
	String varHeader0=null;
	String varHeader1 =null;
	TwoIndependentWilcoxonResult result = null;

	/**Initialize the Analysis*/
	public void init(){

		showInput = false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		analysisType = AnalysisType.TWO_INDEPENDENT_WILCOXON;
		//analysisDescription = "The book example used here: " + ExampleData.getExampleSource();
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = TwoIndependentWilcoxonExamples.availableExamples;
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		depLabel.setText(VARIABLE_1);
		indLabel.setText(VARIABLE_2);
		validate();
		reset();
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
		////////System.out.println("TwoIndependentWilcoxon start doAnalysis");
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

		varHeader0 = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		varHeader1 = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();
		//////System.out.println("TwoIndependentWilcoxon varHeader0 = "+varHeader0);
		//////System.out.println("TwoIndependentWilcoxon varHeader1 = "+varHeader0);
		//JOptionPane.showMessageDialog(this, "In regression, DependentIndex = "+dependentIndex);
		//JOptionPane.showMessageDialog(this, "In regression, IndependentIndex = "+independentIndex);

		Data data = new Data();
		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>(); 
		ArrayList<String> yList = new ArrayList<String>();
		xLength=0;
		yLength=0;
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
			//////System.out.println("Exception In outer catch: " + e );
		}
		if (xLength <= 0 || yLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}
		double[] x = new double[xLength];
		double[] y = new double[yLength];

		for (int i = 0; i < yLength; i++) {
			try {
				y[i] = Double.parseDouble((String)yList.get(i));
				//resultPanelTextArea.append(" Y = "+y[i] );
			} catch (Exception e) {
				//resultPanelTextArea.append("\nException y " + e );
			}
		}
		for (int i = 0; i < xLength; i++) {
			try{
				x[i] = Double.parseDouble((String)xList.get(i));
				//resultPanelTextArea.append(" X = "+x[i] );
			} catch (Exception e) {
				//showError("\nException x " + e );
			}

		}


		data.appendX(varHeader1, x, DataType.QUANTITATIVE);
		data.appendY(varHeader0, y, DataType.QUANTITATIVE);


		try {
			result = (TwoIndependentWilcoxonResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {
		}
		// Retreive the data from Data Object using HashMap

		

		try {
			isLargeSample = result.isLargeSample();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			meanX = result.getMeanX();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanY = result.getMeanY();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			groupNameSmall = result.getGroupNameSmall();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion3 " +e + "");
		}

		try {
			groupNameLarge = result.getGroupNameLarge();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion3 " +e + "");
		}
		try {
			rankSumSmall = result.getRankSumSmallerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion3 " +e + "");
		}
		try {
			rankSumLarge = result.getRankSumLargerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_LARGE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			uStatSmall = result.getUStatSmallerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.U_STAT_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			uStatLarge = result.getUStatLargerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.U_STAT_LARGE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			meanU = result.getMeanU();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			varU = result.getVarianceU();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			zScore = result.getZScore();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			pValue2 = result.getPValueTwoSided();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			pValue1 = result.getPValueOneSided();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}

		try {
			uFormulaExp = result.getUMeanFormula();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			uFormulaVar = result.getUVarianceFormula();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
			} catch (Exception e) {
				//showError(e + "");
		}
		try {
			dataSummary1 = result.getDataSummaryOfGroup1();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			dataSummary2 = result.getDataSummaryOfGroup2();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}

		if (meanX <= meanY) {
			whosSmaller = varHeader1 + " < " + varHeader0;
		}
		else {
			whosSmaller = varHeader0 + " < " + varHeader1;
		}
		dataSummary1 = dataSummary1.substring(0, dataSummary1.length()-2);
		dataSummary2 = dataSummary2.substring(0, dataSummary2.length()-2);

		updateResults();
	}
	
	public void updateResults(){

		if (result==null)
		return;
	
		result.setDecimalFormat(dFormat);	
	

	setDecimalFormat(dFormat);
	
	resultPanelTextArea.setText("\n"); //clear first
	

	resultPanelTextArea.append("\tResults of Two Independent Sample Wilcoxon Rank Sum Test:" );
	//resultPanelTextArea.append("\n\tVariable 1 =" + varHeader0 + ". Sample Size = " + yLength);

	//resultPanelTextArea.append("\n\tVariable 2 =" + varHeader1 + ",. Sample Size = " + xLength+ "\n" );

	resultPanelTextArea.append("\n\n\tData (with Ranks):");
	resultPanelTextArea.append("\n");

	resultPanelTextArea.append("\n\tData of Group " + dataSummary2);
	resultPanelTextArea.append("\n\n\tData of Group " +dataSummary1);
	resultPanelTextArea.append("\n");

	if (varHeader0.equalsIgnoreCase(groupNameSmall)) {
		resultPanelTextArea.append("\n\tGroup "+ varHeader0 + ":\n\tSample Size = " + yLength + "\n\tMean = " + result.getFormattedDouble(meanY) + "\n\tRank Sum = " + rankSumSmall + "\n\tTest Statistics = " +  result.getFormattedDouble(uStatSmall) + "\n\t");
		resultPanelTextArea.append("\n\tGroup "+ varHeader1 + ":\n\tSample Size = " + xLength + "\n\tMean = " + result.getFormattedDouble(meanX) + "\n\tRank Sum = " + rankSumLarge + "\n\tTest Statistics = " +  result.getFormattedDouble(uStatLarge) + "\n\t");

	} else {
		resultPanelTextArea.append("\n\tGroup "+ varHeader0 + ":\n\tSample Size = " + yLength + "\n\tMean = " + result.getFormattedDouble(meanY) + "\n\tRank Sum = " + rankSumLarge + "\n\tTest Statistics = " +  result.getFormattedDouble(uStatLarge));
		resultPanelTextArea.append("\n\n\tGroup "+ varHeader1 + ":\n\tSample Size = " + xLength + "\n\tMean = " + result.getFormattedDouble(meanX) + "\n\tRank Sum = " + rankSumSmall + "\n\tTest Statistics = " +  result.getFormattedDouble(uStatSmall));
	}
	if (isLargeSample) {
		resultPanelTextArea.append("\n\n\tExpectation of Test Statistics = " + result.getFormattedDouble(meanU));
		resultPanelTextArea.append("\n\n\tVariance of Test Statistics = " + result.getFormattedDouble(varU));
		resultPanelTextArea.append("\n\n\tZ-Score " + result.getFormattedDouble(zScore));
		resultPanelTextArea.append("\n\n\tOne-Sided P-Value for " + whosSmaller + ": " + result.getFormattedDouble(pValue1));
		resultPanelTextArea.append("\n\n\tTwo-Sided P-Value for " + varHeader0 + " not equal to " + varHeader1 + ": " + result.getFormattedDouble(pValue2));
	} else {
		resultPanelTextArea.append("\n\n\tOne-Sided P-Value for " + whosSmaller + ": " + result.getFormattedDouble(pValue1));
		resultPanelTextArea.append("\n\n\tTwo-Sided P-Value for " + varHeader0 + " not equal to " + varHeader1 + ": " + result.getFormattedDouble(pValue2));

	}
	resultPanelTextArea.append("\n\n\t*********************** Note ***********************");

	if (isLargeSample) {
		//resultPanelTextArea.append("\n\nEither sample size > "+ SIZE_LARGE_SAMPLE + ", use large sample approximation.");

		resultPanelTextArea.append("\n\tUse Normal Approximation, when at least 1 sample-size is > 10.");
	}
	else {
		resultPanelTextArea.append("\n\tUse the exact U-test when both sample sizes are <= 10.\n"+
					"\tUse Normal Approximation, when at least 1 sample-size is > 10.\n");
	}
	if (uFormulaExp != null)
		resultPanelTextArea.append("\n\tFormula Used for the Expectation of the Test Statistics = " + uFormulaExp + "\n");
	if (uFormulaVar != null)
		resultPanelTextArea.append("\tFormula Used for the Variance of the Test Statistics = " + uFormulaVar + "\n\n");

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
	
    public String getOnlineDescription(){
        return new String("http://en.wikipedia.org/wiki/Mann-Whitney_U"); // M-W is not paired.
    }
    
	protected void setResultPanel() {
		super.setResultPanel();
		resultPanelTextArea.setRows(30);
		resultPanelTextArea.setColumns(70);
	}
}