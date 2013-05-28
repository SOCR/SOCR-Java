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

import java.awt.datatransfer.Clipboard;

import javax.swing.JTable;


/**
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public interface IExperiment extends Pluginable {
	
	  public JTable getResultTable();
    /**
     * This method gets the time parameter of the stochastic process. If the
     * process is to replicate a basic random experiment, then the time
     * parameter is the number of runs.
     */
    public int getTime();

    /** This method stops the simulation thread */
    public void stop();

    /**
     * This method defines what the experiment actually does, and should be
     * overridden
     */
    public void doExperiment();

    /**
     * This method is the default step method, that runs the process one time
     * unit. This method can be overridden to add sound or other elements
     */
    public void step();

    /**
     * This method is the default reset method, that resets the process to its
     * initial state. This method should be overridden.
     */
    public void reset();

    /**
     * This method is the default update method and defines how the display is
     * updated. This method should be overridden.
     */
    public void update();
    
    public void graphUpdate();
    public void  pasteData(Clipboard c);
    /**
     * This method defines the boolean variable that stops the process, when the
     * simulation is in run mode
     */
    public void setStopNow(boolean b);

    /** This method returns the stop frequency */
    public int getStopFreq();

    /** This method sets the stop frequency */
    public void setStopFreq(int i);

    public String getOnlineDescription();
    
    public void initialize();
 

	// Needs to be overwritten by objects that extend this class
    public String getAppletInfo();
    
    public void setShowModelDistribution(boolean flag);
    

}
