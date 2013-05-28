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
/* Mixture.java
 *
 * Mixture.java contains the constructor and methods for "Mixture" distribution models
 *		Part of the EM applet MixtureEMExperiment.java
 *
 * @author Ivo Dinov
 * @version 1.0 March 12 2004
 */

package edu.ucla.stat.SOCR.chart.util;

import java.awt.*;
import javax.swing.*;

abstract public class Mixture {
  boolean manualKernels= false;
  final public static int maxkp = 20;
  final public int typegauss = 0;
  final public int typeuniform = 1;
  final public int typecurvedgauss = 2;
  final public int typescaleshift = 3;
  final public int typeline = 101;
  public Object[] kernel = new Object[maxkp];
  public int type[] = new int[maxkp];
  double[] weight = new double[maxkp];
  int nk;
  public double xsiz, ysiz;
  double[] px;
  public Database db;

  Paint [] kernelColor = new Color[maxkp];

  public Mixture(double xSize, double ySize, Database DB) {
    xsiz = xSize;
    ysiz = ySize;  
    db = DB;
  }

  public void initKernel(Object mod, int tp, int pos) {
    kernel[pos] =  mod;
    type[pos] = tp;
  }

  public void setManualKernels(boolean b ){
	  manualKernels= b;
  }
  
  public void setnk(int nK) {
    if(nk == nK) return;
    nk = nK;
    for (int i = 0; i < nK; i++)
    	kernelColor[i] = new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random())); 

    randomKernels();
   
  }

  public int getnk() {
    return nk;
  }

    public void setnk(int nK, double[] ws) {
	double sum;
	int i;
        double[] w;

	nk = nK;
        w = new double[nk];
	sum = 0;
	for(i = 0;  i < nk; i++) {
	    sum += ws[i];
	}
	for(i = 0; i < nk; i++) {
	    w[i] = ws[i] / sum;
	   // kernelColor[i] = new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random())); 
	    
	}
	randomKernels(w);
    }

  public void randomKernels(double[] ws) {
    double sum, tmp;
    int i;
    
    sum = 0; 
    for(i = 0; i < nk; i++) {
	sum += ws[i];
    }
    for(i = 0; i < nk; i++) {
      tmp = ws[i] / sum;
      switch(type[i]) {
        case typecurvedgauss :
        	if (!	manualKernels)
        		((CurvedGaussian) kernel[i]).randomKernel(tmp);
        	else 
        		((CurvedGaussian) kernel[i]).fixedKernel(tmp);
          break;
	case typeuniform :
			//if (!	manualKernels)
				((Uniform) kernel[i]).randomKernel(tmp);
			//else 
			//	((Uniform) kernel[i]).fixedKernel(tmp);
          break;
      }
    }
  }

  public Object getKernel(int kernel_index)
  {
	return kernel[kernel_index];
  }

  public void randomKernels() {
    double[] ws = new double[nk];
    for(int i = 0; i < nk; i++) 
    	ws[i] = 1.0 / nk;
    
    randomKernels(ws);
  }
  
/*
  public void paint(Graphics g) {
    int i;
    for(i = 0; i < nk; i++) {
     	switch(type[i]) {
	  case typecurvedgauss :
          ((CurvedGaussian) kernel[i]).paint(g, db, kernelColor[i]);
          break;
	  case typeuniform :
          ((Uniform) kernel[i]).paint(g, db);
          break;
      }
    } 
    g.setColor(Color.red);
    g.drawString("Mean LogLikelihood = " + likelihood(), 0, 30);
  }*/
  

  public Paint getKernelColor(int i)
  {
	return kernelColor[i];
  }

  public void  setKernelColor(int i, Paint c)
  {
	kernelColor[i] =c;
  }
  
  private void calcpx() {
    int i, j, np;
    double[] probs;

    np = db.nPoints();
    px = new double[np];
    for(j = 0; j < np; j++) {
      px[j] = 0;
    }
    for(i = 0; i < nk; i++) {    
      probs = ((Module)kernel[i]).calcp(db);
      for(j = 0; j < np; j++) {
        px[j] += probs[j];
      }
    }
  }

  public void EM(double[] ws) {
    EMmain(ws);
  }

  public void EMmain(double[] ws) {
    double sum, newlike;
    int i, j;
    
    if(db.nPoints() <= 2) return;
    sum = 0;
    for(i = 0; i < nk; i++) {
      sum += ws[i];
    }
   // System.out.println("Mixture: sum="+sum);
    calcpx();
    for(i = 0; i < nk; i++) {
      ((Module)kernel[i]).EMprob(px, db);
      switch(type[i]) {
	case typecurvedgauss :
          ((CurvedGaussian)kernel[i]).EMpar(db, ws[i] / sum);
	  break;
	case typeuniform :
          ((Uniform)kernel[i]).EMpar(db, ws[i] / sum);
	  break;
	}
    }
    return;
  }

  public double likelihood() {
    int i, j, np;
    double tmp;

    np = db.nPoints(); 
    if(np == 0) return 0;
    calcpx();
    tmp = 0;
    for(j = 0; j < np; j++) {
      tmp += Math.log(px[j]);
    }
    return tmp / np;
  }
}	// END::Mixture Class

