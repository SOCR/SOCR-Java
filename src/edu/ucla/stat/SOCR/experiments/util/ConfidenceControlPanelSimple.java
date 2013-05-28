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
/* ConfidenceControlPanel.java */

/*  ConfidenceControlPanel.java illustrates construction & utiliation of CI's
* @author Ivo Dinov
* @version 1.0 Feb. 19 2004
*/

/*File ConfidenceControlPanel.java  */
package edu.ucla.stat.SOCR.experiments.util;

import java.awt.*;
import java.applet.*;
import javax.swing.*;
import edu.ucla.stat.SOCR.experiments.*;

public class ConfidenceControlPanelSimple extends JPanel{
	ConfidenceIntervalExperimentSimple applet;
	FlowLayout fl = new FlowLayout();
	JComboBox alphaChoice;
	int alpha_indx=3;
	double alpha_values[] = {0.5, 0.2, 0.1, 0.05, 0.01, 0.001, 0.0001};
        public JToolBar toolbar = new JToolBar();
	public JTextField numberOfTrialsText, sampleSizeText;
	JLabel numberOfTrialsLabel, sampleSizeLabel;
	
	/**
	 * constructor of control panel
	 * @param applet the calling applet
	 */
	public ConfidenceControlPanelSimple(ConfidenceIntervalExperimentSimple applet){
		this.applet=applet;
		setBackground(new Color(230,230,230));
		setLayout(fl);
		alphaChoice = new JComboBox();
		alphaChoice.addItem("alpha = "+alpha_values[0]);
		alphaChoice.addItem("alpha = "+alpha_values[1]);
		alphaChoice.addItem("alpha = "+alpha_values[2]);
		alphaChoice.addItem("alpha = "+alpha_values[3]);
		alphaChoice.addItem("alpha = "+alpha_values[4]);
		alphaChoice.addItem("alpha = "+alpha_values[5]);
		alphaChoice.addItem("alpha = "+alpha_values[6]);

		alphaChoice.setSelectedIndex(alpha_indx);
		
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(alphaChoice);
		//sampleSizeText = new JTextField("", 10);
        sampleSizeText = new JTextField("",3);
		numberOfTrialsText = new JTextField("", 3);
		sampleSizeLabel = new JLabel("Sample Size");
		numberOfTrialsLabel = new JLabel("Number of Intervals");

		//PlayButton play = new PlayButton(applet,"Play");
			// ACTION PASSED DIRECTLY TO APPLET
		//add(play); 
		toolbar.add(sampleSizeText);
		toolbar.add(sampleSizeLabel);
		toolbar.add(numberOfTrialsText);
		toolbar.add(numberOfTrialsLabel);
	}

	/**
	 *  clears panel 
	 */
	public void clear() {
				}

	/**
	 * Gets alpha information
	 * 0-> alpha=0.05, 1-> alpha = 0.01 1-> p < mu0, and 2 -> p != mu0
	 */
	public int getAlphaIndex(){
		return alphaChoice.getSelectedIndex();
		}

	public double getAlphaValue(){
		return alpha_values[alphaChoice.getSelectedIndex()];
	}


	public int getSampleSize(){
		int n= Integer.parseInt(sampleSizeText.getText());
		if (n>=1) return n;
		else {
			sampleSizeText.setText("1");
			return 1;
		}
	}

	public int getNumberOfTrials(){
		int nTrials = Integer.parseInt(numberOfTrialsText.getText());
		if (nTrials>=1) return nTrials;
		else {
			numberOfTrialsText.setText("1");
			return 1;
		}
	}


	public void setSampleSize(int n){
		if (n>=1) sampleSizeText.setText(Integer.toString(n));
		else sampleSizeText.setText(Integer.toString(1));
	}

	public void setNumberOfTrials(int nTrials){
		if (nTrials>=1) numberOfTrialsText.setText(Integer.toString(nTrials));
		else numberOfTrialsText.setText(Integer.toString(1));
	}
}

