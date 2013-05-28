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
/**
 * MGFGraphPanel.java 0.1 06/05/03 Ivo D. Dinov, Ph.D., Jianming, Rahul Gidwani
 * He This code was originially written by Ivo, Jianming modified it, later modified by Rahul Gidwani
 */

package edu.ucla.stat.SOCR.core;

import java.awt.*;

import java.awt.event.*;
import java.text.*;
import java.util.Observable;

import javax.swing.*;

import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.distributions.exception.*;

public class MGFGraphPanel extends GraphPanels implements MouseListener,
        MouseMotionListener {
	
	/**
	 * @param container
	 * Calls the constructor for the Super Class.
	 */
	public MGFGraphPanel(SOCRDistributionFunctors container) {
		super(container);
	}
	
   
    /**
	    * @uml.property name="PANELFILE"
	    * PANELFILE is the name of the textfile stored on the system which 
	    * populates the drop down menus.
	    */
	public final static String PANELFILE = "implementedMGFs.txt";

    /**
	    * @uml.property name="dist"
	    * Sets the distribution for viewing Moment Generating Functions
	    * overrides the setDistribution from the Parent Class.
	    */
    public void setDistribution(Distribution d) {
        if (d != null) {
            dist = d;
            domain = dist.getMgfDomain(); // ** Changed by Rahul Gidwani to call getMGFDomain()
            type = CONTINUOUS;
          	left = domain.getLowerBound();
        	right = domain.getUpperBound();
        	double y_max = dist.MAXMGFYVAL;
        	try { y_max = dist.getMGF(right); } catch (NoMGFException e) {System.err.println(e.getMessage());}
        	setScale(left, right, 0, y_max);

        }
        repaint();
    }
    
    /** This method returns the filename for which distributions belong to this panel. */
    public String getPanelFile()
	{   return PANELFILE;
	}

	/** 
	 * Method to graph the MGF(Moment Generating Function) for a particular function to the screen.
	 */
    public void paintComponent(Graphics g) 
    {

    	super.paintComponent(g);

    	(javax.swing.SwingUtilities.getRoot(this)).validate();
    	container.updateStatus();

    	if (dist != null) {
    		double x, a, b, delta, width, values;

    		width = domain.getWidth();
    		values = domain.getSize();

    		//Draw axes
    		g.setColor(Color.black);
    		drawAxis(g, 0, yMax, 0.1 * yMax, xMin, VERTICAL);
    		drawAxis(g, domain, 0, HORIZONTAL, type);
    		g.drawLine(leftMargin, topMargin, getWidth() - rightMargin, topMargin);
    		g.drawLine(getWidth() - rightMargin, topMargin, getWidth()
    				- rightMargin, getHeight() - bottomMargin);

    		//Draw distribution getMGF Replaced getDensity Function from GraphPanel.java
    		g.setColor(Color.red);

    		a = domain.getLowerValue();
    		b = domain.getUpperValue();
    		delta = (b - a) / (getWidth() - leftMargin - rightMargin);
    		for (x = a; x < b; x = x + delta) {
    			try{
    				drawLine(g, x, dist.getMGF(x), x + delta, dist.getMGF(x
    						+ delta));
    			}
    			catch (NoMGFException e) {System.err.println(e.getMessage());}
    		}
    		a = left;
    		b = right;
    		for (x = a; x < b; x = x + delta) {
    			try{
    				drawLine(g, x, dist.getMGF(x), x, 0);
    			}
    			catch (NoMGFException e) {System.err.println(e.getMessage());}
    		}

    		//Draw Cursor Position
    		if (xPosition > leftMargin && xPosition < getWidth() - rightMargin
    				&& yPosition > topMargin
    				&& yPosition < getHeight() - bottomMargin) {
    			g.setColor(Color.black);

    			int y1 = 0; // Default initialization to zero....check if correct RG

    			try {
    				y1 = yGraph(dist.getMGF(xScale(xPosition)));

    			} 
    			catch (NoMGFException e) {System.err.println(e.getMessage());}
    			g.drawLine(xPosition, y1 - 6, xPosition, y1 + 6);
    			g.drawLine(xPosition - 6, y1, xPosition + 6, y1);

    			String s1 = "";

    			try{
    				s1 = "( " + format(xScale(xPosition)) + ", "
    				+ format(dist.getMGF(xScale(xPosition))) + " )";
    			}
    			catch (NoMGFException e) {System.err.println(e.getMessage());}
    			g.drawString(s1, xPosition + 5, y1 - 5); 
    		}
    	}
    }
    
    public double getLeftCDF() {
    	double left_mgf = 0.0;
    	try { left_mgf = dist.getMGF(left); }
    	catch (NoMGFException e) {System.err.println(e.getMessage());}
    	return left_mgf;
    }

    public double getRightCDF() {
        double right_mgf = 0.0;
        try { right_mgf = dist.getMGF(right); }
        catch (NoMGFException e) {System.err.println(e.getMessage());}
        return right_mgf;
    }

    public double getBetweenCDF() {
        return (this.getRightCDF() - this.getLeftCDF());
    }
    // end of changed - RG
}
