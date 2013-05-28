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
package edu.uah.math.experiments;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JToolBar;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.TriangleDistribution;
import edu.uah.math.distributions.CircleDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.BivariateScatterPlot;
import edu.uah.math.devices.StatisticsTable;


/**
* This class models a bivariate uniform experiment.  Three regions can be selected:
* a square region, a circular region, and a triangular region.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BivariateUniformExperiment extends Experiment implements Serializable{
	//Variables
	private int distType = 0;
	private double rho = 0, sum;
	
	// To Do: This class needs to be extended to allow for specification of the bivariate correlation
	// If X and Y are bivariate standard normal with correlation rho
	// then U and V will have correlation (6/pi)*arcsin(rho/2).
	// X and Y are generated as bivariate normal with mean 0 and covariance
	// matrix ((1 rho) (rho 1)), then U = Phi(X), V = Phi(Y) would be
	// correlated uniforms (where Phi is the standard normal CDF).  
	
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "Y"});
	private RandomVariable x = new RandomVariable(new ContinuousUniformDistribution(-6, 6), "X");
	private RandomVariable y = new RandomVariable(new ContinuousUniformDistribution(-6, 6), "Y");
	private RandomVariableGraph xGraph = new RandomVariableGraph(x);
	private RandomVariableGraph yGraph = new RandomVariableGraph(y);
	private RandomVariableTable xTable = new RandomVariableTable(x);
	private RandomVariableTable yTable = new RandomVariableTable(y);
	private BivariateScatterPlot xyScatter = new BivariateScatterPlot(new Domain(-6, 6, 1, Domain.CONTINUOUS));
	private StatisticsTable xyTable = new StatisticsTable("(X, Y)", new String[]{"Cov", "Cor", "Slope", "Intercept"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox distChoice = new JComboBox();

	/**
	* This method initializes the experiment, including the distribution choice, toolbar,
	* scatterplot, random variable tables and graphs.
	*/
	public void init(){
		super.init();
		setName("Bivariate Uniform Experiment");
		//Distribution choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Region");
		distChoice.addItem("Square");
		distChoice.addItem("Triangle");
		distChoice.addItem("Circle");
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(distChoice);
		addToolBar(toolBar);
		//Scatterplot
		xyScatter.setParameters(0, 0);
		xyScatter.setToolTipText("(X, Y) scatterplot");
		xyScatter.setMinimumSize(new Dimension(100, 100));
		addComponent(xyScatter, 0, 0, 1, 1);
		//xGraph
		xGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(xGraph, 1, 0, 1, 1);
		//yGraph
		yGraph.setMinimumSize(new Dimension(100, 100));;
		addComponent(yGraph, 2, 0, 1, 1);
		//Record table
		recordTable.setDescription("(X, Y): sample point");
		addComponent(recordTable, 0, 1, 1, 2);
		//X Table
		addComponent(xTable, 1, 1, 1, 1);
		//Y Table
		addComponent(yTable, 2, 1, 1, 1);
		//xy Table
		xyTable.setDescription("Covariance, correlation, regression slope and intercept of (X, Y)");
		addComponent(xyTable, 1, 2, 2, 1);
		//Final actions
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
			+ "The experiment generates a point (X, Y) from a uniform distribution on a region of the\n"
			+ "plane. Any of the following regions can be selected with the list box: the square \n"
			+ " -6 < x < 6, -6 < y <6; the triangle -6 < y < x < 6; and t he circle x^2 + y^2 < 36.\n"
			+ "The random point is shown has a red dot in the scatterplot, and the coordinates are\n"
			+ "recorded on each update. The scatterplot also shows the distribution regression line in\n"
			+ "blue and the sample regression line in red. The distribution and sample density and\n"
			+ "moments for X and for Y are shown in the second and third graphs. The distribution\n"
			+ "and sample moments are given in the second and third tables. The distribution and sample\n"
			+ "correlation are given in the last table.";
	}

	/**
	* This method defines the experiment. A point from the specified uniform distribution
	* is simulated and passed to the scatterplot.
	*/
	public void doExperiment(){
		double r, theta, u, v;
		super.doExperiment();
		switch(distType){
		case 0:
			x.setValue(12 * Math.random() - 6);
			y.setValue(12 * Math.random() - 6);
			break;
		case 1:
			u = 12 * Math.random() - 6;
			v = 12 * Math.random() - 6;
			x.setValue(Math.max(u, v));
			y.setValue(Math.min(u, v));
			break;
		case 2:
			u = 6 * Math.random();
			v = 6 * Math.random();
			r = Math.max(u, v);
			theta = 2 * Math.PI * Math.random();
			x.setValue(r * Math.cos(theta));
			y.setValue(r * Math.sin(theta));
			break;
		}
		sum = sum + x.getValue() * y.getValue();
		xyScatter.addPoint(x.getValue(), y.getValue());
	}

	/**
	* This method updates the experiment, including the record table, random variable
	* graphs and tables, correlation table.
	*/
	public void update(){
		super.update();
		int runs = getTime();
		double sampleCov = (sum -  runs * x.getIntervalData().getMean() * y.getIntervalData().getMean()) / (runs - 1);
		double sampleCor = sampleCov / (x.getIntervalData().getSD() * y.getIntervalData().getSD());
		double sampleSlope = sampleCov / x.getIntervalData().getVariance();
		double sampleIntercept = y.getIntervalData().getMean() - sampleSlope * x.getIntervalData().getMean();
		recordTable.addRecord(new double[]{getTime(), x.getValue(), y.getValue()});
		xGraph.repaint();
		yGraph.repaint();
		xTable.repaint();
		yTable.repaint();
		xyTable.setDataValues(new double[]{sampleCov, sampleCor, sampleSlope, sampleIntercept});
		if (runs > 1) xyScatter.setStatistics(sampleSlope, sampleIntercept);
		xyScatter.repaint();
	}
	public void graphUpdate(){
		super.update();
		xGraph.setShowModelDistribution(showModelDistribution);
		yGraph.setShowModelDistribution(showModelDistribution);
		xyScatter.setShowModelDistribution(showModelDistribution);		
		xGraph.repaint();
		yGraph.repaint();
		xyScatter.repaint();
		
		xTable.setShowModelDistribution(showModelDistribution);
		xyTable.setShowModelDistribution(showModelDistribution);
		yTable.setShowModelDistribution(showModelDistribution);
		xyTable.repaint();
		xTable.repaint();
		yTable.repaint();
	}
	/**
	* This method resets the experiment, including the random variable graphs and tables,
	* the record table, the scatterplot, and the correlation table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		sum = 0;
		x.reset();
		y.reset();
		xyScatter.reset();
		xGraph.reset();
		yGraph.reset();
		xTable.reset();
		yTable.reset();
		xyTable.reset();
	}

	/**
	* This method handles the choice events that occur when the distribuiton is changed.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == distChoice){
			double distCov = 0, distCor = 0, distSlope, distIntercept;
			distType = distChoice.getSelectedIndex();
			switch(distType){
			case 0:
				x.setDistribution(new ContinuousUniformDistribution(-6, 6));
				y.setDistribution(new ContinuousUniformDistribution(-6, 6));
				distCov = 0; distCor = 0;
				break;
			case 1:
				x.setDistribution(new TriangleDistribution(-6, 6, TriangleDistribution.UP));
				y.setDistribution(new TriangleDistribution(-6, 6, TriangleDistribution.DOWN));
				distCov = 4; distCor = 0.5;
				break;
			case 2:
				x.setDistribution(new CircleDistribution(6));
				y.setDistribution(new CircleDistribution(6));
				distCov = 0; distCor = 0;
			}
			distSlope = distCor * y.getDistribution().getSD() / x.getDistribution().getSD();
			distIntercept = y.getDistribution().getMean() - distSlope * x.getDistribution().getMean();
			xyScatter.setParameters(distSlope, distIntercept);
			xyTable.setDistributionValues(new double[]{distCov, distCor, distSlope, distIntercept});
			reset();
		}
		else super.itemStateChanged(e);
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}


