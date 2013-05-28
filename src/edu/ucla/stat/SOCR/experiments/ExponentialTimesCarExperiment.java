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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.uah.math.experiments;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Timeline;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.NegativeBinomialDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the Times between Cars driving on a highway.
* The times between cars follows Exponential distribution with some rate-parameter lambda, Exp(lambda).
* The expected time between two cars is 1/lambda!
* 
* This Exp(lambda) distribution is a special case of Gamma( k , theta ) distribution.
* i.e., Gamma(k=1, theta = 1/lambda) ~ Exponential(lambda);
* http://en.wikipedia.org/wiki/Gamma_distribution#Specializations* 
* 
* Note that the "Number of Arrivals" in a fixed time period follows Poisson distribution.
* @author Ivo Dinov
* @version January 15, 2008
*/

public class ExponentialTimesCarExperiment extends Experiment implements Serializable{
	//Variables
	private int k = 1, trialCount=1, stepCount=0;
	private double currentTime=0, time, r=1, timeStep, totalTime=0;
	private double distributionDomainUpperValue;
	
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "Y"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter rScroll = new Parameter(0.01, 5, 0.01, r, "Exponential Rate Parameter", "r");
	private Parameter sampleScroll = new Parameter(1, 1000, 1, trialCount, "Number of Cars", "n");
		//Get the Exp(lambda=rate=r) ~~ Gamma(1, 1/r)
	private GammaDistribution trialsDist = new GammaDistribution(k, 1/r);

	private RandomVariable trials = new RandomVariable(trialsDist, "Y");
	private RandomVariableGraph trialsGraph = new RandomVariableGraph(trials);
	private RandomVariableTable trialsTable = new RandomVariableTable(trials);
	private Timeline timeline = new Timeline(trialsDist.getDomain());
	private Timer timer = new Timer(100, this);

	/**
	* This method initializes the experiment by setting up the toolbar, random
	* variable graph and table, timeline.
	*/
	public void init(){
		super.init();
		setName("Exponential Times Car Experiment");
		//Listeners
		//Sliders
		sampleScroll.getSlider().addChangeListener(this);
		toolBar.add(sampleScroll);

		rScroll.applyDecimalPattern("0.00");
		rScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rScroll);

		addToolBar(toolBar);
		addToolBar(timeline);
		//Graphs

		//Timeline - Top-Graph
		timeline.setMargins(35, 20, 20, 20);
		timeline.setMinimumSize(new Dimension(100, 40));
		timeline.setToolTipText("Between-Car Timeline");
		addComponent(timeline, 0, 0, 2, 1, 10, 2);

		//Trials Graph
		//trialsGraph.setDomain(new Domain(1, trialsDist.getDomain().getUpperValue(), 1, Domain.DISCRETE));
		addComponent(trialsGraph, 0, 1, 2, 1);

		//Record Table
		recordTable.setDescription("Y: Exponential times between cars");
		addComponent(recordTable, 0, 2, 1, 1);
		//Trials Table
		addComponent(trialsTable, 1, 2, 1, 1);
		timeStep = trialsDist.getDomain().getWidth();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return "SOCR Experiments - Exponetial Times Car Experiment\n\n"
		+ "http://socr.ucla.edu/htmls/SOCR_Experiments.html\n"
		+ "http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_ExpCarTimeExperiment\n\n"
		+ "This Experiment models the Times between random  Cars driving at a constant \n"
		+ "speed on a highway. The times between cars follows Exponential distribution \n"
		+ "with some rate-parameter (r), i.e. T ~ Exp(r). The expected time between two cars\n"
		+ "is 1/r!\n\n"
		+ "This Exp(r) distribution is a special case of Gamma(k, theta ) distribution.\n"
		+ "i.e., Gamma(k=1, theta = 1/r) ~ Exponential(r);\n"
		+ "http://en.wikipedia.org/wiki/Gamma_distribution#Specializations.\n\n"
		+ "Note that the Number of Arrivals in a fixed time-period follows Poisson distribution.\n";
	}

	/**
	* This method defines the experiment.  The Exponential trials are performed
	* "trialCount" times. The trial outcomes are passed to the timeline. 
	* All Exponential values are recorded and passed to the random variable.
	*/
	public void doExperiment(){
		super.doExperiment();
		performTrials();
		trials.setValue(trialCount);
	}

	/**
	* This method performs the Bernoulli trials and adds the random points to the timeline.
	*/
	public void performTrials(){
		timeline.resetData();
		for (int i = 0; i < trialCount; i++) {
			trials.sample();
			totalTime += trials.getValue();
			timeline.addTime(trials.getValue(), new Color(
					(int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random())));
		}
	}

	/**
	* This method starts the step timer and shows the experiment with additional annimation
	* and sound.
	*/
	public void step(){
		stop();
		performTrials();
		//currentTime = 1; Default
		currentTime = 0;
		timeline.setCurrentTime(currentTime);
		timer.start();
		stepCount++;
	}

	/**
	* This method stops the step timer, if necessary, and then calls the usual run method.
	*/
	public void run(){
		//timer.stop();
		//super.run();
	}

	/**
	* This method stops the step timer, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}


	/**
	* This method resets the experiment, including the timeline, record table,
	* random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		timeline.reset();
		trials.reset();
		trialsGraph.reset();
		trialsTable.reset();
		currentTime=0;
		totalTime = 0;
		stepCount=0;
		//trialsGraph.setDomain(new Domain(1, trialsDist.getDomain().getUpperValue(), 1, Domain.DISCRETE));
	}

	/**
	* This method updates the experiment, including the timeline, record table
	* random variable graph and table.
	*/
	public void update(){
		super.update();
		//timeline.setCurrentTime(trialCount);
		trialsGraph.repaint();
		trialsTable.repaint();
		
		/* double[] record = new double[2];
		record[0] = getTime();
		record[1] = timeline.getTime(getTime());
		if (record[0] >0) recordTable.addRecord(record);
		*/	
		
		/******
		 double[] record = new double[2];
		for (int i = 0; i<trialCount; i++) {
			record[0] = getTime();     //(stepCount-1)*trialCount +i+1;
			record[1] = timeline.getTime(getTime());
			recordTable.addRecord(record);
		}
		*******/
	}
	
	public void graphUpdate(){
		super.update();
		
		trialsGraph.setShowModelDistribution(showModelDistribution);
		trialsGraph.repaint();	
		trialsTable.setShowModelDistribution(showModelDistribution);
		trialsTable.repaint();	
	}

	/**
	* This method handles the scrollbar events that correspond to varying the
	* rate and the order of the arrival time.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == rScroll.getSlider()){
			r = rScroll.getValue();
			setDistribution();
			reset();
		}
		else if (e.getSource() == sampleScroll.getSlider()){
			trialCount = (int)sampleScroll.getValue();
			reset();
		}
	}

	/**
	* This method handles the events associated with the timer. The random points up to the
	* current time are drawn.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			/*if (currentTime + timeStep < time){
				currentTime = currentTime + timeStep;
				timeline.setCurrentTime(currentTime);
			}
			*/

			//if (currentTime < timeStep*(totalTime + trialsDist.getDomain().getUpperValue())){
			/******************************************/
			  	// This works, but does not animate the times/cars
			 	double max = 0;
				
				for (int i = 0; i<trialCount; i++) {
					//trials.sample();
					//timeline.addTime(trials.getValue(), new Color(
						//(int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random())));
					//currentTime++;
					//currentTime = currentTime + timeStep;
					currentTime = timeline.getTime(i);					
					if (i==0) max = currentTime;
					else if (max<currentTime) max=currentTime;
					timeline.setCurrentTime(currentTime);
					
					super.update();
					trialsGraph.repaint();
					trialsTable.repaint();
				
					double[] record = new double[2];
					record[0] = (stepCount-1)*trialCount +i+1;
					record[1] = timeline.getTime(i);
					recordTable.addRecord(record);
					//timer.setDelay(1000 / (int)distributionDomainUpperValue);
				}
				//update();
				timeline.setCurrentTime(max);
				timer.stop();
			/***************************************************************/
			
			/***************************
			//   Start of NEW TEST
				// 1. If currentTime < Total-Time for all cars - animate-draw the car positions! 
			//if (currentTime < (totalTime + trialsDist.getDomain().getUpperValue())/timeStep){
			if (currentTime < totalTime){
				 	double max = 0;
				
				 	if (currentTime<timeline.getTime(0)) {	// For initial-time dash-scroll
				 		currentTime += timeStep;
						timeline.setCurrentTime(currentTime);
				 	}
				 	else {	// For 2-nd, 3rd, ... car-time animations, just increment all previous ones	
				 		currentTime += timeStep;
				 		for (int i = 0; i<trialCount; i++) {
				 			//currentTime += timeline.getTime(i);
				 			if (i==0) timeline.setCurrentTime(currentTime);
				 			else currentTime += 
				 				timeline.getTime(0)-timeline.getCummilativeTimesAtIndex(i);
				 			timeline.setCurrentTime(currentTime);
						
				 			super.update();
							trialsGraph.repaint();
							trialsTable.repaint();
					
							double[] record = new double[2];
							record[0] = (stepCount-1)*trialCount +i+1;
							record[1] = timeline.getTime(i);
							recordTable.addRecord(record);
				 		}
				 		//timeline.setCurrentTime(max);
				 		//timer.stop();
				 	}
			} 
			else {	//2. If all cars passed by - stop animate-drawing
				timer.stop();
				super.doExperiment();
				trials.setValue(trialCount);
				update();
			}
			//   END of NEW TEST
			 * 
			 */
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the parameters of the distribution, when these have
	* been changed with the scrollbars.
	*/
	public void setDistribution(){
		trialsDist.setParameters(k, 1/r);
		distributionDomainUpperValue = trialsDist.getDomain().getUpperValue();
		timeline.setDomain(trialsDist.getDomain());
		// timeline.setDomain(new Domain(1, distributionDomainUpperValue, 1, Domain.DISCRETE));
		timer.setDelay(2000 / (int)distributionDomainUpperValue);
		timeStep = trialsDist.getDomain().getWidth();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}
