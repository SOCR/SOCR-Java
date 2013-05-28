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
import java.awt.FlowLayout;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import edu.uah.math.distributions.Domain;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.InteractiveHistogram;
import edu.uah.math.devices.DataTable;
import edu.uah.math.devices.Parameter;

/**
* This class models an interactive historgram. The user can add points to an
* interval dataset by clicking on the x-axis of the histogram. The number of classes
* (and hence the width) of the interval dataset can be varied. The user can choose
* a histogram that diplays freqencies, relative frequencies, or densities, Several sets
* of summary statistics can be chosen for display.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class HistogramGame extends Game implements Serializable{
	private int index = 0;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Point", "X"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private InteractiveHistogram histogram = new InteractiveHistogram(new Domain(0, 5, 0.1, Domain.CONTINUOUS));
	private DataTable dataTable = new DataTable(histogram.getIntervalData());
	private Parameter nSlider = new Parameter(1, 50, 1, 50, "Number of classes", "n");
	private JLabel widthLabel = new JLabel("w = 0.100");
	private JComboBox freqChoice = new JComboBox();
	private JComboBox statsChoice = new JComboBox();
	private DecimalFormat decimalFormat = new DecimalFormat("0.000");

	/**
	* This method initialize the applet, including the slider for adjusting the number
	* of classes, the choice for the type of histogram, the choice of the type of
	* summary statistics to display, the toolbar, the histogram, and the record table.
	*/
	public void init(){
		super.init();
		setName("Interactive Histogram");
		//n slider
		nSlider.getSlider().addChangeListener(this);
		//Width label
		widthLabel.setToolTipText("Class width");
		//Frequency choice
		freqChoice.addItemListener(this);
		freqChoice.setToolTipText("Graph Type");
		freqChoice.addItem("Frequency");
		freqChoice.addItem("Relative Frequency");
		freqChoice.addItem("Density");
		freqChoice.setSelectedIndex(0);
		//Stats choice
		statsChoice.addItemListener(this);
		statsChoice.setToolTipText("Summary Statistics");
		statsChoice.addItem("None");
		statsChoice.addItem("Mean, SD");
		statsChoice.addItem("Interval Mean, SD");
		statsChoice.addItem("Boxplot");
		statsChoice.addItem("Median, MAD");
		statsChoice.setSelectedIndex(1);
		//Toolbars
		addTool(freqChoice);
		addTool(statsChoice);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(nSlider);
		toolBar.add(widthLabel);
		addToolBar(toolBar);
		//Histogram
		histogram.addMouseListener(this);
		histogram.setStatisticsType(2);
		addComponent(histogram, 0, 0, 2, 1);
		//Record Table
		recordTable.setDescription("X: sample point");
		addComponent(recordTable, 0, 1, 1, 1);
		//Data Table
		dataTable.setStatisticsType(2);
		addComponent(dataTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method handles the choice events associated with changes in the histogram
	* type and changes in the type of summary statistics to display.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		int i;
		if (e.getSource() == freqChoice){
			histogram.setHistogramType(freqChoice.getSelectedIndex());
			dataTable.setDistributionType(freqChoice.getSelectedIndex());
		}
		if (e.getSource() == statsChoice){
			histogram.setStatisticsType(statsChoice.getSelectedIndex());
			dataTable.setStatisticsType(statsChoice.getSelectedIndex());
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method resets the applet, including the record table, data table,
	* and histogram
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		histogram.reset();
		dataTable.reset();
	}

	/**
	* This method handles the scrollbar events that correspond to changing the width
	* of the histogram.
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
	}

	/**
	* This method handles the mouse clicks that correspond to the user adding points
	* to the data set.
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		if (e.getSource() == histogram){
			dataTable.repaint();
			if (histogram.getIntervalData().getSize()>1) 
				recordTable.addRecord(new double[]{histogram.getIntervalData().getSize(), 
						histogram.getValue()});
		}
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "Click near the horizontal (X) axis using the mouse. \n"
			+ "A new data point (tally) will be added to the data set.\n"
			+ "All data points are tallied and the resulting interactive histogram is visually \n"
			+ "displayed in the graph panel and numerically shown in the tables below.";
	}
	
	//The following mouse events are not handled.
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}

}

