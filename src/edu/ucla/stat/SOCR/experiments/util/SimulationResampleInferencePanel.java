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
/* ConfidenceControlPanel.java */

/*  ConfidenceControlPanel.java illustrates construction & utiliation of CI's
* @author Ivo Dinov
* @version 1.0 Feb. 19 2004
*/

/*File ConfidenceControlPanel.java  */
package edu.ucla.stat.SOCR.experiments.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.RandomVariable;
import edu.ucla.stat.SOCR.experiments.util.SimulationResampleType;
import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.experiments.*;
import edu.ucla.stat.SOCR.util.FloatSlider;
import edu.ucla.stat.SOCR.util.Histogram;

public class SimulationResampleInferencePanel extends JPanel implements Observer{
	SimulationResampleExperiment applet;
   
    JComboBox alphaChoice;
    JComboBox ciChoice;
    int alpha_indx = 3;

    final static double ALPHA_VALUES[] = {0.5, 0.2, 0.1, 0.05, 0.01, 0.001, 0.0001};
    final static double HALF_ALPHA[] = {0.25, 0.1, 0.05, 0.025, 0.005, 0.0005, 0.00005};
    public JToolBar firstToolbar;
   // public JToolBar secondToolbar;
   // public JToolBar secondToolbar2;
    public JToolBar thirdToolbar;

    public UserHypothesisHistogram hisGraph;
  //  public  RandomVariableGraph hisGraph;
    
    public JTextField leftCutOffText, rightCutOffText;
    JLabel numberOfTrialsLabel, sampleSizeLabel, popVarianceLabel;
    public JCheckBox popVariance;
   // public ValueSliderFloat proportionValueSlider, meanValueSlider, VarValueSlider;
   // public FloatSlider meanValueSlider, VarValueSlider;
    public FloatSlider meanValueSlider, VarValueSlider;
    public JComboBox resampleChoice;
    public JPanel cutOff;
    public SOCRApplet.SOCRTextArea resultDisplay;
    public JSplitPane outputPanel;
    public JScrollPane outContainer;
    protected double pValue;
    

   // public int bsSampleSize = BootStrap.defaultSampleSize;

    private Observable observable = new Observable() {
		public void notifyObservers() {
			setChanged();
			super.notifyObservers(SimulationResampleInferencePanel.this);
		}
	};

    /**
     * constructor of control panel
     *
     * @param applet the calling applet
     */
    public SimulationResampleInferencePanel(SimulationResampleExperiment applet) {
        this.applet = applet;
    
        init();
    }
    
    public void addObserver(Observer observer) {
		observable.addObserver(observer);
		meanValueSlider.addObserver(observer);
		VarValueSlider.addObserver(observer);
	}
    
    
    public void init(){
    	this.removeAll();
    	this.validate();
    	
    	resultDisplay = new SOCRApplet.SOCRTextArea();
    	
        firstToolbar = new JToolBar();   
        thirdToolbar = new JToolBar();
        
        firstToolbar.setFloatable(false);      
        thirdToolbar.setFloatable(false);
    

        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(filter(SimulationResampleType.values()));
        ciChoice = new JComboBox(comboBoxModel);
        ciChoice.setToolTipText("choose Mean/Proportion/Variance");
        
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {//update the slider
                addSlider();
                updateDisplay();
                observable.notifyObservers();
            }
        };
        ciChoice.addActionListener(actionListener);

        alphaChoice = new JComboBox();
        alphaChoice.setToolTipText("");
        alphaChoice.addItem("1-alpha = " + (1-ALPHA_VALUES[0]));
        alphaChoice.addItem("1-alpha = " + (1-ALPHA_VALUES[1]));
        alphaChoice.addItem("1-alpha = " + (1-ALPHA_VALUES[2]));
        alphaChoice.addItem("1-alpha = " + (1-ALPHA_VALUES[3]));
        alphaChoice.addItem("1-alpha = " + (1-ALPHA_VALUES[4]));
        alphaChoice.addItem("1-alpha = " + (1-ALPHA_VALUES[5]));
        alphaChoice.addItem("1-alpha = " + (1-ALPHA_VALUES[6]));

        alphaChoice.setSelectedIndex(alpha_indx);
        ActionListener actionListener2 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {//update the slider
               updateDisplay();
            }
        };
        
        Observer observer= new Observer() {
			public void update(Observable arg0, Object arg1) {
				updateDisplay();				
			}        
        };
        
        alphaChoice.addActionListener(actionListener2);
        meanValueSlider = new FloatSlider("Hypothesis Mean", 0, 0);
        meanValueSlider.setVisible(true);  
        meanValueSlider.addObserver(observer);
        meanValueSlider.setBackground(new Color(200, 200, 200));
  //      proportionValueSlider = new ValueSliderFloat("Cut off", 0, 0, false);
  //      proportionValueSlider.setVisible(false);
        VarValueSlider = new FloatSlider("Hypothesis Variance", 0, 0);
        VarValueSlider.addObserver(observer);
        VarValueSlider.setVisible(false);
        VarValueSlider.setBackground(new Color(200, 200, 200));
 
        leftCutOffText = new JTextField(8);
        leftCutOffText.setBackground(Color.white);
        leftCutOffText.setMaximumSize(new Dimension(100, 25));
        leftCutOffText.setPreferredSize(new Dimension(80, 25));
        rightCutOffText = new JTextField(8);
        rightCutOffText.setBackground(Color.white);
        rightCutOffText.setMaximumSize(new Dimension(100, 25));
        rightCutOffText.setPreferredSize(new Dimension(80, 25));
         
       
       
        leftCutOffText.addActionListener(actionListener2);
        rightCutOffText.addActionListener(actionListener2);
        
        alphaChoice.addActionListener(actionListener2);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
       
        JPanel CIgroup = new JPanel();
        CIgroup.setLayout(new BoxLayout(CIgroup, BoxLayout.Y_AXIS));
        firstToolbar.setBackground(new Color(200, 200, 200));
      
        thirdToolbar.setBackground(new Color(200, 200, 200));
        CIgroup.setBackground(new Color(200, 200, 200));       
        
        firstToolbar.add(ciChoice);
        firstToolbar.add(alphaChoice);
        
        //assembleSecondToolbar();
             
        cutOff = new JPanel();
        cutOff.setBackground(new Color(200, 200, 200));
        cutOff.add(new JLabel("LeftCutOff:"));
        cutOff.add(leftCutOffText);
        cutOff.add(new JLabel("RightCutOff:"));
        cutOff.add(rightCutOffText);
        cutOff.setVisible(false);
        
        thirdToolbar.add(meanValueSlider); 
        thirdToolbar.add(VarValueSlider);
        thirdToolbar.add(cutOff);
        
        resampleChoice = new JComboBox();
        firstToolbar.add(resampleChoice);

        
        CIgroup.add(firstToolbar);
        CIgroup.add(thirdToolbar);
        CIgroup.setBorder(new TitledBorder(new EtchedBorder(),
                "User Hypothesis:"));
        
        this.add(new JScrollPane(CIgroup));
        resultDisplay.setText("Result:");
        resultDisplay.setPreferredSize(new Dimension(250,250));
        hisGraph = new UserHypothesisHistogram(); 
       // hisGraph = new RandomVariableGraph(); 
        hisGraph.setPreferredSize(new Dimension(250, 250));
        hisGraph.setShowModelDistribution(false);
      
        outputPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , new JScrollPane(resultDisplay), new JScrollPane(hisGraph));
        outputPanel.setDividerLocation(0.5);
       
        outContainer = new JScrollPane(outputPanel);
        this.add(outContainer);
        
        updateDisplay();
        this.validate();
       
    }


    
    /**
     * clears panel
     */
    public void clear() {
    }

   
    public void makeObservable(Distribution dist) {
        dist.addObserver(this);
    }

    
    public void addSlider() {
      
    	 double lower_bound = applet.getLowerBound();
    	 double upper_bound = applet.getUpperBound();  
     	
    	//System.out.println("addSlider get called:lower_bound ="+lower_bound+" upper_bound="+upper_bound);
    	
    	 String[] list = null;
    	
        if (isProportionInterval()) {
        	meanValueSlider.setVisible(false);
            VarValueSlider.setVisible(false);
            cutOff.setVisible(true);
            list = new String[]{"Resample Proportion - Hypothesis Proportion != 0","Resample Proportion - Hypothesis Proportion > 0", "Resample Proportion - Hypothesis Proportion < 0"};

        } else if( isMeanInterval() ){	
        
        	meanValueSlider.setAll(lower_bound, upper_bound, (upper_bound-lower_bound)/2);
            meanValueSlider.setVisible(true);
            VarValueSlider.setVisible(false);
            cutOff.setVisible(false);
            list = new String[]{"Resample Mean - Hypothesis Mean != 0","Resample Mean - Hypothesis Mean > 0", "Resample Mean - Hypothesis Mean < 0"};
        }else if(isVarInterval()){
        	
        	VarValueSlider.setAll(lower_bound, upper_bound, (upper_bound-lower_bound)/2);
        	VarValueSlider.setVisible(true);

            meanValueSlider.setVisible(false); 
            cutOff.setVisible(false);
            list = new String[]{"Resample variance - Hypothesis Variance != 0","Resample variance - Hypothesis Variance > 0", "Resample variance - Hypothesis Variance < 0"};
        }
      
        
        resampleChoice.setModel(new DefaultComboBoxModel(list));
        firstToolbar.validate();
        thirdToolbar.setVisible(true);
        thirdToolbar.validate();
        
        this.repaint();
    }

    
    public double getAlphaValue() {
        return ALPHA_VALUES[alphaChoice.getSelectedIndex()];
    }
    /**
     * Gets alpha information
     * 0-> alpha=0.05, 1-> alpha = 0.01 1-> p < mu0, and 2 -> p != mu0
     */
    public int getAlphaIndex() {
        return alphaChoice.getSelectedIndex();
    }


    public SimulationResampleType whichIntervalSelected() {
        return  (SimulationResampleType)ciChoice.getSelectedItem();
    }

    public boolean isProportionInterval() {
        if (whichIntervalSelected().equals(SimulationResampleType.proportion_wald)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isMeanInterval() {
        if (whichIntervalSelected().equals(SimulationResampleType.xbar_sigmaKnown) ) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isVarInterval() {
        if (whichIntervalSelected().equals(SimulationResampleType.sigma)) {
            return true;
        } else {
            return false;
        }
    }
 
    
 /*   public Double getProportionValue() {
        if (isProportionInterval()) {
            return proportionValueSlider.getValue();
        } else
            return null;
    }*/
    
    public double getCIMeanValue() {
        if (isMeanInterval()) {
        	//System.out.println("ciMean ="+meanValueSlider.getFloatValue());
            return meanValueSlider.getFloatValue();
        } else
            return 0;
    }
    
    public double getCIVarValue() {
        if (isVarInterval()) {
            return VarValueSlider.getFloatValue();
        } else
            return 0;
    }


    public void update(Observable o, Object arg) {
       // this.setDistribution(chosenSOCRDistribution);
        addSlider(); 
        observable.notifyObservers();
    }
    
    
    public void updateDisplay(){
    	//System.out.println("udpateDisplay get called");
    	
    	Domain domain = new Domain(applet.getLowerBound(), applet.getUpperBound(), 1,applet.getNumberOfResample());
    	IntervalData d = new IntervalData(domain);
    	double[] xbar = applet.getBootStrapXBar(ciChoice.getSelectedIndex()); // 0 mean, 1
    	
    	for (int i=0; i<applet.getNumberOfResample(); i++){
    		d.setValue(xbar[i]);	
    		//System.out.println(xbar[i]);
    	}
    	//System.out.println("intervalData size"+d.getSize() +" domain"+ d.getDomain().getLowerBound()+","+d.getDomain().getUpperBound());
    	
    	if(isProportionInterval()){
    		double min = d.getDomain().getLowerBound();
    		double max = d.getDomain().getUpperBound();
	    	if(leftCutOffText.getText().length()==0)
	    		leftCutOffText.setText(String.valueOf(min+(max-min)/4));
	    	if(rightCutOffText.getText().length()==0)
	    		rightCutOffText.setText(String.valueOf(max-(max-min)/4));
    	}
    //	hisGraph= new UserHypothesisHistogram();
    	

    /*	RandomVariable rv = new RandomVariable();
    	for (int i=0; i<applet.getNumberOfResample(); i++){
    		rv.setValue(xbar[i]);
    		//System.out.println(xbar[i]);
    	}*/
    	
    	hisGraph= new UserHypothesisHistogram(d.getDomain(), this);
    	hisGraph.setHistogramType(Histogram.FREQ);
    	hisGraph.setIntervalData(d);
    	if (!isProportionInterval())
    		hisGraph.setUserHypothesis(sliderValue());
    	else {
    		hisGraph.setLeftCutOff(Double.parseDouble(leftCutOffText.getText()));
    		hisGraph.setRightCutOff(Double.parseDouble(rightCutOffText.getText()));
    	}
    	hisGraph.setHistogramColor(Color.white);
    	
    	//hisGraph = new RandomVariableGraph(); 
    	//hisGraph.setMomentType(RandomVariableGraph.MSDS);
    	 //	hisGraph.setRandomVariable(rv);
    	
    	
    	//hisGraph.setDomain(new Domain(applet.getLowerBound(), applet.getUpperBound(), 1,applet.getNumberOfResample()));
   
    	
    	//hisGraph.setShowModelDistribution(false);

    	setPValue();
    	resultDisplay.setText(outputText());
    	hisGraph.setPValue(pValue);
    	
    	
    //	outputPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , new JScrollPane(resultDisplay), new JScrollPane(hisGraph));
     //   outputPanel.setDividerLocation(0.5);
        
    	outputPanel.setLeftComponent(new JScrollPane(resultDisplay));
    	outputPanel.setRightComponent(new JScrollPane(hisGraph));
    	
        outContainer.validate();
        this.validate();
    	observable.notifyObservers();
    }
    
    private void setPValue(){
    	pValue=  0;
    	double[] xbar = applet.getBootStrapXBar(resampleChoice.getSelectedIndex());
    	int resampleSize = applet.getNumberOfResample();
    	double alpha = getAlphaValue();
    			
    	double[] diff= new double[resampleSize];
    	
    	
    	if (isMeanInterval()){
    		double userMean = getCIMeanValue();
    		for (int i=0; i<resampleSize; i++){
    	    	diff[i] = xbar[i]-userMean;
        	}
    		
    	}
    	else if (isVarInterval()){
    		double userVar = getCIVarValue();
    		
    			
    	}
    	else if (isProportionInterval()){
    		double userLeft= Double.parseDouble(leftCutOffText.getText());
    		double userRight = Double.parseDouble(rightCutOffText.getText());
    				
    	}
    	else pValue=0;
    
    }
    
    private String outputText(){
    
    	String slider;
    	if (isMeanInterval())
    		slider = "Hypothesis Mean = "+getCIMeanValue();
    	else if (isVarInterval())
    		slider = "Hypothesis Variance = "+getCIVarValue();
    	else if (isProportionInterval())
    		slider="Hypothesis Left Cut Off = "+leftCutOffText.getText()+", Right Cut Off = "+rightCutOffText.getText();
    	else slider ="";
    	
    	String text = whichIntervalSelected().toString()
    	+"\n1-alpha = "+(1-getAlphaValue())
    	+"\nSample Size = "
    	+ applet.getSampleSize()
    	+"\nNumber of Resamples = "+applet.getNumberOfResample()+"\n"
    	+slider +
    	"\n P_Value " + pValue;
    	
    	return text;
    }
    
    private double sliderValue(){
        
    	double slider = 0;
    	if (isMeanInterval())
    		slider = getCIMeanValue();
    	else if (isVarInterval())
    		slider = getCIVarValue();
    	
    	
    	 return slider;
    }
    
    SimulationResampleType[] filter(SimulationResampleType[] list){
    	SimulationResampleType[] temp = new SimulationResampleType[list.length];
		int j=0;
		
		for (int i=0; i<list.length; i++ ){
			//System.out.println("*"+list[i].toString()+"*");
			if (list[i].toString().length()!=0){
				temp[j]=list[i];
				j++;
			}
		}
		
		//System.out.println("j="+j);
		SimulationResampleType[] out = new SimulationResampleType[j];
		for (int i=0; i<j; i++ )
			out[i]=temp[i];
			
		
		return out;
	}
}

