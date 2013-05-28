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
package edu.ucla.stat.SOCR.util;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/* This class models a spinner. */
public class Spinner extends JPanel implements Runnable{
  private int n;    /* number of divisions */
  private Color[] Colors = {Color.pink, Color.cyan, Color.green, Color.yellow, Color.orange, Color.magenta};
  private double angle, savedAngle;
  private int value, w, h, min, r;
  private int step;

  /* This special constuctor creates a new spinner with a specified number of divisions. */
  public Spinner(int n)
  {
    this.n = n;
    setDivisions(n);
    this.angle = angle;
  }

  /* This default constructor creates a new spinner with four divisions. */
  public Spinner()
  {
    this(4);

  }

  /** This method computes a random angle and the value of a spin based on that angle. */
  public void spin()
   {
      angle = (-360 * Math.random());
      value = 1-(int)(angle / step);
   }

   /**  This method paints the spinner based on the number of divisions and the pointer
   based on the random angle computed in the spin method. */
   public void paintComponent(Graphics g){
		super.paintComponent(g);

      w = getSize().width;
		  h = getSize().height;
      min = Math.min(w, h);
      r = min / 2;
      for(int i = 0; i < n; i++)
      {
		    g.setColor(Colors[i]);
        g.fillArc(0, 0, min, min, i * step, step);
      }
      g.setColor(Color.black);
      g.drawLine(r, r, r + (int)(r * Math.cos(angle * Math.PI / 180)), r + (int)(r * Math.sin(angle * Math.PI / 180)));
   }

   /** This method sets the number of divisions for a spinner and computes
   the step size (angle measure of each division) */
   public void setDivisions(int n)
   {
      this.n = n;
      repaint();
      step = 360 / n;
   }

   /** This method sets a specific angle measure and computes the value of the spin
   corresponding to that angle. */
   public void setAngle(double a)
   {
    angle = a;
    value = (int)Math.rint(angle / step);
   }

   /** This method returns the value of a spin. */
   public int getValue(){
    return value;
   }

   /** This method gets the minimum size of the spinner. */
	public Dimension getMinimumSize(){
		return new Dimension(40, 40);
	}

  /** This method gets the preferred size of the spinner. */
	public Dimension getPreferredSize(){
		return new Dimension(40, 40);
	}

   /* This method spins the spinner exactly once. */
   public void run(){
   savedAngle = angle;
   try{
    for(angle = 0; angle <= 720 + savedAngle; angle = angle + 15){
    repaint();
    if(angle <= 180)
      Thread.sleep(25);
    else if(angle <= 360)
      Thread.sleep(50);
    else if(angle <= 540)
      Thread.sleep(75);
    else
      Thread.sleep(100);
    }
   }
   catch(InterruptedException e) {}
  }
}
