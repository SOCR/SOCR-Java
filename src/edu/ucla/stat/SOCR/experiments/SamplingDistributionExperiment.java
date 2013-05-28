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

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/** A Sampling Distribution and CLT Experiment */
public class SamplingDistributionExperiment extends edu.uah.math.experiments.Experiment  {
    	//Variables
    	//currently selected Distribution;
    protected Distribution dist;
    Distribution chosenSOCRDistribution;

    double left_limit, right_limit; // These are the variables storing the Native-support - to - 
    double localSD, localMean, localMedian;	// 33-bin plot [0 : 32] transformation.
	
    //Objects
    private SOCRJComboBox distributionJComboBox;
    public JComboBox firstSamplingDistJComboBox, secondSamplingDistJComboBox;
    public JComboBox firstSampleSizeJComboBox, secondSampleSizeJComboBox;

    public JButton populationTitleButton, sampleTitleButton, stat1TitleButton, stat2TitleButton;
    public JButton takeSOCRDistributionButton;	// this is the button that passes the specific 
    	// user-selected distribution from the local instance of SOCRDistributionObject
    	// SOCRDistributions/Tab to the samplingDistributionObject
    
    public JButton refreshStatsTableButton, goToSOCR_CLT_Activity;
    
    JCheckBox fitNormalModelSample1CheckBox, fitNormalModelSample2CheckBox;
   	
    JPanel controlPanel;
    JPanel titleGraphPanel, histogramGraphPanel, resultSummaryPanel;
    JTabbedPane samplingHorizontalTabbedPane;
    JSplitPane titleGraphSplitPane, graphSummarySplitPane;
    JScrollPane distributionPanel;
    SamplingExperimentMainFrame samplingDistributionObject;
    
    SOCRDistributions SOCRDistributionObject;
	JPanel distributionPanelObject;

    String[] summaryColumnNames = {"Sample Size", "Mean", "Median", "SD", "Skewness", "Kurtosis"};
    String[] summaryColumnNamesParameters = {"Number of Samples", "Mean", "Median", "SD", "Skewness", "Kurtosis"};

    double [][] summaryStatisticsData; // summaryStatisticsData[0<=histogramIndex<4][0<=statisticsIndex<6]
    int histogramIndex=4, statisticsIndex=6; // see stats above
    JTable[] statsTable;

    public int[] frequenciesSOCRDistributionData;
                 
    int i, j;

    /**
     * Initialize the experiment: tables, graphs, scrollbars, labels, drop-down
     * box
     */
    public void init() {
   	  super.init();
      setName("Sampling Distribution Experiment");

	  constructSamplingDistributionObject();
	  
	  frequenciesSOCRDistributionData = new int[samplingDistributionObject.getBinNumbers()];
	  for (int i=0; i < frequenciesSOCRDistributionData.length; i++) frequenciesSOCRDistributionData[i] = 0;
		  
	  constructDistributionPanelObject();

	  setNormalAsDefaultDistribution();

		//JComboBoxes
	  firstSamplingDistJComboBox = samplingDistributionObject.stat;
	  firstSamplingDistJComboBox.setToolTipText("Choose the First Population Parameter to Estimate from Sample Data");
      secondSamplingDistJComboBox = samplingDistributionObject.stat2c;
      secondSamplingDistJComboBox.setToolTipText("Choose the Second Population Parameter to Estimate from Sample Data");
      firstSampleSizeJComboBox = samplingDistributionObject.sampleSize;
      firstSampleSizeJComboBox.setToolTipText("Choose the Sample-Size for the First Parameter Estimate");
	  secondSampleSizeJComboBox = samplingDistributionObject.sampleSize2;
	  secondSampleSizeJComboBox.setToolTipText("Choose the Sample-Size for the Second Parameter Estimate");

		// CheckBoxes
	  fitNormalModelSample1CheckBox = samplingDistributionObject.fit1;
	  fitNormalModelSample1CheckBox.setToolTipText("Select This Checkbox to Fit Normal Model to First Sampling Distribution");
	  fitNormalModelSample2CheckBox = samplingDistributionObject.fit2;
	  fitNormalModelSample2CheckBox.setToolTipText("Select This Checkbox to Fit Normal Model to First Sampling Distribution");
	  
	  	// JButton
	  refreshStatsTableButton = new JButton("Refresh Stats Table");
	  refreshStatsTableButton.setToolTipText("Click This Button to Update the Summary Statistics in the Table below");
	  refreshStatsTableButton.addActionListener(this);
	  
	  goToSOCR_CLT_Activity = new JButton("Go To CLT Activity");
	  goToSOCR_CLT_Activity.setToolTipText("Click This Button to Get a Browser Open with a matching SOCR CLT Activity");
	  goToSOCR_CLT_Activity.addActionListener(this);
	  
		// Control Panel
	  controlPanel = new JPanel();
	  controlPanel.add(refreshStatsTableButton);
	  controlPanel.add(fitNormalModelSample1CheckBox);
      controlPanel.add(firstSamplingDistJComboBox);
      controlPanel.add(firstSampleSizeJComboBox);
	  controlPanel.add(fitNormalModelSample2CheckBox);
      controlPanel.add(secondSamplingDistJComboBox);
      controlPanel.add(secondSampleSizeJComboBox);
	  controlPanel.add(samplingDistributionObject.progressBar);
	  controlPanel.add(goToSOCR_CLT_Activity);
	  samplingDistributionObject.progressBar.setToolTipText("Shows Progress for Large Sampling Tasks");
	  
		// Add the tool-controls to the toolbar
      addToolBar(controlPanel);	

		// COnstruct the JTabbedPane
	  samplingHorizontalTabbedPane = new JTabbedPane();
	          
		// COnstruct the TitleGraphPanel, HistogramGraphs panel and the ResultSummary Panel
	  titleGraphPanel = new JPanel ();
	  titleGraphPanel.setPreferredSize(new Dimension(50, 440));
	  titleGraphPanel.setLayout(new GridLayout(0,1));

	  // Instantiate the 4 rows of buttons (labels) and Distribution Graphs
	  populationTitleButton = new JButton(samplingDistributionObject.h.title);
	  samplingDistributionObject.h.setToolTipText("Row 1: Native Process Distribution");
	  populationTitleButton.setToolTipText("Row 1: Native Process");
	  sampleTitleButton = new JButton(samplingDistributionObject.hh.title);
	  sampleTitleButton.setToolTipText("Row 2: Animated Samples/Sample Histogram Distribution");
	  samplingDistributionObject.hh.setToolTipText("Row 2: Animated Samples/Sample Histogram Distribution");
	  stat1TitleButton = new JButton(samplingDistributionObject.hhh.title);
	  stat1TitleButton.setToolTipText("Row 3: Sampling Distribution of the estimate of the First Parameter");
	  samplingDistributionObject.hhh.setToolTipText("Row 3: Sampling Distribution of the estimate of the First Parameter");
	  stat2TitleButton = new JButton(samplingDistributionObject.hhhh.title);
	  stat2TitleButton.setToolTipText("Row 4: Sampling Distribution of the estimate of the Second Parameter");
	  samplingDistributionObject.hhhh.setToolTipText("Row 4: Sampling Distribution of the estimate of the Second Parameter");
	  
	  titleGraphPanel.add(populationTitleButton);
	  titleGraphPanel.add(sampleTitleButton);
	  titleGraphPanel.add(stat1TitleButton);
	  titleGraphPanel.add(stat2TitleButton);
	  //titleGraphPanel.setToolTipText("Each of these 4 rows represents the\n "+
	  	//"Native Population (Top), Sample Data, First and Second (Bottom) Sampling Distributions");

	  histogramGraphPanel = new JPanel ();
	  histogramGraphPanel.setPreferredSize(new Dimension(500, 440));
	  histogramGraphPanel.setLayout(new GridLayout(0,1));
	  histogramGraphPanel.add(samplingDistributionObject.h);
	  histogramGraphPanel.add(samplingDistributionObject.hh);
	  histogramGraphPanel.add(samplingDistributionObject.hhh);
	  histogramGraphPanel.add(samplingDistributionObject.hhhh);
	  //histogramGraphPanel.setToolTipText("These 4 panels represent the distribution graphs of the\n "+
			  	//"Native Process (Top), Individual Sample, First and Second (Bottom) Sampling Distributions");

	  resultSummaryPanel = new JPanel ();
	  resultSummaryPanel.setPreferredSize(new Dimension(250, 440));
	  resultSummaryPanel.setLayout(new GridLayout(0,1));

	  statsTable = new JTable [histogramIndex];
	  
	  summaryStatisticsData = new double [histogramIndex][statisticsIndex];
	  for (i=0; i<histogramIndex; i++)
	  	for (j=0; j<statisticsIndex; j++) summaryStatisticsData[i][j]=0;

	  for (i=0; i<histogramIndex; i++) 
	  {	Object [][] tempData = new Object [1][statisticsIndex];
		for (j=0; j<statisticsIndex; j++) tempData[0][j]= new Double(summaryStatisticsData[i][j]);

			// Bottom two rows of stats for the two parameters that we study should have "Number of samples"
			// as the title of the first column.
			// The top two rows of the Native and Smaple population should have the first columns be
			// "Sample Size"
	 	if (i>=2) statsTable[i] = new JTable (tempData, summaryColumnNamesParameters);
	 	else statsTable[i] = new JTable (tempData, summaryColumnNames);
	 	
	 	if (i==0) statsTable[i].setToolTipText("Summary statistics for the distribution of the Native Process");
	 	else if (i==1) statsTable[i].setToolTipText("Summary statistics of the Animated Sample");
	 	else if (i==2) statsTable[i].setToolTipText("Summary statistics for the sampling distribution of the estimate of the First Parameter");
	 	else if (i==3) statsTable[i].setToolTipText("Summary statistics for the sampling distribution of the estimate of the Second Parameter");

	 	resultSummaryPanel.add(statsTable[i].getTableHeader(), BorderLayout.CENTER);
		resultSummaryPanel.add(statsTable[i], BorderLayout.CENTER);
	   }
	  resultSummaryPanel.setToolTipText("This Table Shows Statistics for the 4 Processes: \n"+
	       "Native Process (Top), Individual Sample, First and Second (Bottom) Sampling Distribution");
	  
		// COnstruct the 2 JSplitPanes of the titleGraph (title + histogramGraphPanel) & 
		//	graphSummary (histogram + resultSummaryPanel)
	  titleGraphSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
	  graphSummarySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

	  titleGraphSplitPane.add(titleGraphPanel);
	  titleGraphSplitPane.add(histogramGraphPanel);

	  graphSummarySplitPane.add(titleGraphSplitPane);
	  graphSummarySplitPane.add(resultSummaryPanel);
	  
	  distributionPanel = new JScrollPane(SOCRDistributionObject.fControlPanel);
	  addComponent(graphSummarySplitPane,1, 0, 1, 1);

		//COnstruct the Second JTabbedPane - for selection of the SOCR Distribution
	  //distributionPanel = new JPanel();

	  	// Add the Panels to the JTabbedPane
	  samplingHorizontalTabbedPane.addTab("Histograms and Summaries", graphSummarySplitPane);
	  samplingHorizontalTabbedPane.addTab("Distributions", distributionPanel);
	  //System.err.println("distributionPanelObject="+distributionPanelObject);
	  
	  this.getContentPane().add(samplingHorizontalTabbedPane);
    }

    /** This method sets the default distribution
     */
    public void setNormalAsDefaultDistribution() {
		chosenSOCRDistribution = new NormalDistribution(0,1);
		
		localSD = chosenSOCRDistribution.getSD();
		localMean = chosenSOCRDistribution.getMean();
		localMedian = chosenSOCRDistribution.getMedian();
  		//System.err.println("localSD="+localSD+"\tlocalMedian="+localMedian);
  		// double left_limit, right_limit; Made these variables Global (See top).
  		
		left_limit = localMean-3*localSD;
    	right_limit = localMean+3*localSD;
		
		double temp_dbl;
		System.err.println("Default Distribution Summaries:\nMean="+localMean+"\tMedian="+localMedian+
				"\tright_limit="+right_limit+"\tleft_limit="+left_limit);
		
		for (int i=0; i < frequenciesSOCRDistributionData.length; i++)
		{	temp_dbl = chosenSOCRDistribution.getDensity(left_limit + 
		 			i*(right_limit - left_limit)/frequenciesSOCRDistributionData.length);
			frequenciesSOCRDistributionData[i] = (int)(
					(samplingDistributionObject.getBinNumbers()-1)*
					temp_dbl/chosenSOCRDistribution.getMaxDensity() +0.5);
		 	//System.out.println("frequenciesSOCRDistributionData["+i+"]="+frequenciesSOCRDistributionData[i]);
		}
	
		samplingDistributionObject.setDistributionName("SOCR Normal Distribution");
		samplingDistributionObject.setDist();
		//if (!samplingDistributionObject.getName().contains("Native Distribution NEW"))
		samplingDistributionObject.h.setTitle("Native Distribution: "+chosenSOCRDistribution.getName());
    }

    /** This method constructs an instance of the SamplingExperimentMainFrame object
     * that is used to to the simulation, rendering and result computing
     */
    public void constructSamplingDistributionObject()
    {   	samplingDistributionObject = new SamplingExperimentMainFrame(this);
    }

    /** This method constructs an instance of the SOCRDistributions object, a subclass of SOCRApplet,
     * that provides access to the fControlPanel distribution selection interface
     */
    public void constructDistributionPanelObject()
    {   SOCRDistributionObject = new SOCRDistributions();
    	java.net.URL SOCR_CodeBase;
    	try { 
    		SOCR_CodeBase = applet.getCodeBase(); 
    		SOCRDistributionObject.setSOCRAppletCodeBase(SOCR_CodeBase);
    		SOCRDistributionObject.init();
    		//SOCRDistributionObject.initGUI();
    		takeSOCRDistributionButton = new JButton ("Sample from this current SOCR Distribution");
    		SOCRDistributionObject.addButton(takeSOCRDistributionButton);
    		takeSOCRDistributionButton.addActionListener(this);
    		distributionPanelObject = SOCRDistributionObject.fControlPanel;
    		
    		// Remove the Left and Right Cutoff Text Fields on the Botton of the SOCR Distribution fControlPanel
    		SOCRDistributionObject.fControlPanel.remove(((SOCRApplet)SOCRDistributionObject).jTextAreaPane);
    		//SOCRDistributionObject.fControlPanel.remove(SOCRDistributionObject.leftCutOff);
    		//SOCRDistributionObject.fControlPanel.remove(SOCRDistributionObject.leftCutOffLabel);
    		//SOCRDistributionObject.fControlPanel.remove(SOCRDistributionObject.rightCutOff);
    		//SOCRDistributionObject.fControlPanel.remove(SOCRDistributionObject.rightCutOffLabel);
    		
    		//	Add the local class as the event-handler for the distribution/parameter changes that are
    		// 	associated with the distributionPanelObject object:
    		//	distributionPanelObject.Container.addActionListener(samplingDistributionObject);
    	}
    	catch (Exception e) {
    		  System.err.println("SOCR Sampling Distribution CLT Experiment\n"+
    		  			"Can not construct the URL object http://www.socr.ucla.edu/htmls/jars/ in "+
    		  			"distributionPanelObject, to parse the implementedDistribution.txt file ");
    	}    	
    }

	/**
	* This method defines the experiment. A random sample of the specified distribution is obtained,
	* the sample statistics are computed and the sampling distribution is displayed.
	*/
	public void doExperiment(){
		super.doExperiment();
		/**
		int sampleSizeInt = (samplingDistributionObject.N+samplingDistributionObject.N2)/2;
		if (sampleSizeInt <= 19) sampleSizeInt = 1;		
		(samplingDistributionObject).sample1(sampleSizeInt);
		***/
		(samplingDistributionObject).sample1(1);
	}

	/**
	* This method starts the step process.
	*/
	public void step(){
		stop();
		doExperiment();

		//updateSummaryStatsTables();
		
		//(samplingDistributionObject).sample1(1);
		update();
		//timer.start();

      }

	/**
	* This method stops the step process, if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		//super.run();
		//if (samplingDistributionObject.N >19 && samplingDistributionObject.N2>19) {
			timer.start();
			//stopCount=0;
		//}
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
		updateSummaryStatsTables();
	}

	/**
	* This method updates the experiment, by updating the random variable graph.
	*/
	public void update(){
		super.update();
		samplingDistributionObject.repaint();
		repaint();
		
		// NEEDS WORK!!!!
		// update graphs + update stats
		//samplingDistributionObject.getComputedHistoStats();
		//updateSummaryStatsTables();
		//(samplingDistributionObject).repaint();
	}

	public void updateSummaryStatsTables()
	{ 	double tempDouble;
	
	  try {
		for (i=0; i<histogramIndex; i++)
	  		for (j=0; j<statisticsIndex; j++) summaryStatisticsData[i][j]=0;
	
		//Distribution chosenSOCRDistribution = (Distribution) SOCRDistributionObject.getCurrentItem();
	
		for (i=0; i<histogramIndex; i++) 
		{	Object [][] tempData = new Object [1][statisticsIndex];
			double[] histogramStats = new double[5];
			int N=0;
			if (i==0) 
			{	N=samplingDistributionObject.h.getNumSamples();
				histogramStats = samplingDistributionObject.h.getComputedHistoStats();
			}
			else if (i==1) 
			{	N=samplingDistributionObject.hh.getNumSamples();
				histogramStats = samplingDistributionObject.hh.getComputedHistoStats();
			}
			else if (i==2) 
			{	N=samplingDistributionObject.hhh.getNumSamples();
				histogramStats = samplingDistributionObject.hhh.getComputedHistoStats();
			}
			else if (i==3) 
			{	N=samplingDistributionObject.hhhh.getNumSamples();
				histogramStats = samplingDistributionObject.hhhh.getComputedHistoStats();
			}
		
			for (j=0; j<statisticsIndex; j++) 
			{	if (j==0)	// get sample-size
				{	statsTable[i].setValueAt(new Double (N), 0, j);
				}
				else if(j==1){	// mean
					double mean = chosenSOCRDistribution.getMean();
					
					//	The Case of the Top-histogram --supposed to represent the Native distribution!!!
					// (i==0 && Double.isNaN(mean)==true ==> Just report NaN.
					// Otherwise report sample summary statistics.
					if (i!=0 || !Double.isNaN(mean)) mean = chosenSOCRDistribution.getMedian();
					
					// if (i>0 && Double.isNaN(mean)) mean = chosenSOCRDistribution.getMedian();
					
					//System.out.println("Mean="+mean+
							//"\thistogramStats[j-1]="+histogramStats[j-1]+"\tlocalSD="+localSD);
					
					tempDouble=mean+(histogramStats[j-1]-16.5)*(3*localSD)/16;
					//if (chosenSOCRDistribution.getName().contains("Bernoulli")) tempDouble=mean;
					statsTable[i].setValueAt(new Double (tempDouble), 0, j);
				}
				else if(j==2){	// median
					localMedian = chosenSOCRDistribution.getMedian();
					System.out.println("localMedian="+localMedian);
					tempDouble=localMedian+(histogramStats[j-1]-16.5)*(3*localSD)/16;
					//if (chosenSOCRDistribution.getName().contains("Bernoulli")) tempDouble=localMedian;
					statsTable[i].setValueAt(new Double (tempDouble), 0, j);
				}
				else if(j==3){	// SD
					tempDouble=histogramStats[j-1]*(localSD)*(3.0/16.5);
					statsTable[i].setValueAt(new Double (tempDouble), 0, j);
				}
				else {			// Skewness and Kurtosis are Linearly Invariant
					tempDouble=histogramStats[j-1];
					statsTable[i].setValueAt(new Double (tempDouble), 0, j);
					//	statsTable[i].setValueAt(new Double (histogramStats[j-1]), 0, j);
					//	Using the Inverse transform X=(6*S/32)(Y-32)+(M-3*S)
				}
			}
			statsTable[i].repaint();
		}
	  } catch (Exception e) {
		  System.out.println("Exception in method updateSummaryStatsTables(): "+ e);
	  }
	}

	/**
	* This method resets the experiment, including the random variable and the
	* random variable graph.
	*/
	public void reset(){
		super.reset();
		resetSample();
		(samplingDistributionObject).repaint();
	}

	/**
	* This method resets the random variable and its graph.
	*/
	public void resetSample(){
		samplingDistributionObject.clearAll();
	}

	/**
	* This method handles the scrollbar event for changing the sample size.
	*/
	public void stateChanged(ChangeEvent event){
        if (event.getSource() == distributionJComboBox) {
        } 
 	}

    public void actionPerformed(ActionEvent evt) {
    	Object source = evt.getSource();
    	if (source==takeSOCRDistributionButton) {
            /* 1. Identify the chosen distribution + its parameters
        	 * 2. Sample from the selected distribution N values	
        	 * 3. Send these to samplingDistributionObject:
    		 *			samplingDistributionObject.theDist="SOCR Distribution";
    		 *			samplingDistributionObject.setDist();
    		 *			samplingDistributionObject.setXdata2();
    		 *			fdata[i] == frequency-data, 0<= i <=32
    		 *			get the chosen distribution median/SD and devide the range [M-3*s : M+3*s] by 33 points
    		 *			then set: samplingDistributionObject.fdata[i]=getDensity[point];
    		 *			frequenciesSOCRDistributionData;
        	 * 4. DO we need to construct the histogram first? If so, we can just send valies of the PDF
        	 * as histogram bins.
        	 */
    		
    		chosenSOCRDistribution = (Distribution) SOCRDistributionObject.getCurrentItem();
     		
    		//double left_limit=chosenSOCRDistribution.inverseCDF(0.01), right_limit=chosenSOCRDistribution.inverseCDF(0.99);
    		
    		/**************************** Problem with Cauchy Distribution! *****/
    		localSD = chosenSOCRDistribution.getSD();
    		localMean = chosenSOCRDistribution.getMean();
    		localMedian = chosenSOCRDistribution.getMedian();
      		//System.err.println("localSD="+localSD+"\tlocalMedian="+localMedian);
      		// double left_limit, right_limit; Made these variables Global (See top).

        	if (Double.isNaN(localSD) || Double.isNaN(localMean) || localSD<=0) {
    			//System.err.println("Test");
    			left_limit = chosenSOCRDistribution.inverseCDF(0.01);
        		right_limit = chosenSOCRDistribution.inverseCDF(0.99);
        		if (Double.isNaN(localMean)) localMean=localMedian; // localMedian = (right_limit-left_limit)/2;
        		if (Double.isNaN(localSD)) localSD=(right_limit-left_limit)/6; // localSD = (right_limit-left_limit)/6;
    		}
    		else {
    			left_limit = localMean-3*localSD;
        		right_limit = localMean+3*localSD;
    		}
    		
        	/********************
			Domain dom = chosenSOCRDistribution.getDomain();
			if (chosenSOCRDistribution.getName().contains("Bernoulli") && !Double.isNaN(dom.lowerValue) && !Double.isNaN(dom.upperValue) ) {
    			//System.err.println("Test");
    			left_limit = dom.lowerValue;
        		right_limit = dom.upperValue;
        		System.err.println("Distribution: "+chosenSOCRDistribution.getName()
    					+" Domain ("+dom.lowerValue+","+dom.upperValue+")!");
			}
			*********/
			
    		if (right_limit <= left_limit)   right_limit = left_limit+1;
    		double temp_dbl;
    		System.err.println("Mean="+localMean+" Median="+localMedian+
    				" left_limit="+left_limit+" right_limit="+right_limit);
    		
    		for (int i=0; i < frequenciesSOCRDistributionData.length; i++)
    		{	temp_dbl = chosenSOCRDistribution.getDensity(left_limit + 
    		 			i*(right_limit - left_limit)/frequenciesSOCRDistributionData.length);
    			//System.out.println("temp_dbl["+i+"]="+temp_dbl);
    		 	//frequenciesSOCRDistributionData[i] = (int)(6*(right_limit - left_limit)*temp_dbl +0.5);
    			//
    			// samplingDistributionObject.getBinNumbers() == 33!!!!!!
    			frequenciesSOCRDistributionData[i] = (int)(
    					(samplingDistributionObject.getBinNumbers()-1)*
    					temp_dbl/chosenSOCRDistribution.getMaxDensity() +0.5);
    		 	//System.out.println("frequenciesSOCRDistributionData["+i+"]="+frequenciesSOCRDistributionData[i]);
    		}
    	
    		samplingDistributionObject.setDistributionName("SOCR Distribution");
    		samplingDistributionObject.setDist();
    		
    		//System.out.println("(1) chosenSOCRDistribution.getName() = "+chosenSOCRDistribution.getName());
    		// Ivo added a fix to update the name of the Native Population Button on Top-left
    		samplingDistributionObject.h.setTitle("Native Distribution: "+chosenSOCRDistribution.getName());
    		populationTitleButton.setText(samplingDistributionObject.h.title);
    		populationTitleButton.setToolTipText("Row 1: Native Process: "+
    				samplingDistributionObject.h.title);
        } 
    	else if (source==refreshStatsTableButton)  updateSummaryStatsTables();
    	else if (source==goToSOCR_CLT_Activity) {
    		try { applet.getAppletContext().showDocument(
                        new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_GeneralCentralLimitTheorem"), 
                			"SOCR: CLT Activity");
               } catch (MalformedURLException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                        e.printStackTrace();
               }
    	}
    	/***
    	 */ 
    	else if (evt.getSource() == timer){
			if ((stopCount == stopFreq) | stopNow) stop();
			else{
				super.doExperiment();
				//(samplingDistributionObject).sample1(1);
				(samplingDistributionObject).sample1NoAnimation(1);
				stopCount++;
				updateCount++;
				if (updateCount == updateFreq) update();
			}
		}
		/**/
    	else super.actionPerformed(evt);
    }
    
    /** This method gets the array of the current frequenciesSOCRDistributionData 
     */
    public int [] getFreqSOCRDistributionData()
    {
    	return frequenciesSOCRDistributionData;
    }
    
	/**
	* This method adds a new component to the second toolbar.
	* @param c the tool
	*/
	public void addTool(Component c){
		//toolBar.add(c);
	}

	public String getAppletInfo(){
		return "\nCopyright (C) 2007, Juana Sanchez, Nicolas Christou, Robert Gould and Ivo Dinov.\n\n"
			+ "This applet is part of the SOCR Experiments: Sampling Distribution (CLT) Experiment,\n"
			+ "which demonstrates the properties of the sampling distributions of various sample statistics.\n"
			+ "This applet can be used to demonstrate the Central Limit Theorem (CLT) as well as:\n"
			+ "\t 1. Investigate the effect of the Native population on various sample statistics\n"
			+ "\t 2. Study the effects of the sample-sizes on the sampling distribution\n"
			+ "\t 3. Determine which sample statistics may have known distributions (e.g., Normal),\n"
			+ "\t    under what conditions\n"
			+ "\t 4. Explore the relations between Population parameters and their corresponding sample counterparts\n"
			+ "\t \t For example population mean/SD and sample mean/SD, symmetry, distribution shape, etc.\n"
			;
	}
    
        /* (non-Javadoc)
         * @see edu.ucla.stat.SOCR.core.IExperiment#getOnlineDescription()
         */
        public String getOnlineDescription() {
            return getAppletInfo();
        }
}
