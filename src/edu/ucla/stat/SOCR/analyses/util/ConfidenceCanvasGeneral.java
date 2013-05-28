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
 It's Online, Therefore, It Exists!
 ***************************************************/

/*  ConfidenceCanvas.java display for confidence applet
* @author Ivo Dinov
* @version 1.0 Feb. 19 2004
*/
package edu.ucla.stat.SOCR.analyses.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;


public class ConfidenceCanvasGeneral extends JPanel{

    Dimension r;
 
    ConfidenceCanvasGeneralUpper upperPanel;
    ConfidenceCanvasGeneralLower lowerPanel;

    /**
     * Constructor for ConfidenceCanvas
     */
    public ConfidenceCanvasGeneral(int[] sampleSizes, int nTrials) {
       
    //    
        upperPanel= new ConfidenceCanvasGeneralUpper(sampleSizes, nTrials);
        lowerPanel= new ConfidenceCanvasGeneralLower(sampleSizes, nTrials);
        setSampleSizeAndNTrials(sampleSizes, nTrials);
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(upperPanel);
        this.add(lowerPanel);
        
        upperPanel.setBackground(Color.white);
    	lowerPanel.setBackground(Color.gray.brighter());    
            
    }
    
    public void setSize(int w, int h){
    	super.setSize(w, h);   	
    	upperPanel.setSize(w, h/2);
      	lowerPanel.setSize(w, h/2);
    }

  /*  public Dimension getPreferredSize() {
        return new Dimension(150, 125);
    }

    public Dimension getMinimumSize() {
        return new Dimension(30, 25);
    }*/

    public void setSampleSizeAndNTrials(int[] _n, int _nTrials) {

    	upperPanel.setSampleSizeAndNTrials(_n, _nTrials);
    	lowerPanel.setSampleSizeAndNTrials(_n, _nTrials);
    }


    /**
     * paint method for ConfidenceCanvas
     */
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    
    	if(upperPanel!=null && lowerPanel!=null 
    			&& upperPanel.getGraphics()!=null && lowerPanel.getGraphics()!=null){
    		upperPanel.paintComponent(upperPanel.getGraphics());
    		lowerPanel.paintComponent(lowerPanel.getGraphics());
    	}
    }

    public int getMissedCount() {
        return lowerPanel.getMissedCount();
    }

    public boolean[] getMissed() {
        return lowerPanel.getMissed();
    }
 
    public void clear(int[] n, int nTrials) {
       setSampleSizeAndNTrials(n, nTrials);
    //   setDistribution(dist);
        
       upperPanel.clear(n, nTrials);
       lowerPanel.clear(n, nTrials);
       repaint();
    }


    /* modified by rahul gidwani */

    public void update(int cvIndex, double[][] ciData, double[][] sampleData, double[] xBar) {
        upperPanel.update(cvIndex, ciData, sampleData, xBar);
        lowerPanel.update(cvIndex, ciData, sampleData, xBar);
        paintComponent(this.getGraphics());
  
    }

    public void update() {      
        paintComponent(this.getGraphics());
    }
    
    public void setIntervalType(IntervalType type) {
    	
        upperPanel.setIntervalType(type);
        lowerPanel.setIntervalType(type);
    }

  /*  public void setDistribution(Distribution dist) {
        upperPanel.setDistribution(dist);
        lowerPanel.setDistribution(dist);
    }*/

    public void setCutOffValue(double left, double right) {
        upperPanel.setCutOffValue(left, right);
        lowerPanel.setCutOffValue(left, right);
    }
    
    public void setCDFValue(double left_cdf, double right_cdf) {
    	upperPanel.setCDFValue(left_cdf, right_cdf);
    	lowerPanel.setCDFValue(left_cdf, right_cdf);
    }
    
    public void setCIMeanValue(double value) {
    		upperPanel.setCIMeanValue(value);
    		lowerPanel.setCIMeanValue(value);
    	
      }
    public void setCIVarValue(double value) {

        upperPanel.setCIVarValue(value);
        lowerPanel.setCIVarValue(value);
      }

}
