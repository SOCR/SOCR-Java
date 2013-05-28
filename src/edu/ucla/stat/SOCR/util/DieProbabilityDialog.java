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
import javax.swing.*;

public class DieProbabilityDialog extends ProbabilityDialog {
	private JPanel buttonPanel = new JPanel();
	private JButton fairButton = new JButton("Fair");
	private JButton flat16Button = new JButton("1-6 Flat");
	private JButton flat25Button = new JButton("2-5 Flat");
	private JButton flat34Button = new JButton("3-4 Flat");
	private JButton leftButton = new JButton("Skewed Left");
	private JButton rightButton = new JButton("Skewed Right");

    public DieProbabilityDialog(Frame owner) {
		super(owner, "Die Probabilities", 6);
		//Event Listeners
		fairButton.addActionListener(this);
		flat16Button.addActionListener(this);
		flat25Button.addActionListener(this);
		flat34Button.addActionListener(this);
		leftButton.addActionListener(this);
		rightButton.addActionListener(this);
		//Layout
		invalidate();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(fairButton);
		buttonPanel.add(flat16Button);
		buttonPanel.add(flat25Button);
		buttonPanel.add(flat34Button);
		buttonPanel.add(leftButton);
		buttonPanel.add(rightButton);
		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		//Labels
		for (int i = 0; i < 6; i++) setLabel(i, String.valueOf(i + 1));
		pack();
    }

	public void actionPerformed(ActionEvent event){
		if (event.getSource() == fairButton) setProbabilities(new double[] {1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6});
		else if (event.getSource() == flat16Button) setProbabilities(new double[] {1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 4});
		else if (event.getSource() == flat25Button) setProbabilities(new double[] {1.0 / 8, 1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 8});
		else if (event.getSource() == flat34Button) setProbabilities(new double[] {1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 8, 1.0 / 8});
		else if (event.getSource() == leftButton) setProbabilities(new double[] {1.0 / 21, 2.0 / 21, 3.0 / 21, 4.0 / 21, 5.0 / 21, 6.0 / 21});
		else if (event.getSource() == rightButton) setProbabilities(new double[] {6.0 / 21, 5.0 / 21, 4.0 / 21, 3.0 / 21, 2.0 / 21, 1.0 / 21});
		else super.actionPerformed(event);
	}
}
