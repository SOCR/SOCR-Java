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
package edu.uah.math.games;
import java.awt.*;
import java.awt.Frame;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BoxLayout;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JApplet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.core.*;

/**
* This class defines a basic shell for an interactive exploration. Typically, the
* user generates the data by making choices in a game or by clicking on a number line
* or scatter plot.  This class must be sub-classed to add the appropriate functionality.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Game extends JApplet implements IGame, ActionListener, ItemListener, ChangeListener, MouseListener, MouseMotionListener, Serializable{
	//Variables
	private int time = 0, toolBarCount = 0, graphCount = 0, tableCount = 0, toolIndex = 0;
	//Panels
	private JToolBar toolBar = new JToolBar("Main Toolbar");
	private JPanel toolBars = new JPanel(), componentPanel = new JPanel();
	//Buttons
	private JButton resetButton = new JButton(), aboutButton = new JButton();
	//JDialog
	private Frame frame;
	//Component panel layout
	private GridBagLayout componentLayout = new GridBagLayout();
	private GridBagConstraints constraints = new GridBagConstraints();
    private JApplet applet;

	/**
	* This method initializes the game, including the basic toolbar with reset and
	* about buttons, and the record table to record the outcome of the game.
	*/
	public void init(){
		//Set color
		setName("Game");
		setBackground(Color.lightGray);
		frame = getFrame(this);
		//Reset button
		resetButton.setToolTipText("Reset");
		resetButton.setIcon(new ImageIcon(getClass().getResource("reset.gif")));
		resetButton.addActionListener(this);
		//About button
		aboutButton.setToolTipText("Information");
		aboutButton.setIcon(new ImageIcon(getClass().getResource("about.gif")));
		aboutButton.addActionListener(this);
		//Layout
		componentPanel.setLayout(componentLayout);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add("North", toolBars);
		this.getContentPane().add("Center", componentPanel);
		//Construct toolbar
		toolBars.setLayout(new BoxLayout(toolBars, BoxLayout.Y_AXIS));
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(resetButton);
		toolBar.add(aboutButton);
		addToolBar(toolBar);
	}

	/**
	* This method returns basic copyright information. The method can be
	* overridden to add additional descriptive and instructional information.
	*/
	public String getAppletInfo(){
		return "\nCopyright (C) 2002, Kyle Siegrist, Dawn Duehring.\n"
			+ "Modified by Dushyanth Krishnamurthy, Jeff Ma and Ivo Dinov\n";
			/***************************
			+ "This program is free software; you can redistribute it and/or modify it under the terms \n"
			+ "of the GNU General Public License as published by the Free Software Foundation. \n"
			+ "This program is distributed in the hope that it will be useful, but without any warranty;\n"
			+ "without even the implied warranty of merchantability or fitness for a particular purpose.\n\n"
			+ "The Reset button clears the data and restores the applet to its initial state.";
			***************************/
	}

	/**
	* This method handles the events for the About button and the Reset button.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == resetButton) reset();
		else if (e.getSource() == aboutButton)
			JOptionPane.showMessageDialog(frame, getName() + getAppletInfo(), "About " + getName(), JOptionPane.INFORMATION_MESSAGE);
	}

	//JSlider event
	public void stateChanged(ChangeEvent event){}

	//Item events
	public void itemStateChanged(ItemEvent event){}

	//Mouse events
	public void mouseClicked(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}
	public void mouseMoved(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}

	/**
	* This is the method for resetting the game and should be overridden.
	*/
	public void reset(){
		time = 0;
	}

	/**
	* This method gets the frame containing a specified component.
	* @param c the component
	* @return the frame containing the component
	*/
	private static Frame getFrame(Component c){
		Frame frame = null;
		while((c = c.getParent()) != null){
			if (c instanceof Frame) frame = (Frame)c;
		}
		return frame;
	}

	/**
	* This method adds a new component to the top panel.  Typically, this
	* component is a toolbar or some other "thin" component.
	* @param c the toolbar to be added
	*/
	public void addToolBar(Component c){
		toolBars.add(c);
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
	* This method adds a component to the component panel at a specified row and column,
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

	public void addComponent(Component c, int x, int y, int w, int h){
		addComponent(c, x, y, w, h, 10, 10);
	}

	/**
	* This method adds a tool to the main toolbar, in the next to the last position
	* (just before the About button).
	* @param c the tool to be added
	*/
	public void addTool(Component c){
		toolBar.add(c, toolIndex);
		toolIndex++;
	}

	/**
	* This method plays one of the standard "telephone" notes 0.au through 9.au. If the
	* index is out of range, the note corresponding to the remainder mod 10 is played.
	* @param i the note index
	*/
	public void playnote(int i){
		applet.play(applet.getCodeBase(), "edu/uah/math/sounds/" + String.valueOf(i % 10) + ".au");
	}

    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.IGame#getOnlineDescription()
     */
    public String getOnlineDescription() {
        return getAppletInfo();
    }

    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.Pluginable#setApplet(javax.swing.JApplet)
     */
    public void setApplet(JApplet applet) {
        this.applet = applet;
    }
    
    //public Container getContentPane() { return displayPane; }
    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.Pluginable#getDisplayPane()
     */
    public Container getDisplayPane() {
        init();
        return this;
    }
    
}

