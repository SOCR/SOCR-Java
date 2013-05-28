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
/* Database.java
 *
 * Database.java contains the 2D data points, their colors and processing methods
 *	(e.g., random point generation) for the MixtureEMExperiment.java applet
 *
 * @author Ivo Dinov
 * @version 1.0 March 12 2004
 *
 */

package edu.ucla.stat.SOCR.util;

import java.awt.Color;
import java.awt.Graphics;

public class Database {
  final int maxnp = 10000;
  final int PtSize = 5;
  int[] x = new int[maxnp];
  int[] y = new int[maxnp];
  Color [] pointColors = new Color[maxnp];
  int xStart = 0;
  int yStart = 0;
  
  int np;
  int xsiz, ysiz;
  Color col;
  boolean lockflag = false;

  public Database(int xSize, int ySize, Color Col) {
    xsiz = xSize;
    ysiz = ySize;
    col = Col;
    np = 0;
  }
  
  public void paint(Graphics g) {
    	g.setColor(col);
    	for(int i = 0; i < np; i++) {
      	if (pointColors[i] != null) g.setColor(pointColors[i]);  
	    	g.fillRect(x[i]-PtSize/2,y[i]-PtSize/2,PtSize,PtSize);
    	} 
  }

  public void paint(Graphics g, Color [] _pointColors) {
     	for(int i = 0; i < np; i++) {
      	g.setColor(_pointColors[i]);
		g.fillRect(x[i]-PtSize/2,y[i]-PtSize/2,PtSize,PtSize);
    	} 
  }

  public void push(int newx, int newy) {
    if(np < maxnp) {
      x[np] = newx;
      y[np] = newy;
      np++;
    }
  }

  public void push(int newx, int newy, Color _newColor) {
    if(np < maxnp) {
      x[np] = newx;
      y[np] = newy;
	pointColors[np] = _newColor;
      np++;
    }
  }

  public void clearPoints() {
    np = 0;
  }

  public void setStarts(int _xStart, int _yStart) {
	  xStart = _xStart;
	  yStart = _yStart;
  }
  
  public int getXStart() {
	  return xStart;
  }

  public int getYStart() {
	  return yStart;
  }

  public void setXSize(int xSize) {
	    xsiz = xSize;
  }
  public int getXSize() {
	    return xsiz;
  }

  public void setYSize(int ySize) {
	    ysiz = ySize;
   }
  public int getYSize() {
	    return ysiz;
  }

  public void randomPoints(int n) {
    for(int i = 0; i < n; i++)
      push((int) (xsiz * Math.random())-xStart, (int) (ysiz * Math.random())-yStart, col);
  }

  public int nPoints() {
    return np;
  }

  public int xVal(int i) {
    return x[i];
  }

  public int yVal(int i) {
    return y[i];
  }

  public Color getPointColor(int i) {
    return pointColors[i];
  }
 
  public void setPointColor(int i, Color _newColor) {
    pointColors[i] = _newColor;
  }
}	//END::Database.java 2D point Object for MixtureEM.java applet
