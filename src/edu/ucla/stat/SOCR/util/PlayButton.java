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
/*  PlayButton.java defines PlayButton class
* @author Ivo Dinov
* @version 1.0 Feb. 19 2004
*/
package edu.ucla.stat.SOCR.util;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;

public class PlayButton extends JButton implements ActionListener {
	PlayApplet applet;
	int repetitions=20;
	int n=25;

	public PlayButton( PlayApplet applet, String label){
		super(label);
		this.applet=applet;
		this.addActionListener(this);
		}


	public void changeRepetitions( int repetitions){
		this.repetitions=repetitions;
		}

	public void change_n(int n){
		this.n = n;
		}

	public void actionPerformed(ActionEvent evt){
		if (evt.getSource()== this) {
			applet.play(repetitions);
		}
	}
}
