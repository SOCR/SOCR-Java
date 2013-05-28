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
/**
 * SOCRDistributions.java 
 */

package edu.ucla.stat.SOCR.core;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

/** This class implements the main interface for SOCRDistributions */
public class SOCRDistributions extends SOCRApplet implements ActionListener, DocumentListener, Observer{

    private GraphPanel graphPanel; //right side part up part

    private SOCRTextArea statusTextArea = new SOCRTextArea(); //right side bottom part
    JSplitPane container;

    private boolean showGraph= true;
    private boolean showCutOffs= true;
   
    //current selected Distribution;
    protected Distribution dist;
    private String defaultSelectedDistribution = "Normal Distribution"; //Normal(0, 50^2)
    
    public boolean showAboutButtons= true;
    public static String ABOUT = "About" ;
    public static String HELP = "Help" ;
    public static String SNAPSHOT= "Snapshot"; // Ivo added this Snapshot capability, 10/25/05
    public JTextField leftCutOff, rightCutOff; // Ivo added this to allow the numeric setting of the left/right
								// limits of the probability calculations, in addition to
								// the already available manual mouse click/drag functionality
    public JLabel leftCutOffLabel;
    public JLabel rightCutOffLabel;
    
    private JFileChooser jfc;
    public Observable observable = new Observable() {
		public void notifyObservers() {
			setChanged();
			super.notifyObservers(SOCRDistributions.this);
		}
	};
    
    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.gui.SOCRApplet#getCurrentItem()
     */
    public Object getCurrentItem() {
        return dist;
    }
       
    public GraphPanel getGraphPanel(){
    	return graphPanel;
    }

    public SOCRTextArea getSOCRTextArea(){
    	return statusTextArea;
    }

	public void addObserver(Observer observer) {
		observable.addObserver(observer);
	}
	
	public void update(Observable o, Object arg) {
		graphPanel.setDistribution(dist);
		valueChanged(o, arg);
		//observable.notifyObservers();
	}

	public boolean isShowGraph(){
		return showGraph;
	}
	
	public void setShowGraph(boolean flag){
		showGraph = flag;
		if(!showGraph)
			container.setDividerLocation(0.05);
		else
			container.setDividerLocation(0.6);
		
		 container.repaint();
	}
	
	public void setShowCutOffs(boolean flag){
		jTextAreaPane.removeAll();
		if(flag){
			addJTextField(leftCutOff, leftCutOffLabel);
			addJTextField(rightCutOff, rightCutOffLabel);
		}
	}
	
	public void valueChanged() {}

	public void valueChanged(Observable o, Object arg) { 
		valueChanged();
		observable.notifyObservers();
		}
	
	public void setImplementedFile(String fileName){
		implementedFile= fileName;
	}
    public void initGUI() {
        //configuration for fControlPanel:: leftside pane
      controlPanelTitle = "SOCR Distributions";
      if (implementedFile==null ||implementedFile.length()==0)
    	  implementedFile = "implementedDistributions.txt";      
      
      // Add all Buttons 
      if (showAboutButtons){
	      addButton(ABOUT, "Learn More About This Distribution!", this);
	      addButton(HELP, "How to Use the Distribution Applets?", this);
	      addButton(SNAPSHOT, "Take a Snapshot and save this Applet as JPG image", this);
      }

       //right side pane
       graphPanel = new GraphPanel(this);
       container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                graphPanel, new JScrollPane(statusTextArea) );
        

       // Add the Left/Right CutOff text fields
       leftCutOff = new JTextField(14);
       leftCutOffLabel = new JLabel("Left Cut Off");
       leftCutOffLabel.setLabelFor(leftCutOff);
       leftCutOff.setToolTipText("Left Cut Off for Computing Probability");
       leftCutOff.setActionCommand("leftCutOff");
       leftCutOff.addActionListener(this);
       
       /* 10/16/2008
        * Commented the action of typing + number parsing of the Left/RIght CUt Off values
        * as this action was disallowing the user to enter manually appropriate cut off
        * values - the reason is that each key stroke was interpreted and typing 1-2-3-.-0
        * for the upper limit 123.0 would cause a problem if the lower limit is > 12!!!
        * 
        * leftCutOff.getDocument().addDocumentListener(this);
        */
       
       addJTextField(leftCutOff, leftCutOffLabel);

       rightCutOff = new JTextField(14);
       rightCutOffLabel = new JLabel("Right Cut Off");
       rightCutOffLabel.setLabelFor(rightCutOff);
       rightCutOff.setToolTipText("Right Cut Off for Computing Probability");
       rightCutOff.setActionCommand("rightCutOff");
       rightCutOff.addActionListener(this);
       
       /* 10/16/2008
        * Commented the action of typing + number parsing of the Left/RIght CUt Off values
        * as this action was disallowing the user to enter manually appropriate cut off
        * values - the reason is that each key stroke was interpreted and typing 1-2-3-.-0
        * for the upper limit 123.0 would cause a problem if the lower limit is > 12!!!
        * 
        * rightCutOff.getDocument().addDocumentListener(this);
        */

       addJTextField(rightCutOff, rightCutOffLabel);

       fPresentPanel.setViewportView(container);
       
       // 06/18/08 -- Set the Default Distribution to Normal(0, 50^2)
       setSelectedApplication(defaultSelectedDistribution);
   	   //System.out.println("(initGUI) defaultSelectedDistribution =" + 
       // defaultSelectedDistribution);
       
       statusTextArea.setToolTipText("The (red-shaded) BETWEEN, and (unshaded) LEFT and " +
    		"RIGHT areas show the probabilities P(LEFT<=X<=RIGHT), P(X<LEFT), "+
    		"P(X>RIGHT), respectively.");
       fControlPanel.setToolTipText("Select a Probability Distribution (drop-down list)"+
    		   ", if appropriate choose distribution parameters.");
    }
    
    public void start() {
    	
    	if(!showGraph)
   		 container.setDividerLocation(0.05);
    	else
    		container.setDividerLocation(0.6);
    }
    
    protected void itemChanged(String className) {
        try {
        	 // set flags back to default
            setShowCutOffs(true);
            setShowGraph(true);
            
            dist = Distribution.getInstance(className);
            dist.addObserver(this);
            dist.initialize();            
            fControlPanel.repaint();	// refresh CutOff etc
            
            //update graphPanel
            graphPanel.setDistribution(dist);
           
        	observable.notifyObservers();
            //to solve the discrepancy of valueSetter and the distribution
            //the following just let the slider notify observers
            try { 	ValueSetter vsetter = dist.getValueSetter(0);
            		if ( vsetter != null)
                		vsetter.setValue(vsetter.getValue());
            		//ComponentSetter csetter = dist.getComponentSetter(0);
            		//if ( csetter != null)
                		//csetter.setToolTipText("Component");
            }
            catch (Exception e) {}
            //comment out the null setting for left cut off and right cutoff Jenny Cui 6/23/09
            // left cut off/right cutoff value are needed by CI experiment
           // leftCutOff.setText(null);
           // rightCutOff.setText(null);
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
            e.printStackTrace();
        }        
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(ABOUT)) {
            try {
                getAppletContext().showDocument(
                new java.net.URL(dist.getOnlineDescription()), 
                   "SOCR: Distribution Online Help (Mathematica)");
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                e.printStackTrace();
            }
        } 

        else if (evt.getActionCommand().equals(HELP)) {
            try {
                JOptionPane.showMessageDialog(this, dist.getLocalHelp());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                e.printStackTrace();
            }
        } 
      
		// Handle the Numeric changes of the leftCutOff and rightCutOff for the prob calculation
        else if (evt.getActionCommand().equals("leftCutOff")) {
            try {
			double left = (new Double(leftCutOff.getText())).doubleValue();
                //	System.out.println("LeftCutOff set at: "+left);
                	graphPanel.setLeftCutOff(left);
            } catch (NumberFormatException e) {
                	double left = graphPanel.getLeftCutOff();
                	leftCutOff.setText((new Double(left)).toString());
                	JOptionPane.showMessageDialog(this, 
                			"You must enter a Double numeric value!!!");
                	e.printStackTrace();
            }
        } 
        else if (evt.getActionCommand().equals("rightCutOff")) {
            try {
			double right = (new Double(rightCutOff.getText())).doubleValue();
                //	System.out.println("RightCutOff set at: "+right);
                	graphPanel.setRightCutOff(right);
            } catch (NumberFormatException e) {
                	double right = graphPanel.getRightCutOff();
                	rightCutOff.setText((new Double(right)).toString());
                	JOptionPane.showMessageDialog(this, 
                			"You must enter a Double numeric value!!!");
                	e.printStackTrace();
            }
        } 
        else if (evt.getActionCommand().equals(SNAPSHOT)) {
      		SwingUtilities.invokeLater( new Runnable() {
        	      	java.awt.image.BufferedImage image;
			      java.io.File f;
			      String type;

			public void run() {
                	  image = capture();

                	  if (jfc==null) jfc = new JFileChooser();
			  else jfc.setVisible(true);
      			  int option = jfc.showSaveDialog(null);
			  f = jfc.getSelectedFile();
			  jfc.setVisible(false);

			  //System.out.println("image " + image);
			  // Fix the problems with users not entering proper file extensions (.gif or .jpg)
                	  if (!f.getName().endsWith(".jpg"))
				 f = new java.io.File(f.getAbsolutePath()+ ".jpg");

                	  type = f.getName().substring(f.getName().lastIndexOf('.') + 1);	
                	  System.out.println("type " + type);
			
                    try {
                	      javax.imageio.ImageIO.write(image,type,f);
                	   } catch (java.io.IOException ioe) {
                	      ioe.printStackTrace();
        	      	      JOptionPane.showMessageDialog(null,
					ioe,"Error Writing File",JOptionPane.ERROR_MESSAGE);
                	   }
          	      }
          	  });
         }
    }
    

	/** This is a method used to capture the images of the applet for saving as JPG
	*/
    private java.awt.image.BufferedImage capture() {

	java.awt.Robot robot;
	
	try {
            robot = new java.awt.Robot();
        } catch (java.awt.AWTException e) {
            throw new RuntimeException(e);
        }

	java.awt.Rectangle screen = this.getContentPane().getBounds();
      	java.awt.Point loc = screen.getLocation();
      	SwingUtilities.convertPointToScreen(loc,this.getContentPane());
	screen.setLocation(loc);
      	return robot.createScreenCapture(screen);
     }

    
    /** updates the collected information of distribution */
    public void updateStatus() {   
        if (dist == null) return;
        String mean = "Mean: " + format(dist.getMean());
        String median = "Median: " + format(dist.getMedian());
        String variance = "Variance: " + format(dist.getVariance());
        String std = "Standard Deviation: " + format(dist.getSD());
        String maxDensity = "Max Density: " + format(dist.getMaxDensity());
        String left = "Left: " + format(graphPanel.getLeftCDF());
        String right = "Right: " + format(graphPanel.getRightCDF());
        String between = "Between (Red-Shaded): " + format(graphPanel.getBetweenCDF());
        statusTextArea.setText("Distribution Properties" + "\t\t\t Probabilities\n" +
        	dist.getName()+"\n"+ mean + "\t\t\t" + left + '\n' + median +
            "\t\t\t" + between + '\n' + variance + "\t\t\t" + right +  '\n' + std + "\n" + 
            maxDensity);           
    }
    
    /*
     * This method allows Setting the default distribution from the list of all SOCR Distributions
     */
    public void setDefaultSelectedDistribution(String distName) {
    	defaultSelectedDistribution = distName;
    }
    /*
     * This method allows Getting the default distribution from the list of all SOCR Distributions
     */
    public String getDefaultSelectedDistribution() {
    	return defaultSelectedDistribution;
    }

    public void insertUpdate(DocumentEvent e) {
		textChanged(e);
	}
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);
	}
	public void changedUpdate(DocumentEvent e) {
		//Plain text components do not fire these events
	}

	public void textChanged(DocumentEvent evt) {
		//System.out.println(evt.getKeyChar()+" pressed for "+evt.getID());
        if (evt.getDocument().equals(leftCutOff.getDocument())){
            try {
                if(leftCutOff.getText().equals("") || leftCutOff.getText().equals("-"))
                    return;

                double left = (new Double(leftCutOff.getText())).doubleValue();
                        //System.out.println("LeftCutOff set at: "+left);
                	graphPanel.setLeftCutOff(left);
            } catch (NumberFormatException e) {
                        double left = graphPanel.getLeftCutOff();
                        //leftCutOff.setText((new Double(left)).toString());
                        JOptionPane.showMessageDialog(this, 
                        		"You must enter a Double numeric value!!!");
                        e.printStackTrace();
            }
        }else if (evt.getDocument().equals(rightCutOff.getDocument())){
            try {
                if(rightCutOff.getText().equals("") || rightCutOff.getText().equals("-"))
                    return;

                double right = (new Double(rightCutOff.getText())).doubleValue();
                        //System.out.println("RightCutOff set at: "+right);
                	graphPanel.setRightCutOff(right);
            } catch (NumberFormatException e) {
                        double right = graphPanel.getRightCutOff();
                        //rightCutOff.setText((new Double(right)).toString());
                        JOptionPane.showMessageDialog(this, 
                        		"You must enter a Double numeric value!!!");
                        e.printStackTrace();
            }
        }
    }
}