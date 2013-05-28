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
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.UserScatterPlot;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;

/**
* This class models a simple interactive scatterplot.  The user
* clicks on the scatterplot to generate points. The regression line
* and summary statistics are shown.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BivariateGame extends Game implements Serializable{
	private int runs = 0;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Point", "X", "Y"});
	private UserScatterPlot xyScatter = new UserScatterPlot(new Domain(-6, 6, 1, Domain.CONTINUOUS));
	private StatisticsTable xTable = new StatisticsTable("X", new String[]{"Mean", "SD"});
	private StatisticsTable yTable = new StatisticsTable("Y", new String[]{"Mean", "SD"});
	private StatisticsTable xyTable = new StatisticsTable("(X, Y)", new String[]{"Cov", "Cor", "Slope", "Intercept"});


	/**
	* This method initializes the applet, including the toolbar,
	* scatterplot, and statistics table.
	*/
	public void init(){
		super.init();
		setName("Interactive Scatterplot");
		//ScatterPlot
		xyScatter.setMinimumSize(new Dimension(200, 200));
        //xyScatter.setPreferredSize(new Dimension(200, 200));
		xyScatter.addMouseListener(this);
		addComponent(xyScatter, 0, 0, 2, 1);
		//X Table
		xTable.setDescription("Mean and standard deviation of X");
		xTable.setShow(StatisticsTable.DATA);
		addComponent(xTable, 0, 1, 1, 1);
		//Y Table
		yTable.setDescription("Mean and standard deviation of Y");
		yTable.setShow(StatisticsTable.DATA);
		addComponent(yTable, 1, 1, 1, 1);
		//Record Table
		recordTable.setDescription("(X, Y): sample point");
		addComponent(recordTable, 0, 2, 1, 1);
		//(X, Y) Table
		xyTable.setDescription("Covariance, correlation, regression slope and intercept of (X, Y)");
		xyTable.setShow(StatisticsTable.DATA);
		addComponent(xyTable, 1, 2, 1, 1);
		//Final Actions
		validate();
		reset();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "Click in the graph to generate bivariate data. The selected points are shown as red dots\n"
			+ "in the scatterplot, and the coordinates are recorded in the first table. The second table\n"
			+ "gives the mean and standard deviation of the x and y coordinates, and the correlation.\n"
			+ "In the graph, the horizontal cross hair is centered at the mean of the x-data and extends\n"
			+ "one standard deviation on either side. Similarly, the vertical cross hair is centered at the\n"
			+ "mean of the y-data and extends one standard deviation on either side. Finally, the least\n"
			+ "squares regression line is shown in the scatterplot.";
	}

	/**
	* This method handles the mouse event generated when a user clicks on
	* the scatterplot. The point is added to the data set, the statistics are recomputed,
	* and the graph, record table, and statistics table are updated.
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		if (e.getSource() == xyScatter){
			runs++;
			IntervalData xData = xyScatter.getXData(), yData = xyScatter.getYData();
			recordTable.addRecord(new double[]{runs, xData.getValue(), yData.getValue()});
			xTable.setDataValues(new double[]{xData.getMean(), xData.getSD()});
			yTable.setDataValues(new double[]{yData.getMean(), yData.getSD()});
			xyTable.setDataValues(new double[]{xyScatter.getCovariance(), xyScatter.getCorrelation(),
				xyScatter.getSlope(), xyScatter.getIntercept()});
		}
		else super.mouseClicked(e);
	}

	/**
	* This method resets the experiment, including the scatterplot,
	* the record table, and the statistics table.
	*/
	public void reset(){
		super.reset();
		runs = 0;
		recordTable.reset();
		xyScatter.reset();
		xTable.reset();
		yTable.reset();
		xyTable.reset();
	}
}

