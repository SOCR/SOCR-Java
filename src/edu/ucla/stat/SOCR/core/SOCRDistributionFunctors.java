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
 * SOCRDistributionFunctors.java 
 * @author Ivo Dinov, Rahul Gidwani
 */
package edu.ucla.stat.SOCR.core;

import java.awt.event.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

/** This class implements the main interface for SOCRDistributions PDF/CDF/MGF's */
public class SOCRDistributionFunctors extends SOCRApplet3 implements ActionListener, Observer {

	private GraphPanels graphPanel; //right side part top part

	private SOCRTextArea statusTextArea = new SOCRTextArea(); //right side bottom part
	JSplitPane container;

	//current selected Distribution;
	protected Distribution dist;


	public static String ABOUT = "About" ;
	public static String HELP = "Help" ;
	public static String SNAPSHOT= "Snapshot"; // Ivo added this Snapshot capability, 10/25/05
	public JTextField leftCutOff, rightCutOff; // Ivo added this to allow the numeric setting of the left/right
	// limits of the probability calculations, in addition to
	// the already available manual mouse click/drag functionality
	public JLabel leftCutOffLabel;
	public JLabel rightCutOffLabel;

	private JFileChooser jfc;

	/* (non-Javadoc)
	 * @see edu.ucla.stat.SOCR.gui.SOCRApplet#getCurrentItem()
	 */
	public Object getCurrentItem() {
		return dist;
	}

	public void initGUI() {
		//configuration for fControlPanel:: leftside pane
		controlPanelTitle = "SOCR Distribution Functors";
		implementedFunctor = "implementedFunctors.txt";
		//implementedFile = "implementedDistributions.txt";        

		// Add all Buttons 
		addButton(ABOUT, "Learn More About This Distribution!", this);
		addButton(HELP, "How to Use the Distribution Applets?", this);
		addButton(SNAPSHOT, "Take a Snapshot and save this Applet as JPG image", this);

		// Add the Left/Right CutOff text fields
		leftCutOff = new JTextField(14);
		leftCutOffLabel = new JLabel("Left Cut Off");
		leftCutOffLabel.setLabelFor(leftCutOff);
		leftCutOff.setToolTipText("Left Cut Off for Computing Probability");
		leftCutOff.setActionCommand("leftCutOff");
		leftCutOff.addActionListener(this);
		addJTextField(leftCutOff, leftCutOffLabel);

		rightCutOff = new JTextField(14);
		rightCutOffLabel = new JLabel("Right Cut Off");
		rightCutOffLabel.setLabelFor(rightCutOff);
		rightCutOff.setToolTipText("Right Cut Off for Computing Probability");
		rightCutOff.setActionCommand("rightCutOff");
		rightCutOff.addActionListener(this);
		addJTextField(rightCutOff, rightCutOffLabel);

	}

	public void start() {
		container.setDividerLocation(0.6);
	}

	protected void itemChanged(String className) {
		try {

		//	System.err.println("Before Distribution.getInstance(className)");
			dist = Distribution.getInstance(className);
		//	System.err.println("After Distribution.getInstance(className)");
			
			dist.addObserver(this);
      	//	System.err.println("After addObserver(this)");
			
			dist.initialize();
		//	System.err.println("After initialize()");
			
			//update graphPanel
			graphPanel.setDistribution(dist);
			
			//to solve the discrepancy of valueSetter and the distribution
			//the following just let the slider notify observers
			try { 	ValueSetter vsetter = dist.getValueSetter(0);
			if ( vsetter != null)
				vsetter.setValue(vsetter.getValue());
			}
			catch (Exception e) {}
		} catch (Throwable e) {
			JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
			e.printStackTrace();
		}        
	}

	protected void functorChanged(String className)
	{
		try { 
			if (className.equals("edu.ucla.stat.SOCR.core.DistributionGraphPanel"))
				graphPanel = new DistributionGraphPanel(this);

			if (className.equals("edu.ucla.stat.SOCR.core.MGFGraphPanel"))
				graphPanel = new MGFGraphPanel(this);
			
			if (className.equals("edu.ucla.stat.SOCR.core.PGFGraphPanel"))
				graphPanel = new PGFGraphPanel(this);

			// if the container has been initialized no reason to re-intialize.
			if (container != null)
			{	
				container.setDividerLocation(0.6);
				container.setLeftComponent(graphPanel);
			}
			else // the container has not been initialized.
			{
				container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
						graphPanel, new JScrollPane(statusTextArea) );
				fPresentPanel.setViewportView(container);
			}
			implementedFile = graphPanel.getPanelFile();
			
		}
		catch(Throwable e) { System.out.println("Error in DistributionFunctors"); e.printStackTrace();}
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
				System.out.println("LeftCutOff set at: "+left);
				graphPanel.setLeftCutOff(left);
			} catch (NumberFormatException e) {
				double left = graphPanel.getLeftCutOff();
				leftCutOff.setText((new Double(left)).toString());
				JOptionPane.showMessageDialog(this, "You must enter a Double numeric value!!!");
				e.printStackTrace();
			}
		} 
		else if (evt.getActionCommand().equals("rightCutOff")) {
			try {
				double right = (new Double(rightCutOff.getText())).doubleValue();
				System.out.println("RightCutOff set at: "+right);
				graphPanel.setRightCutOff(right);
			} catch (NumberFormatException e) {
				double right = graphPanel.getRightCutOff();
				rightCutOff.setText((new Double(right)).toString());
				JOptionPane.showMessageDialog(this, "You must enter a Double numeric value!!!");
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


	/* The value of Distribution changed, so repaint itself
	 * just repaint is not enough, need updata domain and scale
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		graphPanel.setDistribution(dist);
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
		String between = "Between: " + format(graphPanel.getBetweenCDF());
		statusTextArea.setText(dist.getName()+"\n"+ mean + "\t\t\t" + left + '\n' + median +
				"\t\t\t" + between + '\n' + variance + "\t\t\t" + right +  '\n' + std + "\t\t\t" + maxDensity);            
	}
}
