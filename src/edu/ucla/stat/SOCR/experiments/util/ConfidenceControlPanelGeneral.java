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

import edu.ucla.stat.SOCR.experiments.util.IntervalType;
import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.experiments.*;
import edu.ucla.stat.SOCR.util.FloatSlider;
import edu.ucla.stat.SOCR.distributions.*;

public class ConfidenceControlPanelGeneral extends JPanel implements Observer{
    ConfidenceIntervalExperimentGeneral applet;
   
    JComboBox alphaChoice;
    JComboBox ciChoice;
    int alpha_indx = 3;

    final static double ALPHA_VALUES[] = {0.5, 0.2, 0.1, 0.05, 0.01, 0.001, 0.0001};
    final static double HALF_ALPHA[] = {0.25, 0.1, 0.05, 0.025, 0.005, 0.0005, 0.00005};
    public JToolBar firstToolbar;
    public JToolBar secondToolbar;
    public JToolBar secondToolbar2;
    public JToolBar thirdToolbar;
    
    public JTextField numberOfTrialsText, sampleSizeText;
    JLabel numberOfTrialsLabel, sampleSizeLabel, popVarianceLabel;
    public JCheckBox popVariance;
   // public ValueSliderFloat proportionValueSlider, meanValueSlider, VarValueSlider;
   // public FloatSlider meanValueSlider, VarValueSlider;
    public FloatSlider meanValueSlider, VarValueSlider;
    
    Distribution chosenSOCRDistribution;
    JScrollPane distributionPanel;
    SOCRDistributions SOCRDistributionObject;
    
    public boolean bootStrap = false;
    public int bsSampleSize = BootStrap.defaultSampleSize;
    public JCheckBox bootFlag;
    
    private Observable observable = new Observable() {
		public void notifyObservers() {
			setChanged();
			super.notifyObservers(ConfidenceControlPanelGeneral.this);
		}
	};

    /**
     * constructor of control panel
     *
     * @param applet the calling applet
     */
    public ConfidenceControlPanelGeneral(ConfidenceIntervalExperimentGeneral applet, SOCRDistributions SOCR_Distribution_Object) {
        this.applet = applet;
     //   setBackground(new Color(230, 230, 230));
        
      /*  this.setRequestFocusEnabled(true);
        final JPopupMenu popupMenu = new JPopupMenu("Popup");
        popupMenu.add(makeMenuItem("Close"));
        this.add(popupMenu);*/
       // this.setSize(new Dimension(500, 280));
        SOCRDistributionObject = SOCR_Distribution_Object;
        
        init();
    }
    
    public void initMLE(){
    	init();
    	setSampleSize(20);
    	setNumberOfTrials(20);
    	ciChoice.setSelectedItem(IntervalType.asy_MLE);  	
    }
    
    public void init(){
    	this.removeAll();
    	this.validate();
    	
        firstToolbar = new JToolBar();
        secondToolbar = new JToolBar();
        secondToolbar2 = new JToolBar();
        thirdToolbar = new JToolBar();
        
        firstToolbar.setFloatable(false);
        secondToolbar.setFloatable(false);
        secondToolbar2.setFloatable(false);
        thirdToolbar.setFloatable(false);
    

        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(filter(IntervalType.values()));
        ciChoice = new JComboBox(comboBoxModel);
        ciChoice.setToolTipText("choose Mean/Proportion/Variance/asy_MLE");
        chosenSOCRDistribution = (Distribution) SOCRDistributionObject.getCurrentItem();
        
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {//update the slider
                chosenSOCRDistribution = (Distribution) SOCRDistributionObject.getCurrentItem();
                makeObservable(chosenSOCRDistribution);
                addSlider();
                observable.notifyObservers();  // notify CI
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
                observable.notifyObservers();  // notify CI
            }
        };
        alphaChoice.addActionListener(actionListener2);
        
        meanValueSlider = new FloatSlider("CI Mean", 0, 0);
        meanValueSlider.setVisible(true);  
        meanValueSlider.setBackground(new Color(200, 200, 200));
  //      proportionValueSlider = new ValueSliderFloat("Cut off", 0, 0, false);
  //      proportionValueSlider.setVisible(false);
        VarValueSlider = new FloatSlider("CI Variance", 0, 0);
        VarValueSlider.setVisible(false);
        VarValueSlider.setBackground(new Color(200, 200, 200));
        
        sampleSizeText = new JTextField(8);
        sampleSizeText.setMaximumSize(new Dimension(100, 25));
        sampleSizeText.setPreferredSize(new Dimension(80, 25));
        sampleSizeText.addActionListener(actionListener2);
        numberOfTrialsText = new JTextField(8);
        numberOfTrialsText.setMaximumSize(new Dimension(100, 25));
        numberOfTrialsText.setPreferredSize(new Dimension(80, 25));
        numberOfTrialsText.addActionListener(actionListener2);
               
        sampleSizeLabel = new JLabel("Sample Size");
        numberOfTrialsLabel = new JLabel(" Number of Intervals");
        sampleSizeLabel.setLabelFor(sampleSizeText);
        numberOfTrialsLabel.setLabelFor(numberOfTrialsText);
        
   
        bootFlag = new JCheckBox();
        ActionListener actionListener3 = new ActionListener() {
            public void actionPerformed(ActionEvent evt) { //update the slider
            	bootStrap = bootFlag.isSelected();
            	whichIntervalSelected().setBootStrap(bootStrap);
            	ComboBoxModel comboBoxModel = new DefaultComboBoxModel(filter(IntervalType.values()));
                ciChoice.setModel(comboBoxModel);
                assembleSecondToolbar();
                observable.notifyObservers();  // notify CI 
            }
        };     
        bootFlag.addActionListener(actionListener3);
        
        secondToolbar2.add(new JLabel(" Resample"));
        secondToolbar2.setToolTipText("Resample a given data set a specified number of times");
      	final JTextField bsSize = new JTextField(8);
    	bsSize.setMaximumSize(new Dimension(100, 25));
    	bsSize.setPreferredSize(new Dimension(40, 25));
      	secondToolbar2.setPreferredSize(new Dimension(60, 20));
      	bsSize.setText(Integer.toString(bsSampleSize));
      	secondToolbar2.add(bsSize);
      	secondToolbar2.add(new JLabel("times"));
      	
      	ActionListener actionListener4 = new ActionListener() {
             public void actionPerformed(ActionEvent evt) { //update the slider
             	int size = Integer.valueOf(bsSize.getText());
             	whichIntervalSelected().setBootStrapSize(size);
             	observable.notifyObservers();  // notify CI 
             }
         };  
         bsSize.addActionListener(actionListener4);
         
         
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
       
        JPanel CIgroup = new JPanel(new BorderLayout());
        firstToolbar.setBackground(new Color(200, 200, 200));
        secondToolbar.setBackground(new Color(200, 200, 200));
        secondToolbar2.setBackground(new Color(200, 200, 200));
        thirdToolbar.setBackground(new Color(200, 200, 200));
        CIgroup.setBackground(new Color(200, 200, 200));       
        
        firstToolbar.add(ciChoice);
        firstToolbar.add(alphaChoice);
        
        assembleSecondToolbar();
             
        thirdToolbar.add(meanValueSlider); 
        thirdToolbar.add(VarValueSlider);
        
        CIgroup.add(firstToolbar, BorderLayout.NORTH);
        CIgroup.add(secondToolbar, BorderLayout.CENTER);
        CIgroup.add(thirdToolbar, BorderLayout.SOUTH);
        CIgroup.setBorder(new TitledBorder(new EtchedBorder(),
                "CI Settings:"));
        
        this.add(new JScrollPane(CIgroup));
        
        distributionPanel = new JScrollPane(SOCRDistributionObject.fControlPanel);
        this.add(distributionPanel);
        this.validate();
       
    }

  /*  private JMenuItem makeMenuItem(String label) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(this);
        return item;
      }*/
    
    /**
     * clears panel
     */
    public void clear() {
    }

   
    public void makeObservable(Distribution dist) {
        dist.addObserver(this);
    }

    public void addObserver(Observer observer) {
		observable.addObserver(observer);
		meanValueSlider.addObserver(observer);
	//	proportionValueSlider.addObserver(observer);
		VarValueSlider.addObserver(observer);
	}
    
    public void assembleSecondToolbar() {
    	  secondToolbar.removeAll();
    	  secondToolbar.add(sampleSizeLabel);
          secondToolbar.add(sampleSizeText);
          secondToolbar.add(numberOfTrialsLabel);
          secondToolbar.add(numberOfTrialsText); 
          secondToolbar.add(new JLabel(" Bootstrap"));
          secondToolbar.add(bootFlag);
          
    	  //System.out.println("assemble second toolbar bootStrap="+bootStrap);
          if(bootStrap){
            secondToolbar2.setVisible(true);
          	secondToolbar.add(secondToolbar2);
          }else{
        	  secondToolbar2.setVisible(false);
          }
          
          secondToolbar.setVisible(true);
          secondToolbar.validate();
          
          this.repaint();
    }
    
    public void addSlider() {
        // based on which distribution is selected.......
        // grab the domain for that distribution and move on.......
        if (isProportionInterval()) {
            Domain d = chosenSOCRDistribution.getDomain();
            double lower_bound = chosenSOCRDistribution.inverseCDF(.01);
        	double upper_bound = chosenSOCRDistribution.inverseCDF(.99);        	
   
            meanValueSlider.setVisible(false);
            VarValueSlider.setVisible(false);

        } else if( isMeanInterval() ){
        	double lower_bound = chosenSOCRDistribution.inverseCDF(.01);
        	double upper_bound = chosenSOCRDistribution.inverseCDF(.99);    
        	meanValueSlider.setAll(lower_bound, upper_bound, chosenSOCRDistribution.getMean());
            meanValueSlider.setVisible(true);
    
            VarValueSlider.setVisible(false);
        }else if(isVarInterval()){
        	double lower_bound = 0;
        	double upper_bound = 100;    
        	VarValueSlider.setAll(lower_bound, upper_bound, chosenSOCRDistribution.getVariance());
        	VarValueSlider.setVisible(true);

            meanValueSlider.setVisible(false);                   
        }
        else if(isMLEInterval()){
        	double lower_bound = 0;
        	double upper_bound = 100;    
        	meanValueSlider = new FloatSlider("CI Lambda", 0, 0);
        	thirdToolbar.removeAll();
        	meanValueSlider.setBackground(new Color(200, 200, 200));
            thirdToolbar.add(meanValueSlider); 
            thirdToolbar.add(VarValueSlider);
        	meanValueSlider.setAll(lower_bound, upper_bound, chosenSOCRDistribution.getMean());
            meanValueSlider.setVisible(true);

            VarValueSlider.setVisible(false);          
        }
      
      
        thirdToolbar.setVisible(true);
        thirdToolbar.validate();
        
        this.repaint();
    }

    public void setDistributionObj(SOCRDistributions distObj) {
        SOCRDistributionObject = distObj;
    }

    // set the distribution such that we can add a slider later.
  /*  public void setDistribution(Distribution dist) {
        chosenSOCRDistribution = dist;
    }*/
    public Distribution  getDistribution() {
    	chosenSOCRDistribution = (Distribution) SOCRDistributionObject.getCurrentItem();
        return chosenSOCRDistribution;
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


    public int getSampleSize() {
        int n = Integer.parseInt(sampleSizeText.getText());
        if (n >= 1) return n;
        else {
            sampleSizeText.setText("1");
            return 1;
        }
    }

    public int getNumberOfTrials() {
        int nTrials = Integer.parseInt(numberOfTrialsText.getText());
        if (nTrials >= 1) return nTrials;
        else {
            numberOfTrialsText.setText("1");
            return 1;
        }
    }

    public IntervalType whichIntervalSelected() {
        return  (IntervalType)ciChoice.getSelectedItem();
    }

    public boolean isProportionInterval() {
        if (whichIntervalSelected().equals(IntervalType.proportion_approx) || whichIntervalSelected().equals(IntervalType.proportion_wald) ||whichIntervalSelected().equals(IntervalType.proportion_exact)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isMeanInterval() {
        if (whichIntervalSelected().equals(IntervalType.xbar_sigmaKnown) || whichIntervalSelected().equals(IntervalType.xbar_sigmaUnknown)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isVarInterval() {
        if (whichIntervalSelected().equals(IntervalType.sigma)||whichIntervalSelected().equals(IntervalType.sigma2) ) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isMLEInterval() {
        if (whichIntervalSelected().equals(IntervalType.asy_MLE) ) {
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
        if (isMeanInterval() || isMLEInterval()) {
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

    public void setSampleSize(int n) {
        if (n >= 1) sampleSizeText.setText(Integer.toString(n));
        else sampleSizeText.setText(Integer.toString(1));
    }

    public void setNumberOfTrials(int nTrials) {
        if (nTrials >= 1) numberOfTrialsText.setText(Integer.toString(nTrials));
        else numberOfTrialsText.setText(Integer.toString(1));
    }

    public void update(Observable o, Object arg) {
       // this.setDistribution(chosenSOCRDistribution);
        addSlider();
        observable.notifyObservers();
    }

    IntervalType[] filter(IntervalType[] list){
		IntervalType[] temp = new IntervalType[list.length];
		int j=0;
		
		for (int i=0; i<list.length; i++ ){
			//System.out.println("*"+list[i].toString()+"*");
			if (list[i].toString().length()!=0){
				temp[j]=list[i];
				j++;
			}
		}
		
		//System.out.println("j="+j);
		IntervalType[] out = new IntervalType[j];
		for (int i=0; i<j; i++ )
			out[i]=temp[i];
			
		
		return out;
	}
}

