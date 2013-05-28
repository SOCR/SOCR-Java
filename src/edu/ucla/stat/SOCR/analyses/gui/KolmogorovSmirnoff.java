/*
Copyright (C) 2002 Ivo D. Dinov, Ph.D.

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 2 of the License, or (at your option)
any later version.

This program is distributed in the hope that it will be useful, but without
any warranty; without even the implied warranty of merchantability or
fitness for a particular purpose. See the GNU General Public License for
more details. http://www.gnu.org/copyleft/gpl.html

http://www.stat.ucla.edu/~dinov
*/

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
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.analyses.example.*;
import org.jfree.chart.*;

/** Two Independent T Test */
public class KolmogorovSmirnoff extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.

	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;

	private double[] sortedX1 = null, sortedX2 = null, y1 = null, y2 = null;
	private double[] logX1 = null, logX2 = null;
	private String varHeader0, varHeader1;

	KolmogorovSmirnoffResult result = null;
	int xLength = 0;
	int yLength = 0;

	/**Initialize the Analysis*/
	public void init(){
		showSelect = false;
		showVisualize= false;
		showInput = false;
		super.init();
		analysisType = AnalysisType.KOLMOGOROV_SMIRNOFF;
		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = KolmogorovSmirnoffExamples.availableExamples;
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
		//////System.out.println("KolmogorovSmirnoff start doAnalysis");
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
		////System.out.println("KolmogorovSmirnoff varHeader0 = "+varHeader0);
		////System.out.println("KolmogorovSmirnoff varHeader1 = "+varHeader0);
		//JOptionPane.showMessageDialog(this, "In regression, DependentIndex = "+dependentIndex);
		//JOptionPane.showMessageDialog(this, "In regression, IndependentIndex = "+independentIndex);

		Data data = new Data();
		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/

		xLength = 0;
		yLength = 0;
		String cellValue = null;
		ArrayList<Object> xList = new ArrayList<Object>();
		ArrayList<Object> yList = new ArrayList<Object>();
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
			////System.out.println("Exception In outer catch: " + e );
		}
		if (xLength <= 0 || yLength <= 0) {
			JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
			return;
		}

		double[] x = new double[xLength];
		double[] y = new double[yLength];
		//System.out.println("yLength = " + yLength);
		//System.out.println("xLength = " + xLength);

		for (int i = 0; i < yLength; i++) {
			y[i] = Double.parseDouble((String)yList.get(i));
			//System.out.println("\nY = "+y[i] );
			resultPanelTextArea.append("\nY = "+y[i] );
		}
		for (int i = 0; i < xLength; i++) {
			x[i] = Double.parseDouble((String)xList.get(i));
			//System.out.println("\nX = "+x[i] );
			resultPanelTextArea.append("\nX = "+x[i] );

		}

		data.appendX("X", x, DataType.QUANTITATIVE);
		data.appendY("Y", y, DataType.QUANTITATIVE);


		try {
			result = (KolmogorovSmirnoffResult)data.getAnalysis(AnalysisType.KOLMOGOROV_SMIRNOFF);
		} catch (Exception e) {
			//System.out.println("result Excepion " +e + "");
		}

		updateResults(); //set format in this method

		doGraph();
	}

	public void updateResults(){
		if (result==null)
			return;

		// Retreive the data from Data Object using HashMap
		double dStat = -1;
		double[] diff = null;
		double[] absDiff = null;

		//double[] sortedX1 = null, sortedX2 = null, y1 = null, y2 = null;
		//double[] logX1 = null, logX2 = null;

		double mean1 = 0, mean2 = 0, sd1 = 0, sd2 = 0, median1 = 0, median2 = 0;
		double q1x1 = 0, q3x1 = 0, q1x2 = 0, q3x2 = 0;

		double[] ci95X1 = null, ci95X2 = null;
		String lowerOutlier1 = null, lowerOutlier2 = null, upperOutlier1 = null, upperOutlier2 = null;

		result.setDecimalFormat(dFormat);
		try {
			sortedX1 = result.getVariable1X();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}


		try {
			sortedX2 = result.getVariable2X();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}

		try {
			y1 = result.getVariable1Y();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}

		try {
			y2 = result.getVariable2Y();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}


		try {
			logX1 = result.getVariable1LogX();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}

		try {
			logX2 = result.getVariable2LogX();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}
		try {
			diff = result.getDiff();
		} catch (Exception e) {
			//System.out.println("diff Excepion " +e + "");
		}
		try {
			absDiff = result.getAbsDiff();
		} catch (Exception e) {
			//System.out.println("absDiff Excepion " +e + "");
		}


		try {
			dStat = result.getDStat();
		} catch (Exception e) {
			//System.out.println("dStat Excepion " +e + "");
		}
		////System.out.println("gui dstat= " + dStat);

		try {
			mean1 = result.getMean1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			mean2 = result.getMean2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			sd1 = result.getSD1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			sd2 = result.getSD2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}


		try {
			median1 = result.getMedian1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			median2 = result.getMedian2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			q1x1 = result.getFirstQuartile1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			q1x2 = result.getFirstQuartile2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			q3x1 = result.getThirdQuartile1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			q3x2 = result.getThirdQuartile2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}


		try {
			ci95X1 = result.getCI95X1();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}

		try {
			ci95X2 = result.getCI95X2();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			lowerOutlier1 = result.getBoxLowerOutlier1();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			lowerOutlier2 = result.getBoxLowerOutlier2();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			upperOutlier1 = result.getBoxUpperOutlier1();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			upperOutlier2 = result.getBoxUpperOutlier2();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		String zFormula = null;
		double z = 0, prob = 0;
		try {
			zFormula = result.getZFormula();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}

		try {
			z = result.getZStat();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			prob = result.getProb();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		resultPanelTextArea.setText("\n");//clear first
		/**************************************************************/
		resultPanelTextArea.append("\n\tVariable 1 = " + varHeader0);

		resultPanelTextArea.append("\n\tVariable 2 = " + varHeader1);

		resultPanelTextArea.append("\n\n\tKolmogorov-Smirnoff Test Result:\n" );

		resultPanelTextArea.append("\n\tVariable 1 = " + varHeader0);
		resultPanelTextArea.append("\n\tSample Size = " + yLength );
		resultPanelTextArea.append("\n\tSample Mean = " + result.getFormattedDouble(mean1));
		resultPanelTextArea.append("\n\tSample SD = " + result.getFormattedDouble(sd1));
		resultPanelTextArea.append("\n\t95% CI of the mean = (" + result.getFormattedDouble(ci95X1[0]) + ", " + result.getFormattedDouble(ci95X1[1]) +
		")");
		resultPanelTextArea.append("\n\tFirst Quartile = " + result.getFormattedDouble(q1x1));
		resultPanelTextArea.append("\n\tMedian = " + result.getFormattedDouble(median1));
		resultPanelTextArea.append("\n\tThird Quartile = " + result.getFormattedDouble(q1x1));
		resultPanelTextArea.append("\n\tBox plot outliers below Q1 - 1.5IQR: " + lowerOutlier1);
		resultPanelTextArea.append("\n\tBox plot outliers above Q3 - 1.5IQR: " + upperOutlier1);

		resultPanelTextArea.append("\n\n\tVariable 2 = " + varHeader1);
		resultPanelTextArea.append("\n\tSample Size = " + xLength);
		resultPanelTextArea.append("\n\tSample Mean = " + result.getFormattedDouble(mean2));
		resultPanelTextArea.append("\n\tSample SD = " + result.getFormattedDouble(sd2));
		resultPanelTextArea.append("\n\t95% CI of the mean = (" + result.getFormattedDouble(ci95X2[0]) + ", " + result.getFormattedDouble(ci95X2[1]) +
		")");
		resultPanelTextArea.append("\n\tFirst Quartile = " + result.getFormattedDouble(q1x2));
		resultPanelTextArea.append("\n\tMedian = " + result.getFormattedDouble(median2));
		resultPanelTextArea.append("\n\tThird Quartile = " + result.getFormattedDouble(q1x2));


		resultPanelTextArea.append("\n\tBox plot outliers below Q1 - 1.5IQR: " + lowerOutlier2);
		resultPanelTextArea.append("\n\tBox plot outliers above Q3 - 1.5IQR: " + upperOutlier2);


		if (dStat != -1) {
			resultPanelTextArea.append("\n\n\tKolmogorov-Smirnoff Test:");
			resultPanelTextArea.append("\n\tD-Statistics = " + result.getFormattedDouble(dStat));
			resultPanelTextArea.append("\n\t" + zFormula + "\tz = " + result.getFormattedDouble(z));
			resultPanelTextArea.append("\n\tCDF(" + result.getFormattedDouble(dStat) + ") = " + result.getFormattedDouble(prob));
			resultPanelTextArea.append("\n\t1 - CDF(" + result.getFormattedDouble(dStat) + ") = " + result.getFormattedDouble((1-prob)));
		}

		resultPanelTextArea.setForeground(Color.BLUE);

	}

	/*********************************************************************************/
   	protected void doGraph() {
		graphPanel.removeAll();
		JPanel innerPanel = new JPanel();
		JScrollPane graphPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		graphPanel.add(graphPane);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

		/******************************************************/
		JFreeChart scatterChart = null;
		ChartPanel chartPanel1 = null, chartPanel2 = null;
		Chart	chartFactory = new Chart();
		String lineType = "excludesZeroNoShape";

		double[][] x = new double[1][];
		double[][] y = new double[1][];
		String[] groupNames = null;

		/********* FIGURE 1, BEFORE TAKING LOG ****************/
		x[0] = sortedX1;
		y[0] = y1;
		groupNames = new String[]{varHeader0};
		scatterChart = chartFactory.getLineChart("Cumulative Factor Plot", varHeader0, "Probability", groupNames.length, groupNames, x, y, lineType);
		chartPanel1 = new ChartPanel(scatterChart, false);
		chartPanel1.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel1);

		x = new double[1][];
		y = new double[1][];
		x[0] = sortedX2;
		y[0] = y2;
		groupNames = new String[]{varHeader1};
		scatterChart = chartFactory.getLineChart("Cumulative Factor Plot", varHeader1, "Probability", groupNames.length, groupNames, x, y, lineType);
		chartPanel1 = new ChartPanel(scatterChart, false);
		chartPanel1.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel1);

		x = new double[2][];
		y = new double[2][];
		x[0] = sortedX1; x[1] = sortedX2;
		y[0] = y1; y[1] = y2;
		groupNames = new String[]{varHeader0, varHeader1};
		scatterChart = chartFactory.getLineChart("Cumulative Factor Plot", "X", "Probability", groupNames.length, groupNames, x, y, lineType);
		chartPanel1 = new ChartPanel(scatterChart, false);
		chartPanel1.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel1);

		scatterChart = null;

		/********* FIGURE 1, AFTER TAKING LOG *****************/
		x = new double[1][];
		y = new double[1][];
		x[0] = logX1;
		y[0] = y1;
		groupNames = new String[]{varHeader0};
		scatterChart = chartFactory.getLineChart("Cumulative Factor Plot (log)", "log(" + varHeader0 + ")", "Probability", groupNames.length, groupNames, x, y, lineType);
		chartPanel2 = new ChartPanel(scatterChart, false);
		chartPanel2.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel2);

		x = new double[1][];
		y = new double[1][];
		x[0] = logX2;
		y[0] = y2;
		groupNames = new String[]{varHeader1};
		scatterChart = chartFactory.getLineChart("Cumulative Factor Plot (log)", "log(" + varHeader0 + ")", "Probability", groupNames.length, groupNames, x, y, lineType);
		chartPanel2 = new ChartPanel(scatterChart, false);
		chartPanel2.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel2);


		x = new double[2][];
		y = new double[2][];
		x[0] = logX1; x[1] = logX2;
		y[0] = y1; y[1] = y2;
		groupNames = new String[]{varHeader0, varHeader1};
		scatterChart = chartFactory.getLineChart("Cumulative Factor Plot (log)", "log(X)", "Probability", groupNames.length, groupNames, x, y, lineType);
		chartPanel2 = new ChartPanel(scatterChart, false);
		chartPanel2.setPreferredSize(new Dimension(plotWidth,plotHeight));
		innerPanel.add(chartPanel2);


		graphPanel.validate();


	}

	/**************************************************************************************/
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
        return new String("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysisActivities_KolmogorovSmirnoff");
    }
}

