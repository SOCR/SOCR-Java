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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.net.*;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
 * this class is exemely similar to SOCRGames may should be subclassed
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class SOCRExperiments extends SOCRApplet implements ActionListener {
    IExperiment experiment;

    public final String ABOUT = "About";
    public final String HELP = "Help";
    public final String RESET = "Reset";
    public static String COPY ="COPY";
    public static String PASTE ="PASTE";
    public final String SNAPSHOT = "Snapshot";
    JFileChooser jfc;
    String _className;
    
    protected boolean MODEL_SWITCH = true;
	protected final String MODELON	= "ON";
	protected final String MODELOFF	= "OFF";
	
	public Clipboard clipboard;
	 
    public void stop() {
        experiment.stop();
    }

    public void initGUI() {
        //for fControlPanel
        controlPanelTitle = "SOCR Simulations & Experiments";
        implementedFile = "implementedExperiments.txt";
        
		try {
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		} catch (Exception e) {
			System.out.println("SOCRExperiments:initGUI(): No Security Access!");
		}

      
        addButton2(RESET, "Reset the Outcome of the Experiments!", this);
        addButton2(HELP, "SOCR Experiments Help", this);
        addButton2(ABOUT, "About this Experiment?", this);
        addButton(SNAPSHOT, "Take a Snapshot and save this Applet as JPG image", this);
        addButton(COPY, "Copy data from result table to mouse buffer", this);
        if (MODEL_SWITCH){
        	String[] bValue = {MODELON, MODELOFF};
        	addRadioButton("Show Model Distribution:", "Show Model Distribution switch", bValue, 0, this);
        }
        //right side panel
    }
  
    public void itemChanged(String className) {
        _className = className;
	try {
            experiment = Experiment.getInstance(className);
            experiment.setApplet(this);
           // System.out.println("(SOCRExperiments)experiment="+ className);
            if (className.indexOf("SimulationResampleExperiment")!=-1){
            	buttonP.removeAll();
            	fControlPanel.validate();
            	addButton(SNAPSHOT, "Take a Snapshot and save this Applet as JPG image", this);
            	addButton(COPY, "Copy data from result table to mouse buffer", this);
            	addButton(PASTE, "Paste data into data table", this);
            	fControlPanel.validate();
            }
            else 
            {
            	buttonP.removeAll();
            	fControlPanel.validate();
            	addButton(SNAPSHOT, "Take a Snapshot and save this Applet as JPG image", this);
            	addButton(COPY, "Copy data from result table to mouse buffer", this);
            	fControlPanel.validate();
            }
            experiment.initialize();
            resetRadioButton(0);
            fPresentPanel.setViewportView(experiment.getDisplayPane());
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, className + "\nnot implemented yet");
            e.printStackTrace();
        }       
    }
    
    public Object getCurrentItem() { return experiment;}

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(ABOUT)) {
            try {  //experiment = Experiment.getInstance(_className);	
		   JOptionPane.showMessageDialog(this, experiment.getName() + Experiment.getInstance(_className).getAppletInfo(), "About " + experiment.getName(), JOptionPane.INFORMATION_MESSAGE);

                /**getAppletContext().showDocument(
                new java.net.URL(experiment.getOnlineDescription()), 
                   "SOCR: GAME Online Help (Mathematica)");
                 **/
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                e.printStackTrace();
            }  catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                e.printStackTrace();
            }
	    
        } else if (evt.getActionCommand().equals(HELP)) {
            try {
                getAppletContext().showDocument(
                new java.net.URL("http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_Experiments"), 
                   "SOCR Experiments Online Help");
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                e.printStackTrace();
            }
        } else if (evt.getActionCommand().equals(RESET)) {
            experiment.reset();
        }
        else if (evt.getActionCommand().equals(SNAPSHOT)) {

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
                	  //System.out.println("type " + type);
                	  try {
                	      javax.imageio.ImageIO.write(image,type,f);
                	  } catch (java.io.IOException ioe) {
                	      ioe.printStackTrace();
        	      	      JOptionPane.showMessageDialog(null,
					ioe,"Error Writing File",JOptionPane.ERROR_MESSAGE);
                	  }
              	      }
          	  });
         }else if (evt.getActionCommand().equals(MODELON)){
        	 experiment.setShowModelDistribution(true);
        	 experiment.graphUpdate();
        	 
         }else if (evt.getActionCommand().equals(MODELOFF)){
        	 experiment.setShowModelDistribution(false);
        	 experiment.graphUpdate();
         }else if (evt.getActionCommand().equals(COPY)) {
     		String cpBuff = "";
     		JTable dTable = experiment.getResultTable();
    	
     		if (dTable ==null)
     			JOptionPane.showMessageDialog(null,null,"No result Table in this experiment",JOptionPane.ERROR_MESSAGE);
     		else
     		{
     			int[] cpRows =  dTable.getSelectedRows();
     			int[] cpCls =  dTable.getSelectedColumns();
     		
     			//System.out.println("cpColLength="+cpCls.length + "cpRowLength="+cpRows.length);
     			if(cpCls.length<1 || cpRows.length<1) {
     				OKDialog OKD = new OKDialog(null, 
     					true, "Make a selection to copy data");
                          		OKD.setVisible(true);
     			} else {
     			// System.out.println("copy"+experiment.getResultTable().getSelectedRowCount()+";"+experiment.getResultTable().getSelectedColumnCount());
     			for(int i = 0;i<cpRows.length;i++) {
     				for(int j = 0;j<cpCls.length;j++) {
     					if(dTable.getValueAt(cpRows[i],cpCls[j])==null)
     						cpBuff = cpBuff + " \t";
     					else
     						cpBuff = cpBuff +
     						dTable.getValueAt(cpRows[i],cpCls[j]) + "\t";
     				}
     				cpBuff = cpBuff.substring(0,cpBuff.length()-1) + "\n";           
     			}                     
     		}

    		  try{
                          StringSelection stTran = new StringSelection(cpBuff);
                          clipboard.setContents(stTran,stTran);
                          
                      }catch(Exception e) {}
    		
            }
         }else if (evt.getActionCommand().equals(PASTE)) {
        	 experiment.pasteData(clipboard);
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
