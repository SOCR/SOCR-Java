/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.experiments;
import java.text.DecimalFormat;
import java.util.Observable;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.Timer;

import java.awt.*;
import java.io.Serializable;
import java.awt.datatransfer.Clipboard;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import javax.swing.event.ChangeListener;

import edu.ucla.stat.SOCR.core.*;

/**
* This class defines a basic, discrete time stochastic process that can be subclassed.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Experiment extends JApplet implements ActionListener, ItemListener,
	MouseListener, MouseMotionListener, WindowListener, ChangeListener, Serializable, IExperiment{
	//Variables
	public int time = 0, updateCount = 0, stopCount = 0, updateFreq = 1, stopFreq = 10, toolIndex = 6;
	public boolean stopNow = false;
	//Panels
	private JToolBar toolBar = new JToolBar("Main Toolbar");
	protected JPanel toolBars = new JPanel(), componentPanel = new JPanel();
	//Buttons
	private JButton stepButton = new JButton(), runButton = new JButton(), stopButton = new JButton(),
		resetButton = new JButton(), aboutButton = new JButton();
	//Update choices
	private JComboBox updateChoice = new JComboBox();
	public JComboBox stopChoice = new JComboBox();
	//JDialog
	private Frame frame;
	//Timer
	public Timer timer = new Timer(10, this);
	//Format
	private DecimalFormat decimalFormat = new DecimalFormat();
	//Component panel layout
	private GridBagLayout componentLayout = new GridBagLayout();
	private GridBagConstraints constraints = new GridBagConstraints();
	//Colors
	public final static Color RED = Color.red, GREEN = new Color(0, 180, 0);
	protected JApplet applet;
	 
	protected boolean showModelDistribution = true;
	
	/**
	* This method initializes the experiment, by setting up the basic simulation
	* buttons (step, run, stop, reset), and the update and stop choices on the main
	* toolbar.  The record table that records the results of the experiment is initialized.
	*/
	public void init(){
		setName("Random Experiment");
		//Frame
		frame = getFrame(this);
		//Buttons
		//Step button
		stepButton.setToolTipText("Single Step");
		stepButton.setIcon(new ImageIcon(getClass().getResource("step.gif")));
		stepButton.addActionListener(this);
		//Run button
		runButton.setToolTipText("Run");
		runButton.setIcon(new ImageIcon(getClass().getResource("run.gif")));
		runButton.addActionListener(this);
		//Stop button
		stopButton.setToolTipText("Stop");
		stopButton.setIcon(new ImageIcon(getClass().getResource("stop.gif")));
		stopButton.addActionListener(this);
		//Reset button
		resetButton.setToolTipText("Reset");
		resetButton.setIcon(new ImageIcon(getClass().getResource("reset.gif")));
		resetButton.addActionListener(this);
		//About button
		aboutButton.setToolTipText("Information");
		aboutButton.setIcon(new ImageIcon(getClass().getResource("about.gif")));
		aboutButton.addActionListener(this);
		//Combo boxes
		//Update choice
		updateChoice.addItemListener(this);
		updateChoice.setToolTipText("Update Frequency");
		updateChoice.addItem("Update 1");
		updateChoice.addItem("Update 10");
		updateChoice.addItem("Update 100");
		updateChoice.addItem("Update 1000");
		//Stop choice
		stopChoice.addItemListener(this);
		stopChoice.setToolTipText("Stop Frequency");
		stopChoice.addItem("Continuous");
		stopChoice.addItem("Stop 10");
		stopChoice.addItem("Stop 100");
		stopChoice.addItem("Stop 1000");
		stopChoice.addItem("Stop 10000");
		stopChoice.setSelectedIndex(1);
		//Construct basic toolbar
		toolBars.setLayout(new BoxLayout(toolBars, BoxLayout.Y_AXIS));
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(stepButton);
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(resetButton);
		toolBar.add(updateChoice);
		toolBar.add(stopChoice);
		toolBar.add(aboutButton);
		addToolBar(toolBar);
		//Add panels to applet
		componentPanel.setLayout(componentLayout);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(toolBars, BorderLayout.NORTH);
		this.getContentPane().add(componentPanel, BorderLayout.CENTER);
		
		//this.clipboard = (SOCRExperments)applet.getClipBoard();
		//System.out.println("edu.uah.math.experiments.Experiment init called");
	}

	/**
	* This method gets the time parameter of the stochastic process.  If the process
	* is to replicate a basic random experiment, then the time parameter is the number of
	* runs.
	* @return the current time parameter of the random process
	*/
	public int getTime(){
		return time;
	}

	 public void  pasteData(Clipboard c){
		 
	 }
	/**
	* This method returns basic copyright, author, and other metadata information.
	* @return applet information
	*/
	public String getAppletInfo(){
		return "\nCopyright (C) 2002, Kyle Siegrist, Dawn Duehring.\n"
			+ "Modified by Dushyanth Krishnamurthy, Jeff Ma and Ivo Dinov\n";
		/*********************
			+ "This program is free software; you can redistribute it and/or modify it under the terms\n"
			+ "of the GNU General Public License as published by the Free Software Foundation.\n"
			+ "This program is distributed in the hope that it will be useful, but without any warranty;\n"
			+ "without even the implied warranty of merchantability or fitness for a particular purpose.\n\n"
			+ "The Single Step button runs the experiment one time and The Run button runs the experiment\n"
			+ "repeatedly. The Stop button stops the experiment, but preserves the simulation data.\n"
			+ "The Reset button clears the simulation data and restores the applet to its initial state.\n"
			+ "The Update Frequency controls how often simulation data are recorded and the Stop Frequency\n"
			+ "controls how often the simulation stops when in run mode.";
		*****************/
	}
    
        /* (non-Javadoc)
         * @see edu.ucla.stat.SOCR.core.IExperiment#getOnlineDescription()
         */
        public String getOnlineDescription() {
            return getAppletInfo();
        }

      
	/**
	* This method stops the simulation when the simulation is in run mode..
	*/
	public void stop(){
		timer.stop();
		stopCount = 0;
		stopNow = false;
		if (updateCount != 0) update();
	}

	/**
	* This method runs the experiment is repeatedly,  and the update and stop counters incremented on each run.
	* If the update count equals the update frequency, the {@link #update() update} method is called.
	* If the stop count equals the stop frequency, the simulation is stopped.
	*/
	public void run(){
		timer.start();
	}

	/**
	* This method handles the action events associated with the simulation buttons.
	* The single step button calls the {@link #step() step} method, which runs the
	* experiment one time, and then stops. The run button calls the {@link #run() run} method,
	* which runs the experiment repeatedly. The stop button calls the {@link #stop() stop} method, which
	* stops the run mode and then calls the {@link #update() update} method if necessary.
	* The reset button callse the {@link #reset() reset} method. The about button
	* shows a message dialog box with the text from {@link #getAppletInfo() getAppletInfo} method.
	* This method also handles the timer events.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		//Step button
		if (e.getSource() == stepButton) step();
		//Run button
		else if (e.getSource() == runButton) run();
		//Stop button
		else if (e.getSource() == stopButton) stop();
		//Reset button
		else if (e.getSource() == resetButton) reset();
		//Help button
		else if (e.getSource() == aboutButton)
			JOptionPane.showMessageDialog(frame, getName() + getAppletInfo(), "About " + getName(), JOptionPane.INFORMATION_MESSAGE);
		else if (e.getSource() == timer){
			if ((stopCount == stopFreq) | stopNow) stop();
			else{
				doExperiment();
				stopCount++;
				updateCount++;
				if (updateCount == updateFreq) update();
			}
		}
	}

	/**
	* This method handles the choice events, associated with the update and stop choices.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		//Update frequency choice
		if (e.getSource() == updateChoice){
			updateFreq = (int)(Math.pow(10, updateChoice.getSelectedIndex()));
		}
		//Stop frequency choice
		else if (e.getSource() == stopChoice){
			int j = stopChoice.getSelectedIndex();
			if (j > 0) stopFreq = (int)(Math.pow(10, j));
			else stopFreq = -1;
		}
	}

	/**
	* This method defines what the experiment actually does, and should be overridden,
	* when this class is subclassed, to define an actual random pocess.
	*/
	public void doExperiment(){
		time++;
	}

	/**
	* This method is the default step method, that runs the process one time unit.
	* This method can be overridden to add sound, additional annimation, or other elements.
	*/
	public void step(){
		stop();
		doExperiment();
		update();
	}

	/**
	* This method is the default reset method, that resets the process to its initial state.
	* This method should be overridden.
	*/
	public void reset(){
		stop();
		time = 0;
	}

	/**
	* This method is the default update method and defines how the display is updated.
	* This method should be overridden.
	*/
	public void update(){
		updateCount = 0;
	}

	public void graphUpdate(){
	}
	/**
	* This method defines the boolean variable that stops the process, when the simulation is in run mode.
	* @param b the boolean variable that defines when the experiment stops
	*/
	public void setStopNow(boolean b){
		stopNow = b;
	}

	/**
	* This method returns the stop frequency.
	* @return the number of runs until the simulation stops.
	*/
	public int getStopFreq(){
		return stopFreq;
	}

	/**
	* This method sets the stop frequency.
	* @param i the number of runs until the simulation stops
	*/
	public void setStopFreq(int i){
		stopFreq = i;
	}

 	/**
 	* This method formats a number as a string in a specified way.
 	* @param x the number to be formatted.
 	* @return the number formatted as a string
 	*/
	public String format(double x){
		return decimalFormat.format(x);
	}

	/**
	* This methd sets the decimal format, so that the properties of the decimal format
	* can then be changed.
	*/
	public void setDecimalFormat(DecimalFormat d){
		decimalFormat = d;
	}

	/**
	* This class method returns the frame that contains a given component.
	* @param c the component
	* @return the frame that contains the component
	*/
	static Frame getFrame(Component c){
		Frame frame = null;
		while((c = c.getParent()) != null){
			if (c instanceof Frame) frame = (Frame)c;
		}
		return frame;
	}

	/**
	* This method adds a component to the component panel at a specified row and column,
	* specified row span and column span, and specified horizontal and vertical weights.
	* @param c the component
	* @param x the column number of the component
	* @param y the row number of the component
	* @param w the number of columns spanned by the component
	* @param h the number of rows panned by the component
	* @param wx the horizontal weight factor;
	* @param wy the vertical weight factor;
	*/
	public void addComponent(Component c, int x, int y, int w, int h, int wx, int wy){
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.weightx = wx;
		constraints.weighty = wy;
		constraints.fill = GridBagConstraints.BOTH;
		componentLayout.setConstraints(c, constraints);
		componentPanel.add(c);
	}

	/**
	* This method adds a component to the component panel at a specified row and column, with a
	* specified row span and column span, and specified fill parameter.
	* @param c the component
	* @param x the column number of the component
	* @param y the row number of the component
	* @param w the number of columns spanned by the component
	* @param h the number of rows panned by the component
	* @param f the fill parameter;
	*/
	public void addComponent(Component c, int x, int y, int w, int h, int f){
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.fill = f;
		componentLayout.setConstraints(c, constraints);
		componentPanel.add(c);
	}

	/**
	* This method adds a component to the component panel at a specified row and column,
	* with a specified row span and column span, and with default horizontal and vertical weights.
	* @param c the component
	* @param x the column number of the component
	* @param y the row number of the component
	* @param w the row span of the component
	* @param h the column span of the component
	*/
	public void addComponent(Component c, int x, int y, int w, int h){
		addComponent(c, x, y, w, h, 10, 10);
	}

	/**
	* This method adds a new component to the toolbar panel.
	* @param c the component to be added
	*/
	public void addToolBar(Component c){
		toolBars.add(c);
	}
	public void clearToolBars(){
		toolBars.removeAll();
	}
	/**
	* This method adds a tool to the main toolbar, in the next to the last position
	* (just before the About button).
	* @param c the component to be added to the toolbar
	*/
	public void addTool(Component c){
		toolBar.add(c, toolIndex);
		toolIndex++;
	}

	/**
	* This method returns the main toolbar.
	* @return the main toolbar
	*/
	public JToolBar getMainToolBar(){
		return toolBar;
	}

	/**
	* This method gets the component panel.
	* @return the component panel
	*/
	public JPanel getComponentPanel(){
		return componentPanel;
	}

	/**
	* This method returns the update choice.
	* @return the update choice
	*/
	public JComboBox getUpdateChoice(){
		return updateChoice;
	}

	/**
	* This method returns the stop choice.
	* @return the stop choice
	*/
	public JComboBox getStopChoice(){
		return stopChoice;
	}

	/**
	* This method sets the stop-choice.
	* @param index is the index to choose from the stopChoice List
	*/
	protected void setStopChoice(int index){
		if (0<= index && index < stopChoice.getItemCount())
			stopChoice.setSelectedIndex(index);
	}

	/**
	* This method sets the stop-choice.
	* @param index is the index to choose from the stopChoice List
	*/
	public void setStopChoiceTipText(String str){
		stopChoice.setToolTipText(str);
	}

	/**
	This method returns the timer.
	* @return the simulation timer
	*/
	public Timer getTimer(){
		return timer;
	}

	/**
	* This method plays one of the standard "telephone" notes 0.au through 9.au. If the
	* index is out of range, the note corresponding to the remainder mod 10 is played.
	* @param i the note index
	*/
	public void playnote(int i){
		applet.play(applet.getCodeBase(), "sounds/" + String.valueOf(i % 10) + ".au");
	}

	/**
	* This method plays note 1 if the boolean argument is true and note 0 otherwise.
	* @param b the boolean variable
	*/
	public void playnote(boolean b){
		if (b) playnote(1);
		else playnote(0);
	}

	/**JSlider events*/
	public void stateChanged(javax.swing.event.ChangeEvent e){}

	/**Mouse events*/
	public void mouseClicked(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}

	/**Mouse motion events*/
	public void mouseMoved(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}

	/**Window events*/
	public void windowOpened(WindowEvent event){}
	public void windowClosing(WindowEvent event){}
	public void windowClosed(WindowEvent event){}
	public void windowIconified(WindowEvent event){}
	public void windowDeiconified(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}

    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.Pluginable#setApplet(javax.swing.JApplet)
     */
    public void setApplet(JApplet applet) {
        this.applet = applet;       
    }

    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.Pluginable#getDisplayPane()
     */
    public Container getDisplayPane() {
        return getContentPane();
    }

    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.IExperiment#initialize()
     */
    public void initialize() {
        init();        
    }
    
    public void setShowModelDistribution(boolean flag) {
		// TODO Auto-generated method stub
		showModelDistribution = flag;
	}
    
    public JTable getResultTable(){
    	return null;
    }

    
}

