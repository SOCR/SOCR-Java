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

import java.awt.Dimension;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class PercentSlider extends FloatSlider{

	    /**
	     * uses default visible(20) and resolution(1000).
	     */
	    public PercentSlider(String title, double cur, double min, double max) {
	        super(title,  cur, DEFAULT_VISIBLE, min, max, DEFAULT_RANGE);
	    }
	    
	    public void setAll(double newmin, double newmax, double newcur) {
	        minFloat = newmin;
	        maxFloat = newmax;
	        formatter = new DecimalFormat("#0");
	        setFloatValue(newcur);
	        
	        removeAll();
	        
	        lLabel= new JLabel(formatter.format(minFloat)+"%");
	        uLabel= new JLabel(formatter.format(maxFloat)+"%");
	        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	        
	        valueText = new JTextField(5);
	        valueText.setMaximumSize(valueText.getPreferredSize());
	        valueText.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent arg0) {
	    			textAreaPeformAction();
	    		}
	    	});
	        valueText.setText(formatter.format(curFloat)+"%");
	        
	        add(lLabel);
	        add(slider);
	        add(uLabel);
	        add(Box.createHorizontalStrut(8));
	        add(valueText);
	        TitledBorder tb = new TitledBorder(new EtchedBorder(), title);
	        setBorder(tb);
	        validate();
	    }
	    
	    public void setTextAreaValue(double v) {
	    	
	       // String nv = String.valueOf(v);
	     //   System.out.println("setting text value "+nv);
	    	if (valueText==null || formatter==null )
	    		return;
	        valueText.setText(formatter.format(v)+"%");
	        textAreaPeformAction();
	    }
	    

	 
	    /**
	     *  do what needed after the text area hit a return
	     */
	    private void textAreaPeformAction() {
	        try {
	        	//System.out.println("textArea="+valueText.getText());
	        	if(valueText.getText().indexOf('%')!=-1)
	        		curFloat = Double.parseDouble(valueText.getText().substring(0,valueText.getText().indexOf('%')));
	        	else curFloat = Double.parseDouble(valueText.getText());
	        	//System.out.println("curFloat="+curFloat);
		    if (curFloat < minFloat) {
		    	setFloatMinimum(curFloat);
		    }
		    else if (curFloat > maxFloat) {
		    	setFloatMaximum(curFloat);
		    }

	            enterTyped = true;
	            setFloatValue(curFloat);
	            observable.notifyObservers();
	            enterTyped = false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            valueText.selectAll();
	        }
	    }

	}
