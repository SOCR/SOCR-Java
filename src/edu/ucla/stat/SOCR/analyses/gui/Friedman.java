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
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.xml.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.example.TwoIndependentFriedmanExamples;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

/** this class is for Kruskal Wallis only. */
public class Friedman extends Analysis implements PropertyChangeListener {

	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	private double[] xData = null;
	private double[][] xDataArray = null;
	private double[] yData = null;
	private double[] predicted = null;
	private double[] residuals = null;
	private double[] sortedResiduals = null;
	private double[] sortedStandardizedResiduals = null;
	private int[] sortedResidualsIndex = null;
	private double[] sortedNormalQuantiles = null;
	private double[] sortedStandardizedNormalQuantiles = null;

	private String independentHeader = null;
	static int times = 0;

	FileDialog fileDialog;
	Frame fileDialogFrame = new Frame();
	File file;
	//FileInputStream fstream;
	private String fileName = "";
	private boolean useHeader = true;
	private String header = null;

	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	private String xmlInputString = null;
	
	TwoIndependentFriedmanResult result;

	/**Initialize the Analysis*/
	public void init(){
		mapDep = false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		showInput = false;
		super.init();
		analysisType = AnalysisType.TWO_INDEPENDENT_FRIEDMAN;

		useInputExample = false;
		useLocalExample = true;
		useRandomExample = false;
		useServerExample = true;
		useStaticExample = TwoIndependentFriedmanExamples.availableExamples;

		onlineDescription = "http://en.wikipedia.org/wiki/Friedman_test";
		depMax = 1; // max number of dependent var
		indMax = 9; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);

		// use the new JFreeChar function. annie che 20060312
		chartFactory = new Chart();
		//depLabel.setText("Variable 1");
		indLabel.setText("SELECT AT LEAST TWO GROUPS:");
		validate();
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
		//System.out.println("friedman hasExample = " + hasExample);


		if (!hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}

		if (independentIndex < 0 || independentLength == 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}
		if (independentLength <= 1) {
			JOptionPane.showMessageDialog(this, "PLEASE SELECT AT LEAST 2 GROUPS.");
			return;
		}
		Object[] independentVar = independentList.toArray();

		//System.out.println("\nFriedman independentList.size() = " + independentList.size() );

	

		int varIndex = -1;
		int varIndexList[] = new int[independentVar.length];
		int independentListLength = independentList.size();
		resultPanelTextArea.append("\tGroups Included = ");
		independentHeaderArray = new String[independentVar.length];
		//////System.out.println("Friedman independentVar.length = " + independentVar.length);
		for (int i = independentVar.length-1; i >=0; i--) {
			varIndex = ( (Integer)independentList.get(i)).intValue();
			independentHeader = columnModel.getColumn(varIndex).getHeaderValue().toString().trim();
			independentHeaderArray[i] = independentHeader;
			resultPanelTextArea.append("  "  + independentHeader);// + " original index = " + varIndex );
			varIndexList[i] = varIndex;
		}


		data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/
		int xLength = 0;
		//int yLength = 0;
		//resultPanelTextArea.append("\n\tResult of Two Independent Friedmen's Test:\n" );
		String cellValue = null;
		ArrayList<String> xList = new ArrayList<String>();
		//ArrayList<String> yList = new ArrayList<String>();
		xData = null;
		yData = null;
		xDataArray = new double[independentLength][xLength];
		int xIndex = 0;


			//System.out.println("\nindependentListLength = " +independentListLength);
			for (int index = 0; index < independentListLength; index++) { // for each independent variable
				xLength = 0;
				//resultPanelTextArea.append("\nindependentIndex(ordered)  = "  + varIndexList[index] + " \n" );
				////System.out.println("\nvarIndexList[index] = " +varIndexList[index]);
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k, varIndexList[index])).trim();
						if (cellValue != null && !cellValue.equals("")) {
							xList.add(xLength , cellValue);
							xLength++;
						}
						else {
							continue; // to the next for iteration
						}
					} catch (Exception e) {
						////System.out.println("Exception In inner catch: " + e );
					}
				}
			xData = new double[xLength];

			for (int i = 0; i < xLength; i++) {
				try {
					xData[i] = Double.parseDouble((String)xList.get(i));
					//resultPanelTextArea.append("  X = "+xList.get(i) );
					////System.out.println("i = " + i + " X = " +xList.get(i));
					} catch (Exception e) {
						// there shouldn't be a need to handle it.
						e.printStackTrace();
					}
			}
			xDataArray[xIndex] = xData;
			String tempHeader = columnModel.getColumn(varIndexList[index]).getHeaderValue().toString().trim();

			xIndex++;
			data.appendX(tempHeader, xData, DataType.QUANTITATIVE);

			if (xLength <= 0) {
				JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
				return;
			}
			}


		// this following passage should be duplicated for ANOVA two way, ANCOVA, etc. -- any of those that have multiple number of regressors.


		// Call the Controller method getAnalysis() delegate the work to Model
		////System.out.println("Friedman applet begin result" );
		result = null;
		String className = null;


		try {
			//System.out.println("TwoIndependentFriedmanResult result start" );

			result = (TwoIndependentFriedmanResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_FRIEDMAN);
			////System.out.println("TwoIndependentFriedmanResult applet begin result = " + result);

		} catch (Exception e) {
			//System.out.println("TwoIndependentFriedmanResult applet exception = " + e);
		}


		/*******************************************************/
		////System.out.println("FINISH try result = " + result);
		// Retreive the data from Data Object using HashMap

		/* doGraph is underconstruction thus commented out. annie che 20060314 */
		//if (useGraph)
		updateResults();
		doGraph();

	}
	

	public void updateResults(){

		if (result==null)
		return;
	
		result.setDecimalFormat(dFormat);	
	

	setDecimalFormat(dFormat);
	
	resultPanelTextArea.setText("\n"); //clear first
	

	String[] groupNames = null;


	double grandMean = 0, grandTotal = 0;
	double[] groupMean = null, groupTotal = null;
	int df = 0, sampleSize = 0, singleGroupSize = 0;
	double pValue = 0, sumSq = 0;
	double chiStat = 0;
	DataCase[][] rankArray = null;
	DataCase[][] dataArray = null;

	try {
		rankArray= result.getRankArray();
	} catch (Exception e) {
	}

	try {
		chiStat= result.getChiStat();
	} catch (Exception e) {
	}

	try {
		singleGroupSize= result.getSingleGroupSize();
	} catch (Exception e) {
	}

	try {
		groupNames= result.getGroupNameList();
	} catch (Exception e) {
	}

	try {
		//////System.out.println("result  = " + result);

		grandMean = result.getGrandMean();
	} catch (Exception e) {
		//////System.out.println("sampleSize Exception  = " + e);
	}
	try {

		sampleSize = result.getSampleSize();
	} catch (Exception e) {
		//////System.out.println("sampleSize Exception  = " + e);
	}		try {
		groupTotal = result.getGroupTotal();
	} catch (NullPointerException e) {
		//showError("NullPointerException  = " + e);
	}
	try {
		groupMean = result.getGroupMean();
	} catch (NullPointerException e) {
		//showError("NullPointerException  = " + e);
	}

	try {
		pValue = result.getPValue();
	} catch (NullPointerException e) {
		//showError("NullPointerException  = " + e);
	}
	//System.out.println("guipValue = " + pValue);

	try {
		sumSq = result.getSumSquares();
	} catch (NullPointerException e) {
	}

	try {
		df = result.getDF();
	} catch (NullPointerException e) {
	}
	try {
		dataArray = result.getDataArray();
	} catch (NullPointerException e) {
	}
	try {
		rankArray = result.getRankArray();
	} catch (NullPointerException e) {
	}

	resultPanelTextArea.append("\tResult of Two Independent Friedmen's Test:\n" );
	resultPanelTextArea.append("\tGroups Included = ");
	for (int i = independentHeaderArray.length-1; i >=0; i--) {
		
		resultPanelTextArea.append("  "  + independentHeaderArray[i]);// + " original index = " + varIndex );
	
	}
	
	int groupLength = groupNames.length;
	resultPanelTextArea.append("\n\n\tTotal Number of Cases = " + sampleSize);

	resultPanelTextArea.append("\n\tNumber of Groups = " + groupLength);
	resultPanelTextArea.append("\n\tGroup Size = " + singleGroupSize);

	resultPanelTextArea.append("\n\n\t");


	for (int i = 0; i <groupLength; i++) {
		resultPanelTextArea.append("\t" + groupNames[i]);
	}
	String[] blockName = new String[singleGroupSize];

	int numberDigitKept = 2;
	//String avgTruncated = null; //AnalysisUtility.truncateDigits(expected, NumberDigitKept);

	for (int j = 0; j < singleGroupSize; j++ ) {

		blockName[j] = (j + 1) + "";
		resultPanelTextArea.append("\n\t"+blockName[j] + "\t");

		for (int i = 0; i <groupLength; i++) {
			resultPanelTextArea.append(dataArray[i][j].getValue() + " (");

			resultPanelTextArea.append(AnalysisUtility.truncateDigits(rankArray[i][j].getValue(), numberDigitKept) + ")\t");

			//resultPanelTextArea.append("\n\n\tAverage"+groupMean[i] +"\t");
		}
		//resultPanelTextArea.append("\n" );
	}
	resultPanelTextArea.append("\n\tAverage\t" );
	for (int i = 0; i <groupLength; i++) {
		resultPanelTextArea.append(AnalysisUtility.truncateDigits(groupMean[i], numberDigitKept) +"\t");
	}

	resultPanelTextArea.append("\n\n\n\tGrand Average = " + result.getFormattedDouble(grandMean));
	resultPanelTextArea.append("\n\tDegrees of Freedom = " + df);
	resultPanelTextArea.append("\n\tSum of Squares = " + result.getFormattedDouble(sumSq));
	resultPanelTextArea.append("\n\tChi-Square Statistics = " + result.getFormattedDouble(chiStat));
	resultPanelTextArea.append("\n\tP-Value = " + result.getFormattedDouble(pValue));

	resultPanelTextArea.append("\n" );

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

        public void reset() {
        	super.reset();
        	independentHeaderArray = null;
        }


    /** Implementation of PropertyChageListener.*/
    public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();
		if(propertyName.equals("DataUpdate")) {
			//update the local version of the dataTable by outside source
			dataTable = (JTable)(e.getNewValue());
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
		}
   }
	public Container getDisplayPane() {
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		return this.getContentPane();
	}

	protected void resetGraph()
	{

	}
	public String getOnlineDescription(){
		return onlineDescription;
	}

	protected void setResultPanel() {
		super.setResultPanel();
		resultPanelTextArea.setRows(30);
		resultPanelTextArea.setColumns(60);
	}

	protected void setMappingPanel() {
		super.setMappingPanel();
		tools1.setVisible(false);
		dependentPane.setVisible(false);

    }


}

