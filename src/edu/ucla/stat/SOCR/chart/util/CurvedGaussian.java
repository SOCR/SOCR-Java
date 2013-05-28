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
package edu.ucla.stat.SOCR.chart.util;

import java.awt.*;
import java.io.*;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class CurvedGaussian extends Module {
  double kmx, kmy, ksx, ksy, ksxy;
  double xStart=0, yStart=0;
 // final int mins = 5;
 // final double mmins = 1 ;
  double minx, miny;
  boolean plotcircle = true;
  Polygon gaussian2ndPolygon = new Polygon();

    public  CurvedGaussian(double xSize, double ySize, double w) {
      super(xSize, ySize, w);
      xStart = xSize/2;
      yStart = ySize/2;
    }

  public void randomKernel(double w) {
	
    weight = w;
    
   // System.out.println("CurvedGaussian randomKernel weight="+weight);
   // System.out.println("CurvedGaussian randomKernel xsiz="+xsiz+" ysiz="+ysiz+" xStart="+xStart+" yStart="+yStart);
   // kmx = xsiz * Math.random()+xStart;
    //.kmy = ysiz * Math.random()+yStart;
    kmx = xsiz * (Math.random()-0.5)+xStart;
    kmy = ysiz * (Math.random()-0.5)+yStart;
  
    minx=xsiz*0.05;
    miny=ysiz*0.05;
    ksx=xsiz/4 + minx;
    ksy = ysiz / 4 + miny; 
    
    ksxy = 0;
   // System.out.println("CurvedGaussian randomKernel :kmx="+kmx);
  }
  
  public void fixedKernel(double w) {
		
	    weight = w;
	    
	 //   System.out.println("CurvedGaussian fixedKernel weight="+weight);
	 //   System.out.println("CurvedGaussian fixedKernel xsiz="+xsiz+" ysiz="+ysiz+" xStart="+xStart+" yStart="+yStart);
	   // kmx = xsiz * Math.random()+xStart;
	    //.kmy = ysiz * Math.random()+yStart;
	    kmx = xsiz *0.5+xStart;
	    kmy = ysiz *0.5+yStart;
	  
	    minx = xsiz*0.05;
	    miny = ysiz*0.05;
	    ksx = xsiz /6;
	    ksy = ysiz /6;
	    
	    ksxy = 0;
	   // System.out.println("CurvedGaussian randomKernel :kmx="+kmx);
	  }

  public void setRange(double xSize, double ySize, double xstart, double ystart){
	  xsiz = xSize;
	  ysiz = ySize;
	  xStart = xstart;
	  yStart = ystart;
	  
	  kmx = xsiz * (Math.random()-0.5)+xStart;
	  kmy = ysiz * (Math.random()-0.5)+yStart;	  
	  minx=xsiz*0.05;
	  miny=ysiz*0.05;
	  ksx=xsiz/4 + minx;
	  ksy = ysiz / 4 + miny; 
  }
  
  public void setFixedRange(double xSize, double ySize, double xstart, double ystart){
	//  System.out.println("CurvedGaussian fixed range");
	  xsiz = xSize;
	  ysiz = ySize;
	  xStart = xstart;
	  yStart = ystart;
	  
	  kmx = xsiz * 0.5+xStart;
	  kmy = ysiz * 0.5+yStart;	  
	  minx = xsiz*0.05;
	  miny = ysiz*0.05;
	  ksx = xsiz/6;
	  ksy = ysiz/6; 
	  ksxy = 0;
  }
  public void setXStart(double _xStart) {
	    xStart= _xStart;
  }
  public void setYStart(double _yStart) {
	    yStart= _yStart;
  }
  
  public double getXStart() {
	    return xStart;
  }

  public double getYStart() {
	    return yStart;
  }

  public void setplotline() {
    plotcircle = false;
  }

  public double[] getPar() {
    double pars[];
    pars = new double[6];
    pars[0] = weight;
    pars[1] = kmx;
    pars[2] = kmy;
    pars[3] = ksx;
    pars[4] = ksy;
    pars[5] = ksxy;
    return pars;
  }
  
  public double getWeight(){
	  return weight;
  }
  
  public double getksx(){
	  return ksx;
  }
  
  public double getkmx(){
	  return kmx;
  }
  
  public double getkmy(){
	  return kmy;
  }
  
  public double getksxy(){
	  return ksxy;
  }
  public double getksy(){
	  return ksy;
  }
  
  public XYSeriesCollection getDataset(){
	  int j;
	    double theta, u1x, u1y, u2x, u2y, r1, r2, tmp, varx, vary;

	//  System.out.println("CurvedGaussian Kernel getDataset: xSize="+xsiz+" ySize="+ysiz);
	//  System.out.println("CurvedGaussian Kernel getDataset: xStart="+xStart+" yStart="+yStart);
	    if(Math.abs(ksxy) <= 1e-4) {
	    //	System.out.println("here1");
	      if(ksx > ksy) {
	        u1x = u2y = 1;
	        u1y = u2x = 0;
	        r1 = ksx;
	        r2 = ksy;
	      } else {
	        u1x = u2y = 0;
	        u1y = u2x = 1;
	        r1 = ksy;
	        r2 = ksx;
	      }
	    } else {
	    	//System.out.println("here2");
	      varx = ksx * ksx;
	      vary = ksy * ksy;
	      // eigen value
	      tmp = varx - vary;
	      tmp = Math.sqrt(tmp * tmp + 4 * ksxy * ksxy);
	      r1 = Math.sqrt((varx + vary + tmp) / 2);
	      r2 = Math.sqrt((varx + vary - tmp) / 2);

	      // eigen vectors
	      u1x = r1 * r1 - vary;
	      tmp = Math.sqrt(u1x * u1x + ksxy * ksxy);
	      u1x /= tmp;
	      u1y = ksxy / tmp;

	      u2x = r2 * r2 - vary;
	      tmp = Math.sqrt(u2x * u2x + ksxy * ksxy);
	      u2x /= tmp;
	      u2y = ksxy / tmp;
	    }    
	    
	   /* if (KernelColor!=null) g.setColor(KernelColor);
	    else g.setColor(Color.red);
	    
	    g.drawString("Weight="+weight, (int) kmx, (int) kmy);

	    if (KernelColor!=null) g.setColor(KernelColor);
	    else g.setColor(Color.blue);*/
	    
	    XYSeriesCollection dataset = new XYSeriesCollection(); 
	    XYSeries series1 = new XYSeries("Weight="+weight, false);
	   
	    if(plotcircle) {
	     for(j = 1; j <= 3; j++) {
	    	 series1 = new XYSeries("Weight="+weight, false);
	    	 series1 = drawCurvedOval(series1, u1x, u1y, u2x, u2y, r1 * j, r2 * j);
		  if (j==2)  // find/save polygon
	        {	//System.out.println("save2ndGaussianPolygon call!!!");
			 save2ndGaussianPolygon(u1x, u1y, u2x, u2y, r1 * j, r2 * j);
		   }
		  dataset.addSeries(series1); 
	      }	
	    } else {
	    	series1.add((kmx + 3 * r1 * u1x), (kmy + 3 * r1 * u1y));
	    	series1.add((kmx - 3 * r1 * u1x), (kmy - 3 * r1 * u1y));
	    	dataset.addSeries(series1);
	    }
	  
	    return dataset;
  }
  
  public XYSeries drawCurvedOval(XYSeries series, 
      double x1, double y1, double x2, double y2, double r1, double r2) {
    double fx, fy, tx, ty;
    
    double w1, w2;
 //  System.out.println("INSIDE::drawCurveOval"+" CurvedGaussian:kmx="+kmx+" kmy ="+kmy);
 //  Exception e = new Exception();
 //  e.printStackTrace();

 //   System.out.println("CurvedGaussian r1="+r1+" CurvedGaussian r2="+r2);
 
   /* System.out.println("CurvedGaussian x1="+x1);
    System.out.println("CurvedGaussian y1="+y1);
    System.out.println("CurvedGaussian x2="+x2);
    System.out.println("CurvedGaussian y2="+y2);*/
    
    fx = (kmx + r1 * x1);
    fy = (kmy + r1 * y1);
    for(double th = 0.1; th < 6.4; th += 0.1) { 
      w1 = Math.cos(th);
      w2 = Math.sin(th);
      tx = (kmx + r1 * x1 * w1 + r2 * x2 * w2);
      ty = (kmy + r1 * y1 * w1 + r2 * y2 * w2);
      //g.drawLine(fx, fy, tx, ty);
     series.add(fx,fy);
   //  System.out.println("CurvedGaussian: adding: "+fx+","+fy);
    // series.add(tx,ty);
      fx = tx;
      fy = ty;
    }
    
  
    return series;
  }
  
 
  
 public void save2ndGaussianPolygon(double x1, double y1, double x2, double y2, double r1, double r2) {
    double  fx, fy, tx, ty;
    double w1, w2;
    gaussian2ndPolygon = new Polygon();

	//System.out.println("INSIDE::save2ndGaussianPolygon Poly= "+gaussian2ndPolygon);

    fx = (kmx + r1 * x1);
    fy = (kmy + r1 * y1);
    gaussian2ndPolygon.addPoint((int)fx, (int)fy);
	//System.out.println("INSIDE::save2ndGaussianPolygon Poly.nPoints= "+gaussian2ndPolygon.npoints);

    for(double th = 0.1; th < 6.4; th += 0.1) {
      w1 = Math.cos(th);
      w2 = Math.sin(th);
      tx = (int) (kmx + r1 * x1 * w1 + r2 * x2 * w2);
      ty = (int) (kmy + r1 * y1 * w1 + r2 * y2 * w2);
      //gaussian2ndPolygon.addPolygonOld2NewPOint(fx, fy, tx, ty);
      gaussian2ndPolygon.addPoint((int)tx, (int)ty);
      fx = tx;
      fy = ty;
    }
	//System.out.println("INSIDE::save2ndGaussianPolygon() Poly.N_Points= "+gaussian2ndPolygon.npoints);
	setPolygon(gaussian2ndPolygon);
	return;
  }

  public void setPolygon(Polygon _gaussian2ndPolygon)
  {	gaussian2ndPolygon = _gaussian2ndPolygon;
	//System.out.println("INSIDE::setPolygon() Poly.N_Points= "+gaussian2ndPolygon.npoints);
   }

  public Polygon getPolygon()
  {	//System.out.println("INSIDE::getPolygon() Poly.N_Points = "+gaussian2ndPolygon.npoints);
	return gaussian2ndPolygon;
   }


  public void EMpar(Database db, double prior) {
    int j, np;
    double x, y, tmp, tmpx, tmpy, tmpsx, tmpsy, tmpsxy;

    np = db.nPoints();
  //  System.out.println("CurvedGaussian EMpar: np="+np);
  //  System.out.println("CurvedGaussian EMpar: weight="+weight);
    tmpsx = tmpsy = tmpsxy = kmx = kmy = 0;
    for(j = 0; j < np; j++) {
      x = db.xVal(j);
      y = db.yVal(j);
      kmx += probs[j] * x;
      kmy += probs[j] * y;
      tmpsx += probs[j] * x * x;
      tmpsy += probs[j] * y * y;
      tmpsxy += probs[j] * x * y;
    }
    tmp = np * weight;
    kmx /= tmp;
    kmy /= tmp;
    ksx = Math.sqrt(tmpsx / tmp - kmx * kmx);
    ksy = Math.sqrt(tmpsy / tmp - kmy * kmy);
    ksxy = tmpsxy / tmp - kmx * kmy;
    if(ksx < minx) 
    	ksx=minx;
    if(ksy < miny) 
    	ksy=miny;
    weight = 0.9 * weight + 0.1 * prior;
 //   System.out.println("CurvedGaussian EMpar: kmx="+kmx);
  }

   double density(double x, double y) {
	    double tmpx, tmpy, tmpxy, det, varx, vary;
	    varx = ksx * ksx;
	    vary = ksy * ksy;
	    det = varx * vary - ksxy * ksxy;
	    tmpx = (x - kmx) * (x - kmx);
	    tmpy = (y - kmy) * (y - kmy);
	    tmpxy = (x - kmx) * (y - kmy);
	    return weight / Math.sqrt(det) / 6.28319 *
	      Math.exp(-(tmpx * vary + tmpy * varx - 2 * tmpxy * ksxy) / det / 2);
   } 
}	//END::CurvedGaussian.java
