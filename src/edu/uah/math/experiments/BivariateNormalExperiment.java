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
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.BivariateScatterPlot;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;

/**
* This experiment generates a point (X, Y) from a bivariate normal distribution with
* mean (0, 0). The point is shown graphically as a red dot in the scatterplot, and the
* coordinates are recorded on each update. The distribution regression line is shown in
* blue in the scatterplot, and the sample regression line is shown in red. The density
* function and moments of X and of Y are shown in the distribution graphs, and the moments
* are given in the distribution tables. The last table gives the distribution and sample
* correlation. The distribution standard deviations and correlations can be varied with
* scroll bars.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BivariateNormalExperiment extends Experiment implements Serializable{
	double muX=0.0, sigmaX = 1; 	// N(muX, sigmaX^2)
	double muY=0.0, sigmaY = 1;	// N(muY, sigmaY^2)
	double sum, distCor=0.0;
	private NormalDistribution xDist = new NormalDistribution(muX, sigmaX);
	private NormalDistribution yDist = new NormalDistribution(muY, sigmaY);
	private NormalDistribution xPlusyDist = new NormalDistribution(muX+muY, 
			Math.sqrt(sigmaX*sigmaX+sigmaY*sigmaY));
	RecordTable recordTable = new RecordTable(new String[] {"Run", "X", "Y"});
	RandomVariable x = new RandomVariable(xDist, "X");
	RandomVariable y = new RandomVariable(yDist, "Y");
	RandomVariable xPlusy = new RandomVariable(xPlusyDist, "X+Y");
	RandomVariableGraph xGraph = new RandomVariableGraph(x);
	RandomVariableGraph yGraph = new RandomVariableGraph(y);
	RandomVariableTable xTable = new RandomVariableTable(x);
	RandomVariableTable yTable = new RandomVariableTable(y);

	BivariateScatterPlot xyScatter = new BivariateScatterPlot(
		new Domain(muX-3.5*sigmaX, muX+3.5*sigmaX, 1, Domain.CONTINUOUS),
		new Domain(muY-3.5*sigmaY, muY+3.5*sigmaY, 1, Domain.CONTINUOUS));

	StatisticsTable xyTable = new StatisticsTable("(X, Y)", new String[]{
			"Var(X+Y)=Var(X)+Var(Y)+2Cov(X,Y)", 
			"Var(X)", "Var(Y)", "Cov(X,Y)", "2Cov(X,Y)=2Cor(X,Y)SD(X)SD(Y)", 
			"Cor(X,Y)", "Slope", "Intercept"});
	JToolBar toolBar = new JToolBar("Parameter Toolbar");
	Parameter muXScroll = new Parameter(-20, 20, 0.1, 0, "Mean of X", "\u03BCx");
	Parameter sigmaXScroll = new Parameter(0.5, 20, 0.1, 1, "Standard deviation of X", "\u03c3x");
	Parameter muYScroll = new Parameter(-20, 20, 0.1, 0, "Mean of Y", "\u03BCy");
	Parameter sigmaYScroll = new Parameter(0.5, 20, 0.1, 1, "Standard deviation of Y", "\u03c3y");
	Parameter rhoScroll = new Parameter(-1, 1, 0.05, 0, "Correlation", "\u03c1");

	/**
	* This method initializes the experiment, including the scatterplot, parameter scrollbars
	* labels, random variable graphs, and the correlation table.
	*/
	public void init(){
		super.init();
		setName("Bivariate Normal Experiment");
		//Sliders
		muXScroll.applyDecimalPattern("0.00");
		muXScroll.getSlider().addChangeListener(this);
		sigmaXScroll.applyDecimalPattern("0.0");
		sigmaXScroll.getSlider().addChangeListener(this);
		muYScroll.applyDecimalPattern("0.00");
		muYScroll.getSlider().addChangeListener(this);
		sigmaYScroll.applyDecimalPattern("0.0");
		sigmaYScroll.getSlider().addChangeListener(this);
		rhoScroll.applyDecimalPattern("0.00");
		rhoScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(muXScroll);
		toolBar.add(sigmaXScroll);
		toolBar.add(muYScroll);
		toolBar.add(sigmaYScroll);
		toolBar.add(rhoScroll);
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
		yGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(yGraph, 2, 0, 1, 1);
		//Record table
		recordTable.setDescription("(X, Y): sample point");
		addComponent(recordTable, 0, 1, 1, 2);
		//X Table
		addComponent(xTable, 1, 1, 1, 1);
		//Y Table
		addComponent(yTable, 2, 1, 1, 1);
		//(X, Y) Table
		xyTable.setDescription("Variance of the sum, Covariance, correlation, regression slope and intercept of (X, Y)");
		xyTable.setDistributionValues(new double[]{xPlusyDist.getVariance(), xDist.getVariance(), 
				yDist.getVariance(), 0, 0, distCor, 0, 0});
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
			+ "The experiment generates a point (X, Y) from a bivariate normal distribution with\n"
			+ "mean (0, 0). The point is shown graphically as a red dot in the scatterplot, and the\n"
			+ "coordinates are recorded on each update. The distribution regression line is shown in\n"
			+ "blue in the scatterplot, and the sample regression line is shown in red. The density\n"
			+ "function and moments of X and of Y are shown in the distribution graphs, and the moments\n"
			+ "are given in the distribution tables. The last table gives the distribution and sample\n"
			+ "correlation. The distribution standard deviations and correlations can be varied withn"
			+ "scroll bars.";
	}

	/**
	* This method defines the experiment.  A point from the bivariate normal distribution is
	* simulated and passed to the scatterplot.
	*/
	public void doExperiment(){
		double r, theta, u, v;
		double xV, yV;
		
		super.doExperiment();
		//Simulate a point from the bivariate normal distribution*/
		r = Math.sqrt(-2 * Math.log(Math.random()));
		theta = 2 * Math.PI * Math.random();
		u = r * Math.cos(theta);
		r = Math.sqrt(-2 * Math.log(Math.random()));
		theta = 2 * Math.PI * Math.random();
		v = r * Math.cos(theta);
		
		xV = sigmaX * u + muX;
		yV = sigmaY * distCor * u + sigmaY * Math.sqrt(1 - distCor * distCor) * v + muY;
		x.setValue(xV);
		y.setValue(yV);
		sum = sum + x.getValue() * y.getValue();
		xPlusy.setValue(xV+yV);
		//Add the point
		xyScatter.addPoint(x.getValue(), y.getValue());
	}

	/**
	* This method updates the experiment, including the random variable graphs, correlation
	* table, and record table.
	*/
	public void update(){
		super.update();
		int runs = getTime();
		//Compute the sample correlation and covariance*/
		double sampleCov = (sum -  runs * x.getIntervalData().getMean() * y.getIntervalData().getMean()) / (runs - 1);
		double sampleCor = sampleCov / (x.getIntervalData().getSD() * y.getIntervalData().getSD());
		double sampleSlope = sampleCov / x.getIntervalData().getVariance();
		double sampleIntercept = y.getIntervalData().getMean() - sampleSlope * x.getIntervalData().getMean();
		
		double xPlusyVariance = xPlusy.getIntervalData().getIntervalVariance();
		double xVariance = x.getIntervalData().getIntervalVariance();
		double yVariance = y.getIntervalData().getIntervalVariance();
		double xVarPlusyVarPlusCovarXY = xVariance+yVariance+2*sampleCov;
		
		recordTable.addRecord(new double[]{getTime(), x.getValue(), y.getValue()});
		//"Var(X+Y)=Var(X)+Var(Y)+2Cov(X,Y)", 
		//"Var(X)", "Var(Y)", "Cov(X,Y)", "2Cov(X,Y)=2Cor(X,Y)SD(X)SD(Y)", 
		//"Cor(X,Y)", "Slope", "Intercept"
		xyTable.setDataValues(new double[]{xVarPlusyVarPlusCovarXY, //xPlusyVariance
				xVariance, yVariance, sampleCov, 
				2*sampleCov, sampleCor, sampleSlope, sampleIntercept});
		//Update tables and graphs
		xyScatter.setStatistics(sampleSlope, sampleIntercept);
		xyScatter.repaint();
		xGraph.repaint();
		yGraph.repaint();
		xTable.repaint();
		yTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		
		xGraph.setShowModelDistribution(showModelDistribution);
		yGraph.setShowModelDistribution(showModelDistribution);
		xyScatter.setShowModelDistribution(showModelDistribution);
		xyScatter.repaint();
		xGraph.repaint();
		yGraph.repaint();
		xTable.setShowModelDistribution(showModelDistribution);
		xyTable.setShowModelDistribution(showModelDistribution);
		yTable.setShowModelDistribution(showModelDistribution);
		xyTable.repaint();
		xTable.repaint();
		yTable.repaint();
		
	}
	/**
	* This method resets the experiment, including the random variable graphs and tables,
	* the correlation table, scatterplot and record table.
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
	* This method handles the scrollbar events that occur when parameters are changed.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		double distCov, distSlope, intercept;
		if (e.getSource() == muXScroll.getSlider() || e.getSource() == sigmaXScroll.getSlider()){
			muX = muXScroll.getValue();
			sigmaX = sigmaXScroll.getValue();
			xDist.setParameters(muX, sigmaX);
		}
		else if (e.getSource() == muYScroll.getSlider() || e.getSource() == sigmaYScroll.getSlider()){
			muY = muYScroll.getValue();
			sigmaY = sigmaYScroll.getValue();
			yDist.setParameters(muY, sigmaY);
		}
		else if (e.getSource() == rhoScroll.getSlider()) {
			distCor = rhoScroll.getValue();
		}
		
		xPlusyDist.setParameters(muX+muY, Math.sqrt(sigmaX*sigmaX+sigmaY*sigmaY+2*distCor*sigmaX*sigmaY));
		
		distCov = distCor * sigmaX * sigmaY;
		distSlope = distCor * sigmaY / sigmaX;
		intercept = muY-distSlope*muX;
	
		xyScatter.setDomains(new Domain(muX-3.5*sigmaX, muX+3.5*sigmaX, 1, Domain.CONTINUOUS),
		new Domain(muY-3.5*sigmaY, muY+3.5*sigmaY, 1, Domain.CONTINUOUS));

		xyScatter.setParameters(distSlope, muY - distSlope * muX);

		//"Var(X+Y)=Var(X)+Var(Y)+2Cov(X,Y)", 
		//"Var(X)", "Var(Y)", "Cov(X,Y)", "2Cov(X,Y)=2Cor(X,Y)SD(X)SD(Y)", 
		//"Cor(X,Y)", "Slope", "Intercept"
		xyTable.setDistributionValues(new double[]{xPlusyDist.getVariance(), xDist.getVariance(), 
				yDist.getVariance(), distCov, 2*distCov, distCor, distSlope, intercept});
		reset();
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}


