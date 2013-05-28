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

package edu.ucla.stat.SOCR.core;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

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
public class ValueSetter extends JPanel
{
    private int type;

    private Observable observable = new Observable()
    {
        public void notifyObservers()
        {
            setChanged();
            super.notifyObservers(ValueSetter.this);
        }
    };

    /**
     * @uml.property name="dvalue"
     */
    private double dvalue; //current selected value

    private double minv;
    private double maxv;
    private double scale = 1; //only used for CONTINUOUS type
    private DecimalFormat formatter = new DecimalFormat("#0.0##");

    /**
     * @uml.property name="slider"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JSlider slider;

    /**
     * @uml.property name="valueText"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private JTextField valueText = new JTextField(5);

    private boolean enterTyped = false; //used to determin how the change caused

    /**
     *  create a Continuous type valueSetter, the real value will be multiply by
     * the scale
     *
     * @param title
     * @param min
     * @param max
     * @param scale the real min is (min * scale)
     */
    public ValueSetter(String title, int min, int max, double scale)
    {
        this(title, min, max, (min + max)/2, scale);
    }

    /**
     * create a Continuous type valueSetter,
     */
    public ValueSetter(String title, int min, int max, int initial, double scale)
    {
        this.scale = scale;
        type = Distribution.CONTINUOUS;
        constructing(title, min, max, initial);
    }

    public ValueSetter(String title, int t, int min, int max)
    {
        this(title, t, min, max, (min+ max)/2);
    }

    public ValueSetter(String title, int t, int min, int max, int initial)
    {
        type = t;
        if (t == Distribution.CONTINUOUS && (max - min) < 10)
        {
            max *= 10;
            min *= 10;
            initial *= 10;
            scale = 0.1;
        }
        constructing(title, min, max, initial);
    }

    private void constructing(String title, int min, int max, int initial)
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeSlider(min, max, initial);
        setTextAreaValue();

        add(slider);
        add(Box.createVerticalStrut(8));
        add(valueText);

        //create border
        TitledBorder tb = new TitledBorder(new EtchedBorder(), title);
        setBorder(tb);

        valueText.setMaximumSize(valueText.getPreferredSize());
        valueText.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e)
            {
                textAreaPeformAction();
            }

            public void removeUpdate(DocumentEvent e)
            {
                textAreaPeformAction();
            }

            public void changedUpdate(DocumentEvent e)
            {
                //Plain text components do not fire these events
            }
        });

    }

    private void initializeSlider(int min, int max, int initial)
    {
        dvalue = initial;
        this.minv = min;
        this.maxv = max;
        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        if (type == Distribution.DISCRETE)
        {
            labels.put( new Integer(min), new JLabel(String.valueOf(min)));
            labels.put( new Integer(max), new JLabel(String.valueOf(max)));
        }
        else
        {
            labels.put(new Integer(min), new JLabel(formatter.format(min*scale)));
            labels.put(new Integer(max), new JLabel(formatter.format(max*scale)));
            dvalue *= scale;
        }

        slider = new JSlider( min, max, initial);
        
        /* Ivo updated these according to an email discussion with Jameel
         * IntValueSetter class was for the following reasons:
		 * 1) setMajorTickSpacing and setMinorTickSpacing take integers as their parameter
		 * 2) When you have max = 500 and min = 2 (or anything not 0-a multiple of 10) 
		 * then you'll get a double that will be truncated to an int. 
		 * This would result in very weird looking spacing. 
		 * For example, (500-2)/10 = 49.8 and (500-2)/20 = 24.9. 
		 * The spacings here should clearly be 50 and 25, but they were set to 
		 * 49 and 24 before, thus the major and minor ticks began to converge 
		 * near the end of the slider.
         */
        slider.setMajorTickSpacing(Math.round((max - min)/10.0f));
        slider.setMinorTickSpacing(Math.round((max - min)/20.0f));
        
        slider.setPreferredSize(new Dimension(70,50));
        slider.setPaintTicks(true);

        slider.setSnapToTicks(true);
        slider.setLabelTable( labels );
        slider.setPaintLabels(true);

        slider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                if (enterTyped) return;
                dvalue = slider.getValue() * scale;
                setTextAreaValue();
            }
        });
    }

    public void addObserver(Observer o)
    {
        observable.addObserver(o);
    }

    /**
     * @uml.property name="dvalue"
     */
    public double getValue()
    {
        return dvalue;
    }

    public int getValueAsInt()
    {
        return (int) dvalue;
    }

    public void setValue(double v)
    {
        String nv = type == Distribution.DISCRETE ? String.valueOf((int)v) :
            String.valueOf(v);
        valueText.setText(nv);
        textAreaPeformAction();
    }

    public void setRange(int min, int max)
    {
        setRange(min, max, (min+max)/2);
    }

    public void setRange(int min, int max, int v)
    {
        if (min >= max) return;
        if (v < min) v = min;
        else if (v > max) v = max;
        remove(slider);
        initializeSlider(min, max, v);
        add(slider, 0);
        revalidate();
        repaint();
    }

    public void setTitle(String title)
    {
        ((TitledBorder)getBorder()).setTitle(title);
    }

    public void setEnabled(boolean b)
    {
        slider.setEnabled(b);
        valueText.setEnabled(b);
        super.setEnabled(b);
    }

    private void setTextAreaValue()
    {
        String ov = valueText.getText();
        String nv = type == Distribution.DISCRETE ? String.valueOf((int)dvalue) :
            formatter.format(dvalue);
        if (ov.equals(nv)) return;
        valueText.setText(nv);
        observable.notifyObservers();
    }

    private void setSliderValue()
    {
        slider.setValue((int)(dvalue / scale));
    }

    /**
     *  do what needed after the text area hit a return
     */
    private void textAreaPeformAction()
    {
        try
        {
            if (valueText.getText().equals(""))
            {
                return;
            }

            try { 
            	dvalue = Double.parseDouble(valueText.getText());
            } catch (Exception e) {
            	return;
            }

            //JSliders only support integer labels. Only values that can be converted to integers can be accepted.
            if(dvalue < Integer.MIN_VALUE || dvalue > Integer.MAX_VALUE)
            {
                JOptionPane.showMessageDialog(this,
                    "Values entered must be between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + ".",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
                valueText.selectAll();
                return;
            }

            // 10/28/05 (Juana Sanchez requested a change!)
	    // Ivo Commented out to fix the problem with user
	    // not being able to freely select the values
	    //(if these make statistical sense)
	    // if (dvalue < minv || dvalue > maxv) {
            if (dvalue < minv && minv >= 0)
            {
                valueText.selectAll();
                return;
            }

	    if (dvalue < minv) setRange((int)dvalue, (int)maxv, (int)dvalue);
	    else if (dvalue > maxv) setRange((int)minv, (int)dvalue, (int)dvalue);

            enterTyped = true;
            setSliderValue();
            observable.notifyObservers();
            enterTyped = false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueText.selectAll();
        }
    }
}