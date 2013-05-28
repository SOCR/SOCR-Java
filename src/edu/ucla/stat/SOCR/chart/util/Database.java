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
 */

package edu.ucla.stat.SOCR.chart.util;

import java.awt.*;

import javax.swing.*;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Database {
  final int maxnp = 10000;
  double[] x = new double[maxnp];
  double[] y = new double[maxnp];
 // Color [] pointColors = new Color[maxnp];
  double xStart = 0;
  double yStart = 0;
  String dbName;
  
  int np;
  double xsiz, ysiz;
  boolean lockflag = false;

  public Database(){
	  xsiz = 700;
	  ysiz = 700;
	
	  np = 0;
	  dbName = "";
  }
  public Database(double xSize, double ySize) {
    xsiz = xSize;
    ysiz = ySize;
    np = 0;
    dbName = "";
  }
  
  public Database(double xSize, double ySize, double xs, double ys) {
	    xsiz = xSize;
	    ysiz = ySize;
	    xStart = xs; 
	    yStart = ys;
	    np = 0;
	    dbName = "";
	  }
  
  /*public void paint(Graphics g) {
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
  }*/

  public void push(double newx, double newy) {
    if(np < maxnp) {
      x[np] = newx;
      y[np] = newy;
      np++;
    }
  }

  /*public void push(int newx, int newy, Color _newColor) {
    if(np < maxnp) {
      x[np] = newx;
      y[np] = newy;
	pointColors[np] = _newColor;
      np++;
    }
  }*/

  public void clearPoints() {
	  xsiz = 700;
	  ysiz = 700;
	
	  np = 0;
  }

  public void setStarts(double _xStart, double _yStart) {
	  xStart = _xStart;
	  yStart = _yStart;
  }
  
  public double getXStart() {
	  return xStart;
  }

  public double getYStart() {
	  return yStart;
  }

  public void setXSize(double xSize) {
	//  System.out.println("setting xSize " +xSize);
	    xsiz = xSize;
  }
  public double getXSize() {
	    return xsiz;
  }

  public void setYSize(double ySize) {
	//  System.out.println("setting ySize " +ySize);
	    ysiz = ySize;
   }
  public double getYSize() {
	    return ysiz;
  }
  
  public double getMin(double[] raw, int count){
  	double min = raw[0];
  	int i =0;
  	
  	for (; i<count; i++)
  		if (raw[i]<min)
  			min = raw[i];
  	
  	return min;
  
  }
  
  public double  getMax(double[] raw, int count){
  	double max = raw[0];
  	int i =0;
  	
  	for (; i<count; i++)
  		if (raw[i]>max)
  			max = raw[i];
  	
  	return max;
  
  }
  
  public void setRange(){
//	 COMPUTE the Range (min & Max) of the X & Y data, by series, if data is given! 
	double xDataMin = getMin(x, np);
	double yDataMin = getMin(y, np);
	double xDataMax = getMax(x, np);
	double yDataMax = getMax(y, np);
	
	//System.out.println("Database: xmin="+xDataMin+", xmax="+xDataMax);
	//System.out.println("Database: ymin="+yDataMin+", ymax="+yDataMax);
	if (np ==0){
		setXSize(700);
		setYSize(700);
		setStarts(350,350);
	}
	else{
	setXSize((1.2*(xDataMax-xDataMin)));	// setSize at 120%, on purpose!
	setYSize((1.2*(yDataMax-yDataMin)));
	double  _xStart = 0.5*(xDataMax+xDataMin);
	double  _yStart = 0.5*(yDataMax+yDataMin);
	
//System.out.println("Database: setting xsize="+xsiz+", ysize="+ysiz);
//System.out.println("Database: setting xstart="+_xStart+", ystart="+_yStart);
	setStarts(_xStart, _yStart);
	}
	
}

  public void randomPoints(int n) {
	  if (np>0)
		  setRange();
    for(int i = 0; i < n; i++)
     // push((int) (xsiz * Math.random())-xStart, (int) (ysiz * Math.random())-yStart);
    	  push((xsiz * (Math.random()-0.5))+xStart, (ysiz *(Math.random()-0.5))+yStart);
  }

  public int nPoints() {
    return np;
  }

  public double xVal(int i) {
    return x[i];
  }

  public double yVal(int i) {
    return y[i];
  }

  public void setDbName(String name){
	  dbName = name;	  
  }
  
  public String getDbName(){
	  return dbName;	  
  }
  
  public XYSeriesCollection getDataset(){
	  XYSeriesCollection dataset = new XYSeriesCollection(); 
	  XYSeries series1 = new XYSeries(dbName, false);
	  if (np==0) 
		 return null;
	  
	  for (int i =0; i<np; i++)
		  series1.add(x[i], y[i]);
	  
	  dataset.addSeries(series1);
	  return  dataset;
  }
  
  /*public Color getPointColor(int i) {
    return pointColors[i];
  }
 
  public void setPointColor(int i, Color _newColor) {
    pointColors[i] = _newColor;
  }*/
}	//END::Database.java 2D point Object for MixtureEM.java applet

