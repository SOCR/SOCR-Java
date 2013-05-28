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
import java.net.*;

import javax.swing.*;

/**
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class SOCRGames extends SOCRApplet implements ActionListener {

    /**
     * 
     * @uml.property name="game"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    IGame game;
    JFileChooser jfc;

    public final String ABOUT = "About";
    public final String HELP = "Help";
    public final String RESET = "Reset";
    public final String SNAPSHOT = "Snapshot";

    //protected URL codeBase;

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*
	public String getAppletInfo(){
		return "SOCR Games developed by Dushyanth Krishnamurthy and Ivo Dinov, 2003-2006!" + game.getAppletInfo();
	}
*/

    public void initGUI() {
        //for fControlPanel
        SOCRCodeBase.setCodeBase(this.getCodeBase());
    	controlPanelTitle = "SOCR Interactive Games";
        implementedFile = "implementedGames.txt";
        
        addButton(RESET, "Reset the Outcome of the Games!", this);
        addButton(ABOUT, "About this Game?", this);
        addButton(HELP, "Help with Using SOCR Games", this);
     	addButton(SNAPSHOT, "Take a Snapshot and save this Applet as JPG image", this);
   
        //right side panel
    }
    
    public void itemChanged(String className) {
        try {
            game = Game.getInstance(className);
            game.setApplet(this); 
            Container pane = game.getDisplayPane();
            fPresentPanel.setViewportView(pane);
            pane.validate(); //did not help for the problem
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Sorry, not implemented yet");
            e.printStackTrace();
        }       
    }
    
    public Object getCurrentItem() { return game;}

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(ABOUT)) {
            JOptionPane.showMessageDialog(this, game.getName()+": " + game.getOnlineDescription(), "About: " + game.getName(), JOptionPane.INFORMATION_MESSAGE);
            	/*************************OLD
	    		try {
                	getAppletContext().showDocument(
                	new java.net.URL(game.getOnlineDescription()), 
                   		"SOCR: GAME Online Help (Mathematica)");
            	} catch (MalformedURLException e) {
                	JOptionPane.showMessageDialog(this, e.getMessage());
                	e.printStackTrace();
            	}
            	****************************************/
        } else if (evt.getActionCommand().equals(HELP)) {
            try {
                getAppletContext().showDocument(
                new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_Games"), 
                   "SOCR Games Online Help");
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                e.printStackTrace();
            }
        } else if (evt.getActionCommand().equals(RESET)) {
            game.reset();
        } else if (evt.getActionCommand().equals(SNAPSHOT)) {

      		SwingUtilities.invokeLater( new Runnable() {
        	      public void run() {
                	  java.awt.image.BufferedImage image = capture();

                	  if (jfc==null) jfc = new JFileChooser();
			  else jfc.setVisible(true);
      			  int option = jfc.showSaveDialog(null);
			  java.io.File f = jfc.getSelectedFile();
			  jfc.setVisible(false);

			  //System.out.println("image " + image);
			  // Fix the problems with users not entering proper file extensions (.gif or .jpg)
                	  if (!f.getName().endsWith(".jpg"))
				 f = new java.io.File(f.getAbsolutePath()+ ".jpg");
                	  
                	  String type = 
				f.getName().substring(f.getName().lastIndexOf('.') + 1);	
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
    
}
