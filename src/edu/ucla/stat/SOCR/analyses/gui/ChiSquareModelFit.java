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

/* 	modified annie che 20070815.
	separate the gui part from the model part
*/

// this class is for goodness of fit.

// use ChiSquareContingencyTable for doing independence test and homogeneity test.

package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.ChiSquareModelFitExamples;

/** The Chi-Square Model Goodness-of-Fit Test */

public class ChiSquareModelFit extends Analysis implements PropertyChangeListener {
	// This must be the same as what's in the HashMap pointed by Data
	// Otherwise you won't get anything.


	//RegressionScatterPlot ScatterPlot;
	public JTabbedPane tabbedPanelContainer;
	//objects
	private JToolBar toolBar;

	private Frame frame;
	private String dependentHeader = null, independentHeader = null;

	double chiSquareStat = 0, pValue = 0;
	double O_E=0;
	int xLength = 0;
	int yLength = 0;
	int numberParameters = 0; // for example, Poisson takes one, normal takes two. etc.
	int df =0;
	
	/**Initialize the Analysis*/
	public void init(){
		showInput= false;
		showGraph = false;
		showSelect = false;
		showVisualize= false;
		super.init();

		analysisType = AnalysisType.CHI_SQUARE_MODEL_FIT;

		useInputExample = false;
		useLocalExample = false;
		useRandomExample = true;
		useServerExample = false;
		useStaticExample = ChiSquareModelFitExamples.availableExamples;
		depMax = 1; // max number of dependent var
		indMax = 1; // max number of independent var
		resultPanelTextArea.setFont(new Font(outputFontFace,Font.BOLD, outputFontSize));
		frame = getFrame(this);
		setName("Chi-Square Model Goodness-of-Fit Test");
		// Create the toolBar
		toolBar = new JToolBar();
		createActionComponents(toolBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		//ScatterPlot = new RegressionScatterPlot();
		//graphPanel.add( "Center", ScatterPlot);
		depLabel.setText("Observed Data");
		indLabel.setText("Expected Data");
		validate();
		reset();
	}

	public void setDecimalFormat(DecimalFormat format){
		dFormat= format;
	}
	

	/** Create the actions for the buttons */
	protected void createActionComponents(JToolBar toolBar){
		super.createActionComponents(toolBar);
	}


	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){
	}

	/** This method defines the specific statistical Analysis to be carried
		out on the user specified data. ChiSquare Test is done in this case. */
	public void doAnalysis(){
		if(dataTable.isEditing())
			dataTable.getCellEditor().stopCellEditing();
		else if (! hasExample ) {
			JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
			//return;
		}
		else if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
			JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
			return;
		}

		 String popMessage = "Enter Number of Model Parameters. Default is zero.";
		 String popWarning = "You didn't enter a number. Default zero will be used. \nClick on CALCULATE if you'd like to change it. \nOr, click on RESULT to see the results.";

		 try {
			 numberParameters = Integer.parseInt(JOptionPane.showInputDialog(popMessage));
		 } catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, popWarning);
		 } catch (Exception e) {
		 }

		////////System.out.println("\nSample size=" + dataTable.getRowCount() + " \n" );
		dependentHeader = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
		independentHeader = columnModel.getColumn(independentIndex).getHeaderValue().toString().trim();

		//Data data = new Data();

		/******************************************************************
		From this point, the code has been modified to work with input cells that are empty.
		******************************************************************/
		chiSquareStat = 0; 
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
			//System.out.println("Exception In outer catch: " + e );
		}

		double[] x = new double[xLength];
		double[] y = new double[yLength];
		for (int i = 0; i < yLength; i++) {
			y[i] = Double.parseDouble((String)yList.get(i));
			//resultPanelTextArea.append("\nY = "+y[i] );
		}
		for (int i = 0; i < xLength; i++) {
			x[i] = Double.parseDouble((String)xList.get(i));
			//resultPanelTextArea.append("\nX = "+x[i] );
		}

		// Set up data first
		// Call the Controller method getAnalysis() delegate the work to Model

		//data.appendX("OBS", x, DataType.QUANTITATIVE);
		//data.appendY("EXP", y, DataType.QUANTITATIVE);
		if (numberParameters >= xLength - 2) {
			popWarning = "Too many parameters. Default zero will be used.";
			numberParameters = 0;
			JOptionPane.showMessageDialog(this, popWarning);

		}
		

		df= xLength  - numberParameters - 1;
		
//System.out.println("xLength="+xLength);

		for (int i = 0; i < xLength; i++) {
			O_E = y[i] - x[i];
			if (x[i]<=0) chiSquareStat += (O_E * O_E)/(0.1);
			else chiSquareStat += (O_E * O_E)/x[i]; // x is expected (bottom panel in mapping panel)
			//resultPanelTextArea.append("\n | i = " + i + " | X = "+x[i] + " | Y = "+y[i]);
		}
		
			//System.out.println("chiSquareStat="+chiSquareStat);
		pValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(
				df)).getCDF(chiSquareStat);

		//System.out.println("\n Chi-Square P-VALUE = " + pValue);

		updateResults();

	}
	
	public void updateResults(){
	/*	if (result==null)
			return;
		
		result.setDecimalFormat(dFormat);*/
		
	
		setDecimalFormat(dFormat);
		
		resultPanelTextArea.setText("\n");//clear first
		resultPanelTextArea.append("\n\tObserved Data = " + dependentHeader + " \n" );
		resultPanelTextArea.append("\n\tExpected Data = " + independentHeader + " \n" );

		
		resultPanelTextArea.append("\n\tChi-Square Goodness of Fit Results:\n" );
		
		resultPanelTextArea.append("\n\tTotal Counts = " + xLength + " \n" );
		resultPanelTextArea.append("\n\tNumber of Parameters = " + numberParameters + " \n" );
		resultPanelTextArea.append("\n\tChi-Square Goodness of Fit Results:\n" );
		
		String newln = System.getProperty("line.separator");
		String chiSquareOutput = new String("\n\n\t********** Chi-Square Statistic is: "+
				dFormat.format(chiSquareStat) + " *********\n");
		chiSquareOutput += "\n\n\t********** Chi-Square Degrees of Freedom is: "+
				xLength + " - " + numberParameters + " - 1 = " + df + " *********\n";
		chiSquareOutput += "\n\n\t********** Chi-Square p-value is: "+
		dFormat.format(pValue) + " *********\n";

		resultPanelTextArea.append(chiSquareOutput);

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
        return new String("http://en.wikipedia.org/wiki/Chi-square_statistic");
    }


}

