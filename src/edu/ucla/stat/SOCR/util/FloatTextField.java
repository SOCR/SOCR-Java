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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class FloatTextField extends JPanel{
	   // private int type;

	 private Observable observable = new Observable() {
	        public void notifyObservers() {
	            setChanged();
	            super.notifyObservers(FloatTextField.this);
	        }
	    };
	    
	 private final static int DEFAULT_RANGE = 1000; // number of descrete steps
	 private final static int DEFAULT_VISIBLE = 20; // pixel width of thumb
	 private double curFloat, minFloat, maxFloat;
	 private boolean isLogScale= true; 
	    
	// private Scrollbar slider;
	//private JSlider slider;
	 private JTextField valueText = new JTextField(5);
	 private DecimalFormat formatter = new DecimalFormat("#0.0##");
	 
	
	    
	 public void addObserver(Observer o) {		
	        observable.addObserver(o);
	 }
	    
	
	    
	 /*   private double dvalue; //current selected value

	    private double minv;
	    private double maxv;
	    private final static int RESOLUTION = 10; 
	    private double scale = 1; 
	    private DecimalFormat formatter = new DecimalFormat("#0.0##");
	    private boolean minimumRange10;  // mininmum range is 10 to force the ticks to show up
*/
	    /**
	     *
	     * @uml.property name="slider"
	     * @uml.associationEnd multiplicity="(1 1)"
	     */
	  
	    private boolean enterTyped = false; //used to determin how the change caused
	  
	    /**
	     * constructs a FloatTextField using a given number of slider positions.
	     * @param orientation - Scrollbar.VERTICAL or Scrollbar.HORIZONTAL.
	     * @param cur - real valued initial value.
	     * @param vis - same as in Scrollbar base class.
	     * @param min - real valued range minimum.
	     * @param max - real valued range maximum.
	     * @param resolution - number of descrete slider positions.
	     * @param log - log scale if true, linear otherwise.
	     */
	    public FloatTextField(String title,  double cur, int vis, double min, double max, int res, boolean log) {
	      //  slider = new Scrollbar(orientation, 0, vis, 0, res+vis);
	    	
	    	initializeSlider(min, max, cur);
	    	initializeTextArea(cur);
	    	
	        isLogScale = log;
	        setAll(min, max, cur);
	      /*  addAdjustmentListener(new AdjustmentListener() {
	            public void adjustmentValueChanged(AdjustmentEvent ae) {
	                int 
	                    ival = slider.getValue(),
	                    vis = slider.getVisibleAmount(),
	                    min = slider.getMinimum(),
	                    max = slider.getMaximum();
	                double dval = transformRange(false,      min,      max-vis,  ival,
	                                             isLogScale, minFloat, maxFloat);
	                //System.out.println("getting: ival="+ival+" -> dval="+dval);
	                setFloatValue(dval);     
	            }
	        });*/
	        
	        JLabel lLabel = new JLabel(formatter.format(minFloat));
	        JLabel uLabel= new JLabel(formatter.format(maxFloat));
 	       
	        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	        
	        add(lLabel);
	      //  add(Box.createHorizontalStrut(8));
	        add(valueText);
	        add(uLabel);
	        TitledBorder tb = new TitledBorder(new EtchedBorder(), title);
	        setBorder(tb);
	        

	    }
	    /**
	     * uses default scale (linear).
	     */
	    public FloatTextField(String title,  double cur, int vis, double min, double max, int res) {
	        this(title, cur, vis, min, max, res, false);
	    }
	    /**
	     * uses default visible(20) and resolution(1000).
	     */
	    public FloatTextField(String title, double cur, double min, double max, boolean log) {
	        this(title,  cur, DEFAULT_VISIBLE, min, max, DEFAULT_RANGE, log);
	    }
	    /**
	     * uses default visible(20), resolution(1000), and scale (linear).
	     */
	    public FloatTextField(String title,  double cur, double min, double max) {
	        this(title,  cur, DEFAULT_VISIBLE, min, max, DEFAULT_RANGE, false);
	    }

	    public FloatTextField(String title,  double min, double max) {
	        this(title, min, DEFAULT_VISIBLE, min, max, DEFAULT_RANGE, false);
	    }
	    private void initializeTextArea( double cur){
	    	valueText.setMaximumSize(valueText.getPreferredSize());
	    	 valueText.addActionListener(new ActionListener() {
	             public void actionPerformed(ActionEvent arg0) {
	                 textAreaPeformAction();
	             }
	         });
	    	valueText.setText(String.valueOf(cur));
	    }
	    
	    private void initializeSlider( double min, double max, double initial) {
	      
	    	  //  	System.out.println("min="+min+ " max="+max+ " initial=" +initial);
	    	        Hashtable labels = new Hashtable();
	    	      
	    	       int scale= DEFAULT_RANGE;
	    	        
	    	      /*  if (Math.abs((int)max)<=1){
	    	          scale= DEFAULT_RANGE;
	    			 for (int i=1; i<5; i++){
	    				
	    			 if ( Math.abs(max *scale) >=1){
	    			    break;
	    			    }
	    			 else scale*=DEFAULT_RANGE;
	    			 }	
	    	        }*/
	    	        
	    	        // take off one digit;
	    	        //    max = (int)(max*scale)/scale;
	    	        //     min = (int)(min*scale)/scale;
	    	        //     initial = (int)(initial*scale)/scale;
	    	        //     scale*=10;  //10 times more to make a better ticks
	    	        
	    	        this.curFloat = initial;
	    	        this.minFloat = min;
	    	        this.maxFloat = max;
	    	      
	    	        //      System.out.println("minimum10="+minimum10);
	    	      /*  if (minimumRange10){ // in order to show ticks
	    	        		if (maxv-minv<10)   
	    	        			maxv = minv+10;s
	    	        }*/
	    	        
	    	    
	    	  
	    	      // System.out.println("minv="+minv+" maxv="+maxv+ "  dvalue= "+dvalue+ " scale="+scale);
	    	    
	    	      /*  slider = new Scrollbar(Scrollbar.HORIZONTAL,  0, DEFAULT_VISIBLE, 0, DEFAULT_RANGE);
	    	        
	    	        slider.addAdjustmentListener(new AdjustmentListener() {
	    	            public void adjustmentValueChanged(AdjustmentEvent ae) {
	    	                int 
	    	                    ival = slider.getValue(),
	    	                    vis = slider.getVisibleAmount(),
	    	                    min = slider.getMinimum(),
	    	                    max = slider.getMaximum();
	    	                double dval = transformRange(false,      min,      max-vis,  ival,
	    	                                             isLogScale, minFloat, maxFloat);
	    	                //System.out.println("getting: ival="+ival+" -> dval="+dval);
	    	                setFloatValue(dval);  
	    	                setTextAreaValue(dval);
	    	            }
	    	        });*/
	    	       /* slider = new JSlider((int)minFloat*scale, (int)maxFloat*scale, (int)curFloat*scale);
	    	       slider.setMajorTickSpacing((int)((maxFloat - minFloat)/10));
	    	        slider.setMinorTickSpacing((int)((maxFloat - minFloat)/20));
	    	        slider.setPreferredSize(new Dimension(70,50));
	    	        slider.setPaintTicks(true);

	    	        slider.setSnapToTicks(true);
	    	        slider.setLabelTable( labels );
	    	        System.out.println("initializeSlider here2");  
	    	        slider.setPaintLabels(true);

	    	        slider.addChangeListener(new ChangeListener() {
	    	            public void stateChanged(ChangeEvent e) {
	    	                if (enterTyped) return;
	    	                curFloat = (double)slider.getValue()/DEFAULT_RANGE;
	    	               // System.out.println("dvalue="+dvalue);
	    	                setTextAreaValue();
	    	            }
	    	        });*/
	    	        setTextAreaValue();

	    	    }

	    
	    
	  /*  protected int rangeValue(double dval) {
	        dval = clamp(dval, minFloat, maxFloat);
	        
	        int ival = (int)Math.round(
	                   transformRange(isLogScale, minFloat, maxFloat, dval,
	                                  false,      min,      max-vis));
	        //System.out.println("setting: dval="+dval+" -> ival="+ival);
	        return ival;
	    }*/

	    public double getFloatMinimum() {
	        return minFloat;
	    }
	    public double getFloatMaximum() {
	        return maxFloat;
	    }
	    public double getFloatValue() {
	        return curFloat;
	    }

	    public void setFloatMinimum(double newmin) {
	        setAll(newmin, maxFloat, getFloatValue());
	    }
	    public void setFloatMaximum(double newmax) {
	        setAll(minFloat, newmax, getFloatValue());
	    }
	    public void setFloatValue(double newcur) {
	        // update the model
	        curFloat = newcur;
	        // update the view
	       // setTextAreaValue(curFloat);
	    }
	    
	    public void setAll(double newmin, double newmax, double newcur) {
	        minFloat = newmin;
	        maxFloat = newmax;
	        setFloatValue(newcur);
	    }

	    private static double clamp(double x, double a, double b)
	    {
	        return x <= a ? a :
	               x >= b ? b : x;
	    }
	    // linear interpolation
	    private static double lerp(double a, double b, double t)
	    {
	        return a + (b-a) * t;
	    }
	    // geometric interpolation
	    private static double gerp(double a, double b, double t)
	    {
	        return a * Math.pow(b/a, t);
	    }
	    // interpolate between A and B (linearly or geometrically)
	    // by the fraction that x is between a and b (linearly or geometrically)
	    private static double
	    transformRange(boolean isLog, double a, double b, double x,
	                   boolean IsLog, double A, double B)
	                   
	    {              
	        if (isLog)
	        {
	            a = Math.log(a);
	            b = Math.log(b);
	            x = Math.log(x);
	        } 
	        double t = (x-a) / (b-a);
	        double X = IsLog ? gerp(A,B,t)
	                         : lerp(A,B,t);
	        return X;
	    }
	    
	    
	    
	    /**
	     * create a Continuous type valueSlider,
	     */
	  /*  public ValueSliderFloat2(String title, double min, double max, double initial, boolean minimumRange10) {
	       this.minimumRange10 = minimumRange10;
	        constructing(title, min, max, initial);
	    }

	    public ValueSliderFloat2(String title, double min, double max, boolean minimumRange10) {
	   
	        this(title, min, max, (min+ max)/2, minimumRange10);
	    }*/

	  /*  private void constructing(String title, double min, double max, double initial) {
	        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	        initializeSlider(min, max, initial);
	        add(slider);
	        add(Box.createHorizontalStrut(8));
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
	    }*/

	 
	    
	    public void setTextAreaValue(double v) {
	    	
	        String nv = String.valueOf(v);
	     //   System.out.println("setting text value "+nv);
	        valueText.setText(nv);
	        textAreaPeformAction();
	    }
	    
	    private void setTextAreaValue() {
	        String ov = valueText.getText();
	        String nv = formatter.format(curFloat);
	        if (ov.equals(nv))
	        	return;
	        valueText.setText(nv);
	        observable.notifyObservers();
	    }

	 
	    /**
	     *  do what needed after the text area hit a return
	     */
	    private void textAreaPeformAction() {
	        try {
	        	curFloat = Double.parseDouble(valueText.getText());

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
	    
	/*	public void adjustmentValueChanged(AdjustmentEvent arg0) {
             int 
             ival = slider.getValue(),
             vis = slider.getVisibleAmount(),
             min = slider.getMinimum(),
             max = slider.getMaximum();
         double dval = transformRange(false,      min,      max-vis,  ival,
                                      isLogScale, minFloat, maxFloat);
         setFloatValue(dval);     
  
		}*/
	}
