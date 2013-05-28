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
package edu.ucla.stat.SOCR.util;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import edu.ucla.stat.SOCR.core.Distribution;
/**
 * this class used to let the user change value
 * <p>
 * It compose of two parts, a JSlider and a TextField.
 * The TextField used to allow user type value directly.
 *
 * When value changed, it will notify any registered Observers.
 * The Observers can get the value from getValue() or from getValueAsInt().
 * It also allow to set a new value by setValue().
 *
 * It has setTitle(), setRange(), setValue() methods to provide similar
 * functions as Original Scrooler
 *
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class ValueSlider extends JPanel {
   // private int type;

    private Observable observable = new Observable() {
        public void notifyObservers() {
            setChanged();
            super.notifyObservers(ValueSlider.this);
        }
    };

    /**
     *
     * @uml.property name="dvalue"
     */
    private int dvalue; //current selected value

    private int minv;
    private int maxv;
    private double scale = 1; 
    private DecimalFormat formatter = new DecimalFormat("#0.0##");
    private boolean minimumRange10;  // mininmum range is 10 to force the ticks to show up

    /**
     *
     * @uml.property name="slider"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JSlider slider;

    /**
     *
     * @uml.property name="valueText"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JTextField valueText = new JTextField(5);

    private boolean enterTyped = false; //used to determin how the change caused
  
    /**
     * create a Continuous type valueSlider,
     */
    public ValueSlider(String title, double min, double max, double initial, boolean minimumRange10) {
       this.minimumRange10 = minimumRange10;
        constructing(title, min, max, initial);
    }

    public ValueSlider(String title,double min, double max, boolean minimumRange10) {
   
        this(title, min, max, (min+ max)/2, minimumRange10);
    }

    private void constructing(String title, double min, double max, double initial) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeSlider(min, max, initial);
        add(slider);
        add(Box.createVerticalStrut(8));
        add(valueText);

        //create border
        TitledBorder tb = new TitledBorder(new EtchedBorder(), title);
        setBorder(tb);

        valueText.setMaximumSize(valueText.getPreferredSize());
        valueText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                textAreaPeformAction();
            }
        });
    }

    private void initializeSlider(double min, double max, double initial) {
        
  //  	System.out.println("min="+min+ " max="+max+ " initial=" +initial);
        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
      
        if (Math.abs((int)max)<=1){
          scale= 10;
		 for (int i=1; i<5; i++){
			
		 if ( Math.abs(max *scale) >=1){
		    break;
		    }
		 else scale*=10;
		 }	
        }
        
        	// take off one digit;
        	//    max = (int)(max*scale)/scale;
        	//     min = (int)(min*scale)/scale;
        	//     initial = (int)(initial*scale)/scale;
        	//     scale*=10;  //10 times more to make a better ticks
        
        dvalue = (int)(initial*scale);
        this.minv = (int)(min*scale);
      
        this.maxv = (int)(max*scale);
        	//      System.out.println("minimum10="+minimum10);
        
        if (minimumRange10){ // in order to show ticks
        		if (maxv-minv<10)   
        			maxv = minv+10;
        }
        
        labels.put(new Integer(minv), new JLabel(formatter.format(minv/scale)));
        labels.put(new Integer(maxv), new JLabel(formatter.format(maxv/scale)));
  
      // System.out.println("minv="+minv+" maxv="+maxv+ "  dvalue= "+dvalue+ " scale="+scale);
    
        slider = new JSlider( minv, maxv, dvalue);
        slider.setMajorTickSpacing((maxv - minv)/10);
        slider.setMinorTickSpacing((maxv - minv)/20);
        slider.setPreferredSize(new Dimension(70,50));
        slider.setPaintTicks(true);

        slider.setSnapToTicks(true);
        slider.setLabelTable( labels );
        slider.setPaintLabels(true);

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (enterTyped) return;
                dvalue = (int)(slider.getValue());
               // System.out.println("dvalue="+dvalue);
                setTextAreaValue();
            }
        });
        setTextAreaValue();
    }

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    /**
     *
     * @uml.property name="dvalue"
     */
    public double getValue() {
   // 	System.out.println("VS:::getValue="+dvalue);
        return dvalue/scale;
    }

    public int getValueAsInt() { 
    		return dvalue; 
    	}

    public void setValue(double v) {
        String nv = String.valueOf(v);
        valueText.setText(nv);
        textAreaPeformAction();
    }

    public void setRange(double min, double max){
  //  	System.out.println("VS:::range="+min+" "+max);
        setRange(min, max, (min+max)/2);
    }

    public void setRange(double min, double max, double v){
        if (min >= max) return;
        if (v < min) v = min;
        else if (v > max) v = max;
        remove(slider);
        initializeSlider(min, max, v);
        add(slider, 0);
        revalidate();
        repaint();
    }

    
    public void setTitle(String title) {
        ((TitledBorder)getBorder()).setTitle(title);
    }

    public void setEnabled(boolean b) {
        slider.setEnabled(b);
        valueText.setEnabled(b);
        super.setEnabled(b);
    }

    private void setTextAreaValue() {
        String ov = valueText.getText();
        String nv = formatter.format(dvalue/scale);
        if (ov.equals(nv)) return;
        valueText.setText(nv);
        observable.notifyObservers();
    }

    private void setSliderValue() {
        slider.setValue(dvalue);
    }

    /**
     *  do what needed after the text area hit a return
     */
    private void textAreaPeformAction() {
        try {
            dvalue = (int)(Double.parseDouble(valueText.getText())*scale);
            // 10/28/05 (Juana Sanchez requested a change!)
	    // Ivo Commented out to fix the problem with user
	    // not being able to freely select the values
	    //(if these make statistical sense)
	    // if (dvalue < minv || dvalue > maxv) {
	    if (dvalue < minv && minv>=0) {
                valueText.selectAll();
                return;
            }

	    if (dvalue < minv) setRange((int)dvalue, (int)maxv, (int)dvalue);
	    else if (dvalue > maxv) setRange((int)minv, (int)dvalue, (int)dvalue);

            enterTyped = true;
            setSliderValue();
            observable.notifyObservers();
            enterTyped = false;
        } catch (Exception e) {
            e.printStackTrace();
            valueText.selectAll();
        }
    }
}
