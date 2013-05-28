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
 * GraphPanel.java 0.1 06/05/03 Ivo D. Dinov, Ph.D., Jianming
 * He This code was originially written by Ivo, Jianming modified it.
 */

package edu.ucla.stat.SOCR.core;

import edu.ucla.stat.SOCR.distributions.Domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;

public class GraphPanel extends JPanel implements MouseListener,
        MouseMotionListener {
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    /**
     *
     * @uml.property name="dist"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private Distribution dist;

    /**
     *
     * @uml.property name="domain"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private Domain domain;


    private int type;
    private int xPosition = 0 ;
    private int yPosition = 0 ;
    private double left;
    private double right;
    private double leftx;
    private double rightx;

    /**
     *
     * @uml.property name="container"
     * @uml.associationEnd multiplicity="(1 1)" inverse="graphPanel:edu.ucla.stat.SOCR.core.SOCRDistributions"
     */
    SOCRDistributions container;

    private Font font = new Font("sansserif", Font.PLAIN, 11);

    public final static int LEFT = 0 ;
    public final static int RIGHT = 1 ;
    public final static int ABOVE = 2 ;
    public final static int BELOW = 3 ;
    public final static int VERTICAL = 0 ;
    public final static int HORIZONTAL = 1 ;
    public final static int MIDPOINTS = 0 ;
    public final static int BOUNDS = 1 ;
    public final static int DISCRETE = Distribution.DISCRETE;
    public final static int CONTINUOUS = Distribution.CONTINUOUS;
    public final static int MIXED = Distribution.MIXED;
    public DecimalFormat decimalFormat = new DecimalFormat();

    public GraphPanel(SOCRDistributions container) {
        this.container = container;
        setScale(0, 1, 0, 1);
        setMargins(30, 30, 30, 30);
        setBackground(Color.white);
        setFont(font);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    /**
     *
     * @uml.property name="dist"
     */
    public void setDistribution(Distribution d) {
        if (d != null) {
            dist = d;
            try { 
            	domain = dist.getDomain();
            } catch (Exception e) {
            	System.out.println("Distribution's Domain is NOT set yet!!!");
            }
            type = dist.getType();

        /************************* Commented out 04/26/06*****************************
		// Set lower/Upper BOunds 0.5 away from the actual bounds to fix
		// a bug with computing the Discrete probabilities - Ivo Dinov
		// 12/09/05
            if (type == DISCRETE) {
            	left = domain.getLowerBound()-0.5;
            	right = domain.getUpperBound()+0.5;
            }
            else  {  // Continuous Distributions
            	left = domain.getLowerBound();
            	right = domain.getUpperBound();
            }
     *******************************04/26/06****************************/
          	left = domain.getLowerBound();
        	right = domain.getUpperBound();

            if (dist.getMaxDensity() < 1) {
                setScale(left, right, 0, dist.getMaxDensity() / 0.7);
            } else {
                setScale(left, right, 0, dist.getMaxDensity());
            }
        }
        
        if (type == DISCRETE) {
    		rigidIntegralMotionOfDiscriteDistributions(true);
    		
    		// Ivo replaced the shift by 1.0, instead of 1.5 (leftCutOff) in: 
    		// this.container.leftCutOff.setText(format(getLeftCutOff()+1.5));
    		this.container.leftCutOff.setText(format(getLeftCutOff()+1));
    		
            this.container.rightCutOff.setText(format(getRightCutOff()));
    	}
        else {
        	this.container.leftCutOff.setText(format(getLeftCutOff()));
        	this.container.rightCutOff.setText(format(getRightCutOff()));
        }
        repaint();
        this.container.observable.notifyObservers();
    }

    /** These methods get/set the Limits, leftCutOff and rightCutOff, 
     * for the probability calculation. These methods are typically 
     * called from SOCRDistribution 
     */
	public void setLeftCutOff(double _left)
	{	if (_left<=right && _left >=domain.getLowerBound()) {
			left = _left;
			if (type == DISCRETE && _left >=domain.getLowerBound()+0.5)
				rigidIntegralMotionOfDiscriteDistributions(true);
			//System.err.println("LeftCutOff set at: "+left);
			repaint();
		} else if (_left<domain.getLowerBound()) {
			if (type == DISCRETE) 
				this.container.leftCutOff.setText(format(domain.getLowerBound()+0.5));
			else if (type == CONTINUOUS) 
				this.container.leftCutOff.setText(format(domain.getLowerBound()));
		} else { // (_left > right)
			if (type == DISCRETE) 
				this.container.leftCutOff.setText(format(left+0.5));
			else if (type == CONTINUOUS) 
				this.container.leftCutOff.setText(format(left));
		}

		this.container.observable.notifyObservers();
		/******* Old: Revision October 16, 2008, to address bug report from Rob Gould
		 	else if (type == DISCRETE) 
				this.container.leftCutOff.setText((new Double(left+0.5)).toString());
			else if (type == CONTINUOUS) 
				this.container.leftCutOff.setText((new Double(left)).toString());
		***************/
    }

	public void setRightCutOff(double _right)
	{	if (_right>=left && _right<=domain.getUpperBound()) {
			right = _right;
			if (type == DISCRETE && _right <=domain.getUpperBound()-0.5)
				rigidIntegralMotionOfDiscriteDistributions(false);
			//System.err.println("RightCutOff set at: "+right);
			repaint();
		} else if (_right>domain.getUpperBound()) {
			if (type == DISCRETE) 
				this.container.rightCutOff.setText(format(domain.getUpperBound()-0.5));
			else if (type == CONTINUOUS) 
				this.container.rightCutOff.setText(format((domain.getUpperBound())));
		} else { 	// (_right < left)
			if (type == DISCRETE) 
				this.container.rightCutOff.setText(format(right-0.5));
			else if (type == CONTINUOUS) 
				this.container.rightCutOff.setText(format(right));
		}
		this.container.observable.notifyObservers();
	}

	public double getLeftCutOff()
	{	if (type == DISCRETE) return (int)(left-0.5); // Discritize for Discrete Distributions
		else return left;
	}
	public double getRightCutOff()
	{	if (type == DISCRETE) return (int)(right-0.5); // Discritize for Discrete Distributions
		else return right;
	}


    /** This method sets the minimum and maximum values on the x and y axes */
    public void setScale(double x0, double x1, double y0, double y1) {
        xMin = x0;
        xMax = x1;
        yMin = y0;
        yMax = y1;
    }

    /** This method sets the margin (in pixels) */
    public void setMargins(int l, int r, int b, int t) {
        leftMargin = l;
        rightMargin = r;
        bottomMargin = b;
        topMargin = t;
    }

    public void paintComponent(Graphics g) {
    //	System.out.println("Calling GraphPanel paint showGraph="+container.isShowGraph());
    	if(!container.isShowGraph()){
    		super.paintComponent(g);
    		return;
    	}
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

            //Draw distribution getDensity
            g.setColor(Color.red);
            if (type == DISCRETE) {
                for (int i = 0; i < values; i++) {
                    x = domain.getValue(i);
                    drawBox(g, x - width/2, 0, x + width/2, dist.getDensity(x));
                }
                a = left;
                b = right;
                // Introduced the slight reduction (0.99) of the delta-step to avoid an annoying Discrete
                // distributions bug with placing blank lines in the density plots
                delta = 0.99*(b - a) / (getWidth() - leftMargin - rightMargin);
                for (x = a; x < b; x = x + delta) {
                    drawLine(g, x, dist.getDensity(x), x, 0);
                }
            } else {
                a = domain.getLowerValue();
                b = domain.getUpperValue();
                delta = (b - a) / (getWidth() - leftMargin - rightMargin);
                for (x = a; x < b; x = x + delta) {
                    drawLine(g, x, dist.getDensity(x), x + delta, dist.getDensity(x
                            + delta));
                }
                a = left;
                b = right;
                for (x = a; x < b; x = x + delta) {
                    drawLine(g, x, dist.getDensity(x), x, 0);
                }
            }

            //Draw Cursor Position
            if (xPosition > leftMargin && xPosition < getWidth() - rightMargin
                    && yPosition > topMargin
                    && yPosition < getHeight() - bottomMargin) {
                g.setColor(Color.black);

                int y1 = yGraph(dist.getDensity(xScale(xPosition)));
                g.drawLine(xPosition, y1 - 6, xPosition, y1 + 6);
                g.drawLine(xPosition - 6, y1, xPosition + 6, y1);

                String s1 = "";
                if (type == DISCRETE) {
                    s1 = "( " + (int) (xScale(xPosition) + 0.5 * domain.getWidth())
                            / domain.getWidth() + ", "
                            + format(dist.getDensity(xScale(xPosition))) + " )";
                } else {
                    s1 = "( " + format(xScale(xPosition)) + ", "
                            + format(dist.getDensity(xScale(xPosition))) + " )";
                }
                g.drawString(s1, xPosition + 5, y1 - 5);
            }
            
            // Draw the values of the left & right cutOffs 
            // at the foots of these vertical lines
            // Update entered 01/20/09, Ivo Dinov, following recommendation by Rob Gould
            // ONLY if CONTINUOUS DISTRIBUTION (01/22/09)
             if (dist.getType()== CONTINUOUS) {
            	 g.setColor(Color.blue);
            	 String str = Double.toString((1.0*((int)(100*left)))/100);
            	 FontMetrics fm = this.getFontMetrics(this.getFont());
            	 int h = fm.getHeight(), w = fm.stringWidth(str);
            	 
            	 // Ivo added these conditions to avoid the overlay of the two labels
            	 if (xGraph(left)>0.1*getWidth())
            		 g.drawString(str, xGraph(left)-w/2, yGraph(0)+h);
             
            	 str = Double.toString((1.0*((int)(100*right)))/100);
            	 if ((getWidth()-xGraph(right))>0.1*getWidth())
            		 g.drawString(str, xGraph(right)-w/2, yGraph(0) + h);
             } 
        }
    }

    //Methods for converting between graph units (in pixels) and scale units

    /**
     * This method computes the x coordinate in graph units for a given x in
     * scale units
     */
    public int xGraph(double x) {
        return leftMargin
                + (int) (((x - xMin) / (xMax - xMin)) * (getSize().width
                        - leftMargin - rightMargin));
    }

    /**
     * This method computes the y coordinate in graph units for a given y in
     * scale units
     */
    public int yGraph(double y) {
        return getSize().height
                - bottomMargin
                - (int) (((y - yMin) / (yMax - yMin)) * (getSize().height
                        - bottomMargin - topMargin));
    }

    /**
     * This method computes the x coordinate in scale units for a given x in
     * graph units
     */
    public double xScale(int x) {
        return xMin
                + ((double) (x - leftMargin) / (getSize().width - leftMargin - rightMargin))
                * (xMax - xMin);
    }

    /**
     * This method computes the y coordinate in scale units for a given y in
     * graph units
     */
    public double yScale(int y) {
        return yMin
                + ((double) (getSize().height - y - bottomMargin) / (getSize().height
                        - bottomMargin - topMargin)) * (yMax - yMin);
    }

    /** This method convert x scale units to x pixels */
    public int xPixels(double x) {
        return xGraph(xMin + x) - xGraph(xMin);
    }

    /** This method converts y scale units to y pixels */
    public int yPixels(double y) {
        return yGraph(yMin + y) - yGraph(yMin);
    }

    //Methods for drawling lines, labels and axes

    /**
     * This method draws a line between (x1, y1) and (x2, y2), where the
     * coordinates are in scale units
     */
    public void drawLine(Graphics g, double x1, double y1, double x2, double y2) {
        g.drawLine(xGraph(x1), yGraph(y1), xGraph(x2), yGraph(y2));
    }

    /**
     * This method draws a tick mark at the specified x and y coordinates (in
     * scale units), i pixels in the negative direction, j in the positive
     * direction. The variable s is style, either horizontal or vertical.
     */
    public void drawTick(Graphics g, double x, double y, int i, int j,
            int orientation) {
        int a = xGraph(x), b = yGraph(y);
        if (orientation == VERTICAL) g.drawLine(a, b - j, a, b + i);
        else g.drawLine(a - i, b, a + j, b);
    }

    /**
     * This method draws a tick mark at the specified x and y coordinates (in
     * scale units), 3 pixels in the positive direction and 3 in the negative
     * direction
     */
    public void drawTick(Graphics g, double x, double y, int orientation) {
        drawTick(g, x, y, 3, 3, orientation);
    }

    /**
     * This method draws label s to the left, right, above, or below (x, y) (in
     * scale units)
     */
    public void drawLabel(Graphics g, String s, double x, double y, int location) {
        FontMetrics fm = this.getFontMetrics(this.getFont());
        int h = fm.getHeight(), w = fm.stringWidth(s);
        switch (location) {
        case LEFT:
            g.drawString(s, xGraph(x) - w - 3, yGraph(y) + h / 2);
            break;
        case RIGHT:
            g.drawString(s, xGraph(x) + 3, yGraph(y) + h / 2);
            break;
        case ABOVE:
            g.drawString(s, xGraph(x) - w / 2, yGraph(y) + 1);
            break;
        case BELOW:
            g.drawString(s, xGraph(x) - w / 2, yGraph(y) + h);
            break;
        }
    }

    /**
     * This method draws an axis corresponding to a partition of an interval at
     * position c relative to the other variable. The orientation indicates
     * style, either horizongal or vertical. The type means that either the
     * midpoints or the bounds are indicated with tick marks. The pattern is the
     * format pattern for the labels
     */
    public void drawAxis(Graphics g, Domain d, double c, int orientation, int type) {
        double t;
        if (orientation == HORIZONTAL) {
            //Draw thte line
            drawLine(g, d.getLowerBound(), c, d.getUpperBound(), c);
            //Draw tick marks, depending on type
            for (int i = 0; i < d.getSize(); i++) {
                if (type == MIDPOINTS) t = d.getValue(i);
                else t = d.getBound(i);
                drawTick(g, t, c, VERTICAL);
            }
            if (type == BOUNDS) drawTick(g, d.getUpperBound(), c, VERTICAL);
            //Draw labels
            if (type == MIDPOINTS) t = d.getLowerValue();
            else t = d.getLowerBound();
            drawLabel(g, format(t), t, c, BELOW);
            if (type == MIDPOINTS) t = d.getUpperValue();
            else t = d.getUpperBound();
            drawLabel(g, format(t), t, c, BELOW);
        } else {
            //Draw the line
            drawLine(g, c, d.getLowerBound(), c, d.getUpperBound());
            //Draw tick marks, depending on type
            for (int i = 0; i < d.getSize(); i++) {
                if (type == MIDPOINTS) t = d.getValue(i);
                else t = d.getBound(i);
                drawTick(g, c, t, HORIZONTAL);
            }
            if (type == BOUNDS) drawTick(g, c, d.getUpperBound(), HORIZONTAL);
            //Draw labels
            if (type == MIDPOINTS) t = d.getLowerValue();
            else t = d.getLowerBound();
            drawLabel(g, format(t), c, t, LEFT);
            if (type == MIDPOINTS) t = d.getUpperValue();
            else t = d.getUpperBound();
            drawLabel(g, format(t), c, t, LEFT);
        }
    }

    /**
     * This method draws an axis corresponding to the partition of [a, b] into
     * subintervals of width w, as in the previous method
     */
    public void drawAxis(Graphics g, double a, double b, double w, double c,
            int orientation, int type) {
        drawAxis(g, new Domain(a, b, w), c, orientation, type);
    }

    /**
     * This method draws an axis corresponding to the partition of [a, b] into
     * subintervals of width w, with tick marks at the partition bounds
     */
    public void drawAxis(Graphics g, double a, double b, double w, double c,
            int orientation) {
        drawAxis(g, a, b, w, c, orientation, BOUNDS);
    }

    //Methods for drawing boxes
    /**
     * This method draws a box between the specified corner points in scale
     * units
     */
    public void drawBox(Graphics g, double x0, double y0, double x1, double y1) {
        g.drawRect(xGraph(x0), yGraph(y1), xGraph(x1) - xGraph(x0), yGraph(y0)
                - yGraph(y1));
    }

    /**
     * This method fills a box between the specified corner points in scale
     * units
     */
    public void fillBox(Graphics g, double x0, double y0, double x1, double y1) {
        g.fillRect(xGraph(x0), yGraph(y1), xGraph(x1) - xGraph(x0), yGraph(y0)
                - yGraph(y1));
    }

    /**
     * The following method draws a symmetric, horizontal boxplot, centered at x
     * of radius r (in scale units). The variable y is the vertical position, in
     * pixels
     */
    public void drawBoxPlot(Graphics g, double x, double r, int y) {
        g.drawRect(xGraph(x - r), y - 3, xGraph(x + r) - xGraph(x - r), 6);
        g.drawLine(xGraph(x), y - 6, xGraph(x), y + 6);
    }

    /**
     * The following method fills a symmetric, horizontal boxplot, centered at x
     * of radius r (in scale units). The variable y is the vertical position in
     * pixels
     */
    public void fillBoxPlot(Graphics g, double x, double r, int y) {
        g.fillRect(xGraph(x - r), y - 3, xGraph(x + r) - xGraph(x - r), 6);
        g.drawLine(xGraph(x), y - 6, xGraph(x), y + 6);
    }

    /**
     * The following method draws a five-number, horizontal boxplot. The
     * variable y is the vertical position, in pixels
     */
    public void drawBoxPlot(Graphics g, double x1, double x2, double x3, double x4,
            double x5, int y) {
        g.drawLine(xGraph(x1), y, xGraph(x5), y);
        g.drawLine(xGraph(x1), y - 3, xGraph(x1), y + 3);
        g.drawLine(xGraph(x5), y - 3, xGraph(x5), y + 3);
        g.drawRect(xGraph(x2), y - 3, xGraph(x4) - xGraph(x2), 6);
        g.drawLine(xGraph(x3), y - 6, xGraph(x3), y + 6);
    }

    /**
     * The following method fills a five-number, horizontal boxplot. The
     * variable y is the vertical position, in pixels
     */
    public void fillBoxPlot(Graphics g, double x1, double x2, double x3, double x4,
            double x5, int y) {
        g.drawLine(xGraph(x1), y, xGraph(x5), y);
        g.drawLine(xGraph(x1), y - 6, xGraph(x1), y + 6);
        g.drawLine(xGraph(x5), y - 6, xGraph(x5), y + 6);
        g.fillRect(xGraph(x2), y - 3, xGraph(x4) - xGraph(x2), 6);
        g.drawLine(xGraph(x3), y - 8, xGraph(x3), y + 8);
    }

    /**
     * Draw a box at the specified x and y coordinates (in scale units) Length l
     * (in scale units) width: i pixels in the negative direction, j in the
     * positive direction
     */

    public void drawBox(Graphics g, int style, double x, double y, double l, int i,
            int j) {
        if (style == VERTICAL) g.drawRect(xGraph(x) - i, yGraph(y + l), i + j,
                yGraph(y) - yGraph(y + l));
        else if (style == HORIZONTAL)
                g.drawRect(xGraph(x), yGraph(y) - i, xGraph(x + l) - xGraph(x), i
                        + j);
    }

    public void fillBox(Graphics g, int style, double x, double y, double l, int i,
            int j) {
        if (style == VERTICAL) g.fillRect(xGraph(x) - i, yGraph(y + l), i + j,
                yGraph(y) - yGraph(y + l));
        else if (style == HORIZONTAL)
                g.fillRect(xGraph(x), yGraph(y) - i, xGraph(x + l) - xGraph(x), i
                        + j);
    }

    public void mouseMoved(MouseEvent evt) {
        xPosition = evt.getX();
        yPosition = evt.getY();
        repaint();
    }

    public void mouseDragged(MouseEvent evt) {
        int x = evt.getX();
        boolean leftOrRightLimit = true; // leftOrRightLimit=true <==> Left Limit moved! Else Right Limit moved

        if (x >= leftMargin && x < getWidth() - rightMargin) {
            double d1 = Math.abs(xScale(x) - left);
            double d2 = Math.abs(xScale(x) - right);

            if (d1 < d2) left = xScale(x);
            else if (d1 == d2) {  left = xScale(x); right = xScale(x);   }
            else {  right = xScale(x);  leftOrRightLimit=false;  }

            if (left > right) {
                double y = left;
                left = right;
                right = y;
            }

        	xPosition = evt.getX();
        	yPosition = evt.getY();
 	    ////System.out.println("GraphPanel mouseDragged xPosition = " + xPosition + ", yPosition = " + yPosition);
       	if (type == DISCRETE) {
        		rigidIntegralMotionOfDiscriteDistributions(leftOrRightLimit);
        		this.container.leftCutOff.setText(format(getLeftCutOff()+1.0));
                this.container.rightCutOff.setText(format(getRightCutOff()));
        	}
            else {
            	this.container.leftCutOff.setText(format(getLeftCutOff()));
            	this.container.rightCutOff.setText(format(getRightCutOff()));
            }
        	repaint();
        	this.container.observable.notifyObservers();
        }
    }

    public void mousePressed(MouseEvent evt) {
        int x = evt.getX();
        boolean leftOrRightLimit = true; // leftOrRightLimit=true <==> Left Limit moved! Else Right Limit moved

        if (x >= leftMargin && x < getWidth() - rightMargin) {
            double d1 = Math.abs(xScale(x) - left);
            double d2 = Math.abs(xScale(x) - right);
            if (d1 < d2) left = xScale(x);
            else if (d1 == d2) {  left = xScale(x); right = xScale(x);   }
            else {	right = xScale(x); leftOrRightLimit=false;  }

            if (left > right) {
                double y = left;
                left = right;
                right = y;
            }


        	if (type == DISCRETE) {
        		rigidIntegralMotionOfDiscriteDistributions(leftOrRightLimit);
        		this.container.leftCutOff.setText(format(getLeftCutOff()+1.5));
                this.container.rightCutOff.setText(format(getRightCutOff()));
        	}
            else {
            	this.container.leftCutOff.setText(format(getLeftCutOff()));
            	this.container.rightCutOff.setText(format(getRightCutOff()));
            }
            repaint();  
            this.container.observable.notifyObservers();
        }
    }

    public void mouseReleased(MouseEvent evt) {}

    public void mouseClicked(MouseEvent evt) {
	}

    public void mouseEntered(MouseEvent evt) {}

    public void mouseExited(MouseEvent evt) {}


     /**
     * This method ensures that LEFT and RIGHT limits for areas in DISCRETE distributions
     * are limited to only integer values. Without this restriction on the discrete
     * distributions, the reported areas under the density continuously change which does
     * not make sense in the discrete case. Bug reported by Juana Sanchez (10/19/05)
     */
    public void rigidIntegralMotionOfDiscriteDistributions(boolean _leftOrRightLimit)
    {
    	if (type == DISCRETE) {
    		//if (_leftOrRightLimit==true) left = (int)(left+0.5)-0.5;
    		//else right = (int)(right+0.5)+0.5;
    		if (_leftOrRightLimit==true) left = (int)(left+0.5)-0.5;
    		else right = (int)(right+0.5)+0.5;
    	}
    }


    public double getLeftCDF() {
    	double cdf;
    	if (type==DISCRETE) cdf = dist.getCDF(left-0.5);
    	else cdf = dist.getCDF(left);
    	if (cdf<0) cdf = 0;
        else if (cdf>1) cdf=1;
        return cdf;
    }

    public double getRightCDF() {
    	double cdf;
    	if (type==DISCRETE) cdf = 1 - dist.getCDF(right-0.5);
    	else cdf = 1 - dist.getCDF(right);
    	if (cdf<0) cdf = 0;
        else if (cdf>1) cdf=1;
        return cdf;
    }

    public double getBetweenCDF() {
        double cdf = 1 - (getLeftCDF() + getRightCDF());
        if (cdf<0) cdf = 0;
        else if (cdf>1) cdf=1;
        return cdf;
    }

    //Formating
    private String format(double x) {
        return decimalFormat.format(x);
    }

}