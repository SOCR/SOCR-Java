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
/* Module.java
 *
 * Module.java contains CurvedGaussian and Uniform classes for drawing
 *		    	Gaussian and Line model fits of the EM algorithm 
 *			MixtureEMExperiment.java
 *
 * @author Ivo Dinov
 */

package edu.ucla.stat.SOCR.chart.util;

import java.awt.*;
import java.io.*;

import org.jfree.data.xy.XYSeriesCollection;

public abstract class Module {
  protected double xsiz, ysiz;
  protected double weight;
  protected double[] probs;
  
  public Module(double xSiz, double ySiz, double w) {
    xsiz = xSiz;
    ysiz = ySiz;
    weight = w;
    randomKernel(w);
  }

  public void setweight(double w) {
	weight = w;
  }

  abstract void randomKernel(double w);
  

  abstract  XYSeriesCollection getDataset();
 // abstract double density(int x, int y);
  abstract double density(double x, double y);
  

  public double[] calcp(Database db) {
    int j, np;
    
    np = db.nPoints();
    probs = new double[np];
    for(j = 0; j < np; j++) probs[j] = density(db.xVal(j), db.yVal(j));
    return probs;
  }

  public void EMprob(double[] px, Database db) {
    int np;
    np = db.nPoints();
    weight = 0;
    for(int j = 0; j < np; j++) {
      probs[j] /= px[j];
      weight += probs[j];
    }
    weight /= np;
  }  

  abstract void EMpar(Database db, double prior);
}	//END::Module Class part of the MixtureEM.java Applet
