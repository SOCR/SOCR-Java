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

package edu.ucla.stat.SOCR.core;

import java.awt.event.*;
import java.net.*;
import java.util.*;

import edu.ucla.stat.SOCR.games.wavelet.*;
import JSci.maths.wavelet.*;
import javax.swing.*;

/** This class implements the wavelet loading dynamically */
public class WaveletLoader implements ActionListener {

    protected FWT fwt;
    private SOCRJComboBox implementedCombo;
    protected	URL codeBase;
    protected String implementedFile = null;

    public Object getCurrentItem() {
        return fwt;
    }
    private Observable observable = new Observable() {
        public void notifyObservers() {
        	try{
            System.out.println("notifying observers");
            super.setChanged();
            super.notifyObservers();
            //super.
        	}catch(Exception e){e.printStackTrace();}
        }
    };


    public WaveletLoader(URL url) {
        //configuration for fControlPanel:: leftside pane
        //System.out.println("this is start of init gui");
       codeBase = url;
    	implementedFile = "implementedWavelets.txt";
       try{
    	if (implementedFile != null) {
       	implementedCombo = new SOCRJComboBox(new URL(codeBase,
                implementedFile).openStream());
    	}
       }catch(Exception e) {e.printStackTrace(); }

       implementedCombo.addActionListener(this);

       itemChanged(implementedCombo.getSelectedClassName());

    }

    public void start() {

    }

    protected void itemChanged(String className) {
        try {
            fwt = (FWT)Class.forName(className).newInstance();
            //System.out.println("1. performing fwt in waveletLoader");
            //observable.
            observable.notifyObservers();


        } catch (Throwable e) {

            e.printStackTrace();
        }
    }

    public void addObserver(Observer o) {
		//System.out.println("adding observer " + o.getClass().toString());
		//System.out.println("Observer Count before = "+observable.countObservers() );
		observable.addObserver(o);
		//System.out.println("Observer Count after = "+observable.countObservers() );
    }

    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == implementedCombo) {
        	itemChanged(implementedCombo.getSelectedClassName());
        }
    }

    public JComboBox getJComboBox() {
    	//panel.add()
    	return this.implementedCombo;
    }

    /* The value of Distribution changed, so repaint itself
     * just repaint is not enough, need updata domain and scale
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */


    /** updates the collected information of distribution */
    public void updateStatus() {

    }
}
