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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class defines a basic shell for an interactive exploration. Typically,
 * the user generates the data by making choices in a game or by clicking on a
 * number line or scatter plot. This class must be sub-classed to add the
 * appropriate functionality
 */

public class Game extends MultiplePartsPanel implements ActionListener, IGame {
    private int time = 0;
    String appletInfoString = "TEST";

    public static IGame getInstance(String classname) throws Exception {
        Class cls = Class.forName(classname);
        if (cls == null) return null;
        return (IGame) cls.newInstance();
    }
    
    /** This is the method for resetting the game and should be overridden. */
    public void reset() {
        time = 0;
    }
    
    /**
     * This method returns an online description of this Statistical Analysis.
     * It should be overwritten by each specific analysis method.
     */
    public String getOnlineDescription() {
        return getAppletInfo();   // OLD: "http://socr.stat.ucla.edu/";
    }

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public void setAppletInfo(String _appletInfoString){
		appletInfoString = _appletInfoString;
	}

	public String getAppletInfo(){
		return new String("SOCR Games developed by Dushyanth Krishnamurthy and Ivo Dinov, 2003-2006!\n http://www.socr.ucla.edu/ \n"+ appletInfoString);
	}


    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
         performAction();        
    }
    
    public void performAction() {}
    
    public void updateGame(Graphics g) {}
    
    /* (non-Javadoc)
     * @see edu.ucla.stat.SOCR.core.Pluginable#getContentPane()
     */
    public Container getDisplayPane() {
        JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                getMainPanel(), getTextPanel() );
        return container;
    }

}

