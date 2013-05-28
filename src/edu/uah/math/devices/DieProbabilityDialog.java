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
package edu.uah.math.devices;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.Serializable;

/**
* This class is a special dialog box for entering die probabilities. The user can specify the
* probability for each face, or can choose from a set of special distributions (fair, 1-6 flat,
* 2-5 flat, 3-4 flat, skewed left, or skewed right).
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DieProbabilityDialog extends ProbabilityDialog implements Serializable{
	private JToolBar buttonBar = new JToolBar("Special Distributions");
	private JButton fairButton = new JButton();
	private JButton flat16Button = new JButton();
	private JButton flat25Button = new JButton();
	private JButton flat34Button = new JButton();
	private JButton leftButton = new JButton();
	private JButton rightButton = new JButton();

    /**
    * This general constructor creates a new dialog box with a specified
    * owner frame. The constructor also sets up the buttons for selecting
    * a special distribution.
    * @param f the owner frame.
    */
    public DieProbabilityDialog(Frame f) {
		super(f, "Die Probabilities", new double[]{1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6});
		//Event Listeners
		fairButton.addActionListener(this);
		fairButton.setToolTipText("Fair");
		fairButton.setIcon(new ImageIcon(getClass().getResource("fair.gif")));
		flat16Button.addActionListener(this);
		flat16Button.setToolTipText("1-6 Flat");
		flat16Button.setIcon(new ImageIcon(getClass().getResource("flat16.gif")));
		flat25Button.addActionListener(this);
		flat25Button.setToolTipText("2-5 Flat");
		flat25Button.setIcon(new ImageIcon(getClass().getResource("flat25.gif")));
		flat34Button.addActionListener(this);
		flat34Button.setToolTipText("3-4 Flat");
		flat34Button.setIcon(new ImageIcon(getClass().getResource("flat34.gif")));
		leftButton.addActionListener(this);
		leftButton.setToolTipText("Skewed Left");
		leftButton.setIcon(new ImageIcon(getClass().getResource("skewedLeft.gif")));
		rightButton.addActionListener(this);
		rightButton.setToolTipText("Skewed Right");
		rightButton.setIcon(new ImageIcon(getClass().getResource("skewedRight.gif")));
		//Layout
		invalidate();
		buttonBar.add(fairButton, null);
		buttonBar.add(flat16Button, null);
		buttonBar.add(flat25Button, null);
		buttonBar.add(flat34Button, null);
		buttonBar.add(leftButton, null);
		buttonBar.add(rightButton, null);
		getContentPane().add(buttonBar, BorderLayout.NORTH);
		//Labels
		pack();
    }

	/**
	* This method handles the events associated with selecting a button for a special
	* distribution.
	* @param e the action event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == fairButton) setProbabilities(new double[] {1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6});
		else if (e.getSource() == flat16Button) setProbabilities(new double[] {1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 4});
		else if (e.getSource() == flat25Button) setProbabilities(new double[] {1.0 / 8, 1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 8});
		else if (e.getSource() == flat34Button) setProbabilities(new double[] {1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 8, 1.0 / 8});
		else if (e.getSource() == leftButton) setProbabilities(new double[] {1.0 / 21, 2.0 / 21, 3.0 / 21, 4.0 / 21, 5.0 / 21, 6.0 / 21});
		else if (e.getSource() == rightButton) setProbabilities(new double[] {6.0 / 21, 5.0 / 21, 4.0 / 21, 3.0 / 21, 2.0 / 21, 1.0 / 21});
		else super.actionPerformed(e);
		repaint();
	}
}