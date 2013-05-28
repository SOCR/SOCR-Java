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
/* Author - Dushyanth Krishnamurthy Nov 29th 2004
This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 2 of the License, or (at your option)
any later version.

This program is distributed in the hope that it will be useful, but without
any warranty; without even the implied warranty of merchantability or
fitness for a particular purpose. See the GNU General Public License for
more details. http://www.gnu.org/copyleft/gpl.html */

package edu.ucla.stat.SOCR.util;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.SOCRModeler;



public class ObservableWrapper implements KeyListener, ActionListener, ItemListener, AdjustmentListener {

	private Observable observable = new Observable() {
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }
    };
    private JPanel controlpanel;
    static int paramCount = 0;
    //Vector jpanels  = new Vector();
   // private chkBox;



    public ObservableWrapper() {
    //controlpanel  = cpanel;

    }

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }


	public void addJCheckBox(JCheckBox chkbox){
		//SOCRModeler.addToGridBag(controlpanel, chkbox, 0, paramCount, 1, 1, 1.0, 0.0);
		paramCount++;
		chkbox.addItemListener(this);


	}

	public void addJTextField(JTextField tf) {
		//SOCRModeler.addToGridBag(controlpanel, tf, 0, paramCount, 1, 1, 1.0, 0.0);
		paramCount++;
		tf.addKeyListener(this);
	}

	public void addJButton(JButton bt) {

		paramCount++;
		bt.addActionListener(this);

	}

	public void addJComboBox(JComboBox bt) {

			paramCount++;
			bt.addActionListener(this);

		}

	public void itemStateChanged(ItemEvent event){
    		//System.out.println("notifying observers");
		observable.notifyObservers();


    }

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == e.VK_ENTER) {
			observable.notifyObservers();
		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}

	public  void adjustmentValueChanged(AdjustmentEvent e) {
	 }


	public void actionPerformed(ActionEvent e) {
		observable.notifyObservers();
	}


    }








