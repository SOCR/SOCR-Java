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
import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import edu.uah.math.distributions.DieDistribution;
import edu.uah.math.distributions.ChiSquareDistribution;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.DiceDistributionGraph;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.DataTable;
import edu.uah.math.devices.DieProbabilityDialog;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.StatisticsTable;


/**
* This class models a chi-square Goodness of fit experiment in terms of dice. The true
* distribution and test distribution can be specified.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ChiSquareFitExperiment extends Experiment implements Serializable{
	//Variables
	private int sampleSize = 10;
	private double chi, criticalValue = 9.236;
	//Arrays
	private double[] observed = new double[6], expected = new double[6];;
	//Awt objects
	private Frame frame;
	//Swing objects
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JButton die0Button = new JButton();
	private JButton die1Button = new JButton();
	private JLabel criticalLabel = new JLabel("v = 15.098");
	//Distribution objects
	private DieDistribution samplingDist = new DieDistribution(DieDistribution.FAIR);
	private DieDistribution testDist = new DieDistribution(DieDistribution.FLAT16);
	private ChiSquareDistribution testDistribution = new ChiSquareDistribution(5);
	private RandomVariable dieVariable = new RandomVariable(samplingDist, "X");
	private IntervalData reject = new IntervalData(new Domain(0, 1, 1, Domain.DISCRETE), "I");
	//Device objects
	private RecordTable recordTable = new RecordTable(new String[] {"Run", "V", "I"});
	private Parameter sampleScroll = new Parameter(5, 100, 1, sampleSize, "Sample size", "n");
	private Parameter levelScroll = new Parameter(0.01, 0.99, 0.01, 0.01, "Significance level", "\u03b1");
	private DiceDistributionGraph diceGraph = new DiceDistributionGraph(dieVariable, testDist);
	private Histogram rejectGraph = new Histogram(reject);
	private CriticalGraph criticalGraph = new CriticalGraph(testDistribution);
	private DataTable rejectTable = new DataTable(reject);
	private DieProbabilityDialog dieProbabilityDialog;
	private StatisticsTable sampleTable = new StatisticsTable("Value", new String[]{"1", "2", "3", "4", "5", "6"});

	/**
	* This method initializes the experiment. This includes the label for displaying
	* the critical values, the buttons for showing the die probability dialogs for
	* the true and test distributions, the choice box for the significance level, the
	* slider for the sample size, the toolbar, the critical graph, the reject graph,
	* the sample table, and the reject table,
	*/
	public void init(){
		super.init();
		setName("Chi Square Dice Experiment");
		//Critical Label
		criticalLabel.setToolTipText("Critical value of V");
		//die buttons
		die0Button.addActionListener(this);
		die0Button.setToolTipText("Sampling distribution");
		die0Button.setIcon(new ImageIcon(getClass().getResource("blueDie.gif")));
		die1Button.addActionListener(this);
		die1Button.setToolTipText("Test distribution");
		die1Button.setIcon(new ImageIcon(getClass().getResource("greenDie.gif")));
		//Sample size slider
		sampleScroll.getSlider().addChangeListener(this);
		levelScroll.getSlider().addChangeListener(this);
		levelScroll.applyDecimalPattern("0.00");
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(die0Button);
		toolBar.add(die1Button);
		toolBar.add(levelScroll);
		toolBar.add(criticalLabel);
		toolBar.add(sampleScroll);
		addToolBar(toolBar);
		//Dice Graph
		diceGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(diceGraph, 0, 0, 1, 1);
		//Critial Graph
		criticalGraph.setCriticalValues(0, 9.236);
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		//Reject Graph
		rejectGraph.setStatisticsType(0);
		rejectGraph.setMinimumSize(new Dimension(50, 100));
		addComponent(rejectGraph, 2, 0, 1, 1);
		//Record Table
		recordTable.setDescription("V: chi-square statistic, I: reject null hypothesis");
		addComponent(recordTable, 0, 1, 1, 1);
		//Sample Table
		sampleTable.setDescription("Expected and observed frequencies");
		sampleTable.setDistributionName("Expected");
		sampleTable.setDataName("Observed");
		computeExpectedFrequencies();
		addComponent(sampleTable, 1, 1, 1, 1);
		//Reject Table,
		rejectTable.setStatisticsType(0);
		addComponent(rejectTable, 2, 1, 1, 1);
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
			+ "The experiment is to select a random sample from a specified distribution and perform the\n"
			+ "chi-square goodness of fit test to another specified distribution, at a specified level of\n"
			+ "significance. The distributions are discrete distributions on {1, 2, 3, 4, 5, 6}, and thus\n"
			+ "the experiment corresponds to rolling n dice, each governed by the same distribution.  The\n"
			+ "sampling and test distributions can be specified by clicking on the die buttons (these\n"
			+ "bring up the die probability dialog box.  The density of the true distribution is shown in\n"
			+ "blue in the first graph; the density of the test distribution is shown in green. The\n"
			+ "significance level can be selected from a list box and the sample size n can be varied with\n"
			+ "a scroll bar.  The test statistic V has (approximately) the chi-square distribution with 5\n"
			+ "degrees of freedom. The density of V and the critical values are shown in blue in the second\n"
			+ "graph. On each update, the sample values are recorded in the first table and the sample\n"
			+ "density is shown in red in the first graph. The value of the test statistic V is shown in\n"
			+ "red in the second graph. Note that the null hypothesis is rejected if and only if V falls\n"
			+ "outside of the critical values. Variable I indicates the event that the null hypothesis is\n"
			+ "rejected. The empirical density of I, as the experiment runs, is shown in red in the last\n"
			+ "graph and is given in the last table. The values of V and I are recorded in the middle table\n"
			+ "on each update.";
	}

	/**
	* This method handles the button events associated with buttons that show the die
	* probability dialog box. The distribution (true or test) is passed to the dialog
	* box. If the user clicks OK, the new distribution is passed to the appropriate
	* random variable.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == die0Button){
			frame = new Frame();
			dieProbabilityDialog = new DieProbabilityDialog(frame);
			Point fp = new Point (100, 100);
			Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
			Point dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			dieProbabilityDialog.setLocation(dp);
			dieProbabilityDialog.setProbabilities(samplingDist.getProbabilities());
			dieProbabilityDialog.setTitle("Sampling distribution");
			dieProbabilityDialog.setVisible(true);
			if (dieProbabilityDialog.isOK()){
				samplingDist.setProbabilities(dieProbabilityDialog.getProbabilities());
				dieVariable.setDistribution(samplingDist);
				diceGraph.setParameters(dieVariable, testDist);
				reset();
			}
		}
		else if (e.getSource() == die1Button){
			frame = new Frame();
			dieProbabilityDialog = new DieProbabilityDialog(frame);
			Point fp = new Point(100,100);
			Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
			Point dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
			dieProbabilityDialog.setLocation(dp);
			dieProbabilityDialog.setProbabilities(testDist.getProbabilities());
			dieProbabilityDialog.setTitle("Test distribution");
			dieProbabilityDialog.setVisible(true);
			if (dieProbabilityDialog.isOK()){
				testDist.setProbabilities(dieProbabilityDialog.getProbabilities());
				diceGraph.setParameters(dieVariable, testDist);
				computeExpectedFrequencies();
				reset();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method handles events associated with the slider that controls the
	* sample size.and the slider that controls the significance level.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		//Sample size
		if(e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			computeExpectedFrequencies();
			reset();
		}
		else if (e.getSource() == levelScroll.getSlider()){
			criticalValue = testDistribution.getQuantile(1 - levelScroll.getValue());
			criticalLabel.setText("v = " + format(criticalValue));
			criticalGraph.setCriticalValues(0, criticalValue);
			reset();
		}
	}

	/**
	* This event defines the experiment. The sample is simulated and the chi-square
	* statistic computed. The test is performed and the reject variable updated.
	*/
	public void doExperiment(){
		super.doExperiment();
		dieVariable.reset();
		for (int i = 0; i < sampleSize; i++) dieVariable.sample();
		chi = 0;
		for (int i = 0; i < 6; i++){
			observed[i] = dieVariable.getIntervalData().getFreq(i + 1);
			chi = chi + ((observed[i] - expected[i]) * (observed[i] - expected[i])) / expected[i];
		}
		criticalGraph.setValue(chi);
		if(criticalValue < chi) reject.setValue(1);
		else reject.setValue(0);
	}

	/**
	* This method runs the experiment one time and plays a sound depending on
	* the outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playNote((int)reject.getValue());
	}

	/**
	* This method updates the display, includng the record table, the dice graph,
	* the critical graph, the sample table, and the reject table.
	*/
	public void update(){
		super.update();
		//Graphs
		recordTable.addRecord(new double[]{getTime(), chi, reject.getValue()});
		diceGraph.repaint();
		criticalGraph.setValueDrawn(true);
		//Tables
		sampleTable.setDataValues(observed);
		rejectGraph.repaint();
		rejectTable.repaint();
	}
	public void graphUpdate(){
		super.update();
		//Graphs
		diceGraph.setShowModelDistribution(showModelDistribution);
		criticalGraph.setShowModelDistribution(showModelDistribution);
		rejectGraph.setShowModelDistribution(showModelDistribution);
		
		diceGraph.repaint();
		criticalGraph.repaint();
		//Tables
		rejectGraph.repaint();
		sampleTable.setShowModelDistribution(showModelDistribution);
		sampleTable.repaint();
	}

	/**
	* This method resets the experiment, including the record table, the die variable,
	* the test statistic, the reject random variable, the dice graph, the critical
	* graph, the reject graph, the reject table, and the sample table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		dieVariable.reset();
		reject.reset();
		diceGraph.repaint();
		criticalGraph.setValueDrawn(false);
		rejectGraph.repaint();
		rejectTable.repaint();
		sampleTable.reset();
	}

	/**
	* This method computes the expected frequencies.
	*/
	public void computeExpectedFrequencies(){
		for (int i = 0; i < 6; i++) expected[i] = sampleSize * testDist.getProbabilities(i);
		sampleTable.setDistributionValues(expected);
	}
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

