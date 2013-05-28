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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.example.CIExamples;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.model.ConfidenceInterval;
import edu.ucla.stat.SOCR.analyses.result.ConfidenceIntervalResult;
import edu.ucla.stat.SOCR.analyses.util.ConfidenceControlPanel;
import edu.ucla.stat.SOCR.analyses.util.IntervalType;
import edu.ucla.stat.SOCR.analyses.util.ConfidenceCanvasGeneralUpper;

/** Confidence Interval */
public class ConfidenceIntervalAnalysis extends Analysis implements PropertyChangeListener, Observer{
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.
	ConfidenceControlPanel confidenceControlPanel;

	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private ConfidenceIntervalResult result;
	int dfCTotal, dfError, dfModel;
	String rssModelString, rssErrorString;
	String mssModelString, mssErrorString;
	String rssTotalString, fValueString;
	String  rSquareString;
	double pValue;
	int xLength;
	int yLength;
	

	protected String dependentHeaders[];	
	protected String independentHeaders[];
	protected int independentVarLength; 
	protected int dependentVarLength;  
	protected String[][] indepValues;
	protected String[][] depValues;	
	boolean trimColumn;
	protected int xyLength;	
	protected String values_storage[][];
	
	int nTrials;
	double alpha;
	int cvIndex;
	IntervalType ci_choice;
	double left, right, knownVariance;
	double[][] sampleData, ciData;
	double[] xBar;
	
	int[] sampleSizes;
	//ConfidenceCanvasGeneral confidenceCanvas;
	ConfidenceCanvasGeneralUpper confidenceCanvas;

	/**Initialize the Analysis*/
	public void init(){
		mapIndep= false;
		INPUT = "CI SETTINGS";
		showSelect = false;
		showVisualize= false;
		confidenceControlPanel = new ConfidenceControlPanel(this);
		confidenceControlPanel.setPreferredSize(new Dimension(200, 200));
		confidenceControlPanel.addObserver(this);
		super.init();
 	
		analysisType = AnalysisType.CI;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = CIExamples.availableExamples;
		onlineDescription = "";
		depLabel.setText(VARIABLE);
		
		depMax = 100; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		getFrame(this);
		analysisName = "Confidence Interval";
		setName(analysisName);
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
	
		resetGraph();

		validate();
		//reset();
	}
	
	 protected void setInputPanel() {
			//System.out.println("dp setInputPanel");
			inputPanel.removeAll();
		
			inputPanel.setPreferredSize(new Dimension(200, 200));
			//inputPanel.setBackground(Color.WHITE);
			
			inputPanel.add(confidenceControlPanel);
			alpha = confidenceControlPanel.getAlphaValue();
			ci_choice = confidenceControlPanel.whichIntervalSelected();
		}


	protected void setArrayFromTable(){
		//independentVarLength = lModel3.getSize();
		dependentVarLength = lModel2.getSize();


		int[] independentVar = new int[independentVarLength];	
		int[] dependentVar = new int[dependentVarLength];	

		dependentHeaders = new String[dependentVarLength];
	//	independentHeaders = new String[independentVarLength];

		//independentVars store the column index for indep
	    int indep = -1; int dep = -1;	
		for (int i = 0; i < listIndex.length; i++) {	
			
			if (listIndex[i]==3){
				indep++;	
				independentHeaders[indep] = columnModel.getColumn(i).getHeaderValue().toString().trim();
				independentVar[indep] = i;
			}else 	if (listIndex[i]==2){
				dep++;	
				dependentHeaders[dep] = columnModel.getColumn(i).getHeaderValue().toString().trim();
				dependentVar[dep] = i;
			}

		}	

		int yLength = 0;
		//resultPanelTextArea.append("\nRESULT:\n" );

		String cellValue = null;
		ArrayList<String> yList = new ArrayList<String>();

		try {
			//dependent Y
			yLength = dataTable.getRowCount();
			depValues = new String[yLength][dependentVarLength];
		//	indepValues = new String[yLength][independentVarLength];
			sampleSizes= new int[dependentVarLength];
			xyLength=0;
			
			for (int index2 = 0; index2 < dependentVarLength; index2++) { 
				yList = new ArrayList<String>();
				int yyLength =  0;
				for (int k =0; k < dataTable.getRowCount(); k++) {
					try {
						cellValue = ((String)dataTable.getValueAt(k,dependentVar[index2])).trim();
						//System.out.println("cellValue["+k+"]=*"+cellValue+"*");
						if (cellValue != null && cellValue.length()!=0) {
							yList.add(yyLength , cellValue);
							yyLength ++;
						}
						
						/*yList.add(yLength , cellValue);
						yLength ++;*/
					} catch (Exception e) { // do nothing?					
					}
				}
			
				for (int i = 0; i < yyLength; i++) {
					depValues[i][index2] = (String)yList.get(i);  //can be null, handle it later
					//System.out.println("depValues["+i+"]["+index2+"]="+depValues[i][index2]);
				}
				
				sampleSizes[index2]= yyLength;
				
				if (xyLength<yyLength)
					xyLength=yyLength;
			}
			
			
		}catch (Exception e) {
			showError("Exception In outer catch: " + e );
		}
		
	//	System.out.println("Chart: setArrayFromTable: independentVarLength="+independentVarLength);
}
	
	/***/
	public void doAnalysis(){
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		if (! hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}
		if (dependentIndex < 0 ) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}
		
		trimColumn = false;
		setArrayFromTable();
		//System.out.println("dependentVarLength="+dependentVarLength);
		//System.out.println("xyLength="+xyLength);
		
		values_storage= new String[dependentVarLength][xyLength];
	//	String[][] y= new String[xyLength][independentVarLength];

		Data data = new Data();
		
		//pass sampleData
		for (int index=0; index<dependentVarLength; index++){
			String[] xData = new String[xyLength];
			for (int i = 0; i < sampleSizes[index]; i++){
				values_storage[index][i] = depValues[i][index];
				if(depValues[i][index]!=null && depValues[i][index].length()!=0)
					xData[i] = depValues[i][index];
				
			}
			data.appendX(dependentHeaders[index], xData, DataType.QUANTITATIVE);
			//System.out.println("appending "+ dependentHeaders[index]+" "+xData[0]);
		}	
		 
		nTrials = dependentVarLength;
	
		alpha = confidenceControlPanel.getAlphaValue();
		cvIndex = confidenceControlPanel.getAlphaIndex();
		ci_choice = confidenceControlPanel.whichIntervalSelected();
		left = confidenceControlPanel.getLeftCutOffValue();
		right = confidenceControlPanel.getRightCutOffValue();
		knownVariance = confidenceControlPanel.getKnownVariance();
		//System.out.println("setting alpha="+alpha+" ci_choice ="+ci_choice.name());
		data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_N_TRAILS,nTrials+"");
		data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_XY_LENGTH, xyLength+"");
		data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_CV_INDEX, cvIndex+"");
		data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_CHOICE, ci_choice);
		data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_LEFT, left+"");
		data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_RIGHT, right+"");
		data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_KNOWN_VARIANCE, knownVariance+"");
		result = null;
		try {
			result = (ConfidenceIntervalResult)data.getAnalysis(AnalysisType.CI);

		} catch (Exception e) {
			//////System.out.println("result NullPointerException  = " + e);
		}
		
		//yAxisLabel = dependentHeader;
		
		xBar= new double[nTrials];

		ciData = result.getCIData();
		sampleData = result.getSampleData();
		xBar = result.getXBar();
		
		doGraph();
		updateResults(); //set format in this method
	}

	public void updateResults(){
		if (result==null)
			return;
		
		result.setDecimalFormat(dFormat);

		
		resultPanelTextArea.setText("\n");//clear first
		resultPanelTextArea.append(nTrials +" samples, \n\t "+ci_choice.toString()+"\t"+ci_choice.getBootStrapSizeString());
		if (confidenceControlPanel.isMeanIntervalKnownVariance())
			resultPanelTextArea.append(" KnownVariance = "+knownVariance);
		resultPanelTextArea.append("\n\t Alpha = "+ (1-confidenceControlPanel.getAlphaValue())+ "\n");
		if (confidenceControlPanel.isProportionInterval())
			resultPanelTextArea.append("\t LeftCutOff: "+left+" RightCutOff: "+right+"\n");
		resultPanelTextArea.append("----------------------------------------------------------------------------------------------------------------\n\n\n");
		for(int i=0; i<nTrials; i++){
			
			resultPanelTextArea.append(dependentHeaders[i]+": \n");
			resultPanelTextArea.append("\tsampleSize = " + sampleSizes[i] + " \n" );
			resultPanelTextArea.append("\t["+ciData[i][1] + ", "+ciData[i][0] +"],   median: "+ xBar[i]+" \n" );
			resultPanelTextArea.append("--------------\n\n");
		}
		
		resultPanelTextArea.append("\n\n" );

		resultPanelTextArea.setForeground(Color.BLUE);
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

		graphPanel.removeAll();
		JPanel innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		graphPanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
		
		confidenceCanvas= new ConfidenceCanvasGeneralUpper(sampleSizes, nTrials);
		confidenceCanvas.setBackground(Color.white);
		
		innerPanel.setPreferredSize(new Dimension(650, 400)); 
		confidenceCanvas.setIntervalType(ci_choice);
		innerPanel.add(confidenceCanvas);
		
		confidenceCanvas.update(cvIndex, ciData, sampleData, xBar);
	
	/*	String[][] ci_storage= new String[xyLength][nTrials];
		for (int i=0; i<nTrials; i++)
			for(int j=0; j<sampleSizes[i]; j++)
				ci_storage[j][i] = sampleData[i][j]+"";
		
		JFreeChart bwChart = chartFactory.getBoxAndWhiskerChart("Box and Whisker Plot", independentHeader, "yHeader",  nTrials, 1, xyLength, ci_storage);
		ChartPanel chartPanel = new ChartPanel(bwChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);
		*/
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

	public void update(Observable ob, Object arg1) {
		//System.out.println("CIAnalysis update get called");
	
		if(arg1.equals(confidenceControlPanel)){
			//System.out.println("ci_choice updated");
			inputPanel.repaint();
		}
	}

}


