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

import java.awt.Graphics2D;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

public class GaussLineMixture extends Mixture {
    final int kmax = 10;
    CurvedGaussian[] curvedGaussian = new CurvedGaussian[kmax];
    
    public GaussLineMixture(double xSize, double ySize, Database DB) {
        super(xSize, ySize, DB);

        initKernel(new Uniform(xsiz, ysiz, 0.0), typeuniform, 0);
        for (int i = 1; i < kmax; i++) {
        	curvedGaussian[i]= new CurvedGaussian(xsiz, ysiz, 0.0);
        	curvedGaussian[i].setplotline();
            initKernel(curvedGaussian[i], typecurvedgauss, i);
        }
        setnk(2);
    }
    
   /* public XYSeriesCollection[] getDatasets(){
    	XYSeriesCollection dss[] = new XYSeriesCollection[kmax];
 	   for (int i = 1; i < kmax; i++) {
 		   dss[i] = curvedGaussian[i].getDataset();
        }
 	   return dss;
    }*/
    
    public XYDataset[] getDatasets(){
 	   XYSeriesCollection[] dss = new XYSeriesCollection[getnk()];
 	   
 	   for (int i = 1; i < getnk(); i++) {
 		   XYSeriesCollection ds = curvedGaussian[i].getDataset();
 		   dss[i-1] = new XYSeriesCollection();
 		   for (int j=0; j<ds.getSeriesCount(); j++){			 
 			   dss[i-1].addSeries(ds.getSeries(j));
 		   }
        }
 	   return dss;
    }
    
    public void setRange( double xsize, double ysize, double xstart, double ystart){
    	//System.out.println("GL setRange get called xSize="+xsize+" ySize="+ysize+" center="+xstart+", "+ystart);
 	   for (int i = 1; i < kmax; i++) {
 		   curvedGaussian[i].setRange(xsize, ysize, xstart, ystart); 		 
 	   }
    }
    
    public void setRange( int kernel_index, double xsize, double ysize, double xstart, double ystart){
    	//System.out.println("GL setRange get called for kernel "+kernel_index+" xSize="+xsize+" ySize="+ysize+" center="+xstart+", "+ystart);
 	 
 		   curvedGaussian[kernel_index].setFixedRange(xsize, ysize, xstart, ystart); 		 

    }
    
   /* 
    public XYDataset getDatasets(){
 	   XYSeriesCollection dss = new XYSeriesCollection();
 	   
 	   for (int i = 1; i < getnk(); i++) {
 		   XYSeriesCollection ds = curvedGaussian[i].getDataset();
 		   for (int j=0; j<ds.getSeriesCount(); j++)
 			   dss.addSeries(ds.getSeries(j));
        }
 	   return dss;
    }*/
    
    
  /*  public double[][] getPolygons(){
 	   double[][] dss = new double[kmax*4][64*4];
 	  double[][] ds = new double[4][64*4];
 	   for (int i = 1; i < kmax; i++) {
 		  ds = curvedGaussian[i].getPolygons_data();
 		   for (int j=0; j<4; j++)
 			   dss[i*4+j] = ds[j];
        }
 	   return dss;
    }*/
}
