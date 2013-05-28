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
package edu.ucla.stat.SOCR.analyses.util;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import edu.ucla.stat.SOCR.analyses.gui.ConfidenceIntervalAnalysis;
import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.experiments.util.BootStrap;
import edu.ucla.stat.SOCR.analyses.util.IntervalType;

public class ConfidenceControlPanel extends JPanel implements Observer, ActionListener{
    ConfidenceIntervalAnalysis applet;
   
    JComboBox alphaChoice;
    JComboBox ciChoice;
    int alpha_indx = 3;
    double right, left, variance;

    final static double ALPHA_VALUES[] = {0.5, 0.2, 0.1, 0.05, 0.01, 0.001, 0.0001};
    final static double HALF_ALPHA[] = {0.25, 0.1, 0.05, 0.025, 0.005, 0.0005, 0.00005};
    public JToolBar firstToolbar;
    public JToolBar secondToolbar;
    public JToolBar secondToolbar2;
    public JToolBar thirdToolbar;
    
    JCheckBox bootFlag;
    boolean bootStrap = false;
    public int bsSampleSize = BootStrap.defaultSampleSize;
    
    JTextField leftCutOff, rightCutOff, knownVariance;
    
    private Observable observable = new Observable() {
		public void notifyObservers() {
			setChanged();
			super.notifyObservers(ConfidenceControlPanel.this);
		}
	};

    /**
     * constructor of control panel
     *
     * @param applet the calling applet
     */
    public ConfidenceControlPanel(ConfidenceIntervalAnalysis applet) {
        this.applet = applet;    
        this.setPreferredSize(new Dimension(300, 200));
        
        init();
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
        
        leftCutOff = new JTextField("0");
        leftCutOff.setMaximumSize(new Dimension(100, 25));
        leftCutOff.setPreferredSize(new Dimension(80, 25));
        leftCutOff.addActionListener(this);
        rightCutOff = new JTextField("0");
        rightCutOff.setMaximumSize(new Dimension(100, 25));
        rightCutOff.setPreferredSize(new Dimension(80, 25));
        rightCutOff.addActionListener(this);
       
        
        
        knownVariance = new JTextField("1");
        knownVariance.setMaximumSize(new Dimension(100, 25));
        knownVariance.setPreferredSize(new Dimension(80, 25));
        knownVariance.addActionListener(this);

        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(filter(IntervalType.values()));
        ciChoice = new JComboBox(comboBoxModel);
        ciChoice.setToolTipText("choose Mean/Proportion/Variance");
        ciChoice.setSelectedItem(IntervalType.xbar_sigmaUnknown);
        ciChoice.addActionListener(this);

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
       // alphaChoice.addActionListener(this);
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
      	bsSize.setPreferredSize(new Dimension(80, 25));
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
         
        firstToolbar.add(ciChoice);
        assembleSecondToolbar();
   
        assemblePanel();
    }
    public void assembleSecondToolbar() {
  	  secondToolbar.removeAll();
  	
  	  secondToolbar.add(alphaChoice);
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
    
    public void assembleThirdToolbar() {
    	thirdToolbar.removeAll();
    	JToolBar cutOff = new JToolBar();
    	
    	cutOff.add(new JLabel("LeftCutOff"));
    	cutOff.add(leftCutOff);
    	cutOff.add(new JLabel("RightCutOff"));
    	cutOff.add(rightCutOff);
    	
    	JToolBar sd = new JToolBar();
    	sd.add(new JLabel("Know SD"));
    	sd.add(knownVariance);
        
    	if(whichIntervalSelected().equals(IntervalType.xbar_sigmaKnown) && !bootStrap){
    		
    	
    		thirdToolbar.add(sd);
    	}
        if(isProportionInterval())
        	thirdToolbar.add(cutOff);
        
        thirdToolbar.setVisible(true);
        thirdToolbar.validate();
        
        this.repaint();
    }
    
    private void assemblePanel(){
    	this.removeAll();
    	 JPanel CIgroup = new JPanel();
    	 CIgroup.setPreferredSize(new Dimension(450, 150));
         CIgroup.setLayout(new BoxLayout(CIgroup, BoxLayout.PAGE_AXIS));

         CIgroup.add(firstToolbar);
         CIgroup.add(secondToolbar);
         assembleThirdToolbar();
         CIgroup.add(thirdToolbar);
  
         CIgroup.setBorder(new TitledBorder(new EtchedBorder(),
                 "CI Settings"));
         this.add(new JScrollPane(CIgroup));
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

    public void addObserver(Observer observer) {
		observable.addObserver(observer);
	}
    
 
    public double getAlphaValue() {
        return ALPHA_VALUES[alphaChoice.getSelectedIndex()];
    }
    
    public double getLeftCutOffValue() {
        return left;
    }
    public double getRightCutOffValue() {
        return right;
    }
    
    public double getKnownVariance() {   
       return variance;
    }
    /**
     * Gets alpha information
     * 0-> alpha=0.05, 1-> alpha = 0.01 1-> p < mu0, and 2 -> p != mu0
     */
    public int getAlphaIndex() {
        return alphaChoice.getSelectedIndex();
    }

    public IntervalType whichIntervalSelected() {
        return (IntervalType) ciChoice.getSelectedItem();
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
    
    public boolean isMeanIntervalKnownVariance() {
        if (whichIntervalSelected().equals(IntervalType.xbar_sigmaKnown)) {
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
    
    public void update(Observable o, Object arg) {
        observable.notifyObservers();
    }


	public void actionPerformed(ActionEvent env) {
		if(env.getSource().equals(leftCutOff))
			left = new Double(leftCutOff.getText()).doubleValue();
		if(env.getSource().equals(rightCutOff))
			right = new Double(rightCutOff.getText()).doubleValue();
		if(env.getSource().equals(knownVariance)){
			 variance = new Double(knownVariance.getText()).doubleValue();
			 if (variance < 0){
				 knownVariance.setText("1");
				 variance = 1;
			 }
				 
		}
		
		if(env.getSource().equals(ciChoice)){
			//System.out.println("ci_choice changed");
			assemblePanel();
			observable.notifyObservers();
		}
		//System.out.println("controlPanel action performed called");
	}
	
	 IntervalType[] filter(IntervalType[] list){
			IntervalType[] temp = new IntervalType[list.length];
			int j=0;
			
			for (int i=0; i<list.length; i++ ){
			//	System.out.println("*"+list[i].toString()+"*");
				if (list[i].toString().length()!=0){
					temp[j]=list[i];
					j++;
				}
			}
			
		//	System.out.println("j="+j);
			IntervalType[] out = new IntervalType[j];
			for (int i=0; i<j; i++ )
				out[i]=temp[i];
				
			
			return out;
		}
}

