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

/* ConfidenceInterval.java */

/*
 * ConfidenceInterval.java illustrates construction & utiliation of CI's @author
 * Ivo Dinov
 * 
 * @version 1.0 Feb. 19 2004
 */

package edu.ucla.stat.SOCR.experiments;

import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.core.Experiment;
import edu.ucla.stat.SOCR.core.SOCRApplet;
import edu.ucla.stat.SOCR.core.SOCRDistributions;
import edu.ucla.stat.SOCR.experiments.util.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class ConfidenceIntervalExperimentGeneral extends Experiment implements Observer{
    ConfidenceCanvasGeneral confidenceCanvas;
    ConfidenceControlPanelGeneral confidenceControlPanel;
   // ConfidenceCanvasCalculator confidenceCanvasCalculator;
   
    int n = 20; //number of datapoints, sample-size
    int nTrials = 20; // number of experiments
 
    int cvIndex = 1; // Critical Value index and values (alpha-values)
    double cvValue;
    Container c;
   
    double left, right;

    JScrollPane ciCanvasPanel;
    JScrollPane ciDistributionPanel;      
    
    SOCRDistributions SOCRDistributionObject; 
   // Distribution chosenSOCRDistribution;
    
    JButton settingButton;
    static protected JFrame frame;
    boolean is_MLE= false;
    JToolBar cutoffs;
    boolean bootStrap;
    /**
     * initialization method
     */

    public ConfidenceIntervalExperimentGeneral() {
    	
    	ciCanvasPanel = new JScrollPane();
    	ciDistributionPanel = new JScrollPane(); 
    }
    
    // override the super class's method, remove the lowerGreen Text area
    public Container getDisplayPane() {
    	
    	JSplitPane container = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                getMainPanel(), getTextPanel());
        return container;
    }
    
    public void initialize() {
        confidenceCanvas = new ConfidenceCanvasGeneral(n, nTrials);
     //   confidenceCanvas.setSize(150, 125);
        confidenceCanvas.setBackground(Color.white);
        
        constructDistributionPanelObject("implementedDistributions.txt", "Normal Distribution");
     // left = (new Double(SOCRDistributionObject.leftCutOff.getText())).doubleValue();
     // right = (new Double(SOCRDistributionObject.rightCutOff.getText())).doubleValue();
        
        // instantiate the canvas to create the interface.
        confidenceControlPanel = new ConfidenceControlPanelGeneral(this, SOCRDistributionObject);
        confidenceControlPanel.setNumberOfTrials(nTrials);
        confidenceControlPanel.setSampleSize(n);
        confidenceControlPanel.addObserver(this);//add this as observer for controlPanel
        
        bootStrap = confidenceControlPanel.bootStrap;
        frame = new JFrame();
        
        settingButton = new JButton("Confidence Interval");
        settingButton.setToolTipText("set CI parameters and choose distribution.");
        settingButton.addActionListener(this);
        addTool(settingButton);
        
        cutoffs = new JToolBar();
        cutoffs.setFloatable(false);
        cutoffs.add(SOCRDistributionObject.leftCutOffLabel);
        cutoffs.add(SOCRDistributionObject.leftCutOff);
        cutoffs.add(SOCRDistributionObject.rightCutOffLabel);
        cutoffs.add(SOCRDistributionObject.rightCutOff);
        addToolbar(cutoffs);
        
        SOCRDistributionObject.addObserver(this);
        
        ciCanvasPanel.setViewportView(confidenceCanvas);
        ciCanvasPanel.setPreferredSize(new Dimension(650, 400));      
        ciDistributionPanel.setViewportView(SOCRDistributionObject.getGraphPanel());
        ciDistributionPanel.setPreferredSize(new Dimension(250, 400));
      
        JSplitPane displayPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                ciCanvasPanel, ciDistributionPanel);	
        displayPane.setOneTouchExpandable(true);
        this.getMainPanel().add(displayPane);

        Distribution chosenSOCRDistribution = (Distribution)SOCRDistributionObject.getCurrentItem();
        confidenceCanvas.setDistribution(chosenSOCRDistribution);  
        confidenceControlPanel.addSlider(); // update the slider
        
        getRecordTable().setText(outputText(chosenSOCRDistribution));
    }
    
    private String outputText(Distribution chosenSOCRDistribution){
    	//System.out.println("outputText called");
    	String name;
    	if (chosenSOCRDistribution.getName().length() != 0) 
    		name = chosenSOCRDistribution.getName();
    	else name = "";
    	
    	String slider;
    	if (confidenceControlPanel.isMeanInterval())
    		slider = "CI_Mean = "+confidenceControlPanel.getCIMeanValue();
    	else if (confidenceControlPanel.isVarInterval())
    		slider = "CI_Variance = "+confidenceControlPanel.getCIVarValue();
    	else if (confidenceControlPanel.isMLEInterval())
    		slider = "CI_Lambda = "+confidenceControlPanel.getCIMeanValue();
    	else if (confidenceControlPanel.isProportionInterval())
    		slider="Left Cut Off = "+left+", Right Cut Off = "+right;
    	else slider ="";
    	
    	String text = confidenceControlPanel.whichIntervalSelected().toString()+":"+confidenceControlPanel.whichIntervalSelected().getBootStrapSizeString()
    	+"\n1-alpha = "+(1-confidenceControlPanel.getAlphaValue())
    	+"\nSample Size = "
    	+confidenceControlPanel.getSampleSize()
    	+"\nNumber of Intervals = "+confidenceControlPanel.getNumberOfTrials()+"\n"
    	+slider
    	+"\n\n"+name
    	+"\nExperiment\tMissed Intervals:\n";
    	
    	return text;
    }

    /**
     * play generates sample data
     * <p/>
     * param nTrials is the number of tests to do
     */
    public void play() {
    	//System.out.println("CI play bootStrap ="+bootStrap);
 
        cvIndex = confidenceControlPanel.getAlphaIndex();

        n = confidenceControlPanel.getSampleSize();
        nTrials = confidenceControlPanel.getNumberOfTrials();
        IntervalType ciChoice = (IntervalType) confidenceControlPanel.whichIntervalSelected();
        ciChoice.setBootStrap(bootStrap);
        
        confidenceCanvas.setSampleSizeAndNTrials(n, nTrials);
        confidenceCanvas.setIntervalType(ciChoice);
        
        Distribution chosenSOCRDistribution = confidenceControlPanel.getDistribution();
        if(confidenceControlPanel.isProportionInterval()){
        	updateProportion();
        }else if(confidenceControlPanel.isMeanInterval()||confidenceControlPanel.isMLEInterval()){
        	Double meanValue = confidenceControlPanel.getCIMeanValue();    
        	ciChoice.updateIntervalType(chosenSOCRDistribution, nTrials, n, cvIndex);
        	confidenceCanvas.setCIMeanValue(meanValue);
        }else if(confidenceControlPanel.isVarInterval()){
        	Double VarValue = confidenceControlPanel.getCIVarValue();
        	ciChoice.updateIntervalType(chosenSOCRDistribution, nTrials, n, cvIndex);
        	confidenceCanvas.setCIVarValue(VarValue);
        }
   
        double[][] sampleData = ciChoice.getSampleData();   
        double[][] ciData = ciChoice.getConfidenceIntervals();
        double[]xBar = ciChoice.getX_bar();
        confidenceCanvas.update(cvIndex, ciData, sampleData, xBar);
    }

    // methods to print the sample data to the screen for testing
    public void printArray(double[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        System.out.println("");
    }

    // methods to print the sample data to the screen for testing
    public void print2DArray(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            double[] newArray = array[i];
            printArray(newArray);
            System.out.println("");
        }
    }

    public void reset() {
    	super.reset();
    	//System.out.println("CI reset get called");
        if (confidenceControlPanel != null && confidenceCanvas != null){
        	Distribution chosenSOCRDistribution = confidenceControlPanel.getDistribution();

        //	System.out.println("chosenDistribution is "+chosenSOCRDistribution.getName());
        	confidenceCanvas.setDistribution(chosenSOCRDistribution);

        	confidenceCanvas.setIntervalType(confidenceControlPanel.whichIntervalSelected());
        	confidenceCanvas.setCIMeanValue(confidenceControlPanel.getCIMeanValue());
        	confidenceCanvas.setCIVarValue(confidenceControlPanel.getCIVarValue());
        	
            confidenceCanvas.clear(confidenceControlPanel.getSampleSize(), confidenceControlPanel.getNumberOfTrials(), chosenSOCRDistribution);            
        
	        if (chosenSOCRDistribution != null) 
	        	getRecordTable().setText(outputText(chosenSOCRDistribution));   
        }

        // when proportion is selected, update canvas here since no slider is used for proportion, so no observable
        if(confidenceControlPanel != null && confidenceControlPanel.isProportionInterval()){
        	updateProportion();        	
        }
      //  System.out.println("reset done");
    }

    protected void updateProportion(){
    	IntervalType ciChoice = confidenceControlPanel.whichIntervalSelected();
    	Distribution chosenSOCRDistribution = confidenceControlPanel.getDistribution();
   
    	try{
        	 left = (new Double(SOCRDistributionObject.leftCutOff.getText())).doubleValue();
        	 right = (new Double(SOCRDistributionObject.rightCutOff.getText())).doubleValue();
    	}catch(NumberFormatException e ){
        		 right=0; left=0;
    	}
        //	 System.out.println("left="+left+" right="+right);        	      
    	ciChoice.setCutOffValue(left, right);        	
    	ciChoice.updateIntervalType(chosenSOCRDistribution, nTrials, n, cvIndex);    	
    	confidenceCanvas.setCutOffValue(left, right);
    	double leftCDF = SOCRDistributionObject.getGraphPanel().getLeftCDF();
    	double rightCDF = SOCRDistributionObject.getGraphPanel().getRightCDF();
    	confidenceCanvas.setCDFValue(leftCDF, rightCDF);
    	confidenceCanvas.update();
    }
    
    public void doExperiment() {
    	//System.out.println("CI doExperiment get called");
        super.doExperiment();
        play();
    }

    // reset if chosenSOCRDistribution /interverType /slider changed in controlPanel
    public void update(Observable o, Object arg) {
 
    	if(confidenceControlPanel != null && confidenceControlPanel.isMLEInterval() && is_MLE ==false){
    		//System.out.println("Switched to Asy-MLE");
    		is_MLE= true;
    		constructDistributionPanelObject("implementedDistributions_short.txt", "Poisson Distribution");
    		confidenceControlPanel.setDistributionObj(this.SOCRDistributionObject);
    		confidenceControlPanel.initMLE();
    		updateGUI();
    		//System.out.println("Switched to Asy-MLE done");
    	}else if (confidenceControlPanel != null && !confidenceControlPanel.isMLEInterval() && is_MLE == true){
    	//	System.out.println("Switched off Asy-MLE");
    		is_MLE= false;
    		constructDistributionPanelObject("implementedDistributions.txt", "Normal Distribution");
    		confidenceControlPanel.setDistributionObj(this.SOCRDistributionObject);
    		confidenceControlPanel.init();
    		updateGUI();
    	//	System.out.println("Switched off Asy-MLE done");
    	}	
    	//System.out.println("CI update() called ");
    /*	double left_temp = (new Double(SOCRDistributionObject.leftCutOff.getText())).doubleValue();
    	double right_temp = (new Double(SOCRDistributionObject.rightCutOff.getText())).doubleValue();
    	if(left_temp!=left || right_temp!=right){
    		if(confidenceControlPanel != null && confidenceControlPanel.isProportionInterval()){
            	updateProportion();             	
    		}
    	}*/
    	
    	reset();
    }

    private void updateGUI(){
    	confidenceControlPanel.setNumberOfTrials(nTrials);
        confidenceControlPanel.setSampleSize(n);
        confidenceControlPanel.addObserver(this);
        
        this.toolbars.remove(cutoffs);
        cutoffs = new JToolBar();
        cutoffs.setFloatable(false);
        cutoffs.add(SOCRDistributionObject.leftCutOffLabel);
        cutoffs.add(SOCRDistributionObject.leftCutOff);
        cutoffs.add(SOCRDistributionObject.rightCutOffLabel);
        cutoffs.add(SOCRDistributionObject.rightCutOff);
        addToolbar(cutoffs);
        
        ciDistributionPanel.setViewportView(SOCRDistributionObject.getGraphPanel());
        SOCRDistributionObject.addObserver(this);
    }
    
    //Experiment method
    public void update() {
    	
        resetUpdateCount();
        getRecordTable().append("\n " + getTime());
      
        if (confidenceCanvas!=null && confidenceCanvas.getGraphics()!=null){
        	bootStrap=confidenceControlPanel.bootStrap;   	
        	//System.out.println("CI update get called, bootStrap="+bootStrap);
        	
        	confidenceCanvas.paintComponent(confidenceCanvas.getGraphics());
        }
        getRecordTable().append("\t" + Integer.toString(confidenceCanvas.getMissedCount()));

    }

    /**
     * This method returns basic information about the applet, including copyright
     * information, descriptive informaion, and instructions.
     */
    public String getAppletInfo() {
        return super.getAppletInfo() + "\n\n"
                + "This experiments demonstrates Confidence intervals for sample Means. \n"
                + "\n1. Alpha level, Number of experiments and sample-size [number of random simulations from N(0,1)].\n"
                + "field of view or by clicking the <RandomPnts> button.\n"
                + "2. Click <STEP> or <RUN> to initiate the process of random sampling and construction\n"
                + " of the corresponding confidence intervals for the mean(s)\n"
                + "A confidence interval which misses the origin (0) is indicated by a large green dot!\n"
                + "This experiment demostrates that our non-constructive definitiion of confidence intervals\n"
                + " and our protocol for obtaining the confidence interval for the mean coinside\n"
                + "Therefore we'd expect to see only about 1 green-dot (incorrectly constructed CI)\n"
                + "in 1 out of 20 experiments, where alpha=0.05, no matter what is the sample-size\n"
                + "Of course, CI will be wider for smaller sample-sizes.\n\n"
                + "This simulation randomly samples from Normal(0, 1) distribution and constructs the \n"
                + "100(1-Alpha)% Confidence interval for the population mean using\n"
                + "\t\t y_bar +/- t(n-1, Alpha)*SE(y_bar)."
                + "\n\n http://wiki.stat.ucla.edu/socr/index.php/EBook";
    }


    /**
     * This methods constructs an instance of the SOCRDistributions object, a subclass of SOCRApplet,
     * that provides access to the fControlPanel distribution selection interface
     */
    public void constructDistributionPanelObject(String implementedFileName, String defaultDistributionName) {
        SOCRDistributionObject = new SOCRDistributions(); 
        try {

            java.net.URL SOCR_CodeBase = ((SOCRApplet) applet).getSOCRAppletCodeBase();

            SOCRDistributionObject.setSOCRAppletCodeBase(SOCR_CodeBase);
            SOCRDistributionObject.showAboutButtons = false;
            if(implementedFileName!=null)
            	SOCRDistributionObject.setImplementedFile(implementedFileName);
            else 
            	SOCRDistributionObject.setImplementedFile("implementedDistributions.txt");
            SOCRDistributionObject.init();
            SOCRDistributionObject.addObserver(this);
        //    SOCRDistributionObject.leftCutOff.addActionListener(this);
        //    SOCRDistributionObject.rightCutOff.addActionListener(this);

            SOCRDistributionObject.setSelectedApplication("Normal Distribution");
            // Remove the Left and Right Cutoff Text Fields on the Botton of the SOCR Distribution fControlPanel
            SOCRDistributionObject.fControlPanel.remove(((SOCRApplet) SOCRDistributionObject).jTextAreaPane);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(
                    "Can not construct the URL object http://www.socr.ucla.edu/htmls/jars/ in " +
                            "distributionPanelObject, to parse the implementedDistribution.txt file ");
        }
    }

    // run buttons
    public void actionPerformed(ActionEvent evt) {

    //	System.out.println("Action performed by "+evt.getSource());
        if (evt.getSource() == settingButton) {        	
        	//frame.setTitle("Settings:");
            frame.add(confidenceControlPanel);
            frame.validate();         
            frame.setSize(new Dimension(700, 550));            
            frame.setVisible(true);
        }else if(evt.getSource() == SOCRDistributionObject.leftCutOff || evt.getSource() == SOCRDistributionObject.leftCutOff ){ //leftcutoff, rightcutoff
        //	System.out.println("leftcutoff or rightcutoff changed");
        	updateProportion();
        }
        	else {// run, step, stop
        //    mainPane.setSelectedIndex(mainPane.indexOfComponent(upperPanelPane));
            super.actionPerformed(evt);
        }
    }

}

