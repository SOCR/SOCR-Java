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


// below from Jpan_btn



// added to enalbe "Frame"

//import java.awt.Toolkit;


// added to get graphPanel

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;

import edu.ucla.stat.SOCR.analyses.example.ClusteringExamples;



import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

import java.awt.Dimension;

import java.awt.Color;

import java.util.LinkedList;


import java.util.Vector;


import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import Jama.Matrix;
import javax.swing.JOptionPane;

import java.util.HashMap;
import java.util.Map;


import java.util.Arrays;


/** this class is for Logistic Regression only. */
public class PrincipalComponentAnalysis extends Analysis implements PropertyChangeListener {
  
        private int dataRow = 0; // added
        private int dataColumn = 0; // added
        
        private Map<Double,double[]> map;
        private double EValueArray[];
        private double EVMatrix[][];
        private EigenDecomposition storedData;
    
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
        private edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk f;
        private static LinkedList<String[]> ClusteringData;
	private double[] xData = null;
        private double[] xData1 = null;
	private double[][] xDataArray = null;
	private double[] yData = null;
        private double[] yData1 = null;
	private double[] predicted = null;
	private double[] residuals = null;
	private double[] sortedResiduals = null;
	private double[] sortedStandardizedResiduals = null;
	private int[] sortedResidualsIndex = null;
	private double[] sortedNormalQuantiles = null;
	private double[] sortedStandardizedNormalQuantiles = null;

	private String dependentHeader = null, independentHeader = null;
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
	
	ClusteringResult result;
	int independentListLength;
	int xLength, yLength;
	

	/**Initialize the Analysis*/
	public void init(){
            mapIndep = false;
            showMapping= false;  // added
            showDendro = false;  // added: only for Clustering
            showGraph = true;
            showPCA = true;
            
		showInput = false;
		showSelect = false;
		showVisualize= false;
		super.init();
		
		analysisType = AnalysisType.PRINCIPAL_COMPONENT_ANALYSIS;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = false;
		useServerExample = false;
		
		useStaticExample = ClusteringExamples.availableExamples;

		onlineDescription = "http://en.wikipedia.org/wiki/Logistic_regression";
		depMax = 1; // max number of dependent var
		indMax = 15; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Regression & Correlation Analysis");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
                
                setPCADataPanel();

		// use the new JFreeChar function. annie che 20060312
          
		chartFactory = new Chart();
		resetGraph();
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
	
                if (!hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			return;
		}

		Data data = new Data();
                
                String firstMessage = "Would you like to use all the data columns in this Principal Components Analysis?";
                String title = "SOCR - Principal Components Analysis";
                String secondMessage = "Please enter the column numbers (seperated by comma) that you would like to use.";
                String columnNumbers = "";
                
                int reply = JOptionPane.showConfirmDialog(null, firstMessage, title, JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                        String cellValue = null;
                int originalRow = 0;
                
                for (int k = 0; k < dataTable.getRowCount(); k++) 
                {
			cellValue = ((String)dataTable.getValueAt(k, 0));
                
			if (cellValue != null && !cellValue.equals("")) {
				originalRow++;
						
                                }
                }
                
                cellValue = null;
                int originalColumn = 0;
                
                for (int k = 0; k < dataTable.getColumnCount(); k++) 
                {
			cellValue = ((String)dataTable.getValueAt(0, k));
                
			if (cellValue != null && !cellValue.equals("")) {
				originalColumn++;
						
                                }
                }
                
                dataRow = originalRow;
                dataColumn = originalColumn;
                
                String PCA_Data1[][] = new String[originalRow][originalColumn];
                double PCA_Data[][] = new double[originalRow][originalColumn];
                                
                
		for (int k =0; k < originalRow; k++) 
                    for (int j = 0; j < originalColumn; j++){
                    
		    if (dataTable.getValueAt(k, j) != null && !dataTable.getValueAt(k, j).equals("")) {
			PCA_Data1[k][j] = (String)dataTable.getValueAt(k, j);
                        PCA_Data[k][j] = Double.parseDouble(PCA_Data1[k][j]);
                    }
		}

                
                double PCA_Adjusted_Data[][] = new double[originalRow][originalColumn];
                double column_Total = 0;
                double column_Mean = 0;
                
                for (int j = 0; j < originalColumn; j++)
                    for (int k = 0; k < originalRow; k++)
                    {
                        column_Total += PCA_Data[k][j]; 
                        
                        if (k == (originalRow - 1))
                        {
                            column_Mean = column_Total/originalRow;
                            
                            for (int p = 0; p < originalRow; p++) 
                            {
                                 PCA_Adjusted_Data[p][j] = PCA_Data[p][j] - column_Mean;
                            }
                            column_Total = 0;
                            column_Mean = 0;
                        }
                    }

                Covariance cov = new Covariance(PCA_Adjusted_Data);
                RealMatrix matrix = cov.getCovarianceMatrix();
                
                EigenDecomposition eigenDecomp = new EigenDecomposition(matrix, 0);
                
                storedData = eigenDecomp;
                
                RealMatrix eigenvectorMatrix = eigenDecomp.getV();
                
                EValueArray = eigenDecomp.getRealEigenvalues();
                 
                
                eigenvectorMatrix = eigenvectorMatrix.transpose();
                
                double eigenvectorArray[][] = eigenvectorMatrix.getData();
                
                /*for (int j = 0; j < 3; j++)
                    for (int k = 0; k < 3; k++)
                    {
                        System.out.println(eigenvectorArray[j][k] + " ");
                    } */
                
                
                Matrix matrix1 = new Matrix(eigenvectorArray);
                Matrix matrix2 = new Matrix(PCA_Adjusted_Data);
                matrix2 = matrix2.transpose();
                
                
                
                Matrix finalProduct = matrix1.times(matrix2);
                finalProduct = finalProduct.transpose();
                
                double finalArray[][] = finalProduct.getArrayCopy();
                
                
                for (int j = 0; j < originalRow; j++)
                    for (int k = 0; k < originalColumn; k++)
                    {
                         PCATable.setValueAt(finalArray[j][k], j, k);
                    }
                
                    xData = new double[originalRow];
                    yData = new double[originalRow];
                
                    for (int i = 0; i < originalRow; i++)
                    {
                        xData[i] = finalArray[i][0];
                    }
                    for (int i = 0; i < originalRow; i++)
                    {
                        yData[i] = finalArray[i][1];
                    }
                    
                    dependentHeader = "C1";
                    independentHeader = "C2";
                }
                
                else {                                                  // start here
                       try {
			 columnNumbers = JOptionPane.showInputDialog(secondMessage);
                            } catch (Exception e) {
                                 }
                     
                
                String columnNumbersFinal = "," + columnNumbers.replaceAll("\\s","") + ",";
                
                
                Vector<Integer> locationOfComma = new Vector<Integer>(100);
                
                for (int i = 0; i < columnNumbersFinal.length(); i++)
                {
                    char d = columnNumbersFinal.charAt(i);
                    if (d == ',')
                        locationOfComma.add(i);
                }
                
                Vector<Integer> vector = new Vector<Integer>(100); // vector containing column selected numbers
                
                for (int i = 0; i < locationOfComma.size()-1; i++)
                {
                    String temp = columnNumbersFinal.substring(locationOfComma.get(i)+1, locationOfComma.get(i+1));
                    if (temp == "")
                        continue;
                    vector.add((Integer.parseInt(temp)-1));

                }
                
                dependentHeader = "C" + (vector.get(0)+1);
                independentHeader = "C" + (vector.get(1)+1);
                
                    
                    // System.out.println("Vector size is: " + vector.size() + "\n");                
                
                String cellValue = null;
                int originalRow = 0;
                
                for (int k = 0; k < dataTable.getRowCount(); k++) 
                {
			cellValue = ((String)dataTable.getValueAt(k, 0));
                
			if (cellValue != null && !cellValue.equals("")) {
				originalRow++;
						
                                }
                }
                
               
                int originalColumn = vector.size();
                
                dataRow = originalRow;
                dataColumn = originalColumn;
                
                String PCA_Data1[][] = new String[originalRow][originalColumn];
                double PCA_Data[][] = new double[originalRow][originalColumn];
                                
                
		for (int k = 0; k < originalRow; k++) 
                    for (int j = 0; j < originalColumn; j++){
                    
		    if (dataTable.getValueAt(k, vector.get(j)) != null && !dataTable.getValueAt(k, vector.get(j)).equals("")) {
			PCA_Data1[k][j] = (String)dataTable.getValueAt(k, vector.get(j));
                        PCA_Data[k][j] = Double.parseDouble(PCA_Data1[k][j]);
                    }
		}
                    
                
                double PCA_Adjusted_Data[][] = new double[originalRow][originalColumn];
                double column_Total = 0;
                double column_Mean = 0;
                
                for (int j = 0; j < originalColumn; j++)
                    for (int k = 0; k < originalRow; k++)
                    {
                        column_Total += PCA_Data[k][j]; 
                        
                        if (k == (originalRow - 1))
                        {
                            column_Mean = column_Total/originalRow;
                            
                            for (int p = 0; p < originalRow; p++) 
                            {
                                 PCA_Adjusted_Data[p][j] = PCA_Data[p][j] - column_Mean;
                            }
                            column_Total = 0;
                            column_Mean = 0;
                        }
                    }

                Covariance cov = new Covariance(PCA_Adjusted_Data);
                RealMatrix matrix = cov.getCovarianceMatrix();
                           
                EigenDecomposition eigenDecomp = new EigenDecomposition(matrix, 0);
                
                storedData = eigenDecomp;
                
                RealMatrix eigenvectorMatrix = eigenDecomp.getV();
                
                EValueArray = eigenDecomp.getRealEigenvalues(); // added              
                
                eigenvectorMatrix = eigenvectorMatrix.transpose();
                
                double eigenvectorArray[][] = eigenvectorMatrix.getData();
                
                /*for (int j = 0; j < 3; j++)
                    for (int k = 0; k < 3; k++)
                    {
                        System.out.println(eigenvectorArray[j][k] + " ");
                    } */
                
                
                Matrix matrix1 = new Matrix(eigenvectorArray);
                Matrix matrix2 = new Matrix(PCA_Adjusted_Data);
                matrix2 = matrix2.transpose();
                
                
                
                Matrix finalProduct = matrix1.times(matrix2);
                finalProduct = finalProduct.transpose();
                
                double finalArray[][] = finalProduct.getArrayCopy();
                
                
               /* for (int j = 0; j < dataTable.getColumnCount(); j++)
                    for (int k = 0; k < dataTable.getRowCount(); k++)
                    {
                        System.out.println(finalArray[j][k] + " ");
                    }*/
                
                
                for (int j = 0; j < originalRow; j++)
                    for (int k = 0; k < originalColumn; k++)
                    {
                         PCATable.setValueAt(finalArray[j][k], j, k);
                    }
                
                xData = new double[originalRow];
                yData = new double[originalRow];
                
                for (int i = 0; i < originalRow; i++)
                {
                    xData[i] = finalArray[i][0];
                }
                
                for (int i = 0; i < originalRow; i++)
                {
                    yData[i] = finalArray[i][1];
                }
            }
                
            map = new HashMap<Double,double[]>();
            
            for (int i = 0; i < dataColumn; i++)
            {
                map.put(EValueArray[i], storedData.getEigenvector(i).toArray());
            }
            
            Arrays.sort(EValueArray);
            
             xData1 = new double[EValueArray.length];  // for Scree Plot
             yData1 = new double[EValueArray.length];
                
             for (int i = 0; i < EValueArray.length; i++)
             {
                  xData1[i] = i+1;
             }
             
             for (int i = 0; i < EValueArray.length; i++)
             {
                  yData1[i] = EValueArray[i];
             }
             
            for(int i = 0; i < yData1.length / 2; i++)
            {
                double temp = yData1[i];
                yData1[i] = yData1[yData1.length - i - 1];
                yData1[yData1.length - i - 1] = temp;
            }
            
            for(int i = 0; i < xData1.length; i++)
            {   
                System.out.println("xData1 contains: " + xData1[i] + "\n");
            }
            
            for(int i = 0; i < yData1.length; i++)
            {   
                System.out.println("yData1 contains: " + yData1[i] + "\n");
            }
             
            
            for(int i = 0; i < EValueArray.length / 2; i++)
            {
                double temp = EValueArray[i];
                EValueArray[i] = EValueArray[EValueArray.length - i - 1];
                EValueArray[EValueArray.length - i - 1] = temp;
            }
            
            resultPanelTextArea.append("Click on \"PCA RESULT\" panel to view the transformed data (Eigenvector Transposed * Adjusted Data Transposed)");
            
            resultPanelTextArea.append("\n\nThe real eigenvalues (in descending order) are: \n\n");
            resultPanelTextArea.append("" + round(EValueArray[0],3));
            
            for (int i = 1; i < EValueArray.length; i++)
            {
                resultPanelTextArea.append("\n"+ round(EValueArray[i],3));
            }
            resultPanelTextArea.append("\n\nThe corresponding eigenvectors (in columns) are: \n\n");
            
            double temp[] = new double [100];
            
            
            for (int j = 0; j < temp.length; j++)
                for (int i = 0; i < EValueArray.length; i++)
                {
                    temp = map.get(EValueArray[i]);
                    resultPanelTextArea.append("" + round(temp[j],3) + "\t");
                    if (i == EValueArray.length-1)
                    {
                        resultPanelTextArea.append("\n");
                    }
                }

            doGraph();
        }
               
		/* every one has to have its own, otherwise one Exception spills the whole. */

		/* doGraph is underconstruction thus commented out. annie che 20060314 */
		//if (useGraph)
	
	
	public void updateResults(){
		/*******************************************************/
		////System.out.println("FINISH try result = " + result);
		// Retreive the data from Data Object using HashMap
		int varLength = independentListLength + 1;
		double[] beta = null;
		double[] seBeta =null;
		double[] tStat = null;
		double[] pValue = null;
		int dfError = 0;
		double rSquare = 0;

		//ArrayList varName = null;
		String[] varList = null;
		
		if (result==null)
			return;
		
		result.setDecimalFormat(dFormat);
		
		resultPanelTextArea.setText("\n");//clear first
		resultPanelTextArea.append("\tNumber of Independent Variable(s) = "  + independentListLength);


		resultPanelTextArea.append("\n\tSample Size =" + xLength);

		resultPanelTextArea.append("\n\tDependent Variable  = "  + dependentHeader);// + " original index = " + dependentIndex );
		resultPanelTextArea.append("\n\tIndependent Variable(s) = ");
		
		for (int i = 0; i < independentListLength; i++) {
			resultPanelTextArea.append("  "  + independentHeaderArray[i]);// + " original index = " + varIndex );
		}
		
		try {
			//varName = (ArrayList)(result.getTexture().get(MultiLinearRegressionResult.VARIABLE_LIST));
			varList = result.getVariableList();
			////System.out.println("varList = " + varList);

			//(String[])(result.getTexture().get(MultiLinearRegressionResult.VARIABLE_LIST));
		} catch (NullPointerException e) {
			////System.out.println("varList e = " + e);

			//showError("NullPointerException  = " + e);
		}
		try {
			beta = result.getBeta();//(double[])(result.getTexture().get(MultiLinearRegressionResult.BETA));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			seBeta = result.getBetaSE();//(double[])(result.getTexture().get(MultiLinearRegressionResult.BETA_SE));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			tStat = result.getBetaTStat();//(double[])(result.getTexture().get(MultiLinearRegressionResult.T_STAT));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			pValue = (double[]) (result.getBetaPValue());//(String[])(result.getTexture().get(MultiLinearRegressionResult.P_VALUE));
		} catch (NullPointerException e) {
		}
		try {
			rSquare = result.getRSquare();//(String[])(result.getTexture().get(MultiLinearRegressionResult.P_VALUE));
		} catch (NullPointerException e) {
		}

		try {
			dfError = result.getDF();//(Integer)result.getTexture().get(MultiLinearRegressionResult.DF_ERROR)).intValue();
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		try {
			predicted = result.getPredicted();//(double[])(result.getTexture().get(MultiLinearRegressionResult.PREDICTED));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			residuals = result.getResiduals();//(double[])(result.getTexture().get(MultiLinearRegressionResult.RESIDUALS));
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);

		try {
			sortedResiduals = result.getSortedResiduals();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			sortedStandardizedResiduals = result.getSortedStandardizedResiduals();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_STANDARDIZED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		try {
			sortedResidualsIndex = result.getSortedResidualsIndex();//(int[])residualMap.get(MultiLinearRegressionResult.SORTED_RESIDUALS_INDEX);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			sortedNormalQuantiles = result.getSortedNormalQuantiles();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();//(double[])residualMap.get(MultiLinearRegressionResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		resultPanelTextArea.append("\n\n\tRegression Model:\n\t\t" + dependentHeader + " = ");
                resultPanelTextArea.append(" "+ "1/(1+exp(-Z)\n\t\t");
                resultPanelTextArea.append("where Z = ");
                
		for (int i = 0; i < beta.length; i++) {
			if (i==0) resultPanelTextArea.append(" "+ result.getFormattedDouble(beta[i]));
			else if (beta[i]<0)
				resultPanelTextArea.append(" "+ result.getFormattedDouble(beta[i])+"*"+varList[i]);
			else resultPanelTextArea.append(" +"+ result.getFormattedDouble(beta[i])+"*"+varList[i]);
		}
		resultPanelTextArea.append(".\n\n");
		
		for (int i = 0; i < beta.length; i++) {
			//resultPanelTextArea.append("\n\n"+varName.get(i) + "\n\tEstimate = "+ beta[i] + "\n\tStd. Error" + seBeta[i] + "\n\t t-valuer" + tStat[i] + "\n\tp-value " + pValue[i]);

			resultPanelTextArea.append("\n\n\t"+varList[i] + ":\n\tEstimate = "+ result.getFormattedDouble(beta[i]) + "\n\tStandard Error = " + result.getFormattedDouble(seBeta[i]) + "\n\tT-Value = " + result.getFormattedDouble(tStat[i]));

			/*if (pValue[i].equals("0.0")) {
				resultPanelTextArea.append("\n\tP-Value: <2E-16");
			}
			else {*/
			//resultPanelTextArea.append("\n\tP-Value = " + AnalysisUtility.enhanceSmallNumber(pValue[i]));
			resultPanelTextArea.append("\n\tP-Value = " +result.getFormattedDouble(pValue[i]));

		}
		resultPanelTextArea.append("\n\n\tR-Square = " + result.getFormattedDouble(rSquare));


/*
		resultPanelTextArea.append("\nPREDICTED        = " );


		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + predicted[i]);
		}
		resultPanelTextArea.append("\nRESIDUALS        = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + residuals[i]);
		}

		resultPanelTextArea.append("\nRESIDUALS SORTED= " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedResiduals[i]);
		}
		resultPanelTextArea.append("\nRESIDUALS INDEX SORTED= " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedResidualsIndex[i]);
		}
		resultPanelTextArea.append("\nRESIDUALS NORMAL QUANTILES = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedNormalQuantiles[i]);
		}

		resultPanelTextArea.append("\nStandardized RESIDUALS Standardized = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedStandardizedResiduals[i]);
		}
		resultPanelTextArea.append("\nStandardized NORMAL QUANTILES = " );

		for (int i = 0; i < xLength; i++) {
			resultPanelTextArea.append(" " + sortedStandardizedNormalQuantiles[i]);
		}

*/		resultPanelTextArea.append("\n" );

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
   	protected void doGraph() {

            // graphComponent is available here
		// data: variables double xData, yData, residuals, predicted are available here after doAnalysis() is run.
		graphPanel.removeAll();
		/************************************/
		JPanel innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		graphPanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		ChartPanel chartPanel = null;

		// 1. scatter plot of data: yData vs. xData
		////////System.out.println("MLR doGraph independentHeaderArray.length = " + independentHeaderArray.length);
		
                JFreeChart scatterChart = chartFactory.getLineChart("Scatter Plot of " + dependentHeader + " vs. " + independentHeader, independentHeader, dependentHeader, xData, yData, "noline");//getChart(title, xlabel, ylabel, xdata,ydata)

			//JFreeChart scatterChart = chartFactory.getQQChart("Scatter Plot of " + dependentHeader + " vs " + independentHeader, independentHeader, dependentHeader, dependentHeader , xData, yData,  "", 0, 0, "");


		chartPanel = new ChartPanel(scatterChart, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);		             
                
                JFreeChart screePlot = chartFactory.getLineChart("Scree Plot of Eigenvalues", "Ordering of Eigenvalues (Descending Order)" , "Eigenvalues", xData1, yData1);//getChart(title, xlabel, ylabel, xdata,ydata)

                chartPanel = new ChartPanel(screePlot, false);
		chartPanel.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel);


		graphPanel.validate();
            
	}
	protected void resetGraph()
	{
		JFreeChart chart = chartFactory.createChart(); // an empty  chart
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(400,300));
		graphPanel.removeAll();
		graphPanel.add(chartPanel);
		graphPanel.validate();

	}
        
        public static LinkedList<String[]> getClusteringData(){
            return ClusteringData;
        }
	public String getOnlineDescription(){
		return onlineDescription;
	}
        
        public static double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
}
}