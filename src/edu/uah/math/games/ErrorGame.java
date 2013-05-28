/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.games;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.InteractiveHistogram;
import edu.uah.math.devices.ErrorGraph;
import edu.uah.math.devices.DataTable;
import edu.uah.math.distributions.Domain;

/**
* This class is an applet for exploring the measures of center and dispersion in
* terms of error functions. These error functions are the mean square error and
* mean absolute error.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ErrorGame extends Game implements Serializable{
	//Variables
	private RecordTable recordTable = new RecordTable(new String[]{"Point", "X"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private InteractiveHistogram histogram = new InteractiveHistogram(new Domain(0, 5, 0.1, Domain.CONTINUOUS));
	private ErrorGraph errorGraph = new ErrorGraph(histogram.getIntervalData(), 0);
	private DataTable dataTable = new DataTable(histogram.getIntervalData());
	private Parameter nSlider = new Parameter(1, 50, 1, 50, "Number of classes", "n");
	private JLabel widthLabel = new JLabel("w = 0.100");
	private JComboBox freqChoice = new JComboBox();
	private JComboBox errorChoice = new JComboBox();
	private DecimalFormat decimalFormat = new DecimalFormat("0.000");

	/**
	* This method initializes the experiment, including the label for the
	* class width, the slider for the number of classes, the choice for the type
	* of histogram, the choice for the error function, the toolbar, the histogram,
	* and the record table.
	*/
	public void init(){
		super.init();
		setName("Interactive Histogram with Error Graph");
		//Width label
		widthLabel.setToolTipText("Class width");
		//Slider
		nSlider.getSlider().addChangeListener(this);
		//Frequency choice
		freqChoice.addItemListener(this);
		freqChoice.setToolTipText("Graph Type");
		freqChoice.addItem("Freq");
		freqChoice.addItem("Rel Freq");
		//Error Choice
		errorChoice.addItemListener(this);
		errorChoice.setToolTipText("Error Type");
		errorChoice.addItem("Mean Square");
		errorChoice.addItem("Mean Absolute");
		//Toolbar
		addTool(freqChoice);
		addTool(errorChoice);
		addTool(nSlider);
		addTool(widthLabel);
		//Histogram
		histogram.addMouseListener(this);
		addComponent(histogram, 0, 0, 1, 1);
		//Error Graph
		addComponent(errorGraph, 1, 0, 1, 1);
		//Record Table
		recordTable.setDescription("X: sample point");
		addComponent(recordTable, 0, 1, 1, 1);
		//Data Table
		addComponent(dataTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "Click on the horizontal axis in the first graph to select points for a frequency distribution.\n"
			+ "The frequency distribution is given in the first table, and the corresponding histogram is\n"
			+ "shown in the first graph. The class width (and hence the number of classes) can be varied with\n"
			+ "the scroll bar. The vertical axis can be selected as either frequency or relative frequency\n"
			+ "from the list box.  The second graph shows the error function, which can be selected as either\n"
			+ "mean square error or mean absolute error. In the first case, the minimum occurs at the mean\n"
			+ "and the minimum value is the variance. In the second case, the minimum occurs throughout the\n"
			+ "median interval and the minimum value is the mean absolute deviation from the median.";
	}

	/**
	* This method handles the mouse events generated when the user clicks on the interactive
	* histogram. The record table and error graph are updated.
	*/
	public void mouseClicked(MouseEvent event){
		if (event.getSource() == histogram){
			dataTable.repaint();
			recordTable.addRecord(new double[]{histogram.getIntervalData().getSize(), histogram.getValue()});
			errorGraph.repaint();
		}
		else super.mouseClicked(event);
	}

	/**
	* This method resets the experiment, including the record table, the histogram,
	* the data table, and the error graph.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		histogram.reset();
		dataTable.reset();
		errorGraph.repaint();
	}

	/**
	* This method handles the choice events associated with the changing the type of the
	* frequency distribution, the type of error graph, and the type of summary statistics.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == freqChoice){
			histogram.setHistogramType(freqChoice.getSelectedIndex());
			dataTable.setDistributionType(freqChoice.getSelectedIndex());
		}
		else if (e.getSource() == errorChoice){
			if (errorChoice.getSelectedIndex() == 0){
			histogram.setStatisticsType(Histogram.MSD);
			dataTable.setStatisticsType(DataTable.MSD);
			errorGraph.setErrorType(0);
			}
			else{
				histogram.setStatisticsType(Histogram.MAD);
				dataTable.setStatisticsType(DataTable.MAD);
				errorGraph.setErrorType(1);
			}
			dataTable.repaint();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changing the width of
	* the domain in the iterval dataset.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nSlider.getSlider()){
			int n = (int)nSlider.getValue();
			double w = 5.0 / n;
			histogram.setWidth(w);
			widthLabel.setText("w = " + decimalFormat.format(w));
			dataTable.reset();
		}
		else super.stateChanged(e);
	}
}

